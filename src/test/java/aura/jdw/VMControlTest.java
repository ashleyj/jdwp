package aura.jdw;

import aura.jdw.vm.VMClass;
import aura.jdw.vm.VMControl;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by ash on 27/12/2016.
 */
public class VMControlTest {


    @Test
    public void getClassList () throws IOException {
        VMControl vmControl = new VMControl();
        List<VMClass> classList = vmControl.getClassList();
        assertTrue(classList.size() > 20);
    }

    @Test
    public void getBootClassPaths() {
        /* this test isn't to test whether or not the VM sets this property, so we'll set it to
         * ensure consistent test results
         */
        System.setProperty("sun.boot.class", "/tmp");
        VMControl vmControl = new VMControl();
        assertTrue(Arrays.equals(vmControl.getBootClassPaths(), new String[] {"/tmp"}));
    }

    @Test
    public void getClassPaths() {
        String path1 = "/tmp/test";
        String path2 = "/tmp/test2";

        /* set class path */
        System.setProperty("java.class.path",path1 + ":" + path2);
        VMControl vmControl = new VMControl();
        String[] classPaths = vmControl.getClassPaths();
        assertTrue(classPaths.length == 2);
    }



}
