package com.tangqiang.adb;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceState;
import com.google.common.collect.Lists;
import com.tangqiang.adb.inter.IAdbBackend;
import com.tangqiang.adb.inter.IAdbDevice;
import com.tangqiang.adb.types.SdkConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class AdbBackend implements IAdbBackend {
    private static final int CONNECTION_ITERATION_TIMEOUT_MS = 200;
    private final List<IAdbDevice> devices;
    private final AndroidDebugBridge bridge;
    private final boolean initAdb;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public AdbBackend() {
        this((String) null, false);
    }

    public AdbBackend(String adbLocation, boolean noInitAdb) {
        this.devices = Lists.newArrayList();
        this.initAdb = !noInitAdb;
        if (adbLocation == null) {
            adbLocation = this.findAdb();
        }

        if (this.initAdb) {
            AndroidDebugBridge.init(false);
        }

        this.bridge = AndroidDebugBridge.createBridge(adbLocation, true);
        logger.info("adbLocation:" + adbLocation + "  noInitAdb:" + noInitAdb + "  bridge:" + bridge);
    }

    private String findAdb() {
        String location = "";
        String mrParentLocation = System.getProperty("com.android.monkeyrunner.bindir");
        if (mrParentLocation != null && mrParentLocation.length() != 0) {
            File platformTools = new File((new File(mrParentLocation)).getParent(), "platform-tools");
            location = platformTools.isDirectory() ? platformTools.getAbsolutePath() + File.separator + SdkConstants.FN_ADB : mrParentLocation + File.separator + SdkConstants.FN_ADB;
        } else {
            location = SdkConstants.FN_ADB;
        }

        logger.debug("findAdb location:" + location);
        return location;
    }

    private IDevice findAttachedDevice(String deviceIdRegex) {
        Pattern pattern = Pattern.compile(deviceIdRegex);
        IDevice[] devices = this.bridge.getDevices();
        int len = devices.length;
        if (len <= 0) {
            return null;
        }

        logger.debug("findAttachedDevice s:" + devices + "  deviceIdRegex:" + deviceIdRegex);
        for (int i = 0; i < len; ++i) {
            IDevice device = devices[i];
            String serialNumber = device.getSerialNumber();
            if (pattern.matcher(serialNumber).matches()) {
                return device;
            }
        }

        return null;
    }

    public IAdbDevice waitForConnection() {
        return this.waitForConnection(2147483647L, ".*");
    }

    public IAdbDevice waitForConnection(long timeoutMs, String deviceIdRegex) {
        do {
            IDevice device = this.findAttachedDevice(deviceIdRegex);
            if (device != null && device.getState() == DeviceState.ONLINE) {
                IAdbDevice chimpDevice = new AdbDevice(device);
                this.devices.add(chimpDevice);
                return chimpDevice;
            }

            try {
                Thread.sleep(200L);
            } catch (InterruptedException var6) {
                logger.error("Error sleeping", var6);
            }

            timeoutMs -= 200L;
        } while (timeoutMs > 0L);

        return null;
    }

    public void shutdown() {
        Iterator i$ = this.devices.iterator();

        while (i$.hasNext()) {
            IAdbDevice device = (IAdbDevice) i$.next();
            device.dispose();
        }

        if (this.initAdb) {
            AndroidDebugBridge.terminate();
        }

    }
}

