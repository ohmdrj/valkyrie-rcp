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

import com.jidesoft.utils.SwingWorker;
import cz.req.ax.data.DataCollectionAware;
import cz.req.ax.widget.editor.AxAbstractMaster;
import org.springframework.util.Assert;
import org.valkyriercp.application.support.StatusBarProgressMonitor;
import org.valkyriercp.widget.editor.provider.DataProviderEvent;
import org.valkyriercp.widget.editor.provider.DataProviderListener;
import org.valkyriercp.widget.table.glazedlists.GlazedListTableWidget;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * AxTableWidget
 *
 * @author Ondrej Burianek
 */
public class AxTableWidget extends AxAbstractMaster implements DataCollectionAware, DataProviderListener {

    private GlazedListTableWidget tableWidget;
    private AxTableDescription tableDescription;
    private DataWorker dataWorker;
    private boolean threaded = true;
    private boolean searchField = false;
    private Observer valueObserver = new Observer() {

        @Override
        public void update(Observable o, Object arg) {
            setValue(arg);
        }
    };
//    private Thread autoRefresh = new Thread("AxTableWidget refresh") {

    public AxTableWidget() {
    }

    public AxTableWidget(AxTableDescription tableDescription) {
        setTableDescription(tableDescription);
    }

    public AxTableDescription getTableDescription() {
        if (tableDescription == null) {
            Assert.notNull(getDataFactory(), "DataFactory not initialized");
            tableDescription = new AxTableDescription(getDataFactory());
        }
        return tableDescription;
    }

    public void setTableDescription(String... columns) {
        AxTableDescription td = new AxTableDescription(getDataProvider());
        for (String string : columns) {
            td.add(string);
        }
        setTableDescription(td);
    }

    public void setTableDescription(AxTableDescription tableDescription) {
        this.tableDescription = tableDescription;
    }

    public GlazedListTableWidget getTableWidget() {
        if (tableWidget == null) {
            Assert.notNull(tableDescription, "TableDescription is null");
            tableWidget = new GlazedListTableWidget(null, tableDescription, true);
            tableWidget.addSelectionObserver(valueObserver);
            tableWidget.getTable().addMouseListener(getCommandAdapter());
            tableWidget.getTable().addKeyListener(getCommandAdapter());
        }
        return tableWidget;
    }

    public void setSearchField(boolean searchField) {
        this.searchField = searchField;
    }

    @Override
    public void loadData(Object data) {
        if (data instanceof Collection) {
            setDataCollection((Collection) data);
        } else {
            clearData();
        }
    }

    @Override
    public void clearData() {
        setDataCollection(Collections.EMPTY_LIST);
    }

    @Override
    public void clearFilter() {
        if (getTableWidget().getTextFilterField() != null) {
            getTableWidget().getTextFilterField().setText("");
        }
    }

    @Override
    public void setFilter(Object query) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDataCollection(Collection data) {
        clearSelection();
        if (data == null || data.isEmpty()) {
            getTableWidget().setRows(Collections.EMPTY_LIST);
        } else {
//            Comparator comparator = getTableDescription().getDefaultComparator();
//            if (comparator == null) {
            getTableWidget().setRows(data);
//            } else {
//                ArrayList list = new ArrayList(data);
//                Collections.sort(list, comparator);
//                getTableWidget().setRows(list);
//            }
        }
    }

    @Override
    public void createWidget() {
        if (searchField) {
            addRow(getTableWidget().getTextFilterField());
        }
        addFull(getTableWidget());
    }

    @Override
    public void refresh() {
        if (dataWorker == null) {
//            //DEBUG
//            threaded = false;
            if (threaded) {
                if (getProgressMonitor() != null) {
                    getProgressMonitor().taskStarted("Loading...", StatusBarProgressMonitor.UNKNOWN);
                }
                dataWorker = new DataWorker(getFilterCriteria());
                dataWorker.execute();
            } else {
                setDataCollection(getDataProvider().getList(getFilterCriteria()));
            }
        }
    }

    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        getTableWidget().getTable().requestFocusInWindow();
    }

    @Override
    public void addSelectionObserver(Observer selectionObserver) {
        getTableWidget().addSelectionObserver(selectionObserver);
    }

    //    public Object setSelection(Object selection, Observer observer) {
//        // return when selecting multiple rows
//        if (selection instanceof Object[]) {
//            return null;
//        }
//        int ret = doSelect(selection, observer);
//        if (selection == null || hasSelection()) {
//            return ret;
//        }
//        ret = doSelect(selection, observer);
//        if (hasSelection()) {
//            return ret;
//        }
//        getTableWidget().getTextFilterField().setText("");
//        return doSelect(selection, observer);
//    }
    private class DataWorker extends SwingWorker<List<Object>, String> {

        private Object criteria;

        DataWorker(Object criteria) {
            this.criteria = criteria;
        }

        @Override
        protected List<Object> doInBackground() throws Exception {
            try {
                return getDataProvider().getList(criteria);
            } catch (Exception ex) {
                log.error("Error listing data from provider", ex);
//                return new ArrayList<Object>();
                throw ex;
            }
        }

        @Override
        protected void done() {
            try {
                setDataCollection(get());
            } catch (InterruptedException ex) {
                log.warn("Interrupted", ex);
            } catch (ExecutionException ex) {
                log.error("Error execution", ex);
                ex.printStackTrace();
            } catch (ConcurrentModificationException ex) {
                log.error("Error concurrent", ex);
                ex.printStackTrace();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                if (getProgressMonitor() != null) {
                    getProgressMonitor().done();
                }
                dataWorker = null;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        log.info("TableUpdate");
        if (arg instanceof DataProviderEvent) {
            DataProviderEvent event = (DataProviderEvent) arg;
            try {
                if (event.getEventType() == DataProviderEvent.EVENT_TYPE_NEW) {
                    getTableWidget().addRowObject(event.getNewEntity());
                } else if (event.getEventType() == DataProviderEvent.EVENT_TYPE_UPDATE) {
                    getTableWidget().replaceRowObject(event.getOldEntity(), event.getNewEntity(), null);
                } else if (event.getEventType() == DataProviderEvent.EVENT_TYPE_DELETE) {
                    getTableWidget().removeRowObject(event.getOldEntity());
                }
                getTableWidget().getTable().requestFocusInWindow();
            } catch (Exception ex) {
                log.error("Error updating table", ex);
            }
        }

    }
}
