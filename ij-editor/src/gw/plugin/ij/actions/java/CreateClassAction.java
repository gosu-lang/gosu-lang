/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.actions.java;

import com.intellij.ide.IdeBundle;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import gw.plugin.ij.actions.ActionUtil;

public class CreateClassAction extends com.intellij.ide.actions.CreateClassAction {

  public CreateClassAction() {
    Presentation presentation = getTemplatePresentation();
    presentation.setText(ActionsBundle.message("action.NewClass.text"));
    presentation.setDescription(IdeBundle.message("action.create.new.class"));
  }


  @Override
  protected boolean isAvailable(DataContext dataContext) {
    if (ActionUtil.isInConfigFolder(dataContext)) {
      return false;
    } else {
      return super.isAvailable(dataContext);
    }
  }

}
