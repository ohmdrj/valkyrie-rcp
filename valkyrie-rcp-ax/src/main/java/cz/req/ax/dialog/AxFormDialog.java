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

package cz.req.ax.dialog;

import cz.req.ax.AxApp;
import cz.req.ax.data.RefreshSupport;
import org.springframework.util.Assert;
import org.valkyriercp.dialog.FormBackedDialogPage;
import org.valkyriercp.form.AbstractForm;

/**
 * @author Ondrej Burianek
 */
public class AxFormDialog<T extends Object> extends AxDialog implements RefreshSupport {

    String messageSuccess, messageFailure;
    AbstractForm form;

    public AxFormDialog() {
    }

    public AxFormDialog(AbstractForm form) {
        setForm(form);
    }

    public T getObject() {
        return (T) getForm().getFormObject();
    }

    //TODO Rename?
    public void setObject(T object) {
        getForm().setFormObject(object);
    }

    public AbstractForm getForm() {
        Assert.notNull(form, "Form is null!");
        return form;
    }

    public final void setForm(AbstractForm form) {
        this.form = form;
        setDialogPage(new FormBackedDialogPage(form));
    }

    private void showInternalMessage(String message) {
        if (message != null) {
            AxApp.applicationConfig().dialogFactory().showConfirmationDialog(message);
        }
    }

    public boolean doAction(T object) throws Exception {
        return true;
    }

    @Override
    protected boolean onFinish() {
        try {
            getForm().commit();
            Object result = getForm().getFormObject();
            boolean action = doAction((T) result);
            if (action) {
                showInternalMessage(messageSuccess);
            }
            return action;
        } catch (Exception ex) {
            ex.printStackTrace();
//            log.error("Dialog action error", ex);
            showInternalMessage(messageFailure);
            return false;
        }
    }

    @Override
    protected void onAboutToShow() {
        doRefresh();
    }

    @Override
    public void doRefresh() {
    }
}
