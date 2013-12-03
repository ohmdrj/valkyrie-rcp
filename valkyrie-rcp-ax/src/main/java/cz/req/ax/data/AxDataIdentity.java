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

import org.springframework.util.Assert;

/**
 * AxDataIdentity
 *
 * @author Ondrej Burianek
 */
public class AxDataIdentity implements DataIdentity {

    private String dataId;
    private Class dataClass;

    public static String getClassSimpleName(Class clazz) {
        String string = clazz.getSimpleName();
        if (string.endsWith("VO") && string.length() > 2) {
            string = string.substring(0, string.length() - 2);
        }
        return string.toLowerCase();
    }

    public AxDataIdentity(Class dataClass) {
        this(null, dataClass);
    }

    public AxDataIdentity(String dataId, Class dataClass) {
        Assert.notNull(dataClass);
        this.dataClass = dataClass;
        if (dataId == null) {
            this.dataId = getClassSimpleName(dataClass);
        } else {
            this.dataId = dataId;
        }
    }

    @Override
    public Class getDataClass() {
        return dataClass;
    }

    @Override
    public String getDataId() {
        return dataId;
    }
}
