/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.application.ex.PathManagerEx;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ModuleRootModificationUtil;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.testFramework.PsiTestData;
import com.intellij.testFramework.PsiTestUtil;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.framework.SmartTextRange;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Mike
 */
public abstract class PsiTestCase extends ModuleTestCase {
  protected PsiManagerImpl myPsiManager;
  private String myDataRoot;

  @Override
  protected void beforeMethod() throws Exception {
    super.beforeMethod();
    myPsiManager = (PsiManagerImpl) PsiManager.getInstance(myProject);
  }

  @Override
  protected void afterMethod() throws Exception {
    myPsiManager = null;
    super.afterMethod();
  }

  protected PsiFile createDummyFile(String fileName, String text) throws IncorrectOperationException {
    FileType type = FileTypeRegistry.getInstance().getFileTypeByFileName(fileName);
    return PsiFileFactory.getInstance(myProject).createFileFromText(fileName, type, text);
  }

  protected PsiFile createFile(@NonNls String fileName, String text) throws Exception {
    return createFile(myModule, fileName, text);
  }
  protected PsiFile createFile(Module module, String fileName, String text) throws Exception {
    File dir = createTempDirectory();
    VirtualFile vDir = LocalFileSystem.getInstance().refreshAndFindFileByPath(dir.getCanonicalPath().replace(File.separatorChar, '/'));

    return createFile(module, vDir, fileName, text);
  }

  protected PsiFile createFile(final Module module, final VirtualFile vDir, final String fileName, final String text) throws IOException {
    return new WriteAction<PsiFile>() {
      @Override
      protected void run(Result<PsiFile> result) throws Throwable {
        if (!ModuleRootManager.getInstance(module).getFileIndex().isInSourceContent(vDir)) {
          addSourceContentToRoots(module, vDir);
        }

        final VirtualFile vFile = vDir.createChildData(vDir, fileName);
        VfsUtil.saveText(vFile, text);
        assertNotNull(vFile);
        final PsiFile file = myPsiManager.findFile(vFile);
        assertNotNull(file);
        result.setResult(file);
      }
    }.execute().getResultObject();
  }

  protected void addSourceContentToRoots(final Module module, final VirtualFile vDir) {
    PsiTestUtil.addSourceContentToRoots(module, vDir);
  }

  protected String getTestDataPath() {
    return PathManagerEx.getTestDataPath();
  }

  protected PsiTestData createData() {
    return new PsiTestData();
  }

  protected static void printText(String text) {
    final String q = "\"";
    System.out.print(q);

    text = StringUtil.convertLineSeparators(text);

    StringTokenizer tokenizer = new StringTokenizer(text, "\n", true);
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();

      if (token.equals("\n")) {
        System.out.print(q);
        System.out.println();
        System.out.print(q);
        continue;
      }

      System.out.print(token);
    }

    System.out.print(q);
    System.out.println();
  }

  protected void addLibraryToRoots(final VirtualFile jarFile, OrderRootType rootType) {
    addLibraryToRoots(myModule, jarFile, rootType);
  }

  protected static void addLibraryToRoots(final Module module, final VirtualFile root, final OrderRootType rootType) {
    assertEquals(OrderRootType.CLASSES, rootType);
    ModuleRootModificationUtil.addModuleLibrary(module, root.getUrl());
  }

  public com.intellij.openapi.editor.Document getDocument(PsiFile file) {
    return PsiDocumentManager.getInstance(getProject()).getDocument(file);
  }

  public com.intellij.openapi.editor.Document getDocument(VirtualFile file) {
    return FileDocumentManager.getInstance().getDocument(file);
  }

  public void commitDocument(com.intellij.openapi.editor.Document document) {
    PsiDocumentManager.getInstance(getProject()).commitDocument(document);
  }

//  public List<SmartTextRange> psis2Ranges(Collection<PsiReference> psis) {
//    List<SmartTextRange> ranges = new ArrayList<SmartTextRange>();
//    for (PsiReference psi : psis) {
//      ranges.add(new SmartTextRange(psi.getElement()));
//    }
//    return ranges;
//  }

  @NotNull
  public List<SmartTextRange> psis2Ranges(@NotNull List<PsiElement> psis) {
    List<SmartTextRange> ranges = new ArrayList<>();
    for (PsiElement psi : psis) {
      ranges.add(new SmartTextRange(psi));
    }
    return ranges;
  }
}
