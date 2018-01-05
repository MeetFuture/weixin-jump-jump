package com.tangqiang;

import com.tangqiang.adb.AdbBackend;
import com.tangqiang.adb.inter.IAdbBackend;
import com.tangqiang.adb.inter.IAdbDevice;
import com.tangqiang.adb.types.Point;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        try {
            App th = new App();
            th.start();
            // th.jumpFind("target/tmp/Screen_20180105205617.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void start() throws Exception {
        logger.info("App jump begin .. ");

        IAdbBackend backend = new AdbBackend();
        IAdbDevice device = backend.waitForConnection();


        jump(device);

        device.dispose();
        backend.shutdown();

        Thread.sleep(2000);
        System.exit(0);
    }


    private void jump(IAdbDevice device) {
        logger.info("jumpGo ..... ");
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                /**等待跳跃完成 */
                Thread.sleep(1200);

                long start = System.currentTimeMillis();
                String imagePath = "target/tmp/Screen_" + new DateTime().toString("yyyyMMddHHmmss") + ".png";

                boolean snapshot = device.takeSnapshot(imagePath);
                long end1 = System.currentTimeMillis();

                logger.info("Snapshot get :" + imagePath + "   used time:" + (end1 - start) + "/ms");
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));

                Point center = JumpUtil.getCenter(bufferedImage);

                Point current = JumpUtil.findCurrent(bufferedImage);
                Point next = JumpUtil.findNext(bufferedImage, current);
                long ctime = JumpUtil.time(current, next, bufferedImage.getWidth());

                long end2 = System.currentTimeMillis();
                logger.info("Find (" + (end2 - end1) + "ms) next:" + next + "  previous:" + current + "  len:" + Math.abs(next.getX() - current.getX()) + "  Time:" + ctime);


                /**读取输入的时间*/
//                String line = scanner.nextLine();
//                if (line.startsWith("q")) {
//                    break;
//                } else {
//                    ctime = Integer.valueOf(line.trim());
//                }
                int randomX = (int) (40 * Math.random() + 200);
                int randomY = (int) (80 * Math.random() - 40);
                device.touch(center.getX() + randomX, center.getY() * 2 - 400 + randomY, ctime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void jumpFind(String path) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new File(path));
        Point current = JumpUtil.findCurrent(bufferedImage);
        Point next = JumpUtil.findNext(bufferedImage, current);
    }


}
