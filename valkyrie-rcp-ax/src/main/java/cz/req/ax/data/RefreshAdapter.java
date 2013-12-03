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

package cz.req.ax.data;

import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;

/**
 * @author Ondrej Burianek
 */
public class RefreshAdapter {

    public RefreshAdapter() {
    }

    public RefreshAdapter(Class clazz) {
        this.clazz = clazz;
    }

    private Class clazz;
    private ArrayList<RefreshListener> listeners = new ArrayList<RefreshListener>();

    public boolean removeRefreshListener(RefreshListener e) {
        return listeners.remove(e);
    }

    public boolean addRefreshListener(RefreshListener e) {
        boolean ret = listeners.add(e);
        return ret;

    }

    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof RefreshEvent) {
            if (clazz == null) {
                fireRefresh();
            } else if (((RefreshEvent) event).getClazz().equals(clazz)) {
                fireRefresh();
            }
        }
    }

    public void fireRefresh() {
        for (RefreshListener l : listeners) {
            l.refresh();
        }
    }
}