/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInspection.expression;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLocalVariable;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.scope.ElementClassHint;
import org.jetbrains.annotations.NotNull;

/**
 */
public class FindLocalVarInScope extends BaseScopeProcessor implements ElementClassHint {
  private String _name;
  private PsiLocalVariable _localFound;

  public FindLocalVarInScope( String name ) {
    _name = name;
  }

  public PsiLocalVariable getLocalFound() {
    return _localFound;
  }

  @Override
  public boolean shouldProcess( DeclarationKind kind ) {
    return kind == DeclarationKind.VARIABLE;
  }

  @Override
  public boolean execute( @NotNull PsiElement pe, ResolveState state ) {
    if( pe instanceof PsiLocalVariable ) {
      PsiLocalVariable local = (PsiLocalVariable)pe;
      String lname = local.getName();
      if( lname.equalsIgnoreCase( _name ) ) {
        _localFound = local;
        return false;
      }
    }
    return true;
  }

  @Override
  public <T> T getHint( @NotNull Key<T> hintKey ) {
    if( hintKey == ElementClassHint.KEY ) {
      return (T)this;
    }
    return super.getHint( hintKey );
  }
}