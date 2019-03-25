package editor;

import javax.swing.plaf.TextUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.View;
import java.awt.*;

public abstract class GosuLabTextUI extends BasicTextUI
{
  protected javax.swing.text.Caret createCaret()
  {
    return new Caret();
  }

  static LayeredHighlighter.LayerPainter WindowsPainter = new WindowsHighlightPainter( null );

  static class Caret extends DefaultCaret implements UIResource
  {
    protected Highlighter.HighlightPainter getSelectionPainter()
    {
      return GosuLabTextUI.WindowsPainter;
    }
  }

  static class WindowsHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
  {
    WindowsHighlightPainter( Color c )
    {
      super( c );
    }

    public void paint( Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c )
    {
      Rectangle alloc = bounds.getBounds();
      try
      {
        TextUI mapper = c.getUI();
        Rectangle p0 = mapper.modelToView( c, offs0 );
        Rectangle p1 = mapper.modelToView( c, offs1 );

        Color color = getColor();

        if( color == null )
        {
          g.setColor( c.getSelectionColor() );
        }
        else
        {
          g.setColor( color );
        }
        boolean firstIsDot = false;
        boolean secondIsDot = false;
        if( c.isEditable() )
        {
          int dot = c.getCaretPosition();
          firstIsDot = (offs0 == dot);
          secondIsDot = (offs1 == dot);
        }
        if( p0.y == p1.y )
        {
          Rectangle r = p0.union( p1 );
          if( r.width > 0 )
          {
            if( firstIsDot )
            {
              r.x++;
              r.width--;
            }
            else if( secondIsDot )
            {
              r.width--;
            }
          }
          g.fillRect( r.x, r.y, r.width, r.height );
        }
        else
        {
          int p0ToMarginWidth = alloc.x + alloc.width - p0.x;
          if( firstIsDot && p0ToMarginWidth > 0 )
          {
            p0.x++;
            p0ToMarginWidth--;
          }
          g.fillRect( p0.x, p0.y, p0ToMarginWidth, p0.height );
          if( (p0.y + p0.height) != p1.y )
          {
            g.fillRect( alloc.x, p0.y + p0.height, alloc.width,
                        p1.y - (p0.y + p0.height) );
          }
          if( secondIsDot && p1.x > alloc.x )
          {
            p1.x--;
          }
          g.fillRect( alloc.x, p1.y, (p1.x - alloc.x), p1.height );
        }
      }
      catch( BadLocationException ignore )
      {
      }
    }

    public Shape paintLayer( Graphics g, int offs0, int offs1,
                             Shape bounds, JTextComponent c, View view )
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
      boolean firstIsDot = false;
      boolean secondIsDot = false;
      if( c.isEditable() )
      {
        int dot = c.getCaretPosition();
        firstIsDot = (offs0 == dot);
        secondIsDot = (offs1 == dot);
      }
      if( offs0 == view.getStartOffset() &&
          offs1 == view.getEndOffset() )
      {
        Rectangle alloc;
        if( bounds instanceof Rectangle )
        {
          alloc = (Rectangle)bounds;
        }
        else
        {
          alloc = bounds.getBounds();
        }
        if( firstIsDot && alloc.width > 0 )
        {
          g.fillRect( alloc.x + 1, alloc.y, alloc.width - 1,
                      alloc.height );
        }
        else if( secondIsDot && alloc.width > 0 )
        {
          g.fillRect( alloc.x, alloc.y, alloc.width - 1,
                      alloc.height );
        }
        else
        {
          g.fillRect( alloc.x, alloc.y, alloc.width, alloc.height );
        }
        return alloc;
      }
      else
      {
        try
        {
          Shape shape = view.modelToView( offs0, Position.Bias.Forward,
                                          offs1, Position.Bias.Backward,
                                          bounds );
          Rectangle r = (shape instanceof Rectangle) ?
                        (Rectangle)shape : shape.getBounds();
          if( firstIsDot && r.width > 0 )
          {
            g.fillRect( r.x + 1, r.y, r.width - 1, r.height );
          }
          else if( secondIsDot && r.width > 0 )
          {
            g.fillRect( r.x, r.y, r.width - 1, r.height );
          }
          else
          {
            g.fillRect( r.x, r.y, r.width, r.height );
          }
          return r;
        }
        catch( BadLocationException ignore )
        {
        }
      }
      return null;
    }
  }
}