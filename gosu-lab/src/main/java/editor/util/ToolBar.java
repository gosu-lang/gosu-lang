package editor.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 */
public class ToolBar extends JToolBar
{
  Border _border;

  /**
   * Creates a toolbar that is not floatable.
   */
  public ToolBar()
  {
    this( JToolBar.HORIZONTAL );
  }

  public ToolBar( int iOrientation )
  {
    super( iOrientation );

    setFloatable( false );
    setMinimumSize( new Dimension( 0, getPreferredSize().height ) );

    _border = getBorder();
  }

  public void setDynamicBorder( Border border )
  {
    _border = border;
  }

  public void setBounds( int x, int y, int width, int height )
  {
    super.setBounds( x, y, width, height );

    if( width < height )
    {
      setBorder( null );
    }
    else
    {
      setBorder( _border );
    }
  }

  public AbstractButton findButtonWithAction( String actionId )
  {
    int numKids = getComponentCount();
    for( int i = 0; i < numKids; i++ )
    {
      Component kid = getComponent( i );
      if( kid instanceof AbstractButton && ((AbstractButton)kid).getAction() != null &&
          actionId.equals( ((AbstractButton)kid).getAction().getValue( Action.ACTION_COMMAND_KEY ) ) )
      {
        return (AbstractButton)kid;
      }
    }

    return null;
  }
}
