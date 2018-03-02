package com.tangqiang;

import com.tangqiang.core.types.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * 显示图片和定点
 *
 * @author Tom
 * @version 1.0 2018-01-09 0009 Tom create
 * @date 2018-01-09 0009
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class JumpFrame extends JFrame {
    private static JumpFrame client;
    private BufferedImage image;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public static JumpFrame show(BufferedImage image) {
        if (client == null) {
            client = new JumpFrame();
            client.init(image.getWidth() / 2, image.getHeight() / 2 + 20);
        }
        client.draw(image);
        return client;
    }


    private void init(int w, int h) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            setTitle("跳一跳");
            setSize(w, h);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(true);
            setVisible(true);

            Container pane = getContentPane();
            pane.setLayout(null);
            pane.setBackground(new Color(199, 237, 204));

            addMouseListener(new MyMouseListener());
        } catch (Exception e) {
            logger.error("AppFrame init error !", e);
        }
    }


    public void draw(BufferedImage image) {
        this.image = image;
        Container pane = getContentPane();
        Graphics g = pane.getGraphics();
        update(g);
        g.drawImage(image, 0, 0, image.getWidth() / 2, image.getHeight() / 2, this);
    }

    public void drawJump(Point current, Point next) {
        try {
            Container pane = getContentPane();
            Graphics g = pane.getGraphics();

            int currX = (int) (current.getX() / 2);
            int currY = (int) (current.getY() / 2);
            int nextX = (int) (next.getX() / 2);
            int nextY = (int) (next.getY() / 2);

            g.setColor(Color.GREEN);
            g.fillRect(currX - 4, currY - 4, 8, 8);

            g.setColor(Color.RED);
            g.fillRect(nextX - 4, nextY - 4, 8, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("MyMouseListener.mouseClicked :" + image.getRGB(e.getX() * 2, e.getY() * 2));
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
