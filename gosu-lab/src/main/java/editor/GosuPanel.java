package editor;

import com.sun.jdi.Location;
import com.sun.jdi.VirtualMachine;
import editor.debugger.BreakpointManager;
import editor.debugger.DebugPanel;
import editor.debugger.Debugger;
import editor.run.IProcessRunner;
import editor.run.IRunConfig;
import editor.run.RunState;
import editor.search.SearchPanel;
import editor.shipit.ExperimentBuild;
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
import editor.util.LabStatusBar;
import editor.util.LabToolbarButton;
import editor.util.LabelListPopup;
import gw.util.PathUtil;
import editor.util.SettleModalEventQueue;
import editor.util.SmartMenu;
import editor.util.SmartMenuItem;
import editor.util.SourceFileCreator;
import editor.util.ToolBar;
import editor.util.TypeNameUtil;
import gw.fs.IFile;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.util.TraceClassVisitor;
import gw.lang.Gosu;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.util.StreamUtil;

import java.io.Writer;
import java.nio.file.Path;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.Reader;
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
  private LabStatusBar _statusBar;
  private boolean _initialFile;
  private TypeNameCache _typeNamesCache;
  private Experiment _experiment;
  private OutputStreamWriter _inWriter;
  private SysInListener _sysInListener;
  private InputStream _oldIn;
  private MessagesPanel _messages;
  private SearchPanel _searches;
  private DebugPanel _debugPanel;
  private IProcessRunner _processRunner;
  private BreakpointManager _breakpointManager;
  private Debugger _debugger;

  public GosuPanel( JFrame frame )
  {
    _parentFrame = frame;
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
    _bottomTabPane = new TabPane( TabPane.MINIMIZABLE | TabPane.RESTORABLE | TabPane.TOP_BORDER_ONLY | TabPane.DYNAMIC );
    bottom.add( _bottomTabPane, BorderLayout.CENTER );

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
    _experimentView.setBackground( Scheme.active().getWindow() );
    TabPane experimentViewTabPane = new TabPane( TabPosition.TOP, TabPane.MINIMIZABLE | TabPane.RESTORABLE | TabPane.TOP_BORDER_ONLY );
    experimentViewTabPane.addTab( "Experiment", null, _experimentView );


    _splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, experimentViewTabPane, _editorTabPane );
    _outerSplitPane = new CollapsibleSplitPane( SwingConstants.VERTICAL, _splitPane, bottom );

    add( _outerSplitPane, BorderLayout.CENTER );

    JPanel statPanel = makeStatusBar();
    add( statPanel, BorderLayout.SOUTH );

    ToolBar toolbar = makeMainToolbar();
    add( toolbar, BorderLayout.NORTH );

    JMenuBar menuBar = makeMenuBar();
    _parentFrame.setJMenuBar( menuBar );
    handleMacStuff();


    EventQueue.invokeLater( () -> _outerSplitPane.collapseBottom( _bottomTabPane ) );

    EventQueue.invokeLater( this::mapKeystrokes );
  }

  private ToolBar makeMainToolbar()
  {
    ToolBar toolbar = new ToolBar();
    toolbar.setDynamicBorder( BorderFactory.createCompoundBorder( BorderFactory.createMatteBorder( 1, 0, 0, 0, Scheme.active().getControlLigthShadow() ), BorderFactory.createEmptyBorder( 1, 1, 2, 1 ) ) );
    LabToolbarButton item;

    item = new LabToolbarButton( new CommonMenus.OpenProjectActionHandler() );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.SaveActionHandler() );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.UndoActionHandler() );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.RedoActionHandler() );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.CutActionHandler( this::getCurrentEditor ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.CopyActionHandler( this::getCurrentEditor ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.PasteActionHandler( this::getCurrentEditor ) );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.FindActionHandler( this::getCurrentEditor ) );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.ReplaceActionHandler( this::getCurrentEditor ) );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.GoBackActionHandler() );
    toolbar.add( item );
    item = new LabToolbarButton( new CommonMenus.GoForwardActionHandler() );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.MakeActionHandler() );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.ClearAndRunActionHandler( this::getRunConfig ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getRunConfig();
      return rc == null ? "Run..." : "Run '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    item = new LabToolbarButton( new CommonMenus.ClearAndDebugActionHandler( this::getRunConfig ) );
    item.setToolTipSupplier( () -> {
      IRunConfig rc = getRunConfig();
      return rc == null ? "Debug..." : "Debug '" + rc.getName() + "'";
    } );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.ShipItActionHandler() );
    toolbar.add( item );

    toolbar.add( makeSeparator() );

    item = new LabToolbarButton( new CommonMenus.SettingsActionHandler() );
    toolbar.add( item );

    return toolbar;
  }
  private JComponent makeSeparator()
  {
    JPanel separator = new JPanel( new BorderLayout() ) {
      @Override
      protected void paintComponent( Graphics g )
      {
        super.paintComponent( g );
        g.setColor( Scheme.active().getSeparator2() );
        g.drawLine( getWidth()/2, 0, getWidth()/2, getHeight()-1 );
      }
    };
    separator.setMaximumSize( new Dimension( 9, 20 ) );
    separator.setBackground( Scheme.active().getMenu() );
    return separator;
  }

  public ExperimentView getExperimentView()
  {
    return _experimentView;
  }

  public MessagesPanel getMessagesPanel()
  {
    return _messages;
  }

  public SearchPanel getSearchPanel()
  {
    return _searches;
  }
  
  public SystemPanel getConsolePanel()
  {
    return _consolePanel;
  }

  public DebugPanel getDebugPanel()
  {
    return _debugPanel;
  }

  public MessagesPanel showMessages( boolean bShow )
  {
    return _messages = showTab( bShow, "Messages", null, _messages, MessagesPanel::new );
  }

  public SearchPanel showSearches( boolean bShow )
  {
    return _searches = showTab( bShow, "Search", null, _searches, SearchPanel::new );
  }

  public SystemPanel showConsole( boolean bShow )
  {
    return _consolePanel = showTab( bShow, "Console", EditorUtilities.loadIcon( "images/console.png" ), _consolePanel, SystemPanel::new );
  }

  public <P extends JComponent> P showTab( boolean bShow, String title, Icon icon, P panel, Supplier<P> creator )
  {
    if( bShow )
    {
      EventQueue.invokeLater( _outerSplitPane::restorePane );

      ITab tab = _bottomTabPane.findTabWithContent( panel );
      if( tab == null )
      {
        panel = creator.get();
        _bottomTabPane.addTab( title, icon, panel );
        return panel;
      }
      else
      {
        _bottomTabPane.selectTab( tab, false );
        return panel;
      }
    }
    else
    {
      _bottomTabPane.removeTabWithContent( panel );
      EventQueue.invokeLater( () -> {
        SettleModalEventQueue.instance().run();
        if( _bottomTabPane.getTabCount() == 0 && !_outerSplitPane.isMin() )
        {
          _outerSplitPane.toggleCollapse( _bottomTabPane );
        }
      } );
      return null;
    }
  }

  private void handleMacStuff()
  {
//## we are using a plaform independent LAF now with menubar on the frame where it belongs
//    if( PlatformUtil.isMac() )
//    {
//      System.setProperty( "apple.laf.useScreenMenuBar", "true" );
//      System.setProperty( "com.apple.mrj.application.apple.menu.about.name", "Gosu Editor" );
//    }
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
    showSearches( false );
    SettleModalEventQueue.instance().run();
    getTabSelectionHistory().dispose();
  }

  private void storeExperimentState()
  {
    if( _initialFile )
    {
      return;
    }
    getExperiment().save();
    LabFrame.instance().saveLabState( _experiment );
  }

  public Experiment getExperiment()
  {
    return _experiment;
  }

  public void restoreExperimentState( Experiment experiment )
  {
    _experiment = experiment;

    RunMe.reinitializeGosu( experiment );
    TypeSystem.refresh( TypeSystem.getGlobalModule() );

    LabFrame.instance().addExperiment( experiment );
    _experimentView.load( _experiment );

    for( String openFile : experiment.getOpenFiles() )
    {
      Path file = PathUtil.create( openFile );
      if( PathUtil.isFile( file ) )
      {
        openFile( file, false );
      }
    }
    String activeFile = experiment.getActiveFile();
    if( activeFile == null )
    {
      EventQueue.invokeLater( () -> SourceFileCreator.instance().getOrMakeUntitledProgram( experiment ) );
    }
    else
    {
      openTab( PathUtil.create( activeFile ), true );
    }
    SettleModalEventQueue.instance().run();
    EventQueue.invokeLater( () -> {
      parse();
      EditorHost currentEditor = getCurrentEditor();
      if( currentEditor != null )
      {
        currentEditor.getEditor().requestFocus();
      }
    } );
  }

  private JPanel makeStatusBar()
  {
    _statusBar = new LabStatusBar();
    return _statusBar;
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setStatus( String status )
  {
    _statusBar.setStatus( status );
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
    EditorHost editor = getTabSelectionHistory().getPreviousEditor();
    if( editor != null )
    {
      if( isDirty( editor ) )
      {
        save( (Path)editor.getClientProperty( "_file" ), editor );
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

  private EditorHost createEditor( Path file, IScriptPartId partId )
  {
    EditorHost editorHost = EditorFactory.createEditor( file, partId );

    editorHost.setBorder( BorderFactory.createEmptyBorder() );
    addDirtyListener( editorHost );
    EventQueue.invokeLater( () -> ((AbstractDocument)editorHost.getEditor().getDocument()).setDocumentFilter( new GosuPanelDocumentFilter( editorHost ) ) );
    return editorHost;
  }

  private void addDirtyListener( final EditorHost editor )
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

    JMenuItem gosuItem = new SmartMenuItem(
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

    JMenuItem helpItem = new SmartMenuItem(
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

    JMenuItem playItem = new SmartMenuItem(
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

    JMenuItem discussItem = new SmartMenuItem(
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

    JMenuItem plugin = new SmartMenuItem(
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

    JMenuItem backItem = new SmartMenuItem( new CommonMenus.GoBackActionHandler() );
    backItem.setMnemonic( 'B' );
    backItem.setAccelerator( KeyStroke.getKeyStroke( "alt LEFT" ) );
    windowMenu.add( backItem );

    JMenuItem forwardItem = new SmartMenuItem( new CommonMenus.GoForwardActionHandler() );
    forwardItem.setMnemonic( 'F' );
    forwardItem.setAccelerator( KeyStroke.getKeyStroke( "alt RIGHT" ) );
    windowMenu.add( forwardItem );


    windowMenu.addSeparator();


    JMenuItem recentItem = new SmartMenuItem(
      new AbstractAction( "Recent Files" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          displayRecentViewsPopup();
        }
      } );
    recentItem.setMnemonic( 'R' );
    recentItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " E" ) );
    windowMenu.add( recentItem );


    windowMenu.addSeparator();


    JMenuItem closeActiveItem = new SmartMenuItem(
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
    closeActiveItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F4" ) );
    windowMenu.add( closeActiveItem );

    JMenuItem closeOthersItem = new SmartMenuItem(
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
    codeMenu.add( CommonMenus.makeCodeComplete( this::getCurrentGosuEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeParameterInfo( this::getCurrentGosuEditor ) );
    codeMenu.add( CommonMenus.makeExpressionType( this::getCurrentGosuEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeGotoDeclaration( this::getCurrentGosuEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeShowFileInTree( this::getCurrentEditor ) );
    codeMenu.addSeparator();
    codeMenu.add( CommonMenus.makeQuickDocumentation( this::getCurrentGosuEditor ) );

    codeMenu.addSeparator();

    JMenuItem openTypeItem = new SmartMenuItem(
      new AbstractAction( "Open Type..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          GotoTypePopup.display();
        }
      } );
    openTypeItem.setMnemonic( 'O' );
    openTypeItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " N" ) );
    codeMenu.add( openTypeItem );

    if( "true".equals( System.getProperty( "spec" ) ) )
    {
      codeMenu.addSeparator();
      JMenuItem markItem = new SmartMenuItem(
        new AbstractAction( "Mark Errors For Gosu Language Test" )
        {
          @Override
          public void actionPerformed( ActionEvent e )
          {
            markErrorsForGosuLanguageTest();
          }
        } );
      markItem.setMnemonic( 'M' );
      markItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " M" ) );
      codeMenu.add( markItem );
    }

    codeMenu.addSeparator();
    JMenuItem viewBytecodeItem = new SmartMenuItem(
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
          return getCurrentGosuEditor() != null && getCurrentEditor().getScriptPart() != null &&
                 getCurrentEditor().getScriptPart().getContainingType() != null;
        }
      } );
    codeMenu.add( viewBytecodeItem );
  }

  public GosuEditor getCurrentGosuEditor()
  {
    EditorHost editor = getCurrentEditor();
    return editor instanceof GosuEditor ? (GosuEditor)editor : null;
  }

  private void makeBuildMenu( JMenuBar menuBar )
  {
    JMenu buildMenu = new SmartMenu( "Build" );
    buildMenu.setMnemonic( 'b' );
    menuBar.add( buildMenu );


    JMenuItem make = new SmartMenuItem( new CommonMenus.MakeActionHandler() );
    make.setMnemonic( 'm' );
    make.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F9" ) );
    buildMenu.add( make );

    JMenuItem rebuild = new SmartMenuItem( new CommonMenus.RebuildActionHandler() );
    rebuild.setMnemonic( 'b' );
    buildMenu.add( rebuild );


    buildMenu.addSeparator();


    JMenuItem shipIt = new SmartMenuItem( new CommonMenus.ShipItActionHandler() );
    shipIt.setMnemonic( 'p' );
    shipIt.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F10" ) );
    buildMenu.add( shipIt );
  }

  public EditorHost getCurrentEditor()
  {
    ITab selectedTab = _editorTabPane.getSelectedTab();
    return selectedTab == null ? null : (EditorHost)selectedTab.getContentPane();
  }

  public IRunConfig getRunConfig()
  {
    // Get the current editor's type

    IType type = getCurrentEditor() == null
                 ? null
                 : getCurrentEditor().getScriptPart() == null
                   ? null
                   : getCurrentEditor().getScriptPart().getContainingType();

    if( !EditorUtilities.isRunnable( type ) )
    {
      type = null;

      // The current type is not runnable, use the most recently run type

      IRunConfig mruRunConfig = getExperiment() == null ? null : getExperiment().getMruRunConfig();
      if( mruRunConfig != null && mruRunConfig.isValid() )
      {
        return mruRunConfig;
      }
    }
    return type == null ? null : getExperiment() == null ? null : getExperiment().getOrCreateRunConfig( type ) ;
  }

  private void makeRunMenu( JMenuBar menuBar )
  {
    JMenu runMenu = new SmartMenu( "Run" );
    runMenu.setMnemonic( 'R' );
    menuBar.add( runMenu );

    runMenu.add( CommonMenus.makeRun( this::getRunConfig ) );
    runMenu.add( CommonMenus.makeDebug( this::getRunConfig ) );

    runMenu.add( CommonMenus.makeRunConfig() );
    runMenu.add( CommonMenus.makeDebugConfig() );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeStop( () -> this ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeStepOver( this::getDebugger ) );
    runMenu.add( CommonMenus.makeStepInto( this::getDebugger ) );
    runMenu.add( CommonMenus.makeStepOut( this::getDebugger ) );
    runMenu.add( CommonMenus.makeRunToCursor( this::getDebugger, this::getBreakpointManager, this::getCurrentGosuEditor ) );
    //noinspection Convert2MethodRef
    runMenu.add( CommonMenus.makeDropFrame( this::getDebugger, () -> _debugPanel.getDropToFrame() ) );
    runMenu.add( CommonMenus.makePause( this::getDebugger ) );
    runMenu.add( CommonMenus.makeResume( this::getDebugger ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeEvaluateExpression( this::getDebugger ) );
    runMenu.add( CommonMenus.makeShowExecutionPoint( this::getDebugger ) );

    runMenu.addSeparator();

    runMenu.add( CommonMenus.makeToggleBreakpoint( this::getBreakpointManager, this::getCurrentGosuEditor ) );
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

    JMenuItem findItem = new SmartMenuItem( new CommonMenus.FindActionHandler( this::getCurrentEditor ) );
    findItem.setMnemonic( 'F' );
    findItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " F" ) );
    searchMenu.add( findItem );

    JMenuItem replaceItem = new SmartMenuItem( new CommonMenus.ReplaceActionHandler( this::getCurrentEditor ) );
    replaceItem.setMnemonic( 'R' );
    replaceItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " R" ) );
    searchMenu.add( replaceItem );

    JMenuItem nextItem = new SmartMenuItem(
      new AbstractAction( "Next" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( isEnabled() )
          {
            getCurrentEditor().gotoNextUsageHighlight();
          }
        }

        @Override
        public boolean isEnabled()
        {
          return getCurrentEditor() != null && getCurrentEditor().getEditor().getHighlighter().getHighlights().length > 0;
        }
      } );
    nextItem.setMnemonic( 'N' );
    nextItem.setAccelerator( KeyStroke.getKeyStroke( "F3" ) );
    searchMenu.add( nextItem );

    JMenuItem previousItem = new SmartMenuItem(
      new AbstractAction( "Previous" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( isEnabled() )
          {
            getCurrentEditor().gotoPrevUsageHighlight();
          }
        }

        @Override
        public boolean isEnabled()
        {
          return getCurrentEditor() != null && getCurrentEditor().getEditor().getHighlighter().getHighlights().length > 0;
        }
      } );
    previousItem.setMnemonic( 'P' );
    previousItem.setAccelerator( KeyStroke.getKeyStroke( "shift F3" ) );
    searchMenu.add( previousItem );

    searchMenu.addSeparator();
    
    JMenuItem findIInPathItem = new SmartMenuItem( new CommonMenus.FindInPathActionHandler( FileTreeUtil::getRoot ) );
    findIInPathItem.setMnemonic( 'P' );
    findIInPathItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift F" ) );
    searchMenu.add( findIInPathItem );

    JMenuItem replaceInPathItem = new SmartMenuItem( new CommonMenus.ReplaceInPathActionHandler( FileTreeUtil::getRoot ) );
    replaceInPathItem.setMnemonic( 'A' );
    replaceInPathItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift R" ) );
    searchMenu.add( replaceInPathItem );
        
    searchMenu.addSeparator();

    searchMenu.add( CommonMenus.makeFindUsages( FileTreeUtil::getRoot ) );
    searchMenu.add( CommonMenus.makeFindUsagesInFile() );
    searchMenu.add( CommonMenus.makeHighlightFindUsagesInFile() );

    searchMenu.addSeparator();

    searchMenu.add( CommonMenus.makePrevOccurrent( () -> getGosuPanel() == null ? null : getGosuPanel().getSearchPanel() ) );
    searchMenu.add( CommonMenus.makeNextOccurrent( () -> getGosuPanel() == null ? null : getGosuPanel().getSearchPanel() ) );

    searchMenu.addSeparator();

    JMenuItem gotoLineItem = new SmartMenuItem(
      new AbstractAction( "Go To Line" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().displayGotoLinePopup();
        }
      } );
    gotoLineItem.setMnemonic( 'G' );
    gotoLineItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " G" ) );
    searchMenu.add( gotoLineItem );

  }

  private GosuPanel getGosuPanel()
  {
    return LabFrame.instance().getGosuPanel();
  }

  private void makeEditMenu( JMenuBar menuBar )
  {
    JMenu editMenu = new SmartMenu( "Edit" );
    editMenu.setMnemonic( 'E' );
    menuBar.add( editMenu );


    JMenuItem undoItem = new SmartMenuItem( new CommonMenus.UndoActionHandler() );
    undoItem.setMnemonic( 'U' );
    undoItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " Z" ) );
    editMenu.add( undoItem );

    JMenuItem redoItem = new SmartMenuItem( new CommonMenus.RedoActionHandler() );
    redoItem.setMnemonic( 'R' );
    redoItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift Z" ) );
    editMenu.add( redoItem );


    editMenu.addSeparator();


    editMenu.add( CommonMenus.makeCut( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makeCopy( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makePaste( this::getCurrentEditor ) );

    editMenu.add( CommonMenus.makePasteJavaAsGosu( this::getCurrentGosuEditor ) );

    editMenu.addSeparator();


    JMenuItem deleteItem = new SmartMenuItem(
      new AbstractAction( "Delete" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( getCurrentEditor() != null && EditorUtilities.containsFocus( getCurrentEditor() ) )
          {
            getCurrentEditor().delete();
          }
        }
      } );
    deleteItem.setMnemonic( 'D' );
    deleteItem.setAccelerator( KeyStroke.getKeyStroke( "DELETE" ) );
    editMenu.add( deleteItem );

    JMenuItem deletewordItem = new SmartMenuItem(
      new AbstractAction( "Delete Word" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteWord();
        }
      } );
    deletewordItem.setMnemonic( 'e' );
    deletewordItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " BACKSPACE" ) );
    editMenu.add( deletewordItem );

    JMenuItem deleteWordForwardItem = new SmartMenuItem(
      new AbstractAction( "Delete Word Forward" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteWordForwards();
        }
      } );
    deleteWordForwardItem.setMnemonic( 'F' );
    deleteWordForwardItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " DELETE" ) );
    editMenu.add( deleteWordForwardItem );

    JMenuItem deleteLine = new SmartMenuItem(
      new AbstractAction( "Delete Line" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().deleteLine();
        }
      } );
    deleteLine.setMnemonic( 'L' );
    deleteLine.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " Y" ) );
    editMenu.add( deleteLine );


    editMenu.addSeparator();


    JMenuItem selectWord = new SmartMenuItem(
      new AbstractAction( "Select Word" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentGosuEditor().selectWord();
        }
        @Override
        public boolean isEnabled()
        {
          return getCurrentGosuEditor() != null;
        }

      } );
    selectWord.setMnemonic( 'W' );
    selectWord.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " W" ) );
    editMenu.add( selectWord );

    JMenuItem narraowSelection = new SmartMenuItem(
      new AbstractAction( "Narrow Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentGosuEditor().narrowSelectWord();
        }
        @Override
        public boolean isEnabled()
        {
          return getCurrentGosuEditor() != null;
        }

      } );
    narraowSelection.setMnemonic( 'N' );
    narraowSelection.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift W" ) );
    editMenu.add( narraowSelection );


    editMenu.addSeparator();


    JMenuItem duplicateItem = new SmartMenuItem(
      new AbstractAction( "Duplicate" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().duplicate();
        }
      } );
    duplicateItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " D" ) );
    editMenu.add( duplicateItem );

    JMenuItem joinItem = new SmartMenuItem(
      new AbstractAction( "Join Lines" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().joinLines();
        }
      } );
    joinItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " J" ) );
    editMenu.add( joinItem );

    JMenuItem indentItem = new SmartMenuItem(
      new AbstractAction( "Indent Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !getCurrentEditor().isCompletionPopupShowing() )
          {
            getCurrentEditor().handleBulkIndent( false );
          }
        }
      } );
    indentItem.setMnemonic( 'I' );
    indentItem.setAccelerator( KeyStroke.getKeyStroke( "TAB" ) );
    editMenu.add( indentItem );

    JMenuItem outdentItem = new SmartMenuItem(
      new AbstractAction( "Outdent Selection" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !getCurrentEditor().isCompletionPopupShowing() )
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

    JMenuItem newExperimentItem = new SmartMenuItem(
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

    JMenuItem openExperimentItem = new SmartMenuItem( new CommonMenus.OpenProjectActionHandler() );
    openExperimentItem.setMnemonic( 'O' );
    fileMenu.add( openExperimentItem );


    fileMenu.addSeparator();

    JMenu reopenExperiment = new SmartMenu( "Reopen Experiment" );
    ReopenExperimentPopup.initialize( reopenExperiment );
    fileMenu.add( reopenExperiment );


    fileMenu.addSeparator();


    JMenu newItem = new SmartMenu( "New" );
    NewFilePopup.addMenuItems( newItem );
    fileMenu.add( newItem );

    JMenuItem openItem = new SmartMenuItem(
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


    JMenuItem saveItem = new SmartMenuItem( new CommonMenus.SaveActionHandler() );
    saveItem.setMnemonic( 'S' );
    saveItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " S" ) );
    fileMenu.add( saveItem );


    fileMenu.addSeparator();


    JMenuItem settings = new SmartMenuItem( new CommonMenus.SettingsActionHandler() ) ;
    saveItem.setMnemonic( 'G' );
    saveItem.setAccelerator( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " alt S" ) );
    fileMenu.add( settings );

    JMenuItem classpathItem = new SmartMenuItem(
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


    JMenuItem exitItem = new SmartMenuItem(
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

  public void shipIt()
  {
    ShipIt.instance().shipIt( getExperiment() );
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
  public boolean make()
  {
    SettleModalEventQueue.instance().run();

    saveIfDirty();
    if( getMessagesPanel() != null )
    {
      getMessagesPanel().clear();
    }
    showMessages( true );

    //## NOTE: We distinguish between making during a debug session and not.  This is primarily for the
    //##       case where the user is running from source (NOT compiling bytecode to disk), in which case
    //##       we only want to compile and reload classes that have changed since the debugger started.

    Debugger debugger = getDebugger();
    ExperimentBuild expBuild = debugger != null ? debugger.getClassRedefiner() : ExperimentBuild.instance();
    return expBuild.make( c -> true );
  }

  public boolean rebuild()
  {
    SettleModalEventQueue.instance().run();

    saveIfDirty();
    if( getMessagesPanel() != null )
    {
      getMessagesPanel().clear();
    }
    showMessages( true );
    return ExperimentBuild.instance().rebuild( c -> true );
  }

  public void exit()
  {
    if( saveIfDirty() )
    {
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

  public EditorHost getGosuEditor()
  {
    return getCurrentEditor();
  }

  /**
   *
   */
  private void mapKeystrokes()
  {
    // Undo/Redo
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Z, EditorUtilities.CONTROL_KEY_MASK ),
                  "Undo", new UndoActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Z, EditorUtilities.CONTROL_KEY_MASK | InputEvent.SHIFT_MASK ),
                  "Redo", new RedoActionHandler() );
//## conflicts with Delete Line, which is also ctrl+y (same as IJ)
//    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Y, EditorUtilities.CONTROL_KEY_MASK ),
//                  "Redo2", new RedoActionHandler() );


    // Old-style undo/redo
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK ),
                  "UndoOldStyle", new UndoActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK ),
                  "RetoOldStyle", new RedoActionHandler() );
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
    JFileChooser fc = new JFileChooser( getCurrentFile().getParent().toFile() );
    fc.setDialogTitle( "Open Gosu Path" );
    fc.setDialogType( JFileChooser.OPEN_DIALOG );
    fc.setCurrentDirectory( getCurrentFile().getParent().toFile() );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f.toPath() );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      openFile( fc.getSelectedFile().toPath(), true );
    }
  }

  public void openFile( Path file, boolean bFocus )
  {
    openFile( makePartId( file ), file, bFocus );
  }

  public boolean openType( String fqn, boolean bFocus )
  {
    FileTree fileTree = FileTreeUtil.find( fqn );
    if( fileTree != null )
    {
      openFile( fileTree.getFileOrDir(), bFocus );
      return true;
    }
    return false;
  }

  public static IScriptPartId makePartId( Path file )
  {
    TypeSystem.pushGlobalModule();
    try
    {
      if( file == null )
      {
        return new ScriptPartId( "New Program", null );
      }
      String classNameForFile = TypeNameUtil.getTypeNameForFile( file );
      return new ScriptPartId( classNameForFile, null );
    }
    finally
    {
      TypeSystem.popGlobalModule();
    }
  }

  public void openInitialFile( IScriptPartId partId, Path file )
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

  private void openFile( IScriptPartId partId, Path file, boolean bFocus )
  {
    if( openTab( file, bFocus ) )
    {
      return;
    }

    final EditorHost editor = createEditor( file, partId );

    if( partId == null )
    {
      throw new IllegalArgumentException( "partId should be non-null" );
    }

    file = file == null ? SourceFileCreator.instance().getOrMakeUntitledProgram( _experiment ) : file;
    editor.putClientProperty( "_file", file );
    removeLruTab();
    String classNameForFile = TypeNameUtil.getTypeNameForFile( file );
    IType type = TypeSystem.getByFullNameIfValid( classNameForFile );
    if( type != null )
    {
      _editorTabPane.addTab( type.getRelativeName(), EditorUtilities.findIcon( type ), editor );
    }
    else
    {
      _editorTabPane.addTab( PathUtil.getName( file ), EditorUtilities.findIcon( file ), editor );
    }
    _editorTabPane.selectTab( _editorTabPane.findTabWithContent( editor ), true );

    String strSource;
    if( !PathUtil.exists( file ) )
    {
      strSource = "";
    }
    else
    {
      try( InputStream in = PathUtil.createInputStream( file ) )
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
      editor.read( partId, strSource );
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
      Path file = (Path)tabCtx.getContentId();
      EditorHost editor = findTab( file );
      if( editor != null )
      {
        closeTab( file );
      }
    }
  }

  private void updateTitle()
  {
    Path file = getCurrentFile();
    Experiment experiment = getExperiment();
    String currentFilePath = file == null ? "  " : " - ..." + File.separator + experiment.makeExperimentRelativePath( file ) + " - ";
    String title = experiment.getName() + " - [" + PathUtil.getAbsolutePathName( experiment.getExperimentDir() ) + "]" + currentFilePath + "Gosu Lab " + Gosu.getVersion();
    _parentFrame.setTitle( title );
  }

  private boolean openTab( Path file, boolean bFocus )
  {
    EditorHost editor = findTab( file );
    if( editor != null )
    {
      _editorTabPane.selectTab( _editorTabPane.findTabWithContent( editor ), bFocus );
      return true;
    }
    return false;
  }

  public EditorHost findTab( Path file )
  {
    if( file == null )
    {
      return null;
    }
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      EditorHost editor = (EditorHost)_editorTabPane.getTabAt( i ).getContentPane();
      if( editor != null && file.equals( editor.getClientProperty( "_file" ) ) )
      {
        return editor;
      }
    }
    return null;
  }

  private void setCurrentFile( Path file )
  {
    getCurrentEditor().putClientProperty( "_file", file );
    openFile( file, false );
  }

  public Path getCurrentFile()
  {
    EditorHost currentEditor = getCurrentEditor();
    return currentEditor == null ? null : (Path)currentEditor.getClientProperty( "_file" );
  }

  public boolean save()
  {
    if( getCurrentFile() == null )
    {
      JFileChooser fc = new JFileChooser();
      fc.setDialogTitle( "Save Gosu Path" );
      fc.setDialogType( JFileChooser.SAVE_DIALOG );
      fc.setCurrentDirectory( new File( "." ) );
      fc.setFileFilter( new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f.toPath() );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
      int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );

      if( returnVal == JFileChooser.APPROVE_OPTION )
      {
        setCurrentFile( fc.getSelectedFile().toPath() );
      }
      else
      {
        return false;
      }
    }

    if( !PathUtil.exists( getCurrentFile() ) )
    {
      String msg = "";
      if( !PathUtil.createNewFile( getCurrentFile() ) )
      {
        JOptionPane.showMessageDialog( this, "Could not create file " + PathUtil.getName( getCurrentFile() ) + msg );
        return false;
      }
    }

    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    return true;
  }

  public boolean save( Path file, EditorHost editor )
  {
    if( !PathUtil.exists( file ) )
    {
      String msg = "";
      if( !PathUtil.createNewFile( file ) )
      {
        JOptionPane.showMessageDialog( this, "Could not create file " + PathUtil.getName( file ) + msg );
        return false;
      }
    }

    saveAndReloadType( file, editor );
    return true;
  }

  private void saveAndReloadType( Path file, EditorHost editor )
  {
    if( !PathUtil.canWrite( file ) )
    {
      return;
    }

    FileTree fileTree = FileTreeUtil.getRoot().find( file );
    if( fileTree != null )
    {
      fileTree.setLastModified();
    }
    try( Writer out = PathUtil.createWriter( file ) )
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

  void reload( IType type )
  {
    if( type == null )
    {
      return;
    }

    for( IFile file: type.getSourceFiles() )
    {
      // This is more thorough re related files than refresh( type )
      TypeSystem.refreshed( file );
    }
  }

  public boolean saveIfDirty()
  {
    if( isDirty( getCurrentEditor() ) )
    {
      return save();
    }
    return true;
  }

  /**
   * This should only be called when either the file's contents change externally,
   * or when the file saves to disk.
   */
  public void refresh( Path file )
  {
    EditorHost editor = findTab( file );
    if( editor != null )
    {
      // The file is open in an editor, refresh it with the contents of the file

      try( Reader reader = PathUtil.createReader( file ) )
      {
        editor.refresh( StreamUtil.getContent( reader ) );
        setDirty( editor, false );
      }
      catch( IOException e )
      {
        throw new RuntimeException( e );
      }
    }

    FileTree root = FileTreeUtil.getRoot();
    FileTree node = root.find( file );

    if( node != null )
    {
      // Refresh the type system to include the changes
      IType type = node.getType();
      if( type != null )
      {
        reload( type );
      }
    }
  }

  public void newExperiment()
  {
    Path untitled = PathUtil.create( getExperiment().getExperimentDir().getParent(), "Untitled" );
    //noinspection ResultOfMethodCallIgnored
    PathUtil.mkdirs( untitled );
    JFileChooser fc = new JFileChooser( untitled.toFile() );
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
    Path selectedFile = fc.getSelectedFile().toPath();
    Experiment experiment = new Experiment( PathUtil.getName( selectedFile ), selectedFile, this );
    clearTabs();
    EventQueue.invokeLater( () -> restoreExperimentState( experiment ) );
  }

  public void openExperiment()
  {
    FileDialog fc = new FileDialog( EditorUtilities.frameForComponent( this ), "Open Experiment", FileDialog.LOAD );
    fc.setDirectory( PathUtil.getAbsolutePathName( getExperiment().getExperimentDir() ) );
    fc.setMultipleMode( false );
    fc.setFile( "*.prj" );
    fc.setVisible( true );
    String selectedFile = fc.getFile();
    if( selectedFile != null )
    {
      Path prjFile = PathUtil.create( fc.getDirectory(), selectedFile );
      if( PathUtil.isFile( prjFile ) )
      {
        Path experimentDir = prjFile.getParent();
        openExperiment( experimentDir );
      }
    }
  }

  public void openExperiment( Path experimentDir )
  {
    storeExperimentState();
    clearTabs();
    EventQueue.invokeLater( () -> restoreExperimentState( new Experiment( experimentDir, this ) ) );
  }

  private boolean isValidGosuSourceFile( Path file )
  {
    if( file == null )
    {
      return false;
    }
    String strName = PathUtil.getName( file ).toLowerCase();
    return strName.endsWith( ".gs" ) ||
           strName.endsWith( ".gsx" ) ||
           strName.endsWith( ".gst" ) ||
           strName.endsWith( ".gsp" );
  }

  public void saveAs()
  {
    JFileChooser fc = new JFileChooser( getCurrentFile().toFile() );
    fc.setDialogTitle( "Save Gosu Path" );
    fc.setDialogType( JFileChooser.SAVE_DIALOG );
    fc.setCurrentDirectory( getCurrentFile() != null ? getCurrentFile().getParent().toFile() : PathUtil.create( "." ).toFile() );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() || isValidGosuSourceFile( f.toPath() );
        }

        public String getDescription()
        {
          return "Gosu source file (*.gsp; *.gs; *.gsx; *.gst)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal == JFileChooser.APPROVE_OPTION )
    {
      setCurrentFile( fc.getSelectedFile().toPath() );
      save();
    }
  }

  public void dumpBytecode()
  {
    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    showConsole( true );
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
    IParseTree locAtCaret = getCurrentGosuEditor().getDeepestLocationAtCaret();
    if( locAtCaret == null )
    {
      return getCurrentGosuEditor().getParsedClass();
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
      return getCurrentGosuEditor().getParsedClass();
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

  public void execute( IRunConfig runConfig )
  {
    if( _runState != RunState.None )
    {
      return;
    }
    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    getExperiment().addRunConfig( runConfig );
    _processRunner = runConfig.run();
  }

  public void debug( IRunConfig runConfig )
  {
    if( _runState != RunState.None )
    {
      return;
    }

    saveAndReloadType( getCurrentFile(), getCurrentEditor() );
    getExperiment().addRunConfig( runConfig );
    _processRunner = runConfig.debug();
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

  public void addBusySignal( RunState runState )
  {
    _runState = runState;
  }

  public void pipeInput()
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
        process.destroyForcibly();
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
    EventQueue.invokeLater( () -> {
      showDebugger( false );
      if( getConsolePanel() != null )
      {
        showConsole( true );
      }
    } );
  }

  public void makeDebugger( VirtualMachine vm )
  {
    EventQueue.invokeLater( () -> {
      _debugger = new Debugger( vm, _breakpointManager );
      _debugger.addChangeListener( dbg -> EventQueue.invokeLater( this::handleDebuggerStateChange ) );
      showDebugger( true );
      showConsole( true );
      _debugger.startDebugging();
    } );
  }

  private void handleDebuggerStateChange()
  {
    if( !EventQueue.isDispatchThread() )
    {
      throw new Error();
    }

    EditorHost editor = getCurrentEditor();
    if( editor != null )
    {
      editor.repaint();
    }

    if( _debugger != null && _debugger.isSuspended() )
    {
      Location location = _debugger.getSuspendedLocation();
      if( location != null )
      {
        jumptToBreakpoint( location, false );
      }
    }
  }

  public void jumptToBreakpoint( Location location, boolean bFocus )
  {
    String fqn = Debugger.getOutermostType( location.declaringType() );
    int line = location.lineNumber();
    if( line <= 0 )
    {
      // ignore invalid line numbers e.g., sometimes -1 is a line number for generated code
      return;
    }
    java.awt.EventQueue.invokeLater( () -> {
      if( openType( fqn, bFocus ) )
      {
        getCurrentEditor().gotoLine( line );
      }
      Debugger debugger = getDebugger();
      if( debugger != null && debugger.getEventName() != null &&
          (debugger.getEventName().contains( "Breakpoint" ) ||
           debugger.getEventName().contains( "Exception" )) )
      {
        showDebugger( true );
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
        _bottomTabPane.addTab( _processRunner.getRunConfig().getName(), EditorUtilities.loadIcon( "images/debug.png" ), _debugPanel );
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
      if( _bottomTabPane.getTabCount() == 0 )
      {
        _outerSplitPane.collapseBottom( _bottomTabPane );
      }
      _debugPanel = null;
    }
  }

  public BreakpointManager getBreakpointManager()
  {
    return _breakpointManager;
  }

  public TabPane getEditorTabPane()
  {
    return _editorTabPane;
  }

  public List<FileTree> getOpenFilesInProject()
  {
    List<FileTree> files = new ArrayList<>();
    for( ITab tab: getEditorTabPane().getTabs() )
    {
      EditorHost editor = (EditorHost)tab.getContentPane();
      if( editor != null )
      {
        Path file = (Path)editor.getClientProperty( "_file" );
        if( file != null )
        {
          FileTree tree = FileTreeUtil.getRoot().find( file );
          if( tree != null )
          {
            files.add( tree );
          }
        }
      }
    }
    return files;
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

  public void removeBusySignal()
  {
    if( _runState != RunState.None )
    {
      _runState = RunState.None;
      if( _consolePanel != null )
      {
        _consolePanel.getOutputPanel().setEditable( false );
        _consolePanel.getOutputPanel().removeKeyListener( _sysInListener );
      }
      _inWriter = null;
      System.setIn( _oldIn );
    }
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

  public void selectTab( Path file )
  {
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      EditorHost editor = (EditorHost)_editorTabPane.getTabAt( i ).getContentPane();
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

  public void closeTab( Path file )
  {
    for( int i = 0; i < _editorTabPane.getTabCount(); i++ )
    {
      EditorHost editor = (EditorHost)_editorTabPane.getTabAt( i ).getContentPane();
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

    LabelListPopup popup = new LabelListPopup( "Recent Files", mruViewsList, "No recent files" );
    popup.addNodeChangeListener(
      e -> {
        ITabHistoryContext context = (ITabHistoryContext)e.getSource();
        getTabSelectionHistory().getTabHistoryHandler().selectTab( context );
      } );
    popup.show( this, getWidth() / 2 - 100, getHeight() / 2 - 200 );
  }

  public boolean isDirty( EditorHost editor )
  {
    if( editor == null )
    {
      return false;
    }
    Boolean bDirty = (Boolean)editor.getClientProperty( "_bDirty" );
    return bDirty == null ? false : bDirty;
  }

  public void setDirty( EditorHost editor, boolean bDirty )
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

  public Clipboard getClipboard()
  {
    return Toolkit.getDefaultToolkit().getSystemClipboard();
  }

  private void markErrorsForGosuLanguageTest()
  {
    GosuDocument document = getCurrentGosuEditor().getDocument();
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
