package com.tangqiang.adb.inter;

import java.awt.image.BufferedImage;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public interface IAdbImage {
    BufferedImage createBufferedImage();

    BufferedImage getBufferedImage();

    IAdbImage getSubImage(int var1, int var2, int var3, int var4);

    byte[] convertToBytes(String var1);

    boolean writeToFile(String var1, String var2);

    int getPixel(int var1, int var2);

    boolean sameAs(IAdbImage var1, double var2);
}
