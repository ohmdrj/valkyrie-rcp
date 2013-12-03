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

package cz.req.ax.options;

import cz.req.ax.AxApp;
import cz.req.ax.components.IntegerSlider;
import cz.thickset.utils.swing.FormFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.valkyriercp.command.support.ActionCommand;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Ondrej Burianek
 */
public class AxOptionsCommand extends ActionCommand {

    @Autowired
    AxApplicationOptions options;
    Map<OptionItem, JComponent> map;

    public AxOptionsCommand() {
        super("optionsCommand");
    }

    protected JComponent load(OptionItem item) {
        JComponent comp = null;
        if (OptionType.String.equals(item.getType())) {
            if (item.getSelections() == null || item.getSelections().isEmpty()) {
                comp = new JTextField((String) item.getValue());
            } else {
                JComboBox comboBox = new JComboBox(item.getSelections().toArray());
                comboBox.setSelectedItem(item.getValue());
                comp = comboBox;
            }
        } else if (OptionType.Integer.equals(item.getType())) {
            IntegerSlider slider = new IntegerSlider();
            slider.setMinimum(0);
            slider.setMaximum(100);
            slider.setValue((Integer) item.getValue());
            comp = slider;
        } else if (OptionType.Boolean.equals(item.getType())) {
            JCheckBox check = new JCheckBox("Zapnuto");
            check.setSelected((Boolean) item.getValue());
            comp = check;
        } else if (OptionType.Directory.equals(item.getType())) {
            FileFinder finder = new FileFinder();
            finder.setFile((File) item.getValue());
            comp = finder;
        } else {
            throw new UnsupportedOperationException();
        }
        map.put(item, comp);
        return comp;
    }

    protected void save(OptionItem item) {
        JComponent comp = map.get(item);
        Object value;
        if (OptionType.String.equals(item.getType())) {
            if (comp instanceof JTextField) {
                value = ((JTextField) comp).getText();
            } else {
                value = ((JComboBox) comp).getSelectedItem();
            }
        } else if (OptionType.Integer.equals(item.getType())) {
            value = ((IntegerSlider) comp).getValue();
        } else if (OptionType.Boolean.equals(item.getType())) {
            value = Boolean.valueOf(((JCheckBox) comp).isSelected());
        } else if (OptionType.Directory.equals(item.getType())) {
            value = ((FileFinder) comp).getFile();
        } else {
            throw new UnsupportedOperationException();
        }
        item.setValue(value);
    }

    protected String getMessageLabel(String name) {
        return AxApp.applicationConfig().messageSource().getMessage("options." + name + ".label", null, Locale.getDefault());
    }

    @Override
    protected void doExecuteCommand() {
        if (options.getOptions().isEmpty()) {
            return;
        }
        map = new HashMap<OptionItem, JComponent>();
        //MIG
//        final JFrame frame = Application.instance().getActiveWindow().getControl();
        final JFrame frame = null;
        final JDialog dialog = new JDialog(frame, "Options");
        FormFactory formFactory = new FormFactory("100px,200px:grow", "p");
        for (OptionGroup group : options.getOptions()) {
            JLabel title = new JLabel(getMessageLabel(group.getName()));
            title.setFont(title.getFont().deriveFont(Font.BOLD));
            title.setBackground(Color.WHITE);
            title.setOpaque(true);
            title.setBorder(new EmptyBorder(2, 4, 2, 4));
            formFactory.addRowExt(title);
            formFactory.addGap();
            for (OptionItem item : group.getOptions()) {
                JLabel label = new JLabel(getMessageLabel(group.getName() + "." + item.getName()));
                JComponent comp = load(item);
                formFactory.addRowExt(label, comp);
            }
            formFactory.addGap();
        }

        ActionCommand actionSave = new ActionCommand("optionsSaveCommnad") {

            @Override
            protected void doExecuteCommand() {
                for (OptionGroup group : options.getOptions()) {
                    for (OptionItem item : group.getOptions()) {
                        save(item);
                    }
                }
                options.writeSettings();
                dialog.setVisible(false);
            }
        };
        AxApp.applicationConfig().commandConfigurer().configure(actionSave);
        formFactory.getConstraints().next2();
        formFactory.addRowExtColapse(null, actionSave.createButton());

        dialog.setLayout(new BorderLayout());
        dialog.add(formFactory.getPanel(), BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}
