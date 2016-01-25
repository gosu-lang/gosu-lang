package editor;

import editor.search.MessageDisplay;
import editor.search.StandardLocalSearch;
import editor.splitpane.CollapsibleSplitPane;
import editor.tabpane.ITab;
import editor.tabpane.TabPane;
import editor.tabpane.TabPosition;
import editor.undo.AtomicUndoManager;
import editor.util.BrowserUtil;
import editor.util.EditorUtilities;
import editor.util.LabelListPopup;
import editor.util.PlatformUtil;
import editor.util.Project;
import editor.util.SettleModalEventQueue;
import editor.util.TaskQueue;
import editor.util.TypeNameUtil;
import gw.config.CommonServices;
import gw.lang.Gosu;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassPathThing;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.StreamUtil;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
public class GosuPanel extends JPanel
{
  private static final boolean DEBUG = true;

  private static final int MAX_TABS = 12;


  private TabPane _resultTabPane;
  private SystemPanel _resultPanel;
  private CollapsibleSplitPane _outerSplitPane;
  private CollapsibleSplitPane _splitPane;
  private ProjectView _projectView;
  private TabPane _projectViewTabPane;
  private JFrame _parentFrame;
  private boolean _bRunning;
  private TabPane _editorTabPane;
  private AtomicUndoManager _defaultUndoMgr;
  private TabSelectionHistory _history;
  private JLabel _status;
  private JPanel _statPanel;
  private boolean _initialFile;
  private Project _project;


  public GosuPanel( JFrame basicGosuEditor )
  {
    _parentFrame = basicGosuEditor;
    _defaultUndoMgr = new AtomicUndoManager( 10 );
    configUI();
  }

  private TabSelectionHistory getTabSelectionHistory()
  {
    return _history;
  }

  void configUI()
  {
    setLayout( new BorderLayout() );

    _resultPanel = new SystemPanel();
    _resultTabPane = new TabPane( TabPane.MINIMIZABLE );
    _resultTabPane.addTab( "Runtime Output", null, _resultPanel );

    _editorTabPane = new TabPane( TabPosition.TOP, TabPane.DYNAMIC | TabPane.MIN_MAX_REST );

    _history = new TabSelectionHistory( _editorTabPane );
    getTabSelectionHistory().setTabHistoryHandler( new EditorTabHistoryHandler() );

    _editorTabPane.addSelectionListener(
      e -> {
        if( !_editorTabPane.isVisible() )
        {
          // clearing tabs, don't save etc.
          return;
        }
        savePreviousTab();
        if( getCurrentEditor() == null )
        {
          return;
        }
        updateTitle();
        getCurrentEditor().getEditor().requestFocus();
        parse();
        storeProjectState();
      } );

    _projectView = new ProjectView();
    _projectView.setBackground( Color.white );
    _projectViewTabPane = new TabPane( TabPosition.TOP, TabPane.MIN_MAX_REST );
    _projectViewTabPane.addTab( "Project", null, _projectView );


    _splitPane = new CollapsibleSplitPane( SwingConstants.HORIZONTAL, _projectViewTabPane, _editorTabPane );
    _outerSplitPane = new CollapsibleSplitPane( SwingConstants.VERTICAL,  _splitPane, _resultTabPane );

    add( _outerSplitPane, BorderLayout.CENTER );

    JPanel statPanel = makeStatusBar();
    add( statPanel, BorderLayout.SOUTH );

    JMenuBar menuBar = makeMenuBar();
    _parentFrame.setJMenuBar( menuBar );
    handleMacStuff();

    EventQueue.invokeLater( () -> {
      setProjectSplitPosition( 70 );
      setEditorSplitPosition( 20 );
     } );

    EventQueue.invokeLater( this::mapKeystrokes );
  }

  public ProjectView getProjectView()
  {
    return _projectView;
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
    SettleModalEventQueue.instance().run();
    getTabSelectionHistory().dispose();
  }

  private void storeProjectState()
  {
    if( _initialFile )
    {
      return;
    }
    getProject().save( _editorTabPane );
    EditorUtilities.saveLayoutState( _project );
  }

