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

package cz.req.ax.widget.editor;

import cz.req.ax.AxApp;
import cz.req.ax.AxApplicationPage;
import cz.req.ax.AxApplicationPageDescriptor;
import cz.req.ax.view.AxViewDescriptor;
import cz.req.ax.widget.AxFormWidget;
import org.valkyriercp.application.ApplicationWindow;

/**
 * AxEditor1Widget
 *
 * @author Ondrej Burianek
 */
public class AxDetailTab extends AxAbstractDetail {

    private AxFormWidget formCreate;
    private AxFormWidget formUpdate;

    public AxFormWidget getFormCreate() {
        return formCreate;
    }

    public void setFormCreate(AxFormWidget formCreate) {
        delWidget(this.formCreate);
        this.formCreate = formCreate;
        addWidget(formCreate);
    }

    public AxFormWidget getFormUpdate() {
        return formUpdate;
    }

    public void setFormUpdate(AxFormWidget formUpdate) {
        delWidget(this.formUpdate);
        this.formUpdate = formUpdate;
        addWidget(formUpdate);
    }

    @Override
    public AxFormWidget getFormWidget() {
        if (isModeNew()) {
            return formCreate;
        } else {
            return formUpdate == null ? formCreate : formUpdate;
        }
    }

    @Override
    public void createWidget() {
    }

    @Override
    public void setDetailObject(Object object) throws CloseCanceled {
        if (object == null) {
            getDetailForm().setFormObject(null);
        } else {
            object = getDataProvider().checkDetailObject(object);
            if (object.getClass().isAssignableFrom(getDataProvider().getDataIdentity().getDataClass())) {
                getDetailForm().setFormObject(object);
            }
        }
    }

    @Override
    public void grabFocus() {
        ApplicationWindow window = AxApp.applicationConfig().windowManager().getActiveWindow();
        AxViewDescriptor viewDescriptor = new AxViewDescriptor("testItemView", getFormWidget());
        AxApplicationPage applicationPage = new AxApplicationPage();
        AxApplicationPageDescriptor pageDescriptor = new AxApplicationPageDescriptor();
        pageDescriptor.setId("testItemPage");
        applicationPage.setDescriptor(pageDescriptor);
        window.showPage(applicationPage);
        applicationPage.showView(viewDescriptor);

        //window.getPage().showView(viewDescriptor);
//        window.getPage().showView("")
        //window.getPage().getActiveComponent().

//        TitledPageApplicationDialog dialog = new TitledPageApplicationDialog(getFormWidget().getForm(), getApplicationWindow().getControl()) {
//
//            @Override
//            protected boolean onFinish() {
//                doCommit();
//                return true;
//            }
//
//            @Override
//            protected void onCancel() {
//                doRevert();
//                super.onCancel();
//            }
//        };
//        dialog.setLocationRelativeTo(getApplicationWindow().getControl());
//        dialog.showDialog();

    }
}