package editor;

import editor.debugger.Breakpoint;
import editor.debugger.BreakpointManager;
import editor.debugger.BreakpointsDialog;
import editor.util.EditorUtilities;
import editor.util.SmartMenuItem;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.text.JTextComponent;

/**
 */
public abstract class AbstractLineInfoManager implements ILineInfoManager
{
  private static int g_iRequiredWidth;

  private ImageIcon _iconBreakpoint;
  private ImageIcon _iconBreakpointDisabled;
  private EditorHost _editor;

  public AbstractLineInfoManager()
  {
    _iconBreakpoint = EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" );
    _iconBreakpointDisabled = EditorUtilities.loadIcon( "images/disabled_breakpoint.png" );
    g_iRequiredWidth = _iconBreakpoint.getIconWidth();
  }

  public void setEditor( EditorHost gosuEditor )
  {
    _editor = gosuEditor;
  }
  public EditorHost getEditor()
  {
    return _editor;
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
    return LabFrame.instance().getGosuPanel();
  }

  public void renderHighlight( Graphics g, int iLine )
  {
    Color color = null;

    if( isExecPointAtLine( iLine ) )
    {
      color = Scheme.active().getExecBreakpoint();
    }
    else if( isBreakpointAtLine( iLine ) )
    {
      color = Scheme.active().getBreakpointColor();
    }
    else if( isFramePointAtLine( iLine ) )
    {
      color = Scheme.active().getFrameBreakpoint();
    }

    if( color == null )
    {
      return;
    }

    JTextComponent editor = getEditor().getEditor();

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
    if( BreakpointsDialog.getShowing() != null )
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
          BreakpointManager bmp = LabFrame.instance().getGosuPanel().getBreakpointManager();
          Breakpoint csr = bmp.getBreakpoint( bp );
          if( csr != null )
          {
            csr.setActive( bp.isActive() );
          }
          EditorHost editor = getEditor();
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
          bmp.toggleLineBreakpoint( getEditor(), getEditor().getScriptPart().getContainingTypeName(), getEditor().getTypeAtLine( iLine ), iLine );
          getEditor().repaint();
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
