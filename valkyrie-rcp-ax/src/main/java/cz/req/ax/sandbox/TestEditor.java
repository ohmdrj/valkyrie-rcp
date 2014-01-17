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

package cz.req.ax.sandbox;

import com.jgoodies.forms.layout.CellConstraints;
import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.data.binding.AxTableBinding;
import cz.req.ax.widget.AxFormFactory;
import cz.req.ax.widget.editor.AxEditor;
import cz.req.ax.widget.table.AxLookupBoxBinding;
import cz.req.ax.widget.table.AxTableDataProvider;
import cz.req.ax.widget.table.AxTableDescription;
import cz.req.ax.widget.table.AxTableWidget;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class TestEditor extends AxEditor {

    ArrayList<TestItem> list = new ArrayList<TestItem>(TestItem.itemsList);

    public TestEditor() {
//        setEditorType(EditorType.NEWTAB);
        setDataProvider(new AxDataProvider(TestItem.class) {

            @Override
            public Object doCreate(Object newData) {
                list.add((TestItem) newData);
                return newData;
            }

            @Override
            public Object doUpdate(Object updatedData) {
                return updatedData;
            }

            @Override
            public void doDelete(Object dataToRemove) {
                list.remove(dataToRemove);
            }

            @Override
            public List getList(Object criteria) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                return list;
            }
        });
    }

    @Override
    public void createWidget() {
        super.createWidget();
        GlazedListTableWidget tableWidget = ((AxTableWidget) getMaster()).getTableWidget();
        String name = tableWidget.getTable().getColumnName(1);
        TableColumn column = tableWidget.getTable().getColumn(name);
    }

    @Override
    public void initTable(AxTableDescription description) {
        description.add("string", SortOrder.ASCENDING);
        description.add("date", 80);
        description.add("enumera", 40);
        description.add("numberDecimal");
    }

    @Override
    public void initForm(AxFormBuilder builder) {
        final String[] stringsArr = {"Prvni", "Druhy", "Duhovy", "Treti"};
        final List<String> stringsLst = Arrays.asList(stringsArr);

        //TODO autoinitialize?
        //builder.setStandardPreset1();
        builder.setStandardPreset2();


        builder.addPropertyWithLabel("string");
        builder.addBindingWithLabel("string", new AxLookupBoxBinding(builder.getFormModel(), "item", "string", new AxTableDataProvider(TestItem.class) {
            @Override
            public List getList(Object criteria) {
                return TestItem.itemsList;
            }
        }));
        builder.addPropertyWithLabel("date");
        builder.addPropertyWithLabel("localDate");
        builder.addPropertyWithLabel("numberInteger");
        builder.addPropertyWithLabel("numberDecimal");
        builder.addPropertyWithLabel("enumera");
        builder.addBindingWithLabel("color", new ColorBinding(builder.getFormModel(),"color1"));
        builder.addBindingWithLabel("color", new ColorBinding(builder.getFormModel(),"color2"));

        AxTableDescription tableDesc = new AxTableDescription(TestItem.class);
        tableDesc.add("string", "date");
        AxTableBinding tableBind = new AxTableBinding(builder.getFormModel(), "childs", tableDesc) {
            @Override
            protected Object getNewFormObject() {
                return new TestItem("New");
            }

            @Override
            protected AbstractForm createAddEditForm() {
                return new AxFormFactory(TestItem.class) {
                    @Override
                    public void initForm(AxFormBuilder builder) {
                        builder.setStandardPreset1();
                        builder.addPropertyWithLabel("string");
                        builder.addPropertyWithLabel("date");
                    }
                }.getForm();
            }
        };
        tableBind.setAllSupported(true);
        builder.addBinding(tableBind, new CellConstraints(5, 1, 3, 13));

    }

}
