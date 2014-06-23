/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.ide.IdeView;
import com.intellij.ide.PasteProvider;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.newvfs.impl.VirtualDirectoryImpl;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.api.IGosuPackageDefinition;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.ClassLord;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.*;

import static com.google.common.collect.Sets.newHashSet;
import static com.intellij.psi.util.PsiMatchers.hasClass;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.parsePackageStatement;

public class GosuFilePasteProvider implements PasteProvider {

  private static final Logger LOG = Logger.getInstance(GosuFilePasteProvider.class);

  public void performPaste(@NotNull DataContext dataContext) {
    try {
      doPaste(dataContext);
    } catch (Exception e) {
      LOG.error("cannot paste the gosu content", e);
    }
  }

  private void doPaste(DataContext dataContext) {
    final Project project = DataKeys.PROJECT.getData(dataContext);
    final IdeView ideView = DataKeys.IDE_VIEW.getData(dataContext);
    final PsiDirectory targetDir = ideView.getOrChooseDirectory();
    if (targetDir == null) {
      return;
    }
    if (project == null) {
      return;
    }

    final String text = getTextFromClipboardContent(project);
    if (text == null) {
      return;
    }
    IGosuFileBase file = GosuPsiParseUtil.parse(text, project);

    final Guesser guesser = new Guesser();
    file.accept(guesser);
    if (!guesser.isGosu) {
      return;
    }

    final String unitName;
    Collection<PsiClass> classes = PsiTreeUtil.findChildrenOfType(file, PsiClass.class);
    //we have at least two classes in the psi: transient and real class.
    if (classes.size() < 2) {
      guesser.isProgram = true;
    }
    String ext;
    if (guesser.isProgram) {
      unitName = ensureFileName("program", targetDir, ext = "gsp", 0);
    } else {
      Iterator<PsiClass> it = classes.iterator();
      it.next();
      PsiClass mainClass = it.next();
      String mainClassName = mainClass.getName();
      unitName = ensureFileName(mainClassName, targetDir, ext = "gs", 0);
    }

    IGosuPackageDefinition pckg = PsiTreeUtil.findChildOfType(file, IGosuPackageDefinition.class);
    final String oldPackageName;
    if (pckg != null) {
      oldPackageName = pckg.getPackageName();
    } else {
      oldPackageName = null;
    }

    final String fileName = unitName + "." + ext;

    new WriteCommandAction(project, "Paste gosu file '" + fileName + "'") {
      @Override
      protected void run(Result result) throws Throwable {
        PsiFile newFile;
        try {
          newFile = targetDir.createFile(fileName);
        } catch (IncorrectOperationException e) {
          return;
        }
        final Document document = PsiDocumentManager.getInstance(project).getDocument(newFile);
        document.setText(text);
        PsiDocumentManager.getInstance(project).commitDocument(document);
        if (newFile instanceof AbstractGosuClassFileImpl || !guesser.isProgram) {
          try {
            updatePackageStatement((AbstractGosuClassFileImpl) newFile, oldPackageName, targetDir);
          } catch (Exception e) {
            LOG.info("Unable to fix imports and add missing imports for the pasting gosu file. " + e.getMessage());
          }
        }

        PsiClass mainClass = PsiTreeUtil.findChildOfType(newFile, PsiClass.class);

        if (mainClass != null && !unitName.equals(mainClass.getName())) {
          mainClass.setName(unitName);
        }

        new OpenFileDescriptor(project, newFile.getVirtualFile()).navigate(true);
      }
    }.execute();
  }

