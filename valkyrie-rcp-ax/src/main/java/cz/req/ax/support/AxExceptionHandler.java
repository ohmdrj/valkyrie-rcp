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

package cz.req.ax.support;

import cz.req.ax.AxApp;
import cz.req.ax.dialog.AxDialog;
import cz.thickset.utils.swing.FormFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.valkyriercp.application.exceptionhandling.MessagesDialogExceptionHandler;
import org.valkyriercp.dialog.AbstractDialogPage;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ondrej Burianek
 */
@Configurable
public class AxExceptionHandler extends MessagesDialogExceptionHandler {

    AxDialog dialog;

    public AxExceptionHandler() {
        dialog = new AxDialog() {
            @Override
            protected boolean onFinish() {
                return true;
            }
        };
    }

    @Override
    public void notifyUserAboutException(Thread thread, Throwable throwable) {
        try {
            try {
                dialog.setParentComponent(AxApp.applicationConfig().windowManager().getActiveWindow().getControl());
            } catch (Exception ex) {
            }
            dialog.setDialogPage(new ExceptionDialogPage(throwable));
            dialog.showDialog();
        } catch (Throwable local) {
            logger.error("Error showing ExceptionDialog", local);
            JOptionPane.showMessageDialog(null, throwable.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    //    @Override
//    public void afterPropertiesSet() throws Exception {
//        MessagesDialogExceptionHandler exceptionHandler1 = new MessagesDialogExceptionHandler();
//        JXErrorDialogExceptionHandler exceptionHandler2 = new JXErrorDialogExceptionHandler();
//        applicationContext.getBeanFactory().registerSingleton("exceptionHandler1", exceptionHandler1);
//        applicationContext.getBeanFactory().registerSingleton("exceptionHandler2", exceptionHandler2);
//
//        List<ExceptionHandlerDelegate> list = new ArrayList<ExceptionHandlerDelegate>();
//        SimpleExceptionHandlerDelegate delegate = new SimpleExceptionHandlerDelegate();
//        delegate.setThrowableClass(java.lang.Throwable.class);
//        delegate.setExceptionHandler(exceptionHandler2);
//        setDelegateList(list);
//        list.add(delegate);
//
//        super.afterPropertiesSet();
//
    private class ExceptionDialogPage extends AbstractDialogPage {

        Throwable throwable;

        ExceptionDialogPage(Throwable throwable) {
            super("exception");
            this.throwable = throwable;
        }

        protected String getTraceHTML() {
            StringBuilder html = new StringBuilder("<html>");
            html.append("<div>");
            Throwable th = throwable;
            html.append("<b>Stack trace:</b>");
            do {
                html.append("<pre>");
                for (StackTraceElement el : th.getStackTrace()) {
                    html.append("    ").append(el.toString().replace("<init>", "&lt;init&gt;")).append("\n");
                }
                html.append("</pre>");

                if (th.getCause() == null || th.equals(th.getCause())) {
                    th = null;
                } else {
                    html.append("<b>Cause:</b>");
                    th = th.getCause();
                }
            } while (th != null);
            html.append("</div></html>");
            return html.toString();
        }

        protected String getMessageHTML() {
            StringBuilder html = new StringBuilder("<html>");
            html.append("<div style=\"width:650px\">");
            if (throwable != null && throwable.getMessage() != null) {
                String message = throwable.getMessage();
                message = message.replaceAll(" nested exception is ", "<hr>");
                message = message.replaceAll(": ", "<br>");
                html.append(message);
            }
            html.append("</div></html>");
            return html.toString();
        }

        @Override
        protected JComponent createControl() {
            FormFactory formFactory = new FormFactory("p", "p");
            JLabel labelException = new JLabel("Exception " + throwable.getClass().getCanonicalName());
            labelException.setFont(labelException.getFont().deriveFont(Font.BOLD, 16));
            JLabel labelMessage = new JLabel(getMessageHTML());
            labelMessage.setFont(labelMessage.getFont().deriveFont(9f));
            JLabel labelTrace = new JLabel(getTraceHTML());
            labelTrace.setFont(labelTrace.getFont().deriveFont(9f));

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(labelTrace);
            JScrollPane scrollTrace = new JScrollPane(panel);
            scrollTrace.setPreferredSize(new Dimension(600, 260));
            scrollTrace.getVerticalScrollBar().setUnitIncrement(20);

            formFactory.addRowGap(labelException);
            formFactory.addRowGap(labelMessage);
            formFactory.setStandardSpecs("fill:p:grow", "p");
            formFactory.addRowGap(scrollTrace);
            return formFactory.getPanel();
        }
    }
}
