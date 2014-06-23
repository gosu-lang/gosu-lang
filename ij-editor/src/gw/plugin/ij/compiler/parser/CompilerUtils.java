/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.compiler.parser;

import com.google.common.io.Files;
import com.intellij.compiler.impl.javaCompiler.OutputItemImpl;
import com.intellij.openapi.compiler.CompileContext;
import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.compiler.TranslatingCompiler;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import gw.plugin.ij.util.GosuMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class CompilerUtils {
  private static final Logger LOG = Logger.getInstance(CompilerUtils.class);

  @Nullable
  public static Pair<String, String> getSourceFolderAndRelativePath(@NotNull Module ijModule, @NotNull VirtualFile file) {
    final ModuleRootManager manager = ModuleRootManager.getInstance(ijModule);
    for (VirtualFile sourceRoot : manager.getSourceRoots()) {
      final String sourceFolderRelative = VfsUtil.getRelativePath(file, sourceRoot, '/');
      if (sourceFolderRelative != null) {
        for (VirtualFile contentRoot : manager.getContentRoots()) {
          final String sourceFolder = VfsUtil.getRelativePath(sourceRoot, contentRoot, '/');
          if (sourceFolder != null) {
            return Pair.create(sourceFolder, sourceFolderRelative);
          }
        }
      }
    }
    return null;
    // throw new RuntimeException("(Gosu Compiler) File " + file.getPath() + " is not under the module's content roots.");
  }

  @NotNull
  public static File getOutputFile(Module ijModule, @NotNull Pair<String, String> sourceFolderAndRelativePath) {
    final VirtualFile outputDir = CompilerPaths.getModuleOutputDirectory(ijModule, false);
    if (outputDir != null) {
      //final String sourceFolder = sourceFolderAndRelativePath.getFirst();
      final String relativePath = sourceFolderAndRelativePath.getSecond();
      return new File(outputDir.getPath() + "/" + relativePath);
    } else {
      return null;
    }
  }

  @NotNull
  public static File getOutputFile(@NotNull CompileContext context, @NotNull VirtualFile file) {
    final Module ijModule = context.getModuleByFile(file);
    final Pair<String, String> sourceFolderAndRelativePath = getSourceFolderAndRelativePath(ijModule, file);
    return getOutputFile(ijModule, sourceFolderAndRelativePath);
  }

  public static void copySourceToOut(@NotNull VirtualFile file, @NotNull File outputFile) {
    try {
      FileUtil.createParentDirs(outputFile);
      FileUtil.copyContent(new File(file.getPath()), outputFile);
      Files.touch(outputFile);
      LocalFileSystem.getInstance().refreshAndFindFileByIoFile(outputFile);
    } catch (IOException e) {
      LOG.error(GosuMessages.create(String.format("Can't copy file '%s' to '%s'", file.getPath(), outputFile.getPath()), e));
    }
  }

  public static TranslatingCompiler.OutputItem copySourceToStandardOut(Module ijModule, VirtualFile file) {
    final Pair<String, String> sourceFolderAndRelativePath = getSourceFolderAndRelativePath(ijModule, file);
    final File outputFile = getOutputFile(ijModule, sourceFolderAndRelativePath);
    copySourceToOut(file, outputFile);
    return new OutputItemImpl(FileUtil.toSystemIndependentName(outputFile.getPath()), file);
  }
}
