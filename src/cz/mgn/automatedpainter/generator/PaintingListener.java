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

import java.awt.image.BufferedImage;

/**
 *
 * @author Martin Indra <aktive at seznam.cz>
 */
public interface PaintingListener {

    /**
     * Informs about paint event.
     *
     * @param image image to paint
     * @param x X coordinate where image should be placed
     * @param y Y coordinate where image should be place
     */
    public void paint(BufferedImage image, int x, int y);
}
