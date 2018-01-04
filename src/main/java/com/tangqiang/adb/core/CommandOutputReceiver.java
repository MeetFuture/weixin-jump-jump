package com.tangqiang.adb.core;

import com.android.ddmlib.IShellOutputReceiver;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class CommandOutputReceiver implements IShellOutputReceiver {
    private final StringBuilder builder = new StringBuilder();

    public CommandOutputReceiver() {
    }

    public void flush() {
    }

    public boolean isCancelled() {
        return false;
    }

    public void addOutput(byte[] data, int offset, int length) {
        String message = new String(data, offset, length);
        this.builder.append(message);
    }

    public String toString() {
        return this.builder.toString();
    }
}
