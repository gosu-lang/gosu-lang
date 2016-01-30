package editor.tabpane;

import editor.IContextMenuHandler;
import editor.search.StudioUtilities;
import editor.splitpane.ICaptionBar;
import editor.splitpane.ICaptionedPanel;
import editor.util.ILabel;
import editor.util.SettleModalEventQueue;
import gw.util.GosuObjectUtil;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

public class TabPane extends JPanel implements ICaptionedPanel
{
  private static final int HEADER_MARGIN = 5;
  static final String TAB_PANE = "_tabPaneProperty";

  /**
   * DisplayOption. Are tabs in this pane minimizable? They can be only if this
   * tab pane belongs to a collapsible panel such as CollapsibleSplitPane
   */
  public static final int MINIMIZABLE = 1;
  /**
   * DisplayOption. Are tabs in this pane maximizable? They can be only if this
   * tab pane belongs to a collapsible panel such as CollapsibleSplitPane
   */
  public static final int MAXIMIZABLE = 2;
  /**
   * DisplayOption. Are tabs in this pane restorable? They can be only if this
   * tab pane belongs to a collapsible panel such as CollapsibleSplitPane
   */
  public static final int RESTORABLE = 4;
  /**
   * DisplayOption.
   */
  public static final int MIN_MAX_REST = 7;
  /**
   * DisplayOption. Are tabs in this pane dynamic? In other words will this tab
   * pane create an arbitrary number of tabs and should those tabs be closable?
   * If so, displays a close button for the tabs.
   */
  public static final int DYNAMIC = 8;
  /**
   * DisplayOption. If set, only draws a border around the top of the tab pane;
   * the part around the tab container.
   */
  public static final int TOP_BORDER_ONLY = 16;

  private TabContainer _tabContainer;
  private ToolContainer _toolContainer;
  private TabAndToolContainer _tabAndToolContainer;
  private ContentContainer _contentContainer;
  private TabPosition _tabPosition;
  private boolean _bDynamic;
  private boolean _bTopBorderOnly;
  private boolean _bActive;
  private boolean _bMaximizable;
  private boolean _bMinimizable;
  private boolean _bRestorable;
  private FocusChangeListener _focusChangeListener;


  public TabPane()
  {
    this( TabPosition.TOP, 0 );
  }

  public TabPane( int iDisplayOptions )
  {
    this( TabPosition.TOP, iDisplayOptions );
  }

  public TabPane( TabPosition tabPosition, int iDisplayOptions )
  {
    _tabPosition = tabPosition;
    _bDynamic = (iDisplayOptions & DYNAMIC) != 0;
    _bTopBorderOnly = (iDisplayOptions & TOP_BORDER_ONLY) != 0;
    _bMaximizable = (iDisplayOptions & MAXIMIZABLE) != 0;
    _bMinimizable = (iDisplayOptions & MINIMIZABLE) != 0;
    _bRestorable = (iDisplayOptions & RESTORABLE) != 0;
    configUi();
    _focusChangeListener = new FocusChangeListener();
    addAncestorListener( new FocusChangeListenerAdderRemover() );
  }

  public ICaptionBar getCaption()
  {
    return _tabAndToolContainer;
  }

  public void addTab( ILabel tabLabel, JComponent contentPane )
  {
    _tabContainer.addTab( new StandardTab( _tabContainer, tabLabel, contentPane, true ) );
  }

  public void addTabWithoutSelecting( ILabel tabLabel, JComponent contentPane )
  {
    _tabContainer.addTabWithoutSelecting( new StandardTab( _tabContainer, tabLabel, contentPane, true ) );
  }

  public void addTab( String strText, Icon icon, JComponent contentPane )
  {
    insertTab( strText, icon, contentPane, -1 );
  }

  public void insertTab( ILabel tabLabel, JComponent contentPane, int iIndex )
  {
    _tabContainer.insertTab( new StandardTab( _tabContainer, tabLabel, contentPane, true ), iIndex, true);
  }

  public void insertTab( String strText, Icon icon, JComponent contentPane, int iIndex )
  {
    _tabContainer.insertTab( new StandardTab( _tabContainer, new SimpleLabel( strText, icon ), contentPane, isDynamic() ), iIndex, true);
  }

  public void removeTabWithContent( JComponent contentPane )
  {
    _tabContainer.removeTabWithContent( contentPane );
  }

  public void removeTab( ITab tab )
  {
    _tabContainer.removeTab( tab );
  }

  public void removeAllTabs()
  {
    _tabContainer.removeAllTabs();
  }

  public void selectTabWithContent( JComponent contentPane, boolean bFocus )
  {
    _tabContainer.selectTab( contentPane, bFocus );
  }

  public void selectTab( ITab tab, boolean bFocus )
  {
    _tabContainer.selectTab( tab, bFocus );
  }

