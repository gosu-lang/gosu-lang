package editor;

import editor.debugger.BreakpointManager;
import editor.util.EditorUtilities;
import editor.util.SmartMenuItem;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 */
public abstract class AbstractLineInfoManager implements ILineInfoManager
{
  private static int g_iRequiredWidth;

  private ImageIcon _iconBreakpoint;
  private ImageIcon _iconBreakpointDisabled;

  public AbstractLineInfoManager()
  {
    _iconBreakpoint = EditorUtilities.loadIcon( "images/debug_lineBreakpoint.png" );
    _iconBreakpointDisabled = EditorUtilities.loadIcon( "images/disabled_Breakpoint.png" );
    g_iRequiredWidth = _iconBreakpoint.getIconWidth();
  }

  public int getRequiredWidth()
  {
    return g_iRequiredWidth;
  }

  public void render( Graphics g, int iLine, int iLineHeight, int iX, int iY )
  {
    Breakpoint bp = getBreakpointAtLine( iLine );
    if( bp == null )
    {
      return;
    }
    ImageIcon icon = (!getBreakpointManager().isMuted() && bp.isActive()) ? _iconBreakpoint : _iconBreakpointDisabled;
    g.drawImage( icon.getImage(), iX, iY + iLineHeight / 2 - icon.getIconHeight() / 2, icon.getIconWidth(), icon.getIconHeight(), null );
  }

  protected BreakpointManager getBreakpointManager()
  {
    return getGosuPanel().getBreakpointManager();
  }

  protected GosuPanel getGosuPanel()
  {
    return RunMe.getEditorFrame().getGosuPanel();
  }

  public void renderHighlight( GosuEditorPane editor, Graphics g, int iLine )
  {
    Color color = null;

    if( isExecPointAtLine( iLine ) )
    {
      color = Scheme.active().getExecBreakpoint();
    }
    else if( isBreakpointAtLine( iLine ) )
    {
      color = Scheme.active().breakpointColor();
    }
    else if( isFramePointAtLine( iLine ) )
    {
      color = Scheme.active().getFrameBreakpoint();
    }

    if( color == null )
    {
      return;
    }

    FontMetrics fm = g.getFontMetrics( editor.getFont() );
    int iLineHeight = fm.getHeight();

    Element root = editor.getDocument().getRootElements()[0];
    Element elemLine = root.getElement( iLine - 1 );
    try
    {
      g.setColor( color );
      Rectangle rcLineBounds = editor.modelToView( elemLine.getStartOffset() );
      rcLineBounds.x = 0;
      rcLineBounds.width = editor.getWidth();
      rcLineBounds.height = iLineHeight;
      g.fillRect( rcLineBounds.x, rcLineBounds.y, rcLineBounds.width, rcLineBounds.height );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  public Cursor getCursor( int iLine )
  {
    return null;
  }

  public void showContextMenu( final MouseEvent e, final int iLine )
  {
    final Breakpoint bp = getBreakpointAtLine( iLine );
    if( bp == null )
    {
      return;
    }
    JPopupMenu contextMenu = new JPopupMenu();
    JMenuItem disableItem = new SmartMenuItem(
      new AbstractAction( bp.isActive() ? "Disable" : "Enable" )
      {
        public void actionPerformed( ActionEvent e )
        {
          bp.setActive( !bp.isActive() );
          BreakpointManager bmp = RunMe.getEditorFrame().getGosuPanel().getBreakpointManager();
          Breakpoint csr = bmp.getBreakpoint( bp );
          if( csr != null )
          {
            csr.setActive( bp.isActive() );
          }
          GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().getGosuEditor();
          if( editor != null )
          {
            java.util.List<? extends JComponent> columns = EditorUtilities.findDecendents( editor, EditorScrollPane.AdviceColumn.class );
            for( JComponent column : columns )
            {
              column.repaint();
            }
          }
        }
      } );
    contextMenu.add( disableItem );
    JMenuItem removeItem = new SmartMenuItem(
      new AbstractAction( "Remove" )
      {
        public void actionPerformed( ActionEvent e )
        {
          BreakpointManager bmp = getGosuPanel().getBreakpointManager();
          bmp.toggleLineBreakpoint( getGosuPanel().getCurrentEditor().getScriptPart().getContainingTypeName(), iLine );
          GosuEditor editor = RunMe.getEditorFrame().getGosuPanel().getGosuEditor();
          if( editor != null )
          {
            editor.repaint();
          }
        }
      } );
    contextMenu.add( removeItem );
    contextMenu.addSeparator();
    contextMenu.add( CommonMenus.makeViewBreakpoints( () -> bp ) );
    contextMenu.add( CommonMenus.makeMuteBreakpoints( this::getBreakpointManager ) );

    contextMenu.show( e.getComponent(), e.getX(), e.getY() );
  }

  protected abstract boolean isBreakpointAtLine( int iLine );
  protected abstract Breakpoint getBreakpointAtLine( int iLine );

  protected abstract boolean isExecPointAtLine( int iLine );
  protected abstract Breakpoint getExecPointAtLine( int iLine );

  protected abstract boolean isFramePointAtLine( int iLine );
  protected abstract Breakpoint getFramePointAtLine( int iLine );
}
