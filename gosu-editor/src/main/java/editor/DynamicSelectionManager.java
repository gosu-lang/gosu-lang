package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IParseTree;
import gw.lang.parser.expressions.IStringLiteralExpression;
import gw.lang.parser.statements.IStatementList;
import gw.util.GosuStringUtil;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;

/**
 */
public class DynamicSelectionManager implements CaretListener
{
  private int _start;
  private boolean _updating;
  private GosuEditor _gsEditor;
  private ArrayList<Point> _expansionList;
  private int _expansionIndex;

  public DynamicSelectionManager( GosuEditor parent )
  {
    parent.getEditor().addCaretListener( this );
    _gsEditor = parent;
  }

  public void expandSelection()
  {
    expandSelection( true );
  }

  public void expandSelection( boolean updateIndex )
  {
    _updating = true;
    try
    {
      ArrayList<Point> expansionsList = getExpansionsList();
      if( updateIndex && _expansionIndex == 0 )
      {
        updateIndexBasedOnCurrentSelection( true );
      }
      _expansionIndex = Math.min( _expansionIndex + 1, expansionsList.size() - 1 );
      setSelection( expansionsList.get( _expansionIndex ) );
    }
    finally
    {
      _updating = false;
    }
  }

  public void reduceSelection()
  {
    _updating = true;
    try
    {
      ArrayList<Point> expansionsList = getExpansionsList();
      if( _expansionIndex == 0 )
      {
        updateIndexBasedOnCurrentSelection( false );
      }
      _expansionIndex = Math.max( _expansionIndex - 1, 0 );
      setSelection( expansionsList.get( _expansionIndex ) );
    }
    finally
    {
      _updating = false;
    }
  }

  private void updateIndexBasedOnCurrentSelection( boolean expanding )
  {
    int selectionStart = _gsEditor.getEditor().getSelectionStart();
    int selectionEnd = _gsEditor.getEditor().getSelectionEnd();
    if( selectionStart != selectionEnd )
    {
      Point currentSelectionPoint = new Point( selectionStart, selectionEnd );
      while( _expansionIndex < _expansionList.size() )
      {
        Point point = _expansionList.get( _expansionIndex );
        if( contains( currentSelectionPoint, point ) )
        {
          _expansionIndex++;
        }
        else
        {
          break;
        }
      }
      if( expanding && _expansionIndex > 0 )
      {
        _expansionIndex--;
      }
    }
  }

  private void setSelection( Point point )
  {
    Rectangle visibleRect = _gsEditor.getEditor().getVisibleRect();
    _gsEditor.getEditor().getCaret().setDot( point.x );
    _gsEditor.getEditor().getCaret().moveDot( point.y );
    _gsEditor.getEditor().scrollRectToVisible( visibleRect );
  }

  private ArrayList<Point> getExpansionsList()
  {

    if( _expansionList == null )
    {
      _expansionList = new ArrayList<Point>();

      //Start at the given point
      Point initialPoint = new Point( _start, _start );
      _expansionList.add( initialPoint );
      addNextSelection( _expansionList );
    }
    return _expansionList;
  }

