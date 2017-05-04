package gw.lang.reflect.json;

import gw.fs.IFile;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuSourceProducer;
import gw.lang.reflect.gs.gen.SrcClass;
import gw.util.GosuClassUtil;
import java.util.StringTokenizer;

/**
 */
public class JsonImplSourceProducer extends GosuSourceProducer<Model>
{
  public static final String IMPL = "impl";

  public JsonImplSourceProducer( ITypeLoader typeLoader )
  {
    super( typeLoader, JsonSourceProducer.FILE_EXTENSIONS, Model::new );
  }

  @Override
  protected String aliasFqn( String fqn, IFile file )
  {
    return makeImplName( fqn );
  }

  static String makeImplName( String fqn )
  {
    return GosuClassUtil.getPackage( fqn ) + '.'+IMPL+'.' + GosuClassUtil.getShortClassName( fqn );
  }

  @Override
  protected boolean isInnerType( String topLevel, String relativeInner )
  {
    topLevel = deriveBaseFqn( topLevel );
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
      childName = deriveBaseFqn( childName );
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

  static String deriveBaseFqn( String fqn )
  {
    String pkg = GosuClassUtil.getPackage( fqn );
    pkg = pkg.substring( 0, pkg.length() - ("."+IMPL).length() );
    return pkg + '.' + GosuClassUtil.getShortClassName( fqn );
  }

  @Override
  protected String produce( String topLevelFqn, Model model )
  {
    StringBuilder sb = new StringBuilder();
    SrcClass srcClass = new JsonImplCodeGen( model.getType(), topLevelFqn ).make();
    srcClass.render( sb, 0 );
    return sb.toString();
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.Class;
  }
}