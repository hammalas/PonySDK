/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
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

package com.ponysdk.ui.server.list2;

import com.google.gwt.user.cellview.client.DataGrid;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.list2.renderer.cell.CellRenderer;
import com.ponysdk.ui.server.list2.renderer.header.HeaderCellRenderer;
import com.ponysdk.ui.server.list2.valueprovider.ValueProvider;

/**
 * Defines a {@link DataGrid} column
 * 
 * @param <D>
 * @param <V>
 */
public class DataGridColumnDescriptor<D, V> {

    protected HeaderCellRenderer headerCellRenderer;
    protected CellRenderer<V> cellRenderer;
    protected CellRenderer<V> subCellRenderer;
    protected ValueProvider<D, V> valueProvider;

    public void setHeaderCellRenderer(final HeaderCellRenderer headerCellRender) {
        this.headerCellRenderer = headerCellRender;
    }

    public HeaderCellRenderer getHeaderCellRenderer() {
        return headerCellRenderer;
    }

    public void setCellRenderer(final CellRenderer<V> cellRenderer) {
        this.cellRenderer = cellRenderer;
    }

    public CellRenderer<V> getCellRenderer() {
        return cellRenderer;
    }

    public void setValueProvider(final ValueProvider<D, V> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public ValueProvider<D, V> getValueProvider() {
        return valueProvider;
    }

    public CellRenderer<V> getSubCellRenderer() {
        return subCellRenderer;
    }

    public void setSubCellRenderer(final CellRenderer<V> subCellRenderer) {
        this.subCellRenderer = subCellRenderer;
    }

    public IsPWidget renderCell(final int row, final D data) {
        if (cellRenderer == null) throw new IllegalArgumentException("CellRenderer is required");
        if (valueProvider == null) throw new IllegalArgumentException("ValueProvider is required");
        return cellRenderer.render(row, valueProvider.getValue(data));
    }

    public IsPWidget renderSubCell(final int row, final D data) {
        if (subCellRenderer == null) throw new IllegalArgumentException("SubCellRenderer is required");
        if (valueProvider == null) throw new IllegalArgumentException("ValueProvider is required");
        return subCellRenderer.render(row, valueProvider.getValue(data));
    }
}