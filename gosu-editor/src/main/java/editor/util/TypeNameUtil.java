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
  public static String getClassNameForFile( File classFile )
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
    return repo.getClassNameFromFile( root, classIFile, new String[]{GosuClassTypeLoader.GOSU_CLASS_FILE_EXT, GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT, GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT, GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT} );
  }
}
