package com.tangqiang.adb.core;

import com.tangqiang.adb.inter.IAdbImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public abstract class AdbImageBase implements IAdbImage {
    private static Logger LOG = LoggerFactory.getLogger(AdbImageBase.class);
    private WeakReference<BufferedImage> cachedBufferedImage = null;

    public AdbImageBase() {
    }

    public static IAdbImage loadImageFromFile(String path) {
        File f = new File(path);
        if (f.exists() && f.canRead()) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(path));
                if (bufferedImage == null) {
                    LOG.warn("Cannot decode file %s", path);
                    return null;
                } else {
                    return new BufferedImageChimpImage(bufferedImage);
                }
            } catch (IOException var3) {
                LOG.warn("Exception trying to decode image", var3);
                return null;
            }
        } else {
            LOG.warn("Cannot read file %s", path);
            return null;
        }
    }

    public abstract BufferedImage createBufferedImage();

    public BufferedImage getBufferedImage() {
        BufferedImage img;
        if (this.cachedBufferedImage != null) {
            img = (BufferedImage) this.cachedBufferedImage.get();
            if (img != null) {
                return img;
            }
        }

        img = this.createBufferedImage();
        this.cachedBufferedImage = new WeakReference(img);
        return img;
    }

    public byte[] convertToBytes(String format) {
        BufferedImage argb = this.convertSnapshot();
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(argb, format, os);
        } catch (IOException var5) {
            return new byte[0];
        }

        return os.toByteArray();
    }

    public boolean writeToFile(String path, String format) {
        if (format != null) {
            return this.writeToFileHelper(path, format);
        } else {
            int offset = path.lastIndexOf(46);
            if (offset < 0) {
                return this.writeToFileHelper(path, "png");
            } else {
                String ext = path.substring(offset + 1);
                Iterator<ImageWriter> writers = ImageIO.getImageWritersBySuffix(ext);
                if (!writers.hasNext()) {
                    return this.writeToFileHelper(path, "png");
                } else {
                    ImageWriter writer = (ImageWriter) writers.next();
                    BufferedImage image = this.convertSnapshot();

                    try {
                        File f = new File(path);
                        f.delete();
                        ImageOutputStream outputStream = ImageIO.createImageOutputStream(f);
                        writer.setOutput(outputStream);

                        try {
                            writer.write(image);
                        } finally {
                            writer.dispose();
                            outputStream.flush();
                        }

                        return true;
                    } catch (IOException var14) {
                        return false;
                    }
                }
            }
        }
    }

    public int getPixel(int x, int y) {
        BufferedImage image = this.getBufferedImage();
        return image.getRGB(x, y);
    }

    private BufferedImage convertSnapshot() {
        BufferedImage image = this.getBufferedImage();
        BufferedImage argb = new BufferedImage(image.getWidth(), image.getHeight(), 2);
        Graphics g = argb.createGraphics();
        g.drawImage(image, 0, 0, (ImageObserver) null);
        g.dispose();
        return argb;
    }

    private boolean writeToFileHelper(String path, String format) {
        BufferedImage argb = this.convertSnapshot();

        try {
            ImageIO.write(argb, format, new File(path));
            return true;
        } catch (IOException var5) {
            return false;
        }
    }

    public boolean sameAs(IAdbImage other, double percent) {
        BufferedImage otherImage = other.getBufferedImage();
        BufferedImage myImage = this.getBufferedImage();
        if (otherImage.getWidth() != myImage.getWidth()) {
            return false;
        } else if (otherImage.getHeight() != myImage.getHeight()) {
            return false;
        } else {
            int[] otherPixel = new int[1];
            int[] myPixel = new int[1];
            int width = myImage.getWidth();
            int height = myImage.getHeight();
            int numDiffPixels = 0;

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    if (myImage.getRGB(x, y) != otherImage.getRGB(x, y)) {
                        ++numDiffPixels;
                    }
                }
            }

            double numberPixels = (double) (height * width);
            double diffPercent = (double) numDiffPixels / numberPixels;
            return percent <= 1.0D - diffPercent;
        }
    }

    public IAdbImage getSubImage(int x, int y, int w, int h) {
        BufferedImage image = this.getBufferedImage();
        return new BufferedImageChimpImage(image.getSubimage(x, y, w, h));
    }

    private static class BufferedImageChimpImage extends AdbImageBase {
        private final BufferedImage image;

        public BufferedImageChimpImage(BufferedImage image) {
            this.image = image;
        }

        public BufferedImage createBufferedImage() {
            return this.image;
        }
    }
}
