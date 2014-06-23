/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.debugger;

import com.intellij.debugger.impl.PositionUtil;
import com.intellij.debugger.ui.breakpoints.BreakpointWithHighlighter;
import com.intellij.debugger.ui.breakpoints.MethodBreakpoint;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.markup.MarkupEditorFilterFactory;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import gw.plugin.ij.core.IdeaReflectionUtil;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import org.jetbrains.annotations.NotNull;

/**
 * This class exists primarily to handle method breakpoints on property get/set methods
 */
public class GosuMethodBreakpoint extends MethodBreakpoint {
  private GosuMethodBreakpoint(@NotNull Project project, Document doc, int lineIndex) {
    super(project);
    RangeHighlighter highlighter = createHighlighter(project, doc, lineIndex);
    IdeaReflectionUtil.setFieldValue("myHighlighter", "k", BreakpointWithHighlighter.class, this, highlighter);
    highlighter.setEditorFilter(MarkupEditorFilterFactory.createIsNotDiffFilter());
    reload();
  }

  public static GosuMethodBreakpoint createGosuMethodBreakpoint(Project myProject, Document document, int line) {
    GosuMethodBreakpoint bp = new GosuMethodBreakpoint(myProject, document, line);
    return (GosuMethodBreakpoint) bp.init();
  }

  @Override
  protected void reload(@NotNull PsiFile psiFile) {
    super.reload(psiFile);

    // Prepend "get" or "set" if the method is a property getter or setter
    PsiMethod method = PositionUtil.getPsiElementAt(psiFile.getProject(), PsiMethod.class, getSourcePosition());
    if (method instanceof GosuMethodImpl) {
      final String fieldName = "myMethodName";
      final String obfuscatedFieldName = "s";
      if (((GosuMethodImpl) method).isForPropertyGetter()) {
        String name = (String) IdeaReflectionUtil.getFieldValue(fieldName, obfuscatedFieldName, MethodBreakpoint.class, this);
        IdeaReflectionUtil.setFieldValue(fieldName, obfuscatedFieldName, MethodBreakpoint.class, this, "get" + name);
      } else if (((GosuMethodImpl) method).isForPropertySetter()) {
        String name = (String) IdeaReflectionUtil.getFieldValue(fieldName, obfuscatedFieldName, MethodBreakpoint.class, this);
        IdeaReflectionUtil.setFieldValue(fieldName, obfuscatedFieldName, MethodBreakpoint.class, this, "set" + name);
      }
    }
  }

}
