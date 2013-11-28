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
package cz.mgn.automatedpainter.generator.algorythms;

import cz.mgn.automatedpainter.generator.PaintUpdate;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Martin Indra <aktive at seznam.cz>
 */
public class LineDrawer {

    protected static final float MIN_SPEED = 10;
    protected static final float MAX_SPEED = 100;
    protected static final float MIN_ANGULAR_VELOCITY = (float) (-Math.PI * 5);
    protected static final float MAX_ANGULAR_VELOCITY = (float) (Math.PI * 5);
    protected float angularVelocity = (MIN_ANGULAR_VELOCITY + MAX_ANGULAR_VELOCITY) / 2;
    protected Vector vector = new Vector((MAX_SPEED + MIN_SPEED) / 2, 0);
    protected Vector position = new Vector(100, 100);
    protected ArrayList<LineSegment> toDraw = new ArrayList<>();

    public LineDrawer() {
    }

    public PaintUpdate drawAndClearBuffer() {
        LineSegment rect = getBufferRectangle();
        int w = (int) Math.ceil(rect.getPointB().x - rect.getPointA().x);
        int h = (int) Math.ceil(rect.getPointB().y - rect.getPointA().y);

        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        //TODO paintit

        PaintUpdate update = new PaintUpdate(rect.getPointA().x, rect.getPointA().y, buffer);
        toDraw.clear();
        return update;
    }

    protected LineSegment getBufferRectangle() {
        if (toDraw.isEmpty()) {
            return null;
        }

        float xMin = Float.MIN_VALUE;
        float xMax = Float.MAX_VALUE;
        float yMin = Float.MIN_VALUE;
        float yMax = Float.MAX_VALUE;

        for (LineSegment l : toDraw) {
            Vector min = l.getOuterRectangleMin();
            Vector max = l.getOuterRectangleMax();

            if (min.getX() < xMin) {
                xMin = min.getX();
            }
            if (min.getY() < yMin) {
                yMin = min.getY();
            }
            if (max.getX() > xMax) {
                xMax = max.getX();
            }
            if (min.getY() > yMax) {
                yMax = max.getY();
            }
        }

        return new LineSegment(new Vector(xMin, yMin), new Vector(xMax, yMax));
    }

    public void update(float time) {
        Vector pointA = position.clone();
        updateAngle(time);
        updateSpeed(time);
        position.addLocal(vector);
        Vector pointB = position.clone();
        toDraw.add(new LineSegment(pointA, pointB));
    }

    protected void updateAngle(float time) {
        float scope = (float) (2 * Math.PI * time);
        Random r = new Random();
        scope = scope * r.nextFloat() - scope / 2;
        angularVelocity += scope;

        vector.rotateLocal(angularVelocity * time);
    }

    protected void updateSpeed(float time) {
        float scope = 0.1f * time;

        Random r = new Random();
        scope = scope * (r.nextFloat() * 2 - 1) + 1;
        vector.scaleLocal(time);

        float size = vector.getSize();

        if (size < MIN_SPEED) {
            if (size > 0) {
                vector.scaleLocal(MIN_SPEED / size);
            } else {
                vector.set(MIN_SPEED, 0);
            }
        } else if (size > MAX_SPEED) {
            vector.scaleLocal(MAX_SPEED / size);
        }
    }

    protected class LineSegment {

        protected Vector pointA;
        protected Vector pointB;

        public LineSegment(Vector pointA, Vector pointB) {
            this.pointA = pointA;
            this.pointB = pointB;
        }

        public Vector getPointA() {
            return pointA;
        }

        public Vector getPointB() {
            return pointB;
        }

        public Vector getOuterRectangleMin() {
            return new Vector(pointA.getX() < pointB.getX() ? pointA.getX() : pointB.getX(),
                    pointA.getY() < pointB.getY() ? pointA.getY() : pointB.getY());
        }

        public Vector getOuterRectangleMax() {
            return new Vector(pointA.getX() > pointB.getX() ? pointA.getX() : pointB.getX(),
                    pointA.getY() > pointB.getY() ? pointA.getY() : pointB.getY());
        }
    }

    protected class Vector implements Cloneable {

        protected float x;
        protected float y;

        public Vector() {
            x = 0;
            y = 0;
        }

        public Vector(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public Vector clone() {
            return new Vector(x, y);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getSize() {
            return (float) Math.sqrt(x * x + y * y);
        }

        public void set(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector add(Vector v) {
            return new Vector(x + v.getX(), y + v.getY());
        }

        public Vector addLocal(Vector v) {
            x += v.getX();
            y += v.getY();
            return this;
        }

        public Vector scale(float n) {
            return new Vector(x * n, y * n);
        }

        public Vector scaleLocal(float n) {
            x *= n;
            y *= n;
            return this;
        }

        public Vector rotate(float angle) {
            float sin = (float) Math.sin(angle), cos = (float) Math.cos(angle);

            float x = cos * this.x - sin * this.y;
            float y = sin * this.x + cos * this.y;

            return new Vector(x, y);
        }

        public Vector rotateLocal(float angle) {
            Vector temp = rotate(angle);
            this.x = temp.getX();
            this.y = temp.getY();
            return this;
        }
    }
}
