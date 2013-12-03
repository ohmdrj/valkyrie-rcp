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

import cz.req.ax.data.AxDataFactory;
import cz.req.ax.data.AxForm;
import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.data.DataFactory;
import cz.req.ax.dialog.AxFormDialog;
import cz.req.ax.support.FormDesigner;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.BindingFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Ondrej Burianek
 */
public abstract class AxFormFactory implements FormDesigner {


    DataFactory dataFactory;

    public AxFormFactory() {
    }

    public AxFormFactory(Class dataClass) {
        this(new AxDataFactory(dataClass));
    }

    protected AxFormFactory(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    public AxForm getForm() {
        AxForm form = new AxForm() {
            @Override
            public FormModel initFormModel() {
                return dataFactory.getFormModel();
            }

            @Override
            public JComponent initFormControl(BindingFactory bindingFactory, FormModel formModel) {
                AxFormBuilder builder = new AxFormBuilder(bindingFactory, formModel);
                initForm(builder);
                return builder.getPanel();
            }
        };
        return form;
    }

    public AxFormWidget getWidget() {
        final AxForm form = getForm();
        AxFormWidget widget = new AxFormWidget(form);
        //TODO Move listener to form.getFormObjectListener??
        widget.addValueChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                form.setFormObject(evt.getNewValue());
            }
        });
        return widget;
    }

    public AxFormDialog getDialog() {
        final AxForm form = getForm();
        AxFormDialog dialog = new AxFormDialog(form);
        return dialog;
    }
}
