/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.expressions.INameInDeclaration;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IParsedElementWithAtLeastOneDeclaration;

public interface IClassDeclaration extends IParsedElementWithAtLeastOneDeclaration, INameInDeclaration
{
  CharSequence getClassName();

  IGosuClass getGSClass();
}
