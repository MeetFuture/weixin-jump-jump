package com.tangqiang.adb.types;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class Rect {
    public int left;
    public int top;
    public int right;
    public int bottom;

    public Rect() {
    }

    public Rect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Rect) {
            Rect r = (Rect)obj;
            if (r != null) {
                return this.left == r.left && this.top == r.top && this.right == r.right && this.bottom == r.bottom;
            }
        }

        return false;
    }

    public int getWidth() {
        return this.right - this.left;
    }

    public int getHeight() {
        return this.bottom - this.top;
    }

    public int[] getCenter() {
        int[] center = new int[]{this.left + this.getWidth() / 2, this.top + this.getHeight() / 2};
        return center;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rect ");
        sb.append("top: ").append(this.top).append(" ");
        sb.append("right: ").append(this.right).append(" ");
        sb.append("bottom: ").append(this.bottom).append(" ");
        sb.append("left: ").append(this.left).append(" ");
        return sb.toString();
    }
}
