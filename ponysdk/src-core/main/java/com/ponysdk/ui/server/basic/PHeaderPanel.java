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
import java.util.Iterator;
import java.util.List;

import com.ponysdk.core.instruction.Add;
import com.ponysdk.core.instruction.Remove;
import com.ponysdk.core.instruction.Update;
import com.ponysdk.core.stm.Txn;
import com.ponysdk.ui.terminal.Dictionnary.PROPERTY;
import com.ponysdk.ui.terminal.WidgetType;

/**
 * A panel that includes a header (top), footer (bottom), and content (middle) area. The header and footer
 * areas resize naturally. The content area is allocated all of the remaining space between the header and
 * footer area.
 */
public class PHeaderPanel extends PPanel {

    private PWidget header;
    private PWidget content;
    private PWidget footer;

    public void resize() {
        final Update update = new Update(getID());
        update.put(PROPERTY.RESIZE, true);
        Txn.get().getTxnContext().save(update);
    }

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.HEADER_PANEL;
    }

    public PWidget getContentWidget() {
        return content;
    }

    public PWidget getFooterWidget() {
        return footer;
    }

    public PWidget getHeaderWidget() {
        return header;
    }

    public void setHeaderWidget(final PWidget child) {
        child.removeFromParent();
        adopt(child);
        header = child;

        final Add add = new Add(child.getID(), getID());
        add.put(PROPERTY.INDEX, 0);
        Txn.get().getTxnContext().save(add);
    }

    public void setContentWidget(final PWidget child) {
        child.removeFromParent();
        adopt(child);
        content = child;

        final Add add = new Add(child.getID(), getID());
        add.put(PROPERTY.INDEX, 1);
        Txn.get().getTxnContext().save(add);
    }

    public void setFooterWidget(final PWidget child) {
        child.removeFromParent();
        adopt(child);
        footer = child;

        final Add add = new Add(child.getID(), getID());
        add.put(PROPERTY.INDEX, 2);
        Txn.get().getTxnContext().save(add);
    }

    @Override
    public boolean remove(final PWidget child) {

        if (child.getParent() != this) return false;

        orphan(child);

        if (child == content) {
            content = null;
            sendRemove(child);
            return true;
        } else if (child == header) {
            header = null;
            sendRemove(child);
            return true;
        } else if (child == footer) {
            footer = null;
            sendRemove(child);
            return true;
        } else {
            return false;
        }
    }

    private void sendRemove(final PWidget child) {
        final Remove remove = new Remove(child.getID(), getID());
        Txn.get().getTxnContext().save(remove);
    }

    @Override
    public void add(final PWidget w) {
        // Add widgets in the order that they appear.
        if (header == null) {
            setHeaderWidget(w);
        } else if (content == null) {
            setContentWidget(w);
        } else if (footer == null) {
            setFooterWidget(w);
        } else {
            throw new IllegalStateException("HeaderPanel already contains header, content, and footer widgets.");
        }
    }

    @Override
    public Iterator<PWidget> iterator() {
        final List<PWidget> widgets = new ArrayList<PWidget>();
        if (header != null) widgets.add(header);
        if (content != null) widgets.add(content);
        if (footer != null) widgets.add(footer);
        return widgets.iterator();
    }

}
