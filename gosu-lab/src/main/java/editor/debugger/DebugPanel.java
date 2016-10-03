package editor.debugger;

import com.sun.jdi.ArrayReference;
import com.sun.jdi.InvalidStackFrameException;
import com.sun.jdi.Location;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.PrimitiveValue;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.StackFrame;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.Value;
import editor.AbstractListCellRenderer;
import editor.AbstractTreeCellRenderer;
import editor.CommonMenus;
import editor.FileTreeUtil;
import editor.GosuEditor;
import editor.RunMe;
import editor.Scheme;
import editor.VarTree;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.tabpane.ToolContainer;
import editor.util.EditorUtilities;
import editor.util.HTMLEscapeUtil;
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
public class DebugPanel extends JPanel
{
  private JComboBox<ThreadReference> _cbThreads;
  private JList<StackFrame> _listFrames;
  private CollapsibleSplitPane _splitPane;
  private JTree _varTree;
  private List<Consumer<Location>> _listeners;
  private Debugger _debugger;

  public DebugPanel( Debugger debugger )
  {
    _debugger = debugger;
    debugger.addChangeListener( this::debuggerChanged );
    setBorder( null );
    setLayout( new BorderLayout() );

    _listeners = new ArrayList<>();

    _splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, makeThreadsPanel(), makeVariablesAndWatchesPanel() );
    add( _splitPane, BorderLayout.CENTER );
    _splitPane.setPosition( 30 );
  }

  private JComponent makeThreadsPanel()
  {
    JPanel panel = new JPanel( new BorderLayout() );

    _cbThreads = new JComboBox<>();
    _cbThreads.setBackground( Scheme.active().getControl() );
    _cbThreads.setBorder( BorderFactory.createMatteBorder( 1, 1, 1, 1, Scheme.active().getControlShadow() ) );
    _cbThreads.setRenderer( new ThreadCellRenderer( _cbThreads.getRenderer() ) );
    _cbThreads.addActionListener( action -> threadChanged() );
    _cbThreads.setFocusable( false );
    panel.add( _cbThreads, BorderLayout.NORTH );

    DefaultListModel<StackFrame> model = new DefaultListModel<>();
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

  public Debugger getDebugger()
  {
    return _debugger;
  }

  public void addLocationListener( Consumer<Location> listener )
  {
    _listeners.add( listener );
  }
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
    _cbThreads.setSelectedItem( thread );
  }

  private void resumed()
  {
    updateThreads( new ArrayList<>(), null );
    threadChanged();
  }


  private void updateVars()
  {
    StackFrame frame = _listFrames.getSelectedValue();

    DefaultTreeModel model = new DefaultTreeModel( new VarTree( frame ) );
    _varTree.setModel( model );

    if( frame ==  null )
    {
      return;
    }

    fireLocationChange( frame.location() );
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

  private ListModel<StackFrame> makeThreadsModel( List<StackFrame> frames )
  {
    DefaultListModel<StackFrame> model = new DefaultListModel<>();
    frames.forEach( model::addElement );
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

    LabToolbarButton item = makeButton( new CommonMenus.ShowExecPointActionHandler( this::getDebugger ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.StepOverActionHandler( this::getDebugger ) );
    tb.add( item, i++ );
    item = makeButton( new CommonMenus.StepIntoActionHandler( this::getDebugger ) );
    tb.add( item, i++ );
    item = makeButton( new CommonMenus.StepOutActionHandler( this::getDebugger ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.DropFrameActionHandler( this::getDebugger, () -> getDropToFrame() ) );
    tb.add( item, i++ );

    tb.add( makeSeparator(), i++ );

    item = makeButton( new CommonMenus.RunToCursorActionHandler( this::getDebugger, this::getBreakpointManager, this::getCurrentEditor ) );
    tb.add( item, i++ );
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
  private GosuEditor getCurrentEditor()
  {
    return RunMe.getEditorFrame().getGosuPanel().getCurrentEditor();
  }

  private BreakpointManager getBreakpointManager()
  {
    return RunMe.getEditorFrame().getGosuPanel().getBreakpointManager();
  }

  public StackFrame getDropToFrame()
  {
    StackFrame frame = _listFrames.getSelectedValue();
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

  private class StackFrameCellRenderer extends AbstractListCellRenderer<StackFrame>
  {
    private StackFrameCellRenderer()
    {
      super( _listFrames );
    }

    @Override
    public void configure()
    {
      setBorder( new EmptyBorder( 0, 2, 0, 0 ) );
      StackFrame frame = getNode();
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
      JLabel cell = (JLabel)_delegate.getListCellRendererComponent( list, thread, index, isSelected, cellHasFocus );
      if( thread != null )
      {
        String text = "<html><b>" + thread.name() + "</b> @" + thread.uniqueID() + " group: <b>" + thread.threadGroup().name() + "</b> - <i>" + Thread.State.values()[thread.status()].name() + "</i>";
        cell.setText( text );
        cell.setIcon( EditorUtilities.loadIcon( "images/thread.png" ) );
      }
      return cell;
    }
  }

  static class VarTreeCellRenderer extends AbstractTreeCellRenderer<VarTree>
  {
    public VarTreeCellRenderer( JTree tree )
    {
      super( tree );
    }

    public void configure()
    {
      VarTree node = getNode();
      if( node == null )
      {
        return;
      }

      setBorder( new EmptyBorder( 0, 3, 0, 3 ) );

      Value value = node.getValue();
      String strValue;
      String address;
      String valueType = value == null ? "" : value.type().name();
      if( value instanceof PrimitiveValue )
      {
        address = "";
        strValue = value.toString();
        if( valueType.equals( char.class.getName() ) )
        {
          strValue = "<font face=monospaced color=#008000>'" + strValue + "'</font> <font color=#000000>" + (int)strValue.charAt( 0 ) + "</font>";
        }
      }
      else if( value instanceof ArrayReference )
      {
        address = "";
        strValue = "["+ ((ArrayReference)value).length() + "] " + makeIdValue( value );
      }
      else if( value == null )
      {
        address = "";
        strValue = "<font color=#000080><b>null</b></font>";
      }
      else
      {
        String idValue = makeIdValue( value );
        address = null;
        strValue = value.toString();
        if( strValue.startsWith( "instance of" ) )
        {
          strValue = idValue;
          address = "";
        }

        if( address == null )
        {
          address = "<font color=#C0C0C0>" + idValue + "</font>";
        }

        if( valueType.equals( String.class.getName() ) )
        {
          strValue = "<font color=#008000><b>" + strValue + "</b></font>";
        }
      }
      setText( "<html><font color=#800000>" + node.getName() + "</font> " + address + " = " + strValue );
      setIcon( node.getIcon() );
    }

    private String makeIdValue( Value value )
    {
      return "{" + value.type().name() + "@" + ((ObjectReference)value).uniqueID() + "}";
    }
  }

}
