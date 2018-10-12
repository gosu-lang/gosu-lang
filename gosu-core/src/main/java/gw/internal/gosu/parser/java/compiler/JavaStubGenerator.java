package gw.internal.gosu.parser.java.compiler;

import gw.config.CommonServices;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.CompoundType;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.DynamicPropertySymbol;
import gw.internal.gosu.parser.GosuAnnotationInfo;
import gw.internal.gosu.parser.GosuClassParseInfo;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.ir.SignatureUtil;
import gw.lang.parser.IExpression;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.JavaTypes;
import gw.util.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 */
public class JavaStubGenerator
{
  private static final JavaStubGenerator INSTANCE = new JavaStubGenerator();

  public static JavaStubGenerator instance()
  {
    return INSTANCE;
  }

  private JavaStubGenerator()
  {
  }

  public String genStub( IGosuClass type )
  {
    StringBuilder sb = new StringBuilder();
    genPackage( type, sb );
    genType( type, sb );
    return sb.toString();
  }

  private void genType( IGosuClass type, StringBuilder sb )
  {
    if( type.isEnum() )
    {
      genEnum( type, sb );
    }
    else if( type.isAnnotation() )
    {
      genAnnotation( type, sb );
    }
    else
    {
      genClassOrInterface( type, sb );
    }
  }

  private void genPackage( IGosuClass type, StringBuilder sb )
  {
    sb.append( "/* Generated Stub from Gosu Class for Java Interop */\n" )
      .append( "package " ).append( type.getNamespace() ).append( ";\n" )
      .append( "\n" );
  }

