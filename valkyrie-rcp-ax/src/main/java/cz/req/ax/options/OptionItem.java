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

package cz.req.ax.options;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Ondrej Burianek
 */
public class OptionItem {

    String name;
    Object value;
    Object deflt;
    OptionType type;
    ArrayList<Object> selections;

    public OptionItem() {
    }

    public Object read(String string) {
        if (string == null) {
            return null;
        }
        if (OptionType.Boolean.equals(type)) {
            return Boolean.valueOf(string);
        }
        if (OptionType.Integer.equals(type)) {
            return Integer.valueOf(string);
        }
        if (OptionType.Directory.equals(type)) {
            if ("user.home".equals(string)) {
                string = System.getProperty("user.home");
            }
            return new File(string);
        }
        return string;
    }

    public String write(Object object) {
        if (object == null) {
            return null;
        }
        if (OptionType.Directory.equals(type)) {
            return ((File) object).getAbsolutePath();
        }
        return object.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getDeflt() {
        return deflt;
    }

    public void setDeflt(Object deflt) {
        this.deflt = deflt;
    }

    public ArrayList<Object> getSelections() {
        return selections;
    }

    public void setSelections(ArrayList<Object> selections) {
        this.selections = selections;
    }

    public OptionType getType() {
        return type;
    }

    public void setType(OptionType type) {
        this.type = type;
    }
}
