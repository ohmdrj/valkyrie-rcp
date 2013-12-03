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

package cz.req.ax.widget;

import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author Ondrej Burianek
 */
//TODO Design review
public class AxMasterCommandAdapter implements MouseListener, KeyListener {

    private AbstractCommand targetCommand;

    public void setTargetCommand(AbstractCommand targetCommand) {
        this.targetCommand = targetCommand;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (targetCommand == null) {
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            targetCommand.execute();
            e.consume();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (targetCommand == null) {
            return;
        }
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            targetCommand.execute();
            e.consume();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
