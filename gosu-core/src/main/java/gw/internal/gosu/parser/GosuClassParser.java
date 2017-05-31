/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.expressions.ClassDeclaration;
import gw.internal.gosu.parser.expressions.InterfacesClause;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.expressions.NameInDeclaration;
import gw.internal.gosu.parser.expressions.NullExpression;
import gw.internal.gosu.parser.expressions.ParameterListClause;
import gw.internal.gosu.parser.expressions.SuperTypeClause;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.expressions.TypeVariableDefinitionImpl;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.ConstructorStatement;
import gw.internal.gosu.parser.statements.DelegateStatement;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.parser.statements.NamespaceStatement;
import gw.internal.gosu.parser.statements.NoOpStatement;
import gw.internal.gosu.parser.statements.NotAStatement;
import gw.internal.gosu.parser.statements.PropertyStatement;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.UsesStatement;
import gw.internal.gosu.parser.statements.VarInitializationVerifier;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.annotation.UsageTarget;
import gw.lang.ir.IRType;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.parser.IParserState;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.IToken;
import gw.lang.parser.ITokenizerOffsetMarker;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.Keyword;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.exceptions.NotImplementedParseException;
import gw.lang.parser.exceptions.ObsoleteConstructorWarning;
import gw.lang.parser.exceptions.ParseException;
import gw.lang.parser.exceptions.ParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.parser.expressions.IModifierListClause;
import gw.lang.parser.expressions.IParameterDeclaration;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.ITypeVariableDefinitionExpression;
import gw.lang.parser.expressions.Variance;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.IFunctionStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IUsesStatementList;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEnhanceableType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IInvocableType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassParser;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.DynamicArray;
import gw.util.GosuExceptionUtil;
import gw.util.GosuObjectUtil;
import gw.util.GosuStringUtil;
import gw.util.Stack;

import java.io.IOException;
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

/**
 */
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public class GosuClassParser extends ParserBase implements IGosuClassParser, ITokenizerOffsetMarker
{
  private int _iClassOffset;
  private int _iClassLineNum;
  private int _iClassColumn;
  private ClassStatement _classStmt;
  private Stack<IGosuClassInternal> _innerClasses;
  private int _innerClassOffset;


  public GosuClassParser( GosuParser owner )
  {
    super( owner );
    _innerClasses = new Stack<IGosuClassInternal>();
  }

  //## todo: maybe ctors should set the class here so that subsequent calls to parseXxx() don't need to take a IGosuClass
  private GosuClassParser( GosuParser owner, IGosuClassInternal innerClass )
  {
    super( owner );
    int mark = ((InnerClassFileSystemSourceFileHandle)innerClass.getSourceFileHandle()).getMark();
    if( mark >= 0 )
    {
      getTokenizer().restoreToMark( mark );
    }
    else
    {
      goToPosition( innerClass.getSourceFileHandle().getOffset() );
    }
    _innerClassOffset = getTokenizer().mark();
    _innerClasses = new Stack<IGosuClassInternal>();
  }

  public static void parseAnonymousInnerClass( GosuParser gosuParser, IGosuClassInternal innerGsClass )
  {
    Stack<BlockExpression> enclosingBlocks = gosuParser._blocks;
    gosuParser.setBlocks( null );
    Map<String, List<IFunctionSymbol>> restoreDfsDecls = copyDFSDecls( gosuParser );
    try
    {
      new GosuClassParser( gosuParser, innerGsClass ).parseHeader(innerGsClass, false, true, true );
      new GosuClassParser( gosuParser, innerGsClass ).parseDeclarations( innerGsClass );
      if( !gosuParser.getContextType().isMethodScoring() )
      {
        new GosuClassParser( gosuParser, innerGsClass ).parseDefinitions( innerGsClass );
      }
    }
    finally
    {
      gosuParser.setDfsDeclInSetByName( restoreDfsDecls );
      gosuParser.setBlocks( enclosingBlocks );
    }
  }

  @Override
  protected String getScript()
  {
    return getOwner().getScript();
  }

  @Override
  public int getLineNumShift()
  {
    return getOwner().getLineNumShift();
  }

  @Override
  public int getOffsetShift()
  {
    return getOwner().getOffsetShift();
  }

  @Override
  public int getOffsetMark()
  {
    if( isInnerClass( getGosuClass() ) )
    {
      return _innerClassOffset;
    }
    return -1;
  }

  @Override
  public ClassStatement getClassStatement()
  {
    return _classStmt;
  }

  private void setClassStatement( ClassStatement classStmt )
  {
    if( classStmt == null )
    {
      throw new IllegalArgumentException( "Class stmt is null" );
    }
    _classStmt = classStmt;
  }

  private IGosuClassInternal getCurrentInnerClass()
  {
    return _innerClasses.isEmpty() ? null : _innerClasses.peek();
  }

  private void pushInnerClass( IGosuClassInternal gsInnerClass )
  {
    _innerClasses.push( gsInnerClass );
  }

  private IGosuClassInternal popInnerClass( IGosuClassInternal gsInnerClass )
  {
    IGosuClassInternal top = _innerClasses.pop();
    if( top != gsInnerClass )
    {
      throw new IllegalStateException( "Unbalanced push/pop for inner classes" );
    }
    return top;
  }

  private boolean isInnerClassesEmpty()
  {
    return _innerClasses.isEmpty();
  }

  /**
   * Parses all declarations including:<br>
   * <ul>
   * <li> Fields
   * <li> Methods
   * <li> Properties
   * <li> Inner types, recursively
   * </ul>
   */
  public void parseDeclarations( IGosuClass gsCls )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)gsCls;

    if( gsClass.isDeclarationsCompiled() )
    {
      if( !gsClass.isInnerDeclarationsCompiled() )
      {
        if( parseDeclarationsOfLeftOverInnerClasses( gsClass ) )
        {
          gsClass.setInnerDeclarationsCompiled();
        }
      }
      return;
    }

    boolean bPushedScope = pushScopeIfNeeded( gsClass );

    getTokenizer().pushOffsetMarker( this );
    ScriptPartId scriptPartId = new ScriptPartId( gsClass, null );
    getOwner().pushScriptPart( scriptPartId );
    GosuClassCompilingStack.pushCompilingType( gsClass );
    gsClass.setCompilingDeclarations( true );
    try
    {
      ClassStatement classStmt = (ClassStatement)gsClass.getClassStatement();
      try
      {
        setClassStatement( classStmt );
      }
      catch( Exception e )
      {
        throw GosuExceptionUtil.forceThrow( e, gsClass.getName() );
      }
      if( isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass ) )
      {
        classStmt.getClassFileStatement().clearParseTreeInformation();
      }

      // Don't need an isolated scope here because class members are all dynamic
      // and, therefore, don't have to be indexed wrt an isolated scope.
      getSymbolTable().pushScope();
      try
      {
        //## todo: reparsing header with annotations this time, any chance we can do that the first time we parse the header, so we can avoid doing it twice?
        String strClassName = parseHeader(gsClass, false, false, true);
        if( gsClass instanceof IGosuEnhancementInternal )
        {
          parseEnhancementBodyDecl( gsClass );
        }
        else
        {
          parseClassBodyDecl( strClassName, gsClass );
        }
      }
      finally
      {
        getSymbolTable().popScope();

        pushStatement( classStmt );
        setLocation( _iClassOffset, _iClassLineNum, _iClassColumn, true );
        popStatement();

        if( isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass ) )
        {
          pushStatement( classStmt.getClassFileStatement() );
          setLocation( 0, 1, _iClassColumn, true );
          popStatement();
        }
      }
      classStmt.compactParseTree();
    }
    finally
    {
      gsClass.setCompilingDeclarations( false );
      // Do not set decls compiled; we do that in parseClassBodyDecl(). Also the decls may not have actually been compiled
      //gsClass.setDeclarationsCompiled();
      GosuClassCompilingStack.popCompilingType();
      getOwner().popScriptPart( scriptPartId );
      popScopeIfNeeded( bPushedScope, gsClass );
      getTokenizer().popOffsetMarker( this );

      removeTypeVarsFromParserMap( gsClass );
    }
  }

  private boolean isTopLevelClass( IGosuClassInternal gsClass )
  {
    return gsClass.getEnclosingType() == null;
  }

