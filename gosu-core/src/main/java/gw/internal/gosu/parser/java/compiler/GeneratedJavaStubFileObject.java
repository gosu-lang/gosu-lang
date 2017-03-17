package gw.internal.gosu.parser.java.compiler;

import com.sun.tools.javac.file.BaseFileObject;
import com.sun.tools.javac.file.JavacFileManager;
import gw.lang.ir.SignatureUtil;
import gw.util.concurrent.LocklessLazyVar;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.function.Supplier;

/**
*/
public class GeneratedJavaStubFileObject extends BaseFileObject
{
  private String _name;
  private long _timestamp;
  private Supplier<String> _sourceSupplier;
  private LocklessLazyVar<String> _src = LocklessLazyVar.make( () -> _sourceSupplier.get() );

  public GeneratedJavaStubFileObject( JavacFileManager mgr, String name, Supplier<String> sourceSupplier )
  {
    super( mgr );
    _name = name;
    _timestamp = System.currentTimeMillis();
    _sourceSupplier = sourceSupplier;
  }

  @Override
  public URI toUri()
  {
    return URI.create( "genstub:///" + _name.replace( '.', '/' ) + Kind.SOURCE.extension );
  }

  @Override
  public String getName()
  {
    return _name;
  }

  @Override
  public InputStream openInputStream() throws IOException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream openOutputStream() throws IOException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public CharSequence getCharContent( boolean ignoreEncodingErrors ) throws IOException
  {
    return _src.get();
  }

  @Override
  public Writer openWriter() throws IOException
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public long getLastModified()
  {
    return _timestamp;
  }

  @Override
  public boolean delete()
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getShortName()
  {
    return SignatureUtil.getSimpleName( _name );
  }

  @Override
  protected String inferBinaryName( Iterable<? extends File> files )
  {
    return _name;
  }

  @Override
  public boolean equals( Object o )
  {
    if( !(o instanceof GeneratedJavaStubFileObject) )
    {
      return false;
    }
    return _name.equals( ((GeneratedJavaStubFileObject)o).getName() );
  }

  @Override
  public int hashCode()
  {
    return _name.hashCode();
  }

  @Override
  public Kind getKind()
  {
    return Kind.SOURCE;
  }

  @Override
  public boolean isNameCompatible( String simpleName, Kind kind )
  {
    return true;
  }
}
