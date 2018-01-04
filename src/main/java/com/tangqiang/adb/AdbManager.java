package com.tangqiang.adb;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.tangqiang.adb.core.AdbException;
import com.tangqiang.adb.inter.IAdbView;
import com.tangqiang.adb.types.PhysicalButton;
import com.tangqiang.adb.view.AdbView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Tom
 * @version 1.0 2018-01-04 0004 Tom create
 * @date 2018-01-04 0004
 * @copyright Copyright © 2018 Grgbanking All rights reserved.
 */
public class AdbManager {
    private Logger LOG = LoggerFactory.getLogger(getClass());
    private Socket monkeySocket;
    private BufferedWriter monkeyWriter;
    private BufferedReader monkeyReader;

    public AdbManager(Socket monkeySocket) throws IOException {
        this.monkeySocket = monkeySocket;
        this.monkeyWriter = new BufferedWriter(new OutputStreamWriter(monkeySocket.getOutputStream()));
        this.monkeyReader = new BufferedReader(new InputStreamReader(monkeySocket.getInputStream()));
    }

    protected void finalize() throws Throwable {
        try {
            this.quit();
        } finally {
            this.close();
            super.finalize();
        }

    }

    public boolean touchDown(int x, int y) throws IOException {
        return this.sendMonkeyEvent("touch down " + x + " " + y);
    }

    public boolean touchUp(int x, int y) throws IOException {
        return this.sendMonkeyEvent("touch up " + x + " " + y);
    }

    public boolean touchMove(int x, int y) throws IOException {
        return this.sendMonkeyEvent("touch move " + x + " " + y);
    }

    public boolean touch(int x, int y) throws IOException {
        return this.sendMonkeyEvent("tap " + x + " " + y);
    }

    public boolean press(String name) throws IOException {
        return this.sendMonkeyEvent("press " + name);
    }

    public boolean keyDown(String name) throws IOException {
        return this.sendMonkeyEvent("key down " + name);
    }

    public boolean keyUp(String name) throws IOException {
        return this.sendMonkeyEvent("key up " + name);
    }

    public boolean press(PhysicalButton button) throws IOException {
        return this.press(button.getKeyName());
    }

    private String sendMonkeyEventAndGetResponse(String command) throws IOException {
        command = command.trim();
        LOG.info("Monkey Command: " + command );
        this.monkeyWriter.write(command + "\n");
        this.monkeyWriter.flush();
        String monkeyResponse = this.monkeyReader.readLine();
        LOG.info("Monkey Response: " + monkeyResponse  + "  command:" + command);
        return monkeyResponse;
    }

    private boolean parseResponseForSuccess(String monkeyResponse) {
        if (monkeyResponse == null) {
            return false;
        } else {
            return monkeyResponse.startsWith("OK");
        }
    }

    private String parseResponseForExtra(String monkeyResponse) {
        int offset = monkeyResponse.indexOf(58);
        return offset < 0 ? "" : monkeyResponse.substring(offset + 1);
    }

    private boolean sendMonkeyEvent(String command) throws IOException {
        synchronized(this) {
            String monkeyResponse = this.sendMonkeyEventAndGetResponse(command);
            return this.parseResponseForSuccess(monkeyResponse);
        }
    }

    public void close() {
        try {
            this.monkeySocket.close();
        } catch (IOException var4) {
            LOG.info("Unable to close monkeySocket", var4);
        }

        try {
            this.monkeyReader.close();
        } catch (IOException var3) {
            LOG.info( "Unable to close monkeyReader", var3);
        }

        try {
            this.monkeyWriter.close();
        } catch (IOException var2) {
            LOG.info("Unable to close monkeyWriter", var2);
        }

    }

    public String getVariable(String name) throws IOException {
        synchronized(this) {
            String response = this.sendMonkeyEventAndGetResponse("getvar " + name);
            return !this.parseResponseForSuccess(response) ? null : this.parseResponseForExtra(response);
        }
    }

    public Collection<String> listVariable() throws IOException {
        synchronized(this) {
            String response = this.sendMonkeyEventAndGetResponse("listvar");
            if (!this.parseResponseForSuccess(response)) {
                Collections.emptyList();
            }

            String extras = this.parseResponseForExtra(response);
            return Lists.newArrayList(extras.split(" "));
        }
    }

    public void done() throws IOException {
        synchronized(this) {
            this.sendMonkeyEventAndGetResponse("done");
        }
    }

    public void quit() throws IOException {
        synchronized(this) {
            try {
                this.sendMonkeyEventAndGetResponse("quit");
            } catch (SocketException var4) {
                ;
            }

        }
    }

    public boolean tap(int x, int y) throws IOException {
        return this.sendMonkeyEvent("tap " + x + " " + y);
    }

    public boolean type(String text) throws IOException {
        StringTokenizer tok = new StringTokenizer(text, "\n", true);

        while(tok.hasMoreTokens()) {
            String line = tok.nextToken();
            boolean success;
            if ("\n".equals(line)) {
                success = this.press(PhysicalButton.ENTER);
                if (!success) {
                    return false;
                }
            } else {
                success = this.sendMonkeyEvent("type " + line);
                if (!success) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean type(char keyChar) throws IOException {
        return this.type(Character.toString(keyChar));
    }

    public void wake() throws IOException {
        this.sendMonkeyEvent("wake");
    }

    public Collection<String> listViewIds() throws IOException {
        synchronized(this) {
            String response = this.sendMonkeyEventAndGetResponse("listviews");
            if (!this.parseResponseForSuccess(response)) {
                Collections.emptyList();
            }

            String extras = this.parseResponseForExtra(response);
            return Lists.newArrayList(extras.split(" "));
        }
    }

    public String queryView(String idType, List<String> ids, String query) throws IOException {
        StringBuilder monkeyCommand = new StringBuilder("queryview " + idType + " ");
        Iterator i$ = ids.iterator();

        String response;
        while(i$.hasNext()) {
            response = (String)i$.next();
            monkeyCommand.append(response).append(" ");
        }

        monkeyCommand.append(query);
        synchronized(this) {
            response = this.sendMonkeyEventAndGetResponse(monkeyCommand.toString());
            if (!this.parseResponseForSuccess(response)) {
                throw new AdbException(this.parseResponseForExtra(response));
            } else {
                return this.parseResponseForExtra(response);
            }
        }
    }

    public IAdbView getRootView() throws IOException {
        synchronized(this) {
            String response = this.sendMonkeyEventAndGetResponse("getrootview");
            String extra = this.parseResponseForExtra(response);
            List<String> ids = Arrays.asList(extra.split(" "));
            if (this.parseResponseForSuccess(response) && ids.size() == 2) {
                AdbView root = new AdbView("accessibilityids", ids);
                root.setManager(this);
                return root;
            } else {
                throw new AdbException(extra);
            }
        }
    }

    public String getViewsWithText(String text) throws IOException {
        synchronized(this) {
            if (text.split(" ").length > 1) {
                text = "\"" + text + "\"";
            }

            String response = this.sendMonkeyEventAndGetResponse("getviewswithtext " + text);
            if (!this.parseResponseForSuccess(response)) {
                throw new AdbException(this.parseResponseForExtra(response));
            } else {
                return this.parseResponseForExtra(response);
            }
        }
    }
}
