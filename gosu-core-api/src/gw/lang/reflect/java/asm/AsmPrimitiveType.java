/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class AsmPrimitiveType extends AsmType {
  public static final AsmPrimitiveType BYTE;
  public static final AsmPrimitiveType SHORT;
  public static final AsmPrimitiveType CHAR;
  public static final AsmPrimitiveType INT;
  public static final AsmPrimitiveType LONG;
  public static final AsmPrimitiveType FLOAT;
  public static final AsmPrimitiveType DOUBLE;
  public static final AsmPrimitiveType BOOLEAN;
  public static final AsmPrimitiveType VOID;

  private static final Map<String, AsmPrimitiveType> PRIMITIVES = new HashMap<String, AsmPrimitiveType>();
  static {
    PRIMITIVES.put( "byte", BYTE = new AsmPrimitiveType( "byte" ) );
    PRIMITIVES.put( "short", SHORT = new AsmPrimitiveType( "short" ) );
    PRIMITIVES.put( "char", CHAR = new AsmPrimitiveType( "char" ) );
    PRIMITIVES.put( "int", INT = new AsmPrimitiveType( "int" ) );
    PRIMITIVES.put( "long", LONG = new AsmPrimitiveType( "long" ) );
    PRIMITIVES.put( "float", FLOAT = new AsmPrimitiveType( "float" ) );
    PRIMITIVES.put( "double", DOUBLE = new AsmPrimitiveType( "double" ) );
    PRIMITIVES.put( "boolean", BOOLEAN = new AsmPrimitiveType( "boolean" ) );
    PRIMITIVES.put( "void", VOID = new AsmPrimitiveType( "void" ) );
    PRIMITIVES.put( "B", BYTE );
    PRIMITIVES.put( "S", SHORT );
    PRIMITIVES.put( "C", CHAR );
    PRIMITIVES.put( "I", INT );
    PRIMITIVES.put( "J", LONG );
    PRIMITIVES.put( "F", FLOAT );
    PRIMITIVES.put( "D", DOUBLE );
    PRIMITIVES.put( "Z", BOOLEAN );
    PRIMITIVES.put( "V", VOID );
  }
  
  public static AsmPrimitiveType findPrimitive( String name ) {
    return PRIMITIVES.get( name );
  }
  
  private AsmPrimitiveType( String name ) {
    super( name );
  }

  @Override
  AsmType copy() {
    return this;
  }

  @Override
  AsmType copyNoArrayOrParameters() {
    return this;
  }

  public boolean isPrimitive() {
    return true;
  }

  @Override
  public void incArrayDims() {
    throw new UnsupportedOperationException( "Primitive types are immutable" );
  }
  
  @Override
  public void addTypeParameter( AsmType type ) {
    throw new UnsupportedOperationException( "Primitive types are immutable" );
  }

  @Override
  public void setName( String name ) {
    throw new UnsupportedOperationException( "Primitive types are immutable" );
  }

  @Override
  public void setTypeVariable() {
    throw new UnsupportedOperationException( "Primitive types are immutable" );
  }
}
