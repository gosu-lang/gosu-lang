/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.parser.IBlockClass;
import gw.lang.function.IBlock;
import gw.config.CommonServices;
import gw.util.GosuClassUtil;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

public class BlockCoercer extends BaseCoercer
{
  private static final BlockCoercer INSTANCE = new BlockCoercer();

  private BlockCoercer()
  {
  }

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    final IBlock blk = (IBlock)value;
    Class<? extends IBlock> aClass = blk.getClass();
    final Method finalInvoke = getInvokeMethod( aClass );
    final IFunctionType funType = (IFunctionType)typeToCoerceTo;

    Set<Class> classes = GosuClassUtil.getAllInterfaces( aClass );
    Class<?>[] interfaces = classes.toArray( new Class<?>[classes.size()] );

    return Proxy.newProxyInstance( blk.getClass().getClassLoader(), interfaces, new InvocationHandler()
    {
      @Override
      public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable
      {
        if( IBlockClass.INVOKE_METHOD_NAME.equals( method.getName() ) )
        {
          Object val = finalInvoke.invoke( blk, args );
          return CommonServices.getCoercionManager().convertValue( val, funType.getReturnType() );
        }
        else if( IBlockClass.INVOKE_WITH_ARGS_METHOD_NAME.equals( method.getName() ) )
        {
          Object val = blk.invokeWithArgs( (Object[]) args[0] );
          return CommonServices.getCoercionManager().convertValue( val, funType.getReturnType() );
        }
        else
        {
          return method.invoke( blk, args );
        }
      }
    } );
  }

  private Method getInvokeMethod( Class<? extends IBlock> aClass )
  {
    Method invokeMethod = null;
    for( Method method : aClass.getMethods() )
    {
      if( method.getName().equals( IBlockClass.INVOKE_METHOD_NAME ) )
      {
        invokeMethod = method;
      }
    }
    if( invokeMethod == null )
    {
      throw new IllegalStateException( "Unable to find an invoke method on class " + aClass.getName() );
    }
    return invokeMethod;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 2;
  }

  public static BlockCoercer instance()
  {
    return INSTANCE;
  }
}
