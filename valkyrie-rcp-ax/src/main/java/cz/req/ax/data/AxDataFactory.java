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

package cz.req.ax.data;

import cz.req.ax.AxUtils;
import org.valkyriercp.binding.form.ValidatingFormModel;
import org.valkyriercp.binding.form.support.DefaultFormModel;
import org.valkyriercp.binding.value.ValueModel;

/**
 * AxDataFactory
 *
 * @author Ondrej Burianek
 */
public class AxDataFactory extends AxDataIdentity implements DataFactory {

    boolean recursive = false;

    public AxDataFactory(Class dataClass) {
        super(dataClass);
    }

    public AxDataFactory(Class dataClass, boolean recursive) {
        super(dataClass);
        this.recursive = recursive;
    }

    public AxDataFactory(String dataId, Class dataClass) {
        super(dataId, dataClass);
    }

    public AxDataFactory(String dataId, Class dataClass, boolean recursive) {
        super(dataId, dataClass);
        this.recursive = recursive;
    }

    @Override
    public Object newInstance() {
        return newInstance(null);
    }

    @Override
    public Object newInstance(Object criteria) {
        try {
            Object instance;
            if (recursive) {
                instance = AxUtils.newRecursiveClassInstance(getDataClass());
            } else {
                instance = getDataClass().newInstance();
            }
            return instance;
        } catch (Exception ex) {
            throw new RuntimeException("Object " + getDataClass() + " creation error", ex);
        }
    }

    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }

    @Override
    public ValidatingFormModel getFormModel() {
        DefaultFormModel formModel = new AxFormModel(newInstance());
        formModel.setId(getDataId());
        return formModel;
    }

    public static class AxFormModel extends DefaultFormModel {

        public AxFormModel(ValueModel domainObjectHolder) {
            super(domainObjectHolder, true);
        }

        public AxFormModel(Object domainObject) {
            super(domainObject);
        }
//        @Override
//        public ConversionService getConversionService() {
//            return super.getConversionService();
//        }
    }
}
