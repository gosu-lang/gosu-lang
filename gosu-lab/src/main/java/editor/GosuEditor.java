package editor;

import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import editor.util.HTMLEscapeUtil;
import editor.util.IReplaceWordCallback;
import editor.util.LabToolbarButton;
import editor.util.PlatformUtil;
import editor.util.SettleModalEventQueue;
import editor.util.TaskQueue;
import editor.util.TextComponentUtil;
import editor.util.transform.java.JavaToGosu;
import gw.fs.IFile;
import gw.lang.GosuShop;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IImplicitTypeAsExpression;
import gw.lang.parser.expressions.IInferredNewExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoaderListener;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuStringUtil;
import gw.util.StreamUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.tree.TreeModel;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static editor.util.EditorUtilities.handleUncaughtException;
import static editor.util.TextComponentUtil.Direction.BACKWARD;
import static editor.util.TextComponentUtil.Direction.FORWARD;

/**
 * A component for editing Gosu source.
 */
public class GosuEditor extends JPanel implements IScriptEditor, IGosuPanel, ITypeLoaderListener
{
  /**
   * Platform dependent keystroke info
   */
  public static final String CONTROL_KEY_NAME;
  public static final int CONTROL_KEY_MASK;

  static
  {
    if( PlatformUtil.isMac() )
    {
      CONTROL_KEY_MASK = KeyEvent.META_DOWN_MASK;
      CONTROL_KEY_NAME = "meta";
    }
    else
    {
      CONTROL_KEY_MASK = KeyEvent.CTRL_DOWN_MASK;
      CONTROL_KEY_NAME = "control";
    }
  }

  public static final String INTELLISENSE_TASK_QUEUE = "_intellisenseParser";

  /**
   * Parse result code for a valid parse.
   */
  public static final int RESCODE_VALID = 0;

  /**
   * Parse result code for a valid parse with warnings.
   */
  public static final int RESCODE_WARNINGS = 1;

  /**
   * Parse result code for an invalid parse.
   */
  public static final int RESCODE_ERRORS = 2;

  /**
   * Parse result code during parsing.
   */
  public static final int RESCODE_PENDING = 4;

  /**
   * Delay in millis for code completion to wait for key presses
   * before displaying.
   */
  static int COMPLETION_DELAY = 500;

  public static final int MIN_LINENUMBER_WIDTH = 16;

  /**
   * The number of spacess assigned to a tab
   */
  public static final int TAB_SIZE = 2;

  private JLabel _labelCaption;
  private GosuEditorFeedbackPanel _panelFeedback;
  private GosuEditorPane _editor;
  private GosuDocumentHandler _docHandler;
  private IContextMenuHandler<IScriptEditor> _contextMenuHandler;
  private volatile IGosuParser _parser;
  private boolean _bStatement;
  private boolean _bProgram;
  private boolean _bClass;
  private boolean _bEnhancement;
  private boolean _bEmptyTextOk;
  private ISymbolTable _symTable;
  private UndoableEditListener _uel;
  BeanInfoPopup _beanInfoPopup;
  JPopupMenu _valuePopup;
  private LabToolbarButton _btnAdvice;
  private Runnable _adviceRunner;
  private IReplaceWordCallback _replaceWordCallback;
  private boolean _bTemplate;
  private boolean _bCompleteCode;
  private IScriptabilityModifier _scriptabilityModifier;
  private AtomicUndoManager _undoMgr;
  private IScriptPartId _partId;
  private EditorScrollPane _scroller;
  private boolean _bAltDown;
  private ParseResultsException _pe;
  boolean _bEnterPressedConsumed;
  private boolean _bTestResource;
  private boolean _bAcceptUses;
  private int _iTimerCount;
  private boolean _bParserSuspended;
  private IGosuClass _parsedGosuClass;
  private String _enhancedTypeName;
  private Map<Integer, IFunctionStatement> _functionStmtsByLineNumber;
  private volatile List<IDynamicFunctionSymbol> _overriddenFunctions;

  private IGosuValidator _validator;
  private HighlightMode _highlightMode = HighlightMode.SEARCH;
  private IGosuParser.ParseType _parseType;
  private List<ParseListener> _parseListeners = new ArrayList<ParseListener>();

  private SmartFixManager _smartFixManager;
  private ContextHighlighter _ctxHighlighter;
  private DynamicSelectionManager _selectionManager;
  private CodeRefactorManager _codeManager;

  //This rectangle is used to signal that the editor is being used in test mode and that popups should therefore
  //not display
  private static final Rectangle TEST_RECTANGLE = new Rectangle( 0, 0, 0, 0 );

  private List<IDynamicFunctionSymbol> _specialFunctions = new ArrayList<IDynamicFunctionSymbol>();
  private Map<IFunctionSymbol, Runnable> _specialFunctionGotoDeclHandlers = new HashMap<IFunctionSymbol, Runnable>();
  private ITokenizerInstructor _tokenizerInstructor;

  private ITypeUsesMap _typeUsesMap;
  private ITypeUsesMap _typeUsesMapFromMostRecentParse;

  private JavadocPopup _javadocPopup;
  private AbstractPopup _spinnerPopup;

  private static TimerPool _timerPool = new TimerPool();
  private IType _programSuperType;

  private enum HighlightMode
  {
    SEARCH,
    USAGES,
  }

  public GosuEditor( ILineInfoManager lineInfoRenderer,
                     AtomicUndoManager undoMgr,
                     IScriptabilityModifier scriptabilityConstraint,
                     IContextMenuHandler<IScriptEditor> contextMenuHandler,
                     boolean bStatement, boolean bEmptyTextOk )
  {
    _undoMgr = undoMgr;
    _contextMenuHandler = contextMenuHandler == null
                          ? new DefaultContextMenuHandler()
                          : contextMenuHandler;
    _bStatement = bStatement;
    _bEmptyTextOk = bEmptyTextOk;
    _bAcceptUses = true;
    _docHandler = new GosuDocumentHandler();
    _parser = null;
    _scriptabilityModifier = scriptabilityConstraint;

    _replaceWordCallback = new ReplaceWordCallback();
    _parseType = IGosuParser.ParseType.EXPRESSION_OR_PROGRAM;

    _ctxHighlighter = new ContextHighlighter( this );

    configureLayout( lineInfoRenderer );
    _smartFixManager = new SmartFixManager( this );
    _selectionManager = new DynamicSelectionManager( this );
    _codeManager = new CodeRefactorManager( this );
  }

  void configureLayout( ILineInfoManager lineInfoRenderer )
  {
    setBorder( UIManager.getBorder( "TextField.border" ) );
    setLayout( new BorderLayout() );

    _editor = createEditorPane();
    ToolTipManager.sharedInstance().registerComponent( _editor );
    ToolTipManager.sharedInstance().setDismissDelay( 60000 );
    ScrollableEditorRootPane editorRootPane = new ScrollableEditorRootPane( _editor );
    editorRootPane.setContentPane( _editor );
    editorRootPane.setBorder( null );

    GosuEditorKit kit = new GosuEditorKit();
    _editor.setEditorKitForContentType( "text/gosu", kit );
    _editor.setContentType( "text/gosu" );
    _editor.setMargin( new Insets( 3, 3, 3, 3 ) ); // set margin directly, otherwise some other platforms (cough, mac, cough) don't have a margin at all
    _editor.setFont( new Font( GosuEditorKit.getStylePreferences().getFontFamily(), Font.PLAIN,
                               GosuEditorKit.getStylePreferences().getFontSize() ) );
    _editor.setBackground( Scheme.active().getWindow() );
    _editor.setForeground( Color.black );
    _editor.putClientProperty( "caretWidth", 2 );
    _editor.setCaretColor( StyleConstants.getForeground( kit.getViewFactory().getStyle( GosuStyleContext.STYLE_Caret ) ) );
    _editor.setEditable( true );
    _editor.addKeyListener( new EditorKeyHandler() );
    _editor.addMouseListener( new ScriptEditorPopupHandler( this, _contextMenuHandler ) );
    _editor.addMouseWheelListener( new ScriptEditorMouseWheelHandler( this ) );
    _editor.addCaretListener( new ErrorAtCaretHandler() );
    _editor.addCaretListener( _ctxHighlighter );
    _editor.addFocusListener( _ctxHighlighter );
    _editor.addFocusListener( new FocusAdapter()
      {
        @Override
        public void focusGained( FocusEvent e )
        {
          _smartFixManager.updateState();
        }
      } );
    TypeSystem.addTypeLoaderListenerAsWeakRef( this );
    MouseInEditorHandler mouseInEditorHandler = new MouseInEditorHandler( this );
    _editor.addMouseListener( mouseInEditorHandler );
    _editor.addMouseMotionListener( mouseInEditorHandler );
    _editor.addMouseListener( new MouseAdapter()
      {
        @Override
        public void mouseClicked( MouseEvent e )
        {
          setCompleteCode( false );
        }

        @Override
        public void mousePressed( MouseEvent e )
        {
          setCompleteCode( false );
        }

        @Override
        public void mouseReleased( MouseEvent e )
        {
          setCompleteCode( false );
        }
      } );

    addDocumentListener();

    addKeyHandlers();

    // Sets the editor's width such that lines won't word wrap
    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          _editor.setSize( 1000, _editor.getHeight() );
        }
      } );

    _scroller = new EditorScrollPane( lineInfoRenderer, _editor, editorRootPane );
    _scroller.setBorder( null );
    JViewport vp = _scroller.getViewport();
    vp.setScrollMode( JViewport.BLIT_SCROLL_MODE );
    _btnAdvice = new LabToolbarButton( editor.util.EditorUtilities.loadIcon( "images/advice.png" ) );
    _btnAdvice.setToolTipText( "Display Smart Help" );
    _btnAdvice.setBorderConstant( true );
    _btnAdvice.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed( ActionEvent e )
      {
        if( _adviceRunner != null )
        {
          _adviceRunner.run();
        }
      }
    } );
    _btnAdvice.setVisible( false );
    editorRootPane.getLayeredPane().add( _btnAdvice, JLayeredPane.PALETTE_LAYER );

    add( BorderLayout.CENTER, _scroller );

    _labelCaption = new JLabel( "<Script Part>" );
    _labelCaption.setBackground( Scheme.active().getControl() );
    _labelCaption.setOpaque( true );
    _labelCaption.setFont( getFont().deriveFont( Font.BOLD ) );
    _labelCaption.setBorder( new EmptyBorder( 0, 4 + MIN_LINENUMBER_WIDTH * (lineInfoRenderer != null ? 2 : 1), 0, 0 ) );
    add( BorderLayout.NORTH, _labelCaption );

    _panelFeedback = new GosuEditorFeedbackPanel();
    add( BorderLayout.EAST, _panelFeedback );

    _spinnerPopup = new AbstractPopup( getEditor() )
    {
      @Override
      public void setValue( Object value )
      {
      }

      @Override
      public void refresh()
      {
      }

      @Override
      protected void registerListeners()
      {
      }

      @Override
      protected void unregisterListeners()
      {
      }
    };
    _spinnerPopup.setFocusable( false );
    _spinnerPopup.add( new JLabel( EditorUtilities.loadIcon( "com/guidewire/studio/resources/images/wait.gif" ) ) );
  }

  private void addKeyHandlers()
  {
    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " T" ), "_typeInfo" );
    _editor.getActionMap().put( "_typeInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayTypeInfoAtCurrentLocation();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " shift T" ), "_clipCopyTypeInfo" );
    _editor.getActionMap().put( "_clipCopyTypeInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    clipCopyTypeInfoAtCurrentLocation();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " SLASH" ), "_bulkComment" );
    _editor.getActionMap().put( "_bulkComment",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isIntellisensePopupShowing() )
                                    {
                                      handleBulkComment();
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( "TAB" ), "_bulkIndent" );
    _editor.getActionMap().put( "_bulkIndent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isIntellisensePopupShowing() )
                                    {
                                      handleBulkIndent( false );
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( "shift TAB" ), "_bulkOutdent" );
    _editor.getActionMap().put( "_bulkOutdent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isIntellisensePopupShowing() )
                                    {
                                      handleBulkIndent( true );
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " W" ), "_selectWord" );
    _editor.getActionMap().put( "_selectWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    selectWord();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " shift W" ), "_narrowSelectWord" );
    _editor.getActionMap().put( "_narrowSelectWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    narrowSelectWord();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " shift J" ), "_joinLines" );
    _editor.getActionMap().put( "_joinLines",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    joinLines();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " G" ), "_gotoLine" );
    _editor.getActionMap().put( "_gotoLine",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayGotoLinePopup();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " P" ), "_parameterInfo" );
    _editor.getActionMap().put( "_parameterInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isIntellisensePopupShowing() )
                                    {
                                      displayParameterInfoPopup( _editor.getCaretPosition() );
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " B" ), "_declaration" );
    _editor.getActionMap().put( "_declaratino",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    gotoDeclaration();
                                  }
                                } );

    // Add accelerator for Quick Javadoc help
    _editor.getInputMap().put( KeyStroke.getKeyStroke( "F1" ), "_javadocHelp" );
    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " Q" ), "_javadocHelp" );
    _editor.getActionMap().put( "_javadocHelp",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayJavadocHelp( getDeepestLocationAtCaret() );
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( "alt F1" ), "_selectFileInTree" );
    _editor.getActionMap().put( "_selectFileInTree",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    showFileInTree();
                                  }
                                } );

