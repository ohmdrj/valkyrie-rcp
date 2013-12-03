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
import org.jdesktop.swingx.*;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.painter.BusyPainter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.valkyriercp.application.splash.AbstractSplashScreen;
import org.valkyriercp.application.splash.MonitoringSplashScreen;
import org.valkyriercp.util.WindowUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

/**
 * @author Ondrej Burianek
 */
public class AxSplashScreen extends AbstractSplashScreen implements MonitoringSplashScreen, org.valkyriercp.progress.ProgressMonitor, ApplicationContextAware {

    ApplicationContext applicationContext;
    private JXBusyLabel busyLabel = new JXBusyLabel(new Dimension(180, 180));
    private JXLabel titleLabel = new JXLabel("");
    private JProgressBar progressBar = new JProgressBar();
    private JXLabel progressLabel = new JXLabel();
    private JXFrame frame;
    private Resource image;
    //    public ProgressMonitor getProgressMonitor() {
//        return new ProgressMonitor() {
    private int currentWork = 0;

    public AxSplashScreen() {
        BusyPainter painter = new BusyPainter(
                new RoundRectangle2D.Float(0, 0, 28.0f, 8.6f, 10.0f, 10.0f),
                new Ellipse2D.Float(15.0f, 15.0f, 70.0f, 70.0f));
        painter.setTrailLength(4);
        painter.setPoints(8);
        painter.setFrame(7);
        painter.setHighlightColor(new Color(30, 42, 102));
        busyLabel.setPreferredSize(new Dimension(100, 100));
        busyLabel.setIcon(new EmptyIcon(100, 100));
        busyLabel.setBusyPainter(painter);
        busyLabel.setDelay(75);

        titleLabel.setFont(titleLabel.getFont().deriveFont(30f));
        titleLabel.setForeground(Color.BLACK);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(170, 20));
//        titleLabel.setBackground(new Color(0x425DA9));
//        titleLabel.setOpaque(false);
//        progressLabel.setBackground(new Color(0x425DA9));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public org.valkyriercp.progress.ProgressMonitor getProgressMonitor() {
        return this;
    }

    @Override
    public void taskStarted(String name, int totalWork) {
        progressBar.setMaximum(totalWork);
        progressBar.setValue(0);
        this.currentWork = 0;
        busyLabel.setBusy(true);
        progressBar.setString(name);
    }

    @Override
    public void subTaskStarted(String name) {
        //progressBar.setString(name);
    }

    @Override
    public void worked(int work) {
        currentWork += work;
        progressBar.setValue(currentWork);
    }

    @Override
    public void done() {
        busyLabel.setBusy(false);
    }

    @Override
    public boolean isCanceled() {
        return false;
    }

    @Override
    public void setCanceled(boolean b) {
    }
//        };
//    }

    @Override
    protected Component createContentPane() {
        JXPanel panel = new JXPanel(new FormLayout("center:200px:nogrow, left:3dlu:nogrow, fill:200px:nogrow", "center:200px:nogrow, center:20px:nogrow"));

        if (image != null) {
            try {
                titleLabel.setIcon(new ImageIcon(ImageIO.read(image.getInputStream())));
            } catch (IOException ex) {
                logger.error("Error loading icon", ex);
            }
        }
        //ip.setStyle(JXImagePanel.Style.SCALED);
        panel.setBackground(Color.WHITE);
        panel.add(busyLabel, new CellConstraints(1, 1));
        JXPanel panel2 = new JXPanel(new FormLayout("center:195px:nogrow", "center:98px:nogrow, center:4dlu:nogrow, center:98px:nogrow"));
        //panel2.setBackground(new Color(0x425DA9));
        panel2.add(titleLabel, new CellConstraints(1, 1, 1, 3));
        panel2.setOpaque(false);
        panel.add(progressBar, new CellConstraints(1, 2, 3, 1));
        panel.add(panel2, new CellConstraints(3, 1));
        //panel.add(ip, new CellConstraints(1, 1, 3, 1, CellConstraints.FILL, CellConstraints.FILL));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        return panel;
    }

    public void splash1() {
        frame = new JXFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setRootPane(new JXRootPane());
        frame.getRootPaneExt().setDoubleBuffered(true);
        frame.getRootPaneExt().setBackground(new Color(255, 255, 255, 255));
        frame.getRootPaneExt().setOpaque(false);
        frame.getContentPane().add(createContentPane());
        frame.pack();
        WindowUtils.centerOnScreen(frame);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            logger.error("Error setting LAF", ex);
        }
        SwingUtilities.updateComponentTreeUI(frame);
        frame.setVisible(true);
    }

    public void dispose1() {
        if (frame != null) {
            frame.dispose();
            frame = null;
        }
    }

    public void setImage(Resource image) {
        this.image = image;
    }
}
