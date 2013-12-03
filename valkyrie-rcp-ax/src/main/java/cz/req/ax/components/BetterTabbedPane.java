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

import com.jidesoft.swing.JideTabbedPane;
import cz.req.ax.AxApplicationPage;
import org.valkyriercp.util.EventListenerListHelper;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * BetterTabbedPane
 *
 * @author Ondrej Burianek
 */
public class BetterTabbedPane extends JideTabbedPane {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    protected HashMap<PageDescriptor, ApplicationPage> singletonCache = new HashMap<PageDescriptor, ApplicationPage>();
    private final EventListenerListHelper selectionListeners = new EventListenerListHelper(BetterTabListener.class);
    private ArrayList<BetterTab> list = new ArrayList<BetterTab>();
    private BetterTab selected;

    public BetterTabbedPane() {
        setTabShape(JideTabbedPane.SHAPE_OFFICE2003);
        setColorTheme(JideTabbedPane.COLOR_THEME_OFFICE2003);
        setShowCloseButton(true);
        setShowCloseButtonOnTab(true);
        setShowCloseButtonOnSelectedTab(true);
        setCloseTabOnMouseMiddleButton(true);
        setShowTabButtons(true);
        addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                selectTab(getSelectedComponent());
                fireTabSelected(selected);
            }
        });
    }

    public BetterTab getTab(Object criteria) {
        if (criteria == null) {
            return null;
        }
        if (criteria instanceof BetterTab) {
            return (BetterTab) criteria;
        }
        if (criteria instanceof Integer) {
            criteria = getComponentAt((Integer) criteria);
        }
        for (BetterTab betterTab : list) {
            if (betterTab.equals(criteria)) {
                return betterTab;
            }
        }
        return null;
    }

    protected Integer getIndex(BetterTab betterTab) {
        if (betterTab == null) {
            return null;
        }
        int index = indexOfComponent(betterTab.getComponent());
        return index == -1 ? null : index;
    }

    public void addTab(BetterTab betterTab) {
        list.add(betterTab);
        TabTitleUpdater updater = new TabTitleUpdater(betterTab.getDisplayName(), betterTab.getComponent());
        ApplicationPage page = betterTab.getPage();
        page.addPageComponentListener(updater);
        try {
            addTabComponent(betterTab);
            updater.update(page.getActiveComponent());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addTabComponent(BetterTab betterTab) {
        setSuppressStateChangedEvents(true);
        addTab(betterTab.getDisplayName(), betterTab.getDescriptor().getIcon(),
                betterTab.getComponent(), betterTab.getDescriptor().getDescription());
        setSuppressStateChangedEvents(false);
    }

    public boolean removeTab() {
        return removeTab(getSelectedComponent());
    }

    public boolean removeTab(Object criteria) {
        if (getTabCount() == 1) {
            return false;
        }
        BetterTab betterTab = getTab(criteria);
        if (betterTab == null) {
            log.warn("Cannot close tab on criteria: " + criteria);
            return false;
        }
        if (betterTab.getPage() instanceof AxApplicationPage) {
            AxApplicationPage axpage = (AxApplicationPage) betterTab.getPage();
            if (!axpage.getPageDescriptor().isClosable()) {
                return false;
            }
        }
        if (betterTab == null) {
            return false;
        }
        list.remove(betterTab);
        if (getIndex(betterTab) == null) {
            return false;
        }
        removeTabAt(getIndex(betterTab));
        return true;
    }

    public void selectTab(Object criteria) {
        doSelectTab(getTab(criteria));
    }

    public void doSelectTab(BetterTab betterTab) {
        if (betterTab == null) {
            return;
        }
        setSuppressStateChangedEvents(true);
        Integer index = getIndex(betterTab);
        if (index == null) {
            return;
        }
        setSelectedIndex(index);
        selected = betterTab;
        setSuppressStateChangedEvents(false);
    }

    public EventListenerListHelper getSelectionListeners() {
        return selectionListeners;
    }

    public void addBetterTabListener(BetterTabListener listener) {
        selectionListeners.add(listener);
    }

    protected void fireTabSelected(BetterTab tab) {
        if (tab != null) {
            selectionListeners.fire("tabSelected", tab);
        }
    }

    public class TabTitleUpdater implements PageComponentListener {

        String pageTitle;
        String viewTitle = null;
        //        String pageSubTitle = null;
        String viewDescription = null;
        Component component;

        public TabTitleUpdater(String titlePage, Component component) {
            this.pageTitle = titlePage;
            this.component = component;
        }

        public String getTitle() {
            StringBuilder newTitle = new StringBuilder();
            if (pageTitle != null) {
                newTitle.append(pageTitle);
            }
            if (viewTitle != null) {
                if (newTitle.length() == 0) {
                    newTitle.append(viewTitle);
                } else {
                    newTitle.append(": ").append(viewTitle);
                }
            }
            return newTitle.toString();
        }

        public void update(PageComponent pageComponent) {
            if (pageComponent instanceof View) {
                View view = (View) pageComponent;
                viewTitle = view.getDisplayName();
                viewDescription = view.getDescription();
            }
            update();
        }

        public void update() {
            int tabIndex = indexOfComponent(component);
            if (tabIndex >= getTabCount()) {
                log.warn("Unable to update index=" + tabIndex + " titles=" + pageTitle + "," + viewTitle + " descri=" + viewDescription);
                return;
            }
            String newTitle = getTitle();
            setTitleAt(tabIndex, newTitle);
            setToolTipTextAt(tabIndex, viewDescription);
            log.debug("UpdateTitle (index=" + tabIndex + " title=" + newTitle + ")");
        }

        @Override
        public void componentOpened(PageComponent component) {
            update(component);
        }

        @Override
        public void componentFocusGained(PageComponent component) {
            update(component);
        }

        @Override
        public void componentFocusLost(PageComponent component) {
//            update(component);
        }

        @Override
        public void componentClosed(PageComponent component) {
            update(component);
        }

        public void setTitle(String title) {
            throw new UnsupportedOperationException();
//            pageTitle = title;
//            update();
        }
    }
}
