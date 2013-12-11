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

package cz.req.ax.data.binding;

import cz.req.ax.AxApp;
import cz.req.ax.widget.editor.AxEditorWidget;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.Binder;
import org.valkyriercp.form.binding.Binding;

import javax.swing.*;
import java.util.Map;

/**
 * Customized ValkyrieRCP file
 *
 * @author Ondrej Burianek
 * @see org.valkyriercp.form.binding.swing.editor.LookupBinder
 */
//TODO @Deprecated?
@Configurable
public abstract class AxAbstractLookupBinder implements Binder {

    private int autoPopupDialog = AxAbstractLookupBinding.AUTOPOPUPDIALOG_NO_UNIQUE_MATCH;
    private boolean revertValueOnFocusLost = true;
    private String selectDialogId = AxAbstractLookupBinding.DEFAULT_SELECTDIALOG_ID;
    private String selectDialogCommandId = AxAbstractLookupBinding.DEFAULT_SELECTDIALOG_COMMAND_ID;
    private final String dataEditorId;
    private String dataEditorViewCommandId;
    private Object filter;
    private boolean enableViewCommand;
    private boolean loadDetailedObject = false;

    public AxAbstractLookupBinder(String dataEditorId) {
        this.dataEditorId = dataEditorId;
        enableViewCommand = false;
    }


    public boolean isLoadDetailedObject() {
        return loadDetailedObject;
    }

    public void setLoadDetailedObject(boolean loadDetailedObject) {
        this.loadDetailedObject = loadDetailedObject;
    }

    public void setAutoPopupDialog(int autoPopupDialog) {
        this.autoPopupDialog = autoPopupDialog;
    }

    public void setRevertValueOnFocusLost(boolean revertValueOnFocusLost) {
        this.revertValueOnFocusLost = revertValueOnFocusLost;
    }

    public void setSelectDialogId(String selectDialogId) {
        this.selectDialogId = selectDialogId;
    }

    public void setSelectDialogCommandId(String selectDialogCommandId) {
        this.selectDialogCommandId = selectDialogCommandId;
    }

    @Override
    public Binding bind(FormModel formModel, String formPropertyPath, Map context) {
        AxAbstractLookupBinding referableBinding = getLookupBinding(formModel, formPropertyPath, context);
        referableBinding.setAutoPopupdialog(getAutoPopupDialog());
        referableBinding.setRevertValueOnFocusLost(isRevertValueOnFocusLost());
        referableBinding.setSelectDialogCommandId(getSelectDialogCommandId());
        referableBinding.setSelectDialogId(getSelectDialogId());
        referableBinding.setDataEditorViewCommandId(dataEditorViewCommandId);
        referableBinding.setEnableViewCommand(enableViewCommand);
        referableBinding.setFilter(filter);
        referableBinding.setLoadDetailedObject(loadDetailedObject);
        return referableBinding;
    }

    protected abstract AxAbstractLookupBinding getLookupBinding(FormModel formModel, String formPropertyPath, Map context);

    @Override
    public Binding bind(JComponent control, FormModel formModel, String formPropertyPath, Map context) {
        throw new UnsupportedOperationException("This binder needs a special component that cannot be given");
    }

    protected int getAutoPopupDialog() {
        return autoPopupDialog;
    }

    protected AxEditorWidget getDataEditor() {
        //TODO VERIFY!!!
        Object dataEditor = AxApp.applicationConfig().applicationContext().getBean(dataEditorId);
        Assert.isInstanceOf(AxEditorWidget.class, dataEditor);
        return (AxEditorWidget) dataEditor;
    }

    protected boolean isRevertValueOnFocusLost() {
        return revertValueOnFocusLost;
    }

    protected String getSelectDialogCommandId() {
        return selectDialogCommandId;
    }

    protected String getSelectDialogId() {
        return selectDialogId;
    }

    public void setDataEditorViewCommandId(String dataEditorViewCommandId) {
        this.dataEditorViewCommandId = dataEditorViewCommandId;
    }

    public void setEnableViewCommand(boolean enableViewCommand) {
        this.enableViewCommand = enableViewCommand;
    }

    public void setFilter(Object filter) {
        this.filter = filter;
    }

    public String getDataEditorViewCommandId() {
        return dataEditorViewCommandId;
    }

    public Object getFilter() {
        return filter;
    }

    public boolean isEnableViewCommand() {
        return enableViewCommand;
    }
}
