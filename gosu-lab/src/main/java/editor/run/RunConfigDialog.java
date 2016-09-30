package editor.run;

import editor.AbstractTreeCellRenderer;
import editor.RunMe;
import editor.search.StudioUtilities;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.tabpane.ToolContainer;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.IEditableLabel;
import editor.util.LabToolbarButton;
import editor.util.ToolBar;
import gw.util.Pair;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class RunConfigDialog extends JDialog
{
  private final Experiment _experiment;
  private final RunState _runState;
  private JButton _btnSave;
  private JButton _btnRun;
  private JTree _tree;
  private ITab _configTab;
  private JPanel _configPanel;
  private List<IRunConfig> _addedConfigs;
  private List<IRunConfig> _removedConfigs;
  private List<IRunConfig> _modifiedConfigs;
  private Map<IRunConfig, Pair<IRunConfigParameters,JComponent>> _mapRunConfigs;
  private IRunConfig _runMe;
  private AbstractButton _btnPlus;
  private AbstractButton _btnMinus;

  public RunConfigDialog( Experiment experiment, RunState runState )
  {
    super( RunMe.getEditorFrame(), "Run Configurations", true );
    _experiment = experiment;
    _runState = runState;
    _addedConfigs = new ArrayList<>();
    _removedConfigs = new ArrayList<>();
    _modifiedConfigs = new ArrayList<>();
    _mapRunConfigs = new HashMap<>();

    configUi();
  }

  @Override
  public void setVisible( boolean visible )
  {
    super.setVisible( visible );
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    _configPanel = new JPanel( new BorderLayout() );
    TabPane configTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    _configTab = configTabPane.addTab( "Config", null, _configPanel );

    JPanel buttonPanel = makeButtonPanel();


    CollapsibleSplitPane splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, makeTreePanel(), configTabPane );
    add( splitPane, BorderLayout.CENTER );
    splitPane.setPosition( 30 );

    contentPane.add( splitPane, BorderLayout.CENTER );


    JPanel south = new JPanel( new BorderLayout() );
    south.setBorder( BorderFactory.createEmptyBorder( 4, 0, 0, 0 ) );
    JPanel filler = new JPanel();
    south.add( filler, BorderLayout.CENTER );

    south.add( buttonPanel, BorderLayout.EAST );
    contentPane.add( south, BorderLayout.SOUTH );

    mapCancelKeystroke();

    setSize( 800, 500 );

    StudioUtilities.centerWindowInFrame( this, getOwner() );
  }

  private JPanel makeButtonPanel()
  {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    addRunButton( buttonPanel );

    addSeparator( buttonPanel );

    addSaveButton( buttonPanel );

    addSeparator( buttonPanel );

    JButton btnCancel = new JButton( "Cancel" );
    btnCancel.addActionListener( e -> close() );
    buttonPanel.add( btnCancel );
    return buttonPanel;
  }

  private void addSeparator( JPanel buttonPanel )
  {
    JPanel separator = new JPanel();
    separator.setMinimumSize( new Dimension( 8, 8 ) );
    buttonPanel.add( separator );
  }

  private void addSaveButton( JPanel buttonPanel )
  {
    _btnSave = new JButton( "Save" );
    _btnSave.setMnemonic( 'S' );
    _btnSave.addActionListener(
      e -> {
        save();
        close();
      } );
    buttonPanel.add( _btnSave );
    getRootPane().setDefaultButton( _btnSave );
    _btnSave.setEnabled( false );
  }

  private void addRunButton( JPanel buttonPanel )
  {
    _btnRun = new JButton( _runState.name() );
    _btnRun.setMnemonic( _runState.name().charAt( 0 ) );
    _btnRun.addActionListener(
      e -> {
        _runMe = getSelectedTree().getRunConfig();
        save();
        close();
      } );
    buttonPanel.add( _btnRun );
    getRootPane().setDefaultButton( _btnRun );
    _btnRun.setEnabled( false );
  }

  public IRunConfig getConfigToRun()
  {
    return _runMe;
  }

  private void showConfigPanel()
  {
    RunConfigTree selection = getSelectedTree();
    if( selection == null )
    {
      return;
    }

    _configPanel.removeAll();

    IRunConfig runConfig = selection.getRunConfig();
    if( runConfig != null )
    {
      Pair<IRunConfigParameters, JComponent> pair = _mapRunConfigs.get( runConfig );
      if( pair == null )
      {
        IRunConfigFactory factory = selection.getParent().getFactory();
        IRunConfigParameters copyParams = copyParams( runConfig, factory );
        //noinspection unchecked
        pair = new Pair<>( copyParams, runConfig.makePanel( copyParams, cp -> runConfigChanged( (IRunConfigParameters)cp ) ) );
        _mapRunConfigs.put( runConfig, pair );

        //## todo: don't add to modified unless the params in the panel are modified
        EventQueue.invokeLater( () -> modifiedConfig( runConfig ) );
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

  private void runConfigChanged( IRunConfigParameters params )
  {
    for( Map.Entry<IRunConfig, Pair<IRunConfigParameters, JComponent>> e: _mapRunConfigs.entrySet() )
    {
      if( e.getValue().getFirst() == params )
      {
        RunConfigTree tree = ((RunConfigTree)_tree.getModel().getRoot()).find( e.getKey() );
        ((DefaultTreeModel)_tree.getModel()).nodeChanged( tree );
        break;
      }
    }
  }

  private IRunConfigParameters copyParams( IRunConfig runConfig, IRunConfigFactory factory )
  {
    IRunConfigParameters params;
    if( !_addedConfigs.contains( runConfig ) )
    {
      params = factory.makeParameters();
      //noinspection unchecked
      runConfig.getParams().copy( params );
    }
    else
    {
      params = runConfig.getParams();
    }
    return params;
  }

  private JComponent makeEmptyPanel()
  {
    try
    {
      JLabel label = new JLabel( "<html>Use <img src=" + getClass().getResource( "/images/plus.png" ).toURI().toURL() + "/> and " +
                                 "<img src=" + getClass().getResource( "/images/minus.png" ).toURI().toURL() + "/> to add " +
                                 "and remove new Run Configurations.  You can also open a file and run or debug it directly to " +
                                 "automatically create a new Run Configuration." );
      label.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
      JPanel panel = new JPanel( new BorderLayout() );
      panel.add( label, BorderLayout.NORTH );
      return panel;
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

  private JComponent makeTreePanel()
  {
    DefaultTreeModel model = new DefaultTreeModel( makeRunConfigTree() );
    _tree = new JTree( model );
    _tree.setBackground( EditorUtilities.WINDOW );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new RunConfigTreeCellRenderer( _tree ) );
    _tree.addTreeSelectionListener( new SelectionHandler() );
    JScrollPane scroller = new JScrollPane( _tree );
    scroller.setBorder( BorderFactory.createEmptyBorder() );
    expandAll();
    selectMruRunConfig();

    TabPane tabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    tabPane.addTab( "Configurations", null, scroller );

    ToolContainer toolbar = tabPane.getToolContainer();
    addTools( toolbar.getToolBar() );

    return tabPane;
  }

  private void addTools( ToolBar tb )
  {
    int i = 0;
    _btnPlus = makeButton( new AddConfigAction() );
    tb.add( _btnPlus, i++ );

    _btnMinus = makeButton( new RemoveConfigAction() );
    tb.add( _btnMinus, i );
  }

  private LabToolbarButton makeButton( Action action )
  {
    LabToolbarButton item = new LabToolbarButton( null, null, 2, 0 );
    item.setAction( action );
    return item;
  }

  private void selectMruRunConfig()
  {
    IRunConfig runConfig = _experiment.getMruRunConfig();
    if( runConfig == null )
    {
      return;
    }

    RunConfigTree tree = ((RunConfigTree)_tree.getModel().getRoot()).find( runConfig );
    if( tree != null )
    {
      tree.select( _tree );
    }
  }

  private RunConfigTree makeRunConfigTree()
  {
    RunConfigTree root = new RunConfigTree();

    RunConfigTree program = new RunConfigTree( ProgramRunConfigFactory.instance(), root );
    root.addChild( program );

    RunConfigTree remote = new RunConfigTree( RemoteRunConfigFactory.instance(), root );
    root.addChild( remote );

    for( IRunConfig rc : _experiment.getRunConfigs() )
    {
      if( rc instanceof ProgramRunConfig )
      {
        program.addChild( new RunConfigTree( rc, program ) );
      }
      else if( rc instanceof RemoteRunConfig )
      {
        remote.addChild( new RunConfigTree( rc, remote ) );
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

  private void save()
  {
    _modifiedConfigs.forEach( e -> {
      Pair<IRunConfigParameters, JComponent> pair = _mapRunConfigs.get( e );
      //noinspection unchecked
      e.setParams( pair.getFirst() );
    });
    _addedConfigs.forEach( _experiment::addRunConfig );
    _removedConfigs.forEach( _experiment::removeRunConfig );
    if( _runMe != null )
    {
      _experiment.setMruRunConfig( _runMe );
    }
    _experiment.save();
  }

  private void close()
  {
    dispose();
  }

  public RunConfigTree getSelectedTree()
  {
    return (RunConfigTree)_tree.getLastSelectedPathComponent();
  }

  private void mapCancelKeystroke()
  {
    Object key = getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).get( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ) );
    if( key == null )
    {
      key = "Cancel";
      getRootPane().getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ).put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), key );
    }
    getRootPane().getActionMap().put( key,
                                      new AbstractAction()
                                      {
                                        public void actionPerformed( ActionEvent e )
                                        {
                                          close();
                                        }
                                      } );
  }

  public class RunConfigTreeCellRenderer extends AbstractTreeCellRenderer<RunConfigTree>
  {
    public RunConfigTreeCellRenderer( JTree tree )
    {
      super( tree );
    }

    public void configure()
    {
      RunConfigTree node = getNode();
      if( node == null )
      {
        return;
      }

      setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

      IRunConfig runConfig = node.getRunConfig();
      if( runConfig != null )
      {
        Pair<IRunConfigParameters, JComponent> pair = _mapRunConfigs.get( runConfig );
        String name;
        if( pair != null )
        {
          // use potientially modified name
          name = pair.getFirst().getName();
        }
        else
        {
          name = runConfig.getName();
        }
        if( _experiment.getMruRunConfig() != null && _experiment.getMruRunConfig().equals( runConfig ) )
        {
          setText( name );
        }
        else
        {
          setText( "<html><font color=#808080>" + name + "</font>" );
        }
      }
      else
      {
        IRunConfigFactory factory = node.getFactory();
        if( factory != null )
        {
          setText( "<html><b>" + factory.getName() + "</b>" );
        }
      }
      setIcon( node.getIcon() );
    }
  }

  private class SelectionHandler implements TreeSelectionListener
  {
    @Override
    public void valueChanged( TreeSelectionEvent e )
    {
      // Show the RunConfig panel for the selection
      showConfigPanel();

      // Enable Run/Debug button
      RunConfigTree selection = getSelectedTree();
      _btnRun.setEnabled( selection != null &&
                          selection.getRunConfig() != null &&
                          !isRunningOrDebugging() &&
                          canRunOrDebug( selection.getRunConfig() ) );

      // Display factory type on Tab
      if( selection != null )
      {
        if( selection.isTerminal() )
        {
          selection = selection.getParent();
        }
        ((IEditableLabel)_configTab.getLabel()).setDisplayName( selection.getFactory().getName() );
      }

      // Update Plus/Minus tools
      EventQueue.invokeLater( () -> {
        _btnPlus.setEnabled( _btnPlus.getAction().isEnabled() );
        _btnMinus.setEnabled( _btnMinus.getAction().isEnabled() );
      } );
    }

    private boolean isRunningOrDebugging()
    {
      return RunMe.getEditorFrame().getGosuPanel().isRunning() ||
             RunMe.getEditorFrame().getGosuPanel().isDebugging();
    }

    private boolean canRunOrDebug( IRunConfig runConfig )
    {
      return _runState == RunState.Run ? runConfig.isRunnable() : runConfig.isDebuggable();
    }
  }

  public class AddConfigAction extends AbstractAction
  {
    public AddConfigAction()
    {
      super( "Add", EditorUtilities.loadIcon( "images/plus.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        RunConfigTree selection = getSelectedTree();
        if( selection.isTerminal() )
        {
          selection = selection.getParent();
        }

        IRunConfigFactory factory = selection.getFactory();
        IRunConfigParameters params = factory.makeParameters();
        params.setName( "Unnamed" );
        //noinspection unchecked
        IRunConfig runConfig = factory.newRunConfig( params );
        addedConfig( runConfig );

        RunConfigTree newNode = new RunConfigTree( runConfig, selection );
        selection.insertViaModel( _tree, newNode, 0 );
        newNode.select( _tree );
      }
    }

    @Override
    public boolean isEnabled()
    {
      RunConfigTree selection = getSelectedTree();
      return selection != null;
    }
  }

  public class RemoveConfigAction extends AbstractAction
  {
    public RemoveConfigAction()
    {
      super( "Remove", EditorUtilities.loadIcon( "images/minus.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        RunConfigTree selection = getSelectedTree();
        selection.deleteViaModel( _tree, selection );
        EventQueue.invokeLater( () -> removedConfig( selection.getRunConfig() ) );
        selectMruRunConfig();
        if( getSelectedTree() == null )
        {
          RunConfigTree tree = (RunConfigTree)_tree.getModel().getRoot();
          tree.getChildAt( 0 ).select( _tree );
        }
      }
    }

    @Override
    public boolean isEnabled()
    {
      RunConfigTree selection = getSelectedTree();
      return selection != null && selection.isTerminal();
    }
  }

  private void addedConfig( IRunConfig runConfig )
  {
    _addedConfigs.add( runConfig );
    enableSave();
  }

  private void removedConfig( IRunConfig runConfig )
  {
    _modifiedConfigs.remove( runConfig );

    if( !_addedConfigs.remove( runConfig ) )
    {
      _removedConfigs.add( runConfig );
    }
    enableSave();
  }

  private void modifiedConfig( IRunConfig runConfig )
  {
    if( !_addedConfigs.contains( runConfig ) )
    {
      _modifiedConfigs.add( runConfig );
    }
    enableSave();
  }

  private void enableSave()
  {
    _btnSave.setEnabled( !(_modifiedConfigs.isEmpty() && _removedConfigs.isEmpty() && _addedConfigs.isEmpty()) );
  }
}