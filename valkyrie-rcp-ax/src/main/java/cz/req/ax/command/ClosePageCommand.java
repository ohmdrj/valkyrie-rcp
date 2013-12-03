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
import org.valkyriercp.command.support.ApplicationWindowAwareCommand;

import java.awt.event.ActionEvent;

/**
 * AxApplicationPageCloseCommand
 *
 * @author Ondrej Burianek
 */
public class ClosePageCommand extends ApplicationWindowAwareCommand {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    public static final String ID = "closePageCommand";

    public ClosePageCommand() {
        super(ID);
    }

    //    @Override
//    protected ApplicationWindow getApplicationWindow() {
////        if (window == null) {
////            //MIG
////            throw new UnsupportedOperationException("Command is not a bean...");
////        }
//        return applicationConfig.windowManager().getActiveWindow();
//    }
    @Override
    protected void doExecuteCommand() {
//        Assert.isInstanceOf(AxApplicationWindow.class, getApplicationWindow(), "not instance of AxApplicationWindow");
//        if (getApplicationWindow() == null) {
//            throw new UnsupportedOperationException("ApplicationWindow is null!?!");
//        }
        AxApplicationWindow applicationWindow = (AxApplicationWindow) getApplicationWindow();
        ActionEvent event = (ActionEvent) getParameter(ACTION_EVENT_PARAMETER_KEY);
        if (event != null && "middleMouseButtonClicked".equals(event.getActionCommand())) {
            Integer index = Integer.valueOf(event.getID());
            applicationWindow.closePage(index);
        } else {
            applicationWindow.closePage();
        }
    }
}
