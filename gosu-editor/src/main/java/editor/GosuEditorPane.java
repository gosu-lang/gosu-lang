package editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 */
public class GosuEditorPane extends JEditorPane
{
  private GosuEditor _gosuEditor;

  public GosuEditorPane( GosuEditor gosuEditor )
  {
    _gosuEditor = gosuEditor;
  }

  public GosuEditor getGosuEditor()
  {
    return _gosuEditor;
  }

  @Override
  public void setHighlighter( Highlighter h )
  {
    super.setHighlighter( new GosuHighlighter( (LayeredHighlighter)h ) );
  }

  @Override
  public Dimension getPreferredSize()
  {
    Dimension dim = super.getPreferredSize();
    dim.width = 0;
    Element root = getDocument().getDefaultRootElement();
    for( int i = 0; i < root.getElementCount(); i++ )
    {
      Element line = root.getElement( i );
      try
      {
        String strLine = getDocument().getText( line.getStartOffset(), line.getEndOffset() - line.getStartOffset() );
        int iWidth = getFontMetrics( getFont() ).stringWidth( strLine ) + 50;
        dim.width = Math.max( iWidth, dim.width );
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
    return dim;
  }

  @Override
  public String getToolTipText( MouseEvent event )
  {
    try
    {
      return _gosuEditor.getTooltipMessage( event );
    }
    catch( ArrayIndexOutOfBoundsException ex )
    {
      // had this happen randomly during a demo... let's not have it happen again.
      return "";
    }
  }

  @Override
  public void paint( Graphics g )
  {
    if( ((GosuEditorKit)getEditorKit()).getViewFactory().isAntialias() )
    {
      ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
    }
    super.paint( g );
  }

  private void paintBreakpoints( Graphics g, Rectangle rcClipBounds )
  {
    Rectangle rc = rcClipBounds == null ? g.getClipBounds() : rcClipBounds;
    int[] lines = getLinesInClipBounds( rc );
    for( int i = lines[0]; i <= lines[1]; i++ )
    {
      EditorScrollPane scroller = _gosuEditor.getScroller();
      if( scroller != null )
      {
        ILineInfoManager lineInfoMgr = scroller.getLineInfoMgr();
        if( lineInfoMgr != null )
        {
          lineInfoMgr.renderHighlight( this, g, i );
        }
      }
    }
  }

  private int[] getLinesInClipBounds( Rectangle rc )
  {
    int iOffset = viewToModel( new Point( rc.x, rc.y ) );
    int iExtent = viewToModel( new Point( rc.x + rc.width, rc.y + rc.height ) );
    Element root = getDocument().getRootElements()[0];
    int iStartLine = 1 + root.getElementIndex( iOffset );
    int iEndLine = 1 + root.getElementIndex( iExtent );
    return new int[]{iStartLine, iEndLine};
  }

  @Override
  public void setCaretPosition( int position )
  {
    try
    {
      Rectangle newCaretRect = modelToView( position );
      if( newCaretRect != null )
      {
        newCaretRect.grow( 0, 100 );
//        scrollRectToVisible( newCaretRect );
      }
      super.setCaretPosition( position );
    }
    catch( BadLocationException e )
    {
      if( getDocument().getLength() < position )
      {
        setCaretPosition( getDocument().getLength() );
      }
    }
  }

  @Override
  public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction )
  {
    if( !GraphicsEnvironment.isHeadless() && orientation == SwingConstants.VERTICAL )
    {
      return getFontMetrics( getFont() ).getHeight();
    }
    else
    {
      return super.getScrollableUnitIncrement( visibleRect, orientation, direction );
    }
  }

  @Override
  public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction )
  {
    return super.getScrollableBlockIncrement( visibleRect, orientation, direction ) - getFontMetrics( getFont() ).getHeight() + 1;
  }

  void simulateKeyEvent( KeyEvent e )
  {
    processKeyEvent( e );
  }

  /**
   * Override the default highlighter to render line breakpoints. Adding normal
   * highlight painters for these is otherwise rather clumsy e.g., have to move
   * them when text changes, recreate them after closing and reopening views, etc.
   */
  private class GosuHighlighter extends LayeredHighlighter
  {
    private LayeredHighlighter _highlighter;

    private GosuHighlighter( LayeredHighlighter h )
    {
      _highlighter = h;
    }

    @Override
    public void install( JTextComponent c )
    {
      _highlighter.install( c );
    }

    @Override
    public void deinstall( JTextComponent c )
    {
      _highlighter.deinstall( c );
    }

    @Override
    public void paint( Graphics g )
    {
      _highlighter.paint( g );

      // Paint only non-layered highlights here i.e., highlights that paint on top of (and hide) text
    }

    @Override
    public void paintLayeredHighlights( Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view )
    {
      Shape saveClip = g.getClip();
      Rectangle rc = viewBounds.getBounds();
      g.clipRect( 0, rc.y, getWidth(), rc.height );
      paintBreakpoints( g, rc );
      g.setClip( saveClip );

      // Paint these last so that selection highlights paint over breakpoint highlights
      _highlighter.paintLayeredHighlights( g, p0, p1, viewBounds, editor, view );
    }

    @Override
    public Object addHighlight( int p0, int p1, HighlightPainter p ) throws BadLocationException
    {
      return _highlighter.addHighlight( p0, p1, p );
    }

    @Override
    public void removeHighlight( Object tag )
    {
      _highlighter.removeHighlight( tag );
    }

    @Override
    public void removeAllHighlights()
    {
      _highlighter.removeAllHighlights();
    }

    @Override
    public void changeHighlight( Object tag, int p0, int p1 ) throws BadLocationException
    {
      _highlighter.changeHighlight( tag, p0, p1 );
    }

    @Override
    public Highlight[] getHighlights()
    {
      return _highlighter.getHighlights();
    }
  }
}