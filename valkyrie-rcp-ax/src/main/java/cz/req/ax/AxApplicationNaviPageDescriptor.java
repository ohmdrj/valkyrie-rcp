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

package cz.req.ax;

import com.l2fprod.common.swing.JLinkButton;
import cz.thickset.utils.swing.FormFactory;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.painter.MattePainter;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.taskpane.JTaskPaneCommandButtonConfigurer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationNaviPageDescriptor extends AxApplicationPageDescriptor {

    //    private List navigation;
//
//    public List getNavigation() {
//        return navigation;
//    }
//
//    public void setNavigation(List navigation) {
//        this.navigation = navigation;
//    }
    List<NaviGroup> groups = new ArrayList<NaviGroup>();
    JPanel container = new JPanel();

    public NaviGroup addGroup(String title) {
        NaviGroup naviGroup = new NaviGroup(title);
        groups.add(naviGroup);
        return naviGroup;
    }

    public JComponent getControl() {
        FormFactory factory = new FormFactory(container, "100px:grow", "p");
        factory.setBorder(3);
        for (NaviGroup group : groups) {
            Color blue = new Color(97, 147, 207);
            JXLabel label = new JXLabel(group.getTitle());
            label.setBorder(new EmptyBorder(3, 5, 3, 5));
            label.setFont(new Font("Dialog", Font.BOLD, 15));
            label.setForeground(Color.white);
            label.setBackgroundPainter(new MattePainter(blue));
            factory.addRowExt(label);
            factory.addRowExt(group.getControl());
            factory.addGap();
        }
//        factory.addRowGap(new JButton(new AbstractAction("Refresh") {
//
//            ApplicationConfig localConfig = applicationConfig;
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                container.removeAll();
//                getControl(localConfig);
//                JComponent control = localConfig.windowManager().getActiveWindow().getPage().getControl();
//                control.invalidate();
//                control.repaint();
//            }
//        }));
        container.setBackground(Color.white);
        return container;

    }

    public static class NaviGroup {

        String title;
        ArrayList<ViewDescriptor> views = new ArrayList<ViewDescriptor>();

        public NaviGroup(String title) {
            this.title = title;
        }

        public void addView(ViewDescriptor... viewDescriptors) {
            this.views.addAll(Arrays.asList(viewDescriptors));
        }

        public String getTitle() {
            return title;
        }

        private JComponent getControl() {
            FormFactory factory = new FormFactory("p:grow", "p");
            factory.setBorder(5);
            for (ViewDescriptor view : views) {
                ActionCommand actionCommand = view.createShowViewCommand(AxApp.applicationConfig().windowManager().getActiveWindow());
                AxApp.applicationConfig().commandConfigurer().configure(actionCommand);
                JLinkButton button = new JLinkButton();
                actionCommand.attach(button, new JTaskPaneCommandButtonConfigurer());
                factory.addRowExt(button);
            }
            return factory.getPanel();
        }
    }
}
