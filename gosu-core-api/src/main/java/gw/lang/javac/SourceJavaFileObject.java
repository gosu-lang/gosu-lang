package gw.lang.javac;

import gw.util.PathUtil;
import gw.util.StreamUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import javax.tools.SimpleJavaFileObject;

/**
 */
public class SourceJavaFileObject extends SimpleJavaFileObject
{
  public SourceJavaFileObject( URI uri )
  {
    super( uri, Kind.SOURCE );
  }

  public SourceJavaFileObject( String filename )
  {
    super( PathUtil.create( filename ).toUri(), Kind.SOURCE );
  }

  @Override
  public CharSequence getCharContent( boolean ignoreEncodingErrors ) throws IOException
  {
    Path file = PathUtil.create( uri );
    try( BufferedReader reader = PathUtil.createReader( file ) )
    {
      return StreamUtil.getContent( reader );
    }
  }
}
