package com.tangqiang;

import com.tangqiang.adb.types.Point;
import com.tangqiang.adb.types.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * 工具类
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class JumpUtil {
    private static Logger logger = LoggerFactory.getLogger(JumpUtil.class);
    /**
     * 分数的高度比
     */
    private static double scorePercent = 500.0 / 2160.0;
    /**
     * 保龄球 特征颜色
     */
    private static int color_red = 52;
    private static int color_green = 53;
    private static int color_blue = 59;
    /**
     * 时间距离比
     */
    private static double timeSizePercent = 1.6 / 1080;

    public static Point getCenter(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return new Point(width / 2, height / 2);
    }

    /**
     * 找到下个图形
     */
    public static Point findNext(BufferedImage bufferedImage) throws Exception {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int heightCenter = height / 2;
        int scorePos = (int) (height * scorePercent);

        logger.info("width:" + width + "  height:" + height + "  scorePos:" + scorePos + "  Center:" + heightCenter);
        for (int h = scorePos; h < heightCenter; h++) {
            int rgbLine = bufferedImage.getRGB(2, h);
            int lineR = (rgbLine >> 16) & 0xFF;
            int lineG = (rgbLine >> 8) & 0xFF;
            int lineB = (rgbLine >> 0) & 0xFF;

            for (int w = 5; w < width - 5; w++) {
                int rgb = bufferedImage.getRGB(w, h);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;
                if (Math.abs(red - lineR) > 10 || Math.abs(green - lineG) > 10 || Math.abs(blue - lineB) > 10) {
                    logger.info("Next x:" + w + " y:" + h + " Line :" + lineR + " " + lineG + " " + lineB + "    Point:" + red + " " + green + " " + blue);
                    return new Point(w, h);
                }
            }
        }
        throw new RuntimeException("Not Found Next");
    }

    /**
     * 找到跳台位置
     */
    public static Point findPrevious(BufferedImage bufferedImage, Point next) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int widthCenter = width / 2;
        int nextY = next.getY();
        int nextX = next.getX();
        Rect rect = nextX > widthCenter ? new Rect(0, nextY, widthCenter, height) : new Rect(widthCenter, nextY, width, height);

        for (int h = rect.top; h < rect.bottom; h++) {
            for (int w = rect.left; w < rect.right; w++) {
                int rgb = bufferedImage.getRGB(w, h);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;
                if (red > color_red - 2 && red < color_red + 2) {
                    logger.info(w + " " + h + " r:" + red + " g:" + green + " b:" + blue);
                    return new Point(w + 3, h);
                }
            }
        }
        throw new RuntimeException("Not Found Previous");
    }


    /**
     * 计算时间
     */
    public static long time(Point previous, Point next, int width) {
        double time = (double) Math.abs(next.getX() - previous.getX()) * (double) width * timeSizePercent;
        return (long)time;
    }

}
