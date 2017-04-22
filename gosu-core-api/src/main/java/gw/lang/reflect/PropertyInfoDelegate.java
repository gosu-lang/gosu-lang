/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public class PropertyInfoDelegate implements IPropertyInfoDelegate
{
  private final IPropertyInfo _source;
  private final IFeatureInfo _container;
  private final String _name;

  public PropertyInfoDelegate( IFeatureInfo container, IPropertyInfo source )
  {
    this( container, source, source.getName() );
  }

  public PropertyInfoDelegate( IFeatureInfo container, IPropertyInfo source, String name )
  {
    _container = container;
    _source = source;
    _name = name;
  }

  public IType getFeatureType()
  {
    return _source.getFeatureType();
  }

  public boolean isReadable()
  {
    return _source.isReadable();
  }

  public boolean isWritable(IType whosAskin) {
    return _source.isWritable(whosAskin);
  }

  public boolean isWritable()
  {
    return isWritable(null);
  }

  public boolean isStatic()
  {
    return _source.isStatic();
  }

  public IPropertyAccessor getAccessor()
  {
    return _source.getAccessor();
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  public String getName()
  {
    return _name;
  }

  public String getDisplayName()
  {
    return _source.getDisplayName();
  }

  public String getDescription()
  {
    return _source.getDescription();
  }

  public boolean isHidden()
  {
    return _source.isHidden();
  }

  public boolean isDeprecated()
  {
    return _source.isDeprecated();
  }

  public String getDeprecatedReason() {
    return _source.getDeprecatedReason();
  }

  public boolean isDefaultImpl() {
    return _source.isDefaultImpl();
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  @SuppressWarnings({"unchecked"})
  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return _source.getAnnotationsOfType( type );
  }

  public boolean hasAnnotation( IType type )
  {
    return _source.hasAnnotation( type );
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return _source.getAnnotations();
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return _source.isVisible( constraint );
  }

  public boolean isScriptable()
  {
    return _source.isScriptable();
  }

  public boolean isAbstract()
  {
    return _source.isAbstract();
  }

  public boolean isFinal()
  {
    return _source.isFinal();
  }

  public IPropertyInfo getSource()
  {
    return _source;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return _source.getDeclaredAnnotations();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return _source.getAnnotation( type );
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return _source.hasDeclaredAnnotation( type );
  }

  public boolean isInternal() {
    return _source.isInternal();
  }

  public boolean isPrivate() {
    return _source.isPrivate();
  }

  public boolean isProtected() {
    return _source.isProtected();
  }

  public boolean isPublic() {
    return _source.isPublic();
  }

  @Override
  public String toString() {
    return _source.toString();
  }

  public IPropertyInfo getDelegatePI()
  {
    return _source;
  }

  @Override
  public ILocationInfo getLocationInfo()
  {
    return _source.getLocationInfo();
  }
}
