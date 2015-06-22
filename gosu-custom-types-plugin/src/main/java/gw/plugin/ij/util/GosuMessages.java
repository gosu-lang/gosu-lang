/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.google.common.base.Throwables;
import com.intellij.diagnostic.LogMessageEx;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;

import java.util.Collections;

public class GosuMessages
{
  public static IdeaLoggingEvent create(String message, Throwable t) {
    final String notificationText = String.format("<html><b>Gosu:</b> %s</html>", message);
    return LogMessageEx.createEvent(message, "Stack Trace: \n" + Throwables.getStackTraceAsString(t), "XXX Title XXXX", notificationText, Collections.emptyList());
  }
}
