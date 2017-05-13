package editor;

import editor.debugger.Breakpoint;
import editor.debugger.BreakpointManager;
import editor.search.SearchElement;
import editor.search.SearchLocation;
import editor.search.UsageSearcher;
import editor.search.UsageTarget;
import editor.undo.AtomicUndoManager;
import editor.util.EditorUtilities;
import editor.util.HTMLEscapeUtil;
import editor.util.IReplaceWordCallback;
import editor.util.LabToolbarButton;
import gw.config.CommonServices;
import manifold.api.fs.IFile;
import java.nio.file.Path;
import editor.util.SettleModalEventQueue;
import editor.util.TaskQueue;
import editor.util.TextComponentUtil;
import gw.lang.GosuShop;
import gw.lang.gosuc.GosuIssueContainer;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuProgramParser;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IImplicitTypeAsExpression;
import gw.lang.parser.expressions.IInferredNewExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.parser.expressions.IParameterDeclaration;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.statements.IClassDeclaration;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.IPropertyStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import manifold.api.host.ITypeLoaderListener;
import manifold.api.host.RefreshRequest;
import gw.lang.reflect.TypeSystem;
import manifold.api.sourceprod.ClassType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuStringUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.undo.CompoundEdit;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import manifold.internal.javac.IIssueContainer;


import static editor.util.TextComponentUtil.Direction.BACKWARD;

/**
 * A component for editing Gosu source.
 */
public class GosuEditor extends EditorHost implements IScriptEditor, IGosuPanel, ITypeLoaderListener
{
  public static final int MIN_LINENUMBER_WIDTH = 16;

  private JLabel _labelCaption;
  private ParserFeedbackPanel _panelFeedback;
  private EditorHostTextPane _editor;
  private GosuDocumentHandler _docHandler;
  private IContextMenuHandler<IScriptEditor> _contextMenuHandler;
  private volatile IGosuParser _parser;
  private boolean _bStatement;
  private boolean _bProgram;
  private boolean _bClass;
  private boolean _bEnhancement;
  private boolean _bEmptyTextOk;
  private ISymbolTable _symTable;
  private IReplaceWordCallback _replaceWordCallback;
  private boolean _bTemplate;
  private IScriptabilityModifier _scriptabilityModifier;
  private AtomicUndoManager _undoMgr;
  private EditorScrollPane _scroller;
  private ParseResultsException _pe;
  private boolean _bTestResource;
  private Path _file;
  private boolean _bAcceptUses;
  private IGosuClass _parsedGosuClass;
  private Map<Integer, IFunctionStatement> _functionStmtsByLineNumber;

  private IGosuValidator _validator;

  private SmartFixManager _smartFixManager;
  private ScopeHighlighter _ctxHighlighter;
  private DynamicSelectionManager _selectionManager;
  private CodeRefactorManager _codeManager;

  //This rectangle is used to signal that the editor is being used in test mode and that popups should therefore
  //not display
  private static final Rectangle TEST_RECTANGLE = new Rectangle( 0, 0, 0, 0 );

  private ITypeUsesMap _typeUsesMap;
  private ITypeUsesMap _typeUsesMapFromMostRecentParse;

  private JavadocPopup _javadocPopup;

  private IType _programSuperType;
  private boolean _bAccessPrivateMembers;
  private IType _expectedType;

  public GosuEditor( ILineInfoManager lineInfoRenderer,
                     AtomicUndoManager undoMgr,
                     IScriptabilityModifier scriptabilityConstraint,
                     IContextMenuHandler<IScriptEditor> contextMenuHandler,
                     Path file,
                     boolean bStatement, boolean bEmptyTextOk )
  {
    this( null, lineInfoRenderer, undoMgr, scriptabilityConstraint, contextMenuHandler, file, bStatement, bEmptyTextOk );
  }

