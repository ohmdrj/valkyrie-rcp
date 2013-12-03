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

import cz.thickset.utils.branding.ThicksetProject;
import org.valkyriercp.application.support.DefaultApplicationDescriptor;

/**
 * @author Ondrej Burianek
 */
public class ThicksetApplicationDescriptor extends DefaultApplicationDescriptor {

    public ThicksetApplicationDescriptor() {
        ThicksetProject project = ThicksetProject.instance();
        if (project != null && !project.getName().equals("Unknown")) {
            setTitle(project.getName());
            setVersion(project.getVersion());
            setDescription(project.getNamePlusVersion());
            setImage(project.getIcon().getImage());
        }
    }
}
