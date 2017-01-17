package gw.lang.reflect.java.asm;

import gw.fs.IFile;
import gw.lang.parser.ISource;
import gw.lang.parser.StringSource;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.util.GosuClassUtil;
import gw.util.StreamUtil;
import java.io.IOException;

/**
 */
public class AsmBackingSourceFileHandle implements ISourceFileHandle
{
  private AsmClass _asmClass;
  private boolean _bTest;
  private IFile _file;
  private int _iOffset;
  private int _iEnd;

  public AsmBackingSourceFileHandle( IFile file, AsmClass asmClass, boolean bTest )
  {
    _bTest = bTest;
    _file = file;
    _asmClass = asmClass;
  }

  @Override
  public ISource getSource()
  {
    try
    {
      return new StringSource( StreamUtil.getContent( StreamUtil.getInputStreamReader( _file.openInputStream() ) ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public String getParentType()
  {
    return null;
  }

  @Override
  public String getNamespace() {
    return GosuClassUtil.getPackage( _asmClass.getName() );
  }

  @Override
  public String getFilePath()
  {
    return _file.getPath().getFileSystemPathString();
  }

  @Override
  public IFile getFile() {
    return _file;
  }

  @Override
  public boolean isTestClass()
  {
    return _bTest;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public boolean isStandardPath()
  {
    return true;
  }

  @Override
  public boolean isIncludeModulePath()
  {
    return false;
  }

  @Override
  public void cleanAfterCompile()
  {
  }

  public ClassType getClassType()
  {
    return ClassType.getFromFileName( _asmClass.getName() );
  }

  @Override
  public String getTypeNamespace()
  {
    String namespace = getNamespace();
    if( namespace.isEmpty() )
    {
      namespace = "default";
    }
    return namespace;
  }

  @Override
  public String getRelativeName()
  {
    return _asmClass.getSimpleName();
  }

  @Override
  public void setOffset( int iOffset )
  {
    _iOffset = iOffset;
  }
  @Override
  public int getOffset()
  {
    return _iOffset;
  }

  @Override
  public void setEnd( int iEnd )
  {
    _iEnd = iEnd;
  }
  @Override
  public int getEnd()
  {
    return _iEnd;
  }

  @Override
  public String getFileName()
  {
    return _file.getName();
  }

  @Override
  public String toString() {
    return _file.getPath().getPathString();
  }
}

