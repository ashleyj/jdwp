package aura.jdw.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static aura.jdw.Utils.*;

public class JDWPReplyPacket extends JDWPPacket {

    int error;
    private int bytes;

    public JDWPReplyPacket(int id, int flag, int error, byte[] data) {
       this.length = HEADERSIZE;
        if (data != null) {
            length += data.length;
        }
       this.id = id;
        /* Ignore flag, set it to 128 (x80)*/
       this.flag = 128;
       this.error = error;
       this.data = data;
    }

    public JDWPReplyPacket() {

    }

    public static JDWPReplyPacket createFromHeader(byte[] inbytes) {
        if (inbytes.length != HEADERSIZE) {
            throw new IllegalArgumentException("Header has invalid length");
        }

        int position = 0;
        int length = getInt(position, inbytes);
        int id = getInt(position+=LENGTHFIELDSIZE, inbytes);
        int flag = getByte(position+=IDFIELDSIZE, inbytes);
        int error = getShort(position+=FLAGFIELDSIZE, inbytes);

        return new JDWPReplyPacket(id, flag, error, null);
    }

    public int getErrorCode() {
        return error;
    }

    public byte[] getBytes() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream(length);
        try {
            byteStream.write(ByteBuffer.allocate(LENGTHFIELDSIZE).putInt(length).array());
            byteStream.write(ByteBuffer.allocate(IDFIELDSIZE).putInt(id).array());
            byteStream.write(ByteBuffer.allocate(FLAGFIELDSIZE).put((byte) flag).array());
            byteStream.write(ByteBuffer.allocate(ERRORCODEFIELDSIZE).putShort((short) error).array());
            if (data != null) {
                byteStream.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toByteArray();
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("id:" + getId());
        string.append(",");
        string.append("length:" + getLength());
        string.append(",");
        string.append("flag:" + getFlag());
        string.append(",");
        string.append("error:" + getErrorCode());
        if (data != null) {
            string.append(",");
            string.append("data:" + new String(getData()));
        }
        return string.toString();
    }
}
