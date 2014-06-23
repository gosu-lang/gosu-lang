/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;

public interface IFileAccessRequestor {
  public static Key<IFileAccessRequestor> FILE_ACCESS_REQUESTOR = new Key<>("FILE_ACCESS_REQUESTOR");

  /**
   * @param file
   * @return list of denided files
   */
  VirtualFile[] ensureFilesWritable(VirtualFile file);
}
