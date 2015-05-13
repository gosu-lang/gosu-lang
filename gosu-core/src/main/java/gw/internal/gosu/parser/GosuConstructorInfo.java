/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.lang.ir.IRType;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.IExpression;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.reflect.IConstructorHandler;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.NotLazyTypeResolver;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.java.GosuTypes;
import gw.util.GosuExceptionUtil;

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
    public Object newInstance( Object... args )
    {
      IGosuClassInternal gsClass = (IGosuClassInternal)getType();
      if( !gsClass.isValid() )
      {
        throw new EvaluationException( "Cannot construct an instance of " + gsClass.getName() + " because it has compile errors." );
      }
      try
      {
        IGosuClassInternal type = getOwnersType();
        if( type.isParameterizedType() )
        {
          IType[] typeParams = getOwnersType().getTypeParameters();
          Object[] argsTemp = new Object[typeParams.length + args.length];
          int i = 0;
          for( ; i < typeParams.length; i++ ) {
            argsTemp[i] = new NotLazyTypeResolver( typeParams[i] );
          }
          System.arraycopy( args, 0, argsTemp, i, args.length );
          args = argsTemp;
        }
        else
        {
          if( type.isGenericType() )
          {
            IGenericTypeVariable[] typeVariables = type.getGenericTypeVariables();
            ArrayList<Object> argList = new ArrayList<Object>();
            for( IGenericTypeVariable typeVariable : typeVariables )
            {
              argList.add( new NotLazyTypeResolver( typeVariable.getBoundingType() ) );
            }
            argList.addAll( Arrays.asList( args ) );
            args = argList.toArray( new Object[argList.size()] );
          }
        }
        Class<?> aClass = getOwnersType().getBackingClass();
        List<IRType> explicitParameterTypes = IRMethodFactory.createIRMethod( GosuConstructorInfo.this ).getAllParameterTypes();
        Class[] paramClasses = new Class[explicitParameterTypes.size()];
        for( int i = 0; i < explicitParameterTypes.size(); i++ )
        {
          paramClasses[i] = explicitParameterTypes.get( i ).getJavaClass();
        }
        Constructor<?> constructor = aClass.getDeclaredConstructor( paramClasses );
        if( !constructor.isAccessible() )
        {
          constructor.setAccessible( true );
        }
        return constructor.newInstance( args );
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