/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

public abstract class SafeCallable<R> implements Computable<R>, Callable<R>, Runnable {
  private R _result;
  private IModule _module;

  public SafeCallable() {
  }

  public SafeCallable(IModule module) {
    _module = module;
  }

  public SafeCallable(@NotNull PsiElement context) {
    try {
      _module = GosuModuleUtil.findModuleForPsiElement( context );
      if (_module == null) {
        _module = TypeSystem.getGlobalModule();
      }
    }
    catch (Exception e) {
      if( !ExceptionUtil.isWrappedCanceled( e ) ) {
        if( e instanceof RuntimeException ) {
          throw (RuntimeException)e;
        }
        throw new RuntimeException( e );
      }
      // Eat ankle-biting ProcessCancelledExceptions
    }
  }

  public abstract R execute() throws Exception;

  private R _execute() throws Exception {
    if (_module != null) {
      TypeSystem.pushModule( _module );
    }
    try {
      _result = execute();
      return _result;
    }
    catch (Exception e) {
      if( !ExceptionUtil.isWrappedCanceled( e ) ) {
        return handleNull( e );
      }
      throw e;
    }
    finally {
      if (_module != null) {
        TypeSystem.popModule( _module );
      }
    }
  }

  @Override
  public final R compute() {
    try {
      return _execute();
    } catch (Exception e) {
      if( e instanceof RuntimeException ) {
        throw (RuntimeException)e;
      }
      throw new RuntimeException(e);
    }
  }

  @Override
  public final R call() throws Exception {
    return _execute();
  }

  public R handleNull( Throwable exception ) {
    return null;
  }

  @Override
  public final void run() {
    try {
      _execute();
    } catch (Exception e) {
      if( e instanceof RuntimeException ) {
        throw (RuntimeException)e;
      }
      throw new RuntimeException(e);
    }
  }

  public R getResult() {
    return _result;
  }
}
