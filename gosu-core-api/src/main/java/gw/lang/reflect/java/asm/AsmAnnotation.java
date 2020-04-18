/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import gw.internal.ext.org.objectweb.asm.Type;

import gw.util.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class AsmAnnotation {
  private AsmType _type;
  private boolean _bVisibleAtRuntime;
  private Map<String, Object> _fieldValues;

  public AsmAnnotation( String desc, boolean bVisibleAtRuntime ) {
    _type = AsmUtil.makeType( desc );
    _bVisibleAtRuntime = bVisibleAtRuntime;
    _fieldValues = new HashMap<>();
  }

  public AsmAnnotation( com.sun.tools.javac.code.Type type, boolean bVisibleAtRuntime ) {
    _type = AsmUtil.makeType( type );
    _bVisibleAtRuntime = bVisibleAtRuntime;
    _fieldValues = new HashMap<>();
  }

  public AsmType getType() {
    return _type;
  }

  public Map<String, Object> getFieldValues() {
    return _fieldValues;
  }

  public boolean isVisibleAtRuntime() {
    return _bVisibleAtRuntime;
  }

  void setValue( String field, Object value ) {
    value = makeAppropriateValue( value );
    _fieldValues.put( field, value );
  }

  static Object makeAppropriateValue( Object value ) {
    if( value == null ) {
      return null;
    }
    if( value instanceof ArrayList && ((ArrayList)value).isEmpty() ) {
      // Initial "array" value is an empty list, which is filled in the annotation visitor
      return value;
    }
    Class<?> cls = value.getClass();
    if( cls == Boolean.class ||
        cls == Byte.class ||
        cls == Character.class ||
        cls == Short.class ||
        cls == Integer.class ||
        cls == Long.class ||
        cls == Float.class ||
        cls == Double.class ||
        cls == String.class ||
        cls == AsmAnnotation.class ) {
      return value;
    }
    if( value instanceof Type ) {
      AsmType type = AsmUtil.makeType( (Type)value );
      return type.getFqn();
    }
    if( value instanceof Attribute.Enum )
    {
      return ((Attribute.Enum)value).value.toString();
    }
    if( value instanceof Symbol.VarSymbol )
    {
      return value.toString();
    }
    if( cls.isArray() ) {
      List<Object> values = new ArrayList<>();
      for( int i = 0; i < Array.getLength( value ); i++ ) {
        values.add( makeAppropriateValue( Array.get( value, i ) ) );
      }
      return values;
    }
    if( value instanceof com.sun.tools.javac.util.List )
    {
      List<Object> values = new ArrayList<>();
      for( Object e: (com.sun.tools.javac.util.List)value ) {
        values.add( makeAppropriateValue( e ) );
      }
      return values;
    }
    throw new IllegalStateException( "Unexpected value " + value );
  }
}
