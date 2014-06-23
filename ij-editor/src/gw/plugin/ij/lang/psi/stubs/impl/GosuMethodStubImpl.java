/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.impl;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.stubs.GosuMethodStub;
import org.jetbrains.annotations.NotNull;

public class GosuMethodStubImpl extends StubBase<IGosuMethod> implements GosuMethodStub {
  private final StringRef myName;
  private final String[] myAnnotations;
  @NotNull
  private final String[] myNamedParameters;

  public GosuMethodStubImpl(StubElement parent, StringRef name, final String[] annotations, final @NotNull String[] namedParameters) {
    super(parent, GosuElementTypes.METHOD_DEFINITION);
    myName = name;
    myAnnotations = annotations;
    myNamedParameters = namedParameters;
  }

  public String getName() {
    return StringRef.toString(myName);
  }

  public String[] getAnnotations() {
    return myAnnotations;
  }

  @NotNull
  public String[] getNamedParameters() {
    return myNamedParameters;
  }
}
