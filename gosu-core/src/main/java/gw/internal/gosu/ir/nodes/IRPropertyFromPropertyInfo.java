/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.internal.gosu.parser.*;
import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.gs.IGosuPropertyInfo;
import gw.lang.reflect.gs.IGosuVarPropertyInfo;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.*;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.IRTypeResolver;
import gw.internal.gosu.ir.transform.util.AccessibilityUtil;

public class IRPropertyFromPropertyInfo implements IRProperty {

  private IPropertyInfo _terminalProperty;

  public IRPropertyFromPropertyInfo(IPropertyInfo originalProperty) {
    _terminalProperty = originalProperty;

    while (_terminalProperty instanceof IPropertyInfoDelegate) {
      _terminalProperty = ((IPropertyInfoDelegate) _terminalProperty).getSource();
    }
  }

  public IPropertyInfo getTerminalProperty() {
    return _terminalProperty;
  }

  @Override
  public IRType getType() {
    return getPropertyIRType( _terminalProperty );
  }

  @Override
  public String getName() {
    if( _terminalProperty.getClass() == JavaFieldPropertyInfo.class ) {
      return ((JavaFieldPropertyInfo)_terminalProperty).getField().getName();
    } else {
      return _terminalProperty.getName();
    }
  }

  @Override
  public boolean isField() {
    return _terminalProperty instanceof IGosuVarPropertyInfo ||
           _terminalProperty instanceof IJavaFieldPropertyInfo;
  }

  @Override
  public boolean isCaptured() {
    return false;
  }

  @Override
  public IRMethod getGetterMethod() {
    return new IRMethodForPropertyGetter( this );
  }

  @Override
  public IRMethod getSetterMethod() {
    return new IRMethodForPropertySetter( this );
  }

