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

import cz.req.ax.support.DetailSupport;
import cz.req.ax.widget.AxDataWidget;
import org.springframework.util.Assert;
import org.valkyriercp.widget.editor.provider.MaximumRowsExceededException;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * AxAbstractEditor
 *
 * @author Ondrej Burianek
 */
public class AxAbstractEditor<M extends AxAbstractMaster, D extends AxAbstractDetail> extends AxDataWidget
        implements DetailSupport {

    AxEditorSharedCommands editorSharedCommands;
    protected Observer selectionObserver;
    protected AxEditorToolbar toolbar;
    protected M master;
    protected D detail;

    public AxAbstractEditor() {
        editorSharedCommands = new AxEditorSharedCommands();
        selectionObserver = new Observer() {

            @Override
            public void update(Observable o, Object newObject) {
                setSelection(newObject);
            }
        };
    }

    //TODO Rename Open??
    @Deprecated
    @Override
    public void doDetail() {
    }

    @Override
    public void createWidget() {
//        Assert.notNull(AxApp.applicationConfig(), "applicationConfig is null... not AutoWired!");
        if (getDataProvider() == null) {
            log.warn("Missing DataProvider!!!");
        }

        editorSharedCommands.setDataProvider(getDataProvider());
        //editorSharedCommands.setCommandConfigurer(AxApp.applicationConfig().commandConfigurer());
        editorSharedCommands.setMaster(master);
        editorSharedCommands.setDetail(detail);
        setSharedCommands(editorSharedCommands);

        toolbar = new AxEditorToolbar();
        toolbar.setDataProvider(getDataProvider());
        toolbar.setSharedCommands(editorSharedCommands);
//        TODO Remove hard dependency
        addRow(toolbar);
        addFull(master);
        if (detail != null) {
            addRow(detail);
        }
        //TODO Detektive, je toto nutne?
        setSelection(null);
    }

    public Object getCriteriaFromString(String textFieldValue) {
        return textFieldValue;
    }

    public void setSelection(Object object) {
        if (getDetail() == null) {
            return;
        }
        try {
            getDetail().setDetailObject(object);
            getMaster().setSelection(object, null);
        } catch (AxAbstractDetail.CloseCanceled ex) {
            log.warn("canceled!");
            getMaster().setSelection(getMaster().getSelection(), selectionObserver);
        }
    }

    public Object setSelectedSearch(Object criteria) {
        // filterField
        master.clearFilter();

//        if (criteria == null) {
//            if (getDataProvider().supportsFiltering()) {
//                getFilterForm().getNewFormObjectCommand().execute();
//            }
//            executeFilter();
//            return null;
//        }
        List resultList = getList(criteria);
//        if (getDataProvider().supportsFiltering()) {
//            if ((resultList == null) || (resultList.size() > 0)) // fill in referable
//            {
//                getFilterForm().setFormObject(criteria);
//            } else { // empty filterForm and execute
//                getFilterForm().getNewFormObjectCommand().execute();
//                executeFilter();
//            }
//        }
        if (resultList != null && resultList.size() == 1) {
            // return the detailObject
            //            return loadEntityDetails(resultList.get(0));
            return getDataProvider().getSimpleObject(resultList.get(0));
        }
        return resultList;
    }

    protected List getList(Object criteria) {
//        if (getDataProvider().supportsBaseCriteria()) {
//            getDataProvider().setBaseCriteria(getBaseCriteria());
//        }
        try {
            List dataSet = getDataProvider().getList(criteria);
            getMaster().loadData(dataSet);
//            setMessage(null);
            return dataSet;
        } catch (MaximumRowsExceededException mre) {
            getMaster().clearData();
//            setMessage(new DefaultMessage(getMessage("MaximumRowsExceededException.notice", new Object[]{mre.getNumberOfRows(), mre.getMaxRows()}), Severity.WARNING));
//            if (getToggleFilterCommand() != null) {
//                getToggleFilterCommand().doShow();
//            }
            return null;
        }
    }

    @Override
    public void setFilterCriteria(Object filterCriteria) {
        super.setFilterCriteria(filterCriteria);
        if (getMaster() != null) {
            getMaster().setFilterCriteria(filterCriteria);
        }
    }

    @Override
    public void onAboutToHide() {
        setSelection(null);
        super.onAboutToHide();
    }

    @Override
    public AxEditorSharedCommands getSharedCommands() {
        return (AxEditorSharedCommands) super.getSharedCommands();
    }

    public M getMaster() {
        return master;
    }

    public void setMaster(M master) {
        Assert.notNull(master);
        Assert.notNull(selectionObserver);
        this.master = master;
        addWidget(master);
        getRefreshAdapter().addRefreshListener(master);
        master.addSelectionObserver(selectionObserver);
    }

    public D getDetail() {
        return detail;
    }

    public void setDetail(D detail) {
        this.detail = detail;
        addWidget(detail);
    }

    public AxEditorToolbar getToolbar() {
        return toolbar;
    }

    @Deprecated
    public void setToolbar(AxEditorToolbar toolbar) {
        throw new UnsupportedOperationException();
//        master.addSelectionObserver(toolbar);
    }
}
