package com.tangqiang.adb;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.google.common.collect.Lists;
import com.tangqiang.adb.core.CommandOutputReceiver;
import com.tangqiang.adb.core.ShellOutputReceiver;
import com.tangqiang.adb.image.AdbImage;
import com.tangqiang.adb.inter.IAdbDevice;
import com.tangqiang.adb.inter.IAdbImage;
import com.tangqiang.adb.types.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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


    public AdbDevice(IDevice device) {
        this.device = device;
    }

    @Override
    public IDevice device() {
        return device;
    }

    @Override
    public String getProperty(String key) {
        try {
            return this.device.getProperty(key);
        } catch (Exception e) {
            logger.error("Unable to get variable: " + key, e);
            return null;
        }
    }

    private String shell(Object... args) {
        StringBuilder cmd = new StringBuilder();
        int len = args.length;

        for (int i = 0; i < len; ++i) {
            String arg = String.valueOf(args[i]);
            cmd.append(arg).append(" ");
        }
        return this.shell(cmd.toString());
    }

    @Override
    public String shell(String cmd) {
        return this.shell(cmd, 5000);
    }

    @Override
    public String shell(String cmd, long timeout) {
        CommandOutputReceiver capture = new CommandOutputReceiver();
        try {
            this.device.executeShellCommand(cmd, capture, timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.error("Error executing command: " + cmd, e);
            return null;
        }
        return capture.toString();
    }

    @Override
    public boolean installPackage(String path) {
        try {
            this.device.installPackage(path, true, new String[0]);
            return true;
        } catch (InstallException e) {
            logger.error("Error installing package: " + path, e);
            return false;
        }
    }

    @Override
    public boolean removePackage(String packageName) {
        try {
            String result = this.device.uninstallPackage(packageName);
            if (result != null) {
                logger.error("Got error uninstalling package " + packageName + ": " + result);
                return false;
            } else {
                return true;
            }
        } catch (InstallException e) {
            logger.error("Error installing package: " + packageName, e);
            return false;
        }
    }


    @Override
    public String press(KeyCode keycode) {
        return press(keycode, false);
    }

    @Override
    public String press(KeyCode keycode, boolean longpress) {
        String cmd = "input keyevent " + (longpress ? " --longpress " : "") + keycode.getValue();
        String result = shell(cmd, 10000);
        logger.debug("Press key:" + keycode + "  Result:" + result);
        return result;
    }

    @Override
    public String type(String string) {
        String result = shell("input", "text", string);
        logger.debug("Type String:" + string + "  Result:" + result);
        return result;
    }

    @Override
    public String touch(int x, int y) {
        String result = shell("input", "tap", x, y);
        logger.debug("Touch x:" + x + "  y:" + y + "  Result:" + result);
        return result;
    }

    @Override
    public String touch(int x, int y, long ms) {
        String result = shell("input", "swipe", x, y, x, y, ms);
        logger.debug("Touch x:" + x + "  y:" + y + " time:" + ms + "  Result:" + result);
        return result;
    }

    @Override
    public void reboot(String into) {
        try {
            this.device.reboot(into);
        } catch (Exception e) {
            logger.error("Unable to reboot device", e);
        }
    }

    @Override
    public void wake() {
        try {
            this.press(KeyCode.NOTIFICATION);
        } catch (Exception e) {
            logger.error("Unable to reboot device", e);
        }
    }

    private void executeAsyncCommand(final String command) {
        this.executor.submit(new Runnable() {
            public void run() {
                try {
                    ShellOutputReceiver outputReceiver = new ShellOutputReceiver();
                    device.executeShellCommand(command, outputReceiver);
                } catch (Exception e) {
                    logger.error("Error execute command: " + command, e);
                }
            }
        });
    }

    @Override
    public IAdbImage takeSnapshotFrame() {
        try {
            return new AdbImage(this.device.getScreenshot());
        } catch (Exception e) {
            logger.error("Unable to take snapshot", e);
            return null;
        }
    }

    /**
     * @param localFile "screenshot.png"
     * @return
     */
    @Override
    public boolean takeSnapshot(String localFile) {
        try {
            File local = new File(localFile);
            this.shell("screencap", "-p", "/sdcard/tmp/screenshot.png");
            this.device.pullFile("/sdcard/tmp/screenshot.png", local.getAbsolutePath());
            return true;
        } catch (Exception e) {
            logger.error("Unable to take snapshot", e);
            return false;
        }
    }

    @Override
    public Future<String> getSystemProperty(String key) {
        return this.device.getSystemProperty(key);
    }


    @Override
    public void startActivity(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags) {
        List<String> intentArgs = this.buildIntentArgString(uri, action, data, mimetype, categories, extras, component, flags);
        this.shell((String[]) Lists.asList("am", "start", intentArgs.toArray(ZERO_LENGTH_STRING_ARRAY)).toArray(ZERO_LENGTH_STRING_ARRAY));
    }

    @Override
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


    @Override
    public String drag(int startx, int starty, int endx, int endy, long ms) {
        String result = shell("input", "swipe", startx, starty, endx, endy, ms);
        logger.debug("Drag x:" + startx + "  y:" + starty + "  endx:" + endx + "  endy:" + endy + " time:" + ms + "  Result:" + result);
        return result;
    }

    private boolean parseResultForSuccess(String result) {
        if (result == null) {
            return false;
        } else {
            return result.startsWith("OK");
        }
    }

    private boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.length() == 0;
    }

    @Override
    public void dispose() {
        this.executor.shutdown();
    }


}
