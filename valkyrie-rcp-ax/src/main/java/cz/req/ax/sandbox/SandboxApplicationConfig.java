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

/**
 * @author Ondrej Burianek
 */
@Configuration
public class SandboxApplicationConfig extends AxApplicationConfig {

    public static void main(String[] args) {
        new AxApplicationLauncher(SandboxApplicationConfig.class);
    }

    @Override
    public ApplicationLifecycleAdvisor applicationLifecycleAdvisor() {
        ApplicationLifecycleAdvisor applicationLifecycleAdvisor = super.applicationLifecycleAdvisor();
        applicationLifecycleAdvisor.setStartingPageDescriptor(naviPage());
        return applicationLifecycleAdvisor;
    }

    @Bean
    public PageDescriptor naviPage() {
        AxApplicationNaviPageDescriptor pageDescriptor = new AxApplicationNaviPageDescriptor();
        pageDescriptor.addGroup("Forms").addView(testEditor(),testAutocomplete());
        pageDescriptor.addGroup("Autowire").addView(testView1(), testView2(), testWidget());
        //pageDescriptor.addGroup("Login").addView(loginView());
        pageDescriptor.setViewDescriptor(testEditor());
        return pageDescriptor;
    }

    @Bean
    public ViewDescriptor testEditor() {
        return new AxViewDescriptor("testEditor", new SandboxEditor());
    }

    @Bean
    public ViewDescriptor testAutocomplete() {
        return new AxViewDescriptor("testAutocomplete", new SandboxAutocomleteCombobox());
    }

    @Bean
    public ViewDescriptor testView1() {
        return new AxViewDescriptor(AutoTestView.class);
    }

    @Bean
    public ViewDescriptor testView2() {
        return new AutoTestView("testView2").getDescriptor();
    }

    @Bean
    public ViewDescriptor testWidget() {
        return new AxViewDescriptor(autoTestWidget());
    }

    @Bean
    public AutoTestWidget autoTestWidget() {
        return new AutoTestWidget();
    }

    /*public ViewDescriptor testItemView() {
        return new AxViewDescriptor("testItemView", getFormWidget());
    }*/

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
        binderSelectionStrategy.registerBinderForPropertyType(TestEnum2.class, enumerationBinder());
        binderSelectionStrategy.registerBinderForPropertyType(TestEnum1.class, enumerationBinder());
    }

    @Bean
    public EnumerationBinder enumerationBinder() {
        return new EnumerationBinder();
    }

}
