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

package cz.req.ax.view;

import cz.req.ax.support.SharedCommandsAware;
import org.valkyriercp.application.PageComponentContext;
import org.valkyriercp.application.support.AbstractView;
import org.valkyriercp.command.support.ActionCommand;

/**
 * AxRefreshView
 *
 * @author Ondrej Burianek
 */
@Deprecated
public abstract class AxRefreshView extends AbstractView {

    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    Boolean refreshEnabled = Boolean.FALSE;
    Integer refreshDelay = 3;
    AutoRefresh refreshThread;
    RefreshCommand refreshCommand = new RefreshCommand();

    public AxRefreshView(String id) {
        super(id);
    }

    public void doRefresh() {
    }

    @Override
    public void componentFocusGained() {
        super.componentFocusGained();
        if (refreshEnabled) {
            refreshThread = new AutoRefresh();
            refreshThread.start();
        } else {
            doRefresh();
        }
    }

    @Override
    public void componentFocusLost() {
        super.componentFocusLost();
        if (refreshThread != null) {
            refreshThread.interrupt();
            refreshThread = null;
        }
    }

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        super.registerLocalCommandExecutors(context);
        context.register(SharedCommandsAware.SHARED_REFRESH, getRefreshCommand());
    }

    public void setRefreshDelay(Integer refreshDelay) {
        this.refreshDelay = refreshDelay;
    }

    public void setRefreshEnabled(Boolean refreshEnabled) {
        this.refreshEnabled = refreshEnabled;
    }

    public RefreshCommand getRefreshCommand() {
        return refreshCommand;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + getId();
    }

    public class RefreshCommand extends ActionCommand {

        public RefreshCommand() {
            super(SharedCommandsAware.SHARED_REFRESH);
        }

        @Override
        protected void doExecuteCommand() {
            doRefresh();
        }
    }

    class AutoRefresh extends Thread {

        AutoRefresh() {
            super("AutoRefresh");
        }

        @Override
        public void run() {
            try {
                while (!interrupted()) {
                    try {
                        doRefresh();
                    } catch (Exception ex) {
                        log.error("Refresh error: " + ex.getMessage());
                    }
                    sleep(refreshDelay * 1000);
                }
            } catch (InterruptedException ex) {
            }
        }
    }
}
