/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework.core;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;

public abstract class VoidCallback implements Callable<Void> {
  private final Project _project;

  protected VoidCallback( Project project ) {
    _project = project;
  }

  public Project getProject() {
    return _project;
  }

  @Nullable
  public Void call() throws Exception {
    try {
      run();
    }
    catch( Exception e ) {
      throw e;
    }
    catch( Throwable t ) {
      throw new RuntimeException( t );
    }
    return null;
  }

  public abstract void run() throws Throwable;
}
