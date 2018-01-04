package com.tangqiang.adb.types;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */

public enum PhysicalButton {

    /**
     * home键
     */
    HOME("KEYCODE_HOME"),
    SEARCH("KEYCODE_SEARCH"),
    MENU("KEYCODE_MENU"),
    /**
     * back键
     */
    BACK("KEYCODE_BACK"),
    DPAD_UP("DPAD_UP"),
    DPAD_DOWN("DPAD_DOWN"),
    DPAD_LEFT("DPAD_LEFT"),
    DPAD_RIGHT("DPAD_RIGHT"),
    DPAD_CENTER("DPAD_CENTER"),


    /**
     * send键
     */
    KEYCODE_CALL("KEYCODE_CALL"),

    /**
     * end键
     */
    KEYCODE_ENDCALL("KEYCODE_ENDCALL"),

    /**
     * 上导航键
     */
    KEYCODE_DPAD_UP("KEYCODE_DPAD_UP"),

    /**
     * 下导航键
     */
    KEYCODE_DPAD_DOWN("KEYCODE_DPAD_DOWN"),

    /**
     * 左导航
     */
    KEYCODE_DPAD_LEFT("KEYCODE_DPAD_LEFT"),

    /**
     * 右导航键
     */
    KEYCODE_DPAD_RIGHT("KEYCODE_DPAD_RIGHT"),

    /**
     * ok键
     */
    KEYCODE_DPAD_CENTER("KEYCODE_DPAD_CENTER"),

    /**
     * 上音量键
     */
    KEYCODE_VOLUME_UP("KEYCODE_VOLUME_UP"),

    /**
     * 下音量键
     */
    KEYCODE_VOLUME_DOWN("KEYCODE_VOLUME_DOWN"),

    /**
     * power键 KEYCODE_POWER
     */
    KEYCODE_POWER("KEYCODE_POWER"),

    /**
     * camera键 KEYCODE_CAMERA
     */
    KEYCODE_CAMERA("KEYCODE_CAMERA"),


    ENTER("enter");

    private String keyName;

    private PhysicalButton(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return this.keyName;
    }
}
