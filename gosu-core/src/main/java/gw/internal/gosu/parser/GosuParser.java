/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.config.ExecutionMode;
import gw.fs.IFile;
import gw.internal.gosu.dynamic.DynamicConstructorInfo;
import gw.internal.gosu.dynamic.DynamicMethodInfo;
import gw.lang.reflect.IDynamicType;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.internal.gosu.parser.expressions.*;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.internal.gosu.parser.statements.AssertStatement;
import gw.internal.gosu.parser.statements.AssignmentStatement;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.parser.statements.BlockInvocationStatement;
import gw.internal.gosu.parser.statements.BreakStatement;
import gw.internal.gosu.parser.statements.CaseClause;
import gw.internal.gosu.parser.statements.CatchClause;
import gw.internal.gosu.parser.statements.ClasspathStatement;
import gw.internal.gosu.parser.statements.ContinueStatement;
import gw.internal.gosu.parser.statements.DelegateStatement;
import gw.internal.gosu.parser.statements.DoWhileStatement;
import gw.internal.gosu.parser.statements.EvalStatement;
import gw.internal.gosu.parser.statements.ForEachStatement;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.internal.gosu.parser.statements.HideFieldNoOpStatement;
import gw.internal.gosu.parser.statements.IfStatement;
import gw.internal.gosu.parser.statements.LoopStatement;
import gw.internal.gosu.parser.statements.MapAssignmentStatement;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.parser.statements.NamespaceStatement;
import gw.internal.gosu.parser.statements.NewStatement;
import gw.internal.gosu.parser.statements.NoOpStatement;
import gw.internal.gosu.parser.statements.NotAStatement;
import gw.internal.gosu.parser.statements.PropertyStatement;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.SwitchStatement;
import gw.internal.gosu.parser.statements.SyntheticMemberAccessStatement;
import gw.internal.gosu.parser.statements.ThrowStatement;
import gw.internal.gosu.parser.statements.TryCatchFinallyStatement;
import gw.internal.gosu.parser.statements.TypeLoaderStatement;
import gw.internal.gosu.parser.statements.UsesStatement;
import gw.internal.gosu.parser.statements.UsesStatementList;
import gw.internal.gosu.parser.statements.UsingStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.internal.gosu.parser.statements.WhileStatement;
import gw.internal.gosu.parser.types.ConstructorType;
import gw.internal.gosu.template.TemplateGenerator;
import gw.internal.gosu.util.StringUtil;
import gw.lang.IReentrant;
import gw.lang.annotation.UsageTarget;
import gw.lang.function.IBlock;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.ICoercer;
import gw.lang.parser.ICoercionManager;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.IInjectedSymbol;
import gw.lang.parser.ILanguageLevel;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseResult;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IParserState;
import gw.lang.parser.IResolvingCoercer;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ISource;
import gw.lang.parser.ISourceCodeTokenizer;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.MemberAccessKind;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.PostCompilationAnalysis;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.SourceCodeReader;
import gw.lang.parser.StandardCoercionManager;
import gw.lang.parser.StandardScope;
import gw.lang.parser.SymbolType;
import gw.lang.parser.ThreadSafeSymbolTable;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.coercers.FunctionToInterfaceCoercer;
import gw.lang.parser.coercers.IdentityCoercer;
import gw.lang.parser.exceptions.DoesNotOverrideFunctionException;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.parser.exceptions.NoCtorFoundException;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.ParseWarning;
import gw.lang.parser.exceptions.ParseWarningForDeprecatedMember;
import gw.lang.parser.exceptions.WrongNumberOfArgsException;
import gw.lang.parser.expressions.IArithmeticExpression;
import gw.lang.parser.expressions.IBlockInvocation;
import gw.lang.parser.expressions.IImplicitTypeAsExpression;
import gw.lang.parser.expressions.IInferredNewExpression;
import gw.lang.parser.expressions.IInitializerExpression;
import gw.lang.parser.expressions.ILiteralExpression;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.expressions.ISynthesizedMemberAccessExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeParameterListClause;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.ITypeVariableDefinitionExpression;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.expressions.Variance;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.ITypeLoaderStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.TerminalType;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.reflect.ConstructorInfoBuilder;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.ICanBeAnnotation;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IConstructorType;
import gw.lang.reflect.IEnumValue;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IInvocableType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INamespaceType;
import gw.lang.reflect.IOptionalParamCapable;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPlaceholder;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.MethodScore;
import gw.lang.reflect.MethodScorer;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.IJavaPropertyInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.DynamicArray;
import gw.util.GosuExceptionUtil;
import gw.util.GosuObjectUtil;
import gw.util.SpaceEfficientHashMap;

import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import gw.util.Stack;

@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public final class GosuParser extends ParserBase implements IGosuParser
{
  public static final IType PENDING_BOUNDING_TYPE = ErrorType.getInstance( "Pending Bounding Type");
  public static ErrorType notfound = ErrorType.getInstance( "_notfound_" );

  private SourceCodeTokenizer _tokenizer;
  private ISymbolTable _symTable;
  private boolean _bParsed;
  private Stack<ParsedElement> _stack;
  private Stack<DynamicFunctionSymbol> _stackDFS;
  private List<ParseTree> _locations;
  private Program _parsingProgram;
  private ArrayList<FunctionType> _parsingFunctions;
  private ArrayList<VarStatement> _parsingFieldInitializer;
  private Map<String, List<IFunctionSymbol>> _dfsDeclByName;
  private ITypeUsesMap _typeUsesMap;
  private String _strNamespace;
  private ITokenizerInstructor _tokenizerInstructor;
  private IScriptabilityModifier _scriptabilityConstraint;
  private int _iBreakOk;
  private int _iContinueOk;
  int _iReturnOk;
  private Stack<IScriptPartId> _scriptPartIdStack;
  private Map<String, ITypeVariableDefinition> _typeVarsByName;
  private Stack<ContextType> _inferredContextStack = new Stack<ContextType>();
  private boolean _bThrowForWarnings;
  private boolean _bStudioEditorParser;
  private boolean _bWarnOnCaseIssue;
  private Stack<Boolean> _parsingAbstractConstructor;
  private ContextInferenceManager _ctxInferenceMgr = new ContextInferenceManager();
  private Stack<IType> _blockReturnTypeStack = new Stack<IType>();
  private Stack<Boolean> _parsingStaticFeature;
  private boolean _bCaptureSymbolsForEval;
  private boolean _parsingAnnotation;
  private boolean _allowWildcards;
  private int _ignoreTypeDeprecation;
  private boolean _bGenRootExprAccess;
  private boolean _bProgramCallFunction;
  private Map<IScriptPartId, Map<String, IType>> _typeCache;

  private int _iStmtDepth;
  private List<ParseTree> _savedLocations;
  private Boolean _bAreUsingStatementsAllowedInStatementLists;

  GosuParser( ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint )
  {
    this(symTable, scriptabilityConstraint, CommonServices.getEntityAccess().getDefaultTypeUses());
  }

  GosuParser( ISymbolTable symTable, IScriptabilityModifier scriptabilityConstraint, ITypeUsesMap tuMap )
  {
    super();
    setOwner( this );
    setWarnOnCaseIssue( Settings.WARN_ON_CASE_DEFAULT.get() == Boolean.TRUE);
    setEditorParser( Settings.IDE_EDITOR_PARSER_DEFAULT.get() == Boolean.TRUE);
    _symTable = symTable;
    //noinspection unchecked
    _typeUsesMap = tuMap.copy();
    _scriptabilityConstraint = scriptabilityConstraint;
    _dfsDeclByName = new HashMap<String, List<IFunctionSymbol>>();

    _stack = new Stack<ParsedElement>();
    _stackDFS = new Stack<DynamicFunctionSymbol>();
    _locations = new ArrayList<ParseTree>();
    _parsingFunctions = new ArrayList<FunctionType>();
    _parsingFieldInitializer = new ArrayList<VarStatement>();
    _typeVarsByName = new HashMap<String, ITypeVariableDefinition>( 2 );
    _parsingStaticFeature = new Stack<Boolean>();
    _parsingAbstractConstructor = new Stack<Boolean>();
    _typeCache = new HashMap<IScriptPartId, Map<String, IType>>();
    _bParsed = false;
    _iReturnOk = 1;
    _allowWildcards = false;
  }

  @Override
  public void setValidator( IGosuValidator validator )
  {
    super.setValidator( validator );
  }

  //------------------------------------------------------------------------------
  // -- IGosuParser implementation --

  public IScriptPartId getScriptPart()
  {
    if( _scriptPartIdStack == null || _scriptPartIdStack.isEmpty() )
    {
      return null;
    }
    return _scriptPartIdStack.peek();
  }
  public Stack<IScriptPartId> getScriptPartIdStack()
  {
    return _scriptPartIdStack;
  }
  public void pushScriptPart( IScriptPartId partId )
  {
    if( _scriptPartIdStack == null )
    {
      _scriptPartIdStack = new Stack<IScriptPartId>();
    }
    _scriptPartIdStack.push( partId );
  }
  void popScriptPart( IScriptPartId partId )
  {
    IScriptPartId top = _scriptPartIdStack.pop();
    if( top != partId )
    {
      throw new IllegalStateException( "Unbalanced push/pop script id" );
    }
  }

  public void setScript( CharSequence source )
  {
    if( source == null )
    {
      source = "";
    }

    setScript( new SourceCodeReader( source ) );
  }

  public void setScript( ISource src )
  {
    if( src == null )
    {
      setScript( (CharSequence)null );
      return;
    }

    ISourceCodeTokenizer tokenizer = src.getTokenizer();
    if( tokenizer == null )
    {
      setScript( src.getSource() );
      src.setTokenizer( _tokenizer );
      _tokenizer.setInstructor( _tokenizerInstructor );
      if( _tokenizerInstructor != null )
      {
        _tokenizerInstructor.setTokenizer( _tokenizer );
      }
      else if( getGosuClass() instanceof IGosuTemplateInternal &&
              ((IGosuTemplateInternal)getGosuClass()).getTokenizerInstructor() != null )
      {
        _tokenizerInstructor = ((IGosuTemplateInternal)getGosuClass()).getTokenizerInstructor();
        _tokenizerInstructor.setTokenizer( _tokenizer );
        if( _tokenizer.getInstructor() == null )
        {
          _tokenizer.setInstructor( _tokenizerInstructor );
        }
      }
    }
    else
    {
      _tokenizer = (SourceCodeTokenizer)tokenizer;
      _tokenizer.setInstructor( _tokenizerInstructor );
      if( _tokenizerInstructor != null )
      {
        _tokenizerInstructor.setTokenizer( _tokenizer );
      }
      _tokenizer.reset();
    }

    reset();
  }

  public void setScript( SourceCodeReader reader )
  {
    if( _tokenizer == null )
    {
      _tokenizer = new SourceCodeTokenizer( reader, _tokenizerInstructor );

      // Initialize the tokenizer
      _tokenizer.wordChars( '_', '_' );
    }
    else
    {
      _tokenizer.reset( reader );
    }

    reset();
  }

  public void resetScript()
  {
    _tokenizer.reset();
    reset();
  }

  private void reset()
  {
    _stack.clear();
    _stackDFS.clear();
    _dfsDeclByName.clear();
    _typeUsesMap.clearNonDefaultTypeUses();
    _strNamespace = null;
    _locations.clear();
    _parsingFunctions.clear();
    _parsingFieldInitializer.clear();
    _typeVarsByName.clear();

    setParsed( false );
  }

  @Override
  protected String getScript()
  {
    return _tokenizer.getSource();
  }

  @Override
  public ISymbolTable getSymbolTable()
  {
    return _symTable;
  }

  public void setSymbolTable( ISymbolTable symTable )
  {
    _symTable = symTable;
  }

  public ITypeUsesMap getTypeUsesMap()
  {
    return _typeUsesMap;
  }

  public void setTypeUsesMap( ITypeUsesMap typeUsesMap )
  {
    _typeUsesMap = typeUsesMap == null ? null : typeUsesMap.copyLocalScope();
  }

  public String getNamespace()
  {
    return _strNamespace;
  }

  void setNamespace( String strNamespace )
  {
    _strNamespace = strNamespace;
    if( _strNamespace != null )
    {
      getTypeUsesMap().addToTypeUses( strNamespace + ".*" );
    }
  }

  public IScriptabilityModifier getVisibilityConstraint()
  {
    return _scriptabilityConstraint;
  }

  public ITokenizerInstructor getTokenizerInstructor()
  {
    return _tokenizerInstructor;
  }

  public void setTokenizerInstructor( ITokenizerInstructor tokenizerInstructor )
  {
    _tokenizerInstructor = tokenizerInstructor;
    if( _tokenizer != null )
    {
      _tokenizer.setInstructor( _tokenizerInstructor );
    }
  }

  public FunctionType peekParsingFunction()
  {
    return _parsingFunctions.get( 0 );
  }
  FunctionType popParsingFunction()
  {
    return _parsingFunctions.remove( 0 );
  }
  void pushParsingFunction( FunctionType functionType )
  {
    _parsingFunctions.add( 0, functionType );
  }
  public boolean isParsingFunction()
  {
    return _parsingFunctions.size() > 0;
  }

  public VarStatement peekParsingFieldInitializer()
  {
    return _parsingFieldInitializer.get( 0 );
  }
  VarStatement popParsingFieldInitializer()
  {
    return _parsingFieldInitializer.remove( 0 );
  }
  void pushParsingFieldInitializer( VarStatement VarStatement )
  {
    _parsingFieldInitializer.add( 0, VarStatement );
  }
  public boolean isParsingFieldInitializer()
  {
    return !isParsingFunction() &&
            !isParsingBlock() &&
            _parsingFieldInitializer.size() > 0;
  }

  Program peekParsingProgram()
  {
    return _parsingProgram;
  }

  boolean isParsingProgram()
  {
    return _parsingProgram != null;
  }

  public Statement parseStatements( IScriptPartId partId ) throws ParseResultsException
  {
    Statement stmt = parseStatements( partId, true, true );
    assignRuntime( stmt, true, null, null, partId);
    return stmt;
  }

  private Statement parseStatements( IScriptPartId partId, boolean verify, boolean isolatedScope ) throws ParseResultsException
  {
    pushScriptPart( partId );
    try
    {
      int iOffset = _tokenizer.getTokenStart();
      int iLineNum = _tokenizer.getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      _parseStatements( isolatedScope );
      Statement stmtList = peekStatement();
      verify( stmtList, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_STMT );
      _bParsed = true;

      if( stmtList instanceof StatementList )
      {
        setLocation( iOffset, iLineNum, iColumn, true );
      }
      if( verify )
      {
        verifyParsedElement( stmtList );
      }
      return stmtList;
    }
    finally
    {
      popScriptPart( partId );
    }
  }

  void _parseStatements( boolean isolatedScope )
  {
    StatementList stmtList = new StatementList( _symTable );
    _tokenizer.nextToken();
    if( _tokenizer.getInstructor() == null )
    {
      parseProgramClasspathStatements();
      parseProgramTypeLoaderStatements();
    }
    if ( isolatedScope )
    {
      _symTable.pushScope();
    }
    try
    {
      UsesStatementList usesStmtList = parseUsesStatementList( true );
      List<Statement> statements = new ArrayList<>();
      parseStatementsAndDetectUnreachable( statements );
      if( usesStmtList != null )
      {
        statements.add( 0, usesStmtList );
      }
      stmtList.setStatements( statements );
      pushStatement( isDontOptimizeStatementLists() ? stmtList : stmtList.getSelfOrSingleStatement() );
    }
    finally
    {
      if ( isolatedScope )
      {
        _symTable.popScope();
      }
    }
  }

  void parseStatementsAndDetectUnreachable( List<Statement> statements )
  {
    for( Statement stmt = null; parseStatement(); )
    {
      stmt = popStatementAndDetectUnreachable( stmt, statements );
    }
  }

  private Statement popStatementAndDetectUnreachable( Statement previousStatement, List<Statement> statements )
  {
    Statement currentStatement = popStatement();
    if( !(previousStatement instanceof ReturnStatement) || !(currentStatement instanceof NotAStatement) )
    {
      boolean[] bAbsolute = {false};
      verifyOrWarn( currentStatement,
              previousStatement == null ||
                      currentStatement.isNoOp() ||
                      previousStatement.getLeastSignificantTerminalStatement( bAbsolute ) == null || !bAbsolute[0],
              !CommonServices.getEntityAccess().isUnreachableCodeDetectionOn(), Res.MSG_UNREACHABLE_STMT );
    }

    if( isParsingFunction() && !isParsingBlock() )
    {
      IType returnType = peekParsingFunction().getReturnType();
      if( previousStatement instanceof ReturnStatement && returnType == JavaTypes.pVOID() )
      {
        if( currentStatement instanceof NotAStatement ||
                ((currentStatement instanceof BeanMethodCallStatement ||
                        currentStatement instanceof MethodCallStatement ||
                        currentStatement instanceof MemberAssignmentStatement) &&
                        currentStatement.getLineNum() == previousStatement.getLineNum()) ||
                (currentStatement instanceof NoOpStatement && isUnexpectedToken( currentStatement )) )
        {
          if( isUnexpectedToken( currentStatement ) )
          {
            currentStatement.clearParseExceptions();
          }
          verify( currentStatement, false, Res.MSG_RETURN_VAL_FROM_VOID_FUNCTION );
        }
      }
    }

    statements.add( currentStatement );
    return currentStatement.isNoOp() ? previousStatement : currentStatement;
  }

  private boolean isUnexpectedToken( Statement statement )
  {
    for( IParseIssue issue : statement.getParseExceptions() )
    {
      if( issue.getMessageKey() == Res.MSG_UNEXPECTED_TOKEN )
      {
        return true;
      }
    }
    return false;
  }

  public IProgram parseProgram( IScriptPartId partId ) throws ParseResultsException
  {
    return parseProgram( partId, true, null );
  }

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException
  {
    return parseProgram( partId, true, expectedExpressionType );
  }

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException
  {
    return parseProgram( partId, true, false, expectedExpressionType, ctx, assignRuntime );
  }

  public IProgram parseProgram( IScriptPartId partId, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException ) throws ParseResultsException
  {
    return parseProgram( partId, true, false, expectedExpressionType, ctx, assignRuntime, bDoNotThrowParseResultsException );
  }

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, IType expectedExpressionType ) throws ParseResultsException
  {
    return parseProgram( partId, true, false, expectedExpressionType, null, true );
  }

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime ) throws ParseResultsException
  {
    return parseProgram( partId, isolatedScope, reallyIsolatedScope, expectedExpressionType, ctx, assignRuntime, false );
  }

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException ) throws ParseResultsException
  {
    return parseProgram( partId, isolatedScope, reallyIsolatedScope, expectedExpressionType, ctx, assignRuntime, bDoNotThrowParseResultsException, null );
  }

  public IProgram parseProgram( IScriptPartId partId, boolean isolatedScope, boolean reallyIsolatedScope, IType expectedExpressionType, IFileContext ctx, boolean assignRuntime, boolean bDoNotThrowParseResultsException, IType superType ) throws ParseResultsException
  {
    Program program = new Program();
    program.setDeclaredReturnType(expectedExpressionType);
    _parsingProgram = program;
    pushScriptPart( partId );
    try
    {
      try
      {
        _tokenizer.nextToken();
        if( _tokenizer.getInstructor() == null )
        {
          parseProgramClasspathStatements();
        }

        if (superType != null)
        {
          IGosuClassInternal superTypeGosuClass = GosuClass.Util.getGosuClassFrom(superType);
          superTypeGosuClass.putClassMembers(this, getSymbolTable(), null, false );
        }

        if ( isolatedScope )
        {
          if ( reallyIsolatedScope ) {
            _symTable.pushIsolatedScope( new GosuParserTransparentActivationContext( partId ) );
          } else {
            _symTable.pushScope();
          }
        }
        try
        {
          //
          // First just find and parse the function Declarations
          //
          for( ISymbol function = parseFunctionOrPropertyDeclaration( program, true, false );
               function != null; function = parseFunctionOrPropertyDeclaration( program, true, false ) )
          {
            _symTable.putSymbol( function );
          }

          //
          // Next we have to reset the tokenizer to the beginning.
          //
          _tokenizer.reset();
          _locations.clear();

          //
          // Now parse the program as normal....
          //

          // Note function definitions are parsed as no-op statements, but are
          // pushed onto the dynamic function symobl stack.
          Statement mainStatement = parseStatements( getScriptPart(), false, false );

          // Map the parsed function definitions by name
          Map<String, DynamicFunctionSymbol> functionMap = new SpaceEfficientHashMap<String, DynamicFunctionSymbol>();
          while( peekDynamicFunctionSymbol() != null )
          {
            DynamicFunctionSymbol function = popDynamicFunctionSymbol();
            functionMap.put( function.getName(), function );
          }

          mainStatement.addParseIssues( program.getParseIssues() );
          program.setMainStatement( mainStatement );
          program.setFunctions( functionMap );
          program.setLocation( mainStatement.getLocation() );
          mainStatement.setParent( program );

          verify( program, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_STMT );
          _bParsed = true;
          PostCompilationAnalysis.maybeAnalyze( program );
          if( !bDoNotThrowParseResultsException )
          {
            verifyParsedElement( program );
          }
          CompileTimeAnnotationHandler.postDefinitionVerification( mainStatement );
        }
        finally
        {
          if( isolatedScope )
          {
            _symTable.popScope();
          }
        }
      }
      finally
      {
        _parsingProgram = null;
      }

      if( assignRuntime )
      {
        assignRuntime( program, isolatedScope, ctx, superType, partId);
      }
    }
    finally
    {
      popScriptPart( partId );
    }

    return program;
  }

  @Override
  public void setGenerateRootExpressionAccessForProgram( boolean bGenRootExprAccess )
  {
    _bGenRootExprAccess = bGenRootExprAccess;
  }
  public boolean isGenerateRootExpressionAccessForProgram()
  {
    return _bGenRootExprAccess;
  }

  private void assignRuntime(ParsedElement elem, boolean bIsolatedScope, IFileContext context, IType superType, IScriptPartId partId) throws ParseResultsException
  {
//    if( isForStringLiteralTemplate() )
//    {
//      return;
//    }

    if( elem.isCompileTimeConstant() )
    {
      return;
    }

    if( bIsolatedScope )
    {
      _symTable.pushScope();
    }
    try
    {
      GosuProgramParser programParser = new GosuProgramParser();
      ParserOptions options = new ParserOptions().withTypeUsesMap( getTypeUsesMap() )
              .withExpectedType( elem.getReturnType() )
              .withTokenizerInstructor( getTokenizerInstructor() )
              .withSuperType( superType )
              .withFileContext( context )
              .withCtxInferenceMgr( _ctxInferenceMgr.copy() )
              .withGenRootExprAccess( isGenerateRootExpressionAccessForProgram() )
              .asThrowawayProgram()
              .withScriptPartId(partId);
      IParseResult result = programParser.parseExpressionOrProgram( getScript(), getSymbolTable(), options );
      IGosuProgramInternal p = (IGosuProgramInternal)result.getProgram();
      if( p == null )
      {
        throw new IllegalStateException();
      }
      elem.setGosuProgram( p );
      if( partId != null )
      {
        partId.setRuntimeType( p );
      }
    }
    catch( Exception t )
    {
      if( t instanceof ParseResultsException )
      {
        if( isForStringLiteralTemplate() )
        {
          //## todo: shouldn't be creating a GosuProgram for this case, but the apps don't start for some reason.
          // Uncomment code at top of method
          return;
        }
        throw (ParseResultsException)t;
      }
      throw (RuntimeException)t;
    }
    finally
    {
      if( bIsolatedScope )
      {
        _symTable.popScope();
      }
    }
  }

  private boolean isForStringLiteralTemplate()
  {
    return getScriptPart() != null &&
            getScriptPart().getId() != null &&
            (getScriptPart().getId().equals( TemplateGenerator.GS_TEMPLATE ) ||
                    getScriptPart().getId().equals( TemplateGenerator.GS_TEMPLATE_PARSED )) &&
            getScriptPart().getContainingType() instanceof IGosuClass;
  }

  List<IClasspathStatement> parseProgramClasspathStatements()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getLineOffset();
    List<IClasspathStatement> returnList = new ArrayList<IClasspathStatement>();
    while( match( null, Keyword.KW_classpath ) )
    {
      ClasspathStatement cpStatement = new ClasspathStatement();
      if( parseStringLiteralSeparately() )
      {
        StringLiteral expression = (StringLiteral)popExpression();
        String strVal = (String)expression.evaluate();
        cpStatement.setClasspath( strVal );
        if( strVal.contains( ";" ) )
        {
          verifyOrWarn( cpStatement, false, true, Res.MSG_COMMA_IS_THE_CLASSPATH_SEPARATOR );
        }
      }
      else
      {
        verify( cpStatement, false, Res.MSG_CLASSPATH_STATEMENT_EXPECTS_A_STRING );
      }
      returnList.add( cpStatement );
      pushStatement(cpStatement);
      try
      {
        setLocation( iOffset, iLineNum, iColumn );
        if( getGosuClass() != null &&
                ((IGosuProgramInternal)getGosuClass()).isParsingExecutableProgramStatements() )
        {
          // Remove unwanted cp stmt resulting from parsing program on second pass for evaluate() method
          getLocationsList().remove( cpStatement.getLocation() );
        }
      }
      finally
      {
        popStatement();
      }

      iOffset = getTokenizer().getTokenStart();
      iLineNum = getTokenizer().getLineNumber();
      iColumn = getTokenizer().getLineOffset();
    }
    return returnList;
  }

  List<ITypeLoaderStatement> parseProgramTypeLoaderStatements()
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getLineOffset();
    List<ITypeLoaderStatement> returnList = new ArrayList<ITypeLoaderStatement>();
    while( match( null, Keyword.KW_typeloader ) )
    {
      TypeLoaderStatement stmt = new TypeLoaderStatement();
      parseTypeLiteral();
      TypeLiteral expression = (TypeLiteral)popExpression();
      stmt.setTypeLoader( expression.getType().getType() );
      returnList.add( stmt );
      pushStatement( stmt );
      try
      {
        setLocation( iOffset, iLineNum, iColumn );
      }
      finally
      {
        popStatement();
      }

      iOffset = getTokenizer().getTokenStart();
      iLineNum = getTokenizer().getLineNumber();
      iColumn = getTokenizer().getLineOffset();
    }
    return returnList;
  }

  @Override
  public ISymbol[] parseProgramFunctionsOrPropertyDecls(IScriptPartId partId, boolean bParseProperties, boolean bParseVars) throws ParseResultsException
  {
    Program program = new Program();

    pushScriptPart( partId );
    _tokenizer.nextToken();

    _symTable.pushScope();
    List<ISymbol> listFunctions;
    try
    {
      listFunctions = new ArrayList<ISymbol>();
      //
      // Just find and parse the function Declarations
      //
      for( ISymbol function = parseFunctionOrPropertyDeclaration( program, bParseProperties, bParseVars);
           function != null; function = parseFunctionOrPropertyDeclaration( program, bParseProperties, bParseVars) )
      {
        if (function instanceof IDynamicPropertySymbol) {
          IDynamicPropertySymbol property1 = (IDynamicPropertySymbol)function;
          String propertyName = property1.getName();
          IDynamicPropertySymbol property2 = null;
          for (ISymbol s : listFunctions) {
            if (s instanceof IDynamicPropertySymbol && propertyName != null && propertyName.equals(s.getName())) {
              property2 = (IDynamicPropertySymbol)s;
            }
          }
          if (property2 == null) {
            listFunctions.add( property1 );
          } else {
            property2.setGetterDfs(property1.getGetterDfs() != null ? property1.getGetterDfs() : property2.getGetterDfs());
            property2.setSetterDfs(property1.getSetterDfs() != null ? property1.getSetterDfs() : property2.getSetterDfs());
          }
        } else {
          listFunctions.add( function );
        }
      }
    }
    finally
    {
      _symTable.popScope();
      popScriptPart( partId );
    }
    return listFunctions.toArray( new ISymbol[listFunctions.size()] );
  }

  public Expression parseExp( IScriptPartId partId ) throws ParseResultsException
  {
    return parseExp( partId, true, null, true );
  }

  public Expression parseExp( IScriptPartId partId, IType expectedExpressionType ) throws ParseResultsException
  {
    return parseExp( partId, true, expectedExpressionType, true );
  }

  public Expression parseExp( IScriptPartId partId, IType expectedExpressionType, IFileContext context, boolean assignRuntime ) throws ParseResultsException
  {
    return parseExp( partId, true, expectedExpressionType, context, assignRuntime );
  }

  private Expression parseExp( IScriptPartId partId, boolean isolatedScope, IType expectedExpressionType, boolean assignRuntime ) throws ParseResultsException {
    return parseExp(partId, isolatedScope, expectedExpressionType, null, assignRuntime);
  }

  private Expression parseExp( IScriptPartId partId, boolean isolatedScope,
                               IType expectedExpressionType, IFileContext context, boolean assignRuntime ) throws ParseResultsException
  {
    pushScriptPart( partId );
    _tokenizer.nextToken();

    Expression expression;
    if ( isolatedScope )
    {
      _symTable.pushScope();
    }
    try
    {
      parseExpression( new ContextType( expectedExpressionType, false ) );

      expression = popExpression();
      verify( expression, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_EXPRESSION );
      _bParsed = true;
      verifyParsedElement( expression );
    }
    finally
    {
      if ( isolatedScope )
      {
        _symTable.popScope();
      }

      popScriptPart( partId );
    }

    if (assignRuntime) {
      assignRuntime( expression, isolatedScope, context, null, partId);
    }

    CompileTimeAnnotationHandler.postDefinitionVerification( expression );

    return expression;
  }

  public IExpression parseExpOrProgram( IScriptPartId partId ) throws ParseResultsException
  {
    return parseExpOrProgram( partId, true, true );
  }

  public IExpression parseExpOrProgram( IScriptPartId partId, boolean isolatedScope, boolean assignRuntime ) throws ParseResultsException
  {
    return parseExpOrProgram( partId, null, isolatedScope, assignRuntime );
  }
  public IExpression parseExpOrProgram( IScriptPartId partId, IType typeExpected, boolean isolatedScope, boolean assignRuntime ) throws ParseResultsException
  {
    IExpression exp;
    try
    {
      exp = parseExp( partId, isolatedScope, typeExpected, assignRuntime );
    }
    catch( ParseResultsException expressionParseResultException )
    {
      boolean isProbablyProgram = !getTokenizer().isEOF();
      //noinspection CaughtExceptionImmediatelyRethrown
      try
      {
        Map<String, List<IFunctionSymbol>> dfsDeclByName = new HashMap<>(_dfsDeclByName);
        resetScript();
        _dfsDeclByName = dfsDeclByName;
        exp = parseProgram( partId, isolatedScope, typeExpected );
      }
      catch( ParseResultsException programParseResultsException )
      {
        if( isProbablyProgram )
        {
          throw programParseResultsException;
        }
        else
        {
          // Note we can't just rethrow the original exception because we need
          // the locations etc. in the parser, so we have to reparse and let it throw.
          Map<String, List<IFunctionSymbol>> dfsDeclByName = new HashMap<>(_dfsDeclByName);
          resetScript();
          _dfsDeclByName = dfsDeclByName;
          exp = parseExp( partId, isolatedScope, null, assignRuntime );
        }
      }
    }
    return exp;
  }

  public TypeLiteral parseTypeLiteral( IScriptPartId partId ) throws ParseResultsException
  {
    pushScriptPart( partId );
    try
    {
      _tokenizer.nextToken();

      _symTable.pushScope();
      try
      {
        parseTypeLiteral();
      }
      finally
      {
        _symTable.popScope();
      }

      Expression expression = popExpression();

      verify( expression, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_EXPRESSION );

      _bParsed = true;

      return (TypeLiteral)expression;
    }
    finally
    {
      popScriptPart( partId );
    }
  }

  public boolean isParsed()
  {
    return _bParsed;
  }

  protected void setParsed( boolean bParsed )
  {
    _bParsed = bParsed;
  }

  //------------------------------------------------------------------------------
  //------------------------------------------------------------------------------
  // GosuParser methods

  @Override
  final public SourceCodeTokenizer getTokenizer()
  {
    return _tokenizer;
  }

  @Override
  List<ParseTree> getLocationsList()
  {
    return _locations;
  }

  public List<IParseTree> getLocations()
  {
    return new ArrayList<IParseTree>( _locations );
  }

  public ParseTree peekLocation()
  {
    if( _locations.isEmpty() )
    {
      return null;
    }
    return _locations.get( _locations.size()-1 );
  }

  public boolean hasWarnings()
  {
    return false;
  }

  public boolean isThrowParseResultsExceptionForWarnings()
  {
    return _bThrowForWarnings;
  }

  public void setThrowParseExceptionForWarnings( boolean bThrowParseExceptionForWarnings )
  {
    _bThrowForWarnings = bThrowParseExceptionForWarnings;
  }

  //------------------------------------------------------------------------------
  // expression
  //   <conditional-expression>
  //
  void parseExpression()
  {
    parseExpressionNoVerify( ContextType.EMPTY );
  }

  void parseExpression( ContextType contextType )
  {
    parseExpression( contextType, true );
  }

  void parseExpression( ContextType ctxType, boolean bVerify )
  {
    parseExpressionNoVerify( ctxType );
    if( bVerify && ctxType.getType() != null && !ctxType.isMethodScoring() )
    {
      Expression expr = popExpression();
      verifyComparable( ctxType.getType(), expr );
      expr = possiblyWrapWithImplicitCoercion( expr, ctxType.getType() );
      pushExpression( expr );
    }
  }

  void parseExpressionNoVerify( ContextType ctxType )
  {
    pushInferredContextTypes( ctxType );
    try
    {
      int iOffset = _tokenizer.getTokenStart();
      int iLineNum = _tokenizer.getLineNumber();
      int iColumn = _tokenizer.getTokenColumn();
      parseConditionalExpression();
      if( !ctxType.isMethodScoring() )
      {
        convertNumberLiteralsToContextType( ctxType.getType() );
        convertNullLiterals( ctxType.getType() );
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    finally
    {
      popInferredContextTypes();
    }
  }

  public void setContextInferenceManager(ContextInferenceManager ctxInferenceMgr) {
    _ctxInferenceMgr = ctxInferenceMgr;
  }

  public void pushInferredContextTypes( ContextType ctxType )
  {
    if( ctxType == null )
    {
      ctxType = ContextType.EMPTY;
    }

    if( ctxType.getType() instanceof IBlockType )
    {
      // Force block param types to initialize if necessary
      ((IBlockType)ctxType.getType()).getParameterTypes();
    }
    _inferredContextStack.push( ctxType );
  }
  public void popInferredContextTypes()
  {
    _inferredContextStack.pop();
  }
  public ContextType getContextType()
  {
    if( _inferredContextStack.isEmpty() )
    {
      return ContextType.EMPTY;
    }
    else
    {
      ContextType ctxType = _inferredContextStack.peek();
      assert ctxType != null;
      return ctxType;
    }
  }

  private void convertNullLiterals( IType contextType )
  {
    if( !_stack.isEmpty() && contextType != null )
    {
      Expression expression = peekExpression();
      if( expression instanceof NullExpression && !contextType.isPrimitive() )
      {
        expression.setType( contextType );
      }
    }
  }

  @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
  private void convertNumberLiteralsToContextType( IType contextType )
  {
    if( _stack.isEmpty() )
    {
      return;
    }
    Expression expr = peekExpression();
    UnaryExpression unary = null;
    if( expr instanceof UnaryExpression )
    {
      unary = (UnaryExpression)expr;
      Expression subexpr = unary.getExpression();
      if( subexpr instanceof NumericLiteral )
      {
        expr = subexpr;
      }
    }
    if( expr instanceof NumericLiteral )
    {
      // If a numeric type is in context, consider this number literal to be of that type
      NumericLiteral literal = (NumericLiteral)expr;
      IType ctxNumberType = getNumberTypeFromContextType( contextType );
      if( literal.getType() == ctxNumberType )
      {
        return;
      }

      if( ctxNumberType != null && !literal.isExplicitlyTyped() )
      {
        // If a numeric type is in context and the literal is not explicity typed, consider this number literal to be of that type
        if( ctxNumberType != null && !literal.isExplicitlyTyped() )
        {
          String strValue = literal.getStrValue();
          try
          {
            if( isPrefixNumericLiteral( strValue ) )
            {
              strValue = parseIntOrLongOrBigInt( strValue ) + "";
            }

            if( JavaTypes.pBYTE().equals( ctxNumberType ) || JavaTypes.BYTE().equals( ctxNumberType ) )
            {
              literal.setValue( Byte.parseByte( strValue ) );
            }
            else if( JavaTypes.pSHORT().equals( ctxNumberType ) || JavaTypes.SHORT().equals( ctxNumberType ) )
            {
              literal.setValue( Short.parseShort( strValue ) );
            }
            else if( JavaTypes.pINT().equals( ctxNumberType ) || JavaTypes.INTEGER().equals( ctxNumberType ) )
            {
              literal.setValue( Integer.parseInt( strValue ) );
            }
            else if( JavaTypes.pLONG().equals( ctxNumberType ) || JavaTypes.LONG().equals( ctxNumberType ) )
            {
              literal.setValue( Long.parseLong( strValue ) );
            }
            else if( JavaTypes.pFLOAT().equals( ctxNumberType ) || JavaTypes.FLOAT().equals( ctxNumberType ) )
            {
              literal.setValue( Float.parseFloat( strValue ) );
            }
            else if( JavaTypes.pDOUBLE().equals( ctxNumberType ) || JavaTypes.DOUBLE().equals( ctxNumberType ) )
            {
              literal.setValue( Double.parseDouble( strValue ) );
            }
            else if( JavaTypes.BIG_INTEGER().equals( ctxNumberType ) )
            {
              literal.setValue( new BigInteger( strValue ) );
            }
            else if( JavaTypes.BIG_DECIMAL().equals( ctxNumberType ) )
            {
              literal.setValue( new BigDecimal( strValue ) );
            }
            else if( literal.getType().isPrimitive() &&
                    (JavaTypes.OBJECT().equals( ctxNumberType ) ||
                            (JavaTypes.NUMBER().equals( ctxNumberType ) && ctxNumberType.isAssignableFrom( TypeSystem.getBoxType( literal.getType() )))) )
            {
              ctxNumberType = TypeSystem.getBoxType( literal.getType() );
            }
            else
            {
              return;
            }
          }
          catch( NumberFormatException ex )
          {
            return;
            //addError( literal, Res.MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE, strValue, ctxNumberType.getName() );
          }
          literal.setType( ctxNumberType );
          if( unary != null )
          {
            unary.setType( ctxNumberType );
          }
        }
      }
    }
  }

  private IType getNumberTypeFromContextType( IType ctxType )
  {
    if( ctxType == null )
    {
      return null;
    }

    // Return ctxType if it's a primitive number, assignable to Number, or is Object, otherwise null.
    IType compType = ctxType.isPrimitive() ? TypeSystem.getBoxType( ctxType ) : ctxType;
    return JavaTypes.NUMBER().isAssignableFrom( compType ) || JavaTypes.OBJECT().equals( compType ) ? ctxType : null;
  }

  //------------------------------------------------------------------------------
  // conditional-expression
  //   <conditional-or-expression>
  //   <conditional-expression> ? <conditional-expression> : <conditional-expression>
  //
  // Left recursion removed is:
  //
  // conditional-expression
  //   <conditional-or-expression> <conditional-expression2>
  //
  // conditional-expression2
  //   ? <conditional-expression> : <conditional-expression>
  //   <null>
  //

  void parseConditionalExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = _tokenizer.getTokenColumn();
    _parseConditionalExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseConditionalExpression()
  {
    // <conditional-or-expression>
    _ctxInferenceMgr.pushCtx();
    parseConditionalOrExpression();

    boolean foundCondExpr = false;
    // <conditional-or-expression> ? <conditional-expression> : <conditional-expression>
    if( match( null, "?", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      foundCondExpr = true;
      ConditionalTernaryExpression cte = new ConditionalTernaryExpression();

      Expression condition = popExpression();
      if( !JavaTypes.pBOOLEAN().equals( condition.getType() ) &&
          !JavaTypes.BOOLEAN().equals( condition.getType() ) )
      {
        condition.addParseException( Res.MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN );
      }
      condition = possiblyWrapWithImplicitCoercion( condition, JavaTypes.pBOOLEAN() );
      verifyComparable( JavaTypes.pBOOLEAN(), condition );

      parseConditionalExpression();
      Expression first = popExpression();

      verify( cte, match( null, ":", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_COLON_TERNARY );

      _ctxInferenceMgr.popCtx( false );
      parseConditionalExpression();
      Expression second = popExpression();

      _ctxInferenceMgr.pushCtx();
      IType type = findLeastUpperBoundWithCoercions(cte, first, second);
      cte.setType( type );
      cte.setCondition( condition );
      cte.setFirst( possiblyWrapWithImplicitCoercion( first, type ) );
      cte.setSecond( possiblyWrapWithImplicitCoercion( second, type ) );

      pushExpression( cte );
    }
    else if( match( null, "?:", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      foundCondExpr = true;
      ConditionalTernaryExpression cte = new ConditionalTernaryExpression();

      Expression first = popExpression();
      verify( first, !first.getType().isPrimitive(), Res.MSG_EXPECTING_REFERENCE_TYPE );

      _ctxInferenceMgr.popCtx( false );
      parseConditionalExpression();
      Expression second = popExpression();

      _ctxInferenceMgr.pushCtx();
      IType type = findLeastUpperBoundWithCoercions(cte, first, second);
      cte.setType( type );
      cte.setCondition( null );
      cte.setFirst( possiblyWrapWithImplicitCoercion( first, type ) );
      cte.setSecond( possiblyWrapWithImplicitCoercion( second, type ) );

      pushExpression( cte );
    }
    _ctxInferenceMgr.popCtx( !foundCondExpr );
  }

  private boolean isPrimitiveOrBoxedOrBigIntegerOrBigDecimal(IType type)
  {
    return type != null &&
            !JavaTypes.pVOID().equals(type) &&
            ( type.isPrimitive() ||
              TypeSystem.getPrimitiveType(type) != null ||
              JavaTypes.BIG_INTEGER().equals(type) ||
              JavaTypes.BIG_DECIMAL().equals(type)
            );
  }

  private IType findLeastUpperBoundWithCoercions(ConditionalTernaryExpression cte, Expression first, Expression second) {
    IType type;
    IType firstType = first.getType();
    IType secondType = second.getType();
    if( isPrimitiveOrBoxedOrBigIntegerOrBigDecimal( firstType ) &&
        isPrimitiveOrBoxedOrBigIntegerOrBigDecimal( secondType ) )
    {
      type = TypeLord.getLeastUpperBoundForPrimitiveTypes( firstType, secondType );
      if( !verify( cte, type != null, Res.MSG_LOSS_OF_PRECISION_IN_CONDITIONAL_EXP ) )
      {
        type = ErrorType.getInstance();
      }
    }
    else if( GosuParserTypes.NULL_TYPE().equals( firstType ) && GosuParserTypes.NULL_TYPE().equals( secondType ) )
    {
      IType ctxType = getContextType().getType();
      return ctxType != null && !ctxType.isPrimitive() ? ctxType : GosuParserTypes.NULL_TYPE();
    }
    else if( GosuParserTypes.NULL_TYPE().equals( firstType ) && secondType.isPrimitive() )
    {
      return TypeLord.getBoxedTypeFromPrimitiveType( secondType );
    }
    else if( GosuParserTypes.NULL_TYPE().equals( secondType ) && firstType.isPrimitive() )
    {
      return TypeLord.getBoxedTypeFromPrimitiveType( firstType );
    } // HACK
      // Do not allow literal strings that are coercable to the type of the other side to modify the
      //type of the expression
    else if( canCoerceFromString(first, second) )
    {
      type = secondType;
    } // HACK
      // Do not allow literal strings that are coercable to the type of the other side to modify the
      //type of the expression
    else if( canCoerceFromString(second, first) )
    {
      type = firstType;
    }
    else
    {
      if( firstType.isPrimitive() && !GosuParserTypes.NULL_TYPE().equals( firstType ) && !secondType.isPrimitive() && !StandardCoercionManager.isBoxed( secondType ) )
      {
        firstType = TypeLord.getBoxedTypeFromPrimitiveType( firstType );
      }
      else if( secondType.isPrimitive() && !GosuParserTypes.NULL_TYPE().equals( secondType ) && !firstType.isPrimitive() && !StandardCoercionManager.isBoxed( firstType ) )
      {
        secondType = TypeLord.getBoxedTypeFromPrimitiveType( secondType );
      }

      List<IType> list = new ArrayList<IType>();

      if( !GosuParserTypes.NULL_TYPE().equals( firstType ) )
      {
        list.add( firstType );
      }

      if( !GosuParserTypes.NULL_TYPE().equals( secondType )  )
      {
        list.add( secondType );
      }

      type = TypeLord.findLeastUpperBound( list );
    }
    return type;
  }

  private boolean canCoerceFromString(Expression first, Expression second)
  {
    return first instanceof ILiteralExpression &&
           !(second instanceof ILiteralExpression) &&
           JavaTypes.STRING().equals(first.getType()) &&
           CommonServices.getCoercionManager().canCoerce( second.getType(), JavaTypes.STRING() );
  }

  private Expression wrapExpressionIfNeeded( Expression first, Expression second )
  {
    if( first.getType() == JavaTypes.pVOID() && second.getType().isPrimitive() )
    {
      return possiblyWrapWithImplicitCoercion( first, TypeSystem.getBoxType( second.getType() ) );
    }
    else if( second.getType() == JavaTypes.pVOID() && first.getType().isPrimitive() )
    {
      return possiblyWrapWithImplicitCoercion( first, TypeSystem.getBoxType( first.getType() ) );
    }
    return first;
  }


  //------------------------------------------------------------------------------
  // conditional-or-expression
  //   <conditional-and-expression>
  //   <conditional-or-expression> || <conditional-and-expression>
  //
  // Left recursion removed is:
  //
  // conditional-or-expression
  //   <conditional-and-expression> <conditional-or-expression2>
  //
  // conditional-or-expression2
  //   || <conditional-and-expression>
  //   <null>
  //
  void parseConditionalOrExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseConditionalAndExpression();

    // <conditional-or-expression2>
    boolean foundOr = false;
    do
    {
      if( match( null, "||", SourceCodeTokenizer.TT_OPERATOR ) || match( null, Keyword.KW_or ) )
      {
        _ctxInferenceMgr.clear();
        foundOr = true;
        parseConditionalAndExpression();

        ConditionalOrExpression e = new ConditionalOrExpression();

        Expression rhs = popExpression();
        verifyComparable( JavaTypes.pBOOLEAN(), rhs, true, true );
        rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pBOOLEAN() );

        Expression lhs = popExpression();
        verifyComparable( JavaTypes.pBOOLEAN(), lhs, true, true );
        lhs = possiblyWrapWithImplicitCoercion( lhs, JavaTypes.pBOOLEAN() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
    if (foundOr) {
      _ctxInferenceMgr.clear();
    }
  }

  //------------------------------------------------------------------------------
  // conditional-and-expression
  //   <equality-expression>
  //   <conditional-and-expression> && <equality-expression>
  //
  // Left recursion removed is:
  //
  // conditional-and-expression
  //   <equality-expression> <conditional-and-expression2>
  //
  // conditional-and-expression2
  //   && <equality-expression>
  //   <null>
  //
  void parseConditionalAndExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseBitwiseOrExpression();

    // <conditional-and-expression2>
    do
    {
      if( match( null, "&&", SourceCodeTokenizer.TT_OPERATOR ) || match( null, Keyword.KW_and ) )
      {
        parseBitwiseOrExpression();

        ConditionalAndExpression e = new ConditionalAndExpression();

        Expression rhs = popExpression();
        verifyComparable( JavaTypes.pBOOLEAN(), rhs, true, true );
        rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pBOOLEAN() );

        Expression lhs = popExpression();
        verifyComparable( JavaTypes.pBOOLEAN(), lhs, true, true );
        lhs = possiblyWrapWithImplicitCoercion( lhs, JavaTypes.pBOOLEAN() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  //------------------------------------------------------------------------------
  // bitwise-or-expression
  //   <bitwise-xor-expression>
  //   <bitwise-or-expression> | <bitwise-xor-expression>
  //
  // Left recursion removed is:
  //
  // bitwise-or-expression
  //   <bitwise-xor-expression> <bitwise-or-expression2>
  //
  // bitwise-or-expression2
  //   | <bitwise-xor-expression>
  //   <null>
  //
  void parseBitwiseOrExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseBitwiseXorExpression();

    // <bitwise-or-expression2>
    do
    {
      if( match( null, "|", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        BitwiseOrExpression e = new BitwiseOrExpression();
        Expression lhs = popExpression();

        lhs = ensureOperandIntOrLongOrBoolean( lhs );

        parseBitwiseXorExpression();
        Expression rhs = popExpression();

        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          rhs = ensureOperandBoolean( rhs );
        }
        else
        {
          rhs = ensureOperandIntOrLong( rhs );
        }

        rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          e.setType( lhs.getType() );
        }
        else
        {
          e.setType( resolveTypeForArithmeticExpression( e, lhs.getType(), "|", rhs.getType() ) );
        }
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  private Expression ensureOperandIntOrLongOrBoolean( Expression op )
  {
    IType opType = op.getType();
    if( verify( op, isPrimitiveOrBoxedIntegerType(opType) || opType == JavaTypes.BOOLEAN() || opType == JavaTypes.pBOOLEAN(), Res.MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG ) )
    {
      opType = opType == JavaTypes.LONG() || opType == JavaTypes.pLONG()
               ? JavaTypes.pLONG()
               : opType == JavaTypes.BOOLEAN() || opType == JavaTypes.pBOOLEAN()
                 ? JavaTypes.pBOOLEAN()
                 : JavaTypes.pINT();
      op = possiblyWrapWithImplicitCoercion( op, opType );
    }
    return op;
  }

  private boolean isPrimitiveOrBoxedIntegerType(IType type) {
    return type == JavaTypes.LONG() || type == JavaTypes.pLONG() ||
      type == JavaTypes.CHARACTER() || type == JavaTypes.pCHAR() ||
      type == JavaTypes.INTEGER() || type == JavaTypes.pINT() ||
      type == JavaTypes.SHORT() || type == JavaTypes.pSHORT() ||
      type == JavaTypes.BYTE() || type == JavaTypes.pBYTE();
  }

  private Expression ensureOperandBoolean( Expression op )
  {
    IType opType = op.getType();
    if( verify( op, opType == JavaTypes.BOOLEAN() || opType == JavaTypes.pBOOLEAN(), Res.MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN ) )
    {
      op = possiblyWrapWithImplicitCoercion( op, JavaTypes.pBOOLEAN() );
    }
    return op;
  }

  private Expression ensureOperandIntOrLong( Expression op )
  {
    IType opType = op.getType();
    if( verify( op, isPrimitiveOrBoxedIntegerType(opType), Res.MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG ) )
    {
      opType = opType == JavaTypes.LONG() || opType == JavaTypes.pLONG() ? JavaTypes.pLONG() : JavaTypes.pINT();
      op = possiblyWrapWithImplicitCoercion( op, opType );
    }
    return op;
  }

  //------------------------------------------------------------------------------
  // bitwise-xor-expression
  //   <bitwise-and-expression>
  //   <bitwise-xor-expression> ^ <bitwise-xor-expression>
  //
  // Left recursion removed is:
  //
  // bitwise-xor-expression
  //   <bitwise-and-expression> <bitwise-xor-expression2>
  //
  // bitwise-xor-expression2
  //   ^ <bitwise-and-expression>
  //   <null>
  //
  void parseBitwiseXorExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseBitwiseAndExpression();

    // <bitwise-xor-expression2>
    do
    {
      if( match( null, "^", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        BitwiseXorExpression e = new BitwiseXorExpression();
        Expression lhs = popExpression();

        lhs = ensureOperandIntOrLongOrBoolean( lhs );

        parseBitwiseAndExpression();
        Expression rhs = popExpression();

        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          rhs = ensureOperandBoolean( rhs );
        }
        else
        {
          rhs = ensureOperandIntOrLong( rhs );
        }

        rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          e.setType( lhs.getType() );
        }
        else
        {
          e.setType( resolveTypeForArithmeticExpression( e, lhs.getType(), "^", rhs.getType() ) );
        }
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  //------------------------------------------------------------------------------
  // bitwise-and-expression
  //   <equality-expression>
  //   <bitwise-and-expression> & <equality-expression>
  //
  // Left recursion removed is:
  //
  // bitwise-and-expression
  //   <equality-expression> <bitwise-and-expression2>
  //
  // bitwise-and-expression2
  //   ^ <equality-expression>
  //   <null>
  //
  void parseBitwiseAndExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseEqualityExpression();

    // <bitwise-and-expression2>
    do
    {
      if( match( null, "&", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        BitwiseAndExpression e = new BitwiseAndExpression();
        Expression lhs = popExpression();

        lhs = ensureOperandIntOrLongOrBoolean( lhs );

        parseEqualityExpression();
        Expression rhs = popExpression();

        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          rhs = ensureOperandBoolean( rhs );
        }
        else
        {
          rhs = ensureOperandIntOrLong( rhs );
        }

        verifyComparable( lhs.getType(), rhs, true, true );
        rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        if( lhs.getType() == JavaTypes.pBOOLEAN() )
        {
          e.setType( lhs.getType() );
        }
        else
        {
          e.setType( resolveTypeForArithmeticExpression( e, lhs.getType(), "&", rhs.getType() ) );
        }
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  void parseEqualityExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    boolean matchOldNotEqOp = false;

    parseRelationalExpression();

    // <relational-expression2>
    do
    {
      boolean bEq;
      Token token = getTokenizer().getCurrentToken();
      String value = token.getStringValue();
      if( token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
          ((bEq = value.equals( "===" )) ||
           value.equals( "!==" )) )
      {
        getTokenizer().nextToken();

        IdentityExpression e = new IdentityExpression();
        Expression lhs = popExpression();

        pushInferredContextTypes( new ContextType( lhs.getType() ) );
        try
        {
          parseRelationalExpression();
        }
        finally
        {
          popInferredContextTypes();
        }
        Expression rhs = popExpression();
        if( verify( lhs, !lhs.getType().isPrimitive() ||
                         JavaTypes.pVOID().isAssignableFrom( lhs.getType() ) ||
                         JavaTypes.STRING().isAssignableFrom( lhs.getType() ), Res.MSG_PRIMITIVES_NOT_ALLOWED_HERE ) &&
            verify( rhs, !rhs.getType().isPrimitive() ||
                         JavaTypes.pVOID().isAssignableFrom( rhs.getType() ) ||
                         JavaTypes.STRING().isAssignableFrom( rhs.getType() ), Res.MSG_PRIMITIVES_NOT_ALLOWED_HERE ) )
        {
          verifyComparable( lhs.getType(), rhs, true, true );
        }

        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setEquals( bEq );
        pushExpression( e );
      }
      else if( token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
          ((bEq = value.equals( "==" )) ||
           value.equals( "!=" ) ||
           (matchOldNotEqOp = value.equals( "!=" ))) )
      {
        getTokenizer().nextToken();

        EqualityExpression e = new EqualityExpression();
        Expression lhs = popExpression();

        verify( e, !matchOldNotEqOp, Res.MSG_OBSOLETE_NOT_EQUAL_OP );
        pushInferredContextTypes( new ContextType( lhs.getType() ) );
        try
        {
          parseRelationalExpression();
        }
        finally
        {
          popInferredContextTypes();
        }

        Expression rhs = popExpression();
        rhs = verifyConditionalTypes( lhs, rhs );

        //## see PL-9512
        verifyCoercionSymmetry( e, lhs, rhs );

        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setEquals( bEq );

        warnOnSuspiciousEqualsOperator(e);

        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }

      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  private void warnOnSuspiciousEqualsOperator( EqualityExpression e )
  {
    Expression lhs = e.getLHS();
    Expression rhs = e.getRHS();
    if( (isThisPointer( lhs ) && isEqualsArgReference( rhs )) ||
            (isEqualsArgReference( lhs ) && isThisPointer( rhs )) )
    {
      warn( e, false, Res.MSG_WARN_ON_SUSPICIOUS_THIS_COMPARISON );
    }
  }

  private boolean isThisPointer( Expression expr )
  {
    return expr != null &&
            expr instanceof Identifier &&
            ((Identifier)expr).getSymbol() != null &&
            Keyword.KW_this.getName().equals( ((Identifier)expr).getSymbol().getName() );
  }

  private boolean isEqualsArgReference( Expression expr )
  {
    if( isParsingFunction() && expr != null && expr instanceof Identifier )
    {
      FunctionType functionType = peekParsingFunction();
      if( "equals".equals( functionType.getDisplayName() ) )
      {
        if( functionType.getParameterTypes().length == 1 &&
                functionType.getParameterTypes()[0] == JavaTypes.OBJECT() )
        {
          ISymbol symbol = ((Identifier)expr).getSymbol();
          if( symbol != null && functionType.getParameterNames()[0].equals( symbol.getName() ) )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  private void verifyCoercionSymmetry( EqualityExpression e, Expression lhs, Expression rhs )
  {
    if( !lhs.hasParseExceptions() && !rhs.hasParseExceptions() )
    {
      ICoercionManager cocerionManager = CommonServices.getCoercionManager();
      boolean bDontWarn =
              ((lhs.getType() != JavaTypes.OBJECT() && rhs.getType() != JavaTypes.OBJECT()) || // neither side is Object, or
                      (lhs.getType() == JavaTypes.pVOID() || rhs.getType() == JavaTypes.pVOID()) ||   // one side is "null", or
                      (lhs.getType() != null && BeanAccess.isBeanType( lhs.getType() ) &&         // both sides are "beans"
                              rhs.getType() != null && BeanAccess.isBeanType( rhs.getType() )) ||        // ... , or
                      cocerionManager.resolveCoercerStatically( lhs.getType(), rhs.getType() ) == // coercer is symmetric
                              cocerionManager.resolveCoercerStatically( rhs.getType(), lhs.getType() ));
      verifyOrWarn( e, bDontWarn, true,
              Res.MSG_ASYMMETRICAL_COMPARISON, lhs.getType(), rhs.getType() );
    }
  }

  private void verifyRelationalOperandsComparable( BinaryExpression expr )
  {
    if( !verify( expr, expr.getRHS().getType() != JavaTypes.OBJECT(), Res.MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE, expr.getOperator(), Object.class.getName() ) )
    {
      return;
    }
    boolean bComparable = false;
    IType lhsType = expr.getLHS().getType();
    IType rhsType = expr.getRHS().getType();
    if( BeanAccess.isNumericType( lhsType ) )
    {
      if( (JavaTypes.IDIMENSION().isAssignableFrom( lhsType ) && isFinalDimension( this, lhsType, expr ) ||
           JavaTypes.IDIMENSION().isAssignableFrom( rhsType ) && isFinalDimension( this, rhsType, expr )) &&
           lhsType != rhsType )
      {
        // Operands must both be Dimensions for comparison
        addError( expr, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE );
        return;
      }
      else
      {
        bComparable = true;
      }
    }
    else if( lhsType == GosuParserTypes.DATETIME_TYPE() )
    {
      bComparable = true;
    }
    else
    {
      if( BeanAccess.isBeanType( lhsType ) )
      {
        if( BeanAccess.isBeanType( rhsType ) )
        {
          if( lhsType.isAssignableFrom( rhsType ) )
          {
            if( JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
            {
              bComparable = true;
            }
          }
        }
      }
      if( !bComparable &&
              (JavaTypes.CHAR_SEQUENCE().isAssignableFrom( lhsType ) ||
                      JavaTypes.CHAR_SEQUENCE().isAssignableFrom( rhsType )) )
      {
        bComparable = true;
      }
      if( !bComparable )
      {
        bComparable = lhsType.isDynamic() || rhsType.isDynamic();
      }
    }
    verify( expr, bComparable, Res.MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE, expr.getOperator(), lhsType );
  }

  private Expression verifyConditionalTypes( Expression lhs, Expression rhs )
  {
    IType lhsType = lhs.getType();
    IType rhsType = rhs.getType();

    if( lhsType.isPrimitive() && !(lhs instanceof NullExpression) && rhs instanceof NullExpression ||
        rhsType.isPrimitive() && !(rhs instanceof NullExpression) && lhs instanceof NullExpression )
    {
      rhs.addParseException( new ParseException( makeFullParserState(), lhsType, Res.MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE, "",  JavaTypes.pVOID().getName() ) );
      return rhs;
    }

    if( areMetaTypes( lhsType ,rhsType ) )
    {
      verify( rhs, TypeSystem.canCast( lhsType, rhsType ), Res.MSG_TYPE_MISMATCH, lhsType.getName(), rhsType.getName() );
      return rhs;
    }

    IType numberType = ParserBase.resolveType(lhsType, '>', rhsType);
    if( numberType instanceof ErrorType ||
        JavaTypes.IDIMENSION().isAssignableFrom( lhsType ) ||
        JavaTypes.IDIMENSION().isAssignableFrom( rhsType ) )
    {
      Expression wrappedRhs = verifyWithComparableDimension( rhs, lhsType );
      if( wrappedRhs != null )
      {
        return wrappedRhs;
      }
      // Not a number, verify types the general way
      verifyComparable( lhs.getType(), rhs, true, true );
    }
    else
    {
      // Get coercion warnings if any
      verifyComparable( numberType, rhs, false, true );
      verifyComparable( numberType, lhs, false, true );
    }
    return rhs;
  }

  private boolean areMetaTypes( IType lhsType, IType rhsType )
  {
    return (lhsType instanceof IMetaType || lhsType instanceof ITypeVariableType) &&
           (rhsType instanceof IMetaType || rhsType instanceof ITypeVariableType);
  }

  private Expression verifyWithComparableDimension( Expression rhs, IType lhsType )
  {
    if( JavaTypes.IDIMENSION().isAssignableFrom( lhsType ) &&
            JavaTypes.COMPARABLE().isAssignableFrom( lhsType ) )
    {
      for( IType iface : lhsType.getInterfaces() )
      {
        if( JavaTypes.COMPARABLE().isAssignableFrom( iface.getGenericType() ) )
        {
          IType type = iface.getTypeParameters()[0];
          verifyComparable( type, rhs, true, true );
          if( !rhs.hasParseExceptions() )
          {
            rhs = possiblyWrapWithImplicitCoercion( rhs, type );
          }
          return rhs;
        }
      }
    }
    return null;
  }

  //------------------------------------------------------------------------------
  // relational-expression
  //   <bitshift-expression>
  //   <relational-expression> < <bitshift-expression>
  //   <relational-expression> > <bitshift-expression>
  //   <relational-expression> <= <bitshift-expression>
  //   <relational-expression> >= <bitshift-expression>
  //   <relational-expression> typeis <type-literal>
  //   <relational-expression> as <type-literal>
  //
  // Left recursion removed from this *grammar* is:
  //
  // relational-expression
  //   <bitshift-expression> <relational-expression2>
  //
  // relational-expression2
  //   < <bitshift-expression>
  //   > <bitshift-expression>
  //   <= <bitshift-expression>
  //   >= <bitshift-expression>
  //   typeis <type-literal>
  //   as <type-literal>
  //   <null>
  //
  void parseRelationalExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    // <bitshift-expression>
    parseIntervalExpression();

    // <relational-expression2>
    do
    {
      Token token = getTokenizer().getCurrentToken();
      String value = token.getStringValue();
      if( token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
          (value.equals( "<" ) ||
           value.equals( ">" ) ||
           value.equals( "<=" )) )
      {
        getTokenizer().nextToken();
        if( value.equals( ">" ) && match( null, "=", SourceCodeTokenizer.TT_OPERATOR, true ) )
        {
          if( token.getTokenEnd() == getTokenizer().getCurrentToken().getTokenStart() )
          {
            value = ">=";
            match( null, "=", SourceCodeTokenizer.TT_OPERATOR );
          }
        }
        parseIntervalExpression();

        RelationalExpression e = new RelationalExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        rhs = verifyConditionalTypes( lhs, rhs );
        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setOperator( value );
        if( !lhs.hasParseExceptions() && !rhs.hasParseExceptions() )
        {
          verifyRelationalOperandsComparable( e );
        }
        pushExpression( e );
      }
      else if( match( null, Keyword.KW_typeis ) )
      {
        parseTypeLiteral();

        TypeIsExpression e = new TypeIsExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        if( verify( rhs, rhs instanceof TypeLiteral, Res.MSG_EXPECTING_TYPELITERAL ) )
        {
          verify( lhs, !lhs.getType().isPrimitive(), Res.MSG_PRIMITIVES_NOT_ALLOWED_HERE );
          IType rhsType = ((TypeLiteral)rhs).getType().getType();
          verify( rhs, !rhsType.isPrimitive(), Res.MSG_PRIMITIVES_NOT_ALLOWED_HERE );
          verify( rhs, TypeLoaderAccess.instance().canCast( lhs.getType(), rhsType ), Res.MSG_INCONVERTIBLE_TYPES, lhs.getType().getName(), rhsType.getName() );
          e.setRHS( (TypeLiteral)rhs );
          _ctxInferenceMgr.updateType( ContextInferenceManager.getUnwrappedExpression( lhs ), e.getRHS().evaluate() );
        }
        e.setLHS( lhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  //------------------------------------------------------------------------------
  // interval-expression
  //   <bitshift-expression>
  //   <interval-expression> .. <bitshift-expression>
  //   <interval-expression> |.. <bitshift-expression>
  //   <interval-expression> ..| <bitshift-expression>
  //   <interval-expression> |..| <bitshift-expression>
  //
  // Left recursion removed from this *grammar* is:
  //
  // interval-expression
  //   <bitshift-expression> <interval-expression2>
  //
  // interval-expression2
  //   .. <bitshift-expression> <interval-expression2>
  //   |.. <bitshift-expression> <interval-expression2>
  //   ..| <bitshift-expression> <interval-expression2>
  //   |..| <bitshift-expression> <interval-expression2>
  //   <null>
  //
  void parseIntervalExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseIntervalExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseIntervalExpression()
  {
    // <multiplicative-expression>
    parseBitshiftExpression();

    // <additive-expression2>
    Token token = getTokenizer().getCurrentToken();
    int mark = getTokenizer().mark();
    String value = token.getStringValue();
    boolean bOperator = token.getType() == SourceCodeTokenizer.TT_OPERATOR;
    boolean bClosed = bOperator && "..".equals( value );
    boolean bLeftOpen = !bClosed && bOperator && "|..".equals( value );
    boolean bNextTokenIsDotNoWhitespace = false;
    if( bClosed || bLeftOpen ) {
      getTokenizer().nextToken();
      Token dotToken = getTokenizer().getTokenAt( mark + 1 );
      if( bNextTokenIsDotNoWhitespace = dotToken != null && dotToken.getType() == '.' )
      {
        getTokenizer().nextToken();
      }
    }
    boolean bRightOpen = !bClosed && !bLeftOpen && bOperator && "..|".equals( value );
    if( bRightOpen )
    {
      getTokenizer().nextToken();
    }
    else if( !bClosed && !bLeftOpen && !bRightOpen && bOperator && "|..|".equals( value ) )
    {
      getTokenizer().nextToken();
      bLeftOpen = bRightOpen = true;
    }
    if( bClosed || bLeftOpen || bRightOpen )
    {
      parseBitshiftExpression();

      Expression rhs = popExpression();
      Expression lhs = popExpression();
      rhs = verifyConditionalTypes( lhs, rhs );

      IType type = IntervalExpression.getIntervalType( rhs.getType() );
      verifyComparable( rhs.getType(), lhs );
      if( !lhs.hasImmediateParseIssue( Res.MSG_IMPLICIT_COERCION_ERROR ) &&
          !lhs.hasImmediateParseIssue( Res.MSG_TYPE_MISMATCH ) )
      {
        lhs = possiblyWrapWithImplicitCoercion( lhs, rhs.getType() );
      }
      else
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        lhs.removeParseException( Res.MSG_IMPLICIT_COERCION_ERROR );
        //noinspection ThrowableResultOfMethodCallIgnored
        lhs.removeParseException( Res.MSG_TYPE_MISMATCH );
        type = IntervalExpression.getIntervalType( lhs.getType() );
        verifyComparable( lhs.getType(), rhs );

        rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );
      }
      IntervalExpression e = new IntervalExpression( bClosed || !bLeftOpen, bClosed || !bRightOpen, lhs, rhs );
      verify( e, !bNextTokenIsDotNoWhitespace, Res.MSG_EXTRA_DOT_FOUND_IN_INTERVAL );
      //## todo: move to foreach: verify( e, JavaTypes.ITERABLE().isAssignableFrom( type ), Res.MSG_INTERVAL_MUST_BE_ITERABLE_HERE );
      e.setType( type );
      if( !lhs.hasParseExceptions() && !rhs.hasParseExceptions() )
      {
        verifyRelationalOperandsComparable( e );
      }
      pushExpression(e);
    }
  }

  //------------------------------------------------------------------------------
  // bitshift-expression
  //   <additive-expression>
  //   <bitshift-expression> << <additive-expression>
  //   <bitshift-expression> >> <additive-expression>
  //   <bitshift-expression> >>> <additive-expression>
  //
  // Left recursion removed from this *grammar* is:
  //
  // bitshift-expression
  //   <addtive-expression> <bitshift-expression2>
  //
  // bitshift-expression2
  //   << <additive-expression> <bitshift-expression2>
  //   >> <additive-expression> <bitshift-expression2>
  //   >>> <additive-expression> <bitshift-expression2>
  //   <null>
  //
  void parseBitshiftExpression()
  {
    Token token = _tokenizer.getCurrentToken();
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();

    // <additive-expression>
    parseAdditiveExpression();

    // <bitshift-expression2>
    do
    {
      token = _tokenizer.getCurrentToken();
      boolean bLeftShift;
      if( (bLeftShift = (SourceCodeTokenizer.TT_OPERATOR == token.getType() && "<<".equals( token.getStringValue() ))) || matchRightShift() )
      {
        Token T = new Token();
        if( bLeftShift )
        {
          match( T, "<<", SourceCodeTokenizer.TT_OPERATOR );
        }

        if( T._strValue == null || !T._strValue.equals( "<<" ) )
        {
          T._strValue = ">>";
          Token gr = new Token();
          int lastEnd = getTokenizer().getPriorToken().getTokenEnd();
          if( match( gr, ">", SourceCodeTokenizer.TT_OPERATOR, true ) )
          {
            if( lastEnd == gr.getTokenStart() )
            {
              T._strValue = ">>>";
              match( null, ">", SourceCodeTokenizer.TT_OPERATOR );
            }
          }
        }

        parseAdditiveExpression();
        BitshiftExpression e = new BitshiftExpression();

        Expression rhs = popExpression();

        // Lhs must be an int or a long
        Expression lhs = popExpression();
        lhs = ensureOperandIntOrLong( lhs );
        // Rhs must be an int
        rhs = ensureOperandIntOrLong( rhs );
        rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pINT() );

        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setOperator( T._strValue );
        e.setType( lhs.getType() );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  private boolean matchRightShift()
  {
    SourceCodeTokenizer tokenizer = getTokenizer();
    int mark = tokenizer.mark();
    Token t = tokenizer.getTokenAt( mark );
    if( t != null && t.getType() == SourceCodeTokenizer.TT_OPERATOR && ">".equals( t.getStringValue() ) )
    {
      t = tokenizer.getTokenAt( mark + 1 );
      if( t != null && t.getType() == SourceCodeTokenizer.TT_OPERATOR && ">".equals( t.getStringValue() ) )
      {
        tokenizer.nextToken();
        tokenizer.nextToken();
        return true;
      }
    }
    return false;
  }

  //------------------------------------------------------------------------------
  // additive-expression
  //   <multiplicative-expression>
  //   <additive-expression> + <multiplicative-expression>
  //   <additive-expression> - <multiplicative-expression>
  //
  // Left recursion removed from this *grammar* is:
  //
  // additive-expression
  //   <multiplicative-expression> <additive-expression2>
  //
  // additive-expression2
  //   + <multiplicative-expression> <additive-expression2>
  //   - <multiplicative-expression> <additive-expression2>
  //   <null>
  //
  void parseAdditiveExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    // <multiplicative-expression>
    parseMultiplicativeExpression();

    // <additive-expression2>
    do
    {
      Token token = getTokenizer().getCurrentToken();
      String value = token.getStringValue();
      boolean bOperator = token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null;
      boolean bPlus = bOperator &&
                      (value.equals( "+" ) ||
                       value.equals( "?+" ) ||
                       value.equals( "!+" ));
      boolean bMinus = !bPlus && bOperator &&
                      (value.equals( "-" ) ||
                       value.equals( "?-" ) ||
                       value.equals( "!-" ));
      if( bPlus || bMinus )
      {
        getTokenizer().nextToken();

        parseMultiplicativeExpression();

        AdditiveExpression e = new AdditiveExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setOperator( value );
        IType type = resolveTypeForArithmeticExpression( e, lhs.getType(), bPlus ? "+" : "-", rhs.getType() );
        e.setType( type );
        verify( e, TypeSystem.isNumericType( type ) || value.charAt(0) != '!', Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, value, lhs.getType().getDisplayName(), rhs.getType().getDisplayName() );
        verify( e, !(e.isNullSafe() && e.getType().isPrimitive()), Res.MSG_EXPECTING_REFERENCE_TYPE );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }

      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  //------------------------------------------------------------------------------
  // multiplicative-expression
  //   <unary-expression>
  //   <multiplicative-expression> * <unary-expression>
  //   <multiplicative-expression> / <unary-expression>
  //   <multiplicative-expression> % <unary-expression>
  //
  // NOTE: See parseAdditiveExpression() above for an explanation of left recursion removal.
  //
  void parseMultiplicativeExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseTypeAsExpression();

    do
    {
      Token token = getTokenizer().getCurrentToken();
      String value = token.getStringValue();
      if( token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
          (value.equals( "*" ) ||
           value.equals( "/" ) ||
           value.equals( "%" ) ||
           value.equals( "?*" ) ||
           value.equals( "!*" ) ||
           value.equals( "?/" ) ||
           value.equals( "?%" )) )
      {
        getTokenizer().nextToken();

        parseTypeAsExpression();

        MultiplicativeExpression e = new MultiplicativeExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        e.setLHS( lhs );
        e.setRHS( rhs );
        e.setOperator( value );
        IType type = resolveTypeForArithmeticExpression( e, lhs.getType(), value, rhs.getType() );
        e.setType( type );
        verify( e, TypeSystem.isNumericType( type ) || value.charAt(0) != '!', Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, "!*", lhs.getType().getDisplayName(), rhs.getType().getDisplayName() );
        verify( e, !(e.isNullSafe() && e.getType().isPrimitive()), Res.MSG_EXPECTING_REFERENCE_TYPE );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  void parseTypeAsExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    parseUnaryExpression();

    do
    {
      if( match( null, Keyword.KW_typeas ) || match( null, Keyword.KW_as ))
      {
        parseTypeLiteral();

        TypeAsExpression e = new TypeAsExpression();
        Expression rhs = popExpression();
        Expression lhs = popExpression();
        if( !(rhs instanceof TypeLiteral) )
        {
          rhs.addParseException( new ParseException( makeFullParserState(), Res.MSG_EXPECTING_TYPELITERAL ) );
        }
        else
        {
          IType rhsType = ((TypeLiteral)rhs).getType().getType();
          checkComparableAndCastable( lhs, rhs );
          e.setType( rhsType );
          e.setCoercer( CommonServices.getCoercionManager().resolveCoercerStatically( rhsType, lhs.getType() ) );

          warn( lhs, lhs.getType() instanceof IErrorType ||
                  rhs.getType() instanceof IErrorType ||
                  !rhsType.isAssignableFrom( lhs.getType() ) ||
                  rhsType.isDynamic(),
                  Res.MSG_UNNECESSARY_COERCION, lhs.getType().getRelativeName(), rhsType.getRelativeName() );
        }

        e.setLHS( lhs );
        pushExpression( e );
      }
      else
      {
        // The <null> case
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );
    }
    while( true );
  }

  private boolean checkComparableAndCastable(Expression lhs, Expression rhs )
  {
    IType rhsType =  ((TypeLiteral)rhs).getType().getType();
    verify( rhs, rhsType != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
    verifyComparable( TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( rhsType ), lhs, false, false );
    boolean hasExceptions = rhs.hasParseExceptions() || lhs.hasParseExceptions();
    if( hasExceptions &&
        (!(lhs instanceof TypeLiteral) ||
         ((TypeLiteral)lhs).getType().getType() instanceof TypeVariableType ||
         !(rhsType instanceof IGosuClass && ((IGosuClass) rhsType).isStructure())) )
    {
      IType lhsType = lhs.getType();
      if( TypeSystem.canCast( lhsType, rhsType ) )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        lhs.removeParseException( Res.MSG_TYPE_MISMATCH );
        hasExceptions = false;
      }
    }
    return !hasExceptions;
  }

  //------------------------------------------------------------------------------
  // unary-expression
  //   + <unary-expression>
  //   - <unary-expression>
  //   <unary-expression-not-plus-minus>
  //
  void parseUnaryExpression()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseUnaryExpression();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseUnaryExpression()
  {
    Token token = getTokenizer().getCurrentToken();
    String value = token.getStringValue();
    if( token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
        (value.equals( "+" ) ||
         value.equals( "-" ) ||
         value.equals( "!-" )) )
    {
      getTokenizer().nextToken();
      
      boolean unchecked = "!-".equals( value );
      boolean negation = value.charAt( 0 ) == '-' || unchecked;
      token = getTokenizer().getCurrentToken();
      if( negation && atNumberLiteralStart() )
      {
        parseNumberLiteral( token, true );
      }
      else
      {
        parseUnaryExpressionNotPlusMinus();

        UnaryExpression ue = new UnaryExpression();
        Expression e = popExpression();
        IType type = e.getType();
        verify( e, ue.isSupportedType( type ), Res.MSG_NUMERIC_TYPE_EXPECTED );
        ue.setNegated( negation );
        ue.setUnchecked( unchecked );
        if( negation )
        {
          if( type == JavaTypes.pCHAR() || type == JavaTypes.pBYTE() || type == JavaTypes.pSHORT()  )
          {
            e = possiblyWrapWithCoercion( e, JavaTypes.pINT(), true );
          }
          else if( type == JavaTypes.CHARACTER() || type == JavaTypes.BYTE() || type == JavaTypes.SHORT() )
          {
            e = possiblyWrapWithCoercion( e, JavaTypes.INTEGER(), true );
          }
        }
        ue.setExpression( e );
        ue.setType( e.getType() );
        pushExpression( ue );
      }
    }
    else
    {
      parseUnaryExpressionNotPlusMinus();
    }
  }

  //------------------------------------------------------------------------------
  // unary-expression-not-plus-minus
  //   ~ <unary-expression>
  //   ! <unary-expression>
  //   not <unary-expression>
  //   typeof <unary-expression>
  //   eval( &lt;expression&gt; )
  //   <primary-expression>
  //
  void parseUnaryExpressionNotPlusMinus()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseUnaryExpressionNotPlusMinus();
    setLocation(iOffset, iLineNum, iColumn);

    checkMemberAccessIsReadable();
  }

  private void checkMemberAccessIsReadable()
  {
    Expression expr = peekExpression();
    if( expr instanceof MemberAccess )
    {
      IPropertyInfo pi = ((MemberAccess)expr).getPropertyInfoWithoutThrowing();
      if( pi != null )
      {
        verify( expr, pi.isReadable(),
                Res.MSG_CLASS_PROPERTY_NOT_READABLE, pi.getName(), pi.getOwnersType().getName() );
      }
    }
    else if( (expr instanceof Identifier &&
            ((Identifier)expr).getSymbol() instanceof DynamicPropertySymbol) )
    {
      DynamicPropertySymbol dps = (DynamicPropertySymbol)((Identifier)expr).getSymbol();
      if( dps != null && !dps.isReadable() )
      {
        verify( expr, dps.isReadable(),
                Res.MSG_CLASS_PROPERTY_NOT_READABLE, dps.getName(), dps.getScriptPart() == null ? "" : dps.getScriptPart().getContainingType().getName() );
      }
    }
  }

  void _parseUnaryExpressionNotPlusMinus()
  {
    if( match( null, "!", SourceCodeTokenizer.TT_OPERATOR ) || match( null, Keyword.KW_not ) )
    {
      _ctxInferenceMgr.pushCtx();
      try
      {
        parseUnaryExpression();
      }
      finally
      {
        _ctxInferenceMgr.popCtx( false );
      }
      UnaryNotPlusMinusExpression ue = new UnaryNotPlusMinusExpression();
      Expression e = popExpression();
      IType type = e.getType();
      verify( e, type == JavaTypes.pBOOLEAN() || type == JavaTypes.BOOLEAN() || type.isDynamic(), Res.MSG_TYPE_MISMATCH, "boolean", type.getDisplayName() );
      e = possiblyWrapWithImplicitCoercion( e, JavaTypes.pBOOLEAN() );
      ue.setExpression( e );
      ue.setNot( true );
      ue.setType( JavaTypes.pBOOLEAN() );
      pushExpression( ue );
    }
    else if( match( null, "~", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      pushInferredContextTypes( ContextType.EMPTY );
      parseUnaryExpression();
      popInferredContextTypes();
      UnaryNotPlusMinusExpression ue = new UnaryNotPlusMinusExpression();
      Expression e = popExpression();
      IType type = e.getType();
      if( type == JavaTypes.LONG() || type == JavaTypes.pLONG() )
      {
        e = possiblyWrapWithImplicitCoercion( e, JavaTypes.pLONG() );
      }
      else
      {
        if( verify( e, !type.isDynamic(), Res.MSG_DYNAMIC_TYPE_NOT_ALLOWED_HERE ) )
        {
          e = ensureOperandIntOrLong( e );
        }
      }
      ue.setExpression( e );
      ue.setBitNot( true );
      ue.setType( e.getType() );
      pushExpression( ue );
    }
    else if( match( null, Keyword.KW_typeof ) )
    {
      parseUnaryExpression();

      TypeOfExpression toe = new TypeOfExpression();
      Expression e = popExpression();
      toe.setExpression( e );
      pushExpression( toe );
    }
    else if( match( null, Keyword.KW_statictypeof ) )
    {
      parseUnaryExpression();

      StaticTypeOfExpression toe = new StaticTypeOfExpression();
      Expression e = popExpression();
      toe.setExpression( e );
      pushExpression( toe );
    }
    else if( match( null, "\\", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseBlockExpression();
    }
    else if( match( null, Keyword.KW_eval ) )
    {
      parseEvalExpression();
    }
    else
    {
      parsePrimaryExpression();
    }
  }

  private void parseEvalExpression()
  {
    EvalExpression evalExpr = new EvalExpression( getTypeUsesMap().copy() );
    List<ICapturedSymbol> captured = new ArrayList<ICapturedSymbol>();
    captureAllSymbols( null, getCurrentEnclosingGosuClass(), captured );
    evalExpr.setCapturedSymbolsForBytecode( captured );
    evalExpr.setCapturedTypeVars( new HashMap<String, ITypeVariableDefinition>( getTypeVariables() ) );

    verify( evalExpr, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_EVAL );
    parseExpression();
    verify( evalExpr, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_EVAL );
    Expression e = popExpression();
    evalExpr.setExpression( e );
    pushExpression( evalExpr );
  }

  public boolean isCaptureSymbolsForEval()
  {
    return _bCaptureSymbolsForEval;
  }
  public void setCaptureSymbolsForEval( boolean bCaputreSymbolsForEval )
  {
    _bCaptureSymbolsForEval = bCaputreSymbolsForEval;
  }

  //------------------------------------------------------------------------------
  // primary-expression
  //   null
  //   true
  //   false
  //   NaN
  //   Infinity
  //   <new-expression>
  //   <exists-expression>
  //   <member-access>
  //   <array-access>
  //   <name>
  //   <literal>
  //   <object-literal>
  //   ( <expression) )
  //   <method-call-expression>
  //   <query-expression>
  //
  void parsePrimaryExpression()
  {
    final Token token = _tokenizer.getCurrentToken();
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    boolean bForceRedundancy = _parsePrimaryExpression();
    setLocation( iOffset, iLineNum, iColumn, bForceRedundancy );
    Expression eas = peekExpression();
    if( recoverFromJavaStyleCast( eas ) )
    {
      setLocation( iOffset, iLineNum, iColumn, bForceRedundancy );
      // re-root the parenthesized expression under the implicit typeas we created
      Expression implicitTypeAsFromRecovery = peekExpression();
      getLocationsList().remove(eas.getLocation());
      implicitTypeAsFromRecovery.getLocation().addChild(eas.getLocation());
      eas.setParent( implicitTypeAsFromRecovery );
    }
    parseIndirectMemberAccess( iOffset, iLineNum, iColumn );
  }

  boolean _parsePrimaryExpression()
  {
    boolean bRet = false;

    Token token = getTokenizer().getCurrentToken();
    if( Keyword.KW_block == token.getKeyword() )
    {
      getTokenizer().nextToken();
      _parseBlockLiteral();
    }
    else if( Keyword.KW_new == token.getKeyword() )
    {
      getTokenizer().nextToken();
      parseNewExpression();
    }
    else if( parseNameOrMethodCall( token ) )
    {
    }
    else if( '(' == token.getType() )
    {
      getTokenizer().nextToken();
      
      parseExpressionNoVerify( isParenthesisTerminalExpression() ? getContextType() : ContextType.EMPTY );
      _ctxInferenceMgr.restoreLastCtx();
      Expression e = popExpression();
      ParenthesizedExpression expr = new ParenthesizedExpression( e );
      pushExpression( expr );

      verify( e, match( null, ')' ), Res.MSG_EXPECTING_EXPRESSION_CLOSE );
    }
    else if( parseStandAloneDataStructureInitialization( token ) )
    {
      bRet = true;
    }
    else
    {
      parseLiteral( token );
    }

    return bRet;
  }

  private boolean parseBooleanLiteral( Token token )
  {
    if( Keyword.KW_true == token.getKeyword() )
    {
      getTokenizer().nextToken();

      BooleanLiteral e = new BooleanLiteral( true );
      pushExpression( e );
      return true;
    }

    if( Keyword.KW_false == token.getKeyword() )
    {
      getTokenizer().nextToken();

      BooleanLiteral e = new BooleanLiteral( false );
      pushExpression( e );
      return true;
    }

    return false;
  }

  private boolean parseNullLiteral( Token token )
  {
    if( Keyword.KW_null == token.getKeyword() )
    {
      getTokenizer().nextToken();

      pushExpression( new NullExpression() );
      return true;
    }
    return false;
  }

  private boolean parseStandAloneDataStructureInitialization( Token token )
  {
    return parseStandAloneDataStructureInitialization( token, false, false );
  }
  private boolean parseStandAloneDataStructureInitialization( Token token, boolean bAvoidContextType, boolean bBacktracking )
  {
    int mark = getTokenizer().mark();
    int iLocationsCount = _locations.size();

    Token startToken = token;

    // infered data constructors
    if( '{' != token.getType() )
    {
      return false;
    }
    else
    {
      getTokenizer().nextToken();
      token = getTokenizer().getCurrentToken();

      bAvoidContextType = bAvoidContextType || shouldThisExpressionAvoidTheContextType();

      IType ctxType = bAvoidContextType ? null : getInitializableType().getType();
      NewExpression e = new InferredNewExpression();

      boolean bPlaceholder = ctxType != null && ctxType.isDynamic();
      if( ctxType == null || bPlaceholder )
      {
        IInitializerExpression initializer;
        IType type;
        if( '}' == token.getType() )
        {
          initializer = new CollectionInitializerExpression();
          type = JavaTypes.ARRAY_LIST().getParameterizedType( bPlaceholder ? ctxType : JavaTypes.OBJECT() );
        }
        else
        {
          parseExpression( bPlaceholder ? new ContextType( ctxType, false ) : ContextType.OBJECT_FALSE );
          Expression initialExpression = popExpression();
          Expression actualInitExpr = initialExpression;
          if( actualInitExpr instanceof ImplicitTypeAsExpression )
          {
            actualInitExpr = ((ImplicitTypeAsExpression)actualInitExpr).getLHS();
          }
          verify( actualInitExpr, actualInitExpr.getType() != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
          if( match( null, ',' ) )
          {
            _parseInitializerExpression( new ContextType( JavaTypes.ARRAY_LIST().getParameterizedType( bPlaceholder ? ctxType : JavaTypes.OBJECT() ), false ) );
            CollectionInitializerExpression collectionInit = (CollectionInitializerExpression)popExpression();
            collectionInit.addFirst( initialExpression );
            IType lub = bPlaceholder ? ctxType : TypeLord.findLeastUpperBound( getTypes( collectionInit.getValues() ) );
            type = JavaTypes.ARRAY_LIST().getParameterizedType( lub );
            initializer = collectionInit;
          }
          else if( match( null, "->", SourceCodeTokenizer.TT_OPERATOR ) )
          {
            parseExpression( bPlaceholder ? new ContextType( ctxType, false ) : ContextType.OBJECT_FALSE );
            Expression initialValueExpression = popExpression();
            MapInitializerExpression mapInitializer;
            if( match( null, ',' ) )
            {
              parseMapInitializerList( new ContextType( JavaTypes.HASH_MAP().getParameterizedType( bPlaceholder ? ctxType : JavaTypes.OBJECT(),
                      bPlaceholder ? ctxType : JavaTypes.OBJECT() ), false ) );
              mapInitializer = (MapInitializerExpression)popExpression();
            }
            else
            {
              mapInitializer = new MapInitializerExpression();
            }

            mapInitializer.addFirst( initialExpression, initialValueExpression );

            IType keysLub = TypeLord.findLeastUpperBound( getTypes( mapInitializer.getKeys() ) );
            IType valuesLub = TypeLord.findLeastUpperBound( getTypes( mapInitializer.getValues() ) );
            type = JavaTypes.HASH_MAP().getParameterizedType( keysLub, valuesLub );
            initializer = mapInitializer;
          }
          else
          {
            CollectionInitializerExpression collectionInit= new CollectionInitializerExpression();
            collectionInit.addFirst( initialExpression );
            IType lub = bPlaceholder ? ctxType : TypeLord.findLeastUpperBound( getTypes( collectionInit.getValues() ) );
            type = JavaTypes.ARRAY_LIST().getParameterizedType( lub );
            initializer = collectionInit;
          }
        }

        verify( e, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER );
        pushExpression( (Expression)initializer );
        setLocation( startToken.getTokenStart(), startToken.getLine(), startToken.getTokenColumn(), true );
        popExpression();

        e.setType( type );
        e.setConstructor( getImplicitConstructor(type) );
        ((Expression)initializer).setType( type );
        e.setInitializer( initializer );
      }
      else
      {
        e.setType( ctxType );
        if( !match( null, '}' ) )
        {
          ContextType typeToInit = getCurrentInitializableContextType();
          if( ctxType.isArray() )
          {
            IType ctxComponentType = ctxType.getComponentType();
            List<Expression> valueExpressions = parseArrayValueList( ctxComponentType );
            e.setValueExpressions( valueExpressions );
            if( !typeToInit.isMethodScoring() )
            {
              ArrayList<IType> types = new ArrayList<IType>();
              for (Object valueExpression : valueExpressions) {
                types.add(((Expression) valueExpression).getType());
              }
              IType componentLeastUpperBound = TypeLord.findLeastUpperBound( types );
              if( componentLeastUpperBound != GosuParserTypes.NULL_TYPE() )
              {
                //## todo: consider finding a way to preserve the compound type so that it can be coerced to a compatible array type
                //  if( componentLeastUpperBound instanceof CompoundType )
                //  {
                //    e.setType( JavaTypes.ARRAY_LIST().getParameterizedType( componentLeastUpperBound ) );
                //  }
                if( componentLeastUpperBound instanceof CompoundType )
                {
                  // Instead of not supporting compound type arrays (no way in java runtime)
                  // we grab one component of the type and use that (better than just Object[], right?)
                  for( IType comp: componentLeastUpperBound.getCompoundTypeComponents() )
                  {
                    if( ctxComponentType.isAssignableFrom( comp ) )
                    {
                      componentLeastUpperBound = comp;
                      if( !comp.isInterface() )
                      {
                        if( ctxComponentType.isInterface() && comp instanceof IMetaType )
                        {
                          componentLeastUpperBound = ctxComponentType;
                        }

                        // Favor class type over interface type
                        break;
                      }
                    }
                  }
                }
                if( !(componentLeastUpperBound instanceof CompoundType) )
                {
                  e.setType( componentLeastUpperBound.getArrayType());
                }
              }
            }
            if( !verify( e, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER ) )
            {
              if( !verify( e, !match( null, "->", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_UNEXPECTED_ARROW ) )
              {
                e.setType( ErrorType.getInstance() );
              }
            }
          }
          else
          {
            _parseInitializerExpression( new ContextType( e.getType(), false ) );
            IInitializerExpression initializerExpression = (IInitializerExpression)popExpression();
            e.setInitializer( initializerExpression );
            e.setConstructor( ctxType.getTypeInfo().getConstructor() );
            if( !typeToInit.isMethodScoring() )
            {
              IType initializerCtxType = getCurrentInitializableContextType().getType();
              if( (initializerCtxType.equals( JavaTypes.MAP() ) ||
                   initializerCtxType.equals( JavaTypes.HASH_MAP() )) &&
                  initializerExpression instanceof MapInitializerExpression )
              {
                MapInitializerExpression mapInitializer = (MapInitializerExpression)initializerExpression;
                IType keysLub = TypeLord.findLeastUpperBound( getTypes( mapInitializer.getKeys() ) );
                IType valuesLub = TypeLord.findLeastUpperBound( getTypes( mapInitializer.getValues() ) );
                if( keysLub != GosuParserTypes.NULL_TYPE() && valuesLub != GosuParserTypes.NULL_TYPE() )
                {
                  e.setType( e.getType().getGenericType().getParameterizedType( keysLub, valuesLub ) );
                }
              }
              else
              {
                if( ( JavaTypes.COLLECTION().equals( initializerCtxType.getGenericType() )  ||
                      JavaTypes.LIST().equals( initializerCtxType.getGenericType() )        ||
                      JavaTypes.ARRAY_LIST().equals( initializerCtxType.getGenericType() )  ||
                      JavaTypes.LINKED_LIST().equals( initializerCtxType.getGenericType() ) ||
                      JavaTypes.SET().equals( initializerCtxType.getGenericType() )         ||
                      JavaTypes.HASH_SET().equals( initializerCtxType.getGenericType() )    ||
                      JavaTypes.ITERABLE().equals( initializerCtxType.getGenericType() ))
                        && initializerExpression instanceof CollectionInitializerExpression )
                {
                  CollectionInitializerExpression collectionInitializerExpression = (CollectionInitializerExpression)initializerExpression;
                  IType valuesLub = TypeLord.findLeastUpperBound( getTypes( collectionInitializerExpression.getValues() ) );
                  if( valuesLub != GosuParserTypes.NULL_TYPE() )
                  {
                    e.setType( e.getType().getGenericType().getParameterizedType( valuesLub ) );
                  }
                }
              }
            }
            if( !verify( e, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER ) )
            {
              if( !verify( e, !match( null, "->", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_UNEXPECTED_ARROW ) )
              {
                e.setType( ErrorType.getInstance() );
              }
            }
            pushExpression( (Expression)initializerExpression );
            setLocation( startToken.getTokenStart(), startToken.getLine(), startToken.getTokenColumn(), true );
            popExpression();
            if( !bBacktracking && initializerExpression.hasParseExceptions() )
            {
              return maybeReparseWithoutContextType( mark, iLocationsCount, (Expression)initializerExpression );
            }
          }
        }
        else
        {
          e.setConstructor( ctxType.getTypeInfo().getConstructor() );
        }
      }

      pushExpression( e );
      return true;
    }
  }

  private boolean maybeReparseWithoutContextType( int mark, int iLocationsCount, Expression initializerExpression )
  {
    backtrack( mark, iLocationsCount, initializerExpression );
    Token token = getTokenizer().getCurrentToken();
    boolean bRes = parseStandAloneDataStructureInitialization( token, true, true );
    if( peekExpression().hasParseExceptions() )
    {
      backtrack( mark, iLocationsCount, initializerExpression );
      return parseStandAloneDataStructureInitialization( token, false, true );
    }
    else
    {
      return bRes;
    }
  }

  private boolean shouldThisExpressionAvoidTheContextType() {
    int mark = getTokenizer().mark();
    eatBlock( '{', '}', false );
    Token token = getTokenizer().getCurrentToken();
    String value = token.getStringValue();
    boolean bAvoidContextType =
      '.' == token.getType() ||
      '[' == token.getType() ||
     (token.getType() == SourceCodeTokenizer.TT_OPERATOR &&
      ("?.".equals( value ) ||
       "*.".equals( value ) ||
       "==".equals( value ) ||
       "!=".equals( value ) ||
       "===".equals( value ) ||
       "!==".equals( value ) ||
       "#".equals( value ) ||
       "?".equals( value ) ||
       "?[".equals( value )));
    getTokenizer().restoreToMark( mark );
    return bAvoidContextType;
  }

  private List<IType> getTypes( List<? extends IExpression> list )
  {
    if( list == null )
    {
      return Collections.emptyList();
    }
    ArrayList<IType> returnList = new ArrayList<>( list.size() );
    for( int i = 0; i < list.size(); i++ )
    {
      IExpression expression = list.get( i );
      if( !(expression instanceof NullExpression) || (i == list.size()-1 && returnList.isEmpty()) ) // don't include NullExpression's Object type in the presence of non-'null' expressions, 'null' assumes the type of the LUB of the other types
      {
        returnList.add( expression.getType() );
      }
    }
    return returnList;
  }

  private ContextType getInitializableType()
  {
    ContextType typeToInit = getCurrentInitializableContextType();
    if( typeToInit.getType() == null )
    {
      return typeToInit;
    }

    if( typeToInit.getType().isInterface() )
    {
      typeToInit = new ContextType( findImpl( typeToInit.getType() ), typeToInit.isMethodScoring() );
    }

    if( typeToInit.getType() != null &&
            (typeToInit.getType().isArray() ||
                    typeToInit.getType().getTypeInfo().getConstructor() != null) )
    {
      return typeToInit; // An array or collection type with a default constructor
    }

    return ContextType.EMPTY;
  }

  private ContextType getCurrentInitializableContextType()
  {
    ContextType ctxType = getContextType();
    return supportsInitializer( ctxType.getType() ) ? ctxType : ContextType.EMPTY;
  }

  public static IType findImpl( IType typeToInit )
  {
    IType genericType;
    if (typeToInit.isParameterizedType()) {
      genericType = typeToInit.getGenericType();
    } else {
      genericType = typeToInit;
    }

    if( genericType.equals( JavaTypes.LIST() ) ||
            genericType.equals( JavaTypes.COLLECTION() ) ||
            genericType.equals( JavaTypes.ITERABLE() ))
    {
      IJavaType arrayListType = JavaTypes.ARRAY_LIST();
      if( typeToInit.isParameterizedType() )
      {
        arrayListType = (IJavaType)arrayListType.getParameterizedType( typeToInit.getTypeParameters() );
      }
      return arrayListType;
    }
    else if( genericType.equals( JavaTypes.SET() ) )
    {
      IJavaType arrayListType = JavaTypes.HASH_SET();
      if( typeToInit.isParameterizedType() )
      {
        arrayListType = (IJavaType)arrayListType.getParameterizedType( typeToInit.getTypeParameters() );
      }
      return arrayListType;
    }
    else if( genericType.equals( JavaTypes.MAP() ) )
    {
      IJavaType hashMapType = JavaTypes.HASH_MAP();
      if( typeToInit.isParameterizedType() )
      {
        hashMapType = (IJavaType)hashMapType.getParameterizedType( typeToInit.getTypeParameters() );
      }
      return hashMapType;
    }
    else
    {
      return null;
    }
  }

  private boolean supportsInitializer( IType type )
  {
    if( type == null )
    {
      return false;
    }
    return type.isArray() ||
            JavaTypes.MAP().isAssignableFrom( type ) ||
            JavaTypes.SET().isAssignableFrom( type ) ||
            JavaTypes.LIST().isAssignableFrom( type ) ||
            JavaTypes.COLLECTION().equals( type.getGenericType() ) ||
            JavaTypes.ITERABLE().equals( type.getGenericType() ) ||
            type.isDynamic();
  }

  private void parseBlockExpression()
  {
    ISymbolTable symTable = getSymbolTable();
    boolean pushed = false;
    int originaliBreakOk = _iBreakOk;
    _iBreakOk = 0;
    int originaliContinueOk = _iContinueOk;
    _iContinueOk = 0;
    _iReturnOk++;
    try
    {
      ContextType contextType = getContextType();
      IType expectedBlockReturnType = inferReturnTypeForBlockArgument( contextType );
      _blockReturnTypeStack.push( expectedBlockReturnType );
      pushed = true;

      boolean foundArrow;

      BlockExpression block = new BlockExpression();
      StandardScope blockScope = new StandardScope();
      symTable.pushScope( blockScope );
      try
      {
        block.setScope( blockScope );

        //If there are arguments, parse them
        if( !match( null, "->", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          //Infer the parameter types of the block
          List<IType> inferredContextTypes = getContextTypesForBlockArgument( contextType );

          IType type = contextType.getType();
          ArrayList<ISymbol> args = parseParameterDeclarationList( block, false, inferredContextTypes, false, false, false, type != null && type.isDynamic() );
          args.forEach( _symTable::putSymbol );
          foundArrow = verify( block, match( null, "->", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_ARROW_AFTER_BLOCK_ARGS );
          block.setArgs( args );
        }
        else
        {
          foundArrow = true;
          block.setArgs( Collections.<ISymbol>emptyList() );
        }

        verify( block, block.getArgs().size() <= IBlock.MAX_ARGS, Res.MSG_BLOCKS_CAN_HAVE_A_MOST_SIXTEEN_ARGS );

        pushCurrentBlock( block );
        try
        {
          //parse the expression body
          if(foundArrow) {
            int iOffset = _tokenizer.getTokenStart();
            int iLineNum = _tokenizer.getLineNumber();
            int iColumn = getTokenizer().getTokenColumn();
            if( match( null, '{' ) )
            {
              _ctxInferenceMgr.pushLoopCompromised();
              try
              {
                parseStatementBlock();
              }
              finally
              {
                _ctxInferenceMgr.popLoopCompromised();
              }
              if( peekStatement() instanceof StatementList )
              {
                setLocation( iOffset, iLineNum, iColumn );
              }
              Statement blockBody = popStatement();

              if( expectedBlockReturnType != null && expectedBlockReturnType != GosuParserTypes.NULL_TYPE() )
              {
                boolean[] bAbsolute = {false};
                ITerminalStatement term = blockBody.getLeastSignificantTerminalStatement( bAbsolute );
                Statement verifyStmt;
                if (blockBody instanceof StatementList &&
                        ((StatementList) blockBody).getStatements() != null &&
                        ((StatementList) blockBody).getStatements().length > 0)
                {
                  StatementList lst = (StatementList)blockBody;
                  verifyStmt = lst.getStatements()[lst.getStatements().length - 1];
                }
                else
                {
                  verifyStmt = blockBody;
                }
                verify( verifyStmt, term != null && (bAbsolute[0] || term.getTerminalType() == TerminalType.ForeverLoop), Res.MSG_MISSING_RETURN );
              }

              block.setBody( blockBody );
            }
            else
            {
              int tokenizerPostion = getTokenizer().getTokenStart();
              parseExpression( expectedBlockReturnType == null ? ContextType.EMPTY : new ContextType( expectedBlockReturnType, false ) );
              Expression exprBody = popExpression();

              // void functions can work in the body of a block
              //noinspection ThrowableResultOfMethodCallIgnored
              exprBody.removeParseException( Res.MSG_VOID_EXPRESSION_NOT_ALLOWED );

              //If someone is trying to do a naked assignment, parse it and give a good error message
              if( matchAssignmentOperator() != null )
              {
                parseExpression();
                Expression assignmentBody = popExpression();
                verify( assignmentBody, false, Res.MSG_ASSIGNMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS );
              }
              else if( tokenizerPostion == getTokenizer().getTokenStart() )
              {
                //If someone is trying to do a naked return, parse the expression and give a good error message
                int mark = getTokenizer().mark();
                if( match( null, Keyword.KW_return ) )
                {
                  String strToken = getTokenizer().getTokenAt( mark ).getStringValue();
                  parseExpression();
                  Expression returnBody = popExpression();
                  addError( returnBody, Res.MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS, strToken );
                }
                else if( match( null, Keyword.KW_var ) ||
                        match( null, Keyword.KW_switch ) ||
                        match( null, Keyword.KW_if ) )
                {
                  String strToken = getTokenizer().getTokenAt( mark ).getStringValue();
                  addError( exprBody, Res.MSG_STATEMENTS_MUST_BE_ENCLOSED_IN_CURLIES_IN_BLOCKS, strToken );
                }
              }

              // expressions with return values are acceptable when a block is going to discard the value
              if( JavaTypes.pVOID().equals( expectedBlockReturnType ) )
              {
                //noinspection ThrowableResultOfMethodCallIgnored
                exprBody.removeParseException( Res.MSG_TYPE_MISMATCH );
              }

              block.setBody( exprBody );
            }
          }

          block.setBlockReturnType( getBlockReturnType( block.getBody(), expectedBlockReturnType ) );
          block.setScope( null );
          block.updateGosuClass();
          pushExpression( block );
        }
        finally
        {
          popCurrentBlock();
        }
      }
      finally
      {
        symTable.popScope();
      }
    }
    finally
    {
      _iBreakOk = originaliBreakOk;
      _iContinueOk = originaliContinueOk;
      _iReturnOk--;
      if( pushed ) {
        _blockReturnTypeStack.pop();
      }
    }
  }

  private IType getBlockReturnType( IParsedElement blockBody, IType ctxType )
  {
    if(blockBody == null) {
      return ErrorType.getInstance();
    }
    if( blockBody instanceof Expression )
    {
      return ((Expression)blockBody).getType();
    }
    else
    {
      Statement stmt = (Statement) blockBody;
      ArrayList<ReturnStatement> returnStatements = new ArrayList<ReturnStatement>();
      ArrayList<IType> returnTypes = new ArrayList<IType>();
      //noinspection unchecked
      stmt.getContainedParsedElementsByTypesWithIgnoreSet( (List) returnStatements,
              new HashSet( Arrays.asList( BlockExpression.class ) ),
              ReturnStatement.class );
      // The statement body must contain only throw statements, so
      // the return type should just be whatever the context type is
      if( returnStatements.size() == 0 )
      {
        return ctxType;
      }
      else
      {
        for( ReturnStatement returnStmt : returnStatements )
        {
          returnTypes.add( returnStmt.getValue().getType() );
        }
        return TypeLord.findLeastUpperBound( returnTypes );
      }
    }
  }


  private List<IType> getContextTypesForBlockArgument( ContextType ctxType )
  {
    if( ctxType == null )
    {
      return null;
    }

    IType type = ctxType.getType();
    if( type == null )
    {
      return null;
    }

    if( type instanceof FunctionType )
    {
      if( ctxType.getAlternateType() instanceof FunctionType )
      {
        // Alternate type includes type vars so that untyped parameters in the block can potentially be inferred *after* the block expression parses
        type = ctxType.getAlternateType();
      }
      return Arrays.asList( ((FunctionType)type).getParameterTypes() );
    }
    else
    {
      IFunctionType functionType = FunctionToInterfaceCoercer.getRepresentativeFunctionType( type );
      if( functionType != null )
      {
        ArrayList<IType> paramTypes = new ArrayList<IType>();
        for( IType parameterType : functionType.getParameterTypes() )
        {
          paramTypes.add( TypeLord.getDefaultParameterizedType( parameterType ) );
        }
        return paramTypes;
      }
    }

    return null;
  }

  private IType inferReturnTypeForBlockArgument( ContextType contextType )
  {
    if( contextType.isMethodScoring() )
    {
      return null;
    }

    IType ctxType = contextType.getType();
    if( ctxType == null )
    {
      return null;
    }

    IType returnType = null;
    if( ctxType instanceof FunctionType )
    {
      if( contextType.getAlternateType() instanceof FunctionType )
      {
        // Alternate type includes type vars so that untyped parameters in the block can potentially be inferred *after* the block expression parses
        ctxType = contextType.getAlternateType();
      }
      returnType = ((FunctionType)ctxType).getReturnType();
    }

    IFunctionType functionType = FunctionToInterfaceCoercer.getRepresentativeFunctionType( ctxType );
    if( functionType != null )
    {
      IType iType = functionType.getReturnType();
      if( returnType == null )
      {
        returnType = iType;
      }
      else if( !returnType.equals( iType ) )
      {
        return null;
      }
    }

    // If we are currently infering the return type, use the bounding type to parse
    // on the way in so that we get the actual type on the way out to infer with
    returnType = TypeLord.boundTypes( returnType, getCurrentlyInferringFunctionTypeVars() );

    return returnType;
  }

  /**
   * <i>new-expression</i>
   * <b>new</b> &lt;type-expression&gt; <b>(</b> [&lt;argument-list&gt;] <b>)</b> <b>{</b> [&lt;initialization-expression&gt;] <b>}</b>
   * <b>new</b> &lt;type-expression&gt; <b>[</b> &lt;expression&gt; <b>]</b>
   * <b>new</b> &lt;type-expression&gt; <b>[</b><b>]</b> <b>{</b> [&lt;array-value-list&gt;] <b>}</b>
   */
  void parseNewExpression()
  {
    parseNewExpressionOrAnnotation( false );
  }

  void parseNewExpressionOrAnnotation( boolean bAnnotation )
  {
    _parseNewExpressionOrAnnotation( bAnnotation, false );
  }
  void _parseNewExpressionOrAnnotation( boolean bAnnotation, boolean bBacktracking )
  {
    boolean original = isParsingAnnotation();
    setParsingAnnotation( bAnnotation );
    try
    {
      int mark = getTokenizer().mark();
      int iLocationsCount = _locations.size();
      TypeLiteral typeLiteral = null;
      if( match( null, null, '(', true ) && isParenthesisTerminalExpression( true ) )
      {
        // handle typeless 'new()' syntax
        typeLiteral = maybeInferTypeLiteralFromContextType();
      }
      if( typeLiteral == null )
      {
        parseTypeLiteralIgnoreArrayBrackets();
        typeLiteral = (TypeLiteral)popExpression();
        IType type = typeLiteral.getType().getType();
        if( !bBacktracking )
        {
          if( type.isParameterizedType() && TypeLord.deriveParameterizedTypeFromContext( type.getGenericType(), null ) == type )
          {
            // Try to infer the constructed type from args rather than assume the default type
            typeLiteral.setType( MetaType.getLiteral( type.getGenericType() ) );
          }
        }
        else if( type == TypeLord.getPureGenericType( type ) )
        {
          // Ensure we never construct a raw generic type
          typeLiteral.setType( MetaType.getLiteral( TypeLord.deriveParameterizedTypeFromContext( type, null ) ) );
        }
      }
      verify( typeLiteral, !(typeLiteral instanceof BlockLiteral), Res.MSG_BLOCKS_LITERAL_NOT_ALLOWED_IN_NEW_EXPR );
      IType declaringClass = typeLiteral.getType().getType();
      if( isParsingStaticFeature() )
      {
        IType type = typeLiteral.getType().getType();
        while( type.getEnclosingType() != null )
        {
          if( type instanceof IGosuClass && verify( typeLiteral, ((IGosuClass)type).isStatic(), Res.MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE ) )
          {
            type = type.getEnclosingType();
          }
          else
          {
            break;
          }
        }
      }
      verify( typeLiteral, !declaringClass.isEnum() || getTokenizer().getCurrentToken().getType() == '[', Res.MSG_ENUM_CONSTRUCTOR_NOT_ACCESSIBLE );
      parseNewExpressionOrAnnotation( declaringClass, bAnnotation, false, typeLiteral, mark );
      if( !bBacktracking && typeLiteral.getType().getType().isGenericType() && !typeLiteral.getType().getType().isParameterizedType() )
      {
        // We didn't infer type parameters from the ctor's parameter types using the raw generic type, backtrack and just use the default generic type
        backtrack( mark, iLocationsCount );
        _parseNewExpressionOrAnnotation( bAnnotation, true );
      }
    }
    finally
    {
      setParsingAnnotation( original );
    }
  }

  private TypeLiteral maybeInferTypeLiteralFromContextType()
  {
    TypeLiteral typeLiteral = null;
    IType ctxType = getContextType().getType();
    if( ctxType != null && !getContextType().isMethodScoring() )
    {
      typeLiteral = new InferredTypeLiteral( ctxType );
      pushExpression( typeLiteral );
      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();
      setLocation( iOffset, iLineNum, iColumn, true );
      popExpression();
    }
    return typeLiteral;
  }

  void parseNewExpressionOrAnnotation( IType declaringClass, boolean bAnnotation, boolean bNoArgNoParenthesis, final TypeLiteral typeLiteral, int mark )
  {
    int iParenStart = _tokenizer.getTokenStart();
    NewExpression e = bAnnotation ? new AnnotationExpression() : new NewExpression();
    e.setType( declaringClass );
    e.setTypeLiteral(typeLiteral);
    verifyCanConstructInnerClassFromCallSite( e, declaringClass );
    if( bNoArgNoParenthesis || match( null, '(' ) )
    {
      boolean bAssumeClosingParenMatch = true;
      e.setArgPosition( iParenStart + 1 );

      List<IConstructorType> listConstructorTypes = (!declaringClass.isInterface() || isAnnotation( declaringClass ))
              ? getPreliminaryConstructorTypes( declaringClass, e )
              : Collections.<IConstructorType>emptyList();

      scrubAnnotationConstructors(declaringClass, listConstructorTypes);

      boolean bNoArgsProvided = false;
      if( !bNoArgNoParenthesis &&
              (!(bNoArgsProvided = match( null, null, ')', true )) ||
                      listConstructorTypes.size() > 0 && listConstructorTypes.get( 0 ).hasOptionalParams()) )
      {
        MethodScore bestConst = parseArgumentList2( e, listConstructorTypes, null, !(declaringClass instanceof ErrorType), bNoArgsProvided );

        IConstructorType constructorType = null;
        if( bestConst.isValid() )
        {
          declaringClass = maybeChangeToInferredType( declaringClass, typeLiteral, bestConst );
          constructorType = (IConstructorType)(bestConst.getInferredFunctionType() == null ? bestConst.getRawFunctionType() : bestConst.getInferredFunctionType());
          List<IExpression> args = bestConst.getArguments();
          verifyArgCount( e, args.size(), constructorType );
          //noinspection SuspiciousToArrayCall
          e.setArgs( args.toArray( new Expression[args.size()] ) );
          e.setType( declaringClass );
          e.setConstructor( constructorType.getConstructor() );
          IType[] argTypes = constructorType.getParameterTypes();
          e.setArgTypes( argTypes );
          e.setNamedArgOrder( bestConst.getNamedArgOrder() );
        }
        else
        {
          verify( e, false, Res.MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, declaringClass.getName() );
          e.setType( ErrorType.getInstance() );
        }
        bAssumeClosingParenMatch = verify( e, match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
        boolean bAnonymous = !isInitializableType( e.getType() ) && match( null, null, '{', true );
        verifyConstructorIsAccessible( declaringClass, e, constructorType, bAnonymous );
      }
      else
      {
        if( bNoArgsProvided )
        {
          match( null, ')' );
        }

        try
        {
          IConstructorType constructorType = getConstructorType( declaringClass, new Expression[0], null, this );
          e.setType( declaringClass );
          e.setConstructor( constructorType.getConstructor() );

          boolean bAnonymous = !isInitializableType( e.getType() ) && match( null, null, '{', true );
          verifyConstructorIsAccessible( declaringClass, e, constructorType, bAnonymous );
        }
        catch( ParseException pe )
        {
          boolean possibleAnonymousClassDecl = declaringClass.isInterface() && !isInitializableType( declaringClass );
          boolean possibleDataStructDecl = isConcreteInitializableType( declaringClass );
          if( (possibleAnonymousClassDecl || possibleDataStructDecl) && match( null, null, '{', true ) )
          {
            // Assume we are constructing an anonymous class on an interface or a data structure
          }
          else
          {
            e.setType( declaringClass );
            IConstructorInfo firstCtor = getConstructor( declaringClass );
            e.setConstructor( firstCtor );
            e.addParseException( pe );
          }
        }
      }

      if( match( null, null, '{', true ) )
      {
        if( isInitializableType( e.getType() ) )
        {
          Token startToken = getTokenizer().getCurrentToken();
          match( null, '{' );
          if( !match( null, '}' ) )
          {
            _parseInitializerExpression( new ContextType( e.getType() ) );
            IInitializerExpression initializerExpression = (IInitializerExpression)peekExpression();
            e.setInitializer( initializerExpression );
            verify( e, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER );
            setLocation( startToken.getTokenStart(), startToken.getLine(), startToken.getTokenColumn() );
            popExpression();
          }
        }
        else if( !(declaringClass instanceof ErrorType) )
        {
          int state = _tokenizer.mark();
          //look ahead 2 to see if this is an object initializer
          if( !declaringClass.isAbstract() && !declaringClass.isInterface() &&
                  match( null, '{' ) && match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
          {
            _tokenizer.restoreToMark( state );
            parseObjectInitializer( typeLiteral.evaluate() );
            ObjectInitializerExpression expression = (ObjectInitializerExpression)popExpression();
            e.setInitializer( expression );
          }
          else if( bAssumeClosingParenMatch )
          {
            // Assume we are constructing an anonymous class on a gosu or java type.
            declaringClass = parseAnonymousInnerClass( declaringClass, typeLiteral, e, state, mark );
          }
        }
      }

      if( e.getConstructor() != null )
      {
        // Prevent abstract types from being constructed (annotation interfaces are ok though)
        IType ownersType = e.getConstructor().getOwnersType();
        boolean bAnnotationType = ownersType instanceof ICanBeAnnotation && ((ICanBeAnnotation)ownersType).isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( ownersType );
        verify( e, bAnnotationType || !ownersType.isAbstract(), Res.MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS, declaringClass.getName() );
        // Prevent recursive types from being constructed directly
        warn( e, declaringClass instanceof ITypeVariableType || !TypeLord.isRecursiveType( declaringClass ), Res.MSG_CANNOT_CONSTRUCT_RECURSIVE_CLASS, declaringClass.getName() );
      }

      pushExpression( e );
    }
    else if( !bAnnotation )
    {
      if( verify( e, match( null, '[' ), Res.MSG_EXPECTING_NEW_ARRAY_OR_CTOR ) )
      {
        if( match( null, ']' ) )
        {
          int numArrays = 1;
          while( match( null, '[' ) )
          {
            verify( e, match( null, ']' ), Res.MSG_EXPECTING_ARRAY_BRACKET );
            ++numArrays;
          }
          for( int i = 0; i < numArrays; i++ )
          {
            declaringClass = declaringClass.getArrayType();
          }

          verify( e, match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_NEW_ARRAY );
          e.setType( declaringClass );
          if( match( null, '}' ) )
          {
            e.setValueExpressions( null );
          }
          else
          {
            List valueExpressions = parseArrayValueList( declaringClass.getComponentType() );
            verify( e, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_NEW_ARRAY );
            //noinspection unchecked
            e.setValueExpressions( valueExpressions );
          }
        }
        else
        {
          parseExpression( ContextType.pINT_FALSE );
          e.addSizeExpression( popExpression() );
          IType arrayType = null;
          boolean bSizelessDimension = false;
          while( verify( e, match( null, ']' ), Res.MSG_EXPECTING_ARRAY_BRACKET ) )
          {
            declaringClass = declaringClass.getArrayType();
            arrayType = declaringClass;

            if( match( null, '[' ) )
            {
              if( !bSizelessDimension && !match( null, null, ']', true ) )
              {
                parseExpression( ContextType.pINT_FALSE );
                e.addSizeExpression( popExpression() );
              }
              else
              {
                bSizelessDimension = true;
              }
            }
            else
            {
              break;
            }
          }
          if( arrayType != null )
          {
            declaringClass = arrayType;
          }
          else
          {
            declaringClass = ErrorType.getInstance();
          }
          e.setType( declaringClass );
        }
      }
      pushExpression( e );
    }
    else
    {
      if( !(declaringClass instanceof ErrorType) )
      {
        ITypeInfo typeInfo = declaringClass.getTypeInfo();
        if( typeInfo != null )
        {
          IConstructorInfo iConstructorInfo = typeInfo.getConstructor();
          if (iConstructorInfo == null) {
            outer:
            for (IConstructorInfo constructorInfo : typeInfo.getConstructors()) {
              if (constructorInfo instanceof IOptionalParamCapable) {
                IExpression[] defaultVals = ((IOptionalParamCapable) constructorInfo).getDefaultValueExpressions();
                for (IExpression defaultVal : defaultVals) {
                  if (defaultVal == null) {
                    continue outer;
                  }
                }
                iConstructorInfo = constructorInfo;
                break;
              }
            }
          }
          if( verify( e, iConstructorInfo != null, Res.MSG_NO_DEFAULT_CTOR_IN, declaringClass.getName() ) )
          {
            e.setType( declaringClass );
            e.setConstructor( iConstructorInfo );
            e.setArgTypes();
            e.setType( declaringClass );
          }
          else
          {
            e.setType( ErrorType.getInstance() );
          }
        }
        else
        {
          e.setType( ErrorType.getInstance() );
        }
      }
      else
      {
        e.setType( ErrorType.getInstance() );
      }

      pushExpression( e );
    }
    IConstructorInfo constructor = e.getConstructor();
    if( (constructor != null) && (constructor.isDeprecated()) )
    {
      IParserState state;
      if( typeLiteral == null )
      {
        state = null;
      }
      else
      {
        state = new IParserState()
        {
          public int getLineNumber()
          {
            return typeLiteral.getLocation().getLineNum();
          }

          public int getTokenColumn()
          {
            return typeLiteral.getLocation().getColumn();
          }

          public String getSource()
          {
            return getTokenizer().getSource();
          }

          public int getTokenStart()
          {
            return typeLiteral.getLocation().getOffset();
          }

          public int getTokenEnd()
          {
            return typeLiteral.getLocation().getExtent();
          }

          public int getLineOffset()
          {
            return getTokenizer().getLineOffset();
          }
          public IParserState cloneWithNewTokenStartAndTokenEnd( int newTokenStart, int newLength )
          {
            return null;
          }
        };
      }
      //noinspection ThrowableInstanceNeverThrown
      e.addParseWarning( new ParseWarningForDeprecatedMember( state,
              TypeInfoUtil.getConstructorSignature( constructor ),
              constructor.getContainer().getName() ) );
    }
  }

  private IType maybeChangeToInferredType( IType declaringClass, TypeLiteral typeLiteral, MethodScore bestConst )
  {
    ConstructorType inferredFunctionType = (ConstructorType)bestConst.getInferredFunctionType();
    if( inferredFunctionType != null &&
        !(declaringClass instanceof ITypeVariableType) &&
        declaringClass != inferredFunctionType.getDeclaringType() )
    {
      declaringClass = inferredFunctionType.getDeclaringType();
      typeLiteral.setType( declaringClass );
    }
    return declaringClass;
  }

  //## todo: remove this after removal of old-style Gosu "annotations" when there will only be a single ctor
  private void scrubAnnotationConstructors(IType declaringClass, List<IConstructorType> listConstructorTypes) {
    if( declaringClass instanceof IGosuClass ) {
      // We only include one ctor for an annotation implemented in Gosu, so let it be called with or without named args
      return;
    }
    if (JavaTypes.ANNOTATION().isAssignableFrom(declaringClass)) {
      if (match(null, ":", ISourceCodeTokenizer.TT_OPERATOR, true)) {

        // Arg[s] are named, so only include the one ctor with default params (this is the new/conventional way)

        Iterator<IConstructorType> iter = listConstructorTypes.iterator();
        while (iter.hasNext()) {
          IConstructorType next = iter.next();
          IConstructorInfo constructor = next.getConstructor();
          if ( !(constructor instanceof ConstructorInfoBuilder.IBuilt &&
                 ((ConstructorInfoBuilder.IBuilt)constructor).getUserData() == AnnotationConstructorGenerator.STANDARD_CTOR_WITH_DEFAULT_PARAM_VALUES) ) {
            // Remove all but the one standard ctor
            iter.remove();
          }
        }
      } else {

        // Arg[s] are not named, only include "legacy" constructors to support old-style ordered arg passing (this is effectively deprecated)

        Iterator<IConstructorType> iter = listConstructorTypes.iterator();
        while (iter.hasNext()) {
          IConstructorType next = iter.next();
          IConstructorInfo constructor = next.getConstructor();
          if ( constructor instanceof ConstructorInfoBuilder.IBuilt &&
               ((ConstructorInfoBuilder.IBuilt)constructor).getUserData() == AnnotationConstructorGenerator.STANDARD_CTOR_WITH_DEFAULT_PARAM_VALUES ) {
            // Remove the standard ctor
            iter.remove();
            break;
          }
        }
      }
    }
  }

  private boolean isAnnotation( IType type )
  {
    return JavaTypes.ANNOTATION().isAssignableFrom( type ) ||
            JavaTypes.IANNOTATION().isAssignableFrom( type );
  }

  private ArrayList<IConstructorType> getPreliminaryConstructorTypes( IType declaringClass, NewExpression e )
  {
    // Get a preliminary constructorTypes to check arguments. Note we do this to aid in error feedback and value popup completion.
    ArrayList<IConstructorType> listConstructorTypes = new ArrayList<IConstructorType>( 2 );
    try
    {
      getConstructorType( declaringClass, null, listConstructorTypes, this );
    }
    catch( ParseException pe )
    {
      IConstructorInfo firstCtor = getConstructor( declaringClass );
      e.setConstructor( firstCtor );
      e.addParseException( pe );
    }
    IType[][] argTypesArray = new IType[listConstructorTypes.size()][];
    IParameterInfo[][] paramTypesPossible = new IParameterInfo[listConstructorTypes.size()][];
    for( int i = 0; i < argTypesArray.length; i++ )
    {
      IConstructorType ctorType = listConstructorTypes.get( i );
      paramTypesPossible[i] = ctorType.getConstructor().getParameters();
      argTypesArray[i] = ctorType.getParameterTypes();
    }
    return listConstructorTypes;
  }

  private IType parseAnonymousInnerClass( IType declaringClass, TypeLiteral typeLiteral, NewExpression newExpr, int state, int mark )
  {
    _tokenizer.restoreToMark( state );
    newExpr.setAnonymousClass(true);
    IGosuClassInternal gsDeclaringClass = null;
    if( declaringClass instanceof IJavaType )
    {
      gsDeclaringClass = GosuClassProxyFactory.instance().create( declaringClass );
      if( declaringClass.isParameterizedType() )
      {
        gsDeclaringClass = (IGosuClassInternal)gsDeclaringClass.getParameterizedType( declaringClass.getTypeParameters() );
      }
    }
    else if( declaringClass instanceof IGosuClassInternal )
    {
      gsDeclaringClass = (IGosuClassInternal)declaringClass;
    }
    else
    {
      verify( newExpr, false, Res.MSG_BAD_ANONYMOUS_CLASS_DECLARATION );
    }

    if( gsDeclaringClass != null && typeLiteral != null )
    {
      declaringClass = TypeLord.makeDefaultParameterizedType( declaringClass );

      ICompilableTypeInternal enclosingType = getCurrentEnclosingGosuClass();

      // For now anonymous classes must be enclosed in a gsclass.
      if( verify( newExpr, enclosingType instanceof IGosuClassInternal, Res.MSG_ANONYMOUS_CLASS_NOT_ALLOWED_HERE ) )
      {
        int iNameOffset = typeLiteral.getLocation().getOffset();
        int originaliBreakOk = _iBreakOk;
        _iBreakOk = 0;
        int originaliContinueOk = _iContinueOk;
        _iContinueOk = 0;
        try
        {
          _parseAnonymousInnerClass( declaringClass, gsDeclaringClass, enclosingType, iNameOffset, newExpr, mark );
        }
        finally
        {
          _iBreakOk = originaliBreakOk;
          _iContinueOk = originaliContinueOk;
        }
      }
    }
    return declaringClass;
  }

  private void _parseAnonymousInnerClass( IType declaringClass, IGosuClassInternal gsDeclaringClass, ICompilableTypeInternal enclosingType, int iNameOffset, NewExpression newExpr, int mark )
  {
    //##todo:
    // Note we do NOT case where we are parsing a field initializer since
    // the anonymous class should have access to all the class' fields. E.g.,
    //
    // class Foo {
    //   static var Field1 = new Whatever() {
    //      override function bar() : String {
    //        return Field2
    //      }
    //   }
    //   static var Field2 : String = "hello"
    // }
    //

    String strAnonymousClass = IGosuClassInternal.ANONYMOUS_PREFIX + "_" + enclosingType.getAnonymousInnerClassCount();
    boolean bTestClass = enclosingType instanceof IGosuClassInternal && ((IGosuClassInternal)enclosingType).isTestClass();
    InnerClassFileSystemSourceFileHandle innerSfh = new InnerClassFileSystemSourceFileHandle(ClassType.Class, enclosingType.getName(), strAnonymousClass, bTestClass );
    innerSfh.setOffset( iNameOffset );
    innerSfh.setMark( mark );
    IGosuClassInternal innerGsClass = (IGosuClassInternal)enclosingType.getTypeLoader().makeNewClass( innerSfh );
    ((IGosuClassInternal)enclosingType).addInnerClass(innerGsClass);
    if( declaringClass != null )
    {
      if( declaringClass.isInterface() )
      {
        innerGsClass.addInterface( TypeLord.makeDefaultParameterizedType( declaringClass ) );
      }
      else
      {
        innerGsClass.setSuperType( TypeLord.makeDefaultParameterizedType( declaringClass ) );
        if( declaringClass.isEnum() )
        {
          innerGsClass.setEnum();
        }
      }
    }
    innerGsClass.setEnclosingType( enclosingType );
    innerGsClass.setNamespace( enclosingType.getNamespace() );
    innerGsClass.createNewParseInfo();


    if( isParsingStaticFeature() )
    {
      innerGsClass.markStatic();
    }

    if( declaringClass.isInterface() )
    {
      innerGsClass.addInterface( declaringClass );
      newExpr.setType( innerGsClass );
    }
    else
    {
      innerGsClass.setSuperType( declaringClass );
      if( declaringClass.isEnum() )
      {
        innerGsClass.setEnum();
      }
      newExpr.setType( innerGsClass );
    }

    if( newExpr.getConstructor() != null )
    {
      GosuConstructorInfo ci = getGsConstructorInfo( newExpr.getConstructor(), gsDeclaringClass );
      verify( newExpr, innerGsClass.getParseInfo().addAnonymousConstructor( _symTable, ci ), Res.MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, gsDeclaringClass.getName() );
    }

    innerGsClass.setCannotCaptureSymbols( isParsingBlock() ||
            ((IGosuClassInternal)innerGsClass.getEnclosingType()).isCannotCaptureSymbols() );
    GosuClassParser.parseAnonymousInnerClass( this, innerGsClass );

    if( newExpr.getConstructor() != null && !innerGsClass.getTypeInfo().getConstructors().isEmpty() )
    {
      IConstructorInfo innerCtor = innerGsClass.getTypeInfo().getConstructors().get( 0 );
      newExpr.setConstructor( innerCtor );
    }

    if( !getContextType().isMethodScoring() )
    {
      popStatement();
    }

    if( declaringClass.isInterface() )
    {
      //noinspection unchecked
      List<IConstructorInfo> ctors = (List<IConstructorInfo>)innerGsClass.getTypeInfo().getConstructors();
      if( ctors.size() > 0 )
      {
        newExpr.setConstructor( ctors.get( 0 ) );
      }
    }
  }

  public boolean isParsingStaticFeature()
  {
    return !_parsingStaticFeature.isEmpty() && _parsingStaticFeature.peek();
  }
  public void pushParsingStaticMember( Boolean bParsingStaticFeature )
  {
    _parsingStaticFeature.push( bParsingStaticFeature );
  }
  public void popParsingStaticMember()
  {
    _parsingStaticFeature.pop();
  }

  public boolean isParsingAbstractConstructor()
  {
    return !_parsingAbstractConstructor.isEmpty() && _parsingAbstractConstructor.peek();
  }
  public void pushParsingAbstractConstructor( Boolean bParsingAbstractConstructor )
  {
    _parsingAbstractConstructor.push( bParsingAbstractConstructor );
  }
  public void popParsingAbstractConstructor()
  {
    _parsingAbstractConstructor.pop();
  }

  private IConstructorInfo getConstructor( IType instanceClass )
  {
    IConstructorInfo firstCtor = null;
    if( instanceClass.getTypeInfo() instanceof IRelativeTypeInfo)
    {
      if( !instanceClass.isInterface() )
      {
        List<? extends IConstructorInfo> ctors = ( (IRelativeTypeInfo) instanceClass.getTypeInfo() ).getConstructors( instanceClass );
        if ( ! ctors.isEmpty() ) {
          firstCtor = ctors.get( 0 );
        }
      }
    }
    else
    {
      if( !instanceClass.isInterface() )
      {
        ITypeInfo typeInfo = instanceClass.getTypeInfo();
        if (typeInfo == null) {
          firstCtor = null;
        } else {
          List<? extends IConstructorInfo> ctors = typeInfo.getConstructors();
          if ( ! ctors.isEmpty() ) {
            firstCtor = ctors.get( 0 );
          }
        }
      }
    }
    return firstCtor;
  }

  private void verifyConstructorIsAccessible( IType instanceClass, NewExpression e, IConstructorType constructorType, boolean bAnonymous )
  {
    if( e.getType() instanceof ErrorType )
    {
      return;
    }

    if( constructorType != null && !(constructorType.getConstructor() instanceof DynamicConstructorInfo) )
    {
      ITypeInfo typeInfo = instanceClass.getTypeInfo();
      List<? extends IConstructorInfo> accessibleCtors = getGosuClass() != null && typeInfo instanceof IRelativeTypeInfo
              ? ((IRelativeTypeInfo)typeInfo).getConstructors( getGosuClass() )
              : typeInfo.getConstructors();
      //noinspection SuspiciousMethodCalls
      verify( e,
              accessibleCtors.contains( constructorType.getConstructor() ) ||
                      (bAnonymous && constructorType.getConstructor().isProtected()),
              Res.MSG_CTOR_HAS_XXX_ACCESS, getModifierString( constructorType ) );
    }
  }

  private String getModifierString( IConstructorType constructorType )
  {
    return constructorType.getConstructor().isPrivate()
            ? "private"
            : constructorType.getConstructor().isInternal()
            ? "internal"
            : constructorType.getConstructor().isProtected()
            ? "protected"
            : "public";
  }

  private boolean parseObjectInitializer( IType objectType )
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    boolean b = _parseObjectInitializer( objectType );
    setLocation( iOffset, iLineNum, iColumn );
    return b;
  }

  private boolean _parseObjectInitializer( IType objectType )
  {
    if( match( null, '{' ) )
    {
      _doParseObjectInitializer(objectType);
      verify( peekExpression(), match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_INITIALIZER );
      return true;
    }
    else
    {
      return false;
    }
  }

  private void _doParseObjectInitializer(IType objectType) {
    ObjectInitializerExpression oie = new ObjectInitializerExpression();
    do
    {
      if( parseInitializerAssignment( objectType ) )
      {
        InitializerAssignment newAssignment = (InitializerAssignment)popStatement();
        for( InitializerAssignment existing : oie.getInitializers() )
        {
          if( existing.getPropertyName() != null && existing.getPropertyName().equals( newAssignment.getPropertyName() ) )
          {
            newAssignment.addParseException( Res.MSG_REDUNTANT_INITIALIZERS, existing.getPropertyName() );
          }
        }
        oie.add( newAssignment );
      }
      else
      {
        verify( oie, false, Res.MSG_EXPECTING_NAME_VALUE_PAIR, makeLightweightParserState() );
      }
    } while( match( null, ',' ) );

    oie.setType( objectType );
    pushExpression(oie);
  }

  private boolean parseInitializerAssignment( IType objectType )
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = _tokenizer.getTokenColumn();
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      //handle two colons in a row with error message to preserve parse tree
      //(helps intellisense)
      boolean bFoundExtraColon = false;
      if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        bFoundExtraColon = true;
      }
      parseInitializerIdentifier( objectType );
      Identifier identifier = (Identifier)popExpression();
      InitializerAssignment se = new InitializerAssignment( objectType, identifier.getSymbol().getName() );
      IPropertyInfo propertyInfo = null;
      try
      {
        if( se.getPropertyName() != null )
        {
          propertyInfo = BeanAccess.getPropertyInfo( objectType, se.getPropertyName(), null, this, getVisibilityConstraint() );
        }
      }
      catch( ParseException e )
      {
        se.addParseException( e );
      }
      IType type = ErrorType.getInstance();
      if( propertyInfo != null )
      {
        verifyCase( se, se.getPropertyName(), propertyInfo.getName(), Res.MSG_PROPERTY_CASE_MISMATCH, false );

        if( !JavaTypes.COLLECTION().isAssignableFrom( propertyInfo.getFeatureType() ) &&
                !JavaTypes.MAP().isAssignableFrom( propertyInfo.getFeatureType() ) )
        {
          try
          {
            verifyPropertyWritable( objectType, se.getPropertyName(), true );
          }
          catch( Exception ex )
          {
            //noinspection ThrowableResultOfMethodCallIgnored
            se.addParseException( ParseException.wrap( ex, makeFullParserState() ) );
          }
        }
        type = propertyInfo.getFeatureType();
      }
      identifier.setType( type );

      verify( se, match( null, "=", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EQUALS_FOR_INITIALIZER_EXPR );

      verify( se, !bFoundExtraColon, Res.MSG_ONLY_ONE_COLON_IN_INITIALIZERS );

      parseExpression( new ContextType( type ) );
      Expression value = popExpression();
      if( value.hasParseExceptions() )
      {
        value.getParseExceptions().get( 0 ).setExpectedType( type );
      }
      se.setRhs( value );

      pushStatement( se );
      setLocation( iOffset, iLineNum, iColumn );
      return true;
    }
    return false;
  }

  private void parseInitializerIdentifier( IType objectType )
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = _tokenizer.getTokenColumn();
    Identifier i = new Identifier();
    String strToken = null;
    int mark = getTokenizer().mark();
    if( verify( i, match( null, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_PROPERTY ) )
    {
      strToken = getTokenizer().getTokenAt( mark ).getStringValue();
    }
    i.setSymbol( new InitializerSymbol( strToken, objectType ), null);
    i.setType( objectType );
    pushExpression( i );
    setLocation( iOffset, iLineNum, iColumn );
  }

  private GosuConstructorInfo getGsConstructorInfo( IConstructorInfo ci, IGosuClassInternal gsInstanceClass )
  {
    if( ci instanceof GosuConstructorInfo )
    {
      return (GosuConstructorInfo)ci;
    }

    List<IType> argTypes = new ArrayList<IType>( 2 );
    if (ci != null) {
      for( IParameterInfo pi : ci.getParameters() )
      {
        argTypes.add( pi.getFeatureType() );
      }
    }
    return (GosuConstructorInfo) gsInstanceClass.getTypeInfo().getConstructor( gsInstanceClass, argTypes.toArray(new IType[argTypes.size()]));
  }

  private boolean isConcreteInitializableType( IType type )
  {
    return isInitializableType( type ) && !type.isAbstract() && !type.isInterface();
  }

  private boolean isInitializableType( IType type )
  {
    return type != null &&
            (JavaTypes.COLLECTION().isAssignableFrom( type ) ||
                    JavaTypes.MAP().isAssignableFrom( type ));
  }

  private void verifyCanConstructInnerClassFromCallSite( NewExpression e, IType innerClass )
  {
    if( !(innerClass instanceof IGosuClassInternal) && !(innerClass instanceof IJavaType) )
    {
      return;
    }

    if( Modifier.isStatic( innerClass.getModifiers() ) )
    {
      return;
    }

    IType innersEnclosingClass = innerClass.getEnclosingType();
    if( innersEnclosingClass == null )
    {
      // Not an inner class
      return;
    }

    IGosuClassInternal constructingFromClass = getScriptPart() != null && getScriptPart().getContainingType() instanceof IGosuClass
                                         ? (IGosuClassInternal)getScriptPart().getContainingType()
                                         : null;
    verify( e, isConstructingNonStaticInnerClassFromNonStaticContext( innersEnclosingClass, constructingFromClass ),
            Res.MSG_CANNOT_INSTANTIATE_NON_STATIC_CLASSES_HERE, innersEnclosingClass.getName(), innerClass.getRelativeName() );
    verify( e, constructingFromClass != null &&
               isNonStaticInnerClassConstructableFromCurrentFunction( innersEnclosingClass, constructingFromClass ),
            Res.MSG_MUST_BE_IN_OUTER_TO_CONSTRUCT_INNER, innersEnclosingClass.getName(), innerClass.getRelativeName() );
  }

  private boolean isConstructingNonStaticInnerClassFromNonStaticContext(IType innersEnclosingClass, IGosuClassInternal constructingFromClass)
  {
    if( !innersEnclosingClass.isAssignableFrom( constructingFromClass ) )
    {
      IGosuClassInternal csr = constructingFromClass;
      while( csr != null && csr != innersEnclosingClass )
      {
        if( csr.isStatic() )
        {
          return false;
        }
        csr = (IGosuClassInternal) csr.getEnclosingType();
      }
    }
    return true;
  }
  private boolean isNonStaticInnerClassConstructableFromCurrentFunction(IType innersEnclosingClass, IGosuClassInternal constructingFromClass)
  {
    if( !innersEnclosingClass.isAssignableFrom( constructingFromClass ) )
    {
      IGosuClassInternal csr = constructingFromClass;
      while( csr != null && csr != innersEnclosingClass )
      {
        csr = (IGosuClassInternal) csr.getEnclosingType();
      }
      if( csr != innersEnclosingClass )
      {
        // current function's declaring class must be enclosed by inner class' declaring class
        return false;
      }
    }
    return true;
  }

  private void _parseInitializerExpression( ContextType type )
  {
    if( type != null && JavaTypes.COLLECTION().isAssignableFrom( type.getType() ) )
    {
      parseCollectionInitializerList( type.getType() );
    }
    else if( type != null && JavaTypes.MAP().isAssignableFrom( type.getType() ) )
    {
      parseMapInitializerList( type );
    }
    else if( type.getType().isDynamic() )
    {
      parseCollectionInitializerList( type.getType() );
    }
    else
    {
      BadInitializerExpression be = new BadInitializerExpression();
      pushExpression(be);
    }
  }

  private IConstructorInfo getImplicitConstructor(IType type) {
    if (type instanceof IErrorType ) {
      return null;
    }
    IConstructorInfo constructor = null;
    if (!type.isAbstract()) {
      ITypeInfo typeInfo = type.getTypeInfo();
      if (typeInfo instanceof IRelativeTypeInfo) {
        constructor = ((IRelativeTypeInfo) typeInfo).getConstructor(getGosuClass(), IType.EMPTY_ARRAY);
      } else {
        constructor = typeInfo.getConstructor();
      }
    }
    return constructor;
  }

  /*
   * <i>collection-init-list</i>
   * &lt;expression&gt;
   * &lt;collection-init-list&gt; , &lt;expression&gt;
   */
  private void parseCollectionInitializerList( IType type )
  {
    CollectionInitializerExpression lie = new CollectionInitializerExpression();

    IType componentType;
    if( type.isDynamic() )
    {
      componentType = type.getComponentType();
    }
    else
    {
      IType listType = TypeLord.findParameterizedTypeInHierarchy( type, JavaTypes.COLLECTION() );
      if( listType.isParameterizedType() && !listType.isGenericType() )
      {
        componentType = listType.getTypeParameters()[0];
      }
      else
      {
        componentType = JavaTypes.OBJECT();
      }
    }

    do
    {
      parseExpression( new ContextType( componentType ) );
      Expression e = popExpression();
      lie.add( e );
    }
    while( match( null, ',' ) );
    lie.setType( type );
    pushExpression( lie );

  }

  /*
   * <i>map-init-list</i>
   * &lt;key-expression&gt <b>-></b> &lt;value-expression&gt ;
   * &lt;map-init-list&gt; , &lt;expression&gt;
   */
  private void parseMapInitializerList( ContextType type )
  {
    MapInitializerExpression mie = new MapInitializerExpression();

    IType listType = TypeLord.findParameterizedTypeInHierarchy( type.getType(), JavaTypes.MAP() );
    IType keyType = JavaTypes.OBJECT();
    IType valueType = JavaTypes.OBJECT();
    if( listType.isParameterizedType() )
    {
      keyType = listType.getTypeParameters()[0];
      valueType = listType.getTypeParameters()[1];
    }

    do
    {
      parseExpression( new ContextType( keyType ) );
      Expression key = popExpression();
      Expression value;
      if( verify( key, match( null, "->", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_ARROW_AFTER_MAP_KEY ) )
      {
        parseExpression( new ContextType( valueType ) );
        value = popExpression();
      }
      else
      {
        value = new NullExpression();
      }

      mie.add( key, value );

    }
    while( match( null, ',' ) );

    pushExpression(mie);

  }

  /*
   * <i>array-value-list</i>
   * &lt;expression&gt;
   * &lt;array-value-list&gt; , &lt;expression&gt;
   */
  List<Expression> parseArrayValueList( IType componentType )
  {
    while( componentType instanceof TypeVariableType )
    {
      componentType = ((TypeVariableType)componentType).getBoundingType();
    }
    List<Expression> valueExpressions = new ArrayList<Expression>();
    do
    {
      parseExpression( new ContextType( componentType ) );
      Expression e = popExpression();
      valueExpressions.add( e );
    }
    while( match( null, ',' ) );

    return valueExpressions;
  }

  private void parseIndirectMemberAccess( int iOffset, int iLineNum, int iColumn )
  {
    parseIndirectMemberAccess( iOffset, iLineNum, iColumn, false );
  }
  private void parseIndirectMemberAccess( int iOffset, int iLineNum, int iColumn, boolean bParsingTypeLiteralOnly )
  {
    do
    {
      if( !_parseIndirectMemberAccess( bParsingTypeLiteralOnly ) )
      {
        Expression expr = peekExpression();

        if( !maybeReplaceErrantPackageExprWithEnumConstEpr( iOffset, iLineNum, iColumn, expr ) )
        {
          maybeReplacePackageExprWithTypeLiteral( iOffset, iLineNum, iColumn, expr );
          verify( peekExpression(), !(peekExpression().getType() instanceof NamespaceType), Res.MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME );
          verify( peekExpression(), !(peekExpression() instanceof SuperAccess), Res.MSG_MEMBER_ACCESS_REQUIRED_FOR_SUPER );
        }
        break;
      }
      setLocation( iOffset, iLineNum, iColumn );

      Expression expr = peekExpression();
      if( expr instanceof MemberAccess )
      {
        IType inferType = _ctxInferenceMgr.infer( expr );
        if( inferType != null )
        {
          popExpression();
          expr = possiblyWrapWithImplicitCoercion( expr, inferType );
          pushExpression( expr );
        }
      }

    } while( true );
  }

  private void maybeReplacePackageExprWithTypeLiteral( int iOffset, int iLineNum, int iColumn, Expression expr )
  {
    if( expr instanceof Identifier && expr.getType() instanceof NamespaceType )
    {
      String strNamespace = expr.getType().getName();
      if( getNamespace() != null )
      {
        popExpression(); // Pop existing expression; we're going to transform it to a type literal
        tryToMakeTypeLiteral( new String[] {strNamespace}, iOffset, iLineNum, iColumn, strNamespace, expr );
      }
    }
  }

  private boolean maybeReplaceErrantPackageExprWithEnumConstEpr( int iOffset, int iLineNum, int iColumn, Expression expr )
  {
    if( expr instanceof Identifier && expr.getType() instanceof NamespaceType )
    {
      MemberAccess enumConstExpr = parseUnqualifiedEnumConstant( ((Identifier)expr).getSymbol().getName() );
      if( enumConstExpr != null )
      {
        // Set the errant expression's location so that its subordinate expressions are not
        // included in the expression we try next...
        setLocation( iOffset, iLineNum, iColumn );
        enumConstExpr.setStartOffset( iOffset );
        getLocationsList().remove( peekExpression().getLocation() );
        popExpression();

        pushExpression( enumConstExpr );
        return true;
      }
    }
    return false;
  }

  boolean _parseIndirectMemberAccess( boolean bParseTypeLiteralOnly )
  {
    Expression peekRootExpression = peekExpression();

    Token token = getTokenizer().getCurrentToken();
    int operatorLineNumber = token.getLine();
    String value = token.getStringValue();
    if( '.' == token.getType() ||
        (token.getType() == SourceCodeTokenizer.TT_OPERATOR && value != null &&
         ("?.".equals( value ) ||
          "*.".equals( value ))) )
    {
      getTokenizer().nextToken();

      MemberAccessKind kind = MemberAccessKind.getForOperator( value );
      Expression expression = popExpression();
      verify( expression, kind != MemberAccessKind.EXPANSION || TypeLord.getExpandableComponentType( expression.getType() ) != null,
              Res.MSG_TYPE_IS_NOT_ITERABLE, expression.getType().getName() );
      Token T = new Token();
      verify( expression, match( T, SourceCodeTokenizer.TT_WORD ) || match( T, SourceCodeTokenizer.TT_KEYWORD ), Res.MSG_EXPECTING_MEMBER_ACCESS_PATH );
      parseMemberAccess( expression, kind, T.getTokenStart(), T._strValue == null ? "" : T._strValue,
                         makeLazyLightweightParserState(), bParseTypeLiteralOnly );
      verifyNonVoidExpression( peekExpression() );
    }
    else if( parseFeatureLiteral( token, peekRootExpression ) )
    {
      // good
    }
    else if( !bParseTypeLiteralOnly &&
            ('[' == token.getType() ||
             (token.getType() == SourceCodeTokenizer.TT_OPERATOR &&
              "?[".equals( value ))) )
    {
      getTokenizer().nextToken();

      Expression rootExpression = popExpression();
      IType rootType = rootExpression.getType();

      IType indexType = ArrayAccess.supportsArrayAccess( rootType )
                        ? JavaTypes.pINT()
                        : MapAccess.supportsMapAccess( rootType )
                          ? MapAccess.getKeyType( rootType )
                          : null;
      boolean bDynamicRoot = rootType.isDynamic();
      if( bDynamicRoot )
      {
        parseExpression();
      }
      else
      {
        parseExpression( new ContextType( indexType ) );
      }
      Expression indexExpression = popExpression();

      Expression arrayAccess;

      // Assume null-safety for backward compatibility in non-open-source versions, otherwise make it explicit with "?["
      boolean bNullSafe = !ILanguageLevel.Util.STANDARD_GOSU() || value != null && "?[".equals( value );

      if( ArrayAccess.supportsArrayAccess( rootType ) )
      {
        ArrayAccess aa = new ArrayAccess();
        aa.setRootExpression( rootExpression );
        if( verify( indexExpression, indexExpression.getType() == JavaTypes.pINT() ||
                                     indexExpression.getType() == JavaTypes.INTEGER() ||
                                     bDynamicRoot,
                    Res.MSG_ARRAY_INDEX_MUST_BE_INT ) )
        {
          aa.setMemberExpression( indexExpression );
        }
        aa.setNullSafe( bNullSafe );
        pushExpression( aa );
        arrayAccess = aa;
      }
      else if( MapAccess.supportsMapAccess( rootType ) )
      {
        MapAccess ma = new MapAccess();
        ma.setRootExpression( rootExpression );
        ma.setKeyExpression( indexExpression );
        ma.setNullSafe( bNullSafe );
        pushExpression( ma );
        arrayAccess = ma;
      }
      else if( isSuperCall( rootExpression, indexExpression ) )
      {
        SuperAccess ma = new SuperAccess();
        ma.setRootExpression( (Identifier)rootExpression );
        ma.setKeyExpression( (TypeLiteral)indexExpression );
        IType superType = verifySuperTypeIsDeclaredInCompilingClass( (TypeLiteral)indexExpression );
        ma.setType( superType );
        pushExpression( ma );
        arrayAccess = ma;
      }
      else
      {
        verify( rootExpression, BeanAccess.isBeanType( rootExpression.getType() ) ||
                (rootExpression.getType() instanceof MetaType), Res.MSG_EXPECTING_BEAN_TYPE_WITH_REFLECTION_OPERATOR );
        verify( indexExpression,
                JavaTypes.CHAR_SEQUENCE().isAssignableFrom( indexExpression.getType() ),
                Res.MSG_PROPERTY_REFLECTION_ONLY_WITH_STRINGS );
        MemberAccess ma = new MemberAccess();
        ma.setRootExpression( rootExpression );
        ma.setMemberExpression( indexExpression );
        ma.setType( JavaTypes.OBJECT() );
        ma.setMemberAccessKind( bNullSafe ? MemberAccessKind.NULL_SAFE : MemberAccessKind.NORMAL );

        pushExpression( ma );
        arrayAccess = ma;
      }

      verify( arrayAccess, match( null, ']' ), Res.MSG_EXPECTING_BRACKET_TO_CLOSE_DYNAMIC_MEMBER_ACCESS );
    }
    else if( peekExpression().getType() instanceof IBlockType && match( null, '(' ) )
    {
      IBlockType iBlockType = (IBlockType)peekExpression().getType();
      BlockInvocation bi = new BlockInvocation( popExpression() );
      boolean bNoArgsProvided;
      if( !(bNoArgsProvided = match( null, ')' )) || iBlockType.hasOptionalParams() )
      {
        MethodScore score = parseArgumentList2( bi, Collections.singletonList( iBlockType ), IType.EMPTY_ARRAY, true, bNoArgsProvided );
        bi.setNamedArgOrder( score.getNamedArgOrder() );
        bi.setArgs( score.getArguments() );
        verify( bi, bNoArgsProvided || match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
      }
      verify( bi, bi.getArgs().size() == iBlockType.getParameterTypes().length, Res.MSG_WRONG_NUM_OF_ARGS,
              iBlockType.getParameterTypes().length );
      bi.setType( iBlockType.getReturnType() );
      verifyNonVoidExpression( bi );
      pushExpression( bi );
    }
    else
    {
      return false;
    }

    // Reroot the root expression under the indirect access
    if( peekRootExpression != null && !peekRootExpression.hasParseExceptions() )
    {
      ParseTree rootLocation = peekRootExpression.getLocation();
      if( rootLocation != null )
      {
        getLocationsList().remove( peekRootExpression.getLocation() );
        setLocation( rootLocation.getOffset(), rootLocation.getLineNum(), rootLocation.getColumn() );
        peekExpression().getLocation().addChild( peekRootExpression.getLocation() );
      }
    }

    setOperatorLineNumber(peekExpression(), operatorLineNumber);
    return true;
  }

  private IType verifySuperTypeIsDeclaredInCompilingClass( TypeLiteral superTypeLiteral )
  {
    verify( superTypeLiteral, !superTypeLiteral.getContainedParsedElementsByType( ITypeParameterListClause.class, null ), Res.MSG_PARAMETERIZED_TYPE_NOT_ALLOWED_HERE );
    IType type = TypeLord.getPureGenericType( superTypeLiteral.getType().getType() );
    ICompilableTypeInternal gosuClass = getGosuClass();
    IType superType = gosuClass.getSupertype();
    if( superType != null && TypeLord.getPureGenericType( superType ) == type )
    {
      return superType;
    }
    else if( superType == null && type == JavaTypes.OBJECT() )
    {
      return type;
    }

    for( IType iface : gosuClass.getInterfaces() )
    {
      if( TypeLord.getPureGenericType( iface ) == type )
      {
        return iface;
      }
    }

    addError( superTypeLiteral, Res.MSG_NOT_A_SUPERTYPE, type );
    return type;
  }

  private boolean isSuperCall( Expression rootExpression, Expression indexExpression )
  {
    return indexExpression instanceof TypeLiteral &&
           rootExpression instanceof Identifier &&
           ((Identifier)rootExpression).getSymbol().getName().equals( Keyword.KW_super.getName() );
  }

  private boolean parseFeatureLiteral( Token token, Expression root )
  {
    Token T = new Token();
    if( SourceCodeTokenizer.TT_OPERATOR == token.getType() && "#".equals( token.getStringValue() ) )
    {
      getTokenizer().nextToken();
      
      if( root != popExpression() )
      {
        throw new IllegalStateException();
      }
      FeatureLiteral fle = new FeatureLiteral( root );
      boolean foundWord = verify( fle, match( T, SourceCodeTokenizer.TT_WORD ) || match( T, Keyword.KW_construct ),
                                  Res.MSG_FL_EXPECTING_FEATURE_NAME );
      if( foundWord )
      {
        if( match( null, "<", SourceCodeTokenizer.TT_OPERATOR, true ) )
        {
          parseErrantFeatureLiteralParameterization( fle );
        }
        if( match( null, '(' ) )
        {
          if( !match( null, ')' ) )
          {
            MethodScore score = parseArgumentList2( fle, fle.getFunctionTypes( T._strValue ), IType.EMPTY_ARRAY, true, false );
            if( allTypeLiterals( score.getArguments() ) )
            {
              //noinspection ThrowableResultOfMethodCallIgnored
              fle.removeParseException( Res.MSG_AMBIGUOUS_METHOD_INVOCATION );

              List<IType> types = evalTypes( score.getArguments() );

              if( T._strValue.equals( Keyword.KW_construct.getName() ) )
              {
                verify( fle, fle.resolveConstructor( types ), Res.MSG_FL_CONSTRUCTOR_NOT_FOUND, StringUtil.join( ",", types ) );
              }
              else
              {
                verify( fle, fle.resolveMethod( T._strValue, types ), Res.MSG_FL_METHOD_NOT_FOUND, T._strValue, StringUtil.join( ",", types ) );
              }
            }
            else
            {
              IInvocableType funcType = score.getRawFunctionType();
              if( funcType == null || funcType.getParameterTypes().length != score.getArguments().size() )
              {
                fle.setFeature( ErrorType.getInstance().getTypeInfo().getMethod( T._strValue ), score.getArguments() );
                verify( fle, false, Res.MSG_FL_METHOD_NOT_FOUND, T._strValue, "" );
              }
              else if( funcType instanceof IConstructorType )
              {
                fle.setFeature( ((IConstructorType)funcType).getConstructor(), score.getArguments() );
              }
              else
              {
                fle.setFeature( ((IFunctionType)funcType).getMethodInfo(), score.getArguments() );
              }
            }
            verify( fle, match( null, ')' ), Res.MSG_FL_EXPECTING_RIGHT_PAREN );
          }
          else
          {
            if( T._strValue.equals( Keyword.KW_construct.getName() ) )
            {
              verify( fle, fle.resolveConstructor( Collections.<IType>emptyList() ), Res.MSG_FL_CONSTRUCTOR_NOT_FOUND, "" );
            }
            else
            {
              verify( fle, fle.resolveMethod( T._strValue, Collections.<IType>emptyList() ), Res.MSG_FL_METHOD_NOT_FOUND, T._strValue, "" );
            }
          }
        }
        else
        {
          boolean propResolved = fle.resolveProperty( T._strValue );
          if( !propResolved )
          {
            verify( fle, propResolved, Res.MSG_FL_PROPERTY_NOT_FOUND, T._strValue );
          }
        }
      }
      else
      {
        fle.setType( ErrorType.getInstance() );
      }

      if( root instanceof FeatureLiteral )
      {
        verify( fle, fle.isPropertyLiteral(), Res.MSG_FL_ONLY_PROPERTIES_MAY_BE_CHAINED );
        verify( fle, ((FeatureLiteral) root).isPropertyLiteral(), Res.MSG_FL_ONLY_PROPERTIES_MAY_BE_CHAINED );
      }

      if( fle.isStaticish() && !fle.hasParseExceptions() )
      {
        verify( fle, root instanceof TypeLiteral, Res.MSG_FL_STATIC_FEATURES_MUST_BE_REFERENCED_FROM_THEIR_TYPES );
      }

      pushExpression( fle );
      return true;
    }
    else
    {
      return false;
    }
  }

  private void parseErrantFeatureLiteralParameterization(FeatureLiteral fle) {
    while( verify( fle, parseTypeLiteral(), null ) )
    {
      popExpression(); // TypeLiteral expr
      if( !match( null, ',' ) )
      {
        break;
      }
    }
    verify( fle, match( null, ">", SourceCodeTokenizer.TT_OPERATOR, true ), Res.MSG_FL_EXPECTING_RIGHT_CARET );
    verify( fle, false, Res.MSG_FL_GENERIC_FUNCTION_REFERENCES_NOT_YET_SUPPORTED );
  }

  private List<IType> evalTypes(List<IExpression> arguments) {
    List<IType> types = new ArrayList<IType>();
    for( IExpression expression : arguments)
    {
      expression.clearParseExceptions();
      expression.clearParseWarnings();
      if( expression instanceof ImplicitTypeAsExpression)
      {
        expression = ((ImplicitTypeAsExpression)expression).getLHS();
      }
      types.add( ((TypeLiteral)expression).evaluate() );
    }
    return types;
  }

  private boolean allTypeLiterals(List<IExpression> args) {
    boolean allTypeLiterals = true;
    for( IExpression arg : args)
    {
      if( !(arg instanceof TypeLiteral || (arg instanceof ImplicitTypeAsExpression && ((ImplicitTypeAsExpression)arg).getLHS() instanceof TypeLiteral)) )
      {
        allTypeLiterals = false;
      }
    }
    return allTypeLiterals;
  }

  private void setOperatorLineNumber( Expression expression, int operatorLineNumber )
  {
    if( expression instanceof IHasOperatorLineNumber )
    {
      ((IHasOperatorLineNumber)expression).setOperatorLineNumber( operatorLineNumber );
    }
  }

  boolean parseNameOrMethodCall( Token token )
  {
    if( token.getType() == SourceCodeTokenizer.TT_KEYWORD &&
        (Keyword.KW_true == token.getKeyword() ||
         Keyword.KW_false == token.getKeyword() ||
         Keyword.KW_NaN == token.getKeyword() ||
         Keyword.KW_Infinity == token.getKeyword() ||
         Keyword.KW_null == token.getKeyword()) )
    {
      return false;
    }

    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    boolean bRet = _parseNameOrMethodCall( token );
    if( bRet )
    {
      verifyNonVoidExpression(peekExpression());
      setLocation( iOffset, iLineNum, iColumn );
    }

    Expression expr = peekExpression();
    if( expr instanceof Identifier )
    {
      IType inferType = _ctxInferenceMgr.infer( expr );
      if( inferType != null )
      {
        Identifier ma = (Identifier)popExpression();
        expr = possiblyWrapWithImplicitCoercion( ma, inferType );
        //ma.setType( inferType );
        pushExpression( expr );
      }
    }

    return bRet;
  }

  boolean _parseNameOrMethodCall( Token token )
  {
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    IParserState state = makeLazyLightweightParserState(); //capture position of word for error reporting
    int markBefore = getTokenizer().mark();
    if( isWordOrValueKeyword( token ) || matchPrimitiveType( true ) )
    {
      getTokenizer().nextToken();
      
      String[] strToken = new String[] {getTokenizer().getTokenAt( markBefore ).getStringValue()};
      MethodCallExpression e = new MethodCallExpression();
      IType[] typeParameters = parsePossibleFunctionParameterization( strToken[0], e );

      String strFunction = strToken[0];
      ISymbol functionSymbol = possiblyResolveFunctionSymbol( e, strFunction );

      int mark = _tokenizer.mark();

      // Only parse the function symbol as a method invocation if it is not a block.  Blocks parse as identifiers
      // and then indirect BlockInvocation expressions
      if( !isBlockSym( functionSymbol ) && !isInSeparateStringTemplateExpression() && match( null, '(' ) )
      {
        parseMethodCall( strToken, iOffset, iLineNum, iColumn, state, e, typeParameters, strFunction, functionSymbol, mark );
      }
      else
      {
        token = getTokenizer().getCurrentToken();
        if( !Keyword.KW_super.equals( strToken[0] ) ||
            token.getType() == '.' ||
            "#".equals( token.getStringValue() ) ||
            token.getType() == '[' )
        {
          parseIdentifierOrTypeLiteralOrEnumConstant( strToken, iOffset, iLineNum, iColumn );
        }
        else
        {
          getTokenizer().restoreToMark( markBefore );
          return false;
        }
      }
      verify( peekExpression(), !GosuObjectUtil.equals( strFunction, Keyword.KW_this.toString() ) ||
              !isParsingStaticFeature() ||
              isParsingConstructor(), Res.MSG_CANNOT_REFERENCE_THIS_IN_STATIC_CONTEXT );
      return true;
    }

    return false;
  }

  private void parseMethodCall( String[] t, int iOffset, int iLineNum, int iColumn, IParserState state, MethodCallExpression e, IType[] typeParameters, String strFunction, ISymbol functionSymbol, int mark )
  {
    int iLocationsCount = _locations.size();
    parseMethodCall( t, state, e, typeParameters, strFunction, functionSymbol );
    Expression expr = peekExpression();
    if( expr.hasParseExceptions() )
    {
      maybeParseIdentifierAssumingOpenParenIsForParenthesizedExpr( t, iOffset, iLineNum, iColumn, state, e, typeParameters, strFunction, functionSymbol, mark, iLocationsCount );
    }
  }

  private void maybeParseIdentifierAssumingOpenParenIsForParenthesizedExpr( String[] t, int iOffset, int iLineNum, int iColumn, IParserState state, MethodCallExpression e, IType[] typeParameters, String strFunction, ISymbol functionSymbol, int mark, int iLocationsCount )
  {
    if( !isOpenParenOnNextLine( mark ) )
    {
      return;
    }

    backtrack( mark, iLocationsCount );
    parseIdentifierOrTypeLiteralOrEnumConstant( t, iOffset, iLineNum, iColumn );

    Expression expr = peekExpression();
    if( expr.hasParseExceptions() )
    {
      // Failed to parse as Identifier etc., reparse as Method call

      backtrack( mark, iLocationsCount );
      parseMethodCall( t, state, e, typeParameters, strFunction, functionSymbol );
    }
  }

  private void backtrack( int mark, int iLocationsCount )
  {
    backtrack( mark, iLocationsCount, popExpression() );
  }
  private void backtrack( int mark, int iLocationsCount, Expression expr )
  {
    _tokenizer.restoreToMark( mark );
    removeInnerClasses( expr );
    removeLocationsFrom( iLocationsCount );
  }

  private void parseMethodCall( String[] t, IParserState state, MethodCallExpression e, IType[] typeParameters, String strFunction, ISymbol functionSymbol )
  {
    // MethodCall
    if( functionSymbol == null && getGosuClass() != null )
    {
      functionSymbol = getGosuClass().getExternalSymbol( strFunction );
    }
    if( functionSymbol != null )
    {
      IType symbolType = functionSymbol.getType();
      if( symbolType instanceof IFunctionType )
      {
        int mark = getTokenizer().mark();
        int iLocationsCount = _locations.size();
        parsePlainFunction( (IFunctionSymbol)functionSymbol );
        verifyCase( peekExpression(), strFunction, functionSymbol.getName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );
        if( peekExpression().hasParseException( Res.MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION ) )
        {
          // maybe there's a print() method defined locally...
          backtrack( mark, iLocationsCount );
        }
        else
        {
          return;
        }
      }
      if( symbolType.isDynamic() )
      {
        parseDynamicFunction( (Symbol)functionSymbol );
        verifyCase( peekExpression(), strFunction, functionSymbol.getName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );
        return;
      }
    }

    int iParenStart = _tokenizer.getTokenStart();
    e.setArgPosition( iParenStart );
    List<IFunctionType> listFunctionTypes = null;
    final boolean bThis = GosuObjectUtil.equals( strFunction, Keyword.KW_this.getName() );
    boolean isRecursiveConstructorCall = false;
    boolean bNoArgsProvided;
    if( !(bNoArgsProvided = match( null, ')' )) ||
        ((listFunctionTypes = getFunctionTypesForName( strFunction )).size() == 1 && listFunctionTypes.get( 0 ).hasOptionalParams()) )
    {
      if( listFunctionTypes == null )
      {
        listFunctionTypes = getFunctionTypesForName( strFunction );
      }
      if( typeParameters != null )
      {
        listFunctionTypes = parameterizeFunctionTypes( e, typeParameters, listFunctionTypes );
      }

      MethodScore bestMethod = parseArgumentList2( e, listFunctionTypes, typeParameters, true, bNoArgsProvided );

      //noinspection SuspiciousToArrayCall
      Expression[] eArgs = bestMethod.getArguments().toArray( new Expression[bestMethod.getArguments().size()] );
      e.setArgs( eArgs );

      boolean bMatched = bestMethod.isValid() && bestMethod.getRawFunctionType().getParameterTypes().length == eArgs.length;
      for( Expression arg : eArgs )
      {
        if( arg.hasParseExceptions() )
        {
          bMatched = false;
          break;
        }
      }

      if( !bMatched )
      {
        if( listFunctionTypes.isEmpty() )
        {
          if( staticRefToNonStaticFunc( strFunction, eArgs ) )
          {
            verify( e, false, state, Res.MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT, strFunction );
          }
          else
          {
            verify( e, false, state, Res.MSG_NO_SUCH_FUNCTION, strFunction );
          }
        }
      }

      if( bestMethod.isValid() )
      {
        // Did not parse as object literal
        IFunctionType rawFunctionType = (IFunctionType)bestMethod.getRawFunctionType();
        verifyArgCount( e, bestMethod.getArguments().size(), rawFunctionType );

        if( !(bThis || GosuObjectUtil.equals( strFunction, Keyword.KW_super.getName() )) )
        {
          verifyCase( e, strFunction, rawFunctionType.getName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );
        }
        else if( bThis )
        {
          final IType[] parameterTypes0 = peekParsingFunction().getParameterTypes();
          final IType[] parameterTypes1 = rawFunctionType.getParameterTypes();
          isRecursiveConstructorCall = parameterTypes0.length == parameterTypes1.length &&
                                       Arrays.equals( parameterTypes0, parameterTypes1 );
        }

        e.setFunctionSymbol( getDFSForFunctionType( strFunction, bestMethod ) );
        e.setNamedArgOrder( bestMethod.getNamedArgOrder() );
        IFunctionType inferredFunctionType = (IFunctionType)bestMethod.getInferredFunctionType();
        if( inferredFunctionType instanceof FunctionType )
        {
          ((FunctionType)inferredFunctionType).setScriptPart( rawFunctionType.getScriptPart() );
        }
        e.setType( inferredFunctionType.getReturnType() );
        e.setFunctionType( inferredFunctionType );
      }
      else
      {
        e.setType( ErrorType.getInstance() );
      }
      verify( e, bNoArgsProvided || match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
    }
    else
    {
      if( bThis &&
          bNoArgsProvided &&
          peekParsingFunction().getParameterTypes().length == 0 &&
          getScriptPart().getContainingType().getName().endsWith( peekParsingFunction().getName() ) )
      {
        isRecursiveConstructorCall = true;
      }

      IFunctionSymbol function = (IFunctionSymbol)getSymbolTable().getSymbol( strFunction + "()" );
      if( function == null )
      {
        function = (IFunctionSymbol)getSymbolTable().getSymbol( strFunction );
      }

      // check for property match *before* checking for overloading
      if( function == null || !(function.getType() instanceof IFunctionType))
      {
        String strPropertyName = getPropertyNameFromMethodName( strFunction );
        boolean bPossiblePropertyName = getGosuClass() != null && strPropertyName != null;
        if( bPossiblePropertyName )
        {
          t[0] = strPropertyName;
          parseIdentifier( new PropertyAsMethodCallIdentifier( strFunction ), t );
          Expression expression = peekExpression();
          if( !expression.hasParseExceptions() )
          {
            return;
          }
          popExpression();
        }
      }

      if( function == null )
      {
        listFunctionTypes = getFunctionTypesForName( strFunction );
        if( !listFunctionTypes.isEmpty() )
        {
          verifyArgCount( e, 0, listFunctionTypes.get( 0 ) );
        }
        List<IFunctionSymbol> possibleMatches = getDfsDeclsForFunction( strFunction );
        if( !possibleMatches.isEmpty() )
        {
          function = possibleMatches.get( 0 );
          e.setFunctionSymbol( function );
        }
      }
      if( function == null || !(function.getType() instanceof IFunctionType) )
      {
        if( staticRefToNonStaticFunc( strFunction, new Expression[0] ) )
        {
          verify( e, false, state, Res.MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT, strFunction );
        }
        else
        {
          verify( e, false, state, Res.MSG_NO_SUCH_FUNCTION, strFunction );
        }
        e.setType( ErrorType.getInstance() );
      }
      else
      {
        e.setFunctionSymbol( function );

        verifyCase( e, strFunction, function.getDisplayName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );

        IFunctionType funcType = (IFunctionType)function.getType();
        if( typeParameters != null )
        {
          if( verifyCanParameterizeType( e, funcType, typeParameters ) )
          {
            IFunctionType parameterizedFuncType = (IFunctionType)funcType.getParameterizedType( typeParameters );
            if( verify( e, parameterizedFuncType != null, Res.MSG_COULD_NOT_PARAMETERIZE ) )
            {
              funcType = parameterizedFuncType;
            }
          }
        }
        assert funcType != null;
        if( isEndOfExpression() )
        {
          funcType = maybeParameterizeOnCtxType( funcType );
        }
        IFunctionType boundFuncType = boundFunctionType( funcType );
        e.setType( boundFuncType.getReturnType() );
        e.setFunctionType( boundFuncType );
      }
      e.setArgs( null );
    }

    verify( e, !isRecursiveConstructorCall, Res.MSG_RECURSIVE_CONSTRUCTOR );

    if( isParsingAbstractConstructor() &&
            e.getFunctionSymbol() instanceof DynamicFunctionSymbol &&
            e.getFunctionSymbol().isAbstract() )
    {
      e.addParseException( new ParseException( state, Res.MSG_NO_ABSTRACT_METHOD_CALL_IN_CONSTR, e.getFunctionSymbol().getDisplayName() ) );
    }

    if( e instanceof MethodCallExpression )
    {
      verifyNotCallingOverridableFunctionFromCtor( (MethodCallExpression)e );
      if( getGosuClass() != null && getGosuClass().isAnonymous() && getGosuClass().getEnclosingType() instanceof IGosuEnhancement )
      {
        verify( e, false, Res.MSG_CANNOT_REFERENCE_ENCLOSING_METHODS_WITHIN_ENHANCEMENTS );
      }
    }

    pushExpression( e );
  }

  private boolean isInSeparateStringTemplateExpression()
  {
    // If parsing a template or string template, look to see if we are parsing tokens within a
    // separate template expression.  We want to avoid fusing two expressions together
    // eg. "${x} ${(x)}" will otherwise try to parse the tokens of the first expr along with the
    // tokens of the second as a method bad call instead of just stopping after the first.
    // We can determine if we've crossed an expression boundary by examining the prior token and
    // checking for the '}' terminal in the whitespace (non-source code content is considered
    // whitespace while parsing a template).
    if( getTokenizerInstructor() != null )
    {
      Token priorToken = getTokenizer().getTokenAt( getTokenizer().getState() - 1 );
      return priorToken != null &&
             priorToken.getType() == ISourceCodeTokenizer.TT_WHITESPACE &&
             (priorToken.getStringValue().indexOf( '}' ) >= 0 ||
              priorToken.getStringValue().indexOf( '>' ) >= 0);
    }
    return false;
  }

  private ISymbol possiblyResolveFunctionSymbol( MethodCallExpression e, String strFunction )
  {
    ISymbol functionSymbol = _symTable.getSymbol(strFunction);
    if( functionSymbol == null )
    {
      // Try to find a captured closure
      functionSymbol = resolveSymbol( e, strFunction, false );

      //noinspection ThrowableResultOfMethodCallIgnored
      e.removeParseException( Res.MSG_BAD_IDENTIFIER_NAME );

      if( !(functionSymbol instanceof CapturedSymbol) )
      {
        functionSymbol = null;
      }
    }
    return functionSymbol;
  }

  private boolean isBlockSym( ISymbol functionSymbol )
  {
    return functionSymbol != null && functionSymbol.getType() instanceof IBlockType;
  }

  private boolean staticRefToNonStaticFunc( String stFunction, Expression[] eArgs )
  {
    if( isParsingStaticFeature() && getGosuClass() != null )
    {
      List<? extends IDynamicFunctionSymbol> matchingDFSs = getGosuClass().getMemberFunctions( stFunction );
      for( IDynamicFunctionSymbol dfs : matchingDFSs )
      {
        if( !dfs.isStatic() && dfs.getArgs().size() == eArgs.length )
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean staticRefToNonStaticProp( String name )
  {
    if( isParsingStaticFeature() && getGosuClass() != null )
    {
      IDynamicPropertySymbol dps = getGosuClass().getMemberProperty( name );
      if( dps != null && !dps.isStatic() )
      {
        return true;
      }
    }
    return false;
  }

  private IFunctionType boundFunctionType( IFunctionType funcType )
  {
    if( funcType.isGenericType() )
    {
      IGenericTypeVariable[] typeVariables = funcType.getGenericTypeVariables();
      List<IType> functionTypeVars = new ArrayList<IType>();
      addTypeVarsToList( functionTypeVars, typeVariables );
      return (IFunctionType)TypeLord.boundTypes( funcType, functionTypeVars );
    }
    else
    {
      return funcType;
    }
  }

  private IDynamicFunctionSymbol getDFSForFunctionType( String strFunction, MethodScore bestMethod )
  {
    List<IFunctionSymbol> list = getDfsDeclsForFunction( strFunction );
    IInvocableType rawFunctionType = bestMethod.getRawFunctionType();
    for( IFunctionSymbol dfs : list )
    {
      IType dfsType = dfs.getType();
      if( dfsType.equals( rawFunctionType ) || dfsType.equals( rawFunctionType.getGenericType() ) )
      {
        return (IDynamicFunctionSymbol) dfs;
      }
    }
    throw new IllegalStateException( "Could not find matching DFS in " + list + " for type " + rawFunctionType );
  }

  private void verifyNotCallingOverridableFunctionFromCtor( MethodCallExpression mce )
  {
    if( !isParsingConstructor() || isParsingBlock() )
    {
      return;
    }

    IFunctionSymbol fs = mce.getFunctionSymbol();
    if( fs instanceof DynamicFunctionSymbol &&
            !(fs instanceof SuperConstructorFunctionSymbol) &&
            !(fs instanceof ThisConstructorFunctionSymbol) )
    {
      DynamicFunctionSymbol dfs = (DynamicFunctionSymbol)fs;
      verifyOrWarn( mce, dfs.isPrivate() || dfs.isFinal() || dfs.isStatic(), true, Res.MSG_CALLING_OVERRIDABLE_FROM_CTOR, dfs.getName() );
    }
  }

  private void verifyNotCallingOverridableFunctionFromCtor( BeanMethodCallExpression mce )
  {
    if( !isParsingConstructor() || isParsingBlock() )
    {
      return;
    }

    if( mce.getRootExpression() instanceof Identifier &&
            ((Identifier)mce.getRootExpression()).getSymbol() != null &&
            Keyword.KW_this.equals( ((Identifier)mce.getRootExpression()).getSymbol().getName() ) )
    {
      IMethodInfo mi = mce.getMethodDescriptor();
      if( mi instanceof AbstractGenericMethodInfo )
      {
        ReducedDynamicFunctionSymbol dfs = ((AbstractGenericMethodInfo)mi).getDfs();
        if (!((AbstractGenericMethodInfo)mi).getDfs().isSuperOrThisConstructor())
        {
          verifyOrWarn( mce, dfs.isPrivate() || dfs.isFinal() || dfs.isStatic(), true, Res.MSG_CALLING_OVERRIDABLE_FROM_CTOR, dfs.getName() );
        }
      }
    }
  }

  private boolean isParsingConstructor()
  {
    if( !isParsingFunction() )
    {
      return false;
    }
    FunctionType funcType = peekParsingFunction();
    return funcType.getReturnType() == getGosuClass() &&
            funcType.getName().equals( funcType.getReturnType().getRelativeName() );
  }

  private IType[] parsePossibleFunctionParameterization( String name, MethodCallExpression e )
  {
    if( match( null, "<", SourceCodeTokenizer.TT_OPERATOR, true ) )
    {
      List<IFunctionType> listFunctionTypes = getFunctionTypesForName( name );
      for( IFunctionType ftype : listFunctionTypes )
      {
        if( ftype.isGenericType() )
        {
          return parseFunctionParameterization( e );
        }
      }
    }
    return null;
  }

  private void parseIdentifier( String[] T )
  {
    parseIdentifier( new Identifier(), T );
  }
  private void parseIdentifier( Identifier e, String[] T )
  {
    // Identifier
    String name = T[0];
    ISymbol s = resolveSymbol( e, name, true );

    if( s instanceof DynamicFunctionSymbol )
    {
      // No function symbols allowed as identifiers; use a blocks for that
      s = resolveNamespaceSymbol( e, name );
    }
    else if( s instanceof DynamicPropertySymbol )
    {
      //wrap in a PropertyAccessIdentifier to distinguish between identifiers that
      //invoke code versus those that do not
      e = new PropertyAccessIdentifier( e );
    }

    IType type = s.getType();

    e.setSymbol( s, _symTable );
    e.setType( type );

    if( s instanceof DynamicPropertySymbol )
    {
      if( getGosuClass() != null && !(getGosuClass() instanceof IGosuProgram) && getGosuClass().isAnonymous() && getGosuClass().getEnclosingType() instanceof IGosuEnhancement )
      {
        if( s.getName().equals( Keyword.KW_outer.toString() ) )
        {
          verify( e, false, Res.MSG_CANNOT_REFERENCE_OUTER_SYMBOL_WITHIN_ENHANCEMENTS );
        }
        else
        {
          verify( e, false, Res.MSG_CANNOT_REFERENCE_ENCLOSING_PROPERTIES_WITHIN_ENHANCEMENTS );
        }
      }
      else if( getGosuClass() != null &&
               getGosuClass().getEnclosingType() != null &&
               getGosuClass().getEnclosingType().isInterface() &&
               s.getName().equals( Keyword.KW_outer.toString() ) )
      {
        verify( e, false, Res.MSG_BAD_IDENTIFIER_NAME, name );
      }

    }

    verify( e, !(s instanceof AmbiguousSymbol), Res.MSG_AMBIGUOUS_SYMBOL_REFERENCE, name );

    pushExpression( e );

    verify( e, !(getCurrentEnclosingGosuClass() instanceof IBlockClass) ||
            !Keyword.KW_super.equals( s.getName() ), Res.MSG_SUPER_NOT_ACCESSIBLE_FROM_BLOCK );

    if( !(type instanceof ErrorType) && !(type instanceof IFunctionType) )
    {
      //resolve a symbol, let's make sure its case is correct
      verifyCase( e, name, s.getName(), Res.MSG_VAR_CASE_MISMATCH, false );
    }
  }

  private void parseIdentifierOrTypeLiteralOrEnumConstant( String[] T, int iOffset, int iLineNum, int iColumn )
  {
    // Identifier
    String name = T[0];
    parseIdentifier( T );
    Expression identifier = peekExpression();
    IType type = identifier.getType();
    if( type instanceof ErrorType || (type instanceof IFunctionType && !(type instanceof IBlockType)))
    {
      // Couldn't parse an identifer , lets try parsing static member-access or a type literal...
      //
      // Note we must parse a type literal here instead of (or in addition to)
      // parseLiteral() because Identifiers and TypeLiterals have the same
      // start symbol.

      // Set the errant expression's location so that its subordinate expressions are not
      // included in the expression we try next...
      setLocation( iOffset, iLineNum, iColumn );
      getLocationsList().remove( peekExpression().getLocation() );

      Expression errantExpression = popExpression();

      // See if it can be parsed as an inferred enum expression
      MemberAccess enumConstExpr = parseUnqualifiedEnumConstant( T[0] );
      if( enumConstExpr != null )
      {
        pushExpression( enumConstExpr );
        enumConstExpr.setStartOffset( iOffset );
      }
      else
      {
        // Try parsing a type literal
        tryToMakeTypeLiteral( T, iOffset, iLineNum, iColumn, name, errantExpression );
      }
    }
  }

  private void tryToMakeTypeLiteral( String[] T, int iOffset, int iLineNum, int iColumn, String name, Expression errantExpression )
  {
    TypeLiteral tl = resolveTypeLiteral(T);
    boolean bArrayOrParameterzied = resolveArrayOrParameterizationPartOfTypeLiteral( T, false, tl );

    if( !bArrayOrParameterzied && ((TypeLiteral)peekExpression()).getType().getType() instanceof ErrorType )
    {
      popExpression();

      // All has failed.
      // Try to determine the most suitable errant expression and push that
      if( staticRefToNonStaticProp( name ) )
      {
        errantExpression.clearParseExceptions();
        verify( errantExpression, false, Res.MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT );
      }

      pushExpression( errantExpression );
    }
    else if( match( null, "&", SourceCodeTokenizer.TT_OPERATOR, true ) )
    {
      setLocation( iOffset, iLineNum, iColumn ); // set location of initial type literal (tl)
      parseAggregateTypeLiteral(false);
    }
  }

  private void parseNamespaceStartOrRelativeType( String[] T, boolean bInterface )
  {
    IType type = resolveNamespace( T[0] );

    if( type != null )
    {
      Identifier e = new Identifier();
      ISymbol s = new Symbol( T[0], type, null );
      e.setSymbol( s, _symTable );
      e.setType( type );

      pushExpression( e );
    }
    else
    {
      TypeLiteral tl = resolveTypeLiteral( T, true, bInterface );
      resolveArrayOrParameterizationPartOfTypeLiteral( T, true, tl );
    }
  }

  private MemberAccess parseUnqualifiedEnumConstant( String strConstValue )
  {
    IType contextType = getContextType().getType();
    if( contextType != null )
    {
      if( contextType.isEnum() || TypeSystem.get( IEnumValue.class ).isAssignableFrom( contextType ) )
      {
        try
        {
          IPropertyInfo property = contextType.getTypeInfo().getProperty( strConstValue );
          if( property != null && property.isStatic() )
          {
            MemberAccess ma = new UnqualifiedEnumMemberAccess();
            TypeLiteral e = new TypeLiteral( MetaType.getLiteral( property.getOwnersType() ) );

            ma.setRootExpression( e );
            ma.setMemberName( property.getName() );
            ma.setType( property.getFeatureType() );
            ma.setMemberAccessKind( MemberAccessKind.NORMAL );
            return ma;
          }
        }
        catch( IllegalArgumentException iae )
        {
          // skip
        }
      }
    }
    return null;
  }

  private void verifyArgCount( ParsedElement element, int iArgs, IFunctionType funcType )
  {
    int expectedArgs = funcType.getParameterTypes().length;
    if( iArgs != expectedArgs )
    {
      ParseException pe = new WrongNumberOfArgsException( makeFullParserState(), Res.MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION, funcType.getParamSignature(), expectedArgs, iArgs );
      pe.setParamTypesExpected( funcType.getParameterTypes() );
      pe.setCausedByArgumentList( true );
      element.addParseException( pe );
    }
  }

  private void verifyOverrideNotOnMethodThatDoesNotExtend( ParsedElement element, DynamicFunctionSymbol dfs )
  {
    if( !dfs.isOverride() )
    {
      return;
    }

    ParseException pe = null;

    ICompilableType gsClass = getGosuClass();
    if( gsClass != null )
    {
      String strPotentialProperty = getPropertyNameFromMethodNameIncludingSetter( dfs.getDisplayName() );
      if( strPotentialProperty != null )
      {
        pe = new DoesNotOverrideFunctionException( makeFullParserState(), Res.MSG_FUNCTION_NOT_OVERRIDE_PROPERTY, dfs.getName(), strPotentialProperty );
      }
    }

    if( pe == null )
    {
      pe = new DoesNotOverrideFunctionException( makeFullParserState(), Res.MSG_FUNCTION_NOT_OVERRIDE, dfs.getName() );
    }

    element.addParseException( pe );
  }

  private void verifyArgCount( ParsedElement element, int iArgs, IConstructorType ctorType )
  {
    if( ctorType.getConstructor() instanceof DynamicConstructorInfo )
    {
      return;
    }

    int expectedArgs = ctorType.getParameterTypes().length;
    if( iArgs != expectedArgs )
    {
      ParseException pe = new ParseException( makeFullParserState(), Res.MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR, ctorType.getArgSignature(), expectedArgs, iArgs );
      pe.setParamTypesExpected( ctorType.getParameterTypes() );
      pe.setCausedByArgumentList( true );
      element.addParseException( pe );
    }
  }

  private void parsePlainFunction( IFunctionSymbol functionSymbol )
  {
    MethodCallExpression e = new MethodCallExpression();

    e.setFunctionSymbol( functionSymbol );
    IFunctionType funcType = (IFunctionType)functionSymbol.getType();
    e.setType( funcType.getReturnType() );

    IType[] argTypes = funcType.getParameterTypes();

    boolean bNoArgsProvided;
    if( !(bNoArgsProvided = match( null, ')' )) || funcType.hasOptionalParams() )
    {
      verify( e, argTypes != null && argTypes.length > 0, Res.MSG_NO_ARGUMENTS, functionSymbol.getName() );

      MethodScore score = parseArgumentList2( e, Collections.singletonList( funcType ), null, true, bNoArgsProvided );
      if( score.isValid() )
      {
        List<IExpression> scoreArgs = score.getArguments();
        verifyArgCount( e, scoreArgs.size(), funcType );
        //noinspection SuspiciousToArrayCall
        e.setArgs( scoreArgs.toArray( new Expression[scoreArgs.size()] ) );
        e.setNamedArgOrder( score.getNamedArgOrder() );
      }
      verify( e, bNoArgsProvided || match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
    }
    else
    {
      verify( e, argTypes == null || argTypes.length == 0, Res.MSG_EXPECTING_ARGS, functionSymbol.getName() );
      e.setArgs( null );
    }
    pushExpression( e );
  }

  private void parseDynamicFunction( Symbol dynamcSymbol )
  {
    MethodCallExpression e = new MethodCallExpression();

    e.setFunctionSymbol( dynamcSymbol );
    e.setType(dynamcSymbol.getType());

    if( !match( null, ')' ) )
    {
      MethodScore score = parseArgumentList2( e, Collections.singletonList(
              new FunctionType( dynamcSymbol.getName(), dynamcSymbol.getType(), IType.EMPTY_ARRAY ) ), null, true, false );
      List<IExpression> scoreArgs = score.getArguments();
      //noinspection SuspiciousToArrayCall
      e.setArgs( scoreArgs.toArray( new Expression[scoreArgs.size()] ) );
      e.setNamedArgOrder( score.getNamedArgOrder() );
      verify( e, match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
    }
    else
    {
      e.setArgs( null );
    }
    pushExpression( e );
  }

  private void parseMemberAccess( Expression rootExpression, MemberAccessKind kind, int iTokenStart, String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly )
  {
    parseMemberAccess( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, false );
  }

  private void parseMemberAccess( Expression rootExpression, MemberAccessKind kind, final int iTokenStart, final String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly, boolean createSynthesizedProperty )
  {
    BeanMethodCallExpression e = new BeanMethodCallExpression();
    IType rootType = rootExpression.getType();
    rootType = IGosuClass.ProxyUtil.isProxy( rootType ) && rootType instanceof IGosuClass ? ((IGosuClass) rootType).getJavaType() : rootType;
    boolean bExpansion = kind == MemberAccessKind.EXPANSION;
    rootType = bExpansion ? TypeLord.getExpandableComponentType( rootType ) : rootType;
    if( rootType != null && !rootType.isArray() )
    {
      boolean bAcceptableType =
              BeanAccess.isBeanType( rootType ) ||
                      rootType == GosuParserTypes.BOOLEAN_TYPE() ||
                      rootType == GosuParserTypes.STRING_TYPE() ||
                      rootType == GosuParserTypes.NUMBER_TYPE() ||
                      rootType instanceof IBlockType ||
                      rootType instanceof MetaType;
      verify( e, bAcceptableType, Res.MSG_EXPECTING_BEANTYPE, rootType.getName() );
    }

    IType[] typeParameters = null;
    try
    {
      if( !bParseTypeLiteralOnly && !(rootType instanceof ErrorType) && match( null, "<", SourceCodeTokenizer.TT_OPERATOR, true ) )
      {
        List<IFunctionType> list = new ArrayList<IFunctionType>();
        // if any function with the specified name is generic, parse parameterization
        getFunctionType( rootType, strMemberName, null, list, this, true );
        for( IFunctionType ftype : list )
        {
          if( ftype.isGenericType() )
          {
            typeParameters = parseFunctionParameterization( e );
            break;
          }
        }
      }
    }
    catch( ParseException pe )
    {
      e.addParseException( pe );
    }

    int iParenStart = _tokenizer.getTokenStart();
    int mark = _tokenizer.mark();
    if( !bParseTypeLiteralOnly && !isInSeparateStringTemplateExpression() && match( null, null, '(', true ) && !isBlockInvoke( rootExpression, strMemberName, rootType ) )
    {
      // Method call

      match( null, '(' );
      parseMethodMember( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, createSynthesizedProperty, e, rootType, bExpansion, typeParameters, iParenStart, mark);
    }
    else
    {
      // Property access

      parsePropertyMember( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, createSynthesizedProperty, rootType, bExpansion );
    }
  }

  private void parseMethodMember( Expression rootExpression, MemberAccessKind kind, int iTokenStart, String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly, boolean createSynthesizedProperty, BeanMethodCallExpression e, IType rootType, boolean bExpansion, IType[] typeParameters, int iParenStart, int mark )
  {
    int iLocationsCount = _locations.size();
    parseMethodMember( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, e, rootType, bExpansion, typeParameters, iParenStart );
    Expression expr = peekExpression();
    if( expr.hasParseExceptions() )
    {
      maybeOpenParenIsForParenthesizedExpr( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, createSynthesizedProperty, e, rootType, bExpansion, typeParameters, iParenStart, mark, iLocationsCount );
    }
  }

  private void maybeOpenParenIsForParenthesizedExpr( Expression rootExpression, MemberAccessKind kind, int iTokenStart, String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly, boolean createSynthesizedProperty, BeanMethodCallExpression e, IType rootType, boolean bExpansion, IType[] typeParameters, int iParenStart, int mark, int iLocationsCount )
  {
    if( !isOpenParenOnNextLine( mark ) )
    {
      return;
    }

    backtrack( mark, iLocationsCount );

    parsePropertyMember( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, createSynthesizedProperty, rootType, bExpansion );
    Expression expr = peekExpression();
    if( expr.hasParseExceptions() )
    {
      // Failed to parse as Property, reparse as Method call

      _tokenizer.restoreToMark(mark);
      expr = popExpression();
      removeInnerClasses( expr );
      removeLocationsFrom( iLocationsCount );

      parseMethodMember( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly, e, rootType, bExpansion, typeParameters, iParenStart );
    }
  }

  private boolean isOpenParenOnNextLine( int mark )
  {
    if( mark <= 0 )
    {
      return false;
    }
    Token priorMarkToken = _tokenizer.getTokenAt( mark - 1 );
    return priorMarkToken != null && priorMarkToken.getType() == ISourceCodeTokenizer.TT_WHITESPACE && priorMarkToken.getText().indexOf( '\n' ) >= 0;
  }

  private void parseMethodMember( Expression rootExpression, MemberAccessKind kind, int iTokenStart, String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly, BeanMethodCallExpression e, IType rootType, boolean bExpansion, IType[] typeParameters, int iParenStart )
  {
    e.setArgPosition( iParenStart + 1 );
    e.setRootExpression( rootExpression );
    IMethodInfo md = null;

    List<IFunctionType> listFunctionTypes = getPreliminaryFunctionTypes( strMemberName, e, rootType, typeParameters );
    boolean bNoArgsProvided;
    if( !(bNoArgsProvided = match( null, ')' )) ||
            (listFunctionTypes.size() == 1 && listFunctionTypes.get( 0 ).hasOptionalParams()) )
    {
      MethodScore methodScore = parseArgumentList2( e, listFunctionTypes, typeParameters, !(rootType instanceof ErrorType), bNoArgsProvided );

      //noinspection SuspiciousToArrayCall
      Expression[] eArgs = methodScore.getArguments().toArray( new Expression[methodScore.getArguments().size()] );
      e.setArgs( eArgs );

      if( methodScore.isValid() )
      {
        IFunctionType funcType = (IFunctionType)methodScore.getInferredFunctionType();
        verifyArgCount( e, eArgs.length, funcType );

        assert funcType != null;
        e.setType( (!bExpansion || funcType.getReturnType().isArray() || funcType.getReturnType() == JavaTypes.pVOID())
                ? funcType.getReturnType()
                : funcType.getReturnType().getArrayType() );
        e.setFunctionType( funcType );
        IType[] argTypes = funcType.getParameterTypes();
        e.setArgTypes( argTypes );
        IFunctionType rawFunctionType = (IFunctionType)methodScore.getRawFunctionType();
        md = rawFunctionType.getMethodInfo();
        if( !md.isVisible( getVisibilityConstraint() ) )
        {
          verify( e, false, Res.MSG_METHOD_NOT_VISIBLE, strMemberName );
        }
        //verify( md == null || !bMetaType || accessList.size() > 1 || md.isStatic(), strFunction + " cannot call non-static methods here." );
        e.setMethodDescriptor( bExpansion ? new ArrayExpansionMethodInfo( md ) : md );
        e.setMemberAccessKind( kind );
        e.setNamedArgOrder( methodScore.getNamedArgOrder() );
        verifyCase( e, strMemberName, funcType.getName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );
        verifySuperAccess( rootExpression, e, funcType.getMethodInfo(), strMemberName );
      }
      else
      {
        if( !(rootType instanceof ErrorType) && !e.hasParseException( Res.MSG_AMBIGUOUS_METHOD_INVOCATION ) )
        {
          verify( e, false, state, Res.MSG_NO_SUCH_FUNCTION, strMemberName );
        }
        e.setType( ErrorType.getInstance() );
      }

      e.setAccessPath( strMemberName );
      verify( e, bNoArgsProvided || match( null, ')' ), Res.MSG_EXPECTING_FUNCTION_CLOSE );
    }
    else
    {
      //No parameters were found
      if( rootType instanceof ErrorType )
      {
        e.setType( ErrorType.getInstance() );
        e.setArgTypes( IType.EMPTY_ARRAY );
        e.setAccessPath( strMemberName );
        md = null;
        e.setMethodDescriptor( md );
        e.setArgs( null );
      }
      else
      {
        IFunctionType funcType;
        try
        {
          funcType = getFunctionType( rootType, strMemberName, new Expression[0], null, this, true );
        }
        catch( ParseException pe )
        {
          // If this method call is a getXXX() call, we can try to convert it to a XXX property reference...
          String strPropertyName = getPropertyNameFromMethodName( strMemberName );
          if( strPropertyName == null )
          {
            try
            {
              funcType = getFunctionType( rootType, strMemberName, new Expression[0], null, this, false );
              e.addParseException( pe );
            }
            catch( ParseException e1 )
            {
              e.addParseException( pe );
              e.setType( ErrorType.getInstance() );
              e.setStartOffset( iTokenStart );
              pushExpression( e );
              return;
            }
          }
          else
          {
            parseMemberAccess( rootExpression, kind, iTokenStart, strPropertyName, state, bParseTypeLiteralOnly, true );
            Expression expression = peekExpression();
            if( expression instanceof MemberAccess ) {
              MemberAccess ma = (MemberAccess) expression;
              ma.setMethodNameForSyntheticAccess( strMemberName );
            }
            if( peekExpression().hasParseExceptions() )
            {
              e.addParseException( pe );
              e.addParseExceptions( peekExpression().getParseExceptions() );
              popExpression();
              e.setType( ErrorType.getInstance() );
              e.setStartOffset( iTokenStart );
              pushExpression( e );
            }
            return;
          }
        }
        if( typeParameters != null )
        {
          if( verifyCanParameterizeType( e, funcType, typeParameters ) )
          {
            IFunctionType parameterizedFuncType = (IFunctionType)funcType.getParameterizedType( typeParameters );
            if( verify( e, parameterizedFuncType != null, Res.MSG_COULD_NOT_PARAMETERIZE ) )
            {
              funcType = parameterizedFuncType;
            }
          }
        }

        assert funcType != null;
        if( isEndOfExpression() )
        {
          funcType = maybeParameterizeOnCtxType( funcType );
        }
        IFunctionType boundType = boundFunctionType( funcType );

        e.setType( (!bExpansion || funcType.getReturnType().isArray() || funcType.getReturnType() == JavaTypes.pVOID())
                ? boundType.getReturnType()
                : boundType.getReturnType().getArrayType() );
        e.setFunctionType( funcType );
        IType[] argTypes = funcType.getParameterTypes();
        e.setArgTypes( argTypes );
        e.setAccessPath( strMemberName );
        md = funcType.getMethodInfo();
        verify( e, md.isVisible( getVisibilityConstraint() ), Res.MSG_METHOD_NOT_VISIBLE, strMemberName );
        verify( e, !md.isAbstract() ||
                !(rootExpression instanceof Identifier) ||
                !((Identifier)rootExpression).getSymbol().getName().equals( Keyword.KW_super.toString() ) ||
                GosuClass.isObjectMethod( md ),
                Res.MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY, strMemberName );

        //verify( md == null || !bMetaType || accessList.size() > 1 || md.isStatic(), strFunction + " cannot call non-static methods here." );
        e.setMethodDescriptor( bExpansion ? new ArrayExpansionMethodInfo( md ) : md );
        e.setMemberAccessKind( kind );

        verifyCase( e, strMemberName, md.getDisplayName(), state, Res.MSG_FUNCTION_CASE_MISMATCH, false );
        verifyArgCount( e, 0, funcType );
        verifySuperAccess( rootExpression, e, funcType.getMethodInfo(), strMemberName );
        e.setArgs( null );
      }
    }
    e.setStartOffset( iTokenStart );
    if( md != null && md.isDeprecated() )
    {
      // Add a warning if the method is deprecated
      e.addParseWarning(
              new ParseWarningForDeprecatedMember( state.cloneWithNewTokenStartAndTokenEnd( iTokenStart, md.getDisplayName().length() ),
                      TypeInfoUtil.getMethodSignature( md ), e.getRootType().getName() ) );
    }
    if( isParsingAbstractConstructor() )
    {
      handleAbstractCtor( iTokenStart, strMemberName, e, state );
    }

    verifyNotCallingOverridableFunctionFromCtor( e );

    pushExpression( e );
  }

  private void parsePropertyMember( Expression rootExpression, MemberAccessKind kind, int iTokenStart, String strMemberName, LazyLightweightParserState state, boolean bParseTypeLiteralOnly, boolean createSynthesizedProperty, IType rootType, boolean bExpansion )
  {
    IPropertyInfo pi = null;

    MemberAccess ma = bExpansion
                      ? new MemberExpansionAccess()
                      : createSynthesizedProperty
                        ? new SynthesizedMemberAccess()
                        : new MemberAccess();
    ma.setRootExpression( rootExpression );
    ma.setMemberAccessKind(kind);
    IType memberType = null;

    try
    {
      if( rootType instanceof IFunctionType && rootExpression instanceof Identifier )
      {
        // Can't deref functions, convert to namespace if possible (this is to handle a bad
        // relative namespaace problem e.g., print.foo.SomeTime where print is a namespace in gw.api.print  ..yeeeaaah)
        Identifier identifier = (Identifier)rootExpression;
        INamespaceType namespaceType = resolveNamespace( identifier.getSymbol().getName() );
        if( namespaceType != null )
        {
          Symbol sym = new Symbol( namespaceType.getRelativeName(), namespaceType, null );
          identifier.setSymbol( sym, getSymbolTable() );
          identifier.setType( namespaceType );
          rootType = namespaceType;
        }
      }

      if( rootType instanceof INamespaceType )
      {
        if( !strMemberName.equals( "*" ) )
        {
          String strType = rootType.getName() + '.' + strMemberName;
          // First, try a sub-namespace
          memberType = TypeSystem.getNamespace( strType );
          if( memberType == null )
          {
            // Now try a fq type name
            memberType = resolveTypeName( strType, true );
            if( memberType == null )
            {
              memberType = resolveNamespace( strType );
            }
            else if( memberType != null )
            {
              String[] T = {strType};
              TypeLiteral tl = resolveTypeLiteral( T );
              resolveArrayOrParameterizationPartOfTypeLiteral( T, bParseTypeLiteralOnly, tl );
              tl.setPackageExpression( rootExpression );
              return;
            }
          }
        }
      }
      else if( rootExpression instanceof TypeLiteral )
      {
        // Try an inner class name
        IType typeLiteralType = ((MetaType)rootType).getType();
        if( typeLiteralType instanceof IHasInnerClass )
        {
          memberType = getInnerClass( strMemberName, memberType, (IHasInnerClass)typeLiteralType );
          if( memberType != null )
          {
            if( !shouldParseMemberInstead( strMemberName, rootType, memberType ) )
            {
              String[] T = new String[1];
              T[0] = memberType.getName();
              TypeLiteral tl = resolveTypeLiteral( T );
              resolveArrayOrParameterizationPartOfTypeLiteral( T, bParseTypeLiteralOnly, tl );
              verifyTypeAccessible( tl, memberType );
              tl.setPackageExpression( rootExpression );
              return;
            }
          }
        }
        else if( typeLiteralType instanceof ErrorType )
        {
          memberType = ErrorType.getInstance();
        }
      }

      if( memberType == null )
      {
        if( bParseTypeLiteralOnly && !(rootType instanceof ErrorType) )
        {
          if( rootType instanceof INamespaceType )
          {
            verify( ma, false, Res.MSG_NO_TYPE_ON_NAMESPACE, strMemberName, rootType == null ? "<no type specified>" : rootType.getName() );
          }
          else
          {
            IType errRootType = rootType instanceof IMetaType ? ((IMetaType)rootType).getType() : rootType;
            addError( ma, Res.MSG_INVALID_INNER_TYPE, strMemberName, TypeLord.getPureGenericType( errRootType ).getRelativeName() );
          }
          memberType = ErrorType.getInstance();
        }
        else
        {
          pi = BeanAccess.getPropertyInfo( rootType, strMemberName, null, this, getVisibilityConstraint() );
          memberType = bExpansion ? new ArrayExpansionPropertyInfo( pi ).getFeatureType() : pi.getFeatureType();

          if( pi != null )
          {
            verifyCase( ma, strMemberName, pi.getName(), state, Res.MSG_PROPERTY_CASE_MISMATCH, false );
          }

          if( pi.isStatic() && !JavaTypes.ITYPE().isAssignableFrom( rootType ) )
          {
            IType intrinsicType = rootExpression.getType();
            if( rootExpression instanceof Identifier &&
                    intrinsicType.getRelativeName().equals( ((Identifier)rootExpression).getSymbol().getName() ) )
            {
              warn( ma, false, Res.MSG_NON_STATIC_ACCESS_WITH_IDENTIFIER_OF_STATIC_MEMBER, pi.getName(), intrinsicType.getName(), ((Identifier)rootExpression).getSymbol().getName(), intrinsicType.getName() );
            }
            else
            {
              warn( ma, false, Res.MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER, pi.getName(), intrinsicType.getName() );
            }
          }
        }
      }
    }
    catch( ParseException e1 )
    {
//        memberType = ma.getRootType();

      if( rootExpression instanceof Identifier && !(rootType instanceof INamespaceType) )
      {
        // This is to handle yet another bad relative namespaace problem e.g., new activity.ActivityDetailHelper(Activity)
        Identifier identifier = (Identifier)rootExpression;
        INamespaceType namespaceType = resolveNamespace( identifier.getSymbol().getName().toLowerCase() );
        if( namespaceType != null )
        {
          ISymbol oldSymbol = identifier.getSymbol();
          Symbol sym = new Symbol( namespaceType.getRelativeName(), namespaceType, null );
          identifier.setSymbol( sym, getSymbolTable() );
          identifier.setType( namespaceType );
          parseMemberAccess( rootExpression, kind, iTokenStart, strMemberName, state, bParseTypeLiteralOnly );
          Expression namespaceExpr = peekExpression();
          if( namespaceExpr.hasParseExceptions() )
          {
            ((Identifier)rootExpression).setSymbol( oldSymbol, getSymbolTable() );
            rootExpression.setType( oldSymbol.getType() );
          }
          else
          {
            return;
          }
        }
      }

      if( rootType instanceof INamespaceType )
      {
        verify( ma, false, Res.MSG_NO_TYPE_ON_NAMESPACE, strMemberName, rootType == null ? "<no type specified>" : rootType.getName() );
      }
      else
      {
        ma.addParseException( e1 );
      }
      memberType = ErrorType.getInstance();
    }
    ma.setType( memberType );
    ma.setMemberName( strMemberName );
    ma.setStartOffset( iTokenStart );
    if( pi != null && pi.isDeprecated() )
    {
      // Add a warning if the property is deprecated
      ma.addParseWarning( new ParseWarningForDeprecatedMember( state.cloneWithNewTokenStartAndTokenEnd( iTokenStart, pi.getName().length() ),
              pi.getName(), ma.getRootType().getName() ) );
    }
    if( pi != null )
    {
      verify( ma, !pi.isAbstract() ||
              !(rootExpression instanceof Identifier) ||
              !((Identifier)rootExpression).getSymbol().getName().equals( Keyword.KW_super.toString() ),
              Res.MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY, strMemberName );
    }
    verifySuperAccess( rootExpression, ma, pi, strMemberName );
    pushExpression( ma );
  }

  private void verifySuperAccess( Expression rootExpression, Expression memberExpr, IAttributedFeatureInfo feature, String strMemberName )
  {
    if( feature == null )
    {
      return;
    }
    verify( memberExpr, !(rootExpression instanceof SuperAccess) || !feature.isAbstract(),
            Res.MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY, strMemberName );
  }

  private boolean shouldParseMemberInstead( String strMemberName, IType rootType, IType memberType )
  {
    IType ctxType = getOwner().getContextType().getType();
    if( !(ctxType instanceof IMetaType) )
    {
      if( memberType != null )
      {
        try
        {
          BeanAccess.getPropertyInfo( rootType, strMemberName, null, this, getVisibilityConstraint() );
          // The case exists where both an inner class and a member share the same simple name.
          // Favor the member parse when the context type is not a MetaType
          return true;
        }
        catch( Exception e )
        {
          // eat me
        }
      }
    }
    return false;
  }

  private boolean isEndOfArgExpression()
  {
    int mark = _tokenizer.mark();
    try
    {
      return match( null, ')' ) && isEndOfExpression();
    }
    finally
    {
      _tokenizer.restoreToMark( mark );
    }
  }

  private boolean isParenthesisTerminalExpression()
  {
    return isParenthesisTerminalExpression( false );
  }
  private boolean isParenthesisTerminalExpression( boolean bMatchOpeningParen )
  {
    int mark = _tokenizer.mark();
    try
    {
      if( bMatchOpeningParen )
      {
        boolean b = match(null, '(');
        assert b;
      }
      eatBlock( '(', ')', false );
      return isEndOfExpression();
    }
    finally
    {
      _tokenizer.restoreToMark( mark );
    }
  }

  private boolean isEndOfExpression()
  {
    return !(match( null, null, '.', true ) ||
             match( null, "?.", SourceCodeTokenizer.TT_OPERATOR, true ) ||
             match( null, "*.", SourceCodeTokenizer.TT_OPERATOR, true ));
  }

  private IType getInnerClass( String strMemberName, IType memberType, IHasInnerClass typeLiteralType )
  {
    try
    {
      memberType = typeLiteralType.getInnerClass( strMemberName );
    }
    catch( IllegalStateException e1 )
    {
      //Ignore, JavaType.getInnerClass throws IllegalStateException if unable to get type for inner class
    }
    return memberType;
  }

  private IFunctionType maybeParameterizeOnCtxType( IFunctionType funcType )
  {
    if( funcType.isGenericType() )
    {
      //## todo: Should we do this during method scoring?
      funcType = funcType.inferParameterizedTypeFromArgTypesAndContextType( IType.EMPTY_ARRAY, getContextType().getType() );
    }
    return funcType;
  }

  private List<IFunctionType> getPreliminaryFunctionTypes( String strMemberName, BeanMethodCallExpression e, IType rootType, IType[] typeParameters )
  {
    // Get a preliminary funcTypes to check arguments. Note we do this to aid in in error feedback and value popup completion.
    List<IFunctionType> listFunctionTypes = new ArrayList<IFunctionType>( 8 );
    try
    {
      if( !(rootType instanceof ErrorType) )
      {
        getFunctionType( rootType, strMemberName, null, listFunctionTypes, this, true );
      }
    }
    catch( ParseException pe )
    {
      e.addParseException( pe );
    }

    if( typeParameters != null )
    {
      listFunctionTypes = parameterizeFunctionTypes( e, typeParameters, listFunctionTypes );
    }
    return listFunctionTypes;
  }

  private boolean isBlockInvoke( Expression rootExpression, String strMemberName, IType rootType )
  {
    if( rootExpression instanceof TypeLiteral )
    {
      // Don't look up inner classes

      IType typeLiteralType = ((MetaType)rootType).getType();
      if( typeLiteralType instanceof IHasInnerClass )
      {
        if( ((IHasInnerClass)typeLiteralType).getInnerClass( strMemberName ) != null )
        {
          return false;
        }
      }
    }

    if( !isErrorType( rootType ) && !(rootType instanceof INamespaceType) && !(rootType instanceof IFunctionType) )
    {
      IPropertyInfo pi = BeanAccess.getPropertyInfo_NoException( rootType, strMemberName, null, this, getVisibilityConstraint() );
      if( pi != null )
      {
        return pi.getFeatureType() instanceof IBlockType;
      }
    }
    return false;
  }

  private boolean isErrorType(IType rootType) {
    return
            rootType instanceof IErrorType ||
                    (rootType instanceof IMetaType && ((IMetaType) rootType).getType() instanceof IErrorType);
  }

  private ArrayList<IFunctionType> parameterizeFunctionTypes( Expression expression, IType[] typeParameters, List<IFunctionType> listFunctionTypes )
  {
    ArrayList<IFunctionType> parameterizedFunctionTypes = new ArrayList<IFunctionType>( 8 );
    for( IFunctionType funcType : listFunctionTypes )
    {
      if( funcType.isGenericType() && verifyCanParameterizeType( expression, funcType, typeParameters ) )
      {
        IFunctionType parameterizedFuncType = (IFunctionType)funcType.getParameterizedType( typeParameters );
        if( verify( expression, parameterizedFuncType != null, Res.MSG_COULD_NOT_PARAMETERIZE ) )
        {
          if( parameterizedFuncType != null )
          {
            parameterizedFunctionTypes.add( parameterizedFuncType );
          }
        }
      }
    }
    return parameterizedFunctionTypes;
  }

  private void handleAbstractCtor( final int iTokenStart, final String strMemberName, BeanMethodCallExpression e, final IParserState state )
  {
    IMethodInfo methodInfo = e.getMethodDescriptor();
    if (methodInfo instanceof GosuMethodInfo ) {
      if (methodInfo.isAbstract()) {
        //noinspection ThrowableInstanceNeverThrown
        e.addParseException(new ParseException(new IParserState() {
          public int getLineNumber() {
            return state.getLineNumber();
          }

          public int getTokenColumn() {
            return state.getTokenColumn();
          }

          public String getSource() {
            return state.getSource();
          }

          public int getTokenStart() {
            return iTokenStart;
          }

          public int getTokenEnd() {
            return iTokenStart + strMemberName.length();
          }

          public int getLineOffset() {
            return state.getLineOffset();
          }

          @Override
          public IParserState cloneWithNewTokenStartAndTokenEnd( int newTokenStart, int newLength ) {
            return null;
          }
        },
                Res.MSG_NO_ABSTRACT_METHOD_CALL_IN_CONSTR,
                methodInfo.getDisplayName()));
      }
    }
  }

  private boolean verifyCanParameterizeType( ParsedElement elem, IType type, IType[] typeParam )
  {
    if( !verify( elem, type.isGenericType(), Res.MSG_CANNOT_PARAMETERIZE_NONGENERIC ) )
    {
      return false;
    }

    ICompilableTypeInternal gsClass = getGosuClass();
    if( gsClass instanceof IGosuClass && !((IGosuClass)gsClass).isHeaderCompiled() )
    {
      return true;
    }

    IGenericTypeVariable[] typeVars = type.getGenericTypeVariables();
    if( verify( elem, typeParam != null && typeParam.length == typeVars.length, Res.MSG_WRONG_NUM_OF_ARGS, "" ) )
    {
      assert typeParam != null;
      boolean bRet = true;
      TypeVarToTypeMap typeVarToTypeMap = new TypeVarToTypeMap();
      for( int i = 0; i < typeVars.length; i++ )
      {
        if( typeVars[i].getTypeVariableDefinition() != null )
        {
          typeVarToTypeMap.put( typeVars[i].getTypeVariableDefinition().getType(), typeParam[i] );
        }

        IType boundingType = typeVars[i].getBoundingType();
        boundingType = TypeLord.getActualType( boundingType, typeVarToTypeMap, true );

        bRet = bRet &&
                verify( elem,

                        // Hack to support recursive types
                        isTypeParamHeaderCompiling( typeParam[i] ) ||
                                isErrorType( typeParam[i] ) ||
                                (typeVars[i].getTypeVariableDefinition() != null && typeVars[i].getTypeVariableDefinition().getType().isAssignableFrom( typeParam[i] )) ||
                                boundingType.isAssignableFrom( typeParam[i] ) ||
                                boundingType instanceof IGosuClass && ((IGosuClass)boundingType).isStructure() && StandardCoercionManager.isStructurallyAssignable( TypeLord.getPureGenericType( boundingType ), typeParam[i] )
                        ,
                        Res.MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO,
                        typeParam[i], boundingType );
      }
      return bRet;
    }
    return false;
  }

  private boolean isTypeParamHeaderCompiling( IType typeParam )
  {
    return (typeParam instanceof IGosuClass && ((IGosuClass)typeParam).isCompilingHeader()) ||
            (typeParam instanceof TypeVariableType && isTypeParamHeaderCompiling( ((TypeVariableType)typeParam).getBoundingType() )) ||
            (typeParam instanceof TypeVariableType && ((TypeVariableType)typeParam).getBoundingType() == PENDING_BOUNDING_TYPE);
  }

  private IType[] parseFunctionParameterization( Expression e )
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    if( match( null, "<", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      List<TypeLiteral> paramTypes = parseTypeParameters( null );
      verify( e, match( null, ">", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE );
      if( paramTypes.isEmpty() )
      {
        return null;
      }
      makeTypeParameterListClause( iOffset, iLineNum, iColumn, paramTypes );
      IType[] types = new IType[paramTypes.size()];
      for( int i = 0; i < paramTypes.size(); i++ )
      {
        TypeLiteral typeLiteral = paramTypes.get( i );
        types[i] = (typeLiteral.getType()).getType();
      }
      return types;
    }
    return null;
  }

  private MethodScore parseArgumentList2( ParsedElement element, List<? extends IInvocableType> listFunctionTypes,
                                          IType[] typeParams, boolean bVerifyArgs, boolean bNoArgsProvided )
  {
    // Avoid *nested* method call scoring -- it incurs exponential complexity.
    //
    // If the current method call is itself an argument to another method which is scoring,
    // this parse tree is going to get thrown away, thus the arguments in this method call
    // are of no consequence, only the method call's return type is meaningful, so no
    // need to score this call, just grab the first function in the list, parse the args, and bail.
    listFunctionTypes = maybeAvoidNestedMethodScoring( listFunctionTypes );

    boolean bShouldScoreMethods = listFunctionTypes.size() > 1;
    List<MethodScore> scoredMethods = new ArrayList<MethodScore>();

    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = _tokenizer.getTokenColumn();

    listFunctionTypes = maybeRemoveNonGenericMethods( listFunctionTypes, typeParams );

    TypeVarToTypeMap inferenceMap = new TypeVarToTypeMap();
    List<Integer> namedArgOrder = null;
    Set<String> namedArgs = new HashSet<String>();

    int mark = getTokenizer().mark();
    int iLocationsCount = _locations.size();

    MethodScore score = null;
    List<Expression> argExpressions = null;
    for( int i = 0; i < listFunctionTypes.size() || (i == 0 && listFunctionTypes.isEmpty()); i++ )
    {
      int iArgs = 0;
      argExpressions = new ArrayList<Expression>( 4 );
      List<LightweightParserState> parserStates = new ArrayList<LightweightParserState>( 4 );

      IInvocableType funcType = listFunctionTypes.isEmpty() ? null : listFunctionTypes.get( i );
      maybeInferFunctionTypeVarsFromReturnType( funcType, inferenceMap );
      pushTypeVariableTypesToInfer( funcType );
      try
      {
        if( !bNoArgsProvided )
        {
          if( score != null )
          {
            backtrackArgParsing( mark, iLocationsCount, (List)score.getArguments() );
          }
          do
          {
            parserStates.add( makeLightweightParserState() );

            int iArgPos = parseArgExpression( funcType, iArgs, argExpressions, inferenceMap, parserStates, namedArgs, bShouldScoreMethods || getContextType().isMethodScoring()/* avoid nested scoring */ );
            namedArgOrder = assignArgExprPosition( listFunctionTypes, iArgs, namedArgOrder, iArgPos );
            iArgs++;
          }
          while( match( null, ',' ) );
        }

        // Extend the args list with default (or empty) values
        addMisingArgsWithDefaultValues( element, funcType, argExpressions, parserStates, bShouldScoreMethods );

        if( !bVerifyArgs )
        {
          score = new MethodScore();
          score.setValid( false );
          //noinspection unchecked
          score.setArguments( (List)argExpressions );
          score.setNamedArgOrder( namedArgOrder );
          if( i == listFunctionTypes.size() - 1 )
          {
            maybeReassignOffsetForArgumentListClause( iArgs, argExpressions, iOffset, iLineNum, iColumn );
          }
          return score;
        }
        else if( isDynamicMethod( listFunctionTypes ) )
        {
          if( i == listFunctionTypes.size() - 1 )
          {
            maybeReassignOffsetForArgumentListClause( iArgs, argExpressions, iOffset, iLineNum, iColumn );
          }
          return makeDynamicMethodScore( listFunctionTypes, argExpressions );
        }

        score = scoreMethod( funcType, listFunctionTypes, argExpressions, !bShouldScoreMethods, !hasContextSensitiveExpression( argExpressions ) );
      }
      finally
      {
        if( funcType != null )
        {
          popInferringFunctionTypeVariableTypes();
        }
      }
      //noinspection unchecked
      score.setArguments( (List)argExpressions );
      score.setParserStates( parserStates );
      scoredMethods.add( score );

      if( score.getScore() == 0 && !hasContextSensitiveExpression( argExpressions ) )
      {
        // perfect score, no need to continue

        //## todo: this should not happen, we need to change the key for the scored method cache to be type *hierarchy* sensitive
        // e.g., overloaded methods can have different enclosing types, but basically in the same hierarchy; for enhancements we should use the enhanced type, for example.
        if( scoredMethods.size() > 1 )
        {
          scoredMethods.clear();
          scoredMethods.add( score );
        }

        break;
      }
    }

    if( scoredMethods.size() > 1 )
    {
      Collections.sort( scoredMethods );
    }

    if( scoredMethods.size() > 1 )
    {
      scoredMethods = factorInParseErrors( scoredMethods );
      if( scoredMethods.size() > 1 )
      {
        // Check for ambiguity, quit with an error if so
        MethodScore score0 = scoredMethods.get( 0 );
        MethodScore score1 = scoredMethods.get( 1 );
        if( score0.getScore() == score1.getScore() && score0.matchesArgSize() )
        {
          addError( element, Res.MSG_AMBIGUOUS_METHOD_INVOCATION );
          score0.setValid( false );
          return score0;
        }
      }
    }

    if( scoredMethods.size() > 0 )
    {
      // Scores are sorted, first one has best score
      MethodScore bestScore = scoredMethods.get( 0 );

      if( listFunctionTypes.isEmpty() )
      {
        bestScore.setValid( false );
        return bestScore;
      }

      IInvocableType rawFunctionType = bestScore.getRawFunctionType();
      pushTypeVariableTypesToInfer( rawFunctionType );
      try
      {
        if( bShouldScoreMethods )
        {
          // Reparse with correct funcType context and get out of dodge
          backtrackArgParsing( mark, iLocationsCount, argExpressions );
          MethodScorer.instance().putCachedMethodScore( bestScore );
          return parseArgumentList2( element, Arrays.asList( bestScore.getRawFunctionType() ), typeParams, bVerifyArgs, bNoArgsProvided );
        }

        maybeReassignOffsetForArgumentListClause( argExpressions.size(), argExpressions, iOffset, iLineNum, iColumn );

        // Infer the function type
        IInvocableType inferredFunctionType = inferFunctionType( rawFunctionType, bestScore.getArguments(), isEndOfArgExpression(), inferenceMap );

        if( !getContextType().isMethodScoring() )
        {
          // Only do the following if this is *not* a *nested* method scoring call i.e., if the current method call is an argument to another method which is scoring,
          // this parse tree is going to get thrown away, the arguments in this method call are of no consequence, only the method call's return type is meaningful,
          // so no need to apply implicit coercions etc.

          // reverify args
          verifyArgTypes( inferredFunctionType.getParameterTypes(), (List)bestScore.getArguments(), bestScore.getParserStates() );

          if( bestScore.isValid() )
          {
            // if the bestScore is valid, bound infered variables to avoid them getting out as raw type variables
            inferredFunctionType = maybeBoundFunctionTypeVars( inferredFunctionType, inferenceMap );

            // Some args may need implicit coercions applied
            handleImplicitCoercionsInArgs( inferredFunctionType.getParameterTypes(),
                    rawFunctionType.getParameterTypes(),
                    (List)bestScore.getArguments() );
          }
        }

        //noinspection unchecked
        bestScore.setArguments( (List)bestScore.getArguments() );
        bestScore.setInferredFunctionType( inferredFunctionType );
        bestScore.setNamedArgOrder( namedArgOrder );
        return bestScore;
      }
      finally
      {
        popInferringFunctionTypeVariableTypes();
      }
    }
    else
    {
      MethodScore errScore = new MethodScore();
      errScore.setValid( false );
      errScore.setArguments( Collections.<IExpression>emptyList() );
      return errScore;
    }
  }

  private void maybeInferFunctionTypeVarsFromReturnType( IInvocableType invType, TypeVarToTypeMap inferenceMap )
  {
    if( !(invType instanceof IFunctionType) )
    {
      return;
    }
    IFunctionType funcType = (IFunctionType)invType;
    if( funcType.isGenericType() &&
        TypeLord.hasTypeVariable( funcType.getReturnType() ) &&
        !getContextType().isMethodScoring() &&
        getContextType().getType() != null &&
        getContextType() != ContextType.EMPTY &&
        (getContextType().getUnboundType() == null || !boundCtxType( getContextType().getUnboundType() ).equals( getContextType().getType() )) ) // no sense in inferring type OUT from default type
    {
      if( isParenthesisTerminalExpression() )
      {
        // Note we must infer in "reverse" because the context type flows INTO the return type
        // For example,
        //    var list: List<String> = Lists.newArrayList( FooJava.filter( {""}, FooJava.not( \ r -> r.Alpha ) ) )
        // The context type, List<String>, can infer type var of Lists.newArrayList() by way of its return type, ArrayList<E>.
        // But the inference relationship is reversed, instead of infering from right-to-left, we infer left-to-right, hence the "Reverse" call here:
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType_Reverse( funcType.getReturnType(), getContextType().getType(), inferenceMap );
      }
    }
  }

  private List<MethodScore> factorInParseErrors( List<MethodScore> scoredMethods )
  {
    List<MethodScore> factored = new ArrayList<MethodScore>( scoredMethods.size() );
    List<MethodScore> noErrors = new ArrayList<MethodScore>( scoredMethods.size() );
    long bestScore = -1;
    for( MethodScore score : scoredMethods )
    {
      boolean bErrors = false;
      for( IExpression arg: score.getArguments() )
      {
        if( arg.hasParseExceptions() )
        {
          // change the score to reflect errors
          score.incScore( 1 );
          bErrors = true;
        }
      }
      if( !bErrors )
      {
        noErrors.add( score );
      }
      // determine best score among the ambiguous calls
      bestScore = bestScore >= 0 ? Math.min( bestScore, score.getScore() ) : score.getScore();
    }
    if( noErrors.size() > 0 )
    {
      // favor non-errant calls
      return noErrors;
    }
    for( MethodScore score : scoredMethods )
    {
      if( score.getScore() == bestScore )
      {
        factored.add( score );
      }
    }
    return factored;
  }

  private boolean hasContextSensitiveExpression( List<Expression> argExpressions )
  {
    for( Expression e : argExpressions )
    {
      if( e instanceof IInferredNewExpression ||
          e instanceof UnqualifiedEnumMemberAccess ||
          isGenericMethodCall( e ) ||
          e instanceof Identifier && e.hasParseExceptions() )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isGenericMethodCall( Expression e )
  {
    if( e instanceof MethodCallExpression )
    {
      IFunctionType functionType = ((MethodCallExpression) e).getFunctionType();
      return functionType != null && (functionType.isGenericType() || functionType.isParameterizedType());
    }
    return false;
  }

  private List<? extends IInvocableType> maybeAvoidNestedMethodScoring( List<? extends IInvocableType> listFunctionTypes )
  {
    if( listFunctionTypes.size() < 2 )
    {
      return listFunctionTypes;
    }

    if( !getContextType().isMethodScoring() )
    {
      // Not nested in a method score
      return listFunctionTypes;
    }

    IType retType = null;
    for( IInvocableType funcType: listFunctionTypes )
    {
      if( !(funcType instanceof IFunctionType) )
      {
        return listFunctionTypes;
      }

      IType csr = ((IFunctionType)funcType).getReturnType();
      if( retType != null && csr != retType )
      {
        // functions have different return types, must score methods here to find correct type for enclosing method score
        return listFunctionTypes;
      }
      retType = csr;
    }

    // Return types are all the same, no need to perform nested method scoring, so only parse against one of them
    return Collections.singletonList( listFunctionTypes.get( 0 ) );
  }

  private void backtrackArgParsing( int mark, int iLocationsCount, List<Expression> argExpressions )
  {
    for( int i = argExpressions.size()-1; i >= 0; i-- )
    {
      backtrack( mark, iLocationsCount, argExpressions.get( i ) );
    }
  }

  private List<? extends IInvocableType> maybeRemoveNonGenericMethods( List<? extends IInvocableType> listFunctionTypes, IType[] typeParams )
  {
    // if there were type parameters, remove any non-generic functions
    if( typeParams != null && typeParams.length > 0 )
    {
      ArrayList<IInvocableType> genericFunctions = new ArrayList<IInvocableType>();
      for( IInvocableType type : listFunctionTypes )
      {
        if( type.isGenericType() )
        {
          genericFunctions.add( type );
        }
      }
      if( !genericFunctions.isEmpty() )
      {
        listFunctionTypes = genericFunctions;
      }
    }
    return listFunctionTypes;
  }

  private void maybeReassignOffsetForArgumentListClause( int iArgs, List<Expression> argExpressions, int iOffset, int iLineNum, int iColumn )
  {
    boolean noLocations = true;
    if( iArgs > 0 )
    {
      // Maybe reassign offset for ArgumentListClause...
      for( Expression argExpr: argExpressions )
      {
        if( argExpr.getLocation() == null )
        {
          continue;
        }
        noLocations = false;
        int iExpressionOffset = argExpr.getLocation().getOffset();
        if( iExpressionOffset < iOffset )
        {
          // Can happen if first arg is a NotAWordExpression in which case the expr's length is 0 and its offset is the
          // previous token's ending offset, which will likely be less than the first token in the arg position's offset.
          iOffset = iExpressionOffset;
          iLineNum = argExpr.getLocation().getLineNum();
          iColumn = argExpr.getLocation().getColumn();
        }
      }
      if(!noLocations)
      {
        ArgumentListClause e = new ArgumentListClause();
        pushExpression( e );
        setLocation( iOffset, iLineNum, iColumn, true );
        popExpression();
      }
    }
  }

  private IInvocableType maybeBoundFunctionTypeVars( IInvocableType inferredFunctionType, TypeVarToTypeMap inferenceMap )
  {
    List<IType> types = new ArrayList<IType>();
    for( IType typeVarType : getCurrentlyInferringFunctionTypeVars() )
    {
      if( inferenceMap.get( (ITypeVariableType)typeVarType ) == null )
      {
        IType encType = TypeLord.getPureGenericType( typeVarType.getEnclosingType() );
        if( encType != null && TypeLord.getPureGenericType( inferredFunctionType ).isAssignableFrom( typeVarType.getEnclosingType() ) )
        {
          types.add( typeVarType );
        }
      }
    }
    return (IInvocableType) TypeLord.boundTypes( inferredFunctionType, types );
  }

  private List<Integer> assignArgExprPosition( List<? extends IInvocableType> listFunctionTypes, int iArgs, List<Integer> namedArgOrder, int iArgPos )
  {
    if( (namedArgOrder != null || iArgPos != iArgs) && listFunctionTypes.size() > 0 )
    {
      if( namedArgOrder == null )
      {
        int iSize = listFunctionTypes.get( 0 ).getParameterTypes().length;
        namedArgOrder = new ArrayList<Integer>( iSize );
        for( int i = 0; i < iSize; i++ )
        {
          namedArgOrder.add( i );
        }
      }
      namedArgOrder.remove( (Integer)iArgPos );
      if( namedArgOrder.size() >= iArgs ) {
        namedArgOrder.add( iArgs, iArgPos );
      }
      else {
        namedArgOrder.add( iArgPos );
      }
    }
    return namedArgOrder;
  }

  void addMisingArgsWithDefaultValues( ParsedElement element, IInvocableType funcType, List<Expression> argExpressions, List<LightweightParserState> parserStates, boolean bShouldScoreMethods )
  {
    if( funcType != null && funcType.hasOptionalParams() )
    {
      for( int i = argExpressions.size(); i < funcType.getParameterTypes().length; i++ )
      {
        if( parserStates != null )
        {
          parserStates.add( makeLightweightParserState() );
        }
        argExpressions.add( i, getDefaultValueOrPlaceHolderForParam( i, funcType ) );
      }

      if( !bShouldScoreMethods )
      {
        for( Expression argExpr : argExpressions )
        {
          if( !verify( element, argExpr != DefaultParamValueLiteral.instance(), Res.MSG_MISSING_REQUIRED_ARGUMENTS ) )
          {
            break;
          }
        }
      }
    }
  }

  private int parseArgExpression( IInvocableType funcType,
                                  int iArgs,
                                  List<Expression> argExpressions,
                                  TypeVarToTypeMap inferenceMap,
                                  List<LightweightParserState> parserStates,
                                  Set<String> namedArgs,
                                  boolean bMethodScoring )
  {
    pushTypeVariableTypesToInfer( funcType );
    try
    {
      IType rawCtxType;
      IType boundCtxType;

      boolean bError_AnonymousArgFollowsNamedArg = false;
      int iArgPos = iArgs;
      boolean bAlreadyDef = false;
      IType[] paramTypes = funcType == null ? IType.EMPTY_ARRAY : funcType.getParameterTypes();
      IBlockType retainTypeVarsCtxType = null;
      if( match( null, ":", ISourceCodeTokenizer.TT_OPERATOR, true ) )
      {
        iArgPos = parseNamedParamExpression( funcType, bMethodScoring );
        if( iArgPos == -1 )
        {
          namedArgs.add( "err" );
        }
        else if( funcType != null )
        {
          String[] parameterNames = funcType.getParameterNames();
          bAlreadyDef = namedArgs.add( parameterNames[iArgPos] );
        }

        if( argExpressions.size() < iArgPos+1 )
        {
          // Extend the args list with default (or empty) values up to, but not including, the newly parsed arg
          for( int i = argExpressions.size(); i < iArgPos; i++ )
          {
            argExpressions.add( i, getDefaultValueOrPlaceHolderForParam( i, funcType ) );
            parserStates.add( i, makeLightweightParserState() );
          }
          assert argExpressions.size() == iArgPos;
        }
      }
      else if( !namedArgs.isEmpty() )
      {
        bError_AnonymousArgFollowsNamedArg = true;
      }

      IType ctxType = iArgPos < 0 ? ErrorType.getInstance() : iArgPos < paramTypes.length ? paramTypes[iArgPos] : null;
      ctxType = ctxType == null ? useDynamicTypeIfDynamicRoot( funcType, ctxType ) : ctxType;
      rawCtxType = ctxType == null ? null : inferArgType( ctxType, inferenceMap );
      if( ctxType == null )
      {
        boundCtxType = null;
      }
      else if( rawCtxType.isGenericType() && !rawCtxType.isParameterizedType() )
      {
        boundCtxType = TypeLord.getDefaultParameterizedType( rawCtxType );
      }
      else
      {
        boundCtxType = boundCtxType( rawCtxType );
        if( rawCtxType instanceof IBlockType )
        {
          retainTypeVarsCtxType = (IBlockType)boundCtxType( rawCtxType, true );
        }
      }
      ContextType ctx = retainTypeVarsCtxType != null
                        ? ContextType.makeBlockContexType( ctxType, retainTypeVarsCtxType, bMethodScoring )
                        : new ContextType( boundCtxType, ctxType, bMethodScoring );

      parseExpressionNoVerify( ctx );
      Expression expression = popExpression();

      verify( expression, !bError_AnonymousArgFollowsNamedArg, Res.MSG_EXPECTING_NAMED_ARG );

      inferFunctionTypeVariables( rawCtxType, boundCtxType, expression.getType(), inferenceMap );
      if( retainTypeVarsCtxType != null )
      {
        IType actualType = TypeLord.getActualType( expression.getType(), inferenceMap, true );
        actualType = boundCtxType( actualType, false );
        expression.setType( actualType );
      }
      iArgPos = iArgPos < 0 ? iArgs : iArgPos;
      if( iArgPos >= 0 )
      {
        if( iArgPos == argExpressions.size() )
        {
          argExpressions.add( iArgPos, expression );
        }
        else if( iArgPos >=0 && iArgPos < argExpressions.size() && bAlreadyDef )
        {
          Expression existingExpr = argExpressions.set( iArgPos, expression );
          verify( expression,
                  existingExpr == DefaultParamValueLiteral.instance() ||
                          existingExpr instanceof DefaultArgLiteral ||
                          existingExpr instanceof NullExpression,
                  Res.MSG_ARGUMENT_ALREADY_DEFINED );
        }
      }
      return iArgPos;
    }
    finally
    {
      if( funcType != null )
      {
        popInferringFunctionTypeVariableTypes();
      }
    }
  }

  private IType useDynamicTypeIfDynamicRoot( IInvocableType funcType, IType ctxType )
  {
    if( funcType instanceof FunctionType && ((FunctionType)funcType).getMethodInfo() != null )
    {
      IMethodInfo mi = ((FunctionType)funcType).getMethodInfo();
      if( mi.getOwnersType().isDynamic() )
      {
        ctxType = ((FunctionType)funcType).getReturnType();
      }
    }
    return ctxType;
  }

  private Expression getDefaultValueOrPlaceHolderForParam( int iParam, IInvocableType invType )
  {
    if( invType == null )
    {
      return DefaultParamValueLiteral.instance();
    }
    IExpression[] defValues = invType.getDefaultValueExpressions();
    if( defValues == null || defValues.length == 0 )
    {
      return DefaultParamValueLiteral.instance();
    }
    IExpression defValue = defValues[iParam];
    if( defValue != null )
    {
      return new DefaultArgLiteral( invType.getParameterTypes()[iParam], defValue );
    }
    return DefaultParamValueLiteral.instance();
  }

  private int parseNamedParamExpression( IInvocableType invType, boolean bMethodScoring )
  {
    match( null, ":", SourceCodeTokenizer.TT_OPERATOR );

    parseNamedParamIdentifier();
    Identifier identifier = (Identifier)popExpression();
    int[] iPos = {-1};
    IType type = getParamTypeFromParamName( invType, identifier.getSymbol().getName(), iPos );
    identifier.setType( type );
    verify( identifier, !(type instanceof ErrorType), Res.MSG_PARAM_NOT_FOUND );
    verify( identifier, match( null, "=", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_EQUALS_ASSIGN );

    return iPos[0];
  }

  private IType getParamTypeFromParamName( IInvocableType invType, String strParam, int[] iPos )
  {
    if( invType == null )
    {
      return ErrorType.getInstance();
    }
    String[] names = invType.getParameterNames();
    for( int i = 0; i < names.length; i++ )
    {
      if( names[i].equals( strParam ) )
      {
        iPos[0] = i;
        return invType.getParameterTypes()[i];
      }
    }
    return ErrorType.getInstance();
  }

  private void parseNamedParamIdentifier()
  {
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = _tokenizer.getTokenColumn();
    Token t = new Token();
    Identifier expr = new Identifier();
    verify( expr, match( t, SourceCodeTokenizer.TT_WORD ) ||
                  match( t, SourceCodeTokenizer.TT_KEYWORD ), Res.MSG_EXPECTING_NAME_PARAM );
    expr.setSymbol( new TypedSymbol( t._strValue, null, null, null, SymbolType.NAMED_PARAMETER ), null );
    pushExpression( expr );
    setLocation( iOffset, iLineNum, iColumn );
  }

  private boolean isDynamicMethod( List<? extends IInvocableType> listFunctionTypes )
  {
    if( listFunctionTypes == null || listFunctionTypes.isEmpty() )
    {
      return false;
    }

    IInvocableType invoType = listFunctionTypes.get( 0 );
    if( invoType instanceof FunctionType  )
    {
      IMethodInfo mi = ((FunctionType)invoType).getMethodInfo();
      if( mi != null && (mi.getOwnersType() instanceof IPlaceholder || mi instanceof DynamicMethodInfo) )
      {
        return true;
      }
    }
    return false;
  }

  private MethodScore makeDynamicMethodScore( List<? extends IInvocableType> listFunctionTypes, List<Expression> argExpressions )
  {
    MethodScore score = new MethodScore();
    score.setValid( true );
    //noinspection unchecked
    score.setArguments( (List)argExpressions );
    IMethodInfo mi = ((FunctionType)listFunctionTypes.get( 0 )).getMethodInfo();
    mi = ((ITypeInfo)mi.getContainer()).getMethod( mi.getName(), getTypes( argExpressions ).toArray( new IType[argExpressions.size()] ) );
    score.setInferredFunctionType( new FunctionType( mi ) );
    score.setRawFunctionType( score.getInferredFunctionType() );
    score.setScore( 1 );
    return score;
  }

  private MethodScore scoreMethod(IInvocableType funcType, List<? extends IInvocableType> listFunctionTypes, List<Expression> argExpressions, boolean bSimple, boolean bLookInCache) {
    List<IType> argTypes = new ArrayList<IType>( argExpressions.size() );
    for( Expression argExpression : argExpressions ) {
      argTypes.add( argExpression.getType() );
    }
    return MethodScorer.instance().scoreMethod( funcType, listFunctionTypes, argTypes, getCurrentlyInferringFunctionTypeVars(), bSimple, bLookInCache );
  }

  private IType boundCtxType( IType ctxType )
  {
    return boundCtxType( ctxType, false );
  }
  private IType boundCtxType( IType ctxType, boolean bKeepTypeVars )
  {
    List<IType> inferringTypes = getCurrentlyInferringFunctionTypeVars();
    return TypeLord.boundTypes( ctxType, inferringTypes, bKeepTypeVars );
  }

  private void inferFunctionTypeVariables( IType rawContextType, IType boundContextType, IType expressionType, TypeVarToTypeMap inferenceMap )
  {
    if( rawContextType != null && boundContextType != null )
    {
      ICoercer iCoercer = CommonServices.getCoercionManager().resolveCoercerStatically( boundContextType, expressionType );
      if( iCoercer instanceof IResolvingCoercer )
      {
        IType resolvedType = ((IResolvingCoercer)iCoercer).resolveType( rawContextType, expressionType );
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( rawContextType, resolvedType, inferenceMap );
      }
      else
      {
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( rawContextType, expressionType, inferenceMap );
      }
    }
  }

  private IType inferArgType( IType contextType, TypeVarToTypeMap inferenceMap )
  {
    TypeLord.addReferencedTypeVarsThatAreNotInMap( contextType, inferenceMap );
    return TypeLord.getActualType( contextType, inferenceMap, true );
  }

  private void handleImplicitCoercionsInArgs( IType[] argTypes, IType[] rawArgTypes, List<Expression> args )
  {
    for( int i = 0; i < argTypes.length && i < args.size(); i++ )
    {
      IType argType = argTypes[i];
      Expression expr = args.get( i );
      if( argType != rawArgTypes[i] &&
              (argType instanceof IGosuArrayClass || argType instanceof TypeVariableArrayType) &&
              !(rawArgTypes[i] instanceof IGosuArrayClass) )
      {
        // Special case for GosuArray -> JavaArray since a generic function can be inferred via structural equivalence
        // e.g., component types are assignable, but different array types on a component type may not be.
        argType = rawArgTypes[i];
      }
      if( !(expr instanceof DefaultParamValueLiteral) )
      {
        args.set( i, possiblyWrapWithImplicitCoercion( expr, argType ) );
      }
    }
  }

  // returns a list of lists of unique types at each argument position,
  private List<List<IType>> extractContextTypes( List<? extends IInvocableType>  funcTypes )
  {
    if( funcTypes != null )
    {
      ArrayList<List<IType>> returnList = new ArrayList<List<IType>>();
      for( IInvocableType funcType : funcTypes )
      {
        for( int i = 0; i < funcType.getParameterTypes().length; i++ )
        {
          IType paramType = funcType.getParameterTypes()[i];
          if( i >= returnList.size() )
          {
            returnList.add( new ArrayList<IType>() );
          }
          List<IType> paramTypeList = returnList.get( i );
          if( !paramTypeList.contains( paramType ) )
          {
            paramTypeList.add( paramType );
          }
        }
      }
      return returnList;
    }
    else
    {
      return Collections.emptyList();
    }
  }

  private void verifyArgTypes( IType[] argTypes, List<Expression> argExpressions, List<LightweightParserState> parserStates )
  {
    if( argTypes == null || argTypes.length == 0 || argExpressions == null || argExpressions.size() == 0 )
    {
      return;
    }

    for( int i = 0; i < argTypes.length && i < argExpressions.size(); i++ )
    {
      Expression e = argExpressions.get( i );
      LightweightParserState state = parserStates.get( i );

      // Adds any parse exceptions that may have been cleared during method scoring
      verifyComparable( argTypes[i], e, false, true, state );

      //Add a warning if a closure with a void return type is passed to a method expecting
      //a non-void return value
      if( argTypes[i] instanceof FunctionType && e.getType() instanceof FunctionType )
      {
        FunctionType expectedFunType = (FunctionType)argTypes[i];
        FunctionType foundFunType = (FunctionType)e.getType();
        if( expectedFunType.getReturnType() != GosuParserTypes.NULL_TYPE() && foundFunType.getReturnType() == GosuParserTypes.NULL_TYPE() )
        {
          warn( e, false, Res.MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE );
        }
      }

      if( e.hasParseExceptions() )
      {
        IParseIssue pe = e.getParseExceptions().get( 0 );
        if( pe.getExpectedType() == null )
        {
          pe.setExpectedType( argTypes[i] );
        }
      }
    }
  }


  //------------------------------------------------------------------------------
  // literal
  //   number
  //   string
  //   <type-literal>
  //
  void parseLiteral( Token token )
  {
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    _parseLiteral( token );
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseLiteral( Token token )
  {
    if( !parseNumberLiteral( token ) &&
        !parseRelativeFeatureLiteral( token ) &&
        !parseStringLiteral( token ) &&
        !parseCharLiteral( token ) &&
        !parseBooleanLiteral( token ) &&
        !parseNullLiteral( token ) &&
        !parseTypeLiteral( token ) )
    {
      Expression expr = popExpression();
      getLocationsList().remove( expr.getLocation() );
      NotAWordExpression notAWord = new NotAWordExpression();
      pushExpression( notAWord );
      verify( notAWord, false, Res.MSG_SYNTAX_ERROR );
      Token T = getTokenizer().getPriorToken();
      setLocation( T.getTokenEnd(), T.getLine(), T.getTokenColumn(), true );
    }
  }

  private boolean parseRelativeFeatureLiteral( Token token )
  {
    if( getGosuClass() != null &&
        SourceCodeTokenizer.TT_OPERATOR == token.getType() && "#".equals( token.getStringValue() ) )
    {
      Expression root = new TypeLiteral( getGosuClass() );
      pushExpression( root );
      if( parseFeatureLiteral( token, root ) )
      {
        return true;
      }
      popExpression();
    }
    return false;
  }

  private boolean parseNumberLiteral( Token token )
  {
    return parseNumberLiteral( token, false );
  }

  private boolean atNumberLiteralStart()
  {
    return match( null, null, SourceCodeTokenizer.TT_INTEGER, true ) || match( null, null, '.', true );
  }

  private boolean parseNumberLiteral( Token token, boolean negated )
  {
    if( Keyword.KW_NaN == token.getKeyword() )
    {
      getTokenizer().nextToken();
      pushExpression( NumericLiteral.NaN.get().copy() );
      return true;
    }
    else if( Keyword.KW_Infinity == token.getKeyword() )
    {
      getTokenizer().nextToken();
      pushExpression( NumericLiteral.INFINITY.get().copy() );
      return true;
    }

    int mark = getTokenizer().mark();
    Token T = new Token();
    if( match( T, SourceCodeTokenizer.TT_INTEGER ) )
    {
      String strValue = (negated ? "-" : "") + T._strValue;
      if( getNumericTypeFrom( strValue ) == null && match( null, '.' ) )
      {
        strValue = maybeStripTypeModifier( strValue, null );
        Token tmp = new Token();
        if( match( tmp, null, SourceCodeTokenizer.TT_INTEGER, true ) )
        {
          if( !isPrefixNumericLiteral(tmp._strValue) )
          {
            match( T, SourceCodeTokenizer.TT_INTEGER );
            strValue += '.';
            strValue += T._strValue;
          }
          else
          {
            strValue += ".0";
          }
        }
        else
        {
          match( T, SourceCodeTokenizer.TT_INTEGER );
          strValue += ".0";
        }
      }
      int lastPos = T.getTokenEnd();
      if( match( T, null, SourceCodeTokenizer.TT_WORD, true ) )
      {
        if( (lastPos >= T.getTokenStart()) && (T._strValue.charAt(0) == 'e' ||
                T._strValue.charAt(0) == 'E' ) )
        {
          match( T, SourceCodeTokenizer.TT_WORD );
          if( T._strValue.length() == 1 )
          {
            strValue += "e";
            if( match( T, "+", SourceCodeTokenizer.TT_OPERATOR ) ||
                    match( T, "-", SourceCodeTokenizer.TT_OPERATOR ) )
            {
              strValue += T._strValue;
            }
            if( match(  T, SourceCodeTokenizer.TT_INTEGER ) )
            {
              strValue += T._strValue;
            }
            else
            {
              getTokenizer().restoreToMark( mark );
              return false;
            }
          }
          else
          {
            strValue += T._strValue;
          }
          int end = strValue.length() - 1;
          char suffix = ' ';
          if ( !Character.isDigit(strValue.charAt(end)) )
          {
            suffix = strValue.charAt(end);
            strValue = strValue.substring(0, end);
          }
          try
          {
            BigDecimal bigNum = new BigDecimal( strValue );
            strValue = bigNum.toPlainString();
            if (bigNum.scale() <= 0)
            {
              strValue += ".0";
            }
          }
          catch( NumberFormatException e )
          {
            getTokenizer().restoreToMark( mark );
            return false;
          }
          if( suffix != ' ')
          {
            strValue += suffix;
          }
        }
      }
      parseNumericValue( strValue );
      return true;
    }
    else if( '.' == token.getType() )
    {
      getTokenizer().nextToken();

      String strValue = (negated ? "-" : "") + ".";
      if( match( T, SourceCodeTokenizer.TT_INTEGER ) )
      {
        strValue += T._strValue;
        parseNumericValue( strValue );
      }
      else
      {
        pushErrorNumberLiteral( Res.MSG_EXPECTING_NUMBER_TO_FOLLOW_DECIMAL );
      }
      return true;
    }
    return false;
  }

  private void parseNumericValue( String strValue )
  {
    if( isPrefixNumericLiteral( strValue ) && strValue.indexOf( '.' ) != -1 )
    {
      pushErrorNumberLiteral( Res.MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE, strValue, JavaTypes.pINT() );
    }
    else
    {
      IType numericTypeFrom = getNumericTypeFrom( strValue );
      if( numericTypeFrom != null )
      {
        parseExplicitlyTypedNumericLiteral( strValue, numericTypeFrom );
      }
      else
      {
        IType ctxType = getNumberTypeFromContextType( getContextType().isMethodScoring() ? null : getContextType().getType() );
        NumericLiteral e;
        if( strValue.indexOf( '.' ) != -1 )
        {
          if( ctxType == JavaTypes.BIG_DECIMAL() )
          {
            e = new NumericLiteral( strValue,  new BigDecimal( strValue ), JavaTypes.BIG_DECIMAL());
          }
          else
          {
            if( ctxType == JavaTypes.pFLOAT() )
            {
              e = parseFloat( strValue );
            }
            else if( ctxType == JavaTypes.pDOUBLE() )
            {
              e = parseDouble( strValue );
            }
            else
            {
              e = parseDoubleOrBigDec( strValue );
            }
          }
        }
        else
        {
          if( ctxType == JavaTypes.BIG_INTEGER() )
          {
            if( isPrefixNumericLiteral(strValue))
            {
              strValue = stripPrefix( strValue );
            }
            e = new NumericLiteral( strValue,  new BigInteger( strValue ), JavaTypes.BIG_INTEGER());
          }
          else
          {
            try
            {
              if( !strValue.startsWith( "0" ) )
              {
                if( ctxType == JavaTypes.pFLOAT() )
                {
                  e = parseFloat( strValue );
                }
                else if( ctxType == JavaTypes.pDOUBLE() )
                {
                  e = parseDouble( strValue );
                }
                else
                {
                  e = parseIntOrLongOrBigInt( strValue );
                }
              }
              else
              {
                e = parseIntOrLongOrBigInt( strValue );
              }
            }
            catch( NumberFormatException ex )
            {
              pushErrorNumberLiteral(Res.MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE, strValue, JavaTypes.pINT());
              return;
            }
          }
        }
        pushExpression( e );
      }
    }
  }

  private NumericLiteral parseIntOrLongOrBigInt( String strValue )
  {
    NumericLiteral e;
    int radix = 10;
    String strippedValue = strValue;

    if( isPrefixNumericLiteral( strValue ) )
    {
      strippedValue = stripPrefix( strValue );
      if( isHexLiteral( strValue ) )
      {
        radix = 16;
      }
      else if ( isBinLiteral( strValue ) )
      {
        radix = 2;
      }
    }
    try
    {
      e = new NumericLiteral( strValue, Integer.parseInt( strippedValue, radix), JavaTypes.pINT() );
    }
    catch( NumberFormatException nfe )
    {
      try
      {
        e = new NumericLiteral( strValue, Long.parseLong( strippedValue, radix ), JavaTypes.pLONG() );
      }
      catch( NumberFormatException nfe2 )
      {
        e = new NumericLiteral( strValue, new BigInteger( strippedValue, radix ), JavaTypes.BIG_INTEGER() );
      }
    }
    return e;
  }

  private String stripPrefix( String strValue ) {
    String strippedValue;
    if( strValue.startsWith( "-" ) )
    {
      strippedValue = "-" + strValue.substring( 3 );
    }
    else
    {
      strippedValue = strValue.substring( 2 );
    }
    return strippedValue;
  }

  private NumericLiteral parseDoubleOrBigDec( String strValue )
  {
    double dValue = Double.parseDouble( strValue );
    if( dValue == Double.POSITIVE_INFINITY || dValue == Double.NEGATIVE_INFINITY )
    {
      return new NumericLiteral( strValue, new BigDecimal( strValue ), JavaTypes.BIG_DECIMAL() );
    }
    else
    {
      return new NumericLiteral( strValue, dValue, JavaTypes.pDOUBLE() );
    }
  }

  private NumericLiteral parseFloat( String strValue )
  {
    float fValue = Float.parseFloat( strValue );
    NumericLiteral floatLiteral = new NumericLiteral( strValue, fValue, JavaTypes.pFLOAT() );
    verify( floatLiteral, fValue != Float.POSITIVE_INFINITY && fValue != Float.NEGATIVE_INFINITY, Res.MSG_NUMBER_LITERAL_TOO_LARGE );
    return floatLiteral;
  }

  private NumericLiteral parseDouble( String strValue )
  {
    double dValue = Double.parseDouble( strValue );
    NumericLiteral doubleLiteral = new NumericLiteral( strValue, dValue, JavaTypes.pDOUBLE() );
    verify( doubleLiteral, dValue != Double.POSITIVE_INFINITY && dValue != Double.NEGATIVE_INFINITY, Res.MSG_NUMBER_LITERAL_TOO_LARGE );
    return doubleLiteral;
  }

  private void parseExplicitlyTypedNumericLiteral( String strValue, IType numericTypeFrom )
  {
    if( isPrefixNumericLiteral( strValue ) )
    {
      parsePrefixNumericLiteral( strValue, numericTypeFrom );
    }
    else
    {
      parsePostfixNumericLiteral(strValue, numericTypeFrom, 10);
    }
  }

  private boolean isPrefixNumericLiteral( String strValue )
  {
    return !(strValue.equalsIgnoreCase( "0b" ) || strValue.equalsIgnoreCase( "0bi" )|| strValue.equalsIgnoreCase( "0bd" ) ||
            strValue.equalsIgnoreCase( "-0b" ) || strValue.equalsIgnoreCase( "-0bi" )|| strValue.equalsIgnoreCase( "-0bd" )) &&
            (strValue.startsWith( "0x" ) || strValue.startsWith( "0X" ) ||
                    strValue.startsWith( "0b" ) || strValue.startsWith( "0B" ) ||
                    strValue.startsWith( "-0x" ) || strValue.startsWith( "-0X" ) ||
                    strValue.startsWith( "-0b" ) || strValue.startsWith( "-0B" ));
  }

  private void parsePrefixNumericLiteral( String strValue, IType numericTypeFrom )
  {
    int radix = 10;
    String strippedValue = stripPrefix( strValue );
    if( isHexLiteral( strValue ) )
    {
      radix = 16;
    }
    else if ( isBinLiteral( strValue ) )
    {
      radix = 2;
    }
    parsePostfixNumericLiteral( strippedValue, numericTypeFrom, radix );
  }

  private boolean isHexLiteral(String num) {
    num = num.toLowerCase();
    return num.startsWith( "0x" ) ||
            num.startsWith( "-0x" );
  }

  private boolean isBinLiteral(String num) {
    num = num.toLowerCase();
    boolean hasPrefix = num.startsWith("0b") ||
            num.startsWith("-0b");
    int b = num.indexOf("b") + 1;
    boolean hasDigit = ( b < num.length() ) && Character.isDigit( num.charAt( b ) );
    return hasPrefix && hasDigit;
  }

  private void parsePostfixNumericLiteral( String num, IType numericTypeFrom, int radix )
  {
    String strValue = maybeStripTypeModifier(num, numericTypeFrom);
    try
    {
      NumericLiteral e;
      if( JavaTypes.pBYTE().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, Byte.parseByte( strValue, radix ), JavaTypes.pBYTE() );
      }
      else if( JavaTypes.pSHORT().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, Short.parseShort( strValue, radix ), JavaTypes.pSHORT() );
      }
      else if( JavaTypes.pINT().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, Integer.parseInt( strValue, radix ), JavaTypes.pINT() );
      }
      else if( JavaTypes.pLONG().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, Long.parseLong( strValue, radix ), JavaTypes.pLONG() );
      }
      else if( JavaTypes.pFLOAT().equals( numericTypeFrom ) )
      {
        float value = Float.parseFloat( strValue );
        e = new NumericLiteral( strValue, value, JavaTypes.pFLOAT() );
        verify( e, !Float.isInfinite( value ), Res.MSG_NUMBER_LITERAL_TOO_LARGE );
      }
      else if( JavaTypes.pDOUBLE().equals( numericTypeFrom ) )
      {
        double value = Double.parseDouble( strValue );
        e = new NumericLiteral( strValue, value, JavaTypes.pDOUBLE() );
        verify( e, !Double.isInfinite( value ), Res.MSG_NUMBER_LITERAL_TOO_LARGE );
      }
      else if( JavaTypes.BIG_INTEGER().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, new BigInteger( strValue , radix), JavaTypes.BIG_INTEGER() );
      }
      else if( JavaTypes.BIG_DECIMAL().equals( numericTypeFrom ) )
      {
        e = new NumericLiteral( strValue, new BigDecimal( strValue ), JavaTypes.BIG_DECIMAL() );
      }
      else
      {
        throw new IllegalStateException( "Do not know how to parse a numeric type of value " + numericTypeFrom );
      }
      if( hasTypeModifier( num ) )
      {
        e.setExplicitlyTyped( true );
      }
      pushExpression( e );
    }
    catch( NumberFormatException ex )
    {
      pushErrorNumberLiteral( Res.MSG_IMPROPER_VALUE_FOR_NUMERIC_TYPE, strValue, numericTypeFrom.getName() );
    }
  }

  private String maybeStripTypeModifier( String strValue, IType numericTypeFrom )
  {
    if( hasTypeModifier(strValue) )
    {
      int modifierLen = JavaTypes.BIG_DECIMAL().equals( numericTypeFrom ) || JavaTypes.BIG_INTEGER().equals( numericTypeFrom ) ? 2 : 1;
      strValue = strValue.substring( 0, strValue.length() - modifierLen );
    }
    return strValue;
  }

  private boolean hasTypeModifier( String strValue )
  {
    boolean hex = isHexLiteral( strValue );
    char ch = strValue.toLowerCase().charAt( strValue.length() - 1 );
    if( hex && ( ch == 's' ) || ( ch == 'l' ) )
    {
      return true;
    }
    else if ( !hex && !Character.isDigit(ch) )
    {
      return true;
    }
    return false;
  }

  private void pushErrorNumberLiteral( ResourceKey key, Object... args )
  {
    NumericLiteral error = new NumericLiteral( "0", 0, JavaTypes.pINT() );
    addError( error, key, args );
    pushExpression(error);
  }

  private IType getNumericTypeFrom( String strValue )
  {
    boolean hex = isHexLiteral( strValue );
    boolean bin = isBinLiteral( strValue );

    if( !hex && (strValue.endsWith( "b" ) || strValue.endsWith( "B" )) )
    {
      return JavaTypes.pBYTE();
    }
    else if( strValue.endsWith( "s" ) || strValue.endsWith( "S" ) )
    {
      return JavaTypes.pSHORT();
    }
    else if( strValue.endsWith( "l" ) || strValue.endsWith( "L" ) )
    {
      return JavaTypes.pLONG();
    }
    else if( !hex && !bin && (strValue.endsWith( "f" ) || strValue.endsWith( "F" )) )
    {
      return JavaTypes.pFLOAT();
    }
    else if( !hex && (strValue.endsWith( "bi" ) || strValue.endsWith( "BI" )) )
    {
      return JavaTypes.BIG_INTEGER();
    }
    else if( !hex && !bin && (strValue.endsWith( "bd" ) || strValue.endsWith( "BD" )) )
    {
      return JavaTypes.BIG_DECIMAL();
    }
    else if( !hex && !bin && (strValue.endsWith( "d" ) || strValue.endsWith( "D" )) )
    {
      return JavaTypes.pDOUBLE();
    }
    else
    {
      return null;
    }
  }

  private boolean parseCharLiteral( Token token )
  {
    if( '\'' == token.getType() )
    {
      getTokenizer().nextToken();

      if( token._strValue.length() != 1 )
      {
        _parseStringLiteral( token._bUnterminated, token );
      }
      else
      {
        char c = token._strValue.charAt( 0 );
        IType ctxType = getContextType().getType();
        Expression e;
        if( !getContextType().isMethodScoring() &&
            c >= 0 && c <= Byte.MAX_VALUE &&
            (ctxType == JavaTypes.pBYTE() || ctxType == JavaTypes.BYTE()) )
        {
          e = new NumericLiteral( token._strValue, (byte)c, ctxType );
        }
        else
        {
          e = new CharLiteral( c );
        }
        verify( e, token.getInvalidCharPos() < 0, Res.MSG_INVALID_CHAR_AT, token.getInvalidCharPos() );
        verify( e, !token._bUnterminated, Res.MSG_UNTERMINATED_STRING_LITERAL );
        pushExpression( e );
      }
      return true;
    }
    return false;
  }

  private boolean parseStringLiteralSeparately()
  {
    final Token token = getTokenizer().getCurrentToken();
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    if( parseStringLiteral( token ) )
    {
      setLocation( iOffset, iLineNum, iColumn );
      return true;
    }
    return false;
  }

  private boolean parseStringLiteral( Token token )
  {
    if( '"' == token.getType() )
    {
      getTokenizer().nextToken();

      _parseStringLiteral( token._bUnterminated, token );
      return true;
    }
    return false;
  }

  private void _parseStringLiteral( boolean bUnterminatedLiteral, Token t )
  {
    Expression e;
    if( t._strValue.contains( TemplateGenerator.SCRIPTLET_BEGIN ) || t._strValue.contains( TemplateGenerator.ALTERNATE_EXPRESSION_BEGIN ) )
    {
      e = parseTemplatizedStringLiteral( t );
    }
    else
    {
      String strValue = t._strValue;
      if( strValue.length() > 0 && strValue.charAt( 0 ) == TemplateGenerator.ESCAPED_SCRIPTLET_MARKER )
      {
        strValue = strValue.substring( 1 )
                .replace( TemplateGenerator.ESCAPED_SCRIPTLET_BEGIN_CHAR, '<' )
                .replace( TemplateGenerator.ESCAPED_ALTERNATE_EXPRESSION_BEGIN_CHAR, '$' );
      }
      e = new StringLiteral( strValue );
    }
    if( bUnterminatedLiteral )
    {
      e.addParseException(new ParseException(makeFullParserState(), Res.MSG_UNTERMINATED_STRING_LITERAL));
    }
    verify( e, t.getInvalidCharPos() < 0, Res.MSG_INVALID_CHAR_AT, t.getInvalidCharPos() );
    pushExpression( e );
  }

  private TemplateStringLiteral parseTemplatizedStringLiteral( Token t )
  {
    TemplateGenerator template = TemplateGenerator.getTemplate( new StringReader( t._strValue ) );
    template.setContextInferenceManager(_ctxInferenceMgr);
    template.setForStringLiteral(true);
    TemplateStringLiteral e = new TemplateStringLiteral( template );
    GosuParser parser = (GosuParser)GosuParserFactory.createParser( _symTable, ScriptabilityModifiers.SCRIPTABLE );
    IScriptPartId scriptPart = getScriptPart();
    parser.pushScriptPart( scriptPart );
    try
    {
      parser.setEditorParser( isEditorParser() );
      parser._ctxInferenceMgr = _ctxInferenceMgr;
      template.setUseStudioEditorParser( isEditorParser() );
      copyBlockStackTo( parser );
      try
      {
        template.verify( parser, _dfsDeclByName, _typeUsesMap );
      }
      catch( ParseResultsException exc )
      {
        // errors are already where they occurred
      }
      boolean hasIssues = false;
      // adjust for open quote
      for( IParseTree location : parser.getLocations() )
      {
        ((ParseTree)location).adjustOffset( 1, 0, 0 );
        for( IParseIssue parseIssue : location.getParsedElement().getParseIssues() )
        {
          ((ParseIssue)parseIssue).setStateSource( _tokenizer.getSource() );
          hasIssues = true;
        }
      }
      setSubTree( parser.getLocations() );
      try
      {
        template.compile( _scriptPartIdStack, _symTable instanceof ThreadSafeSymbolTable ? _symTable : _symTable.copy(), _dfsDeclByName, _typeUsesMap, _blocks, _ctxInferenceMgr );
      }
      catch( TemplateParseException exc )
      {
        if( !hasIssues &&
                exc.getParseException() != null &&
                !(getScriptPart().getContainingType() instanceof IGosuFragment) )
        {
          List<IParseIssue> parseExceptions = exc.getParseException().getParseExceptions();
          for( IParseIssue p : parseExceptions )
          {
            e.addParseException( p );
          }
        }
      }
      return e;
    }
    finally
    {
      parser.popScriptPart( scriptPart );
    }
  }

  //------------------------------------------------------------------------------
  // type-literal
  //   name[\<<type-parameter-list>\>][\[\]]
  //
  public boolean parseTypeLiteral()
  {
    return parseTypeLiteral( getTokenizer().getCurrentToken(), false );
  }
  public boolean parseTypeLiteral( Token token )
  {
    return parseTypeLiteral( token, false );
  }
  boolean parseTypeLiteral( boolean bInterface )
  {
    return parseTypeLiteral( getTokenizer().getCurrentToken(), bInterface );
  }
  boolean parseTypeLiteral( Token token, boolean bInterface )
  {
    boolean bNoContextType = getContextType().getType() == null;
    if( bNoContextType )
    {
      // Hint that we are trying to parse a type literal (during indirect member parsing)
      pushInferredContextTypes( new ContextType( MetaType.DEFAULT_TYPE_TYPE.get() ) );
    }
    try
    {
      int iOffset = token.getTokenStart();
      int iLineNum = token.getLine();
      int iColumn = token.getTokenColumn();
      boolean bSuccess = _parseTypeLiteralWithAggregateSyntax( token, false, bInterface );
      if( bSuccess )
      {
        Expression e = peekExpression();
        if( e instanceof TypeLiteral )
        {
          IType monitorLockType = GosuTypes.IMONITORLOCK();
          verify( e,
                  monitorLockType == null ||
                          !monitorLockType.equals( ((TypeLiteral)e).getType().getType() ),
                  Res.MSG_IMONITOR_LOCK_SHOULD_ONLY_BE_USED_WITHIN_USING_STMTS );
        }
        setLocation( iOffset, iLineNum, iColumn, true );
      }
      return bSuccess;
    }
    finally
    {
      if( bNoContextType )
      {
        popInferredContextTypes();
      }
    }
  }

  void parseTypeLiteralIgnoreArrayBrackets()
  {
    Token token = getTokenizer().getCurrentToken();
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    if( _parseTypeLiteralWithAggregateSyntax( token, true, false ) )
    {
      setLocation( iOffset, iLineNum, iColumn );
    }
  }

  boolean _parseTypeLiteralWithAggregateSyntax( Token token, boolean bIgnoreArrayBrackets, boolean bInterface )
  {
    boolean bRet = _parseTypeLiteral( token, bIgnoreArrayBrackets, bInterface );

    token = getTokenizer().getCurrentToken();
    if( SourceCodeTokenizer.TT_OPERATOR == token.getType() && "&".equals( token.getStringValue() ) )
    {
      parseAggregateTypeLiteral( bInterface );
    }
    return bRet;
  }

  boolean _parseTypeLiteral( Token token, boolean bIgnoreArrayBrackets, boolean bInterface )
  {
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();

    if( SourceCodeTokenizer.TT_KEYWORD == token.getType() && Keyword.KW_block == token.getKeyword() )
    {
      getTokenizer().nextToken();
      
      _parseBlockLiteral();
      setLocation( iOffset, iLineNum, iColumn, true );
    }
    else
    {
      if( isWordOrValueKeyword( token ) || matchPrimitiveType( false ) )
      {
        getTokenizer().nextToken();
      }
      else
      {
        TypeLiteral typeLiteral = bInterface ? new InterfaceTypeLiteral( ErrorType.getInstance() ) : new TypeLiteral( ErrorType.getInstance() );
        verify( typeLiteral, false, Res.MSG_EXPECTING_TYPE_NAME );
        pushExpression( typeLiteral );
        Token priorT = getTokenizer().getPriorToken();
        setLocation( priorT.getTokenEnd(), priorT.getLine(), priorT.getTokenColumn(), true, true );
        return false;
      }
      parseTypeLiteral( new String[]{token.getStringValue()}, bIgnoreArrayBrackets, bInterface, iOffset, iLineNum, iColumn );
    }

    return true;
  }

  private boolean matchPrimitiveType( boolean bSuperThis ) {
    Token token = getTokenizer().getCurrentToken();
    if( token.getType() == SourceCodeTokenizer.TT_KEYWORD )
    {
      boolean bMatch =
             Keyword.KW_void == token.getKeyword() ||
             Keyword.KW_boolean == token.getKeyword() ||
             Keyword.KW_char == token.getKeyword() ||
             Keyword.KW_byte == token.getKeyword() ||
             Keyword.KW_short == token.getKeyword() ||
             Keyword.KW_int == token.getKeyword() ||
             Keyword.KW_long == token.getKeyword() ||
             Keyword.KW_float == token.getKeyword() ||
             Keyword.KW_double == token.getKeyword() ||
             (bSuperThis &&
              (Keyword.KW_this == token.getKeyword() ||
               Keyword.KW_super == token.getKeyword()));
      if( bMatch )
      {
        return true;
      }
    }
    return false;
  }


  private void parseAggregateTypeLiteral( boolean bInterface )
  {
    CompoundTypeLiteral typeLiteral = new CompoundTypeLiteral();
    List<IType> types = new ArrayList<>();
    TypeLiteral typeLiteralComponent = (TypeLiteral) peekExpression();
    while( true )
    {
      addToCompoundType( types );
      if( !match( null, "&", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        break;
      }
      _parseTypeLiteral( getTokenizer().getCurrentToken(), false, bInterface );
    }
    verify( typeLiteral, types.size() > 1, Res.MSG_AGGREGATES_MUST_CONTAIN_MORE );
    verify( typeLiteral, !(typeLiteralComponent.getType().getType() instanceof TypeVariableType), Res.MSG_ONLY_ONE_TYPE_VARIABLE );

    typeLiteral.setType( CompoundType.get( new HashSet<IType>( types ) ) );
    pushExpression(typeLiteral);
  }

  private void addToCompoundType( List<IType> types )
  {
    TypeLiteral typeLiteralComponent = (TypeLiteral)popExpression();
    IType t = typeLiteralComponent.getType().getType();
    verify( typeLiteralComponent, t != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
    Set<IType> componentTypes = t.isCompoundType() ? t.getCompoundTypeComponents() : Collections.singleton(t);
    for( IType componentType : componentTypes )
    {
      boolean bFoundClassAlready = false;
      for( IType csr : types )
      {
        if( !(csr instanceof ErrorType) )
        {
          if( verify( typeLiteralComponent, csr != componentType, Res.MSG_ALREADY_CONTAINS_TYPE, componentType ) )
          {
            verify( typeLiteralComponent, !(csr.isAssignableFrom( componentType ) || StandardCoercionManager.isStructurallyAssignable( csr, componentType )),Res.MSG_INTERFACE_REDUNDANT, csr, componentType );
            verify( typeLiteralComponent, !(componentType.isAssignableFrom( csr ) || StandardCoercionManager.isStructurallyAssignable( componentType, csr )), Res.MSG_INTERFACE_REDUNDANT, componentType, csr );
          }
        }
        if( !csr.isInterface() )
        {
          bFoundClassAlready = true;
        }
        verify( typeLiteralComponent, componentType.isInterface() || !bFoundClassAlready, Res.MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE );
      }
      verify( typeLiteralComponent, !componentType.isArray(), Res.MSG_NO_ARRAY_IN_COMPONENT_TYPE );
      verify( typeLiteralComponent, !componentType.isPrimitive(), Res.MSG_NO_PRIMITIVE_IN_COMPONENT_TYPE );
      types.add( componentType );
    }
  }

  void parseBlockLiteral() {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    _parseBlockLiteral();
    setLocation( iOffset, iLineNum, iColumn );
  }

  void _parseBlockLiteral()
  {
    BlockLiteral literal = new BlockLiteral();
    verify( literal, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_BLOCK );

    ArrayList<IType> argTypes = new ArrayList<IType>();
    ArrayList<String> argNames = new ArrayList<String>();
    ArrayList<IExpression> defValues = new ArrayList<IExpression>();

    if( !match( null, ')' ) )
    {
      do
      {
        String result;
        int state = _tokenizer.mark();
        Token t = new Token();
        boolean bEquals = false;
        Expression defExpr = null;
        TypeLiteral blockLiteral = null;

        if( match( t, SourceCodeTokenizer.TT_WORD ) )
        {
          if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
          {
            result = t._strValue;
          }
          else if( bEquals = match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
          {
            result = t._strValue;
            parseExpression();
            defExpr = popExpression();
          }
          else if( match( null, null, '(', true ) )
          {
            result = t._strValue;
            parseBlockLiteral();
            blockLiteral = (TypeLiteral) popExpression();
          }
          else
          {
            _tokenizer.restoreToMark( state );
            result = null;
          }
        }
        else
        {
          _tokenizer.restoreToMark( state );
          result = null;
        }
        String name = result;
        TypeLiteral typeLiteral = null;
        if( !bEquals )
        {
          if ( blockLiteral == null )
          {
            parseTypeLiteral();
            typeLiteral = (TypeLiteral)popExpression();
            if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
            {
              parseExpression( new ContextType( typeLiteral.getType().getType(), false ) );
              defExpr = popExpression();
            }
          }
          else
          {
            typeLiteral = blockLiteral;
          }
          argTypes.add( typeLiteral.getType().getType() );
          verifyOrWarn( typeLiteral, name != null, true, Res.MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES );
          verify( typeLiteral, typeLiteral.getType().getType() != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
        }
        else
        {
          argTypes.add( defExpr.getType() );
          verifyOrWarn( literal, name != null, true, Res.MSG_BLOCK_TYPES_SHOULD_HAVE_ARG_NAMES );
          verify( literal, defExpr.getType() != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
        }
        if( defExpr != null )
        {
          if( verify( defExpr, defExpr.isCompileTimeConstant(), Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED ) )
          {
            defValues.add( defExpr );
          }
        }
        else
        {
          defValues.add( null );
        }
        if( name != null && argNames.contains( name ) )
        {
          verify( typeLiteral == null ? literal : typeLiteral, false, Res.MSG_VARIABLE_ALREADY_DEFINED, name );
        }
        argNames.add( name == null ? "" : name );
      } while( match( null, ',' ) );

      verify( literal, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_BLOCK );
    }

    argNames.trimToSize();
    argTypes.trimToSize();
    literal.setArgTypes( argTypes );
    literal.setArgNames( argNames );
    literal.setDefValueExpressions( defValues );

    TypeLiteral returnType;
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseTypeLiteral();
      returnType = (TypeLiteral)popExpression();
    }
    else
    {
      returnType = new TypeLiteral( JavaTypes.pVOID() );
      returnType.setType( JavaTypes.pVOID() );
    }

    verify( literal, argNames.size() <= IBlock.MAX_ARGS, Res.MSG_BLOCKS_CAN_HAVE_A_MOST_SIXTEEN_ARGS );

    literal.setReturnType( returnType );

    pushExpression( literal );
  }

  void parseTypeLiteral( String[] T, boolean bIgnoreArrayBrackets, boolean bInterface, int iOffset, int iLineNum, int iColumn )
  {
    parseCompoundTypeLiteralExpression( T, bInterface, iOffset, iLineNum, iColumn );
    Expression expr = popExpression();

    if( expr instanceof Identifier && !(expr.getType() instanceof ErrorType) )
    {
      //!! todo: this is a hack so we can resolve types with the same name as identifiers (case insensitively)
      TypeLiteral e = bInterface ? new InterfaceTypeLiteral( MetaType.getLiteral( expr.getType() ) ) : new TypeLiteral( MetaType.getLiteral( expr.getType() ) );
      e.setPackageExpression( expr );
      expr = e;
    }
    else if( !(expr instanceof TypeLiteral) )
    {
      TypeLiteral e = bInterface ? new InterfaceTypeLiteral( ErrorType.getInstance() ) : new TypeLiteral( ErrorType.getInstance() );
      e.setPackageExpression( expr );
      e.addParseException( new ParseException( null, Res.MSG_EXPECTING_TYPE_NAME ) );
      expr = e;
    }
    IType type = ((TypeLiteral)expr).getType().getType();
    verifyTypeAccessible( (TypeLiteral)expr, type );
    T[0] = type.getName();
    resolveArrayOrParameterizationPartOfTypeLiteral( T, bIgnoreArrayBrackets, (TypeLiteral)expr );
  }

  private void verifyTypeAccessible( TypeLiteral expr, IType type )
  {
    ICompilableType gsClass = getGosuClass();
    if( gsClass == null || Modifier.isPublic( type.getModifiers() ) )
    {
      return;
    }

    IRelativeTypeInfo.Accessibility acc = FeatureManager.getAccessibilityForClass( type, gsClass );
    if( Modifier.isPrivate(type.getModifiers()) )
    {
      verify(expr,
              acc == IRelativeTypeInfo.Accessibility.PRIVATE,
              Res.MSG_TYPE_HAS_XXX_ACCESS, type.getName(), Keyword.KW_private.toString());
    }
    else if( Modifier.isProtected( type.getModifiers() ) )
    {
      verify( expr,
              acc == IRelativeTypeInfo.Accessibility.PROTECTED ||
                      acc == IRelativeTypeInfo.Accessibility.INTERNAL ||
                      acc == IRelativeTypeInfo.Accessibility.PRIVATE,
              Res.MSG_TYPE_HAS_XXX_ACCESS, type.getName(), Keyword.KW_protected.toString() );
    }
    else if( /* package-protected (or internal) */ !Modifier.isPublic( type.getModifiers() ) &&
            !Modifier.isProtected( type.getModifiers() ))
    {
      verify( expr,
              acc == IRelativeTypeInfo.Accessibility.INTERNAL ||
                      acc == IRelativeTypeInfo.Accessibility.PRIVATE,
              Res.MSG_TYPE_HAS_XXX_ACCESS, type.getName(), Keyword.KW_internal.toString() );
    }
  }

  private void parseCompoundTypeLiteralExpression( String[] T, boolean bInterface, int iOffset, int iLineNum, int iColumn )
  {
    parseNamespaceStartOrRelativeType( T, bInterface );
    Expression expr = peekExpression();
    if( expr.hasParseExceptions() )
    {
      List<IParseIssue> exceptions = expr.getParseExceptions();
      if( exceptions.size() != 1 ||
              (exceptions.get( 0 ).getMessageKey() != Res.MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT &&
                      exceptions.get( 0 ).getMessageKey() != Res.MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO &&
                      exceptions.get( 0 ).getMessageKey() != Res.MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE) )
      {
        ParseException pe = expr.removeParseException( null );// Res.MSG_BAD_IDENTIFIER_NAME );
        pe.setMessage( Res.MSG_INVALID_TYPE, T[0] );
        expr.addParseException( pe );
      }
    }
    setLocation( iOffset, iLineNum, iColumn );
    parseIndirectMemberAccess( iOffset, iLineNum, iColumn, true );
  }

  private boolean isTypeParameterErrorMsg( Expression expr, List<IParseIssue> exceptions )
  {
    return exceptions.get( 0 ).getMessageKey() == Res.MSG_TYPE_PARAM_NOT_ASSIGNABLE_TO && expr instanceof TypeLiteral && !isErrorType( ((TypeLiteral)expr).getType().getType() );
  }

  /**
   * @return True if parsed parameterized type.
   */
  private boolean resolveArrayOrParameterizationPartOfTypeLiteral( String[] T, boolean bIgnoreArrayBrackets, TypeLiteral e )
  {
    boolean bArrayOrParameterization = false;
    if( !bIgnoreArrayBrackets )
    {
      bArrayOrParameterization = parseArrayType( e );
    }
    pushExpression(e);

    if( !T[0].endsWith( "[]" ) )
    {
      int iOffset = _tokenizer.getTokenStart();
      int iLineNum = _tokenizer.getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      if( match( null, "<", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        TypeLiteral typeLiteral = (TypeLiteral)peekExpression();
        IType type = typeLiteral.getType().getType();
        verify( e, type.isGenericType(), Res.MSG_PARAMETERIZATION_NOT_SUPPORTED_FOR_TYPE, type.getName() );
        List<TypeLiteral> paramTypes = parseTypeParameters( type );
        verify( e, match( null, ">", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE );
        makeTypeParameterListClause( iOffset, iLineNum, iColumn, paramTypes );
        int numArrays = 0;
        while( !bIgnoreArrayBrackets && match( null, '[' ) )
        {
          verify( e, match( null, ']' ), Res.MSG_EXPECTING_ARRAY_BRACKET );
          ++numArrays;
        }
        if( !(type instanceof ErrorType) )
        {
          IType[] types = new IType[paramTypes.size()];
          for( int i = 0; i < paramTypes.size(); i++ )
          {
            TypeLiteral tl = paramTypes.get( i );
            types[i] = (tl.getType()).getType();
          }
          verifyCanParameterizeType( e, type, types );
          typeLiteral.setParameterTypes( types );
          type = typeLiteral.getType().getType();
          if( numArrays > 0 )
          {
            for( int i = 0; i < numArrays; i++ )
            {
              type = type.getArrayType();
            }
            typeLiteral.setType( MetaType.getLiteral( type ) );
          }
        }
        bArrayOrParameterization = true;
      }
      else
      {
        TypeLiteral typeLiteral = (TypeLiteral)peekExpression();
        IType type = typeLiteral.getType().getType();
        try
        {
          if( type.isGenericType() && !type.isParameterizedType() && !isParsingCompileTimeConstantExpression() )
          {
            // If a generic type, assume the default parameterized version e.g., List => List<Object>.
            // But if the type is assignable to the context type and the context type is parameterized,
            // derive the parameters from the context type.
            type = TypeLord.deriveParameterizedTypeFromContext( type, getContextType().getType() );
            typeLiteral.setType( MetaType.getLiteral( type ) );
          }
        }
        catch( Exception ex )
        {
          throw GosuExceptionUtil.forceThrow( ex, type.getName() );
        }
      }
    }
    return bArrayOrParameterization;
  }

  private boolean isParsingCompileTimeConstantExpression() {
    return getContextType() != null && getContextType().isCompileTimeConstant();
  }

  private boolean parseArrayType( TypeLiteral tl )
  {
    boolean bBalancedBrackets = true;
    IType baseType = tl.getType().getType();
    while( match( null, '[' ) )
    {
      bBalancedBrackets = match( null, ']' );
      if( !bBalancedBrackets )
      {
        advanceToNextTokenSilently();
      }
      try
      {
        baseType = baseType.getArrayType();
      }
      catch( IllegalArgumentException iae )
      {
        tl.addParseException( Res.MSG_ARRAY_NOT_SUPPORTED, baseType.getName() );
        baseType = ErrorType.getInstance();
      }
    }
    if( baseType != tl.getType().getType() )
    {
      warn( tl, !tl.getType().getType().isParameterizedType() ||
                TypeLord.getDefaultParameterizedType( tl.getType().getType() ) == tl.getType().getType(),
            Res.MSG_PARAMETERIZED_ARRAY_COMPONENT );
      tl.setType( MetaType.getLiteral( baseType ) );
      verify( tl, bBalancedBrackets, Res.MSG_EXPECTING_ARRAY_BRACKET );
      return true;
    }
    return false;
  }

  //------------------------------------------------------------------------------
  // type-parameter-list
  //   <type-parameter>
  //   <type-parameter-list> , <type-parameter>
  //
  List<TypeLiteral> parseTypeParameters( IType enclosingType )
  {
    List<TypeLiteral> paramTypes = new ArrayList<TypeLiteral>();
    int i = 0;
    do
    {
      IType boundingType = JavaTypes.OBJECT();
      if( enclosingType != null && enclosingType.isGenericType() )
      {
        IGenericTypeVariable[] typeVars = enclosingType.getGenericTypeVariables();
        if( typeVars != null && typeVars.length > i )
        {
          boundingType = typeVars[i].getBoundingType();
        }
      }
      parseParameterType( boundingType );
      paramTypes.add( (TypeLiteral)popExpression() );
    }
    while( match( null, ',' ) && ++i > 0 );

    return paramTypes;
  }

  private void makeTypeParameterListClause( int iOffset, int iLineNum, int iColumn, List<TypeLiteral> paramTypes )
  {
    if( paramTypes.size() > 0 )
    {
      TypeParameterListClause e = new TypeParameterListClause( paramTypes.toArray( new ITypeLiteralExpression[paramTypes.size()] ) );
      pushExpression( e );
      boolean bZeroLength = _tokenizer.getTokenStart() == iOffset;
      if( bZeroLength )
      {
        Token priorToken = getTokenizer().getPriorToken();
        iOffset = priorToken.getTokenEnd();
        iLineNum = priorToken.getLine();
        iColumn = priorToken.getTokenColumn();
      }
      setLocation( iOffset, iLineNum, iColumn, true );
      popExpression();
    }
  }

  //------------------------------------------------------------------------------
  // type-parameter
  //   ? [extends <type-literal>]
  //   ? [super <type-literal>]
  //   <type-literal>
  //
  boolean parseParameterType( IType boundingType )
  {
    boolean isWildcard = false;
    Expression superTypeLiteral = null;
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    if( match( null, "?", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      isWildcard = true;
      if( match( null, Keyword.KW_extends ) )
      {
        if( match( null, "?", SourceCodeTokenizer.TT_OPERATOR, true ) )
        {
          // A strange, but necessary case. E.g.,
          // List<E> has method: addAll( Collection<? extends E> c )
          // when we have type List<?> we'll have addAll( Collection<? extends ?> c )
          // which essentially needs to be just that - a Collection of something undefined
          if( !parseParameterType( boundingType ) )
          {
            return false;
          }
        }
        else
        {
          parseTypeLiteral();
        }
      }
      else if( match( null, Keyword.KW_super ) )
      {
        if( match( null, "?", SourceCodeTokenizer.TT_OPERATOR, true ) )
        {
          // A strange, but necessary case. E.g.,
          // List<E> has method: addAll( Collection<? super E> c )
          // when we have type List<?> we'll have addAll( Collection<? super ?> c )
          // which essentially needs to be just that - a Collection of something undefined
          if( !parseParameterType( boundingType ) )
          {
            return false;
          }
        }
        else
        {
          parseTypeLiteral();
          superTypeLiteral = popExpression(); // Eat whatever type literal is here (we punt on contravariance)

          TypeLiteral typeLiteral = new TypeLiteral( MetaType.getLiteral( boundingType ) );
          pushExpression( typeLiteral );
        }
      }
      else
      {
        pushExpression( new TypeLiteral( JavaTypes.OBJECT() ) );
        setLocation( iOffset, iLineNum, iColumn );
      }
    }
    else
    {
      parseTypeLiteral();
    }
    TypeLiteral tl = (TypeLiteral)peekExpression();
    if( !isAllowingWildcards() )
    {
      if( superTypeLiteral != null )
      {
        verify(superTypeLiteral, !isWildcard, Res.MSG_NO_WILDCARDS, tl.getType().getType().getRelativeName() );
      }
      verify(tl, !isWildcard, Res.MSG_NO_WILDCARDS, tl.getType().getType().getRelativeName() );
    }
    boxTypeLiteralsType( tl );
    return true;
  }

  private void boxTypeLiteralsType( TypeLiteral tl )
  {
    IType tlType = tl.getType().getType();
    if( !warn( tl, !tlType.isPrimitive(), Res.MSG_PRIMITIVE_TYPE_PARAM, tlType.getName(), TypeSystem.getBoxType( tlType ) ) )
    {
      tl.setType( TypeSystem.getBoxType( tlType ) );
    }
  }

  //------------------------------------------------------------------------------
  // statement
  //   namespace-statement
  //     package namespace-name
  //
  //   uses-statement
  //     uses type-literal
  //     uses namespace-ref
  //
  //   block
  //     { [statement-list] }
  //       statement-list
  //         <statement>
  //         <statement-list> <statement>
  //       * Left recursion removed is: *
  //       statement-list
  //         <statement> <statement-list2>
  //       statement-list2
  //         <statement>
  //         null
  //
  //   assignment-statement
  //     <identifier> = <expression>
  //     <member-access> = <expression>
  //     <array-access> = <expression>
  //
  //   method-call-statement
  //     <method-call-expression>
  //
  //   argument-list
  //     <expression>
  //     <argument-list> , <expression>
  //
  //   if-statement
  //     if ( <expression> ) <statement> [ else <statement> ]
  //
  //   for...in-statement
  //     for ( <identifier> in <expression> [ index <identifier> ] ) <statement>
  //
  //   while-statement
  //     while ( <expression> ) <statement>
  //
  //   do...while-statetment
  //     do <statement> while ( <expression> )
  //
  //   var-statement
  //     var <identifier> [ : <type-literal> ] = <expression>
  //     var <identifier> : <type-literal> [ = <expression> ]
  //
  //   switch-statement
  //     switch ( <expression> ) { [switch-cases] [switch-default] }
  //
  //   switch-cases
  //     <switch-case>
  //     <switch-cases> <switch-case>
  //
  //   switch-case
  //     case <expression> : [statement-list]
  //
  //   switch-default
  //     default : [statement-list]
  //
  //   continue-statement
  //     continue
  //
  //   break-statement
  //     break
  //
  //   return-statement
  //     return <expression>
  //     return ;
  //
  //   assert-statement
  //     assert <expression>
  //     assert <expression> : <expression>
  //
  //   class-definition
  //     [modifiers] class <identifier> [extends <base-class>] [implements <interfaces-list>] { <class-members> }
  //
  //   function-definition
  //     [modifiers] function <identifier> ( [ <argument-declaration-list> ] ) [ : <type-literal> ] <statement-block>
  //
  //   argument-declaration-list
  //     <argument-declaration>
  //     <argument-declarationlist> , <argument-declaration>
  //
  //   argument-declaration
  //     <identifier> : <type-literal>
  //
  //   try-catch-finally-statement
  //     try <statement> [ catch ( <identifier> ) <statement> ] [ finally <statement> ]
  //
  boolean parseStatement()
  {
    return parseStatement( false );
  }
  boolean parseStatement( boolean bAsStmtBlock )
  {
    incStatementDepth();
    try
    {
      int iOffset = _tokenizer.getTokenStart();
      int iLineNum = _tokenizer.getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      checkInstruction( true );

      ParseTree prevLocation = peekLocation();
      IParsedElement prevStmt =
              prevLocation != null && prevLocation.getParsedElement() != null
                      ? prevLocation.getParsedElement() instanceof Statement
                      ? prevLocation.getParsedElement()
                      : null
                      : null;

      boolean bRet;
      boolean bSetLocation = true;
      boolean bMatchedBrace = !bAsStmtBlock && match( null, '{' );
      if( bMatchedBrace || bAsStmtBlock )
      {
        parseStatementBlock( bMatchedBrace || !bAsStmtBlock );
        bRet = true;
        bSetLocation = peekStatement() instanceof StatementList;
      }
      else
      {
        bRet = _parseStatement();
      }
      if( bRet && bSetLocation )
      {
        // Consume optional trailing semi as part of the statement
        match( null, ';' );

        setLocation( iOffset, iLineNum, iColumn, bAsStmtBlock );
        ParsedElement currentStmt = peekStatement();
        if( !(currentStmt instanceof NoOpStatement) && !(prevStmt instanceof NoOpStatement) )
        {
          warn( currentStmt, prevStmt == null || prevStmt.getLineNum() != currentStmt.getLineNum() ||
                  prevStmt.hasParseExceptions() || currentStmt.hasParseExceptions(),
                  Res.MSG_STATEMENT_ON_SAME_LINE );
        }
      }

      return bRet;
    }
    finally
    {
      decStatementDepth();
    }
  }

  boolean parseLoopStatement()
  {
    _iBreakOk++;
    _iContinueOk++;
    try
    {
      return parseStatement();
//      if( parseStatement() ) {
//        Statement stmt = peekStatement();
//        warn( stmt, doesLoopCycle( stmt ), Res.MSG_LOOP_DOESNT_LOOP );
//        return true;
//      }
//      return false;
    }
    finally
    {
      _iBreakOk--;
      _iContinueOk--;
    }
  }

//## todo: doesn't handle this case (break is absolute with help of continue, but info about continue does not exist past the break)
//## propbably best to change getLeastSignificantTerminal() to return something that provides more information, yet retains the same
//## terminal statement for normal unreachable code detection.
//  do {
//    if( cond ) {
//      break
//    }
//    else {
//      continue
//    }
//  } while( true )
//
//  private boolean doesLoopCycle( Statement stmt )
//  {
//    boolean[] bAbsolute = {false};
//    ITerminalStatement termStmt = stmt.getLeastSignificantTerminalStatement( bAbsolute );
//    if( termStmt == null || !bAbsolute[0] ) {
//      return true;
//    }
//    return termStmt.getTerminalType() == TerminalType.Continue;
//  }

  boolean _parseStatement()
  {
    Token token = getTokenizer().getCurrentToken();
    final Keyword keyword = token.getKeyword();
    if( areUsingStatementsAllowedInStatementLists() && Keyword.KW_uses == keyword )
    {
      _tokenizer.nextToken();
      parseUsesStatement();
    }
    else if( Keyword.KW_if == keyword )
    {
      _tokenizer.nextToken();
      parseIfStatement();
    }
    else if( Keyword.KW_try == keyword )
    {
      _tokenizer.nextToken();
      parseTryCatchFinallyStatement();
    }
    else if( Keyword.KW_throw == keyword )
    {
      _tokenizer.nextToken();
      parseThrowStatement();
    }
    else if( Keyword.KW_continue == keyword )
    {
      _tokenizer.nextToken();
      ContinueStatement stmt = new ContinueStatement();
      verify( stmt, _iContinueOk > 0, Res.MSG_CONTINUE_OUTSIDE_LOOP );
      pushStatement( stmt );
    }
    else if( Keyword.KW_break == keyword )
    {
      _tokenizer.nextToken();
      BreakStatement stmt = new BreakStatement();
      verify( stmt, _iBreakOk > 0, Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP );
      pushStatement( stmt );
    }
    else if( Keyword.KW_return == keyword )
    {
      _tokenizer.nextToken();
      parseReturnStatement();
    }
    else if( Keyword.KW_foreach == keyword || Keyword.KW_for == keyword )
    {
      _tokenizer.nextToken();
      parseForEachStatement();
    }
    else if( Keyword.KW_while == keyword )
    {
      _tokenizer.nextToken();
      parseWhileStatement();
    }
    else if( Keyword.KW_do == keyword )
    {
      _tokenizer.nextToken();
      parseDoWhileStatement();
    }
    else if( Keyword.KW_switch == keyword )
    {
      _tokenizer.nextToken();
      parseSwitchStatement();
    }
    else if( Keyword.KW_using == keyword )
    {
      _tokenizer.nextToken();
      parseUsingStatement();
    }
    else if( Keyword.KW_assert == keyword )
    {
      _tokenizer.nextToken();
      parseAssertStatement();
    }
    else if( Keyword.KW_final == keyword )
    {
      _tokenizer.nextToken();
      VarStatement varStmt = new VarStatement();
      varStmt.setModifierInfo( new ModifierInfo(0) );
      varStmt.setFinal( true );
      parseLocalVarStatement( varStmt );
    }
    else if( Keyword.KW_var == keyword )
    {
      VarStatement varStmt = new VarStatement();
      parseLocalVarStatement( varStmt );
    }
    else if( ';' == token.getType() )
    {
      _tokenizer.nextToken();
      pushStatement( new NoOpStatement() );
    }
    else if( getGosuClass() instanceof IGosuProgram &&
            getStatementDepth() == 1 &&
            !isParsingBlock() &&
            !((IGosuProgramInternal)getGosuClass()).isStatementsOnly() &&
            (maybeAdvanceTokenizerToEndOfSavedLocation()) )
    {
      pushStatement( new NoOpStatement() );
    }
    else if( !isParsingFunction() && parseFunctionDefinition() )
    {
      return true;
    }
    else if( !isParsingFunction() && parsePropertyDefinition() )
    {
      return true;
    }
    else if( Keyword.KW_eval == keyword )
    {
      int iOffset = token.getTokenStart();
      int iLineNum = token.getLine();
      int iColumn = token.getTokenColumn();
      _tokenizer.nextToken();
      parseEvalExpression();
      setLocation( iOffset, iLineNum, iColumn, true );
      pushStatement( new EvalStatement( (EvalExpression)popExpression() ) );
    }
    else if( !parseAssignmentOrMethodCall() )
    {
      if( SourceCodeTokenizer.TT_EOF != token.getType() )
      {
        int iOffset = token.getTokenStart();
        int iLineNum = token.getLine();
        int iColumn = token.getTokenColumn();

        if( '}' != token.getType() &&
            ';' != token.getType() )
        {
          if( isParsingFunction() )
          {
            if( maybeAdvanceTokenizerToEndOfSavedLocation() )
            {
              pushStatement( new NoOpStatement() );
              setLocation( iOffset, iLineNum, iColumn, true, true );
              popStatement();
              return false;
            }
            else if( Keyword.KW_construct == token.getKeyword() ||
                     Keyword.KW_function == token.getKeyword() ||
                     Keyword.KW_property == token.getKeyword() )
            {
              Statement noop = new NoOpStatement();
              getTokenizer().nextToken();
              eatStatementBlock( noop, Res.MSG_SYNTAX_ERROR );
              pushStatement( noop );
              setLocation( iOffset, iLineNum, iColumn, true, true );
              popStatement();
              return false;
            }
          }

          String str = _tokenizer.getTokenAsString();
          TypeLiteral type = resolveTypeLiteral( str );
          _tokenizer.nextToken();

          Statement noop = new NoOpStatement();
          if( !(type.evaluate() instanceof ErrorType) )
          {
            int state = _tokenizer.mark();
            int iLocationsCount = _locations.size();
            parsePrimaryExpression();
            Expression expression = popExpression();
            if( expression instanceof Identifier && match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
            {
              expression.clearParseExceptions();
              parseExpression(); //bad rhs
              popExpression();
              verify( noop, false, Res.MSG_JAVA_STYLE_VARIABLE_DECLARATION, str );
            }
            else
            {
              _tokenizer.restoreToMark( state );
              removeLocationsFrom( iLocationsCount );
              verify( noop, false, Res.MSG_UNEXPECTED_TOKEN, str );
            }
          }
          else
          {
            verify( noop, false, Res.MSG_UNEXPECTED_TOKEN, str );
          }

          pushStatement( noop );
        }
        else if( getStatementDepth() == 1 &&
                !isParsingBlock() &&
                getGosuClass() instanceof IGosuProgram &&
                ((IGosuProgramInternal)getGosuClass()).isParsingExecutableProgramStatements() )
        {
          Statement noop = new NoOpStatement();
          verify( noop, false, Res.MSG_UNEXPECTED_TOKEN, _tokenizer.getTokenAsString() );
          pushStatement( noop );
          _tokenizer.nextToken();
        }
        else
        {
          return false;
        }
      }
      else
      {
        return false;
      }
    }

    return true;
  }

  private void parseAssertStatement()
  {
    AssertStatement assertStmt = new AssertStatement();

    if( verify( assertStmt, getGosuClass() instanceof IGosuClassInternal, Res.MSG_ASSERTIONS_NOT_ALLOWED_HERE ) )
    {
      ((IGosuClassInternal)getGosuClass()).setHasAssertions( true );
    }

    parseExpression( ContextType.pBOOLEAN_FALSE );
    Expression condition = popExpression();
    if( !verify( condition, !(condition instanceof NotAWordExpression), Res.MSG_EXPECTING_CONDITION_FOR_ASSERT ) )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      condition.removeParseException( Res.MSG_SYNTAX_ERROR );
    }
    assertStmt.setCondition( condition );
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseExpression( ContextType.OBJECT_FALSE );
      Expression detail = popExpression();
      if( !verify( detail, !(detail instanceof NotAWordExpression), Res.MSG_EXPECTING_MESSAGE_FOR_ASSERT ) )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        detail.removeParseException( Res.MSG_SYNTAX_ERROR );
      }
      assertStmt.setDetail( detail );
    }
    pushStatement( assertStmt );
  }

  private boolean areUsingStatementsAllowedInStatementLists()
  {
    if( _bAreUsingStatementsAllowedInStatementLists == null )
    {
      _bAreUsingStatementsAllowedInStatementLists=
        getGosuClass() == null ||
        (getGosuClass() instanceof IGosuProgramInternal && ((IGosuProgramInternal)getGosuClass()).allowsUses()) ||
        CommonServices.getEntityAccess().areUsesStatementsAllowedInStatementLists( getGosuClass() );
    }
    return _bAreUsingStatementsAllowedInStatementLists;
  }

  private int getStatementDepth()
  {
    return _iStmtDepth;
  }
  private void incStatementDepth()
  {
    _iStmtDepth++;
  }
  private void decStatementDepth()
  {
    _iStmtDepth--;
  }

  void parseLocalVarStatement( VarStatement varStmt )
  {
    Token t = new Token();
    verify( varStmt, match( t, Keyword.KW_var ), Res.MSG_EXPECTING_VAR_STMT );
    int iNameOffset = getTokenizer().getTokenStart();
    if( verify( varStmt, match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_VAR ) )
    {
      varStmt.setNameOffset( iNameOffset, t._strValue );
      warn( varStmt, !Keyword.isKeyword( t._strValue ), Res.MSG_IMPROPER_USE_OF_KEYWORD, t._strValue );
    }
    else
    {
      t._strValue = null;
    }
    parseVarStatement( varStmt, t, false );
  }

  void parseVarStatement( VarStatement varStmt, Token idToken, boolean bClassMember )
  {
    String strIdentifier = idToken._strValue == null ? "" : idToken._strValue;
    warn( varStmt, !Keyword.isKeyword( strIdentifier ), Res.MSG_IMPROPER_USE_OF_KEYWORD, strIdentifier );
    Token priorToken = getTokenizer().getPriorToken();
    boolean bZeroLength = strIdentifier.length() <= 0;
    addNameInDeclaration( strIdentifier,
            bZeroLength ? priorToken.getTokenEnd() : idToken._iDocPosition,
            priorToken.getLine(), priorToken.getTokenColumn(), !bZeroLength );

    ISymbol existingSymbol = _symTable.getSymbol( strIdentifier );
    if( (isParsingBlock() || getParsingAnonymousClass() != null) && !isParsingAnnotation() )
    {
      existingSymbol = captureSymbol( getCurrentEnclosingGosuClass(), strIdentifier, null );
    }
    boolean bFieldSymbolFromProgram = false;
    if( !bClassMember && existingSymbol != null )
    {
      bFieldSymbolFromProgram = getGosuClass() instanceof IGosuProgram &&
              (bClassMember || isLocalVarTopLevelFunctionBodyStmt()) &&
              ((IGosuProgramInternal)getGosuClass()).isParsingExecutableProgramStatements() &&
              (existingSymbol instanceof DynamicSymbol );
      if( verify( varStmt, bFieldSymbolFromProgram, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier ) )
      {
        // Overwrite program var's symbol with class field symbol
        varStmt.setSymbol( existingSymbol );
      }
    }

    if( varStmt.getModifierInfo() == null )
    {
      varStmt.setModifierInfo( new ModifierInfo(0) );
    }

    TypeLiteral typeLiteral = null;
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseTypeLiteral();
      typeLiteral = (TypeLiteral)popExpression();
    }
    else if( !match( null, "=", SourceCodeTokenizer.TT_OPERATOR, true ) )
    {
      if( match( null, null, '(', true ) )
      {
        parseBlockLiteral();
        typeLiteral = (TypeLiteral)popExpression();
      }
    }

    if( !bClassMember && getGosuClass() instanceof IGosuProgram &&
            getGosuClass().getMemberField( strIdentifier ) != null )
    {
      // Eat the property alias if this is a program field (the class parser already made the property)
      if( match( null, Keyword.KW_as ) )
      {
        match( null, Keyword.KW_readonly );
        match( null, SourceCodeTokenizer.TT_WORD );
      }
    }

    Expression eas = null;
    if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      pushParsingFieldInitializer( varStmt );
      putThisAndSuperSymbols( varStmt.getModifierInfo() ); // assume caller must pushes scope
      try
      {
        parseExpression( typeLiteral == null ? ContextType.EMPTY : new ContextType( typeLiteral.getType().getType() ) );
      }
      finally
      {
        popParsingFieldInitializer();
      }
      eas = popExpression();
      if( eas.hasParseExceptions() )
      {
        if( typeLiteral != null )
        {
          IType typeCast = typeLiteral.getType().getType();
          eas.getParseExceptions().get( 0 ).setExpectedType( typeCast );
        }
      }
      detectLikelyJavaCast(eas);
    }

    verify( varStmt, eas != null || typeLiteral != null, Res.MSG_VARIABLE_TYPE_OR_VALUE_REQUIRED );
    if( typeLiteral != null )
    {
      verify( typeLiteral, typeLiteral.getType().getType() != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
    }

    // Create a local symbol for the identifier part of the 'var' statement
    // (so it can be legally referenced in the containing scope).
    IType type = null;
    if( eas != null )
    {
      type = eas.getType();
      if( type == null )
      {
        type = ErrorType.getInstance();
      }
    }
    if( typeLiteral != null )
    {
      type = typeLiteral.getType().getType();
    }
    else if( bClassMember && eas != null )
    {
      IPropertyInfo varProperty = getGosuClass().getTypeInfo().getProperty(getGosuClass(), strIdentifier);
      if( varProperty instanceof IGosuVarPropertyInfo )
      {
        // Assign inferred type to var property corresponding with this field
        IGosuVarPropertyInfo vpi = (IGosuVarPropertyInfo) varProperty;
        vpi.assignActualType( eas.getType() );
        vpi.assignSymbolType( eas.getType() );
      }
    }

    verify( varStmt, !JavaTypes.pVOID().equals(type) && !JavaTypes.VOID().equals( type ), Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE );


    //if no type was found, we have added an error so give the symbol the error type
    if( type == null )
    {
      type = ErrorType.getInstance();
    }

    ISymbol symbol;
    if( bClassMember || bFieldSymbolFromProgram )
    {
      symbol = varStmt.getSymbol();
      symbol.setType( type );
      varStmt.setType( type );
    }
    else
    {
      Symbol newSym = new Symbol( strIdentifier, type, _symTable, null );
      newSym.setModifierInfo( varStmt.getModifierInfo() );
      symbol = newSym;
      varStmt.setSymbol( newSym );
    }

    if( existingSymbol == null )
    {
      _symTable.putSymbol( symbol );
    }

    eas = possiblyWrapWithImplicitCoercion( eas, type );
    varStmt.setAsExpression( eas );
    varStmt.setTypeLiteral( typeLiteral );
    varStmt.setScriptPart( getScriptPart() );
    varStmt.setDefinitionParsed( true );

    boolean bHideFieldForEditorParsing = getGosuClass() != null && getGosuClass().isCreateEditorParser() && bFieldSymbolFromProgram;
    if( bHideFieldForEditorParsing )
    {
      pushStatement( new HideFieldNoOpStatement( varStmt ) );
    }
    else
    {
      pushStatement( varStmt );
    }
  }

  private boolean isLocalVarTopLevelFunctionBodyStmt() {
    if( _symTable.getScopeCount() > 1 ) {
      return _symTable.peekScope( 1 ).getActivationCtx() != null;
    }
    return false;
  }

  private void detectLikelyJavaCast(Expression eas) {
    if( eas instanceof IParenthesizedExpression || (eas instanceof IImplicitTypeAsExpression && ((IImplicitTypeAsExpression)eas).getLHS() instanceof IParenthesizedExpression) ) {
      IParenthesizedExpression parenExpr;
      if(eas instanceof IParenthesizedExpression) {
        parenExpr = (IParenthesizedExpression)eas;
      } else {
        parenExpr = (IParenthesizedExpression)((IImplicitTypeAsExpression)eas).getLHS();
      }
      if (parenExpr.getExpression() instanceof TypeLiteral) {
        IType castType = eas.getType();
        if(castType instanceof IMetaType) {
          castType = ((IMetaType)castType).getType();
        }
        eas.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_LIKELY_JAVA_CAST, castType.getName() ) );
      }
    }
  }

  private boolean recoverFromJavaStyleCast( Expression eas )
  {
    if( eas instanceof IParenthesizedExpression &&
            ((IParenthesizedExpression)eas).getExpression() instanceof TypeLiteral )
    {
      if( getTokenizer().getLineNumber() == eas.getLocation().getLineNum() )
      {
        // Something follows a java-style-cast-looking expression on same line, so assume it's a java-style cast...
        IType castType = eas.getType();
        if( castType instanceof IMetaType ){
          castType = ((IMetaType) castType).getType();
        }
        int mark = getTokenizer().mark();
        parseUnaryExpression();
        Expression maybeRealEas = popExpression();
        if( maybeRealEas.hasParseExceptions() )
        {
          _locations.remove( maybeRealEas.getLocation() );
          getTokenizer().restoreToMark( mark );
        }
        else
        {
          popExpression();
          eas.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_LIKELY_JAVA_CAST, castType.getName() ) );
          eas = possiblyWrapWithImplicitCoercion( maybeRealEas, castType );
          pushExpression( eas );
          return true;
        }
      }
    }
    return false;
  }

  DynamicPropertySymbol parseVarPropertyClause( VarStatement varStmt, String strVarIdentifier, IType varType, boolean parseInitializer )
  {
    if( !match( null, Keyword.KW_as ) )
    {
      return null;
    }

    boolean bReadonly = match( null, Keyword.KW_readonly ) || varStmt.isFinal();

    final int iNameStart = getTokenizer().getTokenStart();
    Token T = new Token();
    varStmt.setHasProperty( true );
    String strPropertyName = null;
    if( verify( varStmt, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_PROPERTY ) )
    {
      strPropertyName = T._strValue;
      warn( varStmt, !Keyword.isKeyword( strPropertyName ), Res.MSG_IMPROPER_USE_OF_KEYWORD, strPropertyName );
    }

    String strIdentifier = strPropertyName == null ? "" : strPropertyName;
    Token restoreState = getTokenizer().getPriorToken();
    addNameInDeclaration( strIdentifier,
            T._iDocPosition,
            restoreState.getLine(), restoreState.getTokenColumn(), strIdentifier.length() > 0 );

    varStmt.setPropertyName( strPropertyName );
    varStmt.setNameOffset(iNameStart, strPropertyName );
    ISymbol symbol = getSymbolTable().getSymbol( strPropertyName );
    if( symbol != null && !symbol.getDisplayName().equals( strPropertyName ) )
    {
      // Force case sensitivity, mainly to make overrides consistent
      symbol = null;
    }
    if( symbol instanceof DynamicPropertySymbol &&
        symbol.getGosuClass() == getGosuClass() &&
        (((DynamicPropertySymbol)symbol).getVarIdentifier() != null &&
        !((DynamicPropertySymbol)symbol).getVarIdentifier().equals( strVarIdentifier) ) )
    {
      varStmt.addParseException( new ParseException( makeFullParserState(), Res.MSG_PROPERTY_ALREADY_DEFINED, strPropertyName) );
    }

    ICompilableType gsClass = getGosuClass();
    DynamicPropertySymbol dps;
    if( symbol instanceof DynamicPropertySymbol )
    {
      dps = new DynamicPropertySymbol( (DynamicPropertySymbol)symbol );
      if( dps.getGetterDfs() == null || dps.getGetterDfs().getScriptPart().getContainingType() != gsClass )
      {
        VarPropertyGetFunctionSymbol getFunctionSymbol = new VarPropertyGetFunctionSymbol( gsClass, getSymbolTable(), strPropertyName, strVarIdentifier, varType );
        getFunctionSymbol.getModifierInfo().setAnnotations( varStmt.getAnnotations() );
        getFunctionSymbol.getModifierInfo().setDescription( varStmt.getModifierInfo().getDescription() );
        getFunctionSymbol.setClassMember( true );
        if (dps.getGetterDfs() != null) {
          getFunctionSymbol.setOverride(true);
          getFunctionSymbol.setSuperDfs(dps.getGetterDfs());
        }
        getFunctionSymbol.setStatic( varStmt.isStatic() );
        dps.setGetterDfs( getFunctionSymbol );
        verifyFunction( getFunctionSymbol, varStmt );
      }
    }
    else
    {
      VarPropertyGetFunctionSymbol getFunctionSymbol = new VarPropertyGetFunctionSymbol( gsClass, getSymbolTable(), strPropertyName, strVarIdentifier, varType );
      getFunctionSymbol.getModifierInfo().setAnnotations( varStmt.getAnnotations() );
      getFunctionSymbol.getModifierInfo().setDescription( varStmt.getModifierInfo().getDescription() );
      getFunctionSymbol.setClassMember( true );
      getFunctionSymbol.setStatic( varStmt.isStatic() );
      verifyFunction( getFunctionSymbol, varStmt );
      dps = new DynamicPropertySymbol( getFunctionSymbol, true );
    }
    if( !bReadonly && (dps.getSetterDfs() == null || dps.getSetterDfs().getScriptPart().getContainingType() != gsClass) )
    {
      VarPropertySetFunctionSymbol setFunctionSymbol = new VarPropertySetFunctionSymbol( gsClass, getSymbolTable(), strPropertyName, strVarIdentifier, varType );
      setFunctionSymbol.getModifierInfo().setAnnotations( varStmt.getAnnotations() );
      setFunctionSymbol.getModifierInfo().setDescription( varStmt.getModifierInfo().getDescription() );
      setFunctionSymbol.setClassMember( true );
      setFunctionSymbol.setStatic( varStmt.isStatic() );
      if (dps.getSetterDfs() != null) {
        setFunctionSymbol.setOverride(true);
        setFunctionSymbol.setSuperDfs(dps.getSetterDfs());
      }
      dps.setSetterDfs( setFunctionSymbol );
      verifyFunction( setFunctionSymbol, varStmt );
    }
    dps.setScriptPart( getOwner().getScriptPart() );
    dps.setVarIdentifier( strVarIdentifier );

    if( parseInitializer && match( null, "=", SourceCodeTokenizer.TT_OPERATOR, false ) )
    {
      pushParsingFieldInitializer( varStmt );
      try
      {
        parseExpression( new ContextType( varType ) );
      }
      finally
      {
        popParsingFieldInitializer();
      }
      Expression expression = popExpression();
      verifyComparable( varType, expression );
      expression = possiblyWrapWithImplicitCoercion( expression, varType );
      varStmt.setAsExpression( expression );
    }

    return dps;
  }

  void parseDelegateStatement( DelegateStatement delegateStmt, String strIdentifier )
  {
    if( delegateStmt.getModifierInfo() == null )
    {
      delegateStmt.setModifierInfo( new ModifierInfo(0) );
    }

    TypeLiteral typeLiteral = null;
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseTypeLiteral();
      typeLiteral = (TypeLiteral)popExpression();
      warn( delegateStmt, typeLiteral.getType().getType() != null &&
                          !typeLiteral.getType().getType().equals( getCurrentEnclosingGosuClass() ),
            Res.MSG_DELEGATES_SHOULD_NOT_SELF_DELEGATE );
    }
    ICompilableType gsClass = getGosuClass();
    List<IType> constituents = new ArrayList<IType>();
    if( verify( delegateStmt, match( null, Keyword.KW_represents ), Res.MSG_EXPECTING_REPRESENTS ) )
    {
      do
      {
        getOwner().parseTypeLiteral();
        TypeLiteral ifaceLiteral = (TypeLiteral)popExpression();
        IType iface = ifaceLiteral.getType().getType();
        if( !(iface instanceof ErrorType) )
        {
          verify( ifaceLiteral, iface.isInterface() && !iface.isCompoundType(), Res.MSG_DELEGATES_REPRESENT_INTERFACES_ONLY );
          verify( ifaceLiteral, TypeLord.isDelegatableInterface( gsClass, iface ), Res.MSG_CLASS_DOES_NOT_IMPL, iface );
          verify( typeLiteral, typeLiteral == null || TypeLord.isDelegatableInterface( typeLiteral.getType().getType(), iface ), Res.MSG_CLASS_DOES_NOT_IMPL, iface );
        }
        constituents.add( iface );
      } while( match( null, ',' ) );
    }
    delegateStmt.setConstituents( constituents );

    Expression eas = null;
    if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      pushParsingFieldInitializer( delegateStmt );
      try
      {
        parseExpression( typeLiteral == null ? ContextType.EMPTY : new ContextType( typeLiteral.getType().getType() ) );
      }
      finally
      {
        popParsingFieldInitializer();
      }
      eas = popExpression();
      if( eas.hasParseExceptions() )
      {
        if( typeLiteral != null )
        {
          IType typeCast = typeLiteral.getType().getType();
          eas.getParseExceptions().get( 0 ).setExpectedType( typeCast );
        }
      }
      if( eas instanceof IParenthesizedExpression &&
              ((IParenthesizedExpression)eas).getExpression() instanceof TypeLiteral )
      {
        IType castType = eas.getType();
        if(castType instanceof IMetaType) {
          castType = ((IMetaType)castType).getType();
        }
        eas.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_LIKELY_JAVA_CAST, castType.getName() ) );
      }
    }

    // Create a local symbol for the identifier part of the 'var' statement
    // (so it can be legally referenced in the containing scope).
    IType type = null;
    if( eas != null )
    {
      type = eas.getType();
      if( type == null )
      {
        type = ErrorType.getInstance();
      }
    }
    if( typeLiteral != null )
    {
      IType typeCast = typeLiteral.getType().getType();
      if( eas != null && type != null )
      {
        verifyComparable( typeCast, eas, false, true );
      }
      type = typeCast;
    }
    else if( !(type instanceof ErrorType) )
    {
      if( constituents.isEmpty() )
      {
        type = ErrorType.getInstance();
      }
      else if( constituents.size() == 1 )
      {
        type = constituents.get( 0 );
      }
      else
      {
        type = CompoundType.get( new HashSet<IType>( constituents ) );
      }
    }

    //if no type was found, we have added an error so give the symbol the error type
    if( type == null )
    {
      type = ErrorType.getInstance();
    }

    ISymbol symbol = delegateStmt.getSymbol();
    if( symbol == null )
    {
      symbol = new Symbol( strIdentifier, type, _symTable );
      delegateStmt.setSymbol( symbol );
    }
    else
    {
      symbol.setType( type );
    }
    _symTable.putSymbol( symbol );
    delegateStmt.setType( type );

    eas = possiblyWrapWithImplicitCoercion( eas, type );
    delegateStmt.setAsExpression( eas );
    delegateStmt.setTypeLiteral( typeLiteral );
    delegateStmt.setScriptPart( getScriptPart() );
    pushStatement( delegateStmt );
  }

  private void parseSwitchStatement()
  {
    SwitchStatement switchStmt = new SwitchStatement();

    verify( switchStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_SWITCH );
    parseExpression();
    verify( switchStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_SWITCH );
    verify( switchStmt, match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_SWITCH );
    Expression e = popExpression();

    switchStmt.setSwitchExpression( e );

    _iBreakOk++;
    try
    {
      parseCaseClauses( switchStmt );
      parseDefaultClause( switchStmt, Arrays.asList( switchStmt.getCases() ) );
    }
    finally
    {
      _iBreakOk--;
    }

    verify( switchStmt, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_SWITCH );

    pushStatement( switchStmt );
  }

  private void parseDoWhileStatement()
  {
    DoWhileStatement whileStmt = new DoWhileStatement();
    _ctxInferenceMgr.pushLoopCompromised();
    boolean loopStmtParsed;
    try
    {
      loopStmtParsed = parseLoopStatement();
    }
    finally
    {
      _ctxInferenceMgr.popLoopCompromised();
    }
    if( verify( whileStmt, loopStmtParsed, Res.MSG_EXPECTING_STATEMENT ) )
    {
      verify( whileStmt, match( null, Keyword.KW_while ), Res.MSG_EXPECTING_WHILE_DO );
      Statement stmt = popStatement();

      verify( whileStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_IF );
      parseExpression( ContextType.pBOOLEAN_FALSE );
      // Bad assignment statement in if clause (mistaken for equality "==")
      if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        parseExpression();
        popExpression();
        verify( whileStmt, false, Res.MSG_ASSIGNMENT_IN_LOOP_STATEMENT);
      }

      verify( whileStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_IF );
      Expression e = popExpression();

      whileStmt.setExpression( e );
      verifyLoopConditionNotAlwaysFalse( e );
      whileStmt.setStatement( stmt );
    }
    pushStatement(whileStmt);
  }

  private void verifyLoopConditionNotAlwaysFalse( Expression e )
  {
    verify( e, !e.isCompileTimeConstant() || e.hasParseExceptions() || (boolean)e.evaluate(),
            Res.MSG_CONDITION_IS_ALWAYS_TRUE_FALSE, false );
  }

  private void parseWhileStatement()
  {
    WhileStatement whileStmt = new WhileStatement();
    _ctxInferenceMgr.pushLoopCompromised();
    verify( whileStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_WHILE );
    parseExpression( ContextType.pBOOLEAN_FALSE );
    // Bad assignment statement in if clause (mistaken for equality "==")
    if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseExpression();
      popExpression();
      verify( whileStmt, false, Res.MSG_ASSIGNMENT_IN_LOOP_STATEMENT);
    }
    verify( whileStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_WHILE );
    Expression e = popExpression();
    _ctxInferenceMgr.pushLastCtx();
    try
    {
      whileStmt.setExpression( e );
      verifyLoopConditionNotAlwaysFalse( e );
      if( verify( whileStmt, parseLoopStatement(), Res.MSG_EXPECTING_STATEMENT ) )
      {
        Statement stmt = popStatement();
        whileStmt.setStatement( stmt );
      }
      pushStatement( whileStmt );
    }
    finally
    {
      _ctxInferenceMgr.popCtx( false );
      _ctxInferenceMgr.popLoopCompromised();
    }
  }

  private void parseForEachStatement()
  {
    Token t = new Token();
    ForEachStatement forEachStmt = new ForEachStatement( _symTable );

    verify( forEachStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_FE );

    boolean bLoneInterval = false;
    Expression ein = null;
    if( !match( null, Keyword.KW_var ) )
    {
      // Handle case where no loop var is provided e.g., for( 1..3 ) ...  similar to ruby's 3.times( ... )
      int state = _tokenizer.mark();
      int iLocationsCount = _locations.size();
      parseExpression();
      ein = popExpression();
      bLoneInterval = JavaTypes.NUMBER_INTERVAL().isAssignableFrom( ein.getType() );
      if( !bLoneInterval )
      {
        _tokenizer.restoreToMark( state );
        removeLocationsFrom( iLocationsCount );
      }
    }

    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    if( !bLoneInterval && verify( forEachStmt, match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_FOREACH ) )
    {
      forEachStmt.setNameOffset( iOffset, t._strValue );
      warn( forEachStmt, !Keyword.isKeyword( t._strValue ), Res.MSG_IMPROPER_USE_OF_KEYWORD, t._strValue );
    }
    else
    {
      t._strValue = null;
    }

    _symTable.pushScope();
    try
    {
      String strIdentifier = t._strValue == null ? "" : t._strValue;

      // var decl *expression* is only for editors
      LocalVarDeclaration varDecl = null;
      if( strIdentifier.length() > 0 )
      {
        varDecl = new LocalVarDeclaration( strIdentifier );
        verify( forEachStmt, _symTable.getSymbol( strIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );
        varDecl.setType( ErrorType.getInstance() ); // this is updated below
        pushExpression( varDecl );
        setLocation( iOffset, iLineNum, iColumn, strIdentifier == null, true );
        popExpression();
      }

      if( !bLoneInterval )
      {
        verify( forEachStmt, match( null, Keyword.KW_in ), Res.MSG_EXPECTING_IN_FOREACH );
        parseExpression();
        ein = popExpression();
      }
      IType typeIn = ein.getType();
      verify( ein, LoopStatement.isIteratorType( typeIn ) || typeIn instanceof ErrorType,
              Res.MSG_EXPECTING_ARRAYTYPE_FOREACH, typeIn.getName() );
      forEachStmt.setInExpression( ein );
      forEachStmt.setStructuralIterable( StandardCoercionManager.isStructurallyAssignable_Laxed( JavaTypes.ITERABLE(), typeIn ) );
      if( strIdentifier != null )
      {
        // Create a temporary symbol for the identifier part of the foreach statement
        // (so it can be legally referenced in the statement).
        IType typeIdentifier = LoopStatement.getArrayComponentType( typeIn );
        if( strIdentifier.length() > 0 )
        {
          varDecl.setType( typeIdentifier );
        }

        Symbol symbol = new Symbol( bLoneInterval ? ("_unused_loop_var_" + iOffset) : strIdentifier, typeIdentifier, _symTable, null );
        _symTable.putSymbol( symbol );

        forEachStmt.setIdentifier( symbol );
      }

      boolean foundIterator = false;
      if( match( null, Keyword.KW_iterator ) )
      {
        foundIterator = true;
        parseIteratorVar( forEachStmt, typeIn );
      }

      if( match( null, Keyword.KW_index ) )
      {
        parseIndexVar( forEachStmt );
      }

      if( !foundIterator && match( null, Keyword.KW_iterator ) )
      {
        foundIterator = true;
        parseIteratorVar( forEachStmt, typeIn );
      }

      if( bLoneInterval &&  foundIterator )
      {
        addError( forEachStmt, Res.MSG_FOREACH_ITERATOR_NOT_ALLOWED );
      }

      verify( forEachStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_FE );

      if( strIdentifier != null )
      {
        _ctxInferenceMgr.pushLoopCompromised();
        boolean bHasBody;
        try
        {
          bHasBody = parseLoopStatement();
        }
        finally
        {
          _ctxInferenceMgr.popLoopCompromised();
        }
        verify( forEachStmt, bHasBody, Res.MSG_EXPECTING_STATEMENT );

        if( bHasBody )
        {
          Statement stmt = popStatement();
          forEachStmt.setStatement( stmt );
        }
      }

      pushStatement( forEachStmt );
    }
    finally
    {
      _symTable.popScope();
    }
  }

  private void parseIndexVar( ForEachStatement forEachStmt ) {
    int iOffset;
    int iLineNum;
    int iColumn;
    iOffset = getTokenizer().getTokenStart();
    iLineNum = getTokenizer().getLineNumber();
    iColumn = getTokenizer().getTokenColumn();

    Token Tindex = new Token();
    if( verify( forEachStmt, match( Tindex, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_FOREACH_INDEX ) )
    {
      warn( forEachStmt, !Keyword.isKeyword( Tindex._strValue ), Res.MSG_IMPROPER_USE_OF_KEYWORD, Tindex._strValue );
    }

    String strIndexIdentifier = Tindex._strValue;

    // index decl *expression* is only for editors
    LocalVarDeclaration varIndexDecl = new LocalVarDeclaration( strIndexIdentifier == null ? "#err" : strIndexIdentifier );
    varIndexDecl.setType( JavaTypes.pINT() );
    verify( forEachStmt, _symTable.getSymbol( strIndexIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIndexIdentifier );

    forEachStmt.setIndexNameOffset( Tindex.getTokenStart() );

    // Create a temporary symbol for the identifier part of the foreach statement's index
    // (so it can be legally referenced in the statement).
    Symbol indexIdentifier = new TypedSymbol( strIndexIdentifier, JavaTypes.pINT(), _symTable, null, SymbolType.FOREACH_VARIABLE );
    indexIdentifier.setFinal( true );
    _symTable.putSymbol( indexIdentifier );
    forEachStmt.setIndexIdentifier( indexIdentifier );

    pushExpression( varIndexDecl );
    setLocation( iOffset, iLineNum, iColumn, strIndexIdentifier == null, false );
    popExpression();
  }

  private void parseIteratorVar( ForEachStatement forEachStmt, IType type )
  {
    int iOffset;
    int iLineNum;
    int iColumn;
    iOffset = getTokenizer().getTokenStart();
    iLineNum = getTokenizer().getLineNumber();
    iColumn = getTokenizer().getTokenColumn();

    Token Titer = new Token();
    if( verify( forEachStmt, match( Titer, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_FOREACH_ITERATOR ) )
    {
      warn( forEachStmt, !Keyword.isKeyword( Titer._strValue ), Res.MSG_IMPROPER_USE_OF_KEYWORD, Titer._strValue );
    }

    String strIterIdentifier = Titer._strValue;

    boolean bIterable = JavaTypes.ITERABLE().isAssignableFrom( type ) || forEachStmt.isStructuralIterable();
    verify( forEachStmt, bIterable, Res.MSG_ITERATOR_SYMBOL_ONLY_SUPPORTED_ON_ITERABLE_OBJECTS );
    IType iterType;
    if( bIterable )
    {
      IType iterParam = LoopStatement.getArrayComponentType( type );
      iterType = JavaTypes.ITERATOR().getParameterizedType( iterParam );
    }
    else
    {
      iterType = ErrorType.getInstance();
    }

    // index decl *expression* is only for editors
    LocalVarDeclaration varIteratorDecl = new LocalVarDeclaration( strIterIdentifier == null ? "#err" : strIterIdentifier );
    varIteratorDecl.setType( iterType );
    verify( forEachStmt, _symTable.getSymbol( strIterIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIterIdentifier );


    forEachStmt.setIndexNameOffset( Titer.getTokenStart() );

    Symbol iteratorIdentifier = new TypedSymbol( strIterIdentifier, iterType, _symTable, null, SymbolType.FOREACH_VARIABLE );
    iteratorIdentifier.setFinal( true );
    _symTable.putSymbol( iteratorIdentifier );
    forEachStmt.setIteratorIdentifier( iteratorIdentifier );

    pushExpression( varIteratorDecl );
    setLocation( iOffset, iLineNum, iColumn, strIterIdentifier == null, false );
    popExpression();
  }

  private void parseReturnStatement()
  {
    ReturnStatement returnStmt = new ReturnStatement();
    IType returnType = null;
    if( isParsingBlock() )
    {
      returnType = _blockReturnTypeStack.peek();
    }
    else if( isParsingFunction() )
    {
      if( _bProgramCallFunction && getGosuClass() instanceof IGosuProgram )
      {
        returnType = ((IGosuProgram)getGosuClass()).getExpectedReturnType();
      }
      if( returnType == null )
      {
        returnType = peekParsingFunction().getReturnType();
      }
    }
    else if ( isParsingProgram() )
    {
      returnType = peekParsingProgram().getDeclaredReturnType();
    }

    verify( returnStmt, _iReturnOk > 0, Res.MSG_RETURN_NOT_ALLOWED_HERRE );
    if( match( null, ';' ) || match( null, null, '}', true ) )
    {
      boolean bShouldNotHaveReturnValue = _bProgramCallFunction || returnType == null || returnType == GosuParserTypes.NULL_TYPE();
      FunctionType functionType = isParsingFunction() ? peekParsingFunction() : null;
      boolean bConstructor = !bShouldNotHaveReturnValue && functionType != null && functionType.getMethodOrConstructorInfo() instanceof IConstructorInfo;
      verify( returnStmt, bShouldNotHaveReturnValue || bConstructor, Res.MSG_MISSING_RETURN_VALUE );
      setReturnNullExpr( returnStmt, _bProgramCallFunction );
    }
    else if( returnType != GosuParserTypes.NULL_TYPE() && !inConstructorCtx() )
    {
      parseExpression( new ContextType( returnType ) );
      Expression retValue = popExpression();
      if( returnType != null && !isParsingBlock() )
      {
        Expression actualReturnExpr = retValue;
        if( retValue instanceof ImplicitTypeAsExpression )
        {
          actualReturnExpr = ((ImplicitTypeAsExpression)retValue).getLHS();
        }
        boolean bVoidReturnType = actualReturnExpr.getType() == GosuParserTypes.NULL_TYPE() &&
                !(actualReturnExpr instanceof NullExpression);
        if( bVoidReturnType )
        {
          //noinspection ThrowableResultOfMethodCallIgnored
          actualReturnExpr.removeParseWarning( Res.MSG_USING_VOID_RETURN_TYPE_FROM_NON_NULL_EXPR );
          verify( actualReturnExpr, !bVoidReturnType, Res.MSG_USING_VOID_RETURN_TYPE_FROM_NON_NULL_EXPR );
        }
      }
      returnStmt.setValue( retValue );
      if( (returnType == null || _bProgramCallFunction) && retValue.getType() == JavaTypes.pVOID() )
      {
        retValue.setType( JavaTypes.OBJECT() );
      }
    }
    else
    {
      setReturnNullExpr( returnStmt, _bProgramCallFunction );
    }

    pushStatement( returnStmt );
  }

  private boolean inConstructorCtx()
  {
    if( isParsingConstructor() )
    {
      return _blocks == null || _blocks.isEmpty();
    }
    return false;
  }

  private void setReturnNullExpr( ReturnStatement returnStmt, boolean bProgramCallFunction )
  {
    if( bProgramCallFunction )
    {
      NullExpression nullExpr = new NullExpression();
      nullExpr.setType( JavaTypes.OBJECT() );
      returnStmt.setValue( nullExpr );
    }
    else
    {
      returnStmt.setValue( NullExpression.instance() );
    }
  }

  private void parseThrowStatement()
  {
    parseExpression();
    Expression e = popExpression();
    if( !JavaTypes.THROWABLE().isAssignableFrom( e.getType() ) )
    {
      verifyComparable( JavaTypes.STRING(), e );
      e = possiblyWrapWithImplicitCoercion( e, JavaTypes.STRING() );
    }

    ThrowStatement throwStmt = new ThrowStatement();
    throwStmt.setExpression( e );

    pushStatement( throwStmt );
  }

  private void parseTryCatchFinallyStatement()
  {
    Token t = new Token();
    TryCatchFinallyStatement tryCatchFinallyStmt = new TryCatchFinallyStatement();

    if( verify( tryCatchFinallyStmt, match( null, null, '{', true ), Res.MSG_EXPECTING_LEFTBRACE_STMTBLOCK ) )
    {
      parseStatement();
      Statement tryStmt = popStatement();
      tryCatchFinallyStmt.setTryStatement( tryStmt );
    }

    for( int iCatchOffset = _tokenizer.getTokenStart(),
                 iCatchLineNum = _tokenizer.getLineNumber(),
                 iCatchColumn = getTokenizer().getTokenColumn();

         match( null, Keyword.KW_catch );

         iCatchOffset = _tokenizer.getTokenStart(),
                 iCatchLineNum = _tokenizer.getLineNumber(),
                 iCatchColumn = getTokenizer().getTokenColumn())
    {
      CatchClause catchClause = new CatchClause();
      _symTable.pushScope();
      try
      {
        verify( tryCatchFinallyStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_CATCH );
        match( null, Keyword.KW_var );

        int iOffset = _tokenizer.getTokenStart();
        int iLineNum = _tokenizer.getLineNumber();
        int iColumn = getTokenizer().getTokenColumn();

        if( verify( tryCatchFinallyStmt, match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_CATCH ) )
        {
          catchClause.setNameOffset( iOffset, t._strValue );
        }
        String strIdentifier = t._strValue;

        LocalVarDeclaration localVarDecl = new LocalVarDeclaration( strIdentifier == null ? "#err" : strIdentifier );
        IType iIntrinsicType;
        if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          parseTypeLiteral();
          TypeLiteral typeLiteral = (TypeLiteral)popExpression();
          iIntrinsicType = typeLiteral.getType().getType();
          verify( typeLiteral, (JavaTypes.THROWABLE().isAssignableFrom( iIntrinsicType ) && !(iIntrinsicType instanceof ITypeVariableType)) || iIntrinsicType instanceof IErrorType,
                  Res.MSG_NOT_A_VALID_EXCEPTION_TYPE, iIntrinsicType.getName() );
        }
        else
        {
          if( !CommonServices.getEntityAccess().getLanguageLevel().supportsNakedCatchStatements() )
          {
            warn( localVarDecl, false, Res.MSG_EXPLICIT_TYPE_RECOMMENDED_FOR_CATCH_STMTS );
          }
          iIntrinsicType = null;
        }
        pushExpression( localVarDecl );
        localVarDecl.setType( iIntrinsicType == null ? ErrorType.getInstance() : iIntrinsicType );
        setLocation( iOffset, iLineNum, iColumn, strIdentifier == null, false );
        popExpression();

        verify( tryCatchFinallyStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_CATCH );
        verify( tryCatchFinallyStmt, _symTable.getSymbol( strIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );
        Symbol symbol = new TypedSymbol( strIdentifier, iIntrinsicType != null ? iIntrinsicType : JavaTypes.THROWABLE(), _symTable, null, SymbolType.CATCH_VARIABLE);
        _symTable.putSymbol( symbol );
        if( tryCatchFinallyStmt.getCatchStatements() != null )
        {
          for( CatchClause c : tryCatchFinallyStmt.getCatchStatements() )
          {
            IType earlierCatchType = c.getCatchType() != null ? c.getCatchType() : CatchClause.getNakedCatchExceptionType();
            IType currentCatchType = iIntrinsicType != null ? iIntrinsicType : CatchClause.getNakedCatchExceptionType();
            if( earlierCatchType.isAssignableFrom( currentCatchType ) )
            {
              verify( catchClause, false, Res.MSG_CATCH_STMT_CANNOT_EXECUTE );
            }
          }
        }
        if( verify( tryCatchFinallyStmt, match( null, null, '{', true ), Res.MSG_EXPECTING_LEFTBRACE_STMTBLOCK ) )
        {
          parseStatement();
          Statement catchStmt = popStatement();
          catchClause.init( iIntrinsicType, catchStmt, symbol );
          pushStatement( catchClause );
          setLocation( iCatchOffset, iCatchLineNum, iCatchColumn );
          popStatement();
          tryCatchFinallyStmt.addCatchClause( catchClause );
        }
      }
      finally
      {
        _symTable.popScope();
      }
    }
    if( match( null, Keyword.KW_finally ) )
    {
      int originaliBreakOk = _iBreakOk;
      _iBreakOk = 0;
      int originaliContinueOk = _iContinueOk;
      _iContinueOk = 0;
      int originalReturnOk = _iReturnOk;
      _iReturnOk = 0;
      try
      {
        if( verify( tryCatchFinallyStmt, match( null,  null, '{', true ), Res.MSG_EXPECTING_LEFTBRACE_STMTBLOCK ) )
        {
          parseStatement();
          Statement finallyStmt = popStatement();
          tryCatchFinallyStmt.setFinallyStatement( finallyStmt );
        }
      }
      finally
      {
        _iBreakOk = originaliBreakOk;
        _iContinueOk = originaliContinueOk;
        _iReturnOk = originalReturnOk;
      }
    }
    if( tryCatchFinallyStmt.getCatchStatements() == null &&
            tryCatchFinallyStmt.getFinallyStatement() == null )
    {
      tryCatchFinallyStmt.addParseException( new ParseException( makeFullParserState(), Res.MSG_CATCH_OR_FINALLY_REQUIRED ) );
    }
    pushStatement( tryCatchFinallyStmt );
  }

  private void parseIfStatement()
  {
    IfStatement ifStmt = new IfStatement();

    verify( ifStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_IF );
    parseExpression( ContextType.pBOOLEAN_FALSE );
    Expression e = popExpression();

    // Bad assignment statement in if clause (mistaken for equality "==")
    if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      parseExpression();
      popExpression();
      verify( ifStmt, false, Res.MSG_ASSIGNMENT_IN_IF_STATEMENT);
    }

    verify( ifStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_IF );

    getSymbolTable().pushScope();
    boolean statementParsed = false;
    try
    {
      _ctxInferenceMgr.pushLastCtx();
      statementParsed = parseStatement();
    }
    finally
    {
      getSymbolTable().popScope();
      _ctxInferenceMgr.popCtx( false );
    }
    verify( ifStmt, statementParsed, Res.MSG_EXPECTING_STATEMENT );
    if( statementParsed )
    {
      Statement stmt = popStatement();

      ifStmt.setExpression( e );
      ifStmt.setStatement( stmt );

      // Swallow a semicolon if necessary
      match( null, ';' );

      if( match( null, Keyword.KW_else ) )
      {
        getSymbolTable().pushScope();
        boolean elseStmtParsed;
        try
        {
          elseStmtParsed = parseStatement();
        }
        finally
        {
          getSymbolTable().popScope();
        }
        verify( ifStmt, elseStmtParsed, Res.MSG_EXPECTING_STATEMENT );
        if( elseStmtParsed )
        {
          ifStmt.setElseStatement( popStatement() );
        }
      }
    }

    pushStatement( ifStmt );
  }

  private void parseUsingStatement()
  {
    UsingStatement usingStmt = new UsingStatement();

    verify( usingStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_USING );

    _symTable.pushScope();
    try
    {
      parseVarStatementsInUsingStatement( usingStmt );
      List<IVarStatement> varStatements = usingStmt.getVarStatements();
      for(IVarStatement vs : varStatements)
      {
        ((VarStatement)vs).setFinal(true);
        verify( usingStmt, vs.getHasInitializer(), Res.MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT, vs.getSymbol().getName() );
      }
      if( usingStmt.getVarStatements().isEmpty() )
      {
        parseExpression();
        Expression expr = popExpression();
        usingStmt.setExpression( expr );
        verifyTypeForUsingStatementPredicate( expr, expr.getType() );
      }

      verify( usingStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_USING );
      if( verify( usingStmt, match( null, null, '{', true ), Res.MSG_EXPECTING_LEFTBRACE_STMTBLOCK ) )
      {
        parseStatement();
        usingStmt.setStatement( popStatement() );
      }
      if( match( null, Keyword.KW_finally ))
      {
        if( verify( usingStmt, match( null,  null, '{', true ), Res.MSG_EXPECTING_LEFTBRACE_STMTBLOCK ) )
        {
          parseStatement();
          Statement finallyStmt = popStatement();
          usingStmt.setFinallyStatement( finallyStmt );
        }
      }
      pushStatement( usingStmt );
    }
    finally
    {
      _symTable.popScope();
    }
  }

  private void parseVarStatementsInUsingStatement( UsingStatement usingStmt )
  {
    Token T = new Token();
    List<IVarStatement> varStmts = new ArrayList<IVarStatement>();
    int iOffset;
    int iLineNum;
    int iColumn;
    do
    {
      iOffset = getTokenizer().getTokenStart();
      iLineNum = getTokenizer().getLineNumber();
      iColumn = getTokenizer().getTokenColumn();
      if( match( null, Keyword.KW_var ) )
      {
        VarStatement varStmt = new VarStatement();
        int iNameOffset = getTokenizer().getTokenStart();
        if( verify( varStmt, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_VAR ) )
        {
          varStmt.setNameOffset( iNameOffset, null );
        }
        else
        {
          T._strValue = null;
        }
        parseVarStatement( varStmt, T, false );
        setLocation( iOffset, iLineNum, iColumn );
        varStmt = (VarStatement)popStatement();
        verifyTypeForUsingStatementPredicate( varStmt, varStmt.getType() );
        varStmts.add( varStmt );
      }
      else
      {
        break;
      }
    }while( match( null, ',' ) );

    if( varStmts.size() > 0 )
    {
      usingStmt.setVarStatements( varStmts );

      // Add a synthetic noop-stmt to avoid "multiple stmts on same line" warning wrt
      // the using stmt's var stmts and its body e.g., if the stmt body stmt list puts
      // an opening brace on same line as vars
      pushStatement( new NoOpStatement() );
      setLocation( getTokenizer().getTokenStart(), iLineNum, getTokenizer().getTokenColumn(), true );
      popStatement();
    }
  }

  private void verifyTypeForUsingStatementPredicate( ParsedElement pe, IType type )
  {
    maybeRemoveIMonitorLockError( pe );
    boolean bAssignableFromUsingType =
            isAssignableFrom(JavaTypes.LOCK(), type) ||
                    isAssignableFrom(JavaTypes.getJreType(Closeable.class), type ) ||
                    isAssignableFrom(JavaTypes.getGosuType( IReentrant.class ), type ) ||
                    isAssignableFrom(GosuTypes.IDISPOSABLE(), type ) ||
                    type == GosuTypes.IMONITORLOCK() ||
                    type.getTypeInfo().getMethod( "dispose" ) != null ||
                    type.getTypeInfo().getMethod( "close" ) != null ||
                    (type.getTypeInfo().getMethod( "lock" ) != null && type.getTypeInfo().getMethod( "unlock" ) != null);

    verify( pe, bAssignableFromUsingType, Res.MSG_BAD_TYPE_FOR_USING_STMT );
  }

  private boolean isAssignableFrom(IType type1, IType type2) {
    return type1 != null && type1.isAssignableFrom( type2 );
  }

  private void maybeRemoveIMonitorLockError( ParsedElement pe )
  {
    if( pe instanceof TypeAsExpression )
    {
      ParseTree after = pe.getLocation().getChildAfter(((TypeAsExpression) pe).getLHS().getLocation());
      if( after != null && after.getParsedElement() instanceof TypeLiteral )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        after.getParsedElement().removeParseException(Res.MSG_IMONITOR_LOCK_SHOULD_ONLY_BE_USED_WITHIN_USING_STMTS);
      }
    }
    else if( pe instanceof VarStatement )
    {
      maybeRemoveIMonitorLockError( ((VarStatement)pe).getAsExpression() );
    }
    else if( pe instanceof ParenthesizedExpression )
    {
      maybeRemoveIMonitorLockError( ((ParenthesizedExpression)pe).getExpression() );
    }
  }

  private void parseStatementBlock()
  {
    parseStatementBlock( true );
  }
  private void parseStatementBlock( boolean bMatchClosingBrace )
  {
    _symTable.pushScope();
    if( !bMatchClosingBrace )
    {
      decStatementDepth();
    }
    try
    {
      ArrayList<Statement> statements = new ArrayList<Statement>();
      parseStatementsAndDetectUnreachable( statements );

      StatementList stmtList = new StatementList( _symTable );
      verify( stmtList, !bMatchClosingBrace || match( null, '}' ), Res.MSG_EXPECTING_RIGHTBRACE_STMTBLOCK );
      stmtList.setStatements( statements );

      pushStatement( isDontOptimizeStatementLists() ? stmtList : stmtList.getSelfOrSingleStatement() );
    }
    finally
    {
      if( !bMatchClosingBrace )
      {
        incStatementDepth();
      }
      _symTable.popScope();
    }
  }

  void parseNamespaceStatement()
  {
    if( isEditorParser() )
    {
      parseNamespaceStatement_editor();
    }
    else
    {
      parseNamespaceStatement_normal();
    }
  }
  void parseNamespaceStatement_editor()
  {
    NamespaceStatement namespaceStmt = new NamespaceStatement();
    parseExpression();
    Expression namespace = popExpression();
    //noinspection ThrowableResultOfMethodCallIgnored
    namespace.clearParseExceptions(); //Res.MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME );
    IGosuClassInternal gsClass = (IGosuClassInternal)getScriptPart().getContainingType();
    String strNamespace = namespace.toString();
    verify( namespaceStmt, strNamespace.equals( gsClass.getNamespace() ), Res.MSG_WRONG_NAMESPACE, strNamespace, gsClass.getNamespace() );
    setNamespace( strNamespace );
    namespaceStmt.setNamespace( strNamespace );
    pushStatement( namespaceStmt );
    while( match( null, ';' ) )
    {
      //pushStatement( new NoOpStatement() );
    }
  }
  void parseNamespaceStatement_normal()
  {
    NamespaceStatement namespaceStmt = new NamespaceStatement();
    int mark = getTokenizer().mark();
    String strToken = null;
    if( verify( namespaceStmt, match( null, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_TYPELITERAL_OR_NAMESPACE ) )
    {
      strToken = getTokenizer().getTokenAt( mark ).getStringValue();
    }
    String strNamespace = parseDotPathWord( strToken );
    strNamespace = strNamespace == null ? "" : strNamespace;
    IGosuClassInternal gsClass = (IGosuClassInternal)getScriptPart().getContainingType();
    verify( namespaceStmt, strNamespace.equals( gsClass.getNamespace() ), Res.MSG_WRONG_NAMESPACE, strNamespace, gsClass.getNamespace() );
    setNamespace( strNamespace );
    namespaceStmt.setNamespace( strNamespace );
    pushStatement( namespaceStmt );
    while( match( null, ';' ) )
    {
      //pushStatement( new NoOpStatement() );
    }
  }

  @Override
  public UsesStatementList parseUsesStatementList( boolean bResolveUsesTypes )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    int iUsesListOffset = iOffset;
    int iUsesListLineNum = iLineNum;
    if( match( null, Keyword.KW_uses, true ) )
    {
      List<IUsesStatement> usesList = new ArrayList<IUsesStatement>();
      UsesStatementList stmtList = new UsesStatementList();
      stmtList.setUsesStatements( usesList );
      while( match( null, Keyword.KW_uses ) )
      {
        getOwner().parseUsesStatement( bResolveUsesTypes );
        setLocation( iOffset, iLineNum, iColumn );
        UsesStatement stmt = (UsesStatement)popStatement();
        //noinspection ThrowableResultOfMethodCallIgnored
        stmt.removeParseWarningRecursively( Res.MSG_DEPRECATED_MEMBER ); // don't show these in uses statements
        IUsesStatement duplicate = stmtList.conflicts( stmt );
        if( duplicate != null )
        {
          if( warn( stmt, !duplicate.getTypeName().equals( stmt.getTypeName() ), Res.MSG_USES_STMT_DUPLICATE ) )
          {
            verify( stmt, false, Res.MSG_USES_STMT_CONFLICT, duplicate.getTypeName() );
          }
        }
        usesList.add( stmt );
        getOwner().checkInstruction( true );
        iOffset = getTokenizer().getTokenStart();
        iLineNum = getTokenizer().getLineNumber();
      }
      pushStatement( stmtList );
      setLocation( iUsesListOffset, iUsesListLineNum, iColumn, true );
      popStatement();
      return stmtList;
    }
    return null;
  }

  void parseUsesStatement()
  {
    parseUsesStatement( true );
  }

  void parseUsesStatement( boolean bResolveTypes )
  {
    if( isEditorParser() )
    {
      parseUsesStatement_editor( bResolveTypes );
    }
    else
    {
      parseUsesStatement_normal( bResolveTypes );
    }
  }
  void parseUsesStatement_editor( boolean bResolveTypes )
  {
    UsesStatement usesStmt = new UsesStatement();
    parseTypeLiteral();
    TypeLiteral typeLiteral = (TypeLiteral)popExpression();
    String t = typeLiteral.getType().getType() instanceof ErrorType && typeLiteral.getPackageExpression() != null
              ? typeLiteral.getPackageExpression().toString()
              : TypeLord.getPureGenericType( typeLiteral.getType().getType() ).getName();
    boolean bForwardRefToInnerClass = getGosuClass() instanceof IGosuClassInternal && t != null && t.startsWith( getGosuClass().getName() );
    verify( usesStmt, t == null || !t.endsWith( "]" ), Res.MSG_BAD_NAMESPACE, t );
    if( !bForwardRefToInnerClass || ((IGosuClassInternal)getGosuClass()).isHeaderCompiled() )
    {
      if( t.endsWith( "*" ) && match( null, "*", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        typeLiteral.clearParseExceptions();
        usesStmt.setTypeName( t );
        if( verify( usesStmt, t.endsWith( ".*" ), Res.MSG_BAD_NAMESPACE, t ) )
        {
          String namespace = t.substring( 0, t.length() - 2 );
          IType type = TypeSystem.getNamespace( namespace );
          if( type == null )
          {
            type = TypeSystem.getByFullNameIfValid( namespace );
          }
          verify( usesStmt, type != null, Res.MSG_BAD_NAMESPACE, namespace );
        }
        getTypeUsesMap().addToTypeUses( usesStmt );
      }
      else
      {
        if( bResolveTypes )
        {
          String strTypeName = TypeLord.getPureGenericType( typeLiteral.getType().getType() ).getName();
          usesStmt.setTypeName( strTypeName );

          if( typeLiteral.hasParseExceptions() )
          {
            IParseIssue first = typeLiteral.getParseExceptions().get( 0 );
            usesStmt.addParseException( first );
            //noinspection ThrowableResultOfMethodCallIgnored
            typeLiteral.removeParseException( first.getMessageKey() );
          }
          else
          {
            getTypeUsesMap().addToTypeUses( usesStmt );
            ICompilableTypeInternal gsClass = getGosuClass();
            if( gsClass != null )
            {
              verify( typeLiteral, !typeLiteral.getType().getType().getRelativeName().equals( gsClass.getRelativeName() ),
                      Res.MSG_SAME_NAME_AS_CLASS, gsClass.getRelativeName() );
            }
          }
        }
        else
        {
          usesStmt.setTypeName( t );
          getTypeUsesMap().addToTypeUses( usesStmt );
        }
      }
    }
    pushStatement( usesStmt );
    while( match( null, ';' ) )
    {
      //pushStatement( new NoOpStatement() );
    }
  }
  void parseUsesStatement_normal( boolean bResolveTypes )
  {
    UsesStatement usesStmt = new UsesStatement();
    int iOffset = _tokenizer.getTokenStart();
    int iLineNum = _tokenizer.getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    int mark = getTokenizer().mark();
    String t = "";
    if( verify( usesStmt, match( null, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_TYPELITERAL_OR_NAMESPACE ) )
    {
      t = getTokenizer().getTokenAt( mark ).getStringValue();
    }
    t = parseDotPathWord( t );
    boolean bForwardRefToInnerClass = getGosuClass() instanceof IGosuClassInternal && t != null && t.startsWith( getGosuClass().getName() );
    if( !bForwardRefToInnerClass || ((IGosuClassInternal)getGosuClass()).isHeaderCompiled() )
    {
      if( match( null, "*", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        usesStmt.setTypeName( t + "*" );
        if( verify( usesStmt, t.endsWith( "." ), Res.MSG_BAD_NAMESPACE, t ) )
        {
          String namespace = t.substring( 0, t.length() - 1 );
          IType type = TypeSystem.getNamespace( namespace );
          if( type == null )
          {
            type = TypeSystem.getByFullNameIfValid( namespace );
          }
          verify( usesStmt, type != null, Res.MSG_BAD_NAMESPACE, namespace );
        }
        getTypeUsesMap().addToTypeUses( usesStmt );
      }
      else
      {
        if( bResolveTypes )
        {
          TypeLiteral typeLiteral = resolveTypeLiteral( t, false, false );
          pushExpression( typeLiteral );
          setLocation( iOffset, iLineNum, iColumn );
          popExpression();

          IType type = TypeLord.getPureGenericType( (typeLiteral.getType()).getType() );
          String strTypeName = type.getName();
          usesStmt.setTypeName( strTypeName );

          if( typeLiteral.hasParseExceptions() )
          {
            IParseIssue first = typeLiteral.getParseExceptions().get( 0 );
            usesStmt.addParseException( first );
            //noinspection ThrowableResultOfMethodCallIgnored
            typeLiteral.removeParseException(first.getMessageKey());
          }
          else
          {
            getTypeUsesMap().addToTypeUses( usesStmt );
            ICompilableTypeInternal gsClass = getGosuClass();
            if( gsClass != null )
            {
              verify( typeLiteral, !typeLiteral.getType().getType().getRelativeName().equals( gsClass.getRelativeName() ),
                      Res.MSG_SAME_NAME_AS_CLASS, gsClass.getRelativeName() );
            }
          }
        }
        else
        {
          usesStmt.setTypeName( t );
          getTypeUsesMap().addToTypeUses( usesStmt );
        }
      }
    }
    pushStatement( usesStmt );
    while( match( null, ';' ) )
    {
      //pushStatement( new NoOpStatement() );
    }
  }

  void parseCaseClauses( SwitchStatement switchStmt )
  {
    List<CaseClause> cases = new ArrayList<CaseClause>();
    while( parseCaseClause( switchStmt, cases ) )
    { /* do nothing */ }
    switchStmt.setCases( cases.toArray( new CaseClause[cases.size()] ) );
  }

  boolean parseCaseClause( SwitchStatement switchStmt, List<CaseClause> cases )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLine = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    if( !match( null, Keyword.KW_case ) )
    {
      return false;
    }

    if( !cases.isEmpty() )
    {
      warnIfCaseNotTerminated( cases.get( cases.size()-1 ).getStatements() );
    }

    Expression switchExpr = switchStmt.getSwitchExpression();
    parseExpression( new ContextType( switchExpr.getType() ) );
    verify( switchStmt, match( null, ":", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_CASE_COLON );
    Expression e = popExpression();
    verifyCaseIsUnique( e, cases );
    boolean typeInferred = switchExpr instanceof TypeOfExpression && e instanceof TypeLiteral && isIsolatedCase( cases );
    List<Statement> statements = new ArrayList<Statement>();
    CaseClause caseClause = new CaseClause( e, statements );
    cases.add( caseClause );
    if( typeInferred )
    {
      IType caseExprType = (IType)e.evaluate();
      Expression typeOfExpr = ((TypeOfExpression)switchExpr).getExpression();
      typeInferred = checkComparableAndCastable( typeOfExpr, e );
      if( verify( e, typeInferred, Res.MSG_TYPE_MISMATCH, typeOfExpr.getType().getDisplayName(), caseExprType.getDisplayName() ) )
      {
        TypeOfExpression toe = (TypeOfExpression)switchExpr;
        _ctxInferenceMgr.pushCtx();
        _ctxInferenceMgr.updateType( toe.getExpression(), caseExprType );
      }
    }

    _symTable.pushScope();
    boolean typeInferenceCancelled = false;
    try
    {
      for( Statement stmt = null; true; )
      {
        // must cancel inference before the next case or default clause is parsed
        if( match( null, Keyword.KW_case, true ) ||
                match( null, Keyword.KW_default, true ) )
        {
          if( typeInferred )
          {
            _ctxInferenceMgr.popCtx( false );
            typeInferenceCancelled = true;
          }
          break;
        }

        if( !parseStatement() )
        {
          break;
        }
        stmt = popStatementAndDetectUnreachable( stmt, statements );
      }
    }
    finally
    {
      _symTable.popScope();
      if( typeInferred && !typeInferenceCancelled )
      {
        _ctxInferenceMgr.popCtx( false );
      }
    }
    pushExpression( caseClause );
    setLocation( iOffset, iLine, iColumn, true );
    popExpression();
    return true;
  }

  private void verifyCaseIsUnique( Expression e, List<CaseClause> cases )
  {
    if( e.getType() instanceof IErrorType || !e.isCompileTimeConstant() && !(e instanceof Literal)  )
    {
      if( e instanceof ImplicitTypeAsExpression )
      {
        verifyCaseIsUnique( ((ImplicitTypeAsExpression)e).getLHS(), cases );
      }
      return; // Can't verify this
    }

    Object value;
    try {
      value = e.evaluate();
      if( value == null && e instanceof TypeAsExpression ) {
        // sometimes a coercer isn't available in a compile-time environment, so bail if null
        return;
      }
    }
    catch( Exception err ) {
      return;
    }

    for( CaseClause cc: cases )
    {
      Expression expr = cc.getExpression();
      if( expr instanceof ImplicitTypeAsExpression )
      {
        expr = ((ImplicitTypeAsExpression)expr).getLHS();
      }
      if( expr != null && !expr.hasParseExceptions() && (expr.isCompileTimeConstant() || expr instanceof Literal) ) {
        Object csr;
        try {
          csr = expr.evaluate();
        }
        catch( Exception err ) {
          // skip over evaluation errors
          continue;
        }
        verify( e,
                !GosuObjectUtil.equals( csr, value ),
                Res.MSG_DUPLICATE_CASE_EXPRESSION );
      }
    }
  }

  private boolean isIsolatedCase( List<CaseClause> cases )
  {
    if( cases.isEmpty() )
    {
      return true;
    }
    else
    {
      CaseClause caseClause = cases.get( cases.size() - 1 );
      List<Statement> statements = caseClause.getStatements();
      int i;
      for( i = statements.size()-1; i >= 0 && statements.get( i ) instanceof NoOpStatement; i-- )
      { /* Loop until we find the index of the last non-noop statement */ }
      if( i >= 0 )
      {
        boolean[] bAbsolute = {false};
        return statements.get( i ).getLeastSignificantTerminalStatement( bAbsolute ) != null && bAbsolute[0];
      }
    }
    return false;
  }

  private void warnIfCaseNotTerminated( List<Statement> statements )
  {
    int i;
    for( i = statements.size()-1; i >= 0 && statements.get( i ) instanceof NoOpStatement; i-- )
    { /* Loop until we find the index of the last non-noop statement */ }
    if( i >= 0 )
    {
      Statement lastStmt = statements.get( i );
      boolean[] bAbsolute = {false};
      verifyOrWarn( lastStmt, lastStmt.hasParseExceptions() || lastStmt.getLeastSignificantTerminalStatement( bAbsolute ) != null && bAbsolute[0],
              true, Res.MSG_NONTERMINAL_CASE_CLAUSE );
    }
  }

  boolean parseDefaultClause( SwitchStatement switchStmt, List<CaseClause> cases )
  {
    if( !match( null, Keyword.KW_default ) )
    {
      return false;
    }

    if( cases.size() > 0 )
    {
      warnIfCaseNotTerminated( cases.get( cases.size()-1 ).getStatements() );
    }

    verify( switchStmt, match( null, ":", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_CASE_COLON );
    verify( switchStmt, switchStmt.getDefaultStatements() == null, Res.MSG_MULTIPLE_DEFAULT_CLAUSES_NOT_PERMITTED );
    _symTable.pushScope();
    try
    {
      List<Statement> defaultStatements = new ArrayList<Statement>();
      parseStatementsAndDetectUnreachable( defaultStatements );
      switchStmt.setDefaultStatements( defaultStatements );
    }
    finally
    {
      _symTable.popScope();
    }
    return true;
  }

  void checkInstruction( boolean bProcessDirectives )
  {
    // If the tokenizer has an 'instructor', it may be in a state where
    // it needs to analyze tokens in a separate context e.g., in a template
    // an expressoin of the form <%=foo.bar%>. Thus "analyze separately"
    // means to parse these as expressions and throw them away. Note we can
    // throw them away because instructors are indicative of pure analysis
    // of the source; the object code is of no interest e.g., a template
    // verifier may use an instructor.

    checkUnexpectedEof();

    while( !_tokenizer.isEOF() && (_tokenizer.isAnalyzingSeparately() || _tokenizer.isAnalyzingDirective()) )
    {
      if( _tokenizer.isAnalyzingDirective() )
      {
        parseDirective( bProcessDirectives );
      }
      else if( !(getGosuClass() instanceof IGosuClass) ||
              !((CompilationState)((IGosuClass)getGosuClass()).getCompilationState()).isReparsingHeader() )
      {
        // Only eat <%= xx %> expressions *after* the header is parsed during definition phase.
        // They need to be parsed within the program evaluate() body parsing phase, which includes class members etc.

        int iOffset = _tokenizer.getTokenStart();

        parseExpression();
        Expression e = popExpression();

        clearExpressionInTemplateUnlessParsingEvaluateFunctionBody( e );

        if( iOffset == _tokenizer.getTokenStart() )
        {
          break;
        }
      }
      else
      {
        break;
      }
    }
  }

  private void checkUnexpectedEof()
  {
    if( _tokenizer.isEOF() && _tokenizer.isAnalyzingSeparately() )
    {
      if( getGosuClass() != null )
      {
        ((ParsedElement)getGosuClass().getClassStatement()).addParseException( makeFullParserState(), Res.MSG_UNEXPECTED_EOF );
      }
    }
  }

  private void clearExpressionInTemplateUnlessParsingEvaluateFunctionBody( Expression e )
  {
    // Retain the parse tree (location) for template expressions when we are parsing the
    // template as a class and *only* during the phase where we are parsing the program
    // entry point aka the evaluate() function. Otherwise throw out the parse tree.

    if( !isParsingProgram() &&
            getGosuClass() instanceof IGosuClass &&
            (((IGosuClass)getGosuClass()).getCompilationState().isDeclarationsCompiled() &&
                    !((CompilationState)((IGosuClass)getGosuClass()).getCompilationState()).isReparsingHeader()) )
    {
      if( !isParsingFunction() || (getProgramEntryPointDfs() != null && !peekParsingFunction().equals( getProgramEntryPointDfs().getType() )) )
      {
        e.clearParseExceptions();
        removeLocation( e.getLocation() );

        removeInnerClasses( e );
      }
    }
  }

  private void removeInnerClasses( IParsedElement e ) {
    if( e == null ) {
      return;
    }
    if( e instanceof BlockExpression ) {
      IBlockClass blockGosuClass = ((BlockExpression)e).getBlockGosuClass();
      if( blockGosuClass != null && blockGosuClass.getEnclosingType() != null ) {
        ICompilableTypeInternal enclosingType = (ICompilableTypeInternal)blockGosuClass.getEnclosingType();
        enclosingType.removeBlock( blockGosuClass );
      }
    }
    else if( e instanceof NewExpression && ((NewExpression)e).isAnonymousClass() ) {
      IGosuClassInternal anon = (IGosuClassInternal)((Expression)e).getType();
      if( anon != null ) {
        ((IGosuClassInternal)anon.getEnclosingType()).removeInnerClass( anon );
      }
    }
    IParseTree location = e.getLocation();
    if( location != null ) {
      for( IParseTree pt: location.getChildren() ) {
        removeInnerClasses( pt.getParsedElement() );
      }
    }
  }

  private void removeLocation( ParseTree location )
  {
    if( location.getParent() != null )
    {
      location.getParent().removeChild( location );
    }
    getLocationsList().remove( location );
  }

  private void parseDirective( boolean processDirectives )
  {
    final Token token = _tokenizer.getCurrentToken();
    int iOffset = token.getTokenStart();
    int iLineNum = token.getLine();
    int iColumn = token.getTokenColumn();
    DirectiveExpression e = new DirectiveExpression();
    if( Keyword.KW_extends == token.getKeyword() )
    {
      _tokenizer.nextToken();

      parseTypeLiteral();
      Expression typeLiteral = peekExpression();
      if( typeLiteral instanceof TypeLiteral )
      {
        IType extendsType = ((TypeLiteral)typeLiteral).getType().getType();
        if( extendsType instanceof IGosuClassInternal )
        {
          IGosuClassInternal supertype = (IGosuClassInternal)extendsType;
          supertype.putClassMembers( this, _symTable, supertype, true );
          List<? extends GosuClassTypeLoader> typeLoaders = TypeSystem.getCurrentModule().getTypeLoaders( GosuClassTypeLoader.class );
          for( GosuClassTypeLoader typeLoader : typeLoaders )
          {
            List<? extends IGosuEnhancement> enhancementsForType = typeLoader.getEnhancementIndex().getEnhancementsForType( supertype );
            for( IGosuEnhancement enhancement : enhancementsForType )
            {
              if( enhancement instanceof IGosuEnhancementInternal )
              {
                ((IGosuEnhancementInternal)enhancement).putClassMembers( this, _symTable, supertype, true );
              }
            }
          }
          for( Object entryObj : _symTable.getSymbols().entrySet() )
          {
            //noinspection unchecked
            Map.Entry<CharSequence, ISymbol> entry = (Map.Entry<CharSequence, ISymbol>)entryObj;
            if( entry.getValue().isPrivate() )
            {
              _symTable.removeSymbol( entry.getKey() );
            }
          }
        }
      }
    }
    else if( "params".equals( token.getStringValue() ) )
    {
      _tokenizer.nextToken();

      verify( e, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_FUNCTION_DEF );
      ArrayList<ISymbol> params = parseParameterDeclarationList( e, false, null );
      if( processDirectives )
      {
        for( ISymbol param : params )
        {
          getSymbolTable().putSymbol( param );
        }
      }
      verify( e, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF );
    }
    else
    {
      advanceToNextTokenSilently();
      e.addParseException( new ParseException( makeFullParserState(), Res.MSG_BAD_TEMPLATE_DIRECTIVE ) );
    }
    pushExpression( e );
    setLocation( iOffset, iLineNum, iColumn );
    popExpression();
  }

  boolean parseAssignmentOrMethodCall()
  {
    boolean bRet = true;

    Token token = getTokenizer().getCurrentToken();
    switch( token.getType() )
    {
      case SourceCodeTokenizer.TT_KEYWORD:
      case SourceCodeTokenizer.TT_WORD:
      case '(':
      case '"':
        break;

      default:
        return false;
    }

    int initialMark = _tokenizer.mark();

    parsePrimaryExpression();
    Expression e = popExpression();

    if( e instanceof NewExpression )
    {
      // New Statement
      NewStatement stmt = new NewStatement();
      stmt.setNewExpression( (NewExpression)e );
      pushStatement( stmt );
    }
    else if( e instanceof MethodCallExpression )
    {
      // Method Call Statement

      MethodCallStatement stmt = new MethodCallStatement();
      stmt.setMethodCall( (MethodCallExpression)e );
      pushStatement( stmt );
      //noinspection ThrowableResultOfMethodCallIgnored
      e.removeParseException( Res.MSG_VOID_EXPRESSION_NOT_ALLOWED );
    }
    else if( e instanceof BeanMethodCallExpression )
    {
      // Bean Method Call Statement

      BeanMethodCallStatement stmt = new BeanMethodCallStatement();
      stmt.setBeanMethodCall( (BeanMethodCallExpression)e );
      pushStatement( stmt );
      //noinspection ThrowableResultOfMethodCallIgnored
      e.removeParseException(Res.MSG_VOID_EXPRESSION_NOT_ALLOWED);
    }
    else if( e instanceof IBlockInvocation )
    {
      pushStatement( new BlockInvocationStatement( (IBlockInvocation)e ) );
      //noinspection ThrowableResultOfMethodCallIgnored
      e.removeParseException(Res.MSG_VOID_EXPRESSION_NOT_ALLOWED);
    }
    else if( e instanceof Identifier ||
            (e instanceof ImplicitTypeAsExpression && ((ImplicitTypeAsExpression)e).getLHS() instanceof Identifier) )
    {
      Identifier id;
      Statement statement;
      // Assigment Statement
      String assignOp = matchAssignmentOperator();
      if( assignOp != null )
      {
        if( e instanceof ImplicitTypeAsExpression )
        {
          id = (Identifier)((ImplicitTypeAsExpression)e).getLHS();
          IParsedElement parent = ContextInferenceManager.unwrapImplicitTypeAs( ((ImplicitTypeAsExpression)e).getLHS() );
          _locations.remove( e.getLocation() );
          if( parent == null )
          {
            _locations.add( id.getLocation() );
          }
        }
        else
        {
          id = (Identifier)e;
        }
        AssignmentStatement as = new AssignmentStatement();
        statement = as;
        as.setIdentifier( id );

        boolean incrOrDecr = "++".equals( assignOp ) ||  "--".equals( assignOp );
        Expression rhs = parseAssignmentRhs( assignOp, e.getType(), e );
        rhs = buildRhsOfCompoundOperator( e, assignOp, rhs );

        if( rhs.hasParseExceptions() )
        {
          rhs.getParseExceptions().get( 0 ).setExpectedType( id.getType() );
        }

        ISymbol idSym = id.getSymbol();
        verify( as, idSym.isWritable() ||
                (idSym.isFinal() && !idSym.isStatic() && !(idSym instanceof CapturedSymbol) &&
                        ((idSym.isLocal() && !((Symbol)idSym).isImplicitlyInitialized()) ||
                                (isParsingConstructor() || isParsingProgramEvaluateMethod()) && isSymbolInScopeDirectly( idSym ))), // initializing final var is ok
                Res.MSG_PROPERTY_NOT_WRITABLE, idSym.getDisplayName() );

        if( id instanceof PropertyAccessIdentifier )
        {
          DynamicPropertySymbol symbol = (DynamicPropertySymbol)idSym;
          DynamicFunctionSymbol setter = symbol.getSetterDfs();
          if(setter != null && setter.getScriptPart() != null) {
            IType settersOwningType = setter.getScriptPart().getContainingType();
            if( settersOwningType instanceof IGosuClassInternal )
            {
              IGosuClassInternal settersOwningClass = (IGosuClassInternal) settersOwningType;
              ICompilableTypeInternal ctxType = getGosuClass();
              if( ctxType instanceof IGosuClassInternal )
              {
                IGosuClassInternal ctxClass = (IGosuClassInternal)ctxType;
                if( !settersOwningClass.isAccessible( ctxClass, setter ) )
                {
                  id.addParseException( Res.MSG_PROPERTY_NOT_VISIBLE, idSym.getDisplayName() );
                }
              }
            }
          }
        }

        if( rhs instanceof Identifier )
        {
          Identifier identifier = (Identifier)rhs;
          if( idSym != null && idSym.equals( identifier.getSymbol() ) )
          {
            as.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_SILLY_ASSIGNMENT, idSym.getName() ) );
          }
          if( identifier.getSymbol() instanceof DynamicPropertySymbol )
          {
            DynamicPropertySymbol dps = (DynamicPropertySymbol) identifier.getSymbol();
            if( dps.getVarIdentifier() != null && dps.getVarIdentifier().equals( idSym.getName() ) )
            {
              as.addParseWarning( new ParseWarning( makeFullParserState(), Res.MSG_SILLY_ASSIGNMENT, idSym.getName() ) );
            }
          }
        }
        //noinspection ThrowableResultOfMethodCallIgnored
        e.removeParseException( Res.MSG_CANNOT_READ_A_WRITE_ONLY_PROPERTY );

        _ctxInferenceMgr.cancelInferences( id, rhs );
        if(!incrOrDecr || !isPrimitiveOrBoxedIntegerType( rhs.getType() ) || !isPrimitiveOrBoxedIntegerType( id.getType() ) )
        {
          verifyComparable( id.getType(), rhs );
        }
        rhs = possiblyWrapWithImplicitCoercion( rhs, id.getType() );
        as.setExpression( rhs );
      }
      else
      {
        NotAStatement nas = new NotAStatement();
        statement = nas;
        nas.setExpression( e );
        verify( nas, false, Res.MSG_NOT_A_STATEMENT );
      }
      pushStatement( statement );
    }
    else if( e instanceof ParenthesizedExpression )
    {
      NotAStatement nas = new NotAStatement();
      nas.setExpression( e );
      verify( nas, false, Res.MSG_EXPECTING_OPERATOR_TO_FOLLOW_EXPRESSION );
      pushStatement( nas );
    }
    else if( e instanceof SynthesizedMemberAccess )
    {
      SyntheticMemberAccessStatement stmt = new SyntheticMemberAccessStatement((ISynthesizedMemberAccessExpression)e);
      pushStatement( stmt );
    }
    else if( e instanceof MemberAccess ||
            (e instanceof ImplicitTypeAsExpression && ((ImplicitTypeAsExpression)e).getLHS() instanceof MemberAccess))
    {
      // Member Assignment Statement
      MemberAssignmentStatement as = new MemberAssignmentStatement();
      MemberAccess ma;
      if( e instanceof ImplicitTypeAsExpression )
      {
        ma = (MemberAccess)((ImplicitTypeAsExpression)e).getLHS();
        IParsedElement parent = ContextInferenceManager.unwrapImplicitTypeAs( ((ImplicitTypeAsExpression)e).getLHS() );
        _locations.remove( e.getLocation() );
        if( parent == null )
        {
          _locations.add( ma.getLocation() );
        }
      }
      else
      {
        ma = (MemberAccess)e;
      }

      // This tests the validity of the access list
      IType typeExpected = ma.getType();

      String assignOp = matchAssignmentOperator();
      if( verify( as, assignOp != null, Res.MSG_EXPECTING_EQUALS_ASSIGN ) )
      {
        try
        {
          // The only case this can be null is for associative array access.
          if (ma.getMemberName() != null) {
            verifyPropertyWritable( ma.getRootType(), ma.getMemberName(), false );
          }
        }
        catch( Exception ex )
        {
          //noinspection ThrowableResultOfMethodCallIgnored
          ma.addParseException( ParseException.wrap( ex, makeFullParserState() ) );
        }

        Expression rhs = parseAssignmentRhs( assignOp, typeExpected, e );

        _ctxInferenceMgr.cancelInferences( ma, rhs );
        typeExpected = ma.getType(); //update type in case an inference was cancelled
        if( !(typeExpected instanceof ErrorType) )
        {
          IPropertyInfo lhsPi = ma.getPropertyInfo();
          if( lhsPi instanceof IJavaPropertyInfo &&
                  ((IJavaPropertyInfo)lhsPi).getWriteMethodInfo() == null &&
                  ((IJavaPropertyInfo)lhsPi).getPublicField() != null )
          {
            typeExpected = TypeSystem.get(((IJavaPropertyInfo) lhsPi).getPublicField().getType());
          }
        }
        verifyComparable( typeExpected, rhs );

        rhs = buildRhsOfCompoundOperator( e, assignOp, rhs );

        if( rhs.hasParseExceptions() )
        {
          rhs.getParseExceptions().get( 0 ).setExpectedType( typeExpected );
        }
        rhs = possiblyWrapWithImplicitCoercion(rhs, typeExpected);
        as.setExpression( rhs );
        as.setCompoundStatement( !"=".equals( assignOp ) );
      }
      //noinspection ThrowableResultOfMethodCallIgnored
      ma.removeParseException( Res.MSG_CANNOT_READ_A_WRITE_ONLY_PROPERTY );
      as.setRootExpression( ma.getRootExpression() );
      as.setMemberName( ma.getMemberName() );
      as.setMemberExpression( ma.getMemberExpression() );
      as.setMemberAccess( ma );
      pushStatement( as );
    }
    else if( e instanceof ArrayAccess )
    {
      // Array Assignment Statement
      Statement statement;

      ArrayAccess aa = (ArrayAccess)e;
      IType typeExpected = aa.getComponentType();
      Token T = new Token();
      ArrayAssignmentStatement as = new ArrayAssignmentStatement();
      String assignOp = matchAssignmentOperator();
      if( verify( as, assignOp != null, Res.MSG_EXPECTING_EQUALS_ASSIGN ) )
      {
        IType type = aa.getRootExpression().getType();
        verify( as, type != JavaTypes.STRING() && (!JavaTypes.CHAR_SEQUENCE().isAssignableFrom(type) ||
                                                   JavaTypes.STRING_BUILDER().isAssignableFrom(type) ||
                                                   JavaTypes.STRING_BUFFER().isAssignableFrom(type)), Res.MSG_STR_IMMUTABLE );
        Expression rhs = parseAssignmentRhs( assignOp, typeExpected, aa );
        verifyComparable( typeExpected, rhs );
        rhs = buildRhsOfCompoundOperator( e, assignOp, rhs );
        rhs = possiblyWrapWithImplicitCoercion( rhs, typeExpected );

        as.setExpression( rhs );
        as.setArrayAccessExpression( aa );
        as.setCompoundStatement( !"=".equals( T._strValue ) );
        statement = as;
      }
      else
      {
        NotAStatement nas = new NotAStatement();
        statement = nas;
        nas.setExpression( aa );
        verify( nas, false, Res.MSG_NOT_A_STATEMENT );
      }
      pushStatement( statement );
    }
    else if( e instanceof MapAccess )
    {
      // Array Assignment Statement
      Statement statement;

      MapAccess ma = (MapAccess)e;
      IType typeExpected = ma.getComponentType();
      Token T = new Token();
      MapAssignmentStatement as = new MapAssignmentStatement();
      String assignOp = matchAssignmentOperator();
      if( verify( as, assignOp != null, Res.MSG_EXPECTING_EQUALS_ASSIGN ) )
      {
        Expression rhs = parseAssignmentRhs( assignOp, typeExpected, e );
        verifyComparable( typeExpected, rhs );
        rhs = buildRhsOfCompoundOperator( e, assignOp, rhs );
        rhs = possiblyWrapWithImplicitCoercion( rhs, ma.getComponentType() );

        as.setExpression( rhs );
        as.setMapAccessExpression( ma );
        as.setCompoundStatement( !"=".equals( T._strValue ) );
        statement = as;
      }
      else
      {
        NotAStatement nas = new NotAStatement();
        statement = nas;
        nas.setExpression( ma );
        verify( nas, false, Res.MSG_NOT_A_STATEMENT );
      }
      pushStatement( statement );
    }
    else if( e instanceof FeatureLiteral )
    {
      NotAStatement nas = new NotAStatement();
      nas.setExpression( e );
      verify( nas, false, Res.MSG_NOT_A_STATEMENT );
      pushStatement( nas );
    }
    else
    {
      _tokenizer.restoreToMark( initialMark );
      _locations.remove( e.getLocation() );
      bRet = false;
    }

    return bRet;
  }

  private boolean isParsingProgramEvaluateMethod() {
    return isParsingFunction() && getProgramEntryPointDfs() != null &&
            peekParsingFunction().equals( getProgramEntryPointDfs().getType() );
  }

  private boolean isSymbolInScopeDirectly( ISymbol idSym ) {
    if( !(idSym instanceof DynamicSymbol) ) {
      return true;
    }
    DynamicSymbol fieldSym = (DynamicSymbol)idSym;
    IGosuClassInternal declaringClass = fieldSym.getGosuClass();
    return getGosuClass() == declaringClass && !isParsingBlock();
  }

  private Expression parseAssignmentRhs( String operation, IType typeExpected, Expression lhs )
  {
    Expression rhs;
    if( "++".equals( operation ) ||
            "--".equals( operation ) )
    {
      AdditiveExpression add = new AdditiveExpression();
      add.setLHS( lhs );
      Expression one = new NumericLiteral( "1", 1, JavaTypes.pINT() );
      IType type = resolveTypeForArithmeticExpression( lhs, lhs.getType(), operation, lhs.getType());
      pushExpression( one );
      setLocation( lhs.getLocation().getExtent() + 1, lhs.getLineNum(), lhs.getLocation().getColumn() + 1 );
      popExpression();
      one = possiblyWrapWithImplicitCoercion(one, type);
      add.setRHS( one );
      add.setOperator( "++".equals( operation ) ? "+" : "-" );
      add.setType( type);

      pushExpression( add );
      setLocation( lhs.getLocation().getOffset(), lhs.getLineNum(), lhs.getLocation().getColumn()+1 );
      popExpression();

      rhs = add;
    }
    else
    {
      parseExpression( new ContextType( typeExpected ), false );
      rhs = popExpression();
      detectLikelyJavaCast( rhs );
    }
    return rhs;
  }

  private Expression buildRhsOfCompoundOperator( Expression lhs, String assignOp, Expression rhs )
  {
    Expression synthetic = null;
    if( "+=".equals( assignOp ) || "-=".equals( assignOp ) )
    {
      AdditiveExpression add = new AdditiveExpression();
      add.setLHS( lhs );
      add.setRHS( rhs );
      add.setOperator( assignOp.charAt( 0 ) == '+' ? "+" : "-" );
      add.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = add;
    }
    else if( "*=".equals( assignOp ) || "/=".equals( assignOp ) || "%=".equals( assignOp ) )
    {
      MultiplicativeExpression mult = new MultiplicativeExpression();
      mult.setLHS( lhs );
      mult.setRHS( rhs );
      mult.setOperator( String.valueOf( assignOp.charAt( 0 ) ) );
      mult.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = mult;
    }
    else if( "&=".equals( assignOp ) )
    {
      BitwiseAndExpression and = new BitwiseAndExpression();
      lhs = ensureOperandIntOrLong( lhs );
      rhs = ensureOperandIntOrLong( rhs );
      rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );
      and.setLHS( lhs );
      and.setRHS( rhs );
      and.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = and;
    }
    else if( "&&=".equals( assignOp ) )
    {
      ConditionalAndExpression and = new ConditionalAndExpression();
      verifyComparable( JavaTypes.pBOOLEAN(), rhs, true, true );
      rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pBOOLEAN() );
      verifyComparable( JavaTypes.pBOOLEAN(), lhs, true, true );
      lhs = possiblyWrapWithImplicitCoercion( lhs, JavaTypes.pBOOLEAN() );
      and.setLHS( lhs );
      and.setRHS( rhs );
      synthetic = and;
    }
    else if( "^=".equals( assignOp ) )
    {
      BitwiseXorExpression xor = new BitwiseXorExpression();
      lhs = ensureOperandIntOrLong( lhs );
      rhs = ensureOperandIntOrLong( rhs );
      rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );
      xor.setLHS( lhs );
      xor.setRHS( rhs );
      xor.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = xor;
    }
    else if( "|=".equals( assignOp ) )
    {
      BitwiseOrExpression or = new BitwiseOrExpression();
      lhs = ensureOperandIntOrLong( lhs );
      rhs = ensureOperandIntOrLong( rhs );
      rhs = possiblyWrapWithImplicitCoercion( rhs, lhs.getType() );
      or.setLHS( lhs );
      or.setRHS( rhs );
      or.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = or;
    }
    else if( "||=".equals( assignOp ) )
    {
      ConditionalOrExpression or = new ConditionalOrExpression();
      verifyComparable( JavaTypes.pBOOLEAN(), rhs, true, true );
      rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pBOOLEAN() );
      verifyComparable( JavaTypes.pBOOLEAN(), lhs, true, true );
      lhs = possiblyWrapWithImplicitCoercion( lhs, JavaTypes.pBOOLEAN() );
      or.setLHS( lhs );
      or.setRHS( rhs );
      synthetic = or;
    }
    else if( "<<=".equals( assignOp ) || ">>=".equals( assignOp ) || ">>>=".equals( assignOp ) )
    {
      BitshiftExpression shift = new BitshiftExpression();

      verifyTypesComparable( rhs, JavaTypes.pINT(), rhs.getType(), false, true );
      rhs = possiblyWrapWithImplicitCoercion( rhs, JavaTypes.pINT() );

      // Lhs must be an int or a long
      IType lhsType = lhs.getType();
      if( verify( lhs,
              lhsType == JavaTypes.LONG() || lhsType == JavaTypes.pLONG() ||
                      lhsType == JavaTypes.INTEGER() || lhsType == JavaTypes.pINT() ||
                      lhsType == JavaTypes.SHORT() || lhsType == JavaTypes.pSHORT() ||
                      lhsType == JavaTypes.BYTE() || lhsType == JavaTypes.pBYTE(),
              Res.MSG_BITSHIFT_LHS_MUST_BE_INT_OR_LONG ) )
      {
        lhsType = lhsType == JavaTypes.LONG() || lhsType == JavaTypes.pLONG() ? JavaTypes.pLONG() : JavaTypes.pINT();
        lhs = possiblyWrapWithImplicitCoercion( lhs, lhsType );
      }

      shift.setLHS( lhs );
      shift.setRHS( rhs );
      shift.setOperator( assignOp );
      shift.setType( resolveTypeForArithmeticExpression( lhs, lhs.getType(), assignOp, rhs.getType() ) );
      synthetic = shift;
    }

    if( synthetic != null )
    {
      pushExpression( synthetic );
      ParseTree rhsLoc = rhs.getLocation();
      setLocation( rhsLoc.getOffset(), rhs.getLineNum(), rhsLoc.getColumn(), true );
      popExpression();
      rhs = synthetic;
    }

    return rhs;
  }

  private String matchAssignmentOperator()
  {
    Token token = getTokenizer().getCurrentToken();
    if( token.getType() == SourceCodeTokenizer.TT_OPERATOR )
    {
      String value = token.getStringValue();
      switch( value )
      {
        case "=":
        case "+=":
        case "-=":
        case "++":
        case "--":
        case "*=":
        case "%=":
        case "/=":
        case "&=":
        case "&&=":
        case "^=":
        case "|=":
        case "||=":
        case "<<=":
          getTokenizer().nextToken();
          return value;
        default:
          return matchRightShiftAssign();
      }
    }
    return null;
  }

  private String matchRightShiftAssign()
  {
    int mark = getTokenizer().mark();
    if( match( null, ">", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      if( match( null, ">", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        if( match( null, ">", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
          {
            return ">>>=";
          }
        }
        else if( match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          return ">>=";
        }
      }
      getTokenizer().restoreToMark( mark );
    }
    return null;
  }

  //------------------------------------------------------------------------------
  //
  // function-declaration
  //   [modifiers] function <identifier> ( [ <argument-declaration-list> ] ) : <type-literal>
  //
  ISymbol parseFunctionOrPropertyDeclaration( ParsedElement element, boolean bParseProperties, boolean bParseVars )
  {
    ModifierInfo modifiers;
    do
    {
      while( match( null, Keyword.KW_uses ) )
      {
        parseUsesStatement();
        popStatement();
      }

      modifiers = parseModifiers();
      if( match( null, Keyword.KW_function ) )
      {
        DynamicFunctionSymbol symbol = parseFunctionDecl(element, modifiers);
        eatStatementBlock( symbol != null && symbol.getDeclFunctionStmt() != null ? symbol.getDeclFunctionStmt() : element, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
        return symbol;
      }
      if( bParseProperties && match( null, Keyword.KW_property ) )
      {
        boolean bGetter = match( null, Keyword.KW_get );
        boolean bSetter = match( null, Keyword.KW_set );
        // We only care about legal properties
        if (bGetter || bSetter) {
          FunctionStatement fs = new FunctionStatement();
          DynamicFunctionSymbol dfs = getOwner().parseFunctionDecl( fs, true, bGetter, modifiers );
          if( dfs == null )
          {
            element.addParseException( new ParseException( makeFullParserState(), Res.MSG_EXPECTING_DECL ) );
            return null;
          }

          fs.setDynamicFunctionSymbol( dfs );
          pushStatement( fs );
          dfs.setClassMember( true );
          eatStatementBlock( fs, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
          DynamicPropertySymbol dps = getOrCreateDynamicPropertySymbol( element, null, dfs, bGetter );
          dps.setClassMember(false);
          return dps;
        }
      }
      if( bParseVars && match( null, Keyword.KW_var ) )
      {
        VarStatement var = new VarStatement();
        Token T = new Token();
        if( !verify( var, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_VAR ) )
        {
          T._strValue = null;
        }
        parseVarStatement( var, T, false );
        return var.getSymbol();
      }

      if( match( null, SourceCodeTokenizer.TT_EOF ) )
      {
        return null;
      }

      _tokenizer.nextToken();
    } while( true );
  }

  boolean parsePropertyDefinition()
  {
    if( isParsingBlock() )
    {
      return false;
    }
    ModifierInfo modifiers = parseModifiers();
    Token token = getTokenizer().getCurrentToken();
    if( Keyword.KW_property == token.getKeyword() )
    {
      getTokenizer().nextToken();

      boolean bGetter = match( null, Keyword.KW_get );
      boolean bSetter = !bGetter && match( null, Keyword.KW_set );

      token = getTokenizer().getCurrentToken();
      int iOffset = token.getTokenStart();
      int iLineNum = token.getLine();
      int iColumn = token.getTokenColumn();
      FunctionStatement functionStmt = parseBaseFunctionDefinition( null, true, bGetter, modifiers );
      verify( functionStmt, bGetter || bSetter, Res.MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER );
      setLocation( iOffset, iLineNum, iColumn );
      getOwner().popStatement();

      DynamicFunctionSymbol dfs = functionStmt.getDynamicFunctionSymbol();
      if( dfs == null )
      {
        // Note, dfs is null only if the function stmt is errant
        dfs = new DynamicFunctionSymbol( _symTable, "@", new FunctionType( "@", JavaTypes.pVOID(), null, GenericTypeVariable.EMPTY_TYPEVARS ), Collections.<ISymbol>emptyList(), (Statement)null );
        functionStmt.setDynamicFunctionSymbol( dfs );
      }

      IType returnType = functionStmt.getDynamicFunctionSymbol().getReturnType();
      verify( functionStmt, bGetter || returnType == JavaTypes.pVOID(), Res.MSG_PROPERTY_SET_MUST_RETURN_VOID );
      dfs.setClassMember( false );
      if( bGetter && dfs.getArgTypes() != null && dfs.getArgTypes().length > 0 )
      {
        List<IParseTree> children = functionStmt.getLocation().getChildren();
        for( IParseTree child : children )
        {
          if( child.getParsedElement() instanceof ParameterDeclaration )
          {
            child.getParsedElement().addParseException(Res.MSG_GETTER_CANNOT_HAVE_PARAMETERS);
          }
        }
      }

      DynamicPropertySymbol dps = getOrCreateDynamicPropertySymbol( functionStmt, null, dfs, bGetter );
      dps.setClassMember( false );
      getOwner().pushStatement( new PropertyStatement( functionStmt, dps ) );
      return true;
    }
    return false;
  }

  DynamicPropertySymbol getOrCreateDynamicPropertySymbol( ParsedElement parsedElement, IGosuClassInternal gsClass, DynamicFunctionSymbol dfs, boolean bGetter )
  {
    String strPropertyName = dfs.getDisplayName().substring( 1 );
    ISymbol symbol = getSymbolTable().getSymbol( strPropertyName );
    DynamicPropertySymbol dps;
    if( !verify( parsedElement, symbol == null || symbol instanceof DynamicPropertySymbol, Res.MSG_VARIABLE_ALREADY_DEFINED, strPropertyName ) )
    {
      return new DynamicPropertySymbol( dfs, bGetter );
    }
    if( symbol == null ||
            (gsClass != null &&
                    gsClass.getMemberProperty( strPropertyName ) == null &&
                    gsClass.getStaticProperty( strPropertyName ) == null) )
    {
      dps = new DynamicPropertySymbol( dfs, bGetter );
      if( symbol != null )
      {
        assert symbol instanceof DynamicPropertySymbol;
        dps.setParent( (DynamicPropertySymbol)symbol );
      }
      return dps;
    }

    assert symbol instanceof DynamicPropertySymbol;
    dps = (DynamicPropertySymbol)symbol;
    if( bGetter )
    {
      verify( parsedElement,
              dps.getImmediateGetterDfs() == null ||
                      dps.getImmediateGetterDfs() instanceof VarPropertyGetFunctionSymbol ||
                      dps.getImmediateGetterDfs().getValueDirectly() != null ||
                      dps.getImmediateGetterDfs().isAbstract() ||
                      (gsClass != null && gsClass.isInterface()),
              Res.MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED,
              strPropertyName );
      dps.setGetterDfs( dfs );
    }
    else
    {
      verify( parsedElement,
              dps.getImmediateSetterDfs() == null ||
                      dps.getImmediateSetterDfs() instanceof VarPropertySetFunctionSymbol ||
                      dps.getImmediateSetterDfs().getValueDirectly() != null ||
                      dps.getImmediateSetterDfs().isAbstract() ||
                      (gsClass != null && gsClass.isInterface()),
              Res.MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED,
              strPropertyName );
      dps.setSetterDfs( dfs );
    }
    return dps;
  }

  //------------------------------------------------------------------------------
  //
  // function-definition
  //   [modifiers] function <identifier> ( [ <argument-declaration-list> ] ) : <type-literal> statement-block
  //
  boolean parseFunctionDefinition()
  {
    ModifierInfo modifiers = parseModifiers();
    if( !isParsingBlock() && match( null, Keyword.KW_function ) )
    {
      parseBaseFunctionDefinition( new FunctionStatement(), false, false, modifiers );
      return true;
    }
    return false;
  }

  FunctionStatement parseBaseFunctionDefinition(FunctionStatement functionStmt, boolean bProperty, boolean bGetter, ModifierInfo modifiers)
  {
    boolean bNullFunctionStmt = functionStmt == null;
    functionStmt = bNullFunctionStmt ? new FunctionStatement() : functionStmt;

    final Token token = getTokenizer().getCurrentToken();
    int iOffsetName = token.getTokenStart();
    int iLineNumName = token.getLine();
    int iColumnName = token.getTokenColumn();
    String strFunctionName = token.getStringValue();
    boolean bHasName;
    if( bHasName = verify( functionStmt, isWordOrValueKeyword( token ), Res.MSG_EXPECTING_NAME_FUNCTION_DEF ) )
    {
      getTokenizer().nextToken();
      functionStmt.setNameOffset( iOffsetName, strFunctionName );
    }

    addNameInDeclaration( strFunctionName, iOffsetName, iLineNumName, iColumnName, bHasName );

    maybeEatNonDeclKeyword( bHasName, strFunctionName );

    strFunctionName = strFunctionName == null
                      ? ""
                      : bProperty
                        ? "@" + strFunctionName
                        : strFunctionName;

    DynamicFunctionSymbol dfsDecl = findCorrespondingDeclDfs( iOffsetName, modifiers.getModifiers() );
    List<TypeVariableDefinitionImpl> defsFromDecl = getTypeVarDefsFromDecl( dfsDecl );
    List<ITypeVariableDefinitionExpression> typeVarDefs = parseTypeVariableDefs( functionStmt, true, defsFromDecl );
    // Must create function type and assign it as the type var's enclosing
    // type *before* we parse the return type (in case it refs function's type vars)
    IGenericTypeVariable[] typeVars = TypeVariableDefinition.getTypeVars( typeVarDefs );
    if( bProperty && !typeVarDefs.isEmpty() )
    {
      verify( functionStmt, false, Res.MSG_GENERIC_PROPERTIES_NOT_SUPPORTED );
    }

    Expression annotationDefault = null;

    try
    {
      ICompilableTypeInternal gsClass = getGosuClass();
      boolean bAnnotation = gsClass instanceof IGosuClass && ((IGosuClass)gsClass).isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( gsClass );

      verify( functionStmt, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_FUNCTION_DEF );

      ArrayList<ISymbol> args = null;
      Statement statement = null;

      boolean functionBodyParsed = true;
      // Pushing an isolated scope to that the compile-time stack indexes are
      // aligned with runtime e.g., args must be indexed relative to the
      // activation record.

      // We push a scope to parse the function decl, then we pop it to find the dfs, then we push it again to parse the body.
      final IScriptPartId scriptPart = getScriptPart();
      _symTable.pushIsolatedScope( new GosuParserTransparentActivationContext( scriptPart ) );
      try
      {
        IType[] argTypes = null;
        if( !match( null, null, ')', true ) )
        {
          args = parseParameterDeclarationList( functionStmt, Modifier.isStatic( modifiers.getModifiers() ), null, bProperty, bGetter, false, false );
          argTypes = new IType[args.size()];
          for( int i = 0; i < args.size(); i++ )
          {
            _symTable.putSymbol( args.get( i ) );
            argTypes[i] = args.get( i ).getType();
          }
        }
        else
        {
          parseParameterDeclarationList( functionStmt, Modifier.isStatic( modifiers.getModifiers() ), null, bProperty, bGetter, true, false );
          verify ( functionStmt, ! bProperty || bGetter, Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER );
        }
        verify( functionStmt, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF );

        TypeLiteral typeLiteral;
        if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          parseTypeLiteral();
          typeLiteral = (TypeLiteral)popExpression();
          if( bGetter )
          {
            IType returnType = typeLiteral.getType().getType();
            verify( typeLiteral, returnType != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
          }
          if( bAnnotation )
          {
            IType returnType = typeLiteral.getType().getType();
            verify( typeLiteral, isValidAnnotationMethodReturnType( returnType ), Res.MSG_INVALID_TYPE_FOR_ANNOTATION_MEMBER );
          }
        }
        else
        {
          verify( functionStmt, !bAnnotation, Res.MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY );
          String[] fabricatedT = {Keyword.KW_void.toString()};
          typeLiteral = resolveTypeLiteral( fabricatedT );
          verify( functionStmt, !bGetter, Res.MSG_MISSING_PROPERTY_RETURN );
        }

        if( bAnnotation &&
            match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          IType ctxType = typeLiteral.getType().getType();
          if( JavaTypes.ANNOTATION().isAssignableFrom( ctxType ) )
          {
            List<IGosuAnnotation> anno = new ArrayList<IGosuAnnotation>( 1 );
            getOwner().parseAnnotation( anno );
            annotationDefault = (Expression)anno.get( 0 ).getExpression();
          }
          else
          {
            parseExpression( new ContextType( ctxType, false, true ) );
            annotationDefault = popExpression();
          }
          if( !annotationDefault.hasParseExceptions() )
          {
            verify( annotationDefault, annotationDefault.isCompileTimeConstant(), Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED );
          }
        }

        // Note dfsDecl *should have* been assigned prior to parsing args etc.  However it can be the case where
        // e.g., crappy pcf types, the symbol can only be resolved from the _symTable.  Sigh.
        if( !bProperty && dfsDecl == null )
        {
          String functionNameWithArgs = DynamicFunctionSymbol.getSignatureName( strFunctionName == null ? "" : strFunctionName, args );
          DynamicFunctionSymbol dfsInSymTable = (DynamicFunctionSymbol)_symTable.getSymbol( functionNameWithArgs );
          if( dfsInSymTable == null )
          {
            dfsDecl = dfsInSymTable;
          }
          else if( dfsInSymTable.getGosuClass() == gsClass )
          {
            dfsDecl = dfsInSymTable;
            dfsDecl = assignPossibleDuplicateDfs( dfsDecl, _symTable.getSymbols().values() );
          }
        }

        FunctionType type = dfsDecl == null
                            ? new FunctionType( strFunctionName, JavaTypes.pVOID(), null, typeVars )
                            : (FunctionType)dfsDecl.getType();
        if( !bProperty && dfsDecl != null )
        {
          type = (FunctionType)dfsDecl.getType();
          if( typeVarDefs != null )
          {
            for( ITypeVariableDefinitionExpression tvd : typeVarDefs )
            {
              ((ITypeVariableDefinition)tvd).setEnclosingType( type );
            }
          }
        }
        else
        {
          type.setRetType( typeLiteral.getType().getType() );
          type.setArgumentTypes( argTypes );
        }

        type.setScriptPart( scriptPart );
        type.setModifiers( modifiers.getModifiers() );

        putThisAndSuperSymbols( modifiers );

        if( !Modifier.isAbstract( modifiers.getModifiers() ) && !Modifier.isNative( modifiers.getModifiers() ) )
        {
          if( scriptPart == null ||
              !(scriptPart.getContainingType() instanceof IGosuClassInternal) ||
              !scriptPart.getContainingType().isInterface() ||
              match( null, null, '{', true ) ) // default iface method
          {
            if( parseFunctionBody( functionStmt, type ) )
            {
              statement = popStatement();
            }
            else
            {
              functionBodyParsed = false;
            }
          }
        }
      }
      finally
      {
        _symTable.popScope();
      }

      ISymbol functionSymbol = _symTable.getSymbol( strFunctionName );
      if( bProperty )
      {
        if( functionSymbol instanceof DynamicPropertySymbol )
        {
          DynamicPropertySymbol dps = (DynamicPropertySymbol)functionSymbol;
          String functionNameWithArgs = DynamicFunctionSymbol.getSignatureName( strFunctionName, args );
          dfsDecl = (dps == null || !dps.getName().equals( strFunctionName ) )
                  ? (DynamicFunctionSymbol)_symTable.getSymbol( functionNameWithArgs )
                  : dps.getFunction( functionNameWithArgs );
        }
        else if( gsClass == null )
        {
          dfsDecl = findProgramPropertyDfs( strFunctionName, args );
        }
      }

      if( dfsDecl != null &&
          dfsDecl.getDeclFunctionStmt() != null &&
          bNullFunctionStmt )
      {
        FunctionStatement funcStmtFromDecl = dfsDecl.getDeclFunctionStmt();
        if( funcStmtFromDecl.getParent() != null &&
            funcStmtFromDecl.getGosuClass() == gsClass )
        {
          functionStmt = dfsDecl.getDeclFunctionStmt();
        }
      }

      verify( functionStmt, functionBodyParsed, Res.MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY );
      verify( functionStmt, (gsClass != null && gsClass.isAbstract()) || !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS );
      verify( functionStmt, dfsDecl != null, Res.MSG_EXPECTING_NAME_FUNCTION_DEF );
      verify( functionStmt, parsingFunctionsEncloseMyClass(), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_FUNCTION_DEF );

      if( dfsDecl != null )
      {
        if( args != null )
        {
          //replace the decl time arg symbols with the impl time arg symbols
          dfsDecl.getArgs().clear();
          dfsDecl.getArgs().addAll( args );
        }
        // Overwrite annotations to use the new-expressions created in
        dfsDecl.getModifierInfo().setAnnotations( modifiers.getAnnotations() );
        dfsDecl.setAnnotationDefault( annotationDefault );
        dfsDecl.setValueDirectly( statement );
        pushDynamicFunctionSymbol( dfsDecl );
        functionStmt.setDynamicFunctionSymbol( dfsDecl );
        verifyOrWarn( functionStmt, isTerminal( statement, dfsDecl.getReturnType() ),
                !CommonServices.getEntityAccess().isUnreachableCodeDetectionOn(),
                Res.MSG_MISSING_RETURN );
      }
      pushStatement( functionStmt );
      DynamicFunctionSymbol dynamicFunctionSymbol = functionStmt.getDynamicFunctionSymbol();
      if(dynamicFunctionSymbol != null && dynamicFunctionSymbol.getDeclFunctionStmt() == null )
      {
        dynamicFunctionSymbol.setDeclFunctionStmt(functionStmt);
      }
      return functionStmt;
    }
    finally
    {
      for( ITypeVariableDefinitionExpression typeVarDef : typeVarDefs )
      {
        Map<String, ITypeVariableDefinition> typeVarMap = getTypeVariables();
        //noinspection SuspiciousMethodCalls
        if( typeVarMap.containsValue( typeVarDef ) )
        {
          typeVarMap.remove( ((ITypeVariableDefinition)typeVarDef).getName() );
        }
      }
    }
  }

  private boolean isValidAnnotationMethodReturnType( IType returnType )
  {
    return (returnType.isPrimitive() && returnType != JavaTypes.pVOID()) || returnType == JavaTypes.STRING() ||
      returnType.getGenericType() == JavaTypes.CLASS() || JavaTypes.ANNOTATION().isAssignableFrom( returnType ) || returnType.isEnum() ||
      (returnType.isArray() && isValidAnnotationMethodReturnType( returnType.getComponentType()));
  }

  private void putThisAndSuperSymbols( ModifierInfo modifiers )
  {
    if( !Modifier.isStatic( modifiers.getModifiers() ) &&
            getScriptPart() != null &&
            (getScriptPart().getContainingType() instanceof IGosuClassInternal) )
    {
      IGosuClassInternal gsClass = (IGosuClassInternal)getScriptPart().getContainingType();
      IType thisType = gsClass;
      if( gsClass instanceof IGosuEnhancementInternal )
      {
        thisType = ((IGosuEnhancementInternal)gsClass).getEnhancedType();
      }
      if( thisType != null )
      {
        thisType = TypeLord.getConcreteType( thisType );
        getSymbolTable().putSymbol( new ThisSymbol( thisType, _symTable ) );
        if( !(gsClass instanceof IGosuEnhancementInternal) )
        {
          IGosuClassInternal superClass = gsClass.getSuperClass();
          if( superClass == null )
          {
            superClass = IGosuClassInternal.Util.getGosuClassFrom( JavaTypes.OBJECT() );
          }
          getSymbolTable().putSymbol( new Symbol( Keyword.KW_super.getName(), superClass, _symTable, null ) );
        }
      }
    }
  }

  private DynamicFunctionSymbol findProgramPropertyDfs( String strFunctionName, ArrayList<ISymbol> args )
  {
    List<IFunctionSymbol> list = getDfsDeclsForFunction( (String)strFunctionName );
    String propertyNameWithArgs = DynamicFunctionSymbol.getSignatureName( strFunctionName == null ? "" : strFunctionName, args );
    for( IFunctionSymbol func : list )
    {
      if( func instanceof DynamicFunctionSymbol )
      {
        DynamicFunctionSymbol dfs = (DynamicFunctionSymbol)func;
        String sig = DynamicFunctionSymbol.getSignatureName( dfs.getDisplayName(), dfs.getArgs() );
        if( propertyNameWithArgs.equals( sig ) )
        {
          return dfs;
        }
      }
    }
    return null;
  }

  private List<TypeVariableDefinitionImpl> getTypeVarDefsFromDecl( DynamicFunctionSymbol dfsDecl )
  {
    if( dfsDecl == null )
    {
      return Collections.emptyList();
    }

    IGenericTypeVariable[] typeVars = dfsDecl.getType().getGenericTypeVariables();
    if( typeVars == null )
    {
      return Collections.emptyList();
    }

    List<TypeVariableDefinitionImpl> result = new ArrayList<TypeVariableDefinitionImpl>( typeVars.length );
    for( IGenericTypeVariable typeVar : typeVars )
    {
      result.add( (TypeVariableDefinitionImpl)typeVar.getTypeVariableDefinition() );
    }
    return result;
  }

  private DynamicFunctionSymbol findCorrespondingDeclDfs( int iOffsetName, int iModifiers )
  {
    ICompilableTypeInternal gsClass = getGosuClass();
    if( gsClass == null )
    {
      return null;
    }

    Collection<DynamicFunctionSymbol> functions = Modifier.isStatic( iModifiers )
            ? gsClass.getParseInfo().getStaticFunctions()
            : gsClass.getParseInfo().getMemberFunctions().values();
    for( DynamicFunctionSymbol dfs : functions )
    {
      FunctionStatement funcStmt = dfs.getDeclFunctionStmt();
      if( funcStmt != null && funcStmt.getNameOffset( null ) == iOffsetName )
      {
        return dfs;
      }
    }
    return null;
  }

  boolean isDeclarationKeyword( String strKeyword )
  {
    return strKeyword != null &&
           (Keyword.KW_function.equals( strKeyword ) ||
            Keyword.KW_construct.equals( strKeyword ) ||
            Keyword.KW_property.equals( strKeyword ) ||
            //Keyword.KW_var.equals( strFunctionName ) ||
            Keyword.KW_delegate.equals( strKeyword ) ||
            Keyword.KW_class.equals( strKeyword ) ||
            Keyword.KW_interface.equals( strKeyword ) ||
            Keyword.KW_annotation.equals( strKeyword ) ||
            Keyword.KW_structure.equals( strKeyword ) ||
            Keyword.KW_enum.equals( strKeyword ));
  }

  static DynamicFunctionSymbol assignPossibleDuplicateDfs( DynamicFunctionSymbol dfsDecl, Iterable symbols )
  {
    DynamicFunctionSymbol result = dfsDecl;
    if( dfsDecl != null && dfsDecl.getValueDirectly() != null )
    {
      int iMin = Integer.MAX_VALUE;
      for( Object csr : symbols )
      {
        if( csr instanceof DynamicFunctionSymbol )
        {
          DynamicFunctionSymbol dfsCsr = (DynamicFunctionSymbol)csr;
          if( dfsCsr.getName().toLowerCase().contains( "_duplicate_" + dfsDecl.getName().toLowerCase() ) && dfsCsr.getGosuClass() == dfsDecl.getGosuClass() )
          {
            String strName = dfsCsr.getName();
            if( dfsCsr.getValueDirectly() == null )
            {
              int iIndex = Integer.parseInt( strName.substring( 0, strName.indexOf( '_' ) ) );
              if( iIndex < iMin )
              {
                iMin = iIndex;
                result = (DynamicFunctionSymbol)csr;
              }
            }
          }
        }
      }
    }
    return result;
  }

  void addNameInDeclaration( String strName, int iOffsetName, int iLineNumName, int iColumnName, boolean bHasName )
  {
    NameInDeclaration name = new NameInDeclaration( strName );
    pushExpression( name );
    setLocation(  bHasName ? iOffsetName : getTokenizer().getPriorToken().getTokenEnd(), iLineNumName, iColumnName, !bHasName, true );
    popExpression();
  }

  private boolean parsingFunctionsEncloseMyClass()
  {
    if( _parsingFunctions.isEmpty() )
    {
      return true;
    }

    for( FunctionType ft : _parsingFunctions )
    {
      if( ft.getScriptPart().getContainingType() == getGosuClass() )
      {
        return false;
      }
    }

    return true;
  }

  FunctionStatement parseProgramEntryPointBody()
  {
    DynamicFunctionSymbol dfsDecl = getProgramEntryPointDfs();

//    if( _tokenizer.getInstructor() == null )
//    {
//      parseClasspathStatements( true );
//    }

    _symTable.pushIsolatedScope( new GosuParserTransparentActivationContext( getScriptPart() ) );
    boolean bFunctionBodyParsed = true;
    StatementList stmtList = null;
    _bProgramCallFunction = true;
    try
    {
      if( dfsDecl != null && parseProgramFunctionBody( (FunctionType)dfsDecl.getType() ) )
      {
        stmtList = (StatementList)popStatement();
      }
      else
      {
        bFunctionBodyParsed = false;
      }
    }
    finally
    {
      _bProgramCallFunction = false;
      _symTable.popScope();
    }

    FunctionStatement fs = new FunctionStatement();
    fs.setDynamicFunctionSymbol( dfsDecl );

    verify( fs, bFunctionBodyParsed, Res.MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY );

    if (dfsDecl != null) {
      dfsDecl.setValueDirectly( stmtList );
    }
    pushDynamicFunctionSymbol( dfsDecl );
    fs.setDynamicFunctionSymbol( dfsDecl );

//    if( bFunctionBodyParsed )
//    {
    addDefaultReturnStmt( dfsDecl, stmtList );
//    }

    pushStatement( fs );
    return fs;
  }

  private DynamicFunctionSymbol getProgramEntryPointDfs()
  {
    String functionNameWithArgs = DynamicFunctionSymbol.getSignatureName( "evaluate", Collections.<ISymbol>singletonList( new Symbol( "symbols", JavaTypes.IEXTERNAL_SYMBOL_MAP(), null ) ) );
    return (DynamicFunctionSymbol)_symTable.getSymbol( functionNameWithArgs );
  }

  private StatementList handleExpressionStatementList( Expression expr )
  {
    Statement stmt;
    if( expr.hasParseExceptions() || expr.getType() != JavaTypes.pVOID() || expr instanceof NullExpression )
    {
      stmt = wrapProgramExpressionInReturnStmt( expr );
    }
    else if( expr instanceof MethodCallExpression )
    {
      stmt = new MethodCallStatement();
      ((MethodCallStatement)stmt).setMethodCall( (MethodCallExpression)expr );
    }
    else if( expr instanceof BeanMethodCallExpression )
    {
      stmt = new BeanMethodCallStatement();
      ((BeanMethodCallStatement)stmt).setBeanMethodCall( (BeanMethodCallExpression)expr );
    }
    else if( expr instanceof BlockInvocation )
    {
      stmt = new BlockInvocationStatement((BlockInvocation)expr);
    }
    else
    {
      throw new UnsupportedOperationException( "Did not expect expression of type: " + expr.getClass().getName() );
    }
    pushStatement( stmt );
    ParseTree exprLoc = expr.getLocation();
    setLocation( exprLoc.getOffset(), exprLoc.getLineNum(), exprLoc.getColumn(), true );
    popStatement();

    StatementList stmtList = new StatementList( _symTable );
    List<Statement> stmts = Collections.singletonList( stmt );
    stmtList.setStatements( stmts );
    pushStatement( stmtList );
    setLocation( exprLoc.getOffset(), exprLoc.getLineNum(), exprLoc.getColumn(), true );
    return stmtList;
  }

  private ReturnStatement wrapProgramExpressionInReturnStmt( Expression e )
  {
    ReturnStatement retStmt = new ReturnStatement();
    retStmt.setSynthetic( true );
    e = possiblyWrapWithImplicitCoercion( e, JavaTypes.OBJECT() );
    if( e.getType() == JavaTypes.pVOID() )
    {
      e.setType( JavaTypes.OBJECT() );
    }
    retStmt.setValue( e );
    return retStmt;
  }

  private void addDefaultReturnStmt( DynamicFunctionSymbol dfsDecl, StatementList stmtList )
  {
    if( dfsDecl != null && !isTerminal( stmtList, dfsDecl.getReturnType() ) )
    {
      ReturnStatement defaultReturnStmt = new ReturnStatement();
      ImplicitTypeAsExpression ta = new ImplicitTypeAsExpression();
      ta.setLHS( new NullExpression() );
      ta.setType( JavaTypes.OBJECT() );
      ta.setCoercer( IdentityCoercer.instance() );
      defaultReturnStmt.setValue( ta );
      List<Statement> stmts;
      if( stmtList.getStatements() == null )
      {
        //## todo: Probably short-circuit the condition when a program is an empty expression i.e., don't generate a class for it
        stmts = new ArrayList<Statement>( 2 );
      }
      else
      {
        stmts = new ArrayList<Statement>( Arrays.asList( stmtList.getStatements() ) );
      }
      stmts.add( defaultReturnStmt );
      stmtList.setStatements( stmts );
    }
  }

  private boolean parseProgramFunctionBody( FunctionType type )
  {
    maybeSetExternalSymbols();

    pushParsingFunction( type );
    try
    {
      setDontOptimizeStatementLists( true );

      int state = _tokenizer.mark();
      int iLocationsCount = _locations.size();
      try
      {
        parseExpression();
        Expression expr = popExpression();
        verify( expr, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_EXPRESSION );
        ((IGosuProgramInternal)getGosuClass()).setExpression( expr );
        verifyParsedElement( expr, false );
        handleExpressionStatementList( expr );
      }
      catch( ParseResultsException exprErr )
      {
        boolean bProbablyProgram = !getTokenizer().isEOF();
        _tokenizer.restoreToMark( state );
        _stack.clear();
        removeLocationsFrom( iLocationsCount );
        try
        {
          if( !parseStatement( true ) )
          {
            // Couldn't parse a function body at all
            return false;
          }
          verifyParsedElement( peekStatement(), false );
          ((IGosuProgramInternal)getGosuClass()).setStatement( peekStatement() );
        }
        catch( ParseResultsException stmtErr )
        {
          if( !bProbablyProgram )
          {
            // Note we can't just rethrow the original exception because we need
            // the locations etc. in the parser, so we have to reparse and let it throw.

            _tokenizer.restoreToMark( state );
            _stack.clear();
            removeLocationsFrom( iLocationsCount );
            parseExpression();
            Expression expr = popExpression();
            verify( expr, match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_EXPRESSION );
            final IGosuValidator validator = getValidator();
            if( validator != null )
            {
              validator.validate( expr, getScript() );
            }
            handleExpressionStatementList( expr );
          }
//          else
//          {
//            int iProgSourceLen = _tokenizer.getReader().getLength();
//            int iProgStmtExtent = peekStatement().getLocation().getExtent() + 1;
//            if( iProgStmtExtent < iProgSourceLen )
//            {
//              ParseTree loc = peekStatement().getLocation();
//              loc.setLength( iProgSourceLen - loc.getOffset() );
//            }
//          }
        }
      }

      Statement body = peekStatement();
      if( body instanceof StatementList )
      {
        ((StatementList)body).setNoScope();
      }
      return true;
    }
    finally
    {
      popParsingFunction();
    }
  }

  private void maybeSetExternalSymbols() {
    if( getGosuClass() instanceof IGosuProgram )
    {
      ISourceFileHandle sfh = getGosuClass().getSourceFileHandle();
      if( sfh instanceof StringSourceFileHandle )
      {
        ISymbolTable extSyms = ((StringSourceFileHandle)sfh).getExternalSymbols();
        if( extSyms != null )
        {
          // If extSyms is non-null, it usually means this program is for context-sensitive evaluation e.g., in a debugger
          HashMap<String, ISymbol> map = new HashMap<String, ISymbol>();
          //noinspection unchecked
          for( Symbol s: (Collection<Symbol>)extSyms.getSymbols().values() )
          {
            map.put( s.getName(), s );
          }
          ExternalSymbolMapForMap extMap = new ExternalSymbolMapForMap( map );
          ((GosuProgramParseInfo)getGosuClass().getParseInfo()).setExternalSymbols( extMap );
        }
      }
    }
  }

  private void removeLocationsFrom( int iLocationsCount )
  {
    for( int i = _locations.size(); i > iLocationsCount; i-- )
    {
      _locations.remove( i-1 );
    }
  }

  private boolean isTerminal( Statement statement, IType returnType )
  {
    boolean[] bAbsolute = {false};
    return returnType == JavaTypes.pVOID()
            || statement == null
            || statement.getLeastSignificantTerminalStatement( bAbsolute ) != null && bAbsolute[0];
  }

  private boolean parseFunctionBody( FunctionStatement functionStmt, FunctionType type )
  {
    pushParsingFunction( type );
    try
    {
      if( (!(getGosuClass() instanceof IGosuProgram) || !((IGosuProgramInternal)getGosuClass()).isParsingExecutableProgramStatements()) &&
              !match( null, null, '{', true ) )
      {
        Token T = getTokenizer().getCurrentToken();
        eatStatementBlock( functionStmt, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
        NotAStatement nas = new NotAStatement();
        pushStatement( nas );
        setLocation( T.getTokenStart(), T.getLine(), T.getTokenColumn() );
      }
      else if( !parseStatement() )
      {
        return false;
      }

      Statement body = peekStatement();
      if( body instanceof StatementList )
      {
        ((StatementList)body).setNoScope();
      }
      return true;
    }
    finally
    {
      popParsingFunction();
    }
  }

  DynamicFunctionSymbol parseFunctionDecl( ParsedElement element, ModifierInfo modifiers )
  {
    return parseFunctionDecl( element, false, false, modifiers );
  }

  DynamicFunctionSymbol parseFunctionDecl(ParsedElement element, boolean bProperty, boolean bGetter, ModifierInfo modifiers)
  {
    return parseFunctionDecl( element, null, bProperty, bGetter, modifiers );
  }

  DynamicFunctionSymbol parseFunctionDecl( ParsedElement element, String T, boolean bProperty, boolean bGetter, ModifierInfo modifiers )
  {
    _symTable.pushIsolatedScope( new FunctionDeclTransparentActivationContext( getScriptPart() ) );
    try
    {
      boolean bHasName = true;
      int iTokenStart;
      if( T == null )
      {
        int mark = getTokenizer().mark();
        bHasName = verify( element, match( null, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_FUNCTION_DEF );
        Token token = getTokenizer().getTokenAt( mark );
        iTokenStart = token == null ? 0 : token.getTokenStart();
        if( bHasName )
        {
          T = token.getStringValue();
        }
      }
      else
      {
        // This must be the 'construct' token start position
        iTokenStart = getTokenizer().getPriorToken( true ).getTokenStart();
      }
      if( element instanceof IParsedElementWithAtLeastOneDeclaration )
      {
        ((IParsedElementWithAtLeastOneDeclaration)element).setNameOffset( iTokenStart, T );
      }

      String strFunctionName = T;

//      if( strFunctionName == null )
//      {
//        return null;
//      }

      maybeEatNonDeclKeyword( bHasName, strFunctionName );

      strFunctionName = strFunctionName == null ? "" : strFunctionName;
      warn( element, !Keyword.isReservedKeyword( strFunctionName ), Res.MSG_IMPROPER_USE_OF_KEYWORD, strFunctionName );

      ICompilableType gsClass = getGosuClass();
      if( gsClass != null && strFunctionName.equals( gsClass.getRelativeName() ) && gsClass.isEnum() )
      {
        verify( element, Modifier.isPrivate( modifiers.getModifiers() ), Res.MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE );
      }

      List<ITypeVariableDefinitionExpression> typeVarDefs = parseTypeVariableDefs( element, true, null );
      // Must create function type and assign it as the type var's enclosing
      // type *before* we parse the return type (in case it refs function's type vars)
      IGenericTypeVariable[] typeVars = TypeVariableDefinition.getTypeVars( typeVarDefs );

      strFunctionName = !bProperty
              ? strFunctionName
              : ('@' + strFunctionName);

      FunctionType type = new FunctionType( strFunctionName, JavaTypes.pVOID(), null, typeVars );
      type.setEnclosingType( getGosuClass() );

      if( bProperty && !typeVarDefs.isEmpty() )
      {
        verify( element, false, Res.MSG_GENERIC_PROPERTIES_NOT_SUPPORTED );
      }

      try
      {
        boolean bAnnotation = gsClass instanceof IGosuClass && ((IGosuClass)gsClass).isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( gsClass );

        verify( element, match( null, '(' ), Res.MSG_EXPECTING_LEFTPAREN_FUNCTION_DEF );

        ArrayList<ISymbol> params = null;
        IType[] paramTypes = null;
        if( !match( null, ')' ) )
        {
          params = parseParameterDeclarationList( element, Modifier.isStatic( modifiers.getModifiers() ), null, bProperty, bGetter, false, false );
          paramTypes = new IType[params.size()];
          for( int i = 0; i < params.size(); i++ )
          {
            _symTable.putSymbol( params.get( i ) );
            paramTypes[i] = params.get( i ).getType();
          }

          verify( element, match( null, ')' ), Res.MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF );
        }
        else
        {
          verify ( element, ! bProperty || bGetter, Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER );
        }

        TypeLiteral typeLiteral;
        if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          parseTypeLiteral();
          typeLiteral = (TypeLiteral)popExpression();
          if( bGetter )
          {
            IType returnType = typeLiteral.getType().getType();
            verify( typeLiteral, returnType != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );
          }
        }
        else
        {
          verify( element, !bAnnotation, Res.MSG_EXPECTING_RETURN_TYPE_OR_FUN_BODY );
          String[] fakeT = {Keyword.KW_void.toString()};
          typeLiteral = resolveTypeLiteral( fakeT );
          verify( element, !bGetter, Res.MSG_MISSING_PROPERTY_RETURN );
        }

        Expression annotationDefault = null;
        if( gsClass instanceof IGosuClass && ((IGosuClass)gsClass).isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( gsClass ) &&
            match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          IType ctxType = typeLiteral.getType().getType();
          if( JavaTypes.ANNOTATION().isAssignableFrom( ctxType ) )
          {
            List<IGosuAnnotation> anno = new ArrayList<IGosuAnnotation>( 1 );
            getOwner().parseAnnotation( anno );
            annotationDefault = (Expression)anno.get( 0 ).getExpression();
          }
          else
          {
            parseExpression( new ContextType( ctxType, false, true ) );
            annotationDefault = popExpression();
          }
          if( !annotationDefault.hasParseExceptions() )
          {
            verify( annotationDefault, annotationDefault.isCompileTimeConstant(), Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED );
          }
        }

        type.setArgumentTypes( paramTypes );
        type.setRetType( typeLiteral.getType().getType() );

        type.setScriptPart( getScriptPart() );

        DynamicFunctionSymbol dfs = new DynamicFunctionSymbol( _symTable, strFunctionName, type, params, (Statement)null );
        dfs.setScriptPart( getScriptPart() );
        dfs.setModifierInfo( modifiers );
        dfs.setAnnotationDefault( annotationDefault );
        if( element instanceof FunctionStatement )
        {
          dfs.setDeclFunctionStmt( (FunctionStatement)element );
        }
        verifyFunction( dfs, element );

        int iDupIndex = nextIndexOfErrantDuplicateDynamicSymbol( dfs, _dfsDeclByName.get( dfs.getDisplayName() ), true );
        if( iDupIndex >= 0 )
        {
          dfs.renameAsErrantDuplicate( iDupIndex );
        }
        putDfsDeclInSetByName( dfs );
        return dfs;
      }
      finally
      {
        for( ITypeVariableDefinitionExpression typeVarDef : typeVarDefs )
        {
          Map<String, ITypeVariableDefinition> typeVarMap = getTypeVariables();
          if( typeVarMap.containsValue( typeVarDef ) )
          {
            typeVarMap.remove( ((ITypeVariableDefinition)typeVarDef).getName() );
          }
        }
      }
    }
    finally
    {
      _symTable.popScope();
    }
  }

  boolean maybeEatNonDeclKeyword( boolean bHasName, String strFunctionName )
  {
    return !bHasName && strFunctionName != null && strFunctionName.length() > 0 &&
            !isDeclarationKeyword( strFunctionName ) &&
            match( null, SourceCodeTokenizer.TT_KEYWORD );
  }

  private void verifyFunction( DynamicFunctionSymbol dfs, ParsedElement element )
  {
    boolean bValidOverrideFound = false;
    for( IFunctionSymbol existing : getDfsDeclsForFunction( dfs.getDisplayName() ) )
    {
      if( existing instanceof DynamicFunctionSymbol )
      {
        DynamicFunctionSymbol dfsExisting = (DynamicFunctionSymbol)existing;

        // proxies are ignored and names must match exactly (error if they do not?)
        if( existing.getDisplayName().equals( dfs.getDisplayName() ) &&
            areDFSsInSameNameSpace( dfs, dfsExisting ))
        {
          // if the parameters match exactly,
          if( areParametersEquivalent( dfs, dfsExisting ) ||
              !dfs.isStatic() && dfsExisting.isStatic() && dfs.getDeclaringTypeInfo().getOwnersType() instanceof IGosuEnhancement && areParametersEquivalent( dfs, dfsExisting, ((IGosuEnhancement)dfs.getDeclaringTypeInfo().getOwnersType()).getEnhancedType() ) ||
              !dfsExisting.isStatic() && dfs.isStatic() && dfsExisting.getDeclaringTypeInfo().getOwnersType() instanceof IGosuEnhancement && areParametersEquivalent( dfsExisting, dfs, ((IGosuEnhancement)dfsExisting.getDeclaringTypeInfo().getOwnersType()).getEnhancedType() ) )
          {
            IGosuClass owningTypeForDfs = getOwningTypeForDfs( dfsExisting );
            ICompilableTypeInternal gsClass = getGosuClass();
            if( owningTypeForDfs instanceof IGosuEnhancement )
            {
              if( dfs.isOverride() || owningTypeForDfs == gsClass )
              {
                addError( element, Res.MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT );
              }
              else
              {
                warn( element, false, Res.MSG_MASKING_ENHANCEMENT_METHODS_MAY_BE_CONFUSING );
              }
            }
            else
            {
              boolean bSameButNotInSameClass = !GosuObjectUtil.equals( dfsExisting.getScriptPart(), dfs.getScriptPart() );
              if( !verify( element, bSameButNotInSameClass, Res.MSG_FUNCTION_ALREADY_DEFINED, dfs.getMethodSignature(), getScriptPart() ) )
              {
                return;
              }
              if( !verify( element, dfs.isStatic() || !dfsExisting.isStatic(), Res.MSG_FUNCTION_ALREADY_DEFINED, dfs.getMethodSignature(), getScriptPart() ) )
              {
                // non-static method cannot override/shadow static
                return;
              }
              boolean bClassAndReturnTypesCompatible = !GosuObjectUtil.equals( dfsExisting.getScriptPart(), dfs.getScriptPart() ) &&
                      returnTypesCompatible( dfsExisting, dfs );
              if( verify( element, bClassAndReturnTypesCompatible, Res.MSG_FUNCTION_CLASH,
                      dfs.getName(), dfs.getScriptPart(), dfsExisting.getName(), dfsExisting.getScriptPart() ) )
              {
                boolean b = !dfsExisting.isFinal() && (gsClass == null || gsClass.getSupertype() == null || !gsClass.getSupertype().isFinal());
                verify( element, b, Res.MSG_CANNOT_OVERRIDE_FINAL, dfsExisting.getName(), dfsExisting.getScriptPart() );
                if( verify( element, !dfs.isStatic() || dfsExisting.isStatic(), Res.MSG_STATIC_METHOD_CANNOT_OVERRIDE, dfs.getName(), dfsExisting.getDeclaringTypeInfo().getName() ) )
                {
                  if( !dfs.isStatic() && !dfsExisting.isStatic() )
                  {
                    IGosuClassInternal existingDeclaringClass = dfsExisting.getGosuClass();
                    boolean bDefaultMethodOverridesClassMethod = gsClass.isInterface() && !dfs.isAbstract() && existingDeclaringClass != null && existingDeclaringClass.isProxy() && existingDeclaringClass.getJavaType() == JavaTypes.IGOSU_OBJECT();
                    if( verify( element, !bDefaultMethodOverridesClassMethod, Res.MSG_OVERRIDES_OBJECT_METHOD, dfs.getName(), dfsExisting.getDeclaringTypeInfo().getName() ) )
                    {
                      if( !dfs.isOverride() )
                      {
                        boolean bIsConstructorName = gsClass != null && gsClass.getRelativeName().equals( dfs.getDisplayName() );
                        warn( element, bIsConstructorName, Res.MSG_MISSING_OVERRIDE_MODIFIER, dfsExisting.getName(), dfsExisting.getScriptPart().getContainingTypeName() );
                        if( !bIsConstructorName )
                        {
                          // Set the override modifier when the modifier is missing
                          dfs.setOverride( true );
                        }
                      }
                      verifyNotWeakerAccess( element, dfs, dfsExisting );
                      verifySameNumberOfFunctionTypeVars( element, dfs, dfsExisting );
                      dfs.setSuperDfs( dfsExisting );
                      bValidOverrideFound = true;
                    }
                  }
                }
              }
            }
          }
          else
          {
            // if the parameters do not match, but reify to the same IR types, it is an error
            verify( element, !doParametersReifyToSameBytecodeType( dfs, dfsExisting ), Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD );
            verify( element, !propertyTypeDiffers( dfs, dfsExisting ), Res.MSG_PROPERTY_OVERRIDES_WITH_INCOMPATIBLE_TYPE );
            verify( element, dfs.getName().startsWith( "@" ) || // there's already an error re not allowing a default value for property setter
                             !dfs.hasOptionalParameters() && !dfsExisting.hasOptionalParameters(), Res.MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS );
          }
        }
      }
    }
    if( !bValidOverrideFound )
    {
      verifyOverrideNotOnMethodThatDoesNotExtend( element, dfs );
    }
    verifyNoImplicitPropertyMethodConflicts( element, dfs );
  }

  private boolean propertyTypeDiffers( DynamicFunctionSymbol dfs, DynamicFunctionSymbol dfsExisting )
  {
    return dfs.getDisplayName().startsWith( "@" ) &&
           dfs.getArgTypes().length > 0 && dfsExisting.getArgTypes().length > 0 &&
           dfs.getArgTypes()[0] != dfsExisting.getArgTypes()[0];
  }

  private void verifySameNumberOfFunctionTypeVars( ParsedElement element, DynamicFunctionSymbol dfs, DynamicFunctionSymbol dfsExisting )
  {
    FunctionType dfsType = (FunctionType)dfs.getType();
    FunctionType existingDfsType = (FunctionType)dfsExisting.getType();
    IGenericTypeVariable[] typeVars = dfsType.getTypeVariables();
    IGenericTypeVariable[] existingTypeVars = existingDfsType.getTypeVariables();
    verify( element, existingTypeVars.length == typeVars.length, Res.MSG_OVERRIDING_FUNCTION_MUST_HAVE_SAME_NUMBER_OF_TYPE_VARS, existingTypeVars.length );
  }

  private boolean areDFSsInSameNameSpace( IDynamicSymbol newDfs, IDynamicSymbol  existingDfs )
  {
    IGosuClass newType = getOwningTypeForDfs( newDfs );
    IType existingType = getOwningTypeForDfs( existingDfs );
    if( newType == null )
    {
      return existingType == null;
    }
    else if( newType.isAnonymous() || newType.getEnclosingType() != null )
    {
      if( existingType instanceof IGosuEnhancement )
      {
        existingType = ((IGosuEnhancement)existingType).getEnhancedType();
      }
      if( IGosuClass.ProxyUtil.isProxy( existingType ) )
      {
        IType type = IGosuClass.ProxyUtil.getProxiedType( existingType );
        return type.isAssignableFrom( newType ) || JavaTypes.IGOSU_OBJECT().equals( type );
      }
      else
      {
        return existingType.isAssignableFrom( newType );
      }
    }
    else
    {
      return true;
    }
  }

  private void verifyNoImplicitPropertyMethodConflicts( ParsedElement element, DynamicFunctionSymbol dfs )
  {
    String name = dfs.getDisplayName();
    if( name.startsWith( "@" ) )
    {
      String propName = name.substring( 1 );
      if( dfs.getArgs().size() == 0 )
      {
        for( IFunctionSymbol func : getDfsDeclsForFunction( "get" + propName ) )
        {
          if( func instanceof DynamicFunctionSymbol )
          {
            DynamicFunctionSymbol existingDfs = (DynamicFunctionSymbol)func;
            if( areDFSsInSameNameSpace( dfs, existingDfs ) )
            {
              verify( element, existingDfs.getArgs().size() != 0, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, existingDfs.getName(), propName );
            }
          }
        }
      }
      else
      {
        for( IFunctionSymbol func : getDfsDeclsForFunction( "set" + propName ) )
        {
          if( func instanceof DynamicFunctionSymbol )
          {
            DynamicFunctionSymbol existingDfs = (DynamicFunctionSymbol)func;
            if( areDFSsInSameNameSpace( dfs, existingDfs ) )
            {
              verifyPropertySetterConflictsWithFunction( element, dfs, propName, existingDfs );
            }
          }
        }
      }
    }
    else
    {
      if( name.startsWith( "set" ) && dfs.getArgs().size() == 1 )
      {
        ISymbol symbol = getSymbolTable().getSymbol( name.substring( 3, name.length() ) );
        if( symbol instanceof DynamicPropertySymbol )
        {
          DynamicPropertySymbol dps = (DynamicPropertySymbol)symbol;
          if( areDFSsInSameNameSpace( dfs, dps ) )
          {
            verifyFunctionConflictsWithPropoertySetter( element, dfs, dps );
          }
        }
      }
      else
      {
        boolean bIs;
        if( ((bIs = name.startsWith( "is" )) || name.startsWith( "get" )) && dfs.getArgs().size() == 0 )
        {
          ISymbol symbol = getSymbolTable().getSymbol( name.substring( bIs ? 2 : 3, name.length() ) );
          if( symbol instanceof DynamicPropertySymbol )
          {
            DynamicPropertySymbol dps = (DynamicPropertySymbol)symbol;
            if( areDFSsInSameNameSpace( dfs, dps ) )
            {
              DynamicFunctionSymbol getterDfs = dps.getGetterDfs();
              verify( element, getterDfs == null || !NameResolver.getFunctionName( dfs ).equals( NameResolver.getFunctionName( getterDfs ) ),
                      Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, dfs.getName(), dps.getName() );
            }
          }
        }
      }
    }
  }

  void verifyFunctionConflictsWithPropoertySetter( ParsedElement element, DynamicFunctionSymbol dfs, DynamicPropertySymbol dps )
  {
    if( dps.getSetterDfs() != null )
    {
      IType argType = dfs.getArgs().get( 0 ).getType();
      if( argType.equals( dps.getType() ) )
      {
        verify( element, false, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, dfs.getName(), dps.getName() );
      }
      else if( doTypesReifyToTheSameBytecodeType( argType, dps.getType() ) )
      {
        verify( element, false, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION, dfs.getName(), dps.getName() );
      }
    }
  }

  void verifyPropertySetterConflictsWithFunction( ParsedElement element, DynamicFunctionSymbol dfs, String propName, DynamicFunctionSymbol existingDfs )
  {
    if( existingDfs.getArgs().size() == 1 )
    {
      IType argType = dfs.getArgs().get( 0 ).getType();
      IType existingArgType = existingDfs.getArgs().get( 0 ).getType();
      if( argType.equals( existingArgType ) )
      {
        verify( element, false, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, existingDfs.getName(), propName );
      }
      if( doTypesReifyToTheSameBytecodeType( argType, existingArgType ) )
      {
        verify( element, false, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION, existingDfs.getName(), propName );
      }
    }
  }

  private boolean returnTypesCompatible( DynamicFunctionSymbol dfsExisting, DynamicFunctionSymbol dfs )
  {
    IType existingReturnType = maybeResolveFunctionTypeVars( dfsExisting, dfsExisting.getReturnType() );
    IType overrideReturnType = maybeResolveFunctionTypeVars( dfs, dfs.getReturnType() );
    if( existingReturnType.isAssignableFrom( overrideReturnType ) || StandardCoercionManager.isStructurallyAssignable( existingReturnType, overrideReturnType ) )
    {
      return true;
    }
    else {
      return dfs.getReturnType() instanceof ErrorType;
    }
  }

  private boolean areParametersEquivalent( IDynamicFunctionSymbol dfs, IDynamicFunctionSymbol dfsExisting, IType... extraParams )
  {
    IType[] args = ((FunctionType)dfs.getType()).getParameterTypes();
    IType[] toArgs = ((FunctionType)dfsExisting.getType()).getParameterTypes();
    if( extraParams != null && extraParams.length > 0 )
    {
      // these are inserted at beginning, to handle case in enhancement where static function and nonstatic function can conflict in bytecode because both are static and the nonstatic one has an implicit 1st pararm that is the enhanced type
      IType[] argsPlus = new IType[args.length+extraParams.length];
      System.arraycopy( extraParams, 0, argsPlus, 0, extraParams.length );
      System.arraycopy( args, 0, argsPlus, extraParams.length, args.length );
      args = argsPlus;
    }
    return _areParametersEquivalent( dfs, dfsExisting, args, toArgs );
  }

  private boolean _areParametersEquivalent( IDynamicFunctionSymbol dfs1, IDynamicFunctionSymbol dfs2, IType[] args, IType[] toArgs )
  {
    if( args == null || args.length == 0 )
    {
      return toArgs == null || toArgs.length == 0;
    }

    if( toArgs == null )
    {
      return false;
    }

    if( args.length != toArgs.length )
    {
      return false;
    }

    for( int i = 0; i < args.length; i++ )
    {
      IType argType = maybeResolveFunctionTypeVars( dfs1, args[i] );
      IType toArgType = maybeResolveFunctionTypeVars( dfs2, toArgs[i] );
      if( !argType.equals( toArgType ) )
      {
        // Note we use equals() check instead of == to handle non-loadable types e.g., TypeVariableTypes

        return false;
      }
    }

    return true;
  }

  private IType maybeResolveFunctionTypeVars( IDynamicFunctionSymbol dfs, IType type )
  {
    if (TypeSystem.isDeleted(type)) {
      return TypeSystem.getErrorType();
    }
    FunctionType funcType = (FunctionType)dfs.getType();
    ArrayList<IType> functionTypeVars = new ArrayList<IType>();
    IType declaringType = dfs.getScriptPart() == null ? null : dfs.getScriptPart().getContainingType();
    boolean bConstructor = declaringType != null && dfs.getDisplayName().equals( declaringType.getRelativeName() );
    if( bConstructor )
    {
      if( declaringType.isGenericType() && !declaringType.isParameterizedType() )
      {
        for( IGenericTypeVariable tv: declaringType.getGenericTypeVariables() )
        {
          functionTypeVars.add( tv.getTypeVariableDefinition().getType() );
        }
      }
    }
    else
    {
      for( IGenericTypeVariable tv : funcType.getTypeVariables() )
      {
        functionTypeVars.add( tv.getTypeVariableDefinition().getType() );
      }
    }
    return TypeLord.boundTypes( type, functionTypeVars );
  }

  public boolean doParametersReifyToSameBytecodeType( IDynamicFunctionSymbol dfs, IDynamicFunctionSymbol dfsExisting )
  {
    IType[] toArgs = ((FunctionType) dfsExisting.getType()).getParameterTypes();
    IType[] args = ((FunctionType)dfs.getType()).getParameterTypes();
    if( args == null || args.length == 0 )
    {
      return toArgs == null || toArgs.length == 0;
    }

    if( toArgs == null )
    {
      return false;
    }

    if( args.length != toArgs.length )
    {
      return false;
    }

    for( int i = 0; i < args.length; i++ )
    {
      if( !doTypesReifyToTheSameBytecodeType( toArgs[i], args[i] ) )
      {
        return false;
      }
    }

    return true;
  }

  boolean doTypesReifyToTheSameBytecodeType( IType toArg, IType arg )
  {
    IRType toArgType = IRElement.maybeEraseStructuralType( null, IRTypeResolver.getDescriptor( toArg ) );
    IRType argType = IRElement.maybeEraseStructuralType( null, IRTypeResolver.getDescriptor( arg ) );
    return argType.equals( toArgType );
  }

  private IGosuClass getOwningTypeForDfs( IDynamicSymbol dfs )
  {
    if( dfs.getScriptPart() != null && dfs.getScriptPart().getContainingType() instanceof IGosuClass )
    {
      return (IGosuClass)dfs.getScriptPart().getContainingType();
    }
    else
    {
      return null;
    }
  }

  private void verifyNotWeakerAccess( ParsedElement element, DynamicFunctionSymbol dfs, DynamicFunctionSymbol dfsExisting )
  {
    if( dfsExisting.isPublic() )
    {
      verify( element, dfs.isPublic(),
              Res.MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES,
              dfs.getName(), dfs.getScriptPart(),
              dfsExisting.getName(), dfsExisting.getScriptPart() );
    }
    else if( dfsExisting.isProtected() )
    {
      verify( element, dfs.isPublic() || dfs.isProtected(),
              Res.MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES,
              dfs.getName(), dfs.getScriptPart(),
              dfsExisting.getName(), dfsExisting.getScriptPart() );
    }
    else if( dfsExisting.isInternal() )
    {
      verify( element, dfs.isPublic() || dfs.isProtected() || dfs.isInternal(),
              Res.MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES,
              dfs.getName(), dfs.getScriptPart(),
              dfsExisting.getName(), dfsExisting.getScriptPart() );
    }
  }

  //------------------------------------------------------------------------------
  public ArrayList<ISymbol> parseParameterDeclarationList( IParsedElement element, boolean bStatic, List<IType> inferredArgumentTypes )
  {
    return parseParameterDeclarationList( element, bStatic, inferredArgumentTypes, false, false, false, false );
  }

  public ArrayList<ISymbol> parseParameterDeclarationList( IParsedElement element, boolean bStatic, List<IType> inferredArgumentTypes, boolean bProperty, boolean bGetter, boolean bEmpty, boolean bVarDynamicArg )
  {
    ArrayList<ISymbol> params = new ArrayList<ISymbol>();
    Token T = new Token();
    int iParamPos = -1;
    boolean bOptionalParamsStarted = false;

    int iOffsetList = getTokenizer().getTokenStart();
    int iLineNumList = getTokenizer().getLineNumber();
    int iColumnList = getTokenizer().getTokenColumn();

    do
    {
      iParamPos++;
      int iOffsetParam = getTokenizer().getTokenStart();
      int iLineNumParam = getTokenizer().getLineNumber();
      int iColumnParam = getTokenizer().getTokenColumn();

      List<IGosuAnnotation> annotations = parseLocalAnnotations( Collections.<IGosuAnnotation>emptyList() );
      boolean bFinal = match( T, Keyword.KW_final );

      int iOffsetArgIdentifier = getTokenizer().getTokenStart();
      int iColumnArgIdentifier = getTokenizer().getTokenColumn();
      int iLineArgIdentifier = getTokenizer().getLineNumber();

      Token tokenBeforeParam = getTokenizer().getCurrentToken();
      boolean bMatchColonWithoutName = false;
      if( bEmpty || !verify( (ParsedElement)element, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_ARGS, "" ) )
      {
        bMatchColonWithoutName = match( null, ":", SourceCodeTokenizer.TT_OPERATOR, true );
        if( !bMatchColonWithoutName )
        {
          break;
        }
        else
        {
          T._strValue = "";
        }
      }

      ModifierListClause e = new ModifierListClause();
      pushExpression( e );
      boolean bZeroLength = tokenBeforeParam.getTokenStart() <= iOffsetParam;
      setLocation( iOffsetParam, iLineNumParam, iColumnParam, bZeroLength, true );
      popExpression();

      String strArgIdentifier = T._strValue;
      ParameterDeclaration parameterIdentifier = new ParameterDeclaration( strArgIdentifier );

      addNameInDeclaration( strArgIdentifier, iOffsetArgIdentifier, iLineArgIdentifier, iColumnArgIdentifier, strArgIdentifier.length() > 0 || bMatchColonWithoutName );

      ISymbol existingSymbol = _symTable.getSymbol( strArgIdentifier );
      if( existingSymbol == null )
      {
        for( ISymbol symbol : params )
        {
          if( symbol.getName().equals( strArgIdentifier ) )
          {
            existingSymbol = symbol;
          }
        }
      }

      boolean bSymbolConflict = existingSymbol != null;
      if( bStatic && existingSymbol instanceof DynamicSymbol )
      {
        bSymbolConflict = existingSymbol.isStatic();
      }
      if( !bSymbolConflict )
      {
        IGosuClassInternal anonClass = getParsingAnonymousClass();
        if( anonClass != null && !isParsingAnnotation() )
        {
          // Conflicts with potential captured symbols?
          bSymbolConflict = captureSymbol( anonClass, strArgIdentifier, null ) != null;
        }
      }

      verify( parameterIdentifier, !bSymbolConflict, Res.MSG_VARIABLE_ALREADY_DEFINED, strArgIdentifier );
      verify( parameterIdentifier, ! bProperty || bGetter || iParamPos == 0, Res.MSG_PROPERTY_SET_MUST_HAVE_ONE_PARAMETER );

      IType argType;
      Expression defExpr = null;
      boolean bColonFound = match( null, ":", SourceCodeTokenizer.TT_OPERATOR );
      boolean bEqualsFound = match( null, "=", SourceCodeTokenizer.TT_OPERATOR );
      boolean bParenFound = match( null, null, '(', true );
      if( (inferredArgumentTypes != null || bVarDynamicArg) && !bColonFound && !bEqualsFound && !bParenFound )
      {
        if( inferredArgumentTypes != null && inferredArgumentTypes.size() > iParamPos )
        {
          argType = inferredArgumentTypes.get(iParamPos);
        }
        else if( bVarDynamicArg )
        {
          argType = IDynamicType.instance();
        }
        else
        {
          argType = ErrorType.getInstance();
        }
      }
      else
      {
        if( bParenFound )
        {
          parseBlockLiteral();
        }
        else
        {
          verify( parameterIdentifier, bColonFound || bEqualsFound, Res.MSG_EXPECTING_TYPE_FUNCTION_DEF );
          if( bEqualsFound )
          {
            parseExpression( new ContextType( null, false, true ) );
          }
          else
          {
            parseTypeLiteral();
          }
        }
        Expression expr = popExpression();
        if( bEqualsFound )
        {
          defExpr = expr;
          argType = defExpr.hasParseExceptions()
                    ? JavaTypes.pVOID()
                    : expr.getType();
          verify( expr, !(expr instanceof NullExpression), Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE );
        }
        else
        {
          argType = ((TypeLiteral)expr).getType().getType();
        }

        verify( expr, bEqualsFound || argType != JavaTypes.pVOID(), Res.MSG_VOID_NOT_ALLOWED );

        if( bColonFound && match( null, "=", SourceCodeTokenizer.TT_OPERATOR ) )
        {
          int iOffsetDef = getTokenizer().getTokenStart();
          int iLineNumDef = getTokenizer().getLineNumber();
          int iColumnDef = getTokenizer().getTokenColumn();
          parseExpression( new ContextType( argType, false, true ) );
          defExpr = popExpression();
          defExpr = possiblyWrapWithImplicitCoercion( defExpr, argType );
          pushExpression( defExpr );
          setLocation( iOffsetDef, iLineNumDef, iColumnDef );
          popExpression();
        }
        verify( defExpr, defExpr == null || !bProperty, Res.MSG_DEFAULT_VALUE_NOT_ALLOWED );
      }

      if( strArgIdentifier != null )
      {
        Symbol symbol = new TypedSymbol( strArgIdentifier, argType, _symTable, null, SymbolType.PARAMETER_DECLARATION );
        if( defExpr != null )
        {
          verifyComparable( argType, defExpr );
          verify( defExpr, defExpr.isCompileTimeConstant(), Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED );
          verify( (ParsedElement)element, argType != JavaTypes.pVOID() || !defExpr.hasParseExceptions(),
                  Res.MSG_PARAM_TYPE_CANT_BE_INFERRED_FROM_LATE_BOUND_EXPRESSION );
          symbol.setDefaultValueExpression( defExpr );
          bOptionalParamsStarted = true;
        }
        else
        {
          verify( parameterIdentifier, !bOptionalParamsStarted, Res.MSG_EXPECTING_DEFAULT_VALUE );
        }
        symbol.setFinal( bFinal );
        symbol.getModifierInfo().setAnnotations( annotations );
        params.add( symbol );
        parameterIdentifier.setType( argType );
        pushExpression( parameterIdentifier );
        setLocation( iOffsetParam, iLineNumParam, iColumnParam, true );
        popExpression();
        if( getGosuClass() instanceof IGosuClassInternal && ((IGosuClassInternal)getGosuClass()).isCompilingDefinitions() )
        {
          verifyModifiers( parameterIdentifier, symbol.getModifierInfo(), UsageTarget.ParameterTarget );
        }
      }
    }
    while( match( null, ',' ) );

    ParameterListClause e = new ParameterListClause();
    pushExpression( e );
    boolean bZeroLength = getTokenizer().getTokenStart() <= iOffsetList;
    setLocation( bZeroLength ? getTokenizer().getPriorToken().getTokenEnd() : iOffsetList, iLineNumList, iColumnList, bZeroLength, true );
    popExpression();

    params.trimToSize();
    return params;
  }

  private List<IGosuAnnotation> parseLocalAnnotations( List<IGosuAnnotation> annotations ) {
    while( match( null, null, '@', true ) )
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
    return annotations;
  }

  //------------------------------------------------------------------------------
  // type-variables
  //   < <type-variable-list> >
  //
  List<ITypeVariableDefinitionExpression> parseTypeVariableDefs( ParsedElement parsedElem, boolean bFunction, List<TypeVariableDefinitionImpl> typeVarDefListFromDecl )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    if( !match( null, "<", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      return Collections.emptyList();
    }
    List<ITypeVariableDefinitionExpression> typeVarDefList = parseTypeVariableDefList( parsedElem, bFunction, typeVarDefListFromDecl );
    if( verify( parsedElem, match( null, ">", SourceCodeTokenizer.TT_OPERATOR ),
                Res.MSG_EXPECTING_CLOSING_ANGLE_BRACKET_FOR_TYPE_VAR_LIST ) )
    {
      verify( parsedElem, getGosuClass() == null || !(((IGosuClass)getGosuClass()).isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( getGosuClass() )), Res.MSG_GENERIC_ANNOTATIONS_NOT_SUPPORTED );
    }

    // For editors only
    TypeVariableListClause e = new TypeVariableListClause( typeVarDefList );
    pushExpression( e );
    setLocation( iOffset, iLineNum, iColumn, true );
    popExpression();

    return typeVarDefList;
  }

  //------------------------------------------------------------------------------
  // type-variable-list
  //   <type-variable>
  //   <type-variable-list> , <type-variable>
  //
  List<ITypeVariableDefinitionExpression> parseTypeVariableDefList( ParsedElement parsedElem, boolean bForFunction, List<TypeVariableDefinitionImpl> typeVarDefListFromDecl )
  {
    List<ITypeVariableDefinitionExpression> typeVarDefList = new ArrayList<ITypeVariableDefinitionExpression>();
    int i = 0;
    do
    {
      TypeVariableDefinition typeVarDef = new TypeVariableDefinition( getEnclosingType(), bForFunction );
      if( typeVarDefListFromDecl != null && !typeVarDefListFromDecl.isEmpty() )
      {
        typeVarDef.setTypeVarDef( typeVarDefListFromDecl.get( i++ ) );
      }
      parseTypeVariableDefinition( parsedElem, typeVarDef );
      typeVarDef = (TypeVariableDefinition)popExpression();
      for( ITypeVariableDefinition csr : _typeVarsByName.values() )
      {
        if( !verify( typeVarDef, !csr.getName().equals( typeVarDef.getName() ) ||
                ((TypeVariableDefinition)csr).getLocation().getExtent() == typeVarDef.getLocation().getExtent(),
                Res.MSG_VARIABLE_ALREADY_DEFINED, typeVarDef.getName() ) )
        {
          break;
        }
      }
      typeVarDefList.add( typeVarDef );
    }
    while( match( null, ',' ) );
    return typeVarDefList;
  }

  //------------------------------------------------------------------------------
  // type-variable
  //   <identifier> [extends <type-literal>]
  //
  void parseTypeVariableDefinition( ParsedElement parsedElem, TypeVariableDefinition typeVarDef )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    if( _parseTypeVariableDefinition( parsedElem, typeVarDef ) )
    {
      setLocation( iOffset, iLineNum, iColumn );
    }
  }

  boolean _parseTypeVariableDefinition( ParsedElement parsedElem, TypeVariableDefinition typeVarDef )
  {
    Token T = new Token();
    parseVariance( parsedElem, typeVarDef );
    if( verify( parsedElem, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_EXISTS ) )
    {
      typeVarDef.setName( T._strValue );
      Map<String, ITypeVariableDefinition> typeVarMap = getTypeVariables();
      if( !typeVarMap.containsKey( typeVarDef.getName() ) )
      {
        getTypeVariables().put( typeVarDef.getName(), typeVarDef );
      }

      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      IType boundingType = null;
      boolean bExtends;
      if( bExtends = match( null, Keyword.KW_extends ) )
      {
        typeVarDef.setBoundingType( PENDING_BOUNDING_TYPE );
        parseTypeLiteral();
        boxTypeLiteralsType( (TypeLiteral)peekExpression() );
        TypeLiteral typeLiteral = (TypeLiteral)popExpression();
        boundingType = typeLiteral.getType().getType();
        if( verify( typeLiteral, boundingType != typeVarDef.getType(), Res.MSG_CYCLIC_INHERITANCE, boundingType.getRelativeName() ) )
        {
          typeVarDef.setBoundingType( boundingType );
        }
      }

      // For editors only
      TypeVariableExtendsListClause e = new TypeVariableExtendsListClause( boundingType );
      pushExpression( e );
      // Note for a zero-length extends clause we must shift it to the left one char to avoid having it follow the enclosing TypeVariableListClause
      setLocation( iOffset - (bExtends ? 0 : 1), iLineNum, iColumn, !bExtends, true );
      popExpression();
    }
    else
    {
      typeVarDef.setName( "" );
      verify( typeVarDef, false, Res.MSG_ERRANT_TYPE_VAR );
      Map<String, ITypeVariableDefinition> typeVarMap = getTypeVariables();
      if( !typeVarMap.containsKey( typeVarDef.getName() ) )
      {
        getTypeVariables().put( typeVarDef.getName(), typeVarDef );
      }
      pushExpression( typeVarDef );
      // Set the location to zero length at the end of the last token
      Token priorT = getTokenizer().getPriorToken();
      setLocation( priorT.getTokenEnd(), priorT.getLine(), priorT.getTokenColumn() );
      return false;
    }
    pushExpression( typeVarDef );
    return true;
  }

  private void parseVariance( ParsedElement parsedElem, TypeVariableDefinition typeVarDef )
  {
    int iOffsetList = getTokenizer().getTokenStart();
    int iLineNumList = getTokenizer().getLineNumber();
    int iColumnList = getTokenizer().getTokenColumn();

    boolean bCovariant = false;
    boolean bContravariant = false;
    while( true )
    {
      Token token = getTokenizer().getCurrentToken();
      if( Keyword.KW_in == token.getKeyword() )
      {
        getTokenizer().nextToken();

        if( verify( parsedElem, !typeVarDef.getType().isFunctionStatement(), Res.MSG_UNEXPECTED_TOKEN, Keyword.KW_in ) )
        {
          if( verify( parsedElem, !bContravariant, Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_in, Keyword.KW_in ) )
          {
            bContravariant = true;
            if( typeVarDef.getVariance() == Variance.COVARIANT )
            {
              typeVarDef.setVariance( Variance.INVARIANT );
            }
            else
            {
              typeVarDef.setVariance( Variance.CONTRAVARIANT );
            }
          }
        }
      }
      else if( Keyword.KW_out == token.getKeyword() )
      {
        getTokenizer().nextToken();

        if( verify( parsedElem, !typeVarDef.getType().isFunctionStatement(), Res.MSG_UNEXPECTED_TOKEN, Keyword.KW_out ) )
        {
          if( verify( parsedElem, !bCovariant, Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_out, Keyword.KW_out ) )
          {
            bCovariant = true;
            if( typeVarDef.getVariance() == Variance.CONTRAVARIANT )
            {
              typeVarDef.setVariance( Variance.INVARIANT );
            }
            else
            {
              typeVarDef.setVariance( Variance.COVARIANT );
            }
          }
        }
      }
      else
      {
        break;
      }
    }

    pushModifierList( iOffsetList, iLineNumList, iColumnList );
  }

  private IType getEnclosingType()
  {
    if( isParsingFunction() )
    {
      return peekParsingFunction();
    }
    return getScriptPart() == null ? null : getScriptPart().getContainingType();
  }

  //------------------------------------------------------------------------------
  @Override
  protected void pushExpression( Expression e )
  {
    assert e != null;
    maybeVerifyDoubleLiterals( e );
    _stack.push( e );
  }

  private void maybeVerifyDoubleLiterals( Expression e )
  {
    if( e instanceof AdditiveExpression || e instanceof MultiplicativeExpression )
    {
      IArithmeticExpression ae = (IArithmeticExpression) e;
      maybeVerifyDoubleLiteral( ae.getLHS(), ae.getRHS() );
      maybeVerifyDoubleLiteral( ae.getRHS(), ae.getLHS() );
    }
  }

  private void maybeVerifyDoubleLiteral( IExpression oneSide, IExpression otherSide )
  {
    if( (JavaTypes.BIG_DECIMAL().equals( oneSide.getType() ) || JavaTypes.BIG_INTEGER().equals( oneSide.getType() )) && JavaTypes.pDOUBLE().equals( otherSide.getType() ))
    {
      if( otherSide instanceof UnaryExpression )
      {
        otherSide = ((UnaryExpression)otherSide).getExpression();
      }
      if( otherSide instanceof NumericLiteral )
      {
        NumericLiteral nl = (NumericLiteral)otherSide;
        boolean repsAreIdentical = new BigDecimal( nl.getStrValue() ).equals( CommonServices.getCoercionManager().makeBigDecimalFrom( nl.getValue() ) );
        verify( (ParsedElement)otherSide, repsAreIdentical, Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL, nl.getStrValue() + "bd" );
      }
    }
  }

  //------------------------------------------------------------------------------
  @Override
  public Expression popExpression()
  {
    return (Expression)_stack.pop();
  }

  @Override
  public void setTokenizer( ISourceCodeTokenizer tokenizer )
  {
    _tokenizer = (SourceCodeTokenizer)tokenizer;
  }

  //------------------------------------------------------------------------------
  @Override
  protected Expression peekExpression()
  {
    IParsedElement elem = peekParsedElement();
    return elem instanceof Expression ? (Expression)elem : null;
  }

  //------------------------------------------------------------------------------
  protected ParsedElement peekParsedElement()
  {
    if( _stack.isEmpty() )
    {
      return null;
    }
    return _stack.peek();
  }

  //------------------------------------------------------------------------------
  @Override
  protected void pushStatement( Statement stmt )
  {
    _stack.push( stmt );
  }

  //------------------------------------------------------------------------------
  @Override
  protected Statement popStatement()
  {
    ParsedElement stmt = _stack.pop();
    return (Statement)stmt;
  }

  @Override
  protected Statement peekStatement()
  {
    IParsedElement elem = peekParsedElement();
    return elem instanceof Statement ? (Statement)elem : null;
  }

  protected void pushDynamicFunctionSymbol( DynamicFunctionSymbol stmt )
  {
    _stackDFS.push( stmt );
  }

  protected DynamicFunctionSymbol popDynamicFunctionSymbol()
  {
    return _stackDFS.pop();
  }

  protected DynamicFunctionSymbol peekDynamicFunctionSymbol()
  {
    if( _stackDFS.isEmpty() )
    {
      return null;
    }
    return _stackDFS.peek();
  }

  protected void clearDfsStack()
  {
    _stackDFS.clear();
  }

  public void putDfsDeclsInTable( ISymbolTable table )
  {
    if( table == null )
    {
      return;
    }

    for( Iterator iterator = table.getSymbols().values().iterator(); iterator.hasNext(); )
    {
      ISymbol symbol = (ISymbol)iterator.next();
      if( symbol instanceof IDynamicFunctionSymbol )
      {
        putDfsDeclInSetByName( (IDynamicFunctionSymbol)symbol );
      }
    }
  }

  public void putDfsDeclInSetByName( IDynamicFunctionSymbol dfs )
  {
    String displayName = dfs.getDisplayName();
    List<IFunctionSymbol> dfsDecls = _dfsDeclByName.get( displayName );
    if( dfsDecls == null )
    {
      dfsDecls = new ArrayList<>( 2 );
      try
      {
        _dfsDeclByName.put( displayName, dfsDecls );
        dfsDecls.add( dfs );
      }
      catch( Exception e )
      {
        throw new RuntimeException( "Map type: " + _dfsDeclByName.getClass().getName(), e );
      }
    }
    else
    {
      int iIndex = dfsDecls.indexOf( dfs );
      if( iIndex >= 0 )
      {
        dfsDecls.set( iIndex, dfs );
      }
      else
      {
        dfsDecls.add( dfs );
      }
    }
  }

  public int nextIndexOfErrantDuplicateDynamicSymbol( IDynamicSymbol ds, Collection<? extends ISymbol> symbols, boolean bCheckContains )
  {
    if( symbols == null )
    {
      return -1;
    }
    int iMax = -1;
    if( !bCheckContains || symbolIn( ds, symbols ) )
    {
      for( ISymbol csr : symbols )
      {
        boolean bInjected = csr instanceof IInjectedSymbol;
        if( !(csr instanceof IDynamicSymbol) && !bInjected )
        {
          continue;
        }

        String strName = csr.getName();
        if( csr.getGosuClass() == ds.getGosuClass() || bInjected )
        {
          if( iMax < 0 && strName.equals( ds.getName() ) )
          {
            iMax = 0;
          }
          else if( strName.toLowerCase().contains( "_duplicate_" + ds.getName().toLowerCase() ) )
          {
            int iIndex = Integer.parseInt( strName.substring( 0, strName.indexOf( '_' ) ) );
            if( iIndex > iMax )
            {
              iMax = iIndex;
            }
          }
        }
      }
    }
    return iMax < 0 ? iMax : iMax+1;
  }

  private boolean symbolIn( IDynamicSymbol ds, Collection<? extends ISymbol> symbols )
  {
    for( ISymbol s : symbols )
    {
      if( s.getName().equals( ds.getName() ) )
      {
        return true;
      }
    }
    return false;
  }

  public void setDfsDeclInSetByName( Map<String, List<IFunctionSymbol>> dfsDecl )
  {
    _dfsDeclByName = dfsDecl;
  }

  protected void newDfsDeclInSetByName()
  {
    _dfsDeclByName = new HashMap<String, List<IFunctionSymbol>>();
  }

  public Map<String, List<IFunctionSymbol>> getDfsDecls()
  {
    return _dfsDeclByName;
  }

  protected List<IFunctionType> getFunctionTypesForName( String strFunctionName )
  {
    List<IFunctionSymbol> list = getDfsDeclsForFunction( strFunctionName );
    List<IFunctionType> listOfTypes = new ArrayList<IFunctionType>( list.size() );
    for (IFunctionSymbol dfs : list)
    {
      listOfTypes.add((FunctionType) dfs.getType());
    }
    return listOfTypes;
  }

  protected TypeLiteral resolveTypeLiteral( String[] T )
  {
    return resolveTypeLiteral( T, true, false );
  }
  protected TypeLiteral resolveTypeLiteral( String[] T, boolean bRelative, boolean bInterface )
  {
    String strTypeName = T[0] == null ? "" : T[0];
    return resolveTypeLiteral( strTypeName, bRelative, bInterface );
  }

  protected List<IFunctionSymbol> getDfsDeclsForFunction( String strFunctionName )
  {
    List<IFunctionSymbol> setOfDfsDecls = _dfsDeclByName.get( strFunctionName );
    return setOfDfsDecls == null ? Collections.<IFunctionSymbol>emptyList() : setOfDfsDecls;
  }

  /**
   * Resolves the type literal given by strTypeName.  If parentType is non null then strTypeName is assumed relative to
   * the given parent.
   * @param strTypeName
   */
  public TypeLiteral resolveTypeLiteral( String strTypeName )
  {
    return resolveTypeLiteral( strTypeName, true, false );
  }
  public TypeLiteral resolveTypeLiteral( String strTypeName, boolean bRelative, boolean bInterface )
  {
    int iArrayDims = 0;
    if( strTypeName.length() > 0 && strTypeName.charAt( strTypeName.length()-1 ) == ']' )
    {
      int iFirstBracketIndex = strTypeName.indexOf( '[' );
      strTypeName = strTypeName.substring( 0, iFirstBracketIndex );
      iArrayDims = (strTypeName.length() - iFirstBracketIndex)/2;
    }

    IType intrType;
    boolean bClassTypeVar = false;
    ITypeVariableDefinition typeVarDef = getTypeVarDef( strTypeName );
    if( typeVarDef != null )
    {
      bClassTypeVar = !typeVarDef.getType().isFunctionStatement();
      intrType = typeVarDef.getType();
      if( intrType == null )
      {
        intrType = resolveTypeByRelativeName( strTypeName );
      }
    }
    else
    {
      if( strTypeName.indexOf( '.' ) >= 0 )
      {
        intrType = resolveTypeName( strTypeName, false );
        if( intrType == null )
        {
          intrType = resolveTypeByRelativeName( strTypeName );
        }
      }
      else
      {
        intrType = resolveTypeByRelativeName( strTypeName );
        if( intrType == null )
        {
          intrType = resolveTypeName( strTypeName, bRelative );
        }
      }
    }

    if( intrType == null )
    {
      intrType = ErrorType.getInstance( strTypeName );
    }

    IType finalType = intrType;
    for( int i = 0; i < iArrayDims; i++ )
    {
      finalType = finalType.getArrayType();
    }

    if (TypeSystem.isDeleted(finalType)) {
      finalType = TypeSystem.getErrorType();
    }

    TypeLiteral typeLiteral = bInterface
            ? new InterfaceTypeLiteral( MetaType.getLiteral( finalType ), _ignoreTypeDeprecation > 0 )
            : new TypeLiteral( MetaType.getLiteral( finalType ), _ignoreTypeDeprecation > 0 );

    verify( typeLiteral, !bClassTypeVar || !isParsingStaticFeature() || isParsingConstructor(), Res.MSG_CANNOT_REFERENCE_CLASS_TYPE_VAR_IN_STATIC_CONTEXT );
    if( verify( typeLiteral, !(intrType instanceof ErrorType) || IErrorType.NAME.equals( strTypeName ), Res.MSG_INVALID_TYPE, strTypeName ) )
    {
      verifyCase( typeLiteral, strTypeName, intrType.getName(), Res.MSG_TYPE_CASE_MISMATCH, true );
    }

    return typeLiteral;
  }

  private IType resolveTypeName( String strTypeName, boolean bRelative )
  {
    return resolveTypeName_Cache( strTypeName, bRelative );
//    return resolveTypeName_NoCache( strTypeName, bRelative );
  }
  private IType resolveTypeName_NoCache( String strTypeName, boolean bRelative )
  {
    IType type;
    if( bRelative )
    {
      type = TypeLoaderAccess.instance().getTypeByRelativeNameIfValid_NoGenerics(strTypeName, getTypeUsesMap());
    }
    else
    {
      type = TypeLoaderAccess.instance().getByFullNameIfValid( strTypeName );
    }
    return type;
  }
  private IType resolveTypeName_Cache( String strTypeName, boolean bRelative )
  {
    IType type;
    Map<String, IType> cache = _typeCache.get( null );
    if( cache == null )
    {
      cache = new HashMap<String, IType>();
      _typeCache.put( getScriptPart(), cache );
      type = null;
    }
    else
    {
      type = cache.get( strTypeName );
    }
    if( type == null )
    {
      type = resolveTypeName_NoCache( strTypeName, bRelative );
      if( type == null )
      {
        type = notfound;
      }
      cache.put( strTypeName, type );
    }
    return type == null || type == notfound
            ? null
            : type;
  }

  private ITypeVariableDefinition getTypeVarDef( String strTypeName )
  {
    ITypeVariableDefinition typeVarDef = getTypeVariables().get( strTypeName );
    if( typeVarDef != null && getGosuClass() != null && getGosuClass().isStatic() && TypeLord.encloses( typeVarDef.getEnclosingType(), getGosuClass() ) )
    {
      // Static inner class cannot access enclosing type vars
      typeVarDef = null;
    }
    if( typeVarDef == null )
    {
      typeVarDef = getEnclosingTypeVars().get( strTypeName );
    }
    return typeVarDef;
  }

  //## do we still need this method since inner class parsing re-uses the enclosing class' owner/parser
  private Map<String, TypeVariableDefinition> getEnclosingTypeVars()
  {
    ICompilableType gsClass = getGosuClass();
    if( gsClass == null )
    {
      return Collections.emptyMap();
    }
    Map<String, TypeVariableDefinition> mapTypeVarDefByName = new HashMap<String, TypeVariableDefinition>( 2 );
    while( !gsClass.isStatic() && gsClass.getEnclosingType() != null )
    {
      // Note we don't resolve type vars defined in outer for static inner classes.
      // This is because we do not maintain separate inner classes for each
      // parameterization of a generic outer class

      ICompilableType enclosingType = gsClass.getEnclosingType();
      for( IGenericTypeVariable gtv : enclosingType.getGenericTypeVariables() )
      {
        Map<String, ITypeVariableDefinition> typeVarMap = getTypeVariables();
        if( !typeVarMap.containsKey( gtv.getTypeVariableDefinition().getName() ) )
        {
          getTypeVariables().put( gtv.getName(), gtv.getTypeVariableDefinition() );
        }
      }
      gsClass = enclosingType;
    }
    return mapTypeVarDefByName;
  }

  private IType resolveTypeByRelativeName( String strTypeName )
  {
    ICompilableType gsClass = getGosuClass();
    if( gsClass == null )
    {
      return null;
    }

    return gsClass.resolveRelativeInnerClass( strTypeName, false );
  }

  public Map<String, ITypeVariableDefinition> getTypeVariables()
  {
    return _typeVarsByName;
  }

  public IGosuClassInternal parseClass( String strQualifiedClassName, ISourceFileHandle sourceFile, boolean bThrowOnWarnings, boolean bFullyCompile ) throws ParseResultsException
  {
    GosuClassTypeLoader classLoader;
    if (!ExecutionMode.isIDE()) {
      classLoader = GosuClassTypeLoader.getDefaultClassLoader(TypeSystem.getGlobalModule());
    } else {
      IFile file = sourceFile.getFile();
      IModule module = TypeSystem.getExecutionEnvironment().getModule(file);
      if (module == null) {
        // these are files outside of the typesystem (i.e. not in any source root)
        classLoader = GosuClassTypeLoader.getDefaultClassLoader(TypeSystem.getGlobalModule());
      } else {
        classLoader = module.getModuleTypeLoader().getTypeLoader(GosuClassTypeLoader.class);
      }
    }
    IGosuClassInternal gsClass = (IGosuClassInternal)classLoader.makeNewClass(sourceFile, _symTable);
    gsClass.setEditorParser(this);
    gsClass.setCreateEditorParser(isEditorParser());
    try
    {
      if( bFullyCompile )
      {
        gsClass.compileDefinitionsIfNeeded( true );
      }
      else
      {
        gsClass.compileDeclarationsIfNeeded();
      }
    }
    catch( ErrantGosuClassException e )
    {
      //ignore
    }

    //noinspection ThrowableResultOfMethodCallIgnored
    if( gsClass.getParseResultsException() != null )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      if( gsClass.getParseResultsException().hasParseExceptions() || bThrowOnWarnings )
      {
        throw gsClass.getParseResultsException();
      }
    }

    return gsClass;
  }

  public IFunctionType getFunctionType( IType classBean, String functionName, Expression[] eArgs, List<IFunctionType> listAllMatchingMethods, GosuParser parser, boolean bMatchParamTypes ) throws ParseException
  {
    if( classBean == null )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_BEAN_CLASS_IS_NULL );
    }

    if( functionName == null )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_BEAN_MEMBER_PATH_IS_NULL );
    }

    ITypeInfo beanInfo = classBean.getTypeInfo();
    if( beanInfo == null )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_NO_EXPLICIT_TYPE_INFO_FOUND, classBean.getName() );
    }

    if( ErrorType.shouldHandleAsErrorType( classBean ) )
    {
      return ErrorType.getInstance().getErrorTypeFunctionType( eArgs, functionName, listAllMatchingMethods );
    }

    boolean bFoundMethodWithName = false;
    MethodList methods = BeanAccess.getMethods( beanInfo, getGosuClass() == null ? JavaTypes.OBJECT() : getGosuClass() );
    if( methods != null )
    {
      DynamicArray<? extends IMethodInfo> theMethods = methods.getMethods(functionName);
      for (int i = 0; i < theMethods.size; i++) {
        IMethodInfo method = (IMethodInfo) theMethods.data[i];
        if( BeanAccess.isDescriptorHidden( method ) )
        {
          continue;
        }

        bFoundMethodWithName = true;

        if( !bMatchParamTypes )
        {
          return new FunctionType(method);
        }

        IParameterInfo[] paramTypes = method.getParameters();
        if( eArgs == null || paramTypes.length == eArgs.length )
        {
          if( listAllMatchingMethods == null )
          {
            return new FunctionType(method);
          }
          listAllMatchingMethods.add(new FunctionType(method));
        }
      }
    }

    if( listAllMatchingMethods != null && listAllMatchingMethods.size() > 0 )
    {
      return listAllMatchingMethods.get( 0 );
    }

    if( bFoundMethodWithName )
    {
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_WRONG_NUMBER_OF_ARGS_FOR_METHOD_ON_CLASS, functionName, TypeSystem.getUnqualifiedClassName( classBean ) );
    }
    else
    {
      checkForStaticMethod( classBean, eArgs, functionName, parser );

      if( classBean.isDynamic() )
      {
        IType[] params = null;
        if( eArgs != null )
        {
          params = new IType[eArgs.length];
          for( int i = 0; i < eArgs.length; i++ )
          {
            params[i] = eArgs[i].getType();
          }
        }
        IMethodInfo mi = classBean.getTypeInfo().getMethod( functionName, params );
        if( mi != null )
        {
          FunctionType funcType = new FunctionType( mi );
          if( listAllMatchingMethods != null )
          {
            listAllMatchingMethods.add( funcType );
          }
          return funcType;
        }
      }
      throw new ParseException( parser == null ? null : parser.makeFullParserState(), Res.MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, functionName, TypeSystem.getUnqualifiedClassName( classBean ) );
    }
  }

  private void checkForStaticMethod( IType classBean, Expression[] eArgs, String strMethod, GosuParser parserState )
          throws ParseException
  {
    if( classBean instanceof MetaType)
    {
      IType referredType = ((MetaType)classBean).getType();
      IType[] paramTypes = new IType[eArgs == null ? 0 : eArgs.length];
      for( int i = 0; i < paramTypes.length; i++ )
      {
        paramTypes[i] = eArgs[i].getType();
      }
      IMethodInfo mi = referredType.getTypeInfo().getCallableMethod( strMethod, paramTypes );
      if( mi != null && !mi.isStatic() )
      {
        throw new ParseException( parserState == null ? null : parserState.makeFullParserState(), Res.MSG_METHOD_IS_NOT_STATIC, strMethod, TypeSystem.getUnqualifiedClassName( classBean ) );
      }
    }
    else
    {
      IType referredType = MetaType.get( classBean );
      IType[] paramTypes = new IType[eArgs == null ? 0 : eArgs.length];
      for( int i = 0; i < paramTypes.length; i++ )
      {
        paramTypes[i] = eArgs[i].getType();
      }
      IMethodInfo mi = referredType.getTypeInfo().getCallableMethod( strMethod, paramTypes );
      if( mi != null && !mi.isStatic() )
      {
        throw new ParseException( parserState == null ? null : parserState.makeFullParserState(), Res.MSG_METHOD_IS_STATIC, strMethod, TypeSystem.getUnqualifiedClassName( classBean ) );
      }
    }
  }

  private IInvocableType inferFunctionType( IInvocableType funcType, List<? extends IExpression> eArgs, boolean bUseCtx, TypeVarToTypeMap inferenceMap )
  {
    if( funcType instanceof IFunctionType && funcType.isGenericType() )
    {
      return inferFunction( funcType, eArgs, bUseCtx );
    }
    else if( funcType instanceof ConstructorType )
    {
      return inferConstructor( funcType, inferenceMap );
    }
    else
    {
      return funcType;
    }
  }

  private IInvocableType inferConstructor( IInvocableType funcType, TypeVarToTypeMap inferenceMap )
  {
    IType declaringType = ((ConstructorType)funcType).getDeclaringType();
    if( declaringType.isGenericType() && !declaringType.isParameterizedType() )
    {
      IType actualDeclaringType = TypeLord.makeParameteredType( declaringType, inferenceMap );
      if( actualDeclaringType != null )
      {
        List<? extends IConstructorInfo> genDeclaredConstructors = ((IRelativeTypeInfo)declaringType.getTypeInfo()).getDeclaredConstructors();
        for( int i = 0; i < genDeclaredConstructors.size(); i++ )
        {
          IConstructorInfo rawCtor = genDeclaredConstructors.get( i );
          if( new ConstructorType( rawCtor ).equals( funcType ) )
          {
            List<? extends IConstructorInfo> paramDeclaredConstructors = ((IRelativeTypeInfo)actualDeclaringType.getTypeInfo()).getDeclaredConstructors();
            for( IConstructorInfo csr: paramDeclaredConstructors )
            {
              if( csr.hasRawConstructor( rawCtor ) )
              {
                return new ConstructorType( csr );
              }
            }
            break;
          }
        }
      }
    }
    return funcType;
  }

  private IInvocableType inferFunction( IInvocableType funcType, List<? extends IExpression> eArgs, boolean bUseCtx )
  {
    IType[] argTypes = new IType[eArgs.size()];
    for( int i = 0; i < eArgs.size(); i++ )
    {
      argTypes[i] = eArgs.get( i ).getType();
    }
    for( int i = 0; i < funcType.getParameterTypes().length; i++ )
    {
      IType paramType = funcType.getParameterTypes()[i];
      if( i < argTypes.length )
      {
        IType argType = argTypes[i];
        IType boundArgType = TypeLord.boundTypes( paramType, getCurrentlyInferringFunctionTypeVars() );
        ICoercer coercer = CommonServices.getCoercionManager().resolveCoercerStatically( boundArgType, argType );
        if( coercer instanceof IResolvingCoercer )
        {
          argTypes[i] = ((IResolvingCoercer)coercer).resolveType( paramType, argType );
        }
      }
    }
    return ((IFunctionType) funcType).inferParameterizedTypeFromArgTypesAndContextType( argTypes, bUseCtx ? getContextType().getType() : null );
  }

  /**
   * Using some simple pattern matching, get a potential property name from a
   * method name at the end of an access list.
   * <p/>
   * Patterns:<br>
   * <code>get</code>&gtmixed-case-name&lt
   * <code>is</code>&gtmixed-case-name&lt
   */
  private static final String[] METHOD_PREFIX_LIST = {"get", "is"};

  private String getPropertyNameFromMethodName( String strMethod )
  {
    if( strMethod == null || strMethod.length() == 0 )
    {
      return null;
    }

    for( String strPrefix : METHOD_PREFIX_LIST )
    {
      String strProperty = getPropertyNameFromMethodName( strPrefix, strMethod );
      if( strProperty != null )
      {
        return strProperty;
      }
    }

    return null;
  }

  private static final String[] METHOD_PREFIX_LIST_WITH_SETTER = {"get", "is", "set" };

  private String getPropertyNameFromMethodNameIncludingSetter( String strMethod )
  {
    if( strMethod == null || strMethod.length() == 0 )
    {
      return null;
    }

    for( String strPrefix : METHOD_PREFIX_LIST_WITH_SETTER )
    {
      String strProperty = getPropertyNameFromMethodName( strPrefix, strMethod );
      if( strProperty != null )
      {
        return strProperty;
      }
    }

    return null;
  }

  private String getPropertyNameFromMethodName( String strPrefix, String strMethod )
  {
    int iPropertyOffset = strPrefix.length();
    if( strMethod.startsWith( strPrefix ) &&
            strMethod.length() > iPropertyOffset &&
            Character.isUpperCase( strMethod.charAt( iPropertyOffset ) ) )
    {
      return strMethod.substring( iPropertyOffset );
    }
    return null;
  }

  private void verifyPropertyWritable( IType classRoot, String strProperty, boolean bFromObjInitializer ) throws ParseException
  {
    if( classRoot == null )
    {
      throw new IllegalArgumentException( "Root class is null|" );
    }

    if( strProperty == null )
    {
      throw new IllegalArgumentException( "Bean member path is null!" );
    }

    IPropertyInfo pi = BeanAccess.getPropertyInfo( classRoot, strProperty, null, null, null );
    if( pi != null )
    {
      if( !BeanAccess.isDescriptorHidden( pi ) )
      {
        if( !pi.isWritable( getGosuClass() ) )
        {
          if( bFromObjInitializer || !(isParsingConstructor() && pi instanceof IGosuVarPropertyInfo && pi.isFinal() && !pi.isStatic()) ) {
            throw new ParseException( makeFullParserState(),  Res.MSG_CLASS_PROPERTY_NOT_WRITABLE, strProperty, TypeSystem.getUnqualifiedClassName( classRoot ));
          }
        }
        return;
      }
    }

    throw new IllegalArgumentException( "No property descriptor found for property, " + strProperty + ", on class, " + TypeSystem.getUnqualifiedClassName( classRoot ) );
  }

  /**
   * Get the type of the method specified in the member path.
   *
   *@param classBean   The declaring class of the constructor.
   * @param parserState The parserState that may be involved in the process of parsing a constructor. Can be null.
   * @return A Gosu type for the constructor.
   */
  public IConstructorType getConstructorType( IType classBean, Expression[] eArgs, List<IConstructorType> listAllMatchingMethods, ParserBase parserState ) throws ParseException
  {
    if( classBean == null )
    {
      throw new ParseException( parserState == null ? null : parserState.makeFullParserState(), Res.MSG_BEAN_CLASS_IS_NULL );
    }

    if( ErrorType.shouldHandleAsErrorType( classBean ) )
    {
      return ErrorType.getInstance().getErrorTypeConstructorType( eArgs, listAllMatchingMethods );
    }

    if( classBean instanceof TypeVariableType )
    {
      // Using dynamic ctor so any params are legal in call e.g., new T( a, b, c ) // this call is not verified until runtime

      IType[] paramTypes = new IType[eArgs == null ? 0 : eArgs.length];
      for( int i = 0; i < paramTypes.length; i++ )
      {
        paramTypes[i] = eArgs[i].getType();
      }
      ConstructorType ctorType = new ConstructorType( new DynamicConstructorInfo( classBean.getTypeInfo(), paramTypes ) );
      if( listAllMatchingMethods != null )
      {
        listAllMatchingMethods.add( ctorType );
      }
      return ctorType;
    }

    ITypeInfo typeInfo = classBean.getTypeInfo();
    if( typeInfo != null )
    {
      List<? extends IConstructorInfo> constructors;
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        while( classBean instanceof ITypeVariableType )
        {
          classBean = ((ITypeVariableType)classBean).getBoundingType();
        }
        constructors = ((IRelativeTypeInfo)typeInfo).getConstructors( classBean );
      }
      else
      {
        constructors = typeInfo.getConstructors();
      }

      for( IConstructorInfo constructor : constructors )
      {
        if( typeInfo instanceof JavaTypeInfo )
        {
          if( constructor.isPrivate() )
          {
            continue;
          }
        }
        IParameterInfo[] paramTypes = constructor.getParameters();
        if( eArgs == null || paramTypes.length == eArgs.length )
        {
          if( listAllMatchingMethods == null )
          {
            return new ConstructorType( constructor );
          }
          listAllMatchingMethods.add( new ConstructorType( constructor ) );
        }
      }

      if( listAllMatchingMethods != null && listAllMatchingMethods.size() > 0 )
      {
        return listAllMatchingMethods.get( 0 );
      }
    }

    throw new NoCtorFoundException( parserState == null ? null : parserState.makeFullParserState(), TypeSystem.getUnqualifiedClassName( classBean ), eArgs == null ? 0 : eArgs.length );
  }

  private void verifyCase( ParsedElement element, String foundName, String actualName, ResourceKey errorKey,
                           boolean isEndsWithMatchOK )
  {
    verifyCase( element, foundName, actualName, null, errorKey, isEndsWithMatchOK );
  }

  private void verifyCase( ParsedElement element, String foundName, String actualName, IParserState state, ResourceKey errorKey, boolean isEndsWithMatchOK )
  {
    if( _bWarnOnCaseIssue )
    {
      if( isEndsWithMatchOK )
      {
        if( !actualName.endsWith( foundName ) )
        {
          if( actualName.toUpperCase().endsWith( foundName.toUpperCase() ) )
          {
            CharSequence correctedName = actualName.subSequence( actualName.length() - foundName.length(), actualName.length() );
            if(state == null) {
              warn( element, false, errorKey, foundName, correctedName );
            } else {
              warn( element, false, state, errorKey, foundName, correctedName );
            }
          }
        }
      }
      else if( !GosuObjectUtil.equals( foundName, actualName ) )
      {
        if( actualName.toUpperCase().equals( foundName.toUpperCase() ) )
        {
          if(state == null) {
            warn( element, false, errorKey, foundName, actualName );
          } else {
            warn( element, false, state, errorKey, foundName, actualName );
          }
        }
      }
    }
  }

  public void setWarnOnCaseIssue( boolean warnOnCaseIssue )
  {
    _bWarnOnCaseIssue = warnOnCaseIssue;
  }

  public void setEditorParser(boolean bStudioEditorParser) {
    _bStudioEditorParser = bStudioEditorParser;
  }

  public boolean isEditorParser() {
    if (getOwner() != this) {
      return getOwner().isEditorParser();
    }
    return _bStudioEditorParser;
  }

  public IParserState getState() {
    return makeFullParserState();
  }

  public boolean isParsingAnnotation()
  {
    return _parsingAnnotation;
  }

  public void setParsingAnnotation( boolean parsingAnnotation )
  {
    _parsingAnnotation = parsingAnnotation;
  }

  public boolean isAllowingWildcards()
  {
    return _allowWildcards;
  }

  public void setAllowWildcards( boolean allowWildcards )
  {
    _allowWildcards = allowWildcards;
  }

  public boolean isIgnoreTypeDeprecation()
  {
    return _ignoreTypeDeprecation > 0;
  }
  public void pushIgnoreTypeDeprecation()
  {
    _ignoreTypeDeprecation++;
  }
  public void popIgnoreTypeDeprecation()
  {
    if( _ignoreTypeDeprecation == 0 )
    {
      throw new IllegalStateException( "Unbalanced calls to push/popIgnoreTypeDeprecation()" );
    }
    _ignoreTypeDeprecation--;
  }

  public void setLocationsFromProgramClassParser( List<ParseTree> savedLocations )
  {
    _savedLocations = savedLocations;
  }
  boolean maybeAdvanceTokenizerToEndOfSavedLocation()
  {
    if( _savedLocations == null )
    {
      return false;
    }
    for( ParseTree pt : _savedLocations )
    {
      Token T = getTokenizer().getCurrentToken();
      if( T.getTokenStart() >= pt.getOffset() && T.getTokenEnd() <= pt.getExtent() )
      {
        try
        {
          getTokenizer().goToPosition( pt.getOffset() + pt.getLength() );
          return true;
        }
        catch( IOException e )
        {
          // Eof ok
          return true;
        }
      }
    }
    return false;
  }

  protected void pushTypeVariableTypesToInfer( IInvocableType functionType )
  {
    if( functionType != null )
    {
      List<IType> typeVariableTypes = new ArrayList<>();
      if( functionType.isGenericType() )
      {
        IGenericTypeVariable[] typeVariables = functionType.getGenericTypeVariables();
        addTypeVarsToList( typeVariableTypes, typeVariables );
      }
      else if( functionType instanceof ConstructorType )
      {
        IType declaringType = ((ConstructorType)functionType).getDeclaringType();
        if( declaringType.isGenericType() && !declaringType.isParameterizedType() )
        {
          IGenericTypeVariable[] typeVariables = declaringType.getGenericTypeVariables();
          addTypeVarsToList( typeVariableTypes, typeVariables );
        }
      }
      pushInferringFunctionTypeVars( typeVariableTypes );
    }
  }

  private void addTypeVarsToList( List<IType> typeVariableTypes, IGenericTypeVariable[] typeVariables )
  {
    for( IGenericTypeVariable typeVariable : typeVariables )
    {
      ITypeVariableDefinition typeVariableDefinition = typeVariable.getTypeVariableDefinition();
      if( typeVariableDefinition != null && typeVariableDefinition.getType() != null )
      {
        typeVariableTypes.add( typeVariableDefinition.getType() );
      }
    }
  }

  @Override
  public String toString()
  {
    return "Parsing: " + getScriptPart();
  }

  private static class GosuParserTransparentActivationContext extends TransparentActivationContext
  {
    public GosuParserTransparentActivationContext( IScriptPartId scriptPart )
    {
      super( scriptPart );
    }

    public String getLabel()
    {
      return getContext().toString();
    }
  }

  private static class FunctionDeclTransparentActivationContext extends TransparentActivationContext
  {
    public FunctionDeclTransparentActivationContext( IScriptPartId scriptPart )
    {
      super( scriptPart );
    }

    public String getLabel()
    {
      return "parseFunctionDecl";
    }
  }
}
