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

package cz.req.ax;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author Ondrej Burianek
 */
public class AxMessageSource extends ResourceBundleMessageSource {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private static String BASE_SPRING = "org.valkyriercp.messages.default";
    private static String BASE_DESKTOP = "cz.req.ax.messages";
    private String[] tempNames = new String[0];

    public AxMessageSource() {
        setDefaultEncoding("UTF-8");
        addBase(BASE_SPRING);
        addBase(BASE_DESKTOP);
    }

    public final void addBase(String basename) {
        tempNames = (String[]) ArrayUtils.addAll(new String[]{basename}, tempNames);
        setBasenames(tempNames);
    }
}