  private void addNextSelection( ArrayList<Point> expansionList )
  {
    Point initialPoint = expansionList.get( expansionList.size() - 1 );
    if( (initialPoint.x == 0 && initialPoint.y >= _gsEditor.getText().length()) ||
        initialPoint.x < 0 || initialPoint.y > _gsEditor.getText().length() + 1 )
    {
      while( initialPoint.x < 0 || initialPoint.y > _gsEditor.getText().length() + 1 )
      {
        expansionList.remove( expansionList.size() - 1 );
        initialPoint = expansionList.get( expansionList.size() - 1 );
      }
      return;
    }

    IParseTree spanningLocation = _gsEditor.getDeepestLocationSpanning( initialPoint.x, initialPoint.y );

    Point boundingPoint;
    if( spanningLocation != null )
    {
      int offset = spanningLocation.getOffset() + getOffsetShift();
      if( offset < initialPoint.x || initialPoint.y <= spanningLocation.getExtent() + getOffsetShift() )
      {
        boundingPoint = makePoint( spanningLocation );
      }
      else
      {
        IParseTree parent = spanningLocation.getParent();
        while( parent != null && parent.getOffset() + getOffsetShift() == offset && parent.getExtent() == spanningLocation.getExtent() )
        {
          IParseTree newParent = parent.getParent();
          if( parent == newParent )
          {
            break;
          }
          parent = newParent;
        }
        spanningLocation = parent;
        if( parent != null )
        {
          Point point = makePoint( parent );
          int lineAtStart = TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), point.x );
          int lineAtEnd = TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), point.y );
          if( lineAtStart != lineAtEnd )
          {
            int newX = TextComponentUtil.getLineStart( _gsEditor.getText(), point.x );
            if( GosuStringUtil.isWhitespace( _gsEditor.getText().substring( newX, point.x ) ) )
            {
              point.x = newX;
            }
          }
          boundingPoint = point;
        }
        else
        {
          boundingPoint = new Point( 0, _gsEditor.getText().length() + 1 );
        }
      }
    }
    else
    {
      boundingPoint = new Point( 0, _gsEditor.getText().length() + 1 );
    }

    int lineAtx = TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), initialPoint.x );
    int lineAty = TextComponentUtil.getLineAtPosition( _gsEditor.getEditor(), initialPoint.y );
    if( lineAtx == lineAty &&
        (initialPoint.x == initialPoint.y ||
         !GosuStringUtil.isWhitespace( _gsEditor.getText().substring( initialPoint.x, initialPoint.y - 1 ) )) )
    {
      int start = TextComponentUtil.getLineStart( _gsEditor.getText(), initialPoint.x );
      int end = TextComponentUtil.getLineEnd( _gsEditor.getText(), initialPoint.y );
      if( end < _gsEditor.getText().length() )
      {
        end++;
      }
      Point lineEndSelection = new Point( start, end );
      if( contains( boundingPoint, lineEndSelection ) && !contains( initialPoint, lineEndSelection ) &&
          !GosuStringUtil.isWhitespace( _gsEditor.getText().substring( lineEndSelection.x, lineEndSelection.y ) ) )
      {
        boundingPoint = lineEndSelection;
      }

      if( initialPoint.x == initialPoint.y )
      {
        int wordStart = getWordStart( _gsEditor.getEditor(), initialPoint.x );
        int wordEnd = getWordEnd( _gsEditor.getEditor(), initialPoint.y );
        if( wordStart < wordEnd )
        {
          Point wordSelection = new Point( wordStart, wordEnd );
          String possibleWord = _gsEditor.getText().substring( wordStart, wordEnd - 1 );
          if( contains( boundingPoint, wordSelection ) && !contains( initialPoint, wordSelection ) && isIdentifier( possibleWord ) )
          {
            boundingPoint = wordSelection;
          }
        }
        else
        {
          wordStart = getWordStart( _gsEditor.getEditor(), initialPoint.x - 1 );
          wordEnd = getWordEnd( _gsEditor.getEditor(), initialPoint.y - 1 );
          if( wordStart < wordEnd )
          {
            Point wordSelection = new Point( wordStart, wordEnd );
            String possibleWord = _gsEditor.getText().substring( wordStart, wordEnd - 1 );
            if( contains( boundingPoint, wordSelection ) && !contains( initialPoint, wordSelection ) && isIdentifier( possibleWord ) )
            {
              boundingPoint = wordSelection;
            }
          }
        }
      }
    }

    if( spanningLocation != null )
    {
      if( spanningLocation.getParsedElement() instanceof IStatementList )
      {
        Point withinStmtList = findNewLineWithinStatementList( spanningLocation );
        if( contains( boundingPoint, withinStmtList ) && !contains( initialPoint, withinStmtList ) )
        {
          boundingPoint = withinStmtList;
        }
      }
      if( spanningLocation.getParsedElement() instanceof IStringLiteralExpression )
      {
        Point justInsideStringLiteral = makePoint( spanningLocation );
        justInsideStringLiteral.x += 1;
        justInsideStringLiteral.y -= 1;
        if( contains( boundingPoint, justInsideStringLiteral ) &&
            !contains( initialPoint, justInsideStringLiteral ) &&
            contains( justInsideStringLiteral, initialPoint ) )
        {
          boundingPoint = justInsideStringLiteral;
        }
      }
    }

    if( !expansionList.contains( boundingPoint ) )
    {
      expansionList.add( boundingPoint );
      addNextSelection( expansionList );
    }
  }

  private Point findNewLineWithinStatementList( IParseTree spanningLocation )
  {
    int start = spanningLocation.getOffset() + getOffsetShift();
    int end = spanningLocation.getExtent() + getOffsetShift();
    int startEnd = TextComponentUtil.getLineEnd( _gsEditor.getText(), start );
    int endStart = TextComponentUtil.getLineStart( _gsEditor.getText(), end );
    startEnd++;

    java.util.List<IParseTree> list = spanningLocation.getChildren();
    if( list != null )
    {
      for( IParseTree parseTree : list )
      {
        if( parseTree.getOffset() + getOffsetShift() < startEnd )
        {
          startEnd = parseTree.getOffset() + getOffsetShift();
        }
        if( parseTree.getExtent() + getOffsetShift() + 1 > endStart )
        {
          endStart = parseTree.getExtent() + getOffsetShift() + 1;
        }
      }
    }
    return new Point( startEnd, endStart );
  }

  private boolean isIdentifier( String possibleWord )
  {
    char[] chars = possibleWord.toCharArray();
    for( char aChar : chars )
    {
      if( !Character.isJavaIdentifierPart( aChar ) )
      {
        return false;
      }
    }
    return true;
  }

  private int getWordEnd( JTextComponent editor, int y )
  {
    try
    {
      while( y < editor.getText().length() && Character.isJavaIdentifierPart( editor.getText( y, 1 ).charAt( 0 ) ) )
      {
        y++;
      }
      return y;
    }
    catch( BadLocationException e )
    {
      return 0;
    }

  }

  private int getWordStart( JTextComponent editor, int x )
  {
    try
    {
      while( x >= 0 && Character.isJavaIdentifierPart( editor.getText( x, 1 ).charAt( 0 ) ) )
      {
        x--;
      }
      return x + 1;
    }
    catch( BadLocationException e )
    {
      return 0;
    }
  }

  private boolean contains( Point point1, Point point2 )
  {
    return point1.x <= point2.x && point1.y >= point2.y;
  }

  private Point makePoint( IParseTree spanningLocation )
  {
    return new Point( Math.max( 0, spanningLocation.getOffset() + getOffsetShift() ), Math.min( _gsEditor.getText().length() + 1, spanningLocation.getExtent() + getOffsetShift() + 1 ) );
  }

  private int getOffsetShift()
  {
    return _gsEditor.getParser().getOffsetShift();
  }

  @Override
  public void caretUpdate( CaretEvent e )
  {
    if( !_updating )
    {
      _start = e.getDot();
      _expansionList = null;
      _expansionIndex = 0;
    }
  }
}
