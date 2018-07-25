/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRMethodFromMethodInfo;
import gw.internal.gosu.parser.GenericTypeVariable;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.ir.IRType;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.SimpleParameterInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.java.ClassInfoUtil;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.ITypeInfoResolver;
import gw.lang.reflect.java.JavaSourceElement;
import gw.lang.reflect.java.Parameter;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;
import manifold.ext.ExtensionMethod;
import manifold.util.ReflectUtil;

import javax.lang.model.element.Name;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gw.internal.gosu.parser.java.classinfo.JavaSourceType.getTypeName;

public class JavaSourceMethod extends JavaSourceElement implements IJavaClassMethod, ITypeInfoResolver
{
  protected MethodTree _method;
  protected JavaSourceType _containingClass;
  protected JavaSourceParameter[] _parameters;
  protected IJavaClassType _genericReturnType;
  protected JavaSourceModifierList _modifierList;
  protected IJavaClassTypeVariable[] _typeParameters;
  protected IJavaClassType[] _genericParameterTypes;
  protected IJavaClassInfo[] _parameterTypes;
  protected IJavaClassInfo _returnType;
  private Method _javaMethod;

  public JavaSourceMethod(MethodTree method, JavaSourceType containingClass) {
    _containingClass = containingClass;
    _method = method;
  }

  @Override
  public IJavaClassInfo getReturnClassInfo() {
    if (_returnType == null) {
      final Tree returnType = _method.getReturnType();
      _returnType = (IJavaClassInfo) JavaSourceType.createType(this, getTypeName(returnType), JavaSourceType.IGNORE_NONE).getConcreteType();
      if (_returnType == null) {
        throw new RuntimeException("Cannot compute return type.");
      }
    }
    return _returnType;
  }

  public IJavaClassType getGenericReturnType() {
    if (_genericReturnType == null) {
      final Tree returnType = _method.getReturnType();
      _genericReturnType = JavaSourceType.createType(this, returnType);
      if (_genericReturnType == null) {
        throw new RuntimeException("Cannot compute return type.");
      }
    }
    return _genericReturnType;
  }

  @Override
  public String getReturnTypeName() {
    IJavaClassType type = getGenericReturnType();
    if (type instanceof JavaArrayClassInfo ) {
      return "[" + ((JavaArrayClassInfo) type).getComponentType().getNameSignature();
    } else {
      return type.getName();
    }
  }

  @Override
  public IType getReturnType() {
    return getReturnClassInfo().getJavaType();
  }

  @Override
  public IJavaClassType[] getGenericParameterTypes() {
    if (_genericParameterTypes == null) {
      _genericParameterTypes = initGenericParameterTypes();
    }
    return _genericParameterTypes;
  }

  @Override
  public IJavaClassInfo[] getParameterTypes() {
    if (_parameterTypes == null) {
      _parameterTypes = initParameterTypes();
    }
    return _parameterTypes;
  }

  public String getName() {
    return _method.getName().toString();
  }

  @Override
  public List<Parameter> getParameterInfos()
  {
    return Arrays.asList( getParameters() );
  }

  public JavaSourceParameter[] getParameters() {
    if (_parameters == null) {
      List<? extends VariableTree> parameters = _method.getParameters();
      if (parameters.isEmpty()) {
        _parameters = new JavaSourceParameter[0];
      } else {
        _parameters = new JavaSourceParameter[parameters.size()];
        for (int i = 0; i < _parameters.length; i++) {
          _parameters[i] = new JavaSourceParameter(this, parameters.get(i));
        }
      }
    }
    return _parameters;
  }

  public boolean isConstructor() {
    return false;
  }

  @Override
  public Tree getTree()
  {
    return _method;
  }

  public JavaSourceType getEnclosingClass() {
    return _containingClass;
  }

  @Override
  public int getModifiers() {
    return getModifierList().getModifiers();
  }

  public IModifierList getModifierList() {
    if (_modifierList == null) {
      _modifierList = new JavaSourceModifierList(this, _method.getModifiers());
    }
    return _modifierList;
  }

  public IJavaClassInfo[] getExceptionTypes() {
    return new IJavaClassInfo[0];
  }

