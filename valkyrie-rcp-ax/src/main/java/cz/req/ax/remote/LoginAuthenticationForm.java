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

import com.jgoodies.forms.layout.FormLayout;
import cz.req.ax.AxApp;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.component.ShadowBorder;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.builder.FormLayoutFormBuilder;

import javax.swing.*;

/**
 * LoginForm
 *
 * @author Ondrej Burianek
 */
public class LoginAuthenticationForm extends AbstractForm {

    public LoginAuthenticationForm() {
        super("loginForm");
    }

    @Override
    protected JComponent createFormControl() {
        FormLayoutFormBuilder formBuilder = new FormLayoutFormBuilder(getBindingFactory(),
                new FormLayout("right:d,10px,fill:p", "p"));
        formBuilder.addLabel("url");
        formBuilder.addPropertyAndLabel("url");
        formBuilder.nextRow();
        formBuilder.addPropertyAndLabel("credentials.username");
        formBuilder.nextRow();
        formBuilder.addPasswordFieldAndLabel("credentials.password");
        formBuilder.nextRow();
        formBuilder.addPropertyAndLabel("credentials.remember");
        JPanel panel = formBuilder.getPanel();
        panel.setBorder(new ShadowBorder());
        return panel;
    }

//    public ConnectionCredentials getConnectionCredentials() {
//        Object formObject = getFormObject();
//        return (ConnectionCredentials) formObject;
//    }

    @Override
    public FormModel createFormModel() {
        return AxApp.applicationConfig().formModelFactory().createFormModel(new ConnectionItem());
    }
}