  private static void updatePackageStatement(final AbstractGosuClassFileImpl gosuFile, String oldPackageName, final PsiDirectory targetDir) {
    final PsiPackage aPackage = JavaDirectoryService.getInstance().getPackage(targetDir);
    if (aPackage == null) {
      return;
    }
    final String newPackg = aPackage.getQualifiedName();
    IGosuPackageDefinition pckgDef = PsiTreeUtil.findChildOfType(gosuFile, IGosuPackageDefinition.class);
    if (pckgDef != null) {
      pckgDef.replace(parsePackageStatement(newPackg, gosuFile));
    } else {
      PsiElement firstChild = gosuFile.getFirstChild();
      if (firstChild != null) {
        gosuFile.addBefore(parsePackageStatement(newPackg, gosuFile), firstChild);
      }
    }

    IParsedElement pe = gosuFile.getParsedElement();
    IGosuClass gosuClass = pe.getGosuClass();
    IGosuParser parser = gosuClass.getParser();
    pe = gosuClass.getClassStatement();
    IModule module = gosuClass.getTypeLoader().getModule();
    TypeSystem.pushModule(module);

    ArrayList<String> imports = new ArrayList<>();;
    try {
      List<ITypeLiteralExpression> typeLitExprs = new ArrayList<>();
      pe.getContainedParsedElementsByType(ITypeLiteralExpression.class, typeLitExprs);
      for (ITypeLiteralExpression expr : typeLitExprs) {
        String className = expr.getLocation().getTextFromTokens();
        className = ClassLord.purgeClassName(className);
        if (!resolveRelativeTypeInParser(className, parser)) {
          String checkName = oldPackageName + "." + className;
          IType typeCheck = TypeSystem.getByFullNameIfValid(checkName);
          if (typeCheck != null) {
             imports.add(checkName);
          }
        }
      }
    } finally {
      TypeSystem.popModule(module);
    }
    for (String imp : imports) {
      ClassLord.doImport(imp, gosuFile);
    }
  }

  public static boolean resolveRelativeTypeInParser(String strRelativeName, IGosuParser parser) {
    IType type = parser.resolveTypeLiteral(strRelativeName).getType().getType();
    return !(type instanceof IErrorType);
  }

  private String ensureFileName(String fileName, PsiDirectory dir, String ext, int attempt) {
    String tryName = fileName;
    if (attempt > 0) {
      tryName += "_" + attempt;
    }
    PsiFile file = dir.findFile(tryName + "." + ext);
    if (file != null) {
      return ensureFileName(fileName, dir, ext, ++attempt);
    } else {
      return tryName;
    }
  }

  public boolean isPastePossible(@NotNull DataContext dataContext) {
    return true;
  }

  public boolean isPasteEnabled(@NotNull DataContext dataContext) {
    Project project = DataKeys.PROJECT.getData(dataContext);
    if (project == null) {
      return false;
    }
    String text = getTextFromClipboardContent(project);
    if (text == null) {
      return false;
    }
    try {
      IGosuFileBase file = GosuPsiParseUtil.parse(text, project);
      Guesser guesser = new Guesser();
      file.accept(guesser);
      return guesser.isGosu;
    } catch (Exception e) {
      //not gosu
      e.printStackTrace();
    }
    return false;
  }

  private static String getTextFromClipboardContent(final Project project) {
    Transferable content = CopyPasteManager.getInstance().getContents();
    if (content != null) {
      String text = null;
      try {
        text = (String) content.getTransferData(DataFlavor.stringFlavor);
      } catch (Exception e) {
        // ignore;
      }
      return text;
    }
    return null;
  }

  static class Guesser extends PsiRecursiveElementVisitor {

    boolean isGosu = false;
    boolean isProgram = true;

    final Class[] gosuDeterminants = {IGosuMethod.class, IGosuField.class};
    final Set<String> classDeterminants = newHashSet("class", "enum", "interface");

    public void visitElement(PsiElement element) {
      for (Class<?> c : gosuDeterminants) {
        if (c.isInstance(element)) {
          isGosu = true;
        }
      }
      if (element instanceof GosuTokenImpl && classDeterminants.contains(element.getText())) {
        isProgram = false;
      }
      super.visitElement(element);
    }
  }
}
