/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import org.jetbrains.annotations.Nullable;

public class GosuTypeCompletionProposal extends GosuCompletionProposal {
  private final IType _type;
  private final PsiElement _ctx;
  private PsiElement _psiClass;

  public GosuTypeCompletionProposal( IType type, PsiElement ctx, int weight ) {
    _type = type;
    _ctx = ctx;
    setWeight( weight );
  }
  @Override
  public PsiElement resolve( PsiElement context ) {
    return _psiClass == null ? _psiClass = PsiTypeResolver.resolveType( _type, _ctx ) : _psiClass;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return _type.getTypeInfo();
  }

  @Override
  public String getGenericName() {
    return _type.getRelativeName();
  }

  public String getPackageName() {
    return _type.getNamespace();
  }
}
