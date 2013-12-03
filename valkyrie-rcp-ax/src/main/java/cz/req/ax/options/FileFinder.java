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

package cz.req.ax.options;

import com.jidesoft.swing.FolderChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Ondrej Burianek
 */
public class FileFinder extends JPanel implements ActionListener {

    JFileChooser chooser;
    JTextField field;
    JButton button;
    File file;

    public FileFinder() {
        field = new JTextField();
        field.setEditable(false);
        chooser = new JFileChooser();
        button = new JButton("Zvolit", null);
        button.addActionListener(this);
        setLayout(new BorderLayout());
        add(field, BorderLayout.CENTER);
        add(button, BorderLayout.EAST);
    }

    public void setFile(File file) {
        this.file = file;
        field.setText(file == null ? "" : file.getAbsolutePath());
    }

    public File getFile() {
        return file;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if (chooser == null) {
        if (getFile() == null) {
            chooser = new FolderChooser();
        } else {
            chooser = new FolderChooser(getFile());
        }
        chooser.setMultiSelectionEnabled(false);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        }
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            setFile(chooser.getSelectedFile());
        }

    }
}
