/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.ir.nodes.IRMethodFactory;
import gw.internal.gosu.ir.nodes.IRMethodFromMethodInfo;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.ir.transform.util.NameResolver;
import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.lang.ir.IRElement;
import gw.lang.ir.IRType;
import gw.lang.parser.IExpression;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodCallHandler;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.NotLazyTypeResolver;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.util.GosuStringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 */
public class GosuMethodInfo extends AbstractGenericMethodInfo implements IGosuMethodInfo
{
  private IType _returnType;
  private IMethodCallHandler _callHandler;

  public GosuMethodInfo( IFeatureInfo container, DynamicFunctionSymbol dfs )
  {
    super( container, dfs );
  }

  public IType getReturnType()
  {
    if( _returnType == null )
    {
      IType rawReturnType = ((FunctionType)getDfs().getType()).getReturnType();
      IType ownerType = getOwnersType();
      if( ownerType.isParameterizedType() &&
          //## Since DFSs are parameterized now, I'm not sure we ever need to get the actual type here (or in the params)
          !(getDfs() instanceof ReducedParameterizedDynamicFunctionSymbol) )
      {
        TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( ownerType, ownerType );
        for( IGenericTypeVariable tv : getTypeVariables() )
        {
          if( actualParamByVarName.isEmpty() )
          {
            actualParamByVarName = new TypeVarToTypeMap();
          }
          actualParamByVarName.put( tv.getTypeVariableDefinition().getType(), tv.getTypeVariableDefinition().getType() );
        }
        _returnType = TypeLord.getActualType( rawReturnType, actualParamByVarName, true );
      }
      else
      {
        _returnType = rawReturnType;
      }
    }
    if (TypeSystem.isDeleted(_returnType)) {
      _returnType =TypeSystem.getErrorType();
    }
    if( _returnType.isGenericType() && !_returnType.isParameterizedType() )
    {
      _returnType = TypeLord.getDefaultParameterizedType( _returnType );
    }
    return _returnType;
  }

  public IMethodCallHandler getCallHandler()
  {
    if( _callHandler == null )
    {
      IGosuClassInternal gsClass = getGosuClass();
      if( !gsClass.isValid() )
      {
        throw new ErrantGosuClassException( gsClass );
      }
      _callHandler = gsClass.isStructure() && !isStatic() ? new ReflectiveMethodCallHandler(): new GosuMethodCallHandler();
    }

    return _callHandler;
  }

  public String getReturnDescription()
  {
    List<IAnnotationInfo> annotation = getAnnotationsOfType(JavaTypes.getGosuType(gw.lang.Returns.class));
    if( annotation.isEmpty() )
    {
      return "";
    }
    else
    {
      String value = (String) annotation.get( 0 ).getFieldValue("value");
      return value == null ? "" : value;
    }
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
    for( IReducedSymbol a : getArgs() )
    {
      names.add( a.getName() );
    }
    return names.toArray( new String[names.size()] );
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
    GosuMethodInfo that = (GosuMethodInfo)o;
    return getName().equals(that.getName());
  }

  public int hashCode()
  {
    return getName().hashCode();
  }

  public int compareTo( Object o )
  {
    return getName().compareTo(((IMethodInfo) o).getName());
  }

  public boolean isMethodForProperty() {
    return getName().startsWith("@");
  }

  @Override
  public IGosuMethodInfo getBackingMethodInfo() {
    return this;
  }

  @Override
  public String toString() {
    return getName();
  }

  public static Method getMethod( Class<?> clazz, String strName, Class[] argClasses )
  {
    if( strName.startsWith( "@" ) )
    {
      strName = argClasses.length == 1
                ? "set" + strName.substring( 1 )
                : "get" + strName.substring( 1 );
    }

    Method method = AbstractElementTransformer.getDeclaredMethod( clazz, strName, argClasses );
    if( method == null )
    {
      throw new IllegalStateException( "Could not find method " + strName + "(" + GosuStringUtil.join( ",", argClasses ) + ") on " + clazz );
    }
    method.setAccessible( true );
    return method;
  }

