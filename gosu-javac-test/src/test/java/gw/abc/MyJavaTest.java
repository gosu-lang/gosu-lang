package gw.abc;

import java.math.BigDecimal;
import java.util.function.Function;
import manifold.api.json.DataBindings;
import org.junit.Test;
import gw.sample.QueryTest;

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
  public void testGosuInSrcDir() {
    SrcGosuClass mo = new SrcGosuClass();
    mo.setFoo( "hi" );
    assertEquals( "hi", mo.getFoo() );
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

  @Test
  public void testGraphQL()
  {
    QueryTest qt = new QueryTest();
    qt.testMoviesQuery();
  }

  @Test
  public void testJavaStructural()
  {
    IMyStructuralInterface foo = (IMyStructuralInterface)new JavaStructuralInterfaceImpl();
    foo.setName( "scott" );
    assertEquals( "scott", foo.getName() );
    foo.setAge( 100 );
    assertEquals( 100, foo.getAge() );
    assertEquals( "hi", foo.foo( "hi" ) );

    DataBindings db = new DataBindings();
    db.put( "Name", "bob" );
    db.put( "Age", 52 );
    db.put( "foo", (Function<String, String>) arg -> arg );
    IMyStructuralInterface mdb = (IMyStructuralInterface)db;
    assertEquals( "bob", mdb.getName() );
    assertEquals( 52, mdb.getAge() );
    assertEquals( "hi", mdb.foo( "hi" ) );
  }

  @Test
  public void testGosuStructural()
  {
    IMyStructuralInterface foo = (IMyStructuralInterface)new GosuStructuralInterfaceImpl();
    foo.setName( "scott" );
    assertEquals( "scott", foo.getName() );
    foo.setAge( 100 );
    assertEquals( 100, foo.getAge() );
    assertEquals( "hi", foo.foo( "hi" ) );

    GosuStructuralTest gst = new GosuStructuralTest();
    gst.testJavaStructural();
  }
}
