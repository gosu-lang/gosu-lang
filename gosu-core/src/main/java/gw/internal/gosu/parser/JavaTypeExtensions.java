package gw.internal.gosu.parser;

/*
 * Copyright 2014 Guidewire Software, Inc.
 */

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Type;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IInjectableClassLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static gw.internal.ext.org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static gw.internal.ext.org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static gw.internal.ext.org.objectweb.asm.Opcodes.*;
import static java.lang.String.format;
import static java.util.Arrays.asList;

class JavaTypeExtensions {
  private JavaTypeExtensions() {
  }

  public static IJavaPropertyInfo maybeExtendProperty(JavaPropertyInfo javaProperty) {
    IJavaPropertyInfo result = javaProperty;
    IJavaClassMethod readMethod = javaProperty.getPropertyDescriptor().getReadMethod();
    if (readMethod != null) {
      IAnnotationInfo extendedPropertyAnnotation = readMethod.getAnnotation(ExtendedProperty.class);
      if (extendedPropertyAnnotation != null) {
        ExtendedTypeDataFactory factory = getExtendedTypeDataFactory((IJavaType) javaProperty.getOwnersType());
        if (factory != null) {
          ExtendedTypeData extendedTypeInfoData = factory.newPropertyData((IJavaType) javaProperty.getOwnersType(), javaProperty.getName());
          result = newExtendedProperty(extendedTypeInfoData.getExtensionInterface(), javaProperty, extendedTypeInfoData.getData());
        }
      }
    }
    return result;
  }

  public static IJavaTypeInternal maybeExtendType(JavaType javaType) {
    IJavaTypeInternal result = javaType;
    ExtendedTypeDataFactory factory = getExtendedTypeDataFactory(javaType);
    if (factory != null) {
      ExtendedTypeData extendedTypeInfoData = factory.newTypeData(javaType.getName());
      result = newExtendedType(extendedTypeInfoData.getExtensionInterface(), javaType, extendedTypeInfoData.getData());
    }
    return result;
  }

  private static ExtendedTypeDataFactory getExtendedTypeDataFactory(IJavaType javaType) {
    boolean extendedType;
    if( ExecutionMode.isRuntime() ) {
      Class<?> backingClass = javaType.getBackingClass();
      // Server runtime case. We can't go through the IJavaClassInfo for this case, because it leads to a
      // circularity w.r.t. the JavaType (ClassAnnotationInfo attempts to get the JavaType)
      extendedType = backingClass.isAnnotationPresent(ExtendedType.class);
    } else {
      // Studio case
      extendedType = javaType.getBackingClassInfo().getAnnotation(ExtendedType.class) != null;
    }
    return extendedType ? CommonServices.getEntityAccess().getExtendedTypeDataFactory(javaType.getName()) : null;
  }

  static IJavaTypeInternal newExtendedType(Class<?> secondaryInterface, IJavaTypeInternal originalType, Object secondaryObject) {
    return newCompositeInstance(
            AbstractExtendedType.class,
            IJavaTypeInternal.class,
            secondaryInterface,
            originalType,
            secondaryObject
    );
  }

  static IJavaPropertyInfo newExtendedProperty(Class<?> secondaryInterface, IJavaPropertyInfo originalProperty, Object secondaryObject) {
    return newCompositeInstance(
            AbstractExtendedProperty.class,
            IJavaPropertyInfo.class,
            secondaryInterface,
            originalProperty,
            secondaryObject
    );
  }

