/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiManagerImpl;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.api.ITypeResolver;
import gw.plugin.ij.util.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FileBasedTypeResolver implements ITypeResolver {
  @Nullable
  @Override
  public PsiElement resolveType(@NotNull IType type, @NotNull PsiElement ctx) {
    final List<VirtualFile> files = FileUtil.getTypeResourceFiles(type);
    if (!files.isEmpty()) {
      final PsiFile psiFile = PsiManagerImpl.getInstance(ctx.getProject()).findFile(files.get(0));
      if (psiFile instanceof PsiClassOwner) {
        final PsiClass[] classes = ((PsiClassOwner) psiFile).getClasses();
        if (classes.length > 0) {
          return classes[0];
        }
      }
      return psiFile;
    }
    return null;
  }

  @Nullable
  @Override
  public PsiClass resolveType(String strFullName, PsiElement ctx) {
    return null;
  }
}
