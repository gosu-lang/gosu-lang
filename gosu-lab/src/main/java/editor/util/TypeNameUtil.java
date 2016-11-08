package editor.util;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;

import java.io.File;

public class TypeNameUtil
{
  public static String getTypeNameForFile( File classFile )
  {
    GosuClassTypeLoader typeLoader = TypeSystem.getCurrentModule().getModuleTypeLoader().getTypeLoader( GosuClassTypeLoader.class );
    IFileSystemGosuClassRepository repo = (IFileSystemGosuClassRepository)typeLoader.getRepository();
    IFile classIFile = CommonServices.getFileSystem().getIFile( classFile );
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
      return null;
    }
    return getTypeNameFromFile( root, classIFile );
  }

  public static String getTypeNameFromFile( IDirectory root, IFile file )
  {
    String strClassPath = root.getPath().getFileSystemPathString() + File.separatorChar;

    String strQualifiedClassName = file.getPath().getFileSystemPathString().substring( strClassPath.length() );
    strQualifiedClassName =
      strQualifiedClassName.substring( 0, strQualifiedClassName.lastIndexOf( '.' ) );
    strQualifiedClassName = strQualifiedClassName.replace( '/', '.' ).replace( '\\', '.' );
    if( strQualifiedClassName.startsWith( "." ) )
    {
      strQualifiedClassName = strQualifiedClassName.substring( 1 );
    }
    return strQualifiedClassName;
  }

}
