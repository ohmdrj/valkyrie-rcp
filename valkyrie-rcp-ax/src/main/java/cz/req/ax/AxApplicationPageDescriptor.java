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

package cz.req.ax;

import cz.req.ax.options.AxApplicationSettings;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.valkyriercp.application.PageLayoutBuilder;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.support.AbstractPageDescriptor;
import org.valkyriercp.application.support.BeanFactoryViewDescriptorRegistry;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationPageDescriptor extends AbstractPageDescriptor implements ApplicationContextAware {

    private AxApplicationSettings settings;
    private ApplicationContext applicationContext;
    private Class pageClass;
    private ViewDescriptor viewDescriptor;
    private boolean singleton = true;
    private boolean closable = true;
    private String displayTitle = "none";

    public AxApplicationPageDescriptor() {
    }

    public AxApplicationPageDescriptor(ViewDescriptor viewDescriptor) {
        this.viewDescriptor = viewDescriptor;
    }

    public AxApplicationPageDescriptor(String id, ViewDescriptor viewDescriptor) {
        setId(id);
        this.viewDescriptor = viewDescriptor;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        try {
            settings = (AxApplicationSettings) applicationContext.getBean("settings");
        } catch (Exception ex) {
        }
    }

    @Override
    public void buildInitialLayout(PageLayoutBuilder pageLayout) {
//        Assert.isInstanceOf(AxApplicationPage.class, pageLayout);
        if (pageLayout instanceof AxApplicationPage) {

            AxApplicationPage page = (AxApplicationPage) pageLayout;
            try {
                BeanFactoryViewDescriptorRegistry registry = new BeanFactoryViewDescriptorRegistry();
                registry.setApplicationContext(applicationContext);
//            page.setApplicationContext(applicationContext);
                page.setViewDescriptorRegistry(registry);
            } catch (Exception ex) {
                throw new RuntimeException("Error fixing View Descriptor Registry", ex);
            }
            try {
                if (settings != null) {
                    String initView = settings.get("initview." + getId());
                    if (initView != null && !initView.isEmpty()) {
                        viewDescriptor = page.getViewDescriptor(initView);
                    }
                }
            } catch (Exception ex) {
            }
        }

        if (viewDescriptor != null) {
            pageLayout.addView(viewDescriptor.getId());
        }
    }

    @Override
    public String getDisplayName() {
        if ("displayName".equals(displayTitle)) {
            return super.getDisplayName();
        }
        if ("none".equals(displayTitle) || "false".equals(displayTitle)) {
            return null;
        }
        return displayTitle;
    }

    public Class getPageClass() {
        return pageClass;
    }

    public void setPageClass(Class pageClass) {
        this.pageClass = pageClass;
    }

    public void setViewDescriptor(ViewDescriptor viewDescriptor) {
        this.viewDescriptor = viewDescriptor;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public String getDisplayTitle() {
        return displayTitle;
    }

    public void setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + getId() + " displayname=" + getDisplayName();
    }
//    public void setPrimaryKey(Long primaryKey) {
//        this.primaryKey = primaryKey;
//    }
//
//    public Long getPrimaryKey() {
//        return primaryKey;
//    }
//    @Override
//    public String toString() {
//        return "AxApplicationPageDescriptor (viewName=" + viewName + " pageClass=" + pageClass + ")";
//    }
}