  public void selectTabWithLabel( String label )
  {
    ITab tab = findTabWithLabel(label);
    if( tab != null )
    {
      _tabContainer.selectTab( tab, true );
    }
  }

  public ITab getTabAt( int iIndex )
  {
    return _tabContainer.getTabAt( iIndex );
  }

  public ITab getSelectedTab()
  {
    return _tabContainer.getSelectedTab();
  }

  public int getSelectedTabIndex()
  {
    return _tabContainer.getSelectedTabIndex();
  }

  public int getTabCount()
  {
    return _tabContainer.getTabCount();
  }

  public ITab[] getTabs()
  {
    return _tabContainer.getTabs();
  }

  public ITab findTabWithContent( JComponent contentPane )
  {
    return _tabContainer.findTabWithContent( contentPane );
  }

  public ITab findTabWithLabel(String label) {
    ITab tab = null;
    for (ITab iTab : getTabs()) {
      if ( GosuObjectUtil.equals( iTab.getLabel().getDisplayName(), label )) {
        tab = iTab;
      }
    }
    return tab;
  }

  public void addSelectionListener( ChangeListener l )
  {
    _tabContainer.addSelectionListener( l );
  }

  public void removeSelectionListener( ChangeListener l )
  {
    _tabContainer.removeSelectionListener( l );
  }

  public boolean isDynamic()
  {
    return _bDynamic;
  }

  public boolean isTopBorderOnly()
  {
    return _bTopBorderOnly;
  }

  public boolean isActive()
  {
    return _bActive;
  }

  public void setActive( boolean bActive )
  {
    if( bActive != _bActive )
    {
      _bActive = bActive;
      repaint();
    }
  }

  public boolean isMaximizable()
  {
    return _bMaximizable;
  }

  public boolean isMinimizable()
  {
    return _bMinimizable;
  }

  public boolean isRestorable()
  {
    return _bRestorable;
  }

  public boolean hasAtLeastOneOfMinMaxRestore()
  {
    return isMaximizable() || isMinimizable() || isRestorable();
  }

  public IContextMenuHandler<JComponent> getContextMenuHandler()
  {
    return _tabContainer.getContextMenuHandler();
  }

  public void setContextMenuHandler( IContextMenuHandler<JComponent> handler )
  {
    _tabContainer.setContextMenuHandler( handler );
  }

  public TabContainer getTabContainer()
  {
    return _tabContainer;
  }

  public ToolContainer getToolContainer()
  {
    return _toolContainer;
  }

  TabAndToolContainer getTabAndToolContainer()
  {
    return _tabAndToolContainer;
  }

  private void configUi()
  {
    setLayout( new BorderLayout() );

    _contentContainer = new ContentContainer( this );
    add( _contentContainer, BorderLayout.CENTER );

    _tabContainer = new TabContainer( _tabPosition );
    _tabContainer.putClientProperty( TAB_PANE, this );
    _toolContainer = new ToolContainer( this );
    _tabAndToolContainer = new TabAndToolContainer( this );

    if( _tabPosition == TabPosition.TOP )
    {
      add( _tabAndToolContainer, BorderLayout.NORTH );
    }
    else if( _tabPosition == TabPosition.BOTTOM )
    {
      add( _tabAndToolContainer, BorderLayout.SOUTH );
    }
    else if( _tabPosition == TabPosition.RIGHT )
    {
      add( _tabAndToolContainer, BorderLayout.EAST );
    }
    else if( _tabPosition == TabPosition.LEFT )
    {
      add( _tabAndToolContainer, BorderLayout.WEST );
    }
    else
    {
      throw new IllegalStateException( "Unknown TabPosition " );
    }

    _tabContainer.addSelectionListener( new ContentDisplayHandler() );
    setBorder();

    if( isDynamic() )
    {
      _tabContainer.setContextMenuHandler( new DefaultContextMenuHandler() );
    }
  }

