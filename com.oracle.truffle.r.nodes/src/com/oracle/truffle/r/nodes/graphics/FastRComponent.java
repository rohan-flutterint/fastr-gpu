/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.r.nodes.graphics;

import com.oracle.truffle.r.nodes.graphics.core.drawables.DrawableObject;
import com.oracle.truffle.r.nodes.graphics.core.geometry.CoordinateSystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FastRComponent extends JComponent {
    private final List<DrawableObject> displayList = Collections.synchronizedList(new ArrayList<>());

    private boolean shouldDraw;
    private CoordinateSystem coordinateSystem;

    @Override
    public void doLayout() {
        super.doLayout();
        Dimension size = getSize();
        coordinateSystem = new CoordinateSystem(0, size.getWidth(), 0, size.getHeight());
        recalculateDisplayList();
    }

    private void recalculateDisplayList() {
        displayList.stream().forEach(d -> d.recalculateForDrawingIn(coordinateSystem));
    }

    /**
     * Note! Called from ED thread.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (shouldDraw) {
            drawDisplayListOn(g2);
        }
    }

    private void drawDisplayListOn(Graphics2D g2) {
        synchronized (displayList) {
            for (DrawableObject drawableObject : displayList) {
                drawableObject.drawOn(g2);
            }
        }
    }

    public void addDrawableObject(DrawableObject drawableObject) {
        displayList.add(drawableObject);
        shouldDraw = true;
        if (coordinateSystem != null) {
            drawableObject.recalculateForDrawingIn(coordinateSystem);
            repaint();
        }
    }
}
