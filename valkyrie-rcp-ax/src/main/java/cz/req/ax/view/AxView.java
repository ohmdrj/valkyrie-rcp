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

package cz.req.ax.view;

import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.support.AbstractView;

/**
 * AxView
 *
 * @author Ondrej Burianek
 */
public abstract class AxView extends AbstractView {

    public AxView(String id) {
        super(id);
    }

    @Override
    public ViewDescriptor getDescriptor() {
        return super.getDescriptor();
    }

    @Override
    protected ViewDescriptor createViewDescriptor(String id) {
        return new AxViewDescriptor(id, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + getId();
    }
}
