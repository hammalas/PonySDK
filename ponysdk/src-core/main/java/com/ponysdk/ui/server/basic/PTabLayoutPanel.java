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

package com.ponysdk.ui.server.basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ponysdk.ui.server.basic.event.HasPAnimation;
import com.ponysdk.ui.server.basic.event.HasPBeforeSelectionHandlers;
import com.ponysdk.ui.server.basic.event.HasPSelectionHandlers;
import com.ponysdk.ui.server.basic.event.PBeforeSelectionHandler;
import com.ponysdk.ui.server.basic.event.PSelectionEvent;
import com.ponysdk.ui.server.basic.event.PSelectionHandler;
import com.ponysdk.ui.terminal.HandlerType;
import com.ponysdk.ui.terminal.PropertyKey;
import com.ponysdk.ui.terminal.WidgetType;
import com.ponysdk.ui.terminal.instruction.Add;
import com.ponysdk.ui.terminal.instruction.AddHandler;
import com.ponysdk.ui.terminal.instruction.EventInstruction;
import com.ponysdk.ui.terminal.instruction.Update;

public class PTabLayoutPanel extends PComplexPanel implements HasPAnimation, HasPBeforeSelectionHandlers<Integer>, HasPSelectionHandlers<Integer>, PSelectionHandler<Integer> {

    private boolean animationEnabled = false;

    private final List<PWidget> children = new ArrayList<PWidget>();

    private final Collection<PBeforeSelectionHandler<Integer>> beforeSelectionHandlers = new ArrayList<PBeforeSelectionHandler<Integer>>();

    private final Collection<PSelectionHandler<Integer>> selectionHandlers = new ArrayList<PSelectionHandler<Integer>>();

    private Integer selectedItemIndex;

    public PTabLayoutPanel() {
        addSelectionHandler(this);
    }

    @Override
    protected WidgetType getType() {
        return WidgetType.TAB_LAYOUT_PANEL;
    }

    public void insert(final IsPWidget widget, final String tabText, final int beforeIndex) {
        insert(asWidgetOrNull(widget), tabText, beforeIndex);
    }

    public void insert(final IsPWidget widget, final IsPWidget tabWidget, final int beforeIndex) {
        insert(asWidgetOrNull(widget), asWidgetOrNull(tabWidget), beforeIndex);
    }

    public void insert(final PWidget widget, final PWidget tabWidget, final int beforeIndex) {
        // Detach new child.
        widget.removeFromParent();

        children.add(beforeIndex, widget);

        // Adopt.
        adopt(widget);

        final Add addWidget = new Add(widget.getID(), getID());
        addWidget.getMainProperty().setProperty(PropertyKey.BEFORE_INDEX, beforeIndex);
        addWidget.getMainProperty().setProperty(PropertyKey.TAB_WIDGET, tabWidget.getID());
        getPonySession().stackInstruction(addWidget);
    }

    public void insert(final PWidget widget, final String tabText, final int beforeIndex) {
        // Detach new child.
        widget.removeFromParent();

        children.add(beforeIndex, widget);
        // Adopt.
        adopt(widget);

        final Add addWidget = new Add(widget.getID(), getID());
        addWidget.getMainProperty().setProperty(PropertyKey.BEFORE_INDEX, beforeIndex);
        addWidget.getMainProperty().setProperty(PropertyKey.TAB_TEXT, tabText);
        getPonySession().stackInstruction(addWidget);
    }

    public void add(final IsPWidget w, final IsPWidget tabWidget) {
        add(asWidgetOrNull(w), asWidgetOrNull(tabWidget));
    }

    public void add(final IsPWidget w, final String tabText) {
        add(asWidgetOrNull(w), tabText);
    }

    public void add(final PWidget w, final String tabText) {
        insert(w, tabText, getWidgetCount());
    }

    public void add(final PWidget w, final PWidget tabWidget) {
        insert(w, tabWidget, getWidgetCount());
    }

    public PWidget getWidget(final int index) {
        return children.get(index);
    }

    public int getWidgetCount() {
        return children.size();
    }

    public int getWidgetIndex(final PWidget child) {
        return children.indexOf(child);
    }

    public void selectTab(final int index) {
        this.selectedItemIndex = index;
        final Update update = new Update(ID);
        update.setMainPropertyValue(PropertyKey.SELECTED_INDEX, index);
        getPonySession().stackInstruction(update);
    }

    @Override
    public void add(final PWidget w) {
        throw new UnsupportedOperationException("A tabText parameter must be specified with add().");
    }

    @Override
    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    @Override
    public void setAnimationEnabled(final boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        final Update update = new Update(ID);
        update.setMainPropertyValue(PropertyKey.ANIMATION, animationEnabled);
        getPonySession().stackInstruction(update);
    }

    @Override
    public void addBeforeSelectionHandler(final PBeforeSelectionHandler<Integer> handler) {
        beforeSelectionHandlers.add(handler);
        final AddHandler addHandler = new AddHandler(getID(), HandlerType.BEFORE_SELECTION_HANDLER);
        getPonySession().stackInstruction(addHandler);
    }

    @Override
    public void removeBeforeSelectionHandler(final PBeforeSelectionHandler<Integer> handler) {
        beforeSelectionHandlers.remove(handler);
    }

    @Override
    public Collection<PBeforeSelectionHandler<Integer>> getBeforeSelectionHandlers() {
        return Collections.unmodifiableCollection(beforeSelectionHandlers);
    }

    @Override
    public void addSelectionHandler(final PSelectionHandler<Integer> handler) {
        selectionHandlers.add(handler);
        final AddHandler addHandler = new AddHandler(getID(), HandlerType.SELECTION_HANDLER);
        getPonySession().stackInstruction(addHandler);
    }

    @Override
    public void removeSelectionHandler(final PSelectionHandler<Integer> handler) {
        selectionHandlers.remove(handler);
    }

    @Override
    public Collection<PSelectionHandler<Integer>> getSelectionHandlers() {
        return Collections.unmodifiableCollection(selectionHandlers);
    }

    @Override
    public void onEventInstruction(final EventInstruction eventInstruction) {
        final HandlerType handlerType = eventInstruction.getType();
        if (HandlerType.SELECTION_HANDLER.equals(handlerType)) {
            for (final PSelectionHandler<Integer> handler : getSelectionHandlers()) {
                final PSelectionEvent<Integer> selection = new PSelectionEvent<Integer>(this, eventInstruction.getMainProperty().getIntValue());
                handler.onSelection(selection);
            }
        } else if (HandlerType.BEFORE_SELECTION_HANDLER.equals(handlerType)) {
            for (final PBeforeSelectionHandler<Integer> handler : getBeforeSelectionHandlers()) {
                handler.onBeforeSelection(eventInstruction.getMainProperty().getIntValue());
            }
        } else {
            super.onEventInstruction(eventInstruction);
        }
    }

    @Override
    public void onSelection(final PSelectionEvent<Integer> event) {
        selectedItemIndex = event.getSelectedItem();
    }

    public Integer getSelectedItemIndex() {
        return selectedItemIndex;
    }

}
