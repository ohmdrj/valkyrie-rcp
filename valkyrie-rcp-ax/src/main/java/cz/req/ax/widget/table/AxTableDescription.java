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

package cz.req.ax.widget.table;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.impl.sort.ComparatorChain;
import cz.req.ax.AxApp;
import cz.req.ax.data.AxDataIdentity;
import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.DataFactory;
import org.valkyriercp.util.MessageConstants;
import org.valkyriercp.widget.table.PropertyColumn;
import org.valkyriercp.widget.table.PropertyColumnTableDescription;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * AxTableDescriptor
 *
 * @author Ondrej Burianek
 */
public class AxTableDescription extends PropertyColumnTableDescription {

    //TODO Description by resolver
    //MessageResolver messageResolver;
    List<Comparator<?>> columnComparators = new ArrayList<Comparator<?>>();
//    ArrayList<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();

    public AxTableDescription(Class dataClass) {
        super(AxDataIdentity.getClassSimpleName(dataClass), dataClass);
    }

    public AxTableDescription(DataFactory dataFactory) {
        super(dataFactory.getDataId(), dataFactory.getDataClass());
    }

    //TODO Lazy DataProvider dependency + Fix NPE
    public AxTableDescription(AxDataProvider dataProvider) {
        this(dataProvider.getDataFactory());
    }

    public AxTableDescription add(String... columns) {
        for (String c : columns) {
            add(c);
        }
        return this;
    }

    public AxTableDescription add(String column) {
        col(column, null, null);
        return this;
    }

    public AxTableDescription add(String column, int width) {
        col(column, width, null);
        return this;
    }

    public AxTableDescription add(String column, SortOrder order) {
        col(column, null, order);
        return this;
    }

    public AxTableDescription add(String column, int width, SortOrder order) {
        col(column, width, order);
        return this;
    }

    public PropertyColumn col(String propertyName) {
        return col(propertyName, null, null);
    }

    public PropertyColumn col(String propertyName, Integer width) {
        return col(propertyName, width, null);
    }

    public PropertyColumn col(String propertyName, SortOrder order) {
        return col(propertyName, null, order);
    }

    public PropertyColumn col(String propertyName, Integer width, SortOrder order) {
        PropertyColumn col = super.addPropertyColumn(propertyName);
        if (width != null) {
            col.setResizable(false);
            col.setMinWidth(width);
            col.setMaxWidth((int) (width * 1.5));
        }
        if (order != null) {
            Comparator comparator = GlazedLists.beanPropertyComparator(getDataType(), propertyName);
            if (SortOrder.ASCENDING.equals(order)) {
                columnComparators.add(comparator);
            } else if (SortOrder.DESCENDING.equals(order)) {
                columnComparators.add(GlazedLists.reverseComparator(comparator));
            }
        }
        return col;
    }

    @Override
    public Object getValue(Object rowObject, int propertyIndex) {
        try {
            return super.getValue(rowObject, propertyIndex);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void setValue(Object rowObject, int propertyIndex, Object newValue) {
        try {
            super.setValue(rowObject, propertyIndex, newValue);
        } catch (Exception ex) {
        }
    }

    @Override
    public PropertyColumn addPropertyColumn(String propertyName, Class propertyType) {
        PropertyColumn column = super.addPropertyColumn(propertyName, propertyType);
        String dataId = AxDataIdentity.getClassSimpleName(getDataType());
        String header = AxApp.applicationConfig().messageResolver().getMessage(dataId, propertyName, MessageConstants.HEADER);
        if (propertyName.equals(header)) {
            column.setHeader(AxApp.applicationConfig().messageResolver().getMessage(dataId, propertyName, MessageConstants.LABEL));
//            column.setHeaderKeys(messageResolver.getMessage(dataFactory.getDataId(), propertyName, MessageConstants.LABEL));
        } else {
            column.setHeader(header);
        }
        return column;
    }

    @Override
    public Comparator<?> getDefaultComparator() {
        if (columnComparators.size() > 0) {
            return new ComparatorChain(columnComparators);
        } else {
            return null;
            //return super.getDefaultComparator();
        }
    }
}
