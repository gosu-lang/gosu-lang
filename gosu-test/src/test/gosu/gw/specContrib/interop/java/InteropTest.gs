package gw.specContrib.interop.java
uses junit.framework.TestCase

class InteropTest extends gw.BaseVerifyErrantTest
{
  function testConstruct()
  {
    var fromJava = new FromJava()
    
    var myGosu = fromJava.construct_no_args()
    assertEquals( "no_args", myGosu.Name )
    
    myGosu = fromJava.construct_one_arg()
    assertEquals( "one_arg", myGosu.Name )
    
    var myGenGosu = fromJava.raw_gen_construct_no_args()
    assertEquals( "no_args", myGenGosu.Name )
    
    myGenGosu = fromJava.raw_gen_construct_one_arg()
    assertEquals( "raw_gen_construct_one_arg", myGenGosu.Name )
    
    myGenGosu = fromJava.raw_gen_construct_two_args()
    assertEquals( "raw_gen_construct_two_args", myGenGosu.Name )
    
    var myParamGenGosu1 = fromJava.param_gen_construct_no_args()
    assertEquals( MyGenericGosu<String>, statictypeof myParamGenGosu1 )
    assertEquals( "no_args", myParamGenGosu1.Name )
    
    var myParamGenGosu2 = fromJava.param_gen_construct_one_arg()
    assertEquals( MyGenericGosu<String>, statictypeof myParamGenGosu2 )
    assertEquals( "param_gen_construct_one_arg", myParamGenGosu2.Name )
    
    var myParamGenGosu3 = fromJava.param_gen_construct_two_args()
    assertEquals( MyGenericGosu<StringBuilder>, statictypeof myParamGenGosu3 )
    assertEquals( "param_gen_construct_two_args", myParamGenGosu3.Name )
    
  }
  
  function testMethodCall()
  {
    var fromJava = new FromJava()
    
    var myGosu = fromJava.method_no_args()
    assertEquals( "method_no_args", myGosu.Name )
    
    myGosu = fromJava.method_one_arg()
    assertEquals( "method_one_arg", myGosu.Name )    
    
    myGosu = fromJava.gen_method_no_args()
    assertEquals( "gen_method_no_args", myGosu.Name )
    
    myGosu = fromJava.gen_method_one_arg()
    assertEquals( "gen_method_one_arg", myGosu.Name )
    
    fromJava.void_block_no_args()
    fromJava.void_block_one_arg( "hi" )
    fromJava.void_block_two_args( "hi", "hey" )
    assertEquals( 5, fromJava.ret_block_no_args( 5 ) )
    assertEquals( 6, fromJava.ret_block_one_arg( 6, "hi" ) )
    assertEquals( 7, fromJava.ret_block_two_args( 7, "hi", "hey" ) )
    
    var javaSubclass = fromJava.useGenericSubclass();
    assertEquals( MyGenericGosu<StringBuilder>, statictypeof javaSubclass )
    assertEquals( new StringBuilder( "foo" ).toString(), javaSubclass.Tee.toString() )
    var value = javaSubclass.ret_block_one_arg( \ e -> e as String, 9 )
    assertEquals( "9", value )
    
    assertEquals( 0, new FromJavaSubclass( "hi" ).call_reified().length() )
    
//    var res = fromJava.reified_gen_method_no_args()
//    assertEquals( Object.Type.Name, res )
//    
//    res = fromJava.reified_gen_method_one_arg()
//    assertEquals( CharSequence.Type.Name, res )
  }
}