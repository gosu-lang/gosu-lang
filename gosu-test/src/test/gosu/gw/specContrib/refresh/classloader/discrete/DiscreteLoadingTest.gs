package gw.specContrib.refresh.classloader.discrete

uses gw.BaseVerifyErrantTest
uses junit.framework.TestCase
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.IType
uses gw.internal.gosu.compiler.GosuClassLoader

public class DiscreteLoadingTest extends BaseVerifyErrantTest {
  function testNonDiscrete() : void {
    var idGarbage1Class = loadAndRunPerm()
    assertNotNull( idGarbage1Class )
    TypeSystem.refresh( true )
    System.gc()
    Thread.sleep( 5000 )

    // Should be SAME classes
    assertTrue( idGarbage1Class == loadAndRunPerm() )
  }

  function testDiscrete() : void {
    var oldValue = System.getProperty( "unloadable.packages", "" )
    System.setProperty( "unloadable.packages", "gw.specContrib.refresh.classloader.discrete.temp" )
    GosuClassLoader.DISCRETE_NAMESPACES.clear();
    try {
      var idGarbage1Class = loadAndRunTemp()
      assertNotNull( idGarbage1Class )
      TypeSystem.refresh( true )
      System.gc()
      Thread.sleep( 5000 )

      // Should be DIFFERENT classes
      assertTrue( idGarbage1Class != loadAndRunTemp() )
    }
    finally {
      System.setProperty( "unloadable.packages", oldValue )
      GosuClassLoader.DISCRETE_NAMESPACES.clear();
    }
  }

  function loadAndRunTemp() : int {
    var type = TypeSystem.getByFullName( "gw.specContrib.refresh.classloader.discrete.temp.Garbage2" )
    var garbage2 = type.TypeInfo.getConstructor( {} ).Constructor.newInstance( {} )
    return System.identityHashCode( garbage2.Class.getMethod( "getGarbage", {} ).invoke( garbage2, {} ).Class )
  }

  function loadAndRunPerm() : int {
    var type = TypeSystem.getByFullName( "gw.specContrib.refresh.classloader.discrete.perm.Garbage2" )
    var garbage2 = type.TypeInfo.getConstructor( {} ).Constructor.newInstance( {} )
    return System.identityHashCode( garbage2.Class.getMethod( "getGarbage", {} ).invoke( garbage2, {} ).Class )
  }

}
