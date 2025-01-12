/******************************************************************************
 *  Compilation:  javac RectHV.java
 *  Execution:    none
 *  Dependencies: Point2D.java
 *
 *  Immutable data type for 2D axis-aligned rectangle.
 *
 ******************************************************************************/

package org.example.firstyearproject.KdTree;

import java.io.Serializable;

/**
 *  The {@code RectHV} class is an immutable data type to encapsulate a
 *  two-dimensional axis-aligned rectagle with real-value coordinates.
 *  The rectangle is <em>closed</em>—it includes the points on the boundary.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/12oop">Section 1.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public final class RectHV implements Serializable {
    private final double xmin, ymin;   // minimum x- and y-coordinates
    private final double xmax, ymax;   // maximum x- and y-coordinates

    /**
     * Initializes a new rectangle [<em>xmin</em>, <em>xmax</em>]
     * x [<em>ymin</em>, <em>ymax</em>].
     *
     * @param  xmin the <em>x</em>-coordinate of the lower-left endpoint
     * @param  ymin the <em>y</em>-coordinate of the lower-left endpoint
     * @param  xmax the <em>x</em>-coordinate of the upper-right endpoint
     * @param  ymax the <em>y</em>-coordinate of the upper-right endpoint
     * @throws IllegalArgumentException if any of {@code xmin},
     *         {@code ymin}, {@code xmax}, or {@code ymax}
     *         is {@code Double.NaN}.
     * @throws IllegalArgumentException if {@code xmax < xmin} or {@code ymax < ymin}.
     */
    public RectHV(double xmin, double ymin, double xmax, double ymax) {
        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
        if (Double.isNaN(xmin) || Double.isNaN(xmax)) {
            throw new IllegalArgumentException("x-coordinate is NaN: " + toString());
        }
        if (Double.isNaN(ymin) || Double.isNaN(ymax)) {
            throw new IllegalArgumentException("y-coordinate is NaN: " + toString());
        }
        if (xmax < xmin) {
            throw new IllegalArgumentException("xmax < xmin: " + toString());
        }
        if (ymax < ymin) {
            throw new IllegalArgumentException("ymax < ymin: " + toString());
        }
    }


    /**
     * Returns true if the two rectangles intersect, and false otherwise.
     * This includes <em>improper intersections</em> (at points on the
     * boundary of each rectangle) and <em>nested intersections</em>
     * (when one rectangle is contained inside the other).
     *
     * @param  that the other rectangle
     * @return {@code true} if this rectangle intersects the argument
    rectangle at one or more points; false otherwise
     */
    public boolean intersects(RectHV that) {
        return this.xmax >= that.xmin && this.ymax >= that.ymin
                && that.xmax >= this.xmin && that.ymax >= this.ymin;
    }

    /**
     * Returns true if this rectangle contains the point, and false otherwise.
     * This includes point on the boundary of the rectangle.
     * @param  p the point
     * @return {@code true} if this rectangle contains the point {@code p},
    possibly at the boundary; {@code false} otherwise
     */
    public boolean contains(Point2D p) {
        return (p.x() >= xmin) && (p.x() <= xmax)
                && (p.y() >= ymin) && (p.y() <= ymax);
    }


}

/******************************************************************************
 *  Copyright 2002-2022, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/