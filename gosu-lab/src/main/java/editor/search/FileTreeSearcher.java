package editor.search;

import editor.FileTree;
import editor.NodeKind;
import editor.util.IProgressCallback;
import gw.util.GosuStringUtil;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.function.Predicate;

/**
 */
public class FileTreeSearcher
{
  private final String _pattern;
  private final boolean _caseSensitive;
  private final boolean _wholeWords;
  private final boolean _regEx;

  public FileTreeSearcher( String pattern,
                           boolean caseSensitive,
                           boolean wholeWords,
                           boolean regEx )
  {
    _pattern = pattern;
    _caseSensitive = caseSensitive;
    _wholeWords = wholeWords;
    _regEx = regEx;
  }

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

  public boolean search( FileTree tree, SearchTree results )
  {
    if( isExcluded( tree ) )
    {
      return false;
    }

    // Search

    String content;
    File file = tree.getFileOrDir();
    try( Reader reader = new FileReader( file ) )
    {
      content = StreamUtil.getContent( reader );
      content = GosuStringUtil.replace( content, "\r\n", "\n" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }

    List<SearchLocation> locations;
    if( _regEx )
    {
      locations = RegExStringUtil.search( content, _pattern, !_caseSensitive );
    }
    else
    {
      locations = StringUtil.search( content, _pattern, !_caseSensitive, _wholeWords );
    }

    if( locations.isEmpty() )
    {
      return false;
    }

    SearchTree searchTree = getOrMakePath( tree, results );

    for( SearchLocation loc: locations )
    {
      SearchTree.SearchTreeNode node = new SearchTree.SearchTreeNode( tree, loc );
      SearchTree res = new SearchTree( NodeKind.Info, node );
      searchTree.addViaModel( res );
      //res.select();
    }
    return true;
  }

  private SearchTree getOrMakePath( FileTree tree, SearchTree results )
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

  private boolean isExcluded( FileTree tree )
  {
    //## todo: maybe filter out binary files and such?
    return false;
  }
}

