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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ConnectionRegistry
 *
 * @author Ondrej Burianek
 */
public class ConnectionRegistry {

    List<ConnectionClass> classes = new ArrayList<ConnectionClass>();
    List<ConnectionItem> defaults = new ArrayList<ConnectionItem>();
    List<ConnectionInstance> instances = new ArrayList<ConnectionInstance>();

    public ConnectionRegistry(ConnectionClass... classes) {
        this.classes.addAll(Arrays.asList(classes));
    }

    public List<ConnectionClass> getClasses() {
        return classes;
    }

    public ConnectionRegistry setClasses(ArrayList<ConnectionClass> classes) {
        this.classes = classes;
        return this;
    }

    public ConnectionRegistry setClasses(ConnectionClass... classes) {
        this.classes = Arrays.asList(classes);
        return this;
    }

    public List<ConnectionItem> getDefaults() {
        return defaults;
    }

    public ConnectionRegistry setDefaults(ArrayList<ConnectionItem> defaults) {
        this.defaults = defaults;
        return this;
    }

    public ConnectionRegistry setDefaults(ConnectionItem... defaults) {
        this.defaults = Arrays.asList(defaults);
        return this;
    }

    public List<ConnectionInstance> getInstances() {
        return instances;
    }

    public void setInstances(ArrayList<ConnectionInstance> instances) {
        this.instances = instances;
    }

    public ConnectionClass findClass(String name) {
        for (ConnectionClass cc : classes) {
            if (cc.getName().equals(name)) {
                return cc;
            }
        }
        throw new RuntimeException("Connection class not registered!");
    }
}
