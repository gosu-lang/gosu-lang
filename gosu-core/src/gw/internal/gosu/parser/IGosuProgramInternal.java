/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.IType;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITokenizerInstructor;

/**
 */
public interface IGosuProgramInternal extends IGosuProgram, IGosuClassInternal
{
  ISymbolTable getSymbolTable();
  void addCapturedProgramSymbols( ISymbolTable classCompilationSymTable );
  void addProgramEntryPoint( ISymbolTable symbolTable, GosuClassParser gosuClassParser );

  void setExpression( Expression expr );
  void setStatement( Statement stmt );

  void setExpectedReturnType( IType expectedReturnType );

  void setGenRootExprAccess( boolean bGenRootExprAccess );
  boolean isGenRootExprAccess();

  ITokenizerInstructor getTokenizerInstructor();
  void setTokenizerInstructor( ITokenizerInstructor ti );

  ISymbolTable getAdditionalDFSDecls();
  void setAdditionalDFSDecls( ISymbolTable symbolTable );

  void setAnonymous( boolean b );
  void setThrowaway( boolean b );
  boolean isThrowaway();
  void setAllowUses( boolean b );
  boolean allowsUses();

  void setCtxInferenceMgr( Object ctxInferenceMgr );
  
  void setStatementsOnly( boolean bStatementsOnly );
  boolean isStatementsOnly();

  void setContextType(IType contextType);

  boolean isParsingExecutableProgramStatements();
  void setParsingExecutableProgramStatements( boolean b );
}