  protected List<IGosuAnnotation> getGosuAnnotations()
  {
    ReducedDynamicFunctionSymbol dfs = getDfs();
    if( dfs instanceof ReducedDelegateFunctionSymbol )
    {
      IMethodInfo miTarget = ((ReducedDelegateFunctionSymbol)dfs).getTargetMethodInfo();
      if( miTarget != this && miTarget instanceof AbstractGenericMethodInfo )
      {
        if( getOwnersType().isCompiled() ) {
          // Ensure the delegate's owner is fully compiled, otherwise the annotations won't be fully formed (have NewExpressions, see PL-21981)
          miTarget.getOwnersType().isValid();
        }
        return ((AbstractGenericMethodInfo)miTarget).getGosuAnnotations();
      }
    }
    return super.getGosuAnnotations();
  }

  @Override
  public boolean hasAnnotationDefault()
  {
    return getDfs().getDefaultValueExpression() != null;
  }

  @Override
  public Object getAnnotationDefault()
  {
    IExpression annotationDefault = getDfs().getDefaultValueExpression();
    if( annotationDefault != null )
    {
      return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( annotationDefault.evaluate(), getOwnersType().getTypeInfo() );
    }
    return null;
  }

  //----------------------------------------------------------------------------
  // -- private methods --

  private class GosuMethodCallHandler implements IMethodCallHandler
  {
    public Object handleCall( Object gsClassInstance, Object... args )
    {
      ReducedDynamicFunctionSymbol dfs = getDfs();
      try
      {
        boolean isEnhancement = AbstractElementTransformer.requiresImplicitEnhancementArg( dfs );
        IGosuClassInternal dfsClass = dfs.getGosuClass();

        // If this is an enhancement method or requires method type variable arguments
        // do the dirty work of extracting the appropriate argumetns
        if( isEnhancement || dfs.hasTypeVariables() || dfs.getGosuClass() instanceof IGosuProgram )
        {
          List<Object> argList = new ArrayList<Object>();

          //Handle enhancement args
          if( isEnhancement )
          {
            argList.add( gsClassInstance );
            if( !dfs.isStatic() )
            {
              if( dfsClass.isParameterizedType() )
              {
                for( int i = 0; i < dfsClass.getTypeParameters().length; i++ )
                {
                  IType paramType = dfsClass.getTypeParameters()[i];
                  argList.add( new NotLazyTypeResolver( paramType ) );
                }
              }
              else
              {
                for( int i = 0; i < dfsClass.getGenericTypeVariables().length; i++ )
                {
                  IGenericTypeVariable tv = dfsClass.getGenericTypeVariables()[i];
                  argList.add( new NotLazyTypeResolver( tv.getBoundingType() ) );
                }
              }
            }
          }

          //handle function args
          for( IGenericTypeVariable typeVar : dfs.getType().getGenericTypeVariables() )
          {
            argList.add( new NotLazyTypeResolver( typeVar.getBoundingType() ) );
          }

          // If it's an instance of a Gosu program, for now pass through null for the external symbols argument
//            if ( dfs.getGosuClass() instanceof IGosuProgram ) {
//              argList.add( null );
//            }

          if ( args != null ) {
            argList.addAll( Arrays.asList( args ) );
          }
          args = argList.toArray();
        }
        Class clazz = dfsClass.getBackingClass();
        IRMethodFromMethodInfo irMethod = IRMethodFactory.createIRMethod(GosuMethodInfo.this, (IFunctionType) dfs.getType());
        List<IRType> allParameterTypes = irMethod.getAllParameterTypes();
        Class[] paramClasses = new Class[allParameterTypes.size()];
        for (int i = 0; i < allParameterTypes.size(); i++) {
          paramClasses[i] = IRElement.maybeEraseStructuralType( allParameterTypes.get( i ) ).getJavaClass();
        }
        Method method = getMethod( clazz, NameResolver.getFunctionName( dfs ), paramClasses );
        return method.invoke( gsClassInstance, args );
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
  }

  private class ReflectiveMethodCallHandler implements IMethodCallHandler {
    @Override
    public Object handleCall( Object ctx, Object... args )
    {
      return ReflectUtil.invokeMethod( ctx, getDisplayName(), args );
    }
  }
}
