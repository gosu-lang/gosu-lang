/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.stubs.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.io.StringRef;
import gw.lang.parser.statements.IInterfacesClause;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.GosuStubElementType;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuImplementsClauseImpl;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import gw.plugin.ij.lang.psi.stubs.impl.GosuReferenceListStubImpl;
import gw.plugin.ij.lang.psi.stubs.index.GosuDirectInheritorsIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GosuImplementsClauseElementType extends GosuStubElementType<GosuReferenceListStub, IGosuImplementsClause> {

  public GosuImplementsClauseElementType() {
    super("implements clause", IInterfacesClause.class);
  }

  @NotNull
  public IGosuImplementsClause createElement(ASTNode node) {
    return new GosuImplementsClauseImpl((GosuCompositeElement) node);
  }

  @NotNull
  public IGosuImplementsClause createPsi(@NotNull GosuReferenceListStub stub) {
    return new GosuImplementsClauseImpl(stub);
  }

  @NotNull
  public GosuReferenceListStub createStub(@NotNull IGosuImplementsClause psi, StubElement parentStub) {
    final IGosuCodeReferenceElement[] elements = psi.getReferenceElements();
    String[] refNames = ContainerUtil.map(elements, new Function<IGosuCodeReferenceElement, String>() {
          @Nullable
          public String fun(@NotNull final IGosuCodeReferenceElement element) {
            return element.getReferenceName();
          }
        }, new String[elements.length]);

    return new GosuReferenceListStubImpl(parentStub, GosuElementTypes.IMPLEMENTS_CLAUSE, refNames);
  }

  public void serialize(@NotNull GosuReferenceListStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    final String[] names = stub.getBaseClasses();
    dataStream.writeByte(names.length);
    for (String s : names) {
      dataStream.writeName(s);
    }
  }

  @NotNull
  public GosuReferenceListStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    final byte b = dataStream.readByte();
    final String[] names = new String[b];
    for (int i = 0; i < b; i++) {
      StringRef name = dataStream.readName();
      names[i] = name == null ? null : name.toString();
    }
    return new GosuReferenceListStubImpl(parentStub, GosuElementTypes.IMPLEMENTS_CLAUSE, names);
  }

  public void indexStub(@NotNull GosuReferenceListStub stub, @NotNull IndexSink sink) {
    for (String name : stub.getBaseClasses()) {
      if (name != null) {
        sink.occurrence(GosuDirectInheritorsIndex.KEY, name);
      }
    }
  }
}
