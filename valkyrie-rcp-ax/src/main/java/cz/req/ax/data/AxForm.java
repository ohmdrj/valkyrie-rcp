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

package cz.req.ax.data;

import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.widget.AbstractFocussableWidgetForm;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * AxForm
 *
 * @author Ondrej Burianek
 */

public abstract class AxForm extends AbstractFocussableWidgetForm {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public AxForm() {
    }

    public AxForm(String id) {
        super(id);
    }

    public abstract FormModel initFormModel();

    public abstract JComponent initFormControl(BindingFactory bindingFactory, FormModel formModel);

    @Override
    public FormModel createFormModel() {
        return initFormModel();
    }

    @Override
    protected JComponent createFormControl() {
        JComponent formControl = initFormControl(getBindingFactory(), getFormModel());
        formControl.isFocusable();
        for (Component c : formControl.getComponents()) {
            if (c instanceof JTextComponent) {
                setFocusControl((JComponent) c);
                return formControl;
            }
        }
        return formControl;
    }

    public void setFocusControl(JComponent[] components) {
        try {
            super.setFocusControl(components[1]);
        } catch (Exception ex) {
            log.warn("Cannot set focus control " + components);
        }
    }

    public void setReadOnly(JComponent[] components) {
        try {
            ((JTextComponent) components[1]).setEditable(false);
        } catch (Exception xx) {
            try {
                components[1].setEnabled(false);
            } catch (Exception ex) {
                log.warn("Cannot set read only " + components);
            }
        }
    }
}
