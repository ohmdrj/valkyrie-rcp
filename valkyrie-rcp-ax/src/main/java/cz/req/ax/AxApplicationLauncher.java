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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.valkyriercp.application.Application;
import org.valkyriercp.application.splash.SplashScreen;
import org.valkyriercp.application.splash.SplashScreenConfig;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * AxApplicationLauncher
 *
 * @author Ondrej Burianek
 */
public class AxApplicationLauncher {

    private final Log logger = LogFactory.getLog(getClass());
    private ApplicationContext startupContext;
    private ApplicationContext applicationContext;
    private SplashScreen splashScreen;

    //TODO args!!!
    public AxApplicationLauncher(Class<? extends AxApplicationConfig> applicationConfig) {
        loadApplicationContext(applicationConfig);
        startApplication();
    }

    public AxApplicationLauncher(Class<? extends SplashScreenConfig> startupConfig,
                                 Class<? extends AxApplicationConfig> applicationConfig) {
        loadStartupContext(startupConfig);
        try {
            loadApplicationContext(applicationConfig);
            startApplication();
        } finally {
            destroySplashScreen();
        }

    }

    public AxApplicationLauncher(String applicationConfig) {
        loadApplicationContextOld(applicationConfig);
        startApplication();
    }

    public AxApplicationLauncher(String startupConfig, String applicationConfig) {
        loadStartupContextOld(startupConfig);
        try {
            loadApplicationContextOld(applicationConfig);
            startApplication();
        } finally {
            destroySplashScreen();
        }
    }

    private void loadStartupContextOld(String startupConfig) {
        startupContext = new ClassPathXmlApplicationContext(new String[]{startupConfig}, true);
    }

    private void loadStartupContext(Class<? extends SplashScreenConfig> startupConfig) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(startupConfig);
        ctx.refresh();
        displaySplashScreen(ctx);
        startupContext = ctx;
    }

    private void loadApplicationContextOld(String applicationConfig) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(new String[]{applicationConfig}, true);
        applicationContext = ctx;
    }

    private void loadApplicationContext(Class<? extends AxApplicationConfig> applicationConfig) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(applicationConfig);
//        ctx.setScopeMetadataResolver(new AnnotationScopeMetadataResolver(ScopedProxyMode.TARGET_CLASS));
//        ctx.register(applicationConfig);

//        if (splashScreen instanceof MonitoringSplashScreen) {
//            ProgressMonitor monitor = ((MonitoringSplashScreen) splashScreen).getProgressMonitor();
//            applicationContext.addBeanFactoryPostProcessor(new ProgressMonitoringBeanFactoryPostProcessor(monitor));
//        }
//        ctx.refresh();
        applicationContext = ctx;
    }

    private void startApplication() {
//        if (startupContext == null) {
//            displaySplashScreen(rootApplicationContext);
//        }
        final Application application;
        try {
            application = applicationContext.getBean(Application.class);
        } catch (NoSuchBeanDefinitionException e) {
            throw new IllegalArgumentException("A single bean definition of type " + Application.class.getName()
                    + " must be defined in the main application context", e);
        }
        try {
            // To avoid deadlocks when events fire during initialization of some swing components
            // Possible to do: in theory not a single Swing component should be created (=modified) in the launcher thread...
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    application.start();
                }
            });
        } catch (InterruptedException e) {
            logger.warn("Application start interrupted", e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            throw new IllegalStateException("Application start thrown an exception: " + cause.getMessage(), cause);
        }
//        logger.debug("Launcher thread exiting...");

    }

    private void displaySplashScreen(BeanFactory beanFactory) {
        splashScreen = beanFactory.getBean(SplashScreen.class);
        logger.debug("Displaying application splash screen...");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    splashScreen.splash();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("EDT threading issue while showing splash screen", e);
        }
    }

    private void destroySplashScreen() {
        if (splashScreen != null) {
            logger.debug("Closing splash screen...");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    splashScreen.dispose();
                    splashScreen = null;
                }
            });
        }
    }
}
