package editor.util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 */
public class ContainerMoverSizer extends JComponent
{
  //
  // Hit test codes
  //
  public final static int PART_NONE = 0;
  public final static int PART_BORDER = 1;
  public final static int PART_BOTTOM = 2;
  public final static int PART_BOTTOMLEFT = 3;
  public final static int PART_BOTTOMRIGHT = 4;
  public final static int PART_CAPTION = 5;
  public final static int PART_CLIENT = 6;
  public final static int PART_HSCROLL = 9;
  public final static int PART_LEFT = 10;
  public final static int PART_MENU = 11;
  public final static int PART_NOWHERE = 12;
  public final static int PART_REDUCE = 13;
  public final static int PART_RIGHT = 14;
  public final static int PART_SYSMENU = 15;
  public final static int PART_TOP = 16;
  public final static int PART_TOPLEFT = 17;
  public final static int PART_TOPRIGHT = 18;
  public final static int PART_TRANSPARENT = 19;
  public final static int PART_VSCROLL = 20;
  public final static int PART_ZOOM = 21;
  public final static int PART_CLOSEBTN = 22;
  public final static int PART_MINBTN = 23;
  public final static int PART_MAXBTN = 24;

  public ContainerMoverSizer( Border border )
  {
    super();
    setBorder( border );
    configUi();
  }

  public Rectangle getClientRect()
  {
    Insets borderInsets = getBorderInsets();
    return new Rectangle( borderInsets.left, borderInsets.top,
                          getWidth() - (borderInsets.left + borderInsets.right),
                          getHeight() - (borderInsets.top + borderInsets.bottom) + 1 );
  }

  public Insets getBorderInsets()
  {
    return getBorder() == null ? new Insets( 3, 3, 3, 3 ) : getBorder().getBorderInsets( this );
  }