//  /**
//   * Extend the bounds of the enclosing ClassFileStatement if need be. Note this is only necessary when
//   * the enclosing class has errors and, therefore, may not have parsed elements with the
//   */
//  private void extendEnclosingClassFileBounds( IParsedElement enclosingClassFileStmt )
//  {
//    if( enclosingClassFileStmt.getLocation() != null )
//    {
//      int iExtentDelta = enclosingClassFileStmt.getLocation().getExtent() - getClassStatement().getClassFileStatement().getLocation().getExtent();
//      if( iExtentDelta < 0 )
//      {
//        enclosingClassFileStmt.getLocation().setLength( enclosingClassFileStmt.getLocation().getLength() + -iExtentDelta );
//      }
//    }
//  }

  public void parseDefinitions( IGosuClass gsCls )
  {
    IGosuClassInternal gsClass = (IGosuClassInternal)gsCls;

    getTokenizer().pushOffsetMarker( this );

    boolean bPushedScope = pushScopeIfNeeded( gsClass );

    gsClass.setCompilingDefinitions( true );

    GosuClassParseInfo parseInfo = gsClass.getParseInfo();
    ClassStatement classStmt = parseInfo.getClassStatement();
    setClassStatement( classStmt );

    clearParseTree( gsClass );

    ScriptPartId scriptPartId = new ScriptPartId( gsClass, null );
    getOwner().pushScriptPart( scriptPartId );
    GosuClassCompilingStack.pushCompilingType( gsClass );
    getOwner()._iReturnOk++;
    if( isDeprecated( (ModifierInfo)gsCls.getModifierInfo() ) )
    {
      getOwner().pushIgnoreTypeDeprecation();
    }
    try
    {
      try
      {
        if( !gsClass.isDefinitionsCompiled() )
        {
          // Don't need an isolated scope here because class members are all dynamic
          // and, therefore, don't have to be indexed wrt an isolated scope.
          getSymbolTable().pushScope();
          try
          {
            //
            // Reset the tokenizer to prepare for secon.. er third pass
            //
            getTokenizer().reset();
            if( isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass ) )
            {
              getLocationsList().clear();
            }
            else
            {
              removeInnerClassDelcarationsFromLocationsList( gsClass );
            }

            //
            // Parse the whole class, including inner types
            //
            // Note function definitions are parsed as no-op statements, but are
            // pushed onto the dynamic function symobl stack.
            //## todo: do we really need to parse the header *again* (maybe for annotations?)
            parseHeader(gsClass, false, false, true );

            if( gsClass instanceof IGosuEnhancementInternal )
            {
              parseClassStatementAsEnhancement( gsClass );
            }
            else
            {
              parseClassStatement();
            }
          }
          finally
          {
            getSymbolTable().popScope();

            if( gsClass instanceof IGosuProgramInternal )
            {
              ((IGosuProgramInternal)gsClass).setParsingExecutableProgramStatements( true );
              try
              {
                FunctionStatement fs = parseExecutableProgramStatements( (IGosuProgramInternal)gsClass );
                makeExprRootFunction( (IGosuProgramInternal)gsClass, fs );
              }
              finally
              {
                ((IGosuProgramInternal)gsClass).setParsingExecutableProgramStatements( false );
              }
            }
            boolean b = isInnerClass( gsClass ) || match( null, SourceCodeTokenizer.TT_EOF );
            if( !verify( classStmt, b, Res.MSG_END_OF_STMT ) )
            {
              consumeTrailingTokens();
            }
            gsClass.setDefinitionsCompiled();
          }
        }

        if( isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass ) )
        {
          getOwner().setParsed( true );
        }
      }
      finally
      {
        pushStatement( classStmt );
        setLocation( _iClassOffset, _iClassLineNum, _iClassColumn, true );

        if( isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass ) )
        {
          popStatement();
          pushStatement( classStmt.getClassFileStatement() );
          setLocation( 0, 1, _iClassColumn, true );
          popStatement();
        }

        assignTokens( classStmt );
      }

      try
      {
        verifyParsedElement( isInnerClass( gsClass ) && !TypeLord.isEvalProgram( gsClass ) ? classStmt : classStmt.getClassFileStatement() );
      }
      catch( ParseResultsException pre )
      {
        gsClass.setParseResultsException( pre );
      }
    }
    finally
    {
      try
      {
        gsClass.setCompilingDefinitions( false );
        gsClass.setDefinitionsCompiled();
        getOwner().popScriptPart( scriptPartId );
      }
      finally
      {
        GosuClassCompilingStack.popCompilingType();
      }
      popScopeIfNeeded( bPushedScope, gsClass );
      getTokenizer().popOffsetMarker( this );
      removeTypeVarsFromParserMap( gsClass );

      getOwner()._iReturnOk--;
      pushStatement( _classStmt.getClassFileStatement() );
      setLocation( 0, 1, _iClassColumn, true );
      popStatement();

      if( isDeprecated( (ModifierInfo)gsCls.getModifierInfo() ) )
      {
        getOwner().popIgnoreTypeDeprecation();
      }

      gsClass.syncGenericAndParameterizedClasses();
      getOwner().clearDfsStack();
      _classStmt = null;

      VarInitializationVerifier.verifyFinalFields( gsClass );
      VarInitializationVerifier.verifyLocalVars( gsClass, true );
      if( isTopLevelClass( gsClass ) )
      {
        postDefinitionVerify(classStmt);
      }
    }
  }

  private void postDefinitionVerify( IClassStatement classStmt )
  {
    if( classStmt == null )
    {
      return;
    }
    IGosuClass gsClass = classStmt.getGosuClass();
    if( gsClass.isAnonymous() || gsClass instanceof IBlockClass )
    {
      return;
    }
    CompileTimeAnnotationHandler.postDefinitionVerification( classStmt );
    for( IGosuClass innerClass: classStmt.getGosuClass().getInnerClasses() )
    {
      postDefinitionVerify( innerClass.getClassStatement() );
    }
  }

  private void removeInnerClassDelcarationsFromLocationsList( IGosuClassInternal gsClass )
  {
    List<ParseTree> locations = getLocationsList();
    for( int i = locations.size()-1; i >= 0; i-- )
    {
      ParseTree csr = locations.get( i );
      if( csr.getScriptPartId().getContainingType() == gsClass )
      {
        IParseTree parent = csr.getParent();
        if( parent != null )
        {
          parent.removeChild( csr );
        }
        locations.remove( csr );
      }
      else
      {
        break;
      }
    }
  }

  private void consumeTrailingTokens()
  {
    while( !match( null, SourceCodeTokenizer.TT_EOF ) )
    {
      getTokenizer().nextToken();
    }
  }

  private void assignTokens( ClassStatement classStmt )
  {
    if( !getOwner().isEditorParser() )
    {
      return;
    }

    if( !isTopLevelClass( classStmt.getGosuClass() ) )
    {
      return;
    }

    List<Token> tokens = getOwner().getTokenizer().getTokens().toList();
    classStmt.getClassFileStatement().assignTokens( tokens );

//## todo: handle programs (see GosuAstTransformer)
//    String strSource = getGosuClass().getSource();
//    String strTextFromParseTree = classStmt.getClassFileStatement().getLocation().getTextFromTokens();
//    if( !strSource.equals( strTextFromParseTree ) )
//    {
//      int[] diff = getDiffOffset( strSource, strTextFromParseTree );
//
//      throw new IllegalStateException( buildInconsistentParseErrorMessage( strSource, strTextFromParseTree, diff ) );
//    }

    //noinspection LoopStatementThatDoesntLoop
    for( IToken token : tokens )
    {
      throw new IllegalStateException( "One or more tokens were not assigned: " + token );
    }
  }

  private String buildInconsistentParseErrorMessage( String strSource, String strTextFromParseTree, int[] diff )
  {
    return
      "Parsed class, " + getGosuClass().getName() + ", inconsistent with source.\n" +
      "Line: " + diff[1] + "  Offset: " + diff[0] + "\n" +
      "*** Parsed Version ***\n" +
      ParseIssue.makeContextString( diff[1], strTextFromParseTree, diff[2] ) + "\n" +
      "*** Source Version ***\n" +
      ParseIssue.makeContextString( diff[1], strSource, diff[2] ) + "\n";
  }

  private int[] getDiffOffset( String strSource, String strTextFromParseTree )
  {
    if( strSource == null || strTextFromParseTree == null )
    {
      return null;
    }
    int i;
    int iLineOffset = 0;
    int iLine = 0;
    for( i = 0; i < strSource.length(); i++ )
    {
      if( i >= strTextFromParseTree.length() )
      {
        return new int[] {i, iLine, iLineOffset};
      }
      char sourceChar = strSource.charAt( i );
      char parserChar = strTextFromParseTree.charAt( i );
      if( sourceChar != parserChar )
      {
        return new int[] {i, iLine, iLineOffset};
      }
      if( parserChar == '\n' )
      {
        iLine++;
        iLineOffset = i;
      }
    }
    return new int[] {i, iLine, iLineOffset};
  }

  private void clearParseTree( IGosuClassInternal gsClass )
  {
    if( (!(gsClass instanceof IGosuProgram) && isTopLevelClass( gsClass )) ||
        TypeLord.isEvalProgram( gsClass ) )
    {
      gsClass.getClassStatement().getClassFileStatement().clearParseTreeInformation();
    }
    else
    {
      gsClass.getClassStatement().clearParseTreeInformation();
      if( gsClass.isAnonymous() )
      {
        //noinspection SuspiciousMethodCalls
        if( !getLocationsList().isEmpty() )
        {
          ParseTree last = getLocationsList().get( getLocationsList().size() - 1 );
          if( last.getParsedElement() == null )
          {
            // Remove abandoned class-stmt parse tree from decl parse
            getLocationsList().remove( last );
          }
        }
      }
    }
  }

  private boolean isInnerClass( IGosuClassInternal gsClass )
  {
    return gsClass.getEnclosingType() != null;
  }

  private FunctionStatement parseExecutableProgramStatements( IGosuProgramInternal gsClass )
  {
    List savedLocations = getOwner().getLocations();
    getTokenizer().resetButKeepTokens();
    getLocationsList().clear();
    getOwner().setLocationsFromProgramClassParser( savedLocations );
    parseHeader( gsClass, false, false, true );
    gsClass.addCapturedProgramSymbols( getSymbolTable() );
    FunctionStatement fs = parseProgramAsFunctionStatement( gsClass );
    List newLocations = getOwner().getLocations();
    removeRedundantUsesStatementList( newLocations );
    getOwner().getLocationsList().clear();
    getOwner().setLocationsFromProgramClassParser( null );
    getOwner().getLocationsList().addAll( savedLocations );
    getOwner().getLocationsList().addAll( newLocations );
    return fs;
  }

  private void removeRedundantUsesStatementList( List newLocations )
  {
    for( int i = 0; i < newLocations.size(); i++ )
    {
      IParseTree pt = (IParseTree)newLocations.get( i );
      if( pt.getParsedElement() instanceof IUsesStatementList )
      {
        newLocations.remove( i-- );
      }
    }
  }

  private void makeExprRootFunction( IGosuProgramInternal gsClass, FunctionStatement callableStmt )
  {
    DynamicFunctionSymbol dfsDecl = getProgramRootExprValueDfs();
    if (dfsDecl != null) {
      getOwner().putDfsDeclInSetByName( dfsDecl );

      StatementList stmtList = makeReturnStatementWithExprRoot( gsClass, callableStmt );
      if( stmtList != null )
      {
        FunctionStatement fs = new FunctionStatement();
        fs.setDynamicFunctionSymbol( dfsDecl );

        dfsDecl.setValueDirectly( stmtList );
        getOwner().pushDynamicFunctionSymbol( dfsDecl );
        fs.setDynamicFunctionSymbol( dfsDecl );

        dfsDecl.setClassMember( true );
        gsClass.getParseInfo().addMemberFunction(dfsDecl);
      }
    }
  }

  private StatementList makeReturnStatementWithExprRoot( IGosuProgramInternal gsClass, FunctionStatement callableStmt )
  {
    Statement statement = (Statement)callableStmt.getDynamicFunctionSymbol().getValueDirectly();

    if( statement != null )
    {
      boolean[] bAbsolute = {false};
      ITerminalStatement significantTerminalStatement = statement.getLeastSignificantTerminalStatement( bAbsolute );
      if( gsClass.isGenRootExprAccess() &&
          bAbsolute[0] &&
          significantTerminalStatement instanceof ReturnStatement &&
          significantTerminalStatement.getParent() != null &&
          significantTerminalStatement.getParent().getParent() == callableStmt )
      {
        ReturnStatement rs = (ReturnStatement)significantTerminalStatement;
        Expression expr = rs.getValue();
        if( expr instanceof IMemberAccessExpression )
        {
          Expression rootExpr = (Expression)((IMemberAccessExpression)expr).getRootExpression();

          ReturnStatement defaultReturnStmt = new ReturnStatement();
          defaultReturnStmt.setValue( rootExpr );
          List<Statement> stmts = new ArrayList<Statement>( 2 );
          stmts.add( defaultReturnStmt );
          StatementList stmtList = new StatementList( getSymbolTable() );
          stmtList.setStatements( stmts );
          return stmtList;
        }
      }
    }

    ReturnStatement defaultReturnStmt = new ReturnStatement();
    NullExpression nullExpr = new NullExpression();
    nullExpr.setType( JavaTypes.OBJECT() );
    defaultReturnStmt.setValue( nullExpr );
    List<Statement> stmts = new ArrayList<Statement>( 2 );
    stmts.add( defaultReturnStmt );
    StatementList stmtList = new StatementList( getSymbolTable() );
    stmtList.setStatements( stmts );
    return stmtList;
  }

  private DynamicFunctionSymbol getProgramRootExprValueDfs()
  {
    for( IDynamicFunctionSymbol dfs : getGosuClass().getMemberFunctions() )
    {
      if( dfs.getName().contains( "evaluateRootExpr" ) )
      {
        return (DynamicFunctionSymbol)dfs;
      }
    }
    return null;
  }

  private FunctionStatement parseProgramAsFunctionStatement( IGosuClassInternal gsClass )
  {
    // Copy the Non-Static Scope so we can reuse it for each member
    //
    IScope nonstaticScope;
    Map<String, List<IFunctionSymbol>> nonstaticDfsMap;
    getSymbolTable().pushScope();
    try
    {
      getOwner().newDfsDeclInSetByName();
      gsClass.putClassMembers( getOwner(), getSymbolTable(), getGosuClass(), false );
      nonstaticDfsMap = getOwner().getDfsDecls();
      getOwner().newDfsDeclInSetByName();
    }
    finally
    {
      nonstaticScope = getSymbolTable().popScope();
    }

    getSymbolTable().pushScope();
    getOwner().newDfsDeclInSetByName();
    FunctionStatement functionStmt;
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    try
    {
      getOwner().setDfsDeclInSetByName( nonstaticDfsMap );
      getOwner().putDfsDeclsInTable( ((IGosuProgramInternal)getGosuClass()).getSymbolTable() );
      getSymbolTable().pushScope( nonstaticScope );
      getOwner().pushParsingStaticMember( false );
      try
      {
        functionStmt = getOwner().parseProgramEntryPointBody();
      }
      finally
      {
        getSymbolTable().popScope();
        getOwner().popParsingStaticMember();
      }

      DynamicFunctionSymbol dfs = functionStmt == null ? null : functionStmt.getDynamicFunctionSymbol();
      if( dfs != null )
      {
        dfs.setClassMember( true );
        if( dfs.getDisplayName().equals( gsClass.getRelativeName() ) )
        {
          gsClass.getParseInfo().addConstructorFunction(dfs);
        }
        else
        {
          gsClass.getParseInfo().addMemberFunction(dfs);
        }
      }
    }
    finally
    {
      getOwner().newDfsDeclInSetByName();
      getSymbolTable().popScope();
    }
    setLocation( iOffset, iLineNum, iColumn, true );
    if( getTokenizer().getTokenStart() == iOffset )
    {
      getLocationsList().remove( getLocationsList().size() - 1 );
    }
    functionStmt = (FunctionStatement)popStatement();
    return functionStmt;
  }

  private void parseClassBodyDecl( String strClassName, IGosuClassInternal gsClass )
  {
    try
    {
      if( strClassName != null )
      {
        IType type = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( strClassName );
        if( TypeSystem.getOrCreateTypeReference( gsClass ) != type && !(gsClass instanceof IGosuClassFragment) )
        {
          getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_DUPLICATE_CLASS_FOUND, type.getName() ) );
        }
      }
    }
    catch( ClassNotFoundException e )
    {
      // ignore
    }

    maybeForceRecursiveTypeToAssignSuperTypes( gsClass );

    verify( getClassStatement(), gsClass instanceof IGosuProgram || match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF );

    if( !putClassMembersOfSuperAndInterfaces( gsClass ) )
    {
      gsClass.setDeclarationsBypassed();
      return;
    }
    if( isInnerClass( gsClass ) && !gsClass.isStatic() )
    {
      addOuterMember( gsClass );
    }

    addAutomaticEnumMethodsAndProperties( gsClass );
    processEnumConstants( gsClass );

    for( Object member = parseFunctionOrConstructorOrFieldDeclaration( gsClass );
         member != null;
         member = parseFunctionOrConstructorOrFieldDeclaration( gsClass ) )
    {
      popStatement();

      if( member instanceof DynamicFunctionSymbol )
      {
        processFunctionSymbol( (DynamicFunctionSymbol)member, gsClass );
      }
      else if( member instanceof DynamicPropertySymbol )
      {
        processPropertySymbol( (DynamicPropertySymbol)member, gsClass );
      }
      else
      {
        processVarStmt( gsClass, (VarStatement)member );
      }
    }
    if( !gsClass.isInterface() )
    {
      if( !gsClass.ensureDefaultConstructor( getSymbolTable(), getOwner() ) )
      {
        getClassStatement().addParseException( new ParseException( makeFullParserState(),
                                                                   Res.MSG_NO_DEFAULT_CTOR_IN,
                                                                   gsClass.getSupertype().getName() ) );
      }
    }
    boolean b = isInnerClass( gsClass ) || match( null, SourceCodeTokenizer.TT_EOF );
    verify( getClassStatement(), b, Res.MSG_END_OF_STMT );

    gsClass.addDelegateImpls( getSymbolTable(), this );

    if( gsClass instanceof IGosuProgramInternal )
    {
      ((IGosuProgramInternal)gsClass).addProgramEntryPoint( getSymbolTable(), this );
    }
    if( gsClass instanceof IGosuTemplateInternal )
    {
      ((IGosuTemplateInternal)gsClass).addTemplateEntryPoints( getSymbolTable(), this );
    }

    gsClass.syncGenericAndParameterizedClasses();

    gsClass.setDeclarationsCompiled();

    if( parseDeclarationsOfLeftOverInnerClasses( gsClass ) )
    {
      gsClass.setInnerDeclarationsCompiled();
    }
  }

  private void maybeForceRecursiveTypeToAssignSuperTypes( IGosuClassInternal gsClass )
  {
    if( gsClass.isParameterizedType() )
    {
      // If this is a recursive type, force super/interface assignment
      gsClass.getSupertype();
      gsClass.getInterfaces();
    }
  }

  private boolean putClassMembersOfSuperAndInterfaces( IGosuClassInternal gsClass )
  {
    if( gsClass.isAnnotation() && JavaTypes.ANNOTATION().isAssignableFrom( gsClass ) )
    {
      // Don't try put members of implicitly extended java.lang.annotation.Annotation
      return true;
    }

    ICompilableTypeInternal enclosingType = gsClass.getEnclosingType();
    if( enclosingType instanceof IGosuClassInternal &&
        ((IGosuClassInternal)enclosingType).isHeaderCompiled() && TypeLord.encloses( enclosingType, getOwner().getGosuClass() ) )
    {
      enclosingType.putClassMembers( getOwner(), getSymbolTable(), getGosuClass(), gsClass.isStatic() );
    }

    for( IType type : gsClass.getInterfaces() )
    {
      if( !(type instanceof ErrorType) )
      {
        if( !putClassMembers( type ) )
        {
          return false;
        }
      }
    }

    return putClassMembers( gsClass.getSuperClass() );
  }

  private boolean putClassMembers( IType type )
  {
    IGosuClassInternal gsType = IGosuClassInternal.Util.getGosuClassFrom( type );
    if( gsType != null )
    {
      gsType.compileDeclarationsIfNeeded();
      if( !gsType.isDeclarationsCompiled() )
      {
        advanceToClassBodyEnd();
        // Try again after enclosing class finishes
        return false;
      }
      gsType.putClassMembers( getOwner(), getSymbolTable(), getGosuClass(), false );
    }
    return true;
  }

  private boolean parseDeclarationsOfLeftOverInnerClasses( IGosuClassInternal gsClass )
  {
    int iCount = 0;
    int iPriorCount;
    Collection<? extends IGosuClass> innerClasses = gsClass.getKnownInnerClassesWithoutCompiling().values();
    do
    {
      iPriorCount = iCount;
      iCount = 0;
      for( IGosuClass c : innerClasses )
      {
        IGosuClassInternal innerClass = (IGosuClassInternal)c;
        if( !innerClass.isDeclarationsCompiled() || !innerClass.isInnerDeclarationsCompiled() )
        {
          if( innerClass.getSourceFileHandle() instanceof InnerClassFileSystemSourceFileHandle )
          {
            int state = getTokenizer().mark();
            parseInnerClassDeclaration( innerClass );
            getTokenizer().restoreToMark( state );
          }
          iCount += (innerClass.isDeclarationsCompiled() && innerClass.isInnerDeclarationsCompiled()) ? 0 : 1;
        }
      }
      if( iPriorCount > 0 && iPriorCount == iCount )
      {
        // Could not decl parse one or more inner classes, must be a cycle; will reparse later
        return false;
      }
    } while( iCount > 0 );
    return true;
  }

  private void addAutomaticEnumMethodsAndProperties( IGosuClassInternal gsClass )
  {
    if( gsClass.isEnum() )
    {
      addEnumProperty( gsClass, new EnumCodePropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
      addEnumProperty( gsClass, new EnumDisplayNamePropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
      addEnumProperty( gsClass, new EnumNamePropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
      addEnumProperty( gsClass, new EnumOrdinalPropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
      addEnumProperty( gsClass, new EnumValuePropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );
      addEnumProperty( gsClass, new EnumAllValuesPropertySymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() ) );

      DynamicFunctionSymbol dfs = new EnumValueOfFunctionSymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() );
      gsClass.getParseInfo().addMemberFunction( dfs );
      getOwner().putDfsDeclInSetByName( dfs );

      dfs = new EnumValuesFunctionSymbol( gsClass, TypeSystem.getCompiledGosuClassSymbolTable() );
      gsClass.getParseInfo().addMemberFunction( dfs );
      getOwner().putDfsDeclInSetByName( dfs );
    }
  }

  private void addEnumProperty( IGosuClassInternal gsClass, DynamicPropertySymbol dps )
  {
    gsClass.getParseInfo().addMemberProperty( dps );
    getOwner().putDfsDeclInSetByName( dps.getGetterDfs() ); // put in dfs map to prevent overriding by enum impl class
  }

  private void processEnumConstants( IGosuClassInternal gsClass )
  {
    boolean bEnum = gsClass != null && gsClass.isEnum();
    if( !bEnum )
    {
      return;
    }

    Token t = new Token();
    int state = getTokenizer().mark();
    boolean bAtLeastOneConst = false;
    boolean bConst;
    do
    {
      bConst = false;
      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      if( match( t, null, SourceCodeTokenizer.TT_WORD, true ) &&
          !Keyword.isKeyword( t._strValue ) &&
          match( t, SourceCodeTokenizer.TT_WORD ) )
      {
        VarStatement varStmt = parseEnumConstantDecl( t._strValue );
        varStmt.setNameOffset( t.getTokenStart(), t._strValue );
        setLocation( iOffset, iLineNum, iColumn );
        popStatement();

        processVarStmt( gsClass, varStmt );
        bAtLeastOneConst = bConst = true;
      }
      if( match( null, ';' ) )
      {
        break;
      }
    } while( bConst && match( null, ',' ) );
    if( !bAtLeastOneConst )
    {
      getTokenizer().restoreToMark( state );
    }
  }

  private VarStatement parseEnumConstantDecl( String strIdentifier )
  {
    VarStatement varStmt = new VarStatement();
    ModifierInfo modifiers = new ModifierInfo( Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL );

    varStmt.setModifierInfo( modifiers );

    verify( varStmt, getSymbolTable().getSymbol( strIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );

    if( match( null, null, '(', true ) )
    {
      eatParenthesized( varStmt, Res.MSG_EXPECTING_RIGHTPAREN_FUNCTION_DEF );
      if( match( null, null, '{', true ) )
      {
        eatStatementBlock( varStmt, Res.MSG_EXPECTING_RIGHTBRACE_STMTBLOCK );
      }
    }

    IType type = getGosuClass();

    AbstractDynamicSymbol symbol = new DynamicSymbol( getGosuClass(), getSymbolTable(), strIdentifier, type, null );
    modifiers.addAll( symbol.getModifierInfo() );
    symbol.setModifierInfo( modifiers );
    varStmt.setSymbol( symbol );
    varStmt.setEnumConstant( true );
    getSymbolTable().putSymbol( symbol );

    pushStatement( varStmt );

    return varStmt;
  }

  private void processVarStmt( IGosuClassInternal gsClass, VarStatement varStmt )
  {
    gsClass.getParseInfo().addMemberField(varStmt);
  }

  public void processFunctionSymbol( DynamicFunctionSymbol dfs, IGosuClassInternal gsClass )
  {
    getSymbolTable().putSymbol( dfs );
    if( dfs.getDisplayName().equals( gsClass.getRelativeName() ) )
    {
      gsClass.getParseInfo().addConstructorFunction(dfs);
    }
    else
    {
      gsClass.getParseInfo().addMemberFunction(dfs);
    }
  }

  void processPropertySymbol( DynamicPropertySymbol dps, ICompilableTypeInternal gsClass )
  {
    getSymbolTable().putSymbol( dps );
      dps.addMemberSymbols( gsClass );
  }

  private void addOuterMember( ICompilableTypeInternal gsClass )
  {
    while( gsClass instanceof IBlockClass )
    {
      // blocks should never be considered part of the outer hierarchy
      gsClass = gsClass.getEnclosingType();
    }
    DynamicFunctionSymbol dfs = new OuterFunctionSymbol( getSymbolTable(), gsClass );
    dfs.setClassMember( true );
    DynamicPropertySymbol dps = getOrCreateDynamicPropertySymbol( getClassStatement(), gsClass, dfs, true );
    processPropertySymbol( dps, gsClass );
  }

  private void parseEnhancementBodyDecl( IGosuClassInternal gsClass )
  {
    try
    {
      IType type = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( gsClass.getName() );
      if( gsClass != type )
      {
        getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_DUPLICATE_ENHANCEMENT_FOUND, type.getName() ) );
      }
    }
    catch( ClassNotFoundException e )
    {
      // ignore
    }

    verify( getClassStatement(), match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF );

    for( Object result = parseFunctionDeclForEnhancement( gsClass );
         result != null;
         result = parseFunctionDeclForEnhancement( gsClass ) )
    {
      if( !result.equals( Boolean.FALSE ) )
      {
        popStatement();

        if( result instanceof DynamicFunctionSymbol )
        {
          DynamicFunctionSymbol dfs = (DynamicFunctionSymbol)result;
          getSymbolTable().putSymbol( dfs );
          gsClass.getParseInfo().addMemberFunction(dfs);
        }
        else if( result instanceof DynamicPropertySymbol )
        {
          getSymbolTable().putSymbol( (DynamicPropertySymbol)result );
          ((DynamicPropertySymbol)result).addMemberSymbols( gsClass );
        }
      }
    }

    verify( getClassStatement(), isInnerClass( gsClass ) || match( null, SourceCodeTokenizer.TT_EOF ), Res.MSG_END_OF_STMT );
    gsClass.syncGenericAndParameterizedClasses();
    gsClass.setDeclarationsCompiled();
    gsClass.setInnerDeclarationsCompiled();
  }

  public List<ParseException> resolveFunctionAndPropertyDecls( ISymbolTable table )
  {

    for( Object member = parseFunctionOrConstructorOrFieldDeclaration( null );
         member != null; member = parseFunctionOrConstructorOrFieldDeclaration( null ) )
    {
      popStatement();

      if( member instanceof DynamicFunctionSymbol )
      {
        table.putSymbol( (DynamicFunctionSymbol)member );
      }
      else if( member instanceof DynamicPropertySymbol )
      {
        table.putSymbol( (DynamicPropertySymbol)member );
      }
    }
    pushStatement( getClassStatement() );
    setLocation( _iClassOffset, _iClassLineNum, _iClassColumn );
    popStatement();

    //noinspection RedundantCast,unchecked
    return (List<ParseException>)(List)getClassStatement().getParseExceptions();
  }

  private Object parseFunctionDeclForEnhancement( IGosuClassInternal gsClass )
  {
    int[] location = new int[3];
    Object rtn = _parseFunctionDeclForEnhancement( gsClass, location );
    if( rtn != null && !Boolean.FALSE.equals( rtn ) )
    {
      setLocation( location[0], location[1], location[2] );
    }
    return rtn;
  }

  private Object _parseFunctionDeclForEnhancement( IGosuClassInternal gsClass, int[] location )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();

    String strMemberKeyword[] = new String[1];
    ModifierInfo modifiers = parseUntilMemberKeyword( strMemberKeyword, false, location );
    
    if( modifiers.getModifiers() == -1 )
    {
      return null;
    }

    if( strMemberKeyword[0] != null && strMemberKeyword[0].equals( Keyword.KW_function.toString() ) )
    {
      FunctionStatement fs = new FunctionStatement();
      DynamicFunctionSymbol dfs = getOwner().parseFunctionDecl( fs, false, false, modifiers );
      fs.setDynamicFunctionSymbol( dfs );
      pushStatement( fs );
      verify( fs, !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, Keyword.KW_function );
      if( dfs != null )
      {
        dfs.setClassMember( true );
      }
      if( verify( getClassStatement(), !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE ) )
      {
        if( !Modifier.isNative( modifiers.getModifiers() ) )
        {
          eatStatementBlock( fs, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
        }
      }

//      verifyTypeVarVariance( Variance.COVARIANT, fs, false, dfs.getType() );

      return dfs;
    }
    else if( strMemberKeyword[0] != null && strMemberKeyword[0].equals( Keyword.KW_property.toString() ) )
    {
      boolean bGetter = match( null, Keyword.KW_get );
      verify( getClassStatement(), bGetter || match( null, Keyword.KW_set ), Res.MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER );
      FunctionStatement fs = new FunctionStatement();
      DynamicFunctionSymbol dfs = getOwner().parseFunctionDecl( fs, true, bGetter, modifiers );
      fs.setDynamicFunctionSymbol( dfs );
      pushStatement( fs );
      setLocation( iOffset, iLineNum, iColumn );
      popStatement();

      verifyNoCombinedFinalStaticModifierDefined( fs, false, modifiers.getModifiers() );
      verify( fs, !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, Keyword.KW_function );
      if( dfs != null )
      {
        dfs.setClassMember( true );
      }
      if( verify( getClassStatement(), !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE ) )
      {
        if( !Modifier.isNative( modifiers.getModifiers() ) )
        {
          eatStatementBlock( fs, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
        }
      }
      DynamicPropertySymbol dps = dfs == null ? null : getOrCreateDynamicPropertySymbol( getClassStatement(), gsClass, dfs, bGetter );
      PropertyStatement statement = new PropertyStatement( fs, dps );
      verifyPropertiesAreSymmetric( bGetter, dfs, dps, statement );
      pushStatement( statement );

//      if( bGetter )
//      {
//        verifyTypeVarVariance( Variance.COVARIANT, fs, false, dps.getGetterDfs().getReturnType() );
//      }
//      else if( dps.getSetterDfs().getArgTypes().length > 0 )
//      {
//        verifyTypeVarVariance( Variance.CONTRAVARIANT, fs, false, dps.getSetterDfs().getArgTypes()[0] );
//      }

      return dps;
    }
    else if( strMemberKeyword[0] != null && strMemberKeyword[0].equals( Keyword.KW_var.toString() ) )
    {
      return Boolean.FALSE;
    }

    return null;
  }

  private void parseClassStatementAsEnhancement( IGosuClassInternal gsClass )
  {
    //## todo: remove this scope?
    IGosuEnhancementInternal enhancement = (IGosuEnhancementInternal)gsClass;
    getSymbolTable().pushScope();
    try
    {
      verify( getClassStatement(), match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF );
      parseClassMembers( gsClass );

      for( Statement stmt = peekStatement(); stmt != null; stmt = peekStatement() )
      {
        stmt = popStatement();
        IType enhancedType = enhancement.getEnhancedType();
        if( stmt instanceof FunctionStatement )
        {
          FunctionStatement func = (FunctionStatement)stmt;
          if( func.getDynamicFunctionSymbol() != null && !(enhancedType instanceof ErrorType) )
          {
            ITypeInfo typeInfo = enhancedType.getTypeInfo();
            if( typeInfo != null )
            {
              IMethodInfo mi = typeInfo instanceof IRelativeTypeInfo
                               ? ((IRelativeTypeInfo)typeInfo).getMethod( enhancement, func.getFunctionName(), func.getDynamicFunctionSymbol().getArgTypes() )
                               : typeInfo.getMethod( func.getFunctionName(), func.getDynamicFunctionSymbol().getArgTypes() );
              if( overridesMethodWithDefaultParams(func, typeInfo) )
              {
                addDeclaredNameParseError( func, Res.MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS, mi.getDisplayName(), enhancedType.getRelativeName() );
              }
              else if( (mi != null) && (!featureIsOwnedByEnhancement( enhancement, mi ) || (enhancedType != JavaTypes.OBJECT() && GosuClass.isObjectMethod( mi ))) )
              {
                addDeclaredNameParseError( func, Res.MSG_CANNOT_OVERRIDE_FUNCTIONS_IN_ENHANCEMENTS, mi.getDisplayName(), enhancedType.getRelativeName() );
              }
              else if( enhancedType instanceof IGosuClass )
              {
                String name = func.getFunctionName();
                DynamicFunctionSymbol dfs = func.getDynamicFunctionSymbol();
                if( name.startsWith( "set" ) && dfs.getArgs().size() == 1 )
                {
                  ITypeInfo ti = enhancedType.getTypeInfo();
                  IPropertyInfo pi = ((IRelativeTypeInfo)ti).getProperty( enhancement, name.substring( 3, name.length() ) );
                  if( pi instanceof GosuPropertyInfo )
                  {
                    ReducedDynamicPropertySymbol dps = ((GosuPropertyInfo)pi).getDps();
                    if( dps.getSetterDfs() != null )
                    {
                      IType argType = dfs.getArgs().get( 0 ).getType();
                      if( argType.equals( dps.getType() ) )
                      {
                        addDeclaredNameParseError( func, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, dfs.getName(), dps.getName() );
                      }
                      else if( getOwner().doTypesReifyToTheSameBytecodeType( argType, dps.getType() ) )
                      {
                        addDeclaredNameParseError( func, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT_UPON_REIFICATION, dfs.getName(), dps.getName() );
                      }
                    }
                  }
                }
                else if( (name.startsWith( "get" ) || name.startsWith( "is" )) && dfs.getArgs().size() == 0 )
                {
                  ITypeInfo ti = enhancedType.getTypeInfo();
                  IPropertyInfo pi = ((IRelativeTypeInfo)ti).getProperty( enhancement, name.substring( name.startsWith( "get" ) ? 3 : 2, name.length() ) );
                  if( pi instanceof GosuPropertyInfo )
                  {
                    ReducedDynamicPropertySymbol dps = ((GosuPropertyInfo)pi).getDps();
                    if( dps.getGetterDfs() != null )
                    {
                      addDeclaredNameParseError( func, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, dfs.getName(), dps.getName() );
                    }
                  }
                }
              }
            }
          }
        }
        else if( stmt instanceof PropertyStatement )
        {
          PropertyStatement prop = (PropertyStatement)stmt;
          ITypeInfo typeInfo = enhancedType.getTypeInfo();
          if( typeInfo != null && !(enhancedType instanceof ErrorType) )
          {
            IPropertyInfo pi = typeInfo instanceof IRelativeTypeInfo
                               ? ((IRelativeTypeInfo)typeInfo).getProperty( enhancement, prop.getFunctionName() )
                               : typeInfo.getProperty( prop.getFunctionName() );
            if( pi != null && !featureIsOwnedByEnhancement( enhancement, pi ) )
            {
              addDeclaredNameParseError( prop, Res.MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS, pi.getDisplayName(), enhancedType.getRelativeName() );
            }
            else
            {
              FunctionStatement funcStmt = prop.getPropertyGetterOrSetter();
              DynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
              String name = dfs.getDisplayName().substring( 1 );
              if( dfs.getArgs().size() == 0 )
              {
                ITypeInfo ti = enhancedType.getTypeInfo();
                IMethodInfo mi = ((IRelativeTypeInfo)ti).getMethod( enhancement, "get" + name );
                mi = mi == null ? ((IRelativeTypeInfo)ti).getMethod( enhancement, "is" + name ) : mi;
                if( mi != null )
                {
                  addDeclaredNameParseError( prop, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, mi.getName(), name );
                }
              }
              else if( funcStmt.getParameters().size() > 0 )
              {
                ITypeInfo ti = enhancedType.getTypeInfo();
                for( IMethodInfo mi: ((IRelativeTypeInfo)ti).getMethods( enhancement ) )
                {
                  if( mi.getDisplayName().equals( "set" + name ) && mi.getParameters().length == 1 )
                  {
                    IType argType = mi.getParameters()[0].getFeatureType();
                    if( argType.equals( dfs.getArgTypes()[0] ) )
                    {
                      addDeclaredNameParseError( prop, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, mi.getName(), dfs.getName() );
                    }
                    else if( getOwner().doTypesReifyToTheSameBytecodeType( argType, dfs.getArgTypes()[0] ) )
                    {
                      addDeclaredNameParseError( prop, Res.MSG_PROPERTY_AND_FUNCTION_CONFLICT, mi.getName(), dfs.getName() );
                    }
                  }
                }
              }
            }
          }
        }
        else if( !(stmt instanceof NoOpStatement ||
                   stmt instanceof NamespaceStatement ||
                   stmt instanceof UsesStatement) )
        {
          ParseException parseException = new ParseException( stmt.getLineNum(), 1, stmt.getLocation().getColumn(), stmt.getLocation().getOffset(), stmt.getLocation().getExtent(),
                                                              getSymbolTable(), Res.MSG_ENHANCEMENT_DOES_NOT_ACCEPT_THIS_STATEMENT );
          stmt.addParseException( parseException );
        }
      }

      verify( getClassStatement(), match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF );
    }
    finally
    {
      getSymbolTable().popScope();
    }
  }

  void addDeclaredNameParseError( IParsedElementWithAtLeastOneDeclaration stmt, ResourceKey key, Object... args )
  {
    int nameOffset = stmt.getNameOffset( null );
    ParseException parseException = new ParseException( stmt.getLineNum(), 1, stmt.getLocation().getColumn(), nameOffset, nameOffset + ((stmt instanceof VarStatement) ? ((VarStatement)stmt).getIdentifierName().length() : stmt.getFunctionName().length()),
                                                        getSymbolTable(), key, args );
    stmt.addParseException( parseException );
  }

  private boolean overridesMethodWithDefaultParams(FunctionStatement func, ITypeInfo typeInfo) {
    if( !(typeInfo instanceof IRelativeTypeInfo) )
    {
      return false;
    }
    IRelativeTypeInfo rti = (IRelativeTypeInfo) typeInfo;
    for( IMethodInfo mi : rti.getMethods( func.getGosuClass() ) )
    {
      if( mi.getDisplayName().equals( func.getFunctionName() ) && mi instanceof GosuMethodInfo && !featureIsOwnedByEnhancement( func.getGosuClass(), mi ) )
      {
        final ReducedDynamicFunctionSymbol dfs0 = ((GosuMethodInfo) mi).getDfs();
        final DynamicFunctionSymbol dfs1 = func.getDynamicFunctionSymbol();
        return dfs0 != null && dfs1 != null && (((IInvocableType) dfs0.getType()).hasOptionalParams() || dfs1.hasOptionalParameters());
      }
    }
    return false;
  }

  private boolean featureIsOwnedByEnhancement( IGosuClass enhancement, IFeatureInfo iMethodInfo )
  {
    if( !(enhancement instanceof IGosuEnhancementInternal) )
    {
      return false;
    }
    IType ownerType = iMethodInfo.getOwnersType();
    if( ownerType != null && ownerType.isParameterizedType() )
    {
      ownerType = ownerType.getGenericType();
    }
    IType enhancementType = enhancement;
    if( enhancementType != null && enhancementType.isParameterizedType() )
    {
      enhancementType = enhancementType.getGenericType();
    }

    if( enhancementType instanceof IGosuEnhancementInternal &&
        ownerType instanceof IGosuEnhancementInternal )
    {
      return GosuObjectUtil.equals( enhancementType.getName(), ownerType.getName() );
    }
    else
    {
      return GosuObjectUtil.equals( enhancementType, ownerType );
    }
  }

  String parseHeader( IGosuClassInternal gsClass, boolean bParseEnhancementOnly, boolean bIsAnonymous, boolean bResolveUsesTypes )
  {
    boolean bPushedScope = pushScopeIfNeeded( gsClass );

    if( gsClass.isHeaderCompiled() )
    {
      ((CompilationState)gsClass.getCompilationState()).setReparsingHeader( true );
    }
    else
    {
      gsClass.setCompilingHeader( true );
    }
    getTokenizer().pushOffsetMarker( this );
    gsClass.createNewParseInfo();
    setClassStatement( gsClass.getParseInfo().getClassStatement() );

    ScriptPartId scriptPartId = new ScriptPartId( gsClass, null );
    getOwner().pushScriptPart( scriptPartId );
    GosuClassCompilingStack.pushCompilingType( gsClass );
    try
    {
      setTokenizerToClassStart();

      if( match( null, SourceCodeTokenizer.TT_EOF ) )
      {
        if( gsClass instanceof IGosuProgram )
        {
          // Let empty *program* source parse
          //## todo: cache and reuse empty program class
          gsClass.setSuperType( JavaTypes.OBJECT() );
        }
        else if( getClassStatement() != null && getClassStatement().getClassFileStatement() != null )
        {
          getClassStatement().getClassFileStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_NO_SOURCE_FOUND ) );
        }
        return null;
      }

      getOwner().checkInstruction( true );

      if( gsClass instanceof IGosuProgram )
      {
        getOwner().parseProgramClasspathStatements();
        getOwner().parseProgramTypeLoaderStatements();
      }

      getOwner().checkInstruction( true );
      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      if( match( null, Keyword.KW_package ) )
      {
        getOwner().parseNamespaceStatement();
        setLocation( iOffset, iLineNum, iColumn );
        popStatement();
      }
      else if( gsClass instanceof IGosuProgram )
      {
        ISourceFileHandle sfh = gsClass.getSourceFileHandle();
        boolean bEval = sfh instanceof StringSourceFileHandle;
        if( bEval )
        {
          ITypeUsesMap typeUsesMap = ((StringSourceFileHandle)sfh).getTypeUsesMap();
          if( typeUsesMap != null )
          {
            getOwner().setTypeUsesMap( typeUsesMap );
          }
        }
        if( gsClass.isAnonymous() )
        {
          // Anonymous implies Eval program...

          gsClass.setEnclosingType( TypeSystem.getByFullNameIfValid( sfh.getParentType() ) );
          IType enclosingType = gsClass.getEnclosingTypeReference();
          getOwner().setNamespace(enclosingType.getNamespace());
          Map<String, ITypeVariableDefinition> capturedTypeVars = bEval ? ((StringSourceFileHandle)sfh).getCapturedTypeVars() : null;
          if( capturedTypeVars != null )
          {
            getOwner().getTypeVariables().putAll( capturedTypeVars );
          }
        }
        else
        {
          String strNamespace = getGosuClass().getNamespace();
          getOwner().setNamespace( strNamespace != null && !strNamespace.isEmpty() ? strNamespace : IGosuProgram.PACKAGE );
        }
      }
      else if( !isInnerClass( gsClass ) )
      {
        getOwner().setNamespace( "" );
      }

      getOwner().checkInstruction( true );

      getOwner().parseUsesStatementList( bResolveUsesTypes );

      if( gsClass.getEnclosingType() == null )
      {
        // Inner classes start parsing right at the class-stmt, so they must
        // get at the uses map from the top-level enclosing class
        gsClass.setTypeUsesMap(getOwner().getTypeUsesMap());
      }

      ClassType classType;
      if( gsClass.isAnonymous() && !(gsClass instanceof IGosuProgram) )
      {
        try
        {
          classType = parseAnonymousClassHeader( gsClass );
        }
        catch( InnerClassNotFoundException e )
        {
          classType = ClassType.Class;
        }
        _iClassOffset = getTokenizer().getTokenStart();
        _iClassLineNum = getTokenizer().getLineNumber();
        _iClassColumn = getTokenizer().getTokenColumn();
      }
      else if( gsClass instanceof IGosuProgram )
      {
        gsClass.setModifierInfo(new ModifierInfo(Modifier.PUBLIC | Modifier.FINAL));
        if( gsClass.isAnonymous() ) // generated 'eval' program
        {
          final IParsedElement enclosingEvalExpression = ((IGosuProgram) gsClass).getEnclosingEvalExpression();
          IParseTree parseTree = enclosingEvalExpression == null ? null : enclosingEvalExpression.getLocation();
          IFunctionStatement fs = (parseTree == null ? null : parseTree.getEnclosingFunctionStatement());
          if( (fs != null && fs.getDynamicFunctionSymbol().isStatic()) ||
              // Note a null enclosingEvalExpression implies this anon program is a bare expression that is artificially
              // executed as though it were defined somewhere within the enclosing class e.g., an old-style Gosu annotation,
              // therefore the expression needs private access to the outer class and must be compiled as a static,
              // yet anonymous, inner class
              enclosingEvalExpression == null )
          {
            ((ModifierInfo)gsClass.getModifierInfo()).addModifiers( Modifier.STATIC );
          }
        }

        // Optional 'extends' clause for specifying Super Class for a program
        parseProgramExtendsStatement( gsClass, bResolveUsesTypes );

        classType = ClassType.Class;
      }
      else
      {
        getOwner().checkInstruction( true );
        _iClassOffset = getTokenizer().getTokenStart();
        _iClassLineNum = getTokenizer().getLineNumber();
        _iClassColumn = getTokenizer().getTokenColumn();

        if( !bIsAnonymous )
        {
          classType = parseClassType( gsClass, true );
          if( classType == ClassType.Interface || classType == ClassType.Structure || classType == ClassType.Annotation )
          {
            ((ModifierInfo)gsClass.getModifierInfo()).addModifiers( Modifier.ABSTRACT );
          }
          else if( classType == ClassType.Enum )
          {
            ((ModifierInfo)gsClass.getModifierInfo()).addModifiers( Modifier.FINAL );
          }

          if( classType == ClassType.Annotation )
          {
            gsClass.addInterface( JavaTypes.ANNOTATION() );
          }
        }
        else
        {
          classType = parseClassTypeForHeader( gsClass );
        }
      }

      if( classType == null )
      {
        if( bParseEnhancementOnly )
        {
          return null;
        }
        verify( getClassStatement(), false, Res.MSG_EXPECTING_NAME_CLASS_DEF );
      }
      if( classType == ClassType.Enhancement )
      {
        if( gsClass instanceof IGosuEnhancementInternal )
        {
          IGosuEnhancementInternal scriptEnhancement = (IGosuEnhancementInternal)gsClass;
          scriptEnhancement.setFoundCorrectHeader();
          return parseEnhancementHeaderSuffix( scriptEnhancement );
        }
        else
        {
          getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_MUST_BE_DEFINED_AS_CLASS ) );
          return null;
        }
      }
      else if( classType != null && !bParseEnhancementOnly )
      {
        if( classType == ClassType.Enum )
        {
          gsClass.setEnum();
        }
        return parseClassOrInterfaceHeaderSuffix( gsClass, classType, bResolveUsesTypes );
      }
      else
      {
        return null;
      }
    }
    finally
    {
      boolean bHeaderCompiled;
      try
      {
        bHeaderCompiled = gsClass.isHeaderCompiled();
        if( !bHeaderCompiled )
        {
          parseInnerClassHeaders( gsClass, bResolveUsesTypes );
        }
      }
      finally
      {
        GosuClassCompilingStack.popCompilingType();
      }
      getOwner().popScriptPart( scriptPartId );
      ((CompilationState)gsClass.getCompilationState()).setReparsingHeader( false );
      gsClass.setCompilingHeader( false );
      gsClass.setHeaderCompiled();
      popScopeIfNeeded( bPushedScope, gsClass );
      getTokenizer().popOffsetMarker( this );
      if( !bHeaderCompiled )
      {
        removeTypeVarsFromParserMap( gsClass );
      }
    }
  }

  private void removeTypeVarsFromParserMap( IGosuClassInternal gsClass )
  {
    for( IGenericTypeVariable gtv : gsClass.getGenericTypeVariables() )
    {
      ITypeVariableDefinition typeVarDef = gtv.getTypeVariableDefinition();
      Map<String, ITypeVariableDefinition> typeVarMap = getOwner().getTypeVariables();
      if( typeVarMap.containsValue( typeVarDef ) )
      {
        typeVarMap.remove( typeVarDef.getName() );
      }
    }
  }

  private boolean pushScopeIfNeeded( final IGosuClassInternal gsClass )
  {
    ISymbolTable compilingClass = CompiledGosuClassSymbolTable.instance().getSymbolTableForCompilingClass( gsClass );
    if( compilingClass != null )
    {
      return false;
    }

    // *barf*
    if( gsClass.getParser() != null )
    {
      CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( gsClass, gsClass.getParser().getSymbolTable() );
    }
    else
    {
      CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable( gsClass );
    }

    getSymbolTable().pushIsolatedScope(new GosuClassTransparentActivationContext(gsClass, false));
    return true;
  }

  private void popScopeIfNeeded( boolean bPop, IGosuClass gsClass )
  {
    if( bPop )
    {
      getSymbolTable().popScope();
      CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable( gsClass );
    }
  }

  private void setTokenizerToClassStart()
  {
    if( isInnerClass( getGosuClass() ) )
    {
      getTokenizer().reset();
    }
    if( !getTokenizer().isPositioned() )
    {
      getTokenizer().nextToken();
    }
  }

  private ClassType parseAnonymousClassHeader( IGosuClassInternal gsClass )
  {
    ClassType classType = ClassType.Class;
    ParsedElement elem;
    if( match( null, null, '(', true ) )
    {
      elem = getClassStatement();
    }
    else if( !getOwner().parseTypeLiteral() )
    {
      throw new InnerClassNotFoundException();
    }
    else
    {
      elem = popExpression();
    }
    eatParenthesized( elem, Res.MSG_EXPECTING_FUNCTION_CLOSE );
    //getLocationsList().remove( superTypeLiteral.getLocation() ); // rely on the new-expr to keep the type literal *it* parses
    return classType;
  }

  private boolean goToPosition( int iOffset )
  {
    try
    {
      getTokenizer().goToPosition( iOffset );
      return true;
    }
    catch( IOException e )
    {
      //noinspection ThrowableResultOfMethodCallIgnored
      getClassStatement().addParseException( ParseException.wrap( e, makeFullParserState() ) );
    }
    return false;
  }

  private ClassType parseClassTypeForHeader( IGosuClassInternal gsClass )
  {
    while( true )
    {
      if( match( null, SourceCodeTokenizer.TT_EOF ) )
      {
        return null;
      }
      ClassType classType = parseClassType( gsClass, !gsClass.isDeclarationsCompiled() );
      if( classType != null )
      {
        return classType;
      }
      getTokenizer().nextToken();
    }
  }

  private ClassType parseClassType( IGosuClassInternal gsClass, boolean bSetModifiers )
  {
    ModifierInfo modifiers = parseModifiersForClass( gsClass, bSetModifiers );

    if( !Modifier.isInternal( modifiers.getModifiers() )
        && !Modifier.isProtected( modifiers.getModifiers() )
        && !Modifier.isPrivate( modifiers.getModifiers() ) )
    {
      modifiers.addModifiers( Modifier.PUBLIC );
    }
    ClassType classType = null;
    if( match( null, Keyword.KW_enhancement ) )
    {
      classType = ClassType.Enhancement;
      if( bSetModifiers )
      {
        verify( getClassStatement(), !Modifier.isPrivate( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, classType.name() );
        verify( getClassStatement(), !Modifier.isProtected( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_protected, classType.name() );
        verify( getClassStatement(), !Modifier.isInternal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_internal, classType.name() );
        verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, classType.name() );
        verify( getClassStatement(), !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_abstract, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        verifyNoAbstractHideOverrideStaticModifierDefined( getClassStatement(), false, modifiers.getModifiers(), Keyword.KW_enhancement );
        gsClass.setModifierInfo(modifiers);
      }
    }
    else if( match( null, Keyword.KW_interface ) )
    {
      classType = ClassType.Interface;
      if( bSetModifiers )
      {
        verify( getClassStatement(), !Modifier.isHide( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, classType.name() );
        verify( getClassStatement(), !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, classType.name() );
        verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        if( gsClass.getEnclosingType() != null )
        {
          modifiers.addModifiers( Modifier.STATIC );
        }
        gsClass.setModifierInfo(modifiers);
      }
    }
    else if( match( null, Keyword.KW_structure ) )
    {
      classType = ClassType.Structure;
      if( bSetModifiers )
      {
        verify( getClassStatement(), !Modifier.isHide( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, classType.name() );
        verify( getClassStatement(), !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, classType.name() );
        verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        if( gsClass.getEnclosingType() != null )
        {
          modifiers.addModifiers( Modifier.STATIC );
        }
        gsClass.setModifierInfo(modifiers);
      }
    }
    else if( match( null, Keyword.KW_annotation ) )
    {
      classType = ClassType.Annotation;
      if( bSetModifiers )
      {
        verify( getClassStatement(), !Modifier.isHide( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, classType.name() );
        verify( getClassStatement(), !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, classType.name() );
        verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        modifiers.addModifiers( Modifier.ANNOTATION );
        if( gsClass.getEnclosingType() != null )
        {
          modifiers.addModifiers( Modifier.STATIC );
        }
        gsClass.setModifierInfo(modifiers);
      }
    }
    else if( match( null, Keyword.KW_class ) )
    {
      classType = ClassType.Class;
      if( bSetModifiers )
      {
        verify( getClassStatement(), !Modifier.isHide( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_hide, classType.name() );
        verify( getClassStatement(), !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        gsClass.setModifierInfo(modifiers);
      }
    }
    else if( match( null, Keyword.KW_enum ) )
    {
      classType = ClassType.Enum;
      if( bSetModifiers )
      {
        verifyNoAbstractHideOverrideModifierDefined( getClassStatement(), false, modifiers.getModifiers(), Keyword.KW_final );
        verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, classType.name() );
        verify( getClassStatement(), !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, classType.name() );
        gsClass.setModifierInfo(modifiers);
      }
    }

    if( gsClass.shouldFullyCompileAnnotations() )
    {
      verifyModifiers( getClassStatement(), modifiers, UsageTarget.TypeTarget );
    }
    gsClass.setFullDescription( modifiers.getDescription() );

    if( bSetModifiers && classType != null && gsClass.getEnclosingType() == null )
    {
      verify( getClassStatement(), !Modifier.isPrivate( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_private, classType.name() );
      verify( getClassStatement(), !Modifier.isProtected( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_protected, classType.name() );
      verify( getClassStatement(), !Modifier.isStatic( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_static, classType.name() );
    }

    return classType;
  }

  private ModifierInfo parseModifiersForClass( IGosuClassInternal gsClass, boolean bSetModifiers )
  {
    ModifierInfo modifiers;ICompilableTypeInternal enclosingType = gsClass.getEnclosingType();
    if( enclosingType instanceof IGosuClassInternal && ((IGosuClassInternal)enclosingType).isDeclarationsCompiled() )
    {
      // push static class symbols for annotations (they are part of modifier parsing)
      ClassScopeCache scopeCache = makeClassScopeCache( (IGosuClassInternal)enclosingType );
      pushClassSymbols( true, scopeCache );
      try
      {
        modifiers = parseModifiers( !bSetModifiers );
      }
      finally
      {
        popClassSymbols();
      }
    }
    else
    {
      modifiers = parseModifiers( !bSetModifiers );
    }
    return modifiers;
  }

  private String parseClassOrInterfaceHeaderSuffix( IGosuClassInternal gsClass, ClassType classType, boolean bResolveTypes )
  {
    String strClassName;

    IGosuClassInternal gosuObjectInterface = getGosuObjectInterface();
    if (gosuObjectInterface == null) {
      return gsClass.getName();
    }

    if( gsClass instanceof IGosuProgram )
    {
      gsClass.addInterface(gosuObjectInterface);
      if( !gsClass.isAnonymous() )
      {
        IType type = parseEnhancedOrImplementedType( gsClass, false, Collections.<IType>emptyList() );
        gsClass.setSuperType( type );
      }
      strClassName = gsClass.getName();
    }
    else if( gsClass.isAnonymous() )
    {
      gsClass.addInterface(gosuObjectInterface);
      strClassName = gsClass.getName();

      if( gsClass.isHeaderCompiled() )
      {
        ClassDeclaration classDeclaration = new ClassDeclaration( strClassName );
        pushExpression( classDeclaration );
        SourceCodeTokenizer tokenizer = getOwner().getTokenizer();
        setLocation( tokenizer.getTokenStart(), tokenizer.getLineNumber(), tokenizer.getTokenColumn(), true, true );
        popExpression();
        getClassStatement().setClassDeclaration( classDeclaration );
//        makeSyntheticClassDeclaration( strClassName, false );
      }
    }
    else
    {
      boolean bStructure = classType.equals( ClassType.Structure );
      boolean bAnnotation = classType.equals( ClassType.Annotation );
      boolean bInterface = bStructure || bAnnotation || classType.equals( ClassType.Interface );
      gsClass.setInterface( bInterface );
      gsClass.setStructure( bStructure );

      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();
      Token t = new Token();
      verify( getClassStatement(), match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_CLASS_DEF );
      strClassName = t._strValue;
      String strNamespace;
      if( isTopLevelClass( getGosuClass() ) )
      {
        strNamespace = getOwner().getNamespace();
      }
      else
      {
        strNamespace = getGosuClass().getEnclosingType().getName();
      }
      strClassName = GosuStringUtil.isEmpty(strNamespace)
                     ? strClassName
                     : strNamespace + '.' + strClassName;
      if( gsClass.getEnclosingTypeReference() == null && strClassName != null && !strClassName.equals( gsClass.getName() ) )
      {
        verify( getClassStatement(), false, Res.MSG_WRONG_CLASSNAME, strClassName, gsClass.getName() );
      }

      if( strClassName != null && gsClass.isHeaderCompiled() )
      {
        ClassDeclaration classDeclaration = new ClassDeclaration( strClassName );
        pushExpression( classDeclaration );
        setLocation( iOffset, iLineNum, iColumn );
        popExpression();
        getClassStatement().setClassDeclaration( classDeclaration );
      }

      List<ITypeVariableDefinitionExpression> typeVarLiteralList = getOwner().parseTypeVariableDefs( getClassStatement(), false, getDeclTypeVars() );
      gsClass.setGenericTypeVariables((List)typeVarLiteralList);

      if( gsClass.isEnum() )
      {
        verify( getClassStatement(), typeVarLiteralList.isEmpty(), Res.MSG_ENUM_MAY_NOT_HAVE_TYPEPARAM );
      }
      iOffset = getTokenizer().getTokenStart();
      iLineNum = getTokenizer().getLineNumber();
      iColumn = getTokenizer().getTokenColumn();

      if( !bInterface && (match( null, Keyword.KW_extends ) || gsClass.isEnum()) )
      {
        IType superType = parseEnhancedOrImplementedType( gsClass, true, Collections.<IType>emptyList() );

        if( superType instanceof IGosuClassInternal )
        {
          if( bResolveTypes )
          {
            ((IGosuClassInternal)superType).compileDeclarationsIfNeeded();
          }
        }
        gsClass.setSuperType( superType );

        if( gsClass.getCompilationState().isCompilingDeclarations() &&
            gsClass.isGenericType() )
        {
          verify( getClassStatement(), !JavaTypes.THROWABLE().isAssignableFrom( superType ) , Res.MSG_INVALID_GENERIC_EXCEPTION );
        }

        SuperTypeClause extendsClause = new SuperTypeClause( superType );
        pushExpression( extendsClause );
        if( gsClass.isDeclarationsCompiled() )
        {
          verifySuperTypeVarVariance( getClassStatement(), superType );
        }
        setLocation( iOffset, iLineNum, iColumn );
        popExpression();

        iOffset = getTokenizer().getTokenStart();
        iLineNum = getTokenizer().getLineNumber();
        iColumn = getTokenizer().getTokenColumn();
      }

      boolean hasImplements = false;
      if( (bInterface && match( null, Keyword.KW_extends )) ||
          (hasImplements = match( null, Keyword.KW_implements )) )
      {
        if( verify( getClassStatement(), !bInterface || !hasImplements, Res.MSG_NO_IMPLEMENTS_ALLOWED ) )
        {
          verify( getClassStatement(), !bAnnotation, Res.MSG_NO_EXTENDS_ALLOWED );
        }

        List<IType> interfaces = new ArrayList<IType>();
        do
        {
          IType type = parseEnhancedOrImplementedType( gsClass, bInterface, interfaces );
          gsClass.addInterface( type );
          if( gsClass.isDeclarationsCompiled() )
          {
            verifySuperTypeVarVariance( getClassStatement(), type );
          }
          interfaces.add( type );
        } while( match( null, ',' ) );

        InterfacesClause interfacesClause = new InterfacesClause( gsClass, interfaces.toArray( new IType[interfaces.size()] ) );
        pushExpression( interfacesClause );
        setLocation( iOffset, iLineNum, iColumn );
        popExpression();
      }

      if( classType == ClassType.Class || classType == ClassType.Interface || classType == ClassType.Structure )
      {
        IGosuClassInternal gsObjectInterace = gosuObjectInterface;
        if( (!gsClass.isInterface() || !interfaceExtendsGosuObject( gsClass, gsObjectInterace )) && !gsClass.getName().startsWith( IGosuClass.PROXY_PREFIX ) )
        {
          gsClass.addInterface( gsObjectInterace );
        }
      }
      else if( classType == ClassType.Enum )
      {
        gsClass.addInterface(gosuObjectInterface);
      }
    }

    if( (isTopLevelClass( gsClass ) ||
         gsClass instanceof IGosuProgram ||
         // Anonymous classes can have inner classes
         gsClass.isAnonymous()) &&
        !gsClass.isHeaderCompiled() )
    {
      // Recursively *load* (no parsing) all nested inner types from the top-level class file

      int state = getTokenizer().mark();
      loadAllNestedInnerClasses( gsClass );
      getTokenizer().restoreToMark( state );
    }

    return strClassName;
  }

  private boolean interfaceExtendsGosuObject( IGosuClassInternal gsClass, IGosuClassInternal gsObjectInterace )
  {
    if( gsClass == gsObjectInterace )
    {
      return true;
    }
    for( IType iface: gsClass.getInterfaces() )
    {
      if( iface instanceof IGosuClass )
      {
        return true;
      }
    }
    return false;
  }

  private List<TypeVariableDefinitionImpl> getDeclTypeVars()
  {
    IGosuClass gsClass = getGosuClass();
    if( !gsClass.isDeclarationsCompiled() )
    {
      return Collections.emptyList();
    }

    IGenericTypeVariable[] typeVars = gsClass.getGenericTypeVariables();
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

  private void makeSyntheticClassDeclaration( String strClassName, boolean bProgram )
  {
    ClassDeclaration classDeclaration = new ClassDeclaration( strClassName );
    pushExpression( classDeclaration );
    SourceCodeTokenizer tokenizer = getOwner().getTokenizer();
    setLocation( bProgram ? 0 : tokenizer.getTokenStart(), tokenizer.getLineNumber(), bProgram ? 0 : tokenizer.getTokenColumn(), true, true );
    popExpression();
    getClassStatement().setClassDeclaration( classDeclaration );
  }

  private void parseInnerClassHeaders( IGosuClassInternal gsClass, boolean bResolveTypes )
  {
    Map<CharSequence, ? extends IGosuClass> innerClassesByName = gsClass.getKnownInnerClassesWithoutCompiling();
    if( innerClassesByName.isEmpty() )
    {
      return;
    }

    int state = getTokenizer().mark();
    int iLocationsSize = getLocationsList().size();
    try
    {
      for( CharSequence name : innerClassesByName.keySet() )
      {
        IGosuClassInternal innerClass = (IGosuClassInternal)innerClassesByName.get( name );
        if( !(innerClass instanceof IBlockClass) )
        {
          innerClass.createNewParseInfo();
          new GosuClassParser( getOwner(), innerClass ).parseHeader( innerClass, false, false, bResolveTypes );
        }
      }
    }
    finally
    {
      while( getLocationsList().size() > iLocationsSize )
      {
        getLocationsList().remove( getLocationsList().size()-1 );
      }
      getTokenizer().restoreToMark( state );
    }
  }

  private void loadAllNestedInnerClasses( IGosuClassInternal gsClass )
  {
    String[] strMemberKeyword = new String[1];

    if( !(gsClass instanceof IGosuProgram) )
    {
      advanceToClassBodyStart();
    }

    ModifierInfo modifiers;
    while( true )
    {
      int[] location = new int[3];
      int[] mark = new int[]{-1};
      modifiers = parseUntilMemberKeyword( strMemberKeyword, true, -1, location, mark );
      if( modifiers.getModifiers() == -1 )
      {
        if( getTokenizer().isEOF() )
        {
          break;
        }
        if( !isInnerClassesEmpty() ) // can be empty e.g., errors with unmatching braces
        {
          IGosuClassInternal innerClass = popInnerClass( getCurrentInnerClass() );
          innerClass.getSourceFileHandle().setEnd( location[0] );
        }
        else if( gsClass.isAnonymous() )
        {
          break;
        }
      }
      else
      {
        ClassType classType = getClassType( strMemberKeyword[0] );
        if( classType != null )
        {
          IGosuClassInternal innerClass = loadNextInnerClass( gsClass, classType );
          if( innerClass == null )
          {
            break;
          }
          innerClass.getSourceFileHandle().setOffset( location[0] );
          ((InnerClassFileSystemSourceFileHandle)innerClass.getSourceFileHandle()).setMark( mark[0] );
          pushInnerClass( innerClass );
        }
      }
    }
  }

  private ClassType getClassType( String strValue )
  {
    return
      Keyword.KW_class.toString().equals( strValue )
      ? ClassType.Class
      : Keyword.KW_interface.equals( strValue )
        ? ClassType.Interface
        : Keyword.KW_annotation.equals( strValue )
          ? ClassType.Annotation
          : Keyword.KW_structure.equals( strValue )
            ? ClassType.Structure
            : Keyword.KW_enum.toString().equals( strValue )
              ? ClassType.Enum
              : null;
  }

  private void advanceToClassBodyStart()
  {
    while( true )
    {
      if( match( null, '{' ) )
      {
        break;
      }
      if( match( null, SourceCodeTokenizer.TT_EOF ) )
      {
        break;
      }
      getTokenizer().nextToken();
    }
  }

  private void advanceToClassBodyEnd()
  {
    int iEnd = getGosuClass().getSourceFileHandle().getEnd();
    if( iEnd <= 0 )
    {
      //assert isTopLevelClass( getGosuClass() ) || isEvalClass() : "Inner class does not have an 'end' marker";
      return;
    }
    try
    {
      //## perf: this is very slow, maybe use a tokenizer mark instead
      getTokenizer().goToPosition( iEnd );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
    verify( getClassStatement(), match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF );
  }

  private IGosuClassInternal loadNextInnerClass( IGosuClassInternal gsClass, ClassType classType )
  {
    Token T = new Token();
    IGosuClassInternal enclosingGsClass = getGosuClass();
    if( verify( getClassStatement(), match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_CLASS_DEF ) )
    {
      enclosingGsClass = getCurrentInnerClass() != null ? getCurrentInnerClass() : enclosingGsClass;
      String strInnerClass = T._strValue;

      IGosuClassInternal innerGsClass;

      innerGsClass = (IGosuClassInternal)enclosingGsClass.getKnownInnerClassesWithoutCompiling().get( strInnerClass );
      if( innerGsClass != null )
      {
        // Duplicate inner class name
        getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_DUPLICATE_CLASS_FOUND, strInnerClass ) );
        strInnerClass = strInnerClass + "_duplicate_" + nextIndexOfErrantDuplicateInnerClass( enclosingGsClass, innerGsClass );
      }

      innerGsClass = (IGosuClassInternal)gsClass.getTypeLoader().makeNewClass(
        new InnerClassFileSystemSourceFileHandle( classType, enclosingGsClass.getName(), strInnerClass, gsClass.isTestClass() ) );
      innerGsClass.setEnclosingType( enclosingGsClass );
      innerGsClass.setNamespace( enclosingGsClass.getNamespace() );
      enclosingGsClass.addInnerClass( innerGsClass );

      advanceToClassBodyStart();

      return innerGsClass;
    }
    return null;
  }

  public int nextIndexOfErrantDuplicateInnerClass( IGosuClassInternal enclosingGsClass, IGosuClassInternal innerClass )
  {
    int iMax = -1;
    String strName = innerClass.getRelativeName() + "_duplicate_";
    while( true )
    {
      IType existingInnerClass = enclosingGsClass.getKnownInnerClassesWithoutCompiling().get( strName + ++iMax );
      if( existingInnerClass == null )
      {
        return iMax;
      }
    }
  }

  private IGosuClassInternal getGosuObjectInterface()
  {
    return IGosuClassInternal.Util.getGosuClassFrom( JavaTypes.IGOSU_OBJECT() );
  }

  private String parseEnhancementHeaderSuffix( IGosuEnhancementInternal gsClass )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getTokenColumn();
    Token t = new Token();
    verify( getClassStatement(), match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_CLASS_DEF );
    String strClassName = t._strValue;
    strClassName = GosuStringUtil.isEmpty(getOwner().getNamespace())
                   ? strClassName
                   : getOwner().getNamespace() + '.' + strClassName;

    ClassDeclaration classDeclaration = new ClassDeclaration( strClassName );
    pushExpression( classDeclaration );
    setLocation( iOffset, iLineNum, iColumn );
    popExpression();
    getClassStatement().setClassDeclaration( classDeclaration );

    if( gsClass.getEnclosingTypeReference() == null && !strClassName.equals( gsClass.getName() ) )
    {
      verify( getClassStatement(), false, Res.MSG_WRONG_CLASSNAME, strClassName, gsClass.getName() );
    }

    List<ITypeVariableDefinitionExpression> typeVarLiteralList = getOwner().parseTypeVariableDefs( getClassStatement(), false, getDeclTypeVars() );
    gsClass.setGenericTypeVariables((List)typeVarLiteralList);

    verify( getClassStatement(), match( null, ":", SourceCodeTokenizer.TT_OPERATOR ), Res.MSG_EXPECTING_COLON_ENHANCEMENT );
    IType enhancedType = parseEnhancedOrImplementedType( gsClass, true, Collections.<IType>emptyList() );
    if( !(enhancedType instanceof ErrorType ||
          enhancedType instanceof IEnhanceableType) )
    {
      verify( getClassStatement(), false, Res.MSG_NOT_AN_ENHANCEABLE_TYPE, enhancedType.getName() );
    }
    gsClass.setEnhancedType( enhancedType );

    ensureEnhancedTypeUsesTypeVarsOfEnhancement( typeVarLiteralList, enhancedType );

    return strClassName;
  }

  private void ensureEnhancedTypeUsesTypeVarsOfEnhancement( List<ITypeVariableDefinitionExpression> typeVarLiteralList, IType enhancedType )
  {
    if( typeVarLiteralList.isEmpty() )
    {
      return;
    }


    for( ITypeVariableDefinitionExpression expr: typeVarLiteralList )
    {
      boolean bReferencedByOtherTypeVar = false;
      for( ITypeVariableDefinitionExpression expr2: typeVarLiteralList )
      {
        if( expr2 != expr )
        {
          if( hasTypeVar( expr2.getTypeVarDef().getBoundingType(), expr.getTypeVarDef().getType() ) )
          {
            bReferencedByOtherTypeVar = true;
            break;
          }
        }
      }
      verify( getClassStatement(), bReferencedByOtherTypeVar || hasTypeVar( enhancedType, expr.getTypeVarDef().getType() ), Res.MSG_ENHANCED_TYPE_MUST_USE_ENHANCEMENT_TYPEVARS );
    }
  }

  private boolean hasTypeVar( IType type, ITypeVariableType typeVar )
  {
    if( type == null )
    {
      return false;
    }

    if( type.isArray() )
    {
      type = TypeLord.getCoreType( type );
    }

    if( type == null || type.equals( typeVar ) )
    {
      return true;
    }

    if( type.isParameterizedType() )
    {
      for( IType typeParam: type.getTypeParameters() )
      {
        if( hasTypeVar( typeParam, typeVar  ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  void parseProgramExtendsStatement( IGosuClassInternal gsClass, boolean bResolveTypes )
  {
    int iOffset = getTokenizer().getTokenStart();
    int iLineNum = getTokenizer().getLineNumber();
    int iColumn = getTokenizer().getLineOffset();
    if( match( new Token(), Keyword.KW_extends ) )
    {
      IType superType = parseEnhancedOrImplementedType( gsClass, true, Collections.<IType>emptyList() );
      SuperTypeClause stmt = new SuperTypeClause( superType );
      if( superType instanceof IGosuClassInternal )
      {
        if( bResolveTypes )
        {
          ((IGosuClassInternal)superType).compileDeclarationsIfNeeded();
        }
      }
      pushExpression( stmt );
      try
      {
        setLocation( iOffset, iLineNum, iColumn );
      }
      finally
      {
        popExpression();
      }
      ITypeInfo typeInfo = superType.getTypeInfo();
      if( !(superType instanceof IErrorType) && typeInfo instanceof IRelativeTypeInfo )
      {
        IConstructorInfo noArgCtor = ((IRelativeTypeInfo)typeInfo).getConstructor( gsClass, null );
        if( verify( stmt, noArgCtor != null, Res.MSG_NO_DEFAULT_CTOR_IN, superType.getName() ) )
        {
          gsClass.setSuperType( superType );
        }
      }
    }
  }

  private IType parseEnhancedOrImplementedType( IGosuClassInternal gsClass, boolean bExtended, List<IType> interfaces )
  {
    IType extendedType = null;

    TypeLiteral extendedTypeExpr = null;
    if( gsClass instanceof IGosuProgram && !bExtended )
    {
      extendedType = gsClass.getSupertype() != null ? gsClass.getSupertype() : JavaTypes.OBJECT();
    }
    else if( !gsClass.isEnum() || !bExtended )
    {
      getOwner().parseTypeLiteral( !(gsClass instanceof IGosuEnhancementInternal) && (gsClass.isInterface() || !bExtended) );
      extendedTypeExpr = (TypeLiteral)popExpression();
      extendedType = extendedTypeExpr.getType().getType();
      if( !verify( extendedTypeExpr, !extendedType.isCompoundType(), Res.MSG_COMPOUND_TYPE_NOT_ALLOWED_HERE ) )
      {
        extendedType = ErrorType.getInstance();
      }
      if( !(extendedType instanceof ErrorType) )
      {
        if( !(gsClass instanceof IGosuEnhancementInternal) )
        {
          if( gsClass.isInterface() )
          {
            verify( extendedTypeExpr, extendedType.isInterface(), Res.MSG_INTERFACE_CANNOT_EXTEND_CLASS );
          }
          else if( bExtended )
          {
            if( verify( extendedTypeExpr, !extendedType.isInterface(), Res.MSG_CLASS_CANNOT_EXTEND_INTERFACE ) )
            {
              verify( extendedTypeExpr, !gsClass.isEnum(), Res.MSG_ENUM_CANNOT_EXTEND_CLASS );
              verify( extendedTypeExpr, extendedType != JavaTypes.OBJECT(), Res.MSG_SUBCLASS_OBJECT, gsClass.getRelativeName() );
              verify( extendedTypeExpr, !extendedType.isArray(), Res.MSG_CANNOT_EXTEND_ARRAY, extendedType.getRelativeName() );
            }
          }
          else
          {
            verify( extendedTypeExpr, extendedType.isInterface(), Res.MSG_CLASS_CANNOT_IMPLEMENT_CLASS );
          }
          verify( extendedTypeExpr, !extendedType.isPrimitive(), Res.MSG_CANNOT_EXTEND_PRIMITIVE_TYPE );
          verify( extendedTypeExpr, !extendedType.isFinal(), Res.MSG_CANNOT_EXTEND_FINAL_TYPE, extendedType.getName() );
          if( verify( extendedTypeExpr, !interfaces.contains( extendedType ), Res.MSG_DUPLICATE_CLASS_FOUND, extendedType.getRelativeName() ) )
          {
            IType[] conflict = inheritsWithDifferentTypeParams( gsClass.getSupertype(), interfaces, extendedType );
            if( conflict != null )
            {
              extendedTypeExpr.addParseException( Res.MSG_INHEREITED_WITH_DIFF_ARG_TYPES, TypeLord.getPureGenericType( conflict[0] ).getName(), Arrays.toString( conflict[0].getTypeParameters() ) + " , " + Arrays.toString( conflict[1].getTypeParameters() ) );
            }
          }
          if( isCyclicInheritance( extendedType, gsClass ) )
          {
            extendedType = ErrorType.getInstance( extendedType.getName() );
            verify( extendedTypeExpr, false, Res.MSG_CYCLIC_INHERITANCE, extendedType.getName() );
          }
        }
        else
        {
          if( extendedType instanceof IGosuEnhancementInternal )
          {
            verify( extendedTypeExpr, false, Res.MSG_ENHANCEMENTS_CANNOT_ENHANCE_OTHER_ENHANCEMENTS, extendedType.getName() );
          }
        }
      }
    }
    else if( gsClass.isEnum() )
    {
      extendedType = JavaTypes.ENUM();
      extendedType = extendedType.getParameterizedType( gsClass );
    }

    makeProxy( gsClass, extendedType );

    extendedType = TypeLord.makeDefaultParameterizedType( extendedType );

    if( !verify( extendedTypeExpr == null ? getClassStatement() : extendedTypeExpr,
                 (!extendedType.isGenericType() || extendedType instanceof IGosuClass && !((IGosuClass) extendedType).isHeaderCompiled()) ||
                 extendedType.isParameterizedType() || gsClass instanceof IGosuEnhancementInternal,
                 Res.MSG_CANNOT_EXTEND_RAW_GENERIC_TYPE, extendedType.getName() ) )
    {
      // If we are unable to resolve a parameterized type, extend the error type
      extendedType = ErrorType.getInstance();
    }

    if( bExtended && !(gsClass instanceof IGosuEnhancementInternal) )
    {
      verify( extendedTypeExpr == null ? getClassStatement() : extendedTypeExpr,
              Modifier.isStatic( extendedType.getModifiers() ) || extendedType.getEnclosingType() == null ||
              TypeLord.enclosingTypeInstanceInScope( extendedType.getEnclosingType(), getGosuClass() ),
              Res.MSG_NO_ENCLOSING_INSTANCE_IN_SCOPE, extendedType.getEnclosingType() );
    }

    if( !(extendedType instanceof ErrorType) )
    {
      if( gsClass.isDeclarationsCompiled() )
      {
        verifySuperTypeVarVariance( getClassStatement(), extendedType );
      }
    }

    return extendedType;
  }

  private IType[] inheritsWithDifferentTypeParams( IType superType, List<IType> interfaces, IType iface )
  {
    if( superType != null )
    {
      IType[] conflict = inheritsWithDifferentTypeParams( null, Arrays.asList( superType.getInterfaces() ), iface );
      if( conflict != null )
      {
        return conflict;
      }
    }

    IType rawIface = TypeLord.getPureGenericType( iface );
    for( IType csr: interfaces )
    {
      if( TypeLord.getPureGenericType( csr ) == rawIface && csr != iface )
      {
        return new IType[] {csr, iface};
      }

      IType[] conflict = inheritsWithDifferentTypeParams( null, Arrays.asList( csr.getInterfaces() ), iface );
      if( conflict != null )
      {
        return conflict;
      }

      conflict = inheritsWithDifferentTypeParams( null, Arrays.asList( iface.getInterfaces() ), csr );
      if( conflict != null )
      {
        return conflict;
      }
    }
    return null;
  }

  private void makeProxy( IGosuClassInternal gsClass, IType extendedType )
  {
    if( !(gsClass instanceof IGosuEnhancementInternal) && extendedType instanceof IJavaType )
    {
      // Create a gosu class proxy for the java one.
      // It is attached to the JavaType as its adapterClass.
      GosuClassProxyFactory.instance().create( extendedType );
    }
  }

  private Object parseFunctionOrConstructorOrFieldDeclaration( IGosuClassInternal gsClass )
  {
    int[] location = new int[3];
    Object rtn = _parseFunctionOrConstructorOrFieldDeclaration( gsClass, location );
    if( rtn != null )
    {
      setLocation( location[0], location[1], location[2] );
    }
    return rtn;
  }

  //------------------------------------------------------------------------------
  //
  // class-member-declaration
  //   <constructor-declaration>
  //   <function-declaration>
  //   <field-declaration>
  //
  // constructor-declaration
  //   [modifiers] function <class-name> ( [ <argument-declaration-list> ] )
  //
  // function-declaration
  //   [modifiers] function <identifier> ( [ <argument-declaration-list> ] ) [: <type-literal>]
  //
  // field-declaration
  //   [modifiers] var <identifier> [ : <type-literal> ] = <expression>
  //   [modifiers] var <identifier> : <type-literal> [ = <expression> ]
  //

  private Object _parseFunctionOrConstructorOrFieldDeclaration( IGosuClassInternal gsClass, int[] location )
  {
    String[] T = new String[1];

    ModifierInfo modifiers;
    boolean bInterface = gsClass.isInterface();
    while( true )
    {
      modifiers = parseUntilMemberKeyword( T, false, -1, location );
      if( modifiers.getModifiers() == -1 )
      {
        return null;
      }
      if( Keyword.KW_class.equals( T[0] ) ||
          Keyword.KW_interface.equals( T[0] ) ||
          Keyword.KW_annotation.equals( T[0] ) ||
          Keyword.KW_structure.equals( T[0] ) ||
          Keyword.KW_enum.equals( T[0] ) )
      {
        if( bInterface && Keyword.KW_enum.equals( T[0] ))
        {
          verify( getClassStatement(), !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_enum );
        }
        parseInnerClassDeclaration();
      }
      else
      {
        break;
      }
    }

    int mod = modifiers.getModifiers();
    if( bInterface )
    {
      modifiers.addModifiers( Modifier.PUBLIC );
    }

    if( T[0] != null &&
        (Keyword.KW_function.equals( T[0] ) ||
         Keyword.KW_construct.equals( T[0] )) )
    {
      String ctorNameToken = null;
      boolean bConstructKeyword = false;
      if( Keyword.KW_construct.equals( T[0] ) )
      {
        T[0] = gsClass.getRelativeName();
        ctorNameToken = T[0];
        bConstructKeyword = true;
      }
      else
      {
        int mark = getTokenizer().mark();
        if( match( null, null, SourceCodeTokenizer.TT_WORD, true ) )
        {
          T[0] = getTokenizer().getTokenAt( mark ).getStringValue();
        }
      }
      FunctionStatement fs = makeFunctionOrConstructorStatement( gsClass, T[0], bConstructKeyword );

      IParserState constructOrFunctionState = makeLazyLightweightParserState();

      verify( fs, !(gsClass instanceof IGosuProgramInternal) || !((IGosuProgramInternal)gsClass).isStatementsOnly(),
              Res.MSG_FUNCTIONS_NOT_ALLOWED_IN_THIS_CONTEXT );

      DynamicFunctionSymbol dfs = getOwner().parseFunctionDecl( fs, ctorNameToken, false, false, modifiers );
      fs.setDynamicFunctionSymbol( dfs );
      pushStatement( fs );

      if( bInterface && !match( null, null, '{', true ) ) {
        modifiers.addModifiers( Modifier.ABSTRACT );
        dfs.setAbstract( true );
      }

      if( dfs != null )
      {
        dfs.setClassMember( true );
        boolean bConstructor = dfs.getDisplayName().equals( gsClass.getRelativeName() );
        if( bConstructor )
        {
          verify( fs, !Modifier.isAbstract(modifiers.getModifiers()), Res.MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE );
          verify( fs, !gsClass.isInterface(), Res.MSG_NOT_ALLOWED_IN_INTERFACE );
          verify( fs, !(gsClass instanceof IGosuProgramInternal), Res.MSG_CONSTRUCTORS_NOT_ALLOWD_IN_THIS_CONTEXT );
          verify( fs, !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, Keyword.KW_construct );
          verify( fs, !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_construct );
          verify( fs, !Modifier.isStatic( modifiers.getModifiers() ), Res.MSG_NO_STATIC_CONSTRUCTOR );
          verify( fs, !Modifier.isTransient( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, Keyword.KW_construct );
          if( !bConstructKeyword )
          {
            fs.addParseWarning( new ObsoleteConstructorWarning( constructOrFunctionState, Res.MSG_OBSOLETE_CTOR_SYNTAX ) );
          }
        }
        else
        {
          verifyNoCombinedFinalStaticModifierDefined( fs, false, modifiers.getModifiers() );
          verify(fs, !Modifier.isAbstract(modifiers.getModifiers()) || !Modifier.isStatic(modifiers.getModifiers()), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_static, Keyword.KW_abstract);
          verify(fs, !Modifier.isAbstract(modifiers.getModifiers()) || !Modifier.isFinal(modifiers.getModifiers()), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_abstract);
          verify( fs, !Modifier.isTransient(modifiers.getModifiers()), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_transient, Keyword.KW_function );
        }
      }

      eatOptionalSemiColon( bInterface );
      if( !Modifier.isNative( modifiers.getModifiers() ) && !Modifier.isAbstract( modifiers.getModifiers() ) )
      {
        eatStatementBlock( fs, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
      }
      return dfs;
    }
    else if( T[0] != null && T[0].equals( Keyword.KW_property.toString() ) )
    {
      boolean bGetter = match( null, Keyword.KW_get );
      verify( getClassStatement(), bGetter || match( null, Keyword.KW_set ), Res.MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER );

      FunctionStatement fs = new FunctionStatement();
      verifyNoCombinedFinalStaticModifierDefined( fs, false, modifiers.getModifiers() );

      verify( fs, !(gsClass instanceof IGosuProgramInternal) || !((IGosuProgramInternal)gsClass).isStatementsOnly(),
              Res.MSG_FUNCTIONS_NOT_ALLOWED_IN_THIS_CONTEXT );

      DynamicFunctionSymbol dfs = getOwner().parseFunctionDecl( fs, true, bGetter, modifiers );
      if( dfs == null )
      {
        getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_EXPECTING_DECL ) );
        return null;
      }

      if( dfs.getDisplayName().length() > 0 &&
          dfs.getDisplayName().charAt(0) == '@' )
      {
        String name = dfs.getDisplayName().substring(1);
        boolean bOuterLocalDefined = findLocalInOuters( name ) instanceof CapturedSymbol;
        verifyOrWarn( fs, !bOuterLocalDefined, false, Res.MSG_VARIABLE_ALREADY_DEFINED, name );
      }

      if( bInterface && !match( null, null, '{', true ) )
      {
        verify( fs, !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_property );
        modifiers.setModifiers( Modifier.setAbstract( modifiers.getModifiers(), true ) );
        dfs.setAbstract( true );
      }
      verify( fs, !Modifier.isAbstract( modifiers.getModifiers() ) || !Modifier.isStatic( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_static, Keyword.KW_abstract );
      verify( fs, !Modifier.isAbstract( modifiers.getModifiers() ) || !Modifier.isFinal( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_final, Keyword.KW_abstract );
      verify( fs, !Modifier.isAbstract( modifiers.getModifiers() ) || gsClass.isAbstract(), Res.MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS );

      fs.setDynamicFunctionSymbol( dfs );
      pushStatement( fs );
      setLocation( location[0], location[1], location[2] );
      popStatement();

      dfs.setClassMember( true );
      eatOptionalSemiColon( bInterface );
      if( !bInterface &&
          !Modifier.isNative( modifiers.getModifiers() ) && !Modifier.isAbstract( modifiers.getModifiers() ) )
      {
        eatStatementBlock( fs, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
      }
      DynamicPropertySymbol dps = getOrCreateDynamicPropertySymbol( getClassStatement(), gsClass, dfs, bGetter );
      PropertyStatement statement = new PropertyStatement( fs, dps );
      verifyPropertiesAreSymmetric( bGetter, dfs, dps, statement );
      pushStatement( statement );

      return dps;
    }
    else if( T[0] != null && T[0].equals( Keyword.KW_var.toString() ) )
    {
      if( bInterface )
      {
        modifiers.setModifiers( Modifier.setStatic( modifiers.getModifiers(), true ) );
        modifiers.setModifiers( Modifier.setFinal( modifiers.getModifiers(), true ) );
      }
      return parseFieldDecl( modifiers );
    }
    else if( T[0] != null && T[0].equals( Keyword.KW_delegate.toString() ) )
    {
      return parseDelegateDecl( modifiers, gsClass );
    }
    else
    {
      getClassStatement().addParseException( new ParseException( makeFullParserState(), Res.MSG_EXPECTING_DECL ) );
      return null;
    }
  }

  private void verifySuperTypeVarVariance( ClassStatement classStatement, IType type )
  {
    if( !type.isParameterizedType() || !getGosuClass().isGenericType() )
    {
      return;
    }

    IGenericTypeVariable[] gtvs = type.getGenericType().getGenericTypeVariables();
    IType[] typeParameters = type.getTypeParameters();
    for( int i = 0; i < typeParameters.length; i++ )
    {
      if( gtvs[i] != null && gtvs[i].getTypeVariableDefinition() != null )
      {
        Variance variance = Variance.maybeInferVariance( type, gtvs[i] );
        verifyTypeVarVariance( variance, classStatement, typeParameters[i] );
      }
    }
  }

  private void verifyTypeVarVariance( Variance ctxVariance, ParsedElement elem, IType type )
  {
    if( !getGosuClass().isGenericType() )
    {
      return;
    }

    Variance.verifyTypeVarVariance( ctxVariance,
      getGosuClass(),
      ( Variance ctxV, Variance typeVarV ) -> {
        verify( elem, typeVarV == ctxV || typeVarV == Variance.DEFAULT || typeVarV == Variance.INVARIANT || ctxV == Variance.PENDING || typeVarV == Variance.PENDING,
                Res.MSG_TYPE_VAR_VARIANCE_ERROR, type.getRelativeName(), typeVarV == null ? "null" : typeVarV.getDesc(), ctxV.getDesc(), type.getRelativeName() );
      },
      type );
  }

  private void verifyPropertiesAreSymmetric( boolean bGetter,
                                             DynamicFunctionSymbol newFunction,
                                             DynamicPropertySymbol propertySymbol,
                                             Statement stmt )
  {
    DynamicFunctionSymbol getter;
    DynamicFunctionSymbol setter;
    if( bGetter )
    {
      getter = newFunction;
      setter = propertySymbol == null ? null : propertySymbol.getSetterDfs();
    }
    else
    {
      getter = propertySymbol == null ? null : propertySymbol.getGetterDfs();
      setter = newFunction;
    }

    if( getter != null && setter != null )
    {
      if( getter.isStatic() != setter.isStatic() )
      {
        verify( stmt, false, Res.MSG_PROPERTIES_MUST_AGREE_ON_STATIC_MODIFIERS );
      }
      if( setter.getArgs().size() == 1 )
      {
        IType setterType = setter.getArgTypes()[0];
        IType returnType = getter.getReturnType();
        if( !setterType.isAssignableFrom( returnType ) ||
            !setterType.isAssignableFrom( propertySymbol.getType() ) )
        {
          verify( stmt, false, Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE );
        }
      }
    }
    else if( getter != null && propertySymbol != null && newFunction != null &&
             getGosuClass() == propertySymbol.getScriptPart().getContainingType() &&
             getter.getSuperDfs() == null )
    {
      verify( stmt, propertySymbol.getType().equals( newFunction.getReturnType() ), Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE );
    }
  }

  private FunctionStatement makeFunctionOrConstructorStatement( IGosuClassInternal gsClass, String strMemberKeyword, boolean bConstructKeyword )
  {
    FunctionStatement fs;
    if( gsClass != null &&
        (bConstructKeyword || gsClass.getRelativeName().equals( strMemberKeyword )) )
    {
      fs = new ConstructorStatement( bConstructKeyword );
    }
    else
    {
      fs = new FunctionStatement();
    }
    return fs;
  }


  private ModifierInfo parseUntilMemberKeyword( String[] T, boolean bIgnoreErrors, int[] location )
  {
    return parseUntilMemberKeyword( T, bIgnoreErrors, -1, location );
  }
  private ModifierInfo parseUntilMemberKeyword( String[] T, boolean bIgnoreErrors, int iEnd, int[] location )
  {
    return parseUntilMemberKeyword( T, bIgnoreErrors, iEnd, location, null );
  }
  private ModifierInfo parseUntilMemberKeyword( String[] T, boolean bIgnoreErrors, int iEnd, int[] location, int[] mark )
  {
    boolean bPeek = T == null;
    while( true )
    {
      if( location != null )
      {
        location[0] = getTokenizer().getTokenStart();
        location[1] = getTokenizer().getLineNumber();
        location[2] = getTokenizer().getTokenColumn();
      }
      if( mark != null )
      {
        mark[0] = getTokenizer().mark();
      }
      ModifierInfo modifiers = parseModifiers( bIgnoreErrors );
      if( matchDeclarationKeyword( T, bPeek, getTokenizer() ) )
      {
        return modifiers;
      }
      popModifierList();
      boolean bAte = false;
      if( getGosuClass() instanceof IGosuProgram )
      {
        bAte = eatPossibleEnclosedVarInStmt(); // e.g., for( var foo in foos ) {...}   we don't want the var foo to be consumed as a field (applies to GosuPrograms).
      }
      bAte = eatPossibleStatementBlock() || bAte;
      if( location != null )
      {
        // Mark possible end location of member definition
        location[0] = getTokenizer().getTokenStart();
      }
      if( match( null, SourceCodeTokenizer.TT_EOF ) ||
          ((!(getGosuClass() instanceof IGosuProgram) || !getGosuClass().isHeaderCompiled()) && match( null, '}' )) ||
          (iEnd >= 0 && getTokenizer().getTokenStart() >= iEnd) )
      {
        modifiers.setModifiers( -1 );
        return modifiers;
      }
      if( !bAte )
      {
        getTokenizer().nextToken();
        if( getTokenizer().isEOF() )
        {
          modifiers.setModifiers( -1 );
          return modifiers;
        }
      }
    }
  }

  private void popModifierList()
  {
    ParseTree parseTree = getOwner().peekLocation();
    if( parseTree == null )
    {
      return;
    }

    ParsedElement pe = parseTree.getParsedElement();
    if( pe instanceof IModifierListClause )
    {
      List<ParseTree> locationsList = getLocationsList();
      locationsList.remove( locationsList.size()-1 );
    }
  }

  private void parseInnerClassDeclaration()
  {
    IGosuClassInternal enclosingGsClass = getClassStatement().getGosuClass();

    int mark = getTokenizer().mark();
    String strInnerClass = null;
    if( verify( getClassStatement(), match( null, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_CLASS_DEF ) )
    {
      strInnerClass = getTokenizer().getTokenAt( mark ).getStringValue();
    }
    if( strInnerClass != null )
    {
      String name = enclosingGsClass.getName();
      String dotInner = "." + strInnerClass;
      verify( getClassStatement(), !name.equals(strInnerClass) &&
                                   !name.contains(dotInner + ".") &&
                                   !name.endsWith(dotInner) &&
                                   !name.startsWith(strInnerClass + "."), Res.MSG_DUPLICATE_CLASS_FOUND, name + dotInner );
      for( IGosuClass c : enclosingGsClass.getKnownInnerClassesWithoutCompiling().values() )
      {
        IGosuClassInternal innerClass = (IGosuClassInternal)c;
        if( innerClass.getRelativeName().equals( strInnerClass ) )
        {
          int i = 0;
          String relativeName = innerClass.getName();
          while( innerClass.isDeclarationsCompiled() || innerClass.isDeclarationsBypassed() )
          {
            // The inner class is already declaration-compiled, maybe this is a duplicate inner class...

            String duplicate = relativeName + "_duplicate_" + i++;
            innerClass = (IGosuClassInternal)TypeSystem.getByFullNameIfValid( duplicate );
            if( innerClass == null )
            {
              return;
            }
          }

          parseInnerClassDeclaration( innerClass );
          break;
        }
      }
    }
  }

  private void parseInnerClassDeclaration( IGosuClassInternal innerClass ) {
    // Preserve dfs decls map of outer class
    Map<String, List<IFunctionSymbol>> restoreDfsDecls = copyDFSDecls( getOwner() );
    try {
      new GosuClassParser( getOwner(), innerClass ).parseDeclarations( innerClass );
      if( innerClass.isInterface() )
      {
        ModifierInfo mi = (ModifierInfo)innerClass.getModifierInfo();
        mi.setModifiers( Modifier.setStatic( mi.getModifiers(), true ));
      }
    }
    finally {
      getOwner().setDfsDeclInSetByName( restoreDfsDecls );
    }
  }

  private static Map<String, List<IFunctionSymbol>> copyDFSDecls( GosuParser owner )
  {
    Map<String, List<IFunctionSymbol>> hashMap = new HashMap<>( owner.getDfsDecls() );
    for( String name : hashMap.keySet() )
    {
      hashMap.put( name, new ArrayList<>( hashMap.get( name ) ) );
    }
    return hashMap;
  }

  private VarStatement parseFieldDecl( ModifierInfo modifiers )
  {
    Token T = new Token();

    VarStatement varStmt = new VarStatement();

    verify( varStmt, !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE );
    verify( varStmt, !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, Keyword.KW_var );

    final int iNameStart = getTokenizer().getTokenStart();
    if( !verify( varStmt, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_VAR ) )
    {
      T._strValue = null;
    }
    String strIdentifier = T._strValue == null ? "" : T._strValue;
    boolean bAlreadyDefined = getSymbolTable().getSymbol( strIdentifier ) != null;
    verify( varStmt, !bAlreadyDefined, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );
    checkForEnumConflict( varStmt, strIdentifier );

    boolean bStatic = Modifier.isStatic( modifiers.getModifiers() );

    TypeLiteral typeLiteral = null;
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      getOwner().parseTypeLiteral();
      typeLiteral = (TypeLiteral)popExpression();
    }
    else if( !match( null, "=", SourceCodeTokenizer.TT_OPERATOR, true ) )
    {
      if( match( null, null, '(', true ) )
      {
        getOwner().parseBlockLiteral();
        typeLiteral = (TypeLiteral)popExpression();
      }
    }

    IType type;

    if( typeLiteral != null )
    {
      type = typeLiteral.getType().getType();
      varStmt.setTypeLiteral( typeLiteral );
    }
    else
    {
      type = GosuParserTypes.NULL_TYPE();
    }

    if( bStatic )
    {
      modifiers.setModifiers( Modifier.setStatic( modifiers.getModifiers(), true ) );
    }

    varStmt.setModifierInfo( modifiers );

    if( !verify( varStmt, varStmt.isPrivate() || type != GosuParserTypes.NULL_TYPE(), Res.MSG_NON_PRIVATE_MEMBERS_MUST_DECLARE_TYPE ) )
    {
      type = ErrorType.getInstance();
    }

    DynamicPropertySymbol dpsVarProperty = getOwner().parseVarPropertyClause( varStmt, strIdentifier, type, false );
    if( dpsVarProperty != null )
    {
      String propertyName = dpsVarProperty.getName();
      ISymbol existingSym = getSymbolTable().getSymbol(propertyName);
      boolean bOuterLocalDefined = findLocalInOuters( propertyName ) instanceof CapturedSymbol;
      bAlreadyDefined = existingSym != null || bOuterLocalDefined || propertyName.equals( strIdentifier );
      verify( varStmt, !bAlreadyDefined || existingSym instanceof DynamicPropertySymbol, Res.MSG_VARIABLE_ALREADY_DEFINED, propertyName );
      getSymbolTable().putSymbol( dpsVarProperty );

      verifyPropertiesAreSymmetric( true, dpsVarProperty.getGetterDfs(), dpsVarProperty, varStmt );
      setStatic( bStatic, dpsVarProperty );

      dpsVarProperty.addMemberSymbols( getGosuClass() );
    }

    AbstractDynamicSymbol symbol = new DynamicSymbol( getGosuClass(), getSymbolTable(), strIdentifier, type, null );
    modifiers.addAll( symbol.getModifierInfo() );
    if( varStmt.isPrivate() )
    {
      // Ensure private bit is explicit
      modifiers.setModifiers( Modifier.setPrivate( modifiers.getModifiers(), true ) );
    }
    symbol.setModifierInfo( modifiers );
    varStmt.setSymbol( symbol );
    varStmt.setNameOffset( iNameStart, T._strValue );

    if( bAlreadyDefined )
    {
      int iDupIndex = getOwner().nextIndexOfErrantDuplicateDynamicSymbol( symbol, getSymbolTable().getSymbols().values(), false );
      if( iDupIndex >= 0 )
      {
        symbol.renameAsErrantDuplicate( iDupIndex );
      }
    }

    getSymbolTable().putSymbol( symbol );

    pushStatement( varStmt );

    return varStmt;
  }

  private void checkForEnumConflict( VarStatement varStmt, String identifier )
  {
    if( getGosuClass().isEnum() )
    {
      ISymbol existingProp = getGosuClass().getMemberProperty( identifier );
      verify( varStmt, !(existingProp instanceof DynamicPropertySymbol), Res.MSG_VARIABLE_ALREADY_DEFINED, identifier );
    }
  }

  private VarStatement parseDelegateDecl( ModifierInfo modifiers, IGosuClassInternal gsClass )
  {
    Token T = new Token();

    DelegateStatement delegateStmt = new DelegateStatement();
    verify( delegateStmt, gsClass == null || (!gsClass.isInterface() && !gsClass.isEnum()), Res.MSG_DELEGATION_NOT_ALLOWED_HERE );
    verify( delegateStmt, !Modifier.isStatic( modifiers.getModifiers() ), Res.MSG_DELEGATES_CANNOT_BE_STATIC );
    verify( delegateStmt, !Modifier.isAbstract( modifiers.getModifiers() ), Res.MSG_MODIFIER_ABSTRACT_NOT_ALLOWED_HERE );
    verify( delegateStmt, !Modifier.isOverride( modifiers.getModifiers() ), Res.MSG_ILLEGAL_USE_OF_MODIFIER, Keyword.KW_override, Keyword.KW_var );
    int iNameOffset = getTokenizer().getTokenStart();
    if( verify( delegateStmt, match( T, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_IDENTIFIER_VAR ) )
    {
      delegateStmt.setNameOffset( iNameOffset, null );
    }

    String strIdentifier = T._strValue == null ? "" : T._strValue;
    verify( delegateStmt, getSymbolTable().getSymbol( strIdentifier ) == null, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );

    TypeLiteral typeLiteral = null;
    if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
    {
      getOwner().parseTypeLiteral();
      typeLiteral = (TypeLiteral)popExpression();
    }
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
    IType type;

    if( typeLiteral != null )
    {
      type = typeLiteral.getType().getType();
      delegateStmt.setTypeLiteral( typeLiteral );
    }
    else
    {
      type = GosuParserTypes.NULL_TYPE();
    }

    delegateStmt.setModifierInfo( modifiers );

    verify( delegateStmt, delegateStmt.isPrivate() || type != GosuParserTypes.NULL_TYPE(), Res.MSG_NON_PRIVATE_MEMBERS_MUST_DECLARE_TYPE );

    AbstractDynamicSymbol symbol = new DynamicSymbol( getGosuClass(), getSymbolTable(), strIdentifier, type, null );
    modifiers.addAll( symbol.getModifierInfo() );
    symbol.setModifierInfo( modifiers );
    delegateStmt.setSymbol( symbol );
    getSymbolTable().putSymbol( symbol );

    pushStatement( delegateStmt );

    return delegateStmt;
  }

  private void setStatic( boolean bStatic, DynamicPropertySymbol dpsVarProperty )
  {
    dpsVarProperty.setStatic( bStatic );
    if( dpsVarProperty.getSetterDfs() != null )
    {
      dpsVarProperty.getSetterDfs().setStatic( bStatic );
    }
    if( dpsVarProperty.getGetterDfs() != null )
    {
      dpsVarProperty.getGetterDfs().setStatic( bStatic );
    }
  }

  //------------------------------------------------------------------------------
  //
  // class-statement
  //   [modifiers] class <identifier> [extends <base-class>] [implements <interfaces-list>] { <class-members> }
  //

  boolean parseClassStatement()
  {
    IGosuClassInternal gsClass = getGosuClass();
    ensureAbstractMethodsImpledAndNoDiamonds( gsClass );
    ensureInheritedMethodsDoNotClash( gsClass );

    //## todo: remove this scope?
    getSymbolTable().pushScope();
    try
    {
      verify( getClassStatement(), gsClass instanceof IGosuProgram || match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_CLASS_DEF );
      Statement currentStmt = (isTopLevelClass( gsClass ) || TypeLord.isEvalProgram( gsClass )) ? null : peekStatement();
      parseClassMembers( gsClass );
      for( Statement stmt = peekStatement(); stmt != currentStmt; stmt = peekStatement() )
      {
        stmt = popStatement();
        if( stmt instanceof VarStatement ||
            stmt instanceof FunctionStatement ||
            stmt instanceof PropertyStatement ||
            stmt instanceof NoOpStatement ||
            stmt instanceof NamespaceStatement ||
            stmt instanceof UsesStatement ||
            stmt instanceof ClassStatement )
        {
          // ignore
        }
        else
        {
          throw new IllegalStateException( "Expecting only statements for: package, uses, var, function, or property." );
        }
      }

      verify( getClassStatement(), match( null, '}' ) || gsClass instanceof IGosuProgram, Res.MSG_EXPECTING_CLOSE_BRACE_FOR_CLASS_DEF );
    }
    finally
    {
      getSymbolTable().popScope();
    }

    return true;
  }

  private void ensureInheritedMethodsDoNotClash( IGosuClassInternal gsClass )
  {
    if( !inheritsFromTwoOrMoreTypes( gsClass ) )
    {
      return;
    }
    MethodList methods = gsClass.getTypeInfo().getMethods( gsClass );
    for( DynamicArray<IMethodInfo> bucket: methods.getMethodBuckets() )
    {
      if( bucket.size() > 1 )
      {
        Map<String, IReducedDynamicFunctionSymbol> functionTypes = new HashMap<>();
        for( IMethodInfo mi : bucket )
        {
          if( mi instanceof IGosuMethodInfo )
          {
            IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo)mi).getDfs();
            IReducedDynamicFunctionSymbol originalDfs = dfs;
            while( true )
            {
              IReducedDynamicFunctionSymbol superDfs = dfs.getSuperDfs();
              if( superDfs != null && superDfs != dfs )
              {
                dfs = superDfs;
              }
              else
              {
                IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
                if( backingDfs != null && backingDfs != dfs )
                {
                  dfs = backingDfs;
                }
                else
                {
                  break;
                }
              }
            }
            if( dfs != originalDfs && !(dfs.getGosuClass() instanceof IGosuEnhancement) )
            {
              FunctionType ft = ((FunctionType)dfs.getType()).getRuntimeType();
              String paramSignature = ft.getParamSignature();
              IReducedDynamicFunctionSymbol existingDfs = functionTypes.get( paramSignature );
              if( existingDfs != null && existingDfs.getGosuClass() != dfs.getGosuClass() )
              {
                addError( getClassStatement(), Res.MSG_FUNCTION_CLASH_PARAMS, dfs.getName(), dfs.getGosuClass().getName(), existingDfs.getName(), existingDfs.getGosuClass().getName() );
              }
              functionTypes.put( paramSignature, dfs );
            }
          }
        }
      }
    }
  }

  private boolean inheritsFromTwoOrMoreTypes( IGosuClassInternal gsClass )
  {
    int iCount = gsClass.getSuperClass() == null ? 0 : 1;
    iCount += (gsClass.getInterfaces().length - 1); // subtract IGosuObject proxy
    return iCount > 1;
  }

  private void ensureAbstractMethodsImpledAndNoDiamonds( IGosuClassInternal gsClass )
  {
    List<IFunctionType> unimpled = gsClass.getUnimplementedMethods();
    for( Iterator<IFunctionType> iter = unimpled.iterator(); iter.hasNext(); )
    {
      IFunctionType funcType = iter.next();
      final IMethodInfo mi = funcType.getMethodInfo();
      if( mi.isDefaultImpl() )
      {
        // mi is a default interface method the class (or interface) does not override,
        // check for a duplicate, not-overridden method that comes from an interface that
        // is unrelated to mi's declaring interface
        // i.e., prohibit "diamond" patterns directly interface-inherited from the class (or interface).

        if( conflictsWithUnrelatedIfaceMethod( gsClass, funcType, unimpled ) )
        {
          iter.remove();
        }
      }
      else if( !gsClass.isInterface() && !gsClass.isAbstract() )
      {
        // mi is abstract, the non-abstract class failed to implement it...

        String strClass = funcType.getEnclosingType().getName();
        strClass = IGosuClass.ProxyUtil.getNameSansProxy( strClass );
        getClassStatement().addParseException( new NotImplementedParseException( makeFullParserState(), gsClass, strClass, funcType ) );
      }
    }
  }

  private boolean conflictsWithUnrelatedIfaceMethod( IGosuClassInternal gsClass, IFunctionType ft, List<IFunctionType> unimpled )
  {
    IMethodInfo mi = ft.getMethodInfo();
    outer:
    for( IFunctionType funcType: unimpled )
    {
      if( ft == funcType )
      {
        continue;
      }
      final IMethodInfo csrMi = funcType.getMethodInfo();
      if( csrMi.getDisplayName().equals( mi.getDisplayName() ) &&
          csrMi.getParameters().length == mi.getParameters().length &&
          !csrMi.getOwnersType().isAssignableFrom( mi.getOwnersType() ) &&
          !mi.getOwnersType().isAssignableFrom( csrMi.getOwnersType() ) )
      {
        IParameterInfo[] csrParams = csrMi.getParameters();
        IParameterInfo[] params = mi.getParameters();
        for( int i = 0; i < csrParams.length; i++ )
        {
          IParameterInfo csrPi = csrParams[i];
          IParameterInfo pi = params[i];
          IRType csrDescriptor = IRTypeResolver.getDescriptor( csrPi.getFeatureType() );
          IRType descriptor = IRTypeResolver.getDescriptor( pi.getFeatureType() );
          if( !csrDescriptor.equals( descriptor ) )
          {
            break outer;
          }
        }
        if( csrMi.isDefaultImpl() )
        {
          getClassStatement().addParseException( makeFullParserState(), Res.MSG_INHERITS_UNRELATED_DEFAULTS, gsClass.getName(), funcType, mi.getOwnersType().getName(), csrMi.getOwnersType().getName() );
        }
        else if( gsClass.isAbstract() ) // interface or abstract class
        {
          getClassStatement().addParseException( makeFullParserState(), Res.MSG_INHERITS_ABSTRACT_AND_DEFAULT, gsClass.getName(), funcType, mi.getOwnersType().getName(), csrMi.getOwnersType().getName() );
        }
        return true;
      }
    }
    return false;
  }

  // class-members
  //   <class-member>
  //   <class-members> <class-member>
  //
  // class-member
  //   <function-definition>
  //   <var-statement>
  //

  private void parseClassMembers( IGosuClassInternal gsClass )
  {
    if( isInnerClass( gsClass ) && !gsClass.isStatic() )
    {
      addOuterMember( gsClass );
    }

    ClassScopeCache scopeCache = makeClassScopeCache( gsClass );

    parseEnumConstants( gsClass, scopeCache );

    do
    {
      getOwner().checkInstruction( true );

      while( match( null, ';' ) )
      {
        pushStatement( new NoOpStatement() );
      }

      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      ModifierInfo modifiers;
      if( gsClass instanceof IGosuProgram )
      {
        int[] locations = new int[3];
        modifiers = parseUntilMemberKeyword( null, false, locations );
        iOffset = locations[0];
        iLineNum = locations[1];
        iColumn = locations[2];
      }
      else
      {
        // push static class symbols for annotations (they are part of modifier parsing)
        pushClassSymbols( true, scopeCache );
        try
        {
          modifiers = parseModifiers();
        }
        finally
        {
          popClassSymbols();
        }
      }
      boolean bStatic = Modifier.isStatic( modifiers.getModifiers() );

      if( gsClass.isInterface() )
      {
        modifiers.setModifiers( Modifier.setPublic( modifiers.getModifiers(), true ) );
      }

      boolean bDeprecated = isDeprecated( modifiers );
      if( bDeprecated )
      {
        getOwner().pushIgnoreTypeDeprecation();
      }
      try
      {
        boolean bConstructSyntax = false;
        Token T = new Token();
        if( match( null, Keyword.KW_function ) ||
            (bConstructSyntax = match( null, Keyword.KW_construct )) )
        {
          FunctionStatement functionStmt;
          if( bConstructSyntax || isOldStyleConstructor( gsClass, T ) )
          {
            functionStmt = parseBaseConstructorDefinition( bConstructSyntax, modifiers.getAnnotations(), scopeCache );
            verifyModifiers( functionStmt, modifiers, UsageTarget.ConstructorTarget );
          }
          else
          {
            pushClassSymbols( bStatic, scopeCache );
            try
            {
              functionStmt = getOwner().parseBaseFunctionDefinition( null, false, false, modifiers );
              if( gsClass.isInterface() && !bStatic )
              {
                eatOptionalSemiColon( true );
                pushStatement( functionStmt );
              }
              verifyModifiers( functionStmt, modifiers, UsageTarget.MethodTarget );
            }
            finally
            {
              popClassSymbols();
            }
          }
          DynamicFunctionSymbol dfs = functionStmt == null ? null : functionStmt.getDynamicFunctionSymbol();
          if( dfs != null )
          {
            dfs.setClassMember( true );
            if( dfs.getDisplayName().equals( gsClass.getRelativeName() ) )
            {
              gsClass.getParseInfo().addConstructorFunction(dfs);
            }
            else
            {
              gsClass.getParseInfo().addMemberFunction(dfs);
            }

            verifyTypeVarVariance( Variance.COVARIANT, functionStmt, dfs.getType() );
          }
          setLocation( iOffset, iLineNum, iColumn );
        }
        else if( match( null, Keyword.KW_property ) )
        {
          pushClassSymbols( bStatic, scopeCache );
          try
          {
            Token t = new Token();
            boolean bGetter = match( t, Keyword.KW_get );
            boolean bSetter = !bGetter && match( null, Keyword.KW_set );

            if( !bGetter && !bSetter )
            {
              getOwner().maybeEatNonDeclKeyword( false, t._strValue );
            }
            FunctionStatement functionStmt = getOwner().parseBaseFunctionDefinition( null, true, bGetter, modifiers );
            verify( functionStmt, bGetter || bSetter, Res.MSG_EXPECTING_PROPERTY_GET_OR_SET_MODIFIER );
            setLocation( iOffset, iLineNum, iColumn );
            getOwner().popStatement();

            DynamicFunctionSymbol dfs = functionStmt.getDynamicFunctionSymbol();
            if( dfs != null )
            {
              IType returnType = functionStmt.getDynamicFunctionSymbol().getReturnType();
              verify( functionStmt, bGetter || returnType == JavaTypes.pVOID(), Res.MSG_PROPERTY_SET_MUST_RETURN_VOID );

              if( bGetter && dfs.getArgTypes() != null && dfs.getArgTypes().length > 0 )
              {
                List<IParameterDeclaration> parameters = functionStmt.getParameters();
                for( IParameterDeclaration par : parameters )
                {
                  par.addParseException( Res.MSG_GETTER_CANNOT_HAVE_PARAMETERS );
                }
              }

              dfs.setClassMember( true );
              DynamicPropertySymbol dps = getOrCreateDynamicPropertySymbol( functionStmt, gsClass, dfs, bGetter );
              PropertyStatement stmt = new PropertyStatement( functionStmt, dps );
              getOwner().pushStatement( stmt );
              setLocation( iOffset, iLineNum, iColumn, true );
              verifyPropertiesAreSymmetric( bGetter, dfs, dps, stmt );
              dps.addMemberSymbols( gsClass );

              if( bGetter )
              {
                verifyTypeVarVariance( Variance.COVARIANT, functionStmt,  dps.getGetterDfs().getReturnType() );
              }
              else if( dps.getSetterDfs().getArgTypes().length > 0 )
              {
                verifyTypeVarVariance( Variance.CONTRAVARIANT, functionStmt, dps.getSetterDfs().getArgTypes()[0] );
              }
            }
            verifyModifiers( functionStmt, modifiers, UsageTarget.PropertyTarget );
          }
          finally
          {
            popClassSymbols();
          }
        }
        else if( match( null, Keyword.KW_var ) )
        {
          getOwner().pushParsingStaticMember( bStatic );
          try
          {
            VarStatement varStmt = parseFieldDefn( gsClass, bStatic, scopeCache, modifiers );
            verifyTypeVarVariance( Variance.INVARIANT, varStmt, varStmt.getType() );
            setLocation( iOffset, iLineNum, iColumn );
            removeInitializerIfInProgram( varStmt );
            verifyModifiers( varStmt, modifiers, UsageTarget.PropertyTarget );
          }
          finally
          {
            getOwner().popParsingStaticMember();
          }
        }
        else if( match( null, Keyword.KW_delegate ) )
        {
          DelegateStatement ds = parseDelegateDefn( gsClass, scopeCache, modifiers );
          verifyModifiers( ds, modifiers, UsageTarget.PropertyTarget );
          verifyTypeVarVariance( Variance.INVARIANT, ds, ds.getType() );
          setLocation( iOffset, iLineNum, iColumn );
        }
        else if( match( T, Keyword.KW_class ) ||
                 match( T, Keyword.KW_interface ) ||
                 match( T, Keyword.KW_annotation ) ||
                 match( T, Keyword.KW_structure ) ||
                 match( T, Keyword.KW_enum ) )
        {
          // Pop the modifier list from the declaration phase, otherwise we'll have duplicates
          popModifierList();

          IGosuClassInternal inner = parseInnerClassDefinition( T );
          if( inner != null )
          {
            inner.setAnnotations( modifiers.getAnnotations() );
            if( inner.isInterface() )
            {
              modifiers.setModifiers( Modifier.setStatic( modifiers.getModifiers(), true ) );
              ModifierInfo existingMI = (ModifierInfo)inner.getModifierInfo();
              existingMI.addModifiers( modifiers.getModifiers() );
            }
            verifyModifiers( inner.getClassStatement(), modifiers, UsageTarget.TypeTarget );
          }
        }
        else
        {
          // Pop the trailing modifier list, which doesn't correspond to any member
          popModifierList();

          if( !match( null, null, '}', true ) &&
              !match( null, SourceCodeTokenizer.TT_EOF ) )
          {
            // Consume token first
            boolean openBrace = false;
            if( match( null, '{' ) )
            {
              openBrace = true;
            }
            else
            {
              getOwner().getTokenizer().nextToken();
            }
            NoOpStatement noop = new NoOpStatement();
            verify( noop, false, Res.MSG_UNEXPECTED_TOKEN, getOwner().getTokenizer().getTokenAsString() );
            pushStatement( noop );
            setLocation( iOffset, iLineNum, iColumn );
            if( openBrace )
            {
              eatBlock( '{', '}', false );
            }
          }
          else
          {
            break;
          }
        }
      }
      finally
      {
        if( bDeprecated )
        {
          getOwner().popIgnoreTypeDeprecation();
        }
      }
    } while( true );
  }

  private boolean isDeprecated( ModifierInfo modifiers )
  {
    List<IGosuAnnotation> annotations = modifiers.getAnnotations();
    if( annotations != null )
    {
      for( IGosuAnnotation an : annotations )
      {
        if( an.getName().equalsIgnoreCase( "Deprecated" ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isOldStyleConstructor( IGosuClassInternal gsClass, Token t )
  {
    return match( t, null, SourceCodeTokenizer.TT_WORD, true ) &&
           !gsClass.isInterface() &&
           t._strValue.equals( gsClass.getRelativeName() );
  }

  private ClassScopeCache makeClassScopeCache( IGosuClassInternal gsClass )
  {
    // Copy the Static Scope so we can reuse it for each member
    //
    IScope staticScope;
    Map<String, List<IFunctionSymbol>> staticDfsMap;
    getSymbolTable().pushScope();
    try
    {
      //getOwner().clearDfsDeclInSetByName();
      getOwner().newDfsDeclInSetByName();
      gsClass.putClassMembers( getOwner(), getSymbolTable(), getGosuClass(), true );
      staticDfsMap = getOwner().getDfsDecls();
    }
    finally
    {
      staticScope = getSymbolTable().popScope();
    }

    // Copy the Non-Static Scope so we can reuse it for each member
    //
    IScope nonstaticScope;
    Map<String, List<IFunctionSymbol>> nonstaticDfsMap;
    getSymbolTable().pushScope();
    try
    {
      getOwner().newDfsDeclInSetByName();
      gsClass.putClassMembers( getOwner(), getSymbolTable(), getGosuClass(), false );
      nonstaticDfsMap = getOwner().getDfsDecls();
      getOwner().newDfsDeclInSetByName();
    }
    finally
    {
      nonstaticScope = getSymbolTable().popScope();
    }
    return new ClassScopeCache( staticScope, staticDfsMap, nonstaticScope, nonstaticDfsMap );
  }

  private void popClassSymbols()
  {
    getSymbolTable().popScope();
    getOwner().popParsingStaticMember();
    getOwner().newDfsDeclInSetByName();
  }

  private void pushClassSymbols( boolean bStatic, ClassScopeCache classScopeCache )
  {
    getOwner().setDfsDeclInSetByName( bStatic ? classScopeCache.getStaticDfsMap() : classScopeCache.getNonstaticDfsMap() );
    getSymbolTable().pushScope( bStatic ? classScopeCache.getStaticScope() : classScopeCache.getNonstaticScope() );
    getOwner().pushParsingStaticMember( bStatic );
  }

  private void removeInitializerIfInProgram( VarStatement varStmt )
  {
    if( !(getGosuClass() instanceof IGosuProgram) || getOwner().isEditorParser() )
    {
      return;
    }

    ParseTree location = varStmt.getLocation();
    List<IParseTree> children = location.getChildren();
    int iChildCount = children.size();
    if( iChildCount > 3 )
    {
      if( iChildCount > 4 )
      {
        if( !(children.get( 3 ).getParsedElement() instanceof NameInDeclaration) ) // this is another NameInDeclaration for the Property name, which can be null if the  name was not specified after the 'as' clause
        {
          throw new IllegalStateException( "Expecting children: 1 for NameInDeclaration, 1 for the type, (maybe another NameInDeclaration if an alias property declared), and 1 for the initializer" );
        }
      }
      IParseTree initializerExpr = children.get( iChildCount -1 );
      if( initializerExpr != null )
      {
        location.removeChild( initializerExpr );
      }
    }
  }

  private IGosuClassInternal parseInnerClassDefinition( Token t )
  {
    IGosuClassInternal enclosingGsClass = getClassStatement().getGosuClass();

    verify( getClassStatement(), match( t, SourceCodeTokenizer.TT_WORD ), Res.MSG_EXPECTING_NAME_CLASS_DEF );
    String strInnerClass = t._strValue;
    if( strInnerClass != null )
    {
      for( IGosuClass c : enclosingGsClass.getKnownInnerClassesWithoutCompiling().values() )
      {
        IGosuClassInternal innerClass = (IGosuClassInternal)c;
        if( innerClass.getRelativeName().equals( strInnerClass ) )
        {
          int i = 0;
          String relativeName = innerClass.getName();
          while( innerClass.isDefinitionsCompiled() )
          {
            // The inner class is already definition-compiled, maybe this is a duplicate inner class...

            String duplicate = relativeName + "_duplicate_" + i++;
            innerClass = (IGosuClassInternal)TypeSystem.getByFullNameIfValid( duplicate );
            if( innerClass == null )
            {
              return null;
            }
          }

          new GosuClassParser( getOwner(), innerClass ).parseDefinitions( innerClass );
          return innerClass;
        }
      }
    }
    return null;
  }

  private void parseEnumConstants( IGosuClassInternal gsClass,
                                   ClassScopeCache scopeCache )
  {
    boolean bEnum = gsClass != null && gsClass.isEnum();
    if( !bEnum )
    {
      return;
    }

    Set<String> constants = new HashSet<String>();
    Token t = new Token();
    boolean bConst;
    do
    {
      bConst = false;
      int iOffset = getTokenizer().getTokenStart();
      int iLineNum = getTokenizer().getLineNumber();
      int iColumn = getTokenizer().getTokenColumn();

      if( match( t, null, SourceCodeTokenizer.TT_WORD, true ) &&
          !Keyword.isKeyword( t._strValue ) &&
          match( t, SourceCodeTokenizer.TT_WORD ) )
      {
        parseEnumConstant( t._strValue, scopeCache, constants.contains( t._strValue ) );
        setLocation(iOffset, iLineNum, iColumn);
        constants.add( t._strValue );
        popStatement();
        bConst = true;
      }
      if( match( null, ';' ) )
      {
        break;
      }
    } while( bConst && match( null, ',' ) );
  }

  private void parseEnumConstant( String strIdentifier, ClassScopeCache scopeCache, boolean bIsDuplicate )
  {
    IGosuClassInternal gsClass = getGosuClass();
    VarStatement varStmt = gsClass.getStaticField( strIdentifier );

    if( bIsDuplicate )
    {
      VarStatement dup = new VarStatement();
      dup.setSymbol( varStmt.getSymbol() );
      dup.setModifierInfo( varStmt.getModifierInfo() );
      dup.setParent( varStmt.getParent() );
      varStmt = dup;
    }

    pushClassSymbols( true, scopeCache );
    try
    {
      getOwner().parseNewExpressionOrAnnotation( gsClass, false, !match( null, null, '(', true ), null, -1 );
      Expression asExpr = popExpression();
      varStmt.setAsExpression( asExpr );
      if( asExpr.hasParseExceptions() )
      {
        for( IParseIssue pe : asExpr.getParseExceptions() )
        {
          varStmt.addParseException( pe );
          //noinspection ThrowableResultOfMethodCallIgnored
          asExpr.removeParseException( pe.getMessageKey() );
        }
      }

      varStmt.setScriptPart( getOwner().getScriptPart() );
      pushStatement( varStmt );

      ISymbol symbol = varStmt.getSymbol();
      symbol.setType( gsClass );
      varStmt.setType( gsClass );
      varStmt.setEnumConstant( true );
      varStmt.setDefinitionParsed( true );

      //noinspection unchecked
      scopeCache.getNonstaticScope().put( varStmt.getSymbol().getName(), varStmt.getSymbol() );

      gsClass.getParseInfo().addMemberField(varStmt);
    }
    finally
    {
      popClassSymbols();
    }
  }

  private VarStatement parseFieldDefn( IGosuClassInternal gsClass, boolean bStatic, ClassScopeCache scopeCache, ModifierInfo modifiers )
  {
    if( gsClass.isInterface() )
    {
      bStatic = true;
    }
    Token t = new Token();
    String strIdentifier = "";
    boolean bHasName;
    if( bHasName = match( t, SourceCodeTokenizer.TT_WORD ) )
    {
      strIdentifier = t._strValue;
    }
    else
    {
      t._strValue = null;
    }
    getOwner().maybeEatNonDeclKeyword( bHasName, strIdentifier );
    VarStatement varStmt;
    boolean bOuterLocalDefined = findLocalInOuters( strIdentifier ) != null;
    if( !bStatic )
    {
      varStmt = findMemberField( gsClass, strIdentifier );
      if( varStmt == null )
      {
        // It might not be in the non-static map if it is a scoped variable
        varStmt = findStaticMemberField( gsClass, strIdentifier );
        if( varStmt != null )
        {
          bStatic = true;
        }
      }
    }
    else
    {
      varStmt = findStaticMemberField( gsClass, strIdentifier );
    }
    verifyOrWarn( varStmt, varStmt == null || !bOuterLocalDefined, false, Res.MSG_VARIABLE_ALREADY_DEFINED, strIdentifier );

    if( !bStatic && varStmt != null && varStmt.isStatic() )
    {
      // Force static scope if the var is static.  This is for scoped vars
      bStatic = true;
    }
    pushClassSymbols( bStatic, scopeCache );
    try
    {
      if( varStmt == null )
      {
        // This is for error conditions like vars appearing on enhancements
        varStmt = new VarStatement();
        getOwner().parseVarStatement( varStmt, t, false );
      }
      else
      {
        getOwner().parseVarStatement( varStmt, t, true );
      }

      if( bStatic )
      {
        //noinspection unchecked
        scopeCache.getNonstaticScope().put( varStmt.getSymbol().getName(), varStmt.getSymbol() );
      }

      DynamicPropertySymbol dps = getOwner().parseVarPropertyClause( varStmt, varStmt.getIdentifierName(), varStmt.getType(), true );
      if( dps != null )
      {
        verifyPropertiesAreSymmetric( true, dps.getGetterDfs(), dps, varStmt );
        setStatic( bStatic, dps );
        dps.addMemberSymbols( gsClass );
        dps.updateAnnotations( modifiers.getAnnotations() );
      }

      // Consume optional trailing semi as part of the statement
      match( null, ';' );

      varStmt.getModifierInfo().setAnnotations( modifiers.getAnnotations() );
      gsClass.getParseInfo().addMemberField(varStmt);
      return varStmt;
    }
    finally
    {
      popClassSymbols();
    }
  }

  private ISymbol findLocalInOuters( String strIdentifier )
  {
    if( (isParsingBlock() || getParsingAnonymousClass() != null) && !getOwner().isParsingAnnotation() )
    {
      return captureSymbol( getCurrentEnclosingGosuClass(), strIdentifier, null );
    }
    return null;
  }

  private VarStatement findMemberField( IGosuClassInternal gsClass, String name )
  {
    gsClass.compileDeclarationsIfNeeded();

    return assignPossibleDuplicateField( name, gsClass.getParseInfo().getMemberFields() );
  }

  private VarStatement findStaticMemberField( IGosuClassInternal gsClass, String name )
  {
    gsClass.compileDeclarationsIfNeeded();

    return assignPossibleDuplicateField( name, gsClass.getParseInfo().getStaticFields() );
  }

  private VarStatement assignPossibleDuplicateField( String name, Map<String, VarStatement> fields )
  {
    VarStatement varStmt = fields.get( name );
    varStmt = assignPossibleDuplicateField( name, varStmt, fields );
    return varStmt;
  }

  VarStatement assignPossibleDuplicateField( String name, VarStatement varStmt, Map<String, VarStatement> map )
  {
    VarStatement result = varStmt;
    if( varStmt == null || varStmt.isDefinitionParsed() )
    {
      int iMin = Integer.MAX_VALUE;
      for( String nameCsr : map.keySet() )
      {
        String strName = nameCsr.toString();
        if( strName.toLowerCase().contains( "_duplicate_" + name.toString().toLowerCase() ) )
        {
          VarStatement stmtCsr = map.get( nameCsr );
          if( !stmtCsr.isDefinitionParsed() )
          {
            int iIndex = Integer.parseInt( strName.substring( 0, strName.indexOf( '_' ) ) );
            if( iIndex < iMin )
            {
              iMin = iIndex;
              result = stmtCsr;
            }
          }
        }
      }
    }
    return result;
  }

  private DelegateStatement parseDelegateDefn( IGosuClassInternal gsClass, ClassScopeCache scopeCache, ModifierInfo modifiers )
  {
    Token t = new Token();
    int iNameOffset = getTokenizer().getTokenStart();
    boolean bHasName = match( t, SourceCodeTokenizer.TT_WORD );
    String strIdentifier = t._strValue == null ? "" : t._strValue;
    getOwner().maybeEatNonDeclKeyword( bHasName, strIdentifier );
    String insensitveIdentifier = strIdentifier;
    VarStatement varStmt = gsClass.getMemberField( insensitveIdentifier );
    if( varStmt != null )
    {
      varStmt.setNameOffset( iNameOffset, strIdentifier );
    }

    pushClassSymbols( false, scopeCache );
    try
    {
      //Need to ensure that the varStmt is indeed a delegate statement, because it might be a conflicting var stmt
      DelegateStatement delStmt;
      if( varStmt instanceof DelegateStatement )
      {
        delStmt = (DelegateStatement)varStmt;
      }
      else
      {
        delStmt = new DelegateStatement();
        delStmt.setModifierInfo( modifiers );
      }
      if( varStmt == null )
      {
        // This is for error conditions like delegates appearing on enhancements
        varStmt = new DelegateStatement();
        varStmt.setModifierInfo( modifiers );
        varStmt.setSymbol( new Symbol( strIdentifier, JavaTypes.OBJECT(), null ) );
        verify( delStmt, !Modifier.isStatic( modifiers.getModifiers() ), Res.MSG_DELEGATES_CANNOT_BE_STATIC );
        getOwner().parseDelegateStatement( delStmt, strIdentifier );
      }
      else
      {
        getOwner().parseDelegateStatement( delStmt, strIdentifier );
      }
      gsClass.getParseInfo().addMemberField(varStmt);
      return delStmt;
    }
    finally
    {
      popClassSymbols();
    }
  }

  DynamicPropertySymbol getOrCreateDynamicPropertySymbol(
    ParsedElement parsedElement, ICompilableTypeInternal gsClass, DynamicFunctionSymbol dfs, boolean bGetter )
  {
    String strPropertyName = dfs.getDisplayName().substring( 1 );
    ISymbol symbol = getSymbolTable().getSymbol( strPropertyName );
    if( symbol != null && !dfs.getDisplayName().contains( symbol.getDisplayName() ) )
    {
      // Force case sensitivity, mainly to make overrides consistent
      symbol = null;
    }

    DynamicPropertySymbol dps;
    if( !(gsClass instanceof IGosuClass && ((IGosuClass)gsClass).isCompilingDefinitions()) &&
        !verify( parsedElement, symbol == null || symbol instanceof DynamicPropertySymbol, Res.MSG_VARIABLE_ALREADY_DEFINED, strPropertyName ) )
    {
      return new DynamicPropertySymbol( dfs, bGetter );
    }
    if( symbol == null ||
        (gsClass != null &&
         gsClass.getMemberProperty( strPropertyName ) == null &&
         gsClass.getStaticProperty( strPropertyName ) == null) )
    {
      dps = new DynamicPropertySymbol( dfs, bGetter );
      dps.setClassMember( true );
      if( symbol != null )
      {
        assert symbol instanceof DynamicPropertySymbol;
        dps.setParent( (DynamicPropertySymbol)symbol );
      }
      return dps;
    }
    else if( !(symbol instanceof DynamicPropertySymbol) )
    {
      // Error already applied from declaration phase
      return new DynamicPropertySymbol( dfs, bGetter );
    }

    assert symbol instanceof DynamicPropertySymbol;
    dps = (DynamicPropertySymbol)symbol;
    if( bGetter )
    {
      verify( parsedElement,
              strPropertyName.equals( Keyword.KW_outer.getName() ) ||
              dps.getImmediateGetterDfs() == null ||
              dps.getImmediateGetterDfs() instanceof VarPropertyGetFunctionSymbol ||
              dps.getImmediateGetterDfs().getValueDirectly() != null ||
              dps.getImmediateGetterDfs() == dfs ||
              (dps.getImmediateGetterDfs().isAbstract() && !dfs.isAbstract()) ||
              (gsClass != null && gsClass.isInterface()),
              Res.MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED,
              strPropertyName );
      if( parsedElement.hasParseException( Res.MSG_FUNCTION_ALREADY_DEFINED ) &&
          parsedElement.hasParseException( Res.MSG_GETTER_FOR_PROPERTY_ALREADY_DEFINED ) )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        parsedElement.removeParseException( Res.MSG_FUNCTION_ALREADY_DEFINED );
      }
      dps.setGetterDfs( dfs );
    }
    else
    {
      verify( parsedElement,
              dps.getImmediateSetterDfs() == null ||
              dps.getImmediateSetterDfs() instanceof VarPropertySetFunctionSymbol ||
              dps.getImmediateSetterDfs().getValueDirectly() != null ||
              dps.getImmediateSetterDfs() == dfs ||
              (dps.getImmediateSetterDfs().isAbstract() && !dfs.isAbstract()) ||
              (gsClass != null && gsClass.isInterface()),
              Res.MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED,
              strPropertyName );
      if( parsedElement.hasParseException( Res.MSG_FUNCTION_ALREADY_DEFINED ) &&
          parsedElement.hasParseException( Res.MSG_SETTER_FOR_PROPERTY_ALREADY_DEFINED ) )
      {
        //noinspection ThrowableResultOfMethodCallIgnored
        parsedElement.removeParseException( Res.MSG_FUNCTION_ALREADY_DEFINED );
      }
      dps.setSetterDfs( dfs );
    }
    return dps;
  }

  @SuppressWarnings({"ConstantConditions"})
  private FunctionStatement parseBaseConstructorDefinition( boolean bConstructor, List<IGosuAnnotation> defnAnnotations, ClassScopeCache scopeCache )
  {
    final IGosuClassInternal gsClass = getGosuClass();

    Token T = new Token();

    getSymbolTable().pushScope();
    try
    {
      String strFunctionName;
      if( bConstructor )
      {
        strFunctionName = gsClass.getRelativeName();
      }
      else
      {
        match( T, SourceCodeTokenizer.TT_WORD );
        strFunctionName = T._strValue;
      }

//      String strNameInSource = T._strValue == null ? "" : T._strValue;
//      getOwner().addNameInDeclaration( strFunctionName, iOffsetName-9, iLineName, iColumnName, true );

      // Since we're going with a two-pass approach the symbols will already be in the table, but w/o values.
      // So we don't want to check for already-defined functions here -- we're going to overwrite them with
      // these identical symbols, but with values.
      //verify( _symTable.getSymbol( strFunctionName ) == null, strFunctionName + Res.MSG_VARIABLE_ALREADY_DEFINED ) );

      match( null, '(' );
      List<ISymbol> args;
      IType[] argTypes;
      FunctionStatement functionStmt = new ConstructorStatement( bConstructor );

      int iOffsetParamList = getTokenizer().getTokenStart();
      int iColumnParamList = getTokenizer().getTokenColumn();
      int iLineParamList = getTokenizer().getLineNumber();

      if( !match( null, null, ')', true  ) )
      {
        pushClassSymbols( false, scopeCache );
        try
        {
          args = getOwner().parseParameterDeclarationList( functionStmt, false, null );
        }
        finally
        {
          popClassSymbols();
        }
        argTypes = new IType[args.size()];
        for( int i = 0; i < args.size(); i++ )
        {
          getSymbolTable().putSymbol( args.get( i ) );
          argTypes[i] = args.get( i ).getType();
        }
      }
      else
      {
        argTypes = IType.EMPTY_ARRAY;
        args = Collections.emptyList();

        pushExpression( new ParameterListClause() );
        setLocation( iOffsetParamList, iLineParamList, iColumnParamList, getTokenizer().getTokenStart() <= iOffsetParamList, true );
        popExpression();
      }

      match( null, ')' );

      if( match( null, ":", SourceCodeTokenizer.TT_OPERATOR ) )
      {
        getOwner().parseTypeLiteral();
        Expression expression = popExpression();
        verify( expression, false, Res.MSG_NO_TYPE_AFTER_CONSTRUCTOR );
      }

      StatementList stmtList;
      int iOffset = getOwner().getTokenizer().getTokenStart();
      int iLineNum = getOwner().getTokenizer().getLineNumber();
      int iColumn = getOwner().getTokenizer().getTokenColumn();
      FunctionType ft = new FunctionType( gsClass.getRelativeName(), gsClass, argTypes );
      ft.setScriptPart( getOwner().getScriptPart() );
      getOwner().pushParsingFunction( ft );

      DynamicFunctionSymbol dfsDecl = findConstructorFunction( gsClass, DynamicFunctionSymbol.getSignatureName( strFunctionName, args ) );
      dfsDecl = (dfsDecl == null || dfsDecl.getType() == GosuTypes.DEF_CTOR_TYPE()) ? null : dfsDecl;
      functionStmt = dfsDecl == null ? functionStmt : dfsDecl.getDeclFunctionStmt();
      verify( functionStmt, dfsDecl != null, Res.MSG_EXPECTING_NAME_FUNCTION_DEF );
      if( verify( functionStmt, match( null, '{' ), Res.MSG_EXPECTING_OPEN_BRACE_FOR_CONSTRUCTOR_DEF ) )
      {
        IGosuClassInternal superClass = gsClass.getSuperClass();
        if( superClass != null )
        {
          if( gsClass.isAnonymous() )
          {
            List<? extends IConstructorInfo> declaredConstructors = gsClass.getTypeInfo().getDeclaredConstructors();
            if( verifyCallSiteCtorImpled( functionStmt, declaredConstructors ) )
            {
              verify( functionStmt, declaredConstructors.size() <= 1, Res.MSG_SINGLE_ANON_CTOR );
            }
          }

          // If it's an enum, there's no default super constructor:  the enum class extends the Enum java class
          // which requires a String and an int.  Those arguments are automatically generated by the compiler.
          if( gsClass.getSupertype().getGenericType() != JavaTypes.ENUM() )
          {
            DynamicFunctionSymbol superDefaultConstructor = superClass.getDefaultConstructor();
            verify( functionStmt,
                    match( T, Keyword.KW_super, true ) ||
                    match( T, Keyword.KW_this, true ) ||
                    (superDefaultConstructor != null && superClass.isAccessible( getGosuClass(), superDefaultConstructor )),
                    Res.MSG_NO_DEFAULT_CTOR_IN, superClass.getName() );
          }
        }
        else if( gsClass.isAnonymous() ) // anon on interface
        {
          if( verify( functionStmt, gsClass.getTypeInfo().getDeclaredConstructors().size() <= 1, Res.MSG_SINGLE_ANON_CTOR ) )
          {
            verify( functionStmt, argTypes.length == 0, Res.MSG_ANON_CTOR_PARAMS_CONFLICT_WITH_CALL_SITE );
          }
        }

        // No need to push an isolated scope here because there are no indexed
        // symbol involved. This scope is only to resolve relative constructor
        // calls from within a constructor e.g., this( foo ), super( foo ), etc.
        boolean bMoreStatements = true;
        MethodCallStatement initializer = null;
        boolean bSuperOrThisCall = (match( T, Keyword.KW_super, true ) || match( T, Keyword.KW_this, true )) && getTokenizer().lookaheadType( 1, true ) == '(';
        if( bSuperOrThisCall )
        {
          // Has to be static scope here since the JVM verifier prevents explicitly passing 'this' to super()
          pushClassSymbols( true, scopeCache );
          try
          {
            putSuperAndThisConstructorSymbols();
            // Push static class members in case they are referenced as args in super( xxx ) or this( xxx )
            bMoreStatements = getOwner().parseStatement();
            initializer = (MethodCallStatement)popStatement();
          }
          finally
          {
            popClassSymbols();
          }
        }
        else if( superClass != null )
        {
          MethodCallExpression e = new MethodCallExpression();
          e.setParent( getClassStatement() );
          DynamicFunctionSymbol defaultSuperConstructor;
          // Enums implicitly call a super function that takes a String and an int, not a no-arg method
          if( gsClass.getSupertype().getGenericType() == JavaTypes.ENUM() )
          {
            defaultSuperConstructor = superClass.getConstructorFunction( "Enum(java.lang.String, int)" );
          }
          else
          {
            defaultSuperConstructor = superClass.getDefaultConstructor();
          }
          if( defaultSuperConstructor != null )
          {
            e.setFunctionSymbol( new SuperConstructorFunctionSymbol( defaultSuperConstructor ) );
            e.setArgs( null );
            e.setType( GosuParserTypes.NULL_TYPE() );
            initializer = new MethodCallStatement();
            initializer.setMethodCall( e );
            e.setParent( initializer );
            initializer.setParent( functionStmt );
          }
        }
        else
        {
          MethodCallExpression e = new MethodCallExpression();
          e.setParent( getClassStatement() );
          e.setFunctionSymbol( new InitConstructorFunctionSymbol( getSymbolTable() ) );
          e.setArgs( null );
          e.setType( GosuParserTypes.NULL_TYPE() );
          initializer = new MethodCallStatement();
          initializer.setMethodCall( e );
          e.setParent( initializer );
          initializer.setParent( functionStmt );
        }

        ArrayList<Statement> statements = new ArrayList<Statement>( 8 );
        if( bMoreStatements )
        {
          pushClassSymbols( false, scopeCache );
          getOwner().pushParsingAbstractConstructor( getClassStatement().getGosuClass().isAbstract() );
          getSymbolTable().pushScope();
          try
          {
            getSymbolTable().putSymbol( new Symbol( Keyword.KW_this.getName(), TypeLord.getConcreteType( gsClass ), getSymbolTable(), null ) );
            getSymbolTable().putSymbol( new Symbol( Keyword.KW_super.getName(),
                                                    superClass == null ? IGosuClassInternal.Util.getGosuClassFrom( JavaTypes.OBJECT() ) :
                                                    superClass, getSymbolTable(), null ) );
            getOwner().parseStatementsAndDetectUnreachable( statements );
          }
          finally
          {
            getSymbolTable().popScope();
            getOwner().popParsingAbstractConstructor();
            popClassSymbols();
          }
        }

        verify( functionStmt, match( null, '}' ), Res.MSG_EXPECTING_CLOSE_BRACE_FOR_CONSTRUCTOR_DEF );

        stmtList = new StatementList( getSymbolTable() );
        stmtList.setStatements( statements );

        Statement statement = isDontOptimizeStatementLists() ? stmtList : stmtList.getSelfOrSingleStatement();
        if( statement == stmtList )
        {
          pushStatement( statement );
          setLocation( iOffset, iLineNum, iColumn );
          popStatement();
        }
        if( dfsDecl != null )
        {
          dfsDecl.setArgs( args );
          dfsDecl.setValueDirectly( statement );
          dfsDecl.setInitializer( initializer );
          dfsDecl.getModifierInfo().setAnnotations( defnAnnotations );
        }
      }
      else
      {
        eatStatementBlock( functionStmt, Res.MSG_EXPECTING_OPEN_BRACE_FOR_FUNCTION_DEF );
        NotAStatement nas = new NotAStatement();
        pushStatement( nas );
        setLocation( iOffset, iLineNum, iColumn );
        popStatement();

        if( dfsDecl != null )
        {
          dfsDecl.setArgs( args );
          dfsDecl.setValueDirectly( nas);
          dfsDecl.getModifierInfo().setAnnotations( defnAnnotations );
        }
      }

      getOwner().pushDynamicFunctionSymbol( dfsDecl );
      if( functionStmt != null )
      {
        functionStmt.setDynamicFunctionSymbol( dfsDecl );
        pushStatement( functionStmt );
      }

      return functionStmt;
    }
    finally
    {
      getSymbolTable().popScope();
      if( getOwner().isParsingFunction() )
      {
        getOwner().popParsingFunction();
      }
    }
  }

  private boolean verifyCallSiteCtorImpled( FunctionStatement functionStmt, List<? extends IConstructorInfo> declaredConstructors )
  {
    if( declaredConstructors.size() != 2 )
    {
      return true;
    }
    for( IConstructorInfo ctor: declaredConstructors )
    {
      if( ctor instanceof GosuConstructorInfo )
      {
        if( !verify( functionStmt, !((GosuConstructorInfo)ctor).getDfs().getType().getName().equals( GosuTypes.DEF_CTOR_TYPE().getName() ), Res.MSG_ANON_CTOR_PARAMS_CONFLICT_WITH_CALL_SITE ) )
        {
          // The ctor from the call site is on super, but not impled by this ctor, therefore it implements the wrong one
          return false;
        }
      }
    }
    return true;
  }

  private DynamicFunctionSymbol findConstructorFunction( IGosuClassInternal gsClass, String signatureName )
  {
    gsClass.compileDeclarationsIfNeeded();

    DynamicFunctionSymbol dfs = gsClass.getParseInfo().getConstructorFunctions().get( signatureName );
    if( dfs != null && dfs.getValueDirectly() != null )
    {
      dfs = GosuParser.assignPossibleDuplicateDfs( dfs, gsClass.getParseInfo().getConstructorFunctions().values() );
    }
    return dfs;
  }

  /**
   * Alias super's ctors and this class's ctors as super(xxx) and this(xxx).
   */
  private void putSuperAndThisConstructorSymbols()
  {
    IGosuClassInternal thisClass = getGosuClass();
    IGosuClassInternal superClass = thisClass.getSuperClass();
    if( superClass != null )
    {
      for( DynamicFunctionSymbol dfs : superClass.getConstructorFunctions() )
      {
        if( superClass.isAccessible( getGosuClass(), dfs ) )
        {
          dfs = new SuperConstructorFunctionSymbol( superClass.isParameterizedType()
                                                    ? dfs.getParameterizedVersion( superClass )
                                                    : dfs );
          getSymbolTable().putSymbol( dfs );
          getOwner().putDfsDeclInSetByName( dfs );
        }
      }
    }
    for( DynamicFunctionSymbol dfs : thisClass.getConstructorFunctions() )
    {
      dfs = new ThisConstructorFunctionSymbol( dfs );
      getSymbolTable().putSymbol( dfs );
      getOwner().putDfsDeclInSetByName( dfs );
    }
  }

  private boolean isCyclicInheritance( IType superType, IGosuClassInternal gsClass )
  {
    if( TypeLord.getPureGenericType( superType ) == gsClass )
    {
      return true;
    }

    if( superType != null && superType instanceof IGosuClassInternal )
    {
      if( isCyclicInheritance( ((IGosuClassInternal)superType).getSuperClass(), gsClass ) )
      {
        return true;
      }

      if( isCyclicInheritance( ((IGosuClassInternal)superType).getEnclosingType(), gsClass ) )
      {
        return true;
      }
    }

    return superType instanceof IGosuClassInternal &&
           isCyclicInterfaceInheritance( (IGosuClassInternal)superType, gsClass );
  }

  private boolean isCyclicInterfaceInheritance( IGosuClassInternal gsExtendee, IGosuClass gsExtendor )
  {
    if( gsExtendee == gsExtendor )
    {
      return true;
    }

    IType[] interfaces = gsExtendee.getInterfaces();
    for( int i = 0; i < interfaces.length; i++ )
    {
      IType type = interfaces[i];
      if( type instanceof ErrorType )
      {
        return false;
      }
      IGosuClassInternal gsClass = IGosuClassInternal.Util.getGosuClassFrom( type );
      if( isCyclicInterfaceInheritance( gsClass, gsExtendor ) )
      {
        return true;
      }
    }

    return false;
  }

  @Override
  IGosuClassInternal getGosuClass()
  {
    return (IGosuClassInternal)super.getGosuClass();
  }

  @Override
  public String toString()
  {    
    IGosuClassInternal gosuClass = getGosuClass();
    return "Parsing Class: " + (gosuClass == null ? "null" : gosuClass.getName());
  }
}
