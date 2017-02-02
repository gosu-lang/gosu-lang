package editor;

import editor.util.TextComponentUtil;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.exceptions.ImplicitCoercionWarning;
import gw.lang.parser.exceptions.ObsoleteConstructorWarning;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.IImplicitTypeAsExpression;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.lang.parser.expressions.ISynthesizedMemberAccessExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.resources.Res;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;

import java.util.function.Supplier;
import javax.swing.*;
import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Handles state and functions relating to smart-fix functionality in the Gosu editor
 *
 * @author cgross
 */
public class SmartFixManager implements MouseMotionListener, KeyListener
{
  //This is a bullshit constant here to deal with the fact that in headless mode swing returns null
  //rectangles when attempting to map points in a document to points on a widget
  private static final Rectangle TEST_RECTANGLE = new Rectangle( 0, 0, 0, 0 );
  private static final String DISPLAYKEY_START = "displaykey.";
  public static final String SHORTCUT = "Alt+Enter";

  private Timer _timer;

  private GosuEditor _gosuEditor;
  private JTextComponent _editor;

  private SmartFixMode _mode;

  //Fix state (should be reset on all updates)
  private Set<String> _possibleTypesToImport;
  private IParsedElement _peToFixWithAsStatement;
  private IParsedElement _javaStyleCast;
  private int _obsoleteCtorStart;
  private int _obsoleteCtorEnd;
  private String _possibleDisplayKey;
  private IParseTree _stringLiteralLocationToReplace;
  private IParsedElement _sourceOfIssue;

  private String _typeToCoerceTo;
  private SmartFixPopup _managerPopup;
  private int _offset;
  private int _length;
  private static final Color SMARTFIX_HIGHLIGHT_COLOR = new Color( 180, 180, 70 );
  static boolean _allowUnusedParameterFix = false;

  SelectClassToImportPopup _selectionPopup;

  public enum SmartFixMode
  {
    NONE( "" ),
    IMPORT( "Add import for unrecognized symbol" ),
    FIX_IMPLICIT_CAST( "Make implicit coercion explicit" ),
    FIX_JAVA_STYLE_CAST( "Fix Java-style type cast" ),
    //    CREATE_DISPLAY_KEY("Create unrecognized display key"),
//    CONVERT_STRING_TO_DISPLAY_KEY("Convert string literal to new display key"),
    FIX_CTOR_SYNTAX( "Change old constructor syntax to new" ),
    FIX_UNUSED_ELEMENT( "Remove unused variable" ),
    ADD_MISSING_OVERRIDE( "Add missing \"override\" modifier" ),
    //    IMPL_FUNCTIONS("Implement functions and properties"),
    FIX_CASE( "Fix case issue" ),
    FIX_RETURN_TYPE( "Fix return type" ),
    GENERATE_CONSTRUCTORS( "Generate constructors" ),
    GENERATE_SUPER_CALL( "Generate super call" ),;

    private final String _humanName;

    SmartFixMode( String humanName )
    {
      _humanName = humanName;
    }

    @Override
    public String toString()
    {
      return _humanName;
    }

  }

  public SmartFixManager( GosuEditor gosuEditor )
  {
    _mode = SmartFixMode.NONE;
    _gosuEditor = gosuEditor;
    _editor = gosuEditor.getEditor();
    _editor.addMouseMotionListener( this );
    _editor.addKeyListener( this );
  }

  public void performFix()
  {

    editor.util.EditorUtilities.settleBackgroundOps();

    switch( _mode )
    {
      case IMPORT:
        fixImport();
        setMode( SmartFixMode.NONE );
        break;
      case FIX_IMPLICIT_CAST:
        fixImplicitCast();
        setMode( SmartFixMode.NONE );
        break;
      case FIX_JAVA_STYLE_CAST:
        fixJavaStyleCast();
        setMode( SmartFixMode.NONE );
        break;
      case FIX_CTOR_SYNTAX:
        fixConstructorSyntax();
        setMode( SmartFixMode.NONE );
        break;
//      case CREATE_DISPLAY_KEY:
//        createDisplayKey();
//        setMode(SmartFixMode.NONE);
//        break;
//      case CONVERT_STRING_TO_DISPLAY_KEY:
//        createDisplayKeyFromStringLiteral();
//        setMode(SmartFixMode.NONE);
//        break;
      case FIX_UNUSED_ELEMENT:
        fixUnusedElement();
        setMode( SmartFixMode.NONE );
        break;
      case ADD_MISSING_OVERRIDE:
        addMissingOverride();
        setMode( SmartFixMode.NONE );
        break;
//      case IMPL_FUNCTIONS:
//        ImplementFunctionsSmartFix.instance().addFunctions(_gosuEditor, _editor, _sourceOfIssue);
//        setMode(SmartFixMode.NONE);
//        break;
      case FIX_CASE:
        fixCase();
        setMode( SmartFixMode.NONE );
        break;
      case FIX_RETURN_TYPE:
        fixReturnType();
        setMode( SmartFixMode.NONE );
        break;
      case GENERATE_CONSTRUCTORS:
        generateConstructors();
        setMode( SmartFixMode.NONE );
        break;
      case GENERATE_SUPER_CALL:
        generateSuperCall();
        setMode( SmartFixMode.NONE );
        break;
      case NONE:
        if( !offerPassiveFix() )
        {
          updateState();
        }
    }

  }

  public JTextComponent getEditor()
  {
    return _editor;
  }

  public void setEditor( JTextComponent editor )
  {
    _editor = editor;
  }

  public IParsedElement getSourceOfIssue()
  {
    return _sourceOfIssue;
  }

  public void setSourceOfIssue( IParsedElement sourceOfIssue )
  {
    _sourceOfIssue = sourceOfIssue;
  }

  public int getOffset()
  {
    return _offset;
  }

  public void setOffset( int offset )
  {
    _offset = offset;
  }

  public int getLength()
  {
    return _length;
  }

  public void setLength( int length )
  {
    _length = length;
  }

  public GosuEditor getGosuEditor()
  {
    return _gosuEditor;
  }

  public void setGosuEditor( GosuEditor gosuEditor )
  {
    _gosuEditor = gosuEditor;
  }

