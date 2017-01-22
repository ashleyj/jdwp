package aura.jdw;

import aura.jdw.vm.VMClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ash on 23/12/2016.
 */
public class Utils {

    private static int SHORT_SIZE = 2;
    private static int INT_SIZE = 4;
    private static int LONG_SIZE = 8;

    public static byte getByte(int index, byte[] data) {
        return data[index];
    }

    public static short getShort(int index, byte[] data) {
        return ByteBuffer.wrap(data,index,SHORT_SIZE).getShort();
    }

    public static int getInt(int index, byte[] data) {
        return ByteBuffer.wrap(data,index,INT_SIZE).getInt();
    }

    public static byte[] squash(byte[]... bytes) {
        int size = 0;
        for (byte[] aByte : bytes) {
            size += aByte.length;
        }
        ByteBuffer buffer = ByteBuffer.wrap(new byte[size]);
        for (byte[] aByte : bytes) {
            buffer = buffer.put(aByte);
        }
        return buffer.array();
    }

    public static byte[] toBytes(int field) {
        return ByteBuffer.allocate(INT_SIZE).putInt(field).array();
    }

    public static byte[] toBytes(long field) {
        return ByteBuffer.allocate(LONG_SIZE).putLong(field).array();
    }

    public static byte[] toBytes(String string) {
        if (string == null) {
            System.err.println("Converting null string to empty string");
            string = "";
        }
        byte[] size = toBytes(string.length());
        return squash(size, string.getBytes());
    }

    public static byte[] toBytes(String[] stringArray) throws IOException {
        if (stringArray == null || stringArray.length == 0) {
            return toBytes(0);
        }

        byte[] countBuffer = toBytes(stringArray.length);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(countBuffer,0, countBuffer.length);
        for (String s : stringArray) {
            out.write(toBytes(s));
        }
        return out.toByteArray();
    }

    public static String getString(int index , byte[] data) throws UnsupportedEncodingException {
        int length = getInt(index, data);
        byte[] byteSegment = Arrays.copyOfRange(data,index += INT_SIZE, index + length);
        return new String(byteSegment, "UTF-8");
    }

    public static byte[] toBytes(List<VMClass> classes) {
        if (classes == null) {
            return null;
        }
        byte[] countBuffer = toBytes(classes.size());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(countBuffer,0, countBuffer.length);
        for (VMClass aClass : classes) {
            out.write(aClass.toBytes(),0, aClass.toBytes().length);
        }
        return out.toByteArray();
    }

}
