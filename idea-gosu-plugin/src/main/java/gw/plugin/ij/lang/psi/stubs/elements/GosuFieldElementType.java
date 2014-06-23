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
import gw.lang.parser.expressions.IVarStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFieldStub;
import gw.plugin.ij.lang.psi.stubs.GosuStubUtils;
import gw.plugin.ij.lang.psi.stubs.impl.GosuFieldStubImpl;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnnotatedMemberIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuFieldNameIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GosuFieldElementType extends GosuStubElementType<GosuFieldStub, IGosuField> {
  public GosuFieldElementType() {
    super("field", IVarStatement.class);
  }

  @NotNull
  public PsiElement createElement(ASTNode node) {
    return new GosuFieldImpl((GosuCompositeElement) node);
  }

  @NotNull
  public IGosuField createPsi(@NotNull GosuFieldStub stub) {
    return new GosuFieldImpl(stub);
  }

  @NotNull
  public GosuFieldStub createStub(@NotNull IGosuField psi, StubElement parentStub) {
    String[] annNames = GosuTypeDefinitionElementType.getAnnotationNames(psi);

    String[] namedParametersArray = ArrayUtil.EMPTY_STRING_ARRAY;

    //## todo:
//    if( psi instanceof GosuFieldImpl )
//    {
//      namedParametersArray = psi.getNamedParametersArray();
//    }

    return new GosuFieldStubImpl(parentStub, StringRef.fromString(psi.getName()), annNames, namedParametersArray, psi.getElementType(), GosuFieldStubImpl.buildFlags(psi));
  }

  public void serialize(@NotNull GosuFieldStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    serializeFieldStub(stub, dataStream);
  }

  @NotNull
  public GosuFieldStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return deserializeFieldStub(dataStream, parentStub);
  }

  public void indexStub(@NotNull GosuFieldStub stub, @NotNull IndexSink sink) {
    indexFieldStub(stub, sink);
  }

  /*
   * ****************************************************************************************************************
   */

  static void serializeFieldStub(@NotNull GosuFieldStub stub, @NotNull StubOutputStream dataStream) throws IOException {
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
  static GosuFieldStub deserializeFieldStub(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    StringRef ref = dataStream.readName();
    final byte b = dataStream.readByte();
    final String[] annNames = new String[b];
    for (int i = 0; i < b; i++) {
      annNames[i] = dataStream.readName().toString();
    }

    final String[] namedParameters = GosuStubUtils.readStringArray(dataStream);
    byte flags = dataStream.readByte();
    return new GosuFieldStubImpl(parentStub, ref, annNames, namedParameters, GosuElementTypes.FIELD, flags);
  }

  static void indexFieldStub(@NotNull GosuFieldStub stub, @NotNull IndexSink sink) {
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
