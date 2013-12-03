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

package cz.req.ax.dialog;

import cz.req.ax.widget.AxWidget;
import cz.req.ax.widget.AxWidgetDialogPage;
import cz.req.ax.widget.editor.AxEditorWidget;

/**
 * @author Ondrej Burianek
 */
@Deprecated
public abstract class AxDialogWidget extends AxDialog {

    public AxDialogWidget(AxWidget widget) {
        AxWidgetDialogPage widgetPage = new AxWidgetDialogPage(widget.getId());
        widgetPage.setWidget(widget);
        if (widget instanceof AxEditorWidget) {
            ((AxEditorWidget) widget).doRefresh();
        }
        setDialogPage(widgetPage);
        throw new UnsupportedOperationException();
    }
}
