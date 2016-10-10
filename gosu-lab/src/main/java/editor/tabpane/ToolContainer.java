package editor.tabpane;


import editor.Scheme;
import editor.actions.GenericAction;
import editor.splitpane.ICaptionActionListener;
import editor.util.EditorUtilities;
import editor.util.LabToolbarButton;
import editor.util.ToolBar;

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
  private LabToolbarButton _btnDisplayTabs;
  private TabPane _tabPane;
  private LabToolbarButton _btnMinimize;
  private LabToolbarButton _btnRestore;
  private LabToolbarButton _btnMaximize;


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
    _toolbar.setBackground( Scheme.active().getControl() );

    _toolbar.add( new JPanel() );
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
      return BorderFactory.createMatteBorder( 0, 0, 1, 0, Scheme.active().getScrollbarBorderColor() );
    }
    else if( tp == TabPosition.BOTTOM )
    {
      return BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getScrollbarBorderColor() );
    }
    else if( tp == TabPosition.LEFT )
    {
      return BorderFactory.createMatteBorder( 0, 0, 0, 1, Scheme.active().getScrollbarBorderColor() );
    }
    else
    {
      return BorderFactory.createMatteBorder( 0, 1, 0, 0, Scheme.active().getScrollbarBorderColor() );
    }
  }

  private void addDynamicTools()
  {
    _btnDisplayTabs = new LabToolbarButton( new DisplayTabsAction() );
    _toolbar.add( _btnDisplayTabs );
    if( _tabPane.hasAtLeastOneOfMinMaxRestore() )
    {
      _toolbar.addSeparator();
    }
  }

  private void addCaptionTools()
  {
    _btnMinimize = new LabToolbarButton( new MinimizeAction() );
    if( _tabPane.isMinimizable() )
    {
      _toolbar.add( _btnMinimize );
    }
    _btnRestore = new LabToolbarButton( new RestoreAction() );
    if( _tabPane.isRestorable() )
    {
      _toolbar.add( _btnRestore );
    }
    _btnRestore.setVisible( false );
    _btnMaximize = new LabToolbarButton( new MaximizeAction() );
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
      LabToolbarButton btnCloseTab = new LabToolbarButton( new CloseTabAction() );
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
              null,
              ' ',
              null,
              "Display Tabs",
              null);
      setIcon( new ImageIcon( EditorUtilities.createSystemColorImage( EditorUtilities.loadIcon( "images/caption_list.gif" ).getImage() ) ) );
      setEnabled( false );
      _tabPane.getTabContainer().addSelectionListener(
        e -> {
          _enabled = _tabPane.getTabContainer().getTabCount() > 0;
          setEnabled( _enabled ); // fire changed
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
      tabListPopup.addNodeChangeListener( e -> _tabPane.getTabContainer().selectTab( (ITab)e.getSource(), true) );
      tabListPopup.show( _btnDisplayTabs, 0, _btnDisplayTabs.getY() + _btnDisplayTabs.getHeight() );
    }
  }

  class CloseTabAction extends GenericAction
  {
    private boolean _enabled;
    public CloseTabAction()
    {
      super( "_closeTab",
              null, //"Close Tab",
              null,
              ' ',
              null,
              "Close Tab",
              null );
      setIcon( new ImageIcon( EditorUtilities.createSystemColorImage( EditorUtilities.loadIcon( "images/caption_close.png" ).getImage() ) ) );
      setEnabled( false );
      _tabPane.getTabContainer().addSelectionListener(
        e -> {
          _enabled = _tabPane.getTabContainer().getTabCount() > 0 &&
                     _tabPane.getTabContainer().getSelectedTab().canClose();
          setEnabled( _enabled ); // fire changed
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
              null,
              ' ',
              null,
              "Minimize",
              null);
      setIcon( new ImageIcon( EditorUtilities.createSystemColorImage( EditorUtilities.loadIcon( "images/caption_min.gif" ).getImage() ) ) );
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
              null,
              ' ',
              null,
              "Restore",
              null );
      setIcon( new ImageIcon( EditorUtilities.createSystemColorImage( EditorUtilities.loadIcon( "images/caption_restore.gif" ).getImage() ) ) );
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
              null,
              ' ',
              null,
              "Maximize",
              null);
      setIcon( new ImageIcon( EditorUtilities.createSystemColorImage( EditorUtilities.loadIcon( "images/caption_max.gif" ).getImage() ) ) );
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
