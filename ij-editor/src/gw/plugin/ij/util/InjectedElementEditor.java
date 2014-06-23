/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InjectedElementEditor {
  public static final String EDITOR = "_editor";
  public static final Key<PsiFile> ORIGINAL_PSI_FILE = new Key<>("ORIGINAL_PSI_FILE");
  public static final Key<IInjectedElementProvider> INJECTED_ELEMENT_PROVIDER = new Key<>("INJECTED_ELEMENT_PROVIDER");
  public static final Key<Boolean> SINGLE_LINE_EDITOR = new Key<>("SINGLE_LINE_EDITOR");

  public static PsiElement getHostInjectionXMLText(XmlTag parent, final String name) {
    final PsiElement[] psiElements = PsiTreeUtil.collectElements(parent, new PsiElementFilter() {
      @Override
      public boolean isAccepted(PsiElement element) {
        if (element instanceof XmlText) {
          final XmlTag parent = PsiTreeUtil.getParentOfType(element, XmlTag.class);
          if (parent != null && name.equals(parent.getName())) {
            return true;
          }
        }
        return false;
      }
    });
    return psiElements.length == 0 ? null : psiElements[0];
  }

  public static PsiElement getHostInjectionXMLAttr(XmlTag parent, String attrName) {
    final XmlAttribute attribute = parent.getAttribute(attrName);
    return attribute == null ? null : attribute.getValueElement();
  }

  public static PsiFile getInjectionPsiElement(Project project, PsiElement hostInjection) {
    final List<Pair<PsiElement, TextRange>> pairs = InjectedLanguageManager.getInstance(project).getInjectedPsiFiles(hostInjection);
    if (pairs != null) {
      for (Pair<PsiElement, TextRange> pair : pairs) {
        final VirtualFile injectedFile = pair.first.getContainingFile().getVirtualFile();
        final PsiFile injectedPsi = PsiManager.getInstance(project).findFile(injectedFile);
        return injectedPsi;
      }
    }
    return null;
  }


  public static boolean areInEquivalentFiles(PsiElement m1, PsiElement m2) {
    final String name1 = getContainingClass(m1).getName();
    final String name2 = getContainingClass(m2).getName();
    return name1.equals(name2 + EDITOR) || name2.equals(name1 + EDITOR);
  }

  public static boolean isInEmbeddedEditor(PsiElement element) {
    final PsiClass psiClass = getContainingClass(element);
    return psiClass != null &&
            psiClass.getName().endsWith(EDITOR) &&
            psiClass.getContainingFile().getVirtualFile() != null &&   //this is for copy for GosuIdentifier <name>IntellijRulezz
            psiClass.getContainingFile().getVirtualFile().getParent() == null; //virtual file parent is null for all light virtual files
  }

  @NotNull
  public static PsiFile getOriginalFile(@NotNull PsiFile file) {
    // TODO: before it was call to file.getContainingFile() but it should return this
    PsiFile originalFile = file.getUserData(ORIGINAL_PSI_FILE);
    if (originalFile == null) {
      final IInjectedElementProvider editor = file.getUserData(INJECTED_ELEMENT_PROVIDER);
      originalFile = editor != null ? editor.getInjectedFile() : null;
    }
    return originalFile != null ? originalFile : file;
  }

  @Nullable
  public static PsiElement getOriginalElement(@NotNull PsiElement element) {
    final PsiFile psiFile = element.getContainingFile();
    final PsiFile originalFile = getOriginalFile(psiFile);

    return originalFile != null ?
            originalFile.equals(psiFile) ?
                    element : originalFile.findElementAt(element.getTextOffset())
            : element;
  }


  @Nullable
  public static PsiClass getContainingClass(@Nullable PsiElement element) {
    while (element != null && !(element instanceof PsiClass)) {
      element = element.getParent();
    }
    return (PsiClass) element;
  }
}
