package editor;

import gw.lang.parser.IParseTree;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MouseInEditorHandler implements MouseListener, MouseMotionListener, KeyListener
{
  private static final UnderlineHighlighterPainter PAINTER = new UnderlineHighlighterPainter( Color.BLACK );
  private final GosuEditor _editor;
  private Cursor _previousCursor;

  public MouseInEditorHandler( GosuEditor editor )
  {
    assert editor != null;
    _editor = editor;
  }

  @Override
  public void mouseClicked( MouseEvent e )
  {
    if( e.getButton() == MouseEvent.BUTTON1 && (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK )
    {
      // Control-click
      _editor.gotoDeclaration();
    }
  }

  @Override
  public void mouseMoved( MouseEvent event )
  {
    removeHighlights();

    JTextComponent editorComponent = _editor.getEditor();
    Highlighter highlighter = editorComponent.getHighlighter();

    if( (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK )
    {
      if( _previousCursor == null )
      {
        _previousCursor = event.getComponent().getCursor();
        event.getComponent().setCursor( Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
      }
      IParseTree deepestStatementLocationAtPos = _editor.getDeepestLocation( event.getPoint() );
      if( deepestStatementLocationAtPos != null )
      {
//        try {
//          int length = TypeInfoDatabaseStudioUtil.calculateLengthOfHighlight(editorComponent, deepestStatementLocationAtPos);
//          highlighter.addHighlight(deepestStatementLocationAtPos.getOffset(),
//                                   deepestStatementLocationAtPos.getOffset() + length,
//                                   PAINTER);
//        } catch (BadLocationException e) {
//          // Ignore
//        }
      }
    }
    else
    {
      if( _previousCursor != null )
      {
        event.getComponent().setCursor( _previousCursor );
        _previousCursor = null;
      }
    }
  }

  @Override
  public void mousePressed( MouseEvent e )
  {
    if( e.getModifiers() == MouseEvent.BUTTON1_MASK && e.getClickCount() == 2 )
    {
      _editor.selectWordForMouseClick();
      e.consume();
    }
  }

  @Override
  public void mouseReleased( MouseEvent e )
  {
  }

  @Override
  public void mouseEntered( MouseEvent e )
  {
  }

  @Override
  public void mouseExited( MouseEvent e )
  {
  }

  @Override
  public void mouseDragged( MouseEvent e )
  {
  }

  @Override
  public void keyTyped( KeyEvent e )
  {
  }

  @Override
  public void keyPressed( KeyEvent e )
  {
  }

  @Override
  public void keyReleased( KeyEvent e )
  {
    removeHighlights();
  }

  private void removeHighlights()
  {
    JTextComponent editorComponent = _editor.getEditor();
    Highlighter highlighter = editorComponent.getHighlighter();

    // Always clean up previous underlines
    Highlighter.Highlight[] highlights = highlighter.getHighlights();
    for( Highlighter.Highlight highlight : highlights )
    {
      if( highlight.getPainter() == PAINTER )
      {
        highlighter.removeHighlight( highlight );
      }
    }
  }

  private static class UnderlineHighlighterPainter extends DefaultHighlighter.DefaultHighlightPainter
  {
    private static final int HEIGHT_PIXELS = 1;

    /**
     * @param color
     */
    public UnderlineHighlighterPainter( Color color )
    {
      super( color );
    }

    /**
     * Paints a highlight over a line just uses the first point a work around only
     */
    @Override
    public Shape paintLayer( Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view )
    {
      Color color = getColor();
      if( color == null )
      {
        g.setColor( c.getSelectionColor() );
      }
      else
      {
        g.setColor( color );
      }

      return paintUnderline( g, offs0, offs1, c, color );
    }

    public void undoUnderline( Highlighter.Highlight highlight, JTextComponent editor )
    {
      paintUnderline( editor.getGraphics(), highlight.getStartOffset(), highlight.getEndOffset(), editor, editor.getBackground() );
    }

    private Rectangle paintUnderline( Graphics g, int startOffset, int endOffset, JTextComponent editor, Color c )
    {
      try
      {
        Rectangle p0 = editor.modelToView( startOffset );
        Rectangle p1 = editor.modelToView( endOffset );

        Rectangle underline = new Rectangle( p0.x, p0.y + p0.height - HEIGHT_PIXELS, p1.x - p0.x, HEIGHT_PIXELS );
        fillRectangle( g, underline );
        return underline;
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }

    private void fillRectangle( Graphics g, Rectangle underline )
    {
      g.fillRect( underline.x, underline.y, underline.width, underline.height );
    }
  }
}