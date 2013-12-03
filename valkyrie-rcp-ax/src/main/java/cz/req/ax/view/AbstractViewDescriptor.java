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

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.config.CommandButtonLabelInfo;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.ShowViewCommand;
import org.valkyriercp.core.support.LabeledObjectSupport;

/**
 * @author Ondrej Burianek
 */
public abstract class AbstractViewDescriptor extends LabeledObjectSupport implements ViewDescriptor, BeanNameAware, InitializingBean {

    private String id;

    public AbstractViewDescriptor() {
    }

    public AbstractViewDescriptor(String id) {
        setId(id);
    }

    @Override
    public void setBeanName(String name) {
        if (id == null) {
            id = name;
        }
    }

    public void setId(String id) {
        Assert.notNull(id, "id is required");
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public CommandButtonLabelInfo getShowViewCommandLabel() {
        //FIX for missing button label, display at least viewId
        //??? should be in ApplicationObjectConfigurer
        CommandButtonLabelInfo label = getLabel();
        if (label == null || label.getText() == null || label.getText().isEmpty()) {
            return CommandButtonLabelInfo.valueOf(getId());
        }
        return getLabel();
    }

    @Override
    public ActionCommand createShowViewCommand(ApplicationWindow window) {
        return new ShowViewCommand(this, window);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(id, "id is required");
    }
}
