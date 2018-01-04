package com.tangqiang.adb.inter;

import com.tangqiang.adb.AdbManager;
import com.tangqiang.adb.types.Rect;

import java.util.List;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public interface IAdbView {
    void setManager(AdbManager var1);

    String getViewClass();

    String getText();

    Rect getLocation();

    boolean getChecked();

    boolean getEnabled();

    boolean getSelected();

    void setSelected(boolean var1);

    boolean getFocused();

    void setFocused(boolean var1);

    IAdbView getParent();

    List<IAdbView> getChildren();

    AccessibilityIds getAccessibilityIds();

    public static class AccessibilityIds {
        private final int windowId;
        private final long nodeId;

        public AccessibilityIds() {
            this.windowId = 0;
            this.nodeId = 0L;
        }

        public AccessibilityIds(int windowId, long nodeId) {
            this.windowId = windowId;
            this.nodeId = nodeId;
        }

        public int getWindowId() {
            return this.windowId;
        }

        public long getNodeId() {
            return this.nodeId;
        }
    }
}
