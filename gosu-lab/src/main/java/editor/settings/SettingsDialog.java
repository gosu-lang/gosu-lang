package editor.settings;

import editor.AbstractTreeCellRenderer;
import editor.GosuPanel;
import editor.IHandleCancel;
import editor.LabFrame;
import editor.Scheme;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.IEditableLabel;
import gw.util.Pair;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 */
public class SettingsDialog extends JDialog implements IHandleCancel
{
  private JPanel _configPanel;
  private ITab _configTab;
  private JButton _btnOk;
  private Experiment _experiment;
  private JTree _tree;
  private List<ISettings> _modifiedSettings;
  private Map<ISettings, Pair<ISettingsParameters,JComponent>> _mapSettings;
  private ISettings _mruSettings;

  public SettingsDialog()
  {
    super( LabFrame.instance(), "Settings", true );
    _experiment = getGosuPanel().getExperiment();
    _modifiedSettings = new ArrayList<>();
    _mapSettings = new HashMap<>();
    configUi();
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    _configPanel = new JPanel( new BorderLayout() );
    TabPane configTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    _configTab = configTabPane.addTab( "No Selection", null, _configPanel );

    JPanel buttonPanel = makeButtonPanel();

    CollapsibleSplitPane splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, makeTreePanel(), configTabPane );
    add( splitPane, BorderLayout.CENTER );
    splitPane.setPosition( 30 );

    contentPane.add( splitPane, BorderLayout.CENTER );

    JPanel south = new JPanel( new BorderLayout() );
    south.setBackground( Scheme.active().getMenu() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    filler.setBackground( Scheme.active().getMenu() );
    south.add( filler, BorderLayout.CENTER );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );
    contentPane.setBackground( Scheme.active().getMenu() );

    mapCancelKeystroke( "Cancel", this::close );

    setSize( 800, 500 );

    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void save()
  {
    _modifiedSettings.forEach( e -> {
      Pair<ISettingsParameters, JComponent> pair = _mapSettings.get( e );
      //noinspection unchecked
      e.setParams( pair.getFirst() );
    });
    _experiment.setMruSettings( _mruSettings );
    _experiment.save();
    LabFrame.saveSettings();
  }

  private void close()
  {
    dispose();
  }

  public SettingsTree getSelectedTree()
  {
    return (SettingsTree)_tree.getLastSelectedPathComponent();
  }
  
