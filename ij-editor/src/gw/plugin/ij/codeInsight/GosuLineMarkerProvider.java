/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInsight;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzerSettings;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.impl.JavaLineMarkerProvider;
import com.intellij.codeInsight.daemon.impl.MarkerType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiIdentifier;
import gw.internal.gosu.parser.DynamicFunctionSymbol;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.lang.parser.IParsedElement;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuLineMarkerProvider extends JavaLineMarkerProvider {
  public GosuLineMarkerProvider( DaemonCodeAnalyzerSettings daemonSettings, EditorColorsManager colorsManager ) {
    super( daemonSettings, colorsManager );
  }

  @Override
  public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement element ) {
    try {
      LineMarkerInfo lmi = super.getLineMarkerInfo( element );
      if( lmi != null ) {
        return lmi;
      }

      return handleFunnyCasesInJavaHierarchy( element );
    }
    catch( ProcessCanceledException pce ) {
      return null;
    }
    catch (IndexNotReadyException e) {
      return null;
    }
    catch( RuntimeException pce ) {
      if( pce.getCause() instanceof ProcessCanceledException ) {
        return null;
      }
    }
    return null;
  }

  // Handle odd cases where param types may not be directly equal in IJ's eyes
  // e.g., Gosu: Collection<Object> Java: Collection<? extends Object>
  private LineMarkerInfo handleFunnyCasesInJavaHierarchy( PsiElement element ) {
    LineMarkerInfo lmi = null;
    if( element instanceof PsiIdentifier && element.getParent() instanceof GosuMethodImpl ) {
      GosuMethodImpl method = (GosuMethodImpl)element.getParent();
      IParsedElement pe = method.getParsedElement();
      if( pe instanceof FunctionStatement ) {
        FunctionStatement funcStmt = (FunctionStatement)pe;
        DynamicFunctionSymbol dfs = funcStmt.getDynamicFunctionSymbol();
        if (dfs != null) {
          DynamicFunctionSymbol superDfs = dfs.getSuperDfs();
          if (superDfs != null) {
            final Icon icon = superDfs.getDeclaringTypeInfo().getOwnersType().isInterface() ? AllIcons.Gutter.OverridingMethod : AllIcons.Gutter.ImplementingMethod;
            final MarkerType type = MarkerType.OVERRIDING_METHOD;
            lmi = new LineMarkerInfo<>(element, element.getTextRange(), icon, Pass.UPDATE_ALL, type.getTooltip(), type.getNavigationHandler(), GutterIconRenderer.Alignment.LEFT);
          }
        }
      }
    }
    return lmi;
  }
}
