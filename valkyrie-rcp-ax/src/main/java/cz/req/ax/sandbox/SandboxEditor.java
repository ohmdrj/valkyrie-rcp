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

import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.widget.editor.AxEditor;
import cz.req.ax.widget.table.AxLookupBoxBinding;
import cz.req.ax.widget.table.AxTableDataProvider;
import cz.req.ax.widget.table.AxTableDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class SandboxEditor extends AxEditor {

    ArrayList<TestItem> list = new ArrayList<TestItem>(TestItem.samples);

    public SandboxEditor() {
        //setEditorType(EditorType.NEWTAB);
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
                return list;
            }
        });
    }

    @Override
    public void createWidget() {
        super.createWidget();

    }

    @Override
    public void initTable(AxTableDescription description) {
        description.add("string");
    }

    @Override
    public void initForm(AxFormBuilder builder) {
        final String[] stringsArr = {"Prvni", "Druhy", "Duhovy", "Treti"};
        final List<String> stringsLst = Arrays.asList(stringsArr);

        //TODO autoinitialize?
        builder.setStandardPreset1();

        /*AxEditor itemEditor = new AxEditor(getDataProvider()) {
            @Override
            public void initForm(AxFormBuilder builder) {
                builder.addPropertyWithLabel("string");
                builder.addPropertyWithLabel("date");
            }

            @Override
            public void initTable(AxTableDescription description) {
                description.add("string", "date");
            }
        };
        /*builder.addBindingWithLabel("item", itemEditor.createBinder("item").bind(builder.getFormModel(),"item",null));*/
        /*builder.addBindingWithLabel("string", new AxLookupEditorBinding(itemEditor, builder.getFormModel(), "item") {
            @Override
            public String getObjectLabel(Object o) {
                if (o instanceof TestItem) {
                    return ((TestItem)o).getString();
                }
                return "#";
            }

            @Override
            protected Object createFilterFromString(String textFieldValue) {
                return null;
            }
        });*/

//        builder.addBindingWithLabel("string", new AxLookupBoxBinding(builder.getFormModel(), "string", "*", new AxTableDataProvider(String.class) {
//
//
//            @Override
//            public List getList(Object criteria) {
//                return stringsLst;
//            }
//        }));
        builder.addBindingWithLabel("string", new AxLookupBoxBinding(builder.getFormModel(), "item", "string", new AxTableDataProvider(TestItem.class) {
            @Override
            public List getList(Object criteria) {
                return TestItem.samples;
            }
        }));
        builder.addRowGap();
        builder.addPropertyWithLabel("date");
        builder.addRowGap();
        builder.addPropertyWithLabel("string");
        builder.addRowGap();
        builder.addPropertyWithLabel("numberInteger");
        builder.addRowGap();
        builder.addPropertyWithLabel("numberFloat");
        builder.addRowGap();
        builder.addPropertyWithLabel("enumera");
    }

}
