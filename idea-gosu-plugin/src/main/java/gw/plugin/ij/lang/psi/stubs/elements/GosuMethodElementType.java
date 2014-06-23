/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import gw.lang.parser.statements.IFunctionStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.stubs.GosuMethodStub;
import gw.plugin.ij.lang.psi.stubs.GosuStubUtils;
import gw.plugin.ij.lang.psi.stubs.impl.GosuMethodStubImpl;
import gw.plugin.ij.lang.psi.stubs.index.GosuAnnotatedMemberIndex;
import gw.plugin.ij.lang.psi.stubs.index.GosuMethodNameIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class GosuMethodElementType extends GosuStubElementType<GosuMethodStub, IGosuMethod> {
  public GosuMethodElementType() {
    super("method definition", IFunctionStatement.class);
  }

  @NotNull
  public IGosuMethod createElement(ASTNode node) {
    return new GosuMethodImpl((GosuCompositeElement) node);
  }

  @NotNull
  public IGosuMethod createPsi(@NotNull GosuMethodStub stub) {
    return new GosuMethodImpl(stub);
  }

  @NotNull
  public GosuMethodStub createStub(@NotNull IGosuMethod psi, StubElement parentStub) {

    return new GosuMethodStubImpl(parentStub, StringRef.fromString(psi.getName()), GosuTypeDefinitionElementType.getAnnotationNames(psi),
        psi.getNamedParametersArray());
  }

  public void serialize(@NotNull GosuMethodStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
    GosuStubUtils.writeStringArray(dataStream, stub.getAnnotations());
    GosuStubUtils.writeStringArray(dataStream, stub.getNamedParameters());
  }

  @NotNull
  public GosuMethodStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    StringRef ref = dataStream.readName();
    final String[] annNames = GosuStubUtils.readStringArray(dataStream);
    String[] namedParameters = GosuStubUtils.readStringArray(dataStream);
    return new GosuMethodStubImpl(parentStub, ref, annNames, namedParameters);
  }

  public void indexStub(@NotNull GosuMethodStub stub, @NotNull IndexSink sink) {
    String name = stub.getName();
    if (name != null) {
      sink.occurrence(GosuMethodNameIndex.KEY, name);
    }
    for (String annName : stub.getAnnotations()) {
      if (annName != null) {
        sink.occurrence(GosuAnnotatedMemberIndex.KEY, annName);
      }
    }
  }
}
