package editor.shipit;

import editor.FileTree;
import editor.FileTreeUtil;
import gw.util.PathUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 */
public class FileChangeFinder
{
  private long _timestamp;

  public FileChangeFinder( boolean rebuild )
  {
    _timestamp = rebuild ? 0 : System.currentTimeMillis();
  }

  public void reset()
  {
    _timestamp = System.currentTimeMillis();
  }

  public boolean isRefreshAll()
  {
    return _timestamp == 0;
  }

  public Set<FileTree> findChangedFiles( Predicate<FileTree> filter )
  {
    return findChangedFiles( FileTreeUtil.getRoot(), filter );
  }

  public Set<FileTree> findChangedFiles( FileTree ft, Predicate<FileTree> filter )
  {
    return findChangedFiles( ft, filter, new HashSet<>() );
  }

  private Set<FileTree> findChangedFiles( FileTree ft, Predicate<FileTree> filter, Set<FileTree> result )
  {
    if( ft.isFile() )
    {
      if( PathUtil.lastModified( ft.getFileOrDir() ) > _timestamp && filter.test( ft ) )
      {
        result.add( ft );
      }
    }
    else
    {
      for( FileTree child: ft.getChildren() )
      {
        findChangedFiles( child, filter, result );
      }
    }
    return result;
  }
}
