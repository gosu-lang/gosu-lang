/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import gw.lang.parser.statements.INamespaceStatement;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuPackageDefinitionImpl extends GosuPsiElementImpl<INamespaceStatement> implements IGosuPackageDefinition {
  public GosuPackageDefinitionImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitPackageDefinition(this);
  }

  @Nullable
  public String getPackageName() {
    IGosuCodeReferenceElement ref = getPackageReference();
    if (ref == null) {
      return "";
    }
    return PsiUtil.getQualifiedReferenceText(ref);
  }

  @Nullable
  public IGosuCodeReferenceElement getPackageReference() {
    return (IGosuCodeReferenceElement) findChildByClass( PsiReference.class );
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitPackageDefinition(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
