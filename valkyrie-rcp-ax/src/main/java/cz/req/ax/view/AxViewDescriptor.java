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

package cz.req.ax.view;

import org.valkyriercp.application.ApplicationWindow;
import org.valkyriercp.application.PageComponent;
import org.valkyriercp.application.View;
import org.valkyriercp.command.support.ActionCommand;
import org.valkyriercp.widget.Widget;

/**
 * AxViewDescriptor
 *
 * @author Ondrej Burianek
 */
public class AxViewDescriptor extends AbstractViewDescriptor {

    protected org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
    Class clazz;
    View view;

    public AxViewDescriptor() {
    }

    public AxViewDescriptor(Class clazz) {
        this.clazz = clazz;
    }

    public AxViewDescriptor(View view) {
        setView(view);
    }

    public AxViewDescriptor(Widget widget) {
        setWidget(widget);
    }

    public AxViewDescriptor(String id, Class clazz) {
        super(id);
        this.clazz = clazz;
    }

    public AxViewDescriptor(String id, View view) {
        super(id);
        setView(view);
    }

    public AxViewDescriptor(String id, Widget widget) {
        super(id);
        setWidget(widget);
    }

    public void setView(View view) {
        this.view = view;
    }

    public void setWidget(Widget widget) {
        view = new AxWidgetView(widget);
        view.setDescriptor(this);
    }

    @Override
    public PageComponent createPageComponent() {
        if (view == null && clazz == null) {
            throw new IllegalArgumentException("PageComponent " + getId() + " needs not null view,widget or clazz");
        }
        if (view != null) {
            view.setDescriptor(this);
            return view;
        } else {
            Object object;
            try {
                object = clazz.newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
            View instance = null;
            if (object instanceof View) {
                instance = (View) object;
            } else if (object instanceof Widget) {
                instance = new AxWidgetView((Widget) object);
            } else {
                throw new UnsupportedOperationException("Unsupported class " + object.getClass());
            }
            instance.setDescriptor(this);
            return instance;
        }
    }

    @Override
    public ActionCommand createShowViewCommand(ApplicationWindow window) {
        return super.createShowViewCommand(window);
    }
//    public void setViewInstance(AxView viewInstance) {
//        this.viewInstance = viewInstance;
//    }
//    @Override
//    protected View createView() {
//        if (view != null) {
//            return view;
//        }
//        Assert.state(getViewClass() != null, "View class to instantiate is not set");
//        Object o = applicationContext.getAutowireCapableBeanFactory().createBean(getViewClass());
//        Assert.isTrue((o instanceof View), "View class '" + getViewClass()
//                + "' was instantiated, but instance is not a View!");
//        View inst = (View) o;
//        inst.setDescriptor(this);
////        if (viewProperties != null) {
////            BeanWrapper wrapper = new BeanWrapperImpl(view);
////            wrapper.setPropertyValues(viewProperties);
////        }
//
//        if (inst instanceof InitializingBean) {
//            try {
//                ((InitializingBean) inst).afterPropertiesSet();
//            } catch (Exception e) {
//                throw new BeanInitializationException("Problem running on " + inst, e);
//            }
//        }
//        return inst;
//    }
}
