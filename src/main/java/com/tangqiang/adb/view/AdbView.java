package com.tangqiang.adb.view;

import com.google.common.collect.Lists;
import com.tangqiang.adb.AdbManager;
import com.tangqiang.adb.inter.IAdbView;
import com.tangqiang.adb.types.Rect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public class AdbView implements IAdbView {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public static final String ACCESSIBILITY_IDS = "accessibilityids";
    public static final String VIEW_ID = "viewid";
    private String viewType;
    private List<String> ids;
    private AdbManager manager;

    public AdbView(String viewType, List<String> ids) {
        this.viewType = viewType;
        this.ids = ids;
    }

    public void setManager(AdbManager manager) {
        this.manager = manager;
    }

    private String queryView(String query) {
        try {
            return this.manager.queryView(this.viewType, this.ids, query);
        } catch (IOException var3) {
            LOG.info("Error querying view: " + var3.getMessage());
            return "";
        }
    }

    public Rect getLocation() {
        List<String> result = Lists.newArrayList(this.queryView("getlocation").split(" "));
        if (result.size() == 4) {
            try {
                int left = Integer.parseInt((String)result.get(0));
                int top = Integer.parseInt((String)result.get(1));
                int width = Integer.parseInt((String)result.get(2));
                int height = Integer.parseInt((String)result.get(3));
                return new Rect(left, top, left + width, top + height);
            } catch (NumberFormatException var6) {
                return new Rect();
            }
        } else {
            return new Rect();
        }
    }

    public String getText() {
        return this.queryView("gettext");
    }

    public String getViewClass() {
        return this.queryView("getclass");
    }

    public boolean getChecked() {
        return Boolean.valueOf(this.queryView("getchecked").trim());
    }

    public boolean getEnabled() {
        return Boolean.valueOf(this.queryView("getenabled").trim());
    }

    public boolean getSelected() {
        return Boolean.valueOf(this.queryView("getselected").trim());
    }

    public void setSelected(boolean selected) {
        this.queryView("setselected " + selected);
    }

    public boolean getFocused() {
        return Boolean.valueOf(this.queryView("getselected").trim());
    }

    public void setFocused(boolean focused) {
        this.queryView("setfocused " + focused);
    }

    public IAdbView getParent() {
        List<String> results = Lists.newArrayList(this.queryView("getparent").split(" "));
        if (results.size() == 2) {
            AdbView parent = new AdbView("accessibilityids", results);
            parent.setManager(this.manager);
            return parent;
        } else {
            return null;
        }
    }

    public List<IAdbView> getChildren() {
        List<String> results = Lists.newArrayList(this.queryView("getchildren").split(" "));
        if (results.size() % 2 != 0) {
            return new ArrayList();
        } else {
            List<IAdbView> children = new ArrayList();

            for(int i = 0; i < results.size() / 2; ++i) {
                List<String> ids = Lists.newArrayList(new String[]{(String)results.get(2 * i), (String)results.get(2 * i + 1)});
                AdbView child = new AdbView("accessibilityids", ids);
                child.setManager(this.manager);
                children.add(child);
            }

            return children;
        }
    }

    public AccessibilityIds getAccessibilityIds() {
        List<String> results = Lists.newArrayList(this.queryView("getaccessibilityids").split(" "));
        if (results.size() == 2) {
            try {
                return new AccessibilityIds(Integer.parseInt((String)results.get(0)), Long.parseLong((String)results.get(1)));
            } catch (NumberFormatException var3) {
                LOG.info( "Error retrieving accesibility ids: " + var3.getMessage());
            }
        }

        return new AccessibilityIds(0, 0L);
    }
}
