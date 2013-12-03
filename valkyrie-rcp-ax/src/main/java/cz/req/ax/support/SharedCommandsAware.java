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

import org.valkyriercp.command.support.ActionCommand;

/**
 * @author Ondrej Burianek
 */
public interface SharedCommandsAware {

    public static final String SHARED_REFRESH = "sharedRefreshCommand";
    public static final String SHARED_SEARCH = "sharedSearchCommand";
    public static final String SHARED_ADD = "sharedAddCommand";
    public static final String SHARED_EDIT = "sharedEditCommand";
    public static final String SHARED_REMOVE = "sharedRemoveCommand";
    public static final String SHARED_DETAIL = "sharedDetailCommand";
    public static final String SHARED_COMMIT = "sharedCommitCommand";

    public ActionCommand getSharedRefresh();

    public ActionCommand getSharedSearch();

    public ActionCommand getSharedAdd();

    public ActionCommand getSharedEdit();

    public ActionCommand getSharedRemove();

    public ActionCommand getSharedDetail();

    public ActionCommand getSharedCommit();

}
