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

import cz.req.ax.AxApplicationWindow;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.command.support.CommandGroupFactoryBean;

/**
 * @author Ondrej Burianek
 */
@Configurable
public class AxNaviCommandFactory extends CommandGroupFactoryBean {

    private AxApplicationWindow applicationWindow;
//    private CommandConfigurer commandConfigurer;

//    @Override
//    public void setApplicationWindow(ApplicationWindow applicationWindow) {
//        this.applicationWindow = (AxApplicationWindow) applicationWindow;
//    }

    public AxApplicationWindow getApplicationWindow() {
        Assert.notNull(applicationWindow, "Application window is null");
        return applicationWindow;
    }


//    public void setViews(List views) {
//        if (views == null | views.isEmpty()) {
//            return;
//        }
//        ArrayList<ActionCommand> commands = new ArrayList<ActionCommand>();
//        for (Object object : views) {
//            ViewDescriptor viewDescriptor = (ViewDescriptor) object;
//            //TODO Fix window
//            ActionCommand command = viewDescriptor.createShowViewCommand(getApplicationWindow());
//            command.setDefaultFaceDescriptorId("label");
//            commandConfigurer.configure(command);
//            commands.add(command);
//        }
//        setMembers(commands.toArray());
//    }

    @Override
    protected void initCommandGroupMembers(CommandGroup group) {
        super.initCommandGroupMembers(group);
    }
}
