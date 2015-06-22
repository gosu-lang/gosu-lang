/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import gw.lang.reflect.module.IProject;
import org.jetbrains.annotations.NotNull;

/**
 */
public class IjProject implements IProject
{
  public static final Key<String> GW_SHADOW_MODE = Key.create("gw.shadowMode");
  private final Project _project;

  public IjProject( Project project ) {
    _project = project;
  }

  @NotNull
  @Override
  public String getName() {
    return _project.getName();
  }

  @Override
  public Object getNativeProject() {
    return _project;
  }

  @Override
  public boolean isDisposed() {
    return _project.isDisposed();
  }

  @Override
  public boolean isHeadless() {
    return ApplicationManager.getApplication().isHeadlessEnvironment();
  }

  @Override
  public String toString() {
    return _project.toString();
  }

  @Override
  public boolean isShadowMode() {
    return Boolean.valueOf(_project.getUserData(GW_SHADOW_MODE));
  }
}
