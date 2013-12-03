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

package cz.req.ax.support;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * ExternalBean
 *
 * @author Ondrej Burianek
 */
public class ExternalBean implements FactoryBean, BeanNameAware {

    private String name;
    private Object object;
    private Class objectType;

    public ExternalBean() {
    }

    public ExternalBean(Class objectType) {
        this.objectType = objectType;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    public void setObject(Object object) {
        if (objectType != null && !objectType.isAssignableFrom(object.getClass())) {
            throw new IllegalArgumentException(name + " requires class " + objectType.getCanonicalName());
        }
        this.object = object;
    }

    @Override
    public Object getObject() throws Exception {
        if (object == null) {
            throw new IllegalArgumentException(name + " external object not set");
        }
        return object;
    }

    public void setObjectType(Class type) {
        this.objectType = type;
    }

    @Override
    public Class getObjectType() {
        if (objectType != null) {
            return objectType;
        }
        if (object != null) {
            return object.getClass();
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
