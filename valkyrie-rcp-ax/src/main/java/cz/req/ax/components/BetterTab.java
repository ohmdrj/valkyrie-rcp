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

package cz.req.ax.components;

import cz.req.ax.AxApplicationPageDescriptor;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.PageDescriptor;

import javax.swing.*;

/**
 * BetterTab
 *
 * @author Ondrej Burianek
 */
public class BetterTab {

    private int index;
    private JComponent component;
    private PageDescriptor descriptor;
    private ApplicationPage page;
//    private String displayName;

    public BetterTab(PageDescriptor descriptor, ApplicationPage page) {
        this.descriptor = descriptor;
        this.page = page;
    }

    public String getDisplayName() {
        String displayName = descriptor.getDisplayName();
        return displayName;
    }

    /*public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }*/

    public boolean isSingleton() {
        return isSingleton(descriptor);
    }

    public static boolean isSingleton(PageDescriptor pageDescriptor) {
        boolean sigleton = true;
        if (pageDescriptor instanceof AxApplicationPageDescriptor) {
            sigleton = ((AxApplicationPageDescriptor) pageDescriptor).isSingleton();
        }
        return sigleton;
    }

    public JComponent getComponent() {
        if (component == null) {
            component = page.getControl();
        }
        return component;
    }

    public PageDescriptor getDescriptor() {
        return descriptor;
    }

    public ApplicationPage getPage() {
        return page;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JComponent) {
            return obj.equals(getComponent());
        }
//        if (obj instanceof PageDescriptor) {
//            return obj.equals(descriptor);
//        }
        if (obj instanceof ApplicationPage) {
            return ((ApplicationPage) obj).getId().equals(page.getId());
        }
        return super.equals(obj);
    }
}
