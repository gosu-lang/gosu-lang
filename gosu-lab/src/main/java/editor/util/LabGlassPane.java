package editor.util;

/**
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * GlassPane for main studio frame. If a component is added with addModalProgressDisplay, the glasspane is shown,
 * and the application essentially becomes disabled (modal) until all components are removed.

 */
public class LabGlassPane extends JPanel
{

  private static final LabGlassPane INSTANCE = new LabGlassPane();

  private final Set<Component> _modalComponents = new HashSet<>();
  private Component _recentFocusOwner;
  private boolean _firstTime = true;

  public static LabGlassPane getInstance()
  {
    return INSTANCE;
  }

  private LabGlassPane()
  {
    super( null );
    setOpaque( false );
    addComponentListener( new ComponentAdapter()
    {
      @Override
      public void componentResized( ComponentEvent e )
      {
        for( Component component : _modalComponents )
        {
          centerComponent( component );
        }
      }
    } );
  }

  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );
    if( !_modalComponents.isEmpty() )
    {
      JRootPane rootPane = SwingUtilities.getRootPane( this );
      if( rootPane != null )
      {
        rootPane.getLayeredPane().print( g );
      }
    }
  }

  public void addModalComponent( Component component )
  {
    if( component == null || !_modalComponents.add( component ) )
    {
      return; // already added
    }
    add( component );
    centerComponent( component );
    validate();
    hideLayeredPaneAndShowThisPane( true );
  }

  public void removeModalComponent( Component component )
  {
    if( component == null || !_modalComponents.remove( component ) )
    {
      return; // does not exist
    }
    remove( component );
    hideLayeredPaneAndShowThisPane( !_modalComponents.isEmpty() );
  }

  private void centerComponent( Component component )
  {
    component.setLocation( (getWidth() - component.getWidth()) / 2,
                           (getHeight() - component.getHeight()) / 2 );
  }

  public void hideLayeredPaneAndShowThisPane( boolean bShowThisPane )
  {
    boolean oldVisible = isVisible();
    setVisible( bShowThisPane );
    JRootPane rootPane = SwingUtilities.getRootPane( this );
    if( rootPane != null && (_firstTime || isVisible() != oldVisible) )
    {
      _firstTime = false;
      if( bShowThisPane )
      {
        Component focusOwner = KeyboardFocusManager.
          getCurrentKeyboardFocusManager().getPermanentFocusOwner();
        if( focusOwner != null &&
            SwingUtilities.isDescendingFrom( focusOwner, rootPane ) )
        {
          _recentFocusOwner = focusOwner;
        }
        rootPane.getLayeredPane().setVisible( false );
        requestFocusInWindow();
      }
      else
      {
        rootPane.getLayeredPane().setVisible( true );
        if( _recentFocusOwner != null )
        {
          _recentFocusOwner.requestFocusInWindow();
        }
        _recentFocusOwner = null;
      }
    }
    if( bShowThisPane )
    {
      repaint();
    }
  }

}
