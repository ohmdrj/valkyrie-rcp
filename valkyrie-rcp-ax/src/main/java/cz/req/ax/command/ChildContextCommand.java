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

package cz.req.ax.command;

import cz.req.ax.AxApp;
import cz.req.ax.AxApplicationContext;
import cz.req.ax.support.ExternalPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.valkyriercp.command.support.ActionCommand;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/**
 * ProjektOpenCommand
 *
 * @author Ondrej Burianek
 */
public abstract class ChildContextCommand extends ActionCommand implements Observer {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    HashMap<Class, BeanPostProcessor> externalPostProcessors = new HashMap<Class, BeanPostProcessor>();
    String childContextPath;

    public ChildContextCommand(String id) {
        super(id);
    }

    public void addExternalValue(Class clazz, Object bean) {
        externalPostProcessors.put(clazz, new ExternalPostProcessor(bean));
    }

    public void setExternalValue(Object bean) {
        if (bean == null) {
            return;
        }
        addExternalValue(bean.getClass(), bean);
    }

    public AxApplicationContext newPageContext() {
        AxApplicationContext childContext = new AxApplicationContext(childContextPath,
                AxApp.applicationConfig().applicationContext(), externalPostProcessors.values());
        return childContext;
    }

    @Override
    public void update(Observable o, Object arg) {
//        beanPostProcessor.setExternalValue(arg);
        throw new UnsupportedOperationException();
    }

    //    public void setExternalName(String externalName) {
//        beanPostProcessor.setExternalName(externalName);
//    }
//
//    public void setExternalValue(Object externalValue) {
//        beanPostProcessor.setExternalValue(externalValue);
//    }
    public void setChildContextPath(String childContextPath) {
        this.childContextPath = childContextPath;
    }
}
