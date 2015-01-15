/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.AnnotationExpression;
import gw.internal.gosu.parser.expressions.ArithmeticExpression;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.DefaultArgLiteral;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.internal.gosu.parser.expressions.Literal;
import gw.internal.gosu.parser.expressions.ModifierListClause;
import gw.internal.gosu.parser.expressions.NotAWordExpression;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.internal.gosu.parser.expressions.TypeAsExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.ClassFileStatement;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.HideFieldNoOpStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.UsageTarget;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.ICoercer;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IFullParserState;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.INonCapturableSymbol;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParserPart;
import gw.lang.parser.IParserState;
import gw.lang.parser.IResolvingCoercer;
import gw.lang.parser.IScope;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IToken;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.StandardScope;
import gw.lang.parser.exceptions.ImplicitCoercionError;
import gw.lang.parser.exceptions.ImplicitCoercionWarning;
import gw.lang.parser.exceptions.IncompatibleTypeException;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.exceptions.SymbolNotFoundException;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.IOverridableOperation;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.Stack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public abstract class ParserBase implements IParserPart
{
  private static final Object[] EMPTY_ARRAY = new Object[0];

  // Constant used as a placholder value to facilitate lazy parser state initialization for error reporting
  private static final IParserState PLACEHOLDER_PARSER_STATE = new PlaceholderParserState();
  private static final INamespaceType PROGRAM_NAMESPACE = new NamespaceType( IGosuProgram.PACKAGE, null);

  private boolean _snapshotSymbols;
  private GosuParser _owner;
  Stack<BlockExpression> _blocks;
  private IGosuValidator _validator;
  protected int _offsetShift; // For class fragments
  private int _lineNumShift;  // For class fragments
  protected boolean _bDontOptimizeStatementLists;
  private List<IParseTree> _subTree;
  private java.util.Stack<List<IType>> _inferringFunctionTypes;
  private static final NotAWordExpression NOT_SET_EXPRESSION = new NotAWordExpression();
  private Set<ResourceKey> _ignoreWarnings;

  public ParserBase()
  {
    this( null );
  }

  public ParserBase( GosuParser owner )
  {
    _inferringFunctionTypes = new java.util.Stack<List<IType>>();
    _owner = owner;
  }

  public GosuParser getOwner()
  {
    return _owner;
  }

  protected void setOwner( GosuParser owner )
  {
    _owner = owner;
  }

  public void setIgnoreWarnings(Set<ResourceKey> msgKeys) {
    _ignoreWarnings = msgKeys;
  }

  SourceCodeTokenizer getTokenizer()
  {
    return _owner.getTokenizer();
  }

  ISymbolTable getSymbolTable()
  {
    return _owner.getSymbolTable();
  }

  void setLocation( int iOffset, int iLineNum, int iColumn )
  {
    setLocation( iOffset, iLineNum, iColumn, false );
  }

  void setLocation( int iOffset, int iLineNum, int iColumn, boolean bForceRedundancy )
  {
    setLocation( iOffset, iLineNum, iColumn, false, bForceRedundancy );
  }
  void setLocation( int iOffset, int iLineNum, int iColumn, boolean bZeroLength, boolean bForceRedundancy )
  {
    if( iOffset == -1 )
    {
      iOffset = 0;
    }

    if( iOffset < 0 )
    {
      throw new IllegalArgumentException( iOffset + " is out of bounds" );
    }

    ParsedElement e = getOwner().peekParsedElement();
    IToken priorToken = (e instanceof ClassFileStatement) && getTokenizer().isEOF()
                        ? getTokenizer().getCurrentToken() // consume trailing whitespace near EOF
                        : getTokenizer().getPriorToken();
    int iLength = bZeroLength ? 0 : priorToken.getTokenEnd() - iOffset;
    if( iLength < 0 )
    {
      iLength = 0;
      //throw new IllegalStateException( "Parsed element has negative length" );
    }

    //## hack alert: We check for positive length locations because we sometime recycle expressions after back tracking
    //## It would be nice if we could detect that an expression was reused somehow.
    if( e != null && (e.getLocation() == null || (e.getLocation().getLength() > 0 || e instanceof ClassStatement)) )
    {
      ParseTree node = e.initLocation( iOffset, iLength, iLineNum, iColumn, getOwner().getScriptPart() );
      ParseTree location = addLocation( node, bForceRedundancy );
      e.setLocation( location );

      if( _subTree != null )
      {
        try
        {
          for( IParseTree childLocation : _subTree )
          {
            childLocation.addUnder( location );
            childLocation.getParsedElement().setParent( location.getParsedElement() );
          }
        }
        finally
        {
          _subTree = null;
        }
      }
    }
  }

  private ParseTree addLocation( ParseTree location, boolean bForceRedundancy )
  {
    List<ParseTree> locationsList = getLocationsList();
    int iLast = location.getChildren().size();
    for( int i = locationsList.size() - 1; i >= 0; i-- )
    {
      ParseTree l = locationsList.get( i );

      if( l.getParsedElement() == location.getParsedElement() )
      {
        return l;
      }

      if( location.getLength() != 0 &&
          (location.contains( l ) ||
           // zero-length elements that follow the element we're adding are intended to belong
           // to it, but since the elements are of zero-length they did not advance the tokenizer
           // so they may have an offset that is out of the bounds of the intended enclosing element.
           // This here condition allows for zero-length elements to be swallowed up by the intended
           // enclosing element (let's hope).
           (l.getLength() == 0 && l.getOffset() > location.getOffset())) )
      {
        if( !bForceRedundancy && l.contains( location ) )
        {
          if( !(location.getParsedElement() instanceof Statement) ||
              l.getParsedElement() instanceof Statement )
          {
            // Already has an element covering the exact same area.
            // Probably an abstract element. Don't allow redundancy.
            return l;
          }
        }

        ParseTree tree = locationsList.remove(i);
        location.addChild( iLast, tree);
      }
      else
      {
        break;
      }
    }

    if( !(location.getParsedElement() instanceof HideFieldNoOpStatement) )
    {
      locationsList.add( location );
    }
    return location;
  }

  List<ParseTree> getLocationsList()
  {
    return getOwner().getLocationsList();
  }

  protected void pushExpression( Expression e )
  {
    getOwner().pushExpression( e );
  }

  protected void verifyParsedElement( IParsedElement element ) throws ParseResultsException
  {
    verifyParsedElement( element, getOwner().isThrowParseResultsExceptionForWarnings() );
  }
  protected void verifyParsedElement( IParsedElement element, boolean bThrowOnWarnings ) throws ParseResultsException
  {
    final IGosuValidator validator = getValidator();
    if( validator != null )
    {
      validator.validate( element, getScript() );
    }

    // Walk through all parse issues and convert local stuff to pointers into the type (i.e. kill the source, fix the locations, etc...)
    List<IParseIssue> issues = element.getParseIssues();
    boolean hasParseException = false;
    boolean hasParseWarning = false;
    for( IParseIssue issue : issues )
    {
      if( issue instanceof ParseException )
      {
        hasParseException = true;
      }
      else
      {
        hasParseWarning = true;
      }
      issue.resolve( this );
    }

    if( hasParseException || (bThrowOnWarnings && hasParseWarning) )
    {
      ParseResultsException resultsException = new ParseResultsException( element );
      resultsException.setContextType( getOwner().getGosuClass() );
      throw resultsException;
    }
  }

  protected abstract String getScript();

  protected Expression popExpression()
  {
    return getOwner().popExpression();
  }

  protected Expression peekExpression()
  {
    return getOwner().peekExpression();
  }

  protected void pushStatement( Statement stmt )
  {
    getOwner().pushStatement( stmt );
  }

  protected Statement popStatement()
  {
    return getOwner().popStatement();
  }

  protected Statement peekStatement()
  {
    return getOwner().peekStatement();
  }

  boolean eatStatementBlock( ParsedElement parsedElement, ResourceKey errorKey )
  {
    verify( parsedElement, match( null, '{' ), errorKey );
    return eatBlock( '{', '}', false ) != null;
  }

  boolean eatPossibleStatementBlock()
  {
    if( match( null, '{' ) )
    {
      eatBlock( '{', '}', false );
      return true;
    }
    return false;
  }

  boolean eatPossibleEnclosedVarInStmt()
  {
    if( (match( null, Keyword.KW_for ) ||
         match( null, Keyword.KW_foreach ) ||
         match( null, Keyword.KW_exists ) ||
         match( null, Keyword.KW_find ) ||
         match( null, Keyword.KW_using )) &&
        match( null, '(' ) )
    {
      eatBlock( '(', ')', false );
      return true;
    }
    return false;
  }

  void eatParenthesized( ParsedElement parsedElement, ResourceKey errorKey )
  {
    verify( parsedElement, match( null, '(' ), errorKey );
    eatBlock( '(', ')', false );
  }

  void eatPossibleParametarization()
  {
    eatPossibleParametarization( true );
  }

  void eatPossibleParametarization( boolean bMatchStart )
  {
    if( !bMatchStart || match( null, "<", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      eatBlock( '<', '>', true );
    }
  }

  public Token eatBlock( char cBegin, char cEnd, boolean bOperator )
  {
    return eatBlock( cBegin, cEnd, bOperator, getTokenizer() );
  }

  public Token eatBlock( char cBegin, char cEnd, boolean bOperator, boolean bStopAtDeclarationKeyword )
  {
    return eatBlock( cBegin, cEnd, bOperator, bStopAtDeclarationKeyword, getTokenizer() );
  }

  public static Token eatBlock( char cBegin, char cEnd, boolean bOperator, SourceCodeTokenizer tokenizer )
  {
    return eatBlock( cBegin, cEnd, bOperator, false, tokenizer );
  }
  public static Token eatBlock( char cBegin, char cEnd, boolean bOperator, boolean bStopAtDeclarationKeyword, SourceCodeTokenizer tokenizer )
  {
    int iBraceDepth = 1;
    Token endToken = new Token();
    boolean bNewMatched = false;
    do
    {
      if( match( null, null, SourceCodeTokenizer.TT_EOF, false, tokenizer ) )
      {
        return null;
      }

      // Total hack to handle an annotation with an anonymous new expression and blocks (they has declarations that need to be eaten)
      // We're hacking this because soon, real soon now, we'll be dropkicking support for an anonymous new expression or block
      // as an argument to a gosu annotation -- gosu annotations need to behave like java annotation, no more no less.
      bNewMatched = bStopAtDeclarationKeyword && (bNewMatched ||
                                                  match( null, Keyword.KW_new.toString(), SourceCodeTokenizer.TT_KEYWORD, true, tokenizer ) ||
                                                  match( null, "->", SourceCodeTokenizer.TT_OPERATOR, true, tokenizer ) ||
                                                  match( null, "#", SourceCodeTokenizer.TT_OPERATOR, true, tokenizer ));
      if( bStopAtDeclarationKeyword && !bNewMatched && matchDeclarationKeyword( null, true, tokenizer ) )
      {
        return null;
      }

      if( (bOperator && match( endToken, String.valueOf( cEnd ), SourceCodeTokenizer.TT_OPERATOR, false, tokenizer )) ||
          match( endToken, null, (int)cEnd, false, tokenizer ) )
      {
        if( --iBraceDepth == 0 )
        {
          return endToken;
        }
        continue;
      }

      if( (bOperator && match( null, String.valueOf( cBegin ), SourceCodeTokenizer.TT_OPERATOR, false, tokenizer )) ||
          match( null, null, (int)cBegin, false, tokenizer ) )
      {
        iBraceDepth++;
        continue;
      }

      tokenizer.nextToken();
    } while( true );
  }

  boolean eatMemberBlockFromProgramClass( Keyword start )
  {
    int iMark = getTokenizer().mark();
    ModifierInfo modifierInfo = parseModifiers( true );
    if( match( null, start ) )
    {
      if( getOwner().maybeAdvanceTokenizerToEndOfSavedLocation() )
      {
        return true;
      }
    }
    removeAnnotationsFromLocationsList( modifierInfo );
    getTokenizer().restoreToMark( iMark );
    return false;
  }

  private void removeAnnotationsFromLocationsList( ModifierInfo modifierInfo ) {
    List<ParseTree> locs = getLocationsList();
    for( int i = locs.size()-1; i >= 0; i-- )
    {
      ParseTree csr = locs.get( i );
      for( IGosuAnnotation ann: modifierInfo.getAnnotations() )
      {
        if( csr == ann.getExpression().getLocation() )
        {
          locs.remove( i );
        }
      }
    }
  }

  /**
   * Parse a dot separated path as a single logical token
   */
  public void parseDotPathWord( Token t )
  {
    StringBuilder sb = t == null ? null : new StringBuilder( t._strValue == null ? "" : t._strValue );
    while( match( null, '.' ) )
    {
      if( sb != null )
      {
        sb.append( '.' );
      }
      Token T = new Token();
      if( match( T, null, SourceCodeTokenizer.TT_WORD ) || match( T, null, SourceCodeTokenizer.TT_KEYWORD ) )
      {
        if( sb != null )
        {
          sb.append( T._strValue );
        }
      }
    }
    if( sb != null )
    {
      t._strValue = sb.toString();
    }
  }

  /**
   * Possibly matches the specified string token value.  If a match occurs the token will be eaten and its
   * information put into T (if T is not null).
   *
   * @param T     the Token object to populate iff a match is found
   * @param token the string object to match
   *
   * @return true if a match occurred, and false otherwise
   */
  protected boolean match( Token T, String token )
  {
    return match( T, token, 0, false );
  }

  /**
   * Possibly matches the specified token type.  If a match occurs then the token will be eaten
   * and its information put into T (if T is not null).
   *
   * @param T     the Token object to populate iff a match is found
   * @param iType the token "type" to match (e.g. {@link SourceCodeTokenizer#TT_WORD})
   *
   * @return true if a match occurred, and false otherwise
   */
  protected boolean match( Token T, int iType )
  {
    return match( T, null, iType, false );
  }

  /**
   * Possibly matches the specified token or name (in token).  If a match occurs then
   * the token will be eaten and its information put into T (if T is not null).
   *
   * @param T     the Token object to populate iff a match is found
   * @param token the string object to match
   * @param iType the token "type" to match (e.g. {@link SourceCodeTokenizer#TT_WORD})
   *
   * @return true if a match occurred, and false otherwise
   */
  public boolean match( Token T, String token, int iType )
  {
    return match( T, token, iType, false );
  }

  /**
   * Possibly matches the specified token or name (in token).  If a match occurs and bPeek is false then
   * the token will be eaten and its information put into T (if T is not null).
   *
   * @param T     the Token object to populate iff a match is found
   * @param token the string object to match
   * @param iType the token "type" to match (e.g. {@link SourceCodeTokenizer#TT_WORD})
   * @param bPeek if true, a matching token will <b>not</b> be consumed (i.e. the stream will not advance to the next token.)
   *              if false, a matching token will be removed from the front of the stream.
   *
   * @return true if a match occurred, and false otherwise
   */
  public boolean match( Token T, String token, int iType, boolean bPeek )
  {
    SourceCodeTokenizer tokenizer = getTokenizer();
    return match( T, token, iType, bPeek, tokenizer );
  }

  private static boolean match( Token T, String token, int iType, boolean bPeek, SourceCodeTokenizer tokenizer )
  {
    boolean bMatch = false;

    if( T != null )
    {
      tokenizer.copyInto( T );
    }

    int iCurrentType = tokenizer.getType();
    if( token != null )
    {
      if( (iType == iCurrentType) || ((iType == 0) && (iCurrentType == SourceCodeTokenizer.TT_WORD)) )
      {
        bMatch = token.equals( tokenizer.getStringValue() );
      }
    }
    else
    {
      bMatch = (iCurrentType == iType) || isValueKeyword( iType, tokenizer );
    }

    if( bMatch && !bPeek )
    {
      tokenizer.nextToken();
    }

    return bMatch;
  }

  private static boolean isValueKeyword( int iType, SourceCodeTokenizer tokenizer )
  {
    return iType == SourceCodeTokenizer.TT_WORD &&
           tokenizer.getType() == SourceCodeTokenizer.TT_KEYWORD &&
           Keyword.isValueKeyword( tokenizer.getStringValue() );
  }

  protected boolean match( Token T, Keyword token )
  {
    return match( T, token, false );
  }

  boolean match( Token T, Keyword token, boolean bPeek )
  {
    boolean bMatch = false;

    if( T != null )
    {
      getTokenizer().copyInto( T );
    }

    SourceCodeTokenizer tokenizer = getTokenizer();
    if( SourceCodeTokenizer.TT_KEYWORD == tokenizer.getType() )
    {
      bMatch = token.toString().equals( tokenizer.getStringValue() );
    }

    if( bMatch && !bPeek )
    {
      tokenizer.nextToken();
    }

    return bMatch;
  }

  void addError( ParsedElement parsedElement, ResourceKey errorMsg, Object... args )
  {
    verify( parsedElement, false, errorMsg, args );
  }


  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  // Optimizations to avoid creating vararg array

  void addError( ParsedElement parsedElement, ResourceKey errorMsg )
  {
    verify( parsedElement, false, errorMsg, EMPTY_ARRAY );
  }

  boolean verify( ParsedElement parsedElement, boolean bExpression, ResourceKey errorMesg, String arg0 )
  {
    if( !bExpression )
    {
      return verify( parsedElement, bExpression, false, errorMesg, (Object)arg0 );
    }
    return bExpression;
  }

  boolean verify( ParsedElement parsedElement, boolean bExpression, ResourceKey errorMesg, String... args)
  {
    if( !bExpression )
    {
      return verify( parsedElement, bExpression, false, errorMesg, args);
    }
    return bExpression;
  }
  //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


  boolean verify( ParsedElement parsedElement, boolean bExpression, ResourceKey errorMesg, Object... args )
  {
    return verify( parsedElement, bExpression, false, errorMesg, args );
  }

  boolean verify( ParsedElement parsedElement, boolean bExpression, IParserState parserState, ResourceKey errorMesg, Object... args )
  {
    return verify( parsedElement, bExpression, false, false, parserState, errorMesg, args );
  }

  boolean verify( ParsedElement parsedElement, boolean bExpression, boolean bNextTokenIfException, ResourceKey errorMesg, Object... args )
  {
    return verify( parsedElement, bExpression, bNextTokenIfException, false, PLACEHOLDER_PARSER_STATE, errorMesg, args );
  }

  boolean verify( ParsedElement parsedElement, boolean bExpression, boolean bNextTokenIfException, IParserState parserState, ResourceKey errorMesg, Object... args )
  {
    return verify( parsedElement, bExpression, bNextTokenIfException, false, parserState, errorMesg, args );
  }

  boolean warn( ParsedElement target, boolean bExpression, ResourceKey err, Object... args )
  {
    return warn( target, bExpression, makeFullParserState(), err, args );
  }

  boolean warn( ParsedElement target, boolean bExpression, IParserState state, ResourceKey err, Object... args )
  {
    return verify( target, bExpression, false, true, state, err, args );
  }

  boolean verifyOrWarn( ParsedElement target, boolean bExpression, boolean bWarning, ResourceKey err, Object... args )
  {
    return verify( target, bExpression, false, bWarning, PLACEHOLDER_PARSER_STATE, err, args );
  }

  private boolean verify( ParsedElement parsedElement, boolean bExpression, boolean bNextTokenIfException, boolean bWarning,
                          IParserState parserState, ResourceKey errorMesg, Object... args )
  {
    if( !bExpression )
    {
      if( bNextTokenIfException )
      {
        advanceToNextTokenSilently();
      }
      if( parserState == PLACEHOLDER_PARSER_STATE )
      {
        parserState = makeFullParserState();
      }
      if( bWarning )
      {
        if (_ignoreWarnings == null || !_ignoreWarnings.contains(errorMesg)) {
          parsedElement.addParseWarning( new ParseWarning( parserState, errorMesg, args ) );
        }
      }
      else
      {
        parsedElement.addParseException( new ParseException( parserState, errorMesg, args ) );
      }
    }
    return bExpression;
  }

  //
  // Advance to the next token so as to include the token being tested in the tokenizer's stored state
  //

  void advanceToNextTokenSilently()
  {
    try
    {
      getTokenizer().nextToken();
    }
    catch( Exception e )
    {
      // ignore
    }
  }

  /**
   * @return a full parser state, which includes symbol table information, a clone of the tokenizer and everything else
   */
  IFullParserState makeFullParserState()
  {
    return new StandardParserState(null, getTokenizer(), getOffsetShift(), getLineNumShift(), getOwner().isEditorParser() );
  }

  /**
   * @return a full parser state, which includes symbol table information, a clone of the tokenizer and everything else
   */
  IFullParserState makeFullParserStateWithSymbols()
  {
    ISymbolTable symTable = getOwner().isEditorParser() && getOwner().shouldSnapshotSymbols() ? getSymbolTable().copy() : null;
    return new StandardParserState(symTable, getTokenizer(), getOffsetShift(), getLineNumShift(), getOwner().isEditorParser() );
  }

  /**
   * @return a lightweight parser state, which includes *only* the offset information of the parser, and no symbol information or
   *         a tokenizer state.
   */
  LightweightParserState makeLightweightParserState()
  {
    return new LightweightParserState( getTokenizer(), getOffsetShift(), getLineNumShift() );
  }

  protected IType resolveType( ParsedElement parsedElement, IType lhsType, int op, IType rhsType )
  {
    if( isDynamic( lhsType ) )
    {
      return lhsType;
    }
    if( isDynamic( rhsType ) )
    {
      return rhsType;
    }

    if( op == '+' &&
        !(lhsType instanceof IErrorType) &&
        !(rhsType instanceof IErrorType) &&
        (JavaTypes.CHAR_SEQUENCE().isAssignableFrom( lhsType ) ||
         JavaTypes.CHAR_SEQUENCE().isAssignableFrom( rhsType )) )
    {
      return GosuParserTypes.STRING_TYPE();
    }

    IType retType = resolveIfDimensionOperand( this, parsedElement, lhsType, op, rhsType );
    if( retType != null )
    {
      verify( parsedElement,
              retType != ErrorType.getInstance() &&
              (!isFinalDimension( this, lhsType, parsedElement ) || BeanAccess.isNumericType( retType )),
              Res.MSG_TYPE_MISMATCH,
              rhsType.getDisplayName(),
              lhsType.getDisplayName(),
              rhsType.getTypeLoader().getModule().getName(),
              lhsType.getTypeLoader().getModule().getName());
      return retType;
    }

    // If we get here and have a non-final dimension on either the LHS or RHS, we want to just short-circuit
    // and return ErrorType, without generating the type mismatch parse error.  There should always already
    // be a parse error indicating that one of the dimensions is non-final.
    if( (isNonFinalDimension( lhsType ) || isNonFinalDimension( rhsType )) )
    {
      assert parsedElement.getParseExceptions().size() > 0;
      return ErrorType.getInstance();
    }

    IType type = resolveType( lhsType, op, rhsType );
    verify( parsedElement,
            type != ErrorType.getInstance() &&
            (!isFinalDimension( this, lhsType, parsedElement ) || BeanAccess.isNumericType( type )),
            Res.MSG_TYPE_MISMATCH, rhsType.getDisplayName(), lhsType.getDisplayName() );
    return type;
  }

  private static boolean isNonFinalDimension( IType type )
  {
    return JavaTypes.IDIMENSION().isAssignableFrom( type ) && !type.isFinal();
  }

  public static IType resolveRuntimeType( IType lhsType, int op, IType rhsType )
  {
    if( op == '+' &&
        (JavaTypes.CHAR_SEQUENCE().isAssignableFrom( lhsType ) ||
         JavaTypes.CHAR_SEQUENCE().isAssignableFrom( rhsType )) )
    {
      return GosuParserTypes.STRING_TYPE();
    }

//## todo: support dimensional arithmetic with Dynamic types
//    IType retType = resolveIfDimensionOperand( null, null, lhsType, op, rhsType );
//    if( retType != null )
//    {
//      return retType;
//    }

    return resolveType( lhsType, op, rhsType );
  }

  public static IType resolveType( IType lhsType, int op, IType rhsType )
  {
    if( isDynamic( lhsType ) )
    {
      return lhsType;
    }
    if( isDynamic( rhsType ) )
    {
      return rhsType;
    }

    if( op == '+' &&
        !(lhsType instanceof IErrorType) &&
        !(rhsType instanceof IErrorType) &&
        (JavaTypes.CHAR_SEQUENCE().isAssignableFrom( lhsType ) ||
         JavaTypes.CHAR_SEQUENCE().isAssignableFrom( rhsType )) )
    {
      return GosuParserTypes.STRING_TYPE();
    }

    if( !BeanAccess.isNumericType( lhsType ) ||
        !BeanAccess.isNumericType( rhsType ) )
    {
      return ErrorType.getInstance();
    }

    if( op == 0x226A /* left shift */ ||
        op == 0x226B /* right shift */ ||
        op == 0x22D9 /* logical right shift */ )
    {
      if( rhsType == JavaTypes.INTEGER() || rhsType == JavaTypes.pINT() )
      {
        return lhsType;
      }
      else
      {
        return ErrorType.getInstance();
      }
    }

    if( JavaTypes.BIG_DECIMAL() == lhsType || JavaTypes.BIG_DECIMAL() == rhsType )
    {
      return JavaTypes.BIG_DECIMAL();
    }

    if( lhsType == JavaTypes.BIG_INTEGER() )
    {
      if( rhsType == JavaTypes.DOUBLE() || rhsType == JavaTypes.pDOUBLE() || rhsType == JavaTypes.FLOAT() || rhsType == JavaTypes.pFLOAT() )
      {
        return JavaTypes.BIG_DECIMAL();
      }
      else
      {
        return JavaTypes.BIG_INTEGER();
      }
    }
    if( rhsType == JavaTypes.BIG_INTEGER() )
    {
      if( lhsType == JavaTypes.DOUBLE() || lhsType == JavaTypes.pDOUBLE() || lhsType == JavaTypes.FLOAT() || lhsType == JavaTypes.pFLOAT() )
      {
        return JavaTypes.BIG_DECIMAL();
      }
      else
      {
        return JavaTypes.BIG_INTEGER();
      }
    }

    return handleBoxedAndPrimitiveTypes( lhsType, rhsType );
  }

  private static IType handleBoxedAndPrimitiveTypes( IType lhsType, IType rhsType )
  {
    IType retType;
    if( lhsType == JavaTypes.DOUBLE() || lhsType == JavaTypes.pDOUBLE() )
    {
      retType = lhsType;
    }
    else if( rhsType == JavaTypes.DOUBLE() || rhsType == JavaTypes.pDOUBLE() )
    {
      retType = rhsType;
    }

    else if( lhsType == JavaTypes.FLOAT() || lhsType == JavaTypes.pFLOAT() )
    {
      retType = lhsType;
    }
    else if( rhsType == JavaTypes.FLOAT() || rhsType == JavaTypes.pFLOAT() )
    {
      retType = rhsType;
    }

    else if( lhsType == JavaTypes.LONG() || lhsType == JavaTypes.pLONG() )
    {
      retType = lhsType;
    }
    else if( rhsType == JavaTypes.LONG() || rhsType == JavaTypes.pLONG() )
    {
      retType = rhsType;
    }

    else if( lhsType == JavaTypes.INTEGER() || lhsType == JavaTypes.pINT() )
    {
      retType = lhsType;
    }
    else if( rhsType == JavaTypes.INTEGER() || rhsType == JavaTypes.pINT() )
    {
      retType = rhsType;
    }

    else if( lhsType == JavaTypes.SHORT() || lhsType == JavaTypes.pSHORT() ||
             rhsType == JavaTypes.SHORT() || rhsType == JavaTypes.pSHORT() ||
             lhsType == JavaTypes.CHARACTER() || lhsType == JavaTypes.pCHAR() ||
             rhsType == JavaTypes.CHARACTER() || rhsType == JavaTypes.pCHAR() ||
             lhsType == JavaTypes.BYTE() || lhsType == JavaTypes.pBYTE() ||
             rhsType == JavaTypes.BYTE() || rhsType == JavaTypes.pBYTE() )
    {
      // Always widen short/byte operations up to int in conformance with Java and to avoid unnecessary overflow
      retType = JavaTypes.pINT();
    }
    else
    {
      retType = ErrorType.getInstance();
    }

    retType = makeBoxedTypeIfEitherOperandIsBoxed( lhsType, rhsType, retType );
    return retType;
  }

  private static IType makeBoxedTypeIfEitherOperandIsBoxed( IType lhsType, IType rhsType, IType retType )
  {
    if( retType.isPrimitive() &&
        (StandardCoercionManager.isBoxed( lhsType ) ||
         StandardCoercionManager.isBoxed( rhsType ) ) )
    {
      retType = TypeLord.getBoxedTypeFromPrimitiveType( retType );
    }
    return retType;
  }

  private static IType resolveIfDimensionOperand( ParserBase parser, ParsedElement parsedElement, IType lhsType, int op, IType rhsType )
  {
    if( isFinalDimension( parser, lhsType, parsedElement ) )
    {
      IType retType = getAndAssignOperatorOverloader( lhsType, rhsType, op, parsedElement );
      if( retType != null )
      {
        return retType;
      }

      if( isFinalDimension( parser, rhsType, parsedElement ) )
      {
        if( op == '*' || op == '/' || op == '%' )
        {
          // Unless the lhs overrides default behavior (via method impl e.g., divide(... )),
          // multiplication is undefined between to dimensions.

          if( parser != null )
          {
            parser.addError( parsedElement, Res.MSG_DIMENSION_MULTIPLICATION_UNDEFINED );
          }
          return ErrorType.getInstance();
        }

        if( lhsType != rhsType )
        {
          // If both addition operands derive from IDimension, they must be the same *exact* type.

          if( parser != null )
          {
            parser.addError( parsedElement, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE );
          }
          return ErrorType.getInstance();
        }
      }
      else if( op == '+' || op == '-' )
      {
        // Operands must both be Dimensions for addition or subtraction

        if( parser != null )
        {
          parser.addError( parsedElement, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE );
        }
        return ErrorType.getInstance();
      }

      return lhsType;
    }
    else if( isFinalDimension( parser, rhsType, parsedElement ) )
    {
      if( op == '*' || op == '+' )
      {
        // Multiplication and addition are commutative, so we can use the override on the rhs if one exists...

        IType retType = getAndAssignOperatorOverloader( rhsType, lhsType, op, parsedElement );
        if( retType != null )
        {
          Expression temp = ((ArithmeticExpression)parsedElement).getLHS();
          ((ArithmeticExpression)parsedElement).setLHS( ((ArithmeticExpression)parsedElement).getRHS() );
          ((ArithmeticExpression)parsedElement).setRHS( temp );
          return retType;
        }
      }

      if( op == '+' || op == '-' )
      {
        // Operands must both be Dimensions for addition or subtraction

        if( parser != null )
        {
          parser.addError( parsedElement, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE );
        }
        return ErrorType.getInstance();
      }

      if( op == '/' || op == '%' )
      {
        // Can't divide a scalar value by a dimension; it's nonsense

        if( parser != null )
        {
          parser.addError( parsedElement, Res.MSG_DIMENSION_DIVIDE_SCALAR_BY_DIMENSION );
        }
        return ErrorType.getInstance();
      }

      return rhsType;
    }
    return null;
  }

  static boolean isFinalDimension(ParserBase parser, IType lhsType, ParsedElement pe)
  {
    if( JavaTypes.IDIMENSION().isAssignableFrom( lhsType ) )
    {
      if( !lhsType.isFinal() )
      {
        if( parser != null )
        {
          parser.addError( pe, Res.MSG_DIMENSION_MUST_BE_FINAL, lhsType.getName() );
        }
        return false;
      }
      return true;
    }
    return false;
  }

  private static IType getAndAssignOperatorOverloader( IType lhsType, IType rhsType, int op, ParsedElement parsedElement )
  {
    IMethodInfo mi = findMathOpMethod( lhsType, op, rhsType );
    if( mi != null )
    {
      if( parsedElement instanceof IOverridableOperation )
      {
        ((IOverridableOperation)parsedElement).setOverride( mi );
      }
      return mi.getReturnType();
    }
    return null;
  }

  public static IMethodInfo findMathOpMethod( IType lhsType, int op, IType rhsType )
  {
    String strMethod;
    switch( op )
    {
      case '+':
        strMethod = "add";
        break;
      case '-':
        strMethod = "subtract";
        break;
      case '*':
        strMethod = "multiply";
        break;
      case '/':
        strMethod = "divide";
        break;
      case '%':
        strMethod = "modulo";
        break;
      default:
        return null;
    }
    return lhsType.getTypeInfo().getMethod( strMethod, rhsType );
  }

  protected ISymbol resolveSymbol( ParsedElement e, String strName, boolean ignoreFunctionSymbols )
  {
    assert getSymbolTable() != null : CommonServices.getGosuLocalizationService().localize( Res.MSG_NULL_SYMBOL_TABLE );

    ISymbol sym;

    // Uberhack alert: query path root symbols can mask existing symbols, so we need to see if one of these
    //                 is around prior to checking for captured symbol values
    ISymbol symbol = findSymbol( strName, ignoreFunctionSymbols );
    if( symbol instanceof QueryPathRootSymbol )
    {
      return symbol;
    }

    if( isParsingBlock() || isOrIsEnclosedByAnonymousClass( getGosuClass() ) && !getOwner().isParsingAnnotation() )
    {
      sym = captureSymbol( getCurrentEnclosingGosuClass(), strName, e );
    }
    else
    {
      sym = findSymbol( strName, ignoreFunctionSymbols );
    }

    if( sym == null && getGosuClass() != null )
    {
      sym = getGosuClass().getExternalSymbol( strName );
    }

    if( sym == null )
    {
      // Produces symbol w/ errant type if not found
      sym = resolveNamespaceSymbol( e, strName );
    }
    else if( sym.getType() instanceof IErrorType && !e.hasParseExceptions() )
    {
      // Ensure a symbol defined with a null type is marked with a parse exception
      SymbolNotFoundException pe = new SymbolNotFoundException(makeFullParserState(), strName);
      IType ctxType = getOwner().getContextType().getType();
      if( ctxType != null )
      {
        pe.setExpectedType( ctxType );
      }
      e.addParseException( pe );
    }

    if( sym == null )
    {
      throw new IllegalStateException( "Should never return null symbol: " + sym.getName() );
    }
    else if( e != null )
    {
      sym = handleForwardReference( e, sym );
    }
    return sym;
  }

  private ISymbol handleForwardReference( ParsedElement e, ISymbol sym )
  {
    if( !getOwner().isParsingFieldInitializer() ||
        (!(sym instanceof DynamicSymbol) && (!(sym instanceof DynamicPropertySymbol) || ((DynamicPropertySymbol)sym).getVarIdentifier() == null)) ||
        sym.getScriptPart() == null ||
        sym.getScriptPart().getContainingType() != getGosuClass() )
    {
      return sym;
    }


    String varName = sym instanceof DynamicSymbol
                     ? sym.getName()
                     : ((DynamicPropertySymbol)sym).getVarIdentifier();

    VarStatement varStmt = (VarStatement)getGosuClass().getMemberField( varName );
    if( varStmt == null )
    {
      varStmt = ((IGosuClassInternal)getGosuClass()).getStaticField( varName );
    }
    if( varStmt == getOwner().peekParsingFieldInitializer() )
    {
      // Reference to field we are currently definition parsing e.g., var _x = _x
      // Allow stuff like: var Logger = Logger.make( "fubar" )
      // Logger in the lhs is a relative type ref, not a ref to the l-value
      sym = resolveNamespaceSymbol( e, sym.getName() );
    }
    else
    {
      verify( e, varStmt == null || varStmt.isDefinitionParsed(), Res.MSG_ILLEGAL_FORWARD_REFERENCE );
    }
    return sym;
  }

  private boolean isOrIsEnclosedByAnonymousClass( ICompilableType type )
  {
    return type instanceof IGosuClassInternal &&
           (type.isAnonymous() || isOrIsEnclosedByAnonymousClass( type.getEnclosingType() ));
  }

  protected ISymbol resolveNamespaceSymbol( ParsedElement e, String strName )
  {
    ISymbol sym;
    INamespaceType namespaceType = resolveNamespace( strName );
    if( namespaceType != null )
    {
      sym = new Symbol( strName, namespaceType, null );
    }
    else
    {
      maybeAddLocalsOfEnclosingType();

      IType ctxType = getOwner().getContextType().getType();
      SymbolNotFoundException pe = new SymbolNotFoundException(makeFullParserStateWithSymbols(), strName);
      if( ctxType != null )
      {
        pe.setExpectedType( ctxType );
      }
      e.addParseException(pe);
      sym = new Symbol( strName, ErrorType.getInstance(), null );
    }
    return sym;
  }

  private void maybeAddLocalsOfEnclosingType()
  {
    if( shouldSnapshotSymbols() )
    {
      // add in outer class symbols if this is an editor parser
      ISymbolTable completionSymbolTable = getSymbolTable();
      ICompilableTypeInternal gosuClass = getGosuClass();
      if( gosuClass != null )
      {
        ICompilableTypeInternal enclosingType = gosuClass.getEnclosingType();
        while( enclosingType  != null)
        {
          ISymbolTable symbolTableForClass = getSymbolTableForClass( enclosingType );
          if( symbolTableForClass != null )
          {
            for( Object key : symbolTableForClass.getSymbols().keySet() )
            {
              String name = key.toString();
              if( completionSymbolTable.getSymbol( name ) == null )
              {
                completionSymbolTable.putSymbol( symbolTableForClass.getSymbol( name ) );
              }
            }
          }
          enclosingType = enclosingType.getEnclosingType();
        }
      }
    }
  }

  protected INamespaceType resolveNamespace( String strName )
  {
    INamespaceType namespaceType = TypeSystem.getNamespace( strName );
    if( namespaceType == null )
    {
      ITypeUsesMap typeUsesMap = getOwner().getTypeUsesMap();
      if( typeUsesMap != null )
      {
        namespaceType = typeUsesMap.resolveRelativeNamespaceInAllNamespaces( strName );
      }
      if( namespaceType == null && strName.equals( IGosuProgram.PACKAGE ) )
      {
        return PROGRAM_NAMESPACE;
      }
    }
    return namespaceType; 
  }

  protected void captureAllSymbols( ICompilableTypeInternal anonClass, ICompilableTypeInternal enclosingClass, List<ICapturedSymbol> capturedSymbols )
  {
    ISymbolTable tableForClass = getSymbolTableForClass( enclosingClass );
    if( tableForClass == null )
    {
      if( anonClass instanceof IGosuProgram )
      {
        // Presumably this is a recursive call where the caller is an eval program, so give it its enclosing class' captured symbols
        Map<String, ICapturedSymbol> enclCaptured = anonClass.getCapturedSymbols();
        capturedSymbols.addAll( enclCaptured.values() );
      }
      return;
    }
    @SuppressWarnings({"unchecked"})
    Map<CharSequence, ISymbol> symbols = (Map<CharSequence, ISymbol>)tableForClass.getSymbols();

    for( ISymbol sym : symbols.values() )
    {
      if( sym != null && sym.canBeCaptured() && (enclosingClass == null || enclosingClass.getExternalSymbol( sym.getName() ) == null) )
      {
        ICapturedSymbol capturedSymbol = sym.makeCapturedSymbol( sym.getName(),
                                                                 getSymbolTable(),
                                                                 anonClass == null ? new StandardScope() : getScope( anonClass ) );
        if( anonClass != null )
        {
          if( anonClass instanceof IBlockClassInternal )
          {
            IBlockExpression expression = ((IBlockClassInternal)anonClass).getBlock();
            if( !expression.isWithinScope( sym, tableForClass ) )
            {
              anonClass.addCapturedSymbol( capturedSymbol );
            }
          }
          else
          {
            anonClass.addCapturedSymbol( capturedSymbol );
          }
        }
        else
        {
          capturedSymbols.add( capturedSymbol );
        }
      }
    }
    if( enclosingClass != null && enclosingClass.isAnonymous() )
    {
      captureAllSymbols( enclosingClass, enclosingClass.getEnclosingType(), capturedSymbols );
    }
  }

  protected ISymbol captureSymbol( ICompilableTypeInternal anonClass, String strName,ParsedElement e )
  {
    //never capture this, outer or super
    if( Keyword.KW_this.equals( strName ) ||
        Keyword.KW_super.equals( strName ) ||
        Keyword.KW_outer.equals( strName ) )
    {
      return findSymbol( strName, true );
    }

    try
    {
      // check if we've already captured this symbol
      ISymbol sym = anonClass.getCapturedSymbol( strName );
      if( sym == null )
      {
        // see if we have access to the uncaptured version of this symbol
        sym = getUncapturedSymbol( anonClass, strName );

        // if we don't have access to the uncaptured version of this symbol, resolve it
        // through the enclosing class
        if( sym == null && isOrIsEnclosedByAnonymousClass( anonClass ) )
        {
          ICompilableTypeInternal enclosingType = anonClass.getEnclosingType();
          if( enclosingType == null )
          {
            sym = resolveForNullEnclosingClass( strName );
          }
          else
          {
            sym = captureSymbol( enclosingType, strName, e );
          }

          warnOnPcfVariablesHack( e, sym );

          if( sym != null && sym.canBeCaptured() && (enclosingType == null || enclosingType.getExternalSymbol( strName ) == null) )
          {
            // and wrap it up as a captured symbol
            ICapturedSymbol capturedSymbol = sym.makeCapturedSymbol( strName,
                                                                     getSymbolTable(),
                                                                     getScope( anonClass ) );
            anonClass.addCapturedSymbol( capturedSymbol );
            sym = capturedSymbol;
          }
        }
      }
      return sym;
    }
    catch( IllegalStateException ise )
    {
      if( e != null )
      {
        e.addParseException( new ParseException( makeFullParserState(), Res.MSG_BAD_CAPTURE_TYPE ) );
      }
      return null;
    }
  }

  private void warnOnPcfVariablesHack( ParsedElement e, ISymbol sym )
  {
    if( sym instanceof INonCapturableSymbol )
    {
      // Ugh... have to do this for PCF variables
      e.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_POTENTIALLY_BAD_CAPTURE ) );
    }
  }

  private IScope getScope( ICompilableType anonClass )
  {
    if( anonClass instanceof IBlockClassInternal )
    {
      return ((IBlockClassInternal)anonClass).getBlock().getScope();
    }
    else
    {
      return getSymbolTableForClass( anonClass ).peekIsolatedScope();
    }
  }

  private ISymbol resolveForNullEnclosingClass( String strName )
  {
    ISymbol symbol = getSymbolTable().getSymbol( strName );
    if( symbol == null )
    {
      symbol = CompiledGosuClassSymbolTable.instance().getSymbol( strName );
    }
    return symbol;
  }

  protected ISymbol getUncapturedSymbol( ICompilableType gsClass, String strName )
  {
    ISymbolTable symTable = getSymbolTableForClass( gsClass );
    if( symTable == null )
    {
      return null;
    }
    ISymbol symbol = symTable.getSymbol( strName );
    if( symbol == null )
    {
      symbol = getSymbolTable().getSymbol( strName );
      if( symbol != null )
      {
        symTable = getSymbolTable();
      }
    }
    if( gsClass instanceof IBlockClassInternal )
    {
      IBlockExpression block = ((IBlockClassInternal)gsClass).getBlock();
      return block.isWithinScope( symbol, symTable ) ? symbol : null;
    }
    else
    {
      return symbol;
    }
  }

  private ISymbolTable getSymbolTableForClass( ICompilableType gsClass )
  {
    if( gsClass == null )
    {
      return getSymbolTable();
    }
    else if( gsClass instanceof IBlockClassInternal )
    {
      return getSymbolTableForClass( gsClass.getEnclosingType() );
    }
    else
    {
      return CompiledGosuClassSymbolTable.instance().getSymbolTableForCompilingClass( gsClass );
    }
  }

  private ISymbol findSymbol( String strName, boolean ignoreFunctionSymbols )
  {
    return findSymbol( strName, getSymbolTable(), ignoreFunctionSymbols );
  }

  private ISymbol findSymbol( String strName, ISymbolTable symTable, boolean ignoreFunctionSymbols )
  {
    if( Keyword.KW_this.getName().equals( strName ) ) {
      if( isEvalClass() ) {
        // In eval expressions 'this' = 'outer' since the eval expression is wrapped in an anonymous inner class
        strName = Keyword.KW_outer.getName();
      }
      else if( getGosuClass() instanceof IGosuProgram ) {
        // 'this' must be an external symbol for non-eval programs e.g., debugger expressions
        return null;
      }
    }

    ISymbol sym = symTable.getSymbol( strName );
    if( sym == null && !ignoreFunctionSymbols )
    {
      List dfsDecls = getOwner().getDfsDeclsForFunction( strName );
      sym = dfsDecls.isEmpty() ? null : (ISymbol)dfsDecls.get( dfsDecls.size() - 1 );
    }
    return sym;
  }

  protected boolean isEvalClass()
  {
    return getGosuClass() instanceof IGosuProgram &&
           getGosuClass().isAnonymous();
  }

  protected void verifyComparable( IType lhsType, Expression rhs, boolean bWarnOnCoercion )
  {
    verifyComparable( lhsType, rhs, false, bWarnOnCoercion );
  }

  protected void verifyComparable( IType lhsType, Expression rhs, boolean bBiDirectional, boolean bWarnOnCoercion )
  {
    verifyComparable( lhsType, rhs, bBiDirectional, bWarnOnCoercion, makeFullParserState() );
  }

  protected void verifyComparable( IType lhsType, Expression rhs, boolean bBiDirectional, boolean bWarnOnCoercion, IParserState state )
  {
    IType rhsType = rhs.getType();
    if (TypeSystem.isDeleted(lhsType) || TypeSystem.isDeleted(rhsType)) {
      return;
    }
    if( lhsType != JavaTypes.pVOID() &&
        (rhsType == GosuParserTypes.NULL_TYPE() && !(rhs instanceof NullExpression)) )
    {
      if( rhs instanceof Identifier && ((Identifier)rhs).getSymbol() instanceof AbstractDynamicSymbol )
      {
        rhs.addParseException( new ParseException( state, Res.MSG_FIELD_TYPE_HAS_NOT_BEEN_INFERRED ) );
      }
    }

    if( rhsType != null )
    {
      verifyTypesComparable( rhs, lhsType, rhsType, bBiDirectional, bWarnOnCoercion, state );
      if( lhsType == GosuParserTypes.DATETIME_TYPE() )
      {
        if( rhs instanceof StringLiteral )
        {
          String str = ((StringLiteral)rhs).getValue();
          if( str != null )
          {
            try
            {
              CommonServices.getCoercionManager().isDateTime( str );
            }
            catch( java.text.ParseException e )
            {
              ParseException pe = ParseException.wrap( e, state );
              pe.setExpectedType( lhsType );
              rhs.addParseException( pe );
            }
          }
        }
      }
      else if( BeanAccess.isBeanType( lhsType ) &&
               rhs instanceof Literal && !(rhs instanceof DefaultParamValueLiteral) && !(rhs instanceof TypeLiteral) && !(rhs instanceof NullExpression) && !rhs.hasParseExceptions() )
      {
        try
        {
          Object valueLiteral = rhs.evaluate();
          List<IType> inferringTypes = getCurrentlyInferringFunctionTypeVars();
          IType verifyType;
          if( !inferringTypes.isEmpty() )
          {
            verifyType = TypeLord.boundTypes( lhsType, inferringTypes );
          }
          else
          {
            verifyType = lhsType;
          }
          if( !CommonServices.getEntityAccess().verifyValueForType( verifyType, valueLiteral ) )
          {
            rhs.addParseException( new ParseException(
              state, lhsType, Res.MSG_VALUE_MISMATCH, valueLiteral, TypeSystem.getUnqualifiedClassName( lhsType ) ) );
          }
        }
        catch( IncompatibleTypeException ite )
        {
          Object valueLiteral = rhs.evaluate();
          rhs.addParseException( new ParseException(
            state, lhsType, Res.MSG_VALUE_MISMATCH, valueLiteral, TypeSystem.getUnqualifiedClassName( lhsType ) ) );
        }
        catch( RuntimeException re )
        {
          //noinspection ThrowableResultOfMethodCallIgnored
          rhs.addParseException( ParseException.wrap( re, state ) );
        }
      }
    }
  }

  protected IType verifyTypesComparable( ParsedElement element, IType lhsType, IType rhsType, boolean bBiDirectional,
                                         boolean bWarnOnCoercion )
  {
    return verifyTypesComparable( element, lhsType, rhsType, bBiDirectional, bWarnOnCoercion, makeFullParserState() );
  }

  protected IType verifyTypesComparable( ParsedElement element, IType lhsType, IType rhsType, boolean bBiDirectional,
                                         boolean bWarnOnCoercion, IParserState state )
  {
    try
    {
      final ICoercionManager coercionManager = CommonServices.getCoercionManager();
      coercionManager.verifyTypesComparable(lhsType, rhsType, bBiDirectional);

      boolean isImplicit = coercionManager.coercionRequiresWarningIfImplicit(lhsType, rhsType);
      if( isImplicit && bBiDirectional )
      {
        isImplicit = coercionManager.coercionRequiresWarningIfImplicit(rhsType, lhsType);
      }
      if( bWarnOnCoercion &&
          CommonServices.getEntityAccess().isWarnOnImplicitCoercionsOn() &&
          isImplicit )
      {
        if( CommonServices.getEntityAccess().getLanguageLevel().allowAllImplicitCoercions() )
        {
          element.addParseWarning( new ImplicitCoercionWarning( state,
                                                                Res.MSG_IMPLICIT_COERCION_WARNING,
                                                                lhsType,
                                                                rhsType.getDisplayName(),
                                                                lhsType.getDisplayName() ) );
        }
        else
        {
          element.addParseException( new ImplicitCoercionError( state,
                                                                Res.MSG_IMPLICIT_COERCION_ERROR,
                                                                lhsType,
                                                                rhsType.getDisplayName(),
                                                                lhsType.getDisplayName() ) );
        }
      }
      if( rhsType instanceof ErrorType )
      {
        List<IParseIssue> pes = element.getParseExceptions();
        if( pes.size() > 0 )
        {
          ParseException pe = (ParseException)pes.get( pes.size() - 1 );
          pe.setExpectedType( lhsType );
        }
      }
    }
    catch( ParseIssue issue )
    {
      ParseException wrappedPe = ParseException.wrap( issue, state );
      wrappedPe.setExpectedType( lhsType );
      element.addParseException( wrappedPe );
    }
    return lhsType;
  }

  public void verifyNonVoidExpression(Expression eas) {
    if( eas != null )
    {
      verify( eas, eas instanceof NullExpression || eas.getType() != JavaTypes.pVOID(), Res.MSG_VOID_EXPRESSION_NOT_ALLOWED );
    }
  }

  ICompilableTypeInternal getGosuClass()
  {
    return getOwner() == null || getOwner().getScriptPart() == null
           ? null
           : getOwner().getScriptPart().getContainingType() instanceof ICompilableTypeInternal
             ? (ICompilableTypeInternal)getOwner().getScriptPart().getContainingType()
             : null;
  }

  ClassStatement getClassStatement()
  {
    return null;
  }

  ModifierInfo parseModifiers()
  {
    return parseModifiers( false );
  }

  ModifierInfo parseModifiers( boolean bIgnoreErrors )
  {
    int iOffsetList = getTokenizer().getTokenStart();
    int iLineNumList = getTokenizer().getLineNumber();
    int iColumnList = getTokenizer().getTokenColumn();

    ICompilableType gsClass = getGosuClass();
    boolean bNotInterface = gsClass == null || (gsClass instanceof IGosuEnhancementInternal) || !gsClass.isInterface();

    ParsedElement elem = getClassStatement();
    bIgnoreErrors = bIgnoreErrors || elem == null;

    List<IGosuAnnotation> annotations = Collections.emptyList();
    ModifierInfo modifiers = new ModifierInfo(0);

    int iModifiers = 0;
    DocCommentBlock block = null;
    boolean matchedStatic = false;
    boolean matchedAbstract = false;
    while( true )
    {
      if( match( null, SourceCodeTokenizer.TT_EOF ) )
      {
        modifiers.setModifiers( -1 );
        return modifiers;
      }

      if( block == null )
      {
        block = popLastComment();
        if( block != null )
        {
          modifiers.setDescription( block.getDescription() );
          if( annotations.isEmpty() )
          {
            annotations = new ArrayList<IGosuAnnotation>( 2 );
          }
          annotations.addAll( block.getAnnotations() );
        }
      }

      if( match( null, null, '@', true ) )
      {
        if( getOwner() == null )
        {
          match( null, '@' );
          throw new IllegalStateException( "Found null owning parser" );
        }
        if( annotations.isEmpty() )
        {
          annotations = new ArrayList<IGosuAnnotation>( 2 );
        }
        parseAnnotation( annotations );
      }
      else if( match( null, Keyword.KW_private ) )
      {
        verify( elem, bIgnoreErrors || bNotInterface || gsClass.getEnclosingType() != null, Res.MSG_NOT_ALLOWED_IN_INTERFACE );
        verifyNoAccessibilityModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_private );
        verifyNoHideOverrideModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_private );
        iModifiers = Modifier.setPrivate( iModifiers, true );
      }
      else if( match( null, Keyword.KW_internal ) )
      {
        verify( elem, bIgnoreErrors || bNotInterface || gsClass.getEnclosingType() != null, Res.MSG_NOT_ALLOWED_IN_INTERFACE );
        verifyNoAccessibilityModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_internal );
        iModifiers = Modifier.setInternal( iModifiers, true );
      }
      else if( match( null, Keyword.KW_protected ) )
      {
        verify( elem, bIgnoreErrors || bNotInterface || gsClass.getEnclosingType() != null, Res.MSG_NOT_ALLOWED_IN_INTERFACE );
        verifyNoAccessibilityModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_protected );
        iModifiers = Modifier.setProtected( iModifiers, true );
      }
      else if( match( null, Keyword.KW_public ) )
      {
        verifyNoAccessibilityModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_public );
        iModifiers = Modifier.setPublic( iModifiers, true );
      }
      else if( match( null, Keyword.KW_static ) )
      {
        verifyNoAbstractHideOverrideStaticModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_static, matchedStatic );
        iModifiers = Modifier.setStatic( iModifiers, true );
        matchedStatic = true;
      }
      else if( match( null, Keyword.KW_abstract ) )
      {
        verifyNoAbstractHideOverrideStaticModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_abstract, matchedAbstract );
        iModifiers = Modifier.setAbstract( iModifiers, true );
        matchedAbstract = true;
      }
      else if( match( null, Keyword.KW_override ) )
      {
        verifyNoAbstractHideOverrideStaticModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_override );
        verify( elem, bIgnoreErrors || !Modifier.isPrivate( iModifiers ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, Keyword.KW_override );
        iModifiers = Modifier.setOverride( iModifiers, true );
      }
      else if( match( null, Keyword.KW_hide ) )
      {
        verifyNoAbstractHideOverrideStaticModifierDefined( elem, bIgnoreErrors, iModifiers, Keyword.KW_hide );
        verify( elem, bIgnoreErrors || !Modifier.isPrivate( iModifiers ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, Keyword.KW_hide );
        iModifiers = Modifier.setHide( iModifiers, true );
      }
      else if( match( null, Keyword.KW_final ) )
      {
        verify( elem, bIgnoreErrors || !Modifier.isAbstract( iModifiers ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_abstract, Keyword.KW_final );
        verify( elem, bIgnoreErrors || !Modifier.isFinal( iModifiers ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_final );
        iModifiers = Modifier.setFinal( iModifiers, true );
      }
      else if( match( null, Keyword.KW_transient ) )
      {
        verify( elem, bIgnoreErrors || !Modifier.isTransient( iModifiers ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, Keyword.KW_transient );
        iModifiers = Modifier.setTransient( iModifiers, true );
      }
      else
      {
        break;
      }
    }

    if( match( null, Keyword.KW_function, true ) ||
        match( null, Keyword.KW_property, true ) ||
        match( null, Keyword.KW_var, true ) ||
        match( null, Keyword.KW_delegate, true ) )
    {
      verifyNoCombinedPrivateAbstract( elem, bIgnoreErrors, iModifiers );
    }

    modifiers.setModifiers( iModifiers );
    modifiers.setAnnotations( annotations );

    if( !bIgnoreErrors )
    {
      pushModifierList( iOffsetList, iLineNumList, iColumnList );
    }

    return modifiers;
  }

  private void pushModifierList( int iOffsetList, int iLineNumList, int iColumnList )
  {
    ModifierListClause e = new ModifierListClause();
    pushExpression( e );
    boolean bZeroLength = getTokenizer().getTokenStart() <= iOffsetList;
    setLocation( iOffsetList, iLineNumList, iColumnList, bZeroLength, true );
    popExpression();
  }

  protected void eatOptionalSemiColon( boolean bEat )
  {
    if( bEat && match( null, ';' ) )
    {
      // Eat optional semi-colon on interface method decls.
    }
  }

  protected static boolean isDynamic( IType type )
  {
    return type instanceof IPlaceholder && ((IPlaceholder)type).isPlaceholder();
  }
  
  protected void parseAnnotation( List<IGosuAnnotation> annotations )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    match( null, '@' );
    getOwner().pushParsingStaticMember( true );
    IType type = ErrorType.getInstance();
    Expression e = NOT_SET_EXPRESSION;
    int end;
    try
    {
      if( getGosuClass() == null || getGosuClass().shouldFullyCompileAnnotations() )
      {
        getOwner().parseNewExpressionOrAnnotation( true );
        setLocation( iOffset, iLineNum, iColumn, true );
        e = popExpression();
        end = e.getLocation().getExtent() + 1;
        type = e.getType();
        maybeVerifyAnnotationArgs( e );
      }
      else
      {
        getOwner().parseTypeLiteral();
        Expression typeLiteral = popExpression();
        if( match( null, '(' ) )
        {
          end = getTokenizer().getTokenStart();
          Token token = getOwner().eatBlock( '(', ')', false, true );
          if( token != null )
          {
            end = token.getTokenEnd();
          }
        }
        else
        {
          end = typeLiteral.getLocation().getExtent() + 1;
        }
        if( typeLiteral instanceof TypeLiteral && !typeLiteral.hasParseExceptions())
        {
          type = (IType)typeLiteral.evaluate();
        }
      }
    }
    finally
    {
      getOwner().popParsingStaticMember();
    }

    if( type == null )
    {
      type = ErrorType.getInstance();
    }
    getOwner().checkInstruction( true );
    if( !ErrorType.getInstance().equals( type ) && type.getTypeLoader() != null )
    {
      IModule module = type.getTypeLoader().getModule();
      TypeSystem.pushModule( module );
      try
      {
        boolean bAnnotation = JavaTypes.IANNOTATION().isAssignableFrom( type ) ||
                              JavaTypes.ANNOTATION().isAssignableFrom( type );
        verify( e, bAnnotation || type instanceof IErrorType, Res.MSG_TYPE_NOT_ANNOTATION, type.getName() );
      }
      finally
      {
        TypeSystem.popModule( module );
      }
    }
    GosuAnnotation annotationInfo = new GosuAnnotation( getGosuClass(), type, e, iOffset, end );
    if( e instanceof AnnotationExpression )
    {
      ((AnnotationExpression)e).setAnnotation( annotationInfo );
    }
    annotations.add( annotationInfo );
  }

  private void maybeVerifyAnnotationArgs( Expression e )
  {
    if( e instanceof AnnotationExpression )
    {
      AnnotationExpression ae = (AnnotationExpression)e;
      if( JavaTypes.ANNOTATION().isAssignableFrom( ae.getType() ) && ae.getArgs() != null)
      {
        for( Expression arg : ae.getArgs() )
        {
          verify( arg, arg.isCompileTimeConstant(), Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED );
        }
      }
    }
  }

  void verifyModifiers( IParsedElement pe, ModifierInfo modInfo, UsageTarget targetType )
  {
    verifyModifiersForFeature( pe, modInfo );
    verifyAnnotations( modInfo, targetType );
  }

  protected void verifyModifiersForFeature( IParsedElement pe, ModifierInfo modInfo )
  {
    if( getGosuClass().getEnclosingType() != null && !getGosuClass().isStatic() )
    {
      verify( (ParsedElement) pe, !Modifier.isStatic( modInfo.getModifiers() ), Res.MSG_STATIC_MODIFIER_NOT_ALLOWED_HERE );
    }
  }

  void verifyAnnotations( ModifierInfo modInfo, UsageTarget targetType )
  {
    List<IType> annotationTypes = new ArrayList<IType>();
    for( IGosuAnnotation annotation : modInfo.getAnnotations() )
    {
      if( annotation instanceof GosuAnnotation )
      {
        IType annotationType = annotation.getType();
        if( !(annotationType instanceof ErrorType) )
        {
          UsageModifier modifer = UsageModifier.getUsageModifier( targetType, annotationType );

          if( modifer.equals( UsageModifier.None ) )
          {
            annotation.getExpression().addParseException( Res.MSG_ANNOTATION_WHEN_NONE_ALLOWED, annotation.getName(), targetType.toString().toLowerCase() );
          }
          else if( modifer.equals( UsageModifier.One ) && annotationTypes.contains( annotationType ) )
          {
            annotation.getExpression().addParseException( Res.MSG_TOO_MANY_ANNOTATIONS, annotation.getName(), targetType.toString().toLowerCase() );
          }
          annotationTypes.add( annotationType );
        }
      }
    }
  }

  void verifyNoAbstractHideOverrideStaticModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier, Keyword kw ) {
    verifyNoAbstractHideOverrideStaticModifierDefined( elem, bIgnoreErrors, modifier, kw, false );
  }

  void verifyNoAbstractHideOverrideStaticModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier, Keyword kw, boolean alreadyMatched )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !Modifier.isOverride( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, kw );
    verify( elem, !Modifier.isHide( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, kw );
    if( !(elem instanceof ClassStatement) || alreadyMatched )
    {
      verify( elem, !Modifier.isAbstract( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_abstract, kw );
      verify( elem, !Modifier.isStatic( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_static, kw );
    }
  }

  void verifyNoAccessibilityModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier, Keyword kw )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !Modifier.isPrivate( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, kw );
    verify( elem, !Modifier.isInternal( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_internal, kw );
    verify( elem, !Modifier.isProtected( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_protected, kw );
    verify( elem, !Modifier.isPublic( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_public, kw );
  }

  void verifyNoAbstractHideOverrideModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier, Keyword kw )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !Modifier.isAbstract( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_abstract, kw );
    verify( elem, !Modifier.isOverride( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, kw );
    verify( elem, !Modifier.isHide( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, kw );
  }

  void verifyNoHideOverrideModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier, Keyword kw )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !Modifier.isOverride( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, kw );
    verify( elem, !Modifier.isHide( modifier ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, kw );
  }

  void verifyNoCombinedPrivateAbstract( ParsedElement elem, boolean bIgnoreErrors, int modifier )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !(Modifier.isPrivate( modifier ) && Modifier.isAbstract( modifier )),
            Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, Keyword.KW_abstract );
  }

  void verifyNoCombinedFinalPrivateModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !(Modifier.isPrivate( modifier ) && Modifier.isFinal( modifier )),
            Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, Keyword.KW_final );
  }

  void verifyNoCombinedFinalStaticModifierDefined( ParsedElement elem, boolean bIgnoreErrors, int modifier )
  {
    if( bIgnoreErrors )
    {
      return;
    }
    verify( elem, !(Modifier.isStatic( modifier ) && Modifier.isFinal( modifier )),
            Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_static, Keyword.KW_final );
  }

  public void setDontOptimizeStatementLists( boolean dontOptimizeStatementLists )
  {
    if( getOwner() != this )
    {
      getOwner().setDontOptimizeStatementLists( dontOptimizeStatementLists );
    }
    _bDontOptimizeStatementLists = dontOptimizeStatementLists;
  }
  public boolean isDontOptimizeStatementLists()
  {
    if( getOwner() != this )
    {
      return getOwner().isDontOptimizeStatementLists();
    }
    return _bDontOptimizeStatementLists;
  }

  public void setSubTree( List<IParseTree> subTree )
  {
    _subTree = subTree;
  }

  public void setBlocks( Stack<BlockExpression> blocks )
  {
    _blocks = blocks;
  }

  public void pushCurrentBlock( BlockExpression block )
  {
    if( _blocks == null )
    {
      _blocks = new Stack<BlockExpression>();
    }

    // Note:  This has to come BEFORE we push the block on the stack, because it depends on
    // whether or not the block stack is empty
    ICompilableTypeInternal enclosingClass = getCurrentEnclosingGosuClass();

    _blocks.push( block );

    IBlockClassInternal blockClass = BlockClass.create( enclosingClass, block, _blocks.size() == 1 && getOwner().isParsingStaticFeature() );
    block.setBlockGosuClass( blockClass );
    if( enclosingClass != null )
    {
      enclosingClass.addBlock( blockClass );
    }
  }

  public void addBlockToBlockStack( BlockExpression block ) {
    if( _blocks == null )
    {
      _blocks = new Stack<BlockExpression>();
    }

    _blocks.push( block );
  }

  protected ICompilableTypeInternal getCurrentEnclosingGosuClass()
  {
    ICompilableTypeInternal enclosingClass;
    if( _blocks == null || _blocks.isEmpty() )
    {
      enclosingClass = getGosuClass();
    }
    else
    {
      enclosingClass = (ICompilableTypeInternal)_blocks.peek().getBlockGosuClass();
    }
    if( enclosingClass == null )
    {
      // enclosingClass can be null, for example, if the block is inside a string template
      enclosingClass = getOuterFromScriptPartStack();
    }
    return enclosingClass;
  }

  private ICompilableTypeInternal getOuterFromScriptPartStack() {
    java.util.Stack<IScriptPartId> scriptPartIdStack = getOwner().getScriptPartIdStack();
    for( int i = scriptPartIdStack.size() - 1; i >= 0; i-- )
    {
      IScriptPartId id = scriptPartIdStack.get( i );
      if( id instanceof ScriptPartId )
      {
        IType type = id.getContainingType();
        if( type instanceof ICompilableTypeInternal )
        {
          return (ICompilableTypeInternal)type;
        }
      }
    }
    return null;
  }

  void popCurrentBlock()
  {
    _blocks.pop();
  }

  public boolean isParsingBlock()
  {
    return _blocks != null && !_blocks.isEmpty();
  }

  protected void copyBlockStackTo( ParserBase otherParser )
  {
    if( _blocks != null )
    {
      for( BlockExpression block : _blocks )
      {
        otherParser.addBlockToBlockStack( block );
      }
    }
  }

  protected IGosuClassInternal getParsingAnonymousClass()
  {
    IType type = getOwner().getScriptPart() != null
                 ? getOwner().getScriptPart().getContainingType()
                 : null;
    while( type instanceof IGosuClassInternal && !((IGosuClassInternal)type).isAnonymous() )
    {
      type = type.getEnclosingType();
    }
    return type instanceof IGosuClassInternal && ((IGosuClassInternal)type).isAnonymous()
           ? (IGosuClassInternal)type
           : null;
  }

  protected Expression possiblyWrapWithImplicitCoercion( Expression expressionToCoerce, IType typeToCoerceTo )
  {
    return possiblyWrapWithCoercion( expressionToCoerce, typeToCoerceTo, true );
  }
  protected Expression possiblyWrapWithCoercion( Expression expressionToCoerce, IType typeToCoerceTo, boolean bImplicit )
  {
    if( expressionToCoerce == null )
    {
      return null;
    }

    if( typeToCoerceTo == null || typeToCoerceTo instanceof ErrorType )
    {
      return expressionToCoerce;
    }

//    if( isMethodScoring() )
//    {
//      return expressionToCoerce;
//    }

    IType resolvedTypeToCoerceTo;
    List<IType> inferringTypes = getCurrentlyInferringFunctionTypeVars();
    if( inferringTypes.size() > 0 )
    {
      resolvedTypeToCoerceTo = TypeLord.boundTypes( typeToCoerceTo, inferringTypes );
    }
    else
    {
      resolvedTypeToCoerceTo = typeToCoerceTo;
    }

    IType typeToCoerceFrom = expressionToCoerce.getType();
    ICoercionManager cocerionManager = CommonServices.getCoercionManager();
    ICoercer coercer = cocerionManager.resolveCoercerStatically( resolvedTypeToCoerceTo, typeToCoerceFrom );

    if( coercer == null )
    {
      return expressionToCoerce;
    }
    else if( JavaTypes.pVOID().equals( typeToCoerceFrom ) && !(expressionToCoerce instanceof NullExpression) )
    {
      return expressionToCoerce;
    }
    else
    {
      TypeAsExpression tas = bImplicit ? new ImplicitTypeAsExpression() : new TypeAsExpression();
      tas.setLHS( expressionToCoerce );

      if( coercer instanceof IResolvingCoercer)
      {
        IResolvingCoercer resolvingCoercer = (IResolvingCoercer) coercer;
        typeToCoerceTo = resolvingCoercer.resolveType( typeToCoerceTo, typeToCoerceFrom );
      }
      tas.setType( typeToCoerceTo );
      tas.setCoercer( coercer );

      setLocationForImplicitTypeAs( expressionToCoerce, tas );
      return tas;
    }
  }

  protected void setLocationForImplicitTypeAs( Expression expressionToCoerce, TypeAsExpression tas )
  {
    if( expressionToCoerce instanceof DefaultArgLiteral )
    {
      // DefaultArgLiterals do not exist in the parse tree
      return;
    }

    ParseTree wrappedLoc = findAndWrapLocation( expressionToCoerce, tas );
    if( wrappedLoc == null )
    {
      throw new IllegalStateException( "The expression wrapped with an implicit type-as did not have its location set." );
    }
  }

  public ParseTree findAndWrapLocation( Expression oldExpr, ParsedElement newExpr )
  {
    ParseTree oldLoc = oldExpr.getLocation();
    if( oldLoc == null )
    {
      return null;
    }
    ParseTree newLoc = newExpr.initLocation(oldLoc.getOffset(), oldLoc.getLength(), oldExpr.getLineNum(), oldExpr.getColumn(), oldLoc.getScriptPartId());
    IParseTree parent = oldLoc.getParent();
    newExpr.setLocation( newLoc );
    newLoc.addChild( oldLoc );
    if( parent != null )
    {
      parent.removeChild( oldLoc );
      newLoc.addChild( oldLoc );
      parent.addChild( newLoc );
    }
    List<ParseTree> locations = getOwner().getLocationsList();
    for( int i = locations.size()-1; i > 0; i-- )
    {
      ParseTree loc = locations.get( i );
      if( loc.getParsedElement() == oldExpr )
      {
        locations.set( i, newLoc );
        break;
      }
    }

    return newLoc;
  }

  private DocCommentBlock popLastComment()
  {
    DocCommentBlock lastComment= getOwner().getTokenizer().popLastComment();
    if( lastComment != null )
    {
      lastComment.setOwnersTypes( getGosuClass() );
    }
    return lastComment;
  }

  public void setValidator( IGosuValidator validator )
  {
    _validator = validator;
  }

  public IGosuValidator getValidator() {
    return _validator != null ? _validator : getOwner() != null && !this.equals(getOwner()) ? getOwner().getValidator() : null;
  }

  protected void setOffsetShift( int offsetShift )
  {
    _offsetShift = offsetShift;
  }

  public void setLineNumShift( int lineNumShift )
  {
    _lineNumShift = lineNumShift;
  }

  public int getLineNumShift()
  {
    return _lineNumShift;
  }

  public int getOffsetShift()
  {
    return _offsetShift;
  }

  protected void pushInferringFunctionTypeVars( List<IType> typeVariableTypes )
  {
    _inferringFunctionTypes.push( typeVariableTypes );
  }
  protected void popInferringFunctionTypeVariableTypes()
  {
    _inferringFunctionTypes.pop();
  }

  public List<IType> getCurrentlyInferringFunctionTypeVars()
  {
    List<IType> types = Collections.emptyList();
    if( _inferringFunctionTypes.size() != 0 )
    {
      for( List<IType> inferringFunctionType : _inferringFunctionTypes )
      {
        for( IType type : inferringFunctionType )
        {
          if( types == Collections.EMPTY_LIST )
          {
            types = new ArrayList<IType>();
          }
          types.add( type );
        }
      }
    }
    return types;
  }

  public static boolean matchDeclarationKeyword( Token T, boolean bPeek, SourceCodeTokenizer tokenizer )
  {
    boolean bMatch =
    (match( T, Keyword.KW_construct.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_function.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_property.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_var.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_delegate.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_class.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_interface.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_annotation.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_structure.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ) ||
     match( T, Keyword.KW_enum.toString(), SourceCodeTokenizer.TT_KEYWORD, bPeek, tokenizer ));
    if( bMatch )
    {
      // We must allow an existing java package to have the same name as a gosu declaration keyword.
      // We check for that by looking for a following '.' after the package reference...

      int iTokenIndex = tokenizer.getState();
      IToken followingToken = tokenizer.getTokenAt( iTokenIndex + (bPeek ? 1 : 0) );
      IToken priorToken = iTokenIndex <= 1 ? null : tokenizer.getTokenAt( iTokenIndex + (bPeek ? -1 : -2) );

      bMatch = (followingToken == null || followingToken.getType() != '.') &&
               (priorToken == null || !(priorToken.getType() == '.' || "#".equals( priorToken.getStringValue() )));
    }
    return bMatch;
  }

  private static final class PlaceholderParserState implements IParserState
  {
    @Override
    public int getLineNumber()
    {
      return 0;
    }

    @Override
    public int getTokenColumn()
    {
      return 0;
    }

    @Override
    public String getSource()
    {
      return null;
    }

    @Override
    public int getTokenStart()
    {
      return 0;
    }

    @Override
    public int getTokenEnd()
    {
      return 0;
    }

    @Override
    public int getLineOffset()
    {
      return 0;
    }
  }

  public boolean shouldSnapshotSymbols() {
    return _snapshotSymbols;
  }

  public void setSnapshotSymbols() {
    _snapshotSymbols = true;
  }
}
