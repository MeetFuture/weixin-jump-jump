package com.tangqiang.adb.image;

import com.android.ddmlib.RawImage;
import com.tangqiang.adb.core.AdbImageBase;

import java.awt.image.BufferedImage;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class AdbImage extends AdbImageBase {
    private final RawImage image;

    public AdbImage(RawImage image) {
        this.image = image;
    }

    @Override
    public BufferedImage createBufferedImage() {
        return ImageUtils.convertImage(this.image);
    }

    public RawImage getRawImage() {
        return this.image;
    }
}
