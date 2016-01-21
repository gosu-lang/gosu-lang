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
public class PackagePropertyInfo extends BaseFeatureInfo implements IPropertyInfo
{
  private PackageType _packageType;
  private IPropertyAccessor _accessor;


  public PackagePropertyInfo( IFeatureInfo container, String strPackage )
  {
    super( container );
    try
    {
      _packageType = (PackageType)PackageTypeLoader.instance().getIntrinsicTypeByFullName( strPackage, true );
    }
    catch( ClassNotFoundException e )
    {
      throw new RuntimeException( e );
    }
  }

  public boolean isStatic()
  {
    return true;
  }

  public String getName()
  {
    return _packageType.getRelativeName();
  }

  public boolean isReadable()
  {
    return true;
  }

  public boolean isWritable( IType whosAskin )
  {
    return false;
  }

  public boolean isWritable()
  {
    return isWritable( null );
  }

  public IPropertyAccessor getAccessor()
  {
    return _accessor == null ? _accessor = new PackagePropertyAccessor() : _accessor;
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  public IType getFeatureType()
  {
    return _packageType;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  private class PackagePropertyAccessor implements IPropertyAccessor
  {
    public Object getValue( Object ctx )
    {
      return _packageType;
    }

    public void setValue( Object ctx, Object value )
    {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public String getDescription()
  {
    return null;
  }
}
