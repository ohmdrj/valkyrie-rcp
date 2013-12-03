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

import cz.req.ax.command.ConfirmActionCommand;
import org.springframework.context.NoSuchMessageException;
import org.valkyriercp.application.config.support.DefaultApplicationObjectConfigurer;

import javax.swing.*;
import java.awt.*;

/**
 * AxApplicationObjectConfigurer
 *
 * @author Ondrej Burianek
 */
public class AxApplicationObjectConfigurer extends DefaultApplicationObjectConfigurer {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    public AxApplicationObjectConfigurer() {
    }

    @Override
    public void configure(Object object, String objectName) {
        if (object instanceof AxApplicationPageDescriptor) {
            log.info("Configure AxApplicationPageDescriptor " + object.toString());
            AxApplicationPageDescriptor descriptor = ((AxApplicationPageDescriptor) object);
//            descriptor.setDisplayTitle(myLoadMessage(objectName + "." + TITLE_KEY));
            descriptor.setImage(myLoadImage(objectName + "." + ICON_KEY));
        }
        if (object instanceof ConfirmActionCommand) {
            ConfirmActionCommand actionCommand = (ConfirmActionCommand) object;
            String confirmMessage = myLoadMessage(actionCommand.getId() + ".confirm");
            actionCommand.setConfirmMessage(confirmMessage);
        }
        try {
            super.configure(object, objectName);
        } catch (Throwable th) {
            log.error("Error configuring object " + object + " as " + objectName);
            throw new RuntimeException(th);
        }
    }

    /**
     * Parent should be protected
     *
     * @param code
     * @return
     */
    private String myLoadMessage(String code) {
//        Assert.required(messageCode, "messageCode");
        try {
            return getMessageSource().getMessage(code, null, getLocale());
        } catch (NoSuchMessageException e) {
            return null;
        }

    }

    /**
     * Parent should be protected
     *
     * @param key
     * @return
     */
    private Icon myLoadIcon(String key) {
        return getIconSource().getIcon(key);
    }

    /**
     * Parent should be protected
     *
     * @param key
     * @return
     */
    private Image myLoadImage(String key) {
        try {
            return getImageSource().getImage(key);
        } catch (Exception ex) {
            return null;
        }
    }
}
//    private void configurePrimaryKey(PrimaryKeyConfigurable object, String objectName) {
//        if (object.getPrimaryKey() != null) {
//            object.setId(objectName + ":" + object.getPrimaryKey());
//        }
//    }

