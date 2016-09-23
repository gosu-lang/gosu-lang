package editor;

import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import editor.debugger.BreakpointManager;
import editor.debugger.Debugger;
import editor.search.StandardLocalSearch;
import editor.search.StudioUtilities;
import editor.shipit.BuildIt;
import editor.shipit.ShipIt;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.undo.AtomicUndoManager;
import editor.util.BrowserUtil;
import editor.util.EditorUtilities;
import editor.util.Experiment;
import editor.util.GosuTextifier;
import editor.util.LabelListPopup;
import editor.util.PlatformUtil;
import editor.util.SettleModalEventQueue;
import editor.util.SmartMenu;
import editor.util.ToolBar;
import editor.util.TypeNameUtil;
import editor.util.XPToolbarButton;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.util.TraceClassVisitor;
import gw.lang.Gosu;
import gw.lang.ir.SignatureUtil;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.StreamUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.tree.TreeModel;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 */
public class GosuPanel extends JPanel
{
  private static final int MAX_TABS = 12;

  public static final String FAILED = "   FAILED: ";
  public static final String SUCCESS = "   SUCCESS ";

  private SystemPanel _consolePanel;
  private CollapsibleSplitPane _outerSplitPane;
  private CollapsibleSplitPane _splitPane;
  private ExperimentView _experimentView;
  private JFrame _parentFrame;
  private RunState _runState;
  private TabPane _editorTabPane;
  private TabPane _bottomTabPane;
  private AtomicUndoManager _defaultUndoMgr;
  private NavigationHistory _history;
  private JLabel _status;
  private JPanel _statPanel;
  private boolean _initialFile;
  private TypeNameCache _typeNamesCache;
  private Experiment _experiment;
  private OutputStreamWriter _inWriter;
  private SysInListener _sysInListener;
  private InputStream _oldIn;
  private MessagesPanel _messages;
  private DebugPanel _debugPanel;
  private IProcessRunner _processRunner;
  private BreakpointManager _breakpointManager;
  private Debugger _debugger;

  public GosuPanel( JFrame basicGosuEditor )
  {
    _parentFrame = basicGosuEditor;
    _defaultUndoMgr = new AtomicUndoManager( 10 );
    _typeNamesCache = new TypeNameCache();
    _runState = RunState.None;
    _breakpointManager = new BreakpointManager();
    configUI();
  }

  public NavigationHistory getTabSelectionHistory()
  {
    return _history;
  }

  void configUI()
  {
    setLayout( new BorderLayout() );

    JPanel bottom = new JPanel( new BorderLayout() );
    _bottomTabPane = new TabPane( TabPane.MINIMIZABLE | TabPane.RESTORABLE | TabPane.TOP_BORDER_ONLY );
    bottom.add( _bottomTabPane, BorderLayout.CENTER );
    bottom.add( createToolbar(), BorderLayout.WEST );

    _messages = new MessagesPanel();

    _consolePanel = new SystemPanel();
    _bottomTabPane.addTab( "Console", EditorUtilities.loadIcon( "images/console.png" ), _consolePanel );

    _editorTabPane = new TabPane( TabPosition.TOP, TabPane.DYNAMIC | TabPane.MIN_MAX_REST );

    _history = new NavigationHistory( _editorTabPane );
    getTabSelectionHistory().setTabHistoryHandler( new EditorTabHistoryHandler() );

    _editorTabPane.addSelectionListener(
      e -> {
        if( !_editorTabPane.isVisible() )
        {
          // clearing tabs, don't save etc.
          return;
        }
        savePreviousTab();
        updateTitle();
        if( getCurrentEditor() == null )
        {
          return;
        }
        // Don't set focus here, otherwise it could be stealing it from somewhere that wants it after it instructed Lab to display an editor,
        // but wants to retain focus, like the debug panel when switching between stakc frames.
        //getCurrentEditor().getEditor().requestFocus();
        parse();
        storeExperimentState();
      } );

    _experimentView = new ExperimentView();
    _experimentView.setBackground( Color.white );
    TabPane experimentViewTabPane = new TabPane( TabPosition.TOP, TabPane.MINIMIZABLE | TabPane.RESTORABLE | TabPane.TOP_BORDER_ONLY );
    experimentViewTabPane.addTab( "Experiment", null, _experimentView );


    _splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, experimentViewTabPane, _editorTabPane );
    _outerSplitPane = new CollapsibleSplitPane( SwingConstants.VERTICAL, _splitPane, bottom );

    add( _outerSplitPane, BorderLayout.CENTER );

    JPanel statPanel = makeStatusBar();
    add( statPanel, BorderLayout.SOUTH );

    JMenuBar menuBar = makeMenuBar();
    _parentFrame.setJMenuBar( menuBar );
    handleMacStuff();

    EventQueue.invokeLater( () -> {
      setExperimentSplitPosition( 70 );
      setEditorSplitPosition( 20 );
    } );

