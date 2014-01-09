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

import cz.req.ax.AxApp;
import cz.req.ax.widget.AxFormWidget;
import cz.thickset.utils.SwingUtils;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.component.Focussable;
import org.valkyriercp.form.FormGuard;

import javax.swing.*;
import java.awt.*;

/**
 * AxDetailEditor
 *
 * @author Ondrej Burianek
 */
public class AxDetailEditor extends AxAbstractDetail implements Focussable {

    public static final String UNSAVEDCHANGES_WARNING_ID = "unsavedchanges.warning";
    public static final String UNSAVEDCHANGES_UNCOMMITTABLE_WARNING_ID = "unsavedchanges.uncommittable.warning";
    public static final String WRAPER_CREATE = "wrapperCreateCommand";
    public static final String WRAPER_UPDATE = "wrapperUpdateCommand";
    public static final String WRAPER_REVERT = "wrapperRevertCommand";
    private AxFormWidget formWidget;
    private ActionCommand createCommand;
    private ActionCommand updateCommand;
    private ActionCommand revertCommand;

    //    public AxDetailEditor(AxDataProvider dataProvider, AxFormWidget formWidget) {
//        super(dataProvider);
//        this.formWidget = formWidget;
//    }
    public void setFormWidget(AxFormWidget formWidget) {
        delWidget(this.formWidget);
        this.formWidget = formWidget;
        addWidget(formWidget);
    }

    @Override
    public AxFormWidget getFormWidget() {
        return formWidget;
    }

    @Override
    public void createWidget() {
        JPanel buttonBar = new JPanel(new GridBagLayout());
        buttonBar.add(new JPanel(), SwingUtils.gbc(1, 1, 1, 1, true));
        buttonBar.add(getCreateCommand().createButton(), SwingUtils.gbc(2, 1, 4));
        buttonBar.add(getUpdateCommand().createButton(), SwingUtils.gbc(3, 1, 4));
        buttonBar.add(getRevertCommand().createButton(), SwingUtils.gbc(4, 1, 4));

        addFull(formWidget);
        addRow(buttonBar);
        setModeNew(false);
    }

    @Override
    public void setModeNew(boolean modeNew) {
        super.setModeNew(modeNew);
        getCreateCommand().setEnabled(false);
        getUpdateCommand().setEnabled(false);
        getRevertCommand().setEnabled(false);
        getCreateCommand().setVisible(modeNew);
        getUpdateCommand().setVisible(!modeNew);
    }

    //    @Override
//    public void setValue(Object newValue) {
//        try {
//            setDetailObject(newValue);
//            super.setValue(newValue);
//        } catch (CloseCanceled ex) {
//        }
//    }
    @Override
    public void setDetailObject(Object object) throws CloseCanceled {
        if (object == null) {
            log.info("detail null");
        } else if (object instanceof Object[]) {
            log.info("detail array");
        } else {
            log.info("detail object");

        }
        if (!canClose()) {
            throw new CloseCanceled();
        }
        if (object == null || object instanceof Object[]) {
            getDetailForm().setEnabled(false);
            getUpdateCommand().setEnabled(false);
        } else {
            getDetailForm().setEnabled(true);
            Object detailObject = object == null ? null : getDataProvider().getDetailObject(object, false);
            getDetailForm().setFormObject(detailObject);
            getUpdateCommand().setEnabled(true && getDataProvider().supportsUpdate());
        }
        setModeNew(false);
    }

    @Override
    public boolean canClose() {
        boolean canClose = true;
        int answer;
        FormModel detailFormModel = getDetailForm().getFormModel();
        if (detailFormModel.isEnabled() && detailFormModel.isDirty()) {
            if (detailFormModel.isCommittable()) {

                answer = AxApp.applicationConfig().dialogFactory().showWarningDialog(getComponent(), UNSAVEDCHANGES_WARNING_ID, JOptionPane.YES_NO_CANCEL_OPTION);
            } else {
                answer = AxApp.applicationConfig().dialogFactory().showWarningDialog(getComponent(), UNSAVEDCHANGES_UNCOMMITTABLE_WARNING_ID, JOptionPane.YES_NO_OPTION);
                answer = answer == JOptionPane.YES_OPTION ? JOptionPane.CANCEL_OPTION : JOptionPane.NO_OPTION;
            }
            switch (answer) {
                case JOptionPane.CANCEL_OPTION:
                    canClose = false;
                    break;
                case JOptionPane.YES_OPTION:
                    doCommit();
                    break;
                case JOptionPane.NO_OPTION:
                    //TODO doRevert?
                    detailFormModel.revert();
                    break;
            }
        }
        return canClose;
    }

    @Override
    public void grabFocus() {
        if (formWidget instanceof Focussable) {
            ((Focussable) formWidget).grabFocus();
        }
    }

    public ActionCommand getCreateCommand() {
        if (createCommand == null) {
            createCommand = new ActionCommand(WRAPER_CREATE) {

                @Override
                protected void doExecuteCommand() {
                    doCommit();
                }
            };
            AxApp.applicationConfig().commandConfigurer().configure(createCommand);
            getDetailForm().addGuarded(createCommand, FormGuard.LIKE_COMMITCOMMAND);
        }
        return createCommand;
    }

    public ActionCommand getUpdateCommand() {
        if (updateCommand == null) {
            updateCommand = new ActionCommand(WRAPER_UPDATE) {

                @Override
                protected void doExecuteCommand() {
                    doCommit();
                }
            };
            AxApp.applicationConfig().commandConfigurer().configure(updateCommand);
            getDetailForm().addGuarded(updateCommand, FormGuard.LIKE_COMMITCOMMAND);
        }
        return updateCommand;
    }

    public ActionCommand getRevertCommand() {
        if (revertCommand == null) {
            revertCommand = getDetailForm().getRevertCommand();
        }
        return revertCommand;
    }
}