  private void genAnnotation( IGosuClass type, StringBuilder sb )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations() );
    genModifiers( sb, type.getModifiers() & ~(Modifier.FINAL | Modifier.ABSTRACT), false, Modifier.PUBLIC );
    sb.append( "@interface " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genClassFeatures( sb, gsClass );

    sb.append( "}" );
  }

  private void genEnum( IGosuClass type, StringBuilder sb )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations() );
    genModifiers( sb, type.getModifiers() & ~Modifier.FINAL, false, Modifier.PUBLIC );
    sb.append( "enum " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genEnumConstants( sb, gsClass );
    genClassFeatures( sb, gsClass );

    sb.append( "}" );
  }

  private void genEnumConstants( StringBuilder sb, IGosuClassInternal gsClass )
  {
    List<String> enumConstants = gsClass.getEnumConstants();
    for( int i = 0; i < enumConstants.size(); i++ )
    {
      String c = enumConstants.get( i );
      sb.append( i > 0 ? ",\n" : "" )
        .append( c )
        .append( i == enumConstants.size()-1 ? ";\n\n" : "" );
    }
  }

  private void genClassOrInterface( IGosuClass type, StringBuilder sb )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations() );
    genModifiers( sb, type.getModifiers(), false, Modifier.PUBLIC );
    sb.append( type.isInterface() ? "interface " : "class " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassExtends( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genClassFeatures( sb, gsClass );

    sb.append( "}" );
  }

  private void genClassFeatures( StringBuilder sb, IGosuClassInternal gsClass )
  {
    GosuClassParseInfo parseInfo = gsClass.getParseInfo();

    genFields( sb, gsClass, parseInfo );
    genConstructors( sb, gsClass, parseInfo );
    genProperties( sb, gsClass, parseInfo );
    genMethods( sb, gsClass, parseInfo );
    genInnerClasses( sb, gsClass );
  }

  private String genClassImplements( IGosuClass type )
  {
    IType[] interfaces = type.getInterfaces();
    interfaces = Arrays.stream( interfaces ).filter( e -> shouldImplement( e, type ) ).toArray( IType[]::new );
    if( interfaces.length == 0 )
    {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append( " implements " );
    for( int i = 0; i < interfaces.length; i++ )
    {
      IType iface = interfaces[i];
      sb.append( i > 0 ? ", " : "" )
        .append( iface.getName() );
    }
    return sb.toString();
  }

  private boolean shouldImplement( IType iface, IGosuClass gsClass )
  {
    return !(
      IGosuClass.ProxyUtil.isProxy( iface ) ||
      gsClass.isAnnotation() && iface == JavaTypes.ANNOTATION()
    );
  }

  private String genClassExtends( IGosuClass type )
  {
    IType supertype = type.getSupertype();
    if( supertype == null )
    {
      return "";
    }
    return " extends " + supertype.getName();
  }

  private String getTypeVariables( IType type )
  {
    if( !type.isGenericType() )
    {
      return "";
    }

    IGenericTypeVariable[] gtvs = type.getGenericTypeVariables();

    StringBuilder sb = new StringBuilder();
    sb.append( '<' );
    for( int i = 0; i < gtvs.length; i++ )
    {
      IGenericTypeVariable gtv = gtvs[i];
      ITypeVariableDefinition tvd = gtv.getTypeVariableDefinition();
      sb.append( i > 0 ? ", " : "" ).append( tvd.getType().getRelativeName() );
      IType boundingType = tvd.getBoundingType();
      if( boundingType != JavaTypes.OBJECT() && boundingType != null )
      {
        sb.append( " extends ").append( getTypeName( boundingType ) );
      }
    }
    sb.append( "> " );
    return sb.toString();
  }

  private void genInnerClasses( StringBuilder sb, IGosuClassInternal gsClass )
  {
    sb.append( "\n// inner classes //\n" );
    for( IGosuClass innerClass : gsClass.getInnerClasses() )
    {
      genType( innerClass, sb );
    }
  }

  private void genFields( StringBuilder sb, IGosuClassInternal gsClass, GosuClassParseInfo parseInfo )
  {
    sb.append( "\n  // fields //\n" );

    Collection<VarStatement> fields = parseInfo.getMemberFields().values();
    for( VarStatement field : fields )
    {
      if( field.isPrivate() )
      {
        continue;
      }

      List<GosuAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( field.getAnnotations(), gsClass.getTypeInfo() );
      genAnnotations( sb, gosuAnnotationInfos );

      sb.append( "  " );
      genModifiers( sb, field.getModifiers(), false, Modifier.PUBLIC );
      sb.append( getTypeName( field.getType() ) ).append( ' ' ).append( field.getIdentifierName() ).append( ";\n" );
    }
  }

  private void genConstructors( StringBuilder sb, IGosuClassInternal gsClass, GosuClassParseInfo parseInfo )
  {
    sb.append( "\n  // constructors //\n" );

    Collection<DynamicFunctionSymbol> constructors = parseInfo.getConstructorFunctions().values();
    for( DynamicFunctionSymbol constructor : constructors )
    {
      if( constructor.isPrivate() )
      {
        continue;
      }

      List<GosuAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( constructor.getAnnotations(), gsClass.getTypeInfo() );
      genAnnotations( sb, gosuAnnotationInfos );

      sb.append( "  " );
      genModifiers( sb, constructor.getModifiers(), false, Modifier.PUBLIC );
      sb.append( SignatureUtil.getSimpleName( gsClass.getName() ) ).append( "(" );
      genParameters( sb, constructor );
      sb.append( ") {}\n" );
    }
  }

  private void genProperties( StringBuilder sb, IGosuClassInternal gsClass, GosuClassParseInfo parseInfo )
  {
    sb.append( "\n  // properties //\n" );

    genProperties( sb, gsClass, parseInfo.getStaticProperties() );
    genProperties( sb, gsClass, parseInfo.getMemberProperties().values() );
  }

  private void genProperties( StringBuilder sb, IGosuClassInternal gsClass, Collection<DynamicPropertySymbol> properties )
  {
    for( DynamicPropertySymbol dps : properties )
    {
      if( dps.isReadable() )
      {
        genMethod( sb, gsClass, dps.getGetterDfs(), dps.getType() == JavaTypes.pBOOLEAN() ? "is" : "get" + dps.getDisplayName() );
      }
      if( dps.isWritable() )
      {
        genMethod( sb, gsClass, dps.getSetterDfs(), "set" + dps.getDisplayName() );
      }
    }
  }

  private void genMethods( StringBuilder sb, IGosuClassInternal gsClass, GosuClassParseInfo parseInfo )
  {
    sb.append( "\n  // methods //\n" );

    genMethods( sb, gsClass, parseInfo.getStaticFunctions() );
    genMethods( sb, gsClass, parseInfo.getMemberFunctions().values() );
  }

  private void genMethods( StringBuilder sb, IGosuClassInternal gsClass, Collection<DynamicFunctionSymbol> methods )
  {
    for( DynamicFunctionSymbol method : methods )
    {
      genMethod( sb, gsClass, method );
    }
  }

  private void genMethod( StringBuilder sb, IGosuClassInternal gsClass, DynamicFunctionSymbol method )
  {
    genMethod( sb, gsClass, method, null );
  }

  private void genMethod( StringBuilder sb, IGosuClassInternal gsClass, DynamicFunctionSymbol method, String name )
  {
    if( method.isPrivate() ) // || method.isReified() )
    {
      return;
    }

    if( method.getDisplayName().startsWith( "@" ) && name == null )
    {
      return;
    }

    if( isBuiltinStaticEnumMethod( method ) )
    {
      return;
    }

    List<GosuAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( method.getAnnotations(), gsClass.getTypeInfo() );
    genAnnotations( sb, gosuAnnotationInfos );

    sb.append( "  " );
    int modifiers = method.getModifiers();
    if( gsClass.isInterface() )
    {
      modifiers &= ~(Modifier.ABSTRACT | Modifier.PUBLIC);
    }
    genModifiers( sb, modifiers, gsClass.isInterface() && !method.isAbstract() && !method.isStatic(), Modifier.PUBLIC );
    IType returnType = method.getReturnType();
    sb.append( getTypeVariables( method.getType() ) ).append( getTypeName( returnType ) ).append( ' ' ).append( name == null ? method.getDisplayName() : name ).append( '(' );
    genParameters( sb, method );
    if( method.isAbstract() )
    {
      IExpression annoDefault = method.getAnnotationDefault();
      if( annoDefault != null )
      {
        sb.append( ") default " ).append( genCompileTimeConstantExpression( method.getReturnType(), annoDefault.evaluate() ) ).append( ";\n" );
      }
      else
      {
        sb.append( ");\n" );
      }
    }
    else
    {
      sb.append( ") {" );
      if( returnType == JavaTypes.pVOID() )
      {
        sb.append( "}\n" );
      }
      else
      {
        genReturnStmt( sb, returnType );
      }
    }
  }

  private boolean isBuiltinStaticEnumMethod( DynamicFunctionSymbol method )
  {
    return method.isStatic() && method.getDeclaringTypeInfo().getOwnersType().isEnum() &&
           (method.getDisplayName().equals( "values" ) || method.getDisplayName().equals( "valueOf"));
  }

  private void genAnnotations( StringBuilder sb, List<? extends IAnnotationInfo> annotations )
  {
    for( IAnnotationInfo ai : annotations )
    {
      IType annoType = ai.getType();
      if( !JavaTypes.ANNOTATION().isAssignableFrom( annoType ) )
      {
        continue;
      }
      sb.append( '@' ).append( ai.getType().getName() ).append( '(' );
      MethodList methods = annoType.getTypeInfo().getMethods();
      for( int i = 0; i < methods.size(); i++ )
      {
        IMethodInfo mi = methods.get( i );
        if( mi.getOwnersType() == annoType )
        {
          String fieldName = mi.getDisplayName();
          fieldName = fieldName == null || fieldName.isEmpty() ? "value" : fieldName;
          sb.append( i == 0 ? ", " : "" ).append( fieldName ).append( '=' ).append( genFieldValue( ai, mi.getReturnType(), fieldName ) );
        }
      }
      sb.append( ")\n" );
    }
  }

  private String genFieldValue( IAnnotationInfo ai, IType fieldType, String fieldName )
  {
    Object value = ai.getFieldValue( fieldName );
    return genCompileTimeConstantExpression( fieldType, value );
  }

  private String genCompileTimeConstantExpression( IType type, Object value )
  {
    if( value == null )
    {
      return "null";
    }
    if( value instanceof IType )
    {
      return ((IType)value).getName() + ".class";
    }
    if( value instanceof String )
    {
      if( JavaTypes.STRING() == type )
      {
        return "\"" + value + "\"";
      }
      if( type.isEnum() )
      {
        return type.getName() + '.' + value;
      }
      if( type == JavaTypes.pCHAR() )
      {
        return "'" + value + "'";
      }
      return (String)value;
    }
    if( value instanceof Character )
    {
      return "'" + value + "'";
    }
    if( value.getClass().isArray() )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( "{" );
      int len = Array.getLength( value );
      for( int i = 0; i < len; i++ )
      {
        Object v = Array.get( value, i );
        sb.append( i > 0 ? ", " : "" ).append( genCompileTimeConstantExpression( type.getComponentType(), v ) );
      }
      sb.append( "}" );
      return sb.toString();
    }
    return value.toString();
  }

  private void genReturnStmt( StringBuilder sb, IType returnType )
  {
    sb.append( " return " ).append( !returnType.isPrimitive() ? "null" : makeDefaultPrimitiveValue( returnType ) ).append( "; }\n" );
  }

  private String makeDefaultPrimitiveValue( IType returnType )
  {
    return genCompileTimeConstantExpression( returnType, CommonServices.getCoercionManager().convertNullAsPrimitive( returnType, false ) );
  }

  private void genParameters( StringBuilder sb, DynamicFunctionSymbol dfs )
  {
    List<ISymbol> parameters = dfs.getArgs();
    int iParam = addReifiedTypeParamaters( sb, dfs );
    for( int i = 0; i < parameters.size(); i++ )
    {
      ISymbol param = parameters.get( i );
      sb.append( iParam > 0 ? ", " : "" ).append( getTypeName( param.getType() ) ).append( ' ' ).append( param.getDisplayName() );
      iParam++;
    }
  }

  private int addReifiedTypeParamaters( StringBuilder sb, DynamicFunctionSymbol dfs )
  {
    int iParam = 0;
    if( dfs.getType().isGenericType() && dfs.isReified() )
    {
      int typeVarCount = getTypeVarCountForDFS( dfs );
      for( int i = 0; i < typeVarCount; i++ )
      {
        sb.append( i > 0 ? ", " : "" ).append( LazyTypeResolver.class.getName() ).append( ' ' ).append( AbstractElementTransformer.TYPE_PARAM_PREFIX ).append( i );
        iParam++;
      }
    }
    return iParam;
  }

  public static int getTypeVarCountForDFS( DynamicFunctionSymbol dfs )
  {
    int typeVarCount = 0;
    if( !dfs.isStatic() && dfs.getGosuClass() instanceof IGosuEnhancement )
    {
      typeVarCount = getTypeVarsForEnhancement( dfs );
    }
    if( dfs.getType().isGenericType() )
    {
      typeVarCount += dfs.getType().getGenericTypeVariables().length;
    }
//    else if( dfs.isConstructor() )
//    {
//      IType declaringType = TypeLord.getPureGenericType( dfs.getDeclaringTypeInfo().getOwnersType() );
//      if( declaringType.isGenericType() )
//      {
//        typeVarCount += declaringType.getGenericTypeVariables().length;
//      }
//    }
    return typeVarCount;
  }

  private static int getTypeVarsForEnhancement( DynamicFunctionSymbol dfs )
  {
    IGosuClass aClass = dfs.getGosuClass();
    if( aClass.isParameterizedType() )
    {
      aClass = (IGosuClass)aClass.getGenericType();
    }
    return aClass.getGenericTypeVariables().length;
  }

  void genModifiers( StringBuilder sb, int mod, boolean isDefault, int defModifier )
  {
    if( isDefault )
    {
      sb.append( "default " );
    }

    if( (mod & Modifier.PUBLIC) != 0 )
    {
      sb.append( "public " );
    }
    else if( (mod & Modifier.PROTECTED) != 0 )
    {
      sb.append( "protected " );
    }
    else if( (mod & Modifier.PRIVATE) != 0 )
    {
      sb.append( "private " );
    }
    else
    {
      genModifiers( sb, defModifier, false, 0 );
    }

    // Canonical order
    if( (mod & Modifier.ABSTRACT) != 0 )
    {
      sb.append( "abstract " );
    }
    if( (mod & Modifier.STATIC) != 0 )
    {
      sb.append( "static " );
    }
    if( (mod & Modifier.FINAL) != 0 )
    {
      sb.append( "final " );
    }
    if( (mod & Modifier.TRANSIENT) != 0 )
    {
      sb.append( "transient " );
    }
    if( (mod & Modifier.VOLATILE) != 0 )
    {
      sb.append( "volatile " );
    }
    if( (mod & Modifier.SYNCHRONIZED) != 0 )
    {
      sb.append( "synchronized " );
    }
    if( (mod & Modifier.INTERFACE) != 0 )
    {
      sb.append( "interface " );
    }
  }

  private String getTypeName( IType type )
  {
    if( type instanceof IGosuClass && ((IGosuClass)type).isStructure() )
    {
      return Object.class.getName();
    }

    if( type.isParameterizedType() )
    {
      if( type == TypeLord.getDefaultParameterizedType( type ) )
      {
        return getTypeName( TypeLord.getPureGenericType( type ) );
      }
      StringBuilder sb = new StringBuilder( getTypeName( type.getGenericType() ) ).append( "<");
      IType[] typeParams = type.getTypeParameters();
      for( int i = 0; i < typeParams.length; i++ )
      {
        IType typeParam = typeParams[i];
        sb.append( i > 0 ? ", " : "" ).append( getTypeName( typeParam ) );
      }
      sb.append( ">" );
      return sb.toString();
    }

    if( TypeSystem.isBytecodeType( type ) )
    {
      if( type instanceof CompoundType )
      {
        return getTypeName( ((CompoundType)type).getTypes().iterator().next() );
      }
      return type.getName();
    }

    if( type.isArray() )
    {
      return getTypeName( type.getComponentType() ) + "[]";
    }

    if( type instanceof ITypeVariableType )
    {
      return type.getRelativeName();
    }

    if( type instanceof IFunctionType )
    {
      return getFunctionalInterface( (IFunctionType)type );
    }

    if( type instanceof IMetaType )
    {
      return IType.class.getName();
    }

    return Object.class.getName();
  }

  private String getFunctionalInterface( IFunctionType funcType )
  {
    IType iface = TypeLord.getFunctionalInterface( funcType );
    return getTypeName( iface );
  }
}
