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

package cz.req.ax.components;

import org.valkyriercp.component.TitlePane;
import org.valkyriercp.core.DefaultMessage;
import org.valkyriercp.core.DescriptionConfigurable;
import org.valkyriercp.core.LabelConfigurable;
import org.valkyriercp.core.support.LabelInfo;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ondrej Burianek
 */
public class AxTitlePane extends TitlePane implements DescriptionConfigurable, LabelConfigurable {

    public AxTitlePane() {
        super(1);
    }

    @Override
    public void setLabelInfo(LabelInfo label) {
        String title = getTitle();
        if (title == null || "Title Pane Title".equals(title)) {
            setTitle(label.getText());
        }
    }

    @Override
    public void setCaption(String shortDescription) {
        setTitle(shortDescription);
    }

    @Override
    public void setDescription(String longDescription) {
        setMessage(new DefaultMessage(longDescription));
    }

    @Override
    protected JComponent createControl() {
        JPanel container = new JPanel(new BorderLayout());
        JComponent titlePane = super.createControl();
        titlePane.setPreferredSize(new Dimension(100, 40));
        container.add(titlePane, BorderLayout.CENTER);
        container.add(new JSeparator(), BorderLayout.SOUTH);
        return container;
    }
}
