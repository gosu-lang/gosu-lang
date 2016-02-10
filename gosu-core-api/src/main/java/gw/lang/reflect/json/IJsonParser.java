package gw.lang.reflect.json;

import javax.script.Bindings;
import javax.script.ScriptException;

/**
 */
public interface IJsonParser
{
  Bindings parseJson( String jsonText ) throws ScriptException;

  static IJsonParser getDefaultParser()
  {
    return NashornJsonParser.instance();
  }
}
