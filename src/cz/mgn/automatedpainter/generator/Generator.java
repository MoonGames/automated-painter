/*
 * Copyright (C) 2013 Martin Indra <aktive at seznam.cz>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cz.mgn.automatedpainter.generator;

import java.util.ArrayList;

/**
 *
 * @author Martin Indra <aktive at seznam.cz>
 */
public class Generator extends Thread {

    /**
     * List of listeners to which should be informed about paint events.
     */
    protected ArrayList<PaintingListener> paintingListeners = new ArrayList<>();

    public Generator() {
    }

    /**
     * Adds listener of paint events.
     *
     * @param listener listener to add
     */
    public void addPaintingListener(PaintingListener listener) {
        synchronized (paintingListeners) {
            paintingListeners.add(listener);
        }
    }

    /**
     * Removes listener from the list of listeners informed about paint events.
     * @param listener listener to remove
     * @return true if listener has been found in the list and removed
     */
    public boolean removePaintingListener(PaintingListener listener) {
        synchronized (paintingListeners) {
            return paintingListeners.remove(listener);
        }
    }

    @Override
    public void run() {

    }
}
