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

package cz.req.ax.remote;

/**
 * ConnectionItem
 *
 * @author Ondrej Burianek
 */
public class ConnectionItem implements Comparable<ConnectionItem> {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private String name, url;
    private boolean autoconnect;
    private ConnectionCredentials credentials;
    private ConnectionClass connectionClass;

    public ConnectionItem() {
        this.credentials = new ConnectionCredentials();
    }

    public ConnectionItem(String name, String url, ConnectionClass connectionClass) {
        this();
        this.name = name;
        this.url = url;
        this.connectionClass = connectionClass;
        this.autoconnect = false;
    }

    public ConnectionItem(String name, String url, String username, String password, ConnectionClass connectionClass) {
        this.name = name;
        this.url = url;
        this.credentials = new ConnectionCredentials(username, password, true);
        this.connectionClass = connectionClass;
        this.autoconnect = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getHostName() {
        if (url == null) {
            return null;
        }
        if (url.indexOf("//") == -1) {
            throw new IllegalArgumentException("Url not valid");
        }
        int a = url.indexOf("//") + 2;
        int b = url.indexOf('/', a + 1);
        String hostName = (b == -1) ? url.substring(a) : url.substring(a, b);
        return hostName;
    }

    public void setUrl(String url) {
        if (url == null) {
            this.url = null;
            return;
        }
        String tmp;
        int fix = url.indexOf("/remoting");
        if (fix == -1) {
            tmp = url;
        } else {
            tmp = url.substring(0, fix);
        }
        if (tmp.endsWith("/")) {
            tmp = tmp.substring(0, tmp.length() - 1);
        }
        this.url = tmp;
    }

    public ConnectionCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(ConnectionCredentials credentials) {
        this.credentials = credentials;
    }

    public ConnectionClass getConnectionClass() {
        return connectionClass;
    }

    public void setConnectionClass(ConnectionClass connectionClass) {
        this.connectionClass = connectionClass;
    }

    public boolean isAutoconnect() {
        return autoconnect;
    }

    public void setAutoconnect(boolean autoconnect) {
        this.autoconnect = autoconnect;
    }

    @Override
    public String toString() {
        return connectionClass == null ? "null" : connectionClass.toString() + " " + url;
    }

    @Override
    public int compareTo(ConnectionItem o) {
        try {
            return getName().compareTo(o.getName());
        } catch (Exception ex) {
            return 0;
        }
    }
}
