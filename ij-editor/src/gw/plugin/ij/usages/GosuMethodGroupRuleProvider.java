/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.intellij.openapi.project.Project;
import com.intellij.usages.impl.FileStructureGroupRuleProvider;
import com.intellij.usages.rules.UsageGroupingRule;

public class GosuMethodGroupRuleProvider implements FileStructureGroupRuleProvider {
  public UsageGroupingRule getUsageGroupingRule(final Project project) {
    return new GosuMethodGroupingRule();
  }
}
