/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.FeatureManager;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeInfoMethodInfo;
import gw.lang.reflect.ITypeInfoPropertyInfo;
import gw.lang.reflect.MetaMethodInfoDelegate;
import gw.lang.reflect.MetaPropertyInfoDelegate;
import gw.lang.reflect.MethodInfoDelegate;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.PropertyInfoBase;
import gw.lang.reflect.PropertyInfoDelegate;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 */
public class MetaTypeTypeInfo extends BaseFeatureInfo implements IRelativeTypeInfo
{
  private FeatureManager _fm;
  private Map<IModule, List<IMethodInfo>> _declaredMethods;
  private Map<IModule, List<IPropertyInfo>> _declaredProperties;

  /**
   */
  public MetaTypeTypeInfo( MetaType intrType )
  {
    super( intrType );
    _declaredMethods = new ConcurrentHashMap<IModule, List<IMethodInfo>>();
    _declaredProperties = new ConcurrentHashMap<IModule, List<IPropertyInfo>>();
    _fm = new FeatureManager( this, true );
  }

  /**
   */
  public String getName()
  {
    return getOwnersType().getType().getTypeInfo().getName() + " Type";
  }

  public IMetaType getOwnersType()
  {
    return (IMetaType)super.getOwnersType();
  }

  /**
   */
  public boolean isStatic()
  {
    return true;
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return getProperties( null );
  }

  @Override
  public IPropertyInfo getProperty( CharSequence propName )
  {
    return getProperty( null, propName );
  }

  @Override
  public IPropertyInfo getProperty( IType whosAskin, CharSequence propName )
  {
    return _fm.getProperty( getAccessibilityForType(whosAskin), propName );
  }

  /**
   */
  public MethodList getMethods()
  {
    return getMethods( (IType)null );
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  /**
   */
  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  /**
   */
  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  /**
   */
  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  private void addForNameMethod( MethodList methods )
  {
    if( FIND.method( methods, "forName", JavaTypes.STRING() ) == null )
    {
      IType iIntrinsicType = TypeSystem.getByFullName( LocalClassForNameHack.class.getName(), TypeSystem.getGlobalModule() );
      StaticMethodInfoDelegate forName =
        new StaticMethodInfoDelegate( this, iIntrinsicType.getTypeInfo().getMethod( "forName", JavaTypes.STRING() ) )
        {
          public boolean isDeprecated()
          {
            IMetaType ownerType = (IMetaType)getOwnersType();
            return super.isDeprecated() ||
                   (ownerType.isLiteral() && 
                    !(ownerType.getType() instanceof IMetaType));
          }

          @Override
          public String getDeprecatedReason()
          {
            return "Access this method from the Type property e.g., SomeType.Type." + getName();
          }
        };
      methods.add( forName );
    }
  }

  private Map<CharSequence, IPropertyInfo> mergeProperties( ITypeInfo typeTypeInfo )
  {
    Map<CharSequence, IPropertyInfo> propertiesByName = new HashMap<CharSequence, IPropertyInfo>();
    if( !getOwnersType().isLiteral() ||
        getOwnersType().getType() instanceof IMetaType )
    {
      loadMetaTypeProperties( propertiesByName );
    }
    else
    {
      // Include typeinfo from IType for backward compatibility
      List<? extends IPropertyInfo> properties;
      if( typeTypeInfo instanceof IRelativeTypeInfo )
      {
        properties = ((IRelativeTypeInfo)typeTypeInfo).getProperties( typeTypeInfo.getOwnersType() );
      }
      else
      {
        properties = typeTypeInfo.getProperties();
      }
      if( properties != null )
      {
        for( IPropertyInfo property : properties )
        {
          if( property.isStatic() )
          {
            IPropertyInfo pi = new PropertyInfoDelegate( this, property );
            propertiesByName.put( pi.getName(), pi );
          }
        }
      }

      addTypeProperty( propertiesByName );
    }
    return propertiesByName;
  }

  private void addTypeProperty( Map<CharSequence, IPropertyInfo> propertiesByName )
  {
    IPropertyInfo pi = new TypeProperty();

    // Only create the "Type" property if this meta-type does not have an existing static property named "Type".
    IPropertyInfo existing = propertiesByName.get( pi.getName() );
    if( existing == null || !existing.getName().equals( pi.getName() ) ) {
      propertiesByName.put( pi.getName(), pi );
    }
  }

  private List<? extends IMethodInfo> getMethods(ITypeInfo typeInfo) {
    if (typeInfo instanceof IRelativeTypeInfo) {
      return ((IRelativeTypeInfo)typeInfo).getMethods(typeInfo.getOwnersType());
    } else {
      return typeInfo.getMethods();
    }
  }

  private MethodList mergeMethods( ITypeInfo typeTypeInfo )
  {
    Set<IMethodInfo> tempMethods = new HashSet<IMethodInfo>();

    if( !getOwnersType().isLiteral() ||
        getOwnersType().getType() instanceof IMetaType )
    {
      loadMetaTypeMethods( tempMethods );
    }
    else
    {
      List<? extends IMethodInfo> methods = getMethods( typeTypeInfo );
      for( IMethodInfo method : methods )
      {
        if( method.isStatic() )
        {
          IMethodInfo mi = new MethodInfoDelegate( this, method );
          tempMethods.add( mi );
        }
      }
    }
    MethodList result = new MethodList();
    result.addAll(tempMethods);
    return result;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public Accessibility getAccessibilityForType( IType whosAskin )
  {   
    IType ownersClass = getOwnersType().getType();
    if( ownersClass == null || whosAskin == null ) {
      return Accessibility.PUBLIC;
    }

    if (canAccessPrivateMembers(ownersClass, whosAskin)) {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }

    return FeatureManager.getAccessibilityForClass( ownersClass, whosAskin );
  }

  /**
   * A private feature is accessible from its declaring class and any inner class
   * defined in its declaring class.
   */
  private boolean canAccessPrivateMembers( IType ownersClass, IType whosAskin )
  {
    return getOwnersType() == whosAskin ||
           getTopLevelTypeName( whosAskin ).equals( getTopLevelTypeName( ownersClass ) );
  }

  private String getTopLevelTypeName( IType type )
  {
    while( type.getEnclosingType() != null )
    {
      type = type.getEnclosingType();
    }
    return type.getName();
  }

  public List<? extends IPropertyInfo> getProperties( IType whosaskin )
  {
    return _fm.getProperties( getAccessibilityForType( whosaskin ) );
  }

  @Override
  public MethodList getMethods(IType whosAskin)
  {
    return _fm.getMethods( getAccessibilityForType( whosAskin ) );
  }


  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    MethodList methods = getMethods( whosaskin );
    return FIND.method( methods, methodName, params );
  }

  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return null;
  }

