/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FileDependencyInfo {
  private final VirtualFile file;
  private final Set<VirtualFile> dependencies;
  private final Set<String> displayKeys;
  private final long fingerprint;
  private final int compileTime; // milliseconds

  public FileDependencyInfo(VirtualFile file, Set<VirtualFile> dependencies, Set<String> displayKeys, long fingerprint, int compileTime) {
    this.file = file;
    this.dependencies = ImmutableSet.copyOf(Sets.difference(dependencies, ImmutableSet.of(file)));
    this.displayKeys = ImmutableSet.copyOf(displayKeys);
    this.fingerprint = fingerprint;
    this.compileTime = compileTime;
  }

  public VirtualFile getFile() {
    return file;
  }

  public Set<VirtualFile> getDependencies() {
    return dependencies;
  }

  public Set<String> getDisplayKeys() {
    return displayKeys;
  }

  public boolean doesDependOn(VirtualFile file) {
    return dependencies.contains(file);
  }

  public long getFingerprint() {
    return fingerprint;
  }

  public int getCompileTime() {
    return compileTime;
  }

  // Save/Load
  @Nullable
  private static VirtualFile findFile(String path) {
    return LocalFileSystem.getInstance().findFileByIoFile(new File(path));
  }

  public static void write(@NotNull FileDependencyInfo info, @NotNull DataOutputStream out) throws IOException {
    out.writeUTF(info.file.getPath());
    out.writeLong(info.fingerprint);
    out.writeInt(info.compileTime);
    out.writeInt(info.dependencies.size());
    for (VirtualFile dependent : info.dependencies) {
      out.writeUTF(dependent.getPath());
    }
    out.writeInt(info.displayKeys.size());
    for (String key : info.displayKeys) {
      out.writeUTF(key);
    }
  }

  @Nullable
  public static FileDependencyInfo read(@NotNull DataInputStream in) throws IOException {
    final VirtualFile file = findFile(in.readUTF());
    final long fingerprint = in.readLong();
    final int compileTime = in.readInt();
    final Set<VirtualFile> dependents = Sets.newHashSet();
    final Set<String> displayKeys = Sets.newHashSet();
    int size = in.readInt();
    for (int i = 0; i < size; ++i) {
      final VirtualFile dependent = findFile(in.readUTF());
      if (dependent != null) {
        dependents.add(dependent);
      }
    }

    size = in.readInt();
    for (int i = 0; i < size; ++i) {
      final String key = in.readUTF();
      if (key != null) {
        displayKeys.add(key);
      }
    }
    return file != null ? new FileDependencyInfo(file, dependents, displayKeys, fingerprint, compileTime) : null;
  }
}