/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiPackage;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mike
 */
public class CreateClassFix extends CreateClassBaseFix implements ITestableCreateClassFix {

  public CreateClassFix(@NotNull GosuTypeLiteralImpl refElement, GosuCreateClassKind kind) {
    super(kind, refElement);
  }

  public String getText(String varName) {
    return QuickFixBundle.message("create.class.from.usage.text", StringUtil.capitalize(myKind.getDescription()), varName);
  }


  public void invokeImpl(@NotNull final Project project, final Editor editor, final PsiFile file) {
    final GosuTypeLiteralImpl element = getRefElement();
    assert element != null;
    if (!CodeInsightUtilBase.preparePsiElementForWrite(element)) return;
    final PsiClass aClass = GosuCreateFromUsageUtils.createClass(element, myKind, null);
    if (aClass == null) return;

    ApplicationManager.getApplication().runWriteAction(
      new Runnable() {
        public void run() {
          GosuTypeLiteralImpl refElement = element;
          try {
            refElement = (GosuTypeLiteralImpl)refElement.bindToElement(aClass);
          }
          catch (IncorrectOperationException e) {
            LOG.error(e);
          }

          IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();

          OpenFileDescriptor descriptor = new OpenFileDescriptor(refElement.getProject(), aClass.getContainingFile().getVirtualFile(),
                                                                 aClass.getTextOffset());
          FileEditorManager.getInstance(aClass.getProject()).openTextEditor(descriptor, true);
        }
      }
    );
  }

  public boolean startInWriteAction() {
    return false;
  }

  @Override
  public void invokeForTest(@NotNull String packageName) {
    final GosuTypeLiteralImpl referenceElement = getRefElement();
    assert referenceElement != null;
    if (!CodeInsightUtilBase.preparePsiElementForWrite(referenceElement)) return;
    assert ApplicationManager.getApplication().isWriteAccessAllowed();
    final PsiPackage pkg = JavaPsiFacadeUtil.findPackage(referenceElement.getProject(), packageName);
    final PsiDirectory dir = pkg.getDirectories()[0];
    final PsiClass aClass = GosuCreateFromUsageUtils.createClass(GosuCreateClassKind.GOSU_CLASS, dir,
        referenceElement.getReferenceName(), referenceElement.getManager(), referenceElement, referenceElement.getContainingFile(), null);
    if (aClass == null) return;

//    ApplicationManager.getApplication().runWriteAction(
//      new Runnable() {
//        public void run() {
//          GosuTypeLiteralImpl refElement = referenceElement;
//          try {
//            refElement = (GosuTypeLiteralImpl)refElement.bindToElement(aClass);
//          }
//          catch (IncorrectOperationException e) {
//            LOG.error(e);
//          }
//
//          IdeDocumentHistory.getInstance(project).includeCurrentPlaceAsChangePlace();
//
//          OpenFileDescriptor descriptor = new OpenFileDescriptor(refElement.getProject(), aClass.getContainingFile().getVirtualFile(),
//                                                                 aClass.getTextOffset());
//          FileEditorManager.getInstance(aClass.getProject()).openTextEditor(descriptor, true);
//        }
//      }
//    );
  }
}
