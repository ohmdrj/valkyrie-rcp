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

import cz.req.ax.components.AbstractBoxedField;

import javax.swing.*;

/**
 * TableLookupBox
 *
 * @author Ondrej Burianek
 */
public class TableLookupBox extends AbstractBoxedField<JComboBox> {

    public TableLookupBox() {
        super(new JComboBox());
    }

    public TableLookupBox(JComboBox component) {
        super(component);
    }

    public JButton getLookupButton() {
        return getButton();
    }

    public JComboBox getComboBox() {
        return getComponent();
    }

}
