/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *  Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *  Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.ui.server.basic;

import com.ponysdk.ui.terminal.Dictionnary.PROPERTY;
import com.ponysdk.ui.terminal.WidgetType;

public class PChosenListBox extends PListBox {

    private String placeholder;

    public PChosenListBox() {
        this(false, false);
    }

    public PChosenListBox(final boolean containsEmptyItem) {
        this(containsEmptyItem, false);
    }

    public PChosenListBox(final boolean containsEmptyItem, final boolean isMultipleSelect) {
        super(containsEmptyItem, isMultipleSelect);
    }

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.CHOSEN_LISTBOX;
    }

    public void setPLaceholderText(final String placeholder) {
        this.placeholder = placeholder;
        stackUpdate(PROPERTY.PLACEHOLDER, placeholder);
    }

    public String getPlaceholder() {
        return placeholder;
    }

}
