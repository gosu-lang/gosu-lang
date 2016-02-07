/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
public interface IExpando {
  Object getFieldValue( String field );
  void setFieldValue( String field, Object value );
  void setDefaultFieldValue( String field );
  Object invoke( String methodName, Object... args );
  Map getMap();

  @SuppressWarnings({"UnusedDeclaration", "unchecked"})
  public static Object fromJsonObject( Object o )
  {
    if( o instanceof Map ) {
      Expando ret = new Expando();
      ((Map)o).forEach( (k, v) -> ret.setFieldValue( (String)k, fromJsonObject( v ) ) );
      o = ret;
    }
    else if( o instanceof List ) {
      o = ((List)o).stream().map( IExpando::fromJsonObject ).collect( Collectors.toList() );
    }
    return o;
  }

  @SuppressWarnings("UnusedDeclaration")
  public static Object fromJsonString( String json ) {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName( "javascript" );
    String script = "Java.asJSONCompatible(" + json + ")";
    try
    {
      Object jsonObj = engine.eval( script );
      return fromJsonObject( jsonObj );
    }
    catch( ScriptException e )
    {
      throw new RuntimeException( e );
    }
  }
}
