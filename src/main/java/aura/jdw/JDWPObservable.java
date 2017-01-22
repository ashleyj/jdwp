package aura.jdw;

import aura.jdw.vm.VMClass;
import aura.jdw.vm.VMControl;
import rx.Observable;

import java.io.IOException;
import java.util.List;

import static aura.jdw.JDWPConstants.*;
import static aura.jdw.Utils.*;

/**
 * Created by ash on 21/12/2016.
 */
public class JDWPObservable {


    public static Observable<ReplyPacket> create(CommandPacket commandPacket) {

        return Observable.create(subscriber -> {

            int error = 0;
            byte[] data = null;
            switch (commandPacket.getCommandSet()) {
                case VM_COMMANDSET:
                    data = generateVMReply(commandPacket.getCommand());
                    break;
                case EVENT_REQUEST_COMMANDSET:
                    data = generateEventReply(commandPacket.getCommand(), commandPacket.getData()
                                                                        , commandPacket.getId());
                    break;
            }

            ReplyPacket replyPacket = new ReplyPacket(
                    commandPacket.getId(),
                    commandPacket.getFlag(),
                    error,
                    data);

            System.out.println("Command: " + commandPacket.toString());
            subscriber.onNext(replyPacket);
            System.out.println("Reply: " + replyPacket.toString());
            //subscriber.onError(new JDWPProtocolException("Invalid Command"));
            //subscriber.onCompleted();
        });
    }

    private static byte[] generateEventReply(int command, byte[] data, int id) {
        byte[] replyData = null;
        switch (command) {
            case EVENT_REQUEST_COMMAND_SET:
                System.out.println(data);
                try {
                    replyData = processEventRequest(command, data, id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        return replyData;
    }

    public static byte[] processEventRequest(int command, byte[] data, int id) throws Exception {
        byte eventType = getByte(0, data);
        byte suspendPolicy = getByte(1, data);
        int modifier = getInt(2, data);
        //Modifier modifier = new Modifier(getInt(2, data));
        switch (eventType) {

            case CLASS_PREPARE:
                //if (suspendPolicy == 0 )
                data = toBytes(id);
                break;

            case CLASS_UNLOAD:
                data = toBytes(id);
                break;

            case THREAD_START:
                data = toBytes(id);
                break;

            case THREAD_END:
                data = toBytes(id);
                break;
            default:
                throw new Exception("Not yet implemented - " + eventType);
        }
        return data;
    }


    public static byte[] generateVMReply(int command) {
        byte[] data = null;
        switch (command) {
            case VM_COMMAND_ID_SIZES:
                data = squash(toBytes(FIELDISIZE), toBytes(METHODIDSIZE),
                        toBytes(OBJECTIDSIZE), toBytes(REFERENCETYPEIDSIZE), toBytes(FRAMEIDSIZE));
                break;
            case VM_COMMAND_VERSION:
                data = getVMVersionInfo();
                break;

            case VM_COMMAND_ALL_CLASSES_WITH_GENERICS:
                data = getVMGenericClasses();
                break;

            case VM_COMMAND_CLASSPATHS:
                data = getVMClassPath();
                break;
        }
        return data;
    }

    public static byte[] getVMClassPath() {
        VMControl vmControl = new VMControl();
        String[] classPathList = vmControl.getClassPaths();
        try {
            return squash(toBytes(vmControl.getBaseDir()), toBytes(classPathList),
                    toBytes(vmControl.getBootClassPaths()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static byte[] getVMVersionInfo() {
        return squash(toBytes(VM_DESCRIPTION),toBytes(JDWP_MAJOR), toBytes(JDWP_MINOR),
                                toBytes(TARGET_VM_VERSION_NAME), toBytes(TARGET_VM_NAME));
    }


    public static byte[] getVMGenericClasses() {
        VMControl vmControl = new VMControl();
        List<VMClass> classes = null;
        try {
            classes = vmControl.getClassList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Utils.toBytes(classes);
    }
}
