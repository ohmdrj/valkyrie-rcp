/*
 * Copyright 2009-2013 Ondřej Buránek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.req.ax;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import cz.req.ax.command.AxCommandConfig;
import cz.req.ax.command.AxCommandConfigurer;
import cz.req.ax.data.ArrayToListModel;
import cz.req.ax.options.AxApplicationOptions;
import cz.req.ax.options.AxApplicationSettings;
import cz.req.ax.remote.ConnectionRegistry;
import cz.req.ax.remote.LoginDataProvider;
import cz.req.ax.remote.LoginWidget;
import cz.req.ax.support.AxExceptionHandler;
import cz.req.ax.support.ThicksetApplicationDescriptor;
import cz.req.ax.view.AxViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.convert.ConversionService;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.valkyriercp.application.*;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.ApplicationMode;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;
import org.valkyriercp.application.config.support.DefaultBinderConfig;
import org.valkyriercp.application.config.support.UIManagerConfigurer;
import org.valkyriercp.application.exceptionhandling.DelegatingExceptionHandler;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.exceptionhandling.SimpleExceptionHandlerDelegate;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.BeanFactoryViewDescriptorRegistry;
import org.valkyriercp.application.support.DefaultApplication;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.application.support.SimplePageComponentPaneFactory;
import org.valkyriercp.binding.form.BindingErrorMessageProvider;
import org.valkyriercp.binding.form.FieldFaceSource;
import org.valkyriercp.binding.form.support.DefaultBindingErrorMessageProvider;
import org.valkyriercp.binding.form.support.MessageSourceFieldFaceSource;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.binding.value.support.DefaultValueChangeDetector;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.CommandRegistry;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.command.config.CommandConfig;
import org.valkyriercp.command.support.DefaultCommandManager;
import org.valkyriercp.command.support.DefaultCommandRegistry;
import org.valkyriercp.command.support.DefaultCommandServices;
import org.valkyriercp.component.DefaultOverlayService;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.convert.support.CollectionToListModelConverter;
import org.valkyriercp.convert.support.ListToListModelConverter;
import org.valkyriercp.factory.*;
import org.valkyriercp.form.FormModelFactory;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.binding.swing.ScrollPaneBinder;
import org.valkyriercp.form.binding.swing.SwingBinderSelectionStrategy;
import org.valkyriercp.form.binding.swing.SwingBindingFactoryProvider;
import org.valkyriercp.form.binding.swing.date.JXDatePickerDateFieldBinder;
import org.valkyriercp.form.builder.*;
import org.valkyriercp.image.DefaultIconSource;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.reporting.DefaultMessageTranslatorFactory;
import org.valkyriercp.rules.reporting.MessageTranslatorFactory;
import org.valkyriercp.security.ApplicationSecurityManager;
import org.valkyriercp.security.SecurityAwareConfigurer;
import org.valkyriercp.security.SecurityController;
import org.valkyriercp.security.SecurityControllerManager;
import org.valkyriercp.security.support.AuthorityConfigurableSecurityController;
import org.valkyriercp.security.support.DefaultApplicationSecurityManager;
import org.valkyriercp.security.support.DefaultSecurityControllerManager;
import org.valkyriercp.text.SelectAllFormComponentInterceptorFactory;
import org.valkyriercp.text.TextComponentPopupInterceptorFactory;
import org.valkyriercp.util.DialogFactory;
import org.valkyriercp.util.ValkyrieRepository;

import javax.annotation.PostConstruct;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Main ApplicationContext configuration.
 * Customized VakyrieRCP
 *
 * @author Ondrej Burianek
 * @see org.valkyriercp.application.config.support.AbstractApplicationConfig
 */
//TODO Freshen support
@Lazy
@Configuration
@Import(DefaultBinderConfig.class)
public abstract class AxApplicationConfig implements ApplicationConfig {

    Class<? extends CommandConfig> commandConfigClass;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DefaultBinderConfig defaultBinderConfig;

    public AxApplicationConfig() {
        this(AxCommandConfig.class);
    }

    public AxApplicationConfig(Class<? extends CommandConfig> commandConfigClass) {
        this.commandConfigClass = commandConfigClass;
    }

