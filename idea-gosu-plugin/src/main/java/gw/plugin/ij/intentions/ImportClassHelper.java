/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.psi.PsiType;

import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuReferenceExpression;
import gw.plugin.ij.lang.psi.api.statements.IGosuUsesStatementList;
import gw.plugin.ij.util.ClassLord;

import static gw.plugin.ij.util.ClassLord.purgeClassName;

public class ImportClassHelper {

  private final String fqn;
  private final String simpleName;
  private final IGosuPsiElement context;
  public ResolveTypeResult resolveResult;
  public FQNAnalyzeResult fqnAnalyzeResult;

  public enum ResolveTypeResult {
    RESOLVED, UNRESOLVED, CONFLICT
  }

  public ImportClassHelper(String fqn, IGosuPsiElement context) {
    this.fqn = purgeClassName(fqn);
    this.simpleName = ClassUtil.extractClassName(fqn);
    this.context = context;
  }

  public ImportClassHelper(IType targetType, IGosuPsiElement context) {
    this(targetType.getName(), context);
  }

  public boolean isResolved() {
    return ResolveTypeResult.RESOLVED.equals(resolveResult);
  }

  public boolean useFQN() {
    return resolveResult == ResolveTypeResult.CONFLICT || fqnIsPreferred();
  }

  public boolean fqnIsPreferred() {
    return fqnAnalyzeResult.fqnHits > fqnAnalyzeResult.simpleHits;
  }

  public boolean needImport() {
    return !useFQN() && resolveResult == ResolveTypeResult.UNRESOLVED;
  }

  public void resolveType() {
    resolveResult = resolveTypeInParser();
  }

  private ResolveTypeResult resolveTypeInParser() {
    IGosuClass gosuClass = getGosuClass();
    if (gosuClass != null) {
      IGosuParser parser = gosuClass.getParser();
      if( parser != null ) {
        TypeSystem.pushModule(gosuClass.getTypeLoader().getModule());
        try {
          IType type = parser.resolveTypeLiteral(simpleName).getType().getType();

          if (type instanceof IErrorType) {
            return ResolveTypeResult.UNRESOLVED;
          }

          if(fqn.equals(purgeClassName(type.getName()))) {
            return ResolveTypeResult.RESOLVED;
          } else {
            return ResolveTypeResult.CONFLICT;
          }
        }
        finally {
          TypeSystem.popModule( gosuClass.getTypeLoader().getModule() );
        }
      }
    }
    return ResolveTypeResult.UNRESOLVED;
  }


  public void analizeFQNUsing() {

    class FQNAnalyzer extends PsiRecursiveElementVisitor {

      FQNAnalyzeResult result = new FQNAnalyzeResult();

      public void visitElement(PsiElement element) {
        if (element instanceof IGosuUsesStatementList) {
          return;
        }
        if (element instanceof GosuTypeLiteralImpl) {
          PsiType type = ((GosuTypeLiteralImpl) element).getType();
          if (fqn.equals(purgeClassName(type.getCanonicalText()))) {
            if (fqn.equals(purgeClassName(element.getText()))) {
              ++result.fqnHits;
            } else {
              ++result.simpleHits;
            }
          }
          return;
        }
        if (element instanceof IGosuPsiElement) {
          IParsedElement parsedElement = ((IGosuPsiElement) element).getParsedElement();
          if (parsedElement instanceof ITypeLiteralExpression) {
            IType type = getType((ITypeLiteralExpression) parsedElement);
            if (type != null && fqn.equals(purgeClassName(type.getName()))) {
              PsiElement memberAccess = PsiTreeUtil.findChildOfType(element, IGosuReferenceExpression.class);
              if (memberAccess != null) {
                ++result.fqnHits;
              } else {
                ++result.simpleHits;
              }
              return;
            }
          }
        }
        super.visitElement(element);
      }
    }

    FQNAnalyzer fqnAnalyzer = new FQNAnalyzer();
    getFile().accept(fqnAnalyzer);
    this.fqnAnalyzeResult =  fqnAnalyzer.result;
  }

  private IType getType(ITypeLiteralExpression literalExpression) {
    IMetaType metaType = literalExpression.getType();
    if (metaType == null) {
      return null;
    }
    IType type = metaType.getType();
    if (type == null) {
      return null;
    }
    return type;
  }

  class FQNAnalyzeResult {
    int fqnHits;
    int simpleHits;
  }

  public IGosuClass getGosuClass() {
    IParsedElement pe = context.getParsedElement();
    if (pe == null) {
      return null;
    }
    IGosuClass gosuClass = pe.getGosuClass();
    if (gosuClass != null) {
      return gosuClass;
    }
    IGosuProgram gosuProgram = pe.getGosuProgram();
    if (gosuProgram == null) {
      return gosuProgram;
    }
    return null;
  }

  public void stickImport() {
    ClassLord.stickImport(fqn, getFile());
  }

  private PsiFile getFile() {
    return context.getContainingFile();
  }
}
