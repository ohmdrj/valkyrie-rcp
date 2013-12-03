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

package cz.req.ax.options;

import cz.req.ax.AxUtils;
import cz.thickset.utils.branding.ThicksetProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationSettings {

    @Autowired
    protected ApplicationDescriptor applicationDescriptor;
    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    File file;
    String appname;
    Map<String, String> values;

    public String getAppname() {
        if (appname == null) {
            appname = applicationDescriptor.getDisplayName();
        }
        if (appname == null) {
            //MIG
            appname = ThicksetProject.instance().getName();
        }
        Assert.notNull(appname, "Settings appname must be defined");
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public boolean contains(String key) {
        return getValues().containsKey(key);
    }

    public String get(String key) {
        String value = getValues().get(key);
        return value;
    }

    public Boolean getBoolean(String key) {
        String val = get(key);
        if (val == null) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(val);
    }

    public Integer getInteger(String key) {
        String val = get(key);
        if (val == null) {
            return null;
        }
        return Integer.valueOf(val);
    }

    public String set(String key, String value) {
        return getValues().put(key, value);
    }

    public String setInteger(String key, Integer value) {
        return set(key, value == null ? null : String.valueOf(value));
    }

    public Map<String, String> getValues() {
        if (values == null) {
            values = new HashMap<String, String>();
            iniLoad();
        }
        return values;
    }

    protected File getFile() {
        if (file == null) {
            file = AxUtils.applicationUserFile(getAppname(), "settings.ini");
            log.info("Using settings file " + file.getAbsolutePath());
        }
        return file;
    }

    protected void iniLoad() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(getFile()));
        } catch (FileNotFoundException ex) {
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        for (Object k : properties.keySet()) {
            String s = (String) k;
            getValues().put(s, properties.getProperty(s));
        }
    }

    protected void iniSave() {
        Properties properties = new Properties();
        properties.putAll(getValues());
        try {
            properties.store(new FileOutputStream(getFile()), getAppname());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void writeSettings() {
        iniSave();
    }
}
