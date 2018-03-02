package com.tangqiang;

import com.tangqiang.core.types.Point;
import com.tangqiang.core.types.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Arrays;

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
     * 跳瓶 特征颜色
     */
    private static int[] color = new int[]{ -12635557 };
    /**
     * 跳 全屏所需时间 毫秒ms
     */
    private static double timeSize = 1700;


    /**
     * 找到跳台位置
     */
    public static Point findCurrent(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        int scorePos = (int) (height * scorePercent);

        for (int h = scorePos; h < height; h++) {
            for (int w = 0; w < width - 2; w++) {
                int rgb = bufferedImage.getRGB(w, h);
                if (Arrays.binarySearch(color, rgb) >= 0) {
                    logger.info("Current x:" + w + " y:" + h);
                    return new Point(w, h);
                }
            }
        }
        throw new RuntimeException("Not Found Current");
    }

    public static Point getCenter(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        return new Point(width / 2, height / 2);
    }

    /**
     * 找到下个图形
     */
    public static Point findNext(BufferedImage bufferedImage, Point current) throws Exception {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int heightCenter = height / 2;
        int scorePos = (int) (height * scorePercent);

        int widthCenter = width / 2;
        int currentX = current.getX();
        Rect rect = currentX > widthCenter ? new Rect(0, scorePos, widthCenter - 40, heightCenter) : new Rect(widthCenter + 40, scorePos, width, heightCenter);


        //logger.info("width:" + width + "  height:" + height + "  scorePos:" + scorePos + "  Center:" + heightCenter);
        for (int h = rect.top; h < rect.bottom; h++) {
            int rgbLine = bufferedImage.getRGB(2, h);
            int lineR = (rgbLine >> 16) & 0xFF;
            int lineG = (rgbLine >> 8) & 0xFF;
            int lineB = (rgbLine >> 0) & 0xFF;

            Point left = null;
            Point right = null;
            for (int w = rect.left; w < rect.right; w++) {
                int rgb = bufferedImage.getRGB(w, h);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb >> 0) & 0xFF;


                if (Math.abs(red - lineR) > 10 || Math.abs(green - lineG) > 10 || Math.abs(blue - lineB) > 10) {
                    logger.info("Next 1 x:" + w + " y:" + h + " Line :" + lineR + " " + lineG + " " + lineB + "    Point:" + red + " " + green + " " + blue);
                    left = new Point(w, h);
                    break;
                }
            }

            if (left != null) {
                for (int w = rect.right - 1; w >= rect.left; w--) {
                    int rgb = bufferedImage.getRGB(w, h);

                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = (rgb >> 0) & 0xFF;


                    if (Math.abs(red - lineR) > 10 || Math.abs(green - lineG) > 10 || Math.abs(blue - lineB) > 10) {
                        logger.info("Next 2 x:" + w + " y:" + h + " Line :" + lineR + " " + lineG + " " + lineB + "    Point:" + red + " " + green + " " + blue);
                        right = new Point(w, h);
                        break;
                    }
                }
                if (right != null) {
                    Point p = new Point((left.getX() + right.getX()) / 2, h);
                    logger.info("Next x:" + p.getX() + " y:" + h);
                    return p;
                }
            }
        }
        throw new RuntimeException("Not Found Next");
    }


    /**
     * 计算时间
     */
    public static long time(Point previous, Point next, int width) {
        double time = (double) Math.abs(next.getX() - previous.getX()) / (double) width * timeSize;
        return (long) time;
    }

}
