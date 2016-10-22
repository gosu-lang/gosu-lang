package editor.debugger;

import editor.Scheme;
import editor.util.EditorUtilities;
import editor.util.LabCheckbox;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
*/
class BreakpointCellRenderer extends JPanel implements TreeCellRenderer
{
  private final JTree _tree;
  private BreakpointTree _bpTree;
  private LabCheckbox _cbActive;
  private JLabel _label;
  private boolean _selected;

  public BreakpointCellRenderer( JTree tree )
  {
    super( new BorderLayout() );
    _tree = tree;
    configUi();
  }

  public LabCheckbox getCheckbox()
  {
    return _cbActive;
  }

  private void configUi()
  {
    setOpaque( false );

    _cbActive = new LabCheckbox();
    _cbActive.setBorderPaintedFlat( true );
    _cbActive.setOpaque( false );
    _cbActive.setVisible( false );

    add( _cbActive, BorderLayout.WEST );

    _label = new JLabel();
    _label.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 5 ) );
    _label.setOpaque( false );
    add( _label, BorderLayout.CENTER );
  }

  void setBreakpoint( BreakpointTree bpTree )
  {
    _bpTree = bpTree;
  }

  @Override
  public Component getTreeCellRendererComponent( JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus )
  {
    _selected = selected;
    BreakpointTree bpTree = (BreakpointTree)value;
    _label.setText( bpTree.toString() );
    _label.setIcon( bpTree.getBreakpoint() != null
             ? bpTree.getBreakpoint().isActive()
               ? EditorUtilities.loadIcon( "images/debug_linebreakpoint.png" )
               : EditorUtilities.loadIcon( "images/disabled_breakpoint.png" )
             : null );
    _label.setEnabled( bpTree.getBreakpoint() == null || bpTree.getBreakpoint().isActive() );
    setBreakpoint( bpTree );
    Breakpoint bp = _bpTree.getBreakpoint();
    if( bp != null )
    {
      _cbActive.setVisible( true );
      _cbActive.setSelected( bp.isActive() );
      revalidate();
    }
    else
    {
      _cbActive.setVisible( false );
      revalidate();
    }
    return this;
  }

  /** */
  public void paint( Graphics g )
  {
    Color bkColor;

    boolean bFocus = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner() == _tree;
    if( _selected )
    {
      bkColor = _tree.isEnabled() && bFocus
                ? Scheme.active().getActiveCaption()
                : Scheme.active().getControl();
    }
    else
    {
      bkColor = _tree.getBackground();
      if( bkColor == null )
      {
        bkColor = getBackground();
      }
    }

    if( bkColor != null )
    {
      g.setColor( bkColor );
      g.fillRect( 0, 0, getWidth() - 1, getHeight() - 1 );

      if( _selected )
      {
        g.setColor( _tree.isEnabled() && bFocus ? Scheme.active().getXpBorderColor() : Scheme.active().getControlShadow() );
        g.drawRect( 0, 0, getWidth() - 1, getHeight() - 1 );
      }
      g.setColor( bkColor );
    }

    setForeground( Scheme.active().getWindowText() );

    super.paint( g );
  }
}
