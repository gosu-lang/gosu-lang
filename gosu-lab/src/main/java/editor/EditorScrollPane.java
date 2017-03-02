package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


/**
 */
public class EditorScrollPane extends JScrollPane
{
  private AdviceColumn _adviceColumn;
  private JTextComponent _editor;
  private ILineInfoManager _lineInfoMgr;

  public EditorScrollPane( ILineInfoManager lineInfoMgr, JTextComponent editor, JComponent view, int iVertPolicy, int iHorzPolicy )
  {
    super( view, iVertPolicy, iHorzPolicy );
    _lineInfoMgr = lineInfoMgr;
    _editor = editor;
    _adviceColumn = new AdviceColumn();
    setRowHeaderView( _adviceColumn );
    setBackground( Scheme.active().getControl() );
  }

  public EditorScrollPane( ILineInfoManager lineInfoRenderer, JTextComponent editor, JComponent view )
  {
    this( lineInfoRenderer, editor, view, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED );
  }

  @Override
  public void setViewportBorder( javax.swing.border.Border border )
  {
    super.setViewportBorder( border );

    Insets insets = new Insets( 0, 0, 0, 0 );

    if( border != null )
    {
      insets = border.getBorderInsets( this );
    }

    // Adjust the ruler panes to align with the client area of the viewport
    if( _adviceColumn != null )
    {
      _adviceColumn.setBorder( new EmptyBorder( new Insets( insets.top, 0, 0, 1 ) ) );
    }
  }

  public ILineInfoManager getLineInfoMgr()
  {
    return _lineInfoMgr;
  }

  public AdviceColumn getAdviceColumn()
  {
    return _adviceColumn;
  }

  /**
   */
  public class AdviceColumn extends JPanel
  {
    public AdviceColumn()
    {
      setLayout( null );
      setBorder( new EmptyBorder( new Insets( 0, 0, 0, 1 ) ) );
      setBackground( Scheme.active().getControl() );
      addMouseListener( new MouseAdapter()
      {
        @Override
        public void mouseReleased( MouseEvent e )
        {
          handleMouseClicked( e );
        }
      } );
      addMouseMotionListener(
        new MouseMotionAdapter()
        {
          @Override
          public void mouseMoved( MouseEvent e )
          {
            updateCursor( e );
          }
        } );
    }

    @Override
    public Dimension getPreferredSize()
    {
      Dimension dim = new Dimension();
      dim.height = _editor.getHeight();

      FontMetrics fm = _editor.getFontMetrics( _editor.getFont() );
      dim.width = fm.stringWidth( String.valueOf( dim.height / fm.getHeight() + 1 ) );
      dim.width = Math.max( dim.width, GosuEditor.MIN_LINENUMBER_WIDTH );
      dim.width += getLineInfoRequiredWidth();

      return dim;
    }

    @Override
    public void paintComponent( Graphics g )
    {
      super.paintComponent( g );

      g.setColor( Scheme.active().getControlShadow() );
      g.setFont( _editor.getFont() );

      FontMetrics fm = g.getFontMetrics( _editor.getFont() );
      int iLineHeight = fm.getHeight();
      iLineHeight += getLineSpacingAttr( iLineHeight );
      int iMargin = _editor.getMargin().top + getLineSpacingAttr( iLineHeight );
      int iLines = getHeight() / iLineHeight;
      for( int i = 1; i <= iLines; i++ )
      {
        String strLine = String.valueOf( i );
        int iWidth = fm.stringWidth( strLine );

        g.drawString( strLine, getWidth() - iWidth - getLineInfoRequiredWidth(), i * iLineHeight - fm.getDescent() + iMargin );
        renderLineInfo( g, i, iLineHeight, getWidth() - getLineInfoRequiredWidth(), (i - 1) * iLineHeight + iMargin );
      }
    }

    private int getLineSpacingAttr( int iLineHeight )
    {
      if( _editor instanceof JTextPane )
      {
        AttributeSet attr = ((JTextPane)_editor).getParagraphAttributes();
        Float lineSpacing = (Float)attr.getAttribute( StyleConstants.LineSpacing );
        if( lineSpacing != null )
        {
          return Math.round( lineSpacing * iLineHeight );
        }
      }
      return 0;
    }

    private int getLineInfoRequiredWidth()
    {
      if( _lineInfoMgr != null )
      {
        return _lineInfoMgr.getRequiredWidth();
      }

      return 0;
    }

    private void renderLineInfo( Graphics g, int iLine, int iLineHeight, int iX, int iY )
    {
      if( _lineInfoMgr == null )
      {
        return;
      }

      _lineInfoMgr.render( g, iLine, iLineHeight, iX, iY );
    }

    private void handleMouseClicked( MouseEvent e )
    {
      _editor.requestFocus();
      if( _lineInfoMgr != null )
      {
        //noinspection deprecation
        FontMetrics fm = getToolkit().getFontMetrics( _editor.getFont() );
        int iLineHeight = fm.getHeight();

        int iLine = (e.getY() - 2) / iLineHeight + 1;
        int iY = e.getY() - ((iLine - 1) * iLineHeight);
        int iX = e.getX() - (getWidth() - getLineInfoRequiredWidth());
        _lineInfoMgr.handleLineClick( e, (e.getY() - 2) / iLineHeight + 1, iX, iY );
        repaint();
        _editor.repaint();
      }
    }

    private void updateCursor( MouseEvent e )
    {
      //noinspection deprecation
      FontMetrics fm = getToolkit().getFontMetrics( _editor.getFont() );
      int iLineHeight = fm.getHeight();

      if( _lineInfoMgr != null )
      {
        setCursor( _lineInfoMgr.getCursor( (e.getY() - 2) / iLineHeight + 1 ) );
      }
    }
  }
}
