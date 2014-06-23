/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenameHandler;

/**
 */
public class GosuMemberInplaceRenameHandler extends MemberInplaceRenameHandler {
  @Override
  protected boolean isAvailable( PsiElement element, Editor editor, PsiFile file ) {
    if( !GosuRefactoringSupportProvider.isInplaceSupported( element ) ||
        !GosuRefactoringSupportProvider.isInplaceSupported( file ) ) {
      // PL-20646
      // Don't support in-place refactoring of a Java getter/setter method *from* inside a Gosu file,
      // it's messed up wrt the property name. The dialog-based refactor works.
      return false;
    }
    return super.isAvailable( element, editor, file );
  }
}
