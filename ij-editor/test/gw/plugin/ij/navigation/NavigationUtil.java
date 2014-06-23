/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.PsiElementProcessor;
import gw.plugin.ij.framework.CaretMarker;
import gw.plugin.ij.framework.FileMarkers;
import gw.plugin.ij.framework.MarkerType;
import gw.plugin.ij.framework.SmartTextRange;
import gw.plugin.ij.lang.psi.impl.statements.GosuFieldImpl;
import junit.framework.Assert;

import java.util.List;

public class NavigationUtil {

  public static void navigate(FileMarkers markers) {
    CaretMarker caret = markers.getCaret(MarkerType.CARET2);
    List<SmartTextRange> ranges = markers.getRanges();
    Assert.assertEquals("Range marker is not defined.", 1, ranges.size());

    PsiElement atCaret = caret.getFile().findElementAt(caret.offset);
    PsiElement expectedTarget = findNamedElement(atCaret);

    for (int offset = ranges.get(0).getStartOffset(); offset < ranges.get(0).getEndOffset(); offset++) {
      final PsiElement[] realTarget = new PsiElement[]{GotoDeclarationAction.findTargetElement(atCaret.getProject(), ranges.get(0).getEditor(), offset)};
      if (realTarget[0] == null) {
        PsiElementProcessor processor = new PsiElementProcessor<PsiElement>() {
          public boolean execute(PsiElement element) {
            realTarget[0] = element;
            return true;
          }
        };
        GotoDeclarationAction.chooseAmbiguousTarget(ranges.get(0).getEditor(), offset, processor, "", new PsiElement[0]);
      }
      Assert.assertNotNull("Navigation failed with ${ranges.first()} at ${offset}", realTarget);
      Assert.assertEquals(expectedTarget, realTarget[0]);
    }
  }

  public static PsiElement findNamedElement(PsiElement element) {
    if (element.getParent() instanceof GosuFieldImpl) {
      boolean asFound = false;
      for (ASTNode c : element.getParent().getNode().getChildren(null)) {
        if (c instanceof LeafPsiElement && c.getText().equals("as")) {
          asFound = true;
        }
        if (asFound && c.getPsi() == element) {
          return element;
        }
      }
    }
    while (element != null && !(element instanceof PsiNamedElement)) {
      element = element.getParent();
    }
    return element;
  }

  public static String getFQN(PsiElement psi ) {
    while (psi != null && !(psi instanceof PsiClass)) {
      psi = psi.getParent();
    }
    return ((PsiClass)psi).getQualifiedName();
  }
}
