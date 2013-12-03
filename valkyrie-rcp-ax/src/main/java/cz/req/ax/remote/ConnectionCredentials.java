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
 * ConnectionCredentials
 *
 * @author Ondrej Burianek
 */
public class ConnectionCredentials {

    private String username, password;
    private boolean remember;

    public ConnectionCredentials() {
        remember = false;
    }

    public ConnectionCredentials(String username, String password, boolean remember) {
        this.username = username;
        this.password = password;
        this.remember = remember;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public boolean isEmpty() {
        if (username == null || username.isEmpty()) {
            return true;
        }
        if (password == null || password.isEmpty()) {
            return true;
        }
        return false;
    }

    public ConnectionCredentials newClone() {
        ConnectionCredentials clone = new ConnectionCredentials();
        clone.setUsername(username);
        clone.setPassword(password);
        clone.setRemember(remember);
        return clone;
    }
}
