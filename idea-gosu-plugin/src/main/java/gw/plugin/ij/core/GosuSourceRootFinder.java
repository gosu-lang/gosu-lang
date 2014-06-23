/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.ide.util.newProjectWizard.SourceRootFinder;
import com.intellij.ide.util.projectWizard.importSources.JavaModuleSourceRoot;
import com.intellij.ide.util.projectWizard.importSources.JavaSourceRootDetectionUtil;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class GosuSourceRootFinder implements SourceRootFinder {
  @Nullable
  public List<Pair<File, String>> findRoots(@NotNull File dir) {
    final Collection<JavaModuleSourceRoot> pairs = JavaSourceRootDetectionUtil.suggestRoots(dir);
    return null;
  }

  @Nullable
  public String getDescription() {
    return null;
  }

  @NotNull
  public String getName() {
    return "Gosu";
  }
}
