/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.impl.ElementPresentationUtil;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.RowIcon;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IHasType;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.impl.GosuDeclaredElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuPsiImplUtil;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class GosuVariableBaseImpl<E extends IParsedElementWithAtLeastOneDeclaration, T extends StubElement>
    extends GosuDeclaredElementImpl<E, T> implements IGosuVariable {
  public static final Logger LOG = Logger.getInstance("gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl");

  public GosuVariableBaseImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  protected GosuVariableBaseImpl(@NotNull final T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  public PsiTypeElement getTypeElement() {
    return null;
  }

  @Nullable
  public PsiExpression getInitializer() {
    for( PsiElement elem = findChildByType( GosuElementTypes.TT_OP_assign ); elem != null; elem = elem.getNextSibling() ) {
      if( elem instanceof PsiExpression ) {
        return (PsiExpression)elem;
      }
    }
    return null;
  }

  public boolean hasInitializer() {
    return getInitializer() != null;
  }

  public void normalizeDeclaration() throws IncorrectOperationException {
  }

  @Nullable
  public Object computeConstantValue() {
    return null;
  }

  @NotNull
  public PsiType getType() {
    return ExecutionUtil.execute( new SafeCallable<PsiType>(this) {
      public PsiType execute() throws Exception {
        PsiType type = getDeclaredType();
        if( type == null ) {
          type = PsiType.getJavaLangObject(getManager(), getResolveScope());
        }
        return type;
      }

      @Override
      public PsiType handleNull( Throwable exception ) {
        // Can't return a null type :/
        return ExecutionUtil.execute( new SafeCallable<PsiType>(GosuVariableBaseImpl.this) {
          public PsiType execute() throws Exception {
            return createType( JavaTypes.OBJECT() );
          }
        });
      }
    });
  }

  @Nullable
  public IGosuTypeElement getTypeElementGosu() {
    return (IGosuTypeElement) findChildByType(GosuElementTypes.ELEM_TYPE_TypeLiteral);
  }

  @Nullable
  public PsiType getDeclaredType() {
    IGosuTypeElement typeElement = getTypeElementGosu();
    if (typeElement != null) {
      return typeElement.getType();
    }

    Object parsedElement = getParsedElement();
    if (parsedElement instanceof IHasType) {
      IType type = ((IHasType) parsedElement).getType();

      if (type instanceof ICompilableType && ((ICompilableType) type).isAnonymous()) {
        IType superType = type.getSupertype();
        if (superType == null) {
          superType = type.getInterfaces()[0];
        }
        type = superType;
      }
      return createType(type);
    }

    return null;
  }

  public void setType(@Nullable PsiType type) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  public IGosuExpression getInitializerGosu() {
    PsiExpression psiInit = getInitializer();
    if( psiInit instanceof IGosuExpression ) {
      return (IGosuExpression) psiInit;
    }
    return null;
  }

  public int getTextOffset() {
    final PsiIdentifier nameIdentifier = getNameIdentifier();
    return nameIdentifier == null ? getTextRange().getStartOffset() : nameIdentifier.getTextRange().getStartOffset();
  }

  @NotNull
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    GosuPsiImplUtil.setName(getNameIdentifier(), name);
    return this;
  }

  @NotNull
  public SearchScope getUseScope() {
    final AbstractStatementWithLocalDeclarationsImpl owner = PsiTreeUtil.getParentOfType(this, AbstractStatementWithLocalDeclarationsImpl.class);
    if (owner != null) {
      return new LocalSearchScope(owner);
    }
    return super.getUseScope();
  }

  @NotNull
  public String getName() {
    return GosuPsiImplUtil.getName(this);
  }

  @Nullable
  public PsiIdentifier getNameIdentifier() {
    //return (PsiIdentifier)findElement( this, GosuElementTypes.ELEM_TYPE_NameInDeclaration );
    class FindIdSkippingAnnotations extends PsiRecursiveElementWalkingVisitor {
      PsiIdentifier id;
      public void visitElement(PsiElement element) {
        if (element instanceof PsiAnnotation) {
          return;
        } else if (element instanceof PsiIdentifier) {
          id = (PsiIdentifier) element;
          stopWalking();
          return;
        }
        super.visitElement(element);
      }
    }
    FindIdSkippingAnnotations finder = new FindIdSkippingAnnotations();
    this.accept(finder);
    PsiIdentifier id = finder.id;
    if (id != null && id.getFirstChild() != null && id.getFirstChild() instanceof PsiIdentifier) {
      // Always return the leaf token node; we always want to patch in just the name and not mess with upper-level tree nodes
      id = (PsiIdentifier) id.getFirstChild();
    }
    return id;
  }

  @Nullable
  public IGosuModifierList getModifierList() {
    return (IGosuModifierList)findChildByClass( PsiModifierList.class );
  }

  @Override
  public boolean hasModifierProperty( @PsiModifier.ModifierConstant @NonNls @NotNull String name ) {
    IGosuModifierList modifierList = getModifierList();
    return modifierList != null ? modifierList.hasModifierProperty(name) : false;
  }

  @NotNull
  public PsiType getTypeNoResolve() {
    return getType();
  }

  @Override
  public Icon getElementIcon(final int flags) {
    final RowIcon baseIcon = ElementPresentationUtil.createLayeredIcon(GosuIcons.VARIABLE, this, false);
    return ElementPresentationUtil.addVisibilityIcon(this, flags, baseIcon);
  }
}
