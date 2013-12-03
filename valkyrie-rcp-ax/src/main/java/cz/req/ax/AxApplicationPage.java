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

import cz.req.ax.flow.*;
import org.springframework.context.ApplicationListener;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.PageComponentDescriptor;
import org.valkyriercp.application.View;
import org.valkyriercp.application.ViewDescriptor;
import org.valkyriercp.application.support.DefaultApplicationPage;
import org.valkyriercp.application.support.DefaultViewContext;

/**
 * @author Ondrej Burianek
 */
public class AxApplicationPage extends DefaultApplicationPage implements FlowAware, TransitionAware {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    private TransitionMultiCaster transitionMultiCaster = new TransitionMultiCaster();
    private Object input;
    private Long timestamp;

    public AxApplicationPage() {
    }

    @Override
    public String getId() {
        String id = super.getId();
        if (getPageDescriptor() instanceof AxApplicationPageDescriptor) {
            AxApplicationPageDescriptor axDescriptor = (AxApplicationPageDescriptor) getPageDescriptor();
            if (!axDescriptor.isSingleton() && timestamp == null) {
                timestamp = System.currentTimeMillis();
            }
        }
        return timestamp == null ? id : id + timestamp;
    }

    @Override
    public AxApplicationPageDescriptor getPageDescriptor() {
        return (AxApplicationPageDescriptor) super.getPageDescriptor();
    }

    @Override
    protected ViewDescriptor getViewDescriptor(String viewDescriptorId) {
        ViewDescriptor viewDescriptor = super.getViewDescriptor(viewDescriptorId);
        if (viewDescriptor == null) {
            log.error("ViewDescriptor " + viewDescriptorId + " not found !!!");
            throw new UnsupportedOperationException("ViewDescriptor " + viewDescriptorId + " not found !!!");
        }
        return viewDescriptor;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    @Override
    public View showView(String id) {
        if (getActiveComponent() != null) {
            close(getActiveComponent());
        }
        return super.showView(id, input);
    }

    @Override
    public View showView(ViewDescriptor viewDescriptor, Object input) {
        return super.showView(viewDescriptor, input);
    }

    public void focusLoose() {
        fireFocusLost(getActiveComponent());
    }

    public void focusGain() {
        giveFocusTo(getActiveComponent());
        fireFocusGained(getActiveComponent());
    }

    @Override
    protected PageComponent createPageComponent(PageComponentDescriptor descriptor) {
        PageComponent pageComponent = descriptor.createPageComponent();
        if (pageComponent.getContext() == null) {
            pageComponent.setContext(new DefaultViewContext(this, createPageComponentPane(pageComponent)));
            if (pageComponent instanceof ApplicationListener && getApplicationEventMulticaster() != null) {
                getApplicationEventMulticaster().addApplicationListener((ApplicationListener) pageComponent);
            }
        }
        //MYCONFIGURE view
//        getObjectConfigurer().configure(pageComponent, descriptor.getId());
        return pageComponent;
    }

    @Override
    public void onTransition(TransitionCarrier carrier) {
        transitionMultiCaster.onTransition(carrier);
    }

    @Override
    protected void fireFocusGained(PageComponent component) {
//        System.out.println("  ~P~ F ++" + getPageDescriptor().getId() + ":" + component.getId());
        super.fireFocusGained(component);
    }

    @Override
    protected void fireFocusLost(PageComponent component) {
//        System.out.println("  ~P~ F --" + getPageDescriptor().getId() + ":" + component.getId());
        super.fireFocusLost(component);
    }

    @Override
    protected void fireOpened(PageComponent component) {
//        System.out.println("  =P= C ++" + getPageDescriptor().getId() + ":" + component.getId());
        super.fireOpened(component);
    }

    @Override
    protected void fireClosed(PageComponent component) {
//        System.out.println("  =P= C --" + getPageDescriptor().getId() + ":" + component.getId());
        super.fireClosed(component);
    }

    @Override
    protected PageComponent findPageComponent(String viewDescriptorId) {
        PageComponent pageComponent = super.findPageComponent(viewDescriptorId);
        return pageComponent;
    }

    @Override
    protected void doAddPageComponent(PageComponent pageComponent) {
        transitionMultiCaster.add(pageComponent);
        super.doAddPageComponent(pageComponent);
    }

    @Override
    protected void doRemovePageComponent(PageComponent pageComponent) {
        transitionMultiCaster.remove(pageComponent);
        super.doRemovePageComponent(pageComponent);
    }

    @Override
    public FlowDescriptor getFlowDescriptor() {
        return ((FlowAware) getPageDescriptor()).getFlowDescriptor();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " id=" + getId();
    }
}
