/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.statements.typedef;

import com.intellij.psi.SyntheticElement;
import gw.plugin.ij.lang.parser.GosuSyntheticCompositeElement;
import gw.plugin.ij.lang.psi.stubs.GosuTypeDefinitionStub;

public class GosuSyntheticClassDefinitionImpl extends GosuClassDefinitionImpl implements SyntheticElement {
  public GosuSyntheticClassDefinitionImpl(GosuSyntheticCompositeElement node) {
    super(node);
  }

  public GosuSyntheticClassDefinitionImpl(final GosuTypeDefinitionStub stub) {
    super(stub);
  }
}
