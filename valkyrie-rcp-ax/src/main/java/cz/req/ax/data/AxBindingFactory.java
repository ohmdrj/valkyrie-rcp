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

package cz.req.ax.data;

import org.springframework.beans.support.PropertyComparator;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.binding.form.support.DefaultFormModel;
import org.valkyriercp.form.binding.swing.ComboBoxBinding;
import org.valkyriercp.form.binding.swing.SwingBindingFactory;
import org.valkyriercp.list.BeanPropertyValueComboBoxEditor;
import org.valkyriercp.list.BeanPropertyValueListRenderer;
import org.valkyriercp.rules.closure.Closure;

/**
 * BindingFactory
 *
 * @author Ondrej Burianek
 */
public class AxBindingFactory extends SwingBindingFactory {

    public AxBindingFactory(FormModel formModel) {
        super(formModel);
        DefaultFormModel x = (DefaultFormModel) formModel;
    }

    //    public TableLookupBoxBinding bindTableLookupBox(String formProperty, String renderedProperty, Closure selectableItemsRetriever) {
//        TableLookupBoxBinding binding = new TableLookupBoxBinding(getFormModel(), formProperty);
//
//        return binding;
//    }
    public ComboBoxBinding bindComboBox(String formProperty, String renderedProperty, Closure selectableItemsRetriever) {
        ComboBoxBinding binding = new ComboBoxBinding(getFormModel(), formProperty);
        binding.setRenderer(new BeanPropertyValueListRenderer(renderedProperty));
        binding.setEditor(new BeanPropertyValueComboBoxEditor(binding.getEditor(), renderedProperty));
        binding.setComparator(new PropertyComparator(renderedProperty, true, true));
        binding.setSelectableItems(selectableItemsRetriever.call(null));
        return binding;
    }
}
