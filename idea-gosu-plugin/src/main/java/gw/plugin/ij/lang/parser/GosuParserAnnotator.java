/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.google.common.base.Objects;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.AnnotationSession;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiNewExpression;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.util.PsiTreeUtil;
import gw.internal.gosu.parser.expressions.ArgumentListClause;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.CompoundTypeLiteral;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.expressions.TypeAsExpression;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IHasType;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.parser.exceptions.ICoercionIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.resources.Res;
import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.statements.IArrayAssignmentStatement;
import gw.lang.parser.statements.IAssignmentStatement;
import gw.lang.parser.statements.IBeanMethodCallStatement;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IMapAssignmentStatement;
import gw.lang.parser.statements.IMemberAssignmentStatement;
import gw.lang.parser.statements.IMethodCallStatement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.INotAStatement;
import gw.lang.parser.statements.IReturnStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.filetypes.GosuFileTypes;
import gw.plugin.ij.intentions.ChangeMethodTypeFix;
import gw.plugin.ij.intentions.CreateClassFix;
import gw.plugin.ij.intentions.CreateGosuClassFromNewFix;
import gw.plugin.ij.intentions.CreateJavaClassFromNewFix;
import gw.plugin.ij.intentions.CreateMethodFix;
import gw.plugin.ij.intentions.GosuAddMissingOverrideFix;
import gw.plugin.ij.intentions.GosuAddMissingUsesFix;
import gw.plugin.ij.intentions.GosuAddTypeCastFix;
import gw.plugin.ij.intentions.GosuChangeTypeCastFix;
import gw.plugin.ij.intentions.GosuCreateClassKind;
import gw.plugin.ij.intentions.GosuImplementMethodsFix;
import gw.plugin.ij.intentions.GosuImportReferenceAnalyzer;
import gw.plugin.ij.intentions.HandleInterfaceRedundantFix;
import gw.plugin.ij.intentions.HandleUnnecessaryCoercionFix;
import gw.plugin.ij.intentions.HandleVarArgFix;
import gw.plugin.ij.intentions.IQuickFixProvider;
import gw.plugin.ij.intentions.ObsoleteConstructorFix;
import gw.plugin.ij.intentions.OrganizeImports;
import gw.plugin.ij.intentions.QuickFixProviderExtensionBean;
import gw.plugin.ij.intentions.RemoveUnnecessaryImports;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatement;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuExtendsClause;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuImplementsClause;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuAnonymousClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.util.InjectedElementEditor;
import gw.util.GosuStringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;
import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.psi.util.ClassUtil.extractPackageName;
import static com.intellij.psi.util.PsiTreeUtil.findChildOfType;
import static gw.plugin.ij.util.GosuBundle.message;
import static gw.plugin.ij.util.ParseTreeUtil.lookupAncestor;
import static java.util.Collections.singleton;

public class GosuParserAnnotator implements Annotator, Condition<VirtualFile> {
  private AnnotationSession session;

  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    ProgressManager.checkCanceled();

