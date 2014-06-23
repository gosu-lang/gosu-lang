/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.psi.IGosuFile;
import gw.plugin.ij.lang.psi.impl.GosuFragmentFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuProgramFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GosuProgramFileProvider implements IGosuFileTypeProvider {
  public static final String EXT_PROGRAM = "gsp";

  public static boolean isScratchpad(@NotNull VirtualFile file) {
    return isProgram(file) && file.getName().startsWith(GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME);
  }

  public static boolean isDebuggerFragement(@NotNull VirtualFile file) {
    return isProgram(file) && file.getName().contains( "Gosu_Frag" );
  }

  public static boolean isProgram(@Nullable VirtualFile file) {
    return file != null && EXT_PROGRAM.equals(file.getExtension());
  }

  @NotNull
  @Override
  public IGosuFile createGosuFile(@NotNull FileViewProvider viewProvider) {
    if (isScratchpad(viewProvider.getVirtualFile())) {
      return new GosuScratchpadFileImpl(viewProvider);
    }
    else if( isDebuggerFragement( viewProvider.getVirtualFile() ) ) {
      return new GosuFragmentFileImpl( viewProvider );
    }
    else {
      return new GosuProgramFileImpl(viewProvider);
    }
  }

  @Nullable
  @Override
  public Icon getIcon(@NotNull VirtualFile file) {
    return isScratchpad(file) ? GosuIcons.FILE_SCRATCHPAD : GosuIcons.FILE_PROGRAM;
  }

  public OpenFileDescriptor getOpenFileDescriptor(Project project, VirtualFile virtualFile, int offset) {
    return new OpenFileDescriptor(project, virtualFile, offset);
  }
}
