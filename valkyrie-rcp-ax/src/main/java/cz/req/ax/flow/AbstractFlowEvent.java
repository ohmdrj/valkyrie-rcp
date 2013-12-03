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

package cz.req.ax.flow;

import org.springframework.context.ApplicationEvent;

/**
 * @author Ondrej Burianek
 */
public class AbstractFlowEvent extends ApplicationEvent {

    public AbstractFlowEvent(Object source) {
        super(source);
    }

    public AbstractFlowEvent(Object source, Object input) {
        super(source);
        this.input = input;

    }

    private Object input;

    public Object getInput() {
        return input;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
