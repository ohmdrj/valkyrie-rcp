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

import cz.req.ax.command.ClosePageCommand;
import cz.req.ax.components.BetterTab;
import cz.req.ax.components.BetterTabListener;
import cz.req.ax.components.BetterTabbedPane;
import cz.req.ax.options.AxApplicationSettings;
import cz.thickset.utils.branding.ThicksetProject;
import cz.thickset.utils.reflect.PrivateClassUtils;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.jdesktop.jxlayer.JXLayer;
import org.jdesktop.jxlayer.plaf.effect.BufferedImageOpEffect;
import org.jdesktop.jxlayer.plaf.ext.LockableUI;
import org.jdesktop.swingx.image.ColorTintFilter;
import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationWindowConfigurer;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.PageListener;
import org.valkyriercp.application.config.ApplicationConfig;
import org.valkyriercp.application.support.AbstractApplicationWindow;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.command.support.CommandGroup;
import org.valkyriercp.util.ValkyrieRepository;

import javax.swing.*;
import java.awt.*;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationWindow extends AbstractApplicationWindow {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    LockableUI lockableUI;
    JXLayer<JComponent> layer;
    JPanel container;
    protected CommandGroup commandGroup;
    protected BetterTabbedPane tabPane = new BetterTabbedPane();
    //    protected ArrayList<ApplicationPage> tabList = new ArrayList<ApplicationPage>();
    protected BidiMap singletonCache = new DualHashBidiMap();

    public AxApplicationWindow(int number, ApplicationConfig config) {
        super(number, config);
    }

    public AxApplicationWindow(ApplicationConfig config) {
        super(config);
    }

    @Override
    protected void init() {
        super.init();
        addPageListener(new PageListener() {

            @Override
            public void pageOpened(ApplicationPage page) {
//                System.out.println(" === pageOpened: " + page);
            }

            @Override
            public void pageClosed(ApplicationPage page) {
//                System.out.println(" === pageClosed: " + page);
            }
        });
    }

    @Override
    protected JComponent createWindowContentPane() {
        lockableUI = new LockableUI(new BufferedImageOpEffect(new ColorTintFilter(Color.WHITE, 0.5f)));
        container = new JPanel(new BorderLayout());
        tabPane.setCloseAction(getClosePageCommand().getActionAdapter());
        tabPane.addBetterTabListener(new BetterTabListener() {

            @Override
            public void tabSelected(BetterTab betterTab) {
                if (betterTab != null) {
//                    if (getPage() != null && getPage().getActiveComponent() != null) {
//                        System.out.println(">W< --" + getPage().getId() + " " + getPage().getActiveComponent().getId());
//                    }
                    setActivePage(betterTab.getPage(), true);
//                    if (getPage() != null && getPage().getActiveComponent() != null) {
//                        System.out.println(">W< ++" + getPage().getId() + " " + getPage().getActiveComponent().getId());
//                    }
                }
            }
        });
        container.add(tabPane, BorderLayout.CENTER);
        layer = new JXLayer<JComponent>(container);
        layer.setUI(lockableUI);
        return layer;
    }

    @Override
    protected ApplicationPage createPage(PageDescriptor descriptor) {
        boolean sigleton = BetterTab.isSingleton(descriptor);
//        log.info("createPage " + (sigleton ? "singleton" : "unique"));
        if (sigleton && singletonCache.containsKey(descriptor)) {
            // descriptor cached, do not create+add, just return
            return (ApplicationPage) singletonCache.get(descriptor);
        }

        AxApp.applicationConfig().applicationObjectConfigurer().configure(descriptor, descriptor.getId());
        ApplicationPage page = super.createPage(descriptor);
        tabPane.addTab(new BetterTab(descriptor, page));
        if (sigleton) {
            singletonCache.put(descriptor, page);
        }
        return page;

    }

    @Override
    public void showPage(String pageId) {
        showPage(pageId, null);
    }

    @Override
    public void showPage(PageDescriptor pageDescriptor) {
        showPage(pageDescriptor, null);
    }

    public void showPage(String pageId, Object input) {
        if (pageId != null) {
            showPage(getPageDescriptor(pageId), input);
        }
    }

    public void showPage(PageDescriptor pageDescriptor, Object input) {
        Assert.notNull(pageDescriptor, "pageDescriptor is null");
        ApplicationPage page = createPage(pageDescriptor);
        Assert.notNull(page, "page is null");
        showPage(page, input);
    }

    public void showPage(ApplicationPage page, Object input) {
//        log.info("showPage " + page.getId());
        if (page instanceof AxApplicationPage) {
            ((AxApplicationPage) page).setInput(input);
        }
        if (getPage() != null) {
            //Open multiple pages
            setActivePage(page);
        } else {
            //Open new window
//            super.showPage(page);
            getAdvisor().onPreWindowOpen(getWindowConfigurer());
            JFrame control = createNewWindowControl();
            PrivateClassUtils.writeField(this, AbstractApplicationWindow.class, "control", control);
            control.addWindowFocusListener(this);
            initWindowControl(control);
            getAdvisor().onWindowCreated(this);
            setActivePage(page);
            control.setVisible(true);
            getAdvisor().onWindowOpened(this);
        }
//        page.getActiveComponent().componentFocusGained();
    }

    // Open page action
    @Override
    protected void setActivePage(ApplicationPage page) {
        tabPane.selectTab(page);
        setActivePage(page, false);
    }

    private void setActivePage(ApplicationPage page, boolean fireFocusEvents) {
        log.info("activePage " + page.getId() + " " + page);
        if (fireFocusEvents && getPage() instanceof AxApplicationPage) {
            ((AxApplicationPage) getPage()).focusLoose();
        }
        setPage(page);
        if (fireFocusEvents && getPage() instanceof AxApplicationPage) {
            ((AxApplicationPage) getPage()).focusGain();
        }
    }

    public void closePage() {
        closePage(getPage());
    }

    public void closePage(Object criteria) {
        tabPane.removeTab(criteria);
//        singletonCache.removeValue(activePage);
    }

    @Override
    public boolean close() {
        ((AxApplicationWindowConfigurer) getWindowConfigurer()).storeWindow(getControl());
        return super.close();
    }

    @Override
    protected ApplicationWindowConfigurer initWindowConfigurer() {
        AxApplicationSettings settings = ValkyrieRepository.getInstance().getBean(AxApplicationSettings.class);
        return new AxApplicationWindowConfigurer(this, settings);
    }

    /**
     * Cascade window location on screen
     *
     * @param windowControl
     * @param configurer
     */
    @Override
    protected void prepareWindowForView(JFrame windowControl, ApplicationWindowConfigurer configurer) {
        try {
            windowControl.setTitle(windowControl.getTitle() + " " + ThicksetProject.instance().getVersion());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (configurer instanceof AxApplicationWindowConfigurer) {
            ((AxApplicationWindowConfigurer) configurer).configureWindow(windowControl);
        } else {
            super.prepareWindowForView(windowControl, configurer);
        }
    }

    protected ActionCommand getClosePageCommand() {
        ActionCommand ret = (ActionCommand) getCommandManager().getCommand(ClosePageCommand.ID);
        if (ret == null) {
            ret = new ClosePageCommand();
        }
        return ret;
    }

    public void setMaximized(boolean maximized) {
        throw new UnsupportedOperationException();
    }

    //MIG
//    protected ApplicationObjectConfigurer getApplicationObjectConfigurer() {
//        return (ApplicationObjectConfigurer) getServices().getService(ApplicationObjectConfigurer.class);
//    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AxApplicationWindow (class=").append(getClass().getSimpleName());
        if (getPage() != null) {
            sb.append(" pageClass=").append(getPage().getClass()).append(" pageId=").append(getPage().getId());
        }

        if (getPage() != null && getPage().getActiveComponent() != null) {
            sb.append(" componentClass=").append(getPage().getActiveComponent());
        }

        sb.append(")");
        return sb.toString();
    }

    public void setLocked(boolean locked) {
        //log.info("layer locked=" + locked);
        lockableUI.setLocked(locked);
        layer.revalidate();
        layer.repaint();
    }

    public boolean isLocked() {
        return lockableUI.isLocked();
    }

    public static void setApplicationLocked(boolean locked) {
        //MIG
//        ((AxApplicationWindow) Application.instance().getActiveWindow()).setLocked(locked);
    }
}
