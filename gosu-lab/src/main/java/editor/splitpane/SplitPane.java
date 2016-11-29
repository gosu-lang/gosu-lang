/*
 *
 * 2002 CaseCenter by Centrica Software
 *
 */
package editor.splitpane;

import editor.Scheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;


/**
 * An alternative split pane with an easier api
 */
public class SplitPane extends JPanel implements SwingConstants
{
  public int SPLITTER_WIDTH = 5;

  protected int _iOrientation;
  protected double _iPosition;
  protected JPanel _pane1;
  protected JPanel _pane2;
  protected JComponent _comp1;
  protected JComponent _comp2;
  protected Splitter _splitter;
  public static final int MIN_POSITION = 0;
  public static final int MAX_POSITION = 100;

  private Set<ActionListener> _listeners = new HashSet<>();


  public SplitPane( int iOrientation, JComponent comp1, JComponent comp2 )
  {
    _iOrientation = iOrientation;

    _pane1 = new JPanel();
    _pane2 = new JPanel();

    _splitter = new Splitter();
    _pane1.setLayout( new BorderLayout() );
    _pane2.setLayout( new BorderLayout() );

    SplitterHandler splitterHandler = new SplitterHandler();
    _splitter.addMouseListener( splitterHandler );
    _splitter.addMouseMotionListener( splitterHandler );

    setBackground( Scheme.active().getMenu() );

    addMainComponents( comp1, comp2 );
  }

  protected void addMainComponents( JComponent comp1, JComponent comp2 )
  {
    add( _pane1 );
    add( _pane2 );
    add( _splitter );

    setLayout( new SplitPaneLayout() );

    setTop( comp1 );
    setBottom( comp2 );
  }

  public void setTop( JComponent c )
  {
    _pane1.add( _comp1 = c, BorderLayout.CENTER );
  }

  public JComponent getTop()
  {
    return _comp1;
  }

  public void clearTop()
  {
    _pane1.removeAll();
  }

  public void setBottom( JComponent c )
  {
    _pane2.add( _comp2 = c, BorderLayout.CENTER );
  }

  public JComponent getBottom()
  {
    return _comp2;
  }

  protected int getSplitterWidth()
  {
    return SPLITTER_WIDTH;
  }

  public int getOrientation()
  {
    return _iOrientation;
  }
  @SuppressWarnings("UnusedDeclaration")
  public void setOrientation( int iOrientation )
  {
    _iOrientation = iOrientation;

    invalidate();

    if( getParent() != null )
    {
      getParent().validate();
    }
  }

  public double getPosition()
  {
    return _iPosition;
  }
  public void setPosition( double iPos )
  {
    iPos = iPos < MIN_POSITION ? MIN_POSITION : iPos;
    iPos = iPos > MAX_POSITION ? MAX_POSITION : iPos;

    _iPosition = iPos;
    invalidate();

    if( getParent() != null )
    {
      getParent().validate();
    }

    for( ActionListener listener : _listeners )
    {
      listener.actionPerformed( new ActionEvent( this, 0, Double.toString( iPos ) ) );
    }
  }

  public void addActionListener( ActionListener l )
  {
    _listeners.add( l );
  }

  class Splitter extends JPanel
  {
    Splitter()
    {
      setBackground( Scheme.active().getMenu() );
      int iOrientation = getOrientation();

      setCursor( iOrientation == HORIZONTAL ? Cursor.getPredefinedCursor( Cursor.E_RESIZE_CURSOR )
                                            : Cursor.getPredefinedCursor( Cursor.N_RESIZE_CURSOR ) );
    }
  }

  class SplitterHandler extends MouseAdapter implements MouseMotionListener
  {
    protected Point _ptDrag;
    protected Point _ptDragSave;
    protected boolean _bDragging;
    protected Rectangle _rcLast;
    protected MouseEvent _mouseEvent;


    public void mousePressed( MouseEvent e )
    {
      _mouseEvent = e;

      beginDrag( e.getPoint() );
      e.consume();
    }

