package editor.search;

import editor.FileTree;
import editor.NodeKind;
import gw.util.GosuStringUtil;
import gw.util.StreamUtil;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
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
}

