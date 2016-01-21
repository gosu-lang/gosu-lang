package editor;

import editor.util.IProgressCallback;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class PackageTypeInfo extends BaseFeatureInfo implements ITypeInfo
{
  private PackageType _packageType;
  private List<IPropertyInfo> _properties;
  private Map<CharSequence, IPropertyInfo> _propertiesByName;

  public PackageTypeInfo( PackageType packageType )
  {
    super( packageType );
    _packageType = packageType;
    _propertiesByName = new HashMap<CharSequence, IPropertyInfo>();
  }

  @Override
  public boolean isStatic()
  {
    return false;
  }

  @Override
  public String getName()
  {
    return _packageType.getName();
  }

  @Override
  public List<? extends IPropertyInfo> getProperties()
  {
    makeProperties();
    return _properties;
  }

  @Override
  public IPropertyInfo getProperty( CharSequence propName )
  {
    makeProperties();
    return _propertiesByName.get( propName );
  }

  @Override
  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return null;
  }

  @Override
  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return null;
  }

  private void makeProperties()
  {
    if( _properties != null )
    {
      return;
    }

    String strNamePlusDot = getName() + '.';
    Set<? extends CharSequence> allTypeNames = TypeSystem.getAllTypeNames();
    Set<String> allInPackage = getAllInPackage( allTypeNames, strNamePlusDot );
    if( allInPackage == null )
    {
      return;
    }

    getPropertiesInPackage( allInPackage, strNamePlusDot.length() );
  }

  private void getPropertiesInPackage( Set<String> allInPackage, int iPropertyOffset )
  {
    boolean bError = false;
    int i = 0;
    for( String strType : allInPackage )
    {
      int iPropertyEnd = strType.indexOf( '.', iPropertyOffset );
      if( iPropertyEnd < 0 )
      {
        try
        {
          if( null != null )
          {
            ((IProgressCallback)null).updateProgress( i, strType );
          }
          TypePropertyInfo pi = new TypePropertyInfo( PackageTypeInfo.this, strType );
          _propertiesByName.put( pi.getFeatureType().getRelativeName(), pi );
        }
        catch( Exception e )
        {
          editor.util.EditorUtilities.handleUncaughtException( e );
          if( !bError )
          {
            //MessageDisplay.displayError( e );
            bError = true;
          }
        }
      }
      else
      {
        String strPackage = strType.substring( 0, iPropertyEnd );
        String strSubPackage = strPackage.substring( iPropertyOffset );
        if( !_propertiesByName.containsKey( strSubPackage ) )
        {
          if( null != null )
          {
            ((IProgressCallback)null).updateProgress( i, strType );
          }
          PackagePropertyInfo pi = new PackagePropertyInfo( PackageTypeInfo.this, strPackage );
          _propertiesByName.put( pi.getFeatureType().getRelativeName(), pi );
        }
      }
      i++;
    }
    _properties = TypeInfoUtil.makeSortedUnmodifiableRandomAccessListFromFeatures( _propertiesByName );
  }

  private Set<String> getAllInPackage( Set<? extends CharSequence> allTypeNames, String strNamePlusDot )
  {
    Set<String> allInPackage = new HashSet<String>();
    for( Object allTypeName : allTypeNames )
    {
      String strType = allTypeName.toString();
      if( strType.startsWith( strNamePlusDot ) &&
          !strType.endsWith( ".PLACEHOLDER" ) )
      {
        allInPackage.add( strType );
      }
    }
    return allInPackage;
  }

  @Override
  public MethodList getMethods()
  {
    return MethodList.EMPTY;
  }

  @Override
  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return null;
  }

  @Override
  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  @Override
  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  @Override
  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  @Override
  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }
}