    EventQueue.invokeLater( this::mapKeystrokes );
  }

  private ToolBar createToolbar()
  {
    ToolBar toolbar = new ToolBar( JToolBar.VERTICAL );
    XPToolbarButton item;

    item = new XPToolbarButton( new CommonMenus.ClearAndRunActionHandler( "", this::getCurrentEditorType ) );
    toolbar.add( item );
    item = new XPToolbarButton( new CommonMenus.ClearAndDebugActionHandler( "", this::getCurrentEditorType ) );
    toolbar.add( item );
    item = new XPToolbarButton( new CommonMenus.StopActionHandler( "", () -> this ) );
    toolbar.add( item );
    item = new XPToolbarButton( new CommonMenus.PauseActionHandler( "", this::getDebugger ) );
    toolbar.add( item );
    item = new XPToolbarButton( new CommonMenus.ResumeActionHandler( "", this::getDebugger ) );
    toolbar.add( item );

    toolbar.addSeparator();

    item = new XPToolbarButton( new CommonMenus.ViewBreakpointsActionHandler( "", () -> null ) );
    toolbar.add( item );
    ToggleToolBarButton titem = new ToggleToolBarButton( new CommonMenus.MuteBreakpointsActionHandler( "", this::getBreakpointManager ) );
    toolbar.add( titem );

    return toolbar;
  }

  public ExperimentView getExperimentView()
  {
    return _experimentView;
  }

  public MessagesPanel getMessagesPanel()
  {
    return _messages;
  }

  public SystemPanel getConsolePanel()
  {
    return _consolePanel;
  }

  public DebugPanel getDebugPanel()
  {
    return _debugPanel;
  }

  public void showMessages( boolean bShow )
  {
    if( bShow )
    {
      ITab tab = _bottomTabPane.findTabWithContent( _messages );
      if( tab == null )
      {
        _bottomTabPane.addTab( "Messages", null, _messages );
      }
      else
      {
        _bottomTabPane.selectTab( tab, false );
      }
    }
    else
    {
      _messages.clear();
      _bottomTabPane.removeTabWithContent( _messages );
    }
  }

  public void showConsole()
  {
    _bottomTabPane.selectTabWithContent( _consolePanel, false );
  }

  private void handleMacStuff()
  {
    if( PlatformUtil.isMac() )
    {
      System.setProperty( "apple.laf.useScreenMenuBar", "true" );
      System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "Gosu Editor" );
    }
  }

  public void clearTabs()
  {
    _editorTabPane.setVisible( false );
    try
    {
      _editorTabPane.removeAllTabs();
    }
    finally
    {
      _editorTabPane.setVisible( true );
    }
    showMessages( false );
    SettleModalEventQueue.instance().run();
    getTabSelectionHistory().dispose();
  }

  private void storeExperimentState()
  {
    if( _initialFile )
    {
      return;
    }
    getExperiment().save( _editorTabPane );
    EditorUtilities.saveLayoutState( _experiment );
  }

  Experiment getExperiment()
  {
    return _experiment;
  }

  public void restoreExperimentState( Experiment experiment )
  {
    _experiment = experiment;

    if( experiment.getSourcePath().size() > 0 )
    {
      //Gosu.setClasspath( experiment.getSourcePath().stream().map( File::new ).collect( Collectors.toList() ) );
      RunMe.reinitializeGosu( experiment );
    }

    //TypeSystem.refresh( TypeSystem.getGlobalModule() );

    RunMe.getEditorFrame().addExperiment( experiment );

    for( String openFile : experiment.getOpenFiles() )
    {
      File file = new File( openFile );
      if( file.isFile() )
      {
        openFile( file, false );
      }
    }
    String activeFile = experiment.getActiveFile();
    if( activeFile == null )
    {
      openFile( experiment.getOrMakeUntitledProgram(), true );
    }
    else
    {
      openTab( new File( activeFile ), true );
    }
    SettleModalEventQueue.instance().run();
    _experimentView.load( _experiment );
    EventQueue.invokeLater( () -> {
      parse();
      GosuEditor currentEditor = getCurrentEditor();
      if( currentEditor != null )
      {
        currentEditor.getEditor().requestFocus();
      }
    } );
  }

  private JPanel makeStatusBar()
  {
    _statPanel = new JPanel( new BorderLayout() );
    _status = new JLabel();
    _statPanel.add( _status, BorderLayout.CENTER );
    _statPanel.setVisible( false );
    return _statPanel;
  }

  public void setStatus( String status )
  {
    _status.setText( status );
    _statPanel.setVisible( status != null && !status.isEmpty() );
    _status.revalidate();
  }

  private void parse()
  {
    EventQueue.invokeLater( () -> {
      if( getCurrentEditor() != null )
      {
        getCurrentEditor().parse();
      }
    } );
  }

  private void savePreviousTab()
  {
    GosuEditor editor = getTabSelectionHistory().getPreviousEditor();
    if( editor != null )
    {
      if( isDirty( editor ) )
      {
        save( (File)editor.getClientProperty( "_file" ), editor );
      }
      else
      {
        if( editor.getParsedClass() != null )
        {
          // Refresh the class so that it SourceFileHandle will have a non-null file,
          // otherwise the editor's transient string one will be there -- there is code
          // around that presumes all GosuClasses in tabs are also on disk
          TypeSystem.refresh( (ITypeRef)editor.getParsedClass() );
        }
      }
    }
  }

  private GosuEditor createEditor()
  {
    final GosuEditor editor = new GosuEditor( new GosuClassLineInfoManager(),
                                              new AtomicUndoManager( 10000 ),
                                              ScriptabilityModifiers.SCRIPTABLE,
                                              new DefaultContextMenuHandler(),
                                              false, true );
    editor.setBorder( BorderFactory.createEmptyBorder() );
    addDirtyListener( editor );
    EventQueue.invokeLater( () -> ((AbstractDocument)editor.getEditor().getDocument()).setDocumentFilter( new GosuPanelDocumentFilter( editor ) ) );
    return editor;
  }

  private void addDirtyListener( final GosuEditor editor )
  {
    editor.getUndoManager().addChangeListener(
      new ChangeListener()
      {
        private ChangeEvent _lastChangeEvent;

        @Override
        public void stateChanged( ChangeEvent e )
        {
          if( e != _lastChangeEvent )
          {
            _lastChangeEvent = e;
            setDirty( editor, true );
          }
        }
      } );

  }

  private GosuEditor initEditorMode( File file, GosuEditor editor )
  {
    if( file != null && file.getName() != null )
    {
      if( file.getName().endsWith( ".gsx" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( false );
        editor.setClass( false );
        editor.setEnhancement( true );
      }
      else if( file.getName().endsWith( ".gs" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( false );
        editor.setClass( true );
        editor.setEnhancement( false );
      }
      else if( file.getName().endsWith( ".gst" ) )
      {
        editor.setProgram( false );
        editor.setTemplate( true );
        editor.setClass( false );
        editor.setEnhancement( false );
      }
      else
      {
        editor.setProgram( true );
        editor.setTemplate( false );
        editor.setClass( false );
        editor.setEnhancement( false );
      }
    }
    return editor;
  }

  private JMenuBar makeMenuBar()
  {
    JMenuBar menuBar = new JMenuBar();

    makeFileMenu( menuBar );
    makeEditMenu( menuBar );
    makeSearchMenu( menuBar );
    makeCodeMenu( menuBar );
    makeBuildMenu( menuBar );
    makeRunMenu( menuBar );
    makeWindowMenu( menuBar );
    makeHelpMenu( menuBar );

    return menuBar;
  }

  private void makeHelpMenu( JMenuBar menuBar )
  {
    JMenu helpMenu = new SmartMenu( "Help" );
    helpMenu.setMnemonic( 'H' );
    menuBar.add( helpMenu );

    JMenuItem gosuItem = new JMenuItem(
      new AbstractAction( "Gosu Online" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org" );
        }
      } );
    gosuItem.setMnemonic( 'G' );
    helpMenu.add( gosuItem );

    helpMenu.addSeparator();

    JMenuItem helpItem = new JMenuItem(
      new AbstractAction( "The Basics" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.github.io/docs.html" );
        }
      } );
    helpItem.setMnemonic( 'B' );
    helpMenu.add( helpItem );

    helpMenu.addSeparator();

    JMenuItem playItem = new JMenuItem(
      new AbstractAction( "Web Editor" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.github.io/play.html" );
        }
      } );
    playItem.setMnemonic( 'W' );
    helpMenu.add( playItem );

    helpMenu.addSeparator();

    JMenuItem discussItem = new JMenuItem(
      new AbstractAction( "Discuss" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://groups.google.com/group/gosu-lang" );
        }
      } );
    discussItem.setMnemonic( 'D' );
    helpMenu.add( discussItem );

    helpMenu.addSeparator();

    JMenuItem plugin = new JMenuItem(
      new AbstractAction( "IntelliJ Plugin" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.github.io/intellij.html" );
        }
      } );
    plugin.setMnemonic( 'I' );
    helpMenu.add( plugin );
  }

  private void makeWindowMenu( JMenuBar menuBar )
  {
    JMenu windowMenu = new SmartMenu( "Window" );
    windowMenu.setMnemonic( 'W' );
    menuBar.add( windowMenu );

    JMenuItem previousItem = new JMenuItem(
      new AbstractAction( "Previous Editor" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          goBackward();
        }

//        public boolean isEnabled()
//        {
//          return canGoBackward();
//        }
      } );
    previousItem.setMnemonic( 'P' );
    previousItem.setAccelerator( KeyStroke.getKeyStroke( "alt LEFT" ) );
    windowMenu.add( previousItem );

    JMenuItem nextItem = new JMenuItem(
      new AbstractAction( "Next Editor" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          goForward();
        }

//        public boolean isEnabled()
//        {
//          return canGoForward();
//        }
      } );
    nextItem.setMnemonic( 'N' );
    nextItem.setAccelerator( KeyStroke.getKeyStroke( "alt RIGHT" ) );
    windowMenu.add( nextItem );


    windowMenu.addSeparator();


    JMenuItem recentItem = new JMenuItem(
      new AbstractAction( "Recent Editors" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          displayRecentViewsPopup();
        }
      } );
    recentItem.setMnemonic( 'R' );
    recentItem.setAccelerator( KeyStroke.getKeyStroke( "control E" ) );
    windowMenu.add( recentItem );


    windowMenu.addSeparator();


    JMenuItem closeActiveItem = new JMenuItem(
      new AbstractAction( "Close Active Editor" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          saveIfDirty();
          closeActiveEditor();
        }
      } );
    closeActiveItem.setMnemonic( 'C' );
    closeActiveItem.setAccelerator( KeyStroke.getKeyStroke( "control F4" ) );
    windowMenu.add( closeActiveItem );

    JMenuItem closeOthersItem = new JMenuItem(
      new AbstractAction( "Close Others" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          closeOthers();
        }
      } );
    closeOthersItem.setMnemonic( 'O' );
    windowMenu.add( closeOthersItem );
  }

  private void makeCodeMenu( JMenuBar menuBar )
  {
    JMenu codeMenu = new SmartMenu( "Code" );
    codeMenu.setMnemonic( 'd' );
    menuBar.add( codeMenu );
    codeMenu.add( CommonMenus.makeCodeComplete( this::getCurrentEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeParameterInfo( this::getCurrentEditor ) );
    codeMenu.add( CommonMenus.makeExpressionType( this::getCurrentEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeGotoDeclaration( this::getCurrentEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeShowFileInTree( this::getCurrentEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeQuickDocumentation( this::getCurrentEditor ) );

    codeMenu.addSeparator();

    JMenuItem openTypeItem = new JMenuItem(
      new AbstractAction( "Open Type..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          GotoTypePopup.display();
        }
      } );
    openTypeItem.setMnemonic( 'O' );
    openTypeItem.setAccelerator( KeyStroke.getKeyStroke( "control N" ) );
    codeMenu.add( openTypeItem );

    if( "true".equals( System.getProperty( "spec" ) ) )
    {
      codeMenu.addSeparator();
      JMenuItem markItem = new JMenuItem(
        new AbstractAction( "Mark Errors For Gosu Language Test" )
        {
          @Override
          public void actionPerformed( ActionEvent e )
          {
            markErrorsForGosuLanguageTest();
          }
        } );
      markItem.setMnemonic( 'M' );
      markItem.setAccelerator( KeyStroke.getKeyStroke( "control M" ) );
      codeMenu.add( markItem );
    }

    codeMenu.addSeparator();
    JMenuItem viewBytecodeItem = new JMenuItem(
      new AbstractAction( "View Bytecode" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          dumpBytecode();
        }

        @Override
        public boolean isEnabled()
        {
          return getCurrentEditor() != null && getCurrentEditor().getScriptPart() != null &&
                 getCurrentEditor().getScriptPart().getContainingType() != null;
        }
      } );
    codeMenu.add( viewBytecodeItem );
  }

  private void makeBuildMenu( JMenuBar menuBar )
  {
    JMenu buildMenu = new SmartMenu( "Build" );
    buildMenu.setMnemonic( 'b' );
    menuBar.add( buildMenu );

    JMenuItem compileMenu = new JMenuItem(
      new AbstractAction( "Compile" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          compile();
        }
      } );
    compileMenu.setMnemonic( 'c' );
    compileMenu.setAccelerator( KeyStroke.getKeyStroke( "control F9" ) );
    buildMenu.add( compileMenu );


    buildMenu.addSeparator();


    JMenuItem shipIt = new JMenuItem(
      new AbstractAction( "Ship It! ..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          shipIt();
        }
      } );
    shipIt.setMnemonic( 'p' );
    shipIt.setAccelerator( KeyStroke.getKeyStroke( "control F10" ) );
    buildMenu.add( shipIt );
  }

  public GosuEditor getCurrentEditor()
  {
    ITab selectedTab = _editorTabPane.getSelectedTab();
    return selectedTab == null ? null : (GosuEditor)selectedTab.getContentPane();
  }

  public IType getCurrentEditorType()
  {
    return getCurrentEditor() == null
           ? null
           : getCurrentEditor().getScriptPart() == null
             ? null
             : getCurrentEditor().getScriptPart().getContainingType();
  }

  private void makeRunMenu( JMenuBar menuBar )
  {
    JMenu runMenu = new SmartMenu( "Run" );
    runMenu.setMnemonic( 'R' );
    menuBar.add( runMenu );

    runMenu.add( CommonMenus.makeRun( this::getCurrentEditorType ) );
    runMenu.add( CommonMenus.makeDebug( this::getCurrentEditorType ) );

    JMenuItem runRecentItem = new JMenuItem( new RunRecentActionHandler() );
    runRecentItem.setMnemonic( 'C' );
    runRecentItem.setAccelerator( KeyStroke.getKeyStroke( "control F5" ) );
    runMenu.add( runRecentItem );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeStop( () -> this ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeStepOver( this::getDebugger ) );
    runMenu.add( CommonMenus.makeStepInto( this::getDebugger ) );
    runMenu.add( CommonMenus.makeStepOut( this::getDebugger ) );
    runMenu.add( CommonMenus.makeRunToCursor( this::getDebugger, this::getBreakpointManager, this::getCurrentEditor ) );
    //noinspection Convert2MethodRef
    runMenu.add( CommonMenus.makeDropFrame( this::getDebugger, () -> _debugPanel.getDropToFrame() ) );
    runMenu.add( CommonMenus.makePause( this::getDebugger ) );
    runMenu.add( CommonMenus.makeResume( this::getDebugger ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeToggleBreakpoint( this::getBreakpointManager, this::getCurrentEditor ) );
    runMenu.add( CommonMenus.makeViewBreakpoints( () -> null ) );
    runMenu.add( CommonMenus.makeMuteBreakpoints( this::getBreakpointManager ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeClear( () -> this ) );
  }

  private void makeSearchMenu( JMenuBar menuBar )
  {
    JMenu searchMenu = new SmartMenu( "Search" );
    searchMenu.setMnemonic( 'S' );
    menuBar.add( searchMenu );

    JMenuItem findItem = new JMenuItem(
      new AbstractAction( "Find..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          StandardLocalSearch.performLocalSearch( getCurrentEditor(), false );
        }
      } );
    findItem.setMnemonic( 'F' );
    findItem.setAccelerator( KeyStroke.getKeyStroke( "control F" ) );
    searchMenu.add( findItem );

    JMenuItem replaceItem = new JMenuItem(
      new AbstractAction( "Replace..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          StandardLocalSearch.performLocalSearch( getCurrentEditor(), true );
        }
      } );
    replaceItem.setMnemonic( 'R' );
    replaceItem.setAccelerator( KeyStroke.getKeyStroke( "control R" ) );
    searchMenu.add( replaceItem );

    JMenuItem nextItem = new JMenuItem(
      new AbstractAction( "Next" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( isEnabled() )
          {
            StandardLocalSearch.repeatFind( getCurrentEditor() );
          }
        }

        @Override
        public boolean isEnabled()
        {
          return StandardLocalSearch.canRepeatFind( getCurrentEditor() );
        }
      } );
    nextItem.setMnemonic( 'N' );
    nextItem.setAccelerator( KeyStroke.getKeyStroke( "F3" ) );
    searchMenu.add( nextItem );

    JMenuItem previousItem = new JMenuItem(
      new AbstractAction( "Previous" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( isEnabled() )
          {
            StandardLocalSearch.repeatFindBackwards( getCurrentEditor() );
          }
        }

        @Override
        public boolean isEnabled()
        {
          return StandardLocalSearch.canRepeatFind( getCurrentEditor() );
        }
      } );
    previousItem.setMnemonic( 'P' );
    previousItem.setAccelerator( KeyStroke.getKeyStroke( "shift F3" ) );
    searchMenu.add( previousItem );


    searchMenu.addSeparator();


    JMenuItem gotoLineItem = new JMenuItem(
      new AbstractAction( "Go To Line" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().displayGotoLinePopup();
        }
      } );
    gotoLineItem.setMnemonic( 'G' );
    gotoLineItem.setAccelerator( KeyStroke.getKeyStroke( "control G" ) );
    searchMenu.add( gotoLineItem );

  }

  private void makeEditMenu( JMenuBar menuBar )
  {
    JMenu editMenu = new SmartMenu( "Edit" );
    editMenu.setMnemonic( 'E' );
    menuBar.add( editMenu );


    JMenuItem undoItem = new JMenuItem(
      new AbstractAction( "Undo" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( getUndoManager().canUndo() )
          {
            getUndoManager().undo();
          }
        }

        @Override
        public boolean isEnabled()
        {
          return getUndoManager().canUndo();
        }
      } );
    undoItem.setMnemonic( 'U' );
    undoItem.setAccelerator( KeyStroke.getKeyStroke( "control Z" ) );
    editMenu.add( undoItem );

    JMenuItem redoItem = new JMenuItem(
      new AbstractAction( "Redo" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( getUndoManager().canRedo() )
          {
            getUndoManager().redo();
          }
        }

        @Override
        public boolean isEnabled()
        {
          return getUndoManager().canRedo();
        }
      } );
    redoItem.setMnemonic( 'R' );
    redoItem.setAccelerator( KeyStroke.getKeyStroke( "control shift Z" ) );
    editMenu.add( redoItem );


    editMenu.addSeparator();


    editMenu.add( CommonMenus.makeCut( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makeCopy( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makePaste( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makePasteJavaAsGosu( this::getCurrentEditor ) );

    editMenu.addSeparator();


    JMenuItem deleteItem = new JMenuItem(
      new AbstractAction( "Delete" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( getCurrentEditor() != null && StudioUtilities.containsFocus( getCurrentEditor() ) )
          {
            getCurrentEditor().delete();
          }
        }
      } );
    deleteItem.setMnemonic( 'D' );
    deleteItem.setAccelerator( KeyStroke.getKeyStroke( "DELETE" ) );
    editMenu.add( deleteItem );

    JMenuItem deletewordItem = new JMenuItem(
      new AbstractAction( "Delete Word" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteWord();
        }
      } );
    deletewordItem.setMnemonic( 'e' );
    deletewordItem.setAccelerator( KeyStroke.getKeyStroke( "control BACKSPACE" ) );
    editMenu.add( deletewordItem );

    JMenuItem deleteWordForwardItem = new JMenuItem(
      new AbstractAction( "Delete Word Forward" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteWordForwards();
        }
      } );
    deleteWordForwardItem.setMnemonic( 'F' );
    deleteWordForwardItem.setAccelerator( KeyStroke.getKeyStroke( "control DELETE" ) );
    editMenu.add( deleteWordForwardItem );

    JMenuItem deleteLine = new JMenuItem(
      new AbstractAction( "Delete Line" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteLine();
        }
      } );
    deleteLine.setMnemonic( 'L' );
    deleteLine.setAccelerator( KeyStroke.getKeyStroke( "control Y" ) );
    editMenu.add( deleteLine );


    editMenu.addSeparator();


    JMenuItem selectWord = new JMenuItem(
      new AbstractAction( "Select Word" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().selectWord();
        }
      } );
    selectWord.setMnemonic( 'W' );
    selectWord.setAccelerator( KeyStroke.getKeyStroke( "control W" ) );
    editMenu.add( selectWord );

    JMenuItem narraowSelection = new JMenuItem(
      new AbstractAction( "Narrow Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().narrowSelectWord();
        }
      } );
    narraowSelection.setMnemonic( 'N' );
    narraowSelection.setAccelerator( KeyStroke.getKeyStroke( "control shift W" ) );
    editMenu.add( narraowSelection );


    editMenu.addSeparator();


    JMenuItem duplicateItem = new JMenuItem(
      new AbstractAction( "Duplicate" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().duplicate();
        }
      } );
    duplicateItem.setAccelerator( KeyStroke.getKeyStroke( "control D" ) );
    editMenu.add( duplicateItem );

    JMenuItem joinItem = new JMenuItem(
      new AbstractAction( "Join Lines" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().joinLines();
        }
      } );
    joinItem.setAccelerator( KeyStroke.getKeyStroke( "control J" ) );
    editMenu.add( joinItem );

    JMenuItem indentItem = new JMenuItem(
      new AbstractAction( "Indent Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !getCurrentEditor().isIntellisensePopupShowing() )
          {
            getCurrentEditor().handleBulkIndent( false );
          }
        }
      } );
    indentItem.setMnemonic( 'I' );
    indentItem.setAccelerator( KeyStroke.getKeyStroke( "TAB" ) );
    editMenu.add( indentItem );

    JMenuItem outdentItem = new JMenuItem(
      new AbstractAction( "Outdent Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !getCurrentEditor().isIntellisensePopupShowing() )
          {
            getCurrentEditor().handleBulkIndent( true );
          }
        }
      } );
    outdentItem.setMnemonic( 'O' );
    outdentItem.setAccelerator( KeyStroke.getKeyStroke( "shift TAB" ) );
    editMenu.add( outdentItem );
  }

  private void makeFileMenu( JMenuBar menuBar )
  {
    JMenu fileMenu = new SmartMenu( "File" );
    fileMenu.setMnemonic( 'F' );
    menuBar.add( fileMenu );

    JMenuItem newExperimentItem = new JMenuItem(
      new AbstractAction( "New Experiment..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          newExperiment();
        }
      } );
    newExperimentItem.setMnemonic( 'P' );
    fileMenu.add( newExperimentItem );

    JMenuItem openExperimentItem = new JMenuItem(
      new AbstractAction( "Open Experiment..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          openExperiment();
        }
      } );
    openExperimentItem.setMnemonic( 'J' );
    fileMenu.add( openExperimentItem );


    fileMenu.addSeparator();

    JMenu reopenExperiment = new JMenu( "Reopen Experiment" );
    ReopenExperimentPopup.initialize( reopenExperiment );
    fileMenu.add( reopenExperiment );


    fileMenu.addSeparator();


    JMenu newItem = new JMenu( "New" );
    NewFilePopup.addMenuItems( newItem );
    fileMenu.add( newItem );

    JMenuItem openItem = new JMenuItem(
      new AbstractAction( "Open..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          openFile();
        }
      } );
    openItem.setMnemonic( 'O' );
    fileMenu.add( openItem );


    JMenuItem saveItem = new JMenuItem(
      new AbstractAction( "Save" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          save();
        }
      } );
    saveItem.setMnemonic( 'S' );
    saveItem.setAccelerator( KeyStroke.getKeyStroke( "control S" ) );
    fileMenu.add( saveItem );


    fileMenu.addSeparator();


    JMenuItem classpathItem = new JMenuItem(
      new AbstractAction( "Dependencies..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          displayClasspath();
        }
      } );
    classpathItem.setMnemonic( 'd' );
    fileMenu.add( classpathItem );


    fileMenu.addSeparator();


    JMenuItem exitItem = new JMenuItem(
      new AbstractAction( "Exit" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          exit();
        }
      } );
    exitItem.setMnemonic( 'x' );
    fileMenu.add( exitItem );
  }

  private void closeActiveEditor()
  {
    if( _editorTabPane.getTabCount() > 1 )
    {
      _editorTabPane.removeTab( _editorTabPane.getSelectedTab() );
    }
    else
    {
      exit();
    }
  }

  private void closeOthers()
  {
    _editorTabPane.setVisible( false );
    try
    {
      for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
      {
        if( _editorTabPane.getSelectedTabIndex() != i )
        {
          _editorTabPane.removeTab( _editorTabPane.getTabAt( i ) );
        }
      }
    }
    finally
    {
      _editorTabPane.setVisible( true );
    }
  }

  private void displayClasspath()
  {
    ClasspathDialog dlg = new ClasspathDialog( new File( "." ) );
    dlg.setVisible( true );
  }

  private void shipIt()
  {
    ShipIt.instance().shipIt( getExperiment() );
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  private boolean compile()
  {
    SettleModalEventQueue.instance().run();

    saveIfDirty();
    getMessagesPanel().clear();
    showMessages( true );
    return BuildIt.instance().buildIt( c -> true );
  }

  public void exit()
  {
    if( saveIfDirty() )
    {
      NoExitSecurityManager.CLOSING = true;
      System.exit( 0 );
    }
  }

  public void setEditorSplitPosition( int iPos )
  {
    if( _splitPane != null )
    {
      _splitPane.setPosition( iPos );
    }
  }

  public void setExperimentSplitPosition( int iPos )
  {
    if( _outerSplitPane != null )
    {
      _outerSplitPane.setPosition( iPos );
    }
  }

  public GosuEditor getGosuEditor()
  {
    return getCurrentEditor();
  }

  /**
   *
   */
  private void mapKeystrokes()
  {
    // Undo/Redo
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Z, InputEvent.CTRL_MASK ),
                  "Undo", new UndoActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Z, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK ),
                  "Redo", new RedoActionHandler() );
