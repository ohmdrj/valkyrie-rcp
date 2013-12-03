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

package cz.req.ax.remote;

import cz.req.ax.AxApplicationContext;
import cz.req.ax.AxApplicationPageDescriptor;
import cz.req.ax.support.ExternalPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.valkyriercp.application.config.ApplicationConfig;

/**
 * LoginAuthentication
 *
 * @author Ondrej Burianek
 */
public class LoginAuthentication {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    ApplicationConfig applicationConfig;

    public LoginAuthentication(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public void login(ConnectionItem connectionItem) throws Throwable {
        ConnectionInstance connectionInstance = new ConnectionInstance(connectionItem);
        connectionInstance.authenticate(connectionItem.getCredentials());
        start(connectionInstance);
    }

    private void start(ConnectionInstance connectionInstance) {
        ApplicationContext connectionContext;
        ConnectionClass connectionClass = connectionInstance.getConnectionItem().getConnectionClass();
        if (connectionClass.getConfigPath() == null) {
            log.info("Loading applicationContext: " + connectionClass.getConfigClass().getSimpleName());
            connectionContext = connectionContextClass(connectionInstance);
        } else {
            log.info("Loading applicationContext: " + connectionClass.getConfigPath());
            connectionContext = connectionContextPath(connectionInstance);
        }
        AxApplicationPageDescriptor bean = AxApplicationContext.getPagesIterator(connectionContext).next();

        log.info("Starting PageDescriptor: " + bean);
        if (bean instanceof AxApplicationPageDescriptor) {
            AxApplicationPageDescriptor pageDescriptor = (AxApplicationPageDescriptor) bean;
            pageDescriptor.setDisplayTitle(connectionInstance.getConnectionItem().getName());
            applicationConfig.windowManager().getActiveWindow().showPage(pageDescriptor);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void showDialog(Throwable throwable) {
        //TODO
//        if (throwable != null) {
//            DefaultValidationMessage message = new DefaultValidationMessage("password", Severity.ERROR, throwable.getMessage());
//            PrivateClassUtils.writeField(message, DefaultMessage.class, "applicationConfig", applicationConfig);
//            dialog.setMessage(message);
//        }
//        form.getControl().requestFocusInWindow();
    }

    private ApplicationContext connectionContextClass(ConnectionInstance connectionInstance) {
        AnnotationConfigApplicationContext connectionContext = new AnnotationConfigApplicationContext();
        //connectionContext.setScopeMetadataResolver(new AnnotationScopeMetadataResolver(ScopedProxyMode.TARGET_CLASS));
        connectionContext.setParent(applicationConfig.applicationContext());
        connectionContext.register(connectionInstance.getConnectionItem().getConnectionClass().getConfigClass());
        connectionContext.refresh();
        return connectionContext;
    }

    private ApplicationContext connectionContextPath(ConnectionInstance connectionInstance) {
        String configPath = connectionInstance.getConnectionItem().getConnectionClass().getConfigPath();
        AxApplicationContext connectionContext = new AxApplicationContext(configPath,
                applicationConfig.applicationContext(), new ExternalPostProcessor(connectionInstance));
        return connectionContext;
    }
}
