/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.refactor.signature;

import com.intellij.refactoring.changeSignature.ParameterInfoImpl;
import gw.plugin.ij.editors.LightweightGosuEditor;

public class ParameterTableModelItem {

  public final ParameterInfoImpl parameter;
  public final LightweightGosuEditor typeCodeFragment;
  public final LightweightGosuEditor defaultValueCodeFragment;

  public ParameterTableModelItem(ParameterInfoImpl parameter,
                                 LightweightGosuEditor typeCodeFragment,
                                 LightweightGosuEditor defaultValueCodeFragment) {
    this.parameter = parameter;
    this.typeCodeFragment = typeCodeFragment;
    this.defaultValueCodeFragment = defaultValueCodeFragment;
  }
}
