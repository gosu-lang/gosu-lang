/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaloonlessNotification extends Notification {

  public BaloonlessNotification( @NotNull String groupDisplayId, @NotNull String title, @NotNull String content, @NotNull NotificationType type ) {
    super(groupDisplayId, title, content, type);
  }

  public BaloonlessNotification( @NotNull String groupDisplayId, @NotNull String title, @NotNull String content, @NotNull NotificationType type, @Nullable NotificationListener listener ) {
    super(groupDisplayId, title, content, type, listener);
  }

  @Override
  public boolean isExpired() {
    return true;
  }
}
