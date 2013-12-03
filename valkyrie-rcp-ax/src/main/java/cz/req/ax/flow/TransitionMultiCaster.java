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

package cz.req.ax.flow;

import java.util.ArrayList;

/**
 * @author Ondrej Burianek
 */
public class TransitionMultiCaster implements TransitionAware {

    private ArrayList<TransitionAware> awares = new ArrayList<TransitionAware>();

    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof TransitionAware) {
            return awares.remove((TransitionAware) o);
        }
        return false;
    }

    public boolean add(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof TransitionAware) {
            return awares.add((TransitionAware) o);
        }
        return false;
    }

    @Override
    public void onTransition(TransitionCarrier carrier) {
        for (TransitionAware uta : awares) {
            uta.onTransition(carrier);
        }
    }
}
