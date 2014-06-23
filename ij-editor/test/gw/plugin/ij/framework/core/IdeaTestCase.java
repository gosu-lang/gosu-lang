/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author mike
 */
public abstract class IdeaTestCase extends PlatformTestCase {

  @SuppressWarnings({"JUnitTestCaseWithNonTrivialConstructors"})
  protected IdeaTestCase() {
    initPlatformPrefix();
  }

  @Override
  protected ModuleType getModuleType() {
    return StdModuleTypes.JAVA;
  }

  public void initPlatformPrefix() {
    initPlatformPrefix("com.intellij.idea.IdeaUltimateApplication", "Idea");
  }

  protected void sortClassesByName(@NotNull final PsiClass[] classes) {
    Arrays.sort(classes, new Comparator<PsiClass>() {
      @Override
      public int compare(@NotNull PsiClass o1, @NotNull PsiClass o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }
}
