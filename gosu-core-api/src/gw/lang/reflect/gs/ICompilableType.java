/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IFileRepositoryBasedType;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IHasInnerClass;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.ICompilable;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;

public interface ICompilableType extends IType, ICompilable, IHasInnerClass, IFileRepositoryBasedType {

  ICompilableType getEnclosingType();

  GosuClassTypeLoader getTypeLoader();

  IRelativeTypeInfo getTypeInfo();

  boolean isAnonymous();

  ISymbol getExternalSymbol(String s);

  ITypeUsesMap getTypeUsesMap();

  IType resolveRelativeInnerClass( String strTypeName, boolean bForce );

  boolean isStatic();

  IGosuParser getParser();

  IClassStatement getClassStatement();

  IGosuClass getBlock(int i);

}
