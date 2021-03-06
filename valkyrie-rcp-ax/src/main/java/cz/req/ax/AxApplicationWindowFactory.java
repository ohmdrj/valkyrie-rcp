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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.ApplicationWindowFactory;
import org.valkyriercp.application.config.ApplicationConfig;

/**
 * @author Ondrej Burianek
 */
@Configurable
public class AxApplicationWindowFactory implements ApplicationWindowFactory {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    @Override
    public ApplicationWindow createApplicationWindow() {
        AxApplicationWindow window = new AxApplicationWindow(applicationConfig);
        return window;
    }
}
