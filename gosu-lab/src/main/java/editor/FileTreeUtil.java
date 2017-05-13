package editor;

import manifold.api.fs.IFile;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import java.nio.file.Path;
import gw.util.PathUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.tree.TreeModel;

/**
 */
public class FileTreeUtil
{
  public static FileTree find( String fqn )
  {
    FileTree fileTree = FileTreeUtil.getRoot().find( fqn );
    if( fileTree == null )
    {
      IType type = TypeSystem.getByFullNameIfValid( fqn );
      if( type != null )
      {
        IFile[] sourceFiles = type.getSourceFiles();
        if( sourceFiles != null && sourceFiles.length > 0 )
        {
          IFile sourceFile = sourceFiles[0];
          fileTree = find( PathUtil.create( sourceFile.toURI() ), fqn );
        }
      }
    }
    return fileTree;
  }

  public static FileTree find( Path file )
  {
    return find( file, null );
  }
  public static FileTree find( Path file, String typeName )
  {
    FileTree fileTree = FileTreeUtil.getRoot().find( file );
    if( fileTree == null )
    {
      //## todo: add this to the root as an external filetree?
      fileTree = FileTreeUtil.makeExternalFileTree( file, typeName );
    }
    return fileTree;
  }

  public static ExperimentView getExperimentView()
  {
    return getGosuPanel() == null ? null : getGosuPanel().getExperimentView();
  }

  public static GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  public static FileTree getRoot()
  {
    ExperimentView experimentView = getExperimentView();
    if( experimentView == null )
    {
      return null;
    }
    TreeModel model = experimentView.getTree().getModel();
    return (FileTree)model.getRoot();
  }

  public static Set<String> getAllExperimentTypes()
  {
    Set<String> names = new TreeSet<>();
    getRoot().getAllTypeNames( names );
    return names;
  }

  public static FileTree makeExternalFileTree( Path file, String fqn )
  {
    return new ExternalFileTree( file, fqn );
  }

  public static boolean isSupportedTextFile( FileTree ft )
  {
    String[] binaryExt = { ".jar", ".zip", ".tar", ".gz", ".hprof", ".png", ".gif", ".jpg", ".bmp", ".exe", ".dll", ".so",  };
    String fileName = PathUtil.getName( ft.getFileOrDir() ).toLowerCase();
    return !Arrays.stream( binaryExt ).anyMatch( fileName::endsWith );
  }
}
