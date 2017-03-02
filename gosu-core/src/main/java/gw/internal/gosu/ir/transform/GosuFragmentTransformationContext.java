/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.lang.ir.IRStatement;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRType;
import gw.internal.gosu.parser.fragments.GosuFragment;
import gw.lang.reflect.IType;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.java.JavaTypes;

import java.util.List;

public class GosuFragmentTransformationContext extends TransformationContextBase {

  private GosuFragment _fragment;
  private IRType _compilingType;
  private String _sourceFile;

  public GosuFragmentTransformationContext(GosuFragment fragment, IRType compilingType, String sourceFile, boolean instrument) {
    super(fragment );
    _fragment = fragment;
    _compilingType = compilingType;
    _sourceFile = sourceFile;
  }

  @Override
  public void maybeAssignOuterRef(List<IRStatement> statements) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void maybePushSupersEnclosingThisRef(List<IRExpression> arguments) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void pushEnumNameAndOrdinal(IType type, List<IRExpression> args) {
    // Do nothing
  }

  @Override
  public void initCapturedSymbolFields(List<IRStatement> statements) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void initTypeVarFields(List<IRStatement> statements) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void initializeInstanceFields(List<IRStatement> statements) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getOuterThisFieldName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getOuterThisParamName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getSourceFileRef() {
    return _sourceFile;
  }

  @Override
  public String getSourceLine(int lineNumber) {
    return null;
  }

  @Override
  public IRType getIRTypeForCurrentClass() {
    return _compilingType;
  }

  @Override
  public boolean shouldUseReflection(IType declaringClass, IRType root, IRelativeTypeInfo.Accessibility accessibility) {
    // We should assume that reflection is required for any non-public method access from a fragment
    return accessibility != IRelativeTypeInfo.Accessibility.PUBLIC;
  }

  @Override
  public boolean isIllegalProtectedCall( IType declaringClass, IRType root, IRelativeTypeInfo.Accessibility accessibility )
  {
    return accessibility != IRelativeTypeInfo.Accessibility.PUBLIC;
  }

  @Override
  public boolean isExternalSymbol(String name) {
    return _fragment.isExternalSymbol(name);
  }

  @Override
  public boolean isFragmentEvaluation() {
    return true;
  }

  @Override
  public void addAssertionsStaticField()
  {
    throw new UnsupportedOperationException();
  }

  // --------------------- Overrides of methods that reference the underlying context

  @Override
  public IType getCurrentFunctionReturnType() {
    return JavaTypes.OBJECT();
  }
}