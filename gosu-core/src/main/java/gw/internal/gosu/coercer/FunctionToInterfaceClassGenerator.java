/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.coercer;

import gw.internal.gosu.parser.GosuClassProxyFactory;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.ISource;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

public class FunctionToInterfaceClassGenerator {
  public static final String PROXY_FOR = "ProxyFor_";

  public static synchronized IGosuClass getBlockToInterfaceConversionClass( IType typeToCoerceTo, IType enclosingType ) {
    if( !(enclosingType instanceof IGosuClass) ) {
      // The enclosing type could be a GosuFragment, for example, which isn't compiled
      // normally, so we use a predefined Gosu class designated for top-level interface proxies
      enclosingType = TypeSystem.getByFullName( "gw.lang.TopLevelBlockToInterfaceHolder" );
    }
    typeToCoerceTo = TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( typeToCoerceTo, enclosingType );
    final String relativeNameWithEncodedSuffix = PROXY_FOR + encodeClassName( typeToCoerceTo.getName() );
    return (IGosuClass)((IHasInnerClass)enclosingType).getInnerClass( relativeNameWithEncodedSuffix );
  }

  public static synchronized IGosuClass getBlockToInterfaceConversionClass( String relativeNameWithEncodedSuffix, IType enclosingType ) {
    String name = decodeClassName( relativeNameWithEncodedSuffix.substring( PROXY_FOR.length() ) );
    IType typeToCoerceTo = TypeLord.parseType( name, new TypeVarToTypeMap() );
    return createProxy( typeToCoerceTo, enclosingType, relativeNameWithEncodedSuffix );
  }

  private static String encodeClassName( String name ) {
    StringBuilder sb = new StringBuilder( name );
    replace( sb, "<", "_L_t_" );
    replace( sb, ">", "_G_t_" );
    replace( sb, "[]", "_A_r_" );
    replace( sb, ".", "_D_t_" );
    replace( sb, ",", "_C_m_" );
    replace( sb, " ", "" );
    return sb.toString();
  }

  private static String decodeClassName( String name ) {
    StringBuilder sb = new StringBuilder( name );
    replace( sb, "_L_t_", "<" );
    replace( sb, "_G_t_", ">" );
    replace( sb, "_A_r_", "[]" );
    replace( sb, "_D_t_", "." );
    replace( sb, "_C_m_", "," );
    return sb.toString();
  }

  private static void replace( StringBuilder sb, String find, String replace ) {
    for( int i = sb.indexOf( find ); i >= 0; i = sb.indexOf( find ) ) {
      sb.replace( i, i+find.length(), replace );
    }
  }

  private static IGosuClass createProxy( final IType typeToCoerceTo, IType enclosingType, final String relativeName )
  {
    IModule mod = enclosingType.getTypeLoader().getModule();
    TypeSystem.pushModule( mod );
    try {
      final String namespace = enclosingType.getNamespace();
      IGosuClassInternal gsClass = (IGosuClassInternal)GosuClassTypeLoader.getDefaultClassLoader().makeNewClass(
        new LazyStringSourceFileHandle( enclosingType.getName() + "." + relativeName, TypeLord.getPureGenericType( enclosingType ), new Callable<StringBuilder>() {
          public StringBuilder call() {
            return genProxy( typeToCoerceTo, namespace, relativeName );
          }
        } ) );
      gsClass.setEnclosingType( enclosingType );
      ((IGosuClassInternal)enclosingType).addInnerClass( gsClass );
      gsClass.compileDeclarationsIfNeeded();
      return gsClass;
    }
    finally {
      TypeSystem.popModule( mod );
    }
  }

