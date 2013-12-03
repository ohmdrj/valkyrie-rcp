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

package cz.req.ax.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Burianek
 */
public class FlowDescriptor {

    private static final String EXIT = "flowExit";
    private static final String NEXT = "flowNext";
    private static final String PREV = "flowPrev";

    private Map nodes = new HashMap();

    public Object resolveTargetBean(String action) {
        return nodes.get(action);
    }

    public Map getNodes() {
        return nodes;
    }

    public void setNodes(Map nodes) {
        this.nodes = nodes;
    }
}