  private JPanel makeButtonPanel()
  {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground( Scheme.active().getMenu() );
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    addOkButton( buttonPanel );

    addSeparator( buttonPanel );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );
    return buttonPanel;
  }

  private void addSeparator( JPanel buttonPanel )
  {
    JPanel separator = new JPanel();
    separator.setBackground( Scheme.active().getMenu() );
    separator.setMinimumSize( new Dimension( 8, 8 ) );
    buttonPanel.add( separator );
  }

  private void addOkButton( JPanel buttonPanel )
  {
    _btnOk = new JButton( "OK" );
    _btnOk.addActionListener(
      e -> {
        save();
        close();
      } );
    buttonPanel.add( _btnOk );
    getRootPane().setDefaultButton( _btnOk );
  }

  private JComponent makeTreePanel()
  {
    DefaultTreeModel model = new DefaultTreeModel( makeSettingsTree() );
    _tree = new JTree( model );
    _tree.setBackground( Scheme.active().getWindow() );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new SettingsTreeCellRenderer( _tree ) );
    _tree.addTreeSelectionListener( new SelectionHandler() );
    JScrollPane scroller = new JScrollPane( _tree );
    scroller.setBorder( BorderFactory.createEmptyBorder() );
    expandAll();
    selectMruSettings();

    TabPane tabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    tabPane.addTab( "Settings", null, scroller );

    return tabPane;
  }

  private SettingsTree makeSettingsTree()
  {
    SettingsTree root = new SettingsTree();

    Collection<ISettings> settings = new ArrayList<>( _experiment.getSettings().values() );
    settings.addAll( LabFrame.instance().getSettings().values() );

    while( !settings.isEmpty() )
    {
      for( Iterator<ISettings> iterator = settings.iterator(); iterator.hasNext(); )
      {
        ISettings rc = iterator.next();
        SettingsTree parent = root.find( rc.getParentPath() );
        if( parent != null )
        {
          parent.addChild( new SettingsTree( rc, parent ) );
          iterator.remove();
        }
      }
    }
    return root;
  }
  
  public void expandAll()
  {
    expandAll( 0, _tree.getRowCount() );
  }

  void expandAll( int startingIndex, int rowCount )
  {
    for( int i = startingIndex; i < rowCount; ++i )
    {
      _tree.expandRow( i );
    }

    if( _tree.getRowCount() != rowCount )
    {
      expandAll( rowCount, _tree.getRowCount() );
    }
  }

  private void showConfigPanel()
  {
    _configPanel.removeAll();

    SettingsTree selection = getSelectedTree();
    if( selection == null )
    {
      return;
    }

    ISettings settings = selection.getSettings();
    if( settings != null )
    {
      Pair<ISettingsParameters, JComponent> pair = _mapSettings.get( settings );
      if( pair == null )
      {
        ISettingsParameters copyParams = copyParams( settings );
        //noinspection unchecked
        pair = new Pair<>( copyParams, settings.makePanel( copyParams, cp -> settingsChanged( (ISettingsParameters)cp ) ) );
        _mapSettings.put( settings, pair );

        //## todo: don't add to modified unless the params in the panel are modified
        EventQueue.invokeLater( () -> modifiedConfig( settings ) );
      }
      _configPanel.add( pair.getSecond(), BorderLayout.CENTER );
    }
    else
    {
      JComponent emptySelection = makeEmptyPanel();
      _configPanel.add( emptySelection, BorderLayout.CENTER );
      _configPanel.revalidate();
    }
    _configPanel.repaint();
  }

  private void settingsChanged( ISettingsParameters params )
  {
    for( Map.Entry<ISettings, Pair<ISettingsParameters, JComponent>> e: _mapSettings.entrySet() )
    {
      if( e.getValue().getFirst() == params )
      {
        SettingsTree tree = ((SettingsTree)_tree.getModel().getRoot()).find( e.getKey() );
        ((DefaultTreeModel)_tree.getModel()).nodeChanged( tree );
        break;
      }
    }
  }

  private ISettingsParameters copyParams( ISettings settings )
  {
    ISettingsParameters params = settings.makeDefaultParameters( _experiment );
    //noinspection unchecked
    settings.getParams().copy( params );
    return params;
  }

  private JComponent makeEmptyPanel()
  {
    return new JPanel( new BorderLayout() );
  }
 
  private void modifiedConfig( ISettings settings )
  {
    if( _modifiedSettings.contains( settings ) )
    {
      throw new IllegalStateException();
    }

    _modifiedSettings.add( settings );
  }
  
  private void selectMruSettings()
  {
    ISettings settings = _experiment.getMruSettings();
    if( settings == null )
    {
      _tree.setSelectionRow( 0 );
      return;
    }

    SettingsTree tree = ((SettingsTree)_tree.getModel().getRoot()).find( settings );
    if( tree != null )
    {
      tree.select( _tree );
    }
  }

  public class SettingsTreeCellRenderer extends AbstractTreeCellRenderer<SettingsTree>
  {
    public SettingsTreeCellRenderer( JTree tree )
    {
      super( tree );
    }

    public void configure()
    {
      SettingsTree node = getNode();
      if( node == null )
      {
        return;
      }

      setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

      ISettings settings = node.getSettings();
      if( settings != null )
      {
        setText( settings.getName() );
      }
      else
      {
        setText( "<root>" );
      }
      setIcon( node.getIcon() );
    }

  }

  private class SelectionHandler implements TreeSelectionListener
  {
    @Override
    public void valueChanged( TreeSelectionEvent e )
    {
      // Show the Settings panel for the selection
      showConfigPanel();

      SettingsTree selection = getSelectedTree();

      if( selection != null )
      {
        // Display name on Tab
        ((IEditableLabel)_configTab.getLabel()).setDisplayName( selection.getSettings().getName() );

        _mruSettings = selection.getSettings();
      }
      else
      {
        ((IEditableLabel)_configTab.getLabel()).setDisplayName( "No Selection" );
        _mruSettings = null;
        _configPanel.repaint();
      }
    }
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

}

