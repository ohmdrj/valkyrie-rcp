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

import cz.req.ax.AxApplicationPage;
import cz.req.ax.view.AxViewDescriptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.valkyriercp.application.ApplicationWindow;

/**
 * @author Ondrej Burianek
 */
public class FlowAdvisor implements ApplicationListener, ApplicationContextAware, ApplicationWindowManager {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private ApplicationContext applicationContext;
    private ApplicationWindowManager applicationWindowManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setApplicationWindowManager(ApplicationWindowManager applicationWindowOpener) {
        this.applicationWindowManager = applicationWindowOpener;
    }

    @Override
    public ApplicationWindow getWindow() {
        return applicationWindowManager.getWindow();
    }

    @Override
    public ApplicationWindow openWindow(String pageId) {
        log.info("OpenWindow " + pageId);
        ApplicationWindow window = applicationWindowManager.openWindow(pageId);
        return window;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AbstractFlowEvent) {
            log.debug("Flow " + event);
            AbstractFlowEvent flowEvent = (AbstractFlowEvent) event;
            if (flowEvent instanceof FlowShowEvent) {
                FlowShowEvent flowShowEvent = (FlowShowEvent) flowEvent;

//                Assert.isInstanceOf(AxViewDescriptor.class, flowShowEvent.getDescriptor());
                String viewId;
                Object descriptor = flowShowEvent.getDescriptor();
                if (descriptor instanceof AxViewDescriptor) {
                    viewId = ((AxViewDescriptor) flowShowEvent.getDescriptor()).getId();
                } else if (descriptor instanceof String) {
                    viewId = ((String) flowShowEvent.getDescriptor());
                } else {
                    throw new UnsupportedOperationException();
                }

//                if (viewDescriptor.getPageDescriptor() != null) {
//                    AxApplicationPageDescriptor pageDescriptor = viewDescriptor.getPageDescriptor();
//                    Assert.isInstanceOf(AxApplicationWindow.class, getWindow());
//                    AxApplicationWindow applicationWindow = (AxApplicationWindow) getWindow();
//                    applicationWindow.showPage(pageDescriptor, flowShowEvent.getInput());
//                }

                AxApplicationPage page = (AxApplicationPage) getWindow().getPage();
                page.showView(viewId, flowShowEvent.getInput());

            }
        }
    }
}
