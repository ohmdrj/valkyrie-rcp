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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * ExternalPostProcessor
 *
 * @author Ondrej Burianek
 */
public class ExternalPostProcessor implements BeanPostProcessor {

    private String externalName;
    private Object externalValue;

    public ExternalPostProcessor() {
    }

    //    public ExternalPostProcessor(String externalName) {
//        this.externalName = externalName;
//    }
    public ExternalPostProcessor(Object externalValue) {
        this.externalValue = externalValue;
    }

    //    public ExternalPostProcessor(String beanName, Object externalValue) {
//        this.externalName = beanName;
//        this.externalValue = externalValue;
//    }
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }

    public void setExternalValue(Object externalValue) {
        this.externalValue = externalValue;
    }

    public String getExternalName() {
        return externalName;
    }

    public Object getExternalValue() {
        return externalValue;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ExternalBean) {
            ExternalBean externalBean = (ExternalBean) bean;
            if (externalBean.getObjectType() == null) {
                return bean;
            }
            if (externalValue == null) {
                return bean;
            }
            if (externalBean.getObjectType().equals(externalValue.getClass())) {
                //Same classes
//                boolean thisOne = true;
//                if (externalName != null) {
//                    thisOne = externalName.equals(beanName);
//                }
//                if (thisOne) {
                externalBean.setObject(externalValue);
//                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
