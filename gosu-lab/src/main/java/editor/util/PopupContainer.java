package editor.util;

import editor.search.StudioUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 */
public abstract class PopupContainer extends JPopupMenu
{
  /**
   * Subclasses override this to add the content panel to the popup.
   */
  protected abstract Component getContentPanel();

  protected void initLayout( String strTitle )
  {
    setOpaque( false );
    setDoubleBuffered( true );

    JPanel pane = new JPanel();
    pane.setLayout( new BorderLayout() );

    Border border = BorderFactory.createCompoundBorder(
      UIManager.getBorder( "PopupMenu.border" ),
      BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
    ContainerMoverSizer content = new ContainerMoverSizer( border );
    content.setLayout( new BorderLayout() );

    JLabel labelTitle = new JLabel( strTitle );
    pane.add( labelTitle, BorderLayout.NORTH );
    pane.add( getContentPanel(), BorderLayout.CENTER );

    content.add( pane, BorderLayout.CENTER );
    add( content );

    JPanel sizerPanel = new JPanel( new BorderLayout() );
    sizerPanel.add( new JPanel(), BorderLayout.CENTER );
    sizerPanel.add( new ContainerSizer(), BorderLayout.EAST );
    content.add( sizerPanel, BorderLayout.SOUTH );
  }

  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      StudioUtilities.removePopupBorder( this );
      transferFocus();
    }
  }
}

