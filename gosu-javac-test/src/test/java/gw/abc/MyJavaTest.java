package gw.abc;

import java.math.BigDecimal;
import org.junit.Test;


import static junit.framework.TestCase.assertEquals;

public class MyJavaTest
{
  @Test
  public void testMe() {
    MyGosuClass mo = new MyGosuClass( "Mo" );
    assertEquals( "Mo", mo.getName() );
    assertEquals( "osu", new MyGosuClass.MyInner().blah() );
    assertEquals( "HI,BYE,I,YE,", mo.foo() );
  }

  @Test
  public void testGosuExtendsJavaBase()
  {
    GosuExtendsJavaBase gosuExtendsJava = new GosuExtendsJavaBase("scott", new StringBuilder("myId"));
    assertEquals( "scott", gosuExtendsJava.getName() );
    assertEquals( "myId", gosuExtendsJava.getId() );
    assertEquals( "7", gosuExtendsJava.baseMethod(7) );
  }

  @Test
  public void testJavaExtendsGosu()
  {
    JavaExtendsGosu javaExtendsGosu = new JavaExtendsGosu( "scott" );
    assertEquals( "scott", javaExtendsGosu.getName() );
    assertEquals( "fromJava", javaExtendsGosu.getId() );
    assertEquals( "7", javaExtendsGosu.baseMethod(7) );
  }

  @Test
  public void testJavaExtendsGosu2()
  {
    JavaExtendsGosu2 javaExtendsGosu = new JavaExtendsGosu2( "scott" );
    assertEquals( "scott", javaExtendsGosu.getName() );
    assertEquals( "fromJava2", javaExtendsGosu.getId() );
    assertEquals( "8", javaExtendsGosu.baseMethod(8) );
  }

  @Test
  public void testPrograms()
  {
    RunMe.execute();
  }

  @Test
  public void testExtension()
  {
    assertEquals( new BigDecimal("1.234"), "1.234".bd() );
    assertEquals( new BigDecimal("1.234"), new UseManifoldExtensions().useStringExtension() );
  }

  @Test
  public void testGenerics()
  {
    MyGosuList<String> list = new MyGosuList<>( 5 );
    list.add( "a" );
    String result = list.myGenFun( "hi" );
  }
}
