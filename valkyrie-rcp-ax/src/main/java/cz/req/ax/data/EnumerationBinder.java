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

package cz.req.ax.data;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.util.Assert;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.AbstractListBinding;
import org.valkyriercp.form.binding.swing.ComboBoxBinder;
import org.valkyriercp.form.binding.swing.ComboBoxBinding;
import org.valkyriercp.list.TextValueListRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Ondrej Burianek
 */
@Configurable
public class EnumerationBinder extends ComboBoxBinder {

    public EnumerationBinder() {
        super();
    }

    @Override
    protected AbstractListBinding createListBinding(JComponent control, FormModel formModel, String formPropertyPath) {
        ComboBoxBinding binding = (ComboBoxBinding) super.createListBinding(control, formModel, formPropertyPath);
        binding.setSelectableItems(createSelectableItems(formModel, formPropertyPath));
        binding.setRenderer(new EnumListRenderer());
//        binding.setEditor(new EnumComboBoxEditor(binding.getEditor()));
        return binding;
    }

    protected Object[] createSelectableItems(FormModel formModel, String formPropertyPath) {
        Class<?> propertyType = getPropertyType(formModel, formPropertyPath);
        try {
            Method method;
            try {
                method = propertyType.getMethod("values");
            } catch (NoSuchMethodException ex) {
                method = propertyType.getMethod("getValues");
            }
            Object result = method.invoke(null);
            if (result instanceof List) {
                return ((List<?>) result).toArray();
            } else {
                return (Object[]) result;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Object[]{"!EnumError"};
        }
    }

    public class EnumListRenderer extends TextValueListRenderer {

        @Override
        protected String getTextValue(Object value) {
            if (value == null) {
                return "";
            }
            try {
//            return messageSourceAccessor.getMessage(valueClass.getName() + "." + valueEnum.name());
                /*Method method;
                try {
                    method = value.getClass().getMethod("valueOf",Str);
                } catch (NoSuchMethodException ex) {
                    method = value.getClass().getMethod("getValue");
                }
                return (String) method.invoke(value);*/
                return value.toString();
            } catch (Exception ex) {
                ex.printStackTrace();
                return "!EnumError";
            }
        }
    }

    public class EnumComboBoxEditor implements ComboBoxEditor {

        private Object current;
        private ComboBoxEditor inner;

        public EnumComboBoxEditor(ComboBoxEditor editor) {
            Assert.notNull(editor, "Editor cannot be null");
            this.inner = editor;
        }

        @Override
        public void selectAll() {
            inner.selectAll();
        }

        @Override
        public Component getEditorComponent() {
            return inner.getEditorComponent();
        }

        @Override
        public void addActionListener(ActionListener l) {
            inner.addActionListener(l);
        }

        @Override
        public void removeActionListener(ActionListener l) {
            inner.removeActionListener(l);
        }

        @Override
        public Object getItem() {
            return current;
        }

        @Override
        public void setItem(Object value) {
            current = value;
            /*if (value != null) {
                try {
                    String string = (String) value.getClass().getMethod("getValue").invoke(value);
                    inner.setItem(string);
                } catch (Exception ex) {
                    inner.setItem(null);
                }
//                inner.setItem(messageSourceAccessor.getMessage(valueClass.getName() + "." + valueEnum.name()));
            } else {
                inner.setItem(null);
            }*/
        }
    }
}
