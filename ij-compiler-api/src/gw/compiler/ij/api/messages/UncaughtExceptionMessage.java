/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.Serializable;

public class UncaughtExceptionMessage implements Serializable {
  public final Throwable e;

  public UncaughtExceptionMessage(Throwable e) {
    this.e = e;
  }
}
