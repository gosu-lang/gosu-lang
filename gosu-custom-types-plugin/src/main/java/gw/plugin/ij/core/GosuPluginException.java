/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

public class GosuPluginException extends RuntimeException {
  private final PluginFailureReason reason;

  public GosuPluginException(String message, PluginFailureReason reason) {
    super(message);
    this.reason = reason;
  }


  public PluginFailureReason getReason() {
    return reason;
  }
}