    @PostConstruct
    protected void postInit() {
        UIManager.put("JXDatePicker.arrowIcon", new ImageIcon(imageSource().getImage("selectDate.icon")));
        UIManager.put("LookupBox.arrowIcon", new ImageIcon(imageSource().getImage("selectLookup.icon")));
    }

    public ApplicationContext applicationContext() {
        return applicationContext;
    }

    @Bean(destroyMethod = "")
    public Application application() {
        return new DefaultApplication();
    }

    @Bean
    public ValkyrieRepository valkyrieRepository() {
        return new ValkyrieRepository();
    }


//    /**
//     * Command configuration, mostly menu bar. Override and extend.
//     */
//    @Override
//    public Class<? extends CommandConfig> getCommandConfigClass() {
//        return commandConfigClass;
//    }

//    @Bean
//    public ApplicationObjectConfigurerBeanPostProcessor beanPostProcessor() {
//        return new ApplicationObjectConfigurerBeanPostProcessor();
//    }

    @Bean
    @Override
    public ApplicationDescriptor applicationDescriptor() {
        return new ThicksetApplicationDescriptor();
    }

    @Bean
    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        DefaultApplicationLifecycleAdvisor lifecycleAdvisor = new DefaultApplicationLifecycleAdvisor();
        lifecycleAdvisor.setCommandConfigClass(commandConfigClass);
        lifecycleAdvisor.setStartingPageDescriptor(startingPage());
        return lifecycleAdvisor;
    }

    @Bean
    @Override
    public ApplicationSessionInitializer applicationSessionInitializer() {
        ApplicationSessionInitializer initializer = new ApplicationSessionInitializer();
        return initializer;
    }

    @Bean
    @Override
    public RegisterableExceptionHandler registerableExceptionHandler() {
        DelegatingExceptionHandler handler = new DelegatingExceptionHandler();
        //TODO Server Exceptions, User Exceptions, Purgeres
//        SimpleExceptionHandlerDelegate delegate1 = new SimpleExceptionHandlerDelegate();
//        delegate1.setThrowableClass(Throwable.class);
//        delegate1.setExceptionHandler(new AxExceptionHandler());
//        handler.addDelegateToList(delegate1);
        SimpleExceptionHandlerDelegate delegate2 = new SimpleExceptionHandlerDelegate();
        delegate2.setThrowableClass(Throwable.class);
//        delegate2.setExceptionHandler(new AxExceptionHandler(windowManager()));
        delegate2.setExceptionHandler(mainExceptionHandler());
        handler.addDelegateToList(delegate2);
        return handler;
    }

    @Bean
    public AxExceptionHandler mainExceptionHandler() {
        return new AxExceptionHandler();
    }

    @Bean
    public MessageSource messageSource() {
        AxMessageSource messageSource = new AxMessageSource();
        return messageSource;
    }

    @Bean
    public ImageSource imageSource() {
        AxImageSource imageSource = new AxImageSource();
        return imageSource;
    }

    @Bean
    public ApplicationObjectConfigurer applicationObjectConfigurer() {
        return new AxApplicationObjectConfigurer();
    }

    @Bean
    public ApplicationSession applicationSession() {
        return new ApplicationSession();
    }

    @Bean
    public WindowManager windowManager() {
        return new WindowManager();
    }

    @Bean
    public CommandServices commandServices() {
        return new DefaultCommandServices();
    }

    @Bean
    @Override
    public CommandConfigurer commandConfigurer() {
        return new AxCommandConfigurer();
    }

    @Bean
    public CommandRegistry commandRegistry() {
        return new DefaultCommandRegistry();
    }

    @Bean
    public CommandManager commandManager() {
        return new DefaultCommandManager();
    }

    @Bean
    public PageComponentPaneFactory pageComponentPaneFactory() {
        return new SimplePageComponentPaneFactory();
    }

    @Bean
    public IconSource iconSource() {
        return new DefaultIconSource();
    }

    @Bean
    public ComponentFactory componentFactory() {
        return new DefaultComponentFactory();
    }

    @Bean
    public ButtonFactory buttonFactory() {
        return new DefaultButtonFactory();
    }

    @Bean
    public MenuFactory menuFactory() {
        return new DefaultMenuFactory();
    }

    @Bean
    public ButtonFactory toolbarButtonFactory() {
        return new DefaultButtonFactory();
    }

    @Bean
    public TableFactory tableFactory() {
        return new DefaultTableFactory();
    }

    @Bean
    public DialogFactory dialogFactory() {
        return new DialogFactory();
    }

    @Bean
    public OverlayService overlayService() {
        return new DefaultOverlayService();
    }

    @Bean
    public FormModelFactory formModelFactory() {
        return new FormModelFactory();
    }

    @Bean
    public ValueChangeDetector valueChangeDetector() {
        return new DefaultValueChangeDetector();
    }

    @Bean
    public FieldFaceSource fieldFaceSource() {
        return new MessageSourceFieldFaceSource();
    }

    @Bean
    public MessageResolver messageResolver() {
        return new MessageResolver();
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    public MessageTranslatorFactory messageTranslatorFactory() {
        return new DefaultMessageTranslatorFactory();
    }

    @Bean
    @Override
    public ApplicationWindowFactory applicationWindowFactory() {
        return new AxApplicationWindowFactory();
    }

    @Bean
    @Override
    public ApplicationPageFactory applicationPageFactory() {
        return new AxApplicationPageFactory();
    }

    @Bean
    @Override
    public ViewDescriptorRegistry viewDescriptorRegistry() {
        BeanFactoryViewDescriptorRegistry viewDesc = new BeanFactoryViewDescriptorRegistry();
        viewDesc.setApplicationContext(applicationContext());
        return viewDesc;
    }

    @Bean
    public ConversionService conversionService() {
//        new DefaultFormattingConversionService;
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new ListToListModelConverter());
        conversionService.addConverter(new CollectionToListModelConverter());
        conversionService.addConverter(new ArrayToListModel());
        return conversionService;
    }

    @Bean
    public BinderSelectionStrategy binderSelectionStrategy() {
        SwingBinderSelectionStrategy swingBinderSelectionStrategy = new SwingBinderSelectionStrategy();
        registerBinders(swingBinderSelectionStrategy);
        return swingBinderSelectionStrategy;
    }

    protected void registerBinders(BinderSelectionStrategy binderSelectionStrategy) {
        binderSelectionStrategy.registerBinderForControlType(JTextComponent.class, defaultBinderConfig.textComponentBinder());
        binderSelectionStrategy.registerBinderForControlType(JFormattedTextField.class, defaultBinderConfig.formattedTextFieldBinder());
        binderSelectionStrategy.registerBinderForControlType(JTextArea.class, defaultBinderConfig.textAreaBinder());
        binderSelectionStrategy.registerBinderForControlType(JToggleButton.class, defaultBinderConfig.toggleButtonBinder());
        binderSelectionStrategy.registerBinderForControlType(JCheckBox.class, defaultBinderConfig.checkBoxBinder());
        binderSelectionStrategy.registerBinderForControlType(JComboBox.class, defaultBinderConfig.comboBoxBinder());
        binderSelectionStrategy.registerBinderForControlType(JList.class, defaultBinderConfig.listBinder());
        binderSelectionStrategy.registerBinderForControlType(JLabel.class, defaultBinderConfig.labelBinder());
        binderSelectionStrategy.registerBinderForControlType(JScrollPane.class, new ScrollPaneBinder(binderSelectionStrategy, JTextArea.class));
        binderSelectionStrategy.registerBinderForPropertyType(String.class, defaultBinderConfig.textComponentBinder());
        binderSelectionStrategy.registerBinderForPropertyType(boolean.class, defaultBinderConfig.checkBoxBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Boolean.class, defaultBinderConfig.checkBoxBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Enum.class, defaultBinderConfig.enumComboBoxBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Boolean.class, defaultBinderConfig.trueFalseNullBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Date.class, new JXDatePickerDateFieldBinder());
        binderSelectionStrategy.registerBinderForPropertyType(Timestamp.class, new JXDatePickerDateFieldBinder());
    }

    @Bean
    public BindingFactoryProvider bindingFactoryProvider() {
        return new SwingBindingFactoryProvider();
    }

    @Bean
    public BindingErrorMessageProvider bindingErrorMessageProvider() {
        return new DefaultBindingErrorMessageProvider();
    }

    @Bean
    public FormComponentInterceptorFactory formComponentInterceptorFactory() {
        ChainedInterceptorFactory factory = new ChainedInterceptorFactory();
        List<FormComponentInterceptorFactory> factories = Lists.newArrayList();
        factories.add(new ColorValidationInterceptorFactory());
        factories.add(new OverlayValidationInterceptorFactory());
        factories.add(new ShowCaptionInStatusBarInterceptorFactory());
        factories.add(new SelectAllFormComponentInterceptorFactory());
        factories.add(new TextComponentPopupInterceptorFactory());
//        factories.add(new PromptTextFieldFormComponentInterceptorFactory());
//        factories.add(new ComboBoxAutoCompletionInterceptorFactory());
//        factories.add(new ShowDescriptionInStatusBarInterceptorFactory());
        factory.setInterceptorFactories(factories);
        return factory;
    }

    @Bean
    public RulesSource rulesSource() {
        return new AxValidationRulesSource();
    }

    @Bean
    public ApplicationSecurityManager applicationSecurityManager() {
        return new DefaultApplicationSecurityManager(false);
    }

    @Bean
    public SecurityAwareConfigurer securityAwareConfigurer() {
        return new SecurityAwareConfigurer();
    }

    @Bean
    public SecurityControllerManager securityControllerManager() {
        DefaultSecurityControllerManager defaultSecurityControllerManager = new DefaultSecurityControllerManager();
        defaultSecurityControllerManager
                .setFallbackSecurityController(authorityConfigurableSecurityController());
        return defaultSecurityControllerManager;
    }

    @Bean
    public SecurityController authorityConfigurableSecurityController() {
        AuthorityConfigurableSecurityController authorityConfigurableSecurityController = new AuthorityConfigurableSecurityController();
        Map<String, String> idAuthorityMap = Maps.newHashMap();
        configureAuthorityMap(idAuthorityMap);
        authorityConfigurableSecurityController
                .setIdAuthorityMap(idAuthorityMap);
        AffirmativeBased accessDecisionManager = new AffirmativeBased();
        RoleVoter roleVoter = new RoleVoter();
        roleVoter.setRolePrefix("");
        accessDecisionManager.setDecisionVoters(Lists
                .<AccessDecisionVoter>newArrayList(roleVoter));
        authorityConfigurableSecurityController
                .setAccessDecisionManager(accessDecisionManager);
        return authorityConfigurableSecurityController;
    }

    protected void configureAuthorityMap(Map<String, String> idAuthorityMap) {
    }

    @Bean
    public Color titlePaneBackgroundColor() {
        return new Color(200, 200, 200);
    }

    @Bean
    public Color titlePanePinstripeColor() {
        return new Color(1f, 1f, 1f, 0.17f);
    }

    public ApplicationMode getApplicationMode() {
        return ApplicationMode.DEVELOPMENT;
    }

    @Bean
    public UIManagerConfigurer uiManagerConfigurer() {
        UIManagerConfigurer configurer = new UIManagerConfigurer();
        configurer.setLookAndFeel(PlasticXPLookAndFeel.class);
        return configurer;
    }

    @Bean
    public AxApplicationOptions options() {
        AxApplicationOptions options = new AxApplicationOptions();
        return options;
    }

    @Bean
    public AxApplicationSettings settings() {
        AxApplicationSettings settings = new AxApplicationSettings();
        //TODO set appname
        return settings;
    }

    @Bean
    public PageDescriptor startingPage() {
        return loginPage();
    }

    @Bean
    public PageDescriptor loginPage() {
        return new AxApplicationPageDescriptor(loginView());
    }

    @Bean
    public ViewDescriptor loginView() {
        return new AxViewDescriptor("loginView", loginWidget());
    }

    @Bean
    public LoginWidget loginWidget() {
        return new LoginWidget();
    }

    @Bean
    public LoginDataProvider loginDataProvider() {
        return new LoginDataProvider();
    }

    @Bean
    public ConnectionRegistry connectionRegistry() {
        return new ConnectionRegistry();
    }
}
