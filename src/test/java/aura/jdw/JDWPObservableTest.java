package aura.jdw;

import aura.jdw.protocol.JDWPConstants;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by ash on 23/12/2016.
 */
public class JDWPObservableTest {

    @Test
    public void processEventRequest() throws Exception {
        byte[] data = JDWPObservable.processEventRequest(JDWPConstants.EVENT_REQUEST_COMMAND_SET, new byte[]{8, 0, 0, 0, 0, 0}, 4);
        assertTrue(Arrays.equals(data, new byte[] {0,0,0,4}));
    }


    @Test
    public void getVMClassPath() throws IOException {

        String path1 = "/tmp/test";
        String path2 = "/tmp/test2";
        String path3 = "/tmp/test3";

        /* set class path */
        System.setProperty("java.class.path",path1 + ":" + path2);
        /* set boot class path */
        System.setProperty("sun.boot.class",path3);

        byte[] data = JDWPObservable.getVMClassPath();
        byte[] classPathEntries = Utils.squash(
                                                    Utils.toBytes("."),   /* "." */
                                                    Utils.toBytes(2),        /* num of class paths*/
                                                    Utils.toBytes(path1),           /* path 1 */
                                                    Utils.toBytes(path2),           /* path2 */
                                                    Utils.toBytes(1),           /* num of boot paths */
                                                    Utils.toBytes(path3)             /* path3 */
                                                );
        assertTrue(Arrays.equals(data, classPathEntries));
    }


}