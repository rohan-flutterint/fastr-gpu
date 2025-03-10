/*
 * Copyright (c) 2013, 2016, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.truffle.r.library.astx.threads;

import java.util.ArrayList;

public class RThreadManager {

    public static RThreadManager INSTANCE = new RThreadManager();

    private ArrayList<Thread> threads;

    private RThreadManager() {
        threads = new ArrayList<>();
    }

    public int addThread(Thread thread) {
        int idx = threads.size();
        threads.add(thread);
        return idx;
    }

    public Thread getThread(int idx) {
        return threads.get(idx);
    }

    public int getNumberOfPendingThreads() {
        return threads.size();
    }

    public void removeThread(int idx) {
        threads.remove(idx);
    }

    public ArrayList<Thread> getAll() {
        return threads;
    }

    public void join(int idx) {
        try {
            threads.get(idx).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