//    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " shift F7" ), "_highlightUsagesInView" );
//    _editor.getActionMap().put( "_highlightUsagesInView",
//                                new AbstractAction()
//                                {
//                                  @Override
//                                  public void actionPerformed( ActionEvent e )
//                                  {
//                                    if (getScriptPart() != null && getScriptPart().getContainingType() != null) {
//                                      _highlightMode = HighlightMode.USAGES;
//                                      highlightUsagesOfFeatureUnderCaret();
//                                    }
//                                  }
//                                } );

//    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "_removeHighlights" );
//    _editor.getActionMap().put( "_removeHighlights",
//                                new AbstractAction()
//                                {
//                                  @Override
//                                  public void actionPerformed( ActionEvent e )
//                                  {
//                                    _highlightMode = HighlightMode.SEARCH;
//                                    removeAllHighlights();
//                                  }
//                                } );
//
//    _editor.getInputMap().put( KeyStroke.getKeyStroke( "F3" ), "_gotoNextHighlight" );
//    _editor.getActionMap().put( "_gotoNextHighlight",
//                                new AbstractAction()
//                                {
//                                  @Override
//                                  public void actionPerformed( ActionEvent e )
//                                  {
//                                    if( _highlightMode == HighlightMode.USAGES )
//                                    {
//                                      gotoNextUsageHighlight();
//                                    }
//                                  }
//                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, CONTROL_KEY_MASK ), "_deleteWord" );
    _editor.getActionMap().put( "_deleteWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteWord();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, CONTROL_KEY_MASK ), "_deleteWordForward" );
    _editor.getActionMap().put( "_deleteWordForward",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteWordForwards();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_Y, CONTROL_KEY_MASK ), "_deleteLine" );
    _editor.getActionMap().put( "_deleteLine",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    deleteLine();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_TAB, KeyEvent.SHIFT_DOWN_MASK ), "_unindent" );
    _editor.getActionMap().put( "_unindent",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    unindent();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " Z" ), "_undo" );
    _editor.getActionMap().put( "_undo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    AtomicUndoManager undoManager = getUndoManager();
                                    if( undoManager.canUndo() )
                                    {
                                      undoManager.undo();
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " alt V" ), "_extractVariable" );
    _editor.getActionMap().put( "_extractVariable",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    extractVariable();
                                  }
                                } );


    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ), "_moveSelectionUp" );
    _editor.getActionMap().put( "_moveSelectionUp",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    moveSelectionUp();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ), "_moveSelectionDown" );
    _editor.getActionMap().put( "_moveSelectionDown",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    moveSelectionDown();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK ), "_smartFix" );
    _editor.getActionMap().put( "_smartFix",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    smartFix();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_L, CONTROL_KEY_MASK ), "_centerView" );
    _editor.getActionMap().put( "_centerView",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    centerView();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( CONTROL_KEY_NAME + " D" ), "_duplicate" );
    _editor.getActionMap().put( "_duplicate",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    duplicate();
                                  }
                                } );

    TextComponentUtil.fixTextComponentKeyMap( _editor );
  }

  public void showFileInTree()
  {
    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    File file = gosuPanel.getCurrentFile();

    TreeModel model = RunMe.getEditorFrame().getGosuPanel().getExperimentView().getTree().getModel();
    FileTree root = (FileTree)model.getRoot();
    FileTree fileTree = root.find( file );
    if( fileTree != null )
    {
      fileTree.select();
    }
  }

  public void centerView()
  {
    try
    {
      Point caretPos = getEditor().modelToView( getEditor().getCaretPosition() ).getLocation();
      Dimension size = getScroller().getBounds().getSize();
      _editor.scrollRectToVisible( new Rectangle( caretPos.x, caretPos.y - size.height / 2, 0, size.height ) );
    }
    catch( BadLocationException e )
    {
      // can't center
    }
  }

  void doSmartHelp()
  {
    if( _adviceRunner != null )
    {
      _adviceRunner.run();
    }
    else
    {
      displayValueCompletionAtCurrentLocation();
    }
  }

  void jumpRight()
  {
    TextComponentUtil.jumpRight( _editor );
  }

  public void displayGotoLinePopup()
  {
    dismissBeanInfoPopup();
    StringPopup popup = new StringPopup( "", "Line number:", getEditor() );
    popup.addNodeChangeListener(
      new ChangeListener()
      {
        @Override
        public void stateChanged( ChangeEvent e )
        {
          try
          {
            int iLine = Integer.parseInt( e.getSource().toString() );
            if( iLine > 0 )
            {
              gotoLine( iLine );
            }
          }
          catch( NumberFormatException nfe )
          {
            // ignore
          }
          getEditor().requestFocus();
          EditorUtilities.fixSwingFocusBugWhenPopupCloses( GosuEditor.this );
          getEditor().repaint();
        }
      } );
    popup.show( this, 0, 0 );
    editor.util.EditorUtilities.centerWindowInFrame( popup, editor.util.EditorUtilities.getWindow() );
  }

  public void gotoLine( int iLine )
  {
    gotoLine( iLine, 0 );
  }
  public void gotoLine( int iLine, int iColumn )
  {
    Element root = _editor.getDocument().getRootElements()[0];
    iLine = root.getElementCount() < iLine ? root.getElementCount() : iLine;
    Element line = root.getElement( iLine - 1 );
    _editor.setCaretPosition( line.getStartOffset() + iColumn );
  }

  public void highlightUsagesOfFeatureUnderCaret()
  {
    highlightUsages( this );
  }

  public void highlightUsages( final IScriptEditor scriptEditor )
  {
    IParseTree deepestLocationAtCaret = scriptEditor.getDeepestLocationAtCaret();
//    if( deepestLocationAtCaret != null )
//    {
//      IParsedElement deepestParsedElementAtCaret = deepestLocationAtCaret.getParsedElement();
//      assert scriptEditor.getScriptPart() != null && scriptEditor.getScriptPart().getContainingType() != null;
//      FeatureInfoRecordFinder localFinder = TypeInfoDatabaseInit.getFeatureInfoRecordFinderForAllLocalFeatures( scriptEditor.getScriptPart().getContainingTypeName() );
//      Set<IFeatureInfoRecord> usages = localFinder.findUsages( deepestParsedElementAtCaret );
//      if( usages != null )
//      {
//        JTextComponent editor = scriptEditor.getEditor();
//        for( IFeatureInfoRecord usage : usages )
//        {
//          TypeInfoDatabaseStudioUtil.highlightRecord( editor, usage );
//        }
//      }
//    }
  }

  public void gotoNextUsageHighlight()
  {
//    if( !isIntellisensePopupShowing() )
//    {
//      JTextComponent editor = getEditor();
//      int caretPosition = editor.getCaretPosition();
//      Highlighter.Highlight[] highlights = editor.getHighlighter().getHighlights();
//      Arrays.sort( highlights, new Comparator<Highlighter.Highlight>()
//      {
//        @Override
//        public int compare( Highlighter.Highlight o1, Highlighter.Highlight o2 )
//        {
//          return o1.getStartOffset() - o2.getStartOffset();
//        }
//      } );
//      int i = -1;
//      do
//      {
//        i++;
//        while( (i < highlights.length) && (highlights[i].getStartOffset() <= caretPosition) )
//        {
//          i++;
//        }
//      } while ( (i < highlights.length) &&
//                !(highlights[i].getPainter() instanceof TypeInfoDatabaseStudioUtil.FindUsageHighlighter));
//
//      if( i == highlights.length )
//      {
//        i = 0;
//      }
//      editor.setCaretPosition( highlights[i].getEndOffset() );
//      editor.moveCaretPosition( highlights[i].getStartOffset() );
//    }
  }

  public void removeAllHighlights()
  {
    if( !isIntellisensePopupShowing() )
    {
      JTextComponent editor = getEditor();
      removeHightlights();
      editor.setCaretPosition( editor.getCaretPosition() );
      hideMiscPopups();
    }
  }

  private void removeHightlights()
  {
    Highlighter highlighter = getEditor().getHighlighter();

    for( Highlighter.Highlight highlight : highlighter.getHighlights() )
    {
      highlighter.removeHighlight( highlight );
    }
  }

  private void hideMiscPopups()
  {
    editor.util.EditorUtilities.hideToolTip( getEditor() );
    _smartFixManager.resetSmartHelpState();
  }

  public void selectWord()
  {
    if( !isIntellisensePopupShowing() && isCaretInEditor() )
    {
      _selectionManager.expandSelection();
    }
  }

  public void selectWordForMouseClick()
  {
    if( !isIntellisensePopupShowing() && isCaretInEditor() )
    {
      _selectionManager.expandSelection( false );
    }
  }

  public void narrowSelectWord()
  {
    if( !isIntellisensePopupShowing() && isCaretInEditor() )
    {
      _selectionManager.reduceSelection();
    }
  }

  private boolean isCaretInEditor()
  {
    // Check selection start. If nothing is selected, getSelectionStart() returns the caret's location.
    return (_editor.getSelectionStart() <= _editor.getDocument().getLength());
  }

  public void joinLines()
  {
    DocumentFilter documentFilter = getGosuDocument().getDocumentFilter();
    if( documentFilter == null || ((SimpleDocumentFilter)documentFilter).acceptEdit( "" ) )
    {
      CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Join Lines" );
      try
      {
        _joinLines();
      }
      finally
      {
        getUndoManager().endUndoAtom( undoAtom );
      }
    }
  }

  private void _joinLines()
  {
    try
    {
      Document document = _editor.getDocument();
      int start = _editor.getSelectionStart();
      int end = _editor.getSelectionEnd();
      if( start == end )
      {
        int i = _editor.getCaret().getDot();
        while( i < document.getLength() )
        {
          if( document.getText( i, 1 ).equals( "\n" ) )
          {
            document.remove( i, 1 );
            swallowSpaces( document, i );
            break;
          }
          i++;
        }
      }
      else
      {
        while( start < end )
        {
          if( document.getText( start, 1 ).equals( "\n" ) )
          {
            document.remove( start, 1 );
            end--;
            int spacesRemoved = swallowSpaces( document, start );
            end -= spacesRemoved;
            start -= spacesRemoved;
          }
          else
          {
            start++;
          }
        }
      }
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private int swallowSpaces( Document document, int i ) throws BadLocationException
  {
    int removedChars = 0;
    while( i < document.getLength() )
    {
      if( document.getText( i, 1 ).equals( " " ) )
      {
        document.remove( i, 1 );
        removedChars++;
      }
      else
      {
        break;
      }
    }
    if( !document.getText( i, 1 ).equals( "." ) && !document.getText( i, 1 ).equals( "#" ) )
    {
      document.insertString( i, " ", null );
      removedChars--;
    }
    return removedChars;
  }

//  public IInheritanceHierarchyPanelAutomator openInheritanceHierarchyDialog()
//  {
//    if( !isIntellisensePopupShowing() )
//    {
//      return new InheritanceHierarchyPanel( GosuEditor.this ).getAutomator();
//    }
//    else
//    {
//      return null;
//    }
//  }
//
//  public IJTreeAutomator openCallHierarchyDialog()
//  {
//    if( !isIntellisensePopupShowing() )
//    {
//      return new CallHierarchyPanel( GosuEditor.this ).getAutomator();
//    }
//    else
//    {
//      return null;
//    }
//  }

  public void gotoDeclaration()
  {
    if( !isIntellisensePopupShowing() )
    {
      gotoDeclarationAtCursor();
    }
  }

  protected GosuEditorPane createEditorPane()
  {
    return new GosuEditorPane( this );
  }

  @Override
  public JComponent getComponent()
  {
    return this;
  }

  @Override
  public SourceType getSourceType()
  {
    if( isProgram() )
    {
      return SourceType.PROGRAM;
    }

    if( isClass() || isEnhancement() )
    {
      return SourceType.CLASS;
    }

    if( isTemplate() )
    {
      return SourceType.TEMPLATE;
    }

    if( isStatement() )
    {
      return SourceType.STATEMENT;
    }

    return SourceType.EXPRESSION;
  }

  @Override
  public String getText()
  {
    return _editor.getText();
  }

  public void setProgram( boolean bProgram )
  {
    _bProgram = bProgram;
  }

  public void setClass( boolean bClass )
  {
    _bClass = bClass;
  }

  public void setEnhancement( boolean bExtension )
  {
    _bEnhancement = bExtension;
  }

  public boolean isProgram()
  {
    return _bProgram;
  }

  public boolean isClass()
  {
    return _bClass;
  }

  public boolean isEnhancement()
  {
    return _bEnhancement;
  }

  public boolean isStatement()
  {
    return _bStatement;
  }

  public void setStatement( boolean bStatement )
  {
    _bStatement = bStatement;
  }

  public void setTemplate( boolean bTemplate )
  {
    _bTemplate = bTemplate;
  }

  public boolean isTemplate()
  {
    return _bTemplate;
  }

  @Override
  public GosuEditorPane getEditor()
  {
    return _editor;
  }

  public IScriptabilityModifier getScriptabilityModifier()
  {
    return _scriptabilityModifier;
  }

  public ParseResultsException getParseResultsException()
  {
    return _pe;
  }

  public boolean hasParseResultsException()
  {
    return _pe != null;
  }

  @Override
  public IScriptPartId getScriptPart()
  {
    return _partId;
  }

  @Override
  public void read( IScriptPartId partId, String strSource ) throws IOException
  {
    _partId = partId;
    _labelCaption.setText( "" );

    AbstractDocument doc = (AbstractDocument)_editor.getDocument();
    if( doc != null )
    {
      doc.removeDocumentListener( _docHandler );
    }

    // Replace the Windows style new-line sequence with just a new-line.
    // Otherwise, the editor thinks new lines are one character
    strSource = GosuStringUtil.replace( strSource, "\r\n", "\n" );

    _editor.read( new StringReader( strSource ), "" );

    addDocumentListener();

    parse();
  }

  public void refresh( String content )
  {
    content = GosuStringUtil.replace( content, "\r\n", "\n" );
    getEditor().setText( content );
    EventQueue.invokeLater( () -> getUndoManager().discardAllEdits() );
  }

  private void addDocumentListener()
  {
    AbstractDocument doc;
    doc = (AbstractDocument)_editor.getDocument();
    if( doc != null )
    {
      doc.addDocumentListener( _docHandler );
      doc.addDocumentListener( _ctxHighlighter );
    }
  }

  public void setTestResource( boolean testResource )
  {
    _bTestResource = testResource;
  }

  @Override
  public void parse()
  {
    parse( false );
  }

  private void parse( boolean forceCodeCompletion )
  {
    postTaskInParserThread( getParseTask( forceCodeCompletion ) );
  }

  public static void postTaskInParserThread( Runnable task )
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    tq.postTask( task );
  }

  public static TaskQueue getParserTaskQueue()
  {
    return TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
  }

  public boolean isParserSuspended()
  {
    return _bParserSuspended;
  }

  public void setParserSuspended( boolean bParserSuspended )
  {
    _bParserSuspended = bParserSuspended;
  }

  private ParseTask getParseTask( boolean forceCodeCompletion )
  {
    return new ParseTask( _editor.getText(), forceCodeCompletion, true );
  }

  public void duplicate()
  {
    DocumentFilter documentFilter = getGosuDocument().getDocumentFilter();
    if( documentFilter == null || ((SimpleDocumentFilter)documentFilter).acceptEdit( "" ) )
    {
      CompoundEdit undoAtom = _undoMgr.getUndoAtom();
      if( undoAtom != null && undoAtom.getPresentationName().equals( "Script Change" ) )
      {
        _undoMgr.endUndoAtom();
      }
      undoAtom = getUndoManager().beginUndoAtom( "Duplicate Line" );
      try
      {
        _duplicate();
      }
      finally
      {
        getUndoManager().endUndoAtom( undoAtom );
      }
    }
  }

  private void _duplicate()
  {
    //No selection, duplicate line
    String selectedText = _editor.getSelectedText();
    if( GosuStringUtil.isEmpty( selectedText ) )
    {
      String currentText = _editor.getText();

      int initialCaretPosition = _editor.getCaretPosition();
      int lineStart = TextComponentUtil.getLineStart( _editor.getText(), initialCaretPosition );
      int lineEnd = TextComponentUtil.getLineEnd( _editor.getText(), initialCaretPosition );

      try
      {
        recordCaretPositionForUndo();
        String insertedLine = "\n" + currentText.substring( lineStart, lineEnd );
        _editor.getDocument().insertString( lineEnd, insertedLine, null );
        if( initialCaretPosition < lineEnd )
        {
          _editor.setCaretPosition( _editor.getCaretPosition() + insertedLine.length() );
          recordCaretPositionForUndo();
        }
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
    else
    {
      try
      {
        int initialSelectionEnd = _editor.getSelectionEnd();
        _editor.getDocument().insertString( initialSelectionEnd, selectedText, null );
        _editor.getCaret().setDot( initialSelectionEnd );
        _editor.getCaret().moveDot( initialSelectionEnd + selectedText.length() );
        recordCaretPositionForUndo();
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private void recordCaretPositionForUndo() throws BadLocationException
  {
    _editor.getDocument().insertString( _editor.getCaretPosition(), "8", null );
    _editor.getDocument().remove( _editor.getCaretPosition() - 1, 1 );
  }

  /**
   * delete the currently selected text, or the current line if nothing is selected
   */
  public void delete()
  {
    TextComponentUtil.expandSelectionIfNeeded( _editor );
    getEditor().replaceSelection( "" );
  }

  /**
   * @return the selected text in the editor, expanding to the entire current line if no selection exists
   */
  public String getExpandedSelection()
  {
    TextComponentUtil.expandSelectionIfNeeded( _editor );
    return getEditor().getSelectedText();
  }

  void deleteWord()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Word" );
    try
    {
      if( !GosuStringUtil.isEmpty( getEditor().getSelectedText() ) )
      {
        delete();
      }
      else
      {
        try
        {
          TextComponentUtil.deleteWordAtCaret( getEditor() );
        }
        catch( BadLocationException e )
        {
          //ignore
        }
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void deleteWordForwards()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Word" );
    try
    {
      if( !GosuStringUtil.isEmpty( getEditor().getSelectedText() ) )
      {
        delete();
      }
      else
      {
        int start = getEditor().getCaretPosition();
        jumpRight();
        int end = getEditor().getCaretPosition();
        getEditor().select( start, end );
        getEditor().replaceSelection( "" );
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void deleteLine()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Delete Line" );
    try
    {
      recordCaretPositionForUndo();
      TextComponentUtil.selectLineAtCaret( _editor );
      getEditor().replaceSelection( "" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void unindent()
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( "Unindent" );
    try
    {
      try
      {
        TextComponentUtil.unindentLineAtCaret( getEditor() );
      }
      catch( BadLocationException e )
      {
        //ignore
      }
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  class ParseTask implements Runnable
  {
    private String _strSource;
    private boolean _forceCodeCompletion;
    private boolean _changed;

    public ParseTask( String strSource, boolean forceCodeCompletion, boolean changed )
    {
      _strSource = strSource;
      _forceCodeCompletion = forceCodeCompletion;
      _changed = changed;
    }

    GosuEditor getEditor()
    {
      return GosuEditor.this;
    }

    @Override
    public void run()
    {
      if( !getEditor().isShowing() )
      {
        return;
      }
      if( !areMoreThanOneParserTasksPendingForThisEditor() || _forceCodeCompletion )
      {
        TypeSystem.lock();
        try
        {
          _parseNow( _strSource, _forceCodeCompletion, _changed );
        }
        finally
        {
          //!! NOTE: do not refresh the type we just parsed in the editor, it will otherwise
          //!!       reparse the type from DISK, which will be stale compared with changes in the editor.
          TypeSystem.unlock();
          if( _forceCodeCompletion )
          {
            setCompleteCode( false );
          }
        }
        for( ParseListener parseListener : _parseListeners )
        {
          parseListener.parseComplete();
        }
      }
    }
  }

  private void _parseNow( String strText, boolean forceCodeCompletion, boolean changed )
  {
    if( isParserSuspended() )
    {
      return;
    }

    try
    {
      if( _parser == null )
      {
        _parser = GosuParserFactory.createParser( getSymbolTable(), _scriptabilityModifier );
        _parser.setThrowParseExceptionForWarnings( true );
        _parser.setDontOptimizeStatementLists( true );
        _parser.setValidator( _validator );
        _parser.setWarnOnCaseIssue( true );
        _parser.setEditorParser( true );
        if( _typeUsesMap != null )
        {
          _parser.setTypeUsesMap( _typeUsesMap );
        }
      }
      else
      {
        if( _parser.getSymbolTable() != getSymbolTable() )
        {
          _parser.setSymbolTable( getSymbolTable() );
        }
      }

      if( changed && getScriptPart() != null && !areMoreThanOneParserTasksGoingToUpdateContainingType() )
      {
        ITypeRef containingType = (ITypeRef)getScriptPart().getContainingType();
        if( containingType != null )
        {
          try
          {
            TypeSystem.refresh( containingType );
          }
          catch( RuntimeException e )
          {
            // eat potential TypeMayHaveBeenDeletedException
          }
        }
      }

      if( !_bEmptyTextOk || (strText != null && strText.length() > 0) )
      {
        _parser.setScript( strText );
        if( getClassType() == ClassType.Template )
        {
          _parser.setTokenizerInstructor( GosuShop.createTemplateInstructor( _parser.getTokenizer() ) );
        }
        if( _parser.getSymbolTable() != null )
        {
          _parser.putDfsDeclsInTable( _parser.getSymbolTable() );
        }
        for( IDynamicFunctionSymbol specialFunction : _specialFunctions )
        {
          _parser.putDfsDeclInSetByName( specialFunction );
        }

        ClassType classType = getClassType();
        if( classType != null )
        {
          // The context here is expected to be a fully qualified class/program/enhancement/template

          _parsedGosuClass = _parser.parseClass( getScriptPart().getContainingTypeName(), new StringSourceFileHandle( getScriptPart().getContainingTypeName(), strText, _bTestResource, classType ), true, true );
        }
        else
        {
          // The context is an anonymous expression/statement e.g., this editor as a small expression field

          if( _bStatement )
          {
            _parser.parseStatements( getScriptPart() );
          }
          else
          {
            IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
            ParserOptions options = new ParserOptions().withParser( _parser ).withSuperType( _programSuperType );
            IParseResult result = programParser.parseExpressionOrProgram( strText, _parser.getSymbolTable(), options );
            _parsedGosuClass = result.getProgram();
            ParseResultsException parseRes = _parsedGosuClass.getParseResultsException();
            if( parseRes != null )
            {
              throw parseRes;
            }
          }
        }
      }
      else if( _parser != null )
      {
        _parser.setScript( "" );
        _parser.setTokenizerInstructor( _tokenizerInstructor );
      }

      clearParseException();
    }
    catch( ParseResultsException ex )
    {
      if( ex.getParsedElement() instanceof IClassFileStatement )
      {
        IClassFileStatement classFileStmt = (IClassFileStatement)ex.getParsedElement();
        IClassStatement classStatement = classFileStmt.getClassStatement();
        if( classStatement != null )
        {
          _parsedGosuClass = classStatement.getGosuClass();
        }
      }
      if( areMoreThanOneParserTasksPendingForThisEditor() && !forceCodeCompletion )
      {
        return;
      }
      _pe = ex;
      handleParseException( forceCodeCompletion );
      return;
    }
    finally
    {
      if( !areMoreThanOneParserTasksPendingForThisEditor() )
      {
        if( _parser != null )
        {
          _functionStmtsByLineNumber = storeFunctionsByLineNumber( _parsedGosuClass, null );
//          if ((TypeInfoDatabaseInit.getExecutor() == null) ||
//              (TypeInfoDatabaseInit.getExecutor().isShutdown())) {
//            storeOverriddenFunctions();
//          } else {
//            TypeInfoDatabaseInit.getExecutor().execute(new Runnable() {
//              @Override
//              public void run() {
//                if ((getScriptPart() != null) &&
//                    (getScriptPart().getContainingTypeName() != null)) {
//                  // No need to check if there are source diff tasks because we are queued after the diff task that got generated for this call to _parseNow()
//                  //(!TypeInfoDatabaseStudioInit.areMoreThanOneSourceDiffTasksPending(getScriptPart().getContainingTypeName()))) {
//                  try
//                  {
//                    storeOverriddenFunctions();
//                  }
//                  catch( Exception e )
//                  {
//                    // mmm mm  good
//                  }
//                }
//              }
//            });
//          }
          _typeUsesMapFromMostRecentParse = _parser.getTypeUsesMap().copy();
          final List<IParseTree> locations = _parser.getLocations();
          EventQueue.invokeLater(
            new Runnable()
            {
              @Override
              public void run()
              {
                if( getGosuDocument().getLocations() == null )
                {
                  _editor.repaint();
                }
                getGosuDocument().setLocations( locations );
                getGosuDocument().setLocationsOffset( _parser.getOffsetShift() );
              }
            } );
        }
      }
    }

    if( areMoreThanOneParserTasksPendingForThisEditor() )
    {
      return;
    }

    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          _panelFeedback.update( RESCODE_VALID, GosuEditor.this );
        }
      } );
  }

  private ClassType getClassType()
  {
    ClassType classType = null;
    if( isTemplate() )
    {
      classType = ClassType.Template;
    }
    else if( isClass() )
    {
      classType = ClassType.Class;
    }
    else if( isProgram() )
    {
      classType = ClassType.Program;
    }
    else if( isEnhancement() )
    {
      classType = ClassType.Enhancement;
    }
    return classType;
  }

//  private ClassLoader setupClassLoaderIfNecessary()
//  {
//    try
//    {
//      IEntityAccess entityAccess = CommonServices.getEntityAccess();
//      if( entityAccess instanceof ShellEntityAccess )
//      {
//        ShellEntityAccess shellEntityAccess = (ShellEntityAccess)entityAccess;
//        ShellClassLoader initialClassLoader = shellEntityAccess.getPluginClassLoader();
//        _currentClassLoader = new ShellClassLoader();
//        List<File> files = Gosu.getPrimordialClasspath();
//        for( File file : files )
//        {
//          initialClassLoader.addURL( file.toURL() );
//        }
//        return initialClassLoader;
//      }
//    }
//    catch( MalformedURLException e )
//    {
//      //ignore
//    }
//    return null;
//  }

  /**
   * @return A copy of the type-uses map from the most recent parse. A copy for thread-safety.
   */
  public ITypeUsesMap getTypeUsesMapFromMostRecentParse()
  {
    return _typeUsesMapFromMostRecentParse;
  }

  private Map<Integer, IFunctionStatement> storeFunctionsByLineNumber( IGosuClass gsClass, Map<Integer, IFunctionStatement> functionStmtsByLineNumber )
  {
    if( gsClass == null || _parsedGosuClass instanceof IGosuEnhancement )
    {
      return null;
    }

    if( functionStmtsByLineNumber == null )
    {
      functionStmtsByLineNumber = new HashMap<Integer, IFunctionStatement>();
    }
    List<IFunctionStatement> functionStatements = new ArrayList<IFunctionStatement>();
    gsClass.getClassStatement().getContainedParsedElementsByType( IFunctionStatement.class, functionStatements );
    for( IFunctionStatement fs : functionStatements )
    {
      functionStmtsByLineNumber.put( fs.getLocation().getLineNum(), fs );
    }
    return functionStmtsByLineNumber;
  }

  private void storeOverriddenFunctions()
  {
//    if( _parsedGosuClass == null || _parsedGosuClass instanceof IGosuEnhancement )
//    {
//      _overriddenFunctions = null;
//      return;
//    }
//
//    List<IDynamicFunctionSymbol> overriddenFunctions = new ArrayList<IDynamicFunctionSymbol>();
//    FeatureInfoRecordFinder finder = TypeInfoDatabaseInit.getFeatureInfoRecordFinder();
//    if( finder != null )
//    {
//      for( IFunctionStatement fs : getFunctionsByLineNumber().values() )
//      {
//        if( !finder.findMethodsThatOverrideThis( fs ).isEmpty() )
//        {
//              overriddenFunctions.add( fs.getDynamicFunctionSymbol() );
//        }
//      }
//    }
//    _overriddenFunctions = overriddenFunctions;
  }

  public Map<Integer, IFunctionStatement> getFunctionsByLineNumber()
  {
    return _functionStmtsByLineNumber == null ? Collections.<Integer, IFunctionStatement>emptyMap() : _functionStmtsByLineNumber;
  }

  public List<IDynamicFunctionSymbol> getOverriddenFunctions()
  {
    return _overriddenFunctions == null ? Collections.<IDynamicFunctionSymbol>emptyList() : _overriddenFunctions;
  }

  protected void clearParseException()
  {
    _pe = null;

    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          getGosuDocument().setParseResultsException( null );
          displayAdvice( null );
        }
      } );
  }

  public void setValidator( IGosuValidator validator )
  {
    _validator = validator;
    if( _parser != null )
    {
      _parser.setValidator( _validator );
    }
  }

  public static boolean areAnyParserTasksPending()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask )
      {
        return true;
      }
    }
    return false;
  }

  private boolean areMoreThanOneParserTasksPendingForThisEditor()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    int iCount = 0;
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask && ((ParseTask)task).getEditor() == this )
      {
        if( iCount > 0 )
        {
          // Note we don't count the first one assuming we're trying to determine
          // if there are any queued behind it.
          return true;
        }
        iCount++;
      }
    }
    return false;
  }

  private boolean areMoreThanOneParserTasksGoingToUpdateContainingType()
  {
    TaskQueue tq = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    int iCount = 0;
    List tasks = tq.getTasks();
    for( Object task1 : tasks )
    {
      Runnable task = (Runnable)task1;
      if( task instanceof ParseTask )
      {
        GosuEditor otherEditor = ((ParseTask)task).getEditor();
        if( otherEditor == this || (otherEditor.getScriptPart() != null && getScriptPart() != null && getScriptPart().getContainingType() == otherEditor.getScriptPart().getContainingType()) )
        {
          if( iCount > 0 )
          {
            // Note we don't count the first one assuming we're trying to determine
            // if there are any queued behind it.
            return true;
          }
          iCount++;
        }
      }
    }
    return false;
  }

  private void handleParseException( final boolean forceCodeCompletion )
  {
    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          handleParseException( _pe, forceCodeCompletion );
        }
      } );
  }

  protected void handleParseException( final ParseResultsException e, final boolean bForceCodeCompletion )
  {
    handleCodeCompletion( bForceCodeCompletion );
    getGosuDocument().setParseResultsException( e );
    if( e != null )
    {
      boolean hasError = false, hasWarning = false;
      for( IParseIssue issue : e.getParseIssues() )
      {
        if( issue.getTokenEnd() > 0 && issue.getTokenStart() < _editor.getDocument().getLength() )
        {
          if( issue instanceof ParseWarning )
          {
            hasWarning = true;
          }
          else
          {
            hasError = true;
          }
        }
      }
      if( !hasError )
      {
        if( hasWarning )
        {
          _panelFeedback.update( RESCODE_WARNINGS, this );
        }
        else
        {
          _panelFeedback.update( RESCODE_VALID, this );
        }
      }
      else
      {
        _panelFeedback.update( RESCODE_ERRORS, this );
      }
    }
  }

  private void handleCodeCompletion( boolean bForceCodeCompletion )
  {
    if( _parser == null )
    {
      return;
    }

    displayAdvice( null );

    //If we are forcing code completion (after a dot) just handle it directly
    if( bForceCodeCompletion )
    {
      handleCompleteCode();
    }
    else
    {
      //## todo:
      //## value completion should be integrated as part of code completion e.g., Enum constants should be in the same popup as members etc.

      handleCompleteValue();
    }
  }

  private void handleCompleteValue()
  {
    List<IParseIssue> errors = getIssuesNearPos( _editor.getCaretPosition() );
    if( errors == null || errors.isEmpty() )
    {
      IParseTree location = IParseTree.Search.getDeepestLocation( _parser.getLocations(), _editor.getCaretPosition() - _parser.getOffsetShift(), false );
      if( location != null && location.getParsedElement().hasParseExceptions() )
      {
        errors = location.getParsedElement().getParseIssues();
      }
      else
      {
        return;
      }
    }

    ParseException e = null;
    IType typeExpected = null;
    for( IParseIssue issue : errors )
    {
      if( issue instanceof ParseException )
      {
        e = (ParseException)issue;
        typeExpected = e.getExpectedType();
        if( typeExpected != null )
        {
          break;
        }
      }
    }
    final ParseException error = e;

    boolean emptyMethodCall = false;
    if( typeExpected == null )
    {
      typeExpected = AbstractParseExceptionResolver.resolvePossibleContextTypesFromEmptyMethodCalls( getExpressionAtCaret(), _editor );
      emptyMethodCall = true;
    }

    if( typeExpected != null )
    {
      String strTypesExpected = ParseResultsException.getExpectedTypeName( typeExpected );
      try
      {
        if( strTypesExpected.length() > 0 && (!TextComponentUtil.getWordBeforeCaret( _editor ).equals( "." ) && !TextComponentUtil.getWordBeforeCaret( _editor ).equals( "#" ))
            && error != null && (emptyMethodCall || error.appliesToPosition( _editor.getCaretPosition() ) ||
                                 (_editor.getCaretPosition() < error.getTokenStart() && _editor.getDocument().getText( _editor.getCaretPosition(), error.getTokenStart() - _editor.getCaretPosition() ).matches( " *" )) ||
                                 (_editor.getCaretPosition() > error.getTokenEnd() && _editor.getDocument().getText( error.getTokenEnd(), _editor.getCaretPosition() - error.getTokenEnd() ).matches( " *" ))) )
        {
          _adviceRunner =
            new Runnable()
            {
              @Override
              public void run()
              {
                setCaretPositionForValueCompletion( error );
                displayValueCompletion( error );
              }
            };
          displayAdvice( e );
        }
      }
      catch( BadLocationException e1 )
      {
        // ignore
      }
    }
  }

  public IGosuParser getParser()
  {
    return _parser;
  }

  GosuDocument getGosuDocument()
  {
    return (GosuDocument)_editor.getDocument();
  }

  @Override
  public EditorScrollPane getScroller()
  {
    return _scroller;
  }

  public IReplaceWordCallback getReplaceWordCallback()
  {
    return _replaceWordCallback;
  }

  @Override
  public ISymbolTable getSymbolTable()
  {
    if( _symTable == null )
    {
      _symTable = new StandardSymbolTable( true );
    }
    return _symTable;
  }

  public void resetSymbolTable( ISymbolTable newSymbols )
  {
    setSymbolTable( newSymbols );
    _parser = null;
    parse();
  }

  public void setSymbolTable( ISymbolTable newSymbols )
  {
    _symTable = newSymbols;
  }

  void handleEnter()
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "New Line" );
    try
    {
      _handleEnter();
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
      undoAtom = _undoMgr.getUndoAtom();
      if( undoAtom != null && undoAtom.getPresentationName().equals( "Script Change" ) )
      {
        _undoMgr.endUndoAtom();
      }
    }
  }

  void _handleEnter()
  {
    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    Element root = _editor.getDocument().getRootElements()[0];
    int index = root.getElementIndex( _editor.getCaretPosition() - 1 );
    Element line = root.getElement( index );
    int iStart = line.getStartOffset();
    int iEnd = line.getEndOffset();
    try
    {
      String strLine = line.getDocument().getText( iStart, iEnd - iStart );
      StringBuilder strbIndent = new StringBuilder();
      for( int iIndent = 0; iIndent < strLine.length(); iIndent++ )
      {
        char c = strLine.charAt( iIndent );
        if( c != ' ' && c != '\t' )
        {
          break;
        }
        strbIndent.append( c );
      }
      if( strbIndent.length() > 0 )
      {
        _editor.replaceSelection( strbIndent.toString() );
      }
      indentIfOpenBracePrecedes( strLine );

      fixCloseBraceIfNecessary( strLine );

      if( strLine.trim().startsWith( "/**" ) )
      {
        _editor.replaceSelection( " * " );
        int caretPos = _editor.getCaretPosition();
        _editor.replaceSelection( "\n" + strbIndent.toString() + " */" );
        _editor.setCaretPosition( caretPos );
      }
      else
      {
        boolean isJavadoc = false;
        while( strLine.trim().startsWith( "*" ) && !strLine.contains( "*/" ) )
        {
          index--;
          if( index >= 0 )
          {
            line = root.getElement( index );
            iStart = line.getStartOffset();
            iEnd = line.getEndOffset();
            strLine = line.getDocument().getText( iStart, iEnd - iStart );
            if( strLine.trim().startsWith( "/**" ) )
            {
              isJavadoc = true;
              break;
            }
          }
        }
        if( isJavadoc )
        {
          _editor.replaceSelection( "* " );
        }
      }
    }
    catch( Exception ex )
    {
      editor.util.EditorUtilities.handleUncaughtException( ex );
    }
  }

  private void fixCloseBraceIfNecessary( String previousLine ) throws BadLocationException
  {
    Element root = _editor.getDocument().getRootElements()[0];
    int iStart = _editor.getCaretPosition();
    Element line = root.getElement( root.getElementIndex( iStart ) );
    int iEnd = line.getEndOffset();
    if( iStart < _editor.getDocument().getLength() )
    {
      String strLine = line.getDocument().getText( iStart, iEnd - iStart );
      if( strLine.trim().startsWith( "}" ) )
      {
        int offset = strLine.indexOf( '}' );
        boolean previousLineWasOpenBrace = previousLine.trim().endsWith( "{" );

        if( previousLineWasOpenBrace )
        {
          _editor.getDocument().insertString( iStart, "\n", null );
          offset += 1;
        }

        parseAndWaitForParser();
        _editor.setCaretPosition( iStart + offset );
        _handleBraceRightNow( _editor.getCaretPosition(), false );

        if( previousLineWasOpenBrace )
        {
          _editor.setCaretPosition( iStart );
        }
      }
    }
  }

  void handleBackspace()
  {
    int caretPosition = getEditor().getCaretPosition();
    try
    {
      if( caretPosition > 0 && (getEditor().getText( caretPosition - 1, 1 ).equals( "." ) || getEditor().getText( caretPosition - 1, 1 ).equals( "#" )) )
      {
        dismissBeanInfoPopup();
        dismissValuePopup();
      }
    }
    catch( BadLocationException e1 )
    {
      // ignore
    }
  }

  private void indentIfOpenBracePrecedes( String strLine )
  {
    strLine = strLine.trim();
    if( strLine.length() > 0 && strLine.charAt( strLine.length() - 1 ) == '{' )
    {
      _editor.replaceSelection( getIndentWhitespace() );
    }
  }

  private String getIndentWhitespace()
  {
    return GosuStringUtil.repeat( " ", TAB_SIZE );
  }

  void handleBulkComment()
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Comment" );
    try
    {
      _handleBulkComment();
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
    }
  }

  void _handleBulkComment()
  {
    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    int iSelectionStart = _editor.getSelectionStart();
    int iSelectionEnd = _editor.getSelectionEnd();
    Element root = _editor.getDocument().getRootElements()[0];
    int iStartIndex = root.getElementIndex( iSelectionStart );
    int iEndIndex = root.getElementIndex( iSelectionEnd );
    if( iStartIndex != iEndIndex && root.getElement( iEndIndex ).getStartOffset() == iSelectionEnd )
    {
      iSelectionEnd--;
      iEndIndex = root.getElementIndex( iSelectionEnd );
    }

    try
    {
      boolean bHasLineWithoutLeadingComment = false;
      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = _editor.getText( iStart, iEnd - iStart );
        String strLineTrimmed = strLine.trim();
        if( strLineTrimmed.length() > 0 && !strLineTrimmed.startsWith( "//" ) )
        {
          bHasLineWithoutLeadingComment = true;
          break;
        }
      }

      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = _editor.getText( iStart, iEnd - iStart );
        if( bHasLineWithoutLeadingComment )
        {
          strLine = "//" + strLine;
        }
        else
        {
          int iCommentIndex = strLine.indexOf( "//" );
          if( iCommentIndex >= 0 )
          {
            strLine = strLine.substring( 0, iCommentIndex ) + strLine.substring( iCommentIndex + 2 );
          }
        }
        iEnd = line.getEndOffset();
        _editor.select( iStart, iEnd );
        _editor.replaceSelection( strLine );
        iSelectionEnd = _editor.getSelectionEnd();
      }
      _editor.select( iSelectionStart, iSelectionEnd );
    }
    catch( Exception ex )
    {
      editor.util.EditorUtilities.handleUncaughtException( ex );
    }
  }

  void handleBulkIndent( boolean bOutdent )
  {
    CompoundEdit atom = getUndoManager().beginUndoAtom( bOutdent ? "Outdent" : "Indent" );
    try
    {
      _handleBulkIndent( bOutdent );
    }
    finally
    {
      getUndoManager().endUndoAtom( atom );
    }
  }

  void _handleBulkIndent( boolean bOutdent )
  {
    String strTabSpaces = getIndentWhitespace();
    int iSelectionStart = _editor.getSelectionStart();
    int iSelectionEnd = _editor.getSelectionEnd();
    if( iSelectionStart == iSelectionEnd )
    {
      _editor.replaceSelection( strTabSpaces );
      return;
    }

    // Calling revalidate after vk_enter to keep the scrollpane, the rootpane, and the editor all in synch.
    revalidate();

    Element root = _editor.getDocument().getRootElements()[0];
    int iStartIndex = root.getElementIndex( iSelectionStart );
    int iEndIndex = root.getElementIndex( iSelectionEnd );
    if( iStartIndex != iEndIndex && root.getElement( iEndIndex ).getStartOffset() == iSelectionEnd )
    {
      iSelectionEnd--;
      iEndIndex = root.getElementIndex( iSelectionEnd );
    }

    try
    {
      for( int i = iStartIndex; i <= iEndIndex; i++ )
      {
        Element line = root.getElement( i );
        int iStart = line.getStartOffset();
        int iEnd = line.getEndOffset();
        String strLine = _editor.getText( iStart, iEnd - iStart );
        if( strLine.trim().length() == 0 )
        {
          continue;
        }
        else if( strLine.length() > 0 && bOutdent )
        {
          if( strLine.startsWith( strTabSpaces ) )
          {
            strLine = strLine.substring( 2 );
          }
          else if( Character.isWhitespace( strLine.charAt( 0 ) ) )
          {
            strLine = strLine.substring( 1 );
          }
        }
        else
        {
          strLine = strTabSpaces + strLine;
        }
        iEnd = line.getEndOffset();
        _editor.select( iStart, iEnd );
        _editor.replaceSelection( strLine );
        iSelectionEnd = _editor.getSelectionEnd();
      }
      _editor.select( iSelectionStart, iSelectionEnd );
    }
    catch( Exception ex )
    {
      editor.util.EditorUtilities.handleUncaughtException( ex );
    }
  }

  void handleBraceRight()
  {
    final int caretPosition = _editor.getCaretPosition();
    EventQueue.invokeLater(
      new Runnable()
      {
        @Override
        public void run()
        {
          postTaskInParserThread(
            new Runnable()
            {
              @Override
              public void run()
              {
                EventQueue.invokeLater(
                  new Runnable()
                  {
                    @Override
                    public void run()
                    {
                      handleBraceRightNow( caretPosition );
                    }
                  } );
              }
            } );
        }
      } );
  }

  private void handleBraceRightNow( int caretPosition )
  {
    CompoundEdit undoAtom = getUndoManager().beginUndoAtom( "Right Brace" );
    try
    {
      _handleBraceRightNow( caretPosition, true );
    }
    finally
    {
      getUndoManager().endUndoAtom( undoAtom );
    }
  }

  private void _handleBraceRightNow( int caretPosition, boolean wasBraceTyped )
  {

    Document doc = _editor.getDocument();
    Element root = doc.getRootElements()[0];
    Element line = root.getElement( root.getElementIndex( caretPosition ) );
    int iBraceLineStart = line.getStartOffset();
    int iBraceLineEnd = line.getEndOffset();
    try
    {
      String strLine = line.getDocument().getText( iBraceLineStart, Math.min( iBraceLineEnd, doc.getLength() ) - iBraceLineStart );
      iBraceLineEnd = strLine.endsWith( "\n" ) ? iBraceLineEnd - 1 : iBraceLineEnd;
      strLine = strLine.trim();
      if( strLine.length() > 1 )
      {
        return;
      }
      IParseTree stmtAtBrace = getDeepestStatementLocationAtPos( caretPosition, true );
      if( stmtAtBrace == null )
      {
        return;
      }

      line = root.getElement( root.getElementIndex( stmtAtBrace.getOffset() ) );
      int iStmtLineStart = line.getStartOffset();
      int iStmtLineEnd = line.getEndOffset();
      strLine = line.getDocument().getText( iStmtLineStart, iStmtLineEnd - iStmtLineStart );
      StringBuilder strbIndent = new StringBuilder();
      for( int iIndent = 0; iIndent < strLine.length(); iIndent++ )
      {
        char c = strLine.charAt( iIndent );
        if( c != ' ' && c != '\t' )
        {
          break;
        }
        strbIndent.append( c );
      }

      String newText = strbIndent.toString() + '}';
      _editor.select( iBraceLineStart, iBraceLineEnd );
      _editor.replaceSelection( newText );

      //restore the caret position if necessary
      if( _editor.getCaretPosition() != caretPosition )
      {
        _editor.setCaretPosition( iBraceLineStart + strbIndent.length() + (wasBraceTyped ? 1 : 0) );
      }

      revalidate();
    }
    catch( Exception e )
    {
      // ignore
    }
  }

  void setCaretPositionForParseIssue( IParseIssue e )
  {
    editor.util.EditorUtilities.settleEventQueue();
    _editor.setCaretPosition( e.getTokenStart() );
  }

  void setCaretPositionForValueCompletion( IParseIssue e )
  {
    editor.util.EditorUtilities.settleEventQueue();
    _editor.setCaretPosition( getCaretPositionForValueCompletion( e ) );
  }

  int getCaretPositionForValueCompletion( IParseIssue e )
  {
    int iErrorPos = e.getSource().getLocation().getOffset();
    int iCaretPos = _editor.getCaretPosition();

    if( getExpressionAtCaret() == null ||
        !AbstractParseExceptionResolver.shouldPositionAtStartOfElement( getExpressionAtCaret().getLocation(), _editor ) )
    {
      return iCaretPos;
    }

    if( iErrorPos <= iCaretPos ||
        TextComponentUtil.isNonWhitespaceBetween( _editor, iCaretPos, iErrorPos ) )
    {
      return iErrorPos;
    }
    return iCaretPos;
  }

  void displayAdvice( ParseException e )
  {
    //Only update the smartfix manager if this editor has focus (it's expensive to do so, and there may be many
    //editors on the screen, e.g. the properties window in the PCF Editor.)
    if( _editor.hasFocus() )
    {
      _smartFixManager.updateState();
    }

    if( !isShowing() || !shouldDisplaySmartHelp( e ) )
    {
      _adviceRunner = null;
      _btnAdvice.setVisible( false );
      return;
    }

    try
    {
      _btnAdvice.setVisible( true );
      _btnAdvice.setSize( _btnAdvice.getPreferredSize() );
      int iPos = getCaretPositionForValueCompletion( e );
      Rectangle rcAdvice = getPositionFromPoint( iPos );
      rcAdvice = SwingUtilities.convertRectangle( _editor, rcAdvice, _btnAdvice.getParent() );
      if( rcAdvice != null )
      {
        int adviceY;
        Rectangle visibleRect = getEditor().getVisibleRect();
        if( rcAdvice.y + rcAdvice.height + _btnAdvice.getHeight() > visibleRect.y + visibleRect.height )
        {
          adviceY = rcAdvice.y - _btnAdvice.getHeight();
        }
        else
        {
          adviceY = rcAdvice.y + rcAdvice.height;
        }
        _btnAdvice.setLocation( rcAdvice.x, adviceY );
      }
      else
      {
        _adviceRunner = null;
        _btnAdvice.setVisible( false );
      }
    }
    catch( Throwable t )
    {
      editor.util.EditorUtilities.handleUncaughtException( "ElementModel change listener error.", t );
    }
  }

  public AtomicUndoManager getUndoManager()
  {
    return _undoMgr;
  }

  /**
   * Sets the one and only undoable edit listener for this editor section.
   * The primary use case for this method is to establish an undo manager
   * connection.
   *
   * @param uel The UndoableEditListener to connect to this section's document.
   */
  public void setUndoableEditListener( UndoableEditListener uel )
  {
    if( _uel != null )
    {
      _editor.getDocument().removeUndoableEditListener( _uel );
    }

    _uel = uel;

    if( _uel != null )
    {
      _editor.getDocument().addUndoableEditListener( _uel );
    }
  }

  @Override
  public void gotoNextError()
  {
    @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
    ParseResultsException pre = getParseResultsException();
    if( pre == null )
    {
      return;
    }
    int iPos = _editor.getCaretPosition();
    List<IParseIssue> pes = pre.getParseExceptions();
    IParseIssue first = null;
    for( IParseIssue pe : pes )
    {
      if( pe.getTokenStart() > iPos && (first == null || pe.getTokenStart() < first.getTokenStart()) )
      {
        first = pe;
      }
    }
    if( first != null )
    {
      setCaretPositionForParseIssue( first );
    }
  }

  @Override
  public void handleCompleteCode()
  {
    setCompleteCode( true );
    postTaskInParserThread( new Runnable()
    {
      @Override
      public void run()
      {
        if( isCompleteCode() )
        {
          try
          {
            final ISymbolTable atCursor = getSymbolTableAtCursor();
            SwingUtilities.invokeLater( new Runnable()
            {
              @Override
              public void run()
              {
                if( isCompleteCode() )
                {
                  try
                  {
                    handleDot( atCursor );
                  }
                  finally
                  {
                    setCompleteCode( false );
                  }
                }
              }
            } );
          }
          catch( RuntimeException e )
          {
            setCompleteCode( false );
            throw e;
          }
        }
      }
    } );
  }

  void setCompleteCode( final boolean bCompleteCode )
  {
    _bCompleteCode = bCompleteCode;
    SwingUtilities.invokeLater( new Runnable()
    {
      @Override
      public void run()
      {
        if( bCompleteCode )
        {
          Rectangle rcCaretBounds;
          try
          {
            rcCaretBounds = getPositionFromPoint( getEditor().getCaretPosition() );
          }
          catch( BadLocationException e )
          {
            throw new RuntimeException( e );
          }
          if( rcCaretBounds != TEST_RECTANGLE && !isIntellisensePopupShowing() )
          {
            _spinnerPopup.show( _editor, rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
          }
        }
        else
        {
          _spinnerPopup.setVisible( false );
        }
      }
    } );
  }

  boolean isCompleteCode()
  {
    return _bCompleteCode;
  }

  void handleDot()
  {
    // The completion delay is here mostly to prevent doing path completion during undo/redo.
    runIfNoKeyPressedInMillis( COMPLETION_DELAY,
                               new Runnable()
                               {
                                 @Override
                                 public void run()
                                 {
                                   setCompleteCode( true );
                                   parse( true );
                                   //handleDot( (ISymbolTable)null );
                                 }
                               } );
  }

  void handleColon()
  {
    // The completion delay is here mostly to prevent doing path completion during undo/redo.
    runIfNoKeyPressedInMillis( COMPLETION_DELAY,
                               new Runnable()
                               {
                                 @Override
                                 public void run()
                                 {
                                   setCompleteCode( true );
                                   parse( true );
                                   //handleDot( (ISymbolTable)null );
                                 }
                               } );
  }

  void handleDot( final ISymbolTable transientSymTable )
  {
    if( transientSymTable == null )
    {
      return;
    }

    if( isIntellisensePopupShowing() )
    {
      // Don't clobber the code completion popup
      return;
    }

    // Hide advice button while handling dot
    displayAdvice( null );

    String strWordAtCaret = TextComponentUtil.getWordAtCaret( _editor );
    if( strWordAtCaret == null || strWordAtCaret.length() == 0 || !Character.isLetterOrDigit( strWordAtCaret.charAt( 0 ) ) )
    {
      strWordAtCaret = TextComponentUtil.getWordBeforeCaret( _editor );
    }

    if( isCompleteCode() || strWordAtCaret.equals( "." ) || strWordAtCaret.equals( "#" ) || strWordAtCaret.equals( ":" ) )
    {
      dismissValuePopup();
      handleDotNow( transientSymTable );
    }
  }

  void handleDotNow( ISymbolTable transientSymTable )
  {
    dismissBeanInfoPopup();
    displayPathCompletion( transientSymTable );
  }

  void displayPathCompletion( final ISymbolTable transientSymTable )
  {
    if( _bTemplate )
    {
      transientSymTable.putSymbol( GosuShop.createSymbol( ITemplateGenerator.PRINT_METHOD, new FunctionType( ITemplateGenerator.PRINT_METHOD, JavaTypes.pVOID(), new IType[]{JavaTypes.STRING(), JavaTypes.pBOOLEAN()} ), null ) );
    }
    PathCompletionIntellisense.instance().complete( this, transientSymTable );
  }

  void displayPathCompletionBeanInfoPopup( final boolean bFeatureLiteralCompletion )
  {
    if( _beanInfoPopup.isDOA() )
    {
      return;
    }

    _beanInfoPopup.addNodeChangeListener( new ChangeListener()
    {
      @Override
      public void stateChanged( ChangeEvent e )
      {
        CompoundEdit undoAtom = _undoMgr.getUndoAtom();
        if( undoAtom != null && undoAtom.getPresentationName().equals( "Script Change" ) )
        {
          _undoMgr.endUndoAtom();
        }
        undoAtom = getUndoManager().beginUndoAtom( "Code Completion" );
        try
        {
          BeanTree beanInfoSelection = (BeanTree)e.getSource();
          String strRef = beanInfoSelection.makePath( bFeatureLiteralCompletion );
          try
          {
            int dotPosition = _editor.getCaretPosition() - 1;
            String s = _editor.getText();
            dotPosition = TextComponentUtil.findCharacterPositionOnLine( dotPosition, s, '.', BACKWARD );
            if( dotPosition == -1 )
            {
              dotPosition = TextComponentUtil.findCharacterPositionOnLine( _editor.getCaretPosition() - 1, s, '#', BACKWARD );
              if( dotPosition == -1 )
              {
                //the user must have moved out of the path's line, so we cannot complete
                return;
              }
            }

            _editor.getDocument().remove( dotPosition + 1, _editor.getCaretPosition() - (dotPosition + 1) );
            _editor.getDocument().insertString( _editor.getCaretPosition(), strRef, null );

            // If we are handling a typed dot, no fixing up stuff
            if( e instanceof BeanInfoPopup.DotWasTypedChangeEvent )
            {
              return;
            }

            //If this is a method we are inserting, select the first argument position within it
            if( strRef.contains( "(" ) )
            {
              String text = _editor.getText();
              int wordStart = TextComponentUtil.findCharacterPositionOnLine( dotPosition, text, '(', FORWARD );
              int closeParen = TextComponentUtil.findCharacterPositionOnLine( dotPosition, text, ')', FORWARD );
              while( !Character.isJavaIdentifierPart( text.charAt( wordStart ) ) && wordStart < closeParen )
              {
                wordStart++;
              }
              if( wordStart != closeParen )
              {
                _editor.setCaretPosition( wordStart );
                TextComponentUtil.selectWordAtCaret( _editor );
              }
            }

            //If we completed halfway through another word, insert a dot for the developer
            if( Character.isJavaIdentifierPart( _editor.getDocument().getText( _editor.getCaretPosition(), 1 ).charAt( 0 ) ) )
            {
              _editor.getDocument().insertString( _editor.getCaretPosition(), ".", null );
            }
          }
          catch( BadLocationException ble )
          {
            throw new RuntimeException( ble );
          }
          _editor.requestFocus();
          EditorUtilities.fixSwingFocusBugWhenPopupCloses( GosuEditor.this );
          _editor.repaint();
        }
        finally
        {
          getUndoManager().endUndoAtom( undoAtom );
        }
      }
    } );
    displayBeanInfoPopup( _editor.getCaretPosition() );
  }

  void displayValueCompletion( ParseException pe )
  {
    ParseExceptionIntellisense.instance().resolve( this, pe );
  }

  @Override
  public boolean displayValueCompletionAtCurrentLocation()
  {
    return ValueCompletionIntellisense.instance().complete( this );
  }

  public void clipCopyTypeInfoAtCurrentLocation()
  {
    IType type = getTypeAtCaretPosition();
    if( type == null )
    {
      return;
    }
    RunMe.getEditorFrame().getGosuPanel().getClipboard().setContents( new StringSelection( type.getName() ), null );
  }

  public void displayTypeInfoAtCurrentLocation()
  {
    IType type = getTypeAtCaretPosition();
    IExpression expr = getExpressionAtCaret();
    if( expr != null )
    {
      if( expr.getParent() instanceof IImplicitTypeAsExpression )
      {
        type = ((IImplicitTypeAsExpression)expr.getParent()).getType();
      }
    }
    final IType typeAtCursor = type;

    final JPopupMenu popup = new JPopupMenu();

    String displayHTML;
    boolean foundType = true;
    if( typeAtCursor != null )
    {
      if( typeAtCursor instanceof IMetaType )
      {
        displayHTML = "<html><b>" + "Type" + ":</b>&nbsp;" + "Type" + "&lt;<i>" + HTMLEscapeUtil.escape( ((IMetaType)type).getType().getName() ) + "</i>&gt;</html>";
      }
      else
      {
        displayHTML = "<html><b>" + "Type" + ":</b>&nbsp;" + HTMLEscapeUtil.escape( type.getName() ) + "</html>";
      }
    }
    else
    {
      displayHTML = "No type found at point";
      foundType = false;
    }

    final JLabel label = new JLabel( displayHTML );
    label.setFocusable( true );
    JPanel panel = new JPanel( true );
    panel.setBackground( Scheme.active().getTooltipBackground() );
    panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS ) );
    panel.setBorder( BorderFactory.createEmptyBorder( 1, 3, 1, 3 ) );
    panel.add( label );
    if( foundType )
    {
      JButton copyBtn = new JButton( "copy" );
      final IType type1 = type;
      copyBtn.setAction( new AbstractAction( "copy" )
      {
        @Override
        public void actionPerformed( ActionEvent e )
        {
          editor.util.EditorUtilities.getClipboard().setContents( new StringSelection( type1.getName() ), null );
          CopyBuffer.instance().captureState();
        }
      } );
      copyBtn.setFont( copyBtn.getFont().deriveFont( 10f ) );
      panel.add( Box.createHorizontalStrut( 4 ) );
      panel.add( copyBtn );
    }
    popup.add( panel );

    try
    {
      Rectangle rectangle = getCaretLocation();
      popup.show( this, rectangle.x + 15, rectangle.y - 20 );
      EventQueue.invokeLater(
        new Runnable()
        {
          @Override
          public void run()
          {
            label.requestFocus();
            label.addKeyListener(
              new KeyAdapter()
              {
                @Override
                public void keyPressed( KeyEvent e )
                {
                  label.removeKeyListener( this );
                  popup.setVisible( false );
                }
              } );
          }
        } );
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  public Rectangle getCaretLocation() throws BadLocationException
  {
    Rectangle rectangle = getEditor().modelToView( getEditor().getCaretPosition() );
    return SwingUtilities.convertRectangle( getEditor(), rectangle, this );
  }

  public void extractVariable()
  {
    _codeManager.extractVariable();
  }

  public IType getTypeAtCaretPosition()
  {
    //if we are not at a word, in the editor, do nothing and return false
    String atCaret = TextComponentUtil.getWordAtCaret( getEditor() );
    if( GosuStringUtil.isEmpty( atCaret ) )
    {
      return null;
    }

    // Get the deepest location at the caret.
    IParseTree deepest = getDeepestLocationAtCaret();
    if( deepest == null )
    {
      return null;
    }

    IParsedElement parsedElement = deepest.getParsedElement();

    if( parsedElement instanceof IClassDeclaration )
    {
      return ((IClassDeclaration)parsedElement).getGSClass();
    }
    else if( parsedElement instanceof IExpression )
    {
      return ((IExpression)parsedElement).getType();
    }
    else if( parsedElement instanceof IVarStatement )
    {
      //TODO cgross - hack to be removed when we begin parsing the declarations within var statements
      IVarStatement varStmt = (IVarStatement)parsedElement;
      IParseTree location = varStmt.getLocation();
      int caret = getEditor().getCaretPosition();
      if( caret > location.getOffset() + Keyword.KW_var.length() && location.areAllChildrenAfterPosition( caret ) )
      {
        return varStmt.getType();
      }
    }
    else if( parsedElement instanceof IForEachStatement )
    {
      //TODO cgross - hack to be removed when we begin parsing the declarations within var statements
      IForEachStatement feStmt = (IForEachStatement)parsedElement;
      IParseTree location = feStmt.getLocation();
      int caret = getEditor().getCaretPosition();
      if( caret > location.getOffset() + Keyword.KW_for.length() && location.areAllChildrenAfterPosition( caret ) )
      {
        return feStmt.getIdentifier().getType();
      }
    }

    return null;
  }

  void replaceLocationAtCaret( String strReplacement )
  {
    IParseTree deepest = getDeepestLocationAtCaret();
    if( deepest == null )
    {
      return;
    }

    replaceLocation( deepest, strReplacement );
  }

  void replaceLocation( IParseTree location, String strReplacement )
  {
    _editor.setCaretPosition( location.getOffset() );
    _editor.moveCaretPosition( location.getOffset() + location.getLength() );
    _editor.replaceSelection( strReplacement );
  }

  boolean shouldDisplaySmartHelp( ParseException pe )
  {
    return pe != null && ParseExceptionIntellisense.instance().canResolve( this, pe );
  }

  IExpression getExpressionBeforeCaret()
  {
    return getExpressionAtPos( _editor.getCaretPosition() - 1 );
  }

  IExpression getExpressionAtCaret()
  {
    return getExpressionAtPos( _editor.getCaretPosition() );
  }

  IExpression getExpressionAtPos( int iPos )
  {
    IParseTree locationBeforeDot = IParseTree.Search.getDeepestLocation( _parser.getLocations(), iPos - _parser.getOffsetShift(), true );

    if( locationBeforeDot != null )
    {
      IParsedElement pe = locationBeforeDot.getParsedElement();
      if( pe instanceof IExpression )
      {
        return (IExpression)pe;
      }
    }
    return null;
  }

  int getPositionAtStartOfExpressionAtCaret()
  {
    IParseTree locationBeforeDot = IParseTree.Search.getDeepestLocation( _parser.getLocations(), _editor.getCaretPosition() - _parser.getOffsetShift(), true );
    return locationBeforeDot.getOffset();
  }

  public IExpression getExpressionContainingCharacterBeforeCaret()
  {
    int iPos = _editor.getCaretPosition() - _parser.getOffsetShift();
    IParseTree locationBeforeDot = IParseTree.Search.getDeepestLocation( _parser.getLocations(), iPos - 1, iPos, true );

    if( locationBeforeDot != null )
    {
      IParsedElement pe = locationBeforeDot.getParsedElement();
      if( pe instanceof IExpression )
      {
        return (IExpression)pe;
      }
    }
    return null;
  }

  public ISymbolTable getSymbolTableAtCursor()
  {
    int iCaretPos = _editor.getCaretPosition();
    StringBuffer sb = new StringBuffer( _editor.getText() );
    sb.insert( iCaretPos, " +yennikcm ;" ); // Force a parse exception
    IGosuParser parserJavadoc = GosuParserFactory.createParser( getSymbolTable(), _scriptabilityModifier );
    parserJavadoc.setEditorParser( true );

    parserJavadoc.setScript( sb.toString() );
    try
    {
//      TypeSystem.refresh();
      TypeSystem.lock();
      try
      {
        if( isProgram() )
        {
          IGosuProgramParser programParser = GosuParserFactory.createProgramParser();
          ParserOptions options = new ParserOptions().withParser( parserJavadoc );
          IParseResult result = programParser.parseExpressionOrProgram( sb.toString(), parserJavadoc.getSymbolTable(), options );
          IGosuClass parsedGosuClass = result.getProgram();
          ParseResultsException parseResultsException = parsedGosuClass.getParseResultsException();
          if( parseResultsException != null )
          {
            throw parseResultsException;
          }
        }
        else if( isClass() )
        {
          // The context here is expected to be the fully qualified class name
          parserJavadoc.parseClass( getScriptPart().getContainingTypeName(), new StringSourceFileHandle( getScriptPart().getContainingTypeName(), sb, _bTestResource, ClassType.Class ), true, true );
        }
        else if( isEnhancement() )
        {
          // The context here is expected to be the fully qualified class name
          parserJavadoc.parseClass( getScriptPart().getContainingTypeName(), new StringSourceFileHandle( getScriptPart().getContainingTypeName(), sb, _bTestResource, ClassType.Enhancement ), true, true );
        }
        else if( _bStatement )
        {
          parserJavadoc.parseStatements( getScriptPart() );
        }
        else
        {
          parserJavadoc.parseExpOrProgram( getScriptPart() );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    catch( ParseResultsException pe )
    {
      List<IParseIssue> errors = pe.getIssuesFromPos( iCaretPos + 2 );
      if( errors.isEmpty() )
      {
        errors = pe.getIssuesFromPos( iCaretPos );
      }
      if( !errors.isEmpty() )
      {
        for( IParseIssue error : errors )
        {
          if( error.getSymbolTable() != null )
          {
            return error.getSymbolTable();
          }
        }
      }
    }
    return getSymbolTable();
  }

  protected void displayBeanInfoPopup( int iPosition )
  {
    if( !_editor.isShowing() )
    {
      return;
    }

    if( isResultOfUndo() )
    {
      return;
    }

    try
    {
      dismissValuePopup();

      Rectangle rcBounds = getPositionFromPoint( iPosition );
      if( rcBounds != TEST_RECTANGLE )
      {
        _beanInfoPopup.show( _editor, rcBounds.x, rcBounds.y + rcBounds.height );
      }

      EventQueue.invokeLater( new Runnable()
      {
        @Override
        public void run()
        {
          _editor.requestFocus();
          _editor.repaint();
        }
      } );
    }
    catch( BadLocationException e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
    }
  }

  private Rectangle getPositionFromPoint( int iPosition )
    throws BadLocationException
  {
    Rectangle rectangle = _editor.modelToView( iPosition );
    if( rectangle == null )
    {
      rectangle = TEST_RECTANGLE;
    }
    return rectangle;
  }

  private boolean isResultOfUndo()
  {
    return Math.abs( getUndoManager().getLastUndoTime() - System.currentTimeMillis() ) < 400;
  }

  void displayValuePopup()
  {
    displayValuePopup( getEditor().getCaretPosition() );
  }

  void displayValuePopup( int iPosition )
  {
    displayValuePopup( iPosition, true );
  }

  void displayValuePopup( int iPosition, boolean bFocusInEditor )
  {
    try
    {
      dismissBeanInfoPopup();

      Rectangle rcCaretBounds = getPositionFromPoint( iPosition );
      if( rcCaretBounds != TEST_RECTANGLE )
      {
        _valuePopup.show( _editor, rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
      }

      if( bFocusInEditor )
      {
        EventQueue.invokeLater( new Runnable()
        {
          @Override
          public void run()
          {
            _editor.requestFocus();
            _editor.repaint();
          }
        } );
      }
    }
    catch( BadLocationException e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
    }
  }

  ParameterInfoPopup displayParameterInfoPopup( int iPosition )
  {
    return ParameterInfoPopup.invoke( this, iPosition );
  }

  void dismissBeanInfoPopup()
  {
    if( _beanInfoPopup != null )
    {
      _beanInfoPopup.setVisible( false );
      _beanInfoPopup = null;
    }
  }

  public BeanInfoPopup getBeanInfoPopup()
  {
    return _beanInfoPopup;
  }

  public void setBeanInfoPopup( BeanInfoPopup beanInfoPopup )
  {
    _beanInfoPopup = beanInfoPopup;
  }

  public IValuePopup getValuePopup()
  {
    return (IValuePopup)_valuePopup;
  }

  public void setValuePopup( IValuePopup valuePopup )
  {
    _valuePopup = (JPopupMenu)valuePopup;
  }

  public boolean isIntellisensePopupShowing()
  {
    return (_beanInfoPopup != null && _beanInfoPopup.isShowing()) ||
           (_valuePopup != null && _valuePopup.isShowing());
  }

  private void dismissValuePopup()
  {
    if( _valuePopup != null )
    {
      _valuePopup.setVisible( false );
      _valuePopup = null;
    }
  }

  @Override
  public void gotoDeclarationAtCursor()
  {
    IParseTree deepestParseTree = getDeepestLocationAtCaret();
    if( deepestParseTree == null )
    {
      return;
    }

    IParsedElement deepestParsedElementAtCaret = deepestParseTree.getParsedElement();
    gotoReference( deepestParsedElementAtCaret );
  }

  private void gotoReference( IParsedElement pe )
  {
    int prevCaretPos = getEditor().getCaretPosition();

    if( pe instanceof IMethodCallExpression )
    {
      IFunctionSymbol fs = ((IMethodCallExpression)pe).getFunctionSymbol();
      if( fs instanceof IDynamicFunctionSymbol )
      {
        handleGotoFeature( ((IDynamicFunctionSymbol)fs).getMethodOrConstructorInfo() );
      }
    }
    else if( pe instanceof IBeanMethodCallExpression )
    {
      handleGotoFeature( ((IBeanMethodCallExpression)pe).getMethodDescriptor() );
    }
    else if( pe instanceof IMemberAccessExpression )
    {
      handleGotoFeature( ((IMemberAccessExpression)pe).getPropertyInfo() );
    }
    else if( pe instanceof IIdentifierExpression )
    {
      ISymbol symbol = ((IIdentifierExpression)pe).getSymbol();
      if( symbol instanceof IDynamicPropertySymbol )
      {
        handleGotoFeature( ((IDynamicPropertySymbol)symbol).getPropertyInfo() );
      }
      else if( symbol instanceof IDynamicSymbol )
      {
        handleGotoFeature( symbol.getGosuClass().getTypeInfo().getProperty( symbol.getGosuClass(), symbol.getName() ) );
      }
      else if( symbol.isLocal() )
      {
        handleGotoLocal( symbol, pe );
      }
    }
    else if( pe instanceof ITypeLiteralExpression )
    {
      //## todo: handle when type literal is a constructor call

      handleGotoFeature( ((ITypeLiteralExpression)pe).getType().getType().getTypeInfo() );
    }
    else
    {
      // If not found, the best thing we can do is display some info on the element
      displayJavadocHelp( pe.getLocation() );
    }

    GosuPanel gosuPanel = RunMe.getEditorFrame().getGosuPanel();
    GosuEditor currentEditor = gosuPanel.getCurrentEditor();
    int currentCaretPos = currentEditor.getEditor().getCaretPosition();
    if( currentEditor == this && currentCaretPos != prevCaretPos )
    {
      // Only need to handle navigation within current file,
      // jumps to other files will be caught as tab selection change events

      gosuPanel.getTabSelectionHistory().addNavigationHistory( this, prevCaretPos, currentCaretPos );
    }
  }

  @Override
  public String getSelectedText()
  {
    return _editor.getSelectedText();
  }

  public void handleGotoFeature( IFeatureInfo feature )
  {
    if( feature == null )
    {
      return;
    }

    IType ownersType = feature.getOwnersType();
    if( !(ownersType instanceof IGosuClass) )
    {
      return;
    }

    IGosuClass gsClass = (IGosuClass)ownersType;
    if( IGosuClass.ProxyUtil.isProxy( gsClass ) )
    {
      return;
    }

    int offset = 0;

    if( feature instanceof IGosuMethodInfo )
    {
      List<IParsedElementWithAtLeastOneDeclaration> res = new ArrayList<>();
      gsClass.getClassStatement().getContainedParsedElementsByType( IParsedElementWithAtLeastOneDeclaration.class, res );
      for( IParsedElementWithAtLeastOneDeclaration fs: res )
      {
        if( ((IGosuMethodInfo)feature).isMethodForProperty() &&
            fs instanceof IVarStatement && ((IVarStatement)fs).hasProperty() &&
            ((IVarStatement)fs).hasProperty() &&
            ((IVarStatement)fs).getPropertyName().equals( feature.getDisplayName().substring( 1 ) ) )
        {
          offset = fs.getNameOffset( ((IVarStatement)fs).getPropertyName() );
          break;
        }

        if( fs instanceof IFunctionStatement && feature.equals( ((IFunctionStatement)fs).getDynamicFunctionSymbol().getMethodOrConstructorInfo() )||
            fs instanceof IPropertyStatement && feature.equals( ((IPropertyStatement)fs).getPropertyGetterOrSetter().getDynamicFunctionSymbol().getMethodOrConstructorInfo() ) )
        {
          offset = fs.getNameOffset( feature.getName() );
          break;
        }
      }
    }
    else if( feature instanceof IGosuPropertyInfo )
    {
      handleGotoFeature( ((IGosuPropertyInfo)feature).getReadMethodInfo() );
      return;
    }
    else if( feature instanceof IGosuVarPropertyInfo )
    {
      IGosuVarPropertyInfo varProp = (IGosuVarPropertyInfo)feature;
      offset = varProp.getOffset();
    }
    else if( feature instanceof ITypeInfo )
    {
      IGosuClass targetClass = ((IGosuClassTypeInfo)feature).getGosuClass();
      IClassDeclaration classDeclaration = targetClass.getClassStatement().getClassDeclaration();
      offset = classDeclaration == null ? 0 : classDeclaration.getNameOffset( null );
    }

    if( gsClass != getParsedClass() )
    {
      IFile sourceFile = gsClass.getSourceFileHandle().getFile();
      if( sourceFile != null && sourceFile.isJavaFile() )
      {
        RunMe.getEditorFrame().getGosuPanel().openFile( sourceFile.toJavaFile(), true );
        SettleModalEventQueue.instance().run();
      }
    }
    RunMe.getEditorFrame().getGosuPanel().getCurrentEditor().getEditor().setCaretPosition( offset );
  }

  public void handleGotoLocal( ISymbol symbol, IParsedElement pe )
  {
    IParsedElement functionAtCaret = getRootParsedElement();
    if( functionAtCaret == null )
    {
      return;
    }

    int offset;

    List<ILocalVarDeclaration> res = new ArrayList<>();
    IParsedElement root = getRootParsedElement();
    root.getContainedParsedElementsByType( ILocalVarDeclaration.class, res );
    for( ILocalVarDeclaration fs: res )
    {
      if( symbol == fs.getSymbol() )
      {
        offset = fs.getNameOffset( (String)fs.getLocalVarName() );
        getEditor().setCaretPosition( offset );
        return;
      }
    }
    List<IVarStatement> v = new ArrayList<>();
    root.getContainedParsedElementsByType( IVarStatement.class, v );
    for( IVarStatement fs: v )
    {
      if( symbol.equals( fs.getSymbol() ) )
      {
        offset = fs.getNameOffset( fs.getIdentifierName() );
        getEditor().setCaretPosition( offset );
        return;
      }
    }
  }

  void displayJavadocHelp( IParseTree parseTree )
  {
    String strHelpText = getContextHelp( parseTree );
    if( strHelpText == null )
    {
      return;
    }

    try
    {
      dismissBeanInfoPopup();

      dismissValuePopup();

      Rectangle rcCaretBounds = _editor.modelToView( parseTree.getOffset() + _parser.getOffsetShift() );
      _javadocPopup = new JavadocPopup( strHelpText, this );
      if( rcCaretBounds != TEST_RECTANGLE )
      {
        _javadocPopup.show( _editor, rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
      }

      EventQueue.invokeLater( new Runnable()
      {
        @Override
        public void run()
        {
          _editor.requestFocus();
          _editor.repaint();
        }
      } );
    }
    catch( BadLocationException e )
    {
      editor.util.EditorUtilities.handleUncaughtException( e );
    }
  }

  String getContextHelp( IParseTree parseTree )
  {
    return ContextHelpUtil.getContextHelp( parseTree );
  }

  String getTooltipMessage( MouseEvent event )
  {
    if( _parser == null )
    {
      return null;
    }

    int iPos = getEditor().viewToModel( event.getPoint() );
    return getErrorMessagesAt( getIssuesFromPos( iPos ) );
  }

  private List<IParseIssue> getIssuesFromPos( int iPos )
  {
    if( _editor == null )
    {
      return null;
    }
    ParseResultsException pe = getParseResultsException();
    if( pe == null )
    {
      return null;
    }

    List<IParseIssue> parseIssues = pe.getIssuesFromPos( iPos );
    try
    {
      if( " ".equals( _editor.getText( iPos - 1, 1 ) ) )
      {
        parseIssues.addAll( pe.getIssuesFromPos( iPos - 1 ) );
      }
    }
    catch( BadLocationException e )
    {
      // never mind
    }
    return parseIssues;
  }

  private List<IParseIssue> getIssuesNearPos( int iPos )
  {
    List<IParseIssue> issues = getIssuesFromPos( iPos );
    if( issues != null && !issues.isEmpty() )
    {
      return issues;
    }

    if( _editor == null )
    {
      return null;
    }
    ParseResultsException pe = getParseResultsException();
    if( pe == null )
    {
      return null;
    }

    int iDelta = Integer.MAX_VALUE;
    IParseIssue error = null;
    for( IParseIssue e : pe.getParseExceptions() )
    {
      Element root = _editor.getDocument().getRootElements()[0];
      int iLine = root.getElementIndex( iPos ) + 1;
      int iCurrentDelta = iPos - e.getTokenEnd();
      if( iCurrentDelta >= 0 && iLine == e.getLine() )
      {
        try
        {
          String strTextBetweenPosAndError = _editor.getText( e.getTokenEnd(), iPos - e.getTokenEnd() );
          if( strTextBetweenPosAndError.trim().length() > 0 )
          {
            continue;
          }
        }
        catch( BadLocationException e1 )
        {
          continue;
        }

        if( iCurrentDelta < iDelta ||
            (iCurrentDelta == iDelta &&
             e.getTokenStart() >= error.getTokenStart() &&
             e.getExpectedType() != null) )
        {
          iDelta = iCurrentDelta;
          error = e;
        }
      }
    }
    if( error != null )
    {
      issues = new ArrayList<IParseIssue>( 2 );
      issues.add( error );
      return issues;
    }
    return null;
  }

  private String getErrorMessagesAt( List parseExceptions )
  {
    if( parseExceptions == null || parseExceptions.isEmpty() )
    {
      return null;
    }

    String strFeedback = "<html>";
    for( int i = 0; i < parseExceptions.size(); i++ )
    {
      if( i > 0 )
      {
        strFeedback += "<br><hr>";
      }

      IParseIssue pi = (IParseIssue)parseExceptions.get( i );
      strFeedback += HTMLEscapeUtil.escape( pi.getUIMessage() );

      if( pi instanceof ParseException )
      {
        ParseException pe = (ParseException)pi;
        IType typeExpected = pe.getExpectedType();

        if( typeExpected != null )
        {
          String strTypeExpected = ParseResultsException.getExpectedTypeName( typeExpected );
          if( strTypeExpected.length() > 0 )
          {
            strFeedback += "<br>" + "Expected Type" + ":" + HTMLEscapeUtil.escape( strTypeExpected );
          }
        }
      }
    }
    return strFeedback;
  }

  public void addParseListener( ParseListener parseListener )
  {
    _parseListeners.add( parseListener );
  }

  public boolean acceptsUses()
  {
    return _bAcceptUses;
  }

  public void setAcceptUses( boolean acceptUses )
  {
    _bAcceptUses = acceptUses;
  }

  public int getLineNumberAtCaret()
  {
    return _editor.getDocument().getDefaultRootElement().getElementIndex( _editor.getCaretPosition() ) + 1;
  }

  public void parseAndWaitForParser()
  {
    parse();
    waitForParser();
  }

  public void waitForParser()
  {
    TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE ).postTaskAndWait(
      new Runnable()
      {
        @Override
        public void run()
        {
          //do nothing
        }
      } );
  }

  @Override
  public IParseTree getDeepestLocationAtCaret()
  {
    waitForParser();
    return getDeepestLocation( _editor.getCaretPosition(), true );
  }

  IParseTree getDeepestLocation( Point pt )
  {
    int iOffset = _editor.viewToModel( pt );
    if( iOffset < 0 )
    {
      return null;
    }
    return getDeepestLocation( iOffset, true );
  }

  IParseTree getDeepestLocation( int iPos, boolean strict )
  {
    if( _parser == null )
    {
      return null;
    }
    else
    {
      List<IParseTree> locations = _parser.getLocations();
      return IParseTree.Search.getDeepestLocation( locations, iPos - _parser.getOffsetShift(), strict );
    }
  }

  IParseTree getDeepestLocationSpanning( int start, int end )
  {
    if( _parser == null )
    {
      return null;
    }
    else
    {
      List<IParseTree> locations = _parser.getLocations();
      return IParseTree.Search.getDeepestLocationSpanning( locations, start - _parser.getOffsetShift(), end - _parser.getOffsetShift(), true );
    }
  }


  @Override
  public IParseTree getDeepestStatementLocationAtCaret()
  {
    return getDeepestStatementLocationAtPos( _editor.getCaretPosition() );
  }

  public IParseTree getDeepestStatementLocationAtPos( int iPos, boolean bStrict )
  {
    if( _parser == null )
    {
      return null;
    }
    else
    {
      List<IParseTree> locations = _parser.getLocations();
      return IParseTree.Search.getDeepestStatementLocation( locations, iPos - _parser.getOffsetShift(), bStrict );
    }
  }

  public IParseTree getDeepestStatementLocationAtPos( int iPos )
  {
    List<IParseTree> locations = _parser.getLocations();
    return IParseTree.Search.getDeepestStatementLocation( locations, iPos - _parser.getOffsetShift(), false );
  }

  @Override
  public IParseTree getStatementAtLineAtCaret()
  {
    int iLineNum = _editor.getDocument().getDefaultRootElement().getElementIndex( _editor.getCaretPosition() ) + 1;
    return getStatementAtLine( iLineNum );
  }

  @Override
  public IParseTree getStatementAtLine( int iLineNum )
  {
    if( _parser != null )
    {
      List<IParseTree> locations = _parser.getLocations();
      return IParseTree.Search.getStatementAtLine( locations, iLineNum - _parser.getLineNumShift(), IStatementList.class );
    }
    return null;
  }

  @Override
  public IParseTree getStatementAtLineOrExpression( int iLine )
  {
    IParseTree statement = getStatementAtLine( iLine );
    return returnStatementOrExpression( statement );
  }

  @Override
  public IParseTree getStatementAtLineAtCaretOrExpression()
  {
    IParseTree statement = getStatementAtLineAtCaret();
    return returnStatementOrExpression( statement );
  }

  private IParseTree returnStatementOrExpression( IParseTree statement )
  {
    if( statement != null )
    {
      return statement;
    }
    if( _parser.getLocations().size() == 1 &&
        _parser.getLocations().get( 0 ).getParsedElement() instanceof IExpression )
    {
      return _parser.getLocations().get( 0 );
    }
    return null;
  }

  public IParsedElement getFunctionCallAtCaret()
  {
    IParseTree location = getDeepestLocationAtCaret();
    if( location == null )
    {
      return null;
    }
    IParsedElement parsedElement = location.getParsedElement();
    return findFunction( parsedElement );
  }

  public IParsedElement findFunction( IParsedElement parsedElement )
  {
    if( parsedElement == null )
    {
      return null;
    }

    while( !(parsedElement instanceof IMethodCallExpression) &&
           !(parsedElement instanceof IMethodCallStatement) &&
           !(parsedElement instanceof IBeanMethodCallExpression) &&
           !(parsedElement instanceof INewExpression && !(parsedElement instanceof IInferredNewExpression)) )
    {
      if( parsedElement.getParent() == null )
      {
        return null;
      }
      parsedElement = parsedElement.getParent();
    }
    return parsedElement;
  }

  public IParsedElement getRootParsedElement()
  {
    IParseTree location = getDeepestLocationAtCaret();
    if( location == null )
    {
      return null;
    }
    IParsedElement parsedElement = location.getParsedElement();
    while( parsedElement.getParent() != null )
    {
      parsedElement = parsedElement.getParent();
    }
    return parsedElement;
  }

  void runIfNoKeyPressedInMillis( long lMillis, final Runnable task )
  {
    final boolean[] bKeyPressed = new boolean[]{false};
    final KeyListener keyListener =
      new KeyAdapter()
      {
        @Override
        public void keyPressed( KeyEvent e )
        {
          bKeyPressed[0] = true;
        }
      };
    _editor.addKeyListener( keyListener );

    Timer timer = _timerPool.requestTimer( (int)lMillis,
                                           new ActionListener()
                                           {
                                             @Override
                                             public void actionPerformed( ActionEvent e )
                                             {
                                               _editor.removeKeyListener( keyListener );
                                               if( !bKeyPressed[0] )
                                               {
                                                 editor.util.EditorUtilities.invokeInDispatchThread( task );
                                               }
                                               _iTimerCount--;
                                             }
                                           } );
    timer.setRepeats( false );
    _iTimerCount++;
    timer.start();
  }

  public int getTimerCount()
  {
    return _iTimerCount;
  }

  public boolean isAltDown()
  {
    return _bAltDown;
  }

  /**
   */
  class EditorKeyHandler extends KeyAdapter
  {
    @Override
    public void keyPressed( KeyEvent e )
    {
      setCompleteCode( false );
      if( e.getKeyChar() == KeyEvent.VK_ENTER && e.getModifiers() == 0 )
      {
        _bEnterPressedConsumed = e.isConsumed() || isIntellisensePopupShowing();
      }
      else if( e.getKeyChar() == KeyEvent.VK_SPACE && (e.getModifiers() & InputEvent.CTRL_MASK) > 0 )
      {
        _bAltDown = (e.getModifiers() & InputEvent.ALT_MASK) > 0;
        if( !isIntellisensePopupShowing() )
        {
          handleCompleteCode();
          e.consume();
        }
      }
      else if( e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == '\b' )
      {
        handleBackspace();
        if( e.getModifiers() == InputEvent.SHIFT_MASK )
        {
          _editor.dispatchEvent( new KeyEvent( (Component)e.getSource(), e.getID(), e.getWhen(), 0, e.getKeyCode(), e.getKeyChar(), e.getKeyLocation() ) );
        }
      }
    }

    @Override
    public void keyTyped( final KeyEvent e )
    {
      final boolean consumed = e.isConsumed();
      if( consumed || !_editor.isEditable() )
      {
        return;
      }

      final char keyChar = e.getKeyChar();
      final int modifiers = e.getModifiers();

      postProcessKeystroke( consumed, keyChar, modifiers );
    }

    private void postProcessKeystroke( boolean consumed, char keyChar, int modifiers )
    {
      if( keyChar == '.' )
      {
        handleDot();
      }
      if( keyChar == ':' )
      {
        handleColon();
      }
      if( keyChar == '#' )
      {
        handleDot();
      }

      else
      {
        if( keyChar == KeyEvent.VK_ENTER && modifiers == 0 )
        {
          if( !consumed && !_bEnterPressedConsumed && !isIntellisensePopupShowing() )
          {
            handleEnter();
          }
        }
        //      else if( e.getKeyChar() == KeyEvent.VK_SPACE && (e.getModifiers() & InputEvent.CTRL_MASK) > 0 )
        //      {
        //        if( !isIntellisenseShowing() )
        //        {
        //          handleCompleteCode();
        //        }
        //      }
        else if( keyChar == '}' )
        {
          if( !consumed && !isIntellisensePopupShowing() )
          {
            handleBraceRight();
          }
        }
      }
    }

  }

  /**
   */
  class GosuDocumentHandler implements DocumentListener
  {
    @Override
    public void changedUpdate( DocumentEvent e )
    {
    }

    @Override
    public void insertUpdate( DocumentEvent e )
    {
      resizeEditor();
      parse();
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      resizeEditor();
      parse();
    }

    private void resizeEditor()
    {
      Dimension dim = _editor.getPreferredSize();
      if( _editor.getWidth() != dim.width )
      {
        //dim.width = _editor.getWidth();
        _editor.setSize( dim );
        validate();
      }
    }
  }

  class ErrorAtCaretHandler implements CaretListener
  {
    @Override
    public void caretUpdate( CaretEvent ce )
    {
      if( TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE ).size() == 0 )
      {
        handleCodeCompletion( false );
      }
    }
  }

  /**
   */
  class ReplaceWordCallback implements IReplaceWordCallback
  {
    @Override
    public boolean shouldReplace( String strWord )
    {
      if( !Character.isJavaIdentifierPart( strWord.charAt( 0 ) ) )
      {
        // Don't replace operators or other 'special' characters. The assumption
        // is a word has been deleted and a new one should be inserted rather than
        // replaced.
        return false;
      }

      //
      // Finally, do a little hack to determine the likelyhood that the word is
      // by itself a legal expression. The assumption is that if the word starts
      // with a valid symbol, it's a valid expression. Not perfect, but should
      // suffice for value completion purposes.
      //
//      int iDotIndex = strWord.indexOf( '.' );
//      if( iDotIndex > 0 )
//      {
//        strWord = strWord.substring( 0, iDotIndex );
//      }
//
//      return _symTable.getSymbol( strWord ) == null;
      return true;
    }
  }

  void moveSelectionUp()
  {
    _codeManager.moveSelectionUp();
  }

  void moveSelectionDown()
  {
    _codeManager.moveSelectionDown();
  }

  private void smartFix()
  {
    _smartFixManager.performFix();
  }

  public SmartFixManager getSmartFixManager()
  {
    return _smartFixManager;
  }

  public void addToUses( String strType )
  {
    _codeManager.addToUses( strType, _bTemplate, _bProgram );
  }

  public void addSpecialFunction( IDynamicFunctionSymbol symbol )
  {
    _specialFunctions.add( symbol );
  }

  public void addSpecialFunctionHandler( IDynamicFunctionSymbol symbol, Runnable handler )
  {
    _specialFunctionGotoDeclHandlers.put( symbol, handler );
  }

  public void clearSpecialFunctions()
  {
    _specialFunctions.clear();
    _specialFunctionGotoDeclHandlers.clear();
  }

  public void makeReadOnly( boolean bReadOnly )
  {
    _editor.setEditable( !bReadOnly );
  }

  public void setPartId( ScriptPartId scriptPartId )
  {
    _partId = scriptPartId;
  }

  public void setTypeUsesMap( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap;
  }

  @Override
  public void setProgramSuperType( IType baseClass )
  {
    _programSuperType = baseClass;
  }

  public IGosuClass getParsedClass()
  {
    return _parsedGosuClass;
  }

  public static void waitOnParserThread()
  {
    TaskQueue queue = TaskQueue.getInstance( INTELLISENSE_TASK_QUEUE );
    queue.postTaskAndWait(
      new Runnable()
      {
        @Override
        public void run()
        {
          //do nothing
        }
      } );
  }

  public JavadocPopup getJavadocPopup()
  {
    return _javadocPopup;
  }

  public static void waitForIntellisenseTimers()
  {
    _timerPool.waitForAllTimersToFinish();
  }

  @Override
  public void refreshed()
  {
    parse();
  }

  @Override
  public void refreshedTypes( RefreshRequest refreshRequest )
  {
    if( refreshRequest.types != null )
    {
      for( String name : refreshRequest.types )
      {
        if( getParsedClass() != null && name.equals( getParsedClass().getName() ) )
        {
          return;
        }
      }
    }
    //!! this causes perpetual parsing since the parse() command refreshes the type...
    // parse();
  }

  private static class TimerPool
  {

    List<Object> _activeTimers = new ArrayList<Object>();

    Timer requestTimer( int millis, final ActionListener action )
    {
      synchronized( this )
      {
        final Object timerToken = new Object();
        Timer timer = new Timer( millis, new ActionListener()
        {
          @Override
          public void actionPerformed( ActionEvent e )
          {
            try
            {
              action.actionPerformed( e );
            }
            finally
            {
              synchronized( TimerPool.this )
              {
                _activeTimers.remove( timerToken );
                TimerPool.this.notify();
              }
            }
          }
        } );
        _activeTimers.add( timerToken );
        return timer;
      }
    }

    void waitForAllTimersToFinish()
    {
      synchronized( this )
      {
        while( !_activeTimers.isEmpty() )
        {
          try
          {
            wait();
          }
          catch( InterruptedException e )
          {
            // ?
          }
        }
      }
    }
  }

  @Override
  public JComponent asJComponent()
  {
    return this;
  }

  public void clipCut( Clipboard clipboard )
  {
    try
    {
      getUndoManager().beginUndoAtom( "Cut" );

      clipCopy( clipboard );
      delete();
    }
    finally
    {
      getUndoManager().endUndoAtom();
    }
  }

  public void clipCopy( Clipboard clipboard )
  {
    try
    {
      Transferable contents = getClipCopyContents();
      if( contents == null )
      {
        return;
      }

      clipboard.setContents( contents, null );
    }
    catch( Exception e )
    {
      handleUncaughtException( e );
    }
  }

  public void clipPaste( Clipboard clipboard, boolean asGosu )
  {
    Transferable t = clipboard.getContents( this );
    if( t == null )
    {
      return;
    }

    if( t.isDataFlavorSupported( DataFlavor.stringFlavor ) )
    {
      try
      {
        String strContents = (String)t.getTransferData( DataFlavor.stringFlavor );
        if ( asGosu )
        {
          strContents = JavaToGosu.convertString( strContents );
          if ("".equals(strContents)) {
            JOptionPane.showMessageDialog( getEditor() , "The copied Java code has errors, only valid Java 8 code can be transformed", "Paste Java as Gosu", JOptionPane.ERROR_MESSAGE);
            return;
          }
        }
        getEditor().replaceSelection( strContents );
      }
      catch( Exception e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private Transferable getClipCopyContents()
  {
    Transferable contents = null;
    String strSelection = getExpandedSelection();
    if( strSelection != null && strSelection.length() > 0 )
    {
      contents = new StringSelection( strSelection );
    }
    return contents;
  }


}
