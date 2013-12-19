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

/**
 * @author Ondrej Burianek
 */
public class TestView extends AxTitledView {

    ActionCommand testCommand;

    public TestView() {
        super("testView1");
    }

    public TestView(String id) {
        super(id);
    }

    @Override
    public JComponent createViewControl() {
        testCommand = new ActionCommand("Test") {

            @Override
            protected void doExecuteCommand() {
                AxApp.applicationConfig().dialogFactory().showConfirmationDialog("Test");
            }
        };

        PropertyColumnTableDescription tableDescription = new PropertyColumnTableDescription(TestItem.class);
        tableDescription.addPropertyColumn("string");
        GlazedListTableWidget tableWidget = new GlazedListTableWidget(TestItem.itemsList, tableDescription, false);

        FormFactory formFactory = new FormFactory("200px", "p");
        formFactory.addRowExt(new JLabel(AxApp.applicationConfig().application().getName()));
        formFactory.addRowExt(tableWidget.getComponent());
        formFactory.addRowExt(testCommand.createButton());
        return formFactory.getPanel();
    }
}
