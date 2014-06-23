/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.move.moveInner.MoveInnerHandler;
import com.intellij.refactoring.move.moveInner.MoveInnerOptions;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.plugin.ij.actions.GosuTemplatesFactory;
import gw.plugin.ij.lang.psi.impl.GosuClassFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 */
public class MoveGosuInnerHandler implements MoveInnerHandler {
  @NotNull
  @Override
  public PsiClass copyClass( @NotNull final MoveInnerOptions options ) {
    PsiClass innerClass = options.getInnerClass();

    PsiClass newClass;
    if( options.getTargetContainer() instanceof PsiDirectory ) {
      //newClass = JavaDirectoryService.getInstance().createClass( (PsiDirectory)options.getTargetContainer(), options.getNewClassName() );
      String fileName = options.getNewClassName() + GosuClassTypeLoader.GOSU_CLASS_FILE_EXT;
      GosuClassFileImpl gsFile = (GosuClassFileImpl)GosuTemplatesFactory.createFromTemplate(
        (PsiDirectory)options.getTargetContainer(), options.getNewClassName(), fileName, GosuTemplatesFactory.GOSU_CLASS_TEMPLATE );
      newClass = gsFile.getClasses()[0];

      PsiDocComment defaultDocComment = newClass.getDocComment();
      if( defaultDocComment != null && innerClass.getDocComment() == null ) {
        innerClass = (PsiClass)innerClass.addAfter( defaultDocComment, null ).getParent();
      }

      newClass = (PsiClass)newClass.replace( innerClass );
      PsiUtil.setModifierProperty( newClass, PsiModifier.STATIC, false );
      PsiUtil.setModifierProperty( newClass, PsiModifier.PRIVATE, false );
      PsiUtil.setModifierProperty( newClass, PsiModifier.PROTECTED, false );
      final boolean makePublic = needPublicAccess( options.getOuterClass(), options.getTargetContainer() );
      if( makePublic ) {
        PsiUtil.setModifierProperty( newClass, PsiModifier.PUBLIC, true );
      }

      final PsiMethod[] constructors = newClass.getConstructors();
      for( PsiMethod constructor : constructors ) {
        final PsiModifierList modifierList = constructor.getModifierList();
        modifierList.setModifierProperty( PsiModifier.PRIVATE, false );
        modifierList.setModifierProperty( PsiModifier.PROTECTED, false );
        if( makePublic ) {
          modifierList.setModifierProperty( PsiModifier.PUBLIC, true );
        }
      }
    }
    else {
      newClass = (PsiClass)options.getTargetContainer().add( innerClass );
    }

    newClass.setName( options.getNewClassName() );

    return newClass;
  }

  protected static boolean needPublicAccess( @NotNull final PsiClass outerClass, final PsiElement targetContainer ) {
    if( outerClass.isInterface() ) {
      return true;
    }
    if( targetContainer instanceof PsiDirectory ) {
      final PsiPackage targetPackage = JavaDirectoryService.getInstance().getPackage( (PsiDirectory)targetContainer );
      final JavaPsiFacade psiFacade = JavaPsiFacade.getInstance( outerClass.getProject() );
      if( targetPackage != null && !psiFacade.isInPackage( outerClass, targetPackage ) ) {
        return true;
      }
    }
    return false;
  }
}
