package aura.jdw;

import org.junit.Test;
import sun.util.locale.BaseLocale;
import sun.util.locale.LocaleObjectCache;

import java.nio.CharBuffer;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

/**
 * Created by ash on 30/12/2016.
 */
public class VMClassTest {

    @Test
    public void getSignatureFromClassName() {
        Class clazz = sun.misc.URLClassPath.class;
        VMClass vmClass = new VMClass(clazz,1);
        assertTrue("Lsun/misc/URLClassPath;".equals(vmClass.getSignatureFromName(clazz.getName())));
    }
    @Test
    public void getGenericSignatureFromClassName() throws NoSuchFieldException, IllegalAccessException {
        Class clazz = java.util.HashSet.class;
        VMClass vmClass = new VMClass(clazz,1);
        assertTrue(vmClass.getGenericSignature(), vmClass.getGenericSignature()
                .equals("<E:Ljava/lang/Object;>Ljava/util/AbstractSet<TE;>;Ljava/util/Set<TE;>;Ljava/lang/Cloneable;Ljava/io/Serializable;"));

        clazz = LocaleObjectCache.class;
        vmClass = new VMClass(clazz, 1);
        assertTrue(vmClass.getGenericSignature(), vmClass.getGenericSignature()
                .equals("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;"));

        clazz = CharBuffer.class;
        vmClass = new VMClass(clazz, 1);
        assertTrue(vmClass.getGenericSignature(),
                vmClass.getGenericSignature()
                .equals("Ljava/nio/Buffer;Ljava/lang/Comparable<Ljava/nio/CharBuffer;>;Ljava/lang/Appendable;Ljava/lang/CharSequence;Ljava/lang/Readable;"));

        clazz = AbstractMap.class;
        vmClass = new VMClass(clazz, 1);
        assertTrue(vmClass.getGenericSignature(), vmClass.getGenericSignature()
                .equals("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/lang/Object;Ljava/util/Map<TK;TV;>;"));

        clazz = ConcurrentHashMap.class;
        vmClass = new VMClass(clazz, 1);
        assertTrue(vmClass.getGenericSignature(), vmClass.getGenericSignature()
                .equals("<K:Ljava/lang/Object;V:Ljava/lang/Object;>Ljava/util/AbstractMap<TK;TV;>;Ljava/util/concurrent/ConcurrentMap<TK;TV;>;Ljava/io/Serializable;"));

    }

}