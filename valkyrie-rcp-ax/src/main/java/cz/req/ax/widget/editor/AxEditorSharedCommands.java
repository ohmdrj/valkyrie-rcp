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

package cz.req.ax.widget.editor;

import cz.req.ax.AxApp;
import cz.req.ax.data.AxDataProvider;
import cz.req.ax.support.SharedCommandsSupport;
import org.springframework.util.Assert;

import javax.swing.*;

/**
 * @author Ondrej Burianek
 */
public class AxEditorSharedCommands extends SharedCommandsSupport {

    private AxDataProvider dataProvider;
    private AxAbstractMaster master;
    private AxAbstractDetail detail;

    public AxDataProvider getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(AxDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setMaster(AxAbstractMaster master) {
        this.master = master;
        if (master != null) {
            //TODO Move
            master.setDetailCommand(getSharedDetail());
        }
    }

    public void setDetail(AxAbstractDetail detail) {
        this.detail = detail;
    }

    @Override
    public void doRefresh() {
        master.doRefresh();
    }

    @Override
    public void doSearch() {
//        master.getGlazedTable().getTextFilterField().requestFocusInWindow();
    }

    @Override
    public void doAdd() {
//        master.clearSelection();
        Assert.notNull(detail, "detail is null");
        detail.doAdd();
    }

    @Override
    public void doEdit() {
        Assert.notNull(detail, "detail is null");
        detail.doEdit();
    }

    @Override
    public void doRemove() {
        Assert.notNull(dataProvider, "dataProvider is null");
        Assert.notNull(master, "master is null");
        int answer = AxApp.applicationConfig().dialogFactory().showWarningDialog("remove.confirmation", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            Object value = master.getValue();
            if (value != null && value instanceof Object[]) {
                for (Object object : (Object[]) value) {
                    dataProvider.delete(object);
                }
            } else if (value != null) {
                dataProvider.delete(value);
            }
        }
    }

    //TODO Rename Open??
    @Override
    public void doDetail() {
        if (master.getSelection() == null) {
            return;
        }
        //TODO Open detail?
    }

    @Override
    public void doCommit() {
        detail.doCommit();
    }
}
