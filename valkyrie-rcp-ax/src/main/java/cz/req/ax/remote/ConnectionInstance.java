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

import cz.thickset.utils.MD5;
import cz.thickset.utils.security.AxForbiddenException;
import cz.thickset.utils.security.AxServiceLoader;
import cz.thickset.utils.security.AxUnauthorizedException;
import cz.thickset.utils.security.AxUnavilableException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * ConnectionInstance
 *
 * @author Ondrej Burianek
 */
public class ConnectionInstance {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    AxServiceLoader serviceLoader;
    ConnectionItem connectionItem;
    ConnectionCredentials connectionCredentials;
    Object loginObject;

    public ConnectionInstance() {
        serviceLoader = new AxServiceLoader();
    }

    public ConnectionInstance(ConnectionItem connectionItem) {
        this();
        setConnectionItem(connectionItem);
    }

    public void setConnectionItem(ConnectionItem connectionItem) {
        this.connectionItem = connectionItem;
        serviceLoader.setUrl(connectionItem.getUrl());
    }

    public ConnectionItem getConnectionItem() {
        return connectionItem;
    }

    public void setConnectionCredentials(ConnectionCredentials connectionCredentials) {
        this.connectionCredentials = connectionCredentials;
    }

    public ConnectionCredentials getConnectionCredentials() {
        if (connectionCredentials == null) {
            return connectionItem.getCredentials();
        }
        return connectionCredentials;
    }

    public Object getLoginObject() {
        return loginObject;
    }

    public AxServiceLoader getServiceLoader() {
        return serviceLoader;
    }

    public Object getService(Class clazz) throws RuntimeException {
        return serviceLoader.getService(clazz);
    }

    public HttpClient createHttpClient() {
        HttpClient httpClient = new HttpClient();
        Assert.notNull(getConnectionCredentials());
        String username = connectionItem.getCredentials().getUsername();
        String password = connectionItem.getCredentials().getPassword();
        Credentials credentials = new UsernamePasswordCredentials(username, new MD5().digest(password));
        httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        return httpClient;
    }

    public Object authenticate() throws Throwable {
        Class loginClass = connectionItem.getConnectionClass().getLoginClass();
        if (loginClass.isInterface()) {
            return authenticateInvoker(getConnectionCredentials());
        } else {
            return getConnectionCredentials().newClone();
        }
    }

    public Object authenticate(ConnectionCredentials connectionCredentials) throws Throwable {
        Class loginClass = connectionItem.getConnectionClass().getLoginClass();
        if (loginClass.isInterface()) {
            return authenticateInvoker(connectionCredentials);
        } else {
            return connectionCredentials.newClone();
        }
    }

    public Object authenticateInvoker(ConnectionCredentials connectionCredentials) throws Throwable {
        Assert.notNull(connectionCredentials.getUsername(), "Username is null");
        Assert.notNull(connectionCredentials.getPassword(), "Password is null");
        try {
            String password = new MD5().digest(connectionCredentials.getPassword());
            serviceLoader.setCredentials(connectionCredentials.getUsername(), password);
            Object service = serviceLoader.getService(connectionItem.getConnectionClass().getLoginClass());
            Method method = service.getClass().getMethod("login");
            loginObject = method.invoke(service);
            this.connectionCredentials = connectionCredentials;
            return loginObject;
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof AxUnauthorizedException || cause instanceof AxForbiddenException) {
                throw new AuthenticationException("Chybné přihlašovací údaje");
            }
            if (cause instanceof AxUnavilableException) {
                throw new AuthenticationException("Server není dostupný");
            }
            this.connectionCredentials = null;
            throw cause;
        }
    }

    public class AuthenticationException extends Exception {

        public AuthenticationException(String message) {
            super(message);
        }

        public AuthenticationException(Exception ex) {
            super("Authentication error", ex);
        }
    }
}
