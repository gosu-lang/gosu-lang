/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import gw.fs.IFile;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CompiledMessage implements Serializable {
  public final CompilationItem item;
  public final int compilationTime;
  public final boolean successfully;

  public final Set<IFile> dependencies;

  public final Set<String> displayKeyDependencies;

  public final long fingerprint;

  public CompiledMessage(CompilationItem item, int compilationTime, boolean successfully, Set<IFile> dependencies, Set<String> displayKeyDependencies, long fingerprint) {
    this.item = item;
    this.compilationTime = compilationTime;
    this.successfully = successfully;

    this.dependencies = new HashSet<IFile>(dependencies);
    this.displayKeyDependencies = new HashSet<>(displayKeyDependencies);
    this.fingerprint = fingerprint;
  }

  @Override
  public String toString() {
    return String.format("CompiledMessage: %s, success: %s, time: %dms", item, successfully, compilationTime);
  }
}
