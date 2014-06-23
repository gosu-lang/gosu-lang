/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.GosuElementType;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.IGosuElementType;
import org.jetbrains.annotations.NotNull;

public abstract class GosuStubElementType<S extends StubElement, T extends IGosuPsiElement> extends IStubElementType<S, T> implements IGosuElementType {
  private final Class<? extends IParsedElement> peType;

  public GosuStubElementType(@NotNull String debugName, Class<? extends IParsedElement> peType) {
    super(debugName, GosuLanguage.instance());
    this.peType = peType;
  }

  public GosuStubElementType(@NotNull Class<? extends IParsedElement> peType) {
    super(GosuElementType.getDebugName(peType), GosuLanguage.instance());
    this.peType = peType;
  }

  public abstract PsiElement createElement(final ASTNode node);

  public void indexStub(final S stub, final IndexSink sink) {
  }

  @NotNull
  public String getExternalId() {
    return "gosu." + super.toString();
  }

  public Class<? extends IParsedElement> getParsedElementType() {
    return peType;
  }
}
