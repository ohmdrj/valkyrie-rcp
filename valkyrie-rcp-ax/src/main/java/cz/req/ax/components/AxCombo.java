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

package cz.req.ax.components;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import cz.req.ax.data.RefreshListener;
import org.valkyriercp.list.BeanPropertyValueListRenderer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * @author Ondrej Burianek
 */
public class AxCombo<T extends Object> extends JComboBox implements RefreshListener {

    Collection<T> collection;
    EventList<T> list;

    public AxCombo(String propertyName) {
        this();
        setPropertyName(propertyName);
    }

    public AxCombo() {
        list = new BasicEventList();
        setModel(new EventComboBoxModel(list));
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fireItemSelected((T) getSelectedItem());
                } catch (ClassCastException ex) {
                    fireItemSelected(null);
                }
            }
        });
    }

    public void fireItemSelected(T selection) {
    }

    public void setPropertyName(String propertyName) {
        setRenderer(new BeanPropertyValueListRenderer(propertyName));
    }

    public void setDataCollection(Collection<T> collection) {
        this.collection = collection;
    }

    public Collection<T> getDataCollection() {
        return collection;
    }

    @Override
    public void refresh() {
        list.clear();
        Collection<T> data = getDataCollection();
        if (data != null) {
            list.addAll(data);
        }
    }
}
