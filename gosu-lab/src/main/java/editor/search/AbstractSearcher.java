package editor.search;

import editor.FileTree;
import editor.FileTreeUtil;
import editor.NodeKind;
import editor.util.IProgressCallback;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 */
public abstract class AbstractSearcher
{
  public abstract boolean search( FileTree tree, SearchTree results );

  public boolean searchTree( FileTree tree, SearchTree results, Predicate<FileTree> filter, IProgressCallback progress )
  {
    return searchTrees( Collections.singletonList( tree ), results, filter, progress );
  }
  public boolean searchTrees( List<FileTree> trees, SearchTree results, Predicate<FileTree> filter, IProgressCallback progress )
  {
    if( progress != null && progress.isAbort() )
    {
      return false;
    }

    boolean bFound = false;
    for( FileTree tree: trees )
    {
      if( tree.isFile() && filter.test( tree ) )
      {
        if( progress != null )
        {
          progress.incrementProgress( tree.getName() );
        }
        bFound = bFound | search( tree, results );
      }
      else if( !tree.isLeaf() )
      {
        if( searchTrees( tree.getChildren(), results, filter, progress ) )
        {
          bFound = true;
        }
      }
    }
    return bFound;
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
    return !FileTreeUtil.isSupportedTextFile( tree );
  }
}
