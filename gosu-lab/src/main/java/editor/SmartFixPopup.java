package editor;

import javax.swing.*;
import java.awt.*;


/**
 * @author cgross
 */
public class SmartFixPopup extends JPopupMenu
{

  private JComponent _editor;

  public SmartFixPopup( String displayText )
  {
    initUI( displayText );
  }

  private void initUI( String displayText )
  {
    JLabel label = new JLabel( displayText );
    JPanel panel = new JPanel( true );
    label.setFont( label.getFont().deriveFont( Font.BOLD ) );
    panel.setBackground( new Color( 181, 208, 251 ) );
    panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
    panel.setBorder( BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
    panel.add( label );
    add( panel );
  }

  public void show( JComponent editor, Rectangle offsetRectangle )
  {
    _editor = editor;
    if( _editor.isShowing() )
    {
      show( _editor, offsetRectangle.x, offsetRectangle.y - 25 );
    }
  }

  @Override
  public void setVisible( boolean bVisible )
  {
    Component focusOwner = DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
    super.setVisible( bVisible );
    if( bVisible && _editor != null )
    {
      if( focusOwner == null || editor.util.EditorUtilities.findAncestor( focusOwner, GosuEditor.class ) == null )
      {
        _editor.requestFocus();
      }
      else
      {
        focusOwner.requestFocus();
      }
    }
  }

}