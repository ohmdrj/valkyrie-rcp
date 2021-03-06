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

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import cz.thickset.utils.swing.FormFactory;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binding;
import org.valkyriercp.form.binding.BindingFactory;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;
import org.valkyriercp.form.builder.AbstractFormBuilder;

import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * @author Ondrej Burianek
 */
//TODO Finish
public class AxFormBuilder extends AbstractFormBuilder {

    protected static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AxFormBuilder.class);
    private FormModel formModel;
    private FormFactory formFactory;
    private boolean autospan = true;

    public AxFormBuilder(BindingFactory bindingFactory, FormModel formModel) {
        this(bindingFactory, formModel, new JPanel());
    }

    public AxFormBuilder(BindingFactory bindingFactory, FormModel formModel, JPanel panel) {
        super(bindingFactory);
        this.formModel = formModel;
        this.formFactory = new FormFactory(panel);
    }

    @Override
    public FormModel getFormModel() {
        return super.getBindingFactory().getFormModel();
    }

    public JPanel getPanel() {
        getBindingFactory().getFormModel().revert();
        return formFactory.getPanel();
    }

    public void setStandardPreset1() {
        setStandardSpecs("p,200px", "p");
    }

    public void setStandardPreset2() {
        setStandardSpecs("p,200px, p,200px", "p");
        this.autospan = false;
    }

    public void setStandardSpecs(String specCol, String specRow, boolean autospan) {
        this.autospan = autospan;
        setStandardSpecs(specCol, specRow);
    }

    public void setStandardSpecs(String specCol, String specRow) {
        formFactory.setStandardSpecs(specCol, specRow);
    }

    public void setStandardRow(RowSpec standardRow) {
        formFactory.setStandardRow(standardRow);
    }

    public void setStandardCols(ColumnSpec... standardCols) {
        formFactory.setStandardCols(standardCols);
    }

    public FormLayout getLayout() {
        return formFactory.getLayout();
    }

    public void addRowGap() {
        formFactory.addRowGap();
    }

    public void addRowExt() {
        formFactory.addRowExt();
    }

    public void addGap() {
        formFactory.addGap();
    }

    public JComponent addComponent(JComponent component) {
        formFactory.addRow(true, autospan, component);
        return component;
    }

    public LabelAndComponent addComponentWithLabel(String property, JComponent component) {
        return addComponentWithLabel(property, component, autospan);
    }

    public LabelAndComponent addComponentWithLabel(String property, JComponent component, boolean autospan) {
        JLabel labelComponent = createLabelFor(property, component);
        formFactory.addRow(true, autospan, labelComponent, component);
        return new LabelAndComponent(labelComponent, component);
    }

    public LabelAndComponent addPropertyWithLabel(String property) {
        return addPropertyWithLabel(property, autospan);
    }

    public LabelAndComponent addPropertyWithLabel(String property, boolean autospan) {
        JComponent propertyComponent = createDefaultBinding(property).getControl();
        JLabel labelComponent = createLabelFor(property, propertyComponent);
        formFactory.addRow(true, autospan, labelComponent, propertyComponent);
        return new LabelAndComponent(labelComponent, propertyComponent);
    }

    public LabelAndComponent addBindingWithLabel(String property, Binding binding) {
        return addBindingWithLabel(property, binding, autospan);
    }

    public LabelAndComponent addBindingWithLabel(String property, Binding binding, boolean autospan) {
        interceptBinding(binding);
        JComponent propertyComponent = binding.getControl();
        JLabel labelComponent = createLabelFor(property, propertyComponent);
        formFactory.addRow(true, autospan, labelComponent, propertyComponent);
        return new LabelAndComponent(labelComponent, propertyComponent);
    }

    public JComponent addBinding(Binding binding, CellConstraints constraints) {
        interceptBinding(binding);
        JComponent propertyComponent = binding.getControl();
        formFactory.addComp(propertyComponent, constraints);
        return propertyComponent;
    }

    private void interceptBinding(Binding binding) {
        ((SwingBindingFactory) getBindingFactory()).interceptBinding(binding);
    }

    public void setReadOnly(LabelAndComponent labelAndComponent) {
        JComponent c = labelAndComponent.getComponent();
        if (c instanceof JTextComponent) {
            ((JTextComponent) c).setEditable(false);
        } else {
            c.setEnabled(false);
        }
    }

    public static class LabelAndComponent {

        private JLabel label;
        private JComponent component;

        public LabelAndComponent(JComponent[] comps) {
            if (comps.length > 1 && comps[0] instanceof JLabel) {
                label = (JLabel) comps[0];
                component = comps[1];
            } else {
                log.warn("Invalid components for LabelAndComponent");
            }
        }

        public LabelAndComponent(JLabel label, JComponent component) {
            this.label = label;
            this.component = component;
        }

        public JComponent getComponent() {
            return component;
        }

        public JLabel getLabel() {
            return label;
        }
    }
}
