/*
 *
 *  Copyright 2010 Guidewire Software, Inc.
 *
 */
package editor;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;

/**
 *
 */
public class PropertyNode extends BeanInfoNode
{
  private MyPropertyRef _descriptor;
  private String _strValue; // For runtime eval (rule debugger)
  private boolean _bVirtual;


  /**
   * @param descriptor
   * @param arrayicize
   * @param whosaskin
   */
  public PropertyNode( ITypeInfo owner, IPropertyInfo descriptor, boolean arrayicize, IType whosaskin )
  {
    super( arrayicize ? descriptor.getFeatureType().getArrayType() : descriptor.getFeatureType() );

    _descriptor = new MyPropertyRef( owner, descriptor, whosaskin );
    _bVirtual = true;
  }

  public void setVirtual( boolean bVirtual )
  {
    _bVirtual = bVirtual;
  }

  public boolean isVirtual()
  {
    return _bVirtual;
  }

  /**
   * @return
   */
  @Override
  public String getDisplayName()
  {
    return _descriptor.get().getName() + (getType() == null ? "" : ("   (" + getTypeName( getType() ) + ")"));
  }

  /**
   * @return
   */
  @Override
  public String getName()
  {
    return _descriptor.get().getName();
  }

  @Override
  public IFeatureInfo getFeatureInfo()
  {
    return _descriptor.get();
  }

  /**
   * @return
   */
  @Override
  public String getValue()
  {
    return _strValue;
  }

  @Override
  public void setValue( String strValue )
  {
    _strValue = strValue;
  }

  public IPropertyInfo getPropertyDescriptor()
  {
    return _descriptor.get();
  }

  private static class MyPropertyRef
  {
    private IType _type;
    private String _name;
    private IType _whosaskin;
    private ITypeInfo _owner;

    public MyPropertyRef( ITypeInfo owner, IPropertyInfo propInfo, IType whosaskin )
    {
      _type = owner.getOwnersType();
      _name = propInfo.getName();
      _whosaskin = whosaskin;
      _owner = owner;
    }

    public IPropertyInfo get()
    {
      if( _type == null )
      {
        return null;
      }
      ITypeInfo typeInfo = _type.getTypeInfo();
      if( typeInfo instanceof IRelativeTypeInfo )
      {
        return ((IRelativeTypeInfo)typeInfo).getProperty( _whosaskin, _name );
      }
      else
      {
        return _owner.getProperty( _name );
      }
    }
  }
}
