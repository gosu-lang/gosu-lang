/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import gw.lang.reflect.IType;
import org.jetbrains.annotations.NotNull;

public interface ITypeResolver
{
  PsiElement resolveType( @NotNull final IType type, final PsiElement ctx );
  PsiClass resolveType( final String strFullName, final PsiElement ctx );
}
