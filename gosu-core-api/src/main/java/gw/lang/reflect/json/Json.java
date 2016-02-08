package gw.lang.reflect.json;


import gw.lang.reflect.Expando;

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
  private static final Json INSTANCE = new Json();

  private Json()
  {}

  public static Json instance()
  {
    return INSTANCE;
  }

  @SuppressWarnings({"UnusedDeclaration", "unchecked"})
  public Object fromJsonObject( Object o )
  {
    if( o instanceof Map ) {
      Expando ret = new Expando();
      ((Map)o).forEach( (k, v) -> ret.setFieldValue( (String)k, fromJsonObject( v ) ) );
      o = ret;
    }
    else if( o instanceof List ) {
      o = ((List)o).stream().map( this::fromJsonObject ).collect( Collectors.toList() );
    }
    return o;
  }

  @SuppressWarnings("UnusedDeclaration")
  public Object fromJsonString( String json ) {
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

  public void renderStructureTypes( String name, Expando expando, StringBuilder sb )
  {
    JsonStructureType type = (JsonStructureType)transformJsonObject( name, null, expando );
    type.render( sb, 0 );
  }

  private IJsonType transformJsonObject( String name, IJsonParentType parent, Object jsonObj )
  {
    IJsonType type = null;

    if( parent != null )
    {
      type = parent.findChild( name );
    }

    if( jsonObj instanceof Expando )
    {
      if( type == null )
      {
        type = new JsonStructureType( parent, name );
      }
      for( String key: ((Expando)jsonObj).getMap().keySet() )
      {
        Object value = ((Expando)jsonObj).getFieldValue( key );
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
