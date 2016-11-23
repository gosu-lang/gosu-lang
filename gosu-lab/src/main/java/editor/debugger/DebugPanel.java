package editor.debugger;

import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.InvalidStackFrameException;
import com.sun.jdi.Location;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VMDisconnectedException;
import editor.AbstractListCellRenderer;
import editor.CommonMenus;
import editor.FileTreeUtil;
import editor.GosuEditor;
import editor.GosuPanel;
import editor.LabFrame;
import editor.Scheme;
import editor.ToggleToolBarButton;
import editor.run.IRunConfig;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.tabpane.ToolContainer;
import editor.util.EditorUtilities;
import editor.util.HTMLEscapeUtil;
import editor.util.IDisposable;
import editor.util.LabToolbarButton;
import editor.util.ToolBar;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 */
public class DebugPanel extends JPanel implements IDisposable
{
  private JComboBox<ThreadReference> _cbThreads;
  private JList<StackFrameRef> _listFrames;
  private JTree _varTree;
  private List<Consumer<Location>> _listeners;
  private Debugger _debugger;

  public DebugPanel( Debugger debugger )
  {
    _debugger = debugger;
    debugger.addChangeListener( e -> EventQueue.invokeLater( () -> debuggerChanged( debugger ) ) );
    setBorder( null );
    setLayout( new BorderLayout() );

    _listeners = new ArrayList<>();

    CollapsibleSplitPane splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, makeThreadsPanel(), makeVariablesAndWatchesPanel() );
    add( splitPane, BorderLayout.CENTER );

    add( makeRunToolbar(), BorderLayout.WEST );

    splitPane.setPosition( 30 );
  }

  @Override
  public void dispose()
  {
    getGosuPanel().killProcess();
  }

  private JComponent makeThreadsPanel()
  {
    JPanel panel = new JPanel( new BorderLayout() );

    _cbThreads = new JComboBox<>();
    _cbThreads.setBorder( BorderFactory.createMatteBorder( 1, 1, 1, 1, Scheme.active().getControlShadow() ) );
    _cbThreads.setRenderer( new ThreadCellRenderer( _cbThreads.getRenderer() ) );
    _cbThreads.addActionListener( action -> threadChanged() );
    _cbThreads.setFocusable( false );
    panel.add( _cbThreads, BorderLayout.NORTH );

    DefaultListModel<StackFrameRef> model = new DefaultListModel<>();
    _listFrames = new JList<>( model );
    _listFrames.setBackground( Scheme.active().getWindow() );
    _listFrames.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
    _listFrames.setFixedCellHeight( 22 );
    _listFrames.setCellRenderer( new StackFrameCellRenderer() );
    _listFrames.addListSelectionListener( e -> updateVars() );
    JScrollPane scroller = new JScrollPane( _listFrames );
    scroller.setBorder( null );
    panel.add( scroller, BorderLayout.CENTER );

    TabPane tabPane = new TabPane( TabPosition.TOP, TabPane.MINIMIZABLE | TabPane.RESTORABLE );
    tabPane.addTab( "Threads", EditorUtilities.loadIcon( "images/thread.png" ), panel );
    return tabPane;
  }

  private JComponent makeRunToolbar()
  {
    JPanel toolbarPanel = new JPanel( new BorderLayout() );
    toolbarPanel.setBackground( Scheme.active().getMenu() );
    toolbarPanel.setBorder( BorderFactory.createEmptyBorder( 1, 2, 1, 2 ) );

    ToolBar toolbar = new ToolBar( JToolBar.VERTICAL );

    LabToolbarButton item;
    item = new LabToolbarButton( new CommonMenus.ClearAndRunActionHandler( () -> getGosuPanel().getRunConfig() ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getGosuPanel().getRunConfig();
      return rc == null ? "Run..." : "Run '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    item = new LabToolbarButton( new CommonMenus.ClearAndDebugActionHandler( () -> getGosuPanel().getRunConfig() ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getGosuPanel().getRunConfig();
      return rc == null ? "Debug..." : "Debug '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    item = new LabToolbarButton( new CommonMenus.StopActionHandler( this::getGosuPanel ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.PauseActionHandler( this::getDebugger ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.ResumeActionHandler( this::getDebugger ) );
    toolbar.add( item );

    toolbar.addSeparator();

    item = new LabToolbarButton( new CommonMenus.ViewBreakpointsActionHandler( () -> null ) );
    toolbar.add( item );
    ToggleToolBarButton titem = new ToggleToolBarButton( new CommonMenus.MuteBreakpointsActionHandler( this::getBreakpointManager ) );
    toolbar.add( titem );

    toolbarPanel.add( toolbar, BorderLayout.CENTER );
    return toolbarPanel;
  }

  public Debugger getDebugger()
  {
    return _debugger;
  }

  public void addLocationListener( Consumer<Location> listener )
  {
    _listeners.add( listener );
  }
  @SuppressWarnings("UnusedDeclaration")
  public void removeLocationListener( Consumer<Location> listener )
  {
    _listeners.remove( listener );
  }
  private void fireLocationChange( Location loc )
  {
    for( Consumer<Location> listener: _listeners )
    {
      listener.accept( loc );
    }
  }

  private void debuggerChanged( Debugger debugger )
  {
    if( debugger.isSuspended() || debugger.isPaused() )
    {
      suspended( debugger.getThreads(), debugger.getSuspendedThread() );
    }
    else
    {
      resumed();
    }
  }

  private void suspended( List<ThreadReference> threads, ThreadReference thread )
  {
    updateThreads( threads, thread );
  }

  private void updateThreads( List<ThreadReference> threads, ThreadReference thread )
  {
    _cbThreads.setModel( makeThreadModel( threads ) );
    if( thread != null )
    {
      _cbThreads.setSelectedItem( thread );
    }
    else if( threads != null && threads.size() > 0 )
    {
      _cbThreads.setSelectedIndex( 0 );
    }
    else
    {
      _cbThreads.setSelectedItem( null );
    }
  }

  private void resumed()
  {
    updateThreads( new ArrayList<>(), null );
    threadChanged();
  }


  private void updateVars()
  {
    StackFrameRef frame = _listFrames.getSelectedValue();

    DefaultTreeModel model = new DefaultTreeModel( new VarTree( frame ) );
    _varTree.setModel( model );

    if( frame ==  null )
    {
      return;
    }

    fireLocationChange( frame.getRef().location() );
  }

  private void threadChanged()
  {
    ThreadReference thread = (ThreadReference)_cbThreads.getSelectedItem();
    try
    {
      if( thread != null )
      {
        _listFrames.setModel( makeThreadsModel( thread.frames() ) );
        _listFrames.setSelectedIndex( 0 );
      }
      else
      {
        _listFrames.setModel( makeThreadsModel( Collections.emptyList() ) );
      }
    }
    catch( Exception e )
    {
      // eat
    }
  }

  private ListModel<StackFrameRef> makeThreadsModel( List<StackFrame> frames )
  {
    DefaultListModel<StackFrameRef> model = new DefaultListModel<>();
    final int[] i = {0};
    frames.forEach( e -> model.addElement( new StackFrameRef( i[0]++ ) ) );
    return model;
  }

  private ComboBoxModel<ThreadReference> makeThreadModel( List<ThreadReference> threads )
  {
    return new DefaultComboBoxModel<>( threads.toArray( new ThreadReference[threads.size()] ) );
  }

  private JComponent makeVariablesAndWatchesPanel()
  {
    CollapsibleSplitPane varPanel = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, makeFramePane(), makeWatchesPane() );
    varPanel.setPosition( 50 );
    return varPanel;
  }

  private TabPane makeWatchesPane()
  {
    TabPane watchesTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    watchesTabPane.addTab( "Watches", null, new JPanel() );
    return watchesTabPane;
  }

  private TabPane makeFramePane()
  {
    _varTree = new JTree( new DefaultTreeModel( new VarTree( null ) ) );
    _varTree.setBorder( null );
    _varTree.setBackground( Scheme.active().getWindow() );
    _varTree.setRootVisible( false );
    _varTree.setShowsRootHandles( true );
    _varTree.setRowHeight( 22 );
    _varTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    _varTree.setVisibleRowCount( 20 );
    _varTree.setCellRenderer( new VarTreeCellRenderer( _varTree ) );
    JScrollPane scroller = new JScrollPane( _varTree );
    scroller.setBorder( null );

    TabPane varTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    varTabPane.addTab( "Frame", EditorUtilities.loadIcon( "images/single_frame.png" ), scroller );
    ToolContainer toolbar = varTabPane.getToolContainer();
    addTools( toolbar.getToolBar() );
    return varTabPane;
  }

  private void addTools( ToolBar tb )
  {
    int i = 0;

    LabToolbarButton item;

    item = makeButton( new CommonMenus.EvaluateExpressionActionHandler( this::getDebugger ) );
    tb.add( item, i++ );
    item = makeButton( new CommonMenus.ShowExecPointActionHandler( this::getDebugger ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.StepOverActionHandler( this::getDebugger ) );
    tb.add( item, i++ );
    item = makeButton( new CommonMenus.StepIntoActionHandler( this::getDebugger ) );
    tb.add( item, i++ );
    item = makeButton( new CommonMenus.StepOutActionHandler( this::getDebugger ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.DropFrameActionHandler( this::getDebugger, this::getDropToFrame ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.RunToCursorActionHandler( this::getDebugger, this::getBreakpointManager, this::getCurrentGosuEditor ) );
    tb.add( item, i );
  }

  private LabToolbarButton makeButton( Action action )
  {
    LabToolbarButton item = new LabToolbarButton( null, null, 2, 0 );
    item.setAction( action );
    return item;
  }

  private JSeparator makeSeparator()
  {
    JSeparator separator = new JSeparator( SwingConstants.VERTICAL );
    separator.setMaximumSize( new Dimension( 4, 20 ) );
    return separator;
  }
  private GosuEditor getCurrentGosuEditor()
  {
    return getGosuPanel().getCurrentGosuEditor();
  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  private BreakpointManager getBreakpointManager()
  {
    return getGosuPanel().getBreakpointManager();
  }

  public StackFrame getDropToFrame()
  {
    StackFrameRef ref = _listFrames.getSelectedValue();
    StackFrame frame = ref.getRef();
    if( isFilteredClass( frame.location().declaringType() ) )
    {
      // don't support dropping too far outside of filtered classes, otherwise
      // we get InternalExceptions from jdwp
      frame = null;
    }
    return frame;
  }

  public ThreadReference getSelectedThread()
  {
    return (ThreadReference)_cbThreads.getSelectedItem();
  }

  private class StackFrameCellRenderer extends AbstractListCellRenderer<StackFrameRef>
  {
    private StackFrameCellRenderer()
    {
      super( _listFrames );
    }

    @Override
    public void configure()
    {
      setBorder( new EmptyBorder( 0, 2, 0, 0 ) );
      StackFrame frame = getNode().getRef();
      if( frame != null )
      {
        Location loc;
        try
        {
          loc = frame.location();
        }
        catch( InvalidStackFrameException e )
        {
          return;
        }

        String fqn = loc.declaringType().name();
        String className = fqn;
        String pkg = "";
        int iDot = fqn.lastIndexOf( '.' );
        if( iDot > 0 )
        {
          pkg = fqn.substring( 0, iDot );
          className = fqn.substring( iDot+1 );
        }
        Color textColor = _listFrames.getForeground();
        String hex;
        if( !isFilteredClass( frame.location().declaringType() ) )
        {
          hex = String.format( "#%02x%02x%02x", textColor.getRed(), textColor.getGreen(), textColor.getBlue() );
        }
        else
        {
          // gray out if we don't have the corresponding source
          hex = "#7F8C8D";
        }
        String text = "<html><font color= " + hex + ">" + HTMLEscapeUtil.escape( loc.method().name() ) + "():" + loc.lineNumber() + ", " + className + "<i>(" + pkg + ")</i></font>";
        setText( text );
        setIcon( EditorUtilities.loadIcon( "images/single_frame.png" ) );
      }
    }
  }

  private boolean isFilteredClass( ReferenceType referenceType )
  {
    String fqn = Debugger.getOutermostType( referenceType );
    return FileTreeUtil.find( fqn ) == null;
  }

  private class ThreadCellRenderer implements ListCellRenderer<ThreadReference>
  {
    private ListCellRenderer<ThreadReference> _delegate;

    private ThreadCellRenderer( ListCellRenderer delegate )
    {
      //noinspection unchecked
      _delegate = delegate;
    }


    @Override
    public Component getListCellRendererComponent( JList<? extends ThreadReference> list, ThreadReference thread, int index, boolean isSelected, boolean cellHasFocus )
    {
      JLabel cell = (JLabel)_delegate.getListCellRendererComponent( list, null, index, isSelected, cellHasFocus );
      try
      {
        if( thread != null && thread.isSuspended() )
        {
          String text = "<html><b>" + thread.name() + "</b> @" + thread.uniqueID() + " group: <b>" + thread.threadGroup().name() + "</b> - <i>" + Thread.State.values()[thread.status()].name() + "</i>";
          cell.setText( text );
          cell.setIcon( EditorUtilities.loadIcon( "images/thread.png" ) );
        }
      }
      catch( VMDisconnectedException e )
      {
        // eat
      }
      return cell;
    }
  }

  //!! using a reference object instead of direct StackFrame because the debugger can indirectly invalidate a StackFrame when it invokes a method remotely on a suspended.
  //!! This happens because all threads are resumed when code executes in the remote VM i.e., a thread resumed after a StackFrame is stored renders the StackFrame unusable,
  //!! so we have to get the latest one.
  public class StackFrameRef
  {
    private final int _index;

    StackFrameRef( int index )
    {
      _index = index;
    }
    StackFrame getRef()
    {
      if( !((ThreadReference)_cbThreads.getSelectedItem()).isSuspended() )
      {
        return null;
      }
      try
      {
        return ((ThreadReference)_cbThreads.getSelectedItem()).frame( _index );
      }
      catch( IncompatibleThreadStateException e )
      {
        throw new RuntimeException( e );
      }
    }
  }
}
