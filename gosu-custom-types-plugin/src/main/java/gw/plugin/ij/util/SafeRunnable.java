/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;

public abstract class SafeRunnable implements Runnable {
  private IModule _module;

  public SafeRunnable() {
  }

  public SafeRunnable(IModule module) {
    _module = module;
  }

  public SafeRunnable(@NotNull PsiElement context) {
    _module = GosuModuleUtil.findModuleForPsiElement( context );
    if (_module == null) {
      _module = TypeSystem.getGlobalModule();
    }
  }

  public abstract void execute() throws Exception;

  public final void run() {
    if (_module != null) {
      TypeSystem.pushModule( _module );
    }
    try {
      execute();
    } catch (Exception e) {
      if (!ExceptionUtil.isWrappedCanceled( e )) {
        throw new RuntimeException(e);
      }
    } finally {
      if (_module != null) {
        TypeSystem.popModule( _module );
      }
    }
  }
}