  public GosuEditor( ISymbolTable symTable,
                     ILineInfoManager lineInfoRenderer,
                     AtomicUndoManager undoMgr,
                     IScriptabilityModifier scriptabilityConstraint,
                     IContextMenuHandler<IScriptEditor> contextMenuHandler,
                     Path file,
                     boolean bStatement, boolean bEmptyTextOk )
  {
    super( undoMgr );
    if( lineInfoRenderer != null )
    {
      lineInfoRenderer.setEditor( this );
    }
    _symTable = symTable;
    _undoMgr = undoMgr;
    _contextMenuHandler = contextMenuHandler == null
                          ? new DefaultContextMenuHandler()
                          : contextMenuHandler;
    _file = file;
    _bStatement = bStatement;
    _bEmptyTextOk = bEmptyTextOk;
    _bAcceptUses = true;
    _docHandler = new GosuDocumentHandler();
    _parser = null;
    _scriptabilityModifier = scriptabilityConstraint;

    _replaceWordCallback = new ReplaceWordCallback();
    _ctxHighlighter = new ScopeHighlighter( this );

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
    _editor.setForeground( Scheme.active().getWindowText() );
    _editor.putClientProperty( "caretWidth", 2 );
    _editor.setCaretColor( Scheme.active().getWindowText() );
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
    EventQueue.invokeLater( () -> _editor.setSize( 1000, _editor.getHeight() ) );

    _scroller = new EditorScrollPane( lineInfoRenderer, _editor, editorRootPane );
    _scroller.setBorder( null );
    JViewport vp = _scroller.getViewport();
    vp.setScrollMode( JViewport.BLIT_SCROLL_MODE );

    add( BorderLayout.CENTER, _scroller );

    _labelCaption = new JLabel( "<Script Part>" );
    _labelCaption.setBackground( Scheme.active().getControl() );
    _labelCaption.setOpaque( true );
    _labelCaption.setFont( getFont().deriveFont( Font.BOLD ) );
    _labelCaption.setBorder( new EmptyBorder( 0, 4 + MIN_LINENUMBER_WIDTH * (lineInfoRenderer != null ? 2 : 1), 0, 0 ) );
    add( BorderLayout.NORTH, _labelCaption );

    _panelFeedback = new ParserFeedbackPanel();
    add( BorderLayout.EAST, _panelFeedback );
  }

  @Override
  public void setLabel( String label )
  {
    _labelCaption.setText( label );
  }

  @Override
  public DocumentListener getDocHandler()
  {
    return _docHandler;
  }

  @Override
  public JComponent getFeedbackPanel()
  {
    return _panelFeedback;
  }

  @Override
  public IIssueContainer getIssues()
  {
    return new GosuIssueContainer( getParseResultsException() );
  }

