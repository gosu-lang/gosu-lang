/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.expressions.Identifier;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.IStatement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITokenizerInstructor;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.expressions.IFieldAccessExpression;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.*;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import org.xml.sax.Attributes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class GosuProgram extends GosuClass implements IGosuProgramInternal
{
  private IType _expectedReturnType;
  private boolean _bGenRootExprAccess;
  private ITokenizerInstructor _tokenizerInstructor;
  private volatile IProgramInstance _sharedInstance;
  private boolean _anonymous;
  private boolean _throwaway;
  private boolean _bStatementsOnly;
  private IType _contextType;
  private ContextInferenceManager _ctxInferenceMgr;
  private boolean _bParsingExecutableProgramStmts;
  private boolean _allowUses;

  public GosuProgram( String strNamespace, String strRelativeName, 
                      GosuClassTypeLoader classTypeLoader, ISourceFileHandle sourceFile, ITypeUsesMap typeUsesMap,
                      ISymbolTable symTable )
  {
    super( strNamespace, strRelativeName, classTypeLoader, sourceFile, typeUsesMap );
    createNewParseInfo();
    getParseInfo().setSymbolTable(symTable instanceof CompiledGosuClassSymbolTable
                ? ((CompiledGosuClassSymbolTable)symTable).getTargetSymbolTable() 
                : symTable);
    addProgramInterfaces();
  }

  protected void addProgramInterfaces()
  {
    addInterface( JavaTypes.IPROGRAM_INSTANCE() );
  }

  /**
   * When changing the places from which this method is called run pc's
   * gw.smoketest.pc.job.common.effectivetime.VisibleEffectiveTimeTest
   * cause it will break!
   */
  public GosuClassParseInfo createNewParseInfo() {
    if( getParseInfo() == null ) {
      _parseInfo = new GosuProgramParseInfo((IGosuClassInternal) getOrCreateTypeReference());
    }
    return getParseInfo();
  }

  public GosuProgramParseInfo getParseInfo() {
    return (GosuProgramParseInfo) super.getParseInfo();
  }

  public ISymbolTable getSymbolTable()
  {
    return getParseInfo().getSymbolTable();
  }

  public void addProgramEntryPoint( ISymbolTable symTable, GosuClassParser parser )
  {
    IJavaType programInstance = JavaTypes.IPROGRAM_INSTANCE();
    IType symbolMap = JavaTypes.IEXTERNAL_SYMBOL_MAP();
    addProgramInstanceMethod( symTable, parser, programInstance, "evaluate", symbolMap);
    addProgramInstanceMethod( symTable, parser, programInstance, "evaluateRootExpr", symbolMap);
  }

  private void addProgramInstanceMethod( ISymbolTable symTable, GosuClassParser parser, IJavaType cls, String strMethod, IType params )
  {
    IGosuProgramInternal pThis = (IGosuProgramInternal)getOrCreateTypeReference();
    IType iface = TypeLord.getDefaultParameterizedType( cls );
    IGosuClassInternal gsInterface = Util.getGosuClassFrom( iface );
    IMethodInfo mi = gsInterface.getTypeInfo().getMethod( strMethod, params );
    GosuMethodInfo gmi = (GosuMethodInfo)mi;

    if (gmi != null) {
      ReducedDynamicFunctionSymbol dfs = gmi.getDfs();
      mi = (IMethodInfo) dfs.getMethodOrConstructorInfo();
      if( !(iface instanceof IGosuClass) )
      {
        String strMethodName = mi.getDisplayName();
        mi = iface.getTypeInfo().getMethod( strMethodName, ((IFunctionType)dfs.getType()).getParameterTypes() );
        if( mi == null )
        {
          throw new IllegalStateException( "Did not find ProgramClass method info for: " + mi );
        }
      }
      ProgramClassFunctionSymbol programClassFs = new ProgramClassFunctionSymbol( pThis, symTable, gmi );
      parser.processFunctionSymbol( programClassFs, pThis );
    }

    addCapturedProgramSymbols( symTable );
  }

  public void addCapturedProgramSymbols( ISymbolTable classCompilationSymTable )
  {
    ISymbolTable symTable = getSymbolTable();
    if( symTable == null )
    {
      return;
    }

    if( !JavaTypes.OBJECT().equals( getSupertype() ) )
    {
      // External symbols are not allowed if there is an explicit super type
      return;
    }

    HashMap<String, ISymbol> externalSymbolsMap = new HashMap<String, ISymbol>( 8 );
    getParseInfo().setExternalSymbols(new ExternalSymbolMapForMap(externalSymbolsMap));
    Map symbols = symTable.getSymbols();
    if( symbols == null )
    {
      return;
    }
    //noinspection unchecked
    for( ISymbol sym : (Collection<ISymbol>)symbols.values() )
    {
      if( !(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null )
      {
        externalSymbolsMap.put( sym.getName(), sym );
      }
    }
  }

  @Override
  public boolean isAnonymous()
  {
    return _anonymous;
  }

  public boolean isThrowaway() {
    return _throwaway;
  }

  public void setThrowaway(boolean throwaway) {
    _throwaway = throwaway;
  }

  @Override
  public void setAllowUses(boolean b) {
    _allowUses = true;
  }

  @Override
  public boolean allowsUses() {
    return _allowUses || getName().contains(IGosuProgram.NAME_PREFIX);
  }

  public void setCtxInferenceMgr( Object ctxInferenceMgr ) {
    _ctxInferenceMgr = (ContextInferenceManager)ctxInferenceMgr;
  }

  @Override
  public ISymbol getExternalSymbol( String strName )
  {
    ExternalSymbolMapForMap externalSymbols = getParseInfo().getExternalSymbols();
    ISymbol sym = externalSymbols == null ? null : externalSymbols.getSymbol( strName );
    if( sym == null )
    {
      if( getSymbolTable() instanceof Attributes )
      {
        //noinspection UnusedAssignment
        sym = getSymbolTable().getSymbol( strName );
      }
      if( sym == null )
      {
        sym = super.getExternalSymbol( strName );
      }
    }
    return sym;
  }

  @Override
  public void setExpression( Expression expr )
  {
    getParseInfo().setExpression(expr);
  }
  @Override
  public IExpression getExpression()
  {
    return getParseInfo().getExpression();
  }

  @Override
  public void setStatement( Statement stmt )
  {
    getParseInfo().setStatement(stmt);
  }
  @Override
  public IStatement getStatement()
  {
    return getParseInfo().getStatement();
  }

  @Override
  public boolean isExpression()
  {
    return getParseInfo().getExpression() != null;
  }

  @Override
  public boolean isLhsExpression()
  {
    IExpression expression = getParseInfo().getExpression();
    return expression instanceof IFieldAccessExpression ||
           expression instanceof Identifier;

  }

  @Override
  public IParsedElement getEnclosingEvalExpression()
  {
    return getParseInfo().getEvalExpression();
  }
  @Override
  public void setEnclosingEvalExpression( IParsedElement evalExprOrAnyExpr )
  {
    getParseInfo().setEvalExpression( evalExprOrAnyExpr );
  }

  @Override
  public Object evaluate(IExternalSymbolMap externalSymbols)
  {
    return runProgram(externalSymbols);
  }

  @Override
  public Object evaluateRoot(IExternalSymbolMap externalSymbols) {
    try
    {
      return getProgramInstance().evaluateRootExpr( externalSymbols );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @Override
  public void assign( Object value )
  {
    //## todo: implement l-value assignment
  }

  @Override
  public void setGenRootExprAccess( boolean bGenRootExprAccess )
  {
    _bGenRootExprAccess = bGenRootExprAccess;
  }
  @Override
  public boolean isGenRootExprAccess()
  {
    return _bGenRootExprAccess;
  }

  @Override
  public ITokenizerInstructor getTokenizerInstructor()
  {
    return _tokenizerInstructor;
  }
  @Override
  public void setTokenizerInstructor( ITokenizerInstructor ti )
  {
    _tokenizerInstructor = ti;
  }

  @Override
  public ISymbolTable getAdditionalDFSDecls() {
    return getParseInfo().getAdditionalDFSDecls();
  }

  @Override
  public void setAdditionalDFSDecls(ISymbolTable symbolTable) {
    getParseInfo().setAdditionalDFSDecls(symbolTable);
  }

  @Override
  protected GosuParser getOrCreateParser(CompiledGosuClassSymbolTable symbolTable)
  {
    GosuParser parser = super.getOrCreateParser(symbolTable);
    parser.setTokenizerInstructor( getTokenizerInstructor() );
    // This is a bit sketchy, so it might not be correct.  This was done so that GosuExpressionAnalysisUtil could
    // pass through additional DFSs, i.e. those defined in the code block, that could be used during compilation.
    ISymbolTable additionalDFSDecls = getParseInfo().getAdditionalDFSDecls();
    if (additionalDFSDecls != null) {
      parser.putDfsDeclsInTable( additionalDFSDecls );
    }
    if( _ctxInferenceMgr != null )
    {
      parser.setContextInferenceManager( _ctxInferenceMgr );
    }
    return parser;
  }

  @Override
  public IType getExpectedReturnType()
  {
    return _expectedReturnType;
  }
  @Override
  public void setExpectedReturnType( IType expectedReturnType )
  {
    if( expectedReturnType != null && expectedReturnType.isPrimitive() && expectedReturnType != JavaTypes.pVOID() )
    {
      expectedReturnType = TypeSystem.getBoxType( expectedReturnType );
    }
    _expectedReturnType = expectedReturnType;
  }

  @Override
  public IType getReturnType()
  {
    if( getExpectedReturnType() != null )
    {
      return getExpectedReturnType();
    }

    IStatement statement = getParseInfo().getStatement();
    if( statement != null )
    {
      return statement.getReturnType();
    }
    return getParseInfo().getExpression().getType();
  }

  @Override
  public IProgramInstance getProgramInstance() {
    if (_sharedInstance != null) {
      return _sharedInstance;
    } else {
      if (canShareProgramInstances()) {
        if (_sharedInstance == null) {
          synchronized (this) {
            if (_sharedInstance == null) {
              _sharedInstance = createNewInstance();
            }
          }
        }
        return _sharedInstance;
      } else {
        return createNewInstance();
      }
    }
  }

  @Override
  public void setAnonymous( boolean b )
  {
    _anonymous = b;
  }

  @Override
  public void setStatementsOnly( boolean bStatementsOnly )
  {
    _bStatementsOnly = bStatementsOnly;
  }
  public boolean isStatementsOnly()
  {
    return _bStatementsOnly;
  }

  private IProgramInstance createNewInstance() {
    try {
      return (IProgramInstance) getBackingClass().newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean canShareProgramInstances()
  {
    // Note we check for existence of member fields from the Class and not the GosuClass
    // to avoid parsing the GosuClass at runtime.  This method is only called at runtime.
    for( Field f : getBackingClass().getDeclaredFields() )
    {
      if( !Modifier.isStatic( f.getModifiers() ) )
      {
        return false;
      }
    }
    return true;
  }

  private Object runProgram(IExternalSymbolMap externalSymbols)
  {
    try
    {
      return getProgramInstance().evaluate( externalSymbols );
    }
    catch( Exception e ) // not catching Throwable b/c we need to *not* ever catch ThreadDeath, which is thrown from the gosu editor to stop the program
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public void setContextType(IType contextType) {
    _contextType = contextType;
  }

  public IType getContextType() {
    return _contextType;
  }

  @Override
  public boolean isParsingExecutableProgramStatements()
  {
    return _bParsingExecutableProgramStmts;
  }

  @Override
  public void setParsingExecutableProgramStatements( boolean b )
  {
    _bParsingExecutableProgramStmts = b;
  }

  @Override
  public ClassType getClassType() {
    return ClassType.Program;
  }
}
