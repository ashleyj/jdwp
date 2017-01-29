package aura.jdw.protocol;

/**
 * Created by ash on 21/12/2016.
 */
public class JDWPConstants {
    /* Command Sets */
    public final static int VM_COMMANDSET = 1;
    public final static int EVENT_REQUEST_COMMANDSET = 15;

    /* Commands */
    public final static int EVENT_REQUEST_COMMAND_SET = 1;
    public final static int VM_COMMAND_ID_SIZES = 7;
    public final static int VM_COMMAND_VERSION = 1;
    public final static int VM_COMMAND_CLASSPATHS = 13;
    public final static int VM_COMMAND_ALL_CLASSES_WITH_GENERICS = 20;

    /* ID Sizes */
    public final static int FIELDISIZE = 8;
    public final static int METHODIDSIZE = 8;
    public final static int OBJECTIDSIZE = 8;
    public final static int REFERENCETYPEIDSIZE = 8;
    public final static int FRAMEIDSIZE = 8;

    /* Event Kinds */
    public final static int VM_DISCONNECTED = 100;
    public final static int SINGLE_STEP = 1;
    public final static int BREAKPOINT = 2;
    public final static int FRAME_POP = 3;
    public final static int EXCEPTION = 4;
    public final static int USER_DEFINED = 5;
    public final static int THREAD_START = 6;
    public final static int THREAD_END = 7;
    public final static int CLASS_PREPARE = 8;
    public final static int CLASS_UNLOAD = 9;
    public final static int CLASS_LOAD = 10;
    public final static int FIELD_ACCESS = 20;
    public final static int FIELD_MODIFICATION = 21;
    public final static int EXCEPTION_CATCH = 30;
    public final static int METHOD_ENTRY = 40;
    public final static int METHOD_EXIT = 41;
    public final static int VM_INIT = 90;
    public final static int VM_DEATH = 99;

    /* Suspend Policy */
    public final static int SUSPEND_NONE = 0;
    public final static int SUSPEND_EVENT_THREAD = 1;
    public final static int SUSPEND_ALL = 2;

    /* VM Information */
    public final static String VM_DESCRIPTION = "JDWP VM";
    public final static int JDWP_MAJOR = 1;
    public final static int JDWP_MINOR = 7;
    public final static String TARGET_VM_NAME = System.getProperty("java.vm.name");
    public final static String TARGET_VM_VERSION_NAME = System.getProperty("java.runtime.version");

    /* Class Reference kinds */
    public final static int REFERENCE_TYPE_CLASS = 1;
    public final static int REFERENCE_TYPE_INTERFACE = 2;
    public final static int REFERENCE_TYPE_ARRAY = 3;

    /* Class load status */
    public final static int VERIFIED = 1;
    public final static int PREPARED = 2;
    public final static int INITIALIZED = 4;
    public final static int ERROR = 8;
}
