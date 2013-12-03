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

import cz.req.ax.options.AxApplicationSettings;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.support.DefaultApplicationWindowConfigurer;
import org.valkyriercp.util.WindowUtils;

import javax.swing.*;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationWindowConfigurer extends DefaultApplicationWindowConfigurer {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    AxApplicationSettings settings;

    public AxApplicationWindowConfigurer(ApplicationWindow window, AxApplicationSettings settings) {
        super(window);
        this.settings = settings;
        setShowToolBar(false);
    }

    public void configureWindow(JFrame windowControl) {
        windowControl.pack();
        windowControl.setSize(800, 600);
        WindowUtils.centerOnScreen(windowControl);
        if (settings == null) {
            return;
        }
        try {
            Integer width = settings.getInteger("window.width");
            Integer height = settings.getInteger("window.height");
            windowControl.setSize(width, height);
        } catch (Exception ex) {
            log.error("Error setting window size", ex);
        }
        try {
            Integer posx = settings.getInteger("window.posx");
            Integer posy = settings.getInteger("window.posy");
            windowControl.setLocation(posx, posy);
        } catch (Exception ex) {
            log.error("Error setting window size", ex);
        }
        try {
            boolean maxi = Boolean.parseBoolean(settings.get("window.maximized"));
            if (maxi) {
                windowControl.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        } catch (Exception ex) {
            log.error("Error setting window state", ex);
        }
    }

    public void storeWindow(JFrame windowControl) {
        if (settings == null) {
            log.warn("Settings is null, cannot store window parameters");
            return;
        }
        settings.setInteger("window.width", (int) windowControl.getWidth());
        settings.setInteger("window.height", (int) windowControl.getHeight());
        settings.setInteger("window.posx", (int) windowControl.getLocation().getX());
        settings.setInteger("window.posy", (int) windowControl.getLocation().getY());
        boolean maxi = (windowControl.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
        settings.set("window.maximized", String.valueOf(maxi));
        settings.writeSettings();
    }
}
