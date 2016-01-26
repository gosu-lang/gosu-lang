package editor.tabpane;

import editor.search.StudioUtilities;
import editor.util.SettleModalEventQueue;
import editor.util.XPToolbarButton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 */
public class MoreTab extends XPToolbarButton
{
  private TabContainer _tabContainer;

  public MoreTab( TabContainer tabContainer )
  {
    setFont( new Font( "Arial", Font.BOLD, 16 ) );
    setOpaque( false );
    _tabContainer = tabContainer;
  }

  protected MouseListener createMouseListener()
  {
    return new TabContainerPopupHandler();
  }

  public Dimension getPreferredSize()
  {
    switch( _tabContainer.getTabPosition() )
    {
      case TOP:
      case BOTTOM:
        if( _tabContainer.getTabCount() > 0 )
        {
          int iTabHeight = _tabContainer.getTabAt( _tabContainer.getTabCount() - 1 ).getComponent().getHeight();
          return new Dimension( iTabHeight, iTabHeight );
        }
        return new Dimension( 20, 10 );

      case LEFT:
      case RIGHT:
        if( _tabContainer.getTabCount() > 0 )
        {
          int iTabWidth = _tabContainer.getTabAt( _tabContainer.getTabCount() - 1 ).getComponent().getWidth();
          return new Dimension( iTabWidth, iTabWidth );
        }
        return new Dimension( 10, 20 );
    }
    throw new IllegalStateException();
  }

  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }

  public Dimension getMaximumSize()
  {
    return getPreferredSize();
  }

  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );

    if( !tooManyTabsToShow() )
    {
      return;
    }

    Graphics2D g2In = (Graphics2D)g;

    BufferedImage bi = g2In.getDeviceConfiguration().createCompatibleImage( getWidth(), getHeight(), Transparency.TRANSLUCENT );
    Graphics2D g2Image = getTransformedGraphics( g2In, bi );

    paintTopOrientation( g2Image );

    if( g2Image != g2In )
    {
      g2Image.dispose();
      g2In.drawImage( bi, null, 0, 0 );
    }
  }

  private Graphics2D getTransformedGraphics( Graphics2D g2, BufferedImage bi )
  {
    if( _tabContainer.getTabPosition() == TabPosition.TOP )
    {
      return g2;
    }

    Graphics2D g2Image = bi.createGraphics();
    AffineTransform transform;
    if( _tabContainer.getTabPosition() == TabPosition.BOTTOM )
    {
      transform = AffineTransform.getScaleInstance( 1, -1 );
      transform.translate( 0, 1 - getHeight() );
    }
    else if( _tabContainer.getTabPosition() == TabPosition.LEFT )
    {
      transform = AffineTransform.getScaleInstance( -1, 1 );
      transform.translate( 1-getWidth(), 0 );
      transform.concatenate( AffineTransform.getRotateInstance( Math.PI / 2 ) );
      transform.translate( 0, 1 - getWidth() );
    }
    else if( _tabContainer.getTabPosition() == TabPosition.RIGHT )
    {
      transform = AffineTransform.getRotateInstance( Math.PI / 2 );
      transform.translate( 0, 1 - getWidth() );
    }
    else
    {
      throw new IllegalStateException( "Unknown TabPosition" );
    }
    g2Image.setTransform( transform );
    return g2Image;
  }

  private void paintTopOrientation( Graphics g )
  {
    int iYStart = 1;
    g.setColor( SystemColor.controlShadow );
    g.drawLine( 0, iYStart, 1, iYStart );
    boolean bLeft = true;
    for( int i = iYStart, x = 1; i <= 32; i+=3, x+=(bLeft ? -1 : 1) )
    {
      g.setColor( SystemColor.controlShadow );
      g.drawLine( x,  i,  x,  i+2 );
      g.setColor( SystemColor.controlDkShadow );
      g.drawLine( x+1,  i,  x+1,  i+2 );
      g.setColor( SystemColor.control );
      g.fillRect( x+2, i, getWidth(), i+2 );
      bLeft = x != 0 && (x == 2 || bLeft);
    }

    g.setFont( getFont() );
    g.setColor( SystemColor.controlText );
    g.drawString( "...", 5, 8 );
  }

  private boolean tooManyTabsToShow()
  {
    if( _tabContainer.getTabCount() > 0 )
    {
      ITab tab = _tabContainer.getTabAt( _tabContainer.getTabCount() - 1 );
      return !isTabFullyVisible( tab );
    }
    return false;
  }

  private boolean isTabFullyVisible( ITab tab )
  {
    switch( _tabContainer.getTabPosition() )
    {
      case TOP:
      case BOTTOM:
        return tab.getComponent().getX() + tab.getComponent().getWidth() <= _tabContainer.getWidth();
      case LEFT:
      case RIGHT:
        return tab.getComponent().getY() + tab.getComponent().getHeight() <= _tabContainer.getHeight();
    }
    throw new IllegalStateException();
  }

  class TabContainerPopupHandler extends MouseAdapter
  {
    private TabPaneSelectionHandler _selectionListener;
    private long _lastSelectionTime;
    private Timer _timer;
    private Placeholder _placeholder;

    public void mouseExited( MouseEvent e )
    {
      e.consume();
      if( _timer != null )
      {
        _timer.stop();
      }
    }
    public void mouseEntered( MouseEvent e )
    {
      e.consume();
      if( !tooManyTabsToShow() )
      {
        return;
      }

      if( System.currentTimeMillis() - _lastSelectionTime < 500 )
      {
        return;
      }
      _timer = new Timer( 250, new PopupAction() );
      _timer.setRepeats( false );
      _timer.start();
    }

    class PopupAction implements ActionListener
    {
      public void actionPerformed( ActionEvent e )
      {
        if( _tabContainer.getParent() instanceof TabPopup )
        {
          // Somehow we got another timer event, so bail (tab container is already in the popup)  
          return;
        }

        TabAndToolContainer parent = (TabAndToolContainer)_tabContainer.getParent();
        JPopupMenu popup = new TabPopup();
        _selectionListener = new TabPaneSelectionHandler( popup );
        popup.setLayout( new BorderLayout() );
        popup.setBackground( SystemColor.control );
        Point pt = _tabContainer.getLocation();
        pt = fitToScreen( pt, parent, _tabContainer );
        _tabContainer.addSelectionListener( _selectionListener );
        int iTabDimDelta = 0;
        switch( _tabContainer.getTabPosition() )
        {
          case TOP:
          case BOTTOM:
            iTabDimDelta = _tabContainer.getHeight() - _tabContainer.getPreferredSize().height;
            break;
          case LEFT:
          case RIGHT:
            iTabDimDelta = _tabContainer.getWidth() - _tabContainer.getPreferredSize().width;
            break;
        }
        parent.remove( _tabContainer );
        parent.add( _placeholder = new Placeholder(), BorderLayout.CENTER );
        popup.add( _tabContainer, BorderLayout.CENTER );
        _tabContainer.revalidate();
        switch( _tabContainer.getTabPosition() )
        {
          case TOP:
            popup.show( parent, pt.x, pt.y + iTabDimDelta );
            break;
          case BOTTOM:
            popup.show( parent, pt.x, pt.y );
            break;
          case LEFT:
            popup.show( parent, pt.x + iTabDimDelta, pt.y );
            break;
          case RIGHT:
            popup.show( parent, pt.x - iTabDimDelta, pt.y );
            break;
          default:
            throw new IllegalStateException();
        }
      }
    }

    private Point fitToScreen( Point pt, JComponent parent, TabContainer tabContainer )
    {
      switch( tabContainer.getTabPosition() )
      {
        case TOP:
        case BOTTOM:
          return fitHorizontally( pt, parent, tabContainer );
        case LEFT:
        case RIGHT:
          return fitVertically( pt, parent, tabContainer );
      }
      throw new IllegalStateException();
    }

    private Point fitHorizontally( Point pt, JComponent parent, TabContainer tabContainer )
    {
      Point ptScreen = new Point( pt );
      SwingUtilities.convertPointToScreen( ptScreen, parent );
      int iDelta = pt.x + tabContainer.getWidth() - parent.getToolkit().getScreenSize().width;
      if( iDelta > 0 )
      {
        pt.x -= iDelta;
      }
      return pt;
    }

    private Point fitVertically( Point pt, JComponent parent, TabContainer tabContainer )
    {
      Point ptScreen = new Point( pt );
      SwingUtilities.convertPointToScreen( ptScreen, parent );
      int iDelta = pt.y + tabContainer.getHeight() - parent.getToolkit().getScreenSize().height;
      if( iDelta > 0 )
      {
        pt.y -= iDelta;
      }
      return pt;
    }

    private class TabPaneSelectionHandler implements ChangeListener
    {
      private JPopupMenu _popup;

      public TabPaneSelectionHandler( JPopupMenu popup )
      {
        _popup = popup;
      }

      public void stateChanged( ChangeEvent e )
      {
        _lastSelectionTime = System.currentTimeMillis();
        _popup.setVisible( false );
        SettleModalEventQueue.instance().run();
        // Reselect tab to ensure it is is view and has focus
        _tabContainer.selectTab( _tabContainer.getSelectedTab(), true );
        for( ITab tab : _tabContainer.getTabs() )
        {
          tab.setHover( false );
          tab.getComponent().repaint();
        }
      }
    }

    private class Placeholder extends JComponent
    {
      public Dimension getPreferredSize()
      {
        switch( _tabContainer.getTabPosition() )
        {
          case TOP:
          case BOTTOM:
            return new Dimension( 1, _tabContainer.getPreferredSize().height );
          case LEFT:
          case RIGHT:
            return new Dimension( _tabContainer.getPreferredSize().width, 1 );
        }
        throw new IllegalStateException();
      }
    }

    private class TabPopup extends JPopupMenu
    {
      private TabAndToolContainer _tabParent;
 
      TabPopup()
      {
        _tabParent = (TabAndToolContainer)_tabContainer.getParent();
        setLightWeightPopupEnabled( false );
      }

      public Dimension getPreferredSize()
      {
        return _tabContainer.getPreferredSize();
      }

      public void removeNotify()
      {
        super.removeNotify();
        _tabContainer.removeSelectionListener( _selectionListener );
        _tabParent.remove( _placeholder );
        _tabParent.add( _tabContainer, BorderLayout.CENTER );
        _tabContainer.revalidate();
      }

      public void setVisible( boolean bVisible )
      {
        super.setVisible( bVisible );

        if( bVisible )
        {
          StudioUtilities.removePopupBorder( this );
        }
      }
    }
  }
}
