package gw.lang.reflect.json;

import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuSourceProducer;
import gw.util.GosuClassUtil;
import java.util.Collections;
import java.util.Set;
import java.util.StringTokenizer;

/**
 */
public class JsonSourceProducer extends GosuSourceProducer<Model>
{
  public static final String FILE_EXTENSION = "json";
  public static final Set<String> FILE_EXTENSIONS = Collections.singleton( FILE_EXTENSION );

  public JsonSourceProducer( ITypeLoader typeLoader )
  {
    super( typeLoader, FILE_EXTENSIONS, Model::new, "editor.plugin.typeloader.json.JsonTypeFactory", null );
  }

  @Override
  protected boolean isInnerType( String topLevel, String relativeInner )
  {
    Model model = getModel( topLevel );
    JsonStructureType type = model == null ? null : model.getType();
    if( type == null )
    {
      return false;
    }
    JsonStructureType csr = type;
    for( StringTokenizer tokenizer = new StringTokenizer( relativeInner, "." ); tokenizer.hasMoreTokens(); )
    {
      String childName = tokenizer.nextToken();
      IJsonParentType child = csr.findChild( childName );
      if( child instanceof JsonStructureType )
      {
        csr = (JsonStructureType)child;
        continue;
      }
      return false;
    }
    return true;
  }

  @Override
  protected String produce( String topLevelFqn, Model model )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( GosuClassUtil.getPackage( topLevelFqn ) ).append( "\n\n" );
    model.getType().render( sb, 2, true );
    return sb.toString();
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.Structure;
  }
}