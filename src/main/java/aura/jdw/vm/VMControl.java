package aura.jdw.vm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ash on 27/12/2016.
 */
public class VMControl {


    public List<VMClass> getClassList() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        List<VMClass> classList = new ArrayList<>();
        int referenceId = 1;
        try {
            Field classField = ClassLoader.class.getDeclaredField("classes");
            classField.setAccessible(true);
            List<Class> classes = (List<Class>) classField.get(classLoader);

            for (int i = 0; i < classes.size(); i++) {
                classList.add(new VMClass(classes.get(i), referenceId++));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return classList;
    }

    public String[] getClassPaths() {
        String classpath = System.getProperty("java.class.path");
        return classpath.split(File.pathSeparator);
    }

    public String getBaseDir() {
        return ".";
    }

    public String[] getBootClassPaths() {
        String bootClassPath = System.getProperty("sun.boot.class");
        if (bootClassPath == null || bootClassPath.equals("")) {
            return null;
        }
        return bootClassPath.split(File.pathSeparator);
    }
}

