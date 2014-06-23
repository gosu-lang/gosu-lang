/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuReferenceList;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.GosuClassReferenceType;
import gw.plugin.ij.lang.psi.stubs.GosuReferenceListStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class GosuReferenceListImpl extends GosuBaseElementImpl<IExpression, GosuReferenceListStub>
    implements StubBasedPsiElement<GosuReferenceListStub>, IGosuReferenceList {
  @Nullable
  private PsiClassType[] cachedTypes;

  public GosuReferenceListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  public GosuReferenceListImpl(@NotNull final GosuReferenceListStub stub, @NotNull IStubElementType elementType) {
    super(stub, elementType);
  }

  @NotNull
  public IGosuCodeReferenceElement[] getReferenceElements() {
    return findChildrenByClass(IGosuCodeReferenceElement.class);
  }

  @NotNull
  public PsiClassType[] getReferenceTypes() {
    final GosuReferenceListStub stub = getStub();
    if (stub != null) {
      return stub.getReferencedTypes();
    }

    if (cachedTypes == null || !isValid()) {
      final ArrayList<PsiClassType> types = new ArrayList<>();
      for (IGosuCodeReferenceElement ref : getReferenceElements()) {
        types.add(new GosuClassReferenceType(ref));
      }
      cachedTypes = types.toArray(new PsiClassType[types.size()]);
    }
    return cachedTypes;
  }

  @Override
  public void subtreeChanged() {
    cachedTypes = null;
  }

  @NotNull
  @Override
  public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
    throw new UnsupportedOperationException();
//    if( element instanceof IGosuCodeReferenceElement && findChildByClass( IGosuCodeReferenceElement.class ) != null )
//    {
//      PsiElement lastChild = getLastChild();
//      lastChild = PsiUtil.skipWhitespaces( lastChild, false );
//      if( !lastChild.getNode().getElementType().equals( GosuTokenTypes.mCOMMA ) )
//      {
//        getNode().addLeaf( GosuTokenTypes.mCOMMA, ",", null );
//      }
//      return super.add( element );
//    }
//    else
//    {
//      return super.add( element );
//    }
  }
}
