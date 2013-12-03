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

package cz.req.ax.options;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
public class OptionGroup {

    String name;
    List<OptionItem> options;

    public OptionGroup() {
        options = new ArrayList<OptionItem>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OptionItem> getOptions() {
        return options;
    }

    public void addOption(OptionItem option) {
        options.add(option);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" ");
        for (OptionItem item : options) {
            sb.append(" ");
            sb.append(item.getName());
            sb.append("=");
            sb.append(item.getValue());
        }
        return sb.toString();
    }
}
