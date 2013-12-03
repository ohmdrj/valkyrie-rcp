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

import cz.req.ax.data.AxDataProvider;
import cz.req.ax.widget.editor.AxAbstractMaster;
import cz.thickset.utils.FlowPanel;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.widget.editor.provider.DataProviderEvent;

import javax.swing.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

@Deprecated
public class AxFilmWidget extends AxAbstractMaster {

    private PaneFactory factory;
    private FlowPanel container;

    public AxFilmWidget() {
    }

    public AxFilmWidget(AxDataProvider dataProvider) {
        super(dataProvider);
    }

    public void setPaneClass(Class<? extends AxFilmPane> paneClass) {
        this.factory = new PaneFactory(paneClass);
    }

    public void setPaneFactory(PaneFactory factory) {
        this.factory = factory;
    }

    @Override
    public void addSelectionObserver(Observer selectionObserver) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDetailCommand(AbstractCommand command) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createWidget() {
        container = new FlowPanel(2, true);
        JScrollPane scroll = new JScrollPane(container);
        throw new UnsupportedOperationException();
//        scroll.getVerticalScrollBar().setBlockIncrement(100);
//        simple(scroll);
    }

    @Override
    public void clearData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadData(Object data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearFilter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFilter(Object query) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refresh() {
        container.reset();
        List list = getDataProvider().getList();
        for (Object object : list) {
            try {
                AxFilmPane pane = factory.newInstance();
                pane.setValue(object);
                container.add(pane.getControl());
            } catch (Exception ex) {
                log.warn("Cannot add cell " + object, ex);
            }
        }
        container.revalidate();
        container.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        log.info("FilmUpdate");
        if (arg instanceof DataProviderEvent) {
            DataProviderEvent event = (DataProviderEvent) arg;
            try {
//                if (event.getEventType() == DataProviderEvent.EVENT_TYPE_NEW) {
//                    tableWidget.addRowObject(event.getNewEntity());
//                } else if (event.getEventType() == DataProviderEvent.EVENT_TYPE_UPDATE) {
//                    tableWidget.replaceRowObject(event.getOldEntity(), event.getNewEntity(), null);
//                } else if (event.getEventType() == DataProviderEvent.EVENT_TYPE_DELETE) {
//                    tableWidget.removeRowObject(event.getOldEntity());
//                }
//                tableWidget.getTable().requestFocusInWindow();
                doRefresh();
            } catch (Exception ex) {
                log.error("Error updating table", ex);
            }
        }

    }

    public static class PaneFactory {

        private Class<? extends AxFilmPane> paneClass;
        private Object[] parameters;

        public PaneFactory(Class<? extends AxFilmPane> paneClass, Object... parameters) {
            this.paneClass = paneClass;
            this.parameters = parameters;
        }

        public Class<? extends AxFilmPane> getPaneClass() {
            return paneClass;
        }

        public Object[] getParameters() {
            return parameters;
        }

        private AxFilmPane newInstance() {
            try {
                Class[] paramclasses = new Class[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    paramclasses[i] = parameters[i].getClass();
                }
                return paneClass.getConstructor(paramclasses).newInstance(parameters);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot factor instance for " + paneClass);
            }
        }
    }
}
