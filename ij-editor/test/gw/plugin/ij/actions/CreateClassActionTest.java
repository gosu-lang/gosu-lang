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
import gw.testharness.Disabled;
import gw.testharness.KnownBreak;
import org.jetbrains.annotations.Nullable;

import java.io.File;


public class CreateClassActionTest extends GosuTestCase {

  public void testClassCreation() {

    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory( getVirtualFile( testRootDir ));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestClassOne";
      final String templateName = "GosuClass.gs";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile( "TestClassOne.gs" );
      if (pfile == null) {
        fail("File TestClassOne.gs did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\n" +
          "class TestClassOne {\n" +
          "\n" +
          "}", result);

     } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testInterfaceCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory( getVirtualFile( testRootDir ));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestInterfaceOne";
      final String templateName = "GosuInterface.gs";
      final String expectedFileName = className + ".gs";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile( expectedFileName );
      if (pfile == null) {
        fail("File " + expectedFileName + " did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\n" +
          "interface TestInterfaceOne {\n" +
          "\n" +
          "}", result);
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

   public void testEnumCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory( getVirtualFile( testRootDir ));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestEnumOne";
      final String templateName = "GosuEnum.gs";
      final String expectedFileName = className + ".gs";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile( expectedFileName );
      if (pfile == null) {
        fail("File " + expectedFileName + " did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\n" +
          "enum TestEnumOne {\n" +
          "\n" +
          "}", result);

     } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testAnnotationCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory( getVirtualFile( testRootDir ));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestAnnotationOne";
      final String templateName = "GosuAnnotation.gs";
      final String expectedFileName = className + ".gs";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile( expectedFileName );
      if (pfile == null) {
        fail("File " + expectedFileName + " did not get created. Check log file for details.");
      }
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = removeHeaderComment(new String(content));
      assertEquals("", "package test.pkg\n" +
          "\n" +
          "uses gw.lang.IAnnotation\n" +
          "\n\n" +
          "class TestAnnotationOne implements IAnnotation {\n" +
          "\n" +
          "}", result);

     } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @Disabled(assignee = "verastov", reason = "functionality does not implemented")
  public void testDuplicateClassCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestClassUnique";
      final String templateName = "GosuClass.gs";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, templateName);
            action.runTest();
            action.runTest(); // run create a second time on purpose
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("File already exists"));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testClassCreationWithJavaConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestJavaClass";
      final String templateName = "GosuClass.gs";
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
      assertTrue("Expecting IllegalArgumentException, but got " + caught[0],
              caught[0] instanceof IllegalArgumentException);
      assertTrue(caught[0].getMessage().contains("Cannot create test.pkg.TestJavaClass since it already exists. Use a different name."));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  @KnownBreak(targetUser="dpetrusca", targetBranch="eng/emerald/pl/active/eclipse", jira="PL-19089")
  public void testJavaClassCreationWithGosuConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestJavaClass";
      final String templateName = "GosuClass.gs";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, templateName);
            action.runTest();
            pkgDir.createFile(className + ".java");
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("Cannot create TestJavaClass.java. A Gosu class with the same name already exists."));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testClassCreationWithTypeConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "ClassTemplateNameConflict";
      final String templateName = "GosuClass.gs";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, templateName);
            pkgDir.createFile(className + ".gst");
            action.runTest();
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue("Expecting IllegalArgumentException, but got " + caught[0],
              caught[0] instanceof IllegalArgumentException);
      assertTrue(caught[0].getMessage().contains("Cannot create test.pkg.ClassTemplateNameConflict since it already exists. Use a different name."));
    } catch (Exception e) {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  private class TestAction extends CreateClassAction {
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
      createFile(_className, _templateName, _dir);
    }
  }
}
