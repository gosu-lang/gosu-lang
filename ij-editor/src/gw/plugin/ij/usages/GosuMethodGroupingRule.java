/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.intellij.openapi.actionSystem.DataKey;
import com.intellij.openapi.actionSystem.DataSink;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.TypeSafeDataProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vcs.FileStatus;
import com.intellij.openapi.vcs.FileStatusManager;
import com.intellij.openapi.vcs.impl.FileStatusProvider;
import com.intellij.openapi.vcs.impl.VcsFileStatusProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSubstitutor;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.Usage;
import com.intellij.usages.UsageGroup;
import com.intellij.usages.UsageView;
import com.intellij.usages.UsageViewSettings;
import com.intellij.usages.rules.PsiElementUsage;
import com.intellij.usages.rules.UsageGroupingRule;
import gw.plugin.ij.lang.psi.IGosuFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuMethodGroupingRule implements UsageGroupingRule {
  private static final Logger LOG = Logger.getInstance("#com.intellij.usages.impl.rules.MethodGroupingRule");

  public UsageGroup groupUsage(@NotNull Usage usage) {
    if (!(usage instanceof PsiElementUsage)) return null;
    PsiElement psiElement = ((PsiElementUsage) usage).getElement();
    PsiFile containingFile = psiElement.getContainingFile();
    PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(containingFile);
    if (topLevelFile instanceof IGosuFile) {
      PsiElement containingMethod = topLevelFile == containingFile ? psiElement : containingFile.getContext();
      do {
        containingMethod = PsiTreeUtil.getParentOfType(containingMethod, PsiMethod.class, true);
        if (containingMethod == null || ((PsiMethod) containingMethod).getContainingClass().getQualifiedName() != null)
          break;
      }
      while (true);

      if (containingMethod != null) {
        return new MethodUsageGroup((PsiMethod) containingMethod);
      }
    }
    return null;
  }

  private static class MethodUsageGroup implements UsageGroup, TypeSafeDataProvider {
    @NotNull
    private final SmartPsiElementPointer<PsiMethod> myMethodPointer;
    private final String myName;
    private Icon myIcon;

    public MethodUsageGroup(@NotNull PsiMethod psiMethod) {
      myName = PsiFormatUtil.formatMethod(
          psiMethod,
          PsiSubstitutor.EMPTY,
          PsiFormatUtil.SHOW_NAME | PsiFormatUtil.SHOW_PARAMETERS,
          PsiFormatUtil.SHOW_TYPE
      );
      myMethodPointer = SmartPointerManager.getInstance(psiMethod.getProject()).createLazyPointer(psiMethod);
      update();
    }

    public void update() {
      if (isValid()) {
        myIcon = getIconImpl(getMethod());
      }
    }

    private static Icon getIconImpl(@NotNull PsiMethod psiMethod) {
      return psiMethod.getIcon(Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
    }

    public int hashCode() {
      return myName.hashCode();
    }

    public boolean equals(@NotNull Object object) {
      if (!(object instanceof MethodUsageGroup)) {
        return false;
      }
      MethodUsageGroup group = (MethodUsageGroup) object;
      if (isValid() && group.isValid()) {
        return getMethod().getManager().areElementsEquivalent(getMethod(), group.getMethod());
      }
      return Comparing.equal(myName, ((MethodUsageGroup) object).myName);
    }

    public Icon getIcon(boolean isOpen) {
      return myIcon;
    }

    @Nullable
    private PsiMethod getMethod() {
      return myMethodPointer.getElement();
    }

    @NotNull
    public String getText(UsageView view) {
      return myName;
    }

    public FileStatus getFileStatus() {
      return isValid() ?
          FileStatusManager.getInstance(getMethod().getProject()).getStatus(getMethod().getContainingFile().getVirtualFile())
          : null;
    }

    public boolean isValid() {
      final PsiMethod method = getMethod();
      return method != null && method.isValid();
    }

    public void navigate(boolean focus) throws UnsupportedOperationException {
      if (canNavigate()) {
        getMethod().navigate(focus);
      }
    }

    public boolean canNavigate() {
      return isValid();
    }

    public boolean canNavigateToSource() {
      return canNavigate();
    }

    public int compareTo(@NotNull UsageGroup usageGroup) {
      if (!(usageGroup instanceof MethodUsageGroup)) {
        LOG.error("MethodUsageGroup expected but " + usageGroup.getClass() + " found");
      }
      MethodUsageGroup other = (MethodUsageGroup) usageGroup;
      PsiMethod myMethod = myMethodPointer.getElement();
      PsiMethod otherMethod = other.myMethodPointer.getElement();
      if (myMethod != null && otherMethod != null && myMethod != otherMethod && !UsageViewSettings.getInstance().IS_SORT_MEMBERS_ALPHABETICALLY) {
        return myMethod.getTextOffset() < otherMethod.getTextOffset() ? -1 : 1;
      }

      return myName.compareTo(other.myName);
    }

    public void calcData(final DataKey key, @NotNull final DataSink sink) {
      if (!isValid()) return;
      if (LangDataKeys.PSI_ELEMENT == key) {
        sink.put(LangDataKeys.PSI_ELEMENT, getMethod());
      }
      if (UsageView.USAGE_INFO_KEY == key) {
        PsiMethod method = getMethod();
        if (method != null) {
          sink.put(UsageView.USAGE_INFO_KEY, new UsageInfo(method));
        }
      }
    }
  }
}
