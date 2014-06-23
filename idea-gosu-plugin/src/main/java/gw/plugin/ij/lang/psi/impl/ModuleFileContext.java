/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import gw.lang.parser.IFileContext;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IGlobalModule;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.Nullable;

public class ModuleFileContext implements IFileContext {
  private final IModule module;
  private final String qualifiedName;

  public ModuleFileContext(IModule module, String qualifiedName) {
    this.module = module; // TODO: do we really need module here?
    this.qualifiedName = qualifiedName;
  }

  @Nullable
  @Override
  public String getContextString() {
    return null;
  }

  @Override
  public String getClassName() {
    return qualifiedName;
  }

  @Nullable
  @Override
  public String getFilePath() {
    if (module instanceof IGlobalModule) {
      return "";
    } else {
      final ISourceFileHandle handle = module.getFileRepository().findClass(qualifiedName, GosuClassTypeLoader.ALL_EXTS);
      return handle != null ? handle.getFilePath() : null;
    }
  }
}
