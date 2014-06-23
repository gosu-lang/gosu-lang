/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.lang.reflect.IProvidesCustomErrorInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.FileUtil;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.TypeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.WeakHashMap;

public class GosuOpenTypeAnnotator implements Annotator, Condition<VirtualFile> {
  private static final Map<Project, GosuOpenTypeAnnotator> INSTANCES = new WeakHashMap<>();

  private final Project _project;

  public static GosuOpenTypeAnnotator instance( Project project ) {
    GosuOpenTypeAnnotator instance = INSTANCES.get( project );
    if( instance == null ) {
      INSTANCES.put( project, instance = new GosuOpenTypeAnnotator( project ) );
    }
    return instance;
  }

  public GosuOpenTypeAnnotator( Project project ) {
    _project = project;
  }

  @Override
  public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    final PsiFile psiFile = psiElement.getContainingFile();
    if (psiFile != null) {
      final VirtualFile virtualFile = psiFile.getVirtualFile();
      if (virtualFile != null) {
        final IModule module = GosuModuleUtil.findModuleForPsiElement(psiElement);
        if (module != null) {
          TypeSystem.pushModule(module);
          try {
            final String[] types = TypeSystem.getTypesForFile(module, FileUtil.toIFile(virtualFile));
            for (String typeName : types) {
              IType type = TypeSystem.getByFullNameIfValid(typeName);
              if (type instanceof IProvidesCustomErrorInfo) {
                for (IProvidesCustomErrorInfo.CustomErrorInfo info : ((IProvidesCustomErrorInfo) type).getCustomErrors()) {
                  final IProvidesCustomErrorInfo.ErrorLevel level = info.getLevel();
                  final TextRange range = new TextRange(info.getStart(), info.getEnd());
                  final String message = info.getMessage();

                  if (level == IProvidesCustomErrorInfo.ErrorLevel.ERROR) {
                    annotationHolder.createErrorAnnotation(range, message);
                  } else if (level == IProvidesCustomErrorInfo.ErrorLevel.WARNING) {
                    annotationHolder.createWarningAnnotation(range, message);
                  } else if (level == IProvidesCustomErrorInfo.ErrorLevel.INFO) {
                    annotationHolder.createInfoAnnotation(range, message);
                  }
                }
              }
            }
          } finally {
            TypeSystem.popModule(module);
          }
        }
      }
    }
  }

  @Override
  public boolean value(VirtualFile file) {
    return !TypeUtil.getTypesForFile(GosuModuleUtil.getGlobalModule(_project), file).isEmpty();
  }
}
