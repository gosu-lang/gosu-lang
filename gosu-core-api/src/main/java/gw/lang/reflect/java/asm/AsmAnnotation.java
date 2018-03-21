/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

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
    _fieldValues = new HashMap<String, Object>();
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
    if( cls == Type.class ) {
      AsmType type = AsmUtil.makeType( (Type)value );
      return type.getFqn();
    }
    if( cls.isArray() ) {
      List<Object> values = new ArrayList<Object>();
      for( int i = 0; i < Array.getLength( value ); i++ ) {
        values.add( makeAppropriateValue( Array.get( value, i ) ) );
      }
      return values;
    }
    throw new IllegalStateException( "Unexpected value " + value );
  }
}
