package editor;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;

import java.util.Collections;
import java.util.List;

/**
 */
public class TypePropertyInfo extends BaseFeatureInfo implements IPropertyInfo
{
  private TypeInPackageType _fauxType;
  private IPropertyAccessor _accessor;

  public TypePropertyInfo( IFeatureInfo container, String strFqName )
  {
    super( container );
    try
    {
      _fauxType = (TypeInPackageType)TypeInPackageTypeLoader.instance().getIntrinsicTypeByFullName( strFqName );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public boolean isStatic()
  {
    return true;
  }

  @Override
  public String getName()
  {
    return _fauxType.getRelativeName();
  }

  @Override
  public boolean isReadable()
  {
    return true;
  }

  @Override
  public boolean isWritable( IType whosAskin )
  {
    return false;
  }

  @Override
  public boolean isWritable()
  {
    return false;
  }

  @Override
  public boolean isDeprecated()
  {
    return false;
  }

  @Override
  public IPropertyAccessor getAccessor()
  {
    return _accessor == null ? _accessor = new MetaTypePropertyAccessor() : _accessor;
  }

  @Override
  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  @Override
  public IType getFeatureType()
  {
    return _fauxType;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  private class MetaTypePropertyAccessor implements IPropertyAccessor
  {
    @Override
    public Object getValue( Object ctx )
    {
      return _fauxType;
    }

    @Override
    public void setValue( Object ctx, Object value )
    {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public String toString()
  {
    return getName();
  }

}
