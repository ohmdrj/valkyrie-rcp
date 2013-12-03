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

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationOptions implements InitializingBean {

    @Autowired
    MessageSource messageSource;
    @Autowired(required = true)
    AxApplicationSettings settings;
    String path;
    List<OptionGroup> options;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<OptionGroup> getOptions() {
        if (options == null) {
            initalizeOptions();
        }
        return options;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initalizeOptions();
    }

    private void initalizeOptions() {
        options = new ArrayList<OptionGroup>();
        loadDefinition();
        readSettings();
    }

    public void readSettings() {
        for (OptionGroup group : getOptions()) {
            for (OptionItem option : group.getOptions()) {
                String key = group.getName() + "." + option.getName();
                if (settings.contains(key)) {
                    option.setValue(option.read(settings.get(key)));
                } else {
                    Object value = option.getDeflt();
                    settings.set(key, option.write(value));
                    option.setValue(value);
                }
            }
        }
    }

    public void writeSettings() {
        for (OptionGroup group : getOptions()) {
            for (OptionItem option : group.getOptions()) {
                String key = group.getName() + "." + option.getName();
                settings.set(key, option.write(option.getValue()));
            }
        }
        settings.iniSave();
    }

    protected void loadDefinition() {
        getOptions().clear();
        if (path == null) {
            //TODO warn no options definition
            return;
        }
        try {
            InputStream inputStream = getClass().getResourceAsStream(path);
            Document document = new SAXReader().read(inputStream);
            Element root = document.getRootElement();
            for (Object o1 : root.elements("group")) {
                Element e1 = (Element) o1;
                OptionGroup group = new OptionGroup();
                group.setName(e1.attributeValue("name"));
                getOptions().add(group);
                for (Object o2 : e1.elements("option")) {
                    Element e2 = (Element) o2;
                    OptionItem item = new OptionItem();
                    item.setName(e2.attributeValue("name"));
                    item.setType(OptionType.valueOf(e2.attributeValue("type")));
                    item.setDeflt(item.read(e2.attributeValue("default")));
//                    item.setSelections(selections);
                    group.addOption(item);
                    ArrayList selections = new ArrayList();
                    for (Object o3 : e2.elements("selection")) {
                        Element e3 = (Element) o3;
                        String value = e3.attributeValue("value");
                        if (value == null) {
                            value = e3.attributeValue("label");
//                            if (value != null) {
//                                value = messageSource.getMessage(value + ".label", null, value, null);
//                            }
                        }
                        if (value != null) {
                            selections.add(value);
                        }
                    }
                    if (!selections.isEmpty()) {
                        item.setSelections(selections);
                    }
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
