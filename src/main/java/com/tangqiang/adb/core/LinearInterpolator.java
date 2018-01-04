package com.tangqiang.adb.core;

import com.tangqiang.adb.types.Point;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class LinearInterpolator {
    private final int steps;

    public LinearInterpolator(int steps) {
        this.steps = steps;
    }

    private static float lerp(float start, float stop, float amount) {
        return start + (stop - start) * amount;
    }

    public void interpolate(Point start, Point end, Callback callback) {
        Math.abs(end.getX() - start.getX());
        Math.abs(end.getY() - start.getY());
        float amount = (float)(1.0D / (double)this.steps);
        callback.start(start);

        for(int i = 1; i < this.steps; ++i) {
            float newX = lerp((float)start.getX(), (float)end.getX(), amount * (float)i);
            float newY = lerp((float)start.getY(), (float)end.getY(), amount * (float)i);
            callback.step(new Point(Math.round(newX), Math.round(newY)));
        }

        callback.end(end);
    }

    public interface Callback {
        void start(Point var1);

        void end(Point var1);

        void step(Point var1);
    }


}

