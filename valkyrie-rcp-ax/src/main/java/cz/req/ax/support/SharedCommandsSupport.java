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

package cz.req.ax.support;

import cz.req.ax.AxApp;
import cz.req.ax.data.RefreshSupport;
import org.valkyriercp.command.CommandConfigurer;
import org.valkyriercp.command.support.ActionCommand;

/**
 * @author Ondrej Burianek
 */
//@Configurable
public abstract class SharedCommandsSupport implements SharedCommandsAware, CrudSupport, RefreshSupport {

    private ActionCommand refreshCommand;
    private ActionCommand searchCommand;
    private ActionCommand addCommand;
    private ActionCommand editCommand;
    private ActionCommand removeCommand;
    private ActionCommand detailCommand;
    private ActionCommand commitCommand;

    @Override
    public abstract void doAdd();

    @Override
    public abstract void doEdit();

    @Override
    public abstract void doRemove();

    public abstract void doDetail();

    public abstract void doSearch();

    public abstract void doCommit();

    protected CommandConfigurer getCommandConfigurer() {
        return AxApp.applicationConfig().commandConfigurer();
    }

    @Override
    public ActionCommand getSharedRefresh() {

        if (refreshCommand == null) {
            refreshCommand = new ActionCommand(SHARED_REFRESH) {

                @Override
                protected void doExecuteCommand() {
                    doRefresh();
                }
            };
            getCommandConfigurer().configure(refreshCommand);
        }
        return refreshCommand;
    }

    @Override
    public ActionCommand getSharedSearch() {

        if (searchCommand == null) {
            searchCommand = new ActionCommand(SHARED_SEARCH) {

                @Override
                protected void doExecuteCommand() {
                    doSearch();
                }
            };
            getCommandConfigurer().configure(searchCommand);
        }
        return searchCommand;
    }

    @Override
    public ActionCommand getSharedAdd() {
        if (addCommand == null) {
            addCommand = new ActionCommand(SHARED_ADD) {

                @Override
                protected void doExecuteCommand() {
                    doAdd();
                }
            };
            getCommandConfigurer().configure(addCommand);
        }
        return addCommand;
    }

    @Override
    public ActionCommand getSharedEdit() {
        if (editCommand == null) {
            editCommand = new ActionCommand(SHARED_EDIT) {

                @Override
                protected void doExecuteCommand() {
                    doEdit();
                }
            };
            getCommandConfigurer().configure(editCommand);
        }
        return editCommand;
    }

    @Override
    public ActionCommand getSharedRemove() {
        if (removeCommand == null) {
            removeCommand = new ActionCommand(SHARED_REMOVE) {

                @Override
                protected void doExecuteCommand() {
                    doRemove();
                }
            };

            getCommandConfigurer().configure(removeCommand);
        }
        return removeCommand;
    }

    @Override
    public ActionCommand getSharedDetail() {
        if (detailCommand == null) {
            detailCommand = new ActionCommand(SHARED_DETAIL) {

                @Override
                protected void doExecuteCommand() {
                    doDetail();
                }
            };
            getCommandConfigurer().configure(detailCommand);
        }
        return detailCommand;
    }

    @Override
    public ActionCommand getSharedCommit() {
        if (commitCommand == null) {
            commitCommand = new ActionCommand(SHARED_COMMIT) {

                @Override
                protected void doExecuteCommand() {
                    doCommit();
                }
            };
            getCommandConfigurer().configure(commitCommand);
        }
        return commitCommand;
    }
}
