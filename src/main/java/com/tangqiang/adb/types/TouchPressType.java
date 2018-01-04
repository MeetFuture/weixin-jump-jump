package com.tangqiang.adb.types;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public enum TouchPressType {
    DOWN("down"),
    UP("up"),
    DOWN_AND_UP("downAndUp"),
    MOVE("move");

    private static final Map<String, TouchPressType> identifierToEnum = new HashMap();

    static {
        TouchPressType[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            TouchPressType type = arr$[i$];
            identifierToEnum.put(type.identifier, type);
        }

    }

    private String identifier;

    private TouchPressType(String identifier) {
        this.identifier = identifier;
    }

    public static TouchPressType fromIdentifier(String name) {
        return (TouchPressType) identifierToEnum.get(name);
    }

    public String getIdentifier() {
        return this.identifier;
    }
}