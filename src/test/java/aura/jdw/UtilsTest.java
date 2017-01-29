package aura.jdw;

import aura.jdw.Utils;
import aura.jdw.vm.VMClass;
import org.junit.Test;
import sun.misc.URLClassPath;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by ash on 23/12/2016.
 */
public class UtilsTest {
    @Test
    public void getByte() throws Exception {
        byte[] a = new byte[] {1,8,4,20};
        assertTrue(Utils.getByte(0,a) == 1);
    }

    @Test
    public void getShort() throws Exception {
        byte[] a = new byte[] {1,8,4,20};
        assertTrue(Utils.getShort(0,a) == 264);
    }

    @Test
    public void getInt() throws Exception {
        byte[] a = new byte[] {1,8,4,20};
        assertTrue(Utils.getShort(1,a) == 2052);
    }

    @Test
    public void toBytes() throws Exception {
        /* Test int conversion */
        byte[] bytes = Utils.toBytes(20);
        assertTrue(Arrays.equals(bytes, new byte[] {0,0,0,20}));

        /* Test string conversion */
        String testString = "test string";
        byte[] testStringBytes = new byte[] {0, 0, 0, 11, 116, 101, 115, 116, 32, 115, 116, 114, 105, 110, 103};
        bytes = Utils.toBytes(testString);
        assertTrue(Arrays.equals(bytes,testStringBytes));


        /* Test class list conversion */
        byte[] classBytes = new byte[] { 0, 0, 0, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 23, 76, 115, 117, 110, 47,
                            109, 105, 115, 99, 47, 85, 82, 76, 67, 108, 97, 115, 115, 80, 97, 116, 104, 59, 0, 0,
                            0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59,
                            0, 0, 0, 7, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 19, 76, 106, 97, 118, 97, 47, 117, 116,
                            105, 108, 47, 72, 97, 115, 104, 83, 101, 116, 59, 0, 0, 0, 113, 60, 69, 58, 76, 106, 97,
                            118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 59, 62, 76, 106, 97, 118,
                            97, 47, 117, 116, 105, 108, 47, 65, 98, 115, 116, 114, 97, 99, 116, 83, 101, 116, 60, 84,
                            69, 59, 62, 59, 76, 106, 97, 118, 97, 47, 117, 116, 105, 108, 47, 83, 101, 116, 60, 84, 69,
                            59, 62, 59, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 67, 108, 111, 110, 101, 97, 98,
                            108, 101, 59, 76, 106, 97, 118, 97, 47, 105, 111, 47, 83, 101, 114, 105, 97, 108, 105, 122,
                            97, 98, 108, 101, 59, 0, 0, 0, 7};

        List<VMClass> classList = Arrays.asList(new VMClass(URLClassPath.class,1),
                                                new VMClass(java.util.HashSet.class,2));
        bytes = Utils.toBytes(classList);
        assertTrue(Arrays.equals(bytes,classBytes));

    }

    @Test
    public void squash() throws Exception {
        byte[] a = new byte[] {0,0,0,20};
        byte[] b = new byte[] {0,0,0,8};

        byte[] squashedBytes = Utils.squash(a,b);
        assertTrue(Arrays.equals(squashedBytes,new byte[]{0,0,0,20,0,0,0,8}));

        squashedBytes = Utils.squash(a);
        assertTrue(Arrays.equals(squashedBytes,a));
    }

    @Test
    public void testBytes() throws Exception {
        byte[] bytes = Utils.toBytes(20);
        assertTrue(Arrays.equals(bytes, new byte[] {0,0,0,20}));
    }


    @Test
    public void getString() throws UnsupportedEncodingException {
        String testString = "test string";
        byte[] bytes = Utils.squash(Utils.toBytes(testString.length()), testString.getBytes());
        assertTrue(testString.equals(Utils.getString(0, bytes)));
    }
}