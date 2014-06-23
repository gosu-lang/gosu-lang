/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.doc;

import com.intellij.lang.ASTNode;
import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

public class EscapingQuickNavigateInfoProvider extends AbstractDocumentationProvider {

  public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    ASTNode valueNode = element.getNode();
    if (valueNode != null) {
      String info = "\n\"" + escape(valueNode.getText()) + "\"";
      PsiFile file = element.getContainingFile();
      if (file != null) {
        info += " [" + file.getName() + "]";
      }
      return info;
    }
    return null;
  }

  private String escape(String string) {
    if (string == null || string.length() == 0) {
      return string;
    }
    StringBuilder resultBuffer = null;
    for (int i = 0, length = string.length(); i < length; i++) {
      String entity = null;
      char ch = string.charAt(i);
      switch (ch) {
        case '<': {
          entity = "&lt;";
          break;
        }
        case '>':
          entity = "&gt;";
          break;
        case '&':
          entity = "&amp;";
          break;
        case '"':
          entity = "&quot;";
          break;
        default:
          break;
      }
      if (entity != null) {
        if (resultBuffer == null) {
          resultBuffer = new StringBuilder(string);
          resultBuffer.setLength(i);
        }
        resultBuffer.append(entity);
      } else if (resultBuffer != null) {
        resultBuffer.append(ch);
      }
    }
    return (resultBuffer != null) ? resultBuffer.toString() : string;
  }

}
