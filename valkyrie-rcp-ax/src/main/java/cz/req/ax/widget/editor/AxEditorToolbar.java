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
import cz.req.ax.widget.AxDataWidget;
import cz.thickset.utils.reflect.PrivateClassUtils;
import cz.thickset.utils.swing.FormFactory;
import org.springframework.util.Assert;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.core.Guarded;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * AxEditorToolbar
 *
 * @author Ondrej Burianek
 */
public class AxEditorToolbar extends AxDataWidget implements Observer {

    private final JPanel firstGroup = new JPanel(new FlowLayout(SwingConstants.HORIZONTAL, 2, 0));
    private final JPanel secondGroup = new JPanel(new FlowLayout(SwingConstants.HORIZONTAL, 2, 0));
    private final JPanel thirdGroup = new JPanel(new FlowLayout(SwingConstants.HORIZONTAL, 2, 0));

    @Override
    public void refresh() {
    }

    @Override
    protected FormFactory initFormFactory(JPanel panel) {
        FormFactory ff = new FormFactory(panel, "fill:p,p,fill:p,p,fill:p:grow", "p");
        ff.setBorder(0);
        ff.setGapHorizontal(2);
        ff.setGapVertical(2);
        return ff;
    }

    @Override
    public void createWidget() {
        Assert.notNull(getDataProvider(), "DataProvider is required!");
        if (getDataProvider().supportsCreate()) {
            firstGroup.add(getSharedCommands().getSharedAdd().createButton());
        }
        if (getDataProvider().supportsUpdate()) {
            firstGroup.add(getSharedCommands().getSharedEdit().createButton());
        }
        if (getDataProvider().supportsDelete()) {
            firstGroup.add(getSharedCommands().getSharedRemove().createButton());
        }
        if (getDataProvider().supportsDetail()) {
            secondGroup.add(getSharedCommands().getSharedDetail().createButton());
        }
        secondGroup.add(getSharedCommands().getSharedRefresh().createButton());

        JSeparator separator1 = new JSeparator(JSeparator.VERTICAL);
        JSeparator separator2 = new JSeparator(JSeparator.VERTICAL);
        addRow(firstGroup, separator1, secondGroup, separator2, thirdGroup);

        //        new SupportedGuard(getDataProvider().supportsCreate(), getSharedCommands().getSharedAdd());
//        guardedGroup = new GuardedGroup(new Guarded[]{
//                    new SupportedGuard(getDataProvider().supportsUpdate(), getSharedCommands().getSharedEdit()),
//                    new SupportedGuard(getDataProvider().supportsDelete(), getSharedCommands().getSharedRemove()),
//                    new SupportedGuard(getDataProvider().supportsDetail(), getSharedCommands().getSharedDetail())
//                });
//        guardedGroup.setEnabled(false);
    }

    public void setFilterField(JTextField filterField) {
        if (filterField == null) {
            log.warn("FilterField is null!");
            return;
        }
        int height = filterField.getPreferredSize().height;
        filterField.setPreferredSize(new Dimension(200, height));
        filterField.setMinimumSize(new Dimension(100, height));
        thirdGroup.add(filterField);
    }

    public void addComponentFirst(JComponent component) {
        firstGroup.add(component);
        fixComponentSize(component);
    }

    public void addComponentSecond(JComponent component) {
        secondGroup.add(component);
        fixComponentSize(component);
    }

    protected void fixComponentSize(JComponent component) {
        int height = 23;
        int width = 120;
        component.setPreferredSize(new Dimension(width, height));
//        component.setMinimumSize(new Dimension(component.getMinimumSize().width, height));
    }

    public AbstractButton addCommandFirst(AbstractCommand command) {
        return addCommand(firstGroup, command);
    }

    public AbstractButton addCommandSecond(AbstractCommand command) {
        return addCommand(secondGroup, command);
    }

    @Deprecated
    public AbstractButton addCommand(JPanel panel, AbstractCommand command) {
        //TODO Revize
        PrivateClassUtils.writeField(command, AbstractCommand.class, "applicationConfig", AxApp.applicationConfig());
        command.afterPropertiesSet();
        AbstractButton button = command.createButton();
        panel.add(button);
        return button;
    }

    //MIG
//    public GuardedGroup getGuardedGroup() {
//        return guardedGroup;
//    }
    @Override
    public void update(Observable o, Object arg) {
//        if (guardedGroup != null) {
//            guardedGroup.setEnabled(arg != null);
//        }
    }

    public class SupportedGuard implements Guarded {

        private final boolean supported;
        private final Guarded guarded;

        public SupportedGuard(boolean supported, Guarded guarded) {
            this.supported = supported;
            this.guarded = guarded;
            this.guarded.setEnabled(supported);
        }

        @Override
        public boolean isEnabled() {
            return guarded.isEnabled();
        }

        @Override
        public void setEnabled(boolean enabled) {
            if (supported) {
                guarded.setEnabled(enabled);
            } else {
                guarded.setEnabled(false);
            }
        }
    }
}
