package gw.lang.reflect.json;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 */
public class NashornJsonParser implements IJsonParser
{
  private static final NashornJsonParser INSTANCE = new NashornJsonParser();

  public static IJsonParser instance()
  {
    return INSTANCE;
  }

  private NashornJsonParser()
  {}

  public Bindings parseJson( String jsonText ) throws ScriptException
  {
    ScriptEngine engine = new ScriptEngineManager().getEngineByName( "javascript" );
    String script = "Java.asJSONCompatible(" + jsonText + ")";
    return (Bindings)engine.eval( script );
  }
}
