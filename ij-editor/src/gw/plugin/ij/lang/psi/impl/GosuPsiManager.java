/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.ProjectTopics;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.util.ConcurrencyUtil;
import com.intellij.util.Function;
import com.intellij.util.containers.ConcurrentWeakHashMap;
import com.intellij.util.containers.ContainerUtil;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuTypeDefinition;
import gw.plugin.ij.lang.psi.stubs.GosuShortNamesCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GosuPsiManager {
  private static final Logger LOG = Logger.getInstance("gw.plugin.ij.lang.psi.impl.GosuPsiManager");
  @NotNull
  private final Project myProject;

  private IGosuTypeDefinition myArrayClass;

  private final ConcurrentWeakHashMap<IGosuPsiElement, PsiType> myCalculatedTypes = new ConcurrentWeakHashMap<>();
  @Nullable
  private final GosuShortNamesCache myCache;

  private static final String SYNTHETIC_CLASS_TEXT = "class __ARRAY__ { public int length }";

  public GosuPsiManager(@NotNull Project project) {
    myProject = project;
    myCache = ContainerUtil.findInstance(project.getExtensions(PsiShortNamesCache.EP_NAME), GosuShortNamesCache.class);

    ((PsiManagerEx) PsiManager.getInstance(myProject)).registerRunnableToRunOnAnyChange(new Runnable() {
      public void run() {
        dropTypesCache();
      }
    });

    myProject.getMessageBus().connect().subscribe(ProjectTopics.PROJECT_ROOTS,
        new ModuleRootListener() {
          public void beforeRootsChange(ModuleRootEvent event) {
          }

          public void rootsChanged(ModuleRootEvent event) {
            dropTypesCache();
          }
        });
  }

  public void dropTypesCache() {
    myCalculatedTypes.clear();
  }

  public static GosuPsiManager getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, GosuPsiManager.class);
  }

  @Nullable
  public <T extends IGosuPsiElement> PsiType getType(@NotNull T element, @NotNull Function<T, PsiType> calculator) {
    PsiType type = myCalculatedTypes.get(element);
    if (type == null) {
      type = calculator.fun(element);
      if (type == null) {
        type = PsiType.NULL;
      }
      type = ConcurrencyUtil.cacheOrGet(myCalculatedTypes, element, type);
    }
    if (!type.isValid()) {
      LOG.error("Type is invalid: " + type + "; element: " + element + " of class " + element.getClass());
    }
    return PsiType.NULL.equals(type) ? null : type;
  }
}
