package gw.abc

uses manifold.api.json.DataBindings;
uses org.junit.Assert#*

class GosuStructuralTest
{
  function testJavaStructural()
  {
    var foo = new JavaStructuralInterfaceImpl() as IMyStructuralInterface;
    foo.setName( "scott" );
    assertEquals( "scott", foo.getName() );
    foo.setAge( 100 );
    assertEquals( 100, foo.getAge() );
    assertEquals( "hi", foo.foo( "hi" ) );

    var db = new DataBindings();
    db.put( "Name", "bob" );
    db.put( "Age", 52 );
    db.put( "foo", \arg: String -> arg );
    var mdb = db as IMyStructuralInterface;
    assertEquals( "bob", mdb.getName() );
    assertEquals( 52, mdb.getAge() );
    assertEquals( "hi", mdb.foo( "hi" ) );
  }
}