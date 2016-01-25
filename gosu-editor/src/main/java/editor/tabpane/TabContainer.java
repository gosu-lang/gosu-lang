package editor.tabpane;

import editor.IContextMenuHandler;
import editor.search.StudioUtilities;
import editor.util.IDisposable;
import editor.util.ILabel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class TabContainer extends JPanel
{
  public static final String TAB_PROPERTY = "_ITab";

  private List<ITab> _orderedTabs;
  private MoreTab _moreTab;
  private TabPosition _tabPosition;
  private EventListenerList _selectionListeners;
  private EventListenerList _tabPositionListeners;
  private IContextMenuHandler<JComponent> _contextMenuHandler;

  public TabContainer( TabPosition tabPosition )
  {
    _tabPosition = tabPosition;
    _orderedTabs = new ArrayList<>();
    _selectionListeners = new EventListenerList();
    _tabPositionListeners = new EventListenerList();
    add( _moreTab = new MoreTab( this ) );
    setLayout( new TabContainerLayoutManager() );
    setOpaque( false );
  }

  public void addNotify()
  {
    super.addNotify();
    EventQueue.invokeLater( this::revalidate );
  }

  public TabPosition getTabPosition()
  {
    return _tabPosition;
  }

  public void setTabPosition( TabPosition tabPosition )
  {
    _tabPosition = tabPosition;
    fireTabPositionChanged();
  }

  public ITab[] getTabs()
  {
    return _orderedTabs.toArray( new ITab[_orderedTabs.size()] );
  }

  public void addTab( ITab tab )
  {
    insertTab( tab, -1, true);
  }

  public void addTabWithoutSelecting( ITab tab )
  {
    insertTabWithoutSelecting( tab, -1 );
  }

  public void insertTab(ITab tab, int iIndex, boolean focus)
  {
    resetComponentOrderToTabOrder();
    iIndex = iIndex < 0 ? _orderedTabs.size() : iIndex;
    _orderedTabs.add( iIndex, tab );
    JComponent comp = tab.getComponent();
    comp.putClientProperty( TAB_PROPERTY, tab );
    add( comp, iIndex );
    tab.getComponent().addMouseListener( new MouseOnTabHandler() );
    selectTab( tab, focus);
  }

  public void insertTabWithoutSelecting( ITab tab, int iIndex )
  {
    ITab selected = getSelectedTab();
    insertTab( tab, iIndex, false );
    if( selected != null )
    {
      selectTab( selected, false );
    }
  }

  public void removeTab( ITab tab )
  {
    _orderedTabs.remove( tab );
    remove( tab.getComponent() );
    dispose( tab );
    revalidate();
    repaint();
    fireSelectionChanged(tab);
  }

  private void dispose( ITab tab )
  {
    ILabel label = tab.getLabel();
    if( label instanceof IDisposable ) {
      ((IDisposable)label).dispose();
    }

    JComponent contentPane = tab.getContentPane();
    if(contentPane instanceof IDisposable && contentPane != label) {
      ((IDisposable) contentPane).dispose();
    }

    tab.dispose();
  }

  public void removeTabWithContent( JComponent contentPane )
  {
    ITab tab = findTabWithContent( contentPane );
    if( tab != null )
    {
      removeTab( tab );
    }
  }

  public void removeAllTabs()
  {
    for( int i = _orderedTabs.size() - 1; i >= 0; i-- )
    {
      ITab tab = _orderedTabs.get( i );
      removeTab( tab );
    }
  }

  public int getTabCount()
  {
    return _orderedTabs.size();
  }

  public ITab getTabAt( int iIndex )
  {
    return _orderedTabs.get( iIndex );
  }

  public ITab getSelectedTab()
  {
    if( _orderedTabs.size() == 0 )
    {
      return null;
    }

    return tabFromComponent( getFirstTabComponent() );
  }

  private Component getFirstTabComponent()
  {
    for( Component c : getComponents() )
    {
      if( c != _moreTab )
      {
        return c;
      }
    }
    return null;
  }

  public int getSelectedTabIndex()
  {
    if( _orderedTabs.size() == 0 )
    {
      return -1;
    }

    return _orderedTabs.indexOf( getSelectedTab() );
  }

  public void selectTab(JComponent contentPane, boolean focus)
  {
    ITab tab = findTabWithContent( contentPane );
    if( tab != null )
    {
      selectTab( tab, focus);
    }
  }

  public void selectTab( int iTabIndex )
  {
    if( iTabIndex >= _orderedTabs.size() )
    {
      throw new IllegalArgumentException( iTabIndex + " > " + _orderedTabs.size() );
    }
    ITab tab = _orderedTabs.get( iTabIndex );
    selectTab( tab, true);
  }

  public void selectTab( ITab tab, boolean bFocus )
  {
    if( isShowing() &&
        (!tab.getComponent().isShowing() || (getTabPane() != null && getTabPane().isDynamic() && !isInView( tab ))) )
    {
      _orderedTabs.remove( tab );
      _orderedTabs.add( 0, tab );
    }
    resetComponentOrderToTabOrder();
    JComponent tabComponent = tab.getComponent();
    add( tabComponent, 0 );
    requestFocus();
    revalidate();
    repaint();
    fireSelectionChanged(tab);
    if( bFocus )
    {
      setFocusToSelectedTab();
    }
  }

  private boolean isInView( ITab tab )
  {
    return tab.getComponent().getX() + tab.getComponent().getWidth() <= getWidth();
  }

  private void setFocusToSelectedTab()
  {
    ITab selectedTab = getSelectedTab();
    JComponent selectedTabComponent = selectedTab == null ? null : selectedTab.getComponent();
    if( selectedTabComponent != null && !StudioUtilities.containsFocus( selectedTabComponent ) )
    {
      selectedTabComponent.transferFocus();
    }
  }

  public ITab findTabWithContent( JComponent contentPane )
  {
    for( int i = 0; i < _orderedTabs.size(); i++ )
    {
      ITab tab = _orderedTabs.get( i );
      if( tab.getContentPane() == contentPane )
      {
        return tab;
      }
    }
    return null;
  }

  protected void addImpl( Component comp, Object constraints, int iIndex )
  {
    if( !(comp instanceof JComponent) )
    {
      throw new IllegalArgumentException( "Not a JComponent" );
    }

    if( comp != _moreTab )
    {
      ITab tab = tabFromComponent( comp );
      if( tab == null )
      {
        throw new IllegalArgumentException( "Not an ITab" );
      }
    }
    
    super.addImpl( comp, constraints, iIndex );
  }

  protected void paintComponent( Graphics g )
  {
    super.paintComponent( g );

    if( !isShowBaseLine() )
    {
      return;
    }

    g.setColor( SystemColor.controlShadow );
    if( getTabPosition() == TabPosition.TOP )
    {
      g.drawLine( 0, getHeight() - 1, getWidth() - 1, getHeight() - 1 );
    }
    else if( getTabPosition() == TabPosition.BOTTOM )
    {
      g.drawLine( 0, 0, getWidth() - 1, 0 );
    }
    else if( getTabPosition() == TabPosition.LEFT )
    {
      g.drawLine( getWidth() - 1, 0, getWidth() - 1, getHeight() - 1 );
    }
    else
    {
      g.drawLine( 0, 0, 0, getHeight() - 1 );
    }
  }

  public void addSelectionListener( ChangeListener l )
  {
    _selectionListeners.add( ChangeListener.class, l );
  }

  public void removeSelectionListener( ChangeListener l )
  {
    _selectionListeners.remove( ChangeListener.class, l );
  }

  private void fireSelectionChanged(ITab tab)
  {
    Object[] listeners = _selectionListeners.getListenerList();
    if( listeners.length == 0 )
    {
      return;
    }

    ChangeEvent e = new TabChangeEvent( this, tab );
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }

  public void addTabpositionListener( ChangeListener l )
  {
    _tabPositionListeners.add( ChangeListener.class, l );
  }

  public void removeTabpositionListener( ChangeListener l )
  {
    _tabPositionListeners.remove( ChangeListener.class, l );
  }

  private void fireTabPositionChanged()
  {
    Object[] listeners = _tabPositionListeners.getListenerList();
    if( listeners.length == 0 )
    {
      return;
    }

    ChangeEvent e = new ChangeEvent( this );
    for( int i = listeners.length - 2; i >= 0; i -= 2 )
    {
      if( listeners[i] == ChangeListener.class )
      {
        ((ChangeListener)listeners[i + 1]).stateChanged( e );
      }
    }
  }

  public void setContextMenuHandler( IContextMenuHandler<JComponent> handler )
  {
    _contextMenuHandler = handler;
  }
  public IContextMenuHandler<JComponent> getContextMenuHandler()
  {
    return _contextMenuHandler;
  }

  private void resetComponentOrderToTabOrder()
  {
    if( _orderedTabs.size() == 0 )
    {
      return;
    }
    ITab tabSelected = tabFromComponent( getFirstTabComponent() );
    add( tabSelected.getComponent(), _orderedTabs.indexOf( tabSelected ) );
  }

  public static ITab tabFromComponent( Component comp )
  {
    return (ITab)((JComponent)comp).getClientProperty( TAB_PROPERTY );
  }

  public boolean isVertical()
  {
    return _tabPosition == TabPosition.LEFT ||
           _tabPosition == TabPosition.RIGHT;
  }

  public boolean isShowBaseLine()
  {
    TabPane tabPane = getTabPane();
    return tabPane != null && tabPane.isShowing();
  }

  public TabPane getTabPane()
  {
    for( Container p = getParent();
         p != null && !(p instanceof Window) && !(p instanceof ContentContainer);
         p = p.getParent() )
    {
      if( p instanceof TabPane )
      {
        return (TabPane)p;
      }
    }
    return null;
  }

  private void displayContextMenu( final MouseEvent e )
  {
    IContextMenuHandler<JComponent> handler = getContextMenuHandler();
    if( handler != null )
    {
      JComponent c = (JComponent)e.getSource();
      handler.displayContextMenu( c, e.getX(), c.getHeight(), e.getComponent());
    }
  }

  public MoreTab getMoreTab()
  {
    return _moreTab;
  }

  class MouseOnTabHandler extends MouseAdapter
  {
    public void mouseReleased( MouseEvent e )
    {
      JComponent tabComp = (JComponent)e.getSource();
      ITab tab = tabFromComponent( tabComp );

      if( SwingUtilities.isMiddleMouseButton( e ) ||
          (e.getModifiers() & Event.SHIFT_MASK) != 0 )
      {
        TabPane tabPane = getTabPane();
        if( tabPane != null && tabPane.isDynamic() )
        {
          removeTab( tab );
          return;
        }
      }

      if( SwingUtilities.isRightMouseButton( e ) )
      {
        displayContextMenu( e );
        return;
      }

      if( getSelectedTab() == tab &&
          // Check if showing for case when minimized in collapsible panel
          tab.getContentPane().isShowing() )
      {
        return;
      }
      selectTab( tab, true);
    }

    public void mouseEntered( MouseEvent e )
    {
      JComponent tabComp = (JComponent)e.getSource();
      ITab tab = tabFromComponent( tabComp );
      if( tab != null )
      {
        tab.setHover( true );
        tabComp.repaint();
      }
    }

    public void mouseExited( MouseEvent e )
    {
      JComponent tabComp = (JComponent)e.getSource();
      ITab tab = tabFromComponent( tabComp );
      if( tab != null )
      {
        tab.setHover( false );
        tabComp.repaint();
      }
    }
  }

  public static class TabChangeEvent extends ChangeEvent
  {
    private ITab _tab;

    public TabChangeEvent(TabContainer tabContainer, ITab tab) {
      super(tabContainer);
      _tab = tab;
    }

    public ITab getTab() {
      return _tab;
    }
  }
}
