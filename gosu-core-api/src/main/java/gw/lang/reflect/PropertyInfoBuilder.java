/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PropertyInfoBuilder {

  private boolean _isStatic;
  private String _name;
  private IPropertyAccessor _accessor;
  private boolean _readable = true;
  private boolean _writable = true;
  private IType _type;
  private String _description;
  private String _deprecated;
  private String _javaGetterMethodName;
  private List<IAnnotationInfo> _annotations = Collections.emptyList();
  private ILocationInfo _locationInfo;

  public PropertyInfoBuilder withName(String name) {
    _name = name;
    return this;
  }

  public PropertyInfoBuilder withType(IType type) {
    _type = type;
    return this;
  }

  public PropertyInfoBuilder withType(Class returnType) {
    return withType(TypeSystem.get(returnType));
  }

  public PropertyInfoBuilder withStatic() {
    return withStatic(true);
  }

  public PropertyInfoBuilder withStatic(boolean isStatic) {
    _isStatic = isStatic;
    return this;
  }

  public PropertyInfoBuilder withAccessor(IPropertyAccessor accessor) {
    _accessor = accessor;
    return this;
  }

  public PropertyInfoBuilder withGetter(String javaGetterMethodName) {
    _javaGetterMethodName = javaGetterMethodName;
    return this;
  }

  public PropertyInfoBuilder withReadable(boolean readable) {
    _readable = readable;
    return this;
  }

  public PropertyInfoBuilder withWritable(boolean writable) {
    _writable = writable;
    return this;
  }

  public PropertyInfoBuilder withDescription(String description) {
    _description = description;
    return this;
  }

  public PropertyInfoBuilder withDeprecated(String deprecated) {
    _deprecated = deprecated;
    return this;
  }

  public IPropertyInfo build(IFeatureInfo container) {
    return new BuiltPropertyInfo(this, container);
  }

  public PropertyInfoBuilder withAnnotations( IAnnotationInfo... annotations ) {
    _annotations = Arrays.asList(annotations);
    return this;
  }

  public PropertyInfoBuilder like( IPropertyInfo prop ) {
    _isStatic = prop.isStatic();
    _name = prop.getName();
    _accessor = prop.getAccessor();
    _readable = prop.isReadable();
    _writable = prop.isWritable();
    _type = prop.getFeatureType();
    _description = prop.getDescription();
    if ( prop.isDeprecated() ) {
      _deprecated = prop.getDeprecatedReason() == null ? "" : prop.getDeprecatedReason();
    }
    else {
      _deprecated = null;
    }
    _annotations = prop.getAnnotations(); // todo dlank - any need to step through annotations and recreate 1-by-1?
    _locationInfo = prop.getLocationInfo();
    return this;
  }

  public PropertyInfoBuilder withLocation( ILocationInfo locationInfo ) {
    _locationInfo = locationInfo;
    return this;
  }

  public static class BuiltPropertyInfo extends BaseFeatureInfo implements IPropertyInfo {

    private final boolean _isStatic;
    private final String _name;
    private final String _javaGetterMethodName;
    private IPropertyAccessor _accessor;
    private boolean _readable = true; // default to true
    private final boolean _writable;
    private IType _type;
    private final String _description;
    private final String _deprecated;
    private List<IAnnotationInfo> _annotations = Collections.emptyList();
    private final ILocationInfo _locationInfo;

    public BuiltPropertyInfo(PropertyInfoBuilder builder, IFeatureInfo container) {
      super(container);
      assert container != null;
      _isStatic = builder._isStatic;
      _name = builder._name;
      _accessor = builder._accessor;
      _javaGetterMethodName = builder._javaGetterMethodName;
      _readable = builder._readable;
      _writable = builder._writable;
      _type = builder._type;
      _description = builder._description;
      _deprecated = builder._deprecated;
      _annotations = builder._annotations;
      _locationInfo = builder._locationInfo == null ? ILocationInfo.EMPTY : builder._locationInfo;

      inferAccessorAndTypeFromName();

      assert _accessor != null;
      assert _type != null;
    }

    public String getJavaMethodName()
    {
      return _javaGetterMethodName;
    }

    private void inferAccessorAndTypeFromName()
    {
      if( _accessor == null && (_type == null || _type instanceof IJavaType) )
      {
        final IType ownerType = getOwnersType();
        if( ownerType instanceof IJavaType )
        {
          IJavaType propertyType = (IJavaType)_type;
          IJavaClassMethod getter;
          if( _javaGetterMethodName != null )
          {
            try
            {
              getter = ((IJavaType)ownerType).getBackingClassInfo().getMethod( _javaGetterMethodName );
            }
            catch( NoSuchMethodException e )
            {
              throw new RuntimeException( e );
            }
          }
          else
          {
            try
            {
              getter = ((IJavaType)ownerType).getBackingClassInfo().getMethod( "get" + _name );
            }
            catch( NoSuchMethodException e )
            {
              try
              {
                getter = ((IJavaType)ownerType).getBackingClassInfo().getMethod( "is" + _name );
              }
              catch( NoSuchMethodException e1 )
              {
                throw new RuntimeException( e1 );
              }
            }
          }

          if( propertyType == null )
          {
            _type = propertyType = (IJavaType) getter.getReturnType();
          }

          IJavaClassMethod setter = null;
          if( _writable )
          {
            try
            {
              setter = ((IJavaType)ownerType).getBackingClassInfo().getMethod( "set" + _name, propertyType.getBackingClassInfo() );
            }
            catch( NoSuchMethodException e )
            {
              throw new RuntimeException( e );
            }
          }
          final String getterName = getter.getName();
          final String setterName = setter.getName();
          _accessor =
            new IPropertyAccessor()
            {
              Method _getMethod = null;
              Method _setMethod = null;

              public Object getValue( Object ctx )
              {
                try
                {
                  if( _getMethod == null )
                  {
                    _getMethod = ((IJavaType)ownerType).getBackingClass().getMethod( getterName );
                  }
                  return _getMethod.invoke( ctx );
                }
                catch( Exception e )
                {
                  throw new RuntimeException( e );
                }
              }

              public void setValue( Object ctx, Object value )
              {
                try
                {
                  if( _setMethod == null )
                  {
                    _setMethod = ((IJavaType)ownerType).getBackingClass().getMethod( setterName, ((IJavaType)_type).getBackingClass() );
                  }
                  _setMethod.invoke( ctx, value );
                }
                catch( Exception e )
                {
                  throw new RuntimeException( e );
                }
              }
            };
        }
      }
    }

    public List<IAnnotationInfo> getDeclaredAnnotations() {
      return _annotations;
    }

    public boolean isStatic() {
      return _isStatic;
    }

    public String getName() {
      return _name;
    }

    public boolean isReadable() {
      return _readable;
    }

    public boolean isWritable(IType whosAskin) {
      return _writable;
    }

    public boolean isWritable() {
      return isWritable(null);
    }

    public IPropertyAccessor getAccessor() {
      return _accessor;
    }

    public IPresentationInfo getPresentationInfo() {
      return IPresentationInfo.Default.GET;
    }

    public IType getFeatureType() {
      return _type;
    }

    public String getDescription() {
      return _description;
    }

    public boolean isDeprecated() {
      return _deprecated != null;
    }

    public String getDeprecatedReason() {
      return _deprecated;
    }

    public String toString() {
      return _name;
    }

    @Override
    public ILocationInfo getLocationInfo() {
      return _locationInfo;
    }
  }

}
