package gw.lang.javac;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;

/**
*/
public class ClassJavaFileObject extends SimpleJavaFileObject
{
  private final ByteArrayOutputStream _outputStream;
  private final String _className;

  public ClassJavaFileObject( String className, Kind kind )
  {
    super( URI.create( "mem:///" + className.replace( '.', '/' ) + kind.extension ), kind );
    _className = className;
    _outputStream = new ByteArrayOutputStream();
  }

  @Override
  public OutputStream openOutputStream() throws IOException
  {
    return _outputStream;
  }

  @Override
  public InputStream openInputStream() throws IOException
  {
    if( _outputStream.size() > 0 )
    {
      return new ByteArrayInputStream( getBytes() );
    }
    return null;
  }

  public byte[] getBytes()
  {
    return _outputStream.toByteArray();
  }

  public String getClassName()
  {
    return _className;
  }
}
