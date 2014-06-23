/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.infos.CandidateInfo;
import com.intellij.psi.search.GlobalSearchScope;
import gw.plugin.ij.lang.psi.IGosuNamedElement;
import gw.plugin.ij.lang.psi.api.auxilary.IGosuModifierList;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMember;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.LightGosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;

public class GosuPsiImplUtil {
  private GosuPsiImplUtil() {
  }

  public static String getFileSource(@NotNull PsiFile file) {
    final PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(file.getProject()).getInjectionHost(file);
    if (host != null) {
      final LiteralTextEscaper<? extends PsiLanguageInjectionHost> escaper = host.createLiteralTextEscaper();
      final StringBuilder sb = new StringBuilder();
      escaper.decode(ElementManipulators.getValueTextRange(host), sb);
      return sb.toString();
    } else {
      return file.getText();
    }
  }


  @Nullable
  public static PsiElement getOriginalElement(@NotNull PsiClass clazz, @NotNull PsiFile containingFile) {
    VirtualFile vFile = containingFile.getVirtualFile();
    final ProjectFileIndex idx = ProjectRootManager.getInstance(containingFile.getProject()).getFileIndex();

    if (vFile == null || !idx.isInLibrarySource(vFile)) {
      return clazz;
    }
    final String qName = clazz.getQualifiedName();
    if (qName == null) {
      return null;
    }
    final List<OrderEntry> orderEntries = idx.getOrderEntriesForFile(vFile);
    PsiClass original = JavaPsiFacadeUtil.findClass(containingFile.getProject(), qName,
        new GlobalSearchScope(containingFile.getProject()) {
          public int compare(VirtualFile file1, VirtualFile file2) {
            return 0;
          }

          public boolean contains(@NotNull VirtualFile file) {
            // order for file and vFile has non empty intersection.
            List<OrderEntry> entries = idx.getOrderEntriesForFile(file);
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < entries.size(); i++) {
              final OrderEntry entry = entries.get(i);
              if (orderEntries.contains(entry)) {
                return true;
              }
            }
            return false;
          }

          public boolean isSearchInModuleContent(@NotNull Module aModule) {
            return false;
          }

          public boolean isSearchInLibraries() {
            return true;
          }
        });

    return original != null ? original : clazz;
  }

  public static PsiMethod[] mapToMethods(@Nullable List<CandidateInfo> list) {
    if (list == null) {
      return PsiMethod.EMPTY_ARRAY;
    }
    PsiMethod[] result = new PsiMethod[list.size()];
    for (int i = 0; i < list.size(); i++) {
      result[i] = (PsiMethod) list.get(i).getElement();

    }
    return result;
  }

  @Nullable
  public static String getName(@NotNull IGosuNamedElement namedElement) {
    final PsiElement nameElement = namedElement.getNameIdentifier();
    final ASTNode node = nameElement == null ? null : nameElement.getNode();
    if (node == null) {
      return "#empty";
    }

    String strName = nameElement.getText();
    if (nameElement instanceof LightGosuIdentifierImpl) {
      strName = ((LightGosuIdentifierImpl) nameElement).getName();
    }

    if (strName.isEmpty()) {
      if (namedElement instanceof GosuClassDefinitionImpl) {
        // E.g. return the relative name of the program
        return ((IGosuIdentifier) ((GosuBaseElementImpl) namedElement).getNameIdentifierImpl()).getName();
      }
      return "#empty";
    }
    return strName;
  }

  public static PsiElement setName(@NotNull PsiElement element, @NotNull String name) {
    PsiIdentifier newNameIdentifier = GosuPsiParseUtil.parseIdentifier(name, element);
    return element.replace(newNameIdentifier);
  }

  public static boolean isDeprecatedByAnnotation(@NotNull PsiModifierListOwner owner) {
    // Java
    if (PsiImplUtil.isDeprecatedByAnnotation(owner)) {
      return true;
    }

    // Gosu
    final PsiModifierList modifierList = owner.getModifierList();
    return modifierList != null && modifierList.findAnnotation("gw.lang.Deprecated") != null;
  }

  public static final PsiElement getSuperOrThisCall(PsiElement[] children) {
    if (children.length > 0) {
      PsiElement callExpr = findChildOfType(children[0], GosuMethodCallExpressionImpl.class);
      if (callExpr != null && (isCallOf("super", callExpr) || isCallOf("this", callExpr))) {
        return callExpr;
      }
    }
    return null;
  }

  private static boolean isCallOf(String name, PsiElement stmt) {
    if (stmt instanceof PsiReference) {
      return name.equals(((PsiReference) stmt).getCanonicalText());
    }
    return false;
  }

  public static PsiMethod findMethod(String mname, PsiMethod[] methods) {
    for (PsiMethod m : methods) {
      if (mname.equals(m.getName())) {
        return m;
      }
    }
    return null;
  }

  public static PsiElement getModifier(IGosuMember member, String modifierName) {
    IGosuModifierList modifierList = member.getModifierList();
    if (modifierList != null) {
      PsiElement[] modifiers = modifierList.getModifiers();
      if (modifiers != null) {
        for (PsiElement m : modifiers) {
          if (modifierName.equals(m.getText())) {
            return m;
          }
        }
      }
    }
    return null;
  }


}
