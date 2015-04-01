/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.Type;

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
      return AsmPrimitiveType.findPrimitive( name );
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
    AsmType typeVarType = new AsmType( tv );
    typeVarType.setTypeVariable();
    return typeVarType;
  }
}