  @Override
  public IJavaClassType resolveType(String relativeName, int ignoreFlags) {
    IJavaClassTypeVariable[] typeParameters = getTypeParameters();
    for (IJavaClassTypeVariable typeParameter : typeParameters) {
      if (relativeName.equals(typeParameter.getName())) {
        return typeParameter;
      }
    }

    return _containingClass.resolveType(relativeName, ignoreFlags);
  }

  @Override
  public IJavaClassType resolveType(String relativeName, IJavaClassInfo whosAskin, int ignoreFlags) {
    return null;
  }

  @Override
  public IJavaClassType resolveImport(String relativeName) {
    return null;
  }

  @Override
  public IModule getModule() {
    return _containingClass.getModule();
  }

  public String toString() {
    return getName() + "(...)";
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getModifierList().isAnnotationPresent(annotationClass);
  }

  @Override
  public IAnnotationInfo getAnnotation(Class annotationClass) {
    return getModifierList().getAnnotation(annotationClass);
  }

  @Override
  public IAnnotationInfo[] getDeclaredAnnotations() {
    return getModifierList().getAnnotations();
  }

  protected IJavaClassInfo[] initParameterTypes() {
    JavaSourceParameter[] psiParameters = getParameters();
    IJavaClassInfo[] paramTypes = new IJavaClassInfo[psiParameters.length];
    for (int i = 0; i < psiParameters.length; i++) {
      paramTypes[i] = psiParameters[i].getType();
    }
    return paramTypes;
  }

  protected IJavaClassType[] initGenericParameterTypes() {
    JavaSourceParameter[] parameters = getParameters();
    IJavaClassType[] types = new IJavaClassType[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      JavaSourceParameter parameter = parameters[i];
      types[i] = parameter.getGenericType();
    }
    return types;
  }

  @Override
  public Object getDefaultValue() {
    throw new RuntimeException("Should never be called");
  }

