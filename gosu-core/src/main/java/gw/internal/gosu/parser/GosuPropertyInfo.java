/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.ir.IRElement;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.parser.EvaluationException;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.*;
import gw.lang.ir.IRType;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.nodes.IRPropertyFactory;
import gw.internal.gosu.ir.nodes.IRMethod;
import gw.util.GosuExceptionUtil;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuPropertyInfo;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 */
public class GosuPropertyInfo extends GosuBaseAttributedFeatureInfo implements IGosuPropertyInfo
{
  private ReducedDynamicPropertySymbol _dps;
  private IType _type;
  private IType _assignableType;
  private IPropertyAccessor _accessor;
  private boolean _bReadable;

  public GosuPropertyInfo( IFeatureInfo container, DynamicPropertySymbol dps )
  {
    super( container );
    _dps = (ReducedDynamicPropertySymbol) dps.createReducedSymbol();
    _bReadable = _dps.isReadable();
    ((GosuClassTypeInfo)getOwnersType().getTypeInfo()).setModifierInfo( this, dps.getModifierInfo() );
  }

  public String getName()
  {
    return _dps.getName();
  }

  public String getDisplayName()
  {
    return _dps.getDisplayName();
  }

  public String getShortDescription()
  {
    return _dps.getDisplayName();
  }

  public String getDescription()
  {
    return _dps.getFullDescription();
  }

  public boolean isStatic()
  {
    return _dps.isStatic();
  }

  public boolean isPrivate()
  {
    return _dps.isPrivate();
  }

  public boolean isInternal()
  {
    return _dps.isInternal();
  }

  public boolean isProtected()
  {
    return _dps.isProtected();
  }

  public boolean isPublic()
  {
    return _dps.isPublic();
  }

  public boolean isAbstract()
  {
    return _dps.isAbstract();
  }

  public boolean isFinal()
  {
    return _dps.isFinal();
  }

  public boolean isReified()
  {
    return _dps.isReified();
  }

  public boolean isGetterDefault()
  {
    return isDefault( _dps.getGetterDfs() );
  }
  public boolean isSetterDefault()
  {
    return isDefault( _dps.getSetterDfs() );
  }
  private boolean isDefault( ReducedDynamicFunctionSymbol dfs )
  {
    if( dfs == null )
    {
      return false;
    }

    // Default methods are public non-abstract instance methods declared in an interface.
    return ((dfs.getModifiers() & (java.lang.reflect.Modifier.ABSTRACT | java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.STATIC)) ==
            java.lang.reflect.Modifier.PUBLIC) && getOwnersType().isInterface();
  }
  @Override
  protected List<IGosuAnnotation> getGosuAnnotations()
  {
    return _dps.getAnnotations();
  }

  public boolean isReadable()
  {
    return isReadable( null );
  }
  public boolean isReadable( IType whosAskin )
  {
    return isAccessible( whosAskin, _dps.getGetterDfs() );
  }

  public boolean isWritable()
  {
    return isWritable( null );
  }
  public boolean isWritable( IType whosAskin )
  {
    return isAccessible( whosAskin, _dps.getSetterDfs() );
  }

  public boolean isAccessible( IType whosAskin, ReducedDynamicFunctionSymbol accessor )
  {
    if( accessor != null )
    {
      IRelativeTypeInfo.Accessibility accessibilityForType = ((IRelativeTypeInfo)getContainer()).getAccessibilityForType( whosAskin );
      switch( accessibilityForType )
      {
        case PUBLIC:
          return accessor.isPublic();
        case PROTECTED:
          return accessor.isPublic() || accessor.isProtected();
        case INTERNAL:
          return accessor.isPublic() || accessor.isInternal() || accessor.isProtected();
        case PRIVATE:
          return accessor.isPublic() || accessor.isInternal() || accessor.isProtected() || accessor.isPrivate();
      }
    }
    return false;
  }

  public IPropertyAccessor getAccessor()
  {
    if( _accessor == null )
    {
      IGosuClassInternal gsClass = getGosuClass();
      if( !gsClass.isValid() )
      {
        throw new ErrantGosuClassException( gsClass );
      }
      _accessor = getOwnersType().isStructure() ? new ReflectivePropertyAccessor() : new GosuPropertyAccessor();
    }
    return _accessor;
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  IGosuClassInternal getGosuClass()
  {
    return getOwnersType();
  }

  public IType getFeatureType()
  {
    if( _type == null )
    {
      _type = getActualTypeInContainer( this, _dps.getType() );
    }
    return _type;
  }

  public IType getAssignableFeatureType()
  {
    if( _assignableType == null )
    {
      _assignableType = getActualTypeInContainer( this, _dps.getAssignableType() );
    }
    return _assignableType;
  }

  public ReducedDynamicPropertySymbol getDps()
  {
    return _dps;
  }

  @Override
  public IType getContainingType() {
    return getGosuClass();
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }
    GosuPropertyInfo that = (GosuPropertyInfo)o;
    return getName().equals( that.getName() );
  }

  public int hashCode()
  {
    return getName().hashCode();
  }

  public GenericTypeVariable[] getTypeVariables()
  {
    return GenericTypeVariable.EMPTY_TYPEVARS;
  }

  public IType getParameterizedReturnType( IType... typeParams )
  {
    return null;
  }

  public IType[] getParameterizedParameterTypes( IType... typeParams )
  {
    return IType.EMPTY_ARRAY; 
  }
  public IType[] getParameterizedParameterTypes2( IType ownersType, IType... typeParams )
  {
    return IType.EMPTY_ARRAY;
  }

  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes( IType... argTypes )
  {
    return null;
  }

  @Override
  public TypeVarToTypeMap inferTypeParametersFromArgumentTypes2( IType owningParameterizedType, IType... argTypes )
  {
    return null;
  }

  //----------------------------------------------------------------------------
  // -- private methods --

  private class GosuPropertyAccessor implements IPropertyAccessor
  {
    public Object getValue( Object ctx )
    {      
      String methodName = NameResolver.getGetterNameForDPS( _dps );
      try
      {
        Object[] args;
        ReducedDynamicFunctionSymbol getterDfs = _dps.getGetterDfs();
        if( AbstractElementTransformer.requiresImplicitEnhancementArg( getterDfs ) )
        {
          IGosuEnhancementInternal enhancement = (IGosuEnhancementInternal)getterDfs.getGosuClass();
          List<Object> argList = new ArrayList<Object>();

          argList.add( ctx );

          if( getterDfs.isReified() )
          {
            if( enhancement.isParameterizedType() )
            {
              IType[] parameters = enhancement.getTypeParameters();
              for( IType parameter : parameters )
              {
                argList.add( new NotLazyTypeResolver( parameter ) );
              }
            }
            else
            {
              IGenericTypeVariable[] typeVariables = enhancement.getGenericTypeVariables();
              for( IGenericTypeVariable typeVariable : typeVariables )
              {
                argList.add( new NotLazyTypeResolver( typeVariable.getBoundingType() ) );
              }
            }
          }
          args = argList.toArray( new Object[argList.size()] );
        }
        else
        {
          args = new Object[0];
        }
        IRMethod getterMethod = IRPropertyFactory.createIRProperty(GosuPropertyInfo.this).getGetterMethod();
        List<IRType> allParameterTypes = getterMethod.getAllParameterTypes();
        Class[] paramClasses = new Class[allParameterTypes.size()];
        for (int i = 0; i < allParameterTypes.size(); i++) {
          paramClasses[i] = IRElement.maybeEraseStructuralType( allParameterTypes.get( i ) ).getJavaClass();
        }
        Method method = GosuMethodInfo.getMethod( getOwnersType().getBackingClass(), methodName, paramClasses );
        return method.invoke( ctx, args );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
      catch( InvocationTargetException e )
      {
        throw GosuExceptionUtil.forceThrow( e.getTargetException() );
      }
    }

    public void setValue( Object ctx, Object value )
    {
      if( !isWritable( getOwnersType()) )
      {
        throw new EvaluationException( "Can't set value on read-only property: " + getDisplayName() );
      }
      
      String methodName = NameResolver.getSetterNameForDPS( _dps );
      try
      {
        Object[] args;
        ReducedDynamicFunctionSymbol setterDfs = _dps.getSetterDfs();
        if( AbstractElementTransformer.requiresImplicitEnhancementArg( setterDfs ) )
        {
          IGosuEnhancementInternal enhancement = (IGosuEnhancementInternal)setterDfs.getGosuClass();
          List<Object> argList = new ArrayList<Object>();

          argList.add( ctx );

          if( setterDfs.isReified() )
          {
            if( enhancement.isParameterizedType() )
            {
              IType[] parameters = enhancement.getTypeParameters();
              for( IType parameter : parameters )
              {
                argList.add( new NotLazyTypeResolver( parameter ) );
              }
            }
            else
            {
              IGenericTypeVariable[] typeVariables = enhancement.getGenericTypeVariables();
              for( IGenericTypeVariable typeVariable : typeVariables )
              {
                argList.add( new NotLazyTypeResolver( typeVariable.getBoundingType() ) );
              }
            }
          }

          argList.add( value );

          args = argList.toArray( new Object[argList.size()] );
        }
        else
        {
          args = new Object[]{value};
        }
        IRMethod setterMethod = IRPropertyFactory.createIRProperty(GosuPropertyInfo.this).getSetterMethod();
        List<IRType> allParameterTypes = setterMethod.getAllParameterTypes();
        Class[] paramClasses = new Class[allParameterTypes.size()];
        for (int i = 0; i < allParameterTypes.size(); i++) {
          paramClasses[i] = IRElement.maybeEraseStructuralType( allParameterTypes.get( i ) ).getJavaClass();
        }
        Method method = GosuMethodInfo.getMethod( getOwnersType().getBackingClass(), methodName, paramClasses );
        method.invoke( ctx, args );
      }
      catch( InvocationTargetException e )
      {
        throw GosuExceptionUtil.forceThrow( e.getTargetException() );
      }
      catch( IllegalAccessException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  }

  private class ReflectivePropertyAccessor implements IPropertyAccessor {
    @Override
    public Object getValue( Object ctx )
    {
      return ReflectUtil.getProperty( ctx, getName() );
    }

    @Override
    public void setValue( Object ctx, Object value )
    {
      ReflectUtil.setProperty( ctx, getName(), value );
    }
  }

  @Override
  public IMethodInfo getReadMethodInfo() {
    ReducedDynamicFunctionSymbol getterDfs = _dps.getGetterDfs();
    if(getterDfs != null) {
      return (IMethodInfo) getterDfs.getMethodOrConstructorInfo();
    } else if(isReadable() && _dps.getParent() instanceof IMethodBackedPropertyInfo) {
      return ((IMethodBackedPropertyInfo)_dps.getParent()).getReadMethodInfo();
    }
    return null;
  }

  @Override
  public IMethodInfo getWriteMethodInfo() {
    ReducedDynamicFunctionSymbol setterDfs = _dps.getSetterDfs();
    if(setterDfs != null) {
      return (IMethodInfo) setterDfs.getMethodOrConstructorInfo();
    }
//    else if(isWritable() && _dps.getParent() instanceof IMethodBackedPropertyInfo) {
//      return ((IMethodBackedPropertyInfo)_dps.getParent()).getWriteMethodInfo();
//    }
    return null;
  }


  public String toString() {
    return getName();
  }

}
