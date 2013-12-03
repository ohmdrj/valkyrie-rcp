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

package cz.req.ax.widget.editor;

import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.DataFilterAware;
import cz.req.ax.data.DataProviderAware;
import cz.req.ax.data.DataSelectionAware;
import cz.req.ax.widget.AxDataWidget;
import cz.req.ax.widget.AxMasterCommandAdapter;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.widget.editor.provider.DataProvider;
import org.valkyriercp.widget.editor.provider.DataProviderListener;

import java.util.Observer;

/**
 * AxAbstractMaster
 *
 * @author Ondrej Burianek
 */
public abstract class AxAbstractMaster extends AxDataWidget
        implements DataProviderAware, DataSelectionAware, DataFilterAware, DataProviderListener {

    AxMasterCommandAdapter commandAdapter = new AxMasterCommandAdapter();

    public AxAbstractMaster(AxDataProvider dataProvider) {
        super(dataProvider);
    }

    public AxAbstractMaster() {
    }

    public abstract void addSelectionObserver(Observer selectionObserver);

    public abstract void loadData(Object data);

    public abstract void clearData();

    public boolean hasSelection() {
        return getSelection() != null;
    }

    @Override
    public void clearSelection() {
        setSelection(null, null);
    }

    @Override
    public Object getSelection() {
        return getValue();
    }

    public Object[] getSelections() {
        Object value = getValue();
        if (value == null) {
            return null;
        }
        if (value instanceof Object[]) {
            return (Object[]) value;
        }
        return new Object[]{value};
    }

    @Override
    public Object setSelection(Object selection) {
        return setSelection(selection, null);
    }

    @Override
    public Object setSelection(Object selection, Observer observer) {
        // return when selecting multiple rows
        if (selection instanceof Object[]) {
            return null;
        }
        doSelect(selection, observer);
        return selection;

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
    }

    public void doSelect(Object selection, Observer observer) {
        setValue(selection);
    }

    public AxMasterCommandAdapter getCommandAdapter() {
        return commandAdapter;
    }

    public void setDetailCommand(AbstractCommand command) {
        commandAdapter.setTargetCommand(command);
    }

    //    @Override
//    public void setSharedCommands(SharedCommandsAware sharedCommands) {
//        super.setSharedCommands(sharedCommands);
//        setDetailCommand(sharedCommands.getSharedDetail());
//    }
    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        if (getDataProvider() != null) {
            getDataProvider().addDataProviderListener(this);
            if (getDataProvider().getRefreshPolicy() == DataProvider.RefreshPolicy.NEVER) {
                return;
            }
        }
        doRefresh();
//        refreshThread.onAboutToShow();
    }

    @Override
    public void onAboutToHide() {
        super.onAboutToHide();
        if (getDataProvider() != null) {
            getDataProvider().removeDataProviderListener(this);
            if (getDataProvider().getRefreshPolicy() == DataProvider.RefreshPolicy.ALLWAYS) {
                clearData();
            }
        }
//        refreshThread.onAboutToHide();
    }
}
