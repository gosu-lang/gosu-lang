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
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.LazyTypeResolver;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 */
public class JavaStubGenerator
{
  private static final JavaStubGenerator INSTANCE = new JavaStubGenerator();
  private static final int INDENT = 2;

  public static JavaStubGenerator instance()
  {
    return INSTANCE;
  }

  private JavaStubGenerator()
  {
  }

  public String genStub( IType type )
  {
    StringBuilder sb = new StringBuilder();
    genPackage( type, sb );
    genType( type, sb, 0 );
    return sb.toString();
  }

  private void genType( IType type, StringBuilder sb, int indent )
  {
    if( type instanceof IGosuClass )
    {
      genClass( (IGosuClass)type, sb, indent );
    }
    else if( type instanceof IJavaType )
    {
      throw new IllegalStateException( "Attempted to generate a stub for a Java type, this means something is wrong: " + type.getName() );
    }
    else
    {
      genTypeInfo( type, sb, indent );
    }
  }

  private void genClass( IGosuClass type, StringBuilder sb, int indent )
  {
    if( type.isEnum() )
    {
      genEnum( type, sb, indent );
    }
    else if( type.isAnnotation() )
    {
      genAnnotation( type, sb, indent );
    }
    else
    {
      genClassOrInterface( type, sb, indent );
    }
  }

  private void genPackage( IType type, StringBuilder sb )
  {
    sb.append( "/* Generated Stub from " ).append( ((ITypeRef)type)._getClassOfRef().getSimpleName() ).append( " for Java Interop */\n" )
      .append( "package " ).append( type.getNamespace() ).append( ";\n\n" )
      .append( "import gw.lang.reflect.*;\n\n" );
  }