    if (session != holder.getCurrentAnnotationSession()) {
      session = holder.getCurrentAnnotationSession();

      // Reparse the file at every session switch, but only if the file is not open in the current editor
      // TODO-dp this may be a LARGE perf bottleneck.
      final AbstractGosuClassFileImpl psiFile = ((AbstractGosuClassFileImpl) session.getFile());

      if( psiFile.isValid() ) {
        if( !psiFile.reparsePsiFromContent() ) {
          annotateFile( psiFile, holder );
        }
      }
    }
  }

  private void annotateFile(AbstractGosuClassFileImpl psiFile, AnnotationHolder holder) {
    TypeSystem.pushModule( psiFile.getModule() );
    try {
      // annotateUnusedImports(psiFile, holder);
      final IClassFileStatement classFileStatement = psiFile.getParseData().getClassFileStatement();
      if (classFileStatement != null) {
        final IGosuClass gsClass = classFileStatement.getGosuClass();
        @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
        final ParseResultsException exception = gsClass.getParseResultsException();
        if (exception != null) {
          // Errors
          for (IParseIssue error : exception.getParseExceptions()) {
            final Annotation annotation = holder.createErrorAnnotation(getAnnotationRange(psiFile, error), error.getUIMessage());
            maybeRegisterFix(error, annotation, psiFile);
          }

          // Warnings
          for (IParseIssue warning : exception.getParseWarnings()) {
            final Annotation annotation = holder.createWarningAnnotation(getAnnotationRange(psiFile, warning), warning.getUIMessage());
            if (warning.getMessageKey() == Res.MSG_DEPRECATED_MEMBER) {
              annotation.setHighlightType(ProblemHighlightType.LIKE_DEPRECATED);
            }
            maybeRegisterFix(warning, annotation, psiFile);
          }
        }
      }
      for (IQuickFixProvider qp : QuickFixProviderExtensionBean.getProviders()) {
        qp.collectQuickFixes(psiFile, holder);
      }
      findUnusedImports(psiFile, holder);
    }
    finally {
      TypeSystem.popModule( psiFile.getModule() );
    }
  }

  private void findUnusedImports(PsiFile file,  AnnotationHolder holder) {
    final IGosuUsesStatementList usesList = findChildOfType(file, IGosuUsesStatementList.class);
    if (usesList == null) {
      return;
    }
    GosuImportReferenceAnalyzer analyzer = new GosuImportReferenceAnalyzer(file);
    analyzer.analyze();
    Set<String> requiredImports = analyzer.getRequiredImports();
    Set<String> requiredPackages = new HashSet<>(requiredImports);

    for (String className : requiredImports) {
      String pckg = extractPackageName(className);
      requiredPackages.add(pckg);
    }

    List<IGosuUsesStatement> unusedImports = new ArrayList<>();

    for (IGosuUsesStatement stmt : usesList.getUsesStatements()) {
      GosuTypeLiteralImpl typeLiteral = findChildOfType(stmt, GosuTypeLiteralImpl.class);
      if (typeLiteral == null) {
        continue;
      }
      String name = typeLiteral.getText();
      boolean wildcardAccess = name.endsWith(".");
      ITypeLiteralExpression pe = typeLiteral.getParsedElement();
      if (!wildcardAccess && pe != null && pe.getType() != null && pe.getType().getType() instanceof IErrorType) {
        continue;
      }
      if (wildcardAccess) {
        if (!requiredPackages.contains(name.substring(0, name.length() - 1))) {
          unusedImports.add(stmt);
        }
      } else {
        if (!requiredImports.contains(name)) {
          unusedImports.add(stmt);
        }
      }
    }

    if (!unusedImports.isEmpty()) {
      RemoveUnnecessaryImports removeAll = new RemoveUnnecessaryImports(unusedImports);
      for (IGosuUsesStatement stmt : unusedImports) {
        Annotation ann = holder.createWarningAnnotation(stmt.getTextRange(), message("annotations.unused.import"));
        ann.setHighlightType(LIKE_UNUSED_SYMBOL);
        ann.registerFix(new RemoveUnnecessaryImports(singleton(stmt)));
        ann.registerFix(removeAll);
        ann.registerFix(new OrganizeImports());
      }
    }
  }

  private TextRange getAnnotationRange(PsiFile file, IParseIssue issue) {

    if (changeReturnTypeFixCanBeApplied(issue)) {

      // Make range for the whole return statement, like:
      // return new FooClass()
      // ^^^^^^^^^^^^^^^^^^^^^

      TextRange returnExprRange = findReturnExprRange(file, issue);
      if (returnExprRange != null) {
        return returnExprRange;
      }
    }

    int start = Objects.firstNonNull(issue.getTokenStart(), 0);
    int end = Objects.firstNonNull(issue.getTokenEnd(), 0);

    final PsiLanguageInjectionHost host = InjectedLanguageManager.getInstance(file.getProject()).getInjectionHost(file);
    if (host != null) {
      final LiteralTextEscaper<? extends PsiLanguageInjectionHost> escaper = host.createLiteralTextEscaper();
      final TextRange range = ElementManipulators.getValueTextRange(host);
      escaper.decode(range, new StringBuilder()); // It's requred to do decode for getOffsetInHost working correctly

      final int startOffset = range.getStartOffset();
      start = escaper.getOffsetInHost(start, range) - startOffset;
      end = escaper.getOffsetInHost(end, range) - startOffset;
    }
    return new TextRange(start, Math.max(start, end));
  }

  private TextRange findReturnExprRange(PsiFile psiFile, IParseIssue issue) {
    FunctionStatement func = lookupAncestor(issue.getSource(), FunctionStatement.class);
    PsiElement target = psiFile.findElementAt(issue.getTokenStart());
    GosuMethodImpl method = PsiTreeUtil.getParentOfType(target, GosuMethodImpl.class);

    if (func != null && method != null && method.getBody() != null &&
            method.getBody().getLastChild() != null) {

      PsiElement last = method.getBody().getLastChild().getPrevSibling();

      while (last != null && (last instanceof PsiWhiteSpace || last instanceof PsiCommentImpl)) {
        last = last.getPrevSibling();
      }
      if (last != null) {
        List<IReturnStatement> returnStmts = new ArrayList<>();
        func.getContainedParsedElementsByType(IReturnStatement.class, returnStmts);

        if (last != null && !returnStmts.isEmpty()) {
          IParseTree location = returnStmts.get(0).getLocation();
          if (location != null) {
            return TextRange.create(location.getOffset(), last.getTextOffset() + last.getTextLength());
          }
        }
      }
    }
    return null;
  }

  private void maybeRegisterFix(IParseIssue issue, Annotation annotation, PsiFile psiFile) {
    final ResourceKey key = issue.getMessageKey();
    final IParsedElement pe = issue.getSource();
    PsiElement target = psiFile.findElementAt(issue.getTokenStart());

    if (key == Res.MSG_LIKELY_JAVA_CAST) {
      target = getParentOfType(psiFile, target, GosuTypeAsExpressionImpl.class);
      if (target != null) {
        annotation.registerFix(new GosuChangeTypeCastFix((GosuTypeAsExpressionImpl) target));
      }
    }

    if (key == Res.MSG_OBSOLETE_CTOR_SYNTAX) {
      target = getParentOfType(psiFile, target, GosuMethodImpl.class);
      if (target != null) {
        annotation.registerFix(new ObsoleteConstructorFix((GosuMethodImpl) target));
      }
    }

    if (key == Res.MSG_INVALID_TYPE) {
      target = getParentOfType(psiFile, target, GosuTypeLiteralImpl.class);
      if (target != null) {
        final GosuTypeLiteralImpl typeLiteral = (GosuTypeLiteralImpl) target;

        if (typeLiteral.getParent() instanceof GosuNewExpressionImpl) {
          annotation.registerFix(new CreateGosuClassFromNewFix((PsiNewExpression) typeLiteral.getParent()));
          annotation.registerFix(new CreateJavaClassFromNewFix((PsiNewExpression) typeLiteral.getParent()));
        } else {
          for (GosuCreateClassKind kind : getClassKinds(typeLiteral)) {
            annotation.registerFix(new CreateClassFix(typeLiteral, kind));
          }
        }
      }
    }

    if (key == Res.MSG_MISSING_OVERRIDE_MODIFIER) {
      target = getParentOfType(psiFile, target, GosuMethodImpl.class);
      annotation.registerFix(new GosuAddMissingOverrideFix((GosuMethodBaseImpl) target));
    }

    if (issue instanceof ICoercionIssue) {
      PsiElement psiElem = findExpressionAt(psiFile, pe.getLocation().getOffset(), pe.getLocation().getExtent() + 1);
      if (psiElem instanceof IGosuPsiElement) {
        PsiType type = GosuBaseElementImpl.createType(((ICoercionIssue) issue).getTypeToCoerceTo(), psiElem);
        if (type != null) {
          annotation.registerFix(new GosuAddTypeCastFix(type, (IGosuPsiElement) psiElem));
        }
      }
    }

    if (key == Res.MSG_TYPE_MISMATCH && pe instanceof IExpression) {
      PsiElement psiElem = findExpressionAt(psiFile, pe.getLocation().getOffset(), pe.getLocation().getExtent() + 1);
      if (psiElem != null && issue.getExpectedType() != null && ((IExpression) pe).getType().isAssignableFrom(issue.getExpectedType())) {
        PsiType type = GosuBaseElementImpl.createType(issue.getExpectedType(), target);
        if (type != null) {
          annotation.registerFix(new GosuAddTypeCastFix(type, (IGosuPsiElement) psiElem));
        }
      }
    }

    // Anonymous class
    if (key == Res.MSG_UNIMPLEMENTED_METHOD) {
      if (target.getParent() instanceof GosuNewExpressionImpl) {
        final GosuAnonymousClassDefinitionImpl anonymousClass = PsiTreeUtil.getNextSiblingOfType(target, GosuAnonymousClassDefinitionImpl.class);
        if (anonymousClass != null) {
          annotation.registerFix(new GosuImplementMethodsFix(anonymousClass));
        }
      }
    }

    if (changeReturnTypeFixCanBeApplied(issue)) {
      
      Document document = PsiDocumentManager.getInstance(psiFile.getProject()).getDocument(psiFile);
      IType type = inferReturnType(pe, document);
      if (type != null && !(type instanceof IErrorType) && !"void".equals(type.getName())) {
        annotation.registerFix(new ChangeMethodTypeFix(target, type.getName()));
      }
    }

    if (key == Res.MSG_NO_SUCH_FUNCTION) {
      GosuMethodCallExpressionImpl call = PsiTreeUtil.getParentOfType(target, GosuMethodCallExpressionImpl.class);
      if (call != null) {
        annotation.registerFix(new CreateMethodFix(call,
                PsiTreeUtil.getParentOfType(call, PsiClass.class)));
      }
    }

    if (key == Res.MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION || key == Res.MSG_TYPE_MISMATCH) {
      IFunctionType functionType = null;
      IParsedElement p = pe;

      if (p.getParent() instanceof ArgumentListClause) {
        p = pe.findAncestorParsedElementByType(MethodCallExpression.class);
        if (p == null) {
          p = pe.findAncestorParsedElementByType(BeanMethodCallExpression.class);
        }
      }
      if (p instanceof MethodCallExpression) {
        functionType = ((MethodCallExpression) p).getFunctionType();
      } else if (p instanceof BeanMethodCallExpression) {
        functionType = ((BeanMethodCallExpression) p).getFunctionType();
      }
      if (functionType != null) {
        IType[] parameterTypes = functionType.getParameterTypes();
        int last = parameterTypes.length - 1;
        if( last >= 0 && parameterTypes[last].isArray() ) {
          annotation.registerFix(new HandleVarArgFix(target.getParent(), p));
        }
      }
    }

    if (key == Res.MSG_INTERFACE_REDUNDANT) {
      if(pe.getParent() instanceof CompoundTypeLiteral) {
        annotation.registerFix(new HandleInterfaceRedundantFix(target.getParent(), pe));
      }
    }

    if (key == Res.MSG_UNNECESSARY_COERCION) {
      if(pe.getParent() instanceof TypeAsExpression) {
        annotation.registerFix(new HandleUnnecessaryCoercionFix(target.getParent()));
      }
    }

    // Other cases
    target = getParentOfType(psiFile, target, IGosuCodeReferenceElement.class);
    if (target != null) {
      final IGosuCodeReferenceElement referenceElement = (IGosuCodeReferenceElement) target;
      if (hasBalancedCarets(target)) {
          if (key == Res.MSG_INVALID_TYPE || key == Res.MSG_BAD_IDENTIFIER_NAME) {
              Boolean singleLine = psiFile.getUserData(InjectedElementEditor.SINGLE_LINE_EDITOR);
              if (singleLine == null || !singleLine) {
                  annotation.registerFix(new GosuAddMissingUsesFix(referenceElement));
              }
          } else if (key == Res.MSG_UNIMPLEMENTED_METHOD) {
              annotation.registerFix(new GosuImplementMethodsFix(referenceElement));
          }
      }
    }
  }

  private boolean changeReturnTypeFixCanBeApplied(IParseIssue issue) {
    return issue.getMessageKey() == Res.MSG_RETURN_VAL_FROM_VOID_FUNCTION;
  }

  private IType inferReturnType(IParsedElement pe, Document document) {
    if (pe instanceof INoOpStatement) {
      IParseTree location = pe.getLocation();
      if (location != null) {
        String toParse = null;
        IParsedElement stmtList = pe.findAncestorParsedElementByType(IStatementList.class);
        if (stmtList != null) {
          IParseTree stmtLocation = stmtList.getLocation();

          if (stmtLocation != null) {
            List<IParseTree> children = stmtLocation.getChildren();
            ListIterator<IParseTree> it = children.listIterator(children.size());
            IParsedElement lastStmt = null;
            IParsedElement returnStmt = null;
            while (it.hasPrevious()) {
              IParseTree prev = it.previous();
              IParsedElement child = prev.getParsedElement();
              if (child != null) {
                if (lastStmt == null) {
                  lastStmt = child;
                } else {
                  if (child instanceof IReturnStatement) {
                    returnStmt = child;
                    break;
                  }
                }
              }
            }
            if (returnStmt != null && lastStmt != null) {
              IParseTree returnLoc = returnStmt.getLocation();
              IParseTree lastLoc = lastStmt.getLocation();
              if (returnLoc != null && lastLoc != null) {
                int from = returnLoc.getOffset() + returnLoc.getLength();
                int to = lastLoc.getOffset() + lastLoc.getLength();
                toParse = document.getText(TextRange.create(from, to));
              }
            }
          }
        }

        if (toParse == null) {
          toParse = location.getTextFromTokens();
        }

        toParse = toParse.trim();
//        int lastSemiitoParse.lastIndexOf(';');
//        if () {
//          
//        }
        
        try {
          IExpression exp = GosuParserFactory.createParser(toParse).parseExp(new TypelessScriptPartId("returned literal"));
          return exp.getType();
        } catch (ParseResultsException e) {
        }
      }
    } else {
      return extractFromStatement(pe);
    }
    return null;
  }

  private IType extractFromStatement(IParsedElement pe) {
    if (pe instanceof IAssignmentStatement) {
      return ((IAssignmentStatement) pe).getIdentifier().getType();
    } else if (pe instanceof IArrayAssignmentStatement) {
      return ((IArrayAssignmentStatement) pe).getArrayAccessExpression().getType();
    } else if (pe instanceof IMapAssignmentStatement) {
      return ((IMapAssignmentStatement) pe).getMapAccessExpression().getType();
    } else if (pe instanceof IMemberAssignmentStatement) {
      return ((IMemberAssignmentStatement) pe).getMemberAccess().getType();
    } else if (pe instanceof INotAStatement) {
      return ((INotAStatement) pe).getExpression().getType();
    } else if (pe instanceof IBeanMethodCallStatement) {
      return ((IBeanMethodCallStatement) pe).getBeanMethodCall().getType();
    } else if (pe instanceof IMethodCallStatement) {
      return ((IMethodCallStatement) pe).getMethodCall().getType();
    } else if (pe instanceof IHasType) {
      return ((IHasType) pe).getType();
    }
    return null;
  }

  private List<GosuCreateClassKind> getClassKinds(GosuTypeLiteralImpl typeLiteral) {
    final PsiClass psiClass = (PsiClass) getParentOfType(typeLiteral.getContainingFile(), typeLiteral, PsiClass.class);

    if (psiElement()
        .withParent(IGosuExtendsClause.class)
        .accepts(typeLiteral)) {
      return psiClass.isInterface() ? GosuCreateClassKind.INTERFACES : GosuCreateClassKind.CLASSES;
    }

    if (psiElement()
        .withParent(IGosuImplementsClause.class)
        .accepts(typeLiteral)) {
      return GosuCreateClassKind.INTERFACES;
    }

    return GosuCreateClassKind.ALL;
  }

  // TODO: use some existing utility method
  private PsiElement getParentOfType(PsiFile psiFile, PsiElement target, Class<? extends PsiElement> parentType) {
    while (target != null && !parentType.isAssignableFrom(target.getClass()) && target != psiFile) {
      target = target.getParent();
    }
    if (target == null) {
      return null;
    }
    return parentType.isAssignableFrom(target.getClass()) ? target : null;
  }

  // TODO: use some existing utility method
  private PsiElement findExpressionAt(PsiFile psiFile, int offset, int end) {
    PsiElement elem = PsiTreeUtil.findElementOfClassAtRange(psiFile, offset, end, PsiExpression.class);
    if (elem != null) {
      while (elem.getFirstChild() != null && elem.getFirstChild().getTextRange().equals(elem.getTextRange())) {
        elem = elem.getFirstChild();
      }

      while (elem != null && !(elem instanceof PsiExpression)) {
        elem = elem.getParent();
      }
    }

    return elem;
  }

  private PsiElement maybeExpandReference(PsiElement referenceElement) {
    final PsiElement parent = referenceElement.getParent();
    if (parent instanceof GosuTypeAsExpressionImpl ||
        parent instanceof GosuParenthesizedExpressionImpl) {
      referenceElement = parent;
    }
    return referenceElement;
  }

  private boolean hasBalancedCarets(PsiElement element) {
    final String text = element.getText();
    return GosuStringUtil.countMatches(text, "<") == GosuStringUtil.countMatches(text, ">");
  }

  @Override
  public boolean value(VirtualFile virtualFile) {
    return GosuFileTypes.isGosuFile(virtualFile);
  }
}
