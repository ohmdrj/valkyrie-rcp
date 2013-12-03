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

import cz.req.ax.AxApp;
import cz.req.ax.view.AxTitledView;
import cz.thickset.utils.swing.FormFactory;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Ondrej Burianek
 */
public class AutoTestView extends AxTitledView {

    public class TestObject implements Comparable<TestObject> {

        String name;

        public TestObject() {
        }

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int compareTo(TestObject o) {
            try {
                return name.compareTo(o.name);
            } catch (Exception ex) {
                ex.printStackTrace();
                return 0;
            }
        }
    }

    ActionCommand testCommand;
    ArrayList<TestObject> list;

    public AutoTestView() {
        super("testView1");
    }

    public AutoTestView(String id) {
        super(id);
    }

    @Override
    public JComponent createViewControl() {
        list = new ArrayList<TestObject>();
        list.add(new TestObject("First"));
        list.add(new TestObject("Second"));

        testCommand = new ActionCommand("Test") {

            @Override
            protected void doExecuteCommand() {
                AxApp.applicationConfig().dialogFactory().showConfirmationDialog("Test");
            }
        };

        PropertyColumnTableDescription tableDescription = new PropertyColumnTableDescription(TestObject.class);
        tableDescription.addPropertyColumn("name");
        GlazedListTableWidget tableWidget = new GlazedListTableWidget(list, tableDescription, false);

        FormFactory formFactory = new FormFactory("p", "p");
        formFactory.addRowExt(new JLabel(AxApp.applicationConfig().application().getName()));
        formFactory.addRowExt(tableWidget.getComponent());
        formFactory.addRowExt(testCommand.createButton());
        return formFactory.getPanel();
    }
}
