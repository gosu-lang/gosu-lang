package editor.util;

import editor.LabFrame;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.module.IFileSystem;
import java.net.MalformedURLException;
import java.nio.file.Path;
import gw.util.PathUtil;
import java.io.File;

public class TypeNameUtil
{
  public static String getTypeNameForFile( Path classFile )
  {
    GosuClassTypeLoader typeLoader = TypeSystem.getCurrentModule().getModuleTypeLoader().getTypeLoader( GosuClassTypeLoader.class );
    IFileSystemGosuClassRepository repo = (IFileSystemGosuClassRepository)typeLoader.getRepository();
    IFile classIFile = PathUtil.getIFile( classFile );
    IDirectory[] classPath = repo.getSourcePath();
    IDirectory root = null;
    for( IDirectory folder : classPath )
    {
      if( classIFile.isDescendantOf( folder ) )
      {
        root = folder;
        break;
      }
    }
    if( root == null )
    {
      IFileSystem fileSystem = CommonServices.getFileSystem();
      for( String path: LabFrame.instance().getGosuPanel().getExperiment().getBackingSourcePath() )
      {
        try
        {
          IDirectory dir = fileSystem.getIDirectory( PathUtil.create( path ).toUri().toURL() );
          if( classIFile.isDescendantOf( dir ) )
          {
            root = dir;
            break;
          }
        }
        catch( MalformedURLException e )
        {
          throw new RuntimeException( e );
        }
      }
    }
    if( root == null )
    {
      return null;
    }
    return getTypeNameFromFile( root, classIFile );
  }

  public static String getTypeNameFromFile( IDirectory root, IFile file )
  {
    String strClassPath = root.getPath().getFileSystemPathString() + File.separatorChar;

    String strQualifiedClassName = file.getPath().getFileSystemPathString().substring( strClassPath.length() );
    int iDot = strQualifiedClassName.lastIndexOf( '.' );
    if( iDot >= 0 )
    {
      strQualifiedClassName = strQualifiedClassName.substring( 0, iDot );
      strQualifiedClassName = strQualifiedClassName.replace( '/', '.' ).replace( '\\', '.' );
    }
    if( strQualifiedClassName.startsWith( "." ) )
    {
      strQualifiedClassName = strQualifiedClassName.substring( 1 );
    }
    return strQualifiedClassName;
  }

}
