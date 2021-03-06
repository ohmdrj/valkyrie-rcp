package org.valkyriercp.application.config;

import org.springframework.binding.convert.ConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.valkyriercp.application.*;
import org.valkyriercp.application.exceptionhandling.RegisterableExceptionHandler;
import org.valkyriercp.application.session.ApplicationSession;
import org.valkyriercp.application.session.ApplicationSessionInitializer;
import org.valkyriercp.application.support.MessageResolver;
import org.valkyriercp.binding.form.BindingErrorMessageProvider;
import org.valkyriercp.binding.form.FieldFaceSource;
import org.valkyriercp.binding.value.ValueChangeDetector;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.CommandManager;
import org.valkyriercp.command.CommandServices;
import org.valkyriercp.component.OverlayService;
import org.valkyriercp.factory.ButtonFactory;
import org.valkyriercp.factory.ComponentFactory;
import org.valkyriercp.factory.MenuFactory;
import org.valkyriercp.factory.TableFactory;
import org.valkyriercp.form.FormModelFactory;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.BindingFactoryProvider;
import org.valkyriercp.form.builder.FormComponentInterceptorFactory;
import org.valkyriercp.image.IconSource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.rules.RulesSource;
import org.valkyriercp.rules.reporting.MessageTranslatorFactory;
import org.valkyriercp.security.ApplicationSecurityManager;
import org.valkyriercp.security.SecurityControllerManager;
import org.valkyriercp.util.DialogFactory;

import java.awt.*;

@Configuration
public interface ApplicationConfig {
    ApplicationMode getApplicationMode();
    ApplicationContext applicationContext();
    Application application();
    ApplicationPageFactory applicationPageFactory();
    ApplicationWindowFactory applicationWindowFactory();
    ApplicationLifecycleAdvisor applicationLifecycleAdvisor();
    ApplicationDescriptor applicationDescriptor();
    ImageSource imageSource();
    WindowManager windowManager();
    CommandServices commandServices();
    CommandConfigurer commandConfigurer();
    PageComponentPaneFactory pageComponentPaneFactory();
    ViewDescriptorRegistry viewDescriptorRegistry();
    IconSource iconSource();
    ComponentFactory componentFactory();
    ButtonFactory buttonFactory();
    MenuFactory menuFactory();
    ButtonFactory toolbarButtonFactory();
    MessageSourceAccessor messageSourceAccessor();
    MessageSource messageSource();
    ApplicationObjectConfigurer applicationObjectConfigurer();
    SecurityControllerManager securityControllerManager();
    RegisterableExceptionHandler registerableExceptionHandler();
    ApplicationSession applicationSession();
    ApplicationSessionInitializer applicationSessionInitializer();
    MessageResolver messageResolver();
    CommandManager commandManager();
    ValueChangeDetector valueChangeDetector();
    MessageTranslatorFactory messageTranslatorFactory();
    RulesSource rulesSource();
    FieldFaceSource fieldFaceSource();
    ConversionService conversionService();
    FormComponentInterceptorFactory formComponentInterceptorFactory();
    BinderSelectionStrategy binderSelectionStrategy();
    BindingFactoryProvider bindingFactoryProvider();
    DialogFactory dialogFactory();
    BindingErrorMessageProvider bindingErrorMessageProvider();
    OverlayService overlayService();
    TableFactory tableFactory();
    ApplicationSecurityManager applicationSecurityManager();
    FormModelFactory formModelFactory();

    @Bean
    Color titlePaneBackgroundColor();

    @Bean
    Color titlePanePinstripeColor();
}
