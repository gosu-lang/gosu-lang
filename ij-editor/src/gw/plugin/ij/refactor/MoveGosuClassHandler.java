/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.move.moveClassesOrPackages.MoveJavaClassHandler;
import com.intellij.refactoring.util.NonCodeUsageInfo;
import com.intellij.usageView.UsageInfo;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;

import java.util.Collection;
import java.util.Iterator;

public class MoveGosuClassHandler extends MoveJavaClassHandler {

  @Override
  public void preprocessUsages(Collection<UsageInfo> results) {
    Iterator<UsageInfo> it = results.iterator();
    while(it.hasNext()) {
      UsageInfo u = it.next();
      if(u instanceof NonCodeUsageInfo) {
        NonCodeUsageInfo info = (NonCodeUsageInfo) u;
        PsiElement referencedElement = info.getReferencedElement();
        if(referencedElement instanceof GosuClassDefinitionImpl &&
           info.newText.endsWith(((GosuClassDefinitionImpl) referencedElement).getName())) {
          it.remove();
        }
      }
    }
    super.preprocessUsages(results);
  }
}
