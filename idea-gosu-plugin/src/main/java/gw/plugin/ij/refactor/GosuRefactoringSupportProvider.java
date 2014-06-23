/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.lang.java.JavaRefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.changeSignature.ChangeSignatureHandler;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBlockExpressionImpl;
import gw.plugin.ij.lang.psi.stubs.elements.GosuStubFileElementType;
import gw.plugin.ij.refactor.intoduceField.GosuIntroduceConstantHandler;
import gw.plugin.ij.refactor.intoduceField.GosuIntroduceFieldHandler;
import gw.plugin.ij.refactor.introduceVariable.GosuIntroduceVariableHandler;
import gw.plugin.ij.refactor.signature.GosuChangeSignatureHandler;
import org.jetbrains.annotations.Nullable;

public class GosuRefactoringSupportProvider extends JavaRefactoringSupportProvider {

  @Override
  public boolean isInplaceRenameAvailable(PsiElement element, PsiElement context) {
    if (!isInplaceSupported(element, context)) {
      return false; // disabled inplace refactoring for file that is skipping parsing during edits
    }
    if (element != null && (element.getContainingFile() instanceof GosuProgramFileImpl ||
            context.getContainingFile() instanceof GosuProgramFileImpl ||
            PsiTreeUtil.getParentOfType(context, GosuBlockExpressionImpl.class) != null ||
            PsiTreeUtil.getParentOfType(element, GosuBlockExpressionImpl.class) != null)) {
      return false; //disabled inplace refactoring for gosu programs
    }
    return super.isInplaceRenameAvailable(element, context);
  }

  @Override
  public boolean isMemberInplaceRenameAvailable(PsiElement element, PsiElement context) {
    if (!isInplaceSupported(element, context)) {
      return false; // disabled inplace refactoring for file that is skipping parsing during edits
    }
    if (element != null && (element.getContainingFile() instanceof GosuProgramFileImpl ||
            context.getContainingFile() instanceof GosuProgramFileImpl ||
            PsiTreeUtil.getParentOfType(context, GosuBlockExpressionImpl.class) != null ||
            PsiTreeUtil.getParentOfType(element, GosuBlockExpressionImpl.class) != null)) {
      return false; //disabled inplace refactoring for gosu programs
    }
    return super.isMemberInplaceRenameAvailable(element, context);
  }

  @Override
  public boolean isInplaceIntroduceAvailable(PsiElement element, PsiElement context) {
    if (!isInplaceSupported(element, context)) {
      return false; // disabled inplace refactoring for file that is skipping parsing during edits
    }
    if (element != null && element.getContainingFile() instanceof GosuProgramFileImpl) {
      return false; //disabled inplace refactoring for gosu programs
    }
    return super.isInplaceIntroduceAvailable(element, context);
  }

  private boolean isInplaceSupported(PsiElement element, PsiElement context) {
    return isInplaceSupported(element) && isInplaceSupported(context);
  }

  public static boolean isInplaceSupported(PsiElement context) {
    if (context != null) {
      PsiFile psiFile = context.getContainingFile();
      if (psiFile instanceof AbstractGosuClassFileImpl) {
        IElementType elementType = ((AbstractGosuClassFileImpl) psiFile).getTreeElement().getElementType();
        if (elementType instanceof GosuStubFileElementType) {
          return !((GosuStubFileElementType) elementType).isSkipParsingWhileTyping();
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public RefactoringActionHandler getIntroduceConstantHandler() {
    return new GosuIntroduceConstantHandler();
  }

  @Override
  public RefactoringActionHandler getIntroduceFieldHandler() {
    return new GosuIntroduceFieldHandler();
  }

  @Override
  public RefactoringActionHandler getIntroduceVariableHandler() {
    return new GosuIntroduceVariableHandler();
  }

  @Override
  @Nullable
  public RefactoringActionHandler getExtractMethodHandler() {
    if (true) {
      return null;
    }
    return super.getExtractMethodHandler();
  }

  @Override
  public RefactoringActionHandler getIntroduceParameterHandler() {
    if (true) {
      return null;
    }
    return super.getIntroduceParameterHandler();
  }

  @Override
  public RefactoringActionHandler getPullUpHandler() {
    if (true) {
      return null;
    }
    return super.getPullUpHandler();
  }

  @Override
  public RefactoringActionHandler getPushDownHandler() {
    if (true) {
      return null;
    }
    return super.getPushDownHandler();
  }

  @Override
  public RefactoringActionHandler getExtractInterfaceHandler() {
    if (true) {
      return null;
    }
    return super.getExtractInterfaceHandler();
  }

  @Override
  public RefactoringActionHandler getExtractSuperClassHandler() {
    if (true) {
      return null;
    }
    return super.getExtractSuperClassHandler();
  }

  @Override
  public ChangeSignatureHandler getChangeSignatureHandler() {
    return new GosuChangeSignatureHandler();
  }

  @Override
  public RefactoringActionHandler getExtractClassHandler() {
    if (true) {
      return null;
    }
    return super.getExtractClassHandler();
  }
}
