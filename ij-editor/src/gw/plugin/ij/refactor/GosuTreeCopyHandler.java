/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.TreeCopyHandler;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.IElementType;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodBaseImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;

import java.util.Map;

public class GosuTreeCopyHandler implements TreeCopyHandler {
  @Override
  public void encodeInformation(TreeElement element, ASTNode original, Map<Object, Object> encodingState) {
    IElementType elementType = element.getElementType();
    if (elementType == GosuElementTypes.METHOD_DEFINITION) {
      PsiElement psi = element.getPsi();
      GosuMethodImpl originalPsi = (GosuMethodImpl) original.getPsi();
      psi.putUserData(GosuMethodBaseImpl.ORIGINAL_CONSTRUCTOR, originalPsi);
    }
  }

  @Override
  public TreeElement decodeInformation(TreeElement element, Map<Object, Object> decodingState) {
    return null;
  }
}
