/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.project.Project;
import gw.plugin.ij.completion.model.BeanInfoModel;
import gw.plugin.ij.completion.model.BeanTree;
import gw.plugin.ij.completion.proposals.GosuCompletionProposal;
import gw.plugin.ij.completion.proposals.PathCompletionProposal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPathCompletionHandler extends AbstractCompletionHandler {
  private final Project _project;
  private final PrefixMatcher _matcher;

  public AbstractPathCompletionHandler(Project p, CompletionParameters params, PrefixMatcher matcher, CompletionResultSet result) {
    super(params, result);
    _project = p;
    _matcher = matcher;
  }

  protected void makeProposals(@NotNull BeanInfoModel model) {
    BeanTree root = model.getRoot();
    for (int i = 0; i < root.getChildCount(); i++) {
      BeanTree child = root.getChildAt(i);
      if (_matcher == null || _matcher.prefixMatches(child.getBeanNode().getName())) {
        addCompletion(makeProposal(child));
      }
    }
  }

  protected GosuCompletionProposal makeProposal(BeanTree child) {
    return new PathCompletionProposal(_project, child);
  }

  @Nullable
  @Override
  public String getStatusMessage() {
    return null;
  }

  public Project getProject() {
    return _project;
  }
}
