package com.tangqiang.adb.core;

import com.android.ddmlib.IShellOutputReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 执行shell时候输出
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class ShellOutputReceiver implements IShellOutputReceiver {
    private final Logger log = LoggerFactory.getLogger("Shell execute");


    public void addOutput(byte[] data, int offset, int length) {
        String message = new String(data, offset, length);
        String[] arr$ = message.split("\n");
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String line = arr$[i$];
            this.log.info(line);
        }
    }

    public void flush() {
    }

    public boolean isCancelled() {
        return false;
    }
}
