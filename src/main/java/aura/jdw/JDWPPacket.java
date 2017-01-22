package aura.jdw;

/**
 * Created by ash on 21/12/2016.
 */
public abstract class JDWPPacket {
    public static int LENGTHFIELDSIZE = 4;
    public static int IDFIELDSIZE = 4;
    public static int FLAGFIELDSIZE = 1;
    public static int ERRORCODEFIELDSIZE = 2;
    public static int HEADERSIZE = 11;

    int length;
    int id;
    int flag;
    byte[] data;


    public int getId() {
        return id;
    }

    public int getFlag() {
        return flag;
    }

    public int getLength() {
        return length;
    }

    public void setData(byte[] bytes) {
        this.data = bytes;
        length += bytes.length;
    }

    public byte[] getData() {
        return data;
    }

    public int getDataSize() {
        return length - HEADERSIZE;
    }
}
