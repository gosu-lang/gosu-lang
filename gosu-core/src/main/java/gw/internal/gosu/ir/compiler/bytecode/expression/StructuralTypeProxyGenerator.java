/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.parser.GosuClassProxyFactory;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.parser.ISource;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.util.concurrent.Callable;

/**
 */
public class StructuralTypeProxyGenerator {
  private final boolean _bStatic;
  private String _type;

  private StructuralTypeProxyGenerator( boolean bStatic ) {
    _bStatic = bStatic;
  }

  public static Class makeProxy( Class<?> iface, Class<?> rootClass, final String name, final boolean bStaticImpl ) {

    final IType type = TypeLord.getPureGenericType( TypeSystem.get( rootClass ) );
    final IType ifaceType = TypeLord.getPureGenericType( TypeSystem.get( iface ) );
    final IModule module = ifaceType.getTypeLoader().getModule();
    GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader( module );
    final StructuralTypeProxyGenerator gen = new StructuralTypeProxyGenerator( bStaticImpl );
    IGosuClass gsProxy = loader.makeNewClass(
      new LazyStringSourceFileHandle( gen.getNamespace( ifaceType ), name, new Callable<StringBuilder>() {
        public StringBuilder call() {
          TypeSystem.pushModule( module );
          try {
            return gen.generateProxy( ifaceType, type, name );
          }
          finally {
            TypeSystem.popModule( module );
          }
        }
      } ) );
    return gsProxy.getBackingClass();
  }

  private static class LazyStringSourceFileHandle extends StringSourceFileHandle {
    private Callable<StringBuilder> _sourceGen;
    private String _namespace;

    public LazyStringSourceFileHandle( String nspace, String fqn, Callable<StringBuilder> sourceGen ) {
      super( fqn, null, false, ClassType.Class );
      _namespace = nspace;
      _sourceGen = sourceGen;
    }

