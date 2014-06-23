/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger.actions;

import com.intellij.debugger.SourcePosition;
import com.intellij.debugger.actions.JavaSmartStepIntoHandler;
import com.intellij.debugger.engine.JVMNameUtil;
import com.intellij.debugger.engine.RequestHint;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import gw.plugin.ij.lang.GosuLanguage;

/**
 */
public class GosuSmartStepIntoHandler extends JavaSmartStepIntoHandler {
  @Override
  public boolean isAvailable( final SourcePosition position ) {
    final PsiFile file = position.getFile();
    return file.getLanguage().isKindOf( GosuLanguage.instance() );
  }

  @Override
  protected RequestHint.SmartStepFilter getSmartStepFilter( PsiMethod psiMethod ) {
    return new RequestHint.SmartStepFilter( JVMNameUtil.getJVMQualifiedName( psiMethod.getContainingClass() ),
                                            psiMethod.isConstructor() ? "<init>" : psiMethod.getName(),
                                            //## todo: generate a signature that includes Goso-specific hidden arguments
                                            //## todo: such as type vars, IExternalSymbol map for program ctors, etc.
                                            JVMNameUtil.getJVMSignature( psiMethod ) );
  }
}

