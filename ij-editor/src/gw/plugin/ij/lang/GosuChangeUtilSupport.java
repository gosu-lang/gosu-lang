/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiMember;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.TreeCopyHandler;
import com.intellij.psi.impl.source.tree.TreeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GosuChangeUtilSupport implements TreeCopyHandler {
  private static final Key<PsiMember> REFERENCED_MEMBER_KEY = Key.create("REFERENCED_MEMBER_KEY");

  public void encodeInformation(final TreeElement element, final ASTNode original, final Map<Object, Object> encodingState) {
//    if (original instanceof CompositeElement) {
//      if (original.getElementType() == GosuElementTypes.ELEM_TYPE_IdentifierExpression ||
//          original.getElementType() == GosuElementTypes.ELEM_TYPE_TypeLiteral
//          ) {
//        final IGosuResolveResult result = ((GosuReferenceExpressionImpl)original.getPsi()).advancedResolve();
//        if (result != null) {
//          final PsiElement target = result.getElement();
//
//          if (target instanceof PsiClass ||
//            (target instanceof PsiMethod || target instanceof PsiField) &&
//                   ((PsiMember) target).hasModifierProperty(PsiModifier.STATIC) &&
//                    result.getCurrentFileResolveContext() instanceof GrImportStatement) {
//            element.putCopyableUserData(REFERENCED_MEMBER_KEY, (PsiMember) target);
//          }
//        }
//      }
//    }
  }

  @Nullable
  public TreeElement decodeInformation(@NotNull TreeElement element, final Map<Object, Object> decodingState) {
    if (element instanceof CompositeElement) {
      if (element.getElementType() == GosuElementTypes.ELEM_TYPE_IdentifierExpression ||
          element.getElementType() == GosuElementTypes.ELEM_TYPE_TypeLiteral) {
//        GosuReferenceExpressionImpl ref = (GosuReferenceExpressionImpl) SourceTreeToPsiMap.treeElementToPsi(element);
//        final PsiMember refMember = element.getCopyableUserData(REFERENCED_MEMBER_KEY);
//        if (refMember != null) {
//          element.putCopyableUserData(REFERENCED_MEMBER_KEY, null);
//          PsiElement refElement1 = ref.resolve();
//          if (!refMember.getManager().areElementsEquivalent(refMember, refElement1)) {
//            try {
//              if (!(refMember instanceof PsiClass) || ref.getQualifier() == null) {
//                // can restore only if short (otherwise qualifier should be already restored)
//                ref = (GosuReferenceExpressionImpl) ref.bindToElement(refMember);
//              }
//            } catch (IncorrectOperationException ignored) {
//            }
//            return (TreeElement) SourceTreeToPsiMap.psiElementToTree(ref);
//          } else {
//            // shorten references to the same package and to inner classes that can be accessed by short name
//            new ReferenceAdjuster(true, false).collect(element, false, false);
//          }
//        }
        return element;
      }
    }
    return null;
  }

}