  @Override
  public Object invoke( Object ctx, Object[] args )
  {
    IAnnotationInfo annotation = getAnnotation( ExtensionMethod.class );
    if( annotation != null )
    {
      return invokeExtensionMethod( ctx, args, annotation );
    }

    if( _javaMethod == null )
    {
      IJavaClassInfo[] paramTypes = getParameterTypes();
      List<IType> params = new ArrayList<>();
      for( IJavaClassInfo paramType : paramTypes )
      {
        params.add( TypeSystem.get( paramType ) );
      }
      IType javaType = getEnclosingClass().getJavaType();
      IMethodInfo mi = javaType.getTypeInfo().getMethod( getName(), params.toArray( IType.EMPTY_TYPE_ARRAY ) );

      IRMethodFromMethodInfo irMethod = IRMethodFactory.createIRMethod( mi, new FunctionType( mi, false ) );
      List<IRType> allParameterTypes = irMethod.getAllParameterTypes();
      Class[] paramClasses = new Class[allParameterTypes.size()];
      for( int i = 0; i < allParameterTypes.size(); i++ )
      {
        paramClasses[i] = allParameterTypes.get( i ).getJavaClass();
      }

      if( ctx == null )
      {
        _javaMethod = ReflectUtil.method( getEnclosingClass().getBackingClass(), getName(), paramClasses ).getMethod();
      }
      else
      {
        _javaMethod = ReflectUtil.method( ctx, getName(), paramClasses ).getMethod();
      }
    }
    try
    {
      return ctx == null
             ? _javaMethod.invoke( null, args )
             : _javaMethod.invoke( ctx, args );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private Object invokeExtensionMethod( Object ctx, Object[] args, IAnnotationInfo annotation )
  {
    String extensionClass = (String)annotation.getFieldValue( ExtensionMethod.extensionClass );
    boolean isStatic = (boolean)annotation.getFieldValue( ExtensionMethod.isStatic );

    assert isStatic || ctx != null;

    IJavaType extensionType = (IJavaType)TypeSystem.getByFullName( extensionClass, getModule() );

    IJavaClassInfo[] paramTypes = getParameterTypes();
    List<IType> params = new ArrayList<>();
    for( IJavaClassInfo paramType: paramTypes )
    {
      params.add( TypeSystem.get( paramType ) );
    }

    if( !isStatic )
    {
      args = args == null ? new Object[0] : args;

      String namespace = extensionType.getNamespace();
      int iExt = namespace.lastIndexOf( "extensions." );
      String fqn = namespace.substring( iExt + "extensions.".length() );

      IType extendedClass = TypeSystem.getByFullName( fqn );
      params.add( 0, extendedClass );

      Object[] tempArgs = new Object[args.length + 1];
      tempArgs[0] = ctx;
      System.arraycopy( args, 0, tempArgs, 1, args.length );
      args = tempArgs;
    }

    IMethodInfo mi = extensionType.getTypeInfo().getMethod( getName(), params.toArray( IType.EMPTY_TYPE_ARRAY ) );
    IRMethodFromMethodInfo irMethod = IRMethodFactory.createIRMethod( mi, new FunctionType( mi, false ) );
    List<IRType> allParameterTypes = irMethod.getAllParameterTypes();
    Class[] paramClasses = new Class[allParameterTypes.size()];
    for( int i = 0; i < allParameterTypes.size(); i++ )
    {
      paramClasses[i] = allParameterTypes.get( i ).getJavaClass();
    }

    return ReflectUtil.method( extensionType.getBackingClass(), getName(), paramClasses ).invokeStatic( args );
  }

  public IJavaClassTypeVariable[] getTypeParameters() {
    if (_typeParameters == null) {
      final List<? extends TypeParameterTree> typeParameters = _method.getTypeParameters();
      if (!typeParameters.isEmpty()) {
        _typeParameters = new IJavaClassTypeVariable[typeParameters.size()];
        for (int i = 0; i < _typeParameters.length; i++) {
          _typeParameters[i] = JavaSourceTypeVariable.create(this, typeParameters.get(i));
        }
      } else {
        _typeParameters = JavaSourceTypeVariable.EMPTY;
      }
    }
    return _typeParameters;
  }

  @Override
  public IGenericTypeVariable[] getTypeVariables( IJavaMethodInfo mi ) {
    IJavaClassTypeVariable[] rawTypeParams = getTypeParameters();
    FunctionType functionType = new FunctionType( mi, true );
    return GenericTypeVariable.convertTypeVars( functionType, mi.getOwnersType(), rawTypeParams );
  }

  @Override
  public boolean isBridge() {
    return false;
  }

  @Override
  public boolean isSynthetic() {
    return false;
  }

  public static JavaSourceMethod create(MethodTree method, JavaSourceType containingClass) {
    final Name name = method.getName();
    if(name == null) {
      return null;
    }
    String methodName = name.toString();
    if (methodName.equals("<init>")) {
      return new JavaSourceConstructor(method, containingClass);
    } else if (containingClass.isAnnotation()) {
      return new JavaSourceAnnotationMethod(method, containingClass);
    } else {
      return new JavaSourceMethod(method, containingClass);
    }
  }

  protected IParameterInfo[] getActualParameterInfos(IFeatureInfo container, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars) {
    IType[] paramTypes = ClassInfoUtil.getActualTypes(getGenericParameterTypes(), actualParamByVarName, bKeepTypeVars);
    IParameterInfo[] paramInfos = new IParameterInfo[paramTypes.length];
    for (int i = 0; i < paramTypes.length; i++) {
      IType paramType = paramTypes[i];
      paramType = TypeLord.replaceRawGenericTypesWithDefaultParameterizedTypes( paramType );
      paramInfos[i] = new SimpleParameterInfo(container, paramType, i);
    }
    return paramInfos;
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode( getParameterTypes() );
    result = 31 * result + getReturnType().hashCode();
    result = 31 * result + getName().hashCode();
    return result;
  }

  public boolean equals( Object o ) {
    if( !(o instanceof IJavaClassMethod) ) {
      return false;
    }

    IJavaClassMethod jcm = (IJavaClassMethod)o;
    return getName().equals( jcm.getName() ) &&
           getReturnType() == jcm.getReturnType() &&
           Arrays.equals( getParameterTypes(), jcm.getParameterTypes() );
  }

  @Override
  public int compareTo( IJavaClassMethod o ) {
    return getName().compareTo( o.getName() );
  }
}
