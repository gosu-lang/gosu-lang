/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.parser.CICS;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked"})
public class FeatureManager<T extends CharSequence> {

  private final boolean _caseSensitive;
  private final boolean _addObjectMethods;
  private IRelativeTypeInfo _typeInfo;
  private volatile Map<IModule, InitState> _methodsInitialized = new HashMap<IModule, InitState>();
  private volatile Map<IModule, InitState> _propertiesInitialized = new HashMap<IModule, InitState>();
  private volatile InitState _ctorsInitialized = InitState.NotInitialized;
  private Map<IModule, PropertyNameMap<T>[]> _properties = new HashMap<IModule, PropertyNameMap<T>[]>();
  private Map<IModule, MethodList[]> _methods = new HashMap<IModule, MethodList[]>();
  private List<IConstructorInfo>[] _constructors = new List[IRelativeTypeInfo.Accessibility_Size];
  private String _superPropertyPrefix;
  private IType _supertypeToCopyPropertiesFrom;
  public FeatureManager(IRelativeTypeInfo typeInfo, boolean caseSensitive) {
    this(typeInfo, caseSensitive, false);
  }

  public FeatureManager(IRelativeTypeInfo typeInfo, boolean caseSensitive, boolean addObjectMethods) {
    _typeInfo = typeInfo;
    _caseSensitive = caseSensitive;// && !ILanguageLevel.Util.STANDARD_GOSU();
    _addObjectMethods = addObjectMethods;
  }

  public static IRelativeTypeInfo.Accessibility getAccessibilityForClass( IType ownersClass, IType whosAskin )
  {
    if( TypeSystem.isIncludeAll() )
    {
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }

    if( ownersClass == null || whosAskin == null )
    {
      return IRelativeTypeInfo.Accessibility.PUBLIC;
    }

    if( getTopLevelTypeName( whosAskin ).equals( getTopLevelTypeName( ownersClass ) ) )
    {
      // Implies private members, which means everything.
      return IRelativeTypeInfo.Accessibility.PRIVATE;
    }
    else if( Modifier.isPrivate( ownersClass.getModifiers() ) )
    {
      return IRelativeTypeInfo.Accessibility.NONE;
    }
    else if( isInSameNamespace( ownersClass, whosAskin ) )
    {
      return IRelativeTypeInfo.Accessibility.INTERNAL;
    }
    else if( Modifier.isInternal( ownersClass.getModifiers() ) )
    {
      return IRelativeTypeInfo.Accessibility.NONE;
    }
    else if( isInEnclosingClassHierarchy( ownersClass, whosAskin ) )
    {
      return IRelativeTypeInfo.Accessibility.PROTECTED;
    }

    return IRelativeTypeInfo.Accessibility.PUBLIC;
  }

  public static boolean isInSameNamespace(IType ownersClass, IType whosAskin) {
    String whosAskinNamespace = getTopLevelEnclosingClassNamespace(whosAskin);
    return whosAskinNamespace != null &&
           whosAskinNamespace.equals( getTopLevelEnclosingClassNamespace( ownersClass ) );
  }

  private static String getTopLevelEnclosingClassNamespace(IType type) {
    IType topLevelClass = type;
    while (topLevelClass.getEnclosingType() != null) {
      topLevelClass = topLevelClass.getEnclosingType();
    }
    return topLevelClass.getNamespace();
  }

  public static boolean isInEnclosingClassHierarchy(IType ownersClass, IType whosAskin) {
    return whosAskin != null && ownersClass != null &&
           (isInHierarchy(ownersClass, whosAskin) ||
            isInEnhancedTypesHierarchy(ownersClass, whosAskin) ||
            isInEnclosingClassHierarchy(ownersClass, whosAskin.getEnclosingType()));
  }

  protected static boolean isInEnhancedTypesHierarchy(IType ownersClass, IType whosAskin) {
    return (whosAskin instanceof IGosuEnhancement &&
           ((IGosuEnhancement)whosAskin).getEnhancedType() != null &&
           ownersClass.isAssignableFrom(((IGosuEnhancement) whosAskin).getEnhancedType()));
  }

