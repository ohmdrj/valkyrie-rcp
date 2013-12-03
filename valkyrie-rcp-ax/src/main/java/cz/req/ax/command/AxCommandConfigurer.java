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

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.valkyriercp.application.config.ApplicationObjectConfigurer;
import org.valkyriercp.command.config.DefaultCommandConfigurer;
import org.valkyriercp.command.support.AbstractCommand;
import org.valkyriercp.command.support.ShowViewCommand;

/**
 * AxCommandConfigurer
 *
 * @author Ondrej Burianek
 */
public class AxCommandConfigurer extends DefaultCommandConfigurer {

    protected String getObjectName(AbstractCommand command) {
        String objectName = command.getId();
        if (command.isAnonymous()) {
            objectName = ClassUtils.getShortNameAsProperty(command.getClass());
            int lastDot = objectName.lastIndexOf('.');
            if (lastDot != -1) {
                objectName = StringUtils.uncapitalize(objectName.substring(lastDot + 1));
            }
        }
        return objectName;
    }

    @Override
    public AbstractCommand configure(AbstractCommand command, ApplicationObjectConfigurer configurer) {
        if (command instanceof ShowViewCommand) {
            command.getId();
        }
        super.configure(command, configurer);
        if (command instanceof ShowViewCommand) {
            command.getDefaultFaceDescriptorId();
        }
        if (!StringUtils.hasText(command.getText())) {
            command.setLabel(getObjectName(command));
        }
        return command;
    }
}
