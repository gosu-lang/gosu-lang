/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.java.classinfo.JavaSourceDefaultValue;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IAnnotatedFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.LazyStringSourceFileHandle;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import gw.util.GosuClassUtil;
import gw.util.Array;
import java.util.List;

/**
 */
public class GosuClassProxyFactory
{
  private static final GosuClassProxyFactory INSTANCE = new GosuClassProxyFactory();


  private GosuClassProxyFactory()
  {
  }

  public static GosuClassProxyFactory instance()
  {
    return INSTANCE;
  }

  public IGosuClassInternal create( IType type )
  {
    if( type instanceof IJavaType )
    {
      IJavaTypeInternal javaType = (IJavaTypeInternal)type;
      return (IGosuClassInternal)createJavaProxy( javaType );
    }

    throw new IllegalArgumentException( "No handler for type: " + type.getClass().getName() );
  }

  public IGosuClassInternal createImmediately( IType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }

    if( type instanceof IJavaType )
    {
      IJavaTypeInternal javaType = (IJavaTypeInternal)type;
      return (IGosuClassInternal)createJavaProxyImmediately( javaType );
    }

    throw new IllegalArgumentException( "No handler for type: " + type.getClass().getName() );
  }

  private IGosuClass createJavaProxy( IJavaTypeInternal type )
  {
    if( type.isParameterizedType() )
    {
      type = (IJavaTypeInternal)type.getGenericType();
    }

    IGosuClassInternal gsAdapterClass;

    gsAdapterClass = type.getAdapterClass();

    if( gsAdapterClass != null )
    {
      return gsAdapterClass;
    }
    return createJavaProxyImmediately( type );
  }

  private IGosuClass createJavaProxyImmediately( IJavaTypeInternal type )
  {
    IGosuClassInternal gsAdapterClass;

    if( type.getEnclosingType() != null )
    {
      // Ensure enclosing type is proxied; it contains the gosu source for the inner type
      IGosuClass outerProxy = (IGosuClass)TypeSystem.getByFullName( getProxyName( TypeLord.getOuterMostEnclosingClass( type ) ) );
      outerProxy.getInnerClasses();
      if( !outerProxy.isCompilingDeclarations() )
      {
        gsAdapterClass = getAdapterClass(type, outerProxy);
        if( gsAdapterClass == null )
        {
          TypeSystem.refresh( (ITypeRef)outerProxy);
          gsAdapterClass = getAdapterClass(type, outerProxy);
        }
      }
      else
      {
        return null;
      }
    }
    else
    {
      if( type.isInterface() )
      {
        gsAdapterClass = (IGosuClassInternal)createJavaInterfaceProxy( type );
      }
      else
      {
        gsAdapterClass = (IGosuClassInternal)createJavaClassProxy( type );
      }
    }

    type.setAdapterClass( gsAdapterClass );

    if( gsAdapterClass != null ) {
      gsAdapterClass.setJavaType( type );
    }
    return gsAdapterClass;
  }

  private IGosuClassInternal getAdapterClass(IJavaTypeInternal type, IGosuClass outerProxy) {
    IGosuClassInternal gsAdapterClass;

    String proxyName = getProxyName(type);
    int index = outerProxy.getName().length() + 1;

    if(index < 0 || index >= proxyName.length()) {
      return null;
    }
    String[] dotPath = proxyName.substring(index).split("\\.");
    int i = 0;
    gsAdapterClass = (IGosuClassInternal)outerProxy;
    while(i < dotPath.length && gsAdapterClass != null)
    {
      gsAdapterClass = (IGosuClassInternal) gsAdapterClass.getInnerClass( dotPath[i] );
      i++;
    }
    return gsAdapterClass;
  }

  private IGosuClass createJavaInterfaceProxy( final IJavaType type )
  {
    final IModule module = type.getTypeLoader().getModule();
    GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader( module );
    String fqn = getProxyName( type );
    return loader.makeNewClass(
        new LazyStringSourceFileHandle( GosuClassUtil.getPackage( fqn ), fqn, () -> {
          TypeSystem.pushModule( module );
          try {
            return genJavaInterfaceProxy( type ).toString();
          }
          finally {
            TypeSystem.popModule( module );
          }
        }, ClassType.Class ));
  }

  private IGosuClass createJavaClassProxy( final IJavaType type )
  {
    String strProxy = IGosuClass.PROXY_PREFIX + '.' + type.getName();
    IType compilingType = GosuClassCompilingStack.getCompilingType( strProxy );
    if( compilingType != null )
    {
      return (IGosuClass)compilingType;
    }

    final IModule module = type.getTypeLoader().getModule();
    String fqn = getProxyName( type );
    return GosuClassTypeLoader.getDefaultClassLoader( module ).makeNewClass(
      new LazyStringSourceFileHandle( GosuClassUtil.getPackage( fqn ), fqn, () -> {
        TypeSystem.pushModule( module );
        try {
          return genJavaClassProxy( type ).toString();
        }
        finally
        {
          TypeSystem.popModule( module );
        }
      }, ClassType.Class ) );
  }

  private static String getProxyName( IType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    return IGosuClass.PROXY_PREFIX + '.' + type.getName();
  }

  private StringBuilder genJavaClassProxy( IJavaType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( IGosuClass.PROXY_PREFIX ).append('.').append( type.getNamespace() ).append( '\n' );
    genClassImpl(type, sb);
    return sb;
  }

  private void genClassImpl( IJavaType type, StringBuilder sb )
  {
    addAnnotations( type.getTypeInfo(), sb );

    addModifiers(type, sb);

    sb.append( "class " ).append( getRelativeName( type ) ).append( '\n' );
    sb.append( "{\n" );

    JavaTypeInfo ti;
    if( type.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)type.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( type + " is not a recognized Java type" );
    }

    // Constructors
    for( Object o : ti.getConstructors( type ) )
    {
      IConstructorInfo ci = (IConstructorInfo)o;
      genConstructor( sb, ci );
    }

    // Properties
    for( Object o : ti.getProperties( type ) )
    {
      IPropertyInfo pi = (IPropertyInfo)o;
      genProperty( pi, sb, type );
    }

    // Methods
    for( Object o : ti.getMethods( type ) )
    {
      IMethodInfo mi = (IMethodInfo)o;
      genMethodImpl( sb, mi );
    }
    // Inner classes/interfaces
    for( IJavaType innerClass : type.getInnerClasses() )
    {
      if( (Modifier.isPublic( innerClass.getModifiers() ) ||
           Modifier.isProtected( innerClass.getModifiers() ) ||
           !Modifier.isPrivate( innerClass.getModifiers() )) )
      {
        if( innerClass.isInterface() )
        {
          genInterfaceImpl( innerClass, sb );
        }
        else
        {
          genClassImpl( innerClass, sb );
        }
      }
    }
    sb.append( "}\n" );
  }

  private void addModifiers(IJavaType type, StringBuilder sb) {
    if( type.isAbstract() )
    {
      sb.append( "abstract " );
    }
    if( type.getEnclosingType() != null && Modifier.isStatic(type.getModifiers()) )
    {
      sb.append( "static " );
    }

    if( type.isFinal() )
    {
      sb.append( Keyword.KW_final ).append( " " );
    }
    if( Modifier.isProtected( type.getModifiers() ) )
    {
      sb.append( Keyword.KW_protected ).append( " " );
    }
    else if( !Modifier.isPrivate( type.getModifiers() ) && !Modifier.isPublic( type.getModifiers() ) )
    {
      sb.append( Keyword.KW_internal ).append( " " );
    }
  }

  private void addAnnotations( IAnnotatedFeatureInfo featureInfo, StringBuilder sb ) {
    for( IAnnotationInfo annotation : featureInfo.getDeclaredAnnotations() ) {
      sb.append( makeAnnotationSource( annotation ) ).append( "\n" );
    }
  }

  private static String makeAnnotationSource( IAnnotationInfo annotation ) {
    if( ErrorType.NAME.equals( annotation.getName() ) ) {
      // It's possible from IJ that the Java type is not compiled completely with incomplete or invalid annotation expressions
      return "";
    }
    StringBuilder sb = new StringBuilder( "@" + annotation.getName() + "(" );
    boolean bFirst = true;
    for( IMethodInfo mi : annotation.getType().getTypeInfo().getMethods() ) {
      if( isObjectMethod( mi ) ) {
        continue;
      }
      String name = mi.getDisplayName();
      if( name.equals( "annotationType" ) ) {
        continue;
      }
      String value = makeValueString( annotation.getFieldValue( name ), mi.getReturnType() );
      if( !bFirst ) {
        sb.append( ", " );
      }
      sb.append( ":" ).append( name ).append( "=" ).append( value );
      bFirst = false;
    }
    sb.append( ")" );
    return sb.toString();
  }

  public static String makeValueString( Object value, IType returnType ) {
    if( value == null ) {
      return "null";
    }
    if( value instanceof JavaSourceDefaultValue ) {
      return ((JavaSourceDefaultValue)value).getValue();
    }
    if( returnType == JavaTypes.STRING() ) {
      return "\"" + value.toString().replace( '\n', ' ' ) + "\"";
    }
    if( returnType.isEnum() ) {
       return value.toString();
    }
    if( returnType.isPrimitive() ) {
      return String.valueOf( value );
    }
    if( JavaTypes.CLASS().isAssignableFrom( returnType ) ) {
      if( value instanceof String ) {
        return (String)value;
      }
      return ((Class)value).getName();
    }
    if( value instanceof IAnnotationInfo ) {
      return makeAnnotationSource( (IAnnotationInfo)value );
    }
    boolean isArray = returnType.isArray();
    if( value.getClass().isArray() ) {
      assert isArray;
      StringBuilder arrayValue = new StringBuilder( "{" );
      for( int i = 0; i < Array.getLength( value ); i++ ) {
        if( i > 0 ) {
          arrayValue.append( ", " );
        }
        arrayValue.append( makeValueString( Array.get( value, i ), returnType.getComponentType() ) );
      }
      arrayValue.append( "}" );
      return arrayValue.toString();
    }
    if( List.class.isAssignableFrom( value.getClass() ) ) {
      assert isArray;
      List list = (List)value;
      StringBuilder arrayValue = new StringBuilder( "{" );
      for( int i = 0; i < list.size(); i++ ) {
        if( i > 0 ) {
          arrayValue.append( ", " );
        }
        arrayValue.append( makeValueString( list.get( i ), returnType.getComponentType() ) );
      }
      arrayValue.append( "}" );
      return arrayValue.toString();
    }
    if(isArray) {
      StringBuilder arrayValue = new StringBuilder( "{" );
      arrayValue.append( makeValueString( value, returnType.getComponentType() ) );
      arrayValue.append( "}" );
      return arrayValue.toString();
    }
    throw new IllegalStateException();
  }

  public static boolean isObjectMethod( IMethodInfo mi )
  {
    IParameterInfo[] params = mi.getParameters();
    IType[] paramTypes = new IType[params.length];
    for( int i = 0; i < params.length; i++ )
    {
      paramTypes[i] = params[i].getFeatureType();
    }
    IRelativeTypeInfo ti = (IRelativeTypeInfo)JavaTypes.OBJECT().getTypeInfo();
    IMethodInfo objMethod = ti.getMethod( JavaTypes.OBJECT(), mi.getDisplayName(), paramTypes );
    return objMethod != null;
  }

  public static boolean isObjectProperty( IPropertyInfo pi )
  {
    IRelativeTypeInfo ti = (IRelativeTypeInfo)JavaTypes.OBJECT().getTypeInfo();
    IPropertyInfo objProp = ti.getProperty( JavaTypes.OBJECT(), pi.getDisplayName() );
    return objProp != null;
  }

  private String getRelativeName( IJavaType type )
  {
    String strName = TypeSystem.getGenericRelativeName( type, false );
    if( type.getEnclosingType() != null )
    {
      int iParamsIndex = strName.indexOf( '<' );
      int iIndex = iParamsIndex > 0
                   ? strName.substring( 0, iParamsIndex ).lastIndexOf( '.' )
                   : strName.lastIndexOf( '.' );
      if( iIndex > 0 )
      {
        strName = strName.substring( iIndex + 1 );
      }
    }
    return strName;
  }

  private StringBuilder genJavaInterfaceProxy( IJavaType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( IGosuClass.PROXY_PREFIX ).append( '.' ).append( type.getNamespace() ).append('\n');
    genInterfaceImpl( type, sb );
    return sb;
  }

  private void genInterfaceImpl( IJavaType type, StringBuilder sb )
  {
    sb.append( Modifier.toModifierString( type.getModifiers() ) ).append( " interface " ).append( getRelativeName( type ) ).append('\n');
    sb.append( "{\n" );

    ITypeInfo ti = type.getTypeInfo();

    // Interface properties
    for( Object o : ti.getProperties() )
    {
      IPropertyInfo pi = (IPropertyInfo)o;
      genInterfacePropertyDecl( sb, pi, type );
    }

    // Interface methods
    for( Object o : ti.getMethods() )
    {
      IMethodInfo mi = (IMethodInfo)o;
      if( mi.isDefaultImpl() || mi.isStatic() ) {
        genMethodImpl( sb, mi );
      }
      else {
        genInterfaceMethodDecl( sb, mi );
      }
    }

    // Inner interfaces
    for( IJavaType iface : type.getInnerClasses() )
    {
      if( iface.isInterface() &&
          (Modifier.isPublic( iface.getModifiers() ) ||
           Modifier.isProtected( iface.getModifiers() ) ||
           !Modifier.isPrivate( iface.getModifiers() )) &&
          !Modifier.isFinal( iface.getModifiers() ) )
      {
        genInterfaceImpl( iface, sb );
      }
    }
    sb.append( "}\n" );
  }

  private void genMethodImpl( StringBuilder sb, IMethodInfo mi )
  {
    if( mi.isPrivate() )
    {
      return;
    }

    if( mi.isStatic() && mi.getDisplayName().indexOf( '$' ) < 0 )
    {
      genStaticMethod( sb, mi );
    }
    else
    {
      genMemberMethod( sb, mi );
    }
  }

  private void genConstructor( StringBuilder sb, IConstructorInfo ci )
  {
    if( (ci instanceof JavaConstructorInfo) && ((JavaConstructorInfo)ci).isSynthetic() )
    {
      return;
    }

    StringBuilder sbModifiers = appendVisibilityModifier( ci );
    if( ci.getDescription() != null )
    {
      sb.append( "\n/** " ).append( ci.getDescription() ).append( " */\n" );
    }
    sb.append( "  " ).append( sbModifiers ).append( "  construct(" );
    IParameterInfo[] params = ci.getParameters();
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ")\n" )
      .append( "{\n" )
      .append( "}\n" );
  }

  private StringBuilder appendVisibilityModifier( IAttributedFeatureInfo fi )
  {
    StringBuilder sb = new StringBuilder();
    addAnnotations( fi, sb );
    if( fi.isPublic() )
    {
      // default
    }
    else if( fi.isProtected() )
    {
      sb.append( Keyword.KW_protected ).append( " " );
    }
    else if( fi.isPrivate() )
    {
      sb.append( Keyword.KW_private ).append( " " );
    }
    else // internal
    {
      sb.append( Keyword.KW_internal ).append( " " );
    }
    return sb;
  }

  private StringBuilder appendFieldVisibilityModifier( IAttributedFeatureInfo fi )
  {
    StringBuilder sb = new StringBuilder();
    addAnnotations( fi, sb );
    if( fi.isPublic() )
    {
      sb.append( Keyword.KW_public ).append( " " );
    }
    else if( fi.isProtected() )
    {
      sb.append( Keyword.KW_protected ).append( " " );
    }
    else if( !fi.isPrivate() )
    {
      sb.append( Keyword.KW_internal ).append( " " );
    }
    return sb;
  }

  private void genMemberMethod( StringBuilder sb, IMethodInfo mi )
  {
    if( !canExtendMethod( mi ) )
    {
      return;
    }

    StringBuilder sbModifiers = buildModifiers( mi );
    if( mi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( mi.getDescription() ).append( " */\n" );
    }
    sb.append( "  " ).append( sbModifiers ).append( "function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = mi.getParameters();
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( mi.getReturnType().getName() ).append( "\n" );
    if( !mi.isAbstract() )
    {
      generateStub( sb, mi.getReturnType() );
    }
  }

  private static void generateStub( StringBuilder sb, IType returnType )
  {
    sb.append( "{\n" )
      .append( (returnType == JavaTypes.pVOID()
                ? ""
                : "    return " +
                  (!returnType.isPrimitive()
                   ? "null"
                   : CommonServices.getCoercionManager().convertNullAsPrimitive( returnType, false ))) );
    sb.append( "}\n" );
  }

  private boolean canExtendMethod( IMethodInfo mi )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return false;
    }

    if( isPropertyMethod( mi ) )
    {
      // We favor properties over methods -- gotta pick one
      return false;
    }

    //## todo: maybe support implementing/overriding Java methods having a keyword as a name by escaping the keyword (using @ maybe?) so the parser can throw it away and not stop on the token as a reserved keyword 77
    if( Keyword.isReservedKeyword( mi.getDisplayName() ) )
    {
      return false;
    }

    int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
    return //!java.lang.reflect.Modifier.isFinal( iMethodModifiers ) &&
      !java.lang.reflect.Modifier.isNative( iMethodModifiers ) &&
      // See GosuClassInstanceFactory.genSuperClassMembers() (we don't allow finalizers)
      !mi.getDisplayName().equals( "finalize" ) &&
      mi.getDisplayName().indexOf( '$' ) < 0;
  }

  private void genStaticMethod( StringBuilder sb, IMethodInfo mi )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Only static JAVA members should be reflected in
      // the proxy.
      return;
    }

    if( isPropertyMethod( mi ) )
    {
      // We favor properties over methods -- gotta pick one
      return;
    }

    StringBuilder sbModifiers = appendVisibilityModifier( mi );
    if( mi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( mi.getDescription() ).append( " */\n" );
    }
    sb.append( "  " ).append( sbModifiers ).append( "static function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = mi.getParameters();
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( mi.getReturnType().getName() ).append( "\n" );
    generateStub( sb, mi.getReturnType() );
  }

  private void genInterfaceMethodDecl( StringBuilder sb, IMethodInfo mi )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }
    if( isPropertyMethod( mi ) )
    {
      return;
    }
    if( mi.getDisplayName().equals( "hashCode" ) || (mi.getDisplayName().equals( "equals" ) && mi.getParameters().length == 1 && mi.getParameters()[0].getFeatureType() == JavaTypes.OBJECT()) || mi.getDisplayName().equals( "toString" ) )
    {
      if( !mi.getOwnersType().getName().equals( IGosuObject.class.getName() ) )
      {
        return;
      }
    }
    if( mi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( mi.getDescription() ).append( " */\n" );
    }
    sb.append( "  function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = mi.getParameters();
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() );
      sb.append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( mi.getReturnType().getName() ).append( ";\n" );
  }

  public static boolean isPropertyMethod( IMethodInfo mi )
  {
    return isPropertyGetter( mi ) ||
           isPropertySetter( mi );
  }

  public static boolean isPropertyGetter( IMethodInfo mi )
  {
    return isPropertyGetter( mi, "get" ) ||
           isPropertyGetter( mi, "is" );
  }

  public static boolean isPropertySetter( IMethodInfo mi )
  {
    String strMethod = mi.getDisplayName();
    if( strMethod.startsWith( "set" ) &&
        strMethod.length() > 3 &&
        mi.getParameters().length == 1 &&
        mi.getReturnType() == JavaTypes.pVOID() )
    {
      String strProp = strMethod.substring( "set".length() );
      if( Character.isUpperCase( strProp.charAt( 0 ) ) )
      {
        ITypeInfo ti = (ITypeInfo)mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
                           ? ((IRelativeTypeInfo)ti).getProperty( mi.getOwnersType(), strProp )
                           : ti.getProperty( strProp );
        if( pi != null && mi.isStatic() == pi.isStatic() &&
            pi.getFeatureType().getName().equals( mi.getParameters()[0].getFeatureType().getName() ) )
        {
          return (!(pi instanceof IJavaPropertyInfo) || ((IJavaPropertyInfo)pi).getPropertyDescriptor().getWriteMethod() != null && ((IJavaPropertyInfo)pi).getPropertyDescriptor().getWriteMethod().getName().equals( mi.getDisplayName() )) &&
                 !Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() );
        }
      }
    }
    return false;
  }

  public static boolean isPropertyGetter( IMethodInfo mi, String strPrefix )
  {
    String strMethod = mi.getDisplayName();
    if( strMethod.startsWith( strPrefix ) &&
        mi.getParameters().length == 0 )
    {
      String strProp = strMethod.substring( strPrefix.length() );
      if( strProp.length() > 0 && Character.isUpperCase( strProp.charAt( 0 ) ) )
      {
        ITypeInfo ti = (ITypeInfo)mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
                           ? ((IRelativeTypeInfo)ti).getProperty( mi.getOwnersType(), strProp )
                           : ti.getProperty( strProp );
        if( pi != null && mi.isStatic() == pi.isStatic() && pi.getFeatureType().getName().equals( mi.getReturnType().getName() ) )
        {
          return (!(pi instanceof IJavaPropertyInfo) ||
                  ((IJavaPropertyInfo)pi).getPropertyDescriptor().getReadMethod() != null && ((IJavaPropertyInfo)pi).getPropertyDescriptor().getReadMethod().getName().equals( mi.getDisplayName() )) &&
                 (!Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() ));
        }
      }
    }
    return false;
  }

  private void genInterfacePropertyDecl( StringBuilder sb, IPropertyInfo pi, IJavaType javaType )
  {
    if( pi.isStatic() )
    {
      genStaticProperty( pi, sb );
      return;
    }

    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }

    IType type = pi.getFeatureType();
    if( pi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( pi.getDescription() ).append( " */\n" );
    }
    if( pi.isReadable() )
    {
      sb.append( " property get " ).append( pi.getName() ).append( "() : " ).append( type.getName() ).append( "\n" );
      IMethodInfo mi = getPropertyGetMethod( pi, javaType );
      if( mi != null && !mi.isAbstract() )
      {
        generateStub( sb, mi.getReturnType() );
      }
    }
    if( pi.isWritable( pi.getOwnersType() ) )
    {
      sb.append( " property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( type.getName() ).append( " )\n" );
      IMethodInfo mi = getPropertySetMethod( pi, javaType );
      if( mi != null && !mi.isAbstract() )
      {
        generateStub( sb, mi.getReturnType() );
      }
    }
  }

  private void genProperty( IPropertyInfo pi, StringBuilder sb, IJavaType type )
  {
    if( pi.isPrivate() )
    {
      return;
    }

    if( pi.isStatic() )
    {
      genStaticProperty( pi, sb );
    }
    else
    {
      genMemberProperty( pi, sb, type );
    }
  }

  private void genMemberProperty( IPropertyInfo pi, StringBuilder sb, IJavaType type )
  {
    if( pi.isStatic() )
    {
      return;
    }

    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a propertyinfo on a java type originates outside of java.
      // E.g., enhancement properties. Gosu does not support extending these.
      return;
    }

    if( Keyword.isKeyword( pi.getName() ) && !Keyword.isValueKeyword( pi.getName() ) )
    {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    if( pi instanceof JavaFieldPropertyInfo )
    {
      StringBuilder sbModifiers = appendFieldVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( " var " ).append( pi.getName() ).append( " : " ).append( pi.getFeatureType().getName() ).append( "\n" );
    }
    else
    {
      IMethodInfo mi = getPropertyGetMethod( pi, type );
      boolean bFinal;
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );

        if( !bFinal )
        {
          if( mi.getDescription() != null )
          {
            sb.append( "\n/** " ).append( mi.getDescription() ).append( " */\n" );
          }
          StringBuilder sbModifiers = buildModifiers( mi );
          sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( pi.getFeatureType().getName() ).append( "\n" );
          if( !mi.isAbstract() )
          {
            generateStub( sb, mi.getReturnType() );
          }
        }
        else
        {
          StringBuilder sbModifiers;
          boolean bAbstract = mi.isAbstract();
          sbModifiers = buildModifiers( mi );
          sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( pi.getFeatureType().getName() ).append( "\n" );
          if( !bAbstract )
          {
            generateStub( sb, pi.getFeatureType() );
          }
        }
      }
      mi = getPropertySetMethod( pi, type );
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );
        if( !bFinal )
        {
          StringBuilder sbModifiers = buildModifiers( mi );
          if( pi.isWritable( pi.getOwnersType() ) )
          {
            sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( pi.getFeatureType().getName() ).append( " )\n" );
            if( !mi.isAbstract() )
            {
              generateStub( sb, JavaTypes.pVOID() );
            }
          }
        }
        else
        {
          if( pi.isWritable( type.getEnclosingType() != null ? null : pi.getOwnersType() ) )
          {
            StringBuilder sbModifiers;
            boolean bAbstact = false;
            if( bFinal )
            {
              bAbstact = mi.isAbstract();
              sbModifiers = buildModifiers( mi );
            }
            else
            {
              sbModifiers = appendVisibilityModifier( pi );
            }
            sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( pi.getFeatureType().getName() ).append( " )\n" );
            if( !bAbstact )
            {
              generateStub( sb, JavaTypes.pVOID() );
            }
          }
        }
      }
    }
  }

  private StringBuilder buildModifiers( IAttributedFeatureInfo fi )
  {
    StringBuilder sbModifiers = new StringBuilder();

    addAnnotations( fi, sbModifiers );

    if( fi.isAbstract() )
    {
      sbModifiers.append( Keyword.KW_abstract ).append( " " );
    }
    else if( fi.isFinal() )
    {
      sbModifiers.append( Keyword.KW_final ).append( " " );
    }
    if( fi.isProtected() )
    {
      sbModifiers.append( Keyword.KW_protected ).append( " " );
    }
    else if( !fi.isPrivate() && !fi.isPublic() )
    {
      sbModifiers.append( Keyword.KW_internal ).append( " " );
    }

    return sbModifiers;
  }

  private IMethodInfo getPropertyGetMethod( IPropertyInfo pi, IJavaType ownerType )
  {
    JavaTypeInfo ti;
    if( !(pi.getOwnersType() instanceof IJavaType) )
    {
      return null;
    }
    if( ownerType.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)ownerType.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( ownerType + " is not a recognized Java type" );
    }

    IType propType = pi.getFeatureType();

    String strAccessor = "get" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod( ownerType, strAccessor );
    if( mi == null )
    {
      strAccessor = "is" + pi.getDisplayName();
      mi = ti.getMethod( ownerType, strAccessor );
    }
    if( mi != null && mi.getReturnType().equals( propType ) )
    {
      return mi;
    }

    return null;
  }

  private IMethodInfo getPropertySetMethod( IPropertyInfo pi, IJavaType ownerType )
  {
    JavaTypeInfo ti;
    if( !(pi.getOwnersType() instanceof IJavaType) )
    {
      return null;
    }
    if( ownerType.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)ownerType.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( ownerType + " is not a recognized Java type" );
    }

    IType propType = pi.getFeatureType();

    // Check for Setter

    String strAccessor = "set" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod( ownerType, strAccessor, propType );
    if( mi != null && mi.getReturnType() == JavaTypes.pVOID() )
    {
      return mi;
    }

    return null;
  }

  private void genStaticProperty( IPropertyInfo pi, StringBuilder sb )
  {
    if( !pi.isStatic() )
    {
      return;
    }

    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }

    if( Keyword.isKeyword( pi.getName() ) )
    {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    if( pi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( pi.getDescription() ).append( " */\n" );
    }
    if( pi instanceof JavaFieldPropertyInfo )
    {
      StringBuilder sbModifiers = appendFieldVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( "static var " ).append( pi.getName() ).append( " : " ).append( pi.getFeatureType().getName() ).append( "\n" );
    }
    else
    {
      if( pi.isReadable( pi.getOwnersType() ) )
      {
        StringBuilder sbModifiers = appendVisibilityModifier( pi );
        sb.append( "  " ).append( sbModifiers ).append( "static property get " ).append( pi.getName() ).append( "() : " ).append( pi.getFeatureType().getName() ).append( "\n" );
        if( !pi.isAbstract() )
        {
          generateStub( sb, pi.getFeatureType() );
        }
      }
      if( pi.isWritable( pi.getOwnersType() ) )
      {
        sb
          .append( "  static property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( pi.getFeatureType().getName() ).append( " )\n" );
          if( !pi.isAbstract() )
          {
            generateStub( sb, JavaTypes.pVOID() );
          }
      }
    }
  }
}
