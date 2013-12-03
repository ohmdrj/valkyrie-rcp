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

package cz.req.ax.widget.table;

import cz.req.ax.data.RefreshListener;
import org.springframework.beans.support.PropertyComparator;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.ComboBoxBinding;
import org.valkyriercp.list.BeanPropertyValueComboBoxEditor;
import org.valkyriercp.list.BeanPropertyValueListRenderer;

import javax.swing.*;

/**
 * TableLookupBoxBinding
 *
 * @author Ondrej Burianek
 */
public class TableLookupBoxBinding extends ComboBoxBinding implements RefreshListener {

    AxTableDataProvider dataProvider;
    TableLookupBoxAdapter tableLookupAdapter;
    TableLookupBox tableLookupBox;
    String fieldPropertyPath;

    public TableLookupBoxBinding(FormModel formModel, String formPropertyPath, String fieldPropertyPath, AxTableDataProvider dataProvider) {
        super(new JComboBox(), formModel, formPropertyPath, null);
        this.fieldPropertyPath = fieldPropertyPath;
        this.dataProvider = dataProvider;

        setRenderer(new BeanPropertyValueListRenderer(fieldPropertyPath));
        setEditor(new BeanPropertyValueComboBoxEditor(getEditor(), fieldPropertyPath));
        setComparator(new PropertyComparator(fieldPropertyPath, true, true));
    }

    @Override
    public JComponent getComponent() {
        if (tableLookupBox == null) {
            JComboBox comboBox = (JComboBox) super.getComponent();
            tableLookupBox = new TableLookupBox(comboBox);
            tableLookupAdapter = new TableLookupBoxAdapter(tableLookupBox, dataProvider, getValueModel());
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

    //    public final void setTableDataProvider(AxTableDataProvider tableDataProvider) {
//        tableLookupAdapter.setDataProvider(tableDataProvider);
//    }
    @Override
    public void refresh() {
        if (dataProvider != null) {
            setSelectableItems(dataProvider.getList());
        }
    }
}
