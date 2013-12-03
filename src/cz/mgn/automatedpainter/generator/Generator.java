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

import cz.mgn.automatedpainter.generator.algorythms.Drawer;
import cz.mgn.automatedpainter.generator.algorythms.LineDrawer;
import cz.mgn.automatedpainter.generator.algorythms.PaintUpdate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin Indra <aktive at seznam.cz>
 */
public class Generator extends Thread {

    /**
     * Updates per second = how often to paint a bit.
     */
    protected static final int UPS = 20;
    protected volatile boolean running = false;
    /**
     * List of listeners to which should be informed about paint events.
     */
    protected ArrayList<PaintingListener> paintingListeners = new ArrayList<>();
    protected Drawer drawer;

    public Generator(int width, int height) {
        //TODO: more options
        this.drawer = new LineDrawer(width, height);
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
     *
     * @param listener listener to remove
     * @return true if listener has been found in the list and removed
     */
    public boolean removePaintingListener(PaintingListener listener) {
        synchronized (paintingListeners) {
            return paintingListeners.remove(listener);
        }
    }

    public void finish() {
        running = false;
    }

    @Override
    public void run() {
        running = true;

        long now = System.nanoTime() / 1000000;
        long before = now;
        long after, sleep;

        while (running) {
            now = System.nanoTime() / 1000000;
            float tpf = (float) (now - before) / 1000;
            before = now;

            update(tpf);

            after = System.nanoTime() / 1000000;
            sleep = 1000 / UPS - after + before;
            try {
                if (sleep > 5) {
                    Thread.sleep(sleep);
                } else {
                    Thread.yield();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Generator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param tpf time per frame = time in seconds which has passed since the
     *  last update
     */
    protected void update(float tpf) {
        drawer.update(tpf);
        PaintUpdate update = drawer.drawAndClearBuffer();

        synchronized (paintingListeners) {
            for (PaintingListener litener : paintingListeners) {
                litener.paint(update.getImage(), update.getX(), update.getY());
            }
        }
    }
}
