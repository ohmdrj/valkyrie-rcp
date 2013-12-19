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

package cz.req.ax.sandbox;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import cz.req.ax.widget.AxWidget;
import cz.thickset.utils.swing.FormFactory;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import java.awt.*;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class TestAutocomlete extends AxWidget {

    @Override
    public void createWidget() {
        FormFactory formFactory = new FormFactory("p", "p");


        JXComboBox comboBox1 = new JXComboBox();
        AutoCompleteSupport.install(comboBox1, GlazedLists.eventList(TestItem.stringsLst)).setStrict(false);


        JXComboBox comboBox2 = new JXComboBox(TestItem.stringsArr);
        comboBox2.setEditable(true);
        AutoCompleteDecorator.decorate(comboBox2);

        JTextField textField = new JTextField();
        AutoCompleteDecorator.decorate(textField, TestItem.stringsLst, false);

        formFactory.addRowGap(comboBox1);
        formFactory.addRowGap(comboBox2);
        formFactory.addRowGap(textField);

        // == Glazed way
        JXComboBox comboBox3 = new JXComboBox();
        Format testFormat = new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                if (obj instanceof TestItem) {
                    TestItem item = (TestItem) obj;
                    toAppendTo.append(item.getString());
                    toAppendTo.append(" / ");
                    toAppendTo.append(item.getNumberInteger());
                }
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos) {
                String search = source.substring(pos.getIndex());
                for (TestItem item : TestItem.itemsList) {
                    if (item.getString().startsWith(search)) {
                        return item;
                    }
                }
                return null;
            }
        };
        AutoCompleteSupport.install(comboBox3,
                GlazedLists.eventList(TestItem.itemsList),
                GlazedLists.textFilterator(TestItem.class, "string"),
                testFormat).setStrict(false);

        // === SwingX way
        JXComboBox comboBox4 = new JXComboBox(TestItem.itemsList.toArray());
        comboBox4.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TestItem) {
                    TestItem item = (TestItem) value;
                    setText(item.getString() + " / " + item.getNumberInteger());
                } else {
                    setText("!");
                }
                return this;
            }
        });
        AutoCompleteDecorator.decorate(comboBox4, new ObjectToStringConverter() {
            @Override
            public String getPreferredStringForItem(Object value) {
                if (value instanceof TestItem) {
                    TestItem item = (TestItem) value;
                    return item.getString() + " / " + item.getNumberInteger();
                }
                return "!";
            }
        });

        formFactory.addRowGap(comboBox3);
        formFactory.addRowGap(comboBox4);

        addFull(formFactory.getPanel());
    }
}
