/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.util.PackageUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.framework.GosuTestCase;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class CreateProgramActionTest extends GosuTestCase {

  public void testProgramCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestProgramOne";
      final String templateName = "GosuProgram.gsp";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile("TestProgramOne.gsp");
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = new String(content);
      assertEquals("", "", result.trim());
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testDuplicateProgramCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestProgramUnique";
      final String templateName = "GosuProgram.gsp";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, templateName);
            action.runTest();
            action.runTest();  // run create a second time on purpose
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue("Unexpected message: " + caught[0].getMessage(), caught[0].getMessage().contains(" already exists"));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testProgramCreationWithJavaConflict() {
    {
      try {
        File testRootDir = createTempDirectory();
        final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
        final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
        final String className = "TestProgramUnique";
        final String templateName = "GosuProgram.gsp";
        final Throwable[] caught = new Throwable[1];
        caught[0] = null;

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          @Override
          public void run() {
            try {
              TestAction action = new TestAction(pkgDir, className, templateName);
              pkgDir.createFile(className + ".java");
              action.runTest();
            } catch (Throwable t) {
              caught[0] = t;
            }
          }
        });
        assertNotNull(caught[0]);
        assertTrue(caught[0] instanceof IncorrectOperationException);
        assertTrue(caught[0].getMessage().contains("Cannot create TestProgramUnique.gsp. A Java class with the same name already exists."));
      } catch (Exception e) {
        fail(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public void testProgramCreationWithTypeConflict() {
    {
      try {
        File testRootDir = createTempDirectory();
        final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
        final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
        final String className = "ProgramClassNameConflict";
        final String templateName = "GosuProgram.gsp";
        final Throwable[] caught = new Throwable[1];
        caught[0] = null;

        configureByText("test/pkg/ProgramClassNameConflict.gs", "package test.pkg class ProgramClassNameConflict{}");

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          @Override
          public void run() {
            try {
              TestAction action = new TestAction(pkgDir, className, templateName);
              action.runTest();
            } catch (Throwable t) {
              caught[0] = t;
            }
          }
        });
        assertNotNull(caught[0]);
        assertTrue(caught[0] instanceof IncorrectOperationException);
        assertTrue(caught[0].getMessage().contains("Cannot create ProgramClassNameConflict.gsp. A type with the same name already exists."));
      } catch (Exception e) {
        fail(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private class TestAction extends CreateProgramAction {

    @Nullable
    PsiDirectory _dir = null;
    @Nullable
    String _className = null;
    @Nullable
    String _templateName = null;

    public TestAction(PsiDirectory dir, String className, String templateName) {
      super();
      _dir = dir;
      _className = className;
      _templateName = templateName;

    }

    public void runTest() {
      checkBeforeCreate(_className, _templateName, _dir);
      createFile(_className, _templateName, _dir);
    }
  }
}
