/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.java.classinfo.JavaSourceDefaultValue;
import gw.lang.parser.ISource;
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
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.Callable;

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
      IGosuClass outerProxy = (IGosuClass)TypeSystem.getByFullName( getProxyName( (IJavaType)TypeLord.getOuterMostEnclosingClass( type ) ) );
      outerProxy.getInnerClasses();
      if( !outerProxy.isCompilingDeclarations() )
      {
        gsAdapterClass = (IGosuClassInternal)outerProxy.getInnerClass( type.getRelativeName().substring( type.getRelativeName().indexOf( '.' ) + 1 ) );
        if( gsAdapterClass == null )
        {
          TypeSystem.refresh( (ITypeRef)outerProxy);
          gsAdapterClass = (IGosuClassInternal)outerProxy.getInnerClass( type.getRelativeName().substring( type.getRelativeName().indexOf( '.' ) + 1 ) );
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

  private IGosuClass createJavaInterfaceProxy( final IJavaType type )
  {
    final IModule module = type.getTypeLoader().getModule();
    GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader( module );
    return loader.makeNewClass(
        new LazyStringSourceFileHandle(type, new Callable<StringBuilder>() {
          public StringBuilder call() {
            TypeSystem.pushModule( module );
            try {
              return genJavaInterfaceProxy(type);
            } finally {
              TypeSystem.popModule( module );
            }
          }
        }));
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
    return GosuClassTypeLoader.getDefaultClassLoader( module ).makeNewClass(
      new LazyStringSourceFileHandle( type, new Callable<StringBuilder>()
      {
        public StringBuilder call()
        {
          TypeSystem.pushModule( module );
          try {
            return genJavaClassProxy( type );
          } 
          finally
          {
            TypeSystem.popModule( module );
          }
        }
      } ) );
  }

  private static class LazyStringSourceFileHandle extends StringSourceFileHandle
  {
    private Callable<StringBuilder> _sourceGen;

    public LazyStringSourceFileHandle( IType type, Callable<StringBuilder> sourceGen )
    {
      super( getProxyName( type ), null, false, ClassType.Class );
      _sourceGen = sourceGen;
    }

    @Override
    public ISource getSource()
    {
      if( getRawSource() == null )
      {
        try
        {
          setRawSource( _sourceGen.call().toString() );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
      return super.getSource();
    }
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
    if( value.getClass().isArray() ) {
      assert returnType.isArray();
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
      assert returnType.isArray();
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
    if( returnType.isArray() ) {
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
      genInterfacePropertyDecl( sb, pi );
    }

    // Interface methods
    for( Object o : ti.getMethods() )
    {
      IMethodInfo mi = (IMethodInfo)o;
      genInterfaceMethodDecl( sb, mi );
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
    IParameterInfo[] params = getGenericParameters( ci );
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
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( "\n" );
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
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( "\n" );
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
    if( mi.getDisplayName().equals( "hashCode" ) || mi.getDisplayName().equals( "equals" ) || mi.getDisplayName().equals( "toString" ) )
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
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() );
      sb.append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( ";\n" );
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
        if( pi != null && pi.isReadable() && mi.isStatic() == pi.isStatic() &&
            getGenericType( pi ).getName().equals( getGenericParameters( mi )[0].getFeatureType().getName() ) )
        {
          return !Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() );
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
        if( pi != null && getGenericType( pi ).getName().equals( getGenericReturnType( mi ).getName() ) )
        {
          return !Keyword.isKeyword( pi.getName() ) || Keyword.isValueKeyword( pi.getName() );
        }
      }
    }
    return false;
  }

  private void genInterfacePropertyDecl( StringBuilder sb, IPropertyInfo pi )
  {
    if( pi.isStatic() )
    {
      genStaticProperty( pi, sb );
      return;
    }
    if( !pi.isReadable() )
    {
      return;
    }
    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }
    IType type = getGenericType( pi );
    if( pi.getDescription() != null )
    {
      sb.append( "\n/** " ).append( pi.getDescription() ).append( " */\n" );
    }
    sb.append( " property get " ).append( pi.getName() ).append( "() : " ).append( type.getName() ).append( "\n" );
    if( pi.isWritable( pi.getOwnersType() ) )
    {
      sb.append( " property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( type.getName() ).append( " )\n" );
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
      sb.append( "  " ).append( sbModifiers ).append( " var " ).append( pi.getName() ).append( " : " ).append( getGenericType( pi ).getName() ).append( "\n" );
    }
    else
    {
      IMethodInfo mi = getPropertyGetMethod( pi, type );
      boolean bFinal = false;
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );
      }

      if( mi != null && !bFinal )
      {
        if( mi.getDescription() != null )
        {
          sb.append( "\n/** " ).append( mi.getDescription() ).append( " */\n" );
        }
        StringBuilder sbModifiers = buildModifiers( mi );
        sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" );
        if( !mi.isAbstract() )
        {
          generateStub( sb, mi.getReturnType() );
        }
      }
      else
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
        sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" );
        if( !bAbstact )
        {
          generateStub( sb, getGenericType( pi ) );
        }
      }

      mi = getPropertySetMethod( pi, type );
      bFinal = false;
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );
      }

      if( mi != null && !bFinal )
      {
        StringBuilder sbModifiers = buildModifiers( mi );
        if( pi.isWritable( pi.getOwnersType() ) )
        {
          sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" );
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
          sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" );
          if( !bAbstact )
          {
            generateStub( sb, JavaTypes.pVOID() );
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
    if( mi == null || mi.getReturnType() != propType )
    {
      strAccessor = "is" + pi.getDisplayName();
      mi = ti.getMethod( ownerType, strAccessor );
    }
    if( mi != null && mi.getReturnType() == propType )
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
      sb.append( "  " ).append( sbModifiers ).append( "static var " ).append( pi.getName() ).append( " : " ).append( getGenericType( pi ).getName() ).append( "\n" );
    }
    else
    {
      StringBuilder sbModifiers = appendVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( "static property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" );
      if( !pi.isAbstract() )
      {
        generateStub( sb, getGenericType( pi ) );
      }

      if( pi.isWritable( pi.getOwnersType() ) )
      {
        sb
          .append( "  static property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" );
          if( !pi.isAbstract() )
          {
            generateStub( sb, JavaTypes.pVOID() );
          }
      }
    }
  }

  private static IType getGenericType( IPropertyInfo pi )
  {
    return (pi instanceof IJavaPropertyInfo)
           ? ((IJavaPropertyInfo)pi).getGenericIntrinsicType()
           : pi.getFeatureType();
  }

  public static IType getGenericReturnType( IMethodInfo mi )
  {
    return (mi instanceof JavaMethodInfo)
           ? ((JavaMethodInfo)mi).getGenericReturnType()
           : mi.getReturnType();
  }

  public static IParameterInfo[] getGenericParameters( IMethodInfo mi )
  {
    return (mi instanceof JavaMethodInfo)
           ? ((JavaMethodInfo)mi).getGenericParameters()
           : mi.getParameters();
  }

  private static IParameterInfo[] getGenericParameters( IConstructorInfo ci )
  {
    return (ci instanceof JavaConstructorInfo)
           ? ((JavaConstructorInfo)ci).getGenericParameters()
           : ci.getParameters();
  }
}
