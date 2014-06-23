/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.impl;

import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import gw.plugin.ij.lang.psi.api.statements.IGosuFieldProperty;
import gw.plugin.ij.lang.psi.stubs.GosuFieldPropertyStub;
import org.jetbrains.annotations.NotNull;

public class GosuFieldPropertyStubImpl extends StubBase<IGosuFieldProperty> implements GosuFieldPropertyStub {
  public static final byte IS_DEPRECATED_BY_DOC_TAG = 0x04;

  private final byte myFlags;
  private final StringRef myName;
  private final String[] myAnnotations;
  private final String[] myNamedParameters;

  public GosuFieldPropertyStubImpl( StubElement parent,
                                    StringRef name,
                                    final String[] annotations,
                                    String[] namedParameters,
                                    final IStubElementType elemType,
                                    byte flags ) {
    super(parent, elemType);
    myName = name;
    myAnnotations = annotations;
    myNamedParameters = namedParameters;
    myFlags = flags;
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

  public boolean isDeprecatedByDocTag() {
    return (myFlags & IS_DEPRECATED_BY_DOC_TAG) != 0;
  }

  public byte getFlags() {
    return myFlags;
  }

  public static byte buildFlags(@NotNull IGosuFieldProperty field) {
    byte f = 0;
    //## todo:
//    if (field instanceof IGosuEnumConstant) {
//      f |= IS_ENUM_CONSTANT;
//    }

//    if (field.isProperty()) {
//      f |= IS_PROPERTY;
//    }

    if (PsiImplUtil.isDeprecatedByDocTag(field)) {
      f |= IS_DEPRECATED_BY_DOC_TAG;
    }
    return f;
  }
}
