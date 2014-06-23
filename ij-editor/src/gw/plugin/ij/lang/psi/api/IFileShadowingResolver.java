/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api;

import com.intellij.openapi.vfs.VirtualFile;
import gw.fs.IFile;
import org.jetbrains.annotations.NotNull;


public interface IFileShadowingResolver {
  IFile resolveType(@NotNull VirtualFile file);
}
