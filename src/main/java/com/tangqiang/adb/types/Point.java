package com.tangqiang.adb.types;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        } else {
            Point that = (Point) obj;
            return this.x == that.x && this.y == that.y;
        }
    }

    public int hashCode() {
        return 1125274389 + this.x + this.y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}