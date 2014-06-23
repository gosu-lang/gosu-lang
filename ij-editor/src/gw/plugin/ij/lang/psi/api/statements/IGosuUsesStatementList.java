/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements;

public interface IGosuUsesStatementList extends IGosuStatement {
  IGosuUsesStatement[] getUsesStatements();
}
