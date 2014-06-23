/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class MetaMethodInfoDelegate extends MethodInfoDelegate implements ITypeInfoMethodInfo
{
  public MetaMethodInfoDelegate( ITypeInfo container, IMethodInfo source )
  {
    super( container, source );
  }

  public boolean isStatic()
  {
    return !getSource().isStatic();
  }

  public IMethodCallHandler getCallHandler()
  {
    return new MetaMethodInfoDelegateCallHandler();
  }

  private class MetaMethodInfoDelegateCallHandler implements IMethodCallHandler
  {
    public Object handleCall( Object ctx, Object... args )
    {
      return getSource().getCallHandler().handleCall( ctx, args );
    }
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    } else if (!(other instanceof MetaMethodInfoDelegate)) {
      return false;
    } else {
      return getName().equals(((MetaMethodInfoDelegate) other).getName());
    }
  }
  
  @Override
  public int hashCode() {
    int result = 17;
    result = 31 * getName().hashCode();
    return result;
  }

  @Override
  public IMethodInfo getBackingMethodInfo()
  {
    return getSource();
  }
}