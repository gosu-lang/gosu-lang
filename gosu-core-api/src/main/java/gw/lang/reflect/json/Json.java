package gw.lang.reflect.json;


import gw.lang.reflect.Expando;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
public class Json
{
  @SuppressWarnings({"UnusedDeclaration", "unchecked"})
  public static Object fromJsonObject( Object o )
  {
    if( o instanceof Map ) {
      Expando ret = new Expando();
      ((Map)o).forEach( (k, v) -> ret.setFieldValue( (String)k, fromJsonObject( v ) ) );
      o = ret;
    }
    else if( o instanceof List ) {
      o = ((List)o).stream().map( Json::fromJsonObject ).collect( Collectors.toList() );
    }
    return o;
  }

  /**
   * Reconstruct the JSON string as one of: Dynamic javax.script.Bindings object, List, or primary object (String, Integer, or Double)
   *
   * @param json A Standard JSON formatted string
   * @return One of: Dynamic javax.script.Bindings object, List, or primary object (String, Integer, or Double)
   */
  @SuppressWarnings("UnusedDeclaration")
  public static Object fromJson( String json )
  {
    //## todo: use our Json parser instead of nashorn...
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

  /**
   * Makes a tree of structure types reflecting the Bindings.
   *<p>
   * A structure type contains a property member for each name/value pair in the Bindings.  A property has the same name as the key and follows these rules:
   * <ul>
   *   <li> If the type of the value is a "simple" type, such as a String or Integer, the type of the property matches the simple type exactly
   *   <li> Otherwise, if the value is a Bindings type, the property type is that of a child structure with the same name as the property and recursively follows these rules
   *   <li> Otherwise, if the value is a List, the property is a List parameterized with the component type, and the component type recursively follows these rules
   * </ul>
   */
  public static String makeStructureTypes( String nameForStructure, Bindings bindings )
  {
    JsonStructureType type = (JsonStructureType)transformJsonObject( nameForStructure, null, bindings );
    StringBuilder sb = new StringBuilder();
    type.render( sb, 0 );
    return sb.toString();
  }

  private static IJsonType transformJsonObject( String name, IJsonParentType parent, Object jsonObj )
  {
    IJsonType type = null;

    if( parent != null )
    {
      type = parent.findChild( name );
    }

    if( jsonObj instanceof Bindings )
    {
      if( type == null )
      {
        type = new JsonStructureType( parent, name );
      }
      for( Object k: ((Bindings)jsonObj).keySet() )
      {
        String key = (String)k;
        key = Expando.getAltKey( key );
        Object value = ((Bindings)jsonObj).get( key );
        IJsonType memberType = transformJsonObject( key, (IJsonParentType)type, value );
        if( memberType != null )
        {
          ((JsonStructureType)type).addMember( key, memberType );
        }
      }
      if( parent != null )
      {
        parent.addChild( name, (IJsonParentType)type );
      }
    }
    else if( jsonObj instanceof List )
    {
      if( type == null )
      {
        type = new JsonListType( parent );
      }
      IJsonType compType = null;
      for( Object elem: (List)jsonObj )
      {
        IJsonType csr = transformJsonObject( name, (IJsonParentType)type, elem );
        if( compType != null && csr != compType )
        {
          throw new RuntimeException( "Types in array are different: " + compType.getName() + " vs: " + csr.getName() );
        }
        compType = csr;
      }
      ((JsonListType)type).setComponentType( compType );
      if( parent != null )
      {
        parent.addChild( name, (IJsonParentType)type );
      }
    }
    else
    {
      type = JsonSimpleType.get( jsonObj );
    }
    return type;
  }
}