    public String getTypeNamespace() {
      return _namespace;
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

  private StringBuilder generateProxy( IType ifaceType, IType type, String name ) {
    _type = type.getName();
    return new StringBuilder()
      .append( "package " ).append( getNamespace( ifaceType ) ).append( "\n" )
      .append( "\n" )
      .append( "class " ).append( name ).append( " implements " ).append( ifaceType.getName() ).append( " {\n" )
      .append( "  final var _root: " ).append( _bStatic ? "Type<" + type.getName() + ">" : type.getName() ).append( "\n" )
      .append( "  \n" )
      .append( "  construct( root: " ).append( _bStatic ? "Type<" + type.getName() + ">" : type.getName() ).append( " ) {\n" )
      .append( "    _root = root\n" )
      .append( "  }\n" )
      .append( "  \n" )
      .append( implementIface( ifaceType, type ) )
      .append( "}" );
  }

  private String getNamespace( IType ifaceType ) {
    String nspace = TypeLord.getOuterMostEnclosingClass( ifaceType ).getNamespace();
    if( nspace.startsWith( "java." ) || nspace.startsWith( "javax." ) )
    {
      nspace = "not" + nspace;
    }
    return nspace;
  }

  private String implementIface( IType ifaceType, IType rootType ) {
    StringBuilder sb = new StringBuilder();
    ITypeInfo ti = ifaceType.getTypeInfo();
    // Interface properties
    for( Object o : ti.getProperties() ) {
      IPropertyInfo pi = (IPropertyInfo)o;
      genInterfacePropertyDecl( sb, pi, rootType );
    }

    // Interface methods
    for( Object o : ti.getMethods() ) {
      IMethodInfo mi = (IMethodInfo)o;
      genInterfaceMethodDecl( sb, mi, rootType );
    }

    return sb.toString();
  }

  // Note we need to include type variables for generic methods so that the bytecode signatures, which include type vars as params, will be compatible.
  // All other type variable references, including the method's type vars, are erased to their bounding types.
  // e.g.,
  // structure FooBar<T extends CharSequence> {
  //   function foo<E>( e: E ) : T
  // }
  // class MyClass<T extends CharSequence>  { // structurally implemetns FooBar<T>
  //   function foo<E>( e: E ) : T {
  //      return x
  //   }
  // }
  //
  // We generate the followin proxy for MyClass, notice we preserve the E type var for the method:
  //
  // class MyClass_structuralproxy_Foobar implements FooBar {
  //   function foo<E>( e: Object ) : CharSequence {
  //     return _root.foo( e )
  //   }
  // }
  private void genInterfaceMethodDecl( StringBuilder sb, IMethodInfo mi, IType rootType ) {
    if( mi.isDefaultImpl() || mi.isStatic() ) {
      return;
    }
    if( mi.getOwnersType() instanceof IGosuEnhancement ) {
      return;
    }
    if( mi.getDisplayName().startsWith( "@" ) ) { // property
      return;
    }
    if( GosuClassProxyFactory.isObjectMethod( mi ) ) {
      return;
    }
    if( mi.getOwnersType() == JavaTypes.IGOSU_OBJECT().getAdapterClass() ) {
      return;
    }
    sb.append( "  function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = mi.getParameters();
    for( int i = 0; i < params.length; i++ ) {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( ": " ).append( TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( pi.getFeatureType() ).getName() );
      sb.append( i < params.length - 1 ? ',' : ' ' );
    }
    IType returnType = TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( mi.getReturnType() );
    sb.append( ") : " ).append( returnType.getName() ).append( " {\n" )
      .append( returnType == JavaTypes.pVOID()
               ? "    "
               : "    return " )
      //## todo: maybe we need to explicitly parameterize if the method is generic for some cases?
      .append( _bStatic ? _type : "_root" ).append( "." ).append( mi.getDisplayName() ).append( "(" );
    for( int i = 0; i < params.length; i++ ) {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( maybeCastParamType( mi, TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( pi.getFeatureType() ), rootType, i ) )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ")" ).append( maybeCastReturnType( mi, returnType, rootType ) )
      .append( "  }\n" );
  }

  private String maybeCastReturnType( IMethodInfo mi, IType returnType, IType rootType ) {
    //## todo:
    return returnType != JavaTypes.pVOID()
           ? " as " + returnType.getName()
           : "";
  }

  private String maybeCastParamType( IMethodInfo ifaceMethod, IType paramType, IType rootType, int iParam ) {
    //## todo: find the parameter type in the corresponding method in rootType, and then cast to that type *only* if necessary
    return "";
  }

  private String maybeCastPropertyAssignment( IPropertyInfo pi, IType rootType ) {
    //## todo: find the corresponding property type in rootType, and then cast to that type *only* if necessary
    return " as " + TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( pi.getFeatureType() ).getName() + "\n";
  }

  private void genInterfacePropertyDecl( StringBuilder sb, IPropertyInfo pi, IType rootType ) {
    if( pi.isDefaultImpl() || pi.isStatic() ) {
      return;
    }
    if( pi.isStatic() ) {
      return;
    }
    if( !pi.isReadable() ) {
      return;
    }
    if( pi.getOwnersType() instanceof IGosuEnhancement ) {
      return;
    }
    if( pi.getOwnersType() == JavaTypes.IGOSU_OBJECT().getAdapterClass() ) {
      return;
    }

    IType ifacePropertyType = TypeLord.getPureGenericType( TypeLord.getDefaultParameterizedType( pi.getFeatureType() ) );
    if( pi.getDescription() != null ) {
      sb.append( "\n/** " ).append( pi.getDescription() ).append( " */\n" );
    }
    ITypeInfo rootTypeInfo = rootType.getTypeInfo();
    // Have to handle private for inner class case e.g., a private field on the inner class implements a property on a structure
    boolean bPrivate = rootTypeInfo instanceof IRelativeTypeInfo && ((IRelativeTypeInfo) rootTypeInfo).getProperty( rootType, pi.getName() ).isPrivate();
    sb.append( "  property get " ).append( pi.getName() ).append( "() : " ).append( ifacePropertyType.getName() ).append( " {\n" );
    if( bPrivate ) {
      sb.append( "    return _root[\"" ).append( pi.getName() ).append( "\"] as " ).append( ifacePropertyType.getName() ).append( "\n" );
    }
    else {
      sb.append( "    return " ).append( _bStatic ? _type : "_root" ).append( "." ).append( pi.getName() ).append( " as " ).append( ifacePropertyType.getName() ).append( "\n" );
    }
    sb.append( "  }\n" );
    if( pi.isWritable( pi.getOwnersType() ) ) {
      sb.append( "  property set " ).append( pi.getName() ).append( "( value: " ).append( ifacePropertyType.getName() ).append( " ) {\n" );
      if( bPrivate ) {
        sb.append( "    _root[\"" ).append( pi.getName() ).append( "\"] = value" ).append( maybeCastPropertyAssignment( pi, rootType ) );
      }
      else {
        sb.append( "    " ).append( _bStatic ? _type : "_root" ).append( "." ).append( pi.getName() ).append( " = value" ).append( maybeCastPropertyAssignment( pi, rootType ) );
      }
      sb.append( "  }\n" );
    }
  }
}
