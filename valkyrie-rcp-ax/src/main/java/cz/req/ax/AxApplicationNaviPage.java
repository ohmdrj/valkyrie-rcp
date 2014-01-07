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

package cz.req.ax;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationNaviPage extends AxApplicationPage {

    @Override
    protected JComponent createControl() {
        JPanel panel = new JPanel(new FormLayout("200px,p,fill:400px:grow", "fill:p:grow"));
        panel.add(createNaviControl(), new CellConstraints(1, 1));
        panel.add(new JSeparator(JSeparator.VERTICAL), new CellConstraints(2, 1));
        panel.add(super.createControl(), new CellConstraints(3, 1));
        return panel;
    }

    @Override
    public AxApplicationNaviPageDescriptor getPageDescriptor() {
        return (AxApplicationNaviPageDescriptor) super.getPageDescriptor();
    }

    public JComponent createNaviControl() {
        if (getPageDescriptor() instanceof AxApplicationPageDescriptor) {
            AxApplicationNaviPageDescriptor pageDescriptor = (AxApplicationNaviPageDescriptor) getPageDescriptor();
            return pageDescriptor.getControl();
//            JXTaskPaneContainer taskContainer = new JXTaskPaneContainer();
//            taskContainer.setBackground(Color.WHITE);
//            for (Object object : pageDescriptor.getNavigation()) {
//                if (object instanceof AxNaviCommandFactory) {
//                    AxNaviCommandFactory commandFactory = (AxNaviCommandFactory) object;
//                    object = commandFactory.getCommandGroup();
//                }
//                if (object instanceof CommandGroup) {
//                    JXTaskPane taskPane = new JXTaskPane();
//                    CommandGroup commandGroup = (CommandGroup) object;
//                    //TODO
////                    taskPane.setTitle(Application.instance().getApplicationContext().getMessage(commandGroup.getId() + ".label", null, Locale.getDefault()));
//                    taskPane.setTitle(commandGroup.getId());
//
//                    JTaskPaneBuilder taskPaneBuilder = new JTaskPaneBuilder();
//                    JComponent buildComponent = taskPaneBuilder.buildComponent(commandGroup);
//                    taskPane.add(buildComponent);
//                    taskContainer.add(taskPane);
//                } else if (object instanceof AxWidget) {
//                    AxWidget widget = (AxWidget) object;
////                    taskPane.setTitle(widget.getViewId());
//                    taskContainer.add(widget.getComponent());
//                } else {
//                    log.error("Unsupported navigation " + object);
//                }
//            }
//            return taskContainer;
        }
        return new JLabel("EmptyNavi!");
    }
}
