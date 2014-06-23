/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;

public class GosuEnumConstantStubImpl extends GosuFieldStubImpl {

  public GosuEnumConstantStubImpl(StubElement parent,
                                  StringRef name,
                                  final String[] annotations,
                                  String[] namedParameters,
                                  final IStubElementType elemType,
                                  byte flags) {
    super(parent, name, annotations, namedParameters, elemType, flags);
  }

  public boolean isEnumConstant() {
    return true;
  }
}