  private void setBorder()
  {
    if( _tabPosition == TabPosition.TOP )
    {
      if( !isTopBorderOnly() )
      {
        setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( HEADER_MARGIN, 0, 0, 0 ) ) );
      }
      else
      {
        setBorder( null );
        _tabAndToolContainer.setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 0, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( HEADER_MARGIN, 0, 0, 0 ) ) );
      }
    }
    else if( _tabPosition == TabPosition.BOTTOM )
    {
      if( !isTopBorderOnly() )
      {
        setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, 0, HEADER_MARGIN, 0 ) ) );
      }
      else
      {
        setBorder( null );
        _tabAndToolContainer.setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 0, 1, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, 0, HEADER_MARGIN, 0 ) ) );
      }
    }
    else if( _tabPosition == TabPosition.RIGHT )
    {
      if( !isTopBorderOnly() )
      {
        setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, 0, 0, HEADER_MARGIN ) ) );
      }
      else
      {
        setBorder( null );
        _tabAndToolContainer.setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 0, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, 0, 0, HEADER_MARGIN ) ) );
      }
    }
    else if( _tabPosition == TabPosition.LEFT )
    {
      if( !isTopBorderOnly() )
      {
        setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 1, 1, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, HEADER_MARGIN, 0, 0 ) ) );
      }
      else
      {
        setBorder( null );
        _tabAndToolContainer.setBorder( BorderFactory.createCompoundBorder(
          BorderFactory.createMatteBorder( 1, 1, 1, 0, SystemColor.controlShadow ),
          BorderFactory.createEmptyBorder( 0, HEADER_MARGIN, 0, 0 ) ) );
      }
    }
  }

  private void addFocusOwnerListener()
  {
    KeyboardFocusManager focusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    if( Arrays.asList( focusMgr.getPropertyChangeListeners() ).contains( _focusChangeListener ) )
    {
      return;
    }
    focusMgr.addPropertyChangeListener( "permanentFocusOwner", _focusChangeListener );
  }

  private void removeFocusOwnerListener()
  {
    KeyboardFocusManager focusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusMgr.removePropertyChangeListener( "permanentFocusOwner", _focusChangeListener );
  }

  public void localeChanged() {
    for (ITab tab : getTabs()) {
      tab.refresh();
    }
  }

  public class ContentDisplayHandler implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      ITab tab = _tabContainer.getSelectedTab();
      Component selectedContentPane = tab != null ? tab.getContentPane() : null;

      if( selectedContentPane != null )
      {
        if( !selectedContentPane.isVisible() )
        {
          selectedContentPane.setVisible( true );
        }
        if( selectedContentPane.getParent() != _contentContainer )
        {
          _contentContainer.add( selectedContentPane );
        }
        _contentContainer.revalidate();
        tab.getContentPane().repaint();
      }

      for( int i = 0; i < _contentContainer.getComponentCount(); i++ )
      {
        JComponent comp = (JComponent)_contentContainer.getComponent( i );
        if( comp != selectedContentPane )
        {
          comp.setVisible( false );
        }
        if( _tabContainer.findTabWithContent( comp ) == null )
        {
          _contentContainer.removeDirect( comp );
        }
      }
    }
  }

  class FocusChangeListener implements PropertyChangeListener
  {
    public void propertyChange( PropertyChangeEvent evt )
    {
      KeyboardFocusManager focusMgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
      if( focusMgr.getPermanentFocusOwner() == null )
      {
        return;
      }
      setActive( StudioUtilities.containsFocus( TabPane.this ) );
    }
  }

  class FocusChangeListenerAdderRemover implements AncestorListener
  {
    public void ancestorAdded( AncestorEvent event )
    {
      addFocusOwnerListener();
    }

    public void ancestorRemoved( AncestorEvent event )
    {
      removeFocusOwnerListener();
    }

    public void ancestorMoved( AncestorEvent event )
    {
    }
  }

  private class DefaultContextMenuHandler implements IContextMenuHandler<JComponent>
  {
    public JPopupMenu getContextMenu( final JComponent editor )
    {
      JPopupMenu contextMenu = new JPopupMenu();
      contextMenu.add(
        new JMenuItem(
          new AbstractAction( "Close This Tab" )
          {
            public void actionPerformed( ActionEvent e )
            {
              closeTab( editor );
            }
          } ) );
      contextMenu.add(
        new JMenuItem(
          new AbstractAction( "Close Other Tabs" )
          {
            public void actionPerformed( ActionEvent e )
            {
              closeAll( editor );
            }
          } ) );
      contextMenu.add(
        new JMenuItem(
          new AbstractAction( "Close All Tabs" )
          {
            public void actionPerformed( ActionEvent e )
            {
              closeAll( null );
            }
          } ) );
      return contextMenu;
    }

    public void displayContextMenu( JComponent editor, int iX, int iY, Component eventSource )
    {
      getContextMenu( editor ).show( editor, iX, iY );
    }

    private void closeAll( JComponent c )
    {
      Component root = StudioUtilities.showWaitCursor( true );
      try
      {
        ITab leaveOpen = c == null ? null : tabForComponent( c );
        if( leaveOpen == null )
        {
          setVisible( false );
        }
        try
        {
          ITab[] tabs = getTabs();
          for( int i = tabs.length-1; i >= 0; i-- )
          {
            ITab tab = tabs[i];
            if( tab != leaveOpen )
            {
              closeTab( tab.getComponent() );
            }
          }
        }
        finally
        {
          setVisible( true );
        }
      }
      finally
      {
        StudioUtilities.showWaitCursor( false, root );
      }
    }

    private void closeTab( Component c )
    {
      ITab tab = tabForComponent( c );
      getTabContainer().removeTab( tab );
      SettleModalEventQueue.instance().run();
    }

    private ITab tabForComponent( Component c )
    {
      if( c == null )
      {
        return null;
      }
      if( c instanceof ITab )
      {
        return (ITab)c;
      }
      return tabForComponent( c.getParent() );
    }
  }
}
