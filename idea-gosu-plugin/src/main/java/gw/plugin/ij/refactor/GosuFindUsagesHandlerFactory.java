/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.JavaFindUsagesHandler;
import com.intellij.find.findUsages.JavaFindUsagesHandlerFactory;
import com.intellij.ide.util.SuperMethodWarningUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

public class GosuFindUsagesHandlerFactory extends JavaFindUsagesHandlerFactory {

  public GosuFindUsagesHandlerFactory(Project project) {
    super(project);
  }

//  @Override
//  public FindUsagesHandler createFindUsagesHandler(@NotNull PsiElement element, boolean forHighlightUsages) {
//    if (element instanceof PsiDirectory) {
//      final PsiPackage psiPackage = JavaDirectoryService.getInstance().getPackage((PsiDirectory)element);
//      return psiPackage == null ? null : new GosuFindUsagesHandler(psiPackage, this);
//    }
//
//    if (element instanceof PsiMethod && !forHighlightUsages) {
//      final PsiMethod[] methods = SuperMethodWarningUtil.checkSuperMethods((PsiMethod) element, JavaFindUsagesHandler.ACTION_STRING);
//      if (methods.length > 1) {
//        return new GosuFindUsagesHandler(element, methods, this);
//      }
//      if (methods.length == 1) {
//        return new GosuFindUsagesHandler(methods[0], this);
//      }
//      return FindUsagesHandler.NULL_HANDLER;
//    }
//
//    return new GosuFindUsagesHandler(element, this);
//  }
}
