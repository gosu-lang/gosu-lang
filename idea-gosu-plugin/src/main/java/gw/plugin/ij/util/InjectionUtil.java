/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InjectionUtil {
  @Nullable
  public static PsiLanguageInjectionHost getInjectionHost(@NotNull PsiElement element) {
    PsiFile psiFile = element.getContainingFile();
    psiFile = psiFile.getOriginalFile();
    psiFile = InjectedElementEditor.getOriginalFile(psiFile.getOriginalFile());

    // Fast
    final PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(psiFile.getProject()).getInjectionHost(psiFile);
    if (host != null) {
      return host;
    }

    // Slow
    final VirtualFile file = psiFile.getVirtualFile();
    if (file instanceof VirtualFileWindow) {
      final VirtualFileWindow window = (VirtualFileWindow) file;
      final PsiFile psiFileHost = PsiManager.getInstance(psiFile.getProject()).findFile(window.getDelegate());
      final int startOffset = window.getDocumentWindow().getHostRanges()[0].getStartOffset();
      final PsiElement hostElement = psiFileHost.findElementAt(startOffset);
      return PsiTreeUtil.getParentOfType(hostElement, PsiLanguageInjectionHost.class);
    }

    return null;
  }
}
