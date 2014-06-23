/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class ModifiedParameterInfo extends SimpleParameterInfo
{
  private IType _originalType;

  public ModifiedParameterInfo(IFeatureInfo container, IType type, IType originalType, int parentIndex) {
    super(container, type, parentIndex);
    _originalType = originalType;
  }

  public IType getOriginalType() {
    return _originalType;
  }
}
