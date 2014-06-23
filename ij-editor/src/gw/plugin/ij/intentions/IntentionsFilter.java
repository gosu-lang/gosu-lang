/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.IntentionManager;
import com.intellij.codeInsight.intention.impl.MoveInitializerToConstructorAction;
import com.intellij.codeInsight.intention.impl.config.IntentionActionWrapper;
import com.intellij.psi.IntentionFilterOwner;

public class IntentionsFilter implements IntentionFilterOwner.IntentionActionsFilter {

  @Override
  public boolean isAvailable(IntentionAction intentionAction) {
    IntentionAction original = intentionAction;

    if(intentionAction instanceof IntentionActionWrapper) {
      intentionAction = ((IntentionActionWrapper) intentionAction).getDelegate();
    }

    if(intentionAction instanceof MoveInitializerToConstructorAction) {
      IntentionManager.getInstance().unregisterIntention(original);
      return false;
    }
    return true;
  }
}
