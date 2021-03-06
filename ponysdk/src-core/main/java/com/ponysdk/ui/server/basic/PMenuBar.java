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
import java.util.List;

import com.ponysdk.core.instruction.Add;
import com.ponysdk.core.instruction.Remove;
import com.ponysdk.core.instruction.Update;
import com.ponysdk.core.stm.Txn;
import com.ponysdk.impl.theme.PonySDKTheme;
import com.ponysdk.ui.server.basic.event.HasPAnimation;
import com.ponysdk.ui.terminal.Dictionnary.PROPERTY;
import com.ponysdk.ui.terminal.WidgetType;

/**
 * A standard menu bar widget. A menu bar can contain any number of menu items, each of which can either fire
 * a {@link PCommand} or open a cascaded menu bar. <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-MenuBar</dt>
 * <dd>the menu bar itself</dd>
 * <dt>.gwt-MenuBar-horizontal</dt>
 * <dd>dependent style applied to horizontal menu bars</dd>
 * <dt>.gwt-MenuBar-vertical</dt>
 * <dd>dependent style applied to vertical menu bars</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItem</dt>
 * <dd>menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItem-selected</dt>
 * <dd>selected menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItemSeparator</dt>
 * <dd>section breaks between menu items</dd>
 * <dt>.gwt-MenuBar .gwt-MenuItemSeparator .menuSeparatorInner</dt>
 * <dd>inner component of section separators</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopLeft</dt>
 * <dd>the top left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopCenter</dt>
 * <dd>the top center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopRight</dt>
 * <dd>the top right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupTopRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleLeft</dt>
 * <dd>the middle left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleCenter</dt>
 * <dd>the middle center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleRight</dt>
 * <dd>the middle right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupMiddleRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomLeft</dt>
 * <dd>the bottom left cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomLeftInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomCenter</dt>
 * <dd>the bottom center cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomCenterInner</dt>
 * <dd>the inner element of the cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomRight</dt>
 * <dd>the bottom right cell</dd>
 * <dt>.gwt-MenuBarPopup .menuPopupBottomRightInner</dt>
 * <dd>the inner element of the cell</dd>
 * </dl>
 * <p>
 * MenuBar elements in UiBinder template files can have a <code>vertical</code> boolean attribute (which
 * defaults to false), and may have only MenuItem elements as children. MenuItems may contain HTML and
 * MenuBars.
 * </p>
 */
public class PMenuBar extends PWidget implements HasPAnimation {

    // TODO warning : gwt contains 2 list 1 all items (with separator) + 1 menuItem only
    private final List<PWidget> items = new ArrayList<PWidget>();

    private boolean animationEnabled = false;
    private final boolean vertical;

    public PMenuBar() {
        this(false);
    }

    public PMenuBar(final boolean vertical) {
        super();
        this.vertical = vertical;
        addStyleName(PonySDKTheme.MENUBAR); // TODO nciaravola must be moved terminal side
        create.put(PROPERTY.MENU_BAR_IS_VERTICAL, vertical);
    }

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.MENU_BAR;
    }

    public PMenuItem addItem(final String text) {
        return addItem(new PMenuItem(text, false));
    }

    public PMenuItem addItem(final PMenuItem item) {
        return insertItem(item, items.size());
    }

    public PMenuItem addItem(final String text, final boolean asHTML, final PCommand cmd) {
        return addItem(new PMenuItem(text, asHTML, cmd));
    }

    public PMenuItem addItem(final String text, final boolean asHTML, final PMenuBar popup) {
        return addItem(new PMenuItem(text, asHTML, popup));
    }

    public PMenuItem addItem(final String text, final PCommand cmd) {
        return addItem(new PMenuItem(text, cmd));
    }

    public PMenuItem addItem(final String text, final PMenuBar popup) {
        return addItem(new PMenuItem(text, popup));
    }

    public PWidget getItem(final int index) {
        return items.get(index);
    }

    public PMenuItem insertItem(final PMenuItem item, final int beforeIndex) throws IndexOutOfBoundsException {
        items.add(beforeIndex, item);
        final Add add = new Add(item.getID(), getID());
        add.put(PROPERTY.BEFORE_INDEX, beforeIndex);
        Txn.get().getTxnContext().save(add);
        return item;
    }

    public boolean removeItem(final int index) {
        final PWidget item = items.remove(index);
        final Remove remove = new Remove(item.getID(), getID());
        Txn.get().getTxnContext().save(remove);
        return true;
    }

    public boolean removeItem(final PMenuItem item) {
        final boolean removed = items.remove(item);
        if (removed) {
            final Remove remove = new Remove(item.getID(), getID());
            Txn.get().getTxnContext().save(remove);
        }
        return removed;
    }

    public boolean removeItem(final PMenuItemSeparator item) {
        final boolean removed = items.remove(item);
        if (removed) {
            final Remove remove = new Remove(item.getID(), getID());
            Txn.get().getTxnContext().save(remove);
        }
        return removed;
    }

    public void addSeparator() {
        addSeparator(new PMenuItemSeparator());
    }

    public PMenuItemSeparator addSeparator(final PMenuItemSeparator itemSeparator) {
        return insertSeparator(itemSeparator, items.size());
    }

    public PMenuItemSeparator insertSeparator(final PMenuItemSeparator itemSeparator, final int beforeIndex) throws IndexOutOfBoundsException {
        items.add(beforeIndex, itemSeparator);
        final Add add = new Add(itemSeparator.getID(), getID());
        add.put(PROPERTY.BEFORE_INDEX, beforeIndex);
        Txn.get().getTxnContext().save(add);
        return itemSeparator;
    }

    public void clearItems() {
        final Update update = new Update(getID());
        update.put(PROPERTY.CLEAR, true);
        Txn.get().getTxnContext().save(update);
        // clear
        items.clear();
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    @Override
    public void setAnimationEnabled(final boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
        final Update update = new Update(ID);
        update.put(PROPERTY.ANIMATION, animationEnabled);
        Txn.get().getTxnContext().save(update);
    }

}
