/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InitializerCompletionProposal extends RawCompletionProposal implements ICompletionHasAdditionalSyntax{
  private final IType _type;
  private final boolean _required;

  public InitializerCompletionProposal(String name, IType type, boolean required) {
    super(name);
    _type = type;
    _required = required;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return null;
  }

  @NotNull
  @Override
  public String getTrailingText() {
    return " = ";
  }

  @Override
  public int getCaretOffsetFromEnd() {
    return 0;
  }

  public String getType() {
    return _type.getRelativeName();
  }

  public boolean isRequired() {
    return _required;
  }
}
