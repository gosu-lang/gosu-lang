package gw.lang.reflect.json;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.StringReader;
import java.util.List;

public class DefaultParser implements IJsonParser {
  @Override
  public Bindings parseJson( String jsonText ) throws ScriptException
  {
    SimpleParserImpl parser = new SimpleParserImpl( new Tokenizer(new StringReader(jsonText)), false);
    Object result = parser.parse();
    List<String> errors = parser.getErrors();
    if(errors.size() != 0) {
      StringBuilder sb = new StringBuilder("Found errors:\n");
      for(String err : errors) {
        sb.append(err).append("\n");
      }
      throw new ScriptException(sb.toString());
    }
    if(result instanceof Bindings) {
      return (Bindings)result;
    }
    return NashornJsonParser.wrapValueInBindings( result );
  }
}