  private static <T> T newCompositeInstance(Class<?> superClass, Class<T> primaryInterface, Class<?> secondaryInterface, T primaryObject, Object secondaryObject) {
    Class<? extends T> compositeClass = getCompositeClass(superClass, primaryInterface, secondaryInterface, secondaryObject.getClass());
    try {
      return primaryInterface.cast(compositeClass.getConstructors()[0].newInstance(primaryObject, secondaryObject));
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(format("Couldn't create an instance of %s with arguments (%s, %s)", compositeClass, primaryObject, secondaryObject), e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(format("Couldn't create an instance of %s with arguments (%s, %s)", compositeClass, primaryObject, secondaryObject), e.getTargetException());
    }
  }

  private static <T> Class<? extends T> getCompositeClass(Class<?> superClass, Class<T> primaryInterface, Class<?> secondaryInterface, Class<?> secondaryObjectClass) {
    try {
      return loadCompositeClass(primaryInterface, secondaryObjectClass);
    } catch (ClassNotFoundException notFound) {
      // Not found, we probably have to define it
      TypeSystem.lock();
      try {
        // Now that we have the lock, check again to make sure somebody didn't beat us to it
        try {
          return loadCompositeClass(primaryInterface, secondaryObjectClass);
        } catch (ClassNotFoundException stillNotFound) {
          // Still not there, we have to define it
          defineCompositeClass(superClass, primaryInterface, secondaryInterface, secondaryObjectClass);
          try {
            return loadCompositeClass(primaryInterface, secondaryObjectClass);
          } catch (ClassNotFoundException uhOh) {
            // unexpected
            throw new IllegalStateException(
                    format("Could not load extended type proxy class '%s' with primary interface '%s' and secondary interface '%s'",
                            getCompositeClassName(secondaryObjectClass), primaryInterface.getName(), secondaryInterface.getName()));
          }
        }
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  private static <T> Class<? extends T> loadCompositeClass(Class<T> primaryInterface, Class<?> secondaryObjectClass) throws ClassNotFoundException {
    return Class.forName(getCompositeClassName(secondaryObjectClass), true, secondaryObjectClass.getClassLoader()).asSubclass(primaryInterface);
  }

  private static String getCompositeClassName(Class<?> secondaryObjectClass) {
    return secondaryObjectClass.getName() + "_ExtendedJavaTypeProxy";
  }

  private static void defineCompositeClass(
          Class<?> superClass,
          Class<?> primaryInterface,
          Class<?> secondaryInterface,
          Class<?> secondaryObjectClass) {
    // NOTE pdalbora 19-May-2016 -- The ASM code to generate this bytecode was developed first by writing example Java
    // code with the same structure that I want to create here. I compiled that, and then read in the compiled bytecode
    // using ClassReader and TraceClassVisitor to output the bytecode in a human-readable format. I then wrote the ASM
    // code to exactly reproduce the bytecode produced by the compiler, using a diff of the human-readable files to see
    // what I got wrong. Eventually, I produced the exact same bytecode, at which point I had chunks of ASM ClassWriter
    // code that I could port over to this class.
    String className = getCompositeClassName(secondaryObjectClass);
    String classInternalName = className.replace('.', '/');
    ClassWriter classWriter = new ClassWriter(COMPUTE_MAXS | COMPUTE_FRAMES);
    classWriter.visit(
            V1_8,
            ACC_PUBLIC | ACC_FINAL | ACC_SUPER,
            classInternalName,
            null, // signature (only for generics)
            Type.getInternalName(superClass),
            new String[] {Type.getInternalName(primaryInterface), Type.getInternalName(secondaryInterface)} // interfaces
    );
    classWriter.visitField(
            ACC_PRIVATE | ACC_FINAL,
            "_secondary",
            Type.getDescriptor(secondaryObjectClass),
            null, // signature (only for generics)
            null // no initializer
    );
    classWriter.visitEnd();

    // Constructor
    MethodVisitor constructor = classWriter.visitMethod(
            ACC_PUBLIC,
            "<init>",
            Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(primaryInterface), Type.getType(secondaryObjectClass)),
            null, // signature (only for generics)
            null // no exceptions
    );
    constructor.visitCode();
    constructor.visitVarInsn(ALOAD, 0);
    constructor.visitVarInsn(ALOAD, 1);
    constructor.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(superClass), "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(primaryInterface)), false);
    constructor.visitVarInsn(ALOAD, 0);
    constructor.visitVarInsn(ALOAD, 2);
    constructor.visitFieldInsn(PUTFIELD, classInternalName, "_secondary", Type.getDescriptor(secondaryObjectClass));
    constructor.visitInsn(RETURN);
    constructor.visitMaxs(0, 0);
    constructor.visitEnd();

    // Keep track of signatures we've seen, so that we avoid adding duplicates when we add the methods for the
    // secondary interface.
    Set<String> signatures = new HashSet<>();

    // Implement primary interface via delegation
    for (Method method : primaryInterface.getMethods()) {
      implementMethodViaDelegation(classWriter, Type.getInternalName(superClass), method, "_primary", primaryInterface, signatures);
    }

    // Implement secondary interface via delegation
    for (Method method : secondaryInterface.getMethods()) {
      implementMethodViaDelegation(classWriter, classInternalName, method, "_secondary", secondaryObjectClass, signatures);
    }

    // Now define the class in the classloader
    byte[] bytes = classWriter.toByteArray();

    // Copied from TypeRefFactory#generateProxyClass
    ClassLoader classLoader = secondaryInterface.getClassLoader();
    if(classLoader instanceof IInjectableClassLoader) {
      ((IInjectableClassLoader) classLoader).defineClass(className, bytes);
    } else {
      try {
        Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(classLoader, className, bytes, 0, bytes.length);
      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static void implementMethodViaDelegation(ClassWriter classWriter, String fieldOwnerInternalName, Method method, String fieldName, Class<?> fieldType, Set<String> signatures) {
    if (!signatures.add(method.getName() + Type.getMethodDescriptor(method))) {
      return;
    }
    MethodVisitor methodBuilder = classWriter.visitMethod(ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, toInternalNames(method.getExceptionTypes()));
    methodBuilder.visitCode();
    // access the field
    methodBuilder.visitVarInsn(ALOAD, 0);
    methodBuilder.visitFieldInsn(GETFIELD, fieldOwnerInternalName, fieldName, Type.getDescriptor(fieldType));
    // push the method params on the stack
    Class<?>[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      methodBuilder.visitVarInsn(Type.getType(parameterTypes[i]).getOpcode(ILOAD), i+1);
    }
    // Invoke the method
    methodBuilder.visitMethodInsn(fieldType.isInterface() ? INVOKEINTERFACE : INVOKEVIRTUAL, Type.getInternalName(fieldType), method.getName(), Type.getMethodDescriptor(method), fieldType.isInterface());
    // either "void" return or value return
    if (void.class.equals(method.getReturnType())) {
      methodBuilder.visitInsn(RETURN);
    } else {
      methodBuilder.visitInsn(Type.getType(method.getReturnType()).getOpcode(IRETURN));
    }
    methodBuilder.visitMaxs(0, 0);
    methodBuilder.visitEnd();
  }

  private static String[] toInternalNames(Class<?>[] classes) {
    return asList(classes).stream().map(Type::getInternalName).toArray(String[]::new);
  }

}
