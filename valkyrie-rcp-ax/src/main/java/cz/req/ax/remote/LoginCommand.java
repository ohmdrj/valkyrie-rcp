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

package cz.req.ax.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.command.support.ActionCommand;

/**
 * @author Ondrej Burianek
 */
public class LoginCommand extends ActionCommand implements Runnable {

    @Autowired
    LoginDataProvider dataProvider;
    LoginAuthenticationDialog authenticationDialog;

    public LoginCommand() {
        authenticationDialog = new LoginAuthenticationDialog();
        setId("loginCommand");
        setEnabled(false);
    }

    //    @Autowired
//    public void setDataProvider(LoginDataProvider loginDataProvider) {
//        this.loginDataProvider = loginDataProvider;
//    }
    public void setConnectionItem(ConnectionItem connectionItem) {
        authenticationDialog.setConnectionItem(connectionItem);
        setEnabled(connectionItem != null);
    }

    //    protected ConnectionCredentials getLoginObject() {
//        Object object = form.getFormObject();
//        if (object instanceof ConnectionCredentials) {
//            return (ConnectionCredentials) object;
//        } else {
//            throw new IllegalArgumentException();
//        }
//    }
    @Override
    protected void doExecuteCommand() {
        setEnabled(false);
        new Thread(this, "Login").start();
    }

    @Override
    public void run() {
        try {
            if (!authenticationDialog.onFinish()) {
                authenticationDialog.showDialog();
            }
        } finally {
            setEnabled(true);
        }
    }
}