  protected void addKeyHandlers()
  {
    super.addKeyHandlers();

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " T" ), "_typeInfo" );
    _editor.getActionMap().put( "_typeInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayTypeInfoAtCurrentLocation();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift T" ), "_clipCopyTypeInfo" );
    _editor.getActionMap().put( "_clipCopyTypeInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    clipCopyTypeInfoAtCurrentLocation();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " W" ), "_selectWord" );
    _editor.getActionMap().put( "_selectWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    selectWord();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift W" ), "_narrowSelectWord" );
    _editor.getActionMap().put( "_narrowSelectWord",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    narrowSelectWord();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " P" ), "_parameterInfo" );
    _editor.getActionMap().put( "_parameterInfo",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( !isCompletionPopupShowing() )
                                    {
                                      displayParameterInfoPopup( _editor.getCaretPosition() );
                                    }
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " B" ), "_declaration" );
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
    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " Q" ), "_javadocHelp" );
    _editor.getActionMap().put( "_javadocHelp",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    displayJavadocHelp( getDeepestLocationAtCaret() );
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " shift F7" ), "_highlightUsagesInView" );
    _editor.getActionMap().put( "_highlightUsagesInView",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    if( getScriptPart() != null && getScriptPart().getContainingType() != null )
                                    {
                                      highlightUsagesOfFeatureUnderCaret();
                                    }
                                  }
                                } );


    _editor.getInputMap().put( KeyStroke.getKeyStroke( EditorUtilities.CONTROL_KEY_NAME + " alt V" ), "_extractVariable" );
    _editor.getActionMap().put( "_extractVariable",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    extractVariable();
                                  }
                                } );


    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_UP, EditorUtilities.CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ), "_moveSelectionUp" );
    _editor.getActionMap().put( "_moveSelectionUp",
                                new AbstractAction()
                                {
                                  @Override
                                  public void actionPerformed( ActionEvent e )
                                  {
                                    moveSelectionUp();
                                  }
                                } );

    _editor.getInputMap().put( KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, EditorUtilities.CONTROL_KEY_MASK | KeyEvent.SHIFT_DOWN_MASK ), "_moveSelectionDown" );
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
  }

  public boolean isAccessAll()
  {
    return _bAccessPrivateMembers;
  }
  public void setAccessAll( boolean accessPrivateMembers )
  {
    _bAccessPrivateMembers = accessPrivateMembers;
  }

  @SuppressWarnings("UnusedDeclaration")
  public IType getExpectedType()
  {
    return _expectedType;
  }
  public void setExpectedType( IType type )
  {
    _expectedType = type;
  }

  @Override
  public String getLineCommentDelimiter()
  {
    return "//";
  }

  public void highlightUsagesOfFeatureUnderCaret()
  {
    setHighlightMode( HighlightMode.USAGES );

    removeAllHighlights();

    IParseTree deepestLocationAtCaret = getDeepestLocationAtCaret();
    if( deepestLocationAtCaret != null )
    {
      UsageTarget target = UsageTarget.makeTargetFromCaret();
      if( target != null )
      {
        List<SearchLocation> locations = new UsageSearcher( target, true, false ).searchLocal();
        for( SearchLocation loc : locations )
        {
          try
          {
            getEditor().getHighlighter().addHighlight( loc._iOffset, loc._iOffset + loc._iLength, LabHighlighter.USAGE );
          }
          catch( BadLocationException e )
          {
            //throw new RuntimeException( e );
          }
        }
      }
    }
    _panelFeedback.repaint();
  }

  protected void hideMiscPopups()
  {
    super.hideMiscPopups();
    _smartFixManager.resetSmartHelpState();
  }

  public void selectWord()
  {
    if( !isCompletionPopupShowing() && isCaretInEditor() )
    {
      _selectionManager.expandSelection();
    }
  }

  public void selectWordForMouseClick()
  {
    if( !isCompletionPopupShowing() && isCaretInEditor() )
    {
      _selectionManager.expandSelection( false );
    }
  }

  public void narrowSelectWord()
  {
    if( !isCompletionPopupShowing() && isCaretInEditor() )
    {
      _selectionManager.reduceSelection();
    }
  }

  private boolean isCaretInEditor()
  {
    // Check selection start. If nothing is selected, getSelectionStart() returns the caret's location.
    return (_editor.getSelectionStart() <= _editor.getDocument().getLength());
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
    if( !isCompletionPopupShowing() )
    {
      gotoDeclarationAtCursor();
    }
  }

  protected EditorHostTextPane createEditorPane()
  {
    return new EditorHostTextPane( this );
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

  @SuppressWarnings("UnusedDeclaration")
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
  public EditorHostTextPane getEditor()
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

  @SuppressWarnings("UnusedDeclaration")
  public boolean hasParseResultsException()
  {
    return _pe != null;
  }

  public void refresh( String content )
  {
    content = GosuStringUtil.replace( content, "\r\n", "\n" );
    getEditor().setText( content );
    EventQueue.invokeLater( () -> getUndoManager().discardAllEdits() );
  }

  protected void addDocumentListener()
  {
    AbstractDocument doc;
    doc = (AbstractDocument)_editor.getDocument();
    if( doc != null )
    {
      doc.addDocumentListener( _docHandler );
      doc.addDocumentListener( _ctxHighlighter );
    }
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setTestResource( boolean testResource )
  {
    _bTestResource = testResource;
  }

  public void parse( String strText, boolean forceCodeCompletion, boolean changed )
  {
    if( isParserSuspended() )
    {
      return;
    }

    if( isAccessAll() )
    {
      TypeSystem.pushIncludeAll();
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

        ClassType classType = getClassType();
        if( classType != null )
        {
          // The context here is expected to be a fully qualified class/program/enhancement/template
           IFile file = _file == null ? null : CommonServices.getFileSystem().getIFile( _file.toFile() );
          _parsedGosuClass = _parser.parseClass( getScriptPart().getContainingTypeName(), new StringSourceFileHandle( getScriptPart().getContainingTypeName(), strText, file, _bTestResource, classType ), true, true );
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
            if( _expectedType != null )
            {
              options.withExpectedType( _expectedType );
            }
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
      if( isAccessAll() )
      {
        TypeSystem.popIncludeAll();
      }

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
            () -> {
              if( getDocument().getLocations() == null )
              {
                _editor.repaint();
              }
              getDocument().setLocations( locations );
              getDocument().setLocationsOffset( _parser.getOffsetShift() );
            } );
        }
      }
    }

    if( areMoreThanOneParserTasksPendingForThisEditor() )
    {
      return;
    }

    EventQueue.invokeLater( () -> _panelFeedback.update( GosuEditor.this ) );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void showFeedback( boolean show )
  {
    _panelFeedback.setVisible( show );
    revalidate();
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
      functionStmtsByLineNumber = new HashMap<>();
    }
    List<IFunctionStatement> functionStatements = new ArrayList<>();
    gsClass.getClassStatement().getContainedParsedElementsByType( IFunctionStatement.class, functionStatements );
    for( IFunctionStatement fs : functionStatements )
    {
      functionStmtsByLineNumber.put( fs.getLocation().getLineNum(), fs );
    }
    return functionStmtsByLineNumber;
  }

  public Map<Integer, IFunctionStatement> getFunctionsByLineNumber()
  {
    return _functionStmtsByLineNumber == null ? Collections.emptyMap() : _functionStmtsByLineNumber;
  }

  protected void clearParseException()
  {
    _pe = null;
    EventQueue.invokeLater( () -> getDocument().setParseResultsException( null ) );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void setValidator( IGosuValidator validator )
  {
    _validator = validator;
    if( _parser != null )
    {
      _parser.setValidator( _validator );
    }
  }

  private void handleParseException( final boolean forceCodeCompletion )
  {
    EventQueue.invokeLater( () -> handleParseException( _pe, forceCodeCompletion ) );
  }

  protected void handleParseException( final ParseResultsException e, final boolean bForceCodeCompletion )
  {
    handleCodeCompletion( bForceCodeCompletion );
    getDocument().setParseResultsException( e );
    _panelFeedback.update( this );
  }

  private void handleCodeCompletion( boolean bForceCodeCompletion )
  {
    if( _parser == null )
    {
      return;
    }

    //If we are forcing code completion (after a dot) just handle it directly
    if( bForceCodeCompletion )
    {
      handleCompleteCode();
    }
  }

  public IType findExpectedTypeErrorAtCaret()
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
        return null;
      }
    }

    IType typeExpected = null;
    for( IParseIssue issue : errors )
    {
      if( issue instanceof ParseException )
      {
        typeExpected = issue.getExpectedType();
        if( typeExpected != null )
        {
          break;
        }
      }
    }
    if( typeExpected == null )
    {
      typeExpected = ParseExceptionResolver.resolvePossibleContextTypesFromEmptyMethodCalls( getExpressionAtCaret(), _editor );
    }
    return typeExpected;
  }

  public IGosuParser getParser()
  {
    return _parser;
  }

  public GosuDocument getDocument()
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

  @SuppressWarnings("UnusedDeclaration")
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

  public AtomicUndoManager getUndoManager()
  {
    return _undoMgr;
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
      getEditor().requestFocusInWindow();
      getEditor().setCaretPosition( first.getTokenStart() );
    }
  }

  protected void handleDot( final ISymbolTable transientSymTable )
  {
    if( transientSymTable == null )
    {
      return;
    }

    if( isCompletionPopupShowing() )
    {
      // Don't clobber the code completion popup
      return;
    }

    String strWordAtCaret = TextComponentUtil.getWordAtCaret( _editor );
    if( strWordAtCaret == null || strWordAtCaret.length() == 0 || !Character.isLetterOrDigit( strWordAtCaret.charAt( 0 ) ) )
    {
      strWordAtCaret = TextComponentUtil.getPartialWordBeforeCaret( _editor );
    }

    if( isCompleteCode() || strWordAtCaret.equals( "." ) || strWordAtCaret.equals( "#" ) || strWordAtCaret.equals( ":" ) )
    {
      handleDotNow( transientSymTable );
    }
  }

  void handleDotNow( ISymbolTable transientSymTable )
  {
    dismissCompletionPopup();
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

  void displayPathCompletionPopup( final boolean bFeatureLiteralCompletion )
  {
    JPopupMenu completionPopup = getCompletionPopup();
    if( ((BeanInfoPopup)completionPopup).isDOA() )
    {
      return;
    }

    ((BeanInfoPopup)completionPopup).addNodeChangeListener( e -> {
      CompoundEdit undoAtom = _undoMgr.getUndoAtom();
      if( undoAtom != null && undoAtom.getPresentationName().equals( "Text Change" ) )
      {
        _undoMgr.endUndoAtom();
      }
      undoAtom = getUndoManager().beginUndoAtom( "Code Completion" );
      try
      {
        BeanTree completionSelection = (BeanTree)e.getSource();
        String strRef = completionSelection.makePath( bFeatureLiteralCompletion );
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

        TextComponentUtil.replaceWordAtCaretDynamic( getEditor(),
                                                     strRef,
                                                     getReplaceWordCallback(),
                                                     true,
                                                     ((BeanInfoPopup)completionPopup).isReplaceWholeWord() );
        // If we are handling a typed dot, no fixing up stuff
        if( e instanceof BeanInfoPopup.DotWasTypedChangeEvent )
        {
          return;
        }

        _editor.requestFocus();
        EditorUtilities.fixSwingFocusBugWhenPopupCloses( GosuEditor.this );
        _editor.repaint();
      }
      finally
      {
        getUndoManager().endUndoAtom( undoAtom );
      }
    } );
    displayCompletionPopup( _editor.getCaretPosition() );
  }

  public void clipCopyTypeInfoAtCurrentLocation()
  {
    IType type = getTypeAtCaretPosition();
    if( type == null )
    {
      return;
    }
    LabFrame.instance().getGosuPanel().getClipboard().setContents( new StringSelection( type.getName() ), null );
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
    popup.setLayout( new BorderLayout() );

    String displayHTML;
    boolean foundType = true;
    Color typeColor = Scheme.active().getCodeTypeLiteral();
    String hexTypeColor = String.format( "#%02x%02x%02x", typeColor.getRed(), typeColor.getGreen(), typeColor.getBlue() );
    if( typeAtCursor != null )
    {
      if( typeAtCursor instanceof IMetaType )
      {
       //displayHTML = "<html>" + "Type" + ":&nbsp;<font color= " + hexTypeColor + "><b>Type" + "&lt;</b>" + HTMLEscapeUtil.escape( ((IMetaType)type).getType().getName() ) + "<b>&gt;</b></font></html>";
        displayHTML = "<html>" + "Type" + ":&nbsp;<span style=\"font-family: monospaced; color: " + hexTypeColor + "\"><b>Type" + "&lt;</b>" + HTMLEscapeUtil.escape( ((IMetaType)type).getType().getName() ) + "<b>&gt;</b></span></html>";
      }
      else
      {
        displayHTML = "<html>Type:&nbsp;<span style=\"font-family: monospaced; color: " + hexTypeColor + "\"><b>" + HTMLEscapeUtil.escape( type.getName() ) + "</b></span></html>";
      }
    }
    else
    {
      displayHTML = "No type found at point";
      foundType = false;
    }

    final JLabel label = new JLabel( displayHTML );
    JPanel panel = new JPanel( new BorderLayout() );
    panel.setBackground( Scheme.active().getTooltipBackground() );
    panel.setBorder( BorderFactory.createEmptyBorder( 1, 3, 1, 1 ) );
    panel.add( label, BorderLayout.CENTER );
    if( foundType )
    {
      final IType finalType = type;
      JButton copyBtn = new LabToolbarButton(
        new AbstractAction( "copy", EditorUtilities.loadIcon( "images/Copy.png" ) )
        {
          @Override
          public void actionPerformed( ActionEvent e )
          {
            editor.util.EditorUtilities.getClipboard().setContents( new StringSelection( finalType.getName() ), null );
            CopyBuffer.instance().captureState();
          }
      } );
      panel.add( copyBtn, BorderLayout.EAST );
    }
    panel.setPreferredSize( new Dimension( panel.getPreferredSize().width + 20, panel.getPreferredSize().height + 4 ) );
    popup.add( panel, BorderLayout.CENTER );

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

    return getTypeFrom( parsedElement );
  }

  private IType getTypeFrom( IParsedElement pe )
  {
    if( pe instanceof IClassDeclaration )
    {
      return ((IClassDeclaration)pe).getGSClass();
    }
    else if( pe instanceof INameInDeclaration )
    {
      pe = pe.getParent();
      if( pe instanceof IVarStatement )
      {
        IVarStatement varStmt = (IVarStatement)pe;
        return varStmt.getType();
      }
      else if( pe instanceof IParameterDeclaration )
      {
        return getTypeFrom( pe );
      }
    }
    else if( pe instanceof ILocalVarDeclaration )
    {
      ILocalVarDeclaration local = (ILocalVarDeclaration)pe;
      return local.getSymbol().getType();
    }
    else if( pe instanceof IExpression )
    {
      return ((IExpression)pe).getType();
    }
    return null;
  }

  @SuppressWarnings("UnusedDeclaration")
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
    return getSymbolTableAtOffset( _editor.getCaretPosition() );
  }

  public ISymbolTable getSymbolTableAtOffset( int offset )
  {
    StringBuffer sb = new StringBuffer( _editor.getText() );
    sb.insert( offset, "; +yennikcm ;" ); // Force a parse exception
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
      List<IParseIssue> errors = pe.getIssuesFromPos( offset + 2 );
      if( errors.isEmpty() )
      {
        errors = pe.getIssuesFromPos( offset );
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

  protected void displayCompletionPopup( int iPosition )
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
      Rectangle rcBounds = getPositionFromPoint( iPosition );
      if( rcBounds != TEST_RECTANGLE )
      {
        getCompletionPopup().show( _editor, rcBounds.x, rcBounds.y + rcBounds.height );
      }

      EventQueue.invokeLater( () -> {
        _editor.requestFocus();
        _editor.repaint();
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

  public ParameterInfoPopup displayParameterInfoPopup( int iPosition )
  {
    return ParameterInfoPopup.invoke( this, iPosition );
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
    gotoDeclaration( deepestParsedElementAtCaret );
  }

  public void gotoDeclaration( IParsedElement pe )
  {
    UsageTarget target = UsageTarget.makeTarget( pe );
    if( target != null )
    {
      SearchElement targetPe = target.getTargetElement();
      if( targetPe == null )
      {
        return;
      }

      int prevCaretPos = getEditor().getCaretPosition();

      IType type = targetPe.getEnclosingType();
      if( type != getParsedClass() )
      {
        while( type.isParameterizedType() )
        {
          type = type.getGenericType();
        }
        FileTree fileTree = FileTreeUtil.find( type.getName() );
        if( fileTree != null )
        {
          Path sourceFile = fileTree.getFileOrDir();
          if( sourceFile != null )
          {
            LabFrame.instance().getGosuPanel().openFile( sourceFile, true );
            SettleModalEventQueue.instance().run();
          }
        }
        else
        {
          //## todo: find in source location for dependency
          return;
        }
      }
      LabFrame.instance().getGosuPanel().getCurrentEditor().getEditor().setCaretPosition( targetPe.getOffset() );

      GosuPanel gosuPanel = LabFrame.instance().getGosuPanel();
      EditorHost currentEditor = gosuPanel.getCurrentEditor();
      int currentCaretPos = currentEditor.getEditor().getCaretPosition();
      if( currentEditor == this && currentCaretPos != prevCaretPos )
      {
        // Only need to handle navigation within current file,
        // jumps to other files will be caught as tab selection change events

        gosuPanel.getTabSelectionHistory().addNavigationHistory( this, prevCaretPos, currentCaretPos );
      }
    }
  }

  @Override
  public String getSelectedText()
  {
    return _editor.getSelectedText();
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
      dismissCompletionPopup();

      Rectangle rcCaretBounds = _editor.modelToView( parseTree.getOffset() + _parser.getOffsetShift() );
      _javadocPopup = new JavadocPopup( strHelpText, this );
      if( rcCaretBounds != TEST_RECTANGLE )
      {
        _javadocPopup.show( _editor, rcCaretBounds.x, rcCaretBounds.y + rcCaretBounds.height );
      }

      EventQueue.invokeLater( () -> {
        _editor.requestFocus();
        _editor.repaint();
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

  public String getTooltipMessage( MouseEvent event )
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
    //noinspection ThrowableResultOfMethodCallIgnored
    ParseResultsException pe = getParseResultsException();
    if( pe == null )
    {
      return null;
    }

    List<IParseIssue> parseIssues = pe.getIssuesFromPos( iPos );
    try
    {
      int backwardPos = iPos;
      while( Character.isWhitespace( _editor.getText( --backwardPos, 1 ).charAt( 0 ) ) )
      {
        parseIssues.addAll( pe.getIssuesFromPos( backwardPos ) );
      }
      if( parseIssues.isEmpty() )
      {
        int forwardPos = iPos;
        while( Character.isWhitespace( _editor.getText( ++forwardPos, 1 ).charAt( 0 ) ) )
        {
          parseIssues.addAll( pe.getIssuesFromPos( forwardPos ) );
        }
        if( parseIssues.isEmpty() )
        {
          parseIssues.addAll( pe.getIssuesFromPos( forwardPos+1 ) );
        }
      }
    }
    catch( Exception e )
    {
      // never mind
    }
    return parseIssues;
  }

  @SuppressWarnings("UnusedDeclaration")
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
    //noinspection ThrowableResultOfMethodCallIgnored
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
      issues = new ArrayList<>( 2 );
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

  public boolean acceptsUses()
  {
    return _bAcceptUses;
  }

  public void setAcceptUses( boolean acceptUses )
  {
    _bAcceptUses = acceptUses;
  }

  @Override
  public boolean canAddBreakpoint( int line )
  {
    IParseTree location = getStatementAtLine( line );
    if( location == null )
    {
      return false;
    }
    IParsedElement parsedElement = location.getParsedElement();
    return !(parsedElement instanceof IStatementList ||
             parsedElement instanceof IFunctionStatement ||
             parsedElement instanceof IPropertyStatement ||
             parsedElement instanceof IClassStatement ||
             parsedElement instanceof IClassFileStatement ||
             parsedElement instanceof IUsesStatement ||
             parsedElement instanceof IUsesStatementList ||
             parsedElement instanceof INamespaceStatement ||
             (parsedElement instanceof IVarStatement && !((IVarStatement)parsedElement).getHasInitializer()));
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
  public int getOffsetOfDeepestStatementLocationAtPos( int caretPosition, boolean strict )
  {
    IParseTree stmt = getDeepestStatementLocationAtPos( caretPosition, strict );
    if( stmt == null )
    {
      return -1;
    }
    return stmt.getOffset();
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
  public String getTypeAtLine( int line )
  {
    IParseTree statementAtLine = getStatementAtLine( line );
    return statementAtLine == null
           ? null
           : statementAtLine.getParsedElement().getGosuClass() == null
             ? getScriptPart().getContainingType().getName()
             : statementAtLine.getParsedElement().getGosuClass().getName();
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

  @SuppressWarnings("UnusedDeclaration")
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
      updateBreakpoints( e );
    }

    @Override
    public void removeUpdate( DocumentEvent e )
    {
      resizeEditor();
      parse();
      updateBreakpoints( e );
    }

    private void updateBreakpoints( DocumentEvent e )
    {
      if( _scroller.getLineInfoMgr() == null )
      {
        return;
      }
      DocumentEvent.ElementChange change = e.getChange( e.getDocument().getDefaultRootElement() );
      if( change != null )
      {
        int linesInserted = change.getChildrenAdded().length - change.getChildrenRemoved().length;
        if( linesInserted != 0 && getScriptPart() != null )
        {
          BreakpointManager bpm = LabFrame.instance().getGosuPanel().getBreakpointManager();
          Collection<Breakpoint> breakpoints = new ArrayList<>( bpm.getLineBreakpoints() );
          String fqn = getScriptPart().getContainingTypeName();
          for( Breakpoint bp : breakpoints )
          {
            if( fqn.equals( bp.getFqn() ) )
            {
              bpm.removeBreakpoint( bp );
              bp = updateBreakpoint( bp, linesInserted, e );
              bpm.toggleLineBreakpoint( GosuEditor.this, bp.getFqn(), bp.getDeclaringFqn(), bp.getLine() );
            }
          }
          _scroller.getAdviceColumn().repaint();
        }
      }
    }

    private Breakpoint updateBreakpoint( Breakpoint bp, int linesInserted, DocumentEvent e )
    {
      int firstLine = e.getDocument().getDefaultRootElement().getElementIndex( e.getOffset() ) + 1;
      int line = bp.getLine();
      if( bp.getLine() > firstLine )
      {
        line = bp.getLine() + linesInserted;
      }
      return new Breakpoint( bp.getFqn(), bp.getDeclaringFqn(), line );
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
      // Finally, do a little hack to determine the likelihood that the word is
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

  public void addToUses( String strType )
  {
    _codeManager.addToUses( strType, _bTemplate, _bProgram );
  }

  @SuppressWarnings("UnusedDeclaration")
  public void makeReadOnly( boolean bReadOnly )
  {
    _editor.setEditable( !bReadOnly );
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

  public JavadocPopup getJavadocPopup()
  {
    return _javadocPopup;
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

  @Override
  public JComponent asJComponent()
  {
    return this;
  }
}