  @Override
  public IRType getOwningIRType() {
    if( _terminalProperty instanceof IJavaPropertyInfo )
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaPropertyDescriptor descriptor = ((IJavaPropertyInfo)_terminalProperty).getPropertyDescriptor();
      IJavaClassMethod m = descriptor.getReadMethod();
      if( m != null )
      {
        return IRTypeResolver.getDescriptor( m.getEnclosingClass() );
      }
      else
      {
        m = descriptor.getWriteMethod();
        if( m != null )
        {
          return IRTypeResolver.getDescriptor( m.getEnclosingClass() );
        }
      }
    } else if (_terminalProperty instanceof IJavaFieldPropertyInfo) {
      IJavaClassField field = ((IJavaFieldPropertyInfo) _terminalProperty).getField();
      if (field != null) {
        return IRTypeResolver.getDescriptor(field.getEnclosingClass());
      }
    }
    return IRTypeResolver.getDescriptor( _terminalProperty.getOwnersType() );
  }

  @Override
  public IType getOwningIType() {
    IType owningType;
    if( _terminalProperty instanceof IJavaPropertyInfo )
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaPropertyDescriptor descriptor = ((IJavaPropertyInfo)_terminalProperty).getPropertyDescriptor();
      IJavaClassMethod m = descriptor.getReadMethod();
      if( m != null )
      {
        owningType = TypeSystem.get( m.getEnclosingClass() );
      }
      else
      {
        m = descriptor.getWriteMethod();
        if( m != null )
        {
          owningType = TypeSystem.get( m.getEnclosingClass() );
        } else {
          owningType = _terminalProperty.getOwnersType();
        }
      }
    } else if (_terminalProperty instanceof IJavaFieldPropertyInfo) {
      IJavaClassField field = ((IJavaFieldPropertyInfo) _terminalProperty).getField();
      if (field != null) {
        owningType = TypeSystem.get(field.getEnclosingClass());
      } else {
        owningType = _terminalProperty.getOwnersType();
      }
    } else {
      owningType = _terminalProperty.getOwnersType();
    }

    if( owningType instanceof IMetaType)
    {
      owningType = ((IMetaType)owningType).getType();
    }
    return owningType;
  }

  @Override
  public IRelativeTypeInfo.Accessibility getAccessibility() {
    return AccessibilityUtil.forFeatureInfo( _terminalProperty );
  }

  @Override
  public boolean isStatic() {
    return _terminalProperty.isStatic();
  }

  @Override
  public IRType getTargetRootIRType() {
    IRType owner = getOwningIRType();
    if( owner instanceof GosuClassIRType && ((GosuClassIRType)owner).getType() instanceof IGosuEnhancement)
    {
      return IRTypeResolver.getDescriptor( ((IGosuEnhancement)((GosuClassIRType)owner).getType()).getEnhancedType() );
    }
    else
    {
      return owner;
    }
  }

  @Override
  public boolean isBytecodeProperty() {
    return _terminalProperty instanceof IJavaPropertyInfo ||
            _terminalProperty instanceof IGosuPropertyInfo ||
            _terminalProperty instanceof IJavaFieldPropertyInfo ||
            _terminalProperty instanceof IGosuVarPropertyInfo;
  }

  public IRType getPropertyIRType( IPropertyInfo pi )
  {
    if( pi instanceof IJavaPropertyInfo)
    {
      // We have to get the owner type from the method because it may be different from the owning type e.g., entity aspects see ContactGosuAspect.AllAdresses
      IJavaPropertyDescriptor descriptor = ((IJavaPropertyInfo)pi).getPropertyDescriptor();
      IJavaClassMethod m = descriptor.getReadMethod();
      if( m != null )
      {
        return IRTypeResolver.getDescriptor( m.getReturnClassInfo() );
      }
      else
      {
        m = descriptor.getWriteMethod();
        if( m != null )
        {
          return IRTypeResolver.getDescriptor( m.getParameterTypes()[0] );
        }
      }
    } else if ( pi instanceof IJavaFieldPropertyInfo) {
      return IRTypeResolver.getDescriptor( ((IJavaFieldPropertyInfo) pi).getField().getType());
    } else if (pi instanceof IGosuPropertyInfo) {
      IReducedDynamicPropertySymbol dps = ((IGosuPropertyInfo)pi).getDps();
      return getBoundedPropertyType( dps );
    } else if (pi instanceof IGosuVarPropertyInfo) {
      IType declaredVarType = ((IGosuVarPropertyInfo)pi).getScopedSymbolType();
      return IRTypeResolver.getDescriptor( TypeLord.getDefaultParameterizedTypeWithTypeVars( declaredVarType ) );
    }

    return IRTypeResolver.getDescriptor( pi.getFeatureType() );
  }

  public IRType getBoundedPropertyType( IReducedDynamicPropertySymbol dps )
  {
    while( dps instanceof ReducedParameterizedDynamicPropertySymbol)
    {
      ReducedParameterizedDynamicPropertySymbol pdfs = (ReducedParameterizedDynamicPropertySymbol)dps;
      dps = pdfs.getDelegate();
    }

    if( dps.getGosuClass() != null && IGosuClass.ProxyUtil.isProxy( dps.getGosuClass() ) )
    {
      return getBoundedReturnTypeFromProxiedClass( dps );
    }

    return IRTypeResolver.getDescriptor( TypeLord.getDefaultParameterizedTypeWithTypeVars( dps.getType() ) );
  }

  private IRType getBoundedReturnTypeFromProxiedClass( IReducedDynamicPropertySymbol dps )
  {
    IJavaPropertyDescriptor pd = getJavaPropertyFromProxy( dps );
    IJavaClassInfo type = pd.getReadMethod() != null
                          ? pd.getReadMethod().getReturnClassInfo()
                          : pd.getPropertyClassInfo();
    return JavaClassIRType.get( type );
  }

  private IJavaPropertyDescriptor getJavaPropertyFromProxy( IReducedDynamicPropertySymbol dps )
  {
    IType proxyType = dps.getGosuClass();
    IJavaType javaType = (IJavaType) IGosuClass.ProxyUtil.getProxiedType( proxyType );

    javaType = (IJavaType)TypeLord.getDefaultParameterizedType( javaType );
    IJavaPropertyInfo jpi = (IJavaPropertyInfo)((IRelativeTypeInfo)javaType.getTypeInfo()).getProperty( javaType, dps.getName() );
    return jpi.getPropertyDescriptor();
  }
}