  protected static boolean isInHierarchy(IType ownersClass, IType whosAskin) {
    return ownersClass.isAssignableFrom(whosAskin) ||
           (ownersClass instanceof IGosuClass && ((IGosuClass) ownersClass).isSubClass( whosAskin ));
  }

  private static String getTopLevelTypeName( IType type )
  {
    while( type.getEnclosingType() != null )
    {
      type = TypeSystem.getPureGenericType( type.getEnclosingType() );
    }
    return TypeSystem.getPureGenericType( type ).getName();
  }

  public static boolean isFeatureAccessible(IAttributedFeatureInfo property, IRelativeTypeInfo.Accessibility accessibility) {
    boolean isAccessible = false;
    switch (accessibility) {
      case NONE:
        break;
      case PUBLIC:
        if (property.isPublic()) {
          isAccessible = true;
        }
        break;
      case PROTECTED:
        if (property.isPublic() || property.isProtected()) {
          isAccessible = true;
        }
        break;
      case INTERNAL:
        if (property.isPublic() || property.isInternal() || property.isProtected()) {
          isAccessible = true;
        }
        break;
      case PRIVATE:
        if (property.isPublic() || property.isInternal() || property.isProtected() || property.isPrivate()) {
          isAccessible = true;
        }
        break;
    }
    return isAccessible;
  }

  public void clear() {
    _methodsInitialized = new HashMap<IModule, InitState>();
    _propertiesInitialized = new HashMap<IModule, InitState>();
    _ctorsInitialized = InitState.NotInitialized;
    clearMaps();
  }

  private void clearMaps() {
    for(PropertyNameMap<T>[] properties : _properties.values()) {
      for (int i = 0; i < properties.length; i++) {
        properties[i] = null;
      }
    }
    for (int i = 0; i < _constructors.length; i++) {
      _constructors[i] = null;
    }
    for(MethodList[] methods : _methods.values()) {
      for (int i = 0; i < methods.length; i++) {
        methods[i] = null;
      }
    }
  }

  private void clearProperties(IModule module) {
    PropertyNameMap<T>[] properties = _properties.get(module);
    if(properties != null) {
      for (int i = 0; i < properties.length; i++) {
        properties[i] = null;
      }
    }
  }

  private void clearMethods(IModule module) {
    MethodList[] methods = _methods.get(module);
    if(methods != null) {
      for (int i = 0; i < methods.length; i++) {
        methods[i] = null;
      }
    }
  }

  private void clearCtors() {
    for (int i = 0; i < _constructors.length; i++) {
      _constructors[i] = null;
    }
  }

  public List<IPropertyInfo> getProperties( IRelativeTypeInfo.Accessibility accessibility ) {
    maybeInitProperties();
    PropertyNameMap<T>[] arr = _properties.get( TypeSystem.getCurrentModule() );
    if( arr == null )
    {
      return Collections.emptyList();
    }
    PropertyNameMap<T> props = arr[accessibility.ordinal()];
    return (List<IPropertyInfo>) (props == null ? Collections.emptyList() : props.values());
  }

  public IPropertyInfo getProperty( IRelativeTypeInfo.Accessibility accessibility, CharSequence propName ) {
    maybeInitProperties();
    PropertyNameMap<T>[] arr = _properties.get( TypeSystem.getCurrentModule() );
    if( arr == null )
    {
      return null;
    }
    PropertyNameMap<T> accessMap = arr[accessibility.ordinal()];
    return accessMap == null ? null : accessMap.get(convertCharSequenceToCorrectSensitivity(propName));
  }

  private T convertCharSequenceToCorrectSensitivity(CharSequence propName) {
    return (T) (_caseSensitive ? propName == null ?  "": propName.toString() : CICS.get( propName ));
  }

  @SuppressWarnings({"unchecked"})
  public MethodList getMethods( IRelativeTypeInfo.Accessibility accessibility) {
    maybeInitMethods();
    MethodList[] arr = _methods.get( TypeSystem.getCurrentModule() );
    if( arr == null )
    {
      return MethodList.EMPTY;
    }
    MethodList iMethodInfos = arr[accessibility.ordinal()];
    return iMethodInfos == null ? MethodList.EMPTY : iMethodInfos;
  }

