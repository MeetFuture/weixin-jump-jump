package com.tangqiang;

import com.tangqiang.adb.AdbDevice;
import com.tangqiang.core.types.Point;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        try {
            App th = new App();
            th.start();
           // th.jumpFind("tmp/Screen_20180214130958.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void start() throws Exception {
        logger.info("App jump begin .. ");

        AdbBackend backend = new AdbBackend();
        AdbDevice device = new AdbDevice(backend.getDevice());


        jump(device);

        device.close();
        backend.shutdown();

        Thread.sleep(1000);
        System.exit(0);
    }


    private void jump(AdbDevice device) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                /**等待跳跃完成 */
                Thread.sleep(1200);

                long start = System.currentTimeMillis();
                String imagePath = "tmp/Screen_" + new DateTime().toString("yyyyMMddHHmmss") + ".png";

                boolean snapshot = device.takeSnapshot(imagePath);
                long end1 = System.currentTimeMillis();

                logger.info("Snapshot get :" + imagePath + "   used time:" + (end1 - start) + "/ms");
                BufferedImage bufferedImage = ImageIO.read(new File(imagePath));



                Point center = JumpUtil.getCenter(bufferedImage);

                Point current = JumpUtil.findCurrent(bufferedImage);
                Point next = JumpUtil.findNext(bufferedImage, current);


                JumpFrame frame =  JumpFrame.show(bufferedImage);
                frame.drawJump(current,next);

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

        JumpFrame.show(bufferedImage);
        //.draw(bufferedImage, current, next);
    }

    /**
     * 找到中心线的颜色
     */
    private void colorFind() throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new File("tmp/Screen_20180214130958.png"));
        Map<Integer, Map<String, Integer>> map = new LinkedHashMap<>();

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        logger.info("Color Count width:" + width + "  height:" + height);

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                int rgb = bufferedImage.getRGB(w, h);
                Map<String, Integer> msg = map.get(rgb);
                if (msg == null) {
                    msg = new HashMap<>();
                    msg.put("c", 1);
                    msg.put("x", w);
                    msg.put("y", h);
                } else {
                    msg.put("c", msg.get("c") + 1);
                }
                map.put(rgb, msg);
            }
        }
        logger.info("Color Count 1:" + map);
        Set<Integer> set = map.keySet();

        for (Integer color : set) {
            Map<String, Integer> msg = map.get(color);
            if (msg.get("c") > 1) {
                map.remove(color);
            }
        }
        logger.info("Color Count 2:" + map);
    }


}
