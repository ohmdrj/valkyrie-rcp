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

package cz.req.ax.view;

import cz.req.ax.AxApp;
import cz.req.ax.components.AxTitlePane;
import cz.req.ax.support.SharedCommandsAware;
import cz.req.ax.widget.AxDataWidget;
import cz.req.ax.widget.AxWidget;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.widget.Widget;
import org.valkyriercp.widget.WidgetView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * AxWidgetView
 *
 * @author Ondrej Burianek
 */
public class AxWidgetView extends WidgetView {

    private final AxTitlePane titlePane = new AxTitlePane();

    public AxWidgetView() {
    }

    public AxWidgetView(Widget widget) {
        super(widget);
    }

    public AxTitlePane getTitlePane() {
        return titlePane;
    }

    @Override
    protected JComponent createControl() {
        AxApp.applicationConfig().applicationObjectConfigurer().configure(getTitlePane(), getId());
        JComponent titlePanel = getTitlePane().getControl();
        //DEBUG
        titlePanel.add(new JButton(new AbstractAction("Restart") {

            @Override
            public void actionPerformed(ActionEvent e) {
                restartWidget();
            }
        }), BorderLayout.EAST);
        JPanel viewPanel = (JPanel) super.createControl();
        viewPanel.add(titlePanel, BorderLayout.NORTH);
        return viewPanel;
    }

    protected void restartWidget() {
        JComponent viewPanel = getControl();
        viewPanel.remove(getWidget().getComponent());
        if (getWidget() instanceof AxWidget) {
            ((AxWidget) getWidget()).resetComponent();
        } else {
            throw new UnsupportedOperationException("Unsupported widget " + getWidget().getClass().getCanonicalName());
        }
        JComponent component = getWidget().getComponent();
        viewPanel.add(component, BorderLayout.CENTER);
        viewPanel.validate();
    }

    @Override
    public void setInput(Object input) {
        super.setInput(input);
        if (getWidget() instanceof AxWidget) {
            ((AxWidget) getWidget()).setInput(input);
        }
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        Widget widget = getWidget();
        if (widget instanceof AxDataWidget) {
//            SharedCommandsAware sharedCommandsAware = ((AxDataWidget) widget).getSharedCommands();
//            if (sharedCommandsAware == null) {
//                deregisterLocalCommandExecutors(context);
//            } else {
//                //TODO
//                //HACK
//                if (sharedCommandsAware instanceof AxEditorSharedCommands) {
//                    ((AxEditorSharedCommands)sharedCommandsAware).setCommandConfigurer(applicationConfig.commandConfigurer());
//                }
//                registerLocalCommandExecutors(context, sharedCommandsAware);
//            }
        }
//        if (getWidget() instanceof TitledWidgetForm) {
//            AbstractForm form = ((TitledWidgetForm) getWidget()).getForm();
//            if (form instanceof SharedCommandsAware) {
//                registerLocalCommandExecutors(context, (SharedCommandsAware) form);
//            }
//        }
    }

    protected void registerLocalCommandExecutors(PageComponentContext context, SharedCommandsAware aware) {
        if (aware.getSharedAdd() != null) {
            context.register(SharedCommandsAware.SHARED_ADD, aware.getSharedAdd());
        }
        if (aware.getSharedEdit() != null) {
            context.register(SharedCommandsAware.SHARED_EDIT, aware.getSharedEdit());
        }
        if (aware.getSharedRemove() != null) {
            context.register(SharedCommandsAware.SHARED_REMOVE, aware.getSharedRemove());
        }
        if (aware.getSharedDetail() != null) {
            context.register(SharedCommandsAware.SHARED_DETAIL, aware.getSharedDetail());
        }
        if (aware.getSharedRefresh() != null) {
            context.register(SharedCommandsAware.SHARED_REFRESH, aware.getSharedRefresh());
        }
        if (aware.getSharedSearch() != null) {
            context.register(SharedCommandsAware.SHARED_SEARCH, aware.getSharedSearch());
        }
        if (aware.getSharedCommit() != null) {
            context.register(SharedCommandsAware.SHARED_COMMIT, aware.getSharedCommit());
        }

    }

    protected void deregisterLocalCommandExecutors(PageComponentContext context) {
    }

    @Override
    public String toString() {
        return "AxWidgetView id=" + getId() + " widget=" + (getWidget() == null ? null : getWidget().toString());
    }
}
