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

package cz.req.ax.remote;

import org.valkyriercp.binding.validation.support.DefaultValidationMessage;
import org.valkyriercp.core.Severity;
import org.valkyriercp.dialog.FormBackedDialogPage;
import org.valkyriercp.dialog.TitledPageApplicationDialog;

/**
 * @author Ondrej Burianek
 */
public class LoginAuthenticationDialog extends TitledPageApplicationDialog {

    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoginAuthenticationDialog.class);
    LoginAuthenticationForm authenticationForm;

    public LoginAuthenticationDialog() {
        authenticationForm = new LoginAuthenticationForm();
        setDialogPage(new FormBackedDialogPage(authenticationForm));
    }

    public void setConnectionItem(ConnectionItem connectionItem) {
        authenticationForm.setFormObject(connectionItem);
    }

    @Override
    protected boolean onFinish() {
        authenticationForm.commit();
        try {
            ConnectionItem connectionItem = (ConnectionItem) authenticationForm.getFormObject();
            new LoginAuthentication(getApplicationConfig()).login(connectionItem);

            if (connectionItem.getCredentials().isRemember()) {
                //TODO Store credentials
//                loginDataProvider.xmlSave();
            }
            return true;
        } catch (ConnectionInstance.AuthenticationException ex) {
            log.warn("Login failed: " + ex.getMessage(), ex);
            setMessage(new DefaultValidationMessage("password", Severity.ERROR, ex.getMessage()));
            return false;
        } catch (Throwable th) {
            setMessage(new DefaultValidationMessage("password", Severity.ERROR, "Application error: " + th.getMessage()));
            log.error("Login error", th);
            throw new RuntimeException(th);
        }
    }
}
