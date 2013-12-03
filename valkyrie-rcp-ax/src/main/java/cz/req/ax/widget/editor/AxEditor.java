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


import cz.req.ax.data.AxDataProvider;
import cz.req.ax.data.AxFormBuilder;
import cz.req.ax.support.FormDesigner;
import cz.req.ax.support.TableDesigner;
import cz.req.ax.widget.AxFormFactory;
import cz.req.ax.widget.table.AxTableDescription;

/**
 * AxEditor
 *
 * @author Ondrej Burianek
 */
public abstract class AxEditor extends AxEditorWidget implements TableDesigner, FormDesigner {

    EditorType editorType;
    AxFormFactory formFactory;

    protected AxEditor() {
    }

    protected AxEditor(AxDataProvider dataProvider) {
        setDataProvider(dataProvider);
    }

    @Override
    public void createWidget() {
        AxTableDescription tableDescription = new AxTableDescription(getDataFactory());
        formFactory = new AxFormFactory(getDataFactory()) {
            @Override
            public void initForm(AxFormBuilder builder) {
                AxEditor.this.initForm(builder);
            }
        };
        initTable(tableDescription);
        setMasterTable(tableDescription);

        if (EditorType.NEWTAB.equals(editorType)) {
            super.setDetailTab(formFactory.getWidget());
        } else if (EditorType.DIALOG.equals(editorType)) {
            super.setDetailDialog(formFactory.getWidget());
        } else {
            super.setDetailEditor(formFactory.getWidget());
        }
        super.createWidget();
    }

    public EditorType getEditorType() {
        return editorType;
    }

    public void setEditorType(EditorType editorType) {
        this.editorType = editorType;
    }

    public static enum EditorType {
        SIMPLE, DIALOG, NEWTAB
    }
}
