package editor.search;

import editor.FileTree;
import editor.NodeKind;
import editor.util.IProgressCallback;

import java.util.function.Predicate;

/**
 */
public abstract class AbstractSearcher
{
  public abstract boolean search( FileTree tree, SearchTree results );

  public boolean searchTree( FileTree tree, SearchTree results, Predicate<FileTree> filter,
                             IProgressCallback progress )
  {
    if( progress.isAbort() )
    {
      return false;
    }

    if( tree.isFile() && filter.test( tree ) )
    {
      progress.incrementProgress( tree.getName() );
      return search( tree, results );
    }
    else if( !tree.isLeaf() )
    {
      boolean bFound = false;
      for( FileTree file : tree.getChildren() )
      {
        if( searchTree( file, results, filter, progress ) )
        {
          bFound = true;
        }
      }
      return bFound;
    }
    return true;
  }

  protected SearchTree getOrMakePath( FileTree tree, SearchTree results )
  {
    if( tree.getParent() != null )
    {
      results = getOrMakePath( tree.getParent(), results );
    }
    for( SearchTree child: results.getChildren() )
    {
      SearchTree.SearchTreeNode node = child.getNode();
      if( node != null && node.getFile() == tree )
      {
        return child;
      }
    }
    SearchTree.SearchTreeNode node = new SearchTree.SearchTreeNode( tree, null );
    SearchTree searchTree = new SearchTree( NodeKind.Directory, node );
    results.addViaModel( searchTree );
    return searchTree;
  }

  protected boolean isExcluded( FileTree tree )
  {
    //## todo: maybe filter out binary files and such?
    return false;
  }
}
