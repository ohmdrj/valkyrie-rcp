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
import org.springframework.util.Assert;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.widget.TitledWidgetApplicationDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Ondrej Burianek
 */
public class TableLookupBoxAdapter {

    AxDataProvider dataProvider;
    AxTableWidget tableWidget;
    AxTableDescription tableDescription;
    ValueModel valueModel;

    public TableLookupBoxAdapter() {
    }

    public TableLookupBoxAdapter(TableLookupBox tableLookupBox, AxDataProvider dataProvider, ValueModel valueModel) {
        this.dataProvider = dataProvider;
        this.valueModel = valueModel;
        insLookupListener(tableLookupBox);
    }

    public void setDataProvider(AxTableDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setValueModel(ValueModel valueModel) {
        this.valueModel = valueModel;
    }

    public void insLookupListener(JComponent component) {
        if (component instanceof TableLookupBox) {
            TableLookupBox tableLookupBox = (TableLookupBox) component;
            tableLookupBox.addButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    showLookupDialog();
                }
            });
        }
    }

    public AxTableDescription getTableDescription() {
        Assert.notNull(dataProvider, "AxDataProvider is null");
        if (tableDescription == null) {
            tableDescription = new AxTableDescription(dataProvider);
        }
        return tableDescription;
    }

    public AxTableWidget getTableWidget() {
        Assert.notNull(dataProvider, "AxDataProvider is null");
        if (tableWidget == null) {
//            tableWidget = new AxTableWidget(dataProvider, dataProvider.getTableDescription());
            tableWidget = new AxTableWidget();
            tableWidget.setDataProvider(dataProvider);
            tableWidget.setTableDescription(tableDescription);
            tableWidget.setSearchField(true);
            tableWidget.getTableWidget().getComponent().setPreferredSize(new Dimension(600, 600));
        }
        return tableWidget;
    }

    public void showLookupDialog() {
        getTableWidget().setDataCollection(dataProvider.getList());
        TitledWidgetApplicationDialog applicationDialog = new TitledWidgetApplicationDialog(getTableWidget(), TitledWidgetApplicationDialog.SELECT_CANCEL_MODE) {

            @Override
            protected boolean onSelectNone() {
                valueModel.setValue(null);
                dispose();
                return true;
            }

            @Override
            protected boolean onFinish() {
                Object selection = getTableWidget().getValue();
                if (selection == null) {
                    valueModel.setValue(null);
                } else if (dataProvider.getDataIdentity().getDataClass().isAssignableFrom(selection.getClass())) {
                    valueModel.setValue(selection);
                }
                return true;
            }
        };
        applicationDialog.showDialog();
    }
}
