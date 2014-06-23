/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IEventInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.gs.TypeName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public class PackageTypeInfo extends BaseFeatureInfo implements IRelativeTypeInfo {
  private final PackageType _packageType;
  private List<IPropertyInfo> _properties;
  @NotNull
  private final Map _propertiesByName;

  public PackageTypeInfo(PackageType packageType) {
    super(packageType);
    _packageType = packageType;
    _propertiesByName = new HashMap();
  }

  public boolean isStatic() {
    return false;
  }

  public String getName() {
    return _packageType.getName();
  }

  public List<? extends IPropertyInfo> getProperties() {
    makeProperties(null);
    return _properties;
  }

  @Nullable
  public IPropertyInfo getProperty(CharSequence propName) {
    makeProperties(null);
    return (IPropertyInfo) _propertiesByName.get(propName);
  }

  @Nullable
  public IMethodInfo getCallableMethod(CharSequence strMethod, IType... params) {
    return null;
  }

  @Nullable
  public IConstructorInfo getCallableConstructor(IType... params) {
    return null;
  }

  private void makeProperties(IType whosaskin) {
    if (_properties != null) {
      return;
    }

    _properties = new ArrayList<>();
    ArrayList<TypeName> children = new ArrayList<>(_packageType.getNamespaceType().getChildren(whosaskin));
    Collections.sort(children);
    for (TypeName typeName : children) {
      IPropertyInfo pi;
      if (typeName.kind == TypeName.Kind.TYPE) {
        TypeInPackageType type = new TypeInPackageType(typeName);
        pi = new TypePropertyInfo(this, type);
        _propertiesByName.put(pi.getFeatureType().getRelativeName(), pi);
      } else {
        pi = new PackagePropertyInfo(PackageTypeInfo.this, typeName);
        _propertiesByName.put(pi.getFeatureType().getRelativeName(), pi);
      }
      _properties.add(pi);
    }
  }

  public MethodList getMethods() {
    return MethodList.EMPTY;
  }

  @Nullable
  public IMethodInfo getMethod(CharSequence methodName, IType... params) {
    return null;
  }

  public List<IConstructorInfo> getConstructors() {
    return Collections.EMPTY_LIST;
  }

  @Nullable
  public IConstructorInfo getConstructor(IType... params) {
    return null;
  }

  public List<IEventInfo> getEvents() {
    return Collections.EMPTY_LIST;
  }

  @Nullable
  public IEventInfo getEvent(CharSequence strEvent) {
    return null;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  @NotNull
  @Override
  public Accessibility getAccessibilityForType(IType whosaskin) {
    return Accessibility.PUBLIC;
  }

  @Nullable
  @Override
  public IConstructorInfo getConstructor(IType whosAskin, IType[] params) {
    return null;
  }

  @Override
  public List<? extends IConstructorInfo> getConstructors(IType whosaskin) {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IConstructorInfo> getDeclaredConstructors() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IMethodInfo> getDeclaredMethods() {
    return Collections.emptyList();
  }

  @Override
  public List<? extends IPropertyInfo> getDeclaredProperties() {
    return getProperties();
  }

  @Nullable
  @Override
  public IMethodInfo getMethod(IType whosaskin, CharSequence methodName, IType... params) {
    return null;
  }

  @Override
  public MethodList getMethods(IType whosaskin) {
    return MethodList.EMPTY;
  }

  @NotNull
  @Override
  public List<? extends IPropertyInfo> getProperties(IType whosaskin) {
    makeProperties(whosaskin);
    List<IPropertyInfo> properties = new ArrayList<>();
    for (IPropertyInfo property : _properties) {
      //TODO-dp this is very slow
//      if (property instanceof TypePropertyInfo) {
//        IType type = TypeSystem.getByFullNameIfValid(property.getFeatureType().getName());
//        if (type != null) {
//          if (type.getTypeInfo() instanceof IRelativeTypeInfo) {
//            switch (((IRelativeTypeInfo) type.getTypeInfo()).getAccessibilityForType(whosaskin)) {
//              case PUBLIC:
//                if (Modifier.isPublic(type.getModifiers())) {
//                  properties.add(property);
//                }
//                break;
//              case PROTECTED:
//                if (Modifier.isPublic(type.getModifiers())
//                    || Modifier.isProtected(type.getModifiers())) {
//                  properties.add(property);
//                }
//                break;
//              case INTERNAL:
//                if (!Modifier.isPrivate(type.getModifiers())) {
//                  properties.add(property);
//                }
//                break;
//              case PRIVATE:
//                properties.add(property);
//                break;
//            }
//          } else {
//            properties.add(property);
//          }
//        }
//      }
//      else
      {
        properties.add(property);
      }
    }
    return properties;
  }

  @Nullable
  @Override
  public IPropertyInfo getProperty(IType whosaskin, CharSequence propName) {
    for (IPropertyInfo propertyInfo : getProperties(whosaskin)) {
      if (propertyInfo.getName().equals(propName)) {
        return propertyInfo;
      }
    }
    return null;
  }
}
