/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.StubElement;
import gw.lang.parser.IStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuStatement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuBaseStatementImpl<E extends IStatement> extends GosuBaseElementImpl<E, StubElement> implements IGosuStatement {
  public GosuBaseStatementImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitStatement( this );
    }
    else {
      visitor.visitElement( this );
    }
  }
}
