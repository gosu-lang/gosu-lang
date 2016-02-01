package editor;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuConstructorInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class ClassStructureViewPopupDelegate
{
  public static final List<IFeatureInfo> EMPTY_LIST = Collections.emptyList();

  public static List<IFeatureInfo> filter( IGosuClassTypeInfo typeInfo, String prefix )
  {
    assert prefix != null;
    AlphabeticalFeatureInfoComparator comparator = new AlphabeticalFeatureInfoComparator();
    List<? extends IPropertyInfo> propertyInfos = new ArrayList<IPropertyInfo>( typeInfo.getProperties( typeInfo.getGosuClass() ) );
    Collections.sort( propertyInfos, comparator );
    List<IGosuConstructorInfo> constructorInfos = new ArrayList<IGosuConstructorInfo>( (Collection<? extends IGosuConstructorInfo>)typeInfo.getConstructors( typeInfo.getGosuClass() ) );
    Collections.sort( constructorInfos, comparator );
    MethodList methodInfos = typeInfo.getMethods( typeInfo.getGosuClass() );
    Collections.sort( methodInfos, comparator );
    String lowerCasePrefix = prefix.toLowerCase();
    List<IFeatureInfo> featureInfos = new ArrayList<IFeatureInfo>();
    for( IPropertyInfo propertyInfo : propertyInfos )
    {
      if( matches( typeInfo, propertyInfo, lowerCasePrefix ) )
      {
        featureInfos.add( propertyInfo );
      }
    }
    for( IGosuConstructorInfo constructorInfo : constructorInfos )
    {
      if( matches( typeInfo, constructorInfo, lowerCasePrefix ) )
      {
        featureInfos.add( constructorInfo );
      }
    }
    for( IMethodInfo methodInfo : methodInfos )
    {
      if( matches( typeInfo, methodInfo, lowerCasePrefix ) )
      {
        featureInfos.add( methodInfo );
      }
    }
    return featureInfos;
  }

  private static boolean matches( IGosuClassTypeInfo typeInfo, IAttributedFeatureInfo featureInfo, String prefix )
  {
    return featureInfo.getName().toLowerCase().startsWith( prefix ) &&
           !featureInfo.getName().startsWith( "@" ) &&
           featureInfo.getOwnersType() == typeInfo.getGosuClass();
  }

  public static class AlphabeticalFeatureInfoComparator implements Comparator<IFeatureInfo>
  {
    public int compare( IFeatureInfo o1, IFeatureInfo o2 )
    {
      return o1.getName().compareTo( o2.getName() );
    }
  }

  public static class FeatureModel extends AbstractGotoPopup.AbstractPopupListModel<IFeatureInfo>
  {
    private List<IFeatureInfo> _allFeatures;

    public FeatureModel( List<IFeatureInfo> allFeatures )
    {
      _allFeatures = allFeatures;
    }

    public int getSize()
    {
      return _allFeatures.size();
    }

    public IFeatureInfo getElementAt( int i )
    {
      return _allFeatures.get( i );
    }
  }
}
