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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * AxApplicationArguments
 *
 * @author Ondrej Burianek
 */
public class AxApplicationArguments extends ArrayList<String> {

    public AxApplicationArguments(String[] args) {
        addAll(Arrays.asList(args));
    }

    /**
     * returns standard parameter value --parameterName=parameterValue
     *
     * @param parameterName
     * @return
     */
    public String get(String parameterName) {
        String value = null;
        for (String string : this) {
            try {
                if (string.startsWith("--" + parameterName + "=")) {
                    value = string.substring(string.indexOf('=') + 1);
                }
            } catch (Exception ex) {
                // Error parsing
            }
        }
        return value;
    }
}
