/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.intellij.debugger.engine.DebuggerUtils;
import com.intellij.debugger.engine.evaluation.CodeFragmentKind;
import com.intellij.debugger.engine.evaluation.TextWithImports;
import com.intellij.debugger.engine.evaluation.TextWithImportsImpl;
import com.intellij.debugger.impl.EditorTextProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiEnumConstant;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiStatement;
import com.intellij.psi.PsiThisExpression;
import com.intellij.psi.PsiVariable;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import org.jetbrains.annotations.Nullable;

/**
 */
public class GosuEditorTextProvider implements EditorTextProvider {
  private static final Logger LOG = Logger.getInstance( GosuEditorTextProvider.class );

  @Override
  public TextWithImports getEditorText( PsiElement elementAtCaret ) {
    String result = null;
    PsiElement element = findExpression( elementAtCaret );
    if( element != null ) {
      if( element instanceof GosuReferenceExpressionImpl ) {
        final GosuReferenceExpressionImpl reference = (GosuReferenceExpressionImpl)element;
        if( reference.getQualifier() == null ) {
          final PsiElement resolved = reference.resolve();
          if( resolved instanceof PsiEnumConstant ) {
            final PsiEnumConstant enumConstant = (PsiEnumConstant)resolved;
            final PsiClass enumClass = enumConstant.getContainingClass();
            if( enumClass != null ) {
              result = enumClass.getName() + "." + enumConstant.getName();
            }
          }
        }
      }
      if( result == null ) {
        result = element.getText();
      }
    }
    return result != null ? new TextWithImportsImpl( CodeFragmentKind.EXPRESSION, result ) : null;
  }

  @Nullable
  private static PsiElement findExpression( PsiElement element ) {
    if( !(element instanceof PsiIdentifier || element instanceof PsiKeyword) ) {
      return null;
    }
    PsiElement parent = element.getParent();
    if( parent instanceof PsiVariable ) {
      return element;
    }
    if( parent instanceof GosuReferenceExpressionImpl ) {
      if( parent.getParent() instanceof PsiCallExpression ) {
        return parent.getParent();
      }
      return parent;
    }
    if( parent instanceof PsiThisExpression ) {
      return parent;
    }
    return null;
  }

  @Nullable
  public Pair<PsiElement, TextRange> findExpression( PsiElement element, boolean allowMethodCalls ) {
    if( !(element instanceof PsiIdentifier || element instanceof PsiKeyword) ) {
      return null;
    }

    PsiElement expression = null;
    PsiElement parent = element.getParent();
    if( parent instanceof PsiVariable ) {
      expression = element;
    }
    else if( parent instanceof GosuReferenceExpressionImpl ) {
      final PsiElement pparent = parent.getParent();
      if( pparent instanceof PsiCallExpression ) {
        parent = pparent;
      }
      if( allowMethodCalls || !DebuggerUtils.hasSideEffects( parent ) ) {
        expression = parent;
      }
    }
    else if( parent instanceof PsiThisExpression ) {
      expression = parent;
    }

    if( expression != null ) {
      try {
        PsiElement context = element;
        if( parent instanceof PsiParameter ) {
          try {
            context = ((PsiMethod)((PsiParameter)parent).getDeclarationScope()).getBody();
          }
          catch( Throwable ignored ) {
          }
        }
        else {
          while( context != null && !(context instanceof PsiStatement) && !(context instanceof PsiClass) ) {
            context = context.getParent();
          }
        }
        TextRange textRange = expression.getTextRange();
        PsiElement psiExpression = GosuPsiParseUtil.parseExpression( expression.getText(), element.getManager() );
        return Pair.create( psiExpression, textRange );
      }
      catch( IncorrectOperationException e ) {
        LOG.debug( e );
      }
    }
    return null;
  }
}
