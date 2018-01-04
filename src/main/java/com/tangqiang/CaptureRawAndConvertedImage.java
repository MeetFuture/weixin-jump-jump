package com.tangqiang;

import com.android.ddmlib.RawImage;
import com.tangqiang.adb.AdbBackend;
import com.tangqiang.adb.inter.IAdbBackend;
import com.tangqiang.adb.inter.IAdbDevice;
import com.tangqiang.adb.inter.IAdbImage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class CaptureRawAndConvertedImage {
    public CaptureRawAndConvertedImage() {
    }



    public static void main(String[] args) throws IOException {
        IAdbBackend backend = new AdbBackend();
        IAdbDevice device = backend.waitForConnection();

        int i = 0;
        while (true) {
            IAdbImage snapshot = device.takeSnapshotFrame();
            snapshot.writeToFile("target/tmp/output" + ++i + ".png", "png");
            System.out.println("CaptureRawAndConvertedImage.main :" + i);
        }
//        System.out.println("aaaaaaaaaaaaa");
        //writeOutImage(((AdbImage)snapshot).getRawImage(), "output.raw");
//        System.exit(0);
    }

    public interface IRawImager {
        RawImage toRawImage();
    }

    private static void writeOutImage(RawImage screenshot, String name) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(name));
        out.writeObject(new ChimpRawImage(screenshot));
        out.close();
    }

    public static class ChimpRawImage implements Serializable, IRawImager {
        public int version;
        public int bpp;
        public int size;
        public int width;
        public int height;
        public int red_offset;
        public int red_length;
        public int blue_offset;
        public int blue_length;
        public int green_offset;
        public int green_length;
        public int alpha_offset;
        public int alpha_length;
        public byte[] data;

        public ChimpRawImage(RawImage rawImage) {
            this.version = rawImage.version;
            this.bpp = rawImage.bpp;
            this.size = rawImage.size;
            this.width = rawImage.width;
            this.height = rawImage.height;
            this.red_offset = rawImage.red_offset;
            this.red_length = rawImage.red_length;
            this.blue_offset = rawImage.blue_offset;
            this.blue_length = rawImage.blue_length;
            this.green_offset = rawImage.green_offset;
            this.green_length = rawImage.green_length;
            this.alpha_offset = rawImage.alpha_offset;
            this.alpha_length = rawImage.alpha_length;
            this.data = rawImage.data;
        }

        public RawImage toRawImage() {
            RawImage rawImage = new RawImage();
            rawImage.version = this.version;
            rawImage.bpp = this.bpp;
            rawImage.size = this.size;
            rawImage.width = this.width;
            rawImage.height = this.height;
            rawImage.red_offset = this.red_offset;
            rawImage.red_length = this.red_length;
            rawImage.blue_offset = this.blue_offset;
            rawImage.blue_length = this.blue_length;
            rawImage.green_offset = this.green_offset;
            rawImage.green_length = this.green_length;
            rawImage.alpha_offset = this.alpha_offset;
            rawImage.alpha_length = this.alpha_length;
            rawImage.data = this.data;
            return rawImage;
        }
    }
}