  private Project getProject()
  {
    return _project;
  }

  static List<String> getLocalClasspath() {
    String javaHome = System.getProperty( "java.home" ).toLowerCase();
    List<String> localPath = new ArrayList<>();
    List<File> classpath = Gosu.getClasspath();
    for( int i = 0; i < classpath.size(); i++ )
    {
      File file = classpath.get( i );
      String filePath = file.getAbsolutePath().toLowerCase();
      if( !isUpperLevelClasspath( javaHome, filePath ) )
      {
        localPath.add( file.getAbsolutePath() );
      }
    }
    return localPath;
  }

  private static boolean isUpperLevelClasspath( String javaHome, String filePath )
  {
    return filePath.startsWith( javaHome ) ||
           filePath.contains( File.separator + "gosu-lang" + File.separator ) ||
           filePath.endsWith( File.separator + "tools.jar" ) ||
           filePath.endsWith( File.separator + "idea_rt.jar" );
  }

  public void restoreProjectState( Project project )
  {
    _project = project;

    if( project.getSourcePath().size() > 0 )
    {
      Gosu.setClasspath( project.getSourcePath().stream().map( File::new ).collect( Collectors.toList() ) );
    }

    TypeSystem.refresh( TypeSystem.getGlobalModule() );

    for( String openFile : project.getOpenFiles() )
    {
      File file = new File( openFile );
      if( file.isFile() )
      {
        openFile( file );
      }
    }
    String activeFile = project.getActiveFile();
    if( activeFile == null )
    {
      openFile( project.getOrMakeUntitledProgram() );
    }
    else
    {
      openTab( new File( activeFile ) );
    }
    SettleModalEventQueue.instance().run();
    _projectView.load( _project );
  }

  private JPanel makeStatusBar()
  {
    _statPanel = new JPanel( new BorderLayout() );
    _status = new JLabel();
    XPToolBarButton btnStop = new XPToolBarButton( "Stop" );
    btnStop.addActionListener( new StopActionHandler() );
    _statPanel.add( btnStop, BorderLayout.WEST );
    _statPanel.add( _status, BorderLayout.CENTER );
    _statPanel.setVisible( false );
    return _statPanel;
  }

