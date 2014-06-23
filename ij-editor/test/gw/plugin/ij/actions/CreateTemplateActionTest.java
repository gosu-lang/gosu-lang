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

public class CreateTemplateActionTest extends GosuTestCase {
  public void testTemplateCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestTemplateOne";
      final String templateName = "GosuTemplate.gst";

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          TestAction action = new TestAction(pkgDir, className, templateName);
          action.runTest();
        }
      });

      // verify result
      PsiFile pfile = pkgDir.findFile("TestTemplateOne.gst");
      byte[] content = pfile.getVirtualFile().contentsToByteArray();
      String result = new String(content);
      assertEquals("", "<%@ params( myParam: String ) %>\n" +
          "\n" +
          "The content of my param is: ${myParam}\n" +
          "\n" +
          "Note you can render this template from a class or program\n" +
          "simply by calling one of its render methods:\n" +
          "\n" +
          "  TestTemplateOne.renderToString( \"wow\" )", result.trim());
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testDuplicateTemplateCreation() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestTemplateUnique";
      final String templateName = "GosuTemplate.gst";
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
      assertTrue(caught[0].getMessage().contains("File already exists"));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testTemplateCreationWithJavaConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TestTemplateUnique";
      final String templateName = "GosuTemplate.gst";
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
      assertTrue(caught[0].getMessage().contains("Cannot create TestTemplateUnique.gst. A Java class with the same name already exists."));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  public void testTemplateCreationWithTypeConflict() {
    try {
      File testRootDir = createTempDirectory();
      final PsiDirectory rootDir = PsiDirectoryFactory.getInstance(getProject()).createDirectory(getVirtualFile(testRootDir));
      final PsiDirectory pkgDir = PackageUtil.findOrCreateDirectoryForPackage(getModule(), "test.pkg", rootDir, false);
      final String className = "TemplateEnhancementNameConflict";
      final String templateName = "GosuTemplate.gst";
      final Throwable[] caught = new Throwable[1];
      caught[0] = null;

      ApplicationManager.getApplication().runWriteAction(new Runnable() {
        @Override
        public void run() {
          try {
            TestAction action = new TestAction(pkgDir, className, templateName);
            pkgDir.createFile(className + ".gsx");
            action.runTest();
          } catch (Throwable t) {
            caught[0] = t;
          }
        }
      });
      assertNotNull(caught[0]);
      assertTrue(caught[0] instanceof IncorrectOperationException);
      assertTrue(caught[0].getMessage().contains("Cannot create TemplateEnhancementNameConflict.gst. A type with the same name already exists."));
    } catch (Exception e) {
      fail(e.getMessage());
      e.printStackTrace();
    }
  }

  private class TestAction extends CreateTemplateAction {

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