  private static StringBuilder genProxy( IType type, String namespace, String relativeName )
  {
    //
    // Note we generate from Gosu source instead of generating ASM directly to take advantage
    // of the TypeAsTransformer for the return value of the generated interface method implementation.
    // Since the return type of a given interface can vary if the interface is generic e.g., see Callable<V>
    //

    IType ifaceType = type.isParameterizedType() ? TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( type ): type;
    StringBuilder sb = new StringBuilder()
      .append( "package " ).append( namespace ).append( "\n" )
      .append( "\n" )
      .append( "static class " ).append( relativeName ).append( " implements " ).append( ifaceType.getName() ).append( " {\n" )
      .append( "  final var _block: gw.lang.function.IBlock\n" )
      .append( "  \n" )
      .append( "  construct( brock: gw.lang.function.IBlock ) {\n" )
      .append( "    _block = brock\n" )
      .append( "  }\n" )
      .append( "  \n" )
      .append( "  override function toString() : String {\n" )
      .append( "    return _block.toString()\n" )
      .append( "  }\n" )
      .append( "\n" );
    implementIface( sb, type );
    sb.append( "}" );
    return sb;
  }

  private static void implementIface( StringBuilder sb, IType type ) {
    IMethodInfo mi = getSingleMethod( type );
    IType returnType = TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( mi.getReturnType() );
    mi = mi instanceof IJavaMethodInfo
         ? getSingleMethod( IGosuClassInternal.Util.getGosuClassFrom( type ) )
         : mi;
    if( mi.getName().startsWith( "@" ) ) {
      if( returnType == JavaTypes.pVOID() ) {
        sb.append( "  property set " );
      }
      else {
        sb.append( "  property get " );
      }
      sb.append( mi.getDisplayName().substring( 1 ) ).append( "(" );
    }
    else {
      sb.append( "  function " );
      sb.append( mi.getDisplayName() ).append( "(" );
    }
    IParameterInfo[] params = GosuClassProxyFactory.getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ ) {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( ": " ).append( TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( pi.getFeatureType() ).getName() );
      sb.append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( returnType.getName() ).append( " {\n" )
      .append( returnType == JavaTypes.pVOID()
               ? "    "
               : "    return " )
      .append( "_block.invokeWithArgs( {" );
    for( int i = 0; i < params.length; i++ ) {
      sb.append( ' ' ).append( "p" ).append( i )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( "} )\n" ).append( maybeCastReturnType( returnType ) )
      .append( "  }\n" );
  }

  private static String maybeCastReturnType( IType returnType ) {
    return returnType != JavaTypes.pVOID()
           ? " as " + returnType.getName()
           : "";
  }

  private static class LazyStringSourceFileHandle extends StringSourceFileHandle {
    private Callable<StringBuilder> _sourceGen;
    private String _typeNamespace;

    public LazyStringSourceFileHandle( String fqn, IType enclosingType, Callable<StringBuilder> sourceGen ) {
      super( fqn, null, false, ClassType.Class );
      _sourceGen = sourceGen;
      setParentType( enclosingType.getName() );
      _typeNamespace = enclosingType.getName();
    }

    public String getTypeNamespace() {
      return _typeNamespace;
    }

    @Override
    public ISource getSource() {
      if( getRawSource() == null ) {
        try {
          setRawSource( _sourceGen.call().toString() );
        }
        catch( Exception e ) {
          throw new RuntimeException( e );
        }
      }
      return super.getSource();
    }
  }

  private static IMethodInfo getSingleMethod( IType interfaceType )
  {
    if( interfaceType.isInterface() )
    {
      List<IMethodInfo> list = new ArrayList<IMethodInfo>( interfaceType.getTypeInfo().getMethods() );

      //extract all object methods since they are guaranteed to be implemented
      for( Iterator<? extends IMethodInfo> it = list.iterator(); it.hasNext(); )
      {
        IMethodInfo methodInfo = it.next();
        IParameterInfo[] parameterInfos = methodInfo.getParameters();
        IType[] paramTypes = new IType[parameterInfos.length];
        for( int i = 0; i < parameterInfos.length; i++ )
        {
          paramTypes[i] = parameterInfos[i].getFeatureType();
        }
        if( JavaTypes.OBJECT().getTypeInfo().getMethod( methodInfo.getDisplayName(), paramTypes ) != null ||
            methodInfo.getOwnersType() instanceof IGosuEnhancement)
        {
          it.remove();
        }
        else if( methodInfo.getOwnersType().getName().contains( IGosuObject.class.getName() ) )
        {
          it.remove();
        }
      }

      if( list.size() == 1 )
      {
        return list.get( 0 );
      }
    }
    return null;
  }

}
