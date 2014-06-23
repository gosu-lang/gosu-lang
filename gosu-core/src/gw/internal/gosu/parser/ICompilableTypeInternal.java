/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.StatementList;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;

import java.util.List;
import java.util.Map;

public interface ICompilableTypeInternal extends ICompilableType {

  ICompilableTypeInternal getEnclosingType();

  Map<String, ICapturedSymbol> getCapturedSymbols();

  IVarStatement getMemberField( String charSequence );

  void addBlock( IBlockClass blockClass );
  void removeBlock( IBlockClass blockClass );

  int getBlockCount();

  void addCapturedSymbol(ICapturedSymbol capturedSymbol);

  ICapturedSymbol getCapturedSymbol( String strName );

  List<? extends IDynamicFunctionSymbol> getMemberFunctions( String names );

  IDynamicPropertySymbol getMemberProperty( String strName );

  IType getEnclosingNonBlockType();

  DynamicPropertySymbol getStaticProperty( String strPropertyName);

  int getDepth();

  void compileDeclarationsIfNeeded();

  void compileDefinitionsIfNeeded( boolean bForce );

  void compileHeaderIfNeeded();

  void putClassMembers(GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic);  
  void putClassMembers(GosuClassTypeLoader loader, GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic);

  void assignTypeUsesMap(GosuParser parser);

  boolean isCreateEditorParser();
    
  int getAnonymousInnerClassCount();
    
  List<? extends IGosuAnnotation> getGosuAnnotations();

  boolean shouldFullyCompileAnnotations();

  List<? extends IVarStatement> getMemberFields();

  List<IVarStatement> getStaticFields();

  String getSource();

  GosuClassParseInfo getParseInfo();

  boolean hasAssertions();
}