/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IExpression;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.NotLazyTypeResolver;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.java.GosuTypes;
import gw.util.GosuExceptionUtil;
import manifold.util.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 */
public class GosuConstructorInfo extends AbstractGenericMethodInfo implements IGosuConstructorInfo
{
  private IType _type;
  private IConstructorHandler _ctorHandler;

  public GosuConstructorInfo( IFeatureInfo container, DynamicFunctionSymbol dfs )
  {
    super( container, dfs );
    _type = getOwnersType();
  }

  public IType getType()
  {
    return _type;
  }

  public IConstructorHandler getConstructor()
  {
    if( _ctorHandler == null )
    {
      IGosuClassInternal gsClass = getGosuClass();
      if( !gsClass.isValid() )
      {
        throw new ErrantGosuClassException( gsClass );
      }
      _ctorHandler = new GosuConstructorHandler();
    }

    return _ctorHandler;
  }

  @Override
  public boolean hasRawConstructor( IConstructorInfo rawCtor )
  {
    return rawCtor instanceof GosuConstructorInfo && ((GosuConstructorInfo)rawCtor).getDfs().getType().equals( getDfs().getType() );
  }

  @Override
  public boolean isDefault() {
    ReducedDynamicFunctionSymbol dfs = getDfs();
    return dfs.getType().equals(GosuTypes.DEF_CTOR_TYPE());
  }

  @Override
  public IExpression[] getDefaultValueExpressions()
  {
    List<IExpression> defValues = new ArrayList<IExpression>();
    for( IReducedSymbol s : getArgs() )
    {
      IExpression defValue = s.getDefaultValueExpression();
      defValues.add( defValue );
    }
    return defValues.toArray( new IExpression[defValues.size()] );
  }

  @Override
  public String[] getParameterNames()
  {
    List<String> names = new ArrayList<String>();
    for( IReducedSymbol s : getArgs() )
    {
      names.add( s.getName() );
    }
    return names.toArray( new String[names.size()] );
  }

  @Override
  public IGosuConstructorInfo getBackingConstructorInfo() {
    return this;
  }

  public class GosuConstructorHandler implements IConstructorHandler
  {
    public Object newInstance( Object... a )
    {
      List<Object> args = a == null || a.length == 0 ? new ArrayList<>() : new ArrayList<>( Arrays.asList( a ) );
      IGosuClassInternal gsClass = (IGosuClassInternal)getType();
      if( !gsClass.isValid() )
      {
        throw new EvaluationException( "Cannot construct an instance of " + gsClass.getName() + " because it has compile errors." );
      }
      try
      {
        IGosuClassInternal type = getOwnersType();
        int iOuterOffset = 0;
        if( type.getEnclosingType() != null && !type.isStatic() )
        {
          iOuterOffset = 1;
        }
        if( type.isParameterizedType() )
        {
          IType[] typeParams = getOwnersType().getTypeParameters();
          for( int i = 0; i < typeParams.length; i++ ) {
            args.add( i + iOuterOffset, new NotLazyTypeResolver( typeParams[i] ) );
          }
        }
        else
        {
          if( type.isGenericType() )
          {
            IGenericTypeVariable[] typeVariables = type.getGenericTypeVariables();
            int i = 0;
            for( IGenericTypeVariable typeVariable : typeVariables )
            {
              args.add( i + iOuterOffset, new NotLazyTypeResolver( typeVariable.getBoundingType() ) );
            }
          }
        }
        Class<?> aClass = getOwnersType().getBackingClass();
        List<IRType> explicitParameterTypes = IRMethodFactory.createIRMethod( GosuConstructorInfo.this ).getAllParameterTypes();
        Class[] paramClasses = new Class[explicitParameterTypes.size()];
        for( int i = 0; i < explicitParameterTypes.size(); i++ )
        {
          paramClasses[i] = IRElement.maybeEraseStructuralType( explicitParameterTypes.get( i ) ).getJavaClass();
        }
        Constructor<?> constructor = aClass.getDeclaredConstructor( paramClasses );
        ReflectUtil.setAccessible( constructor );
        return constructor.newInstance( args.toArray() );
      }
      catch( InvocationTargetException e )
      {
        throw GosuExceptionUtil.forceThrow( e.getTargetException() );
      }
      catch( Throwable e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
    }
  }
}