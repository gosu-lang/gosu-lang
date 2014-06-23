/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.daemon.impl.quickfix.ImportClassFixBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMember;
import com.intellij.psi.util.ClassUtil;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.handlers.filter.CompletionFilter;
import gw.plugin.ij.completion.handlers.filter.CompletionFilterExtensionPointBean;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GosuAddMissingUsesFix extends ImportClassFixBase<IGosuCodeReferenceElement, IGosuCodeReferenceElement> {

  private final IGosuCodeReferenceElement ref;

  public GosuAddMissingUsesFix(@NotNull IGosuCodeReferenceElement ref) {
    super(ref, ref);
    this.ref = ref;
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiFile file) {
    return true;
  }

  @Override
  protected String getReferenceName(@NotNull IGosuCodeReferenceElement reference) {
    return reference.getReferenceName();
  }

  @Nullable
  @Override
  protected PsiElement getReferenceNameElement(IGosuCodeReferenceElement reference) {
    return null;
  }

  @Override
  protected boolean hasTypeParameters(@NotNull IGosuCodeReferenceElement reference) {
    return reference.getTypeArguments().length > 0;
  }

  @Override
  protected boolean isAccessible(PsiMember psiMember, IGosuCodeReferenceElement iGosuCodeReferenceElement) {
    return true;
  }

  @NotNull
  @Override
  protected String getQualifiedName(@NotNull IGosuCodeReferenceElement reference) {
    return reference.getCanonicalText();
  }

  @Override
  protected boolean isQualified(@NotNull IGosuCodeReferenceElement reference) {
    return reference.getQualifier() != null;
  }

  @Override
  protected boolean hasUnresolvedImportWhichCanImport(PsiFile psiFile, String name) {
//    if (!(psiFile instanceof IGosuFile)) return false;
//    final IGosuUsesStatement[] importStatements = ((IGosuFile)psiFile).getUsesStatements();
//    for (IGosuUsesStatement importStatement : importStatements) {   n
//      final IGosuCodeReferenceElement importReference = importStatement.getImportReference();
//      if (importReference == null || importReference.resolve() != null) {
//        continue;
//      }
//      return true;
//    }
    return false;
  }

  @NotNull
  @Override
  public List<PsiClass> getClassesToImport() {
    List<PsiClass> classes = new ArrayList<>();
    classes.addAll(super.getClassesToImport());
    IModule ctxModule = GosuModuleUtil.findModuleForPsiElement(ref);
    if (ctxModule == null) {
      ctxModule = TypeSystem.getGlobalModule();
    }

    String referenceName = ref.getReferenceName();
    Set<String> fqns = new HashSet<>();
    for (PsiClass pc : classes) {
      fqns.add(pc.getQualifiedName());
    }

    for (IModule module : ctxModule.getModuleTraversalList()) {
      for (ITypeLoader loader : module.getModuleTypeLoader().getTypeLoaderStack()) {
        if (!(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader)) {
          TypeSystem.pushModule(module);
          try {
            for (CharSequence typeName : loader.getAllTypeNames()) {
              String fqn = typeName.toString();
              if (!fqns.add(fqn)) {
                continue;
              }
              String simpleName = ClassUtil.extractClassName(fqn);
              if (simpleName.equals(referenceName)) {
                IType type = TypeSystem.getByFullNameIfValid(fqn, loader.getModule());
                if (type != null) {
                  PsiClass psiClass = CustomPsiClassCache.instance().getPsiClass(type);
                  if (psiClass != null) {
                    classes.add(psiClass);
                  }
                }
              }
            }
          } finally {
            TypeSystem.popModule(module);
          }
        }
      }
    }
    filter(classes);
    return classes;
  }

  private void filter(List<PsiClass> classes) {
    for (Iterator<PsiClass> it = classes.iterator(); it.hasNext();) {
      PsiClass cl = it.next();
      List<CompletionFilter> filters = CompletionFilterExtensionPointBean.getFilters();
      for (CompletionFilter filter : filters) {
        if (!filter.allowsImportInsertion(cl.getQualifiedName())) {
          it.remove();
        }
      }
    }
  }
}
