package com.tangqiang.adb.inter;

import com.tangqiang.adb.AdbManager;
import com.tangqiang.adb.types.PhysicalButton;
import com.tangqiang.adb.types.TouchPressType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public interface IAdbDevice {
    AdbManager getManager();

    void dispose();

    IAdbImage takeSnapshotFrame();

    boolean takeSnapshot(String localFile);

    void reboot(@Nullable String var1);

    Collection<String> getPropertyList();

    String getProperty(String var1);

    String getSystemProperty(String var1);

    void touch(int var1, int var2, TouchPressType var3);

    void press(String var1, TouchPressType var2);

    void press(PhysicalButton var1, TouchPressType var2);

    void drag(int x1, int y1, int x2, int y2, int var5, long var6);

    void type(String message);

    String shell(String command);

    String shell(String command, long timeout);

    boolean installPackage(String var1);

    boolean removePackage(String var1);

    void startActivity(@Nullable String var1, @Nullable String var2, @Nullable String var3, @Nullable String var4, Collection<String> var5, Map<String, Object> var6, @Nullable String var7, int var8);

    void broadcastIntent(@Nullable String var1, @Nullable String var2, @Nullable String var3, @Nullable String var4, Collection<String> var5, Map<String, Object> var6, @Nullable String var7, int var8);

    Map<String, Object> instrument(String var1, Map<String, Object> var2);

    void wake();

    Collection<String> getViewIdList();

    IAdbView getView(ISelector var1);

    IAdbView getRootView();

    Collection<IAdbView> getViews(IMultiSelector var1);
}
