package editor.tabpane;


import editor.actions.GenericAction;
import editor.splitpane.ICaptionActionListener;
import editor.util.ToolBar;
import editor.util.XPToolbarButton;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 */
public class ToolContainer extends JPanel
{
  private ToolBar _toolbar;
  private XPToolbarButton _btnDisplayTabs;
  private TabPane _tabPane;
  private XPToolbarButton _btnMinimize;
  private XPToolbarButton _btnRestore;
  private XPToolbarButton _btnMaximize;


  public ToolContainer( TabPane tabPane )
  {
    super( new BorderLayout() );
    _tabPane = tabPane;
    _tabPane.getTabContainer().addTabpositionListener( new TabPositionChangeHandler() );
    addCaptionTypeChangeListener();
    configUi();
  }

  public ToolBar getToolBar()
  {
    return _toolbar;
  }

  private void configUi()
  {
    setBorder( makeBorder() );
    _toolbar = new ToolBar( isVertical() ? ToolBar.VERTICAL : ToolBar.HORIZONTAL );
    if( _tabPane.isDynamic() )
    {
      addDynamicTools();
    }
    addCaptionTools();
    add( _toolbar, BorderLayout.SOUTH );
  }

  public void addNotify()
  {
    super.addNotify();
    revalidate();
  }

  public TabPane getTabPane()
  {
    return _tabPane;
  }

  public Border getBorder()
  {
    if( _tabPane != null && !_tabPane.isShowing() )
    {
      return null;
    }
    return super.getBorder();
  }

  private Border makeBorder()
  {
    TabPosition tp = _tabPane.getTabContainer().getTabPosition();
    if( tp == TabPosition.TOP )
    {
      return BorderFactory.createMatteBorder( 0, 0, 1, 0, SystemColor.controlShadow );
    }
    else if( tp == TabPosition.BOTTOM )
    {
      return BorderFactory.createMatteBorder( 1, 0, 0, 0, SystemColor.controlShadow );
    }
    else if( tp == TabPosition.LEFT )
    {
      return BorderFactory.createMatteBorder( 0, 0, 0, 1, SystemColor.controlShadow );
    }
    else
    {
      return BorderFactory.createMatteBorder( 0, 1, 0, 0, SystemColor.controlShadow );
    }
  }

  private void addDynamicTools()
  {
    _btnDisplayTabs = new XPToolbarButton( new DisplayTabsAction() );
    _btnDisplayTabs.setToolTipText( "Open views" );
    _toolbar.add( _btnDisplayTabs );
    if( _tabPane.hasAtLeastOneOfMinMaxRestore() )
    {
      _toolbar.addSeparator();
    }
  }

  private void addCaptionTools()
  {
    _btnMinimize = new XPToolbarButton( new MinimizeAction() );
    _btnMinimize.setToolTipText( "Minimize" );
    if( _tabPane.isMinimizable() )
    {
      _toolbar.add( _btnMinimize );
    }
    _btnRestore = new XPToolbarButton( new RestoreAction() );
    _btnRestore.setToolTipText( "Restore" );
    if( _tabPane.isRestorable() )
    {
      _toolbar.add( _btnRestore );
    }
    _btnRestore.setVisible( false );
    _btnMaximize = new XPToolbarButton( new MaximizeAction() );
    _btnMaximize.setToolTipText( "Maximize" );
    if( _tabPane.isMaximizable() )
    {
      _toolbar.add( _btnMaximize );
    }
    if( _tabPane.isDynamic() )
    {
      if( _tabPane.hasAtLeastOneOfMinMaxRestore() )
      {
        _toolbar.addSeparator();
      }
      XPToolbarButton btnCloseTab = new XPToolbarButton( new CloseTabAction() );
      btnCloseTab.setToolTipText( "Close tab" );
      _toolbar.add( btnCloseTab );
    }
  }

  private boolean isVertical()
  {
    return _tabPane.getTabContainer().isVertical();
  }

  private void addCaptionTypeChangeListener()
  {
    EventQueue.invokeLater(
      () -> {
        if( _tabPane.getTabAndToolContainer() != null )
        {
          _tabPane.getTabAndToolContainer().addCaptionTypeListener( new CaptionTypeChangeHandler() );
        }
      } );
  }

  public ToolBar getToolbar()
  {
    return _toolbar;
  }

