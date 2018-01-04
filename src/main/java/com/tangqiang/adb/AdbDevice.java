package com.tangqiang.adb;

import com.android.ddmlib.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tangqiang.adb.core.CommandOutputReceiver;
import com.tangqiang.adb.core.LinearInterpolator;
import com.tangqiang.adb.core.ShellOutputReceiver;
import com.tangqiang.adb.image.AdbImage;
import com.tangqiang.adb.inter.*;
import com.tangqiang.adb.types.PhysicalButton;
import com.tangqiang.adb.types.Point;
import com.tangqiang.adb.types.TouchPressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class AdbDevice implements IAdbDevice {
    private static final String[] ZERO_LENGTH_STRING_ARRAY = new String[0];

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final IDevice device;
    private AdbManager manager;


    public AdbDevice(IDevice device) {
        this.device = device;
        this.manager = createManager("127.0.0.1", 12345);
    }

    private static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }


    public AdbManager getManager() {
        return this.manager;
    }

    public void dispose() {
        try {
            this.manager.quit();
        } catch (IOException var2) {
            logger.info("Error getting the manager to quit", var2);
        }

        this.manager.close();
        this.executor.shutdown();
        this.manager = null;
    }


    private AdbManager createManager(String address, int port) {
        AdbManager mm = null;
        try {
            String command = "monkey --port " + port;
            logger.info("open device monkey :" + command);
            this.executeAsyncCommand(command);
//            device.executeShellCommand(command, outputReceiver);

            logger.info("createForward port:" + port + "  device:" + device.getSerialNumber() + "  State:" + device.getState());
            device.createForward(port, port);

            boolean success = false;
            long start = System.currentTimeMillis();
            InetAddress addr = InetAddress.getByName(address);

            while (!success) {
                try {
                    long now = System.currentTimeMillis();
                    long diff = now - start;
                    if (diff > 5000L) {
                        logger.error("Timeout while trying to create chimp mananger");
                        break;
                    }

                    Thread.sleep(1000L);
                    logger.info("create Socket addr:" + addr + " port:" + port);
                    Socket monkeySocket = new Socket(addr, port);

                    logger.info("init AdbManager .. monkeySocket:" + monkeySocket);
                    mm = new AdbManager(monkeySocket);

                    mm.wake();
                    success = true;
                } catch (Exception e) {
                    logger.error("Create AdbManager error ", e);
                    success = false;
                }
            }

        } catch (Exception e) {
            logger.error("Create AdbManager error !" + e.getMessage(), e);
        }
        logger.info("createManager :" + mm);
        return mm;
    }


    private void executeAsyncCommand(final String command) {
        this.executor.submit(new Runnable() {
            public void run() {
                try {
                    ShellOutputReceiver outputReceiver = new ShellOutputReceiver();
                    device.executeShellCommand(command, outputReceiver);
                } catch (Exception e) {
                    logger.error("Error starting command: " + command, e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public IAdbImage takeSnapshotFrame() {
        try {
            return new AdbImage(this.device.getScreenshot());
        } catch (TimeoutException var2) {
            logger.error("Unable to take snapshot", var2);
            return null;
        } catch (AdbCommandRejectedException var3) {
            logger.error("Unable to take snapshot", var3);
            return null;
        } catch (Exception var4) {
            logger.error("Unable to take snapshot", var4);
            return null;
        }
    }

    /**
     * @param localFile "screenshot.png"
     * @return
     */
    public boolean takeSnapshot(String localFile) {
        try {
            File local = new File(localFile);
            this.shell("screencap -p /sdcard/tmp/screenshot.png");

            this.device.pullFile("/sdcard/tmp/screenshot.png", local.getAbsolutePath());
            return true;
        } catch (Exception var4) {
            logger.error("Unable to take snapshot", var4);
            return false;
        }
    }

    public String getSystemProperty(String key) {
        return this.device.getProperty(key);
    }

    public String getProperty(String key) {
        try {
            return this.manager.getVariable(key);
        } catch (IOException var3) {
            logger.error("Unable to get variable: " + key, var3);
            return null;
        }
    }

    public Collection<String> getPropertyList() {
        try {
            return this.manager.listVariable();
        } catch (IOException var2) {
            logger.error("Unable to get variable list", var2);
            return null;
        }
    }

    public void wake() {
        try {
            this.manager.wake();
        } catch (IOException var2) {
            logger.error("Unable to wake device (too sleepy?)", var2);
        }

    }

    private String shell(String... args) {
        StringBuilder cmd = new StringBuilder();
        String[] arr$ = args;
        int len$ = args.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String arg = arr$[i$];
            cmd.append(arg).append(" ");
        }

        return this.shell(cmd.toString());
    }

    public String shell(String cmd) {
        return this.shell(cmd, 5000);
    }

    public String shell(String cmd, long timeout) {
        CommandOutputReceiver capture = new CommandOutputReceiver();
        try {
            this.device.executeShellCommand(cmd, capture, timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException var5) {
            logger.error("Error executing command: " + cmd, var5);
            return null;
        } catch (ShellCommandUnresponsiveException var6) {
            logger.error("Error executing command: " + cmd, var6);
            return null;
        } catch (AdbCommandRejectedException var7) {
            logger.error("Error executing command: " + cmd, var7);
            return null;
        } catch (IOException var8) {
            logger.error("Error executing command: " + cmd, var8);
            return null;
        }
        return capture.toString();
    }

    public boolean installPackage(String path) {
        try {
            this.device.installPackage(path, true, new String[0]);
            return true;
        } catch (InstallException var3) {
            logger.error("Error installing package: " + path, var3);
            return false;
        }
    }

    public boolean removePackage(String packageName) {
        try {
            String result = this.device.uninstallPackage(packageName);
            if (result != null) {
                logger.error("Got error uninstalling package " + packageName + ": " + result);
                return false;
            } else {
                return true;
            }
        } catch (InstallException var3) {
            logger.error("Error installing package: " + packageName, var3);
            return false;
        }
    }

    public void press(String keyName, TouchPressType type) {
        try {
            switch (type) {
                case DOWN_AND_UP:
                    this.manager.press(keyName);
                    break;
                case DOWN:
                    this.manager.keyDown(keyName);
                    break;
                case UP:
                    this.manager.keyUp(keyName);
            }
        } catch (IOException var4) {
            logger.error("Error sending press event: " + keyName + " " + type, var4);
        }

    }

    public void press(PhysicalButton key, TouchPressType type) {
        this.press(key.getKeyName(), type);
    }

    public void type(String string) {
        try {
            this.manager.type(string);
        } catch (Exception var3) {
            logger.error("Error Typing: " + string, var3);
        }

    }

    public void touch(int x, int y, TouchPressType type) {
        try {
            switch (type) {
                case DOWN_AND_UP:
                    this.manager.tap(x, y);
                    break;
                case DOWN:
                    this.manager.touchDown(x, y);
                    break;
                case UP:
                    this.manager.touchUp(x, y);
                    break;
                case MOVE:
                    this.manager.touchMove(x, y);
            }
        } catch (Exception var5) {
            logger.error("Error sending touch event: " + x + " " + y + " " + type, var5);
        }

    }

    public void reboot(String into) {
        try {
            this.device.reboot(into);
        } catch (TimeoutException var3) {
            logger.error("Unable to reboot device", var3);
        } catch (AdbCommandRejectedException var4) {
            logger.error("Unable to reboot device", var4);
        } catch (Exception var5) {
            logger.error("Unable to reboot device", var5);
        }

    }

    public void startActivity(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags) {
        List<String> intentArgs = this.buildIntentArgString(uri, action, data, mimetype, categories, extras, component, flags);
        this.shell((String[]) Lists.asList("am", "start", intentArgs.toArray(ZERO_LENGTH_STRING_ARRAY)).toArray(ZERO_LENGTH_STRING_ARRAY));
    }

    public void broadcastIntent(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags) {
        List<String> intentArgs = this.buildIntentArgString(uri, action, data, mimetype, categories, extras, component, flags);
        this.shell((String[]) Lists.asList("am", "broadcast", intentArgs.toArray(ZERO_LENGTH_STRING_ARRAY)).toArray(ZERO_LENGTH_STRING_ARRAY));
    }

    private List<String> buildIntentArgString(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags) {
        List<String> parts = Lists.newArrayList();
        if (!isNullOrEmpty(action)) {
            parts.add("-a");
            parts.add(action);
        }

        if (!isNullOrEmpty(data)) {
            parts.add("-d");
            parts.add(data);
        }

        if (!isNullOrEmpty(mimetype)) {
            parts.add("-t");
            parts.add(mimetype);
        }

        Iterator i$ = categories.iterator();

        while (i$.hasNext()) {
            String category = (String) i$.next();
            parts.add("-c");
            parts.add(category);
        }

        i$ = extras.entrySet().iterator();

        while (i$.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) i$.next();
            Object value = entry.getValue();
            String valueString;
            String arg;
            if (value instanceof Integer) {
                valueString = Integer.toString((Integer) value);
                arg = "--ei";
            } else if (value instanceof Boolean) {
                valueString = Boolean.toString((Boolean) value);
                arg = "--ez";
            } else {
                valueString = value.toString();
                arg = "--es";
            }

            parts.add(arg);
            parts.add(entry.getKey());
            parts.add(valueString);
        }

        if (!isNullOrEmpty(component)) {
            parts.add("-n");
            parts.add(component);
        }

        if (flags != 0) {
            parts.add("-f");
            parts.add(Integer.toString(flags));
        }

        if (!isNullOrEmpty(uri)) {
            parts.add(uri);
        }

        return parts;
    }

    public Map<String, Object> instrument(String packageName, Map<String, Object> args) {
        List<String> shellCmd = Lists.newArrayList(new String[]{"am", "instrument", "-w", "-r"});
        Iterator i$ = args.entrySet().iterator();

        while (i$.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry) i$.next();
            String key = (String) entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                shellCmd.add("-e");
                shellCmd.add(key);
                shellCmd.add(value.toString());
            }
        }

        shellCmd.add(packageName);
        String result = this.shell((String[]) shellCmd.toArray(ZERO_LENGTH_STRING_ARRAY));
        return convertInstrumentResult(result);
    }

    private Map<String, Object> convertInstrumentResult(String result) {
        Map<String, Object> map = Maps.newHashMap();
        Pattern pattern = Pattern.compile("^INSTRUMENTATION_(\\w+): ", 8);
        Matcher matcher = pattern.matcher(result);
        int previousEnd = 0;

        String previousWhich;
        String resultLine;
        int splitIndex;
        String key;
        String value;
        for (previousWhich = null; matcher.find(); previousWhich = matcher.group(1)) {
            if ("RESULT".equals(previousWhich)) {
                resultLine = result.substring(previousEnd, matcher.start()).trim();
                splitIndex = resultLine.indexOf("=");
                key = resultLine.substring(0, splitIndex);
                value = resultLine.substring(splitIndex + 1);
                map.put(key, value);
            }
            previousEnd = matcher.end();
        }

        if ("RESULT".equals(previousWhich)) {
            resultLine = result.substring(previousEnd, matcher.start()).trim();
            splitIndex = resultLine.indexOf("=");
            key = resultLine.substring(0, splitIndex);
            value = resultLine.substring(splitIndex + 1);
            map.put(key, value);
        }
        return map;
    }

    public void drag(int startx, int starty, int endx, int endy, int steps, long ms) {
        final long iterationTime = ms / (long) steps;
        LinearInterpolator lerp = new LinearInterpolator(steps);
        Point start = new Point(startx, starty);
        Point end = new Point(endx, endy);
        lerp.interpolate(start, end, new LinearInterpolator.Callback() {
            public void step(Point point) {
                try {
                    AdbDevice.this.manager.touchMove(point.getX(), point.getY());
                } catch (IOException var4) {
                    logger.error("Error sending drag start event", var4);
                }

                try {
                    Thread.sleep(iterationTime);
                } catch (InterruptedException var3) {
                    logger.error("Error sleeping", var3);
                }

            }

            public void start(Point point) {
                try {
                    AdbDevice.this.manager.touchDown(point.getX(), point.getY());
                    AdbDevice.this.manager.touchMove(point.getX(), point.getY());
                } catch (IOException var4) {
                    logger.error("Error sending drag start event", var4);
                }

                try {
                    Thread.sleep(iterationTime);
                } catch (InterruptedException var3) {
                    logger.error("Error sleeping", var3);
                }

            }

            public void end(Point point) {
                try {
                    AdbDevice.this.manager.touchMove(point.getX(), point.getY());
                    AdbDevice.this.manager.touchUp(point.getX(), point.getY());
                } catch (IOException var3) {
                    logger.error("Error sending drag end event", var3);
                }

            }
        });
    }

    public Collection<String> getViewIdList() {
        try {
            return this.manager.listViewIds();
        } catch (IOException var2) {
            logger.error("Error retrieving view IDs", var2);
            return new ArrayList();
        }
    }

    public IAdbView getView(ISelector selector) {
        return selector.getView(this.manager);
    }

    public Collection<IAdbView> getViews(IMultiSelector selector) {
        return selector.getViews(this.manager);
    }

    public IAdbView getRootView() {
        try {
            return this.manager.getRootView();
        } catch (IOException var2) {
            logger.error("Error retrieving root view");
            return null;
        }
    }
}
