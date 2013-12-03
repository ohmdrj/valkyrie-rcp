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

package cz.req.ax;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.WindowManager;
import org.valkyriercp.command.CommandConfigurer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * AxApplicationContext
 *
 * @author Ondrej Burianek
 */
//TODO Rework to AnnotationConfigApplicationContext
public class AxApplicationContext extends ClassPathXmlApplicationContext {

    private ApplicationWindow window;
    private CommandConfigurer configurer;
    private Collection<? extends BeanPostProcessor> beanPostProcessors;

    public static Iterator<AxApplicationPageDescriptor> getPagesIterator(ApplicationContext context) {
        Map<String, AxApplicationPageDescriptor> beans = context.getBeansOfType(AxApplicationPageDescriptor.class);
        if (beans == null || beans.isEmpty()) {
            throw new IllegalArgumentException("No PageDescriptor found in applicationContext");
        }
        return beans.values().iterator();
    }

    public AxApplicationContext(String location, ApplicationContext parent, Collection<? extends BeanPostProcessor> beanPostProcessors) {
        super(new String[]{location}, false, parent);
        this.beanPostProcessors = beanPostProcessors;
        this.window = parent.getBean("windowManager", WindowManager.class).getActiveWindow();
        this.configurer = parent.getBean("commandConfigurer", CommandConfigurer.class);
        refresh();
    }

    public AxApplicationContext(String location, ApplicationContext parent, BeanPostProcessor... beanPostProcessors) {
        this(location, parent, Arrays.asList(beanPostProcessors));
    }

    //    public void setWindow(ApplicationWindow window) {
//        this.window = window;
//    }
    public void showPage(String pageId) {
        window.showPage(getPage(pageId));
    }

    public void showPage(AxApplicationPageDescriptor pageDescriptor) {
        window.showPage(pageDescriptor);
    }

    public AxApplicationPageDescriptor getPage(String beanId) {
        AxApplicationPageDescriptor pageDescriptor = (AxApplicationPageDescriptor) getBean(beanId, AxApplicationPageDescriptor.class);
        return pageDescriptor;
    }

    public void setPageTitle(String pageId, String pageTitle) {
        getPage(pageId).setDisplayTitle(pageTitle);
    }

    /**
     * Install our bean post-processors.
     *
     * @param beanFactory the bean factory used by the application context
     * @throws org.springframework.beans.BeansException
     *          in case of errors
     */
    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //MIG
//        beanFactory.addBeanPostProcessor(new ApplicationWindowSetter());
//        beanFactory.addBeanPostProcessor(new CommandConfigurerSetter());
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }
    //MIG
//    public class ApplicationWindowSetter implements BeanPostProcessor {
//
//        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//            return bean;
//        }
//
//        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//            if (bean instanceof ApplicationWindowAware) {
//                ((ApplicationWindowAware) bean).setApplicationWindow(window);
//            }
//            return bean;
//        }
//    }
//
//    public class CommandConfigurerSetter implements BeanPostProcessor {
//
//        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//            return bean;
//        }
//
//        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//            if (bean instanceof CommandGroupFactoryBean) {
//                ((CommandGroupFactoryBean) bean).setCommandConfigurer(configurer);
//            }
//            return bean;
//        }
//    }
}
