package editor;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.MethodList;

import java.util.Collections;
import java.util.List;

/**
 */
public class TypeInPackageTypeInfo extends BaseFeatureInfo implements ITypeInfo
{
  private TypeInPackageType _type;

  public TypeInPackageTypeInfo( TypeInPackageType type )
  {
    super( type );
    _type = type;
  }

  public boolean isStatic()
  {
    return false;
  }

  public String getName()
  {
    return _type.getName();
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return Collections.emptyList();
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return null;
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return null;
  }

  public MethodList getMethods()
  {
    return MethodList.EMPTY;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return null;
  }

  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }
}
