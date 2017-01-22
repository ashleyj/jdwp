package aura.jdw;

import aura.jdw.ReplyPacket;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Created by ash on 19/12/2016.
 */
public class ReplyPacketTest {


    @Test(expected = IllegalArgumentException.class)
    public void createrHeaderFromInvalidArray() {
        byte[] invalidByteArray = new byte[]{0, 0, 11};
        ReplyPacket.createFromHeader(invalidByteArray);
    }

    @Test
    public void createHeaderFromValidArray() {
        byte[] validHeader = new byte[] {0,0,0,11,0,0,0,1,0,1,7};
        ReplyPacket.createFromHeader(validHeader);
    }

    @Test
    public void getBytes() {
        byte[] basicHeader = new byte[] {0,0,0,11,0,0,0,1,0,0,0};
        byte[] headerWithData = new byte[] {0,0,0,15,0,0,0,1,0,0,0};
        byte[] data = new byte[] {2,4,5,1};
        ReplyPacket replyPacket = ReplyPacket.createFromHeader(basicHeader);
        assertTrue(Arrays.equals(replyPacket.getBytes(), basicHeader));

        replyPacket.setData(data);
        byte[] fullPacket = new byte[basicHeader.length + data.length];
        ByteBuffer.wrap(fullPacket).put(headerWithData).put(data);
        assertTrue(Arrays.equals(replyPacket.getBytes(), fullPacket));
    }
}
