/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.diagnostic.DefaultIdeaErrorLogger;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.diagnostic.ErrorLogger;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diff.DiffManager;
import com.intellij.openapi.diff.DiffPanel;
import com.intellij.openapi.diff.SimpleContent;
import com.intellij.openapi.diff.SimpleDiffRequest;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import gw.plugin.ij.actions.TypeSystemAwareAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static gw.plugin.ij.util.ExecutionUtil.DISPATCH;

public class ExceptionUtil
{
  public static final String GOSU_NOTIFICATION_GROUP = "GosuGroup";
  private static final ErrorLogger ERROR_LOGGER = new DefaultIdeaErrorLogger();
  public static boolean _inTestMode = false;

  public static boolean isWrappedCanceled(@Nullable Throwable cause) {
    while (cause != null) {
      if (cause instanceof ProcessCanceledException) {
        return true;
      }
      cause = cause.getCause();
    }
    return false;
  }

  public static void showDiffWindow(final String text1, final String text2, final String title1, final String title2, final String title, final Project project) {
    Thread.dumpStack();

    ExecutionUtil.execute(DISPATCH, new SafeRunnable() {
      public void execute() {
        try {
          final DiffPanel diffPanel = DiffManager.getInstance().createDiffPanel(null, project, null);
          final SimpleDiffRequest diff = new SimpleDiffRequest(project, "Diff");
          diff.setContents(new SimpleContent(text1), new SimpleContent(text2));
          diffPanel.setDiffRequest(diff);
          diffPanel.setTitle1(title1);
          diffPanel.setTitle2(title2);

          final DialogBuilder builder = new DialogBuilder(project);
          builder.setCenterPanel(diffPanel.getComponent());
          builder.addDisposable(diffPanel);
          builder.removeAllActions();
          builder.setTitle(title);

          final AnAction action = new TypeSystemAwareAction() {
            public void actionPerformed(final AnActionEvent e) {
              builder.getDialogWrapper().close(0);
            }
          };
          action.registerCustomShortcutSet(new CustomShortcutSet(KeymapManager.getInstance().getActiveKeymap().getShortcuts("CloseContent")), diffPanel.getComponent());
          builder.showModal(true);
        } catch (Throwable e) {
          throw new RuntimeException("Cannot show diff panel");
        }
      }
    });
  }

  // Reporting errros
  public static void showError(@NotNull String title, @NotNull Throwable t) {
    System.err.println("DEV MODE ERROR REPORTING (REMOVE IN PRODUCTION)");
    t.printStackTrace();

    while (t.getCause() != null) {
      t = t.getCause();
    }
    String realError = t.getMessage();
    realError = realError != null ? realError : t.getClass().getSimpleName();
    realError = formatToHTML(realError);
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, realError, NotificationType.ERROR));
  }

  public static void showNonFatalError(String title, String text) {
    if (_inTestMode) {
      throw new RuntimeException(title + ": " + text);
    }

    final String htmlText = formatToHTML(text + "\n" + "<b><font size=\"3\" color=\"red\">This is a bug and you need to report it.</font></b>");
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, "Oops! " + title, htmlText, NotificationType.WARNING));
  }

  public static void showNonFatalError(String title, @NotNull Throwable e) {
    if (isWrappedCanceled(e)) {
      if (e instanceof ProcessCanceledException) {
        throw (ProcessCanceledException) e;
      } else {
        throw new ProcessCanceledException(e);
      }
    }
    showNonFatalError(title, e.getMessage());
    ERROR_LOGGER.handle(new IdeaLoggingEvent(title, e));
  }

  public static void showInfo(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.INFORMATION));
  }

  public static void showWarning(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.WARNING));
  }

  public static void showError(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new Notification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.ERROR));
  }

  public static void showBaloonlessError(@NotNull String title, @NotNull String text) {
    Notifications.Bus.notify(new BaloonlessNotification(GOSU_NOTIFICATION_GROUP, title, formatToHTML(text), NotificationType.ERROR));
  }

  private static String formatToHTML(@NotNull String realError) {
    return realError.replaceAll("\\n", "<br>");
  }

  public static String collectMessage(Throwable e) {
    String message = "";
    do {
      message += "- " + e.getMessage() + "\n";
      e = e.getCause();
    } while (e != null);
    return message;
  }
}

