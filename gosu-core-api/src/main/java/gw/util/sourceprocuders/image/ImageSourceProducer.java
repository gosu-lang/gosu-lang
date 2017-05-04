package gw.util.sourceprocuders.image;

import gw.fs.IFile;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuSourceProducer;
import gw.lang.reflect.gs.gen.SrcClass;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 */
public class ImageSourceProducer extends GosuSourceProducer<Model>
{
  public static final Set<String> FILE_EXTENSIONS = new HashSet<>( Arrays.asList( "jpg", "png", "bmp", "wbmp", "gif" ) );

  public ImageSourceProducer( ITypeLoader typeLoader )
  {
    super( typeLoader, FILE_EXTENSIONS, Model::new );
  }

  @Override
  protected String aliasFqn( String fqn, IFile file )
  {
    return fqn + '_' + file.getExtension();
  }

  @Override
  protected boolean isInnerType( String topLevel, String relativeInner )
  {
    return false;
  }

  @Override
  protected String produce( String topLevelFqn, Model model )
  {
    SrcClass srcClass = new ImageCodeGen( model._url, topLevelFqn ).make();
    StringBuilder sb = srcClass.render( new StringBuilder(), 0 );
    return sb.toString();
  }

  @Override
  public ClassType getClassType( String fqn )
  {
    return ClassType.Class;
  }
}