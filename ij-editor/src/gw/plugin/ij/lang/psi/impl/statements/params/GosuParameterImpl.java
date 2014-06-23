/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.params;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.parser.expressions.IParameterDeclaration;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.auxilary.annotation.IGosuAnnotation;
import gw.plugin.ij.lang.psi.api.statements.IGosuParametersOwner;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public class GosuParameterImpl extends GosuVariableImpl<IParameterDeclaration> implements IGosuParameter {
  public GosuParameterImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public void accept(@NotNull GosuElementVisitor visitor) {
    visitor.visitParameter(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitParameter(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
  @NotNull
  public String toString() {
    return "Parameter";
  }

  public void setType(@Nullable PsiType type) {
    throw new UnsupportedOperationException("Men at work");
//    final IGosuTypeElement typeElement = getTypeElementGosu();
//    if( type == null )
//    {
//      if( typeElement != null )
//      {
//        typeElement.delete();
//      }
//      return;
//    }
//
//    IGosuTypeElement newTypeElement;
//    try
//    {
//      newTypeElement = GosuPsiElementFactory.getInstance( getProject() ).createTypeElement( type );
//    }
//    catch( IncorrectOperationException e )
//    {
//      LOG.error( e );
//      return;
//    }
//
//    if( typeElement == null )
//    {
//      final IGosuModifierList modifierList = getModifierList();
//      newTypeElement = (IGosuTypeElement)addAfter( newTypeElement, modifierList );
//    }
//    else
//    {
//      newTypeElement = (IGosuTypeElement)typeElement.replace( newTypeElement );
//    }
//
//    PsiUtil.shortenReferences( newTypeElement );
  }

  @Nullable
  public IGosuTypeElement getTypeElementGosu() {
    return findChildByClass(IGosuTypeElement.class);
  }

  @Nullable
  public IGosuPsiElement getDefaultInitializer() {
    boolean equalsFound = false;
    for (ASTNode child : this.getNode().getChildren(null)) {
      if (!equalsFound && child instanceof LeafPsiElement && child.getText().equals("=")) {
        equalsFound = true;
      }
      if (equalsFound && child instanceof GosuCompositeElement && child.getPsi() instanceof IGosuPsiElement) {
        return (IGosuPsiElement) child.getPsi();
      }
    }
    return null;
  }

  public boolean isOptional() {
    return getDefaultInitializer() != null;
  }

  @NotNull
  public SearchScope getUseScope() {
    PsiElement scope = getDeclarationScope();
//## todo:
//    if( scope instanceof GosuDocCommentOwner )
//    {
//      GosuDocCommentOwner owner = (GosuDocCommentOwner)scope;
//      final GosuDocComment comment = owner.getDocComment();
//      if( comment != null )
//      {
//        return new LocalSearchScope( new PsiElement[]{scope, comment} );
//      }
//    }

    return new LocalSearchScope(scope);
  }

  @NotNull
  public String getName() {
    final PsiIdentifier identifier = getNameIdentifier();
    return identifier != null ? identifier.getText() : "";
  }

  public int getTextOffset() {
    final PsiIdentifier identifier = getNameIdentifier();
    return identifier != null ? identifier.getTextRange().getStartOffset() : getParent().getTextOffset();
  }

  @NotNull
  public IGosuModifierList getModifierList() {
    return (IGosuModifierList)findChildByClass( PsiModifierList.class );
  }

  @Override
  public boolean hasModifierProperty( @PsiModifier.ModifierConstant @NonNls @NotNull String name ) {
    return getModifierList().hasModifierProperty( name );
  }

  @NotNull
  public PsiElement getDeclarationScope() {
    return checkNotNull(PsiTreeUtil.getParentOfType(this, IGosuParametersOwner.class), "No owner for parameter " + getName());
  }

  public boolean isVarArgs() {
    return false;
  }

  @NotNull
  public IGosuAnnotation[] getAnnotations() {
    return IGosuAnnotation.EMPTY_ARRAY;
  }

}
