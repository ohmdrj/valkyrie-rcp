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

package cz.req.ax.data.binding;

import cz.req.ax.widget.editor.AxEditorWidget;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.widget.table.Accessor;
import org.valkyriercp.widget.table.ClassUtils;

import java.awt.*;
import java.util.Map;

/**
 * @author Ondrej Burianek
 */
//TODO @Deprecated?
public class AxEditorBinder extends AxAbstractLookupBinder {

    protected AxEditorWidget dataEditor;
    protected String field;

    public AxEditorBinder() {
        this(null, null);
    }

    public AxEditorBinder(AxEditorWidget dataEditor, String field) {
        super(null);
        this.dataEditor = dataEditor;
        this.field = field;
        setSelectDialogCommandId("foreignKeyCommand");
    }

    public void setDataEditor(AxEditorWidget dataEditor) {
        this.dataEditor = dataEditor;
    }

    @Override
    protected AxEditorWidget getDataEditor() {
        return dataEditor;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldValue(Object o) {
        try {
            Accessor accessor = ClassUtils.getAccessorForProperty(o.getClass(), field);
            return (String) accessor.getValue(o);
        } catch (Exception ex) {
            ex.printStackTrace();
            return "#";
        }
    }

    @Override
    protected AxAbstractLookupBinding getLookupBinding(FormModel formModel, String formPropertyPath, Map context) {
        AxAbstractLookupBinding binding = new AxAbstractLookupBinding(getDataEditor(), formModel, formPropertyPath) {

            @Override
            public String getObjectLabel(Object o) {
                return getFieldValue(o);
            }

            @Override
            protected Object createFilterFromString(String textFieldValue) {
                return dataEditor.getCriteriaFromString(textFieldValue);
            }

            @Override
            public Dimension getDialogSize() {
                return new Dimension(420, 480);
            }

            @Override
            protected Object initializeDataEditor() {
                Object search = createFilterFromString(getKeyComponentText());
                Object result;
                if (search.getClass().equals(dataEditor.getDataProvider().getDataIdentity().getDataClass())) {
//                    result = dataEditor.setSelectedSearch(dataEditor.getDefaultCriteria());
                    result = dataEditor.setSelectedSearch(null);
                    dataEditor.setValue(search);
                } else {
                    result = dataEditor.setSelectedSearch(search);
                }
                return result;
            }
        };
        binding.setAutoPopupdialog(AxAbstractLookupBinding.AUTOPOPUPDIALOG_ALWAYS);

        return binding;
    }
}
