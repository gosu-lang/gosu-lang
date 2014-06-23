/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.doc;

import com.intellij.codeInsight.javadoc.JavaDocExternalFilter;
import com.intellij.codeInsight.javadoc.JavaDocInfoGenerator;
import com.intellij.codeInsight.javadoc.JavaDocUtil;
import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.java.JavaDocumentationProvider;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GosuDocumentationProvider implements DocumentationProvider {
  private static final Pattern ourTypePattern = Pattern.compile("[ ]+[^ ^\\[^\\]]");

  @Override
  public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    return null;
  }

  @Override
  public List<String> getUrlFor(PsiElement element, PsiElement originalElement) {
    return JavaDocumentationProvider.getExternalJavaDocUrl(element);
  }

  @Override
  public String generateDoc(@Nullable PsiElement element, PsiElement originalElement) {
    if (element != null) {
      final List<String> docURLs = JavaDocumentationProvider.getExternalJavaDocUrl(element);
      final GosuDocInfoGenerator javaDocInfoGenerator = new GosuDocInfoGenerator(element.getProject(), element);
      return JavaDocExternalFilter.filterInternalDocInfo(javaDocInfoGenerator.generateDocInfo(docURLs));
    }
    return null;
  }

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    return null;
  }

  @Override
  public PsiElement getDocumentationElementForLink(@NotNull PsiManager manager, @NotNull String refText, @NotNull PsiElement context) {
    int poundIndex = refText.indexOf('#');
    final JavaPsiFacade facade = JavaPsiFacade.getInstance(manager.getProject());
    if (poundIndex < 0) {
      PsiClass aClass = facade.getResolveHelper().resolveReferencedClass(refText, context);

      if (aClass == null) {
        aClass = facade.findClass(refText, context.getResolveScope());
      }

      if (aClass != null) {
        return aClass.getNavigationElement();
      }
      PsiPackage aPackage = facade.findPackage(refText);
      if (aPackage != null) {
        return aPackage;
      }
      return null;
    } else {
      String classRef = refText.substring(0, poundIndex).trim();
      if (classRef.length() > 0) {
        PsiClass aClass = facade.getResolveHelper().resolveReferencedClass(classRef, context);

        if (aClass == null) {
          aClass = facade.findClass(classRef, context.getResolveScope());
        }

        if (aClass == null) {
          return null;
        }
        return findReferencedMember(aClass, refText.substring(poundIndex + 1), context);
      } else {
        String memberRefText = refText.substring(1);
        PsiElement scope = context;
        while (true) {
          if (scope instanceof PsiFile) {
            break;
          }
          if (scope instanceof PsiClass) {
            PsiElement member = findReferencedMember((PsiClass) scope, memberRefText, context);
            if (member != null) {
              return member;
            }
          }
          scope = scope.getParent();
        }
        return null;
      }
    }
  }

  @Nullable
  private static PsiElement findReferencedMember(@NotNull PsiClass aClass, @NotNull String memberRefText, PsiElement context) {
    int parenthIndex = memberRefText.indexOf('(');
    if (parenthIndex < 0) {
      final PsiField field = aClass.findFieldByName(memberRefText, true);
      if (field != null) {
        return field.getNavigationElement();
      }

      final PsiClass inner = aClass.findInnerClassByName(memberRefText, true);
      if (inner != null) {
        return inner.getNavigationElement();
      }

      for (PsiMethod method : aClass.findMethodsByName(memberRefText, true)) {
        return method.getNavigationElement();
      }

      return null;
    } else {
      String name = memberRefText.substring(0, parenthIndex).trim();
      int rparenIndex = memberRefText.lastIndexOf(')');
      if (rparenIndex == -1) {
        return null;
      }

      String parmsText = memberRefText.substring(parenthIndex + 1, rparenIndex).trim();
      StringTokenizer tokenizer = new StringTokenizer(parmsText.replaceAll("[*]", ""), ",");
      PsiType[] types = new PsiType[tokenizer.countTokens()];
      int i = 0;
      PsiElementFactory factory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();
      while (tokenizer.hasMoreTokens()) {
        String parmText = tokenizer.nextToken().trim();
        try {
          Matcher typeMatcher = ourTypePattern.matcher(parmText);
          String typeText = parmText;

          if (typeMatcher.find()) {
            typeText = parmText.substring(0, typeMatcher.start());
          }

          PsiType type = factory.createTypeFromText(typeText, context);
          types[i++] = type;
        } catch (IncorrectOperationException e) {
          throw new RuntimeException(e);
        }
      }
      PsiMethod[] methods = aClass.findMethodsByName(name, true);
      MethodsLoop:
      for (PsiMethod method : methods) {
        PsiParameter[] parms = method.getParameterList().getParameters();
        if (parms.length != types.length) {
          continue;
        }

        for (int k = 0; k < parms.length; k++) {
          PsiParameter parm = parms[k];
          if (
              types[k] != null &&
                  !TypeConversionUtil.erasure(parm.getType()).getCanonicalText().equals(types[k].getCanonicalText()) &&
                  !parm.getType().getCanonicalText().equals(types[k].getCanonicalText())
              ) {
            continue MethodsLoop;
          }
        }

        int hashIndex = memberRefText.indexOf('#', rparenIndex);
        if (hashIndex != -1) {
          int parameterNumber = Integer.parseInt(memberRefText.substring(hashIndex + 1));
          if (parameterNumber < parms.length) {
            return method.getParameterList().getParameters()[parameterNumber].getNavigationElement();
          }
        }
        return method.getNavigationElement();
      }
      return null;
    }
  }
}
