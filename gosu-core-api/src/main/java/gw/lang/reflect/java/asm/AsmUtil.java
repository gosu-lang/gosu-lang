/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Types;
import gw.internal.ext.org.objectweb.asm.Type;
import manifold.api.gen.SrcAnnotated;
import manifold.internal.javac.JavacPlugin;

/**
 */
public class AsmUtil {
  public static String makeDotName( String name ) {
    return name.replace( '/', '.' );
  }

  public static String makeBaseName( String name ) {
    if( name.length() == 1 ) {
      return name;
    }
    while( name.charAt( 0 ) == '[' ) {
      name = name.substring( 1 );
    }
    if( name.endsWith( ";" ) ) {
      name = name.substring( 1, name.length() - 1 );
    }
    else {
      AsmType asmType = AsmPrimitiveType.findPrimitive( name );
      if( asmType != null ) {
        name = asmType.getName();
      }
    }
    return makeDotName( name );
  }

  public static AsmType makeType( Type type ) {
    if( type.getSort() >= Type.ARRAY ) {
      return makeType( type.getInternalName() );
    }
    else {
      return AsmPrimitiveType.findPrimitive( type.getClassName() );
    }
  }
  public static AsmType makeType( String name ) {
    if( name.length() == 1 ) {
      AsmPrimitiveType primitive = AsmPrimitiveType.findPrimitive( name );
      if( primitive != null ) {
        return primitive;
      }
    }

    int iDims = 0;
    while( name.charAt( 0 ) == '[' ) {
      iDims++;
      name = name.substring( 1 );
    }
    if( name.endsWith( ";" ) ) {
      name = name.substring( 1, name.length() - 1 );
    }
    else {
      AsmType asmType = AsmPrimitiveType.findPrimitive( name );
      if( asmType != null ) {
        name = asmType.getName();
      }
    }
    return new AsmType( makeDotName( name ), iDims );
  }

  public static AsmType makeErasedType( com.sun.tools.javac.code.Type type )
  {
    return makeType( type, true );
  }
  public static AsmType makeType( com.sun.tools.javac.code.Type type )
  {
    return makeType( type, false );
  }

  private static AsmType makeType( com.sun.tools.javac.code.Type type, boolean erased )
  {
    if( erased )
    {
      type = Types.instance( JavacPlugin.instance().getContext() ).erasure( type );
    }

    switch( type.getKind() )
    {
      case WILDCARD:
        com.sun.tools.javac.code.Type.WildcardType wildcard = (com.sun.tools.javac.code.Type.WildcardType)type;
        com.sun.tools.javac.code.Type bound = wildcard.isUnbound()
                                              ? Types.instance( JavacPlugin.instance().getContext() ).erasure( wildcard )
                                              : wildcard.isExtendsBound()
                                                ? wildcard.getExtendsBound()
                                                : wildcard.isSuperBound()
                                                  ? wildcard.getSuperBound()
                                                  : Types.instance( JavacPlugin.instance().getContext() ).erasure( wildcard );
        AsmType boundType = AsmUtil.makeType( bound, false );
        return new AsmWildcardType( boundType, !wildcard.isSuperBound() );

      case TYPEVAR:
        AsmType typeVar = new AsmType( type.tsym.name.toString() );
        typeVar.setTypeVariable();
        typeVar.setFunctionTypeVariable( type.tsym.owner instanceof Symbol.MethodSymbol );
        return typeVar;
    }

    int dims = 0;
    com.sun.tools.javac.code.Type compType = type;
    while( compType instanceof com.sun.tools.javac.code.Type.ArrayType )
    {
      dims++;
      compType =((com.sun.tools.javac.code.Type.ArrayType)type).getComponentType();
    }

    AsmType asmType;
    if( dims > 0 )
    {
      asmType = makeType( compType );
      for( int i = 0; i < dims; i++ )
      {
        asmType.incArrayDims();
      }
    }
    else
    {
      asmType = new AsmType( type.tsym.getQualifiedName().toString() );
    }

    if( type.isParameterized() )
    {
      for( com.sun.tools.javac.code.Type param: type.getTypeArguments() )
      {
        asmType.addTypeParameter( makeType( param, false ) );
      }
    }

    return asmType;
  }

  public static AsmInnerClassType makeInnerType( Symbol.ClassSymbol innerClassSym )
  {
    int modifiers = (int)SrcAnnotated.modifiersFrom( innerClassSym.getModifiers() );
    AsmInnerClassType t = new AsmInnerClassType( innerClassSym.getQualifiedName().toString(), modifiers );

    if( innerClassSym.type.isParameterized() )
    {
      for( com.sun.tools.javac.code.Type param: innerClassSym.type.getParameterTypes() )
      {
        t.addTypeParameter( makeType( param ) );
      }
    }

    return t;
  }

  public static AsmType makeNonPrimitiveType(String name) {
    int iDims = 0;
    while( name.charAt( 0 ) == '[' ) {
      iDims++;
      name = name.substring( 1 );
    }
    if( name.endsWith( ";" ) ) {
      name = name.substring( 1, name.length() - 1 );
    }
    return new AsmType( makeDotName( name ), iDims );
  }

  public static AsmType makeTypeVariable( String tv ) {
    return makeTypeVariable( tv, false );
  }
  public static AsmType makeTypeVariable( String tv, boolean bFunctionTypeVar ) {
    AsmType typeVarType = new AsmType( tv );
    typeVarType.setTypeVariable();
    typeVarType.setFunctionTypeVariable( bFunctionTypeVar );
    return typeVarType;
  }
}