    public void mouseReleased( MouseEvent e )
    {
      _mouseEvent = e;

      endDrag();
      e.consume();
    }

    public void mouseDragged( MouseEvent e )
    {
      _mouseEvent = e;

      drag( e.getPoint() );
      e.consume();
    }

    public void mouseMoved( MouseEvent e )
    {
      e.consume();
    }

    protected void beginDrag( Point pt )
    {
      _bDragging = true;
      _ptDrag = pt;
    }

    protected void endDrag()
    {
      try
      {
        if( _rcLast != null )
        {
          setPosition();

          _rcLast = null;
          _ptDrag = null;
          _ptDragSave = null;
        }
      }
      finally
      {
        _bDragging = false;
      }
    }

    protected void drag( Point pt )
    {
      if( _ptDrag == null )
      {
        return;
      }

      Rectangle rcBounds = _splitter.getBounds();

      int iXDiff = 0;
      int iYDiff = 0;
      if( getOrientation() == HORIZONTAL )
      {
        iXDiff = pt.x - _ptDrag.x;
      }
      else
      {
        iYDiff = pt.y - _ptDrag.y;
      }

      if( (iXDiff == 0) && (iYDiff == 0) )
      {
        return;
      }

      rcBounds.x += iXDiff;
      rcBounds.y += iYDiff;

      if( _ptDragSave != null && _ptDragSave.equals( pt ) )
      {
        return;
      }

      _ptDragSave = pt;

      _rcLast = new Rectangle( rcBounds.x, rcBounds.y, rcBounds.width, rcBounds.height );
      setPosition();
    }

    private void setPosition()
    {
      if( getOrientation() == HORIZONTAL )
      {
        SplitPane.this.setPosition( (double)(_rcLast.x + _rcLast.width / 2) / SplitPane.this.getWidth() * MAX_POSITION );
      }
      else
      {
        SplitPane.this.setPosition( (double)(_rcLast.y + _rcLast.height / 2) / SplitPane.this.getHeight() * MAX_POSITION );
      }
    }
  }

  class SplitPaneLayout implements LayoutManager
  {
    public SplitPaneLayout()
    {
    }

    public void addLayoutComponent( String name, Component comp )
    {
    }

    public void removeLayoutComponent( Component comp )
    {
    }

    public Dimension preferredLayoutSize( Container parent )
    {
      return parent.getSize();
    }

    public Dimension minimumLayoutSize( Container parent )
    {
      return new Dimension( 0, 0 );
    }

    public void layoutContainer( Container parentContainer )
    {
      int iOrientation = getOrientation();

      Insets insets = getInsets();

      if( iOrientation == HORIZONTAL )
      {
        int iPos = (int)(getWidth() * (getPosition() / MAX_POSITION));
        _splitter.setBounds( insets.left + (iPos - getSplitterWidth() / 2), insets.top, getSplitterWidth(), getHeight() - insets.top - insets.bottom );

        _pane1.setBounds( insets.left, insets.top, _splitter.getX() - insets.left, getHeight() - insets.top - insets.bottom );
        _pane2.setBounds( _splitter.getX() + _splitter.getWidth(), insets.top, getWidth() - insets.right - (_splitter.getX() + _splitter.getWidth()), getHeight() - insets.top - insets.bottom );
      }
      else
      {
        int iPos = (int)(getHeight() * (getPosition() / MAX_POSITION));
        _splitter.setBounds( insets.left, insets.top + (iPos - getSplitterWidth() / 2), getWidth() - insets.left - insets.right, getSplitterWidth() );

        _pane1.setBounds( insets.left, insets.top, getWidth() - insets.left - insets.right, _splitter.getY() - insets.top );
        _pane2.setBounds( insets.left, _splitter.getY() + _splitter.getHeight(), getWidth() - insets.left - insets.right, getHeight() - insets.bottom - (_splitter.getY() + _splitter.getHeight()) );
      }
    }
  }
}
