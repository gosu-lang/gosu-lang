/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.plugin.ij.debugger.evaluation;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionService;
import com.intellij.codeInsight.completion.JavaCompletionUtil;
import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.codeinsight.RuntimeTypeEvaluator;
import com.intellij.debugger.engine.evaluation.CodeFragmentFactory;
import com.intellij.debugger.engine.evaluation.TextWithImports;
import com.intellij.debugger.engine.evaluation.expression.EvaluatorBuilder;
import com.intellij.debugger.impl.DebuggerContextImpl;
import com.intellij.debugger.impl.DebuggerSession;
import com.intellij.debugger.ui.DebuggerExpressionComboBox;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.PairFunction;
import com.intellij.util.concurrency.Semaphore;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class GosuCodeFragmentFactory extends CodeFragmentFactory {
  private static final class SingletonHolder {
    public static final GosuCodeFragmentFactory ourInstance = new GosuCodeFragmentFactory();
  }

  public static GosuCodeFragmentFactory getInstance() {
    return SingletonHolder.ourInstance;
  }

  public JavaCodeFragment createPresentationCodeFragment( final TextWithImports item, final PsiElement context, final Project project ) {
    return createCodeFragment( item, context, project );
  }

  public JavaCodeFragment createCodeFragment( TextWithImports item, PsiElement context, final Project project ) {
    final String text = item.getText();

    final GosuFragmentFileImpl fragment = createExpressionCodeFragment( context, project, text );

    if( item.getImports().length() > 0 ) {
      fragment.addImportsFromString( item.getImports() );
    }
    fragment.setVisibilityChecker( JavaCodeFragment.VisibilityChecker.EVERYTHING_VISIBLE );
    //noinspection HardCodedStringLiteral
    fragment.putUserData( DebuggerExpressionComboBox.KEY, "DebuggerComboBoxEditor.IS_DEBUGGER_EDITOR" );
    fragment.putCopyableUserData( JavaCompletionUtil.DYNAMIC_TYPE_EVALUATOR, new PairFunction<PsiExpression, CompletionParameters, PsiType>() {
      public PsiType fun( PsiExpression expression, CompletionParameters parameters ) {
        if( !RuntimeTypeEvaluator.isSubtypeable( expression ) ) {
          return null;
        }

        if( parameters.getInvocationCount() <= 1 && containsMethodCalls( expression ) ) {
          final CompletionService service = CompletionService.getCompletionService();
          if( service.getAdvertisementText() == null && parameters.getInvocationCount() < 2 ) {
            service.setAdvertisementText( "Invoke completion once more to see runtime type variants" );
          }
          return null;
        }

        final DebuggerContextImpl debuggerContext = DebuggerManagerEx.getInstanceEx( project ).getContext();
        DebuggerSession debuggerSession = debuggerContext.getDebuggerSession();
        if( debuggerSession != null ) {
          final Semaphore semaphore = new Semaphore();
          semaphore.down();
          final AtomicReference<PsiClass> nameRef = new AtomicReference<>();
          final RuntimeTypeEvaluator worker =
            new RuntimeTypeEvaluator( null, expression, debuggerContext, ProgressManager.getInstance().getProgressIndicator() ) {
              @Override
              protected void typeCalculationFinished( @Nullable PsiClass type ) {
                nameRef.set( type );
                semaphore.up();
              }
            };
          debuggerContext.getDebugProcess().getManagerThread().invoke( worker );
          for( int i = 0; i < 50; i++ ) {
            ProgressManager.checkCanceled();
            if( semaphore.waitFor( 20 ) ) {
              break;
            }
          }
          final PsiClass psiClass = nameRef.get();
          if( psiClass != null ) {
            return JavaPsiFacade.getElementFactory( project ).createType( psiClass );
          }
        }
        return null;
      }
    } );

    return fragment;
  }

  public static boolean containsMethodCalls(@Nullable final PsiElement qualifier) {
    if (qualifier == null) return false;
    if (qualifier instanceof PsiMethodCallExpression || qualifier instanceof PsiNewExpression) return true;
    if (qualifier instanceof PsiArrayAccessExpression) {
      return containsMethodCalls(((PsiArrayAccessExpression)qualifier).getArrayExpression());
    }
    return containsMethodCalls(getQualifier(qualifier));
  }

  @Nullable
  static PsiElement getQualifier(final PsiElement element) {
    return element instanceof PsiJavaCodeReferenceElement ? ((PsiJavaCodeReferenceElement)element).getQualifier() : null;
  }

  private GosuFragmentFileImpl createExpressionCodeFragment( PsiElement context, Project project, String text ) {
    return new GosuFragmentFileImpl( project, text, context );
  }

  public boolean isContextAccepted( PsiElement contextElement ) {
    return contextElement == null ||
           contextElement.getContainingFile() instanceof AbstractGosuClassFileImpl;
  }

  public LanguageFileType getFileType() {
    return GosuCodeFileType.INSTANCE;
  }

  @Override
  public EvaluatorBuilder getEvaluatorBuilder() {
    return GosuEvaluatorBuilderImpl.instance();
  }
}
