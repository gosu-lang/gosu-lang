package gw.specContrib.interop.java;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import gw.lang.function.IFunction1;
import gw.lang.reflect.ClassLazyTypeResolver;

public class FromJavaSubclass<T extends CharSequence> extends MyGenericGosu<T>
{
  private T _myT;
  
  public FromJavaSubclass( T t )
  {
    super( t, "hi" );
  }
  
  @Override
  public T getTee()
  {
    return _myT;
  }
  
  public void setMyTee( T t)
  {
    _myT = t;
  }
  
  @Override
  public <E> Map<T,E> foo( T t, E e )
  {
    Map<T,E> map = new HashMap<>();
    map.put( t, e );
    return map;
  }
  
  @Override
  public <R, E> R ret_block_one_arg( IFunction1<R,E> func, E e )
  {
    return func.invoke( e );
  }
  
  public StringBuilder call_reified()
  {
    return this.<StringBuilder>make( ClassLazyTypeResolver.StringBuilder ); 
  }
}