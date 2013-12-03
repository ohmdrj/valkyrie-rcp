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

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;

/**
 * @author Ondrej Burianek
 */
@Deprecated
public class ImplementedCommandProxy {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private Object object;
    private Class proxy;

    public ImplementedCommandProxy(Object object, Class proxy) {
        this.object = object;
        this.proxy = proxy;
    }

    public boolean supports() {
        return ArrayUtils.contains(object.getClass().getInterfaces(), proxy);
    }

    public void invoke() {
        if (supports()) {
            for (Method methodProxy : proxy.getMethods()) {
                if (methodProxy.getParameterTypes().length > 0) {
                    log.warn("Cannot invoke proxy " + proxy.getSimpleName() + " method " + methodProxy + ": parameters not allowed!");
                }
                try {
                    Method methodObject = object.getClass().getMethod(methodProxy.getName());
                    methodObject.invoke(object);
                } catch (Throwable th) {
                    log.error("Error invoking proxy " + proxy.getSimpleName() + " on " + object.getClass().getSimpleName());
                }
            }
        }
    }
}
