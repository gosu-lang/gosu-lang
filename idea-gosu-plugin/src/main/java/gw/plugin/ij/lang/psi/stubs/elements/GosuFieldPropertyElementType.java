/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ArrayUtil;
import com.intellij.util.io.StringRef;
import gw.lang.parser.expressions.INameInDeclaration;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.statements.IGosuFieldProperty;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldPropertyImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFieldPropertyStub;
import gw.plugin.ij.lang.psi.stubs.GosuStubUtils;
import gw.plugin.ij.lang.psi.stubs.impl.GosuFieldPropertyStubImpl;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnnotatedMemberIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuFieldNameIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GosuFieldPropertyElementType extends GosuStubElementType<GosuFieldPropertyStub, IGosuFieldProperty> {
  public GosuFieldPropertyElementType() {
    super("fieldProperty", INameInDeclaration.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuFieldPropertyImpl((GosuCompositeElement) node);
  }

  @NotNull
  public IGosuFieldProperty createPsi(@NotNull GosuFieldPropertyStub stub) {
    return new GosuFieldPropertyImpl(stub);
  }

  @NotNull
  public GosuFieldPropertyStub createStub(@NotNull IGosuFieldProperty psi, StubElement parentStub) {
    String[] annNames = GosuTypeDefinitionElementType.getAnnotationNames(psi);

    String[] namedParametersArray = ArrayUtil.EMPTY_STRING_ARRAY;

    return new GosuFieldPropertyStubImpl(parentStub, StringRef.fromString(psi.getName()), annNames, namedParametersArray, psi.getElementType(), GosuFieldPropertyStubImpl.buildFlags( psi ));
  }

  public void serialize(@NotNull GosuFieldPropertyStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    serializeFieldStub(stub, dataStream);
  }

  @NotNull
  public GosuFieldPropertyStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return deserializeFieldStub(dataStream, parentStub);
  }

  public void indexStub(@NotNull GosuFieldPropertyStub stub, @NotNull IndexSink sink) {
    indexFieldStub(stub, sink);
  }

  /*
   * ****************************************************************************************************************
   */

  static void serializeFieldStub(@NotNull GosuFieldPropertyStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    final String[] annotations = stub.getAnnotations();
    dataStream.writeByte(annotations.length);
    for (String s : annotations) {
      dataStream.writeName(s);
    }

    final String[] namedParameters = stub.getNamedParameters();
    GosuStubUtils.writeStringArray(dataStream, namedParameters);

    dataStream.writeByte(stub.getFlags());
  }

  @NotNull
  static GosuFieldPropertyStub deserializeFieldStub(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    StringRef ref = dataStream.readName();
    final byte b = dataStream.readByte();
    final String[] annNames = new String[b];
    for (int i = 0; i < b; i++) {
      annNames[i] = dataStream.readName().toString();
    }

    final String[] namedParameters = GosuStubUtils.readStringArray(dataStream);
    byte flags = dataStream.readByte();
    return new GosuFieldPropertyStubImpl(parentStub, ref, annNames, namedParameters, GosuElementTypes.FIELD_PROPERTY, flags);
  }

  static void indexFieldStub(@NotNull GosuFieldPropertyStub stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(GosuFieldNameIndex.KEY, name);
    }
    for (String annName : stub.getAnnotations()) {
      if (annName != null) {
        sink.occurrence(GosuAnnotatedMemberIndex.KEY, annName);
      }
    }
  }
}
