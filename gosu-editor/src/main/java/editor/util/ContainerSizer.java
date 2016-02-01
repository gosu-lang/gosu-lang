package editor.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 */
public class ContainerSizer extends JComponent
{
  private static ImageIcon g_grabber;


  public ContainerSizer()
  {
    super();
    configUi();
  }

  public void paint( Graphics g )
  {
    super.paint( g );
    // Always paint grabber image in bottom right corner
    g_grabber.paintIcon( this, g,
                         getWidth() - g_grabber.getIconWidth(),
                         getHeight() - g_grabber.getIconHeight() );
  }

  private void configUi()
  {
    setCursor( Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR ) );
    g_grabber = editor.util.EditorUtilities.loadIcon( "images/status_grabber.gif" );
    g_grabber.setImage( editor.util.EditorUtilities.createSystemColorImage( g_grabber.getImage() ) );
    setPreferredSize( new Dimension( g_grabber.getIconWidth(), g_grabber.getIconHeight() ) );
    setOpaque( true );
    setBackground( editor.util.EditorUtilities.CONTROL );
    DragController controller = new DragController();
    addMouseListener( controller );
    addMouseMotionListener( controller );
  }

  private class DragController extends MouseAdapter implements MouseMotionListener
  {
    private boolean _bDragging;
    private Point _ptInitialDragLocation;
    private Container _container;

    public void mousePressed( MouseEvent e )
    {
      setInitialDragLocation( e.getPoint() );
      setDragging( true );
    }

    public void mouseReleased( MouseEvent e )
    {
      if( !isDragging() )
      {
        return;
      }
      setDragging( false );
    }

    public void mouseDragged( MouseEvent e )
    {
      resize( e.getPoint() );
    }

    public void mouseMoved( MouseEvent e )
    {
    }

    private void resize( Point pt )
    {
      Container container = getContainer();
      int iNewWidth = container.getWidth() + pt.x - getInitialDragLocation().x;
      int iNewHeight = container.getHeight() + pt.y - getInitialDragLocation().y;
      container.setSize( iNewWidth, iNewHeight );
      container.validate();
    }

    private Container getContainer()
    {
      if( _container != null )
      {
        return _container;
      }

      for( Container p = getParent(); p != null; p = p.getParent() )
      {
        if( p.getParent() instanceof JLayeredPane && p.getParent().getWidth() > p.getWidth() )
        {
          _container = p;
          break;
        }
        else if( p instanceof Window )
        {
          _container = p;
          break;
        }
      }

      return _container;
    }

    private boolean isDragging()
    {
      return _bDragging;
    }

    private void setDragging( boolean bDragging )
    {
      _bDragging = bDragging;
    }

    private void setInitialDragLocation( Point ptInitialDragLocation )
    {
      _ptInitialDragLocation = ptInitialDragLocation;
    }

    private Point getInitialDragLocation()
    {
      return _ptInitialDragLocation;
    }
  }
}