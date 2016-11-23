package editor.search;

import editor.ExternalFileTree;
import editor.FileTree;
import editor.FileTreeUtil;
import editor.GosuPanel;
import editor.LabFrame;
import editor.NodeKind;
import gw.util.GosuStringUtil;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TextSearcher extends AbstractSearcher
{
  private final String _pattern;
  private final boolean _caseSensitive;
  private final boolean _wholeWords;
  private final boolean _regEx;

  public TextSearcher( String pattern,
                       boolean caseSensitive,
                       boolean wholeWords,
                       boolean regEx )
  {
    _pattern = pattern;
    _caseSensitive = caseSensitive;
    _wholeWords = wholeWords;
    _regEx = regEx;
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

  public List<SearchLocation> searchLocal()
  {
    GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
    FileTree tree = FileTreeUtil.find( gosuPanel.getCurrentFile() );
    if( tree == null )
    {
      tree = new ExternalFileTree( gosuPanel.getCurrentFile(), gosuPanel.getCurrentEditor().getParsedClass().getName() );
    }
    SearchTree results = new SearchTree( "root", NodeKind.Directory, SearchTree.empty() );
    searchTree( tree, results, this::isTextFile, null );
    return findLocations( results, new ArrayList<>() );
  }

  private boolean isTextFile( FileTree ft )
  {
    return FileTreeUtil.isSupportedTextFile( ft );
  }

  private List<SearchLocation> findLocations( SearchTree tree, List<SearchLocation> locations )
  {
    if( tree == null )
    {
      return locations;
    }

    SearchTree.SearchTreeNode node = tree.getNode();
    if( node != null && node.getLocation() != null )
    {
      locations.add( node.getLocation() );
    }
    else
    {
      for( int i = 0; i < tree.getChildCount(); i++ )
      {
        findLocations( tree.getChildAt( i ), locations );
      }
    }

    return locations;
  }

}

