/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetemplates;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import gw.plugin.ij.filetypes.GosuCodeFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Custom logic to suppress gosu template types from showing up in the pull down action menu
 *
 * @author pfong
 */
public class GosuCreateFromTemplateHandler extends DefaultCreateFromTemplateHandler {
  public boolean handlesTemplate(@NotNull FileTemplate template) {
    final FileType type = FileTypeManagerEx.getInstanceEx().getFileTypeByExtension(template.getExtension());
    return type instanceof GosuCodeFileType;
  }

  public boolean canCreate(PsiDirectory[] dirs) {
    return false;
  }

  public static boolean canCreate(@NotNull PsiDirectory dir) {
    return JavaDirectoryService.getInstance().getPackage(dir) != null;
  }
}
