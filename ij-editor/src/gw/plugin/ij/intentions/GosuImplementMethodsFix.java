/*
 * Copyright 2014 Guidewire Software, Inc.
 */


package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.codeInsight.GosuOverrideImplementUtil;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuImplementMethodsFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public GosuImplementMethodsFix(PsiElement aClass) {
    super(aClass);
  }

  @NotNull
  @Override
  public String getText() {
    return QuickFixBundle.message("implement.methods.fix");
  }

  @NotNull
  public String getFamilyName() {
    return QuickFixBundle.message("implement.methods.fix");
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    PsiElement myPsiElement = startElement;
    return myPsiElement.isValid() && myPsiElement.getManager().isInProject(myPsiElement);
  }

  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable("is null when called from inspection") final Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    final PsiElement myPsiElement = startElement;
     if (editor == null || !CodeInsightUtilBase.prepareFileForWrite(myPsiElement.getContainingFile())) {
      return;
    }

    if (myPsiElement instanceof PsiEnumConstant) {
      throw new UnsupportedOperationException("men at work");
//      FeatureUsageTracker.getInstance().triggerFeatureUsed(ProductivityFeatureNames.CODEASSISTS_OVERRIDE_IMPLEMENT);
//      final TreeMap<MethodSignature, CandidateInfo> result =
//          new TreeMap<MethodSignature, CandidateInfo>(new OverrideImplementUtil.MethodSignatureComparator());
//      final HashMap<MethodSignature, PsiMethod> abstracts = new HashMap<MethodSignature, PsiMethod>();
//      for (PsiMethod method : ((PsiEnumConstant) myPsiElement).getContainingClass().getMethods()) {
//        if (method.hasModifierProperty(PsiModifier.ABSTRACT)) {
//          abstracts.put(method.getHierarchicalMethodSignature(), method);
//        }
//      }
//      final HashMap<MethodSignature, PsiMethod> finals = new HashMap<MethodSignature, PsiMethod>();
//      final HashMap<MethodSignature, PsiMethod> concretes = new HashMap<MethodSignature, PsiMethod>();
//      OverrideImplementUtil.collectMethodsToImplement(null, abstracts, finals, concretes, result);
//
//      final MemberChooser<PsiMethodMember> chooser =
//          OverrideImplementUtil.showOverrideImplementChooser(editor, myPsiElement, true, result.values(), Collections.<CandidateInfo>emptyList());
//      if (chooser == null) {
//        return;
//      }
//
//      final List<PsiMethodMember> selectedElements = chooser.getSelectedElements();
//      if (selectedElements == null || selectedElements.isEmpty()) {
//        return;
//      }
//
//      new WriteCommandAction(project, file) {
//        protected void run(final Result result) throws Throwable {
//          final PsiClass psiClass = ImplementAbstractMethodHandler.addClassInitializer((PsiEnumConstant) myPsiElement);
//          OverrideImplementUtil.overrideOrImplementMethodsInRightPlace(editor, psiClass, selectedElements, chooser.isCopyJavadoc(),
//              chooser.isInsertOverrideAnnotation());
//        }
//      }.execute();
    } else {
      PsiClass aClass = findOwningPsiClass(myPsiElement);
      if(aClass instanceof IGosuTypeDefinition) {
        IGosuTypeDefinition gosuPsiClass = (IGosuTypeDefinition) aClass;
        GosuOverrideImplementUtil.invokeOverrideImplement(editor, gosuPsiClass, true);
      }
    }

  }

  @NotNull
  private PsiClass findOwningPsiClass(PsiElement element) {
    PsiElement current = element;
    while(current != null && !(current instanceof PsiClass)) {
      current = current.getParent();
    }
    return (PsiClass) current;
  }

  public boolean startInWriteAction() {
    return false;
  }

}
