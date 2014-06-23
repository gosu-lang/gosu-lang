/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.compiled.ClassFileStubBuilder;
import com.intellij.psi.impl.java.stubs.JavaStubElementTypes;
import com.intellij.psi.impl.java.stubs.PsiClassStub;
import com.intellij.psi.stubs.StubElement;
import gw.plugin.ij.filetypes.GosuCodeFileType;

import java.util.Set;

/**
 * We don't want Java .class file indexer to index .class files produced by Gosu compiler.
 * For now, we just check if there is a Gosu file with the same name near the .class file.
 * If so, do not index .class file.
 */
public class ClassFileStubBuilderWrapper extends ClassFileStubBuilder {

  private final Set<String> extensions;

  public ClassFileStubBuilderWrapper() {
    extensions = ImmutableSet.copyOf(GosuCodeFileType.INSTANCE.getExtensions());
  }

  @Override
  public StubElement buildStubTree(VirtualFile file, byte[] content, Project project) {
    StubElement stub = super.buildStubTree(file, content, project);
    if (stub != null) {
      PsiClassStub<PsiClass> classStub = (PsiClassStub<PsiClass>) stub.findChildStubByType(JavaStubElementTypes.CLASS);
      if (classStub != null && classStub.getSourceFileName() != null) {
        String ext = Files.getFileExtension(classStub.getSourceFileName());
        if (extensions.contains(ext)) {
          // Forbid building stubs for .class files produced by Gosu
          return null;
        }
      }
    }
    return stub;
  }
}
