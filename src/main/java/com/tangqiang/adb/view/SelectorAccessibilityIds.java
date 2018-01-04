package com.tangqiang.adb.view;

import com.google.common.collect.Lists;
import com.tangqiang.adb.AdbManager;
import com.tangqiang.adb.inter.IAdbView;
import com.tangqiang.adb.inter.ISelector;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class SelectorAccessibilityIds implements ISelector {
    private int windowId;
    private long accessibilityId;

    public SelectorAccessibilityIds(int windowId, long accessibilityId) {
        this.windowId = windowId;
        this.accessibilityId = accessibilityId;
    }

    public IAdbView getView(AdbManager manager) {
        AdbView view = new AdbView("accessibilityids", Lists.newArrayList(new String[]{Integer.toString(this.windowId), Long.toString(this.accessibilityId)}));
        view.setManager(manager);
        return view;
    }
}