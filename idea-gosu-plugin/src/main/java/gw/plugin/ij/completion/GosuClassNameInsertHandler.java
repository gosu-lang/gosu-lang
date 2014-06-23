/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.ExpectedTypeInfo;
import com.intellij.codeInsight.ExpectedTypesProvider;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.completion.handlers.filter.CompletionFilter;
import gw.plugin.ij.completion.handlers.filter.CompletionFilterExtensionPointBean;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuClassParseData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GosuClassNameInsertHandler implements InsertHandler<JavaPsiClassReferenceElement> {
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.completion.JavaClassNameInsertHandler");
  public static final InsertHandler<JavaPsiClassReferenceElement> GOSU_CLASS_INSERT_HANDLER = new GosuClassNameInsertHandler();

  public void handleInsert(@NotNull final InsertionContext context, @NotNull final JavaPsiClassReferenceElement item) {
    if (!canBeInserted(item.getQualifiedName())) {
      return;
    }

    final char c = context.getCompletionChar();
    if (c == '\t' || c == '\n') {
      context.setAddCompletionChar(false);
    }

    int offset = context.getTailOffset() - 1;
    final PsiFile file = context.getFile();
    if (PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiImportStatementBase.class, false) != null) {
      final PsiJavaCodeReferenceElement ref = PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiJavaCodeReferenceElement.class, false);
      final String qname = item.getQualifiedName();
      if (qname != null && (ref == null || !qname.equals(ref.getCanonicalText()))) {
        AllClassesGetter.INSERT_FQN.handleInsert(context, item);
      }
      return;
    }

    PsiElement position = file.findElementAt(offset);
    PsiClass psiClass = item.getObject();
    final Project project = context.getProject();
    final boolean annotation = insertingAnnotation(context, item);

    final Editor editor = context.getEditor();
    if (c == '#') {
      context.setLaterRunnable(new Runnable() {
        public void run() {
          new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(project, editor);
        }
      });
    }

    if (position != null) {
      PsiElement parent = position.getParent();
      if (parent instanceof PsiJavaCodeReferenceElement) {
        final PsiJavaCodeReferenceElement ref = (PsiJavaCodeReferenceElement) parent;
        if (PsiTreeUtil.getParentOfType(position, PsiDocTag.class) != null) {
          if (ref.isReferenceTo(psiClass)) {
            return;
          }
        }
        final PsiReferenceParameterList parameterList = ref.getParameterList();
        if (parameterList != null && parameterList.getTextLength() > 0) {
          return;
        }
      }
    }

    if (completingRawConstructor(file.findElementAt(context.getTailOffset() - 1), item)) {
      JavaCompletionUtil.insertParentheses(context, item, false, true);
      //TODO-dp
//      if (ConstructorInsertHandler.insertParentheses(context, item, psiClass)) {
//        AutoPopupController.getInstance(project).autoPopupParameterInfo(editor, null);
//      }
    } else if (insertingAnnotationWithParameters(context, item)) {
      JavaCompletionUtil.insertParentheses(context, item, false, true);
      AutoPopupController.getInstance(project).autoPopupParameterInfo(editor, null);
    }

    // Added a commit to to avoid an exception -> ERROR - impl.PsiToDocumentSynchronizer - Attempt to modify PSI for non-committed Document!
    PsiDocumentManager.getInstance(project).commitDocument(context.getDocument());
    PsiFile psiFile = context.getFile();
    if( psiFile instanceof AbstractGosuClassFileImpl ) {
      ((AbstractGosuClassFileImpl)psiFile).reparsePsiFromContent();
    }
    addImportForItem( psiFile, item.getQualifiedName(), item.getLookupString());

    if (annotation) {
      // Check if someone inserts annotation class that require @
      PsiElement elementAt = file.findElementAt(context.getStartOffset());
      final PsiElement parentElement = elementAt != null ? elementAt.getParent() : null;

      if (elementAt instanceof PsiIdentifier &&
          (PsiTreeUtil.getParentOfType(elementAt, PsiAnnotationParameterList.class) != null ||
              parentElement instanceof PsiErrorElement && parentElement.getParent() instanceof PsiJavaFile // top level annotation without @
          )
          && isAtTokenNeeded(context)) {
        int expectedOffsetForAtToken = elementAt.getTextRange().getStartOffset();
        context.getDocument().insertString(expectedOffsetForAtToken, "@");
      }
    }
  }

  public boolean canBeInserted(String fqn) {
    List<CompletionFilter> filters = CompletionFilterExtensionPointBean.getFilters();
    for (CompletionFilter filter : filters) {
      if (!filter.allowsImportInsertion(fqn)) {
        return false;
      }
    }
    return true;
  }

  public static boolean addImportForItem( @NotNull PsiFile file, @NotNull String strQName, String strRelativeName ) {
    if( !(file instanceof AbstractGosuClassFileImpl) ) {
      return false;
    }

    PsiClass psiClass = ((AbstractGosuClassFileImpl) file).getPsiClass();
    if( psiClass != null && strQName.startsWith(psiClass.getQualifiedName()) ) {
      // The item to import is an inner class of the file
      return false;
    }

    AbstractGosuClassFileImpl gosuClassFile = (AbstractGosuClassFileImpl) file;
    GosuClassParseData parseData = gosuClassFile.getParseData();
    if (parseData != null) {
      IClassFileStatement classFileStatement = parseData.getClassFileStatement();
      if (classFileStatement != null) {
        IGosuClass gosuClass = classFileStatement.getGosuClass();
        if (gosuClass != null) {
          ITypeUsesMap typeUsesMap = gosuClass.getTypeUsesMap();
          strQName = maybeRemoveBrackets(strQName);
          strRelativeName = maybeRemoveBrackets(strRelativeName);
          if (typeUsesMap != null && !typeUsesMap.containsType(strQName)) {
            if( resolveRelativeTypeInParser(strQName, strRelativeName, gosuClass ) ) {
              // The type can be resolved in the parser's context, so no need to import
              return false;
            }
            gosuClassFile.addImport( strQName );
            return true;
          }
        }
      }
    }
    return false;
  }

  private static String maybeRemoveBrackets(String s) {
    int i = s.indexOf("<");
    if(i != -1) {
      s = s.substring(0, i);
    }
    i = s.indexOf("[");
    if(i != -1) {
      s = s.substring(0, i);
    }
    return s;
  }

  public static boolean resolveRelativeTypeInParser(String strQName, String strRelativeName, @NotNull IGosuClass gosuClass) {
    IGosuParser parser = gosuClass.getParser();
    if( parser != null ) {
      TypeSystem.pushModule( gosuClass.getTypeLoader().getModule() );
      try {
        IType type = parser.resolveTypeLiteral(strRelativeName).getType().getType();
        if( !(type instanceof IErrorType) && strQName.equals(type.getName()) ) {
          return true;
        }
      }
      finally {
        TypeSystem.popModule( gosuClass.getTypeLoader().getModule() );
      }
    }
    return false;
  }

  private static boolean completingRawConstructor(@NotNull PsiElement position, @NotNull JavaPsiClassReferenceElement item) {
    PsiNewExpression newExpression = PsiTreeUtil.getParentOfType(position, PsiNewExpression.class);
    if (newExpression != null && newExpression instanceof PsiNewExpression) {
      PsiTypeParameter[] typeParameters = item.getObject().getTypeParameters();
      for (ExpectedTypeInfo info : ExpectedTypesProvider.getExpectedTypes(newExpression, true)) {
        final PsiType type = info.getType();

        if (info.isArrayTypeInfo()) {
          return false;
        }
        if (typeParameters.length > 0 && type instanceof PsiClassType && !((PsiClassType) type).isRaw()) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  private static boolean insertingAnnotationWithParameters(@NotNull InsertionContext context, @NotNull LookupElement item) {
    if (insertingAnnotation(context, item)) {
      final Document document = context.getEditor().getDocument();
      PsiDocumentManager.getInstance(context.getProject()).commitDocument(document);
      PsiElement elementAt = context.getFile().findElementAt(context.getStartOffset());
      if (elementAt instanceof PsiIdentifier) {
        final PsiModifierListOwner parent = PsiTreeUtil.getParentOfType(elementAt, PsiModifierListOwner.class, false, PsiCodeBlock.class);
        if (parent != null) {
          for (PsiMethod m : ((PsiClass) item.getObject()).getMethods()) {
            if (!(m instanceof PsiAnnotationMethod)) continue;
            final PsiAnnotationMemberValue defaultValue = ((PsiAnnotationMethod) m).getDefaultValue();
            if (defaultValue == null) return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean insertingAnnotation(@NotNull InsertionContext context, @NotNull LookupElement item) {
    final Object obj = item.getObject();
    if (!(obj instanceof PsiClass) || !((PsiClass) obj).isAnnotationType()) return false;

    final Document document = context.getEditor().getDocument();
    PsiDocumentManager.getInstance(context.getProject()).commitDocument(document);
    final int offset = context.getStartOffset();

    final PsiFile file = context.getFile();

    if (PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiImportStatement.class, false) != null) return false;

    //outside of any class: we are surely inserting an annotation
    if (PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiClass.class, false) == null) return true;

    //the easiest check that there's a @ before the identifier
    return PsiTreeUtil.findElementOfClassAtOffset(file, offset, PsiAnnotation.class, false) != null;

  }

  private static boolean isAtTokenNeeded(@NotNull InsertionContext myContext) {
    HighlighterIterator iterator = ((EditorEx) myContext.getEditor()).getHighlighter().createIterator(myContext.getStartOffset());
    LOG.assertTrue(iterator.getTokenType() == JavaTokenType.IDENTIFIER);
    iterator.retreat();
    if (iterator.getTokenType() == TokenType.WHITE_SPACE) iterator.retreat();
    return iterator.getTokenType() != JavaTokenType.AT && iterator.getTokenType() != JavaTokenType.DOT;
  }
}