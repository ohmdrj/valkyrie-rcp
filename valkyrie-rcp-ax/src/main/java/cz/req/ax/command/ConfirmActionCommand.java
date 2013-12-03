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

import cz.req.ax.AxApp;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.util.MessageConstants;

import javax.swing.*;

/**
 * @author Ondrej Burianek
 */
public abstract class ConfirmActionCommand extends ActionCommand {

    String confirmMessage;

    public ConfirmActionCommand(String commandId) {
        super(commandId);
    }

    public ConfirmActionCommand() {
    }

    public String getConfirmMessage() {
        if (confirmMessage == null) {
            return "?";
        }
        return confirmMessage;
    }

    public void setConfirmMessage(String confirmMessage) {
        this.confirmMessage = confirmMessage;
    }

    @Override
    protected void doExecuteCommand() {
        //MIG
        String message = AxApp.applicationConfig().messageResolver().getMessage(null, getId(), MessageConstants.TEXT);
        String title = AxApp.applicationConfig().messageResolver().getMessage(null, getId(), MessageConstants.TITLE);
        int selected = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
        if (selected == JOptionPane.YES_OPTION) {
            doExecuteConfirmedCommand();
        }
    }

    protected abstract void doExecuteConfirmedCommand();
}
