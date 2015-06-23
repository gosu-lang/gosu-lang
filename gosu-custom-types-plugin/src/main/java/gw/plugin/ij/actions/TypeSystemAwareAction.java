/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import gw.internal.gosu.parser.TypeSystemState;
import gw.lang.reflect.TypeSystem;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class TypeSystemAwareAction extends AnAction {

  protected TypeSystemAwareAction() {
  }

  protected TypeSystemAwareAction( @Nullable String text ) {
    super(text);
  }

  protected TypeSystemAwareAction( @Nullable Icon icon ) {
    super(icon);
  }

  protected TypeSystemAwareAction( String text, @Nullable String description, @Nullable Icon icon ) {
    super(text, description, icon);
  }

  @Override
  public final void update(AnActionEvent e) {
    if ( TypeSystem.getState() == TypeSystemState.STARTED) {
      updateImpl(e);
    } else {
      final Presentation presentation = e.getPresentation();
      presentation.setVisible(false);
      presentation.setEnabled(false);
    }
  }

  protected void updateImpl(AnActionEvent e) {

  }

  public static void printTiming(String description, double thresholdSecs, long start) {
    final double dt = (System.nanoTime() - start) * 1e-9;
    if (dt >= thresholdSecs) {
      System.out.printf(description + " done in %.3fs.\n", dt);
    }
  }

}
