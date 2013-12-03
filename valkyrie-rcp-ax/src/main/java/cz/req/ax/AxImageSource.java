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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.valkyriercp.image.AwtImageResource;
import org.valkyriercp.image.ImageSource;
import org.valkyriercp.image.NoSuchImageResourceException;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Ondrej Burianek
 */
@org.springframework.stereotype.Component
public class AxImageSource implements ImageSource {

    @Autowired
    ResourceLoader resourceLoader;
    AxImageResourceMap imageResources;
    HashMap<Resource, Image> imageCache;
    AwtImageResource brokenResource;
    Image brokenImage;


    public AxImageSource() {
        imageResources = new AxImageResourceMap();
        imageCache = new HashMap<Resource, Image>();
        try {
            brokenResource = new AwtImageResource(new ClassPathResource("org/valkyriercp/images/alert/error_ov.gif"));
            brokenImage = brokenResource.getImage();
            Assert.notNull(brokenImage);
        } catch (Exception ex) {
            //System.err.println("Unable to load broken image resource at '" + brokenResource + "'");
            throw new RuntimeException("Unable to load broken image resource at '" + brokenResource + "'");
        }
    }

    @Override
    public Image getImage(String key) {
        Assert.notNull(key);
        AwtImageResource resource = getImageResource(key);
        try {
            Image image = imageCache.get(resource);
            if (image == null) {
                image = resource.getImage();
                imageCache.put(resource, image);
            }
            Assert.notNull(image);
            return image;
        } catch (Exception e) {
            return brokenImage;
        }
    }

    @Override
    public AwtImageResource getImageResource(String key) {
        Assert.notNull(key);
        Resource resource = null;
        final Object tmp = imageResources.get(key);
        if (tmp instanceof Resource)
            resource = (Resource) tmp;
        if (tmp instanceof String) {
            resource = resourceLoader.getResource((String) tmp);
            Assert.notNull(resourceLoader, "Resource loader must be set to resolve resources");
        }
        if (resource == null) {
            throw new NoSuchImageResourceException(key);
        }
        try {
            resource.getInputStream();
            return new AwtImageResource(resource);
        } catch (IOException e) {
            if (brokenResource == null) {
                throw new NoSuchImageResourceException(resource, e);
            }
            System.err.println("Unable to load image resource at '" + resource + "'");
            return brokenResource;
        }
    }

    public void addPackage(String fileBase, String iconsPackage) {
        imageResources.addPackage(fileBase, iconsPackage);
    }

    public void addPackage(String fileBase) {
        imageResources.addPackage(fileBase);
    }
};
