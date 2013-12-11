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
package cz.req.ax.widget;

import cz.req.ax.data.*;
import cz.req.ax.support.SharedCommandsAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.valkyriercp.binding.value.ValueModel;
import org.valkyriercp.binding.value.support.ValueHolder;
import org.valkyriercp.widget.Widget;

import java.beans.PropertyChangeListener;

/**
 * AxDataWidget
 *
 * @author Ondrej Burianek
 */
public abstract class AxDataWidget extends AxWidget
        implements DataProviderAware, RefreshSupport, RefreshListener, ValueModel, InitializingBean {

    private SharedCommandsAware sharedCommands;
    private DataFactory dataFactory;
    private AxDataProvider dataProvider;
    private ValueModel valueModel;
    private Object filterCriteria;
    private RefreshAdapter refreshAdapter;

    public AxDataWidget() {
        refreshAdapter = new RefreshAdapter();
        refreshAdapter.addRefreshListener(this);
    }

    public AxDataWidget(DataFactory dataFactory) {
        this();
        this.dataFactory = dataFactory;
    }

    public AxDataWidget(AxDataProvider dataProvider) {
        this();
        this.dataProvider = dataProvider;
    }

    public AxDataWidget(AxDataProvider dataProvider, SharedCommandsAware sharedCommands) {
        this();
        this.dataProvider = dataProvider;
        this.sharedCommands = sharedCommands;
    }

    public Object getFilterCriteria() {
        return filterCriteria;
    }

    public void setFilterCriteria(Object filterCriteria) {
        this.filterCriteria = filterCriteria;

    }

    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        refreshAdapter.fireRefresh();
    }

    @Override
    public void doRefresh() {
        refreshAdapter.fireRefresh();
    }

    @Override
    public void refresh() {
        log.debug("Refresh support " + getClass().getSimpleName() + " not overriden!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public AxDataProvider getDataProvider() {
        if (dataProvider == null) {
            log.warn("Null data provider at " + getClass().getSimpleName() + "!");
        }
        return dataProvider;
    }

    @Override
    public void setDataProvider(AxDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        setWidgetDataProvider(this, dataProvider);
    }

    public DataFactory getDataFactory() {
        return dataProvider == null ? dataFactory : dataProvider.getDataFactory();
    }

    public void setDataFactory(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
        throw new UnsupportedOperationException();
    }

    public void setDataProviderSupport(boolean supportsCreate, boolean supportsUpdate, boolean supportsDelete, boolean supportsDetail) {
        Assert.notNull(dataProvider, "DataProvider required!");
        dataProvider.setSupport(supportsCreate, supportsUpdate, supportsDelete, supportsDetail);
    }

    public SharedCommandsAware getSharedCommands() {
        return sharedCommands;
    }

    public void setSharedCommands(SharedCommandsAware sharedCommands) {
        this.sharedCommands = sharedCommands;
        setWidgetSharedCommands(this, sharedCommands);
    }

    //TODO delWidget
    @Override
    protected void addWidget(Widget widget) {
        super.addWidget(widget);
        //TODO data factory?
        String info = getClass().getSimpleName() + ":" + getId() + " while adding " + widget.getClass().getSimpleName() + ":" + widget.getId();
        if (dataProvider == null) {
//            log.warn("DataProvider not initialized " + info);
        } else {
            setWidgetDataProvider(widget, dataProvider);
        }
        if (sharedCommands == null) {
//            log.warn("SharedCommands not initialized " + info);
        } else {
            setWidgetSharedCommands(widget, sharedCommands);
        }
    }

    private void setWidgetDataProvider(Widget widget, AxDataProvider dataProvider) {
        if (widget == null || this.equals(widget)) {
            for (Widget w : getWidgets()) {
                setWidgetDataProvider(w, dataProvider);
            }
        } else if (widget instanceof AxDataWidget) {
            ((AxDataWidget) widget).setDataProvider(dataProvider);
        }
    }

    private void setWidgetSharedCommands(Widget widget, SharedCommandsAware commandsAware) {
        if (widget == null || this.equals(widget)) {
            for (Widget w : getWidgets()) {
                setWidgetSharedCommands(w, commandsAware);
            }
        } else if (widget instanceof AxDataWidget) {
            ((AxDataWidget) widget).setSharedCommands(commandsAware);
        }
    }

    public ValueModel getValueModel() {
        if (valueModel == null) {
            valueModel = new ValueHolder();
        }
        return valueModel;
    }

    public void setValueModel(ValueModel valueModel) {
        this.valueModel = valueModel;
    }

    public RefreshAdapter getRefreshAdapter() {
        return refreshAdapter;
    }

    @Override
    public void setValueSilently(Object newValue, PropertyChangeListener listenerToSkip) {
        logger.debug("setValue ~> " + getClass().getSimpleName() + ": " + newValue);
        getValueModel().setValueSilently(newValue, listenerToSkip);
    }

    @Override
    public void removeValueChangeListener(PropertyChangeListener listener) {
        getValueModel().removeValueChangeListener(listener);
    }

    @Override
    public Object getValue() {
        return getValueModel().getValue();
    }

    @Override
    public void setValue(Object newValue) {
        logger.debug("setValue => " + getClass().getSimpleName() + ": " + newValue);
        getValueModel().setValue(newValue);
    }

    public Object[] getValueArray() {
        Object value = getValueModel().getValue();
        if (value instanceof Object[]) {
            return (Object[]) value;
        }
        return new Object[]{value};
    }

    @Override
    public void addValueChangeListener(PropertyChangeListener listener) {
        getValueModel().addValueChangeListener(listener);
    }
}
