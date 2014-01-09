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

package cz.req.ax.remote;

import cz.req.ax.AxApplicationArguments;
import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.widget.AxFormFactory;
import cz.req.ax.widget.editor.AxAbstractDetail;
import cz.req.ax.widget.editor.AxEditorWidget;
import cz.req.ax.widget.table.AxTableDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.form.binding.swing.ComboBoxBinder;
import org.valkyriercp.form.binding.swing.ComboBoxBinding;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * LoginWidget
 *
 * @author Ondrej Burianek
 */
public class LoginWidget extends AxEditorWidget<AxAbstractDetail> {

    @Autowired
    LoginDataProvider loginDataProvider;
    @Autowired
    ComboBoxBinder comboBoxBinder;
    LoginCommand loginCommand;
    AxApplicationArguments applicationArguments;

    public LoginWidget() {
    }

    @Override
    public void createWidget() {
        Assert.notNull(loginDataProvider, "loginDataProvider is null");
        loginCommand = new LoginCommand();
        AxFormFactory formFactory = new AxFormFactory(loginDataProvider.getDataFactory()) {
            @Override
            public void initForm(AxFormBuilder builder) {
                builder.setStandardPreset1();
                Map context = new HashMap();
                ComboBoxBinding binding = (ComboBoxBinding) comboBoxBinder.bind(builder.getFormModel(), "connectionClass", context);
                binding.setRenderer(new ConnectionClassCellRenderer());
                binding.setSelectableItems(loginDataProvider.getConnectionRegistry().getClasses().toArray());

                builder.addBindingWithLabel("connectionClass", binding);
                builder.addPropertyWithLabel("name");
                builder.addPropertyWithLabel("url");
                //builder.addPropertyWithLabel("autoconnect");
                builder.addPropertyWithLabel("credentials.username");
                builder.addPropertyWithLabel("credentials.password");
                builder.addPropertyWithLabel("credentials.remember");
            }
        };

        AxTableDescription tableDescription = new AxTableDescription(loginDataProvider);
        tableDescription.col("name", SortOrder.ASCENDING).withRenderer(new ConnectionTableCellRenderer());
        tableDescription.col("url");
        tableDescription.col("credentials.username");

        setDataProvider(loginDataProvider);
        setMasterTable(tableDescription);
        //TODO Dialog
        setDetailEditor(formFactory.getWidget());

        super.createWidget();

        getMaster().getTableWidget().getTable().setRowHeight(25);
        getMaster().setDetailCommand(loginCommand);
        getMaster().addSelectionObserver(new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                if (arg instanceof ConnectionItem) {
                    loginCommand.setConnectionItem((ConnectionItem) arg);
                } else {
                    loginCommand.setConnectionItem(null);
                }
            }
        });

        getToolbar().addCommandFirst(loginCommand);
        getToolbar().addCommandSecond(new AxConnectionResetDefaultsCommand());

        doRefresh();
        doAutoConnect();
    }

    protected void doConnect() {
        Object selection = getMaster().getValue();
        if (selection instanceof ConnectionItem) {
            ConnectionItem connectionItem = (ConnectionItem) selection;
            SwingUtilities.invokeLater(new ConnectionThread(connectionItem));
        }
    }

    public void doAutoConnect() {
        if (applicationArguments != null && applicationArguments.get("connect") != null) {
            ConnectionItem connectionItem = loginDataProvider.findOne(applicationArguments.get("connect"));
            if (connectionItem != null) {
                SwingUtilities.invokeLater(new ConnectionThread(connectionItem));
            }
        } else {
            for (ConnectionItem connectionItem : loginDataProvider.findAuto()) {
                SwingUtilities.invokeLater(new ConnectionThread(connectionItem));
            }
        }
    }

    class ConnectionThread extends Thread {

        ConnectionItem connectionItem;

        ConnectionThread(ConnectionItem connectionItem) {
            super("ConnectionThread");
            this.connectionItem = connectionItem;
        }

        @Override
        public void run() {
            try {
                loginCommand.setConnectionItem(connectionItem);
                loginCommand.doExecuteCommand();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            new LoginAuthentication(applicationConfig, loginDataProvider, connectionItem).login();
        }
    }

    class ConnectionTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setFont(getFont().deriveFont(18f));
            return this;
        }
    }

    class ConnectionClassCellRenderer implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = new JLabel();
            try {
                String name = value == null ? "" : ((ConnectionClass) value).getName();
                label.setText(name);
            } catch (Exception ex) {
                log.error("Error", ex);
                label.setText("!!!");
            }
            return label;
        }
    }

    public class AxConnectionResetDefaultsCommand extends ActionCommand {

        public AxConnectionResetDefaultsCommand() {
            super("axConnectionResetDefaultsCommand");
        }

        @Override
        protected void doExecuteCommand() {
            loginDataProvider.resetDefaults();
            getMaster().doRefresh();
        }
    }
}
