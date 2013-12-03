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

package cz.req.ax.widget.table;

import cz.req.ax.data.RefreshListener;
import org.valkyriercp.binding.form.FormModel;
import org.valkyriercp.form.binding.swing.AbstractGlazedListsBinding;
import org.valkyriercp.widget.table.TableDescription;

/**
 * AxTableBinding
 *
 * @author Ondrej Burianek
 */
public class AxTableBinding extends AbstractGlazedListsBinding implements RefreshListener {

    AxTableDescription tableDescription;

    public AxTableBinding(FormModel formModel, String formPropertyPath) {
        super(formModel, formPropertyPath);
        setAllSupported(false);
    }

    public AxTableBinding(FormModel formModel, String formPropertyPath, AxTableDescription tableDescription) {
        super(formModel, formPropertyPath);
        this.tableDescription = tableDescription;
    }

    //    public AxTableBinding(FormModel formModel, String formPropertyPath, boolean useOriginalSortOrder) {
//        super(formModel, formPropertyPath, useOriginalSortOrder);
//    }
    @Override
    public void refresh() {
        listChanged();
    }

    public void setTableDescription(AxTableDescription tableDescription) {
        this.tableDescription = tableDescription;
    }

    @Override
    protected TableDescription getTableDescription() {
        return tableDescription;
    }

    public final void setAllSupported(boolean supported) {
        super.setAddSupported(supported);
        super.setEditSupported(supported);
        super.setRemoveSupported(supported);
        super.setShowDetailSupported(supported);
    }
}
