/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.util;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.GeneratedMarkerVisitor;
import com.intellij.psi.impl.source.javadoc.PsiDocCommentImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.StringBuilderSpinAllocator;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.IGosuFileBase;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.api.statements.IGosuVariable;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.ModuleFileContext;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.lang.psi.stubs.GosuFileStub;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.LightVirtualFileWithModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.openapi.util.text.StringUtil.join;
import static com.intellij.psi.util.PsiMatchers.hasClass;
import static gw.plugin.ij.util.ClassLord.purgeClassName;

public class GosuPsiParseUtil {
  public static final String TRANSIENT_PROGRAM = "transient_program.gsp";
  public static final String TRANSIENT_TEMPLATE = "transient_template.gst";

  @Nullable
  public static IGosuFileBase createVirtualProgramFile(@NotNull IGosuPsiElement psi, String strScript) {
    AbstractGosuClassFileImpl file = (AbstractGosuClassFileImpl) psi.getContainingFile();
    ASTNode child = file.getNode().findChildByType(GosuElementTypes.ELEM_TYPE_UsesStatementList);
    if (child != null) {
      strScript = child.getText() + "\n" + strScript;
    }
    child = file.getNode().findChildByType(GosuElementTypes.ELEM_TYPE_NamespaceStatement);
    if (child != null) {
      String text = child.getText();
      text = text.replace("package", "uses") + ".*";
      strScript = text + "\n" + strScript;
    }

    final LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, strScript, GosuModuleUtil.findModuleForPsiElement(psi));
    return (IGosuFileBase) PsiManager.getInstance(psi.getProject()).findFile(virtualFile);
  }

  public static PsiElement parseRelativeTypeLiteral(@NotNull String fullName, @NotNull PsiElement ctx) {
    String purged = purgeClassName(fullName);
    String qualifiedName = fullName.substring(purged.lastIndexOf(".") + 1);
    String text = "uses " + fullName + " return null as " + qualifiedName;
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, text, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    return new PsiMatcherImpl(psiFile)
            .descendant(PsiMatchers.hasClass(GosuSyntheticClassDefinitionImpl.class))
            .descendant(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_ReturnStatement))
            .descendant(PsiMatchers.hasClass(GosuTypeLiteralImpl.class))
            .getElement();
  }

  public static PsiElement parseTypeLiteral(@NotNull String fullName, @NotNull PsiElement ctx) {
    return new PsiMatcherImpl(parseImport(fullName, ctx)).descendant(hasClass(GosuTypeLiteralImpl.class)).getElement();
  }

  public static PsiElement parseFullTypeLiteral(@NotNull String literalText, @NotNull PsiElement ctx, Iterable<String> imports) {
    StringBuilder src = new StringBuilder();
    for (String imp : imports) {
      src.append("uses ").append(imp).append("\n");
    }
    src.append("var x : ").append(literalText);
    IGosuFileBase psiFile = parse(src, ctx);
    
    return new PsiMatcherImpl(psiFile)
      .descendant(hasClass(PsiVariable.class))
      .descendant(hasClass(GosuTypeLiteralImpl.class))
      .getElement();
  }
  
  public static IGosuFileBase parse(CharSequence src, PsiElement ctx) {
    return parse(src, ctx.getProject(), GosuModuleUtil.findModuleForPsiElement(ctx));
  }

  public static IGosuFileBase parse(CharSequence src, Project project) {
    return parse(src, project, GosuModuleUtil.getGlobalModule(project));
  }

  public static IGosuFileBase parse(CharSequence src, Project project, IModule module) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, src, module);
    return (IGosuFileBase) PsiManager.getInstance(project).findFile(virtualFile);
  }
  
  public static PsiElement parseDeclaration(String s, @NotNull PsiManager manager, @Nullable IModule module) {
    if (module != null) {
      TypeSystem.pushModule(module);
    }
    try {
      LightVirtualFile fakeFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, s, module);
      GosuFileStub gosuFileStub = new GosuFileStub(new GosuProgramFileImpl(new SingleRootFileViewProvider(manager, fakeFile, false)));
      PsiElement[] children = gosuFileStub.getPsi().getChildren();
      return children[0].getChildren()[2];
    } finally {
      if (module != null) {
        TypeSystem.popModule(module);
      }
    }
  }

  public static PsiElement parseExpression(String expression, @NotNull PsiManager manager) {
    return parseExpression(expression, manager, null);
  }

  public static PsiElement parseExpression(String expression, @NotNull PsiManager manager, @Nullable IModule module) {
    if (module == null) {
      module = GosuModuleUtil.getGlobalModule(manager.getProject());
    }

    TypeSystem.pushModule(module);
    try {
      final LightVirtualFile file = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, expression, module);
      IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(manager.getProject()).findFile(file);
      return psiFile.getChildren()[0].getChildren()[2].getChildren()[0];
    } finally {
      TypeSystem.popModule(module);
    }
  }

  public static PsiElement parseProgramm(String content, @NotNull PsiManager manager, @Nullable IModule module) {
    return parseProgramm(content, null, manager, module);
  }

  public static PsiElement parseProgramm(String content, @Nullable PsiElement elemCtx, @NotNull PsiManager manager, @Nullable IModule module) {
    if (module == null) {
      module = GosuModuleUtil.getGlobalModule(manager.getProject());
    }

    String uses = getPackageAndUsesStatementsFrom(elemCtx);

    TypeSystem.pushModule(module);
    try {
      final LightVirtualFile file = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, uses + content, module);
      IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(manager.getProject()).findFile(file);
      return psiFile;
    } finally {
      TypeSystem.popModule(module);
    }
  }

  private static String getPackageAndUsesStatementsFrom(PsiElement element) {
    StringBuilder out = new StringBuilder();
    PsiElement file = new PsiMatcherImpl(element)
            .ancestor(hasClass(AbstractGosuClassFileImpl.class))
            .getElement();
    if (file instanceof AbstractGosuClassFileImpl) {
      AbstractGosuClassFileImpl clazz = (AbstractGosuClassFileImpl) file;
      String packageName = clazz.getPackageName();
      IGosuUsesStatementList usesStatementList = clazz.findUsesStatementList();
      if (packageName != null) {
        out.append("package " + packageName + "\n");
      }
      if (usesStatementList != null) {
        out.append(usesStatementList.getText() + "\n");
      }
    }
    return out.toString();
  }

  @Nullable
  public static PsiElement createModifierFromText(final String strModifier, @NotNull PsiManager manager) {
    PsiElement psi = parseExpression(strModifier, manager);
    return psi.getFirstChild();
  }

  public static PsiElement parseImport(String s, @NotNull PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, "uses " + s, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    return psiFile.getChildren()[0].getChildren()[2].getChildren()[0];
  }

  @NotNull
  public static IGosuUsesStatementList parseUsesList(String importListAsText, @NotNull PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, importListAsText, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    IGosuUsesStatementList statementList = (IGosuUsesStatementList) psiFile.getChildren()[0].getChildren()[2];
    GeneratedMarkerVisitor.markGenerated(statementList);
    return statementList;
  }

  public static PsiElement parseTemplateImport(String s, @NotNull PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_TEMPLATE, "<%uses " + s + "%>", GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    return psiFile.getChildren()[0].getChildren()[2].getChildren()[0];
  }

  public static PsiElement parsePackageStatement(String strPackage, @NotNull PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, "package " + strPackage, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    PsiElement psiElement = psiFile.getChildren()[0].getChildren()[2];
    GeneratedMarkerVisitor.markGenerated(psiElement);
    return psiElement;
  }

  @Nullable
  public static PsiElement createReferenceNameFromText(@NotNull GosuReferenceExpressionImpl referenceExpression, String refName) {
    String strScript = "return " + refName;
    IGosuFileBase file = createVirtualProgramFile(referenceExpression, strScript);
    int iRefNameIndex = file.getText().indexOf(strScript) + "return ".length();
    PsiElement psiRefName = file.findElementAt(iRefNameIndex);
    if (psiRefName == null) {
      throw new IllegalStateException("Did not find reference name psi");
    }
    return psiRefName;
  }

  @NotNull
  public static GosuParameterImpl createParameter(String name, @NotNull PsiType argType, @NotNull PsiElement contextElement) {
    GosuMethodImpl method = (GosuMethodImpl) parseDeclaration(
            "construct(" + name + ": " + argType.getPresentableText() + ") {}",
            contextElement.getManager(), GosuModuleUtil.findModuleForPsiElement(contextElement));
    return (GosuParameterImpl) method.getParameters()[0];
  }

  public static PsiIdentifier parseIdentifier(String name, PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, "return " + name, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    return (PsiIdentifier) new PsiMatcherImpl(psiFile)
            .descendant(PsiMatchers.hasClass(GosuSyntheticClassDefinitionImpl.class))
            .descendant(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_ReturnStatement))
            .descendant(PsiMatchers.hasClass(GosuIdentifierExpressionImpl.class))
            .getElement()
            .getFirstChild();
  }

  public static PsiElement parseIdentifierOrTokenOrRelativeType(String name, PsiElement ctx) {
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, "return " + name, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    PsiElement element = new PsiMatcherImpl(psiFile)
            .descendant(PsiMatchers.hasClass(GosuSyntheticClassDefinitionImpl.class))
            .descendant(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_ReturnStatement)).getElement();
    return element.findElementAt("return ".length());
  }

  public static PsiDocComment parseJavadocComment(String text, PsiElement ctx) {
    String code = "/**\n*" + text + "\n*/\nvar x: int";
    LightVirtualFile virtualFile = LightVirtualFileWithModule.create(TRANSIENT_PROGRAM, code, GosuModuleUtil.findModuleForPsiElement(ctx));
    IGosuFileBase psiFile = (IGosuFileBase) PsiManager.getInstance(ctx.getProject()).findFile(virtualFile);
    PsiDocComment firstChild = (PsiDocComment) new PsiMatcherImpl(psiFile)
            .descendant(PsiMatchers.hasClass(GosuSyntheticClassDefinitionImpl.class))
            .descendant(PsiMatchers.hasClass(PsiDocCommentImpl.class))
            .getElement();
    return firstChild;
  }

  /**
   * this method was copied from PsiElementFactoryImpl
   *
   * @param project
   * @param name
   * @param initializer
   * @return
   * @throws IncorrectOperationException
   */
  @NotNull
  public static IGosuVariable createLocalVariableDeclarationStatement(@NotNull Project project, @NotNull final String name,
                                                                      boolean isFinal,
                                                                      final PsiExpression initializer) throws IncorrectOperationException {
    final PsiElement psiElement = createLocalVariableDeclarationAssignmentStatement(project, name, isFinal, initializer);

    final GosuVariableImpl variable = PsiTreeUtil.findChildOfType(psiElement, GosuVariableImpl.class);

    GeneratedMarkerVisitor.markGenerated(variable);
    return (GosuVariableImpl) CodeStyleManager.getInstance(project).reformat(variable);
  }

  @NotNull
  private static PsiElement createLocalVariableDeclarationAssignmentStatement(@NotNull Project project, @NotNull final String name,
                                                                              boolean isFinal,
                                                                              final PsiExpression initializer) throws IncorrectOperationException {
    if (!JavaPsiFacade.getInstance(project).getNameHelper().isIdentifier(name)) {
      throw new IncorrectOperationException("\"" + name + "\" is not an identifier.");
    }

    final StringBuilder builder = StringBuilderSpinAllocator.alloc();
    if (initializer != null) {
      PsiType type = initializer.getType();
      if (initializer instanceof PsiArrayInitializerExpression) {
        final PsiExpression[] initializers = ((PsiArrayInitializerExpression) initializer).getInitializers();
        type = initializers.length == 0 ? type : initializers[0].getType();
      }
      if (type != null) {
        builder.append("uses ").append(type.getCanonicalText()).append("\n");
      }
    }
    builder.append("function foo(){").
            append(isFinal ? "final " : "").
            append("var ");
    int i = 2;
    while (i-- > 0) { //one iteration for declaration and one for assignment
      builder.append(name).
              append(" = ");
      if (initializer != null) {
        builder.append(initializer.getText());
      } else {
        builder.append("1");
      }
      builder.append(" ");
    }

    builder.append("}");

    final String text = builder.toString();
    StringBuilderSpinAllocator.dispose(builder);

    final PsiElement psiElement = parseProgramm(text, PsiManager.getInstance(project), null);

    return psiElement;
  }


  public static PsiAssignmentExpression createAssignmentStatement(@NotNull Project project, @NotNull final String name, final PsiExpression initializer) {
    final PsiElement psiElement = createLocalVariableDeclarationAssignmentStatement(project, name, false, initializer);

    final PsiAssignmentExpression assignmentStatement = PsiTreeUtil.findChildOfType(psiElement, PsiAssignmentExpression.class);

    GeneratedMarkerVisitor.markGenerated(assignmentStatement);
    return assignmentStatement;
  }

  public static PsiElement setName(PsiIdentifier identifier, String name) {
    PsiIdentifier newIdentifier = parseIdentifier(name, identifier);
    return identifier.replace(newIdentifier);
  }

  public static PsiMethod createConstructor(final String name, @NotNull PsiManager manager, @Nullable IModule module) {
    final PsiElement program = parseProgramm(join("class ", name, " { construct () {} }"), manager, module);
    final PsiMethod method = PsiTreeUtil.findChildOfType(program, GosuMethodImpl.class);
    GeneratedMarkerVisitor.markGenerated(method);
    return (PsiMethod) CodeStyleManager.getInstance(manager.getProject()).reformat(method);
  }

  public static PsiExpressionList createEmptyExpressionList(@NotNull PsiManager manager) {
    PsiElement psiElement = parseProgramm("print(null)", manager, null);
    PsiExpressionList exprList = PsiTreeUtil.findChildOfType(psiElement, PsiExpressionList.class);
    exprList.getExpressions()[0].delete();
    return exprList;
  }

  public static IGosuProgram parseProgram(@NotNull IGosuParser parser, @NotNull ParserOptions options, ModuleFileContext context, String contents) throws ParseResultsException {
    final ParserOptions parserOptions = options
        .withParser(parser)
        .withTypeUsesMap(parser.getTypeUsesMap())
        .withFileContext(context);

    return GosuParserFactory.createProgramParser()
        .parseExpressionOrProgram(contents, parser.getSymbolTable(), parserOptions)
        .getProgram();
  }
}
