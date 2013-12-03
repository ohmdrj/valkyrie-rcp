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

package cz.req.ax.data;

import org.springframework.context.ApplicationEvent;

/**
 * @author Ondrej Burianek
 */
public class RefreshEvent extends ApplicationEvent {

    private Class clazz;

    public RefreshEvent(Object source, Class clazz) {
        super(source);
        this.clazz = clazz;
    }

    public static boolean isInstance(ApplicationEvent evt, Class c2) {
        if (evt instanceof RefreshEvent) {
            if (((RefreshEvent) evt).getClazz().equals(c2)) {
                return true;
            }
        }
        return false;
    }

    public Class getClazz() {
        return clazz;
    }
}
