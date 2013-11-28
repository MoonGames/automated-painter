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

/**
 *
 * @author Martin Indra <aktive at seznam.cz>
 */
public class LineDrawer {

    protected static final float MIN_SPEED = 10;
    protected static final float MAX_SPEED = 100;
    protected static final float MIN_ANGULAR_VELOCITY = (float) (-Math.PI * 5);
    protected static final float MAX_ANGULAR_VELOCITY = (float) (Math.PI * 5);
    protected float angularVelocity = 0;
    protected Vector vector = new Vector((MAX_SPEED + MIN_SPEED) / 2, 0);

    public LineDrawer() {
    }


    protected void update(float time) {
        //TODO
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
    }

    protected class Vector {

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

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
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
