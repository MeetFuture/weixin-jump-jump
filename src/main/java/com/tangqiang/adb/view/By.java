package com.tangqiang.adb.view;

import com.tangqiang.adb.inter.IMultiSelector;
import com.tangqiang.adb.inter.ISelector;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class By {
    public By() {
    }

    public static ISelector id(String id) {
        return new SelectorId(id);
    }

    public static ISelector accessibilityIds(int windowId, long accessibilityId) {
        return new SelectorAccessibilityIds(windowId, accessibilityId);
    }

    public static IMultiSelector text(String searchText) {
        return new MultiSelectorText(searchText);
    }
}
