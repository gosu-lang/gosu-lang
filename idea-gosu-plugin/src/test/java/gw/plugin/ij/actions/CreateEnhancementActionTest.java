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

public class CreateEnhancementActionTest extends GosuTestCase {

  public void testEnhanceJavaClass() {

    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "CalendarEnhancement";
      final String enhancedClassName = "java.util.Calendar";
      final String templateName = "GosuEnhancement.gsx";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile("CalendarEnhancement.gsx");
      if (pfile == null) {
        fail("File CalendarEnhancement.gsx did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\nenhancement CalendarEnhancement : java.util.Calendar {\n" +
          "\n" +
          "}\n", result);

    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testEnhanceGosuClass() {

    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "SomeEnhancement";
      final String enhancedClassName = "some.pkg.SomeClass";
      final String templateName = "GosuEnhancement.gsx";

      configureByText("some/pkg/SomeClass.gs", "package some.pkg class SomeClass{}");

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile("SomeEnhancement.gsx");
      if (pfile == null) {
        fail("File SomeEnhancement.gsx did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\nenhancement SomeEnhancement : some.pkg.SomeClass {\n" +
          "\n" +
          "}\n", result);

    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testEnhancementCreationWithInvalidClass() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "EnhancementWithInvalidClass";
      final String enhancedClassName = "java.util.ThisClassIsInvalid";
      final String templateName = "GosuEnhancement.gsx";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
            action.runTest();
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("Cannot create enhancement for invalid type : java.util.ThisClassIsInvalid"));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testDuplicateEnhancementCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "ListEnhancement";
      final String enhancedClassName = "List";
      final String templateName = "GosuEnhancement.gsx";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
            action.runTest();
            action.runTest(); // run a second time on purpose
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains(" already exists"));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testEnhancementCreationWithJavaConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "ListEnhancement";
      final String enhancedClassName = "List";
      final String templateName = "GosuEnhancement.gsx";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
            pkgDir.createFile(className + ".java");
            action.runTest();
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("Cannot create ListEnhancement.gsx. A Java class with the same name already exists."));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testEnhancementCreationWithTypeConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "EnhancementProgramNameConflict";
      final String enhancedClassName = "List";
      final String templateName = "GosuEnhancement.gsx";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, enhancedClassName, templateName);
            pkgDir.createFile(className + ".gsp");
            action.runTest();
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("Cannot create EnhancementProgramNameConflict.gsx. A type with the same name already exists."));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  private class TestAction extends CreateEnhancementAction {
    @Nullable
    PsiDirectory _dir = null;
    @Nullable
    String _className = null;
    @Nullable
    String _enhancedClassName = null;
    @Nullable
    String _templateName = null;

    public TestAction(PsiDirectory dir, String className, String enhancedClassName, String templateName) {
      super();
      _dir = dir;
      _className = className;
      _enhancedClassName = enhancedClassName;
      _templateName = templateName;

    }

    public void runTest() {
      checkBeforeCreate(_className, _enhancedClassName, _templateName, _dir);
      createFile(_className, _enhancedClassName, _templateName, _dir);
    }
  }
}
