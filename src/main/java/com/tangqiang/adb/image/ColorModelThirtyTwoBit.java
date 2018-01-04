package com.tangqiang.adb.image;

import com.android.ddmlib.RawImage;

import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.Raster;

/**
 * bbp 32 的图像模式
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

class ColorModelThirtyTwoBit extends ColorModel {
    private static final int[] BITS = new int[]{8, 8, 8, 8};
    private final int alphaLength;
    private final int alphaMask;
    private final int alphaOffset;
    private final int blueMask;
    private final int blueLength;
    private final int blueOffset;
    private final int greenMask;
    private final int greenLength;
    private final int greenOffset;
    private final int redMask;
    private final int redLength;
    private final int redOffset;

    public ColorModelThirtyTwoBit(RawImage rawImage) {
        super(32, BITS, ColorSpace.getInstance(1000), true, false, 3, 0);
        this.redOffset = rawImage.red_offset;
        this.redLength = rawImage.red_length;
        this.redMask = ImageUtils.getMask(this.redLength);
        this.greenOffset = rawImage.green_offset;
        this.greenLength = rawImage.green_length;
        this.greenMask = ImageUtils.getMask(this.greenLength);
        this.blueOffset = rawImage.blue_offset;
        this.blueLength = rawImage.blue_length;
        this.blueMask = ImageUtils.getMask(this.blueLength);
        this.alphaLength = rawImage.alpha_length;
        this.alphaOffset = rawImage.alpha_offset;
        this.alphaMask = ImageUtils.getMask(this.alphaLength);
    }

    @Override
    public boolean isCompatibleRaster(Raster raster) {
        return true;
    }

    private int getPixel(Object inData) {
        byte[] data = (byte[]) ((byte[]) inData);
        int value = data[0] & 255;
        value |= (data[1] & 255) << 8;
        value |= (data[2] & 255) << 16;
        value |= (data[3] & 255) << 24;
        return value;
    }

    @Override
    public int getAlpha(Object inData) {
        int pixel = this.getPixel(inData);
        return this.alphaLength == 0 ? 255 : (pixel >>> this.alphaOffset & this.alphaMask) << 8 - this.alphaLength;
    }

    @Override
    public int getBlue(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >>> this.blueOffset & this.blueMask) << 8 - this.blueLength;
    }

    @Override
    public int getGreen(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >>> this.greenOffset & this.greenMask) << 8 - this.greenLength;
    }

    @Override
    public int getRed(Object inData) {
        int pixel = this.getPixel(inData);
        return (pixel >>> this.redOffset & this.redMask) << 8 - this.redLength;
    }

    @Override
    public int getAlpha(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getBlue(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getGreen(int pixel) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getRed(int pixel) {
        throw new UnsupportedOperationException();
    }
}
