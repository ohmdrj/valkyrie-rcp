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

package cz.req.ax.command;

import cz.req.ax.AxApplicationContext;
import cz.req.ax.AxApplicationPageDescriptor;
import org.springframework.util.Assert;

/**
 * PageContextCommand
 *
 * @author Ondrej Burianek
 */
public class PageContextCommand extends ChildContextCommand {

    String customTitle;

    public PageContextCommand(String id) {
        super(id);
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    @Override
    protected void doExecuteCommand() {
        showPage();
    }

    protected void showPage() {
        AxApplicationContext childContext = newPageContext();
        AxApplicationPageDescriptor pageDescriptor = AxApplicationContext.getPagesIterator(childContext).next();
        showPage(childContext, pageDescriptor);
    }

    protected void showPage(AxApplicationContext childContext, String beanId) {
        Assert.notNull(beanId);
        Object bean = ((AxApplicationPageDescriptor) childContext.getBean(beanId));
        AxApplicationPageDescriptor pageDescriptor = (AxApplicationPageDescriptor) bean;
        showPage(childContext, pageDescriptor);
    }

    protected void showPage(AxApplicationContext childContext, AxApplicationPageDescriptor pageDescriptor) {
        if (customTitle != null) {
            pageDescriptor.setDisplayTitle(customTitle);
        }
        childContext.showPage(pageDescriptor);
    }
}