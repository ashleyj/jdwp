package aura.jdw.vm;

import aura.jdw.JDWPConstants;
import aura.jdw.Utils;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
import sun.reflect.generics.repository.AbstractRepository;
import sun.reflect.generics.repository.ClassRepository;
import sun.reflect.generics.tree.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Created by ash on 27/12/2016.
 */
public class VMClass {

    byte refType;
    long referenceId;
    String signature;
    String genericSignature;
    int status;
    Class vmClass;


    public VMClass(Class clazz, int referenceId) {
        this.vmClass = clazz;
        this.referenceId = referenceId;
        signature = getSignatureFromName(vmClass.getName());
        if (vmClass.isInterface()) {
            refType = JDWPConstants.REFERENCE_TYPE_INTERFACE;
        } else if (vmClass.isArray()) {
            refType = JDWPConstants.REFERENCE_TYPE_ARRAY;
        } else {
            refType = JDWPConstants.REFERENCE_TYPE_CLASS;
        }

        status = JDWPConstants.VERIFIED + JDWPConstants.PREPARED +   JDWPConstants.INITIALIZED;
    }


    public String getSignatureFromName(String className) {
        return "L" + className.replace(".","/") + ";";
    }

    public String getGenericSignature() throws NoSuchFieldException, IllegalAccessException {
        if (genericSignature != null) {
            return genericSignature;
        }
        Type[] genericInterfaces = vmClass.getGenericInterfaces();
        StringBuilder signature = new StringBuilder();

        ClassRepository repository = (ClassRepository) getPrivateField("genericInfo", vmClass, Class.class);
        ClassSignature tree = (ClassSignature) getPrivateField("tree", repository, AbstractRepository.class);

        FormalTypeParameter[] params = tree.getFormalTypeParameters();
        if (params.length > 0) {
            signature.append("<");
            for (FormalTypeParameter param : params) {
                FieldTypeSignature[] fieldTypeSignature = param.getBounds();
                for (FieldTypeSignature typeSignature : fieldTypeSignature) {
                    signature.append(param.getName());
                    signature.append(":");
                    if (typeSignature instanceof ClassTypeSignature) {
                        for (SimpleClassTypeSignature simpleClassTypeSignature : ((ClassTypeSignature) typeSignature).getPath()) {
                            signature.append(getSignatureFromName(simpleClassTypeSignature.getName()));
                        }
                    }
                }
            }
            signature.append(">");
        }

        ClassTypeSignature sClass = tree.getSuperclass();
        for (SimpleClassTypeSignature simpleClassTypeSignature : sClass.getPath()) {
            signature.append(getSignatureFromName(simpleClassTypeSignature.getName()).replace(";",""));
            if (simpleClassTypeSignature.getTypeArguments().length > 0 ) {
                signature.append("<");
                for (TypeArgument typeArgument : simpleClassTypeSignature.getTypeArguments()) {
                    String identifier = (String) getPrivateField("identifier", typeArgument, TypeVariableSignature.class);
                    signature.append("T");
                    signature.append(identifier);
                    signature.append(";");
                }
                signature.append(">");
            }
            signature.append(";");
        }

        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof  ParameterizedTypeImpl) {
                String className = ((ParameterizedTypeImpl) genericInterface).getRawType().getName();
                signature.append(getSignatureFromName(className).replace(";",""));
                if (((ParameterizedTypeImpl) genericInterface).getActualTypeArguments().length > 0) {
                    signature.append("<");
                    for (Type type : ((ParameterizedTypeImpl) genericInterface).getActualTypeArguments()) {
                        if (type instanceof TypeVariableImpl) {
                            signature.append("T");
                            signature.append(type.getTypeName());
                            signature.append(";");
                        } else {
                            signature.append(getSignatureFromName(type.getTypeName()));
                        }
                    }
                    signature.append(">");
                    signature.append(";");
                }
            } else {
                signature.append(getSignatureFromName(genericInterface.getTypeName()));
            }
        }
        return signature.toString();
    }

    private Object getPrivateField(String fieldName, Object object, Class clazz) throws NoSuchFieldException {
        Field privateField =  clazz.getDeclaredField(fieldName);
        privateField.setAccessible(true);
        try {
            Object privateFieldValue = privateField.get(object);
            return privateField.get(object);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            //e.printStackTrace();
        }
        return null;
    }


    public byte[] toBytes() {
        try {
            return Utils.squash(new byte[]{refType},
                    Utils.toBytes(referenceId),
                    Utils.toBytes(signature),
                    Utils.toBytes(getGenericSignature()),
                    Utils.toBytes(status));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
