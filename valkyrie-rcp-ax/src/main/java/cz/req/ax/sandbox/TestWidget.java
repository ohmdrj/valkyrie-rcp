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
import cz.req.ax.remote.ConnectionRegistry;
import cz.req.ax.widget.AxWidget;
import cz.req.ax.widget.table.AxTableWidget;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class TestWidget extends AxWidget {

    @Autowired
    ConnectionRegistry connectionRegistry;
    AxTableWidget tableWidget;

    @Override
    public void createWidget() {
        tableWidget = new AxTableWidget();
        tableWidget.setDataProvider(new AxDataProvider(TestItem.class) {

            @Override
            public List getList(Object criteria) {
                return TestItem.itemsList;
            }
        });
        tableWidget.setTableDescription("string");
        addFull(tableWidget);
        addRow(new JButton(new AbstractAction("Refresh") {

            @Override
            public void actionPerformed(ActionEvent e) {
                tableWidget.refresh();
            }
        }));
        addRow(new JLabel("Autowire: " + (connectionRegistry == null ? "NULL :(" : "OK!! :)")));
    }
}
