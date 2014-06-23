/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.io.StringRef;
import gw.lang.parser.expressions.IVarStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuEnumConstant;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuEnumConstantImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import gw.plugin.ij.lang.psi.stubs.GosuStubUtils;
import gw.plugin.ij.lang.psi.stubs.impl.GosuFieldStubImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GosuEnumConstantElementType extends GosuStubElementType<GosuFieldStub, IGosuEnumConstant> {
  public GosuEnumConstantElementType() {
    super("Enumeration constant", IVarStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuEnumConstantImpl((GosuCompositeElement) node);
  }

  @NotNull
  public IGosuEnumConstant createPsi(@NotNull GosuFieldStub stub) {
    return new GosuEnumConstantImpl(stub);
  }

  @NotNull
  @Override
  public GosuFieldStub createStub(@NotNull IGosuEnumConstant psi, StubElement parentStub) {
    return new GosuFieldStubImpl(
        parentStub, StringRef.fromString(psi.getName()),  ArrayUtil.EMPTY_STRING_ARRAY, ArrayUtil.EMPTY_STRING_ARRAY,
        GosuElementTypes.ENUM_CONSTANT, GosuFieldStubImpl.buildFlags(psi));
  }

  public void serialize(GosuFieldStub stub, StubOutputStream dataStream) throws IOException {
    GosuFieldElementType.serializeFieldStub(stub, dataStream);
  }

  @NotNull
  public GosuFieldStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    StringRef ref = dataStream.readName();
    final byte b = dataStream.readByte();
    final String[] annNames = new String[b];
    for (int i = 0; i < b; i++) {
      annNames[i] = dataStream.readName().toString();
    }

    final String[] namedParameters = GosuStubUtils.readStringArray(dataStream);
    byte flags = dataStream.readByte();
    return new GosuFieldStubImpl(parentStub, ref, annNames, namedParameters, GosuElementTypes.ENUM_CONSTANT, flags);
  }

  public void indexStub(GosuFieldStub stub, IndexSink sink) {
    GosuFieldElementType.indexFieldStub(stub, sink);
  }
}
