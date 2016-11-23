package gw.internal.gosu.parser.java.compiler;

import gw.lang.javac.ClassJavaFileObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
*/
class GosuJavaFileManager extends ForwardingJavaFileManager<JavaFileManager>
{
  private final List<ClassJavaFileObject> _classFiles;
  private List<ClassJavaFileObject> _sessionFiles;

  GosuJavaFileManager( JavaFileManager fileManager )
  {
    super( fileManager );
    _classFiles = new ArrayList<>();
  }

  @Override
  public JavaFileObject getJavaFileForOutput( Location location, String className, JavaFileObject.Kind kind, FileObject sibling ) throws IOException
  {
    ClassJavaFileObject file = new ClassJavaFileObject( className, kind );
    _classFiles.add( file );
    if( _sessionFiles != null )
    {
      _sessionFiles.add( file );
    }
    return file;
  }

  public List<ClassJavaFileObject> getGeneratedOutputFiles()
  {
    return _classFiles;
  }

  public void beginSession()
  {
    _sessionFiles = new ArrayList<>();
  }
  public List<ClassJavaFileObject> endSession()
  {
    ArrayList<ClassJavaFileObject> sessionFiles = new ArrayList<>( _sessionFiles );
    _sessionFiles = null;
    return sessionFiles;
  }
}
