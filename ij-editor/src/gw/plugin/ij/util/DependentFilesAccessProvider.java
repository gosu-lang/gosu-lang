/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.collect.Lists;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.WritingAccessProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DependentFilesAccessProvider extends WritingAccessProvider {
  @NotNull
  @Override
  public Collection<VirtualFile> requestWriting(@NotNull VirtualFile... files) {
    final List<VirtualFile> denied = Lists.newArrayList();
    for (VirtualFile file : files) {
      IFileAccessRequestor requestor = file.getUserData(IFileAccessRequestor.FILE_ACCESS_REQUESTOR);
      if (requestor != null) {
        Collections.addAll(denied, requestor.ensureFilesWritable(file));
      }
    }
    return denied;
  }

  @Override
  public boolean isPotentiallyWritable(@NotNull VirtualFile file) {
    return true;
  }
}
