package com.tangqiang.adb.image;

import com.android.ddmlib.RawImage;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

/**
 * bbp 16 的图像模式
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

class ColorModelSixteenBit extends ColorModel {
    private static final int[] BITS = new int[]{8, 8, 8, 8};

    public ColorModelSixteenBit(RawImage rawImage) {
        super(32, BITS, ColorSpace.getInstance(1000), true, false, 3, 0);
    }

    public boolean isCompatibleRaster(Raster raster) {
        return true;
    }

    private int getPixel(Object inData) {
        byte[] data = (byte[])((byte[])inData);
        int value = data[0] & 255;
        value |= data[1] << 8 & '\uff00';
        return value;
    }

    public int getAlpha(Object inData) {
        return 255;
    }

    public int getBlue(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >> 0 & 31) << 3;
    }

    public int getGreen(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >> 5 & 63) << 2;
    }

    public int getRed(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >> 11 & 31) << 3;
    }

    public int getAlpha(int pixel) {
        throw new UnsupportedOperationException();
    }

    public int getBlue(int pixel) {
        throw new UnsupportedOperationException();
    }

    public int getGreen(int pixel) {
        throw new UnsupportedOperationException();
    }

    public int getRed(int pixel) {
        throw new UnsupportedOperationException();
    }
}

