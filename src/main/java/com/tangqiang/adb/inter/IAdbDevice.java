package com.tangqiang.adb.inter;

import com.android.ddmlib.IDevice;
import com.tangqiang.adb.types.KeyCode;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public interface IAdbDevice {

    IDevice device();

    String getProperty(String key);

    String shell(String cmd);

    String shell(String cmd, long timeout);

    boolean installPackage(String path);

    boolean removePackage(String packageName);

    String press(KeyCode keycode);

    String press(KeyCode keycode, boolean longpress);

    String type(String message);

    String touch(int x, int y);

    String touch(int x, int y, long ms);

    void reboot(String into);

    void wake();

    IAdbImage takeSnapshotFrame();

    boolean takeSnapshot(String localFile);

    Future<String> getSystemProperty(String key);

    void startActivity(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags);

    void broadcastIntent(String uri, String action, String data, String mimetype, Collection<String> categories, Map<String, Object> extras, String component, int flags);

    String drag(int startx, int starty, int endx, int endy, long ms);

    void dispose();
}
