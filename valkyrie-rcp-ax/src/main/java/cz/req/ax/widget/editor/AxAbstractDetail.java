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

import cz.req.ax.widget.AxDataWidget;
import cz.req.ax.widget.AxFormWidget;
import org.springframework.util.Assert;
import org.valkyriercp.component.Focussable;
import org.valkyriercp.form.AbstractForm;
import org.valkyriercp.form.FormGuard;
import org.valkyriercp.util.ValueMonitor;

/**
 * AxAbstractDetail
 *
 * @author Ondrej Burianek
 */
public abstract class AxAbstractDetail extends AxDataWidget implements Focussable {

    private boolean modeNew = false;
    ValueMonitor valueUpdateMonitor = new ValueMonitor();

    public ValueMonitor getValueUpdateMonitor() {
        return valueUpdateMonitor;
    }

    public boolean isModeNew() {
        return modeNew;
    }

    public void setModeNew(boolean modeNew) {
        this.modeNew = modeNew;
    }

    public void doAdd() {
        try {
            setDetailObject(getDataProvider().getDataFactory().newInstance());
        } catch (CloseCanceled ex) {
        }
        setModeNew(true);
        grabFocus();
    }

    public void doEdit(Object object) {
        try {
            setDetailObject(getValue());
        } catch (CloseCanceled ex) {
            log.error("Error setDetailObject", ex);
            return;
        }
        doEdit();
    }

    public void doEdit() {
        setModeNew(false);
        grabFocus();
    }

    public abstract void setDetailObject(Object object) throws CloseCanceled;

    public abstract AxFormWidget getFormWidget();

    protected AbstractForm getDetailForm() {
        Assert.notNull(getDataProvider(), "DataProvider is null at " + getClass().getSimpleName());
        Assert.notNull(getFormWidget(), "FormWidget is null at " + getClass().getSimpleName());
        if (!getDataProvider().supportsCreate() && !getDataProvider().supportsUpdate()) {
            getFormWidget().getForm().getFormModel().setReadOnly(true);
        }
        if (getSharedCommands() != null) {
            getFormWidget().getForm().addGuarded(getSharedCommands().getSharedCommit(), FormGuard.LIKE_COMMITCOMMAND);
        }
        return getFormWidget().getForm();
    }

    protected void doCommit() {
        getDetailForm().commit();
        Object object = getDetailForm().getFormObject();
        try {
            if (modeNew) {
                object = getDataProvider().create(object);
            } else {
                object = getDataProvider().update(object);
            }
            setValue(object);
            getValueUpdateMonitor().setValue(object);
        } catch (RuntimeException e) {
            Object failingObject = getDetailForm().getFormObject();
            doRevert();
            //MIG
//            RcpSupport.mapObjectOnFormModel(getDetailForm().getFormModel(), failingObject);
            throw e;
        }
    }

    protected void doRevert() {
        getDetailForm().getRevertCommand().execute();
    }

    public static class CloseCanceled extends Exception {
    }
}
