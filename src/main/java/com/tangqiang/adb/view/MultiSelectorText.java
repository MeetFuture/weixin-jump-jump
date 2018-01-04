package com.tangqiang.adb.view;

import com.google.common.collect.Lists;
import com.tangqiang.adb.AdbManager;
import com.tangqiang.adb.inter.IAdbView;
import com.tangqiang.adb.inter.IMultiSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class MultiSelectorText implements IMultiSelector {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    private String text;

    public MultiSelectorText(String text) {
        this.text = text;
    }

    public Collection<IAdbView> getViews(AdbManager manager) {
        String response;
        List ids;
        try {
            response = manager.getViewsWithText(this.text);
            ids = Arrays.asList(response.split(" "));
        } catch (IOException var8) {
            LOG.error("Error communicating with device: " + var8.getMessage());
            return new ArrayList();
        }

        if (ids.size() % 2 != 0) {
            LOG.error("Error retrieving views: " + response);
            return Collections.emptyList();
        } else {
            List<IAdbView> views = new ArrayList();

            for(int i = 0; i < ids.size() / 2; ++i) {
                List<String> accessibilityIds = Lists.newArrayList(new String[]{(String)ids.get(2 * i), (String)ids.get(2 * i + 1)});
                AdbView view = new AdbView("accessibilityids", accessibilityIds);
                view.setManager(manager);
                views.add(view);
            }

            return views;
        }
    }
}
