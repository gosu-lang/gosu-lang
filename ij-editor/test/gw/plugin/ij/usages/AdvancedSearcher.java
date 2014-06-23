/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.intellij.find.FindManager;
import com.intellij.find.findUsages.FindUsagesManager;
import com.intellij.find.findUsages.FindUsagesOptions;
import com.intellij.find.findUsages.JavaFindUsagesHandler;
import com.intellij.find.impl.FindManagerImpl;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public  class AdvancedSearcher {

  @NotNull
  public static List<PsiElement> search(@NotNull PsiElement elementAt) {
    FindUsagesManager findUsagesManager = ((FindManagerImpl) FindManager.getInstance(elementAt.getProject())).getFindUsagesManager();
    JavaFindUsagesHandler handler = (JavaFindUsagesHandler) findUsagesManager.getFindUsagesHandler(elementAt, false);
    final List<PsiElement> elements = new ArrayList<>();
    handler.processElementUsages(elementAt, new Processor<UsageInfo>() {
      public boolean process(@NotNull UsageInfo usageInfo) {
        PsiElement element = usageInfo.getElement();
        if(!(element instanceof PsiFile)) {
          elements.add(element);
        }
        return true;
      }
    }, new FindUsagesOptions(elementAt.getProject()));
    return elements;
  }


}
