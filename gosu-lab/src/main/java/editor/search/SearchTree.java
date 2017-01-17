package editor.search;

import editor.AbstractTree;
import editor.EditorHost;
import editor.FileTree;
import editor.ITreeNode;
import editor.LabFrame;
import editor.NodeKind;
import java.nio.file.Path;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.util.GosuEscapeUtil;
import gw.util.PathUtil;
import gw.util.StreamUtil;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.Writer;
import java.util.function.Consumer;

/**
 */
public class SearchTree extends AbstractTree<SearchTree, SearchTree.SearchTreeNode>
{
  public SearchTree( JTree tree )
  {
    super( tree );
  }

  public SearchTree( NodeKind kind, SearchTreeNode node )
  {
    super( kind, node );
  }

  public SearchTree( String text, NodeKind kind, SearchTreeNode node )
  {
    super( text, kind, node );
  }

  @Override
  public String getText()
  {
    String text = super.getText();
    if( text != null )
    {
      if( text.contains( "$count" ) )
      {
        text = text.replace( "$count", String.valueOf( getCount() ) );
      }
      return text;
    }

    SearchTreeNode node = getNode();
    SearchLocation location = node.getLocation();
    if( location == null )
    {
      text = makeDirectoryText( node );
    }
    else
    {
      text = makOccuranceText( location );
    }
    _text = text;
    return getText();
  }

  private String makeDirectoryText( SearchTreeNode node )
  {
    return "<html>" + node.getFile().getName() + "&nbsp;<font style=italic color=#808080>($count&nbsp;occurrences)</font>&nbsp;";
  }

  private String makOccuranceText( SearchLocation loc )
  {
    String text;
    int col = loc._iColumn - 1;
    try
    {
      if( loc._iLength <= 200 )
      {
        String textLine = getTextLine( loc );
        String beforeMatch = GosuEscapeUtil.escapeForHTML( textLine.substring( 0, col ) );
        String match = GosuEscapeUtil.escapeForHTML( textLine.substring( col, col + loc._iLength ) );
        String afterMatch = GosuEscapeUtil.escapeForHTML( textLine.substring( col + loc._iLength ) );
        text = "<html><font style=italic color=#808080>(" + loc._iLine + ":&nbsp;" + (col + 1) + ")</font> " + beforeMatch + "<b>" + match + "</b>" + afterMatch;
      }
      else
      {
        text = "<html><font style=italic color=#808080>(" + loc._iLine + ":&nbsp;" + (col + 1) + ")</font>";
      }
    }
    catch( StringIndexOutOfBoundsException e )
    {
      // this can happen after the user Replaces the text and the overall document size becomes smaller
      text = "<html><font style=italic color=#808080>(" + loc._iLine + ":&nbsp;" + (col + 1) + ")</font> <b>Invalid</b>";
    }
    return text;
  }

  private String getTextLine( SearchLocation loc )
  {
    try( BufferedReader reader = PathUtil.createReader( getNode().getFile().getFileOrDir() ) )
    {
      for( int line = 0; line < loc._iLine; line++ )
      {
        String textLine = reader.readLine();
        if( line == loc._iLine-1 )
        {
          return textLine;
        }
      }
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    return "";
  }

  @Override
  public Icon getIcon()
  {
    return findIcon();
  }

  private Icon findIcon()
  {
    SearchTreeNode node = getNode();
    if( node != null && node.getLocation() == null )
    {
      FileTree file = node.getFile();
      return file == null ? null : file.getIcon();
    }
    return null;
  }

  public static SearchTreeNode empty()
  {
    return new SearchTreeNode() {
      @Override
      public boolean hasTarget()
      {
        return false;
      }
    };
  }

  public int getCount()
  {
    SearchTreeNode node = getNode();
    if( node.isFile() )
    {
      return getChildCount();
    }

    int count = 0;
    for( SearchTree child: getChildren() )
    {
      count += child.getCount();
    }
    return count;
  }

  public void replace( String pattern )
  {
    int count = getChildCount();
    if( count > 0 )
    {
      SearchTreeNode node = getNode();
      if( node != null && node.isFile() )
      {
        makeChanges( content -> {
          for( int i = count - 1; i >= 0; i-- )
          {
            SearchTree child = getChildAt( i );
            SearchLocation loc = child.getNode().getLocation();
            content.replace( loc._iOffset, loc._iOffset + loc._iLength, pattern );
            maybeUpdateDoc( loc._iOffset, loc._iLength, pattern );
          }
        } );
      }
      else
      {
        for( SearchTree child : getChildren() )
        {
          child.replace( pattern );
        }
      }
    }
    else if( getNode() != null && getNode().getLocation() != null )
    {
      getParent().makeChanges( content -> {
        SearchLocation loc = getNode().getLocation();
        content.replace( loc._iOffset, loc._iOffset + loc._iLength, pattern );
        maybeUpdateDoc( loc._iOffset, loc._iLength, pattern );
      } );
    }
  }

  private void maybeUpdateDoc( int iOffset, int iLength, String pattern )
  {
    Path file = getNode().getFile().getFileOrDir();
    EditorHost editor = LabFrame.instance().getGosuPanel().findTab( file );
    if( editor == null )
    {
      return;
    }

    editor.getUndoManager().beginUndoAtom( "Replace" );
    try
    {
      ((AbstractDocument)editor.getEditor().getDocument()).replace( iOffset, iLength, pattern, null );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      editor.getUndoManager().endUndoAtom();
    }
    IType type = getNode().getFile().getType();
    if( type != null )
    {
      TypeSystem.refresh( (ITypeRef)type );
    }
  }

  private void makeChanges( Consumer<StringBuilder> contentChanger )
  {
    SearchTreeNode node = getNode();
    StringBuilder content;
    try( Reader reader = PathUtil.createReader( node.getFile().getFileOrDir() ) )
    {
      content = new StringBuilder( StreamUtil.getContent( reader ).replace( "\r\n", "\n" ) );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }

    contentChanger.accept( content );

    node.getFile().setLastModified();
    try( Writer writer = PathUtil.createWriter( node.getFile().getFileOrDir() ) )
    {
      writer.write( content.toString() );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  public static class SearchTreeNode implements ITreeNode
  {
    private final FileTree _file;
    private SearchLocation _loc;
    private Boolean _bFile;

    private SearchTreeNode()
    {
      _file = null;
      _loc = null;
    }

    public SearchTreeNode( FileTree file, SearchLocation loc )
    {
      _file = file;
      _loc = loc;
    }

    @Override
    public boolean hasTarget()
    {
      return getLocation() != null;
    }

    @Override
    public void jumpToTarget()
    {
      if( getLocation() == null )
      {
        return;
      }

      LabFrame.instance().getGosuPanel().openFile( _file.getFileOrDir(), true );
      EventQueue.invokeLater( () -> LabFrame.instance().getGosuPanel().getCurrentEditor().gotoOffset( getLocation()._iOffset ) );
    }

    public FileTree getFile()
    {
      return _file;
    }

    public SearchLocation getLocation()
    {
      return _loc;
    }

    public boolean isFile()
    {
      return _bFile == null
             ? _bFile = _file != null && _file.isFile()
             : _bFile;
    }
  }
}
