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
import cz.req.ax.widget.table.AxTableDataProvider;
import cz.req.ax.widget.table.AxTableDescription;
import cz.req.ax.widget.table.TableLookupBoxBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class SandboxEditor extends AxEditor {

    ArrayList<TestItem> list = new ArrayList<TestItem>();

    public SandboxEditor() {
        list.add(new TestItem("Foo"));
        list.add(new TestItem("Bar"));

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
    public void initTable(AxTableDescription description) {
        description.add("string");
    }

    @Override
    public void initForm(AxFormBuilder builder) {
        //TODO autoinitialize
        builder.setStandardPreset1();
        builder.addBindingWithLabel("string", new TableLookupBoxBinding(builder.getFormModel(), "string", "*", new AxTableDataProvider(String.class) {

            List<String> strings = Arrays.asList(new String[]{"Prvni", "Druhy", "Treti"});

            @Override
            public List getList(Object criteria) {
                return strings;
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

    public static class TestItem {

        Date date;
        String string;
        Integer numberInteger;
        Float numberFloat;
        TestEnum enumera;

        public TestItem() {
        }

        public TestItem(String string) {
            this.string = string;
            this.date = new Date();
            this.numberInteger = (int) Math.random() * 1000;
            this.numberFloat = (float) Math.random() * 1000f;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Integer getNumberInteger() {
            return numberInteger;
        }

        public void setNumberInteger(Integer numberInteger) {
            this.numberInteger = numberInteger;
        }

        public Float getNumberFloat() {
            return numberFloat;
        }

        public void setNumberFloat(Float numberFloat) {
            this.numberFloat = numberFloat;
        }

        public TestEnum getEnumera() {
            return enumera;
        }

        public void setEnumera(TestEnum enumera) {
            this.enumera = enumera;
        }
    }

    public static enum TestEnum {
        Foo, Bar, Ass

    }
}
