package aura.jdw;

import aura.jdw.CommandPacket;
import aura.jdw.ReplyPacket;
import org.junit.Test;

/**
 * Created by ash on 19/12/2016.
 */
public class CommandPacketTest {


    @Test(expected = IllegalArgumentException.class)
    public void createrHeaderFromInvalidArray() {
        byte[] invalidByteArray = new byte[]{0, 0, 11};
        CommandPacket.createFromHeader(invalidByteArray);
    }
    @Test
    public void createHeaderFromValidArray() {
        byte[] validHeader = new byte[] {0,0,0,11,0,0,0,1,0,1,7};
        CommandPacket commandPacket = CommandPacket.createFromHeader(validHeader);
        commandPacket.getDataSize();
    }
}
