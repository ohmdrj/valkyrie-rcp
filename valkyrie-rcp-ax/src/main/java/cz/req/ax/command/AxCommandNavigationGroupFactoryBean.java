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

package cz.req.ax.command;

import cz.req.ax.AxApplicationWindow;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.config.support.ApplicationWindowAware;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;

/**
 * @author Ondrej Burianek
 */
public class AxCommandNavigationGroupFactoryBean extends CommandGroupFactoryBean implements ApplicationWindowAware {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private ApplicationWindow window;

    public AxCommandNavigationGroupFactoryBean() {
    }

    @Override
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
    }

    @Override
    protected void initCommandGroupMembers(CommandGroup group) {
        super.initCommandGroupMembers(group);
        if (window != null && window instanceof AxApplicationWindow) {
//            CommandGroup navigation = ((AxApplicationWindow) window).getWindowNavigation();
//            if (navigation != null && navigation.getMemberCount() > 0) {
//                ///Object result = navigation.getClass().getMethod("getMemberList").invoke(navigation);
//                group.add(navigation, true);
//                configureIfNecessary(navigation);
//            }
        }
    }

    @Override
    protected void configureIfNecessary(AbstractCommand command) {
        super.configureIfNecessary(command);
    }

    @Override
    public Object getObject() throws Exception {
        return super.getObject();
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
