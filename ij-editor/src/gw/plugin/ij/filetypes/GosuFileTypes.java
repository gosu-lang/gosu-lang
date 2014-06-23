/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuFileTypes {

  public static boolean isGosuFile(@Nullable PsiFile file) {
    return file != null && isGosuFile(file.getVirtualFile());
  }

  public static boolean isGosuFile(@Nullable VirtualFile file) {
    return file != null && file.getFileType() instanceof GosuCodeFileType;
  }

  public static boolean isTopLevelGosuFile(@Nullable PsiFile file) {
    if (file == null) {
      return false;
    }
    PsiFile parentFile = file.getUserData(InjectedElementEditor.ORIGINAL_PSI_FILE);
    if (parentFile != null) {
      return isTopLevelGosuFile(parentFile.getVirtualFile());
    } else {
      return isTopLevelGosuFile(file.getVirtualFile());
    }
  }

  public static boolean isTopLevelGosuFile(@NotNull VirtualFile file) {
    if (file instanceof VirtualFileWindow) {
      return isTopLevelGosuFile(((VirtualFileWindow) file).getDelegate());
    }
    return file != null && isGosuFile(file);
  }

}
