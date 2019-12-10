/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.parser.exceptions.IWarningSuppressor;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public interface IParsedElement
{
  void addExceptionsFrom( IParsedElement elem );

  IParseTree getLocation();
  void setLocation( IParseTree location );

  void visit( Consumer<IParsedElement> visitor );

  boolean hasParseIssues();

  List<IParseIssue> getParseIssues();

  List<IParseIssue> getImmediateParseIssues();

  IParseIssue getImmediateParseIssue( ResourceKey errKey );

  boolean hasParseExceptions();
  boolean hasParseException( ResourceKey errKey );
  boolean hasImmediateParseIssue( ResourceKey errKey );

  List<IParseIssue> getParseExceptions();

  void addParseException( ResourceKey msgKey, Object... args );
  void addParseException( IParseIssue e );

  void addParseWarning( ResourceKey msgKey, Object... args );
  void addParseWarning( IParseIssue warning );
  boolean hasParseWarning( ResourceKey errKey );

  void clearParseExceptions();

  void clearParseWarnings();

  boolean hasImmediateParseWarnings();

  boolean hasParseWarnings();

  List<IParseIssue> getParseWarnings();

  boolean hasParseIssue( IParseIssue pi );

  @SuppressWarnings("unchecked" )
  <E extends IParsedElement> boolean getContainedParsedElementsByType( Class<E> parsedElementType, List<E> listResults );

  boolean getContainedParsedElementsByTypes( List<IParsedElement> listResults, Class<? extends IParsedElement>... parsedElementTypes );

  boolean getContainedParsedElementsByTypesWithIgnoreSet( List<IParsedElement> listResults, Set<Class<? extends IParsedElement>> ignoreSet, Class<? extends IParsedElement>... parsedElementTypes );

  IType getReturnType();

  void clearParseTreeInformation();

  IParsedElement getParent();

  void setParent( IParsedElement rootElement );

  int getLineNum();

  int getColumn();

  boolean isSynthetic();

  String getFunctionName();

  IParsedElement findRootParsedElement();

  IParsedElement findAncestorParsedElementByType( Class... parsedElementClasses );

  IParsedElementWithAtLeastOneDeclaration findDeclaringStatement( IParsedElement parsedElement, String identifierName );

  List<IToken> getTokens();

  IGosuProgram getGosuProgram();

  IGosuClass getGosuClass();

  boolean isCompileTimeConstant();

  boolean isSuppressed( IWarningSuppressor suppressor );
}
