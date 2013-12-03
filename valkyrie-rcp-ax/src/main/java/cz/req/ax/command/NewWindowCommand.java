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

import org.valkyriercp.command.support.ApplicationWindowAwareCommand;

/**
 * Intended for multiple ApplicationWindow usage. Not used
 *
 * @author Ondrej Burianek
 */
@Deprecated
public class NewWindowCommand extends ApplicationWindowAwareCommand {

    public static final String ID = "newWindowCommand";

    public NewWindowCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        //MIG
//        String startingPageId = Application.instance().getLifecycleAdvisor().getStartingPageId();
//        FlowOpenWindowEvent event = new FlowOpenWindowEvent(this, startingPageId);
//        Application.instance().getApplicationContext().publishEvent(event);
        throw new UnsupportedOperationException();
    }
}
