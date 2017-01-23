package editor.debugger;

import editor.DefaultContextMenuHandler;
import editor.EditorHost;
import editor.EditorScrollPane;
import editor.GosuClassLineInfoManager;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.GotoExceptionTypePopup;
import editor.IHandleCancel;
import editor.LabFrame;
import editor.Scheme;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.tabpane.ToolContainer;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import editor.util.LabButton;
import editor.util.LabToolbarButton;
import editor.util.SettleModalEventQueue;
import editor.util.ToolBar;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.JavaTypes;
import gw.util.ContextSymbolTableUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class BreakpointsDialog extends JDialog implements IHandleCancel
{
  private static BreakpointsDialog _current;

  private JTree _tree;
  private JButton _btnViewSource;
  private JPanel _configPanel;
  private AbstractButton _btnPlus;
  private Map<Breakpoint, JPanel> _mapToPanel;
  private Runnable _listener;
  private BreakpointTree _exceptionBpTree;
  private JCheckBox _cbRunScript;
  private JCheckBox _cbSuspend;
  private JCheckBox _cbUncaught;
  private JCheckBox _cbCaught;
  private JLabel _labelCondition;
  private GosuEditor _fieldExpr;
  private GosuEditor _fieldRunScript;

  public static BreakpointsDialog getOrCreate( Breakpoint bp )
  {
    if( _current == null )
    {
      _current = new BreakpointsDialog( bp );
    }
    return _current;
  }
  @SuppressWarnings("UnusedDeclaration")
  public static BreakpointsDialog getOrCreate()
  {
    if( _current == null )
    {
      _current = new BreakpointsDialog();
    }
    return _current;
  }

  private BreakpointsDialog( Breakpoint bp )
  {
    this();
    selectBreakpoint( bp );
  }

  private BreakpointsDialog()
  {
    super( LabFrame.instance(), "Breakpoints" );
    _mapToPanel = new HashMap<>();
    configUi();
    setDefaultCloseOperation( DISPOSE_ON_CLOSE );
    addCloseListener();
  }

  private void selectBreakpoint( Breakpoint bp )
  {
    if( bp == null )
    {
      return;
    }

    BreakpointTree tree = ((BreakpointTree)_tree.getModel().getRoot()).find( bp );
    _tree.setSelectionPath( tree.getPath() );
  }

  protected void configUi()
  {
    JComponent contentPane = (JComponent)getContentPane();
    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.setLayout( new BorderLayout() );

    _configPanel = new JPanel( new BorderLayout() );
    TabPane configTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    configTabPane.addTab( "Settings", null, _configPanel );

    JPanel buttonPanel = makeButtonPanel();

    CollapsibleSplitPane splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, createBreakpointsList(), configTabPane );
    add( splitPane, BorderLayout.CENTER );
    splitPane.setPosition( 35 );

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

    mapCancelKeystroke( "Close", this::close );

    setSize( 800, 500 );
    EditorUtilities.centerWindowInFrame( this, getOwner() );
  }

  private void addCloseListener()
  {
    addWindowListener( new WindowAdapter() {
      public void windowClosed( WindowEvent e )
      {
        BreakpointManager bpm = getGosuPanel().getBreakpointManager();
        bpm.removeChangeLisener( _listener );
        _current = null;
      }
    });
  }

  private JPanel createConfigPanel( Breakpoint bp )
  {
    JPanel configPanel = new JPanel( new GridBagLayout() );
    configPanel.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );

    final GridBagConstraints c = new GridBagConstraints();

    int iY = 0;

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 10, 10 );
    _cbSuspend = new JCheckBox( "Suspend" );
    EventQueue.invokeLater( () -> _cbSuspend.setSelected( bp.isSuspend() ) );
    _cbSuspend.addActionListener( e -> {
      boolean selected = _cbSuspend.isSelected();
      bp.setSuspend( selected );
      _labelCondition.setEnabled( selected );
      _fieldExpr.setEnabled( selected );
    } );
    configPanel.add( _cbSuspend, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 10, 0, 5, 10 );
    _labelCondition = new JLabel( "Condition:" );
    configPanel.add( _labelCondition, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = .3;
    c.insets = new Insets( 0, 0, 5, 0 );
    IType type = TypeSystem.getByFullNameIfValidNoJava( bp.getFqn() );
    _fieldExpr = new GosuEditor( type instanceof IGosuClass && bp.getLine() > 0 ? ContextSymbolTableUtil.getSymbolTableAtOffset( (IGosuClass)type, bp.getOffset() ) : new StandardSymbolTable( true ),
      new GosuClassLineInfoManager(), new AtomicUndoManager( 10000 ), ScriptabilityModifiers.SCRIPTABLE, new DefaultContextMenuHandler(), false, true );
    _fieldExpr.setExpectedType( JavaTypes.pBOOLEAN() );
    addEscapeHandler( _fieldExpr );
    _fieldExpr.setAccessAll( true );
    try
    {
      String expr = bp.getExpression();
      _fieldExpr.read( new TypelessScriptPartId( "debugger condition" ), expr == null ? "" : expr );
      EventQueue.invokeLater( _fieldExpr::parse );
      _fieldExpr.getEditor().getDocument().addDocumentListener( new DocHandler( bp, true ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    JPanel panel = new JPanel( new BorderLayout() );
    panel.setPreferredSize( new Dimension( 30, 30 ) );
    panel.add( _fieldExpr, BorderLayout.CENTER );
    configPanel.add( panel, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.NONE;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 0;
    c.weighty = 0;
    c.insets = new Insets( 5, 0, 0, 5 );
    _cbRunScript= new JCheckBox( "Run script" );
    EventQueue.invokeLater( () -> _cbRunScript.setSelected( bp.isRunScriptOn() ) );
    _cbRunScript.addActionListener( e -> {
      boolean selected = _cbRunScript.isSelected();
      bp.setRunScriptOn( selected );
      _fieldRunScript.setEnabled( selected );
    } );
    configPanel.add( _cbRunScript, c );

    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = iY++;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridheight = 1;
    c.weightx = 1;
    c.weighty = .7;
    c.insets = new Insets( 0, 0, 10, 0 );
    type = TypeSystem.getByFullNameIfValidNoJava( bp.getFqn() );
    _fieldRunScript = new GosuEditor( type instanceof IGosuClass && bp.getLine() > 0 ? ContextSymbolTableUtil.getSymbolTableAtOffset( (IGosuClass)type, bp.getOffset() ) : new StandardSymbolTable( true ),
      new GosuClassLineInfoManager(), new AtomicUndoManager( 10000 ), ScriptabilityModifiers.SCRIPTABLE, new DefaultContextMenuHandler(), false, true );
    addEscapeHandler( _fieldRunScript );
    _fieldRunScript.setAccessAll( true );
    //exprField.showFeedback( false );
    try
    {
      String expr = bp.getRunScript();
      _fieldRunScript.read( new TypelessScriptPartId( "debugger run script" ), expr == null ? "" : expr );
      EventQueue.invokeLater( _fieldRunScript::parse );
      _fieldRunScript.getEditor().getDocument().addDocumentListener( new DocHandler( bp, false ) );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    panel = new JPanel( new BorderLayout() );
    panel.setPreferredSize( new Dimension( 30, 30 ) );
    panel.add( _fieldRunScript, BorderLayout.CENTER );
    configPanel.add( panel, c );

    if( bp.isExceptionBreakpoint() )
    {
      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.NONE;
      c.gridx = 0;
      c.gridy = iY++;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 0;
      c.weighty = 0;
      c.insets = new Insets( 10, 0, 5, 10 );
      _cbCaught = new JCheckBox( "Caught exception" );
      _cbCaught.setSelected( bp.isCaughtException() );
      _cbCaught.addActionListener( e -> {
        bp.setCaughtException( _cbCaught.isSelected() );
        updateBreakpointJdi( bp );
      } );
      configPanel.add( _cbCaught, c );

      c.anchor = GridBagConstraints.WEST;
      c.fill = GridBagConstraints.NONE;
      c.gridx = 0;
      c.gridy = iY;
      c.gridwidth = GridBagConstraints.REMAINDER;
      c.gridheight = 1;
      c.weightx = 0;
      c.weighty = 0;
      c.insets = new Insets( 0, 0, 5, 10 );
      _cbUncaught = new JCheckBox( "Uncaught exception" );
      _cbUncaught.setSelected( bp.isUncaughtException() );
      _cbUncaught.addActionListener( e -> {
        bp.setUncaughtException( _cbUncaught.isSelected() );
        updateBreakpointJdi( bp );
      } );
      configPanel.add( _cbUncaught, c );
    }

    return configPanel;
  }

  private void addEscapeHandler( GosuEditor gosuEditor )
  {
    gosuEditor.getEditor().addKeyListener( new KeyAdapter() {
      @Override
      public void keyPressed( KeyEvent e )
      {
        if( e.getKeyCode() == KeyEvent.VK_ESCAPE && !gosuEditor.isCompletionPopupShowing() )
        {
          close();
        }
      }
    } );
  }

  private JPanel makeButtonPanel()
  {
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground( Scheme.active().getMenu() );
    buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.X_AXIS ) );

    addRunButton( buttonPanel );

    addSeparator( buttonPanel );

    JButton btnClose = new LabButton( "Close" );
    btnClose.addActionListener( e -> close() );
    buttonPanel.add( btnClose );
    getRootPane().setDefaultButton( btnClose );

    return buttonPanel;
  }

  private void addSeparator( JPanel buttonPanel )
  {
    JPanel separator = new JPanel();
    separator.setBackground( Scheme.active().getMenu() );
    separator.setMinimumSize( new Dimension( 8, 8 ) );
    buttonPanel.add( separator );
  }

  private void addRunButton( JPanel buttonPanel )
  {
    _btnViewSource = new LabButton( "View Source" );
    _btnViewSource.setMnemonic( 'V' );
    _btnViewSource.addActionListener( new ViewSourceAction() );
    buttonPanel.add( _btnViewSource );
    getRootPane().setDefaultButton( _btnViewSource );
    _btnViewSource.setEnabled( false );
  }

  private JComponent createBreakpointsList()
  {
    DefaultTreeModel model = new DefaultTreeModel( makeBreakpointTree() );
    _tree = new JTree( model );
    _tree.setBackground( Scheme.active().getWindow() );
    _tree.setRootVisible( false );
    _tree.setShowsRootHandles( true );
    _tree.setRowHeight( 22 );
    _tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _tree.setVisibleRowCount( 20 );
    _tree.setCellRenderer( new BreakpointCellRenderer( _tree ) );
    JScrollPane scroller = new JScrollPane( _tree );
    scroller.setBorder( BorderFactory.createEmptyBorder() );
    expandAll();

    _tree.addTreeSelectionListener(
      e -> {
        Breakpoint bp = getSelectedBreakpoint();
        if( bp != null )
        {
          _btnViewSource.setEnabled( bp.isLineBreakpoint() );
          updateConfigPanel();
          _configPanel.setVisible( true );
        }
        else
        {
          _btnViewSource.setEnabled( false );
          _configPanel.setVisible( false );
        }
      } );

    _tree.addMouseListener(
      new MouseAdapter()
      {
        public void mouseClicked( MouseEvent e )
        {
          BreakpointTree bpTree = getSelectedValue();
          if( bpTree == null )
          {
            return;
          }
          Breakpoint bp = bpTree.getBreakpoint();
          if( bp == null )
          {
            return;
          }

          BreakpointCellRenderer renderer = (BreakpointCellRenderer)_tree.getCellRenderer();
          TreePath selectionPath = _tree.getSelectionPath();
          renderer.getTreeCellRendererComponent( _tree, bpTree, true, true, true, _tree.getRowForPath( selectionPath ), true );
          renderer.setBounds( _tree.getPathBounds( selectionPath ) );
          renderer.doLayout();
          if( renderer.getCheckbox().contains( e.getX() - renderer.getX(), e.getY() % renderer.getHeight() ) )
          {
            bp.setActive( !bp.isActive() );
            _tree.repaint();

            BreakpointManager bpm = getGosuPanel().getBreakpointManager();
            Breakpoint csr = bpm.getBreakpoint( bp );
            if( csr != null )
            {
              csr.setActive( bp.isActive() );
            }
            EditorHost editor = getGosuPanel().getGosuEditor();
            if( editor != null )
            {
              java.util.List<? extends JComponent> columns = EditorUtilities.findDecendents( editor, EditorScrollPane.AdviceColumn.class );
              for( JComponent column : columns )
              {
                column.repaint();
              }
            }
          }
        }
      } );

    BreakpointManager bpm = getGosuPanel().getBreakpointManager();
    bpm.addChangeListener( _listener = this::updateBreakpoints );

    TabPane tabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    tabPane.addTab( "Breakpoints", null, scroller );

    ToolContainer toolbar = tabPane.getToolContainer();
    addTools( toolbar.getToolBar() );

    return tabPane;
  }

  public void expandAll()
  {
    expandAll( 0, _tree.getRowCount() );
  }

  private void expandAll( int startingIndex, int rowCount )
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

  private BreakpointTree makeBreakpointTree()
  {
    BreakpointTree root = new BreakpointTree();

    BreakpointTree lineBpTree = new BreakpointTree( LineBreakpointFactory.instance(), root );
    root.addChild( lineBpTree );

    _exceptionBpTree = new BreakpointTree( ExceptionBreakpointFactory.instance(), root );
    root.addChild( _exceptionBpTree );

    BreakpointManager bpm = getGosuPanel().getBreakpointManager();
    for( Breakpoint bp : bpm.getLineBreakpoints() )
    {
      lineBpTree.addChild( new BreakpointTree( bp, lineBpTree ) );
    }
    for( Breakpoint bp: bpm.getExceptionBreakpoints() )
    {
      _exceptionBpTree.addChild( new BreakpointTree( bp, _exceptionBpTree ) );
    }
    return root;
  }

  private void updateBreakpointJdi( Breakpoint bp )
  {
    Debugger debugger = getGosuPanel().getDebugger();
    if( debugger != null )
    {
      debugger.removeBreakpointJdi( bp );
      debugger.addBreakpointJdi( bp );
    }
  }

  private void updateBreakpoints()
  {
    Breakpoint bp = getSelectedBreakpoint();

    _tree.setModel( new DefaultTreeModel( makeBreakpointTree() ) );


    BreakpointTree tree = ((BreakpointTree)_tree.getModel().getRoot()).find( bp );
    if( tree != null )
    {
      tree.select( _tree );
    }
  }

  private void updateConfigPanel()
  {
    if( _configPanel.getComponentCount() > 0 )
    {
      _configPanel.remove( 0 );
    }

    Breakpoint bp = getSelectedBreakpoint();
    if( bp == null )
    {
      return;
    }

    JPanel panel = _mapToPanel.get( bp );
    if( panel == null )
    {
      panel = createConfigPanel( bp );
      _mapToPanel.put( bp, panel );
    }
    _configPanel.add( panel, BorderLayout.CENTER );
    _configPanel.validate();
    _configPanel.repaint();
  }

  private Breakpoint getSelectedBreakpoint()
  {
    BreakpointTree bpTree = getSelectedValue();
    return bpTree == null ? null : bpTree.getBreakpoint();
  }

  private BreakpointTree getSelectedValue()
  {
    return _tree.getSelectionPath() == null ? null : (BreakpointTree)_tree.getSelectionPath().getLastPathComponent();
  }

  private void addTools( ToolBar tb )
  {
    int i = 0;
    _btnPlus = makeButton( new AddBreakpointAction() );
    tb.add( _btnPlus, i++ );

    AbstractButton btnMinus = makeButton( new RemoveBreakpointAction() );
    tb.add( btnMinus, i );
  }

  public static BreakpointsDialog getShowing()
  {
    return _current;
  }

  private class AddBreakpointAction extends AbstractAction
  {
    public AddBreakpointAction()
    {
      super( "Add", EditorUtilities.loadIcon( "images/plus.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      GotoExceptionTypePopup.display( _btnPlus, 0, _btnPlus.getHeight(), BreakpointsDialog.this::addExceptionBreakpoint );
    }
  }

  private void addExceptionBreakpoint( String fqn )
  {
    BreakpointManager bpm = getGosuPanel().getBreakpointManager();
    if( bpm.getExceptionBreakpoint( fqn ) == null )
    {
      Breakpoint bp = bpm.addExceptionBreakpoint( fqn );
      _exceptionBpTree.addViaModel( _tree, new BreakpointTree( bp, _exceptionBpTree ) );
    }
    selectBreakpoint( bpm.getExceptionBreakpoint( fqn ) );
  }

  private class RemoveBreakpointAction extends AbstractAction
  {
    public RemoveBreakpointAction()
    {
      super( "Remove", EditorUtilities.loadIcon( "images/minus.png" ) );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( !isEnabled() )
      {
        return;
      }

      TreePath[] indexes = _tree.getSelectionPaths();
      int row = _tree.getRowForPath( indexes[0] );
      TreePath parent = indexes[0].getParentPath();
      for( int i = indexes.length-1; i >= 0; i-- )
      {
        BreakpointTree bpTree = (BreakpointTree)indexes[i].getLastPathComponent();
        Breakpoint bp = bpTree.getBreakpoint();
        if( bp == null )
        {
          continue;
        }
        bpTree.getParent().deleteViaModel( _tree, bpTree );
        BreakpointManager bpm = getGosuPanel().getBreakpointManager();
        bpm.removeBreakpoint( bp );
        _mapToPanel.remove( bp );
        EditorHost editor = getGosuPanel().getCurrentEditor();
        if( editor != null )
        {
          editor.repaint();
        }
      }
      if( row < _tree.getRowCount() )
      {
        _tree.expandPath( parent );
        BreakpointTree bpTree = (BreakpointTree)_tree.getPathForRow( row ).getLastPathComponent();
        if( bpTree.getBreakpoint() != null )
        {
          _tree.setSelectionRow( row );
        }
        else
        {
          _tree.setSelectionRow( row-1 );
        }
      }
      else
      {
        _tree.expandPath( parent );
        _tree.setSelectionRow( row-1 );
      }
      _tree.scrollPathToVisible( getSelectedValue().getPath() );
    }

    @Override
    public boolean isEnabled()
    {
      if( _tree.getSelectionCount() > 0 )
      {
        Breakpoint bp = ((BreakpointTree)_tree.getSelectionPath().getLastPathComponent()).getBreakpoint();
        return bp != null && !bp.isStatic();
      }
      return false;
    }
  }

  private LabToolbarButton makeButton( Action action )
  {
    LabToolbarButton item = new LabToolbarButton( null, null, 2, 0 );
    item.setAction( action );
    return item;
  }

  protected void close()
  {
    dispose();
  }

  private class ViewSourceAction extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      Breakpoint bp = getSelectedBreakpoint();
      if( bp != null )
      {
        String strType = bp.getFqn();
        getGosuPanel().openType( strType, false );
        SettleModalEventQueue.instance().run();
        EditorHost currentEditor = getGosuPanel().getCurrentEditor();
        if( currentEditor != null )
        {
          int iLine = bp.getLine();
          if( iLine >= 1 )
          {
            JTextComponent editor = currentEditor.getEditor();
            Element root = editor.getDocument().getRootElements()[0];
            iLine = root.getElementCount() < iLine ? root.getElementCount() : iLine;
            Element line = root.getElement( iLine - 1 );
            editor.setCaretPosition( line.getStartOffset() );
          }
        }
      }
    }
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  private class DocHandler implements DocumentListener
  {
    private final Breakpoint _bp;
    private final boolean _bExpr;

    private DocHandler( Breakpoint bp, boolean bExpr )
    {
      _bp = bp;
      _bExpr = bExpr;
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
      update( e );
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      update( e );
    }

    @Override
    public void changedUpdate( DocumentEvent e )
    {
      update( e );
    }

    private void update( DocumentEvent e )
    {
      try
      {
        String text = e.getDocument().getText( 0, e.getDocument().getLength() );
        if( _bExpr )
        {
          _bp.setExpression( text );
        }
        else
        {
          _bp.setRunScript( text );
        }
      }
      catch( BadLocationException e1 )
      {
        throw new RuntimeException( e1 );
      }
    }
  }
}