package com.tangqiang;

/**
 * Unit test
 */
public class AppTest {
//        extends TestCase {
//    IAdbBackend backend = null;
//    IAdbDevice device = null;
//    public AppTest(String testName) {
//        super(testName);
//    }
//
//
//    @Override
//    protected void setUp() throws Exception {
//        backend = new AdbBackend();
//        device = backend.waitForConnection();
//    }
//
//
//    public void testCapture() {
//        long start = System.currentTimeMillis();
//        String file = "target/tmp/output" + start + ".png";
//        boolean success = device.takeSnapshot(file);
//        Assert.assertTrue(success);
//    }
//
//    /***/
//    public void testCapture1() {
//        long start = System.currentTimeMillis();
//        IAdbImage snapshot = device.takeSnapshotFrame();
//        snapshot.writeToFile("target/tmp/output" + start + ".png", "png");
//    }
//
//    public void testPress() {
//        String result = device.press(KeyCode.HOME);
//        Assert.assertNotNull(result);
//    }
//
//
//    @Override
//    protected void tearDown() throws Exception {
//        device.dispose();
//        backend.shutdown();
//    }
}