  @Override
  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );

    g.setColor( getBackground() );
    g.fillRect( 0, 0, getWidth(), getHeight() );
  }

  private void configUi()
  {
    setOpaque( true );
    setBackground( editor.util.EditorUtilities.CONTROL_BACKGROUND );
    DragController controller = new DragController();
    addMouseListener( controller );
    addMouseMotionListener( controller );
  }

  public synchronized int hitTest( Point pt )
  {
    if( (pt == null) || (getParent() == null) )
    {
      return PART_NOWHERE;
    }

    Point ptPos = getLocation();
    ptPos.x += pt.x;
    ptPos.y += pt.y;

    if( getParent().getComponentAt( ptPos.x, ptPos.y ) != this )
    {
      return PART_NONE;
    }

    if( getClientRect().contains( pt ) )
    {
      return PART_CLIENT;
    }

    int iFrameW = getWidth();
    int iFrameH = getHeight();
    Insets borderInsets = getBorderInsets();

    Rectangle rcBorder = new Rectangle( 0, 10, borderInsets.left, iFrameH - 2 * 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_LEFT;
    }

    rcBorder.setBounds( 10, 0, iFrameW - 2 * 10, borderInsets.top );
    if( rcBorder.contains( pt ) )
    {
      return PART_TOP;
    }

    rcBorder.setBounds( iFrameW - borderInsets.right, 10, borderInsets.right, iFrameH - 2 * 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_RIGHT;
    }

    rcBorder.setBounds( 10, iFrameH - borderInsets.bottom, iFrameW - 2 * 10, borderInsets.bottom );
    if( rcBorder.contains( pt ) )
    {
      return PART_BOTTOM;
    }

    rcBorder.setBounds( 0, 0, 10, 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_TOPLEFT;
    }

    rcBorder.setBounds( iFrameW - 10, 0, 10, 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_TOPRIGHT;
    }

    rcBorder.setBounds( 0, iFrameH - 10, 10, 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_BOTTOMLEFT;
    }

    rcBorder.setBounds( iFrameW - 10, iFrameH - 10, 10, 10 );
    if( rcBorder.contains( pt ) )
    {
      return PART_BOTTOMRIGHT;
    }

    return PART_NOWHERE;
  }

  private void updateCursor( int iDragPart )
  {
    Cursor cursor = getCursor();

    switch( iDragPart )
    {
      case PART_LEFT:
      {
        if( cursor == null || cursor.getType() != Cursor.W_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.W_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_RIGHT:
      {
        if( cursor == null || cursor.getType() != Cursor.E_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_TOP:
      {
        if( cursor == null || cursor.getType() != Cursor.N_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_BOTTOM:
      {
        if( cursor == null || cursor.getType() != Cursor.S_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.S_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_TOPLEFT:
      {
        if( cursor == null || cursor.getType() != Cursor.NW_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.NW_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_TOPRIGHT:
      {
        if( cursor == null || cursor.getType() != Cursor.NE_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.NE_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_BOTTOMLEFT:
      {
        if( cursor == null || cursor.getType() != Cursor.SW_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.SW_RESIZE_CURSOR ) );
        }
        break;
      }

      case PART_BOTTOMRIGHT:
      {
        if( cursor == null || cursor.getType() != Cursor.SE_RESIZE_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.SE_RESIZE_CURSOR ) );
        }
        break;
      }

      default:
      {
        if( cursor == null || cursor.getType() != Cursor.DEFAULT_CURSOR )
        {
          setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
        }
        break;
      }
    }
  }


  private class DragController extends MouseAdapter implements MouseMotionListener
  {
    private boolean _bDragging;
    private Point _ptInitialDragLocation;
    private Container _container;
    private int _iDragPart;

    @Override
    public void mousePressed( MouseEvent e )
    {
      setInitialDragLocation( e.getPoint() );
      setDragging( true );
    }

    @Override
    public void mouseReleased( MouseEvent e )
    {
      if( !isDragging() )
      {
        return;
      }
      setDragging( false );
    }

    @Override
    public void mouseDragged( MouseEvent e )
    {
      move( e.getPoint() );
    }

    @Override
    public void mouseMoved( MouseEvent e )
    {
      setDragPart( hitTest( e.getPoint() ) );
      updateCursor( getDragPart() );
    }

    @Override
    public void mouseExited( MouseEvent e )
    {
      updateCursor( hitTest( e.getPoint() ) );
    }

    public int getDragPart()
    {
      return _iDragPart;
    }

    private void setDragPart( int iDragPart )
    {
      _iDragPart = iDragPart;
    }

    private void move( Point pt )
    {
      Container container = getContainer();

      int iXDiff = pt.x - getInitialDragLocation().x;
      int iYDiff = pt.y - getInitialDragLocation().y;

      if( (iXDiff == 0) && (iYDiff == 0) )
      {
        return;
      }
      Rectangle rcBounds = container.getBounds();
      Rectangle rcOldBounds = new Rectangle( rcBounds );

      switch( getDragPart() )
      {
        case PART_CAPTION:
        case PART_CLIENT:
        {
          rcBounds.x += iXDiff;
          rcBounds.y += iYDiff;
          break;
        }

        case PART_LEFT:
        {
          rcBounds.width -= iXDiff;
          rcBounds.x += iXDiff;
          break;
        }

        case PART_RIGHT:
        {
          rcBounds.width += iXDiff;
          setInitialDragLocation( pt );
          break;
        }

        case PART_TOP:
        {
          rcBounds.y += iYDiff;
          rcBounds.height -= iYDiff;
          break;
        }

        case PART_BOTTOM:
        {
          rcBounds.height += iYDiff;
          setInitialDragLocation( pt );
          break;
        }

        case PART_TOPLEFT:
        {
          rcBounds.height -= iYDiff;
          rcBounds.y += iYDiff;
          rcBounds.width -= iXDiff;
          rcBounds.x += iXDiff;
          break;
        }

        case PART_TOPRIGHT:
        {
          rcBounds.height -= iYDiff;
          rcBounds.y += iYDiff;
          rcBounds.width += iXDiff;
          getInitialDragLocation().x = pt.x;
          break;
        }

        case PART_BOTTOMLEFT:
        {
          rcBounds.width -= iXDiff;
          rcBounds.x += iXDiff;
          rcBounds.height += iYDiff;
          getInitialDragLocation().y = pt.y;
          break;
        }

        case PART_BOTTOMRIGHT:
        {
          rcBounds.height += iYDiff;
          rcBounds.width += iXDiff;
          setInitialDragLocation( pt );
          break;
        }

        default:
          return;
      }

      if( rcBounds.equals( rcOldBounds ) )
      {
        return;
      }

      Dimension sizeMinimum = container.getMinimumSize();

      if( (rcBounds.width < sizeMinimum.width) || (rcBounds.height < sizeMinimum.height) )
      {
        return;
      }

      if( getDragPart() == PART_CLIENT || getDragPart() == PART_CAPTION )
      {
        container.setLocation( rcBounds.x, rcBounds.y );
      }
      else
      {
        container.setBounds( rcBounds );
      }
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