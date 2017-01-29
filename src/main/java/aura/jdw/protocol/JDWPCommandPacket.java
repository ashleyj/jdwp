package aura.jdw.protocol;


import static aura.jdw.Utils.*;

/**
 * Created by ash on 19/12/2016.
 */
public class JDWPCommandPacket extends JDWPPacket {

    public static int COMMANDSETSIZE = 1;
    public static int COMMANDSIZE = 1;

    private int commandSet;
    private int command;

    public JDWPCommandPacket(int length, int id, int flag, int commandSet, int command, byte[] data) {
       this.length = length;
       this.id = id;
       this.flag = flag;
       this.commandSet = commandSet;
       this.command = command;
       this.data = data;
    }

    public static JDWPCommandPacket createFromHeader(byte[] inbytes) {
        if (inbytes.length != HEADERSIZE) {
            throw new IllegalArgumentException("Header has invalid length");
        }

        int position = 0;
        int length = getInt(position, inbytes);
        int id = getInt(position+=LENGTHFIELDSIZE, inbytes);
        int flag = getByte(position+=IDFIELDSIZE, inbytes);
        int commandSet = getByte(position+=FLAGFIELDSIZE, inbytes);
        int command = getByte(position+=COMMANDSETSIZE, inbytes);

        return new JDWPCommandPacket(length, id, flag, commandSet, command, null);
    }

    public int getCommandSet() {
        return commandSet;
    }

    public void setCommandSet(int commandSet) {
        this.commandSet = commandSet;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getDataSize() {
        return length - HEADERSIZE;
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
        string.append("commandSet:" + getCommandSet());
        string.append(",");
        string.append("command:" + getCommand());
        if (data != null) {
            string.append(",");
            string.append("data:" + new String(getData()));
        }
        return string.toString();
    }

}
