/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.google.common.base.Function;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateBuilderImpl;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TextExpression;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IHasType;
import gw.lang.parser.expressions.IArgumentListClause;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuIdentifier;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameterList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.util.HasParsedElement;
import gw.plugin.ij.lang.psi.util.LeafPsiMatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.google.common.base.Joiner.on;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Lists.transform;
import static com.intellij.psi.codeStyle.VariableKind.PARAMETER;
import static com.intellij.psi.util.ClassUtil.extractClassName;
import static com.intellij.psi.util.PsiMatchers.hasClass;
import static gw.plugin.ij.completion.GosuClassNameInsertHandler.addImportForItem;
import static gw.plugin.ij.lang.psi.util.GosuPsiParseUtil.parseProgramm;
import static gw.plugin.ij.util.ClassLord.purgeClassName;
import static java.beans.Introspector.decapitalize;

public class CreateMethodFix extends BaseIntentionAction {

  private final GosuMethodCallExpressionImpl callExpr;
  private final PsiClass targetClass;

  public CreateMethodFix(GosuMethodCallExpressionImpl callExpr, PsiClass targetClass) {
    checkArgument(targetClass instanceof IGosuPsiElement);
    this.callExpr = callExpr;
    this.targetClass = targetClass;
  }

  private static final Function<String, String> FQN_TO_IMPORT = new Function<String, String>() {

    public String apply(String fqn) {
      return "uses " + fqn;
    }
  };

  private static final Function<String, String> TO_ARG_DEF = new Function<String, String>() {

    public String apply(String typeName) {
      return "_" + decapitalize(extractClassName(purgeClassName(typeName))) + " : " +  typeName;
    }
  };

  private int bestPlaceToInsert() {
//    PsiClass exprClass = (PsiClass) new PsiMatcherImpl(callExpr).ancestor(hasClass(PsiClass.class)).getElement();
//    if (targetClass.equals(exprClass)) {
//      PsiElement methodCtx = new PsiMatcherImpl(targetClass).ancestor(hasClass(IGosuMethod.class)).getElement();
//      if (methodCtx != null) {
//        return methodCtx.getTextRange().getEndOffset();
//      }
//    }
//    PsiField[] fields = targetClass.getAllFields();
//    PsiMethod[] methods = targetClass.getAllMethods();
//    if (methods.length != 0) {
//      return methods[methods.length - 1].getParent() .getTextRange().getEndOffset();
//    } else if (fields.length != 0) {
//      return fields[fields.length - 1].getTextRange().getEndOffset();
//    } else {
      return targetClass.getLastChild().getPrevSibling().getTextOffset();
//    }
  }

  private List<IType> findArgTypes() {
    List<IType> result = new ArrayList<>();
    PsiElement args = new PsiMatcherImpl(callExpr).descendant(new HasParsedElement(IArgumentListClause.class)).getElement();
    if (args != null) {
      for (PsiElement child : args.getChildren()) {
        if (child instanceof IGosuPsiElement) {
          result.add(((IHasType)((IGosuPsiElement)child).getParsedElement()).getType());
        }
      }
    }
    return result;
  }

  @Override
  public void invokeImpl(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {

    PsiFile targetFile = targetClass.getContainingFile();

    Document document = PsiDocumentManager.getInstance(project).getDocument(targetFile);

    if (document == null) {
      return;
    }

    String name = getMethodName();
    List<IType> argTypes = findArgTypes();
    List<String> typeNames = new ArrayList<>(argTypes.size());
    List<String> imports = new ArrayList<>();

    for (IType type : argTypes) {
      ImportClassHelper importHelper = new ImportClassHelper(type, (IGosuPsiElement) targetClass);
      importHelper.resolveType();
      importHelper.analizeFQNUsing();

      if (importHelper.needImport()) {
        String purgedName = purgeClassName(type.getName());
        addImportForItem(targetFile, purgedName, purgeClassName(type.getRelativeName()));
        importHelper.stickImport();
        imports.add(purgedName);
      }
      typeNames.add(importHelper.useFQN() ? type.getName() : type.getRelativeName());
    }

    String stubStr = on('\n').join(transform(imports, FQN_TO_IMPORT)) +
            "\n function " + name + "(" + on(", ").join(transform(typeNames, TO_ARG_DEF)) + "){\n}";

    PsiElement stub = parseProgramm(stubStr, file.getManager(), null);
    PsiElement method = new PsiMatcherImpl(stub).descendant(hasClass(IGosuMethod.class)).getElement();

//    targetClass.add(method);

    TemplateBuilderImpl builder = new TemplateBuilderImpl(method);

    PsiElement[] children = new PsiMatcherImpl(method).descendant(hasClass(IGosuParameterList.class)).getElement().getChildren();
    for (int i = 0; i < children.length; ++i) {
      PsiElement e = children[i];
      if (e instanceof IGosuParameter) {
        PsiElement id = new LeafPsiMatcher(e).descendant(hasClass(IGosuIdentifier.class)).getElement();
        PsiElement typeLiteral = new LeafPsiMatcher(e).descendant(hasClass(GosuTypeLiteralImpl.class)).getElement();

        if (id != null && typeLiteral != null) {
          JavaCodeStyleManager styleManager = JavaCodeStyleManager.getInstance(project);
          SuggestedNameInfo suggestedInfo = styleManager.suggestVariableName(PARAMETER, null, null, ((GosuTypeLiteralImpl)typeLiteral).getType());
          String[] names = suggestedInfo.names;
          String pName;
          if (names.length == 0) {
            pName = "p" + (i + 1);
          } else {
            pName = names[0];
          }
          builder.replaceElement(id, new TextExpression(pName));
          builder.replaceElement(typeLiteral, new TextExpression(typeLiteral.getText()));
        }
      }
    }

    Editor newEditor;
    if (targetFile.equals(file)) {
      newEditor = editor;
    } else {
      OpenFileDescriptor descriptor = new OpenFileDescriptor(project, targetFile.getVirtualFile());
      newEditor = FileEditorManager.getInstance(project).openTextEditor(descriptor, true);

//      RangeMarker rangeMarker = document.createRangeMarker(method.getTextRange());
//      newEditor.getCaretModel().moveToOffset(rangeMarker.getStartOffset());
//      newEditor.getDocument().deleteString(rangeMarker.getStartOffset(), rangeMarker.getEndOffset());
    }

    Template template = builder.buildTemplate();
    template.setToReformat(true);
    template.setToIndent(true);

    int offset = targetClass.getLastChild().getPrevSibling().getTextOffset();

    newEditor.getDocument().insertString(offset, "\n");

    editor.getCaretModel().moveToOffset(offset + 1);
    TemplateManager.getInstance(project).startTemplate(newEditor, template);
//    targetClass.add(method);

//    CodeStyleManager csManager = CodeStyleManager.getInstance(project);
//    csManager.reformat(method);

  }

  @NotNull
  @Override
  public String getText() {
    return "Create Method " + getMethodName();
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return "gosu";
  }

  public String getMethodName() {
    String name = callExpr.getCanonicalText();
    return name.replaceAll("\\(.*$", "");
  }
}
