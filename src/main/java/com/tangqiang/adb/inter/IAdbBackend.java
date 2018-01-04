package com.tangqiang.adb.inter;


/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public interface IAdbBackend {
    IAdbDevice waitForConnection();

    IAdbDevice waitForConnection(long var1, String var3);

    void shutdown();
}
