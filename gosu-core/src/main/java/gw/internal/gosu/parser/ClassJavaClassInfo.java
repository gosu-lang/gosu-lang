/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.JavaSourceUtil;
import gw.lang.GosuShop;
import gw.lang.javadoc.IClassDocNode;
import gw.lang.reflect.BeanInfoUtil;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.java.AbstractJavaClassInfo;
import gw.lang.reflect.java.IClassJavaClassInfo;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodDescriptor;
import gw.lang.reflect.java.IJavaPropertyDescriptor;
import gw.lang.reflect.module.IModule;
import gw.util.concurrent.LockingLazyVar;
import gw.util.concurrent.LocklessLazyVar;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class ClassJavaClassInfo extends TypeJavaClassType implements IClassJavaClassInfo {
  private Class<?> _class;
  private transient LockingLazyVar<GenericBeanInfo> _beanInfo = new LockingLazyVar<GenericBeanInfo>() {
    @Override
    protected GenericBeanInfo init() {
      return NewIntrospector.getBeanInfo(_class);
    }
  };
  private IJavaClassMethod[] _declaredMethods;
  private IJavaClassInfo[] _interfaces;
  private IJavaClassInfo _superclass;
  private IJavaClassTypeVariable[] _typeVariables;
  private IJavaClassField[] _declaredFields;
  private IJavaClassConstructor[] _declaredConstructors;
  private IAnnotationInfo[] _declaredAnnotations;
  private IJavaPropertyDescriptor[] _propertyDescriptors;
  private IJavaMethodDescriptor[] _methodDescriptors;
  private IJavaClassField[] _fields;
  private IJavaClassType[] _genericInterfaces;
  private IJavaClassInfo[] _declaredClasses;
  private ISourceFileHandle _fileHandle;
  private String _namespace;
  private LocklessLazyVar<IType> _enclosingClass = new LocklessLazyVar<IType>() {
    protected IType init() {
      Class enclosingClass = _class.getEnclosingClass();
      return enclosingClass == null ? null : TypeSystem.get(enclosingClass, _module);
    }
  };

  public ClassJavaClassInfo(Class cls, IModule module) {
    super(cls, module);
    if (cls == null) {
      throw new IllegalArgumentException("Class cannot be null.");
    }
    _class = cls;
    _module = module;
  }

  @Override
  public boolean isAnnotation() {
    return _class.isAnnotation();
  }

  @Override
  public boolean isInterface() {
    return _class.isInterface();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return this;
  }

  @Override
  public String getName() {
    return _class.getName();
  }

  public String getNameSignature() {
    if (_class.isArray()) {
      return _class.getName();
    } else {
      return GosuShop.toSignature(_class.getName());
    }
  }

  @Override
  public IJavaClassMethod getMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    Class[] javaParamTypes = new Class[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      IJavaClassInfo paramType = paramTypes[i];
      if (paramType == null) {
        javaParamTypes[i] = null;
      } else {
        javaParamTypes[i] = paramType.getBackingClass();
        if (javaParamTypes[i] == null) {
          throw new IllegalStateException("Class info for " + getName() + " is concrete, but class info for method parameter " + paramType.getName() + " is not (it's a " + paramType.getClass() + "), so can't get method by signature");
        }
      }
    }
    return new MethodJavaClassMethod(_class.getMethod(methodName, javaParamTypes), _module);
  }

  public IJavaClassMethod getDeclaredMethod(String methodName, IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    Class[] javaParamTypes = new Class[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      IJavaClassInfo paramType = paramTypes[i];
      Class backingClass = paramType.getBackingClass();
      javaParamTypes[i] = backingClass;
      if( backingClass == null ) {
        throw new IllegalStateException("Class info for " + getName() + " is concrete, but class info for method parameter " + paramType.getName() + " is not (it's a " + paramType.getClass() + "), so can't get method by signature");
      }
    }
    return new MethodJavaClassMethod(_class.getMethod(methodName, javaParamTypes), _module);
  }

  @Override
  public IJavaClassMethod[] getDeclaredMethods() {
    if (_declaredMethods == null) {
      Method[] rawMethods = NewIntrospector.getDeclaredMethods(_class);
      IJavaClassMethod[] methods = new IJavaClassMethod[rawMethods.length];
      for (int i = 0; i < rawMethods.length; i++) {
        methods[i] = new MethodJavaClassMethod(rawMethods[i], _module);
      }
      _declaredMethods = methods;
    }
    return _declaredMethods;
  }

  @Override
  public Object newInstance() throws InstantiationException, IllegalAccessException {
    return _class.newInstance();
  }

  @Override
  public Object[] getEnumConstants() {
    return _class.getEnumConstants();
  }

  @Override
  public IType getJavaType() {
    return TypeSystem.get(_class, _module);
  }

  @Override
  public IJavaClassInfo[] getInterfaces() {
    if (_interfaces == null) {
      Class[] rawInterfaces = _class.getInterfaces();
      IJavaClassInfo[] interfaces = new IJavaClassInfo[rawInterfaces.length];
      for (int i = 0; i < rawInterfaces.length; i++) {
        interfaces[i] = JavaSourceUtil.getClassInfo(rawInterfaces[i], _module);
      }
      _interfaces = interfaces;
    }
    return _interfaces;
  }

  @Override
  public IJavaClassInfo getSuperclass() {
    if (_superclass == null) {
      _superclass = _class.getSuperclass() == null ? NULL_TYPE : JavaSourceUtil.getClassInfo(_class.getSuperclass(), _module);
    }
    return _superclass == NULL_TYPE ? null : _superclass;
  }

  public IJavaClassTypeVariable[] getTypeParameters() {
    if (_typeVariables == null) {
      TypeVariable[] rawTypeVariables = _class.getTypeParameters();
      IJavaClassTypeVariable[] typeVariables = new IJavaClassTypeVariable[rawTypeVariables.length];
      for (int i = 0; i < rawTypeVariables.length; i++) {
        typeVariables[i] = new TypeVariableJavaTypeVariable(rawTypeVariables[i], _module);
      }
      _typeVariables = typeVariables;
    }
    return _typeVariables;
  }

  @Override
  public IJavaClassField[] getDeclaredFields() {
    if (_declaredFields == null) {
      Field[] rawFields = _class.getDeclaredFields();
      IJavaClassField[] fields = new IJavaClassField[rawFields.length];
      for (int i = 0; i < rawFields.length; i++) {
        fields[i] = new FieldJavaClassField(rawFields[i], _module);
      }
      _declaredFields = fields;
    }
    return _declaredFields;
  }

  @Override
  public IJavaClassConstructor[] getDeclaredConstructors() {
    if (_declaredConstructors == null) {
      Constructor<?>[] rawCtors = _class.getDeclaredConstructors();
      List<IJavaClassConstructor> ctors = new ArrayList<IJavaClassConstructor>(rawCtors.length);
      for (Constructor<?> rawCtor : rawCtors) {
        if (!rawCtor.isSynthetic()) {
          ctors.add(new ConstructorJavaClassConstructor(rawCtor, _module));
        }
      }
      _declaredConstructors = ctors.toArray(new IJavaClassConstructor[ctors.size()]);
    }
    return _declaredConstructors;
  }

  public IJavaClassConstructor getConstructor(IJavaClassInfo... paramTypes) throws NoSuchMethodException {
    Class[] javaParamTypes = new Class[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      IJavaClassInfo paramType = paramTypes[i];
      if (paramType instanceof ClassJavaClassInfo) {
        javaParamTypes[i] = ((ClassJavaClassInfo) paramType)._class;
      } else {
        throw new IllegalStateException("Class info for " + getName() + " is concrete, but class info for method parameter " + paramType.getName() + " is not (it's a " + paramType.getClass() + "), so can't get method by signature");
      }
    }
    return new ConstructorJavaClassConstructor(_class.getConstructor( javaParamTypes ), _module);
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return _class.isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    Annotation annotation = _class.getAnnotation(annotationClass);
    for (IAnnotationInfo annotationInfo : getDeclaredAnnotations()) {
      if (((ClassAnnotationInfo)annotationInfo).getInstance() == annotation) {
        return annotationInfo;
      }
    }
    return null;
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    if (_declaredAnnotations == null) {
      Annotation[] annotations = _class.getDeclaredAnnotations();
      IAnnotationInfo[] declaredAnnotations = new IAnnotationInfo[annotations.length];
      for (int i = 0; i < declaredAnnotations.length; i++) {
        declaredAnnotations[i] = new ClassAnnotationInfo(annotations[i], this);
      }
      _declaredAnnotations = declaredAnnotations;
    }
    return _declaredAnnotations;
  }

  @Override
  public IClassDocNode createClassDocNode() {
    return GosuShop.getJavaDocFactory().create(_class);
  }

  @Override
  public IJavaPropertyDescriptor[] getPropertyDescriptors() {
    if (_propertyDescriptors == null) {
      PropertyDescriptor[] rawPropDesc = _beanInfo.get().getPropertyDescriptors();
      IJavaPropertyDescriptor[] propDesc = new IJavaPropertyDescriptor[rawPropDesc.length];
      for (int i = 0; i < rawPropDesc.length; i++) {
        propDesc[i] = new PropertyDescriptorJavaPropertyDescriptor(rawPropDesc[i], _module);
      }
      _propertyDescriptors = propDesc;
    }
    return _propertyDescriptors;
  }

  @Override
  public IJavaMethodDescriptor[] getMethodDescriptors() {
    if (_methodDescriptors == null) {
      GWMethodDescriptor[] rawMDs = _beanInfo.get().getGWMethodDescriptors();
      IJavaMethodDescriptor[] mds = new IJavaMethodDescriptor[rawMDs.length];
      for (int i = 0; i < rawMDs.length; i++) {
        mds[i] = new MethodDescriptorJavaMethodDescriptor(rawMDs[i], _module);
      }
      _methodDescriptors = mds;
    }
    return _methodDescriptors;
  }

  @Override
  public boolean hasCustomBeanInfo() {
    return !(_beanInfo.get() instanceof GenericBeanInfo);
  }

  @Override
  public String getRelativeName() {
    BeanDescriptor bd = _beanInfo.get().getBeanDescriptor();
    return bd != null ? bd.getName() : getJavaType().getRelativeName();
  }

  @Override
  public String getDisplayName() {
    BeanDescriptor bd = _beanInfo.get().getBeanDescriptor();
    return bd != null ? bd.getDisplayName() : getJavaType().getRelativeName();
  }

  @Override
  public String getSimpleName() {
    return _class.getSimpleName();
  }

  @Override
  public boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint) {
    return _beanInfo.get().getBeanDescriptor() == null || BeanInfoUtil.isVisible(_beanInfo.get().getBeanDescriptor(), constraint);
  }

  @Override
  public boolean isHiddenViaFeatureDescriptor() {
    return _beanInfo.get().getBeanDescriptor() != null && _beanInfo.get().getBeanDescriptor().isHidden();
  }

  @Override
  public IJavaClassField[] getFields() {
    if (_fields == null) {
      Field[] rawFields = _class.getFields();
      IJavaClassField[] fields = new IJavaClassField[rawFields.length];
      for (int i = 0; i < rawFields.length; i++) {
        fields[i] = new FieldJavaClassField(rawFields[i], _module);
      }
      _fields = fields;
    }
    return _fields;
  }

  public Class getJavaClass() {
    return _class;
  }

  @Override
  public IJavaClassInfo getComponentType() {
    return JavaSourceUtil.getClassInfo(_class.getComponentType(), _module);
  }

  @Override
  public boolean isArray() {
    return _class.isArray();
  }

  @Override
  public boolean isEnum() {
    return _class.isEnum();
  }

  @Override
  public int getModifiers() {
    return _class.getModifiers();
  }

  @Override
  public boolean isPrimitive() {
    return _class.isPrimitive();
  }

  @Override
  public IJavaClassInfo getEnclosingClass() {
    Class enclosingClass = _class.getEnclosingClass();
    if (enclosingClass != null) {
      return TypeSystem.getJavaClassInfo(enclosingClass, _module);
    }
    return null;
  }

  @Override
  public IType getEnclosingType() {
    return _enclosingClass.get();
  }

  @Override
  public String getNamespace() {
    if( _namespace == null ) {
      Class cls = _class;
      Package aPackage = cls.getPackage();
      while (aPackage == null && cls.isArray()) {
        cls = cls.getComponentType();
        aPackage = cls.getPackage();
      }
      _namespace = aPackage == null ? null : aPackage.getName();
    }
    return _namespace;
  }

  @Override
  public IJavaClassType[] getGenericInterfaces() {
    if (_genericInterfaces == null) {
      Type[] rawIfaces = _class.getGenericInterfaces();
      IJavaClassType[] ifaces = new IJavaClassType[rawIfaces.length];
      for (int i = 0; i < rawIfaces.length; i++) {
        ifaces[i] = TypeJavaClassType.createType(rawIfaces[i], _module);
      }
      _genericInterfaces = ifaces;
    }
    return _genericInterfaces;
  }

  @Override
  public IJavaClassType getGenericSuperclass() {
    return TypeJavaClassType.createType(_class.getGenericSuperclass(), _module);
  }

  @Override
  public IJavaClassInfo getArrayType() {
    DefaultTypeLoader defaultTypeLoader = (DefaultTypeLoader)_module.getModuleTypeLoader().getDefaultTypeLoader();
    return defaultTypeLoader.getJavaClassInfo( Array.newInstance(_class, 0).getClass(), _module );
  }

  @Override
  public IJavaClassInfo[] getDeclaredClasses() {
    if (_declaredClasses == null) {
      Class[] rawClasses = _class.getDeclaredClasses();
      ArrayList<IJavaClassInfo> declaredClasses = new ArrayList<IJavaClassInfo>(rawClasses.length);
      for (int i = 0; i < rawClasses.length; i++) {
        if (!rawClasses[i].isAnonymousClass()) {
          DefaultTypeLoader defaultTypeLoader = (DefaultTypeLoader)_module.getModuleTypeLoader().getDefaultTypeLoader();
          IJavaClassInfo declaredClassInfo = defaultTypeLoader.getJavaClassInfo( rawClasses[i], _module );
          declaredClasses.add( declaredClassInfo );
        }
      }
      _declaredClasses = declaredClasses.toArray(new IJavaClassInfo[declaredClasses.size()]);
    }
    return _declaredClasses;
  }

  @Override
  public boolean isAssignableFrom(IJavaClassInfo aClass) {
    return AbstractJavaClassInfo.isAssignableFrom(this, aClass);
  }

  @Override
  public boolean isPublic() {
    return Modifier.isPublic(_class.getModifiers());
  }

  @Override
  public boolean isProtected() {
    return Modifier.isProtected(_class.getModifiers());
  }

  @Override
  public boolean isInternal() {
    return !isPublic() && !isProtected() && !isPrivate();
  }

  @Override
  public boolean isPrivate() {
    return Modifier.isPrivate(_class.getModifiers());
  }

  @Override
  public boolean equals(Object obj) {
    return AbstractJavaClassInfo.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return AbstractJavaClassInfo.hashCode(this);
  }

  public String toString() {
    return _class.toString();
  }

  @Override
  public Class getBackingClass() {
    return _class;
  }

  @Override
  public ISourceFileHandle getSourceFileHandle() {
    return _fileHandle;
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  public boolean isTypeGosuClassInstance() {
    return IGosuObject.class.isAssignableFrom(_class) &&
        TypeSystem.getByFullNameIfValid(_class.getName().replace('$', '.')) instanceof IGosuClass;
  }

  @Override
  public IJavaClassType resolveType(String relativeName, int ignoreFlags) {
    return null;
  }

  @Override
  public IJavaClassType resolveType(String relativeName, IJavaClassInfo whosAskin, int ignoreFlags) {
    Class backingClass = getBackingClass();
    for (Class innerClass : backingClass.getDeclaredClasses()) {
      if (innerClass.getName().equals(getName() + "$" + relativeName)) {
        return JavaSourceUtil.getClassInfo(innerClass, getJavaType().getTypeLoader().getModule());
      }
    }
    return null;
  }

  @Override
  public IJavaClassType resolveImport(String relativeName) {
    return null;
  }

  public void setSourceFileHandle(ISourceFileHandle fileHandle) {
    _fileHandle = fileHandle;
  }
}
