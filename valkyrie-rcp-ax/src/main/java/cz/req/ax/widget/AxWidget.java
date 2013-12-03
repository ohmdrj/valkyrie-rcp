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

import com.jgoodies.forms.layout.RowSpec;
import cz.req.ax.AxApp;
import cz.thickset.utils.swing.FormFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.widget.AbstractWidget;
import org.valkyriercp.widget.Widget;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AxWidget
 *
 * @author Ondrej Burianek
 */
public abstract class AxWidget extends AbstractWidget {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private List<Widget> widgets = new ArrayList<Widget>();
    private JPanel panel;
    private FormFactory formFactory;
    private String id;
    private boolean enabled = true;

    public AxWidget() {
    }

    public AxWidget(String id) {
        this.id = id;
    }

    public abstract void createWidget();

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInput(Object input) {
    }

    public FormFactory getFormFactory() {
        if (formFactory == null) {
            panel = new JPanel();
            formFactory = initFormFactory(panel);
        }
        return formFactory;
    }

    protected FormFactory initFormFactory(JPanel panel) {
        FormFactory ff = new FormFactory(panel, "p:grow", "p");
        ff.setBorder(2);
        ff.setGapHorizontal(2);
        ff.setGapVertical(2);
        return ff;
    }

    public List<Widget> getWidgets() {
        return widgets;
    }

    protected void addWidget(Widget widget) {
        if (widget == null) {
            return;
        }
        widgets.add(widget);
    }

    protected void delWidget(Widget widget) {
        if (widget == null) {
            return;
        }
        widgets.remove(widget);
    }

    protected JComponent[] convert(Object[] objects) {
        ArrayList<JComponent> list = new ArrayList<JComponent>();
        for (Object object : objects) {
            if (object instanceof Widget) {
                list.add(((Widget) object).getComponent());
                ;
//            } else if (object instanceof AbstractCommand) {
//                return addImpl((AbstractCommand) object, constraint);
            } else if (object instanceof JComponent) {
                list.add((JComponent) object);
                ;
            } else if (object != null) {
                throw new IllegalArgumentException("Unsupported object: " + object);
            }
        }
        return list.toArray(new JComponent[list.size()]);

    }

    public void addRow(Object... objects) {
        getFormFactory().setStandardRow(RowSpec.decode("fill:p"));
        getFormFactory().addRowExt(convert(objects));
    }

    public void addFull(Object... objects) {
        getFormFactory().setStandardRow(RowSpec.decode("fill:p:grow"));
        getFormFactory().addRowExt(convert(objects));
    }

    @Override
    public JComponent getComponent() {
        if (panel == null) {
            createWidget();
        }
        return panel;
    }

    public void resetComponent() {
        panel = null;
        formFactory = null;
    }

    @Override
    public void onAboutToShow() {
        super.onAboutToShow();
        for (Widget widget : widgets) {
            widget.onAboutToShow();
        }
    }

    @Override
    public void onAboutToHide() {
        super.onAboutToHide();
        for (Widget widget : widgets) {
            widget.onAboutToHide();
        }

    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for (Widget widget : widgets) {
            widget.getComponent().setEnabled(enabled);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public AbstractWidget getHeader() {
        return null;
    }
//    public SharedCommandsAware getSharedCommandsAware() {
//        return null;
//    }

    public ProgressMonitor getProgressMonitor() {
        return getApplicationWindow().getStatusBar().getProgressMonitor();
    }

    public ApplicationWindow getApplicationWindow() {
        return AxApp.applicationConfig().windowManager().getActiveWindow();
    }

    //    public String getViewId() {
//        if (viewId == null) {
//            log.warn("viewId is null");
//        }
//        return viewId;
//    }
//
//    public void setViewId(String viewId) {
//        this.viewId = viewId;
//    }
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
