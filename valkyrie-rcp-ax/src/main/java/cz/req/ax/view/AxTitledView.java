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

package cz.req.ax.view;

import cz.req.ax.AxApp;
import cz.req.ax.components.AxTitlePane;
import org.valkyriercp.core.TitleConfigurable;

import javax.swing.*;
import java.awt.*;

/**
 * AxTitledView
 *
 * @author Ondrej Burianek
 */
public abstract class AxTitledView extends AxView implements TitleConfigurable {

    private AxTitlePane titlePane = new AxTitlePane();

    public AxTitledView(String id) {
        super(id);
    }

    @Override
    public void setTitle(String title) {
        //MIG
        this.titlePane.setCaption(title);
    }

    public abstract JComponent createViewControl();

    @Override
    protected JComponent createControl() {
        AxApp.applicationConfig().applicationObjectConfigurer().configure(titlePane, getId());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titlePane.getControl(), BorderLayout.NORTH);
        panel.add(createViewControl(), BorderLayout.CENTER);
        return panel;

    }
}
