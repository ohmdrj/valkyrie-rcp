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

import cz.req.ax.flow.ApplicationWindowManager;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.CommandGroup;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor implements ApplicationWindowManager {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    protected String navigationBeanName;

    public void setNavigationBeanName(String navigationBeanName) {
        this.navigationBeanName = navigationBeanName;
    }

    @Override
    public ApplicationWindow getWindow() {
        //MIG
        throw new UnsupportedOperationException();
//        return getApplication().getActiveWindow();
    }

    @Override
    public ApplicationWindow openWindow(String pageId) {
        //MIG
        throw new UnsupportedOperationException();
//        getApplication().openWindow(pageId);
//        return getOpeningWindow();
    }

    //MIG
//    public void setCommandsDefinitions(String commandsDefinitions) {
//        super.setWindowCommandManagerBeanName("commandManager");
//        super.setWindowCommandBarDefinitions(commandsDefinitions);
//    }

    @Override
    public CommandGroup getMenuBarCommandGroup() {
        CommandGroup commandGroup = super.getMenuBarCommandGroup();
        return commandGroup;
    }

    @Override
    public StatusBar getStatusBar() {
        return super.getStatusBar();
    }

    //    public CommandGroup getWindowNavigation() {
//        return getWindowNavigation(null);
//    }
    public CommandGroup getNavigation(String beanName) {
        //MIG
        throw new UnsupportedOperationException();
////        ConfigurableListableBeanFactory naviBeanFactory = getCommandBarFactory();
//        if (beanName != null && naviBeanFactory.containsBean(beanName)) {
//            return (CommandGroup) naviBeanFactory.getBean(beanName, CommandGroup.class);
//        } else if (navigationBeanName != null && naviBeanFactory.containsBean(navigationBeanName)) {
//            return (CommandGroup) naviBeanFactory.getBean(navigationBeanName, CommandGroup.class);
//        } else {
//            return null;
//        }
    }
}