//## conflicts with Delete Line, which is also ctrl+y (same as IJ)
//    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Y, InputEvent.CTRL_MASK ),
//                  "Redo2", new RedoActionHandler() );


    // Old-style undo/redo
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK ),
                  "UndoOldStyle", new UndoActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK ),
                  "RetoOldStyle", new RedoActionHandler() );

    // Run
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ),
                  "Run", new CommonMenus.ClearAndRunActionHandler( "Run", () -> getCurrentEditor().getScriptPart().getContainingType() ) );

    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.CTRL_MASK ),
                  "Run", new CommonMenus.ClearAndRunActionHandler( "Run", () -> getCurrentEditor().getScriptPart().getContainingType() ) );
  }

  private void mapKeystroke( KeyStroke ks, String strCmd, Action action )
  {
    enableInputMethods( true );
    enableEvents( AWTEvent.KEY_EVENT_MASK );
    InputMap imap = getInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

    Object key = imap.get( ks );
    if( key == null )
    {
      key = strCmd;
      imap.put( ks, key );
    }
    getActionMap().put( key, action );
  }

  void resetChangeHandler()
  {
    ScriptChangeHandler handler = new ScriptChangeHandler( getUndoManager() );
    handler.establishUndoableEditListener( getCurrentEditor() );
  }

  public void openFile()
  {
    JFileChooser fc = new JFileChooser( getCurrentFile().getParentFile() );
    fc.setDialogTitle( "Open Gosu File" );
    fc.setDialogType( JFileChooser.OPEN_DIALOG );
    fc.setCurrentDirectory( getCurrentFile().getParentFile() );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      openFile( fc.getSelectedFile(), true );
    }
  }

  public void openFile( File file, boolean bFocus )
  {
    openFile( makePartId( file ), file, bFocus );
  }

  public boolean openType( String fqn, boolean bFocus )
  {
    TreeModel model = getExperimentView().getTree().getModel();
    FileTree root = (FileTree)model.getRoot();
    FileTree fileTree = root.find( fqn );
    if( fileTree != null )
    {
      openFile( fileTree.getFileOrDir(), bFocus );
      return true;
    }
    return false;
  }

  public static IScriptPartId makePartId( File file )
  {
    TypeSystem.pushGlobalModule();
    try
    {
      if( file == null )
      {
        return new ScriptPartId( "New Program", null );
      }
      else if( file.getName().endsWith( ".gs" ) ||
               file.getName().endsWith( ".gsx" ) ||
               file.getName().endsWith( ".gsp" ) ||
               file.getName().endsWith( ".gst" ) )
      {
        String classNameForFile = TypeNameUtil.getClassNameForFile( file );
        return new ScriptPartId( classNameForFile, null );
      }

      else
      {
        return new ScriptPartId( "Unknown Resource Type", null );
      }
    }
    finally
    {
      TypeSystem.popGlobalModule();
    }
  }

  public void openInitialFile( IScriptPartId partId, File file )
  {
    _initialFile = true;
    try
    {
      if( file != null || _editorTabPane.getTabCount() == 0 )
      {
        openFile( partId, file, true );
      }
    }
    finally
    {
      _initialFile = false;
    }
  }

  private void openFile( IScriptPartId partId, File file, boolean bFocus )
  {
    if( openTab( file, bFocus ) )
    {
      return;
    }

    final GosuEditor editor = createEditor();

    if( partId == null )
    {
      throw new IllegalArgumentException( "partId should be non-null" );
    }

    initEditorMode( file, editor );

    file = file == null ? getExperiment().getOrMakeUntitledProgram() : file;
    editor.putClientProperty( "_file", file );
    removeLruTab();
    String classNameForFile = TypeNameUtil.getClassNameForFile( file );
    IType type = TypeSystem.getByFullNameIfValid( classNameForFile );
    if( type == null )
    {
      return;
    }
    _editorTabPane.addTab( type.getRelativeName(), EditorUtilities.findIcon( type ), editor );
    _editorTabPane.selectTab( _editorTabPane.findTabWithContent( editor ), true );

    String strSource;
    if( !file.exists() )
    {
      strSource = "";
    }
    else
    {
      try( FileInputStream in = new FileInputStream( file ) )
      {
        strSource = StreamUtil.getContent( StreamUtil.getInputStreamReader( in ) );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }
    if( _parentFrame != null )
    {
      updateTitle();
    }

    try
    {
      editor.read( partId, strSource, "" );
      resetChangeHandler();
      if( bFocus )
      {
        EventQueue.invokeLater( () -> editor.getEditor().requestFocus() );
      }
    }
    catch( Throwable t )
    {
      throw new RuntimeException( t );
    }
  }

  private void removeLruTab()
  {
    if( _editorTabPane.getTabCount() < MAX_TABS )
    {
      return;
    }

    List<ITabHistoryContext> mruList = getTabSelectionHistory().getTabMruList();
    for( int i = mruList.size() - 1; i >= 0; i-- )
    {
      ITabHistoryContext tabCtx = mruList.get( i );
      File file = (File)tabCtx.getContentId();
      GosuEditor editor = findTab( file );
      if( editor != null )
      {
        closeTab( file );
      }
    }
  }

  private void updateTitle()
  {
    File file = getCurrentFile();
    Experiment experiment = getExperiment();
    String currentFilePath = file == null ? "  " : " - ..." + File.separator + experiment.makeExperimentRelativePath( file ) + " - ";
    String title = experiment.getName() + " - [" + experiment.getExperimentDir().getAbsolutePath() + "]" + currentFilePath + "Gosu Lab " + Gosu.getVersion();
    _parentFrame.setTitle( title );
  }

  private boolean openTab( File file, boolean bFocus )
  {
    GosuEditor editor = findTab( file );
    if( editor != null )
    {
      _editorTabPane.selectTab( _editorTabPane.findTabWithContent( editor ), bFocus );
      return true;
    }
    return false;
  }

  public GosuEditor findTab( File file )
  {
    if( file == null )
    {
      return null;
    }
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      GosuEditor editor = (GosuEditor)_editorTabPane.getTabAt( i ).getContentPane();
      if( editor != null && file.equals( editor.getClientProperty( "_file" ) ) )
      {
        return editor;
      }
    }
    return null;
  }

  private void setCurrentFile( File file )
  {
    getCurrentEditor().putClientProperty( "_file", file );
    openFile( file, false );
  }

  public File getCurrentFile()
  {
    GosuEditor currentEditor = getCurrentEditor();
    return currentEditor == null ? null : (File)currentEditor.getClientProperty( "_file" );
  }

  public boolean save()
  {
    if( getCurrentFile() == null )
    {
      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle( "Save Gosu File" );
      fc.setDialogType( JFileChooser.SAVE_DIALOG );
      fc.setCurrentDirectory( new File( "." ) );
      fc.setFileFilter( new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
      int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );

      if( returnVal == JFileChooser.APPROVE_OPTION )
      {
        setCurrentFile( fc.getSelectedFile() );
      }
      else
      {
        return false;
      }
    }

    if( !getCurrentFile().exists() )
    {
      boolean created = false;
      String msg = "";
      try
      {
        created = getCurrentFile().createNewFile();
      }
      catch( IOException e )
      {
        //ignore
        e.printStackTrace();
        msg += " : " + e.getMessage();
      }

      if( !created )
      {
        JOptionPane.showMessageDialog( this, "Could not create file " + getCurrentFile().getName() + msg );
        return false;
      }
    }

    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    return true;
  }

  public boolean save( File file, GosuEditor editor )
  {
    if( !file.exists() )
    {
      boolean created = false;
      String msg = "";
      try
      {
        created = file.createNewFile();
      }
      catch( IOException e )
      {
        //ignore
        e.printStackTrace();
        msg += " : " + e.getMessage();
      }

      if( !created )
      {
        JOptionPane.showMessageDialog( this, "Could not create file " + file.getName() + msg );
        return false;
      }
    }

    saveAndReloadType( file, editor );
    return true;
  }

  private void saveAndReloadType( File file, GosuEditor editor )
  {
    try( FileOutputStream out = new FileOutputStream( file ) )
    {
      StreamUtil.copy( new StringReader( editor.getText() ), out );
      setDirty( editor, false );
      reload( editor.getScriptPart().getContainingType() );
    }
    catch( IOException ex )
    {
      throw new RuntimeException( ex );
    }
  }

  private void reload( IType type )
  {
    if( type == null )
    {
      return;
    }

    TypeSystem.refresh( (ITypeRef)type );
  }

  public boolean saveIfDirty()
  {
    if( isDirty( getCurrentEditor() ) )
    {
      return save();
    }
    return true;
  }

  public void newExperiment()
  {
    File untitled = new File( getExperiment().getExperimentDir().getParentFile(), "Untitled" );
    //noinspection ResultOfMethodCallIgnored
    untitled.mkdirs();
    JFileChooser fc = new JFileChooser( untitled );
    fc.setDialogTitle( "New Experiment" );
    fc.setDialogType( JFileChooser.OPEN_DIALOG );
    fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    fc.setMultiSelectionEnabled( false );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return !new File( f, f.getName() + ".prj" ).exists();
        }

        public String getDescription()
        {
          return "Gosu Experiment Directory (directory name is your experiment name)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal != JFileChooser.APPROVE_OPTION )
    {
      return;
    }
    File selectedFile = fc.getSelectedFile();
    Experiment experiment = new Experiment( selectedFile.getName(), selectedFile, this );
    clearTabs();
    EventQueue.invokeLater( () -> restoreExperimentState( experiment ) );
  }

  public void openExperiment()
  {
    FileDialog fc = new FileDialog( EditorUtilities.frameForComponent( this ), "Open Experiment", FileDialog.LOAD );
    fc.setDirectory( getExperiment().getExperimentDir().getAbsolutePath() );
    fc.setMultipleMode( false );
    fc.setFile( "*.prj" );
    fc.setVisible( true );
    String selectedFile = fc.getFile();
    if( selectedFile != null )
    {
      File prjFile = new File( fc.getDirectory(), selectedFile );
      if( prjFile.isFile() )
      {
        File experimentDir = prjFile.getParentFile();
        openExperiment( experimentDir );
      }
    }
  }

  public void openExperiment( File experimentDir )
  {
    storeExperimentState();
    clearTabs();
    EventQueue.invokeLater( () -> restoreExperimentState( new Experiment( experimentDir, this ) ) );
  }

  private boolean isValidGosuSourceFile( File file )
  {
    if( file == null )
    {
      return false;
    }
    String strName = file.getName().toLowerCase();
    return strName.endsWith( ".gs" ) ||
           strName.endsWith( ".gsx" ) ||
           strName.endsWith( ".gst" ) ||
           strName.endsWith( ".gsp" );
  }

  public void saveAs()
  {
    JFileChooser fc = new JFileChooser( getCurrentFile() );
    fc.setDialogTitle( "Save Gosu File" );
    fc.setDialogType( JFileChooser.SAVE_DIALOG );
    fc.setCurrentDirectory( getCurrentFile() != null ? getCurrentFile().getParentFile() : new File( "." ) );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      setCurrentFile( fc.getSelectedFile() );
      save();
    }
  }

  public void dumpBytecode()
  {
    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    clearOutput();
    byte[] bytes = TypeSystem.getGosuClassLoader().getBytes( getClassAtCaret() );
    ClassReader cr = new ClassReader( bytes );
    //int flags = ClassReader.SKIP_FRAMES;
    int flags = 0;
    StringWriter out = new StringWriter();
    cr.accept( new TraceClassVisitor( null, new GosuTextifier(), new PrintWriter( out ) ), flags );
    System.out.println( out );
  }

  private IGosuClass getClassAtCaret()
  {
    IParseTree locAtCaret = getCurrentEditor().getDeepestLocationAtCaret();
    if( locAtCaret == null )
    {
      return getCurrentEditor().getParsedClass();
    }
    IParsedElement elemAtCaret = locAtCaret.getParsedElement();
    while( elemAtCaret != null &&
           !(elemAtCaret instanceof IClassStatement) &&
           !(elemAtCaret instanceof IBlockExpression) )
    {
      elemAtCaret = elemAtCaret.getParent();
    }
    if( elemAtCaret == null )
    {
      return getCurrentEditor().getParsedClass();
    }
    if( elemAtCaret instanceof IClassStatement )
    {
      return elemAtCaret.getGosuClass();
    }
    if( elemAtCaret instanceof IBlockExpression )
    {
      return ((IBlockExpression)elemAtCaret).getBlockGosuClass();
    }
    throw new IllegalStateException( "Unexpected parse element: " + elemAtCaret.getClass().getName() );
  }

  public void execute( String fqn )
  {
    if( _runState != RunState.None )
    {
      return;
    }

    saveAndReloadType( getCurrentFile(), getCurrentEditor() );

    showConsole();

    //InProcessRunner.execute( fqn, this );
    _processRunner = new OutOfProcessRunner( RunState.Run );
    _processRunner.execute( fqn, this );
  }

  public void debug( String fqn )
  {
    if( _runState != RunState.None )
    {
      return;
    }

    saveAndReloadType( getCurrentFile(), getCurrentEditor() );

    showConsole();

    //InProcessRunner.execute( fqn, this );
    _processRunner = new OutOfProcessRunner( RunState.Debug );
    _processRunner.execute( fqn, this );
  }

  public boolean isRunning()
  {
    return _runState == RunState.Run;
  }

  public boolean isDebugging()
  {
    return _runState == RunState.Debug;
  }

  public TypeNameCache getTypeNamesCache()
  {
    return _typeNamesCache;
  }

  void addBusySignal( RunState runState )
  {
    _runState = runState;
  }

  void pipeInput()
  {
    EventQueue.invokeLater( () -> {
      try
      {
        _oldIn = System.in;
        PipedInputStream sysIn = new PipedInputStream();
        Process process = _processRunner.getProcess();
        if( process == null )
        {
          // Assume we are in-process
          _inWriter = new OutputStreamWriter( new PipedOutputStream( sysIn ) );
          System.setIn( sysIn );
        }
        else
        {
          _inWriter = new OutputStreamWriter( process.getOutputStream() );
        }
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
      JTextPane outputPanel = _consolePanel.getOutputPanel();
      outputPanel.setEditable( true );
      _sysInListener = new SysInListener();
      outputPanel.addKeyListener( _sysInListener );
    } );
  }

  public void killProcess()
  {
    if( _processRunner != null )
    {
      if( _debugger != null && (_debugger.isSuspended() || _debugger.isPaused()) )
      {
        _debugger.resumeExecution();
      }

      Process process = _processRunner.getProcess();
      if( process != null && process.isAlive() )
      {
        process.destroy();
      }
    }
  }

  public Debugger getDebugger()
  {
    return _debugger;
  }
  public void clearDebugger()
  {
    _debugger = null;
    showDebugger( false );
  }

  public void makeDebugger( VirtualMachine vm )
  {
    EventQueue.invokeLater( () -> {
      _debugger = new Debugger( vm, _breakpointManager );
      _debugger.addChangeListener( dbg -> repaintEditor() );
      showDebugger( true );
      _debugger.startDebugging();
    } );
  }

  private void repaintEditor()
  {
    if( getCurrentEditor() != null )
    {
      getCurrentEditor().repaint();
    }
    if( _debugger.isSuspended() )
    {
      jumptToBreakpoint( _debugger.getSuspendedLocation(), false );
    }
  }

  public void jumptToBreakpoint( Location location, boolean bFocus )
  {
    String fqn = Debugger.getOutermostType( location.declaringType() );
    int line = location.lineNumber();
    java.awt.EventQueue.invokeLater( () -> {
      if( openType( fqn, bFocus ) )
      {
        getCurrentEditor().gotoLine( line );
      }
    } );
  }

  public void showDebugger( boolean bShow )
  {
    if( bShow )
    {
      if( _debugPanel == null )
      {
        _debugPanel = new DebugPanel( _debugger );
        _bottomTabPane.addTab( "<html>Debugging: <i>" + SignatureUtil.getSimpleName( _processRunner.getTypeName() ) + "</i>", EditorUtilities.loadIcon( "images/debug.png" ), _debugPanel );
        _debugPanel.addLocationListener( loc -> jumptToBreakpoint( loc, false ) );
      }
      else
      {
        ITab tab = _bottomTabPane.findTabWithContent( _debugPanel );
        _bottomTabPane.selectTab( tab, false );
      }
    }
    else
    {
      _bottomTabPane.removeTabWithContent( _debugPanel );
      _debugPanel = null;
    }
  }

  public BreakpointManager getBreakpointManager()
  {
    return _breakpointManager;
  }

  class SysInListener extends KeyAdapter
  {
    @Override
    public void keyReleased( KeyEvent e )
    {
      if( e.getKeyCode() == KeyEvent.VK_ENTER )
      {
        JTextPane op = _consolePanel.getOutputPanel();
        Element elem = getElementAt( op.getCaretPosition() - 1 );
        try
        {
          String text = _consolePanel.getOutputPanel().getText( elem.getStartOffset(), elem.getEndOffset() - elem.getStartOffset() );
          _inWriter.write( text );
          _inWriter.flush();
        }
        catch( Exception e1 )
        {
          //throw new RuntimeException( e1 );
        }
      }
    }

    public Element getElementAt( int offset )
    {
      return getElementAt( _consolePanel.getOutputPanel().getDocument().getDefaultRootElement(), offset );
    }

    private Element getElementAt( Element parent, int offset )
    {
      if( parent.isLeaf() )
      {
        return parent;
      }
      return getElementAt( parent.getElement( parent.getElementIndex( offset ) ), offset );
    }
  }

  void removeBusySignal()
  {
    if( _runState != RunState.None )
    {
      _runState = RunState.None;
      _statPanel.setVisible( false );
      _statPanel.revalidate();
      _consolePanel.getOutputPanel().setEditable( false );
      _consolePanel.getOutputPanel().removeKeyListener( _sysInListener );
      _inWriter = null;
      System.setIn( _oldIn );
    }
  }

  void executeTemplate()
  {
    try
    {
      System.out.println( "Will prompt for args soon, for now run the template programmatically from a program" );
    }
    catch( Throwable t )
    {
      t.printStackTrace();
    }
  }
  void debugTemplate()
  {
    executeTemplate();
  }

  public void clearOutput()
  {
    _consolePanel.clear();
  }

  public AtomicUndoManager getUndoManager()
  {
    return getCurrentEditor() != null
           ? getCurrentEditor().getUndoManager()
           : _defaultUndoMgr;
  }

  public void selectTab( File file )
  {
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      GosuEditor editor = (GosuEditor)_editorTabPane.getTabAt( i ).getContentPane();
      if( editor != null )
      {
        if( editor.getClientProperty( "_file" ).equals( file ) )
        {
          _editorTabPane.selectTab( _editorTabPane.getTabAt( i ), true );
          return;
        }
      }
    }
    openFile( file, true );
  }

  public void closeTab( File file )
  {
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      GosuEditor editor = (GosuEditor)_editorTabPane.getTabAt( i ).getContentPane();
      if( editor != null )
      {
        if( editor.getClientProperty( "_file" ).equals( file ) )
        {
          _editorTabPane.removeTab( _editorTabPane.getTabAt( i ) );
          return;
        }
      }
    }
  }

  public void goBackward()
  {
    getTabSelectionHistory().goBackward();
  }

  public boolean canGoBackward()
  {
    return getTabSelectionHistory().canGoBackward();
  }

  public void goForward()
  {
    getTabSelectionHistory().goForward();
  }

  public boolean canGoForward()
  {
    return getTabSelectionHistory().canGoForward();
  }

  public void displayRecentViewsPopup()
  {
    List<ITabHistoryContext> mruViewsList = new ArrayList<>( getTabSelectionHistory().getTabMruList() );

    for( int i = 0; i < mruViewsList.size(); i++ )
    {
      ITabHistoryContext ctx = mruViewsList.get( i );
      if( ctx != null && ctx.represents( getCurrentEditor() ) )
      {
        mruViewsList.remove( ctx );
      }
    }

    LabelListPopup popup = new LabelListPopup( "Recent Views", mruViewsList, "No recent views" );
    popup.addNodeChangeListener(
      e -> {
        ITabHistoryContext context = (ITabHistoryContext)e.getSource();
        getTabSelectionHistory().getTabHistoryHandler().selectTab( context );
      } );
    popup.show( this, getWidth() / 2 - 100, getHeight() / 2 - 200 );
  }

  public boolean isDirty( GosuEditor editor )
  {
    if( editor == null )
    {
      return false;
    }
    Boolean bDirty = (Boolean)editor.getClientProperty( "_bDirty" );
    return bDirty == null ? false : bDirty;
  }

  public void setDirty( GosuEditor editor, boolean bDirty )
  {
    editor.putClientProperty( "_bDirty", bDirty );
  }

  class UndoActionHandler extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        getUndoManager().undo();
      }
    }

    public boolean isEnabled()
    {
      return getUndoManager().canUndo();
    }
  }

  class RedoActionHandler extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        getUndoManager().redo();
      }
    }

    public boolean isEnabled()
    {
      return getUndoManager().canRedo();
    }
  }

  class RunRecentActionHandler extends CommonMenus.ClearAndRunActionHandler
  {
    public RunRecentActionHandler()
    {
      //noinspection Convert2Lambda
      super( "Run Recent",
             new Supplier<IType>()
             {
               @Override
               public IType get()
               {
                 String recentProgram = getExperiment() == null ? null : getExperiment().getRecentProgram();
                 if( recentProgram != null )
                 {
                   return TypeSystem.getByFullNameIfValid( recentProgram );
                 }
                 return null;
               }
             } );
    }
  }

  public Clipboard getClipboard()
  {
    return Toolkit.getDefaultToolkit().getSystemClipboard();
  }

  private void markErrorsForGosuLanguageTest()
  {
    GosuDocument document = getCurrentEditor().getGosuDocument();
    //noinspection ThrowableResultOfMethodCallIgnored
    ParseResultsException pre = document.getParseResultsException();
    if( pre == null || (!pre.hasParseExceptions() && !pre.hasParseWarnings()) )
    {
      return;
    }
    final Map<Integer, List<String>> map = new HashMap<>();
    for( IParseIssue pi : pre.getParseIssues() )
    {
      ResourceKey messageKey = pi.getMessageKey();
      if( messageKey != null )
      {
        String issue = messageKey.getKey();
        int iLine = pi.getLine();
        List<String> issues = map.get( iLine );
        if( issues == null )
        {
          map.put( iLine, issues = new ArrayList<>() );
        }
        issues.add( issue );
      }
    }
    final List<Integer> lines = new ArrayList<>( map.keySet() );
    Collections.sort( lines );

    String text;
    try
    {
      text = document.getText( 0, document.getLength() );
      String[] strLines = text.split( "\n" );
      removeOldIssueKeyMarkers( strLines );
      addIssueKeyMarkers( strLines, lines, map );
      CompoundEdit atom = getUndoManager().beginUndoAtom( "Mark Phase" );
      document.replace( 0, text.length(), joinLines( strLines ), null );
      getUndoManager().endUndoAtom( atom );
    }
    catch( BadLocationException e )
    {
      e.printStackTrace();
    }
  }

  private String joinLines( String[] strLines )
  {
    StringBuilder sb = new StringBuilder();
    for( String line : strLines )
    {
      sb.append( line ).append( '\n' );
    }
    return sb.toString();
  }

  private void removeOldIssueKeyMarkers( String[] lines )
  {
    for( int i = 0; i < lines.length; i++ )
    {
      int issueIndex = lines[i].indexOf( "  //## issuekeys:" );
      if( issueIndex != -1 )
      {
        lines[i] = lines[i].substring( 0, issueIndex );
      }
    }
  }

  private void addIssueKeyMarkers( String[] strLines, List<Integer> lines, Map<Integer, List<String>> map )
  {
    for( int iLine : lines )
    {
      String issues = makeIssueString( map.get( iLine ) );
      strLines[iLine - 1] = strLines[iLine - 1].concat( issues );
    }
  }

  private String makeIssueString( List<String> issues )
  {
    StringBuilder sb = new StringBuilder();
    for( String issue : issues )
    {
      sb.append( sb.length() != 0 ? ", " : "" ).append( issue );
    }
    sb.insert( 0, "  //## issuekeys: " );
    return sb.toString();
  }
}
