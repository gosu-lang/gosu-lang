/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;

public class LightVirtualFileWithModule
{
  private static final Key<IModule> LIGHT_VIRTUAL_FILE_MODULE = Key.create("LIGHT_VIRTUAL_FILE_MODULE");

  @NotNull
  public static LightVirtualFile attachModule(@NotNull LightVirtualFile file, IModule module) {
    file.putUserData(LIGHT_VIRTUAL_FILE_MODULE, checkNotNull(module));
    return file;
  }

  @Nullable
  public static IModule getModule(@NotNull VirtualFile file) {
    return file.getUserData(LIGHT_VIRTUAL_FILE_MODULE);
  }

  @NotNull
  public static LightVirtualFile create(IModule module) {
    return attachModule(new LightVirtualFile(), module);
  }

  @NotNull
  public static LightVirtualFile create(@NonNls String name, IModule module) {
    return attachModule(new LightVirtualFile(name), module);
  }

  @NotNull
  public static LightVirtualFile create(@NonNls String name, CharSequence content, IModule module) {
    return attachModule(new LightVirtualFile(name, content), module);
  }

  @NotNull
  public static LightVirtualFile create(final String name, final FileType fileType, final CharSequence text, IModule module) {
    return attachModule(new LightVirtualFile(name, fileType, text), module);
  }

  @NotNull
  public static LightVirtualFile create(VirtualFile original, final CharSequence text, long modificationStamp, IModule module) {
    return attachModule(new LightVirtualFile(original, text, modificationStamp), module);
  }

  @NotNull
  public static LightVirtualFile create(final String name, final FileType fileType, final CharSequence text, final long modificationStamp, IModule module) {
    return attachModule(new LightVirtualFile(name, fileType, text, modificationStamp), module);
  }

  @NotNull
  public static LightVirtualFile create(final String name, final FileType fileType, final CharSequence text, Charset charset, final long modificationStamp, IModule module) {
    return attachModule(new LightVirtualFile(name, fileType, text, charset, modificationStamp), module);
  }
}
