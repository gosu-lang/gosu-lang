package gw.plugin.ij.intentions;


import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiMatcherImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiMatchers.hasClass;


public class GosuRemoveWildcardFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  private String _typeParam;

  public GosuRemoveWildcardFix(GosuTypeLiteralImpl typeLiteral, String typeParam) {
    super(typeLiteral);
    _typeParam = typeParam;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.wildcard");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.upgrade.issues");
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())||
        !(startElement instanceof GosuTypeLiteralImpl)) {
      return;
    }

    PsiElement stub = GosuPsiParseUtil.parseTypeLiteral("Dummy<" + _typeParam + ">", file);
    PsiElement tl = new PsiMatcherImpl(stub).descendant(hasClass(GosuTypeLiteralImpl.class))
                          .getElement();

    if(tl != null) {
      PsiElement prev = startElement.getPrevSibling();
      PsiElement toDeleteEnd = prev;
      PsiElement toDeleteStart = null;
      PsiElement parent = startElement.getParent();
      while(prev != null) {
        if( "?".equals(prev.getText()))
        {
          toDeleteStart = prev;
          break;
        }
        prev = prev.getPrevSibling();
      }
      if(toDeleteStart != null) {
        parent.deleteChildRange(toDeleteStart, toDeleteEnd);
        startElement.replace(tl);
      }
    }
  }


  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuTypeLiteralImpl;
  }

}

