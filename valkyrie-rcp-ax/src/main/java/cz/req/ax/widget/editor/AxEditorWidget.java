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

package cz.req.ax.widget.editor;

import cz.req.ax.data.AxEditorBinder;
import cz.req.ax.widget.AxFormWidget;
import cz.req.ax.widget.table.AxTableDescription;
import cz.req.ax.widget.table.AxTableWidget;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.Observable;
import java.util.Observer;

/**
 * AxEditorWidget
 * Widget for TableWidget and DetailWidget decorated by header+toolbar.
 * Initialize with setMasterTable and setDetailEditor/setDetailDialog
 *
 * @author Ondrej Burianek
 */
//TODO Rename to AxTableEditor?
public class AxEditorWidget<D extends AxAbstractDetail> extends AxAbstractEditor<AxTableWidget, D> {

    @PostConstruct
    protected void initWidget() {
    }

    @Override
    public void createWidget() {
        super.createWidget();
        if (getToolbar() != null) {
            JTextField filterField = getMaster().getTableWidget().getTextFilterField();
            getToolbar().setFilterField(filterField);
        }
    }

    public AxEditorBinder createBinder(String field) {
        return new AxEditorBinder(this, field);
    }

    public void setDetailEditor(AxFormWidget widget) {
        AxDetailEditor detailEditor = new AxDetailEditor();
        detailEditor.setDataProvider(getDataProvider());
        detailEditor.setFormWidget(widget);
        setDetail((D) detailEditor);
    }

    public void setDetailDialog(AxFormWidget widget) {
        AxDetailDialog detailDialog = new AxDetailDialog();
        detailDialog.setDataProvider(getDataProvider());
        detailDialog.setFormCreate(widget);
        setDetail((D) detailDialog);
    }

    // Testing
    public void setDetailTab(AxFormWidget widget) {
        AxDetailTab detailTab = new AxDetailTab();
        detailTab.setDataProvider(getDataProvider());
        detailTab.setFormCreate(widget);
        setDetail((D) detailTab);
    }

    public AxTableDescription getMasterTable() {
        return getMaster().getTableDescription();
    }

    public void setMasterTable(AxTableDescription tableDescription) {
        AxTableWidget tableWidget = new AxTableWidget();
        tableWidget.setFilterCriteria(getFilterCriteria());
        tableWidget.setTableDescription(tableDescription);
        //TODO Move? Revize? Hotfix table refresh
        getDataProvider().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                getMaster().doRefresh();
            }
        });
        setMaster(tableWidget);
    }
}