  public IMethodInfo getMethod( IRelativeTypeInfo.Accessibility accessibility, CharSequence methodName, IType... params ) {
    maybeInitMethods();
    return ITypeInfo.FIND.method( getMethods( accessibility ), methodName, params );
  }

  @SuppressWarnings({"unchecked"})
  public List<? extends IConstructorInfo> getConstructors( IRelativeTypeInfo.Accessibility accessibility ) {
    maybeInitConstructors();
    List<IConstructorInfo> list = _constructors[accessibility.ordinal()];
    return (List<IConstructorInfo>) (list == null ? Collections.emptyList() : list);
  }

  public IConstructorInfo getConstructor( IRelativeTypeInfo.Accessibility accessibility, IType[] params ) {
    maybeInitConstructors();
    return ITypeInfo.FIND.constructor( getConstructors(accessibility), params );
  }

  @SuppressWarnings({"ConstantConditions"})
  protected void maybeInitMethods() {
    IModule module = TypeSystem.getCurrentModule();
    if (module == null) {
      throw new NullPointerException("Cannot init the FeatureManager with no current module.");
    }
    if (_methodsInitialized.get(module) != InitState.Initialized && _methodsInitialized.get(module) != InitState.ERROR) {
      TypeSystem.lock();
      try {
        if (_methodsInitialized.get(module) != InitState.Initialized) {
          if (_methodsInitialized.get(module) == InitState.Initializing) {
            throw new IllegalStateException("Methods for " + _typeInfo.getOwnersType() + " are cyclic.");
          }
          _methodsInitialized.put(module, InitState.Initializing);
          clearMethods(module);
          try {
            MethodList[] methods = new MethodList[IRelativeTypeInfo.Accessibility_Size];
            {
              MethodList privateMethods = new MethodList();
              if( _addObjectMethods ) {
                mergeMethods( privateMethods, convertType( JavaTypes.OBJECT() ), false );
              }
              if( _typeInfo == null ) {
                throw new IllegalStateException( "Null TypeInfo" );
              }
              if( _typeInfo.getOwnersType() == null ) {
                throw new IllegalStateException( "null owner" );
              }
              if( _typeInfo.getOwnersType().getInterfaces() == null ) {
                throw new IllegalStateException( "null interfaces for " + _typeInfo.getOwnersType().getName() );
              }
              for (IType type : _typeInfo.getOwnersType().getInterfaces()) {
                mergeMethods(privateMethods, convertType(type), false);
              }
              if ( _typeInfo.getOwnersType().getSupertype() != null) {
                mergeMethods(privateMethods, convertType( _typeInfo.getOwnersType().getSupertype()), true);
              }
              List<? extends IMethodInfo> declaredMethods = _typeInfo.getDeclaredMethods();
              for (IMethodInfo methodInfo : declaredMethods) {
                mergeMethod(privateMethods, methodInfo, true);
              }
              addEnhancementMethods(privateMethods);
              privateMethods.trimToSize();
//              privateMethods = Collections.unmodifiableList(privateMethods);
              // The size checking madness is to save memory.  If the lists/maps are the same then reuse.
              methods[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = privateMethods;
              methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateMethods.filterMethods(IRelativeTypeInfo.Accessibility.PROTECTED);
              if (methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateMethods.size()) {
                methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateMethods;
              }
              methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = privateMethods.filterMethods(IRelativeTypeInfo.Accessibility.INTERNAL);
              if (methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = methods[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
              }
              methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = privateMethods.filterMethods(IRelativeTypeInfo.Accessibility.PUBLIC);
              if (methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                methods[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = methods[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
              }
              methods[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = MethodList.EMPTY;
            }
            _methods.put(module, methods);

            _methodsInitialized.put(module, InitState.Initialized);
          } finally {
            if (_methodsInitialized.get(module) != InitState.Initialized) {
              _methodsInitialized.put(module, InitState.ERROR);
            }
          }
        }
      } catch ( Exception ex ) {
        ex.printStackTrace(); // exception is swallowed by source diff handler? print it again here
        throw GosuExceptionUtil.forceThrow( ex, _typeInfo.getOwnersType().getName() );
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  @SuppressWarnings({"ConstantConditions"})
  protected void maybeInitProperties() {
    maybeInitMethods(); // because properties depend on methods for their getter or setter method !

    IModule module = TypeSystem.getCurrentModule();
    if (module == null) {
      throw new NullPointerException("Cannot init the FeatureManager with no current module.");
    }
    if (_propertiesInitialized.get(module) != InitState.Initialized && _propertiesInitialized.get(module) != InitState.ERROR) {
      TypeSystem.lock();
      try {
        if (_propertiesInitialized.get(module) != InitState.Initialized) {
          if (_propertiesInitialized.get(module) == InitState.Initializing) {
            throw new IllegalStateException("Properties for " + _typeInfo.getOwnersType() + " are cyclic.");
          }
          _propertiesInitialized.put(module, InitState.Initializing);
          clearProperties(module);
          try {
            PropertyNameMap<T>[] properties = new PropertyNameMap[IRelativeTypeInfo.Accessibility_Size];
            {
              PropertyNameMap privateProps = new PropertyNameMap();
              for (IType type : _typeInfo.getOwnersType().getInterfaces()) {
                mergeProperties(privateProps, convertType(type), false);
              }
              IType supertype = _typeInfo.getOwnersType().getSupertype();
              if ( supertype != null ) {
                mergeProperties( privateProps, convertType( supertype ), true );
              }

              List<IPropertyInfo> declaredProperties = (List<IPropertyInfo>) _typeInfo.getDeclaredProperties();
              for (IPropertyInfo property : declaredProperties) {
                mergeProperty(privateProps, property, true);
              }
              addEnhancementProperties(privateProps, _caseSensitive);
              privateProps.freeze();
              // The size checking madness is to save memory.  If the lists/maps are the same then reuse.
              properties[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = privateProps;
              properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.PROTECTED));
              if (properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateProps.size()) {
                properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateProps;
              }
              properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.INTERNAL));
              if (properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = properties[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
              }
              properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = convertToMap(filterFeatures(privateProps.values(), IRelativeTypeInfo.Accessibility.PUBLIC));
              if (properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                properties[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = properties[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
              }
              properties[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = new PropertyNameMap();
            }
            _properties.put(module, properties);

            _propertiesInitialized.put(module, InitState.Initialized);
          } finally {
            if (_propertiesInitialized.get(module) != InitState.Initialized) {
              _propertiesInitialized.put(module, InitState.ERROR);
            }
          }
        }
      } catch ( Exception ex ) {
        ex.printStackTrace(); // exception is swallowed by source diff handler? print it again here
        throw GosuExceptionUtil.forceThrow( ex, _typeInfo.getOwnersType().getName() );
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  @SuppressWarnings({"ConstantConditions"})
  protected void maybeInitConstructors() {
    if (_ctorsInitialized != InitState.Initialized && _ctorsInitialized != InitState.ERROR) {
      TypeSystem.lock();
      try {
        if (_ctorsInitialized != InitState.Initialized) {
          if (_ctorsInitialized == InitState.Initializing) {
            throw new IllegalStateException("Constructors for " + _typeInfo.getOwnersType() + " are cyclic.");
          }
          _ctorsInitialized = InitState.Initializing;
          clearCtors();
          try {
            if(_ctorsInitialized != InitState.Initialized && _ctorsInitialized != InitState.ERROR) {
              clearCtors();
              try {

                List<IConstructorInfo>[] constructors = new List[IRelativeTypeInfo.Accessibility_Size];
                {
                  List<IConstructorInfo> privateConstructors = new ArrayList<IConstructorInfo>( _typeInfo.getDeclaredConstructors());
                  ((ArrayList) privateConstructors).trimToSize();
                  privateConstructors = Collections.unmodifiableList(privateConstructors);
                  constructors[IRelativeTypeInfo.Accessibility.PRIVATE.ordinal()] = Collections.unmodifiableList(privateConstructors);
                  constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateConstructors, IRelativeTypeInfo.Accessibility.PROTECTED));
                  if (constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size() == privateConstructors.size()) {
                    constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()] = privateConstructors;
                  }
                  constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateConstructors, IRelativeTypeInfo.Accessibility.INTERNAL));
                  if (constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size() == constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()].size()) {
                    constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()] = constructors[IRelativeTypeInfo.Accessibility.PROTECTED.ordinal()];
                  }
                  constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = Collections.unmodifiableList((List<IConstructorInfo>) filterFeatures(privateConstructors, IRelativeTypeInfo.Accessibility.PUBLIC));
                  if (constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()].size() == constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()].size()) {
                    constructors[IRelativeTypeInfo.Accessibility.PUBLIC.ordinal()] = constructors[IRelativeTypeInfo.Accessibility.INTERNAL.ordinal()];
                  }
                  constructors[IRelativeTypeInfo.Accessibility.NONE.ordinal()] = Collections.emptyList();
                }
                _constructors = constructors;
                _ctorsInitialized = InitState.Initialized;
              } finally {
                if(_ctorsInitialized != InitState.Initialized) {
                  _ctorsInitialized = InitState.ERROR;
                }
              }
            }

            _ctorsInitialized = InitState.Initialized;
          } finally {
            if (_ctorsInitialized != InitState.Initialized) {
              _ctorsInitialized = InitState.ERROR;
            }
          }
        }
      } catch ( Exception ex ) {
//        ex.printStackTrace(); // exception is swallowed by source diff handler? print it again here
        throw GosuExceptionUtil.forceThrow( ex, _typeInfo.getOwnersType().getName() );
      } finally {
        TypeSystem.unlock();
      }
    }
  }

  protected IType convertType(IType type) {
    return type;
  }

  protected void addEnhancementMethods(List<IMethodInfo> privateMethods) {
    CommonServices.getEntityAccess().addEnhancementMethods(_typeInfo.getOwnersType(), privateMethods );
  }

  protected void addEnhancementProperties(PropertyNameMap<T> privateProps, boolean caseSensitive) {
    CommonServices.getEntityAccess().addEnhancementProperties(_typeInfo.getOwnersType(), privateProps, caseSensitive );
  }

  public void setSuperPropertyPrefix( String superPropertyPrefix ) {
    _superPropertyPrefix = superPropertyPrefix;
  }

  public void setSupertypeToCopyPropertiesFrom( IType supertypeToCopyPropertiesFrom ) {
    _supertypeToCopyPropertiesFrom = supertypeToCopyPropertiesFrom;
  }

  private PropertyNameMap<T> convertToMap(List<IPropertyInfo> features) {
    PropertyNameMap<T> ret = new PropertyNameMap();
    for (IPropertyInfo feature : features) {
      ret.put(convertCharSequenceToCorrectSensitivity(feature.getName()), feature);
    }
    ret.freeze();
    return ret;
  }

  private List filterFeatures(List props, IRelativeTypeInfo.Accessibility accessibility) {
    ArrayList<IFeatureInfo> ret = new ArrayList<IFeatureInfo>();
    for (Object o : props) {
      IAttributedFeatureInfo property = (IAttributedFeatureInfo) o;
      if (isFeatureAccessible(property, accessibility)) {
        ret.add(property);
      }
    }
    ret.trimToSize();
    return ret;
  }

  protected void mergeProperties(PropertyNameMap<T> props, IType type, boolean replace) {
    if( type != null )
    {
      List<? extends IPropertyInfo> propertyInfos;
      if (type.getTypeInfo() instanceof IRelativeTypeInfo) {
        propertyInfos = ((IRelativeTypeInfo) type.getTypeInfo()).getProperties( _typeInfo.getOwnersType());
      } else {
        propertyInfos = type.getTypeInfo().getProperties();
      }
      for (IPropertyInfo propertyInfo : propertyInfos) {
        IType ownersType = propertyInfo.getOwnersType();
        if ( _supertypeToCopyPropertiesFrom == null || ownersType.isAssignableFrom( _supertypeToCopyPropertiesFrom ) || ownersType instanceof IGosuEnhancement ) {
          mergeProperty( props, propertyInfo, replace );
        }
      }
    }
  }

  protected void mergeProperty(PropertyNameMap<T> props, IPropertyInfo propertyInfo, boolean replace) {
    boolean prependPrefix = _superPropertyPrefix != null && ! propertyInfo.getOwnersType().equals( _typeInfo.getOwnersType() );
    T cs = convertCharSequenceToCorrectSensitivity( prependPrefix ? ( _superPropertyPrefix + propertyInfo.getName() ) : propertyInfo.getName() );
    if (replace || !props.containsKey(cs)) {
      if ( prependPrefix ) {
        props.put( cs, new PropertyInfoDelegate( propertyInfo.getContainer(), propertyInfo, cs.toString() ) );
      }
      else {
        props.put(cs, propertyInfo);
      }
    }
  }

  protected void mergeMethods(List<IMethodInfo> methods, IType type, boolean replace) {
    List<? extends IMethodInfo> methodInfos;
    if (type != null && !TypeSystem.isDeleted(type)) {
      if (type.getTypeInfo() instanceof IRelativeTypeInfo) {
        methodInfos = ((IRelativeTypeInfo) type.getTypeInfo()).getMethods( _typeInfo.getOwnersType());
      } else {
        methodInfos = type.getTypeInfo().getMethods();
      }

      for (IMethodInfo methodInfo : methodInfos) {
        mergeMethod(methods, methodInfo, replace);
      }
    }
  }

  protected void mergeMethod(List<IMethodInfo> methods, IMethodInfo thisMethodInfo, boolean replace) {
    IType[] paramTypes = removeGenericMethodParameters(thisMethodInfo);
    boolean add = true;
    int replacementIndex = -1;
    for (int i = 0; i < methods.size(); i++) {
      IMethodInfo superMethodInfo = methods.get(i);
      replacementIndex++;
      if (isOverride(thisMethodInfo, superMethodInfo)) {
        IType[] superParamTypes;
        superParamTypes = removeGenericMethodParameters(superMethodInfo);
        if (argsEqual(superParamTypes, paramTypes)) {
          if (replace) {
            methods.set(replacementIndex, thisMethodInfo);
          }
          add = false;
          break;
        }
      }
    }

    if (add) {
      methods.add(thisMethodInfo);
    }
  }

  private boolean isOverride(IMethodInfo thisMethodInfo, IMethodInfo superMethodInfo) {
//    if( ILanguageLevel.Util.STANDARD_GOSU() ) {
      return superMethodInfo.getDisplayName().equals(thisMethodInfo.getDisplayName());
//    }
//    return superMethodInfo.getDisplayName().equalsIgnoreCase(thisMethodInfo.getDisplayName());
  }

  private IType[] removeGenericMethodParameters(IMethodInfo thisMethodInfo) {
    IParameterInfo[] parameters;
    if (thisMethodInfo instanceof IJavaMethodInfo) {
      parameters = ((IJavaMethodInfo) thisMethodInfo).getGenericParameters();
    } else {
      parameters = thisMethodInfo.getParameters();
    }
    IType[] paramTypes = new IType[parameters.length];

    List<IType> methodTypeVars = null;
    if (thisMethodInfo instanceof IGenericMethodInfo) {
      IGenericTypeVariable[] typeVariables = ((IGenericMethodInfo) thisMethodInfo).getTypeVariables();
      if (typeVariables != null && typeVariables.length > 0) {
        methodTypeVars = new ArrayList<IType>();
        for (IGenericTypeVariable typeVariable : typeVariables) {
          ITypeVariableType typeVarType = typeVariable.getTypeVariableDefinition().getType();
          methodTypeVars.add(typeVarType);
        }
      }
    }

    for (int i = 0; i < parameters.length; i++) {
      IParameterInfo parameter = parameters[i];
      IType featureType = parameter.getFeatureType();
      if (methodTypeVars != null) {
        featureType = TypeSystem.boundTypes(featureType, methodTypeVars);
      }
      paramTypes[i] = featureType;
    }

    return paramTypes;
  }

  protected boolean areMethodParamsEqual(IType thisMethodParamType, IType superMethodParamType) {
    return thisMethodParamType.equals(superMethodParamType);
  }

  private boolean argsEqual(IType[] parameters, IType[] parameters1) {
    if (parameters.length == parameters1.length) {
      for (int i = 0; i < parameters.length; i++) {
        IType parameter = parameters[i];
        if ( !areMethodParamsEqual(parameter, parameters1[i]) ) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  public String toString() {
    return _typeInfo.getOwnersType().getName();
  }

  private enum InitState {
    NotInitialized,
    Initializing,
    ERROR, Initialized
  }
}
