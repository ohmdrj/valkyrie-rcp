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

package cz.req.ax;

import org.valkyriercp.application.StatusBar;
import org.valkyriercp.application.config.support.DefaultApplicationLifecycleAdvisor;
import org.valkyriercp.command.support.CommandGroup;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationLifecycleAdvisor extends DefaultApplicationLifecycleAdvisor {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Override
    public StatusBar getStatusBar() {
        return new AxStatusBar();
    }

    public CommandGroup getNavigation(String beanName) {
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
