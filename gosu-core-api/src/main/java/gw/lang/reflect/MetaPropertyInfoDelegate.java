/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class MetaPropertyInfoDelegate extends PropertyInfoDelegate implements ITypeInfoPropertyInfo
{
  public MetaPropertyInfoDelegate( ITypeInfo container, IPropertyInfo source )
  {
    super( container, source );
  }

  public boolean isStatic()
  {
    return !getSource().isStatic();
  }

  public IPropertyAccessor getAccessor()
  {
    return new Accessor();
  }

  @Override
  public IPropertyInfo getBackingPropertyInfo()
  {
    return getDelegatePI();
  }

  private class Accessor implements IPropertyAccessor
  {
    public Object getValue( Object ctx )
    {
      return getSource().getAccessor().getValue( ctx );
    }

    public void setValue( Object ctx, Object value )
    {
      getSource().getAccessor().setValue( ctx, value );
    }
  }
}