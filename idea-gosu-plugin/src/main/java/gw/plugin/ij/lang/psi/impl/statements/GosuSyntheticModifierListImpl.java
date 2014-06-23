/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements;

import com.intellij.psi.SyntheticElement;
import gw.plugin.ij.lang.parser.GosuSyntheticCompositeElement;

public class GosuSyntheticModifierListImpl extends GosuModifierListImpl implements SyntheticElement {
  public GosuSyntheticModifierListImpl( GosuSyntheticCompositeElement node ) {
    super(node);
  }
}
