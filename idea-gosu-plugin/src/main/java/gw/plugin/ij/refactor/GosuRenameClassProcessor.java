/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.codeInsight.ChangeContextUtil;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.JavaTokenType;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.PsiImplUtil;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.ClassHidesImportedClassUsageInfo;
import com.intellij.refactoring.rename.CollidingClassImportUsageInfo;
import com.intellij.refactoring.rename.RenameJavaClassProcessor;
import com.intellij.refactoring.rename.ResolvableCollisionUsageInfo;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import gw.config.CommonServices;
import gw.lang.reflect.TypeSystem;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GosuRenameClassProcessor extends RenameJavaClassProcessor {

  @Override
  public void renameElement(PsiElement element, String newName, UsageInfo[] usages, RefactoringElementListener listener) throws IncorrectOperationException {
    PsiClass aClass = (PsiClass) element;
    if( aClass.getName().equals( newName ) ) {
      // Same name, nothing to do
      return;
    }

    ArrayList<UsageInfo> postponedCollisions = new ArrayList<>();

    // rename all references
    for (final UsageInfo usage : usages) {
      if (usage instanceof ResolvableCollisionUsageInfo) {
        if (usage instanceof CollidingClassImportUsageInfo) {
          ((CollidingClassImportUsageInfo)usage).getImportStatement().delete();
        }
        else {
          postponedCollisions.add(usage);
        }
      }
    }

    String path = aClass.getContainingFile().getViewProvider().getVirtualFile().getPath();
    path = path.replace("/" + aClass.getName() + ".", "/" + newName + ".");
    File ioFIle = new File(path);
    boolean bCreatedNewFile = false;
    try {
      // Note this won't create a new file on Windows if the name is different only by case.
      // In this situation we don't need to create another file since internally we are able
      // to resolve a type name based on a file case-insensitively (on Windows).
      bCreatedNewFile = ioFIle.createNewFile();
      if( bCreatedNewFile ) {
        TypeSystem.created(CommonServices.getFileSystem().getIFile(ioFIle));
        StringBuilder source = generateSource(aClass, newName);
        FileUtil.writeToFile(ioFIle, source.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // do actual rename
    ChangeContextUtil.encodeContextInfo(aClass, true, false);
    boolean isRenameFile = isRenameFileOnRenaming(aClass);
    setName(aClass, newName);

    for (UsageInfo usage : usages) {
      if (!(usage instanceof ResolvableCollisionUsageInfo)) {
        final PsiReference ref = usage.getReference();
        if (ref == null) continue;
        try {
          ref.bindToElement(aClass);
        }
        catch (IncorrectOperationException e) {//fall back to old scheme
          ref.handleElementRename(newName);
        }
      }
    }

    if( bCreatedNewFile ) {
      ioFIle.delete();
    }

    if (isRenameFile) {
      PsiFile file = (PsiFile)aClass.getParent();
      String fileName = file.getName();
      int dotIndex = fileName.lastIndexOf('.');
      file.setName(dotIndex >= 0 ? newName + "." + fileName.substring(dotIndex + 1) : newName);
    }

    ChangeContextUtil.decodeContextInfo(aClass, null, null); //to make refs to other classes from this one resolve to their old referent

    // resolve collisions
    for (UsageInfo postponedCollision : postponedCollisions) {
      ClassHidesImportedClassUsageInfo collision = (ClassHidesImportedClassUsageInfo) postponedCollision;
      collision.resolveCollision();
    }

    listener.elementRenamed(aClass);
  }

  private StringBuilder generateSource(PsiClass aClass, String newName) {
    StringBuilder source = new StringBuilder();
    generateSource(aClass.getContainingFile().getNode(), aClass.getName(), newName, source);
    return source;
  }

  private void generateSource(ASTNode node, String oldName, String newName, StringBuilder source) {
    if (node instanceof LeafElement) {
      String text = node.getText();
      if (node.getElementType() == JavaTokenType.IDENTIFIER && text.equals(oldName)) {
        PsiElement psi = node.getPsi().getParent();
        if (psi instanceof PsiClass) {
          text = newName;
        } else if (psi instanceof PsiMethod && ((PsiMethod) psi).getName().equals(oldName)) {
          text = newName;
        } else if (psi instanceof PsiReference) {
          PsiElement resolve = ((PsiReference) psi).resolve();
          if (resolve instanceof PsiClass && ((PsiClass) resolve).getName().equals(oldName)) {
            text = newName;
          }
        }
      }
      source.append(text);
    } else {
      for (ASTNode child : node.getChildren(null)) {
        generateSource(child, oldName, newName, source);
      }
    }
  }

  public PsiElement setName(PsiClass aClass, @NotNull String newName) throws IncorrectOperationException{
    String oldName = aClass.getName();

    PsiIdentifier nameIdentifier = aClass.getNameIdentifier();
    if (nameIdentifier instanceof IGosuIdentifier) {
      GosuPsiParseUtil.setName(nameIdentifier, newName);
    } else {
      PsiImplUtil.setName(nameIdentifier, newName);
    }
    // This is the Java implementation
    //PsiImplUtil.setName(aClass.getNameIdentifier(), newName);

    // rename constructors
    for (PsiMethod method : aClass.getConstructors()) {
      if (method.getName().equals(oldName)) {
        method.setName(newName);
      }
    }

    return aClass;
  }

  private boolean isRenameFileOnRenaming(PsiClass aClass) {
    final PsiElement parent = aClass.getParent();
    if (parent instanceof PsiFile) {
      PsiFile file = (PsiFile)parent;
      String fileName = file.getName();
      int dotIndex = fileName.lastIndexOf('.');
      String name = dotIndex >= 0 ? fileName.substring(0, dotIndex) : fileName;
      String oldName = aClass.getName();
      return name.equals(oldName);
    }
    else {
      return false;
    }
  }

}
