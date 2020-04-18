package gw.interop;

import gw.lang.reflect.IType;
import java.util.Map;

public class FromJava
{
  public MyGosu construct_no_args()
  {
    return new MyGosu();
  }  
  public MyGosu construct_one_arg()
  {
    return new MyGosu( "one_arg" );
  }
  
  public MyGenericGosu raw_gen_construct_no_args()
  {
    return new MyGenericGosu();
  }
  public MyGenericGosu raw_gen_construct_one_arg()
  {
    return new MyGenericGosu( "raw_gen_construct_one_arg" );
  }
  public MyGenericGosu raw_gen_construct_two_args()
  {
    return new MyGenericGosu<>( new StringBuilder(), "raw_gen_construct_two_args" );
  }
  
  public MyGenericGosu<String> param_gen_construct_no_args()
  {
    return new MyGenericGosu<String>();
  }
  public MyGenericGosu<String> param_gen_construct_one_arg()
  {
    return new MyGenericGosu<String>( "param_gen_construct_one_arg" );
  }
  public MyGenericGosu<StringBuilder> param_gen_construct_two_args()
  {
    return new MyGenericGosu<StringBuilder>( new StringBuilder(), "param_gen_construct_two_args" );
  }
  
  public void void_block_no_args()
  {
    new MyGenericGosu().void_block_no_args( () -> {} );
  }
  public void void_block_one_arg( String s )
  {
    new MyGenericGosu<>().void_block_one_arg( (e) -> {}, s );
  }
  public void void_block_two_args( String s, String r )
  {
    new MyGenericGosu<>().void_block_two_args( (e, f) -> {}, s, r );
  }
  
  public <R> R ret_block_no_args( R r )
  {
    return new MyGenericGosu<>().ret_block_no_args( () -> r );
  }
  public <R, E> R ret_block_one_arg( R r, E e )
  {
    return new MyGenericGosu<>().ret_block_one_arg( (ee) -> r, e );
  }
  public <R, E, F> R ret_block_two_args( R r, E e, F f )
  {
    return new MyGenericGosu<>().ret_block_two_args( (ee, ff) -> r, e, f );
  }
  
  public MyGosu method_no_args()
  {
    return new MyGosu().method_no_args();
  }
  public MyGosu method_one_arg()
  {
    return new MyGosu().method_one_arg( "method_one_arg" );
  }
  public MyGosu gen_method_no_args()
  {
    return new MyGosu().gen_method_no_args();
  }
  public MyGosu gen_method_one_arg()
  {
    return new MyGosu().gen_method_one_arg( "gen_method_one_arg" );
  }
  
  public MyGenericGosu<StringBuilder> useGenericSubclass()
  {
    StringBuilder sb = new StringBuilder();
    FromJavaSubclass<StringBuilder> fjs = new FromJavaSubclass<>( sb );
    fjs.setMyTee( new StringBuilder( "foo" ) );
    Map<StringBuilder, Integer> map = fjs.foo( fjs.getTee(), 5 );
    System.out.println( map.get( fjs.getTee() ) );
    return fjs;
  }
  
//  public String reified_gen_method_no_args()
//  {
//    return new MyGosu().<StringBuilder>reified_gen_method_no_args();
//  }
//  public String reified_gen_method_one_arg()
//  {
//    return new MyGosu().reified_gen_method_one_arg( "reified_gen_method_one_arg" );
//  }
}