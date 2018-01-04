package com.tangqiang.adb.image;

import com.android.ddmlib.RawImage;

import java.awt.*;
import java.awt.image.*;
import java.util.Hashtable;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class ImageUtils {
    private static Hashtable<?, ?> EMPTY_HASH = new Hashtable();
    private static int[] BAND_OFFSETS_32 = new int[]{0, 1, 2, 3};
    private static int[] BAND_OFFSETS_16 = new int[]{0, 1};

    private ImageUtils() {
    }

    public static BufferedImage convertImage(RawImage rawImage, BufferedImage image) {
        switch(rawImage.bpp) {
            case 16:
                return rawImage16toARGB(image, rawImage);
            case 32:
                return rawImage32toARGB(rawImage);
            default:
                return null;
        }
    }

    public static BufferedImage convertImage(RawImage rawImage) {
        return convertImage(rawImage, (BufferedImage)null);
    }

    static int getMask(int length) {
        int res = 0;

        for(int i = 0; i < length; ++i) {
            res = (res << 1) + 1;
        }

        return res;
    }

    private static BufferedImage rawImage32toARGB(RawImage rawImage) {
        DataBufferByte dataBuffer = new DataBufferByte(rawImage.data, rawImage.size);
        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(0, rawImage.width, rawImage.height, 4, rawImage.width * 4, BAND_OFFSETS_32);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
        return new BufferedImage(new ColorModelThirtyTwoBit(rawImage), raster, false, EMPTY_HASH);
    }

    private static BufferedImage rawImage16toARGB(BufferedImage image, RawImage rawImage) {
        DataBufferByte dataBuffer = new DataBufferByte(rawImage.data, rawImage.size);
        PixelInterleavedSampleModel sampleModel = new PixelInterleavedSampleModel(0, rawImage.width, rawImage.height, 2, rawImage.width * 2, BAND_OFFSETS_16);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, new Point(0, 0));
        return new BufferedImage(new ColorModelSixteenBit(rawImage), raster, false, EMPTY_HASH);
    }
}
