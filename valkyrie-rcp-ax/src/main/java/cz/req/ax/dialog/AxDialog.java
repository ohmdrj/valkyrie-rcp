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

import cz.thickset.utils.Callback;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.dialog.CloseAction;
import org.valkyriercp.dialog.DialogPage;
import org.valkyriercp.dialog.TitledPageApplicationDialog;
import org.valkyriercp.form.Form;

import javax.swing.*;
import java.awt.*;

/**
 * AxDialog
 *
 * @author Ondrej Burianek
 */
@Configurable
public abstract class AxDialog extends TitledPageApplicationDialog {

    public AxDialog(DialogPage dialogPage, Window parent, CloseAction closeAction) {
        super(dialogPage, parent, closeAction);
    }

    public AxDialog(DialogPage dialogPage, Window parent) {
        super(dialogPage, parent);
    }

    public AxDialog(Form form, Window parent) {
        super(form, parent);
    }

    public AxDialog(DialogPage dialogPage) {
        super(dialogPage);
    }

    public AxDialog(Window parent) {
        setParentComponent(parent);
    }

    /**
     * Default used constructor
     */
    public AxDialog() {
    }

    @Override
    protected boolean onFinish() {
        return true;
    }

    @Override
    public void setDialogPage(DialogPage dialogPage) {
        super.setDialogPage(dialogPage);
    }

    @Override
    public void showDialog() {
        super.showDialog();
    }

    public void showDialogBackground() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showDialog();
            }
        });
    }

    public void closeDialog() {
        getDialog().setVisible(false);
    }

    public Callback getClosingCallback() {
        return new ClosingCallback(getDialog());
    }

    public static class ClosingCallback implements Callback {

        JDialog dialog;

        public ClosingCallback(JDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void callback(Object... object) {
            dialog.setVisible(false);
        }
    }
}
