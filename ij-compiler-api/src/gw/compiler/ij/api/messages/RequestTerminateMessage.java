/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.compiler.ij.api.messages;

import java.io.Serializable;

public class RequestTerminateMessage implements Serializable {
  public static final RequestTerminateMessage INSTANCE = new RequestTerminateMessage();

  private RequestTerminateMessage() {

  }

  public String toString() {
    return "RequestTerminateMessage";
  }
}