  private void genAnnotation( IGosuClass type, StringBuilder sb, int indent )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, type.getModifiers() & ~(Modifier.FINAL | Modifier.ABSTRACT), false, Modifier.PUBLIC );
    sb.append( "@interface " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genClassFeatures( sb, gsClass, indent + INDENT );

    indent( sb, indent );
    sb.append( "}" );
  }

  private void genEnum( IGosuClass type, StringBuilder sb, int indent )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, type.getModifiers() & ~Modifier.FINAL, false, Modifier.PUBLIC );
    sb.append( "enum " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genEnumConstants( sb, gsClass, indent );
    genClassFeatures( sb, gsClass, indent );

    indent( sb, indent );
    sb.append( "}" );
  }

  private void genEnumConstants( StringBuilder sb, IGosuClassInternal gsClass, int indent )
  {
    List<String> enumConstants = gsClass.getEnumConstants();
    for( int i = 0; i < enumConstants.size(); i++ )
    {
      String c = enumConstants.get( i );
      sb.append( i > 0 ? ",\n" : "" )
        .append( indent( sb, indent ) )
        .append( c )
        .append( i == enumConstants.size()-1 ? ";\n\n" : "" );
    }
  }

  private void genClassOrInterface( IGosuClass type, StringBuilder sb, int indent )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)type;

    gsClass.compileDeclarationsIfNeeded();

    genAnnotations( sb, type.getTypeInfo().getDeclaredAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, type.getModifiers(), false, Modifier.PUBLIC );
    sb.append( type.isInterface() ? "interface " : "class " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassExtends( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    genClassFeatures( sb, gsClass, indent + INDENT );

    indent( sb, indent );
    sb.append( "}\n" );
  }

  private void genClassFeatures( StringBuilder sb, IGosuClassInternal gsClass, int indent )
  {
    GosuClassParseInfo parseInfo = gsClass.getParseInfo();

    genFields( sb, gsClass, parseInfo, indent );
    genConstructors( sb, gsClass, indent );
    genProperties( sb, gsClass, indent );
    genMethods( sb, gsClass, indent );
    genInnerClasses( sb, gsClass, indent );
  }

  private String genClassImplements( IType type )
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

  private boolean shouldImplement( IType iface, IType type )
  {
    return !(
      IGosuClass.ProxyUtil.isProxy( iface ) ||
      (type instanceof IGosuClass) && ((IGosuClass)type).isAnnotation() && iface == JavaTypes.ANNOTATION()
    );
  }

  private String genClassExtends( IType type )
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

  private void genInnerClasses( StringBuilder sb, IHasInnerClass type, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// inner classes //\n" );
    for( IType innerClass : type.getInnerClasses() )
    {
      genType( innerClass, sb, indent );
    }
  }

  private void genFields( StringBuilder sb, IGosuClassInternal gsClass, GosuClassParseInfo parseInfo, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// fields //\n" );

    Collection<VarStatement> fields = parseInfo.getStaticFields().values();
    genFields( sb, gsClass, fields, indent );

    fields = parseInfo.getMemberFields().values();
    genFields( sb, gsClass, fields, indent );
  }
  private void genFields( StringBuilder sb, IGosuClassInternal gsClass, Collection<VarStatement> fields, int indent )
  {
    for( VarStatement field : fields )
    {
//      if( field.isPrivate() && !field.isFinal() )
//      {
//        continue;
//      }

      if( field.getType() == JavaTypes.pVOID() )
      {
        // skip type-inferred private fields (remember, this is a stub)
        continue;
      }

      List<? extends IAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( field.getAnnotations(), gsClass.getTypeInfo() );
      genAnnotations( sb, gosuAnnotationInfos, indent );
      indent( sb, indent );
      genModifiers( sb, field.getModifiers(), false, Modifier.PRIVATE );
      sb.append( getTypeName( field.getType() ) ).append( ' ' ).append( field.getIdentifierName() );
      if( field.isFinal() )
      {
        sb.append( " = " ).append( getEmptyValueForType( field.getType() ) );
      }
      sb.append( ";\n" );
    }
  }

  private String indent( StringBuilder sb, int indent )
  {
    for( int i = 0; i < indent; i++ )
    {
      sb.append( ' ' );
    }
    return "";
  }

  private void genConstructors( StringBuilder sb, IGosuClassInternal gsClass, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// constructors //\n" );

    Collection<DynamicFunctionSymbol> constructors = gsClass.getConstructorFunctions();
    for( DynamicFunctionSymbol constructor : constructors )
    {
      // Generate private constructors for tooling and better error messages
//      if( constructor.isPrivate() )
//      {
//        continue;
//      }

      if( gsClass.isGenericType() )
      {
        genConstructor( sb, gsClass, constructor, true, indent );
      }
      genConstructor( sb, gsClass, constructor, false, indent );
    }
  }

  private void genConstructor( StringBuilder sb, IGosuClassInternal gsClass, DynamicFunctionSymbol constructor, boolean hideReified, int indent )
  {
    List<? extends IAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( constructor.getAnnotations(), gsClass.getTypeInfo() );
    genAnnotations( sb, gosuAnnotationInfos, indent );
    indent( sb, indent );
    genModifiers( sb, constructor.getModifiers(), false, Modifier.PUBLIC );
    sb.append( SignatureUtil.getSimpleName( gsClass.getName() ) ).append( "(" );
    genParameters( sb, constructor, true, hideReified );
    sb.append( ") {}\n" );
  }

  private void genProperties( StringBuilder sb, IGosuClassInternal gsClass, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// properties //\n" );

    genProperties( sb, gsClass, gsClass.getStaticProperties(), indent );
    genProperties( sb, gsClass, gsClass.getMemberProperties(), indent );
  }

  private void genProperties( StringBuilder sb, IGosuClassInternal gsClass, List<DynamicPropertySymbol> properties, int indent )
  {
    for( int i = 0; i < properties.size(); i++ )
    {
      DynamicPropertySymbol dps = properties.get( i );

      if( dps.isReadable() )
      {
        genMethod( sb, gsClass, dps.getGetterDfs(), dps.getType() == JavaTypes.pBOOLEAN() ? "is" : "get" + dps.getDisplayName(), indent );
      }
      if( dps.isWritable() )
      {
        genMethod( sb, gsClass, dps.getSetterDfs(), "set" + dps.getDisplayName(), indent );
      }
    }
  }

  private void genMethods( StringBuilder sb, IGosuClassInternal gsClass, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// methods //\n" );

    genMethods( sb, gsClass, gsClass.getStaticFunctions(), indent );
    genMethods( sb, gsClass, gsClass.getMemberFunctions(), indent );
  }

  private void genMethods( StringBuilder sb, IGosuClassInternal gsClass, Collection<DynamicFunctionSymbol> methods, int indent )
  {
    for( DynamicFunctionSymbol method : methods )
    {
      genMethod( sb, gsClass, method, indent );
    }
  }

  private void genMethod( StringBuilder sb, IGosuClassInternal gsClass, DynamicFunctionSymbol method, int indent )
  {
    genMethod( sb, gsClass, method, null, indent );
  }

  private void genMethod( StringBuilder sb, IGosuClassInternal gsClass, DynamicFunctionSymbol method, String name, int indent )
  {
    // Generating private methods for tooling to recognize its existence e.g., more informative error messages
//    if( method.isPrivate() ) // || method.isReified() )
//    {
//      return;
//    }

    if( method.getDisplayName().startsWith( "@" ) && name == null )
    {
      return;
    }

    if( isBuiltinStaticEnumMethod( method ) )
    {
      return;
    }

    List<GosuAnnotationInfo> gosuAnnotationInfos = AbstractElementTransformer.makeAnnotationInfos( method.getAnnotations(), gsClass.getTypeInfo() );
    genAnnotations( sb, gosuAnnotationInfos, indent );
    indent( sb, indent );
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

  private void genTypeInfo( IType type, StringBuilder sb, int indent )
  {
    ITypeInfo ti = type.getTypeInfo();

    genAnnotations( sb, ti.getDeclaredAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, type.getModifiers(), false, Modifier.PUBLIC );
    sb.append( "class " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( getTypeVariables( type ) )
      .append( genClassExtends( type ) )
      .append( genClassImplements( type ) )
      .append( " {\n" );

    sb.append( indent( sb, indent + INDENT ) ).append( "private static final IType GOSU_TYPE = TypeSystem.getByFullNameIfValidNoJava(" ).append( enquote( sb, type.getName() ) ).append( ");\n" );
    sb.append( indent( sb, indent + INDENT ) ).append( "private final Object _delegate;\n" );

    genTypeInfoFeatures( sb, type, ti, indent + INDENT );

    genWrappingMethod( sb, type, indent + INDENT );

    sb.append( indent( sb, indent) ).append( "}\n" );
  }

  private void genWrappingMethod( StringBuilder sb, IType type, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// wrapping method //\n" );
    sb.append( indent( sb, indent ) ).append( "public static " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( " _wrap_(Object obj) {\n" )
      .append( indent( sb, INDENT+indent ) ).append( "return new " ).append( SignatureUtil.getSimpleName( type.getName() ) ).append( "(GOSU_TYPE, obj);\n" )
      .append( indent( sb, indent ) ).append( "}\n" );
    indent( sb, indent );
    genModifiers( sb, 0, false, Modifier.PRIVATE );
    sb.append( SignatureUtil.getSimpleName( type.getName() ) ).append( "(IType discriminator, Object obj) {\n" )
      .append( indent( sb, INDENT+indent ) ).append( "_delegate = obj;\n" )
      .append( indent( sb, indent ) ).append( "}\n\n" );
  }

  private void genTypeInfoFeatures( StringBuilder sb, IType type, ITypeInfo ti, int indent )
  {
    sb.append( "\n" ).append( indent( sb, indent )).append( "// static properties //\n" );
    ti.getProperties().stream().filter( IAttributedFeatureInfo::isStatic ).forEach( pi -> genPropertyInfo( sb, pi, indent ) );
    sb.append( "\n" ).append( indent( sb, indent )).append( "// static methods //\n" );
    ti.getMethods().stream().filter( IAttributedFeatureInfo::isStatic ).forEach( mi -> genMethodInfo( sb, mi, indent ) );

    sb.append( "\n" ).append( indent( sb, indent )).append( "// constructors //\n" );
    ti.getConstructors().forEach( ci -> genConstructorInfo( sb, ci, type, indent + INDENT ) );

    sb.append( "\n" ).append( indent( sb, indent )).append( "// instance properties //\n" );
    ti.getProperties().stream().filter( pi -> !pi.isStatic() ).forEach( pi -> genPropertyInfo( sb, pi, indent ) );
    sb.append( "\n" ).append( indent( sb, indent )).append( "// instance methods //\n" );
    ti.getMethods().stream().filter( mi -> !mi.isStatic() ).forEach( mi -> genMethodInfo( sb, mi, indent ) );

    if( type instanceof IHasInnerClass )
    {
      genInnerClasses( sb, (IHasInnerClass)type, indent );
    }
  }

  private void genMethodInfo( StringBuilder sb, IMethodInfo mi, int indent )
  {
    genAnnotations( sb, mi.getAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, getModifiers( mi ), false, Modifier.PUBLIC );
    sb.append( mi.getReturnType().getName() ).append( ' ' ).append( mi.getDisplayName() ).append( "(" ).append( genParameters( sb, mi ) ).append( ") {\n" );
    if( mi.isStatic() )
    {
      sb.append( indent( sb, INDENT+indent ) ).append( "return " ).append( "(" ).append( mi.getReturnType().getName() ).append( ")ReflectUtil.invokeStaticMethod(" ).append( enquote( sb, mi.getOwnersType().getName() ) ).append( ", " ).append( enquote( sb, mi.getDisplayName() ) ).append( mi.getParameters().length > 0 ? ", " : "" ).append( genArgs( sb, mi ) ).append( ");\n" );
    }
    else
    {
      sb.append( indent( sb, INDENT+indent ) ).append( "return " ).append( "(" ).append( mi.getReturnType().getName() ).append( ")ReflectUtil.invokeMethod(_delegate, " ).append( enquote( sb, mi.getDisplayName() ) ).append( mi.getParameters().length > 0 ? ", " : "" ).append( genArgs( sb, mi ) ).append( ");\n" );
    }
    sb.append( indent( sb, indent ) ).append( "}\n\n" );
  }

  private String enquote( StringBuilder sb, String text )
  {
    sb.append( '"' ).append( text ).append( '"' );
    return "";
  }

  private void genPropertyInfo( StringBuilder sb, IPropertyInfo pi, int indent )
  {
    if( pi.isReadable() )
    {
      genAnnotations( sb, pi.getAnnotations(), indent );
      indent( sb, indent );
      genModifiers( sb, getModifiers( pi ), false, Modifier.PUBLIC );
      sb.append( pi.getFeatureType().getName() ).append( pi.getFeatureType() == JavaTypes.pBOOLEAN() ? " is" : " get" ).append( pi.getDisplayName() ).append( "() {\n" );
      if( pi.isStatic() )
      {
        sb.append( indent( sb, INDENT+indent ) ).append( "return " ).append( "(" ).append( pi.getFeatureType().getName() ).append( ")ReflectUtil.getStaticProperty(GOSU_TYPE, " ).append( enquote( sb, pi.getDisplayName() ) ).append( ");\n" );
      }
      else
      {
        sb.append( indent( sb, INDENT+indent ) ).append( "return " ).append( "(" ).append( pi.getFeatureType().getName() ).append( ")ReflectUtil.getProperty(_delegate, " ).append( enquote( sb, pi.getDisplayName() ) ).append( ");\n" );
      }
      sb.append( indent( sb, indent ) ).append( "}\n\n" );
    }
    if( pi.isWritable() )
    {
      genAnnotations( sb, pi.getAnnotations(), indent );
      indent( sb, indent );
      genModifiers( sb, getModifiers( pi ), false, Modifier.PUBLIC );
      sb.append( "void set" ).append( pi.getDisplayName() ).append( "(" ).append( pi.getFeatureType().getName() ).append( " value) { \n" );
      if( pi.isStatic() )
      {
        sb.append( indent( sb, INDENT+indent ) ).append( "ReflectUtil.setStaticProperty(GOSU_TYPE, " ).append( enquote( sb, pi.getDisplayName() ) ).append( ", value);\n" );
      }
      else
      {
        sb.append( indent( sb, INDENT+indent ) ).append( "ReflectUtil.setProperty(_delegate, " ).append( enquote( sb, pi.getDisplayName() ) ).append( ", value);\n" );
      }
      sb.append( indent( sb, indent ) ).append( "}\n\n" );
    }
  }

  private void genConstructorInfo( StringBuilder sb, IConstructorInfo ci, IType type, int indent )
  {
    genAnnotations( sb, ci.getAnnotations(), indent );
    indent( sb, indent );
    genModifiers( sb, getModifiers( ci ), false, Modifier.PUBLIC );
    sb.append( SignatureUtil.getSimpleName( type.getName() ) ).append( "(" ).append( genParameters( sb, ci ) ).append( ") {\n" )
      .append( indent( sb, INDENT+indent ) ).append( "_delegate = ReflectUtil.construct(" ).append( genArgs( sb, ci ) ).append( ")\n" )
      .append( indent( sb, indent ) ).append( "}\n\n" );
  }

  private String genArgs( StringBuilder sb, IHasParameterInfos ci )
  {
    IParameterInfo[] parameters = ci.getParameters();
    for( int i = 0; i < parameters.length; i++ )
    {
      IParameterInfo param = parameters[i];
      sb.append( i > 0 ? ", " : "" ).append( param.getDisplayName() );
    }
    return "";
  }

  private String genParameters( StringBuilder sb, IHasParameterInfos ci )
  {
    IParameterInfo[] parameters = ci.getParameters();
    for( int i = 0; i < parameters.length; i++ )
    {
      IParameterInfo param = parameters[i];
      sb.append( i > 0 ? ", " : "" ).append( getTypeName( param.getFeatureType() ) ).append( ' ' ).append( param.getDisplayName() );
    }
    return "";
  }

  private int getModifiers( IAttributedFeatureInfo fi )
  {
    int modifiers = 0;
    if( fi.isStatic() )
    {
      modifiers = Modifier.STATIC;
    }

    if( fi.isPublic() )
    {
      modifiers |= Modifier.PUBLIC;
    }
    else if( fi.isProtected() )
    {
      modifiers |= Modifier.PROTECTED;
    }
    else if( fi.isPrivate() )
    {
      modifiers |= Modifier.PRIVATE;
    }
    return modifiers;
  }

  private boolean isBuiltinStaticEnumMethod( DynamicFunctionSymbol method )
  {
    return method.isStatic() && method.getDeclaringTypeInfo().getOwnersType().isEnum() &&
           (method.getDisplayName().equals( "values" ) || method.getDisplayName().equals( "valueOf"));
  }

  private void genAnnotations( StringBuilder sb, List<? extends IAnnotationInfo> annotations, int indent )
  {
    for( IAnnotationInfo ai : annotations )
    {
      IType annoType = ai.getType();
      if( !JavaTypes.ANNOTATION().isAssignableFrom( annoType ) )
      {
        continue;
      }
      indent( sb, indent );
      sb.append( '@' ).append( ai.getType().getName() ).append( '(' );
      MethodList methods = annoType.getTypeInfo().getMethods();
      int index = 0;
      for( int i = 0; i < methods.size(); i++ )
      {
        IMethodInfo mi = methods.get( i );
        if( mi.getOwnersType() == annoType )
        {
          String fieldName = mi.getDisplayName();
          fieldName = fieldName == null || fieldName.isEmpty() ? "value" : fieldName;
          sb.append( index++ > 0 ? ", " : "" ).append( fieldName ).append( '=' ).append( genFieldValue( ai, mi.getReturnType(), fieldName ) );
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
    sb.append( " return " ).append( getEmptyValueForType( returnType ) ).append( "; }\n" );
  }

  private String getEmptyValueForType( IType returnType )
  {
    return !returnType.isPrimitive() ? "null" : makeDefaultPrimitiveValue( returnType );
  }

  private String makeDefaultPrimitiveValue( IType returnType )
  {
    return genCompileTimeConstantExpression( returnType, CommonServices.getCoercionManager().convertNullAsPrimitive( returnType, false ) );
  }

  private void genParameters( StringBuilder sb, DynamicFunctionSymbol dfs )
  {
    genParameters( sb, dfs, false, false );
  }
  private void genParameters( StringBuilder sb, DynamicFunctionSymbol dfs, boolean isConstructor, boolean hideReified )
  {
    List<ISymbol> parameters = dfs.getArgs();
    int iParam = hideReified ? 0 : addReifiedTypeParamaters( sb, dfs, isConstructor );
    for( int i = 0; i < parameters.size(); i++ )
    {
      ISymbol param = parameters.get( i );
      sb.append( iParam > 0 ? ", " : "" ).append( getTypeName( param.getType() ) ).append( ' ' ).append( param.getDisplayName() );
      iParam++;
    }
  }

  private int addReifiedTypeParamaters( StringBuilder sb, DynamicFunctionSymbol dfs, boolean isConstructor )
  {
    int iParam = 0;
    if( dfs.isReified() || ((dfs.isConstructor() || isConstructor) && dfs.getDeclaringTypeInfo().getOwnersType().isGenericType()) )
    {
      int typeVarCount = getTypeVarCountForDFS( dfs, isConstructor );
      for( int i = 0; i < typeVarCount; i++ )
      {
        sb.append( i > 0 ? ", " : "" ).append( LazyTypeResolver.class.getName() ).append( ' ' ).append( AbstractElementTransformer.TYPE_PARAM_PREFIX ).append( i );
        iParam++;
      }
    }
    return iParam;
  }

  public static int getTypeVarCountForDFS( DynamicFunctionSymbol dfs, boolean isConstructor )
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
    else if( dfs.isConstructor() || isConstructor )
    {
      IType declaringType = TypeLord.getPureGenericType( dfs.getDeclaringTypeInfo().getOwnersType() );
      if( declaringType.isGenericType() )
      {
        typeVarCount += declaringType.getGenericTypeVariables().length;
      }
    }
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
    IType iface = IRTypeResolver.getDescriptor( funcType ).getType();
    iface = TypeLord.getPureGenericType( iface );
    if( iface.isGenericType() )
    {
      IGenericTypeVariable[] gtvs = iface.getGenericTypeVariables();
      IType[] typeParams = new IType[gtvs.length];
      IType returnType = funcType.getReturnType();
      boolean hasReturn = returnType != JavaTypes.pVOID();
      for( int i = 0; i < gtvs.length; i++ )
      {
        if( i == 0 && hasReturn )
        {
          if( returnType.isPrimitive() )
          {
            returnType = TypeSystem.getBoxType( returnType );
          }
          typeParams[i] = returnType;
        }
        else if( funcType.getParameterTypes().length > 0 )
        {
          IType paramType = funcType.getParameterTypes()[i - (hasReturn ? 1 : 0)];
          if( paramType.isPrimitive() )
          {
            paramType = TypeSystem.getBoxType( paramType );
          }
          typeParams[i] = paramType;
        }
        else if( i == 0 )
        {
          iface = TypeLord.getDefaultParameterizedType( iface );
        }
      }
      iface = iface.getParameterizedType( typeParams );
    }
    return getTypeName( iface );
  }
}
