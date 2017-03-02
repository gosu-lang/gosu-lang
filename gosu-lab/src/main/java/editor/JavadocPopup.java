/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
public class JavadocPopup extends JPopupMenu implements MouseMotionListener, MouseListener
{
  private static final int MAX_WIDTH = 520;

  private HtmlViewer _viewer;
  private String _strHelpText;
  private GosuEditor _editor;
  private JScrollPane _scroller;
  private EditorKeyListener _editorKeyListener;
  private boolean _bResizedWidth;
  private boolean _bResizedHeight;
  private Point _clickLocation;


  //--------------------------------------------------------------------------------------------------
  public JavadocPopup( String strHelpText, GosuEditor editor )
  {
    super();

    _strHelpText = strHelpText;
    _editor = editor;

    initLayout();
  }

  //--------------------------------------------------------------------------------------------------
  protected void initLayout()
  {
    setOpaque( false );
    setBorder( BorderFactory.createLineBorder( Scheme.active().getWindowBorder() ) );

    GridBagLayout gridBag = new GridBagLayout();
    JPanel pane = new JPanel();
    pane.addMouseListener( this );
    pane.addMouseMotionListener( this );
    pane.setLayout( gridBag );
    GridBagConstraints c = new GridBagConstraints();

    _viewer = new HtmlViewer();
    _viewer.setText( _strHelpText );
    _viewer.setBorder( BorderFactory.createEmptyBorder( 0, 10, 0, 10 ) );
    _viewer.addMouseListener( this );
    _viewer.addMouseMotionListener( this );
    _scroller = new JScrollPane( _viewer );
    _scroller.setBorder( BorderFactory.createEmptyBorder( 0, 0, 0, 0 ) );
    _scroller.setMaximumSize( calcMaximumViewerSize() );
    _viewer.setBackground( Scheme.active().getTooltipBackground() );

    c.anchor = GridBagConstraints.CENTER;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 1;
    c.weighty = 1;
    pane.add( _scroller, c );

    _editorKeyListener = new EditorKeyListener();

    add( pane );

    addSeparator();

    addMouseListener( this );
    addMouseMotionListener( this );
  }

  //--------------------------------------------------------------------------------------------------
  @Override
  public void show( final Component c, final int iX, final int iY )
  {
    super.show( c, iX, iY );

    EventQueue.invokeLater(
      () -> {
        if( !_bResizedWidth && _viewer.getWidth() > MAX_WIDTH )
        {
          //noinspection SuspiciousNameCombination
          _scroller.setPreferredSize( new Dimension( MAX_WIDTH, MAX_WIDTH ) );
          _bResizedWidth = true;
          pack();
          setVisible( false );
          show( c, iX, iY );
          return;
        }
        if( !_bResizedHeight && isViewerLessThanHalfFull() )
        {
          _scroller.setPreferredSize( new Dimension( MAX_WIDTH, _scroller.getHeight() / 2 ) );
          _bResizedHeight = true;
          pack();
          setVisible( false );
          show( c, iX, iY );
        }
      } );
  }

  /**
   *
   * @return
   */
  private Dimension calcMaximumViewerSize()
  {
    Toolkit defToolkit = Toolkit.getDefaultToolkit();
    int iMaxHeight = defToolkit.getScreenSize().height * 2 / 3;
    return new Dimension( MAX_WIDTH, iMaxHeight );
  }

  /**
   *
   * @return
   */
  private boolean isViewerLessThanHalfFull()
  {
    Position endPos = _viewer.getDocument().getEndPosition();
    if( endPos == null )
    {
      return false;
    }

    int iEndPos = endPos.getOffset() - 1;
    try
    {
      Rectangle rcEndPos = _viewer.modelToView( iEndPos );
      if( rcEndPos != null )
      {
        return rcEndPos.y + rcEndPos.height < _scroller.getHeight() / 2;
      }
    }
    catch( Exception e )
    {
      // Do nothing
    }
    return false;
  }

  //--------------------------------------------------------------------------------------------------
  @Override
  public void setVisible( boolean bVisible )
  {
    super.setVisible( bVisible );

    if( bVisible )
    {
      registerListeners();
    }
    else
    {
      unregisterListeners();
      _editor.getEditor().requestFocus();
    }
  }

  //--------------------------------------------------------------------------------------------------
  void registerListeners()
  {
    unregisterListeners();

    _editor.getEditor().addKeyListener( _editorKeyListener );
  }

  //--------------------------------------------------------------------------------------------------
  void unregisterListeners()
  {
    _editor.getEditor().removeKeyListener( _editorKeyListener );
  }

  @Override
  public void mouseDragged( MouseEvent e )
  {
    if( _clickLocation != null )
    {
      Point location = getLocationOnScreen();
      location.translate( e.getX() - _clickLocation.x, e.getY() - _clickLocation.y );
      setLocation( location );
    }
  }

  @Override
  public void mouseMoved( MouseEvent e )
  {
  }

  @Override
  public void mouseClicked( MouseEvent e )
  {
  }

  @Override
  public void mousePressed( MouseEvent e )
  {
    _clickLocation = e.getPoint();
  }

  @Override
  public void mouseReleased( MouseEvent e )
  {
    _clickLocation = null;
  }

  @Override
  public void mouseEntered( MouseEvent e )
  {
  }

  @Override
  public void mouseExited( MouseEvent e )
  {
  }

  //----------------------------------------------------------------------------------------------
  //----------------------------------------------------------------------------------------------
  class EditorKeyListener extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_ESCAPE )
      {
        setVisible( false );

        e.consume();
      }
    }
  }
}