  public List<? extends IPropertyInfo> getDeclaredProperties()
  {
    IModule module = TypeSystem.getCurrentModule();
    List<IPropertyInfo> declaredProperties = _declaredProperties.get( module );
    if( declaredProperties == null )
    {      
      TypeSystem.lock();
      try
      {
        declaredProperties = _declaredProperties.get( module );
        if( declaredProperties == null )
        {
          ITypeInfo typeTypeInfo = getOwnersType().getType().getTypeInfo();
          Map<CharSequence, IPropertyInfo> propertiesByName = mergeProperties( typeTypeInfo );
          declaredProperties = new ArrayList<IPropertyInfo>( propertiesByName.values() );
          _declaredProperties.put( module, declaredProperties );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return declaredProperties;
  }
  
  public List<? extends IMethodInfo> getDeclaredMethods()
  {
    IModule module = TypeSystem.getCurrentModule();
    List<IMethodInfo> declaredMethods = _declaredMethods.get( module );
    if( declaredMethods == null )
    {      
      TypeSystem.lock();
      try
      {
        declaredMethods = _declaredMethods.get( module );
        if( declaredMethods == null )
        {
          ITypeInfo typeTypeInfo = getOwnersType().getType().getTypeInfo();
          MethodList methods = mergeMethods( typeTypeInfo );
          addForNameMethod( methods );
          declaredMethods = methods;
          _declaredMethods.put( module, declaredMethods );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return declaredMethods;
  }

  public List<? extends IConstructorInfo> getDeclaredConstructors()
  {
    return Collections.emptyList();
  }

  private void loadMetaTypeProperties( Map<CharSequence, IPropertyInfo> properties )
  {
    IType type = getOwnersType().getType();
    if( type != null )
    {
      Set<IType> allTypes = getTypeInterfaces( getTypeOfType(type), makeTreeSet());
      for( IType t : allTypes )
      {
        for( IPropertyInfo pi : t.getTypeInfo().getProperties() )
        {
          IPropertyInfo existing = properties.get(pi.getName());
          if (existing == null || existing.getFeatureType().isAssignableFrom(pi.getFeatureType())) {
            properties.put( pi.getName(), new MetaPropertyInfoDelegate( this, pi ) );
          }
        }
      }
    }
  }

  private TreeSet<IType> makeTreeSet() {
    return new TreeSet<IType>(new Comparator<IType>() {
      @Override
      public int compare(IType o1, IType o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
  }

  private IType getTypeOfType(IType type) {
    return TypeSystem.get( type.getClass(), TypeSystem.getGlobalModule() );
  }

  private void loadMetaTypeMethods( Set<IMethodInfo> methods )
  {
    IType type = getOwnersType().getType();
    if( type != null )
    {
      Set<IType> allTypes = getTypeInterfaces( getTypeOfType(type), makeTreeSet() );
      for( IType t : allTypes )
      {
        for( IMethodInfo pi : t.getTypeInfo().getMethods() ) {
          if (shouldAddMethod(methods, pi)) {
            methods.add( new MetaMethodInfoDelegate( this, pi ) );
          }
        }
      }
    }
  }

  private boolean shouldAddMethod(Set<IMethodInfo> methodInfos, IMethodInfo candidate) {
    for (Iterator<IMethodInfo> iterator = methodInfos.iterator(); iterator.hasNext(); ) {
      MetaMethodInfoDelegate current = (MetaMethodInfoDelegate) iterator.next();
      if (current.getBackingMethodInfo().equals(candidate)) {
        return false;
      }
      if (current.getDisplayName().equals(candidate.getDisplayName()) && areParameterTypesEqual(current, candidate)) {
        boolean isCovariantOverride = current.getReturnType().isAssignableFrom(candidate.getReturnType());
        if (isCovariantOverride) {
          iterator.remove();
        }
        return isCovariantOverride;
      }
    }
    return true;
  }

  private boolean areParameterTypesEqual(IMethodInfo methodInfo, IMethodInfo candidate) {
    IParameterInfo[] params1 = methodInfo.getParameters();
    IParameterInfo[] params2 = candidate.getParameters();
    if (params1.length == params2.length) {
      for (int i = 0, l = params1.length; i < l; i++) {
        IParameterInfo p1 = params1[i];
        IParameterInfo p2 = params2[i];
        if (!p1.getFeatureType().equals(p2.getFeatureType())) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private Set<IType> getTypeInterfaces( IType type, Set<IType> set )
  {
    if( getOwnersType().getType() == MetaType.DEFAULT_TYPE.get() )
    {
      return Collections.singleton( (IType)JavaTypes.ITYPE() );
    }

    for( IType iface : type.getInterfaces() )
    {
      if( !iface.getNamespace().startsWith( "gw.internal" ) )
      {
        set.add( iface );
      }
      else
      {
        getTypeInterfaces(iface, set);
      }
    }
    return set;
  }

  private static class DeprecatedStaticPropertyInfoDelegate extends PropertyInfoDelegate implements ITypeInfoPropertyInfo
  {
    public DeprecatedStaticPropertyInfoDelegate( IFeatureInfo container, IPropertyInfo source )
    {
      super( container, source );
    }

    @Override
    public boolean isStatic()
    {
      return true;
    }

    @Override
    public boolean isDeprecated()
    {
      return true;
    }

    @Override
    public IPropertyInfo getBackingPropertyInfo()
    {
      return getDelegatePI();
    }
    
    @Override
    public String getDeprecatedReason()
    {
      return "Access this property from the Type property e.g., SomeType.Type." + getName();
    }

    @Override
    public boolean isDefaultImpl() {
      return false;
    }
  }

  private static class StaticMethodInfoDelegate extends MethodInfoDelegate
  {
    public StaticMethodInfoDelegate( IFeatureInfo container, IMethodInfo source )
    {
      super( container, source );
    }

    @Override
    public boolean isStatic()
    {
      return true;
    }
  }

  private static class DeprecatedStaticMethodInfoDelegate extends StaticMethodInfoDelegate implements ITypeInfoMethodInfo
  {
    public DeprecatedStaticMethodInfoDelegate( IFeatureInfo container, IMethodInfo source )
    {
      super( container, source );
    }

    @Override
    public boolean isDeprecated()
    {
      return true;
    }

    @Override
    public IMethodInfo getBackingMethodInfo()
    {
      return getSource();
    }
    
    @Override
    public String getDeprecatedReason()
    {
      return "Access this method from the Type property e.g., SomeType.Type." + getName();
    }
  }

  public class TypeProperty extends PropertyInfoBase
  {
    private IPropertyAccessor _accessor;

    public TypeProperty()
    {
      super( MetaTypeTypeInfo.this );
    }

    public boolean isStatic()
    {
      return true;
    }

    public boolean isReadable()
    {
      return true;
    }

    public boolean isWritable( IType whosAskin )
    {
      return false;
    }

    public IPropertyAccessor getAccessor()
    {
      return _accessor != null
             ? _accessor
             : (_accessor =
        new IPropertyAccessor()
        {
          public Object getValue( Object ctx )
          {
            return MetaTypeTypeInfo.this.getOwnersType().getType();
          }

          public void setValue( Object ctx, Object value )
          {
            throw new IllegalStateException();
          }
        });
    }

    public List<IAnnotationInfo> getDeclaredAnnotations()
    {
      return Collections.emptyList();
    }

    @Override
    public boolean isDefaultImpl() {
      return false;
    }

    public String getName()
    {
      return "Type";
    }

    public IType getFeatureType()
    {
      return MetaType.get( MetaTypeTypeInfo.this.getOwnersType().getType() );
    }
  }
}
