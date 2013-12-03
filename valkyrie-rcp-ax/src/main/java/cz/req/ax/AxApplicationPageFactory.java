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

import org.springframework.util.Assert;
import org.valkyriercp.application.ApplicationPage;
import org.valkyriercp.application.ApplicationPageFactory;
import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageDescriptor;
import org.valkyriercp.application.support.AbstractApplicationPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationPageFactory implements ApplicationPageFactory {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private HashMap<Class<? extends PageDescriptor>, ApplicationPageFactory> factoryRegistry =
            new HashMap<Class<? extends PageDescriptor>, ApplicationPageFactory>();

    public AxApplicationPageFactory() {
        factoryRegistry.put(AxApplicationNaviPageDescriptor.class, new GenericApplicationPageFactory(AxApplicationNaviPage.class));
        factoryRegistry.put(AxApplicationPageDescriptor.class, new GenericApplicationPageFactory(AxApplicationPage.class));
//        factoryRegistry.put(TestPageDescriptor.class, new GenericApplicationPageFactory(TestPage.class));
    }

    public void setFactoryRegistry(Map map) {
        Assert.notNull(map);
        for (Object key : map.keySet()) {
            if (!key.getClass().isAssignableFrom(PageDescriptor.class)) {
                continue;
            }
            Object value = map.get(key);
            if (value instanceof ApplicationPageFactory) {
                factoryRegistry.put((Class<PageDescriptor>) key, (ApplicationPageFactory) value);
            }
            if (value instanceof Class && value.getClass().isAssignableFrom(AbstractApplicationPage.class)) {
                factoryRegistry.put((Class<PageDescriptor>) key, new GenericApplicationPageFactory((Class<AbstractApplicationPage>) value));
            }
        }
    }

    public ApplicationPageFactory resolveFactory(PageDescriptor descriptor) {
        //TODO Redundant?
        ApplicationPageFactory factory = null;
        if (descriptor instanceof AxApplicationPageDescriptor) {
            AxApplicationPageDescriptor axDescriptor = (AxApplicationPageDescriptor) descriptor;
            if (axDescriptor.getPageClass() != null) {
                factory = new GenericApplicationPageFactory(axDescriptor.getPageClass());
            }
        } else {
            factory = new GenericApplicationPageFactory(AxApplicationPage.class);
        }
        if (factory == null) {
            for (Class<? extends PageDescriptor> c : factoryRegistry.keySet()) {
                if (c.equals(descriptor.getClass())) {
                    factory = factoryRegistry.get(c);
                } else if (factory == null && c.isAssignableFrom(descriptor.getClass())) {
                    factory = factoryRegistry.get(c);
                }
            }
        }
        return factory;
    }

    @Override
    public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
        ApplicationPageFactory factory = resolveFactory(descriptor);
        Assert.notNull(factory, "Cannot resolve ApplicationPageFactory from " + descriptor.getClass());
//        log.info("Descriptor " + descriptor + " > " + factory);
        ApplicationPage page = factory.createApplicationPage(window, descriptor);

        //MYCONFIGURE page
//        getObjectConfigurer().configure(page, page.getId());

        return page;
    }

//    protected ApplicationObjectConfigurer getObjectConfigurer() {
//        return (ApplicationObjectConfigurer) Application.services().getService(ApplicationObjectConfigurer.class);
//    }

    public class GenericApplicationPageFactory implements ApplicationPageFactory {

        public GenericApplicationPageFactory(Class<? extends AbstractApplicationPage> clazz) {
            this.clazz = clazz;
        }

        private Class<? extends AbstractApplicationPage> clazz;

        @Override
        public ApplicationPage createApplicationPage(ApplicationWindow window, PageDescriptor descriptor) {
            try {
                AbstractApplicationPage page = clazz.newInstance();
                page.setApplicationWindow(window);
                page.setDescriptor(descriptor);
                return page;
            } catch (Exception ex) {
                String message = "Error instantializing page from " + clazz;
                log.error(message, ex);
                throw new RuntimeException(message, ex);
            }
        }

        @Override
        public String toString() {
            return "GenericApplicationPageFactory for " + clazz;
        }
    }
}
