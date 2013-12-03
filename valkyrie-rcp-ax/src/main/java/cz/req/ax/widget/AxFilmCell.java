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

package cz.req.ax.widget;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

@Deprecated
public class AxFilmCell implements TableCellRenderer {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private Class<? extends AxFilmPane> cellClass;

    public AxFilmCell(Class<? extends AxFilmPane> cellClass) {
        this.cellClass = cellClass;
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        try {
            AxFilmPane pane = cellClass.newInstance();
            pane.setValue(value);
            pane.setSelected(isSelected);
            JComponent control = pane.getControl();
            return control;
        } catch (Exception ex) {
            log.error("Instantiation error " + cellClass.getSimpleName(), ex);
            return new JLabel("?#@!");
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getCellEditorValue() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isCellEditable(EventObject anEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean stopCellEditing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void cancelCellEditing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addCellEditorListener(CellEditorListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeCellEditorListener(CellEditorListener l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
