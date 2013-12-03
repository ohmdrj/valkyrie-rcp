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

package cz.req.ax.command;

import com.jgoodies.forms.builder.ButtonBarBuilder2;
import org.springframework.util.Assert;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.support.AbstractCommand;

import javax.swing.*;

/**
 * AxButtonBarBuilder
 *
 * @author Ondrej Burianek
 */
public class AxButtonBarBuilder extends ButtonBarBuilder2 {

    CommandConfigurer commandConfigurer;

    public AxButtonBarBuilder(CommandConfigurer commandConfigurer, Object... commands) {
        this.commandConfigurer = commandConfigurer;
        addCommands(commands);
    }

    public void addCommands(Object... commands) {
        for (Object object : commands) {
            if (object instanceof AbstractCommand) {
                addCommand((AbstractCommand) object);
            } else if (object instanceof JComponent) {
                add((JComponent) object);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }

    public void addCommand(AbstractCommand command) {
        Assert.notNull(commandConfigurer, "CommandConfigurer not autowired!");
        commandConfigurer.configure(command);
        addButton(command.createButton());
    }
}
