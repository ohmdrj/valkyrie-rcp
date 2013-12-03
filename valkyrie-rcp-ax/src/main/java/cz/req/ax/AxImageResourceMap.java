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

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Properties;

/**
 * @author Ondrej Burianek
 */
public class AxImageResourceMap extends HashMap<String, Object> {

    private DefaultResourceLoader loader = new DefaultResourceLoader();
    public static String defaultPath = "cz/req/ax/image";

    public AxImageResourceMap() {
        addBase("org.valkyriercp.images.images");
        addPackage("cz.req.ax.image.images");
    }

    public void addBase(String fileBase) {
        Resource resource = loader.getResource("classpath:/" + fileBase.replace('.', '/') + ".properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            CollectionUtils.mergePropertiesIntoMap(properties, this);
        } catch (Exception ex) {
            //TODO Logging
            ex.printStackTrace();
        }
    }

    public void addPackage(String fileBase) {
        addPackage(fileBase, null);
    }

    public void addPackage(String fileBase, String iconsPackage) {
        String filePath = fileBase.replace('.', '/');
        String packPath = iconsPackage == null ? defaultPath : iconsPackage.replace('.', '/');
        Resource resource = loader.getResource("classpath:/" + filePath + ".properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            for (String propertyName : properties.stringPropertyNames()) {
                put(propertyName, '/' + packPath + '/' + properties.getProperty(propertyName));
            }
        } catch (Exception ex) {
            //TODO Logging
            ex.printStackTrace();
        }
    }
}