  class DisplayTabsAction extends GenericAction
  {
    private boolean _enabled;
    public DisplayTabsAction()
    {
      super( "_displayTabs",
              null, //"Display Tabs",
              "images/caption_list.gif",
              ' ',
              null,
              "Display Tabs",
              null);
      setEnabled( false );
      _tabPane.getTabContainer().addSelectionListener(
        new ChangeListener()
        {
          public void stateChanged( ChangeEvent e )
          {
            _enabled = _tabPane.getTabContainer().getTabCount() > 0;
            setEnabled( _enabled ); // fire changed
          }
        } );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      displayTabPopup();
    }

    @Override
    public boolean isEnabled() {
      return _enabled;
    }

    private void displayTabPopup()
    {
      if( _tabPane.getTabContainer().getTabCount() == 0 )
      {
        return;
      }

      TabListPopup tabListPopup = new TabListPopup( _tabPane.getTabContainer() );
      tabListPopup.addNodeChangeListener(
        new ChangeListener()
        {
          public void stateChanged( ChangeEvent e )
          {
            _tabPane.getTabContainer().selectTab( (ITab)e.getSource(), true);
          }
        } );
      tabListPopup.show( _btnDisplayTabs, _btnDisplayTabs.getX(),
                         _btnDisplayTabs.getY() + _btnDisplayTabs.getHeight() );
    }
  }

  class CloseTabAction extends GenericAction
  {
    private boolean _enabled;
    public CloseTabAction()
    {
      super( "_closeTab",
              null, //"Close Tab",
              "images/caption_close.gif",
              ' ',
              null,
              "Close Tab",
              null );
      setEnabled( false );
      _tabPane.getTabContainer().addSelectionListener(
        new ChangeListener()
        {
          public void stateChanged( ChangeEvent e )
          {
            _enabled = _tabPane.getTabContainer().getTabCount() > 0 &&
                        _tabPane.getTabContainer().getSelectedTab().canClose();
            setEnabled( _enabled ); // fire changed
          }
        } );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      closeTab();
    }

    @Override
    public boolean isEnabled() {
      return _enabled;
    }

    private void closeTab()
    {
      if( _tabPane.getTabContainer().getTabCount() == 0 )
      {
        return;
      }
      _tabPane.getTabContainer().removeTab( _tabPane.getTabContainer().getSelectedTab() );
    }
  }

  class MinimizeAction extends GenericAction
  {
    public MinimizeAction()
    {
      super( "_minimize",
              null, //"Minimize",
              "images/caption_min.gif",
              ' ',
              null,
              "Minimize",
              null);
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      ((TabAndToolContainer)_tabPane.getCaption()).fireCaptionActionPerformed(
        ICaptionActionListener.ActionType.MINIMIZE  );
      _btnMinimize.setVisible( false );
      _btnRestore.setVisible( true );
      _btnMaximize.setVisible( true );
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }

  class RestoreAction extends GenericAction
  {
    public RestoreAction()
    {
      super( "_restore",
              null, //"Restore",
              "images/caption_restore.gif",
              ' ',
              null,
              "Restore",
              null );
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      ((TabAndToolContainer)_tabPane.getCaption()).fireCaptionActionPerformed(
        ICaptionActionListener.ActionType.RESTORE  );
      _btnRestore.setVisible( false );
      _btnMinimize.setVisible( true );
      _btnMaximize.setVisible( true );
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }

  class MaximizeAction extends GenericAction
  {
    public MaximizeAction()
    {
      super( "_maximize",
              null, //"Maximize",
              "images/caption_max.gif",
              ' ',
              null,
              "Maximize",
              null);
    }

    @Override
    public void actionPerformed( ActionEvent e )
    {
      ((TabAndToolContainer)_tabPane.getCaption()).fireCaptionActionPerformed(
        ICaptionActionListener.ActionType.MAXIMIZE  );
      _btnMaximize.setVisible( false );
      _btnRestore.setVisible( true );
      _btnMaximize.setVisible( true );
    }

    @Override
    public boolean isEnabled() {
      return true;
    }
  }

  class TabPositionChangeHandler implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      getToolBar().setOrientation( _tabPane.getTabContainer().isVertical()
                                   ? ToolBar.VERTICAL
                                   : ToolBar.HORIZONTAL );
    }
  }

  class CaptionTypeChangeHandler implements ChangeListener
  {
    public void stateChanged( ChangeEvent e )
    {
      EventQueue.invokeLater(
        () -> {
          ICaptionActionListener.ActionType actionType = _tabPane.getTabAndToolContainer().getCaptionType();
          _btnMaximize.setVisible( actionType != ICaptionActionListener.ActionType.MAXIMIZE );
          _btnRestore.setVisible( actionType != ICaptionActionListener.ActionType.RESTORE );
          _btnMinimize.setVisible( actionType != ICaptionActionListener.ActionType.MINIMIZE );
        } );
    }
  }
}