  private void fixUnusedElement()
  {
    try
    {
      int start = getStartOffsetOfUnused();
      int end = getEndOffsetOfUnused();
      _editor.getDocument().remove( start, (end - start) + 1 );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  private int getStartOffsetOfUnused()
  {
    int i = _sourceOfIssue.getLocation().getOffset() - 1;
    while( (i > 0) && (_editor.getText().charAt( i ) == ' ') )
    {
      i--;
    }
    if( (i > 0) && (_editor.getText().charAt( i ) == '\n') )
    {
      i--;
    }
    return i + 1;
  }

  private int getEndOffsetOfUnused()
  {
    int i = _sourceOfIssue.getLocation().getExtent() + 1;
    while( (i < _editor.getText().length()) &&
           ((_editor.getText().charAt( i ) == ' ') || (_editor.getText().charAt( i ) == ';')) )
    {
      i++;
    }
    return i - 1;
  }


  private void addMissingOverride()
  {
    int offset = _sourceOfIssue.getLocation().getOffset();
    offset = getOverrideTarget( offset );
    try
    {
      _editor.getDocument().insertString( offset, "override ", null );
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  private void fixCase()
  {

    ReplaceChunk replaceChunk = getReplaceChunk( _sourceOfIssue, _gosuEditor.getText() );
    if( replaceChunk != null )
    {
      try
      {
        int offsetShift = _gosuEditor.getParser().getOffsetShift();
        _editor.getDocument().remove( replaceChunk.offset + offsetShift, replaceChunk.length );
        _editor.getDocument().insertString( replaceChunk.offset + offsetShift, replaceChunk.replaceText, null );
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
    }
  }

  private void fixReturnType()
  {
    IType type = getReturnTypeFromPartialStatement( _sourceOfIssue );
    IParsedElement functionStatement = _sourceOfIssue.findAncestorParsedElementByType( IFunctionStatement.class );
    if( functionStatement != null )
    {
      List<IParseTree> trees = functionStatement.getLocation().getChildren();
      for( IParseTree tree : trees )
      {
        if( tree.getParsedElement() instanceof IStatementList )
        {
          int offset = tree.getOffset();
          try
          {

            String prefix;
            if( _editor.getDocument().getText( offset - 1, 1 ).equals( " " ) )
            {
              prefix = ": ";
            }
            else
            {
              prefix = " : ";
            }
            _editor.getDocument().insertString( offset, prefix + type.getRelativeName() + " ", null );
          }
          catch( BadLocationException e )
          {
            //ignore
          }
          break;
        }
      }
    }
  }

  private void generateConstructors()
  {
    IType supertype = ((IClassStatement)_sourceOfIssue).getGosuClass().getSupertype();
    String text = _editor.getText();
    int insertionPoint = text.indexOf( "\n", text.indexOf( "{", _sourceOfIssue.getLocation().getOffset() ) ) + 1;
    List<? extends IConstructorInfo> constructors = supertype.getTypeInfo().getConstructors();
    try
    {
      for( IConstructorInfo constructor : constructors )
      {
        IParameterInfo[] parameterInfos = constructor.getParameters();
        StringBuilder constructorText = new StringBuilder( "  construct(" );
        for( IParameterInfo parameterInfo : parameterInfos )
        {
          constructorText.append( parameterInfo.getName() ).append( " : " ).append( parameterInfo.getFeatureType().getRelativeName() ).append( ", " );
        }
        if( parameterInfos.length > 0 )
        {
          constructorText.setLength( constructorText.length() - 2 );
        }
        constructorText.append( ") {\n" +
                                "    super(" );
        for( IParameterInfo parameterInfo : parameterInfos )
        {
          constructorText.append( parameterInfo.getName() ).append( ", " );
        }
        if( parameterInfos.length > 0 )
        {
          constructorText.setLength( constructorText.length() - 2 );
        }
        constructorText.append( ")\n" +
                                "  }\n" );
        _editor.getDocument().insertString( insertionPoint, constructorText.toString(), null );
        insertionPoint += constructorText.length();
      }
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  private void generateSuperCall()
  {
    IType supertype = ((IFunctionStatement)_sourceOfIssue).getDynamicFunctionSymbol().getDeclaringTypeInfo().getOwnersType().getSupertype();
    List<? extends IConstructorInfo> constructors = supertype.getTypeInfo().getConstructors();
    if( constructors.size() == 1 )
    {
      IConstructorInfo constructor = constructors.get( 0 );
      generateSuperCall( constructor );
    }
    else
    {
      Rectangle caretRect;
      _selectionPopup = SelectClassToImportPopup.instance();
      hidePopup();
      final Map<String, IConstructorInfo> ctorMap = new HashMap<>();
      LinkedHashSet<String> ctorStrs = new LinkedHashSet<>();
      for( IConstructorInfo constructor : constructors )
      {
        ctorStrs.add( TypeInfoUtil.getParameterDisplay( constructor ) );
        ctorMap.put( TypeInfoUtil.getParameterDisplay( constructor ), constructor );
      }
      try
      {
        caretRect = getEditor().modelToView( getEditor().getCaretPosition() );
        if( caretRect != null )
        {
          SwingUtilities.convertRectangle( getEditor(), caretRect, null );
        }
      }
      catch( BadLocationException e )
      {
        throw new RuntimeException( e );
      }
      _selectionPopup.show( getEditor(), caretRect, ctorStrs, className -> generateSuperCall( ctorMap.get( className ) ),
                            "Select constructor", new CtorCellRenderer( _selectionPopup::getList ) );
    }
  }

  private void generateSuperCall( IConstructorInfo constructor )
  {
    int insertPos = _editor.getText().indexOf( "{", _sourceOfIssue.getLocation().getOffset() ) + 1;
    int firstParamStart = -1, firstParamEnd = -1;
    StringBuilder superCall = new StringBuilder( "\n    super(" );
    for( IParameterInfo parameterInfo : constructor.getParameters() )
    {
      if( firstParamStart == -1 )
      {
        firstParamStart = insertPos + superCall.length();
        firstParamEnd = firstParamStart + parameterInfo.getName().length();
      }
      superCall.append( parameterInfo.getName() ).append( ", " );
    }
    if( constructor.getParameters().length > 0 )
    {
      superCall.setLength( superCall.length() - 2 );
    }
    superCall.append( ")" );
    try
    {
      _editor.getDocument().insertString( insertPos, superCall.toString(), null );
      if( firstParamStart != -1 )
      {
        _editor.setSelectionStart( firstParamStart );
        _editor.setSelectionEnd( firstParamEnd );
      }
    }
    catch( BadLocationException e )
    {
      throw new RuntimeException( e );
    }
  }

  private int getOverrideTarget( int offset )
  {
    int functionLoc = _editor.getText().indexOf( "function", offset );
    int propertyLoc = _editor.getText().indexOf( "property", offset );
    if( functionLoc != -1 && (functionLoc < propertyLoc || propertyLoc == -1) )
    {
      return functionLoc;
    }
    else
    {
      return propertyLoc;
    }
  }

//  private void createDisplayKeyFromStringLiteral()
//  {
//    JTextComponent jTextComponent = _editor;
//    try
//    {
//      Rectangle rectangle = jTextComponent.modelToView( jTextComponent.getCaretPosition() );
//      IParseTree locationToReplace = _stringLiteralLocationToReplace;
//      String s = _editor.getText();
//
//      ArrayList<String> nearbyDisplayKeys = new ArrayList<String>();
//      Pattern p = Pattern.compile( "displaykey\\.[a-zA-Z.]*" );
//      Matcher matcher = p.matcher( s );
//      while( matcher.find() )
//      {
//        nearbyDisplayKeys.add( matcher.group().substring( DISPLAYKEY_START.length() ) );
//      }
//
//      String value = _editor.getDocument().getText( locationToReplace.getOffset(), locationToReplace.getLength() );
//      value = StringUtils.strip( value, "\"" );
//      ExternalDisplayKeyDialog dialog = new ExternalDisplayKeyDialog( null, value, jTextComponent, rectangle.x, rectangle.y, nearbyDisplayKeys.toArray( new String[nearbyDisplayKeys.size()] ) );
//      if( dialog.createdKey() )
//      {
//        String keyName = dialog.getDisplayKeyTypeName();
//        _editor.getDocument().remove( locationToReplace.getOffset(), locationToReplace.getLength() );
//        _editor.getDocument().insertString( locationToReplace.getOffset(), keyName, null );
//        _gosuEditor.parse();
//      }
//    }
//    catch( BadLocationException e )
//    {
//      // ignore
//    }
//  }
//
//  private void createDisplayKey()
//  {
//    try
//    {
//      JTextComponent jTextComponent = _editor;
//      Rectangle rectangle = jTextComponent.modelToView( jTextComponent.getCaretPosition() );
//      new ExternalDisplayKeyDialog( _possibleDisplayKey, "", jTextComponent, rectangle.x,  rectangle.y );
//      _gosuEditor.parse();
//    }
//    catch( BadLocationException e )
//    {
//      //ignore
//    }
//  }

  private boolean offerPassiveFix()
  {
    return false;
  }

//  private boolean offerToTurnStringLiteralToDisplayKey( IParseTree locationAtCaret )
//  {
//    if( locationAtCaret != null && locationAtCaret.getParsedElement() instanceof IStringLiteralExpression && !locationAtCaret.getParsedElement().hasParseExceptions() )
//    {
//      IType[] contextTypes = ((IStringLiteralExpression) locationAtCaret.getParsedElement()).getContextTypes();
//      if (contextTypes != null) {
//        for (IType type : contextTypes) {
//          if (type instanceof ITypeList) {
//            // typekey comparison, don't offer to turn into a display key
//            return false;
//          }
//        }
//      }
//
//      showSmartFix( locationAtCaret.getParsedElement(), StudioDisplayKeys.resolve("SmartFixManager.Convert_string_literal_to_DisplayK") + " (" + SHORTCUT + ")" );
//
//      _stringLiteralLocationToReplace = locationAtCaret;
//      _mode = SmartFixMode.CONVERT_STRING_TO_DISPLAY_KEY;
//      return true;
//    }
//    return false;
//  }

  private void setMode( SmartFixMode mode )
  {
    if( isModeAvailable( mode ) )
    {
      _mode = mode;
    }
  }

  private void fixImplicitCast()
  {
    try
    {
      IParsedElement expr = _peToFixWithAsStatement;
      if( expr.getParent() instanceof IImplicitTypeAsExpression )
      {
        expr = expr.getParent();
      }
      String to = _typeToCoerceTo;
      IParsedElement parent = expr.getParent();
      if( parent == null ||
          parent instanceof IStatement ||
          parent instanceof IBeanMethodCallExpression ||
          parent instanceof IMethodCallExpression )
      {
        _editor.getDocument().insertString( expr.getLocation().getExtent() + 1, " as " + to, null );
      }
      else
      {
        _editor.getDocument().insertString( expr.getLocation().getExtent() + 1, " as " + to + ")", null );
        _editor.getDocument().insertString( expr.getLocation().getOffset(), "(", null );
      }
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private void fixConstructorSyntax()
  {
    try
    {
      Document document = _editor.getDocument();

      document.remove( _obsoleteCtorStart, _obsoleteCtorEnd - _obsoleteCtorStart );
      document.insertString( _obsoleteCtorStart, Keyword.KW_construct.toString(), null );
      _editor.setCaretPosition( _obsoleteCtorStart + Keyword.KW_construct.toString().length() );
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private void fixJavaStyleCast()
  {
    try
    {
      Document document = _editor.getDocument();
      IParseTree location = _javaStyleCast.getLocation();
      int pointToRemoveFrom = location.getOffset();
      int lengthToDelete = location.getLength();
      if( pointToRemoveFrom > 0 && document.getText( pointToRemoveFrom - 1, 1 ).equals( " " ) )
      {
        pointToRemoveFrom -= 1;
        lengthToDelete += 1;
      }
      int startPosition = _peToFixWithAsStatement.getLocation().getOffset() - lengthToDelete;
      int castInsertionPoint = _peToFixWithAsStatement.getLocation().getExtent() - lengthToDelete + 1;
      String coercionString = " as " + _typeToCoerceTo;

      document.remove( pointToRemoveFrom, lengthToDelete );
      document.insertString( castInsertionPoint, coercionString, null );
      _editor.setCaretPosition( startPosition );
    }
    catch( BadLocationException e )
    {
      //ignore
    }
  }

  private void fixImport()
  {
    Set<String> possibleTypesToImport = _possibleTypesToImport;
    if( possibleTypesToImport != null )
    {
      if( possibleTypesToImport.size() == 1 )
      {
        _gosuEditor.addToUses( possibleTypesToImport.iterator().next() );
      }
      else
      {
        try
        {
          Rectangle rectangle = getLocationFromOffset( _editor.getCaretPosition() );
          SelectClassToImportPopup.instance().show( _editor, rectangle, possibleTypesToImport, _gosuEditor::addToUses,
                                                    "Select class to import", new TypeCellRenderer( SelectClassToImportPopup.instance().getList() ) );
        }
        catch( BadLocationException e )
        {
          //ignore
        }
      }
    }
  }

  /**
   * Updates the state of the SmartFixManager, which may display tool tips and offer to fix issues in the
   * gosu program.
   */
  public void updateState()
  {
/*
    if( _editor.getCaretPosition() == _iCaretPos )
    {
      if( isOtherPopupShowing() )
      {
        resetSmartHelpState();
      }      
      return;
    }

*/
    resetSmartHelpState();

    if( isOtherPopupShowing() )
    {
      return; //don't clobber existing popups
    }

    //Iterate through all the closest issues within one line of the current cursor and offer to fix the closest one
    final List<IParseIssue> parseIssues = findParseIssuesOrderedByDistanceFromCaret( 1 );
    setMode( SmartFixMode.NONE );
    for( Highlighter.Highlight highlight : _editor.getHighlighter().getHighlights() )
    {
      if( highlight.getPainter() instanceof SmartFixHighlightPainter )
      {
        _editor.getHighlighter().removeHighlight( highlight );
      }
    }
    final Set<String> processed = new HashSet<String>();
//    if (isModeAvailable(SmartFixMode.CONVERT_STRING_TO_DISPLAY_KEY)) {
//      int lineNum = _gosuEditor.getLineNumberAtCaret();
//      Element line = _gosuEditor.getEditor().getDocument().getDefaultRootElement().getElement(lineNum - 1);
//      try {
//        if (_gosuEditor.getEditor().getText(line.getStartOffset(), line.getEndOffset() - line.getStartOffset()).contains("\"")) {
//          IParseTree locationAtCaret = _gosuEditor.getDeepestLocationAtCaret();
//          if (offerToTurnStringLiteralToDisplayKey(locationAtCaret)) {
//            _stringLiteralLocationToReplace = locationAtCaret;
//            return;
//          }
//        }
//      } catch (BadLocationException e) {
//        // ignore
//      }
//    }

    final ITypeUsesMap[] uses = new ITypeUsesMap[]{_gosuEditor.getTypeUsesMapFromMostRecentParse()};

    Runnable runnable = new Runnable()
    {
      @Override
      public void run()
      {
        if( uses[0] == null )
        {
          _gosuEditor.waitForParser();
          uses[0] = _gosuEditor.getTypeUsesMapFromMostRecentParse();
        }
        for( IParseIssue parseIssue : parseIssues )
        {
          IParsedElement source = parseIssue.getSource();
          if( isModeAvailable( SmartFixMode.IMPORT ) && _gosuEditor.acceptsUses() && handlePossibleImportFix( source, uses[0], processed ) )
          {
            setMode( SmartFixMode.IMPORT );
            return;
          }
          else if( isModeAvailable( SmartFixMode.FIX_IMPLICIT_CAST ) && isImplictCoercion( parseIssue ) )
          {
            setMode( SmartFixMode.FIX_IMPLICIT_CAST );
            return;
          }
          else if( isModeAvailable( SmartFixMode.FIX_CTOR_SYNTAX ) && isObsoleteConstructor( parseIssue ) )
          {
            setMode( SmartFixMode.FIX_CTOR_SYNTAX );
            return;
          }
          else if( isModeAvailable( SmartFixMode.FIX_JAVA_STYLE_CAST ) && isJavaStyleCast( parseIssue ) )
          {
            setMode( SmartFixMode.FIX_JAVA_STYLE_CAST );
            return;
          }
//          else if( isModeAvailable(SmartFixMode.CREATE_DISPLAY_KEY) && isPossibleDisplayKey( parseIssue ) )
//          {
//            setMode( SmartFixMode.CREATE_DISPLAY_KEY );
//            return;
//          }
          if( isModeAvailable( SmartFixMode.ADD_MISSING_OVERRIDE ) && isMissingOverride( parseIssue ) )
          {
            setMode( SmartFixMode.ADD_MISSING_OVERRIDE );
            return;
          }
          else if( isModeAvailable( SmartFixMode.FIX_CASE ) && isCaseIssue( parseIssue ) )
          {
            setMode( SmartFixMode.FIX_CASE );
            return;
          }
          else if( isModeAvailable( SmartFixMode.FIX_RETURN_TYPE ) && isVoidReturnTypeIssue( parseIssue ) )
          {
            setMode( SmartFixMode.FIX_RETURN_TYPE );
            return;
//          else if( isModeAvailable( SmartFixMode.IMPL_FUNCTIONS ) && showImplementPopup(parseIssue))
//          {
//            setMode( SmartFixMode.IMPL_FUNCTIONS );
//            return;
          }
          else if( isModeAvailable( SmartFixMode.GENERATE_CONSTRUCTORS ) && isMissingConstructor( parseIssue ) )
          {
            setMode( SmartFixMode.GENERATE_CONSTRUCTORS );
            return;
          }
          else if( isModeAvailable( SmartFixMode.GENERATE_SUPER_CALL ) && isMissingSuperCall( parseIssue ) )
          {
            setMode( SmartFixMode.GENERATE_SUPER_CALL );
            return;
          }
        }
      }
    };

    editor.util.EditorUtilities.doBackgroundOp( runnable );
  }

  private boolean isMissingConstructor( IParseIssue parseIssue )
  {
    if( parseIssue.getMessageKey() == Res.MSG_NO_DEFAULT_CTOR_IN )
    {
      _sourceOfIssue = parseIssue.getSource();
      if( _sourceOfIssue instanceof IClassStatement )
      {
        showSmartFix( _sourceOfIssue, "Generate constructors" );
        return true;
      }
    }
    return false;
  }

  private boolean isMissingSuperCall( IParseIssue parseIssue )
  {
    if( parseIssue.getMessageKey() == Res.MSG_NO_DEFAULT_CTOR_IN )
    {
      _sourceOfIssue = parseIssue.getSource();
      if( _sourceOfIssue instanceof IFunctionStatement )
      {
        showSmartFix( _sourceOfIssue, "Generate super call" );
        return true;
      }
    }
    return false;
  }

  private boolean isCaseIssue( IParseIssue parseIssue )
  {
    if( parseIssue instanceof ParseWarning )
    {
      if( isCaseParseIssue( parseIssue ) )
      {
        _sourceOfIssue = parseIssue.getSource();
        showSmartFix( _sourceOfIssue, "Fix case issue  " + " (" + SHORTCUT + ")" );
        return true;
      }
    }
    return false;
  }

  private boolean isVoidReturnTypeIssue( IParseIssue parseIssue )
  {
    if( parseIssue instanceof ParseException )
    {
      if( parseIssue.getMessageKey() == Res.MSG_RETURN_VAL_FROM_VOID_FUNCTION &&
          getReturnTypeFromPartialStatement( parseIssue.getSource() ) != null )
      {
        IType type = getReturnTypeFromPartialStatement( parseIssue.getSource() );
        _sourceOfIssue = parseIssue.getSource();
        showSmartFix( _sourceOfIssue, "Make function return type " + type.getRelativeName() + "? (" + SHORTCUT + ")" );
        return true;
      }
    }
    return false;
  }

  private IType getReturnTypeFromPartialStatement( IParsedElement source )
  {
    if( source instanceof IAssignmentStatement )
    {
      return ((IAssignmentStatement)source).getIdentifier().getType();
    }
    else if( source instanceof IArrayAssignmentStatement )
    {
      return ((IArrayAssignmentStatement)source).getArrayAccessExpression().getType();
    }
    else if( source instanceof IMapAssignmentStatement )
    {
      return ((IMapAssignmentStatement)source).getMapAccessExpression().getType();
    }
    else if( source instanceof IMemberAssignmentStatement )
    {
      return ((IMemberAssignmentStatement)source).getMemberAccess().getType();
    }
    else if( source instanceof INotAStatement )
    {
      return ((INotAStatement)source).getExpression().getType();
    }
    else if( source instanceof IBeanMethodCallStatement )
    {
      return ((IBeanMethodCallStatement)source).getBeanMethodCall().getType();
    }
    else if( source instanceof IMethodCallStatement )
    {
      return ((IMethodCallStatement)source).getMethodCall().getType();
    }
    return null;
  }


  private boolean isModeAvailable( SmartFixMode mode )
  {
    if( mode == SmartFixMode.NONE )
    {
      return true;
    }
//    if(ExperimentProperties.getInstance() != null) {
//      Set<SmartFixMode> disallowed = ExperimentProperties.getInstance().getCodeCompletionOptions().getDisallowedSmartFixModes();
//      if(disallowed.contains(mode)) {
//        return false;
//      }
//    }
//    if (mode.getExcludedViewTypes() != null && StudioShell.GET.instance() != null) {
//      IView activeView = StudioShell.GET.instance().getActiveView();
//      if (activeView != null) {
//        Class<? extends IView> activeViewClass = activeView.getClass();
//        for (Class excludedViewType : mode.getExcludedViewTypes()) {
//          if (excludedViewType.isAssignableFrom(activeViewClass)) {
//            return false;
//          }
//        }
//      }
//    }
    return true;
  }

  private boolean isMissingOverride( IParseIssue parseIssue )
  {
    if( parseIssue.getMessageKey() == Res.MSG_MISSING_OVERRIDE_MODIFIER )
    {
      _sourceOfIssue = parseIssue.getSource();
      if( _sourceOfIssue != null && _sourceOfIssue.getLocation() != null )
      {
        int offset = _sourceOfIssue.getLocation().getOffset();
        int length = _editor.getText().indexOf( "\n", offset ) - offset;
        if( offset <= _editor.getCaretPosition() && offset + length >= _editor.getCaretPosition() )
        {
          showSmartFix( offset, length, "Add missing override?" + " (" + SHORTCUT + ")" );
          return true;
        }
      }
    }
    return false;
  }

//  private boolean isPossibleDisplayKey( IParseIssue issue )
//  {
//    IParsedElement source = issue.getSource();
//    if( source instanceof IExpression ) {
//      while (source.getParent() instanceof IFieldAccessExpression ) {
//        source = source.getParent();
//      }
//      try {
//        int caret = _editor.getCaretPosition();
//        if (source.getLocation().containsOrBorders(caret, false)) {
//          String possibleDisplayKey = _editor.getDocument().getText(source.getLocation().getOffset(), source.getLocation().getLength());
//          if (possibleDisplayKey != null && possibleDisplayKey.startsWith(DISPLAYKEY_START) && possibleDisplayKey.length() > DISPLAYKEY_START.length() + 3) {
//            _possibleDisplayKey = possibleDisplayKey.substring(DISPLAYKEY_START.length());
//            showSmartFix(source, StudioDisplayKeys.resolve("SmartFixManager.Create_display_key___") + " (" + SHORTCUT + ")");
//            return true;
//          }
//        }
//      }
//      catch (BadLocationException e) {
//        //ignore
//      }
//    }
//
//    return false;
//  }

  private boolean isOtherPopupShowing()
  {
    return _gosuEditor.isCompletionPopupShowing();
  }

  private boolean isImplictCoercion( IParseIssue parseIssue )
  {
    if( parseIssue instanceof ImplicitCoercionWarning )
    {
      ImplicitCoercionWarning warning = (ImplicitCoercionWarning)parseIssue;
      IParsedElement element = warning.getSource();
      int caret = _editor.getCaretPosition();
      if( element.getLocation().contains( caret ) || element.getLocation().getExtent() + 1 == caret )
      {
        _peToFixWithAsStatement = warning.getSource();
        _typeToCoerceTo = warning.getTypeToCoerceTo().getName();
        showSmartFix( element, "Coerce to " + _typeToCoerceTo + "? (" + SHORTCUT + ")" );
        return true;
      }
    }

    return false;
  }

  private boolean isObsoleteConstructor( IParseIssue parseIssue )
  {
    if( parseIssue instanceof ObsoleteConstructorWarning )
    {
      ObsoleteConstructorWarning warning = (ObsoleteConstructorWarning)parseIssue;
      IParsedElement element = warning.getSource();
      int offset = element.getLocation().getOffset();
      String text = _editor.getText();
      int nextParen = text.indexOf( '(', offset );
      if( nextParen != -1 )
      {
        String functionDecl = text.substring( offset, nextParen );
        int caret = _editor.getCaretPosition();
        if( caret >= offset && caret <= offset + functionDecl.length() )
        {
          _obsoleteCtorStart = offset;
          _obsoleteCtorEnd = offset + functionDecl.length();
          showSmartFix( _obsoleteCtorStart, _obsoleteCtorEnd - _obsoleteCtorStart, "Convert to new constructor syntax " + " (" + SHORTCUT + ")" );
          return true;
        }
      }
    }

    return false;
  }

  private boolean isJavaStyleCast( IParseIssue parseIssue )
  {
    if( parseIssue.getMessageKey() == Res.MSG_LIKELY_JAVA_CAST )
    {
      IParsedElement source = parseIssue.getSource();
      IParenthesizedExpression parenthesizedExpr;
      if( source instanceof IParenthesizedExpression )
      {
        parenthesizedExpr = (IParenthesizedExpression)source;
      }
      else
      {
        parenthesizedExpr = (IParenthesizedExpression)((IImplicitTypeAsExpression)source).getLHS();
      }
      int caret = _editor.getCaretPosition();
      if( parenthesizedExpr.getLocation().contains( caret ) || parenthesizedExpr.getLocation().getExtent() + 1 == caret )
      {
        IParseTree nextSibling = parenthesizedExpr.getParent().getLocation().getNextSibling();
        if( nextSibling != null && parenthesizedExpr.getLocation().getLineNum() == nextSibling.getLineNum() )
        {
          _javaStyleCast = parenthesizedExpr;
          _peToFixWithAsStatement = nextSibling.getDeepestFirstChild().getParsedElement();
          _typeToCoerceTo = ((IMetaType)parenthesizedExpr.getType()).getType().getName();
          showSmartFix( parenthesizedExpr, "This appears to be a java-style cast.  Shall I convert it to a Gosu style cast?" + " (" + SHORTCUT + ")" );
          return true;
        }
      }
    }

    return false;
  }

  public void resetSmartHelpState()
  {
    _possibleTypesToImport = null;
    _peToFixWithAsStatement = null;
    _typeToCoerceTo = null;
    _javaStyleCast = null;
    _possibleDisplayKey = null;
    _stringLiteralLocationToReplace = null;
    if( _managerPopup != null )
    {
      _managerPopup.setVisible( false );
      _managerPopup = null;
    }
  }

  private boolean handlePossibleImportFix( IParsedElement source, ITypeUsesMap typeUses, Set<String> processed )
  {
    String relativeTypeName = null;

    //If this is a type literal, dig through it to find a reasonable type to attempt to import
    if( source instanceof ITypeLiteralExpression )
    {
      ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression)source;
      IType iIntrinsicType = typeLiteral.getType().getType();
      if( iIntrinsicType.isArray() )
      {
        iIntrinsicType = iIntrinsicType.getComponentType();
      }
      if( iIntrinsicType instanceof IErrorType )
      {
        IErrorType errorType = (IErrorType)iIntrinsicType;
        if( !errorType.getErrantTypeName().equals( IErrorType.NAME ) )
        {
          relativeTypeName = errorType.getErrantTypeName();
        }
      }
    }

    //If this is an identifier, use its symbols name
    if( source instanceof IIdentifierExpression )
    {
      IIdentifierExpression identifier = (IIdentifierExpression)source;
      if( identifier.getType() instanceof IErrorType )
      {
        ISymbol symbol = identifier.getSymbol();
        if( symbol != null )
        {
          relativeTypeName = symbol.getName();
        }
      }
    }

    //If this is a bean method call expression, look to see if the error is a symbol not found and the
    //symbol name is a valid type
    if( source instanceof INotAStatement )
    {
      INotAStatement nas = (INotAStatement)source;
      if( nas.getExpression() != null &&
          nas.getExpression() instanceof IIdentifierExpression &&
          ((IIdentifierExpression)nas.getExpression()).getSymbol() != null )
      {
        relativeTypeName = ((IIdentifierExpression)nas.getExpression()).getSymbol().getName();
      }
    }

    //If we found a possible relative name, see if it is a possible valid type
    if( relativeTypeName != null && !processed.contains( relativeTypeName ) )
    {
      processed.add( relativeTypeName );
      try
      {
        Rectangle rectangle = getLocationFromOffset( source.getLocation().getOffset() );

        if( _editor.getVisibleRect().contains( rectangle ) || rectangle == TEST_RECTANGLE )
        {
          // See if the type is already available through the type-uses map of the parser
          IType type = typeUses.resolveType( relativeTypeName );
          if( type == null )
          {
            // Detect if the type resolves as a top level type
            try
            {
              type = TypeSystem.getByFullName( relativeTypeName );
            }
            catch( Exception e )
            {
              // ignore
            }
          }

          TreeSet<String> possibleTypesToImport = new TreeSet<>( new TypeNameComparator() );
          if( type == null )
          {
            List<String> fullyQualifiedNames = LabFrame.instance().getGosuPanel().getTypeNamesCache().getFullyQualifiedClassNameFromRelativeName( relativeTypeName );
            if( fullyQualifiedNames != null )
            {
              for( CharSequence fullyQualifiedName : fullyQualifiedNames )
              {
                possibleTypesToImport.add( fullyQualifiedName.toString() );
              }
            }
          }

          if( possibleTypesToImport.size() > 0 )
          {
            String displayText = possibleTypesToImport.size() == 1
                                 ? possibleTypesToImport.iterator().next() + "? (" + SHORTCUT + ")"
                                 : "Multiple Matches...(" + SHORTCUT + ")";
            _possibleTypesToImport = possibleTypesToImport;
            showSmartFix( source, displayText );
            return true;
          }
        }
      }
      catch( BadLocationException e )
      {
        //ignore
      }
    }

    return false;
  }

  private Rectangle getLocationFromOffset( int i ) throws BadLocationException
  {
    Rectangle rectangle = _editor.modelToView( i );
    if( rectangle == null )
    {
      rectangle = TEST_RECTANGLE;
    }
    return rectangle;
  }

  private void showSmartFix( IParsedElement source, String displayText )
  {
    showSmartFix( source.getLocation().getOffset(), source.getLocation().getLength(), displayText );
  }

  public void showSmartFix( final int offset, final int length, final String displayText )
  {
    Runnable runnable = () -> {
      try
      {
        _offset = offset;
        _length = length;
        Rectangle rectangle = getLocationFromOffset( _offset );
        if( rectangle != TEST_RECTANGLE && _editor.isShowing() )
        {
          _managerPopup = new SmartFixPopup( displayText );
        }
        _editor.getHighlighter().addHighlight( _offset, _offset + _length, new SmartFixHighlightPainter( SMARTFIX_HIGHLIGHT_COLOR ) );
      }
      catch( BadLocationException e )
      {
        resetSmartHelpState();  //The user must have cleared out the given smart fix.
      }
    };
    SwingUtilities.invokeLater( runnable );
  }

  private List<IParseIssue> findParseIssuesOrderedByDistanceFromCaret( int maxLines )
  {
    ArrayList<IParseIssue> issues = new ArrayList<IParseIssue>();

    int caretPosition = _editor.getCaretPosition();

    final int line = TextComponentUtil.getLineAtPosition( _editor, caretPosition );
    final int col = TextComponentUtil.getColumnAtPosition( _editor, caretPosition );

    ParseResultsException pe = _gosuEditor.getParseResultsException();
    if( pe != null )
    {
      for( IParseIssue parseIssue : pe.getParseIssues() )
      {
        if( Math.abs( parseIssue.getLine() - line ) <= maxLines )
        {
          issues.add( parseIssue );
        }
      }
    }

    Collections.sort( issues, ( o1, o2 ) -> {
      double d1 = getDistanceFromPosition( o1, line, col );
      double d2 = getDistanceFromPosition( o2, line, col );
      if( d1 > d2 )
      {
        return 1;
      }
      else if( d1 < d2 )
      {
        return -1;
      }
      else
      {
        return 0;
      }
    } );

    return issues;
  }

  /**
   * Returns the cartesian distance of this parse issue from the given column/line
   * in column/line units
   */
  private double getDistanceFromPosition( IParseIssue pi, int line, int col )
  {
    if( pi.getSource() != null && pi.getSource().getLocation() != null )
    {
      int squaredDist = ((pi.getSource().getLocation().getLineNum() - line) * (pi.getSource().getLocation().getLineNum() - line)) +
                        ((pi.getSource().getLocation().getColumn() - col) * (pi.getSource().getLocation().getColumn() - col));
      return Math.sqrt( squaredDist );
    }
    else
    {
      return Double.MAX_VALUE;
    }
  }


  public SmartFixMode getMode()
  {
    return _mode;
  }

  public Set<String> getPossibleTypesToImport()
  {
    return _possibleTypesToImport;
  }

  public IParsedElement getPeToFixWithAsStatement()
  {
    return _peToFixWithAsStatement;
  }

  public IParsedElement getJavaStyleCast()
  {
    return _javaStyleCast;
  }

  public String getTypeToCoerceTo()
  {
    return _typeToCoerceTo;
  }

  @Override
  public void mouseDragged( MouseEvent e )
  {
  }

  @Override
  public void mouseMoved( MouseEvent e )
  {
    if( _managerPopup != null )
    {
      if( getTargetBounds().contains( e.getPoint() ) )
      {
        if( !_managerPopup.isShowing() && _selectionPopup == null )
        {
          bufferShowPopup( true );
        }
      }
      else if( _managerPopup.isShowing() )
      {
        hidePopup();
      }
    }
  }

  private void hidePopup()
  {
    if( _managerPopup != null )
    {
      _managerPopup.setVisible( false );
    }
    if( _timer != null )
    {
      if( _timer.isRunning() )
      {
        _timer.stop();
      }
      _timer = null;
    }
  }

  private void bufferShowPopup( boolean restartIfActive )
  {
    if( _timer == null )
    {
      _timer = new Timer( 500, e -> SwingUtilities.invokeLater( this::showPopup ) );
      _timer.setRepeats( false );
      _timer.start();
    }
    else if( restartIfActive )
    {
      _timer.restart();
    }
  }

  @Override
  public void keyTyped( KeyEvent e )
  {
  }

  @Override
  public void keyPressed( KeyEvent e )
  {
    if( _managerPopup != null )
    {
      if( e.getKeyCode() == KeyEvent.VK_ALT && !_managerPopup.isShowing() )
      {
        bufferShowPopup( false );
      }
    }
  }

  @Override
  public void keyReleased( KeyEvent e )
  {
    if( _managerPopup != null )
    {
      if( e.getKeyCode() == KeyEvent.VK_ALT )
      {
        hidePopup();
      }
    }
  }

  private void showPopup()
  {
    if( _managerPopup != null )
    {
      try
      {
        if( !SelectClassToImportPopup.instance().isShowing() &&
            (_gosuEditor.getCompletionPopup() == null || !_gosuEditor.getCompletionPopup().isShowing()) &&
            (!(_gosuEditor.getCompletionPopup() instanceof Component) || !_gosuEditor.getCompletionPopup().isShowing()) &&
            (_gosuEditor.getJavadocPopup() == null || !_gosuEditor.getJavadocPopup().isShowing()) )
        {
          _managerPopup.show( _editor, getLocationFromOffset( _offset ) );
        }
      }
      catch( BadLocationException e1 )
      {
        // ignore
      }
    }
  }

  private Rectangle getTargetBounds()
  {
    Rectangle p0;
    Rectangle p1;
    try
    {
      TextUI mapper = _editor.getUI();
      p0 = mapper.modelToView( _editor, _offset );
      p1 = mapper.modelToView( _editor, _offset + _length );
    }
    catch( Exception e )
    {
      return new Rectangle( 0, 0, 0, 0 );
    }
    Rectangle bounds = _editor.getBounds();

    if( p0.y == p1.y )
    {
      // same line, render a rectangle
      Rectangle r1 = p0.union( p1 );
      r1.grow( 20, 20 );
      return r1;
    }
    else
    {
      // different lines
      int p0ToMarginWidth = bounds.x + bounds.width - p0.x;
      Rectangle r1 = new Rectangle( p0.x, p0.y, p0ToMarginWidth, p0.height );
      if( (p0.y + p0.height) != p1.y )
      {
        r1 = r1.union( new Rectangle( bounds.x, p0.y + p0.height, bounds.width, p1.y - (p0.y + p0.height) ) );
      }
      r1 = r1.union( new Rectangle( bounds.x, p1.y, (p1.x - bounds.x), p1.height ) );
      r1.grow( 20, 20 );
      return r1;
    }
  }

  private class SmartFixHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
  {
    public SmartFixHighlightPainter( Color c )
    {
      super( c );
    }

    @Override
    public Shape paintLayer( Graphics g, int p0, int p1, Shape shape, JTextComponent c, View view )
    {
      if( shape == null )
      {
        return null;
      }
      Shape result = null;
      try
      {
        result = view.modelToView(
          Math.min( p0, p1 ),
          javax.swing.text.Position.Bias.Forward,
          Math.max( p0, p1 ),
          javax.swing.text.Position.Bias.Backward,
          shape );
        Rectangle bounds = result.getBounds();
        drawWavyLine( g, bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width - 1 );
      }
      catch( final BadLocationException e )
      {
        // ignore it
      }
      return result;
    }

    protected void drawWavyLine( Graphics g, int x, int y, int x2 )
    {
      Color oldColor = g.getColor();
      try
      {
        g.setColor( getColor() );

        int wavyLineWidth = x2 - x;
        if( wavyLineWidth > 0 )
        {
          int[] wf = {0, +1, 0, -1};
          int[] xPoints = new int[wavyLineWidth + 1];
          int[] yPoints = new int[wavyLineWidth + 1];

          for( int i = 0; i <= wavyLineWidth; i++ )
          {
            xPoints[i] = x + i;
            yPoints[i] = y + wf[i % 4];
          }
          g.drawPolyline( xPoints, yPoints, wavyLineWidth );
        }
      }
      finally
      {
        g.setColor( oldColor );
      }

    }
  }

  //==================================================================================
  // Common methods shared with BulkFixManager
  //==================================================================================
  public static boolean isCaseParseIssue( IParseIssue parseIssue )
  {
    boolean caseIssue = parseIssue.getMessageKey() == Res.MSG_VAR_CASE_MISMATCH ||
                        parseIssue.getMessageKey() == Res.MSG_PROPERTY_CASE_MISMATCH ||
                        parseIssue.getMessageKey() == Res.MSG_TYPE_CASE_MISMATCH ||
                        parseIssue.getMessageKey() == Res.MSG_FUNCTION_CASE_MISMATCH;

    IParsedElement sourceOfIssue = parseIssue.getSource();
    boolean fixableElement = sourceOfIssue instanceof IIdentifierExpression ||
                             sourceOfIssue instanceof IBeanMethodCallExpression ||
                             sourceOfIssue instanceof ITypeLiteralExpression ||
                             sourceOfIssue instanceof ISynthesizedMemberAccessExpression ||
                             sourceOfIssue instanceof IFieldAccessExpression;

    return caseIssue && fixableElement;
  }

  public static ReplaceChunk getReplaceChunk( IParsedElement sourceOfIssue, String gosuSource )
  {
    ReplaceChunk returnChunk = new ReplaceChunk();

    if( sourceOfIssue instanceof IIdentifierExpression )
    {
      returnChunk.offset = sourceOfIssue.getLocation().getOffset();
      returnChunk.length = sourceOfIssue.getLocation().getLength();
      returnChunk.replaceText = ((IIdentifierExpression)sourceOfIssue).getSymbol().getName();
    }
    else if( sourceOfIssue instanceof IBeanMethodCallExpression )
    {
      returnChunk.offset = ((IBeanMethodCallExpression)sourceOfIssue).getStartOffset();
      returnChunk.replaceText = ((IBeanMethodCallExpression)sourceOfIssue).getFunctionType().getName();
      returnChunk.length = returnChunk.replaceText.length();
    }
    else if( sourceOfIssue instanceof ITypeLiteralExpression )
    {
      if( gosuSource == null )
      {
        throw new IllegalArgumentException( "The original source must be passed into getReplaceChunk() so that it can determine what text to replace" );
      }
      String literalToReplace = gosuSource.substring( sourceOfIssue.getLocation().getOffset(), sourceOfIssue.getLocation().getExtent() + 1 );
      int openCaret = literalToReplace.indexOf( '<' );
      if( openCaret != -1 )
      {
        literalToReplace = gosuSource.substring( 0, openCaret );
      }
      returnChunk.offset = sourceOfIssue.getLocation().getOffset();
      IType type = ((ITypeLiteralExpression)sourceOfIssue).getType().getType();
      if( type.isParameterizedType() )
      {
        type = type.getGenericType();
      }
      String typeName = type.getName();
      returnChunk.replaceText = typeName.substring( Math.max( 0, typeName.length() - literalToReplace.length() ) );
      returnChunk.length = returnChunk.replaceText.length();
    }
    else if( sourceOfIssue instanceof ISynthesizedMemberAccessExpression )
    {
      ISynthesizedMemberAccessExpression access = (ISynthesizedMemberAccessExpression)sourceOfIssue;
      returnChunk.offset = access.getStartOffset();
      returnChunk.replaceText = ((IFieldAccessExpression)sourceOfIssue).getPropertyInfo().getName();
      returnChunk.length = access.getLocation().getExtent() - access.getStartOffset() + 1;
    }
    else if( sourceOfIssue instanceof IFieldAccessExpression )
    {
      returnChunk.offset = ((IFieldAccessExpression)sourceOfIssue).getStartOffset();
      returnChunk.replaceText = ((IFieldAccessExpression)sourceOfIssue).getPropertyInfo().getName();
      returnChunk.length = returnChunk.replaceText.length();
    }
    else
    {
      return null;
    }

    return returnChunk;
  }


  public static class ReplaceChunk
  {
    public int offset;
    public int length;
    public String replaceText;
  }

  private static class TypeNameComparator implements Comparator<String>
  {
    @Override
    public int compare( String o1, String o2 )
    {
      int o1category = getCategory( o1 ).ordinal();
      int o2category = getCategory( o2 ).ordinal();
      if( o1category != o2category )
      {
        return o1category - o2category;
      }
      int idx = 0;
      while( idx < o1.length() && idx < o2.length() )
      {
        char o1ch = o1.charAt( idx );
        char o2ch = o2.charAt( idx );
        idx++;
        if( o1ch == o2ch )
        {
          continue;
        }
        if( Character.isUpperCase( o1ch ) && !Character.isUpperCase( o2ch ) )
        {
          return -1;
        }
        else if( Character.isUpperCase( o2ch ) && !Character.isUpperCase( o1ch ) )
        {
          return 1;
        }
        return o1ch - o2ch;
      }
      return o1.length() - o2.length();
    }

    private ITypeCategory getCategory( String typeName )
    {
      if( typeName.startsWith( "java.lang." ) )
      {
        return ITypeCategory.JAVA; // Use case: java.lang.Double shows at top of list
      }
      if( typeName.startsWith( "java.util." ) )
      {
        return ITypeCategory.JAVA; // Use case: java.util.Date shows at top of list
      }
      if( typeName.equals( "javax.xml.namespace.QName" ) )
      {
        return ITypeCategory.QNAME; // QName is prevalent in new XML system - move to top of list
      }
      return ITypeCategory.NORMAL;
    }

    private static enum ITypeCategory
    {
      JAVA, QNAME, NORMAL
    }

  }

  private class CtorCellRenderer extends AbstractListCellRenderer<String>
  {
    public CtorCellRenderer( Supplier<JComponent> list )
    {
      super( list, true );
    }

    @Override
    public void configure()
    {
      setText( getNode() );
    }
  }
}
