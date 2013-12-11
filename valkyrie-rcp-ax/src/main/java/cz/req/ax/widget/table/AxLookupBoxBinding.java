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

package cz.req.ax.widget.table;

import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.RefreshListener;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.support.PropertyComparator;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.ComboBoxBinding;
import org.valkyriercp.list.BeanPropertyValueListRenderer;

import javax.swing.*;

/**
 * TableLookupBinding
 * 2 variants: ComboBox, AutoComplete
 *
 * @author Ondrej Burianek
 */

public class AxLookupBoxBinding extends ComboBoxBinding implements RefreshListener {

    AxDataProvider dataProvider;
    TableLookupBoxAdapter tableLookupAdapter;
    TableLookupBox tableLookupBox;
    String propertyPath;

    public AxLookupBoxBinding(FormModel formModel, String formPropertyPath, String fieldPropertyPath, AxDataProvider dataProvider) {
        super(new JComboBox(), formModel, formPropertyPath, null);
        this.propertyPath = fieldPropertyPath;
        this.dataProvider = dataProvider;

        setRenderer(new BeanPropertyValueListRenderer(fieldPropertyPath));
        //setEditor(new BeanPropertyValueComboBoxEditor(getEditor(), propertyPath));
        setComparator(new PropertyComparator(fieldPropertyPath, true, true));
    }

    protected ObjectToStringConverter createStringConverter() {
        return  new ObjectToStringConverter() {
            final BeanWrapperImpl beanWrapper = new BeanWrapperImpl();

            @Override
            public String getPreferredStringForItem(Object item) {
                if (item == null) {
                    return null;
                }
                if (propertyPath == null || propertyPath.equals("*")) {
                    return String.valueOf(item);
                }
                beanWrapper.setWrappedInstance(item);
                Object propertyValue = beanWrapper.getPropertyValue(propertyPath);
                return String.valueOf(propertyValue);
            }
        };
    }

    @Override
    public JComponent getComponent() {
        if (tableLookupBox == null) {
            JComboBox comboBox = (JComboBox) super.getComponent();
            AutoCompleteDecorator.decorate(comboBox, createStringConverter());
            tableLookupBox = new TableLookupBox(comboBox);
            tableLookupAdapter = new TableLookupBoxAdapter(tableLookupBox, dataProvider, getValueModel());
            tableLookupAdapter.getTableDescription().add(propertyPath);
            refresh();
        }
        return tableLookupBox;
    }

    @Override
    public JComboBox getComboBox() {
        getComponent();
        JComboBox comboBox = tableLookupBox.getComboBox();
        return comboBox;
    }

    @Override
    public void refresh() {
        if (dataProvider != null) {
            setSelectableItems(dataProvider.getList());
        }
    }
}
