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

package cz.req.ax.widget.table;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TableLookupBox
 *
 * @author Ondrej Burianek
 */
public class TableLookupBox extends JPanel {

    private JComboBox comboBox;
    private JButton lookupButton;

    public TableLookupBox() {
        this(new JComboBox());
    }

    public TableLookupBox(JComboBox c) {
        super(new BorderLayout(0, 0));
        comboBox = c;
        int add = 1;
        comboBox.setMinimumSize(new Dimension(comboBox.getMinimumSize().width, comboBox.getMinimumSize().height + add));
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().height, comboBox.getPreferredSize().height + add));
        lookupButton = new JButton();
        lookupButton.setIcon(UIManager.getIcon("LookupBox.arrowIcon"));
        lookupButton.setMinimumSize(new Dimension(26, comboBox.getMinimumSize().height + add));
        lookupButton.setPreferredSize(new Dimension(26, comboBox.getPreferredSize().height + add));
        add(comboBox, BorderLayout.CENTER);
        add(lookupButton, BorderLayout.EAST);

        comboBox.addPropertyChangeListener("enabled", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                lookupButton.setEnabled((Boolean) evt.getNewValue());
            }
        });
    }

    public JButton getLookupButton() {
        return lookupButton;
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public void removeLookupListener(ActionListener l) {
        lookupButton.removeActionListener(l);
    }

    public void addLookupListener(ActionListener l) {
        lookupButton.addActionListener(l);
    }
}
