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
public class SelectorId implements ISelector {
    private String id;

    public SelectorId(String id) {
        this.id = id;
    }

    public IAdbView getView(AdbManager manager) {
        AdbView view = new AdbView("viewid", Lists.newArrayList(new String[]{this.id}));
        view.setManager(manager);
        return view;
    }
}

