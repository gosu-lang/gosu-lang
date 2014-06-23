/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageRefactoringSupport;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.refactoring.changeSignature.ChangeSignatureHandler;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.GosuTokenTypes;

import java.util.ArrayList;
import java.util.List;

public class ChangeSignatureUtil {
  private ChangeSignatureUtil() {
  }

  public static <Parent extends PsiElement, Child extends PsiElement> void synchronizeList(Parent list,
                                                                                           final List<Child> newElements,
                                                                                           ChildrenGenerator<Parent, Child> generator,
                                                                                           final boolean[] shouldRemoveChild)
          throws IncorrectOperationException {

    ArrayList<Child> elementsToRemove = null;
    List<Child> elements;

    int index = 0;
    while (true) {
      elements = generator.getChildren(list);
      if (index == newElements.size()) break;

      if (elementsToRemove == null) {
        elementsToRemove = new ArrayList<Child>();
        for (int i = 0; i < shouldRemoveChild.length; i++) {
          if (shouldRemoveChild[i] && i < elements.size()) {
            elementsToRemove.add(elements.get(i));
          }
        }
      }

      Child oldElement = index < elements.size() ? elements.get(index) : null;
      Child newElement = newElements.get(index);
      if (newElement != null) {
        if (!newElement.equals(oldElement)) {
          if (oldElement != null && elementsToRemove.contains(oldElement)) {
            deleteAsParam(oldElement);
            index--;
          } else {
            assert list.isWritable() : PsiUtilBase.getVirtualFile(list);
            list.addBefore(newElement, oldElement);
            if (list.equals(newElement.getParent())) {
              deleteAsParam(newElement);
            }
          }
        }
      } else {
        if (newElements.size() > 1 && (!elements.isEmpty() || index < newElements.size() - 1)) {
          PsiElement anchor;
          if (index == 0) {
            anchor = list.getFirstChild();
          } else {
            anchor = index - 1 < elements.size() ? elements.get(index - 1) : null;
          }

          final ASTNode astNode = list.getNode();
          anchor = anchor.getNextSibling();
          if (anchor != null) {
            astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", anchor.getNode());
          } else {
            astNode.addLeaf(GosuTokenTypes.TT_OP_comma, ",", null);
          }
        }
      }
      index++;
    }
    for (int i = newElements.size(); i < elements.size(); i++) {
      deleteAsParam(elements.get(i));
    }
  }

  public static void deleteAsParam(PsiElement param) {
    PsiElement last = param;
    PsiElement first;
    PsiElement d = last;
    do {
      first = d;
      d = d.getPrevSibling();
    } while (d != null && notParam(d));

    if (first == last) {
      d = last;
      do {
        last = d;
        d = d.getNextSibling();
      } while (d != null && notParam(d));
    }
    last.getParent().deleteChildRange(first, last);
  }

  private static boolean notParam(PsiElement d) {
    return d instanceof PsiWhiteSpace || ",".equals(d.getText());
  }

  public static void invokeChangeSignatureOn(PsiMethod method, Project project) {
    final ChangeSignatureHandler handler =
            LanguageRefactoringSupport.INSTANCE.forLanguage(method.getLanguage()).getChangeSignatureHandler();
    handler.invoke(project, new PsiElement[]{method}, null);
  }

  public interface ChildrenGenerator<Parent extends PsiElement, Child extends PsiElement> {
    List<Child> getChildren(Parent parent);
  }
}
