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

package cz.req.ax.widget;

import org.valkyriercp.component.Focussable;
import org.valkyriercp.form.AbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * AxFormWidget
 *
 * @author Ondrej Burianek
 */
public class AxFormWidget extends AxDataWidget implements Focussable {

    private AbstractForm form;
    private boolean enabled = true;

    public AxFormWidget(AbstractForm form) {
        setForm(form);
    }

    /*public AxFormWidget(AxF designer) {
//            this.formGuard = new FormGuard(formModel);
        form = new AxForm() {
            @Override
            public FormModel initFormModel() {
                return getDataFactory().getFormModel();
            }
            @Override
            public JComponent initFormControl(BindingFactory bindingFactory, FormModel formModel) {
        };
        addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                form.setFormObject(evt.getNewValue());
            }
        });
        form.setEnabled(enabled);
    }*/
    /*public BindingFactory getBindingFactory() {
        if (bindingFactory == null) {
            //MIG
            throw new UnsupportedOperationException();
//            bindingFactory = ((BindingFactoryProvider) getService(BindingFactoryProvider.class)).getBindingFactory(getDataFactory().getFormModel());
        }
        return bindingFactory;
    }*/
    final void setForm(AbstractForm form) {
        this.form = form;
        addValueChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                AxFormWidget.this.form.setFormObject(evt.getNewValue());
            }
        });
        this.form.setEnabled(enabled);
    }

    public AbstractForm getForm() {
        return form;
    }

    @Override
    public void createWidget() {
        //TODO Replace Header?
        addFull(getForm());
    }

    @Override
    public void grabFocus() {
        if (getForm() instanceof Focussable) {
            ((Focussable) form).grabFocus();
        }
    }

    public void setFormObject(Object formObject) {
        getForm().setFormObject(formObject);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