  private void parse()
  {
    EventQueue.invokeLater( () -> {if( getCurrentEditor() != null ) getCurrentEditor().parse();} );
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
    }
  }

  private GosuEditor createEditor()
  {
    final GosuEditor editor = new GosuEditor( null,
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


    JMenuItem contextItem = new JMenuItem(
      new AbstractAction( "Doc Lookup at Caret" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().displayJavadocHelp( getCurrentEditor().getDeepestLocationAtCaret() );
        }
      } );
    contextItem.setMnemonic( 'D' );
    contextItem.setAccelerator( KeyStroke.getKeyStroke( "F1" ) );
    helpMenu.add( contextItem );


    helpMenu.addSeparator();


    JMenuItem introItem = new JMenuItem(
      new AbstractAction( "Introduction" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/intro.shtml" );
        }
      } );
    introItem.setMnemonic( 'I' );
    helpMenu.add( introItem );

    JMenuItem docsItem = new JMenuItem(
      new AbstractAction( "Documentation" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/doc/index.html" );
        }
      } );
    docsItem.setMnemonic( 'D' );
    helpMenu.add( docsItem );

    JMenuItem historyItem = new JMenuItem(
      new AbstractAction( "History" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/history.shtml" );
        }
      } );
    historyItem.setMnemonic( 'H' );
    helpMenu.add( historyItem );


    helpMenu.addSeparator();


    JMenuItem eclipseItem = new JMenuItem(
      new AbstractAction( "Eclipse" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/eclipse.shtml" );
        }
      } );
    eclipseItem.setMnemonic( 'E' );
    helpMenu.add( eclipseItem );

    JMenuItem intellijItem = new JMenuItem(
      new AbstractAction( "IntelliJ" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/editors.shtml" );
        }
      } );
    intellijItem.setMnemonic( 'I' );
    helpMenu.add( intellijItem );


    helpMenu.addSeparator();


    JMenuItem bugItem = new JMenuItem(
      new AbstractAction( "Report Bugs" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://code.google.com/p/gosu-lang/issues/entry" );
        }
      } );
    bugItem.setMnemonic( 'B' );
    helpMenu.add( bugItem );

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

    JMenuItem otherItem = new JMenuItem(
      new AbstractAction( "Other Links" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          BrowserUtil.openURL( "http://gosu-lang.org/links.shtml" );
        }
      } );
    otherItem.setMnemonic( 'L' );
    helpMenu.add( otherItem );
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

    JMenuItem completeItem = new JMenuItem(
      new AbstractAction( "Complete Code" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().handleCompleteCode();
        }
      } );
    completeItem.setMnemonic( 'C' );
    completeItem.setAccelerator( KeyStroke.getKeyStroke( "control SPACE" ) );
    codeMenu.add( completeItem );


    codeMenu.addSeparator();


    JMenuItem paraminfoItem = new JMenuItem(
      new AbstractAction( "Parameter Info" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          if( !getCurrentEditor().isIntellisensePopupShowing() )
          {
            getCurrentEditor().displayParameterInfoPopup( getCurrentEditor().getEditor().getCaretPosition() );
          }
        }
      } );
    paraminfoItem.setMnemonic( 'P' );
    paraminfoItem.setAccelerator( KeyStroke.getKeyStroke( "control P" ) );
    codeMenu.add( paraminfoItem );

    JMenuItem typeItem = new JMenuItem(
      new AbstractAction( "Expression Type" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().displayTypeInfoAtCurrentLocation();
        }
      } );
    typeItem.setMnemonic( 'T' );
    typeItem.setAccelerator( KeyStroke.getKeyStroke( "control T" ) );
    codeMenu.add( typeItem );


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
  }

  public GosuEditor getCurrentEditor()
  {
    ITab selectedTab = _editorTabPane.getSelectedTab();
    return selectedTab == null ? null : (GosuEditor)selectedTab.getContentPane();
  }

  private void makeRunMenu( JMenuBar menuBar )
  {
    JMenu runMenu = new SmartMenu( "Run" );
    runMenu.setMnemonic( 'R' );
    menuBar.add( runMenu );

    JMenuItem runItem = new JMenuItem( new RunActionHandler() );
    runItem.setMnemonic( 'R' );
    runItem.setAccelerator( KeyStroke.getKeyStroke( "F5" ) );
    runMenu.add( runItem );

    JMenuItem stopItem = new JMenuItem( new StopActionHandler() );
    stopItem.setMnemonic( 'S' );
    stopItem.setAccelerator( KeyStroke.getKeyStroke( "control F2" ) );
    runMenu.add( stopItem );

    runMenu.addSeparator();


    JMenuItem clearItem = new JMenuItem(
      new AbstractAction( "Clear" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          clearOutput();
        }
      } );
    clearItem.setMnemonic( 'C' );
    clearItem.setAccelerator( KeyStroke.getKeyStroke( "alt C" ) );
    runMenu.add( clearItem );
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
          if( StandardLocalSearch.canRepeatFind( getCurrentEditor() ) )
          {
            StandardLocalSearch.repeatFind( getCurrentEditor() );
          }
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
          if( StandardLocalSearch.canRepeatFind( getCurrentEditor() ) )
          {
            StandardLocalSearch.repeatFindBackwards( getCurrentEditor() );
          }
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


    JMenuItem cutItem = new JMenuItem(
      new AbstractAction( "Cut" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().clipCut( getClipboard() );
        }
      } );
    cutItem.setMnemonic( 't' );
    cutItem.setAccelerator( KeyStroke.getKeyStroke( "control X" ) );
    editMenu.add( cutItem );

    JMenuItem copyItem = new JMenuItem(
      new AbstractAction( "Copy" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().clipCopy( getClipboard() );
        }
      } );
    copyItem.setMnemonic( 'C' );
    copyItem.setAccelerator( KeyStroke.getKeyStroke( "control C" ) );
    editMenu.add( copyItem );

    JMenuItem pasteItem = new JMenuItem(
      new AbstractAction( "Paste" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().clipPaste( getClipboard() );
        }
      } );
    pasteItem.setMnemonic( 'P' );
    pasteItem.setAccelerator( KeyStroke.getKeyStroke( "control V" ) );
    editMenu.add( pasteItem );


    editMenu.addSeparator();


    JMenuItem deleteItem = new JMenuItem(
      new AbstractAction( "Delete" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          getCurrentEditor().delete();
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

    JMenuItem newProjectItem = new JMenuItem(
      new AbstractAction( "New Project..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          newProject();
        }
      } );
    newProjectItem.setMnemonic( 'P' );
    fileMenu.add( newProjectItem );

    JMenuItem openProjectItem = new JMenuItem(
      new AbstractAction( "Open Project..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          openProject();
        }
      } );
    openProjectItem.setMnemonic( 'J' );
    fileMenu.add( openProjectItem );


    fileMenu.addSeparator();


    JMenuItem newItem = new JMenuItem(
      new AbstractAction( "New..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          newSourceFile();
        }
      } );
    newItem.setMnemonic( 'N' );
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


    JMenuItem saveAsItem = new JMenuItem(
      new AbstractAction( "Save As..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          saveAs();
        }
      } );
    saveAsItem.setMnemonic( 'A' );
    fileMenu.add( saveAsItem );

    fileMenu.addSeparator();

    JMenuItem classpathItem = new JMenuItem(
      new AbstractAction( "Classpath..." )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          displayClasspath();
        }
      } );
    classpathItem.setMnemonic( 'h' );
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

  public void setProjectSplitPosition( int iPos )
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
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_Y, InputEvent.CTRL_MASK ),
                  "Redo2", new RedoActionHandler() );


    // Old-style undo/redo
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK ),
                  "UndoOldStyle", new UndoActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK ),
                  "RetoOldStyle", new RedoActionHandler() );

    // Run
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ),
                  "Run", new RunActionHandler() );

    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, InputEvent.CTRL_MASK ),
                  "Run", new RunActionHandler() );

    // Clear and Run
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_X, InputEvent.ALT_MASK ),
                  "ClearAndRun", new ClearAndRunActionHandler() );
    mapKeystroke( KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0 ),
                  "ClearAndRun", new ClearAndRunActionHandler() ); // dlank prefers a single keystroke for this action, please leave this unless you need F2 for something else

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
      openFile( fc.getSelectedFile() );
    }
  }

  public void openFile( final File file )
  {
    openFile( makePartId( file ), file );
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
        openFile( partId, file );
      }
    }
    finally
    {
      _initialFile = false;
    }
  }

  private void openFile( IScriptPartId partId, File file )
  {
    if( openTab( file ) )
    {
      return;
    }

    final GosuEditor editor = createEditor();

    if( partId == null )
    {
      partId = new TypelessScriptPartId( "Untitled.gsp" );
    }

    initEditorMode( file, editor );

    file = file == null ? new File( "Untitled.gsp" ) : file;
    editor.putClientProperty( "_file", file );
    removeLruTab();
    String classNameForFile = TypeNameUtil.getClassNameForFile( file );
    IType type = TypeSystem.getByFullNameIfValidNoJava( classNameForFile );
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
      try
      {
        strSource = StreamUtil.getContent( StreamUtil.getInputStreamReader( new FileInputStream( file ) ) );
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
      EventQueue.invokeLater(
        () -> {
          editor.parse();
          editor.getEditor().requestFocus();
        } );
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

    List<ITabHistoryContext> mruList = getTabSelectionHistory().getMruList();
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
    _parentFrame.setTitle( "[" + (file == null ? "Untitled" : file.getAbsolutePath()) + "] - " +
                           "Gosu Editor" );
  }

  private boolean openTab( File file )
  {
    GosuEditor editor = findTab( file );
    if( editor != null )
    {
      _editorTabPane.selectTab( _editorTabPane.findTabWithContent( editor ), true );
      return true;
    }
    return false;
  }

  private GosuEditor findTab( File file )
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
    openFile( file );
  }

  public File getCurrentFile()
  {
    return (File)getCurrentEditor().getClientProperty( "_file" );
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

  private void debug( String s )
  {
    if( DEBUG )
    {
      System.out.println( s );
    }
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
    try
    {
      StreamUtil.copy( new StringReader( editor.getText() ), new FileOutputStream( file ) );
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
//    EventQueue.invokeLater(
//      new Runnable()
//      {
//        public void run()
//        {
//          TypeSystem.getGosuClassLoader().reloadDisposedClasses();
//        }
//      } );
  }

  public boolean saveIfDirty()
  {
    if( isDirty( getCurrentEditor() ) )
    {
      return save();
    }
    return true;
  }

  public void newSourceFile()
  {
    JFileChooser fc = new JFileChooser( getCurrentFile() );
    fc.setDialogTitle( "New Gosu File" );
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
      File selectedFile = fc.getSelectedFile();
      if( isValidGosuSourceFile( selectedFile ) )
      {
        if( selectedFile.exists() )
        {
          MessageDisplay.displayError( "File: " + selectedFile.getName() + " already exists.  Please select a unique name." );
        }
        else
        {
          saveIfDirty();
          createSourceFile( selectedFile );
        }
      }
      else
      {
        MessageDisplay.displayError( "File: " + selectedFile.getName() + " is not a valid Gosu source file name." );
      }
    }
  }

  public void newProject()
  {
    JFileChooser fc = new JFileChooser( getProject().getProjectDir() );
    fc.setDialogTitle( "New Project" );
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
          return "Gosu Project Directory (directory name is your project name)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal != JFileChooser.APPROVE_OPTION )
    {
      return;
    }
    File selectedFile = fc.getSelectedFile();
    Project project = new Project( selectedFile.getName(), selectedFile, this );
    clearTabs();
    EventQueue.invokeLater( () -> restoreProjectState( project ) );
  }

  public void openProject()
  {
    JFileChooser fc = new JFileChooser( getProject().getProjectDir() );
    fc.setDialogTitle( "Open Project" );
    fc.setDialogType( JFileChooser.OPEN_DIALOG );
    fc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
    fc.setMultiSelectionEnabled( false );
    fc.setFileFilter(
      new FileFilter()
      {
        public boolean accept( File f )
        {
          return f.isDirectory() ||
                 (f.isFile() && f.getName().equals( f.getParentFile().getName() + ".prj" ));
        }

        public String getDescription()
        {
          return "Gosu Project (.prj)";
        }
      } );
    int returnVal = fc.showOpenDialog( editor.util.EditorUtilities.frameForComponent( this ) );
    if( returnVal != JFileChooser.APPROVE_OPTION )
    {
      return;
    }
    clearTabs();
    File selectedFile = fc.getSelectedFile();
    if( selectedFile.isFile() )
    {
      EventQueue.invokeLater( () -> restoreProjectState( new Project( selectedFile.getParentFile(), this ) ) );
    }
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

  private void createSourceFile( File selectedFile )
  {
    try
    {
      if( selectedFile.createNewFile() )
      {
        if( !writeStub( selectedFile ) )
        {
          //noinspection ResultOfMethodCallIgnored
          selectedFile.delete();
          return;
        }
      }
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }

    TypeSystem.created( CommonServices.getFileSystem().getIFile( selectedFile ) );
    TypeSystem.refresh( TypeSystem.getGlobalModule() );

    openFile( selectedFile );
  }

  private boolean writeStub( File file )
  {
    String strFile = file.getName().toLowerCase();
    if( strFile.endsWith( ".gs" ) )
    {
      return writeClassStub( file );
    }
    if( strFile.endsWith( ".gsx" ) )
    {
      return writeEnhancementStub( file );
    }
    else if( strFile.endsWith( ".gst" ) )
    {
      return writeTempateStub( file );
    }
    return true;
  }

  private boolean writeClassStub( File file )
  {
    String strName = TypeNameUtil.getClassNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        MessageDisplay.displayError( "A class must have a parent directory" );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "package " + strPackage + eol +
                    eol +
                    "class " + strRelativeName + " {" + eol +
                    eol +
                    "}" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }

  private boolean writeTempateStub( File file )
  {
    String strName = TypeNameUtil.getClassNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        MessageDisplay.displayError( "A template must have a parent directory" );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "<%@ params( myParam: String ) %>" + eol +
                    eol +
                    "The content of my param is: ${myParam}" + eol +
                    eol +
                    "Note you can render this template from a class or program" + eol +
                    "simply by calling one of its render methods:" + eol +
                    eol +
                    "  " + strRelativeName + ".renderToString( \"wow\" )" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
  }

  private int displayTypeWarning( File file )
  {
    return MessageDisplay.displayConfirmation( "<html>The class " + file.getName() + " is not on the current classpath.  " +
                                               "Create the class anyway and put it's parent directory in the classpath?  " +
                                               "<br><br>" +
                                               "WARNING!!!  Ensure that the parent directory does not cover other files and directories you don't want in your class path." +
                                               "<br><br>" +
                                               "Consider creating a \"src\" directory and create package folders in there.", JOptionPane.YES_NO_OPTION );
  }

  private boolean writeEnhancementStub( File file )
  {
    String strName = TypeNameUtil.getClassNameForFile( file );
    if( strName == null )
    {
      int iOption = displayTypeWarning( file );
      if( iOption != JOptionPane.YES_OPTION )
      {
        return false;
      }
      if( file.getParentFile() == null )
      {
        MessageDisplay.displayError( "A class must have a parent directory" );
        return false;
      }
      strName = file.getParentFile().getName() + '.' + file.getName().substring( 0, file.getName().lastIndexOf( '.' ) );
    }
    int iLastDot = strName.lastIndexOf( '.' );
    String strRelativeName = strName.substring( iLastDot + 1 );
    String strPackage = iLastDot > 0 ? strName.substring( 0, iLastDot ) : "";

    try
    {
      FileWriter writer = new FileWriter( file );
      String eol = System.getProperty( "line.separator" );
      writer.write( "package " + strPackage + eol +
                    eol +
                    "enhancement " + strRelativeName + " : Object //## todo: change me " + eol +
                    "{" + eol +
                    eol +
                    "}" );
      writer.flush();
      writer.close();
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    return true;
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

  void execute()
  {
    try
    {
      if( _bRunning )
      {
        return;
      }

      //## todo: Run in separate process (or classloader) so that changes can take effect

      saveAndReloadType( getCurrentFile(), getCurrentEditor() );

      ClassLoader loader = getClass().getClassLoader();
      URLClassLoader runLoader = new URLClassLoader( getAllUrlsAboveGosuclassProtocol( (URLClassLoader)loader ), loader.getParent() );

      TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
      addBusySignal();
      queue.postTask(
        () -> {
          IGosuProgram program = (IGosuProgram)getCurrentEditor().getParsedClass();

          try
          {
            Class<?> runnerClass = Class.forName( "editor.GosuPanel$Runner", true, runLoader );
            try
            {
              String result = (String)runnerClass.getMethod( "run", String.class, List.class ).invoke( null, program.getName(), Gosu.getClasspath() );
              EventQueue.invokeLater(
                () -> {
                  removeBusySignal();
                  if( result != null )
                  {
                    System.out.print( result );
                  }
                } );
            }
            finally {
              GosuClassPathThing.addOurProtocolHandler();
            }
          }
          catch( Exception e )
          {
            throw new RuntimeException( e );
          }
        } );
    }
    catch( Throwable t )
    {
      editor.util.EditorUtilities.handleUncaughtException( t );
    }
  }

  private URL[] getAllUrlsAboveGosuclassProtocol( URLClassLoader loader )
  {
    List<URL> urls = new ArrayList<>();
    boolean bAdd = true;
    for( URL url: loader.getURLs() ) {
      if( bAdd && !url.getProtocol().contains( "gosu" ) ) {
        urls.add( url );
      }
      else {
        bAdd = false;
      }
    }
    return urls.toArray( new URL[urls.size()] );
  }

  public static class Runner
  {
    public static String run( String programName, List<File> classpath )
    {
      Gosu.init( classpath );
      GosuClassPathThing.addOurProtocolHandler();
      GosuClassPathThing.init();
      IGosuProgram program = (IGosuProgram)TypeSystem.getByFullNameIfValid( programName );
      Object result = program.evaluate( null );
      return (String)CommonServices.getCoercionManager().convertValue( result, JavaTypes.STRING() );
    }
  }

  private void addBusySignal()
  {
    _bRunning = true;
    Timer t =
      new Timer( 2000,
                 e -> {
                   //noinspection ConstantConditions
                   if( _bRunning )
                   {
                     _status.setIcon( EditorUtilities.loadIcon( "images/status_anim.gif" ) );
                     _status.setText( "<html>Running <i>" + getCurrentFile().getName() + "</i></html>" );
                     _statPanel.setVisible( true );
                     _statPanel.revalidate();
                   }
                 } );
    t.setRepeats( false );
    t.start();
  }

  private void removeBusySignal()
  {
    if( _bRunning )
    {
      _bRunning = false;
      _statPanel.setVisible( false );
      _statPanel.revalidate();
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

  void clearOutput()
  {
    _resultPanel.clear();
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
    openFile( file );
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
    List<ITabHistoryContext> mruViewsList = new ArrayList<ITabHistoryContext>( getTabSelectionHistory().getMruList() );

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
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          ITabHistoryContext context = (ITabHistoryContext)e.getSource();
          getTabSelectionHistory().getTabHistoryHandler().selectTab( context );
        }
      } );
    popup.show( this, getWidth() / 2 - 100, getHeight() / 2 - 200 );
  }

  public boolean isDirty( GosuEditor editor )
  {
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

  class ClearAndRunActionHandler extends AbstractAction
  {
    public void actionPerformed( ActionEvent e )
    {
      clearOutput();
      new RunActionHandler().actionPerformed( e );
    }
  }

  class RunActionHandler extends AbstractAction
  {
    public RunActionHandler()
    {
      super( "Run" );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
//        CommandLineAccess.setRawArgs( Arrays.asList( _commandLine.split( " +" ) ) );
//        CommandLineAccess.setExitEnabled( false );
        if( getCurrentEditor().isTemplate() )
        {
          executeTemplate();
        }
        else
        {
          execute();
        }
      }
    }

    public boolean isEnabled()
    {
      return getCurrentEditor() != null && !getCurrentEditor().isClass() && !getCurrentEditor().isEnhancement() && !_bRunning;
    }
  }

  class StopActionHandler extends AbstractAction
  {
    public StopActionHandler()
    {
      super( "Stop" );
    }

    public void actionPerformed( ActionEvent e )
    {
      if( isEnabled() )
      {
        TaskQueue queue = TaskQueue.getInstance( "_execute_gosu" );
        TaskQueue.emptyAndRemoveQueue( "_execute_gosu" );
        //noinspection deprecation
        queue.stop();
        removeBusySignal();
      }
    }
  }

  public Clipboard getClipboard()
  {
    return Toolkit.getDefaultToolkit().getSystemClipboard();
  }

  private class SmartMenu extends JMenu implements MenuListener
  {
    public SmartMenu( String strLabel )
    {
      super( strLabel );
      addMenuListener( this );
    }

    @Override
    public void menuSelected( MenuEvent e )
    {
      for( int i = 0; i < getItemCount(); i++ )
      {
        JMenuItem item = getItem( i );
        if( item != null && item.getAction() != null )
        {
          item.setEnabled( item.getAction().isEnabled() );
        }
      }
    }

    @Override
    public void menuDeselected( MenuEvent e )
    {
    }

    @Override
    public void menuCanceled( MenuEvent e )
    {
    }
  }
}
