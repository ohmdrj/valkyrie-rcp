/*
 * Copyright 2009-2013 Ondrej Burianek.
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

package cz.req.ax.sandbox;

import cz.req.ax.AxApplicationConfig;
import cz.req.ax.AxApplicationLauncher;
import cz.req.ax.AxApplicationNaviPageDescriptor;
import cz.req.ax.data.EnumerationBinder;
import cz.req.ax.remote.ConnectionClass;
import cz.req.ax.remote.ConnectionRegistry;
import cz.req.ax.view.AxViewDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.config.ApplicationLifecycleAdvisor;
import org.valkyriercp.form.binding.BinderSelectionStrategy;
import org.valkyriercp.form.binding.swing.NumberBinder;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * @author Ondrej Burianek
 */
@Configuration
public class SandboxApplicationConfig extends AxApplicationConfig {

    public static void main(String[] args) {
        Locale.setDefault(new Locale("cs", "CZ"));
//        Locale.setDefault(new Locale("en", "US"));
        new AxApplicationLauncher(SandboxApplicationConfig.class);
    }

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor applicationLifecycleAdvisor = super.applicationLifecycleAdvisor();
        applicationLifecycleAdvisor.setStartingPageDescriptor(naviPage());
        options().setPath("/cz/req/ax/sandbox/options.xml");
        return applicationLifecycleAdvisor;
    }

    @Bean
    public PageDescriptor naviPage() {
        AxApplicationNaviPageDescriptor pageDescriptor = new AxApplicationNaviPageDescriptor();
        pageDescriptor.addGroup("Editors").addView(testEditor());
        pageDescriptor.addGroup("Forms").addView(testForm(), testAutocomplete());
        pageDescriptor.addGroup("Views").addView(testView(), testView1(), testView2(), testView3());
        pageDescriptor.setViewDescriptor(testForm());
        return pageDescriptor;
    }

    @Bean
    public ViewDescriptor testEditor() {
        return new AxViewDescriptor("testEditor", new TestEditor());
    }

    @Bean
    public ViewDescriptor testForm() {
        return new AxViewDescriptor("testForm", new TestForm());
    }

    @Bean
    public ViewDescriptor testAutocomplete() {
        return new AxViewDescriptor("testAutocomplete", new TestAutocomlete());
    }

    @Bean
    public ViewDescriptor testView() {
        return new AxViewDescriptor("testView");
    }

    @Bean
    public ViewDescriptor testView1() {
        return new TestView("testView1").getDescriptor();
    }

    @Bean
    public ViewDescriptor testView2() {
        return new AxViewDescriptor("testView2", TestView.class);
    }

    @Bean
    public ViewDescriptor testView3() {
        return new AxViewDescriptor(testWidget());
    }

    @Bean
    public TestWidget testWidget() {
        return new TestWidget();
    }

    @Bean
    @Override
    public ConnectionRegistry connectionRegistry() {
        return new ConnectionRegistry(connectionClassStonebox());
    }

    @Bean
    public ConnectionClass connectionClassStonebox() {
        return new ConnectionClass("Stonebox", StoneboxApplicationConfig.class, SandboxLoginService.class);
    }

    @Override
    protected void registerBinders(BinderSelectionStrategy binderSelectionStrategy) {
        super.registerBinders(binderSelectionStrategy);
        NumberBinder numberBinder = new NumberBinder(BigDecimal.class);
        numberBinder.setFormat("###,##0.00");
        numberBinder.setUnformat("#0.####");
        binderSelectionStrategy.registerBinderForPropertyType(BigDecimal.class, numberBinder);
        binderSelectionStrategy.registerBinderForPropertyType(Integer.class, new NumberBinder(Integer.class));
        binderSelectionStrategy.registerBinderForPropertyType(TestEnum2.class, enumerationBinder());
        binderSelectionStrategy.registerBinderForPropertyType(TestEnum1.class, enumerationBinder());
    }

    @Bean
    public EnumerationBinder enumerationBinder() {
        return new EnumerationBinder();
    }

}
