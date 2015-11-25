package gw.specContrib.refresh.classloader.discrete

uses gw.BaseVerifyErrantTest
uses junit.framework.TestCase
uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.IType
uses gw.lang.reflect.ITypeRef
uses gw.internal.gosu.compiler.GosuClassLoader
uses gw.lang.parser.ParserOptions
uses gw.lang.parser.GosuParserFactory
uses gw.config.CommonServices
uses gw.lang.reflect.java.JavaTypes

public class DiscreteLoadingTest extends BaseVerifyErrantTest {
  function testNonDiscrete() : void {
    var idGarbage1Class = loadAndRunPerm()
    assertNotNull( idGarbage1Class )
    TypeSystem.refresh( true )
    System.gc()
    Thread.sleep( 1000 )

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
      Thread.sleep( 1000 )

      // Should be DIFFERENT classes
      assertTrue( idGarbage1Class != loadAndRunTemp() )
    }
    finally {
      System.setProperty( "unloadable.packages", oldValue )
      GosuClassLoader.DISCRETE_NAMESPACES.clear();
    }
  }

  function testProgram() {
    var fqn = "scott.McKinney"
    var idGarbage1Class = loadProgram( fqn, "'hello'" )
    assertNotNull( idGarbage1Class )
    System.gc()
    Thread.sleep( 1000 )

    // Should be DIFFERENT classes
    assertTrue( idGarbage1Class != loadProgram( fqn, "'bye'" ) )
  }

  function testScratchpad() {
    var idGarbage1Class = loadProgram( Gosu.GOSU_SCRATCHPAD_FQN, "'hello'" )
    assertNotNull( idGarbage1Class )
    System.gc()
    Thread.sleep( 1000 )

    // Should be DIFFERENT classes
    assertTrue( idGarbage1Class != loadProgram( Gosu.GOSU_SCRATCHPAD_FQN, "'bye'" ) )
  }

  function loadProgram( fqn: String, script: String ) : int {
    var scriptParser = GosuParserFactory.createParser( script )

    var programParser = GosuParserFactory.createProgramParser()
    var options = new ParserOptions()
        .withParser( scriptParser )
        .asThrowawayProgram()
        .withFileContext( new ProgramFileContext( null, fqn ) )
    var parseResult = programParser.parseExpressionOrProgram( script, scriptParser.getSymbolTable(), options )
    var program = parseResult.getProgram()
    var result = program.evaluate( null )
    if( result != null ) {
      print( "Return Value: " + CommonServices.getCoercionManager().convertValue( result, JavaTypes.STRING() ) )
    }
    //assertSame( program.ProgramInstance.Class, program.BackingClass )
    print( program.BackingClass.Name )
    return System.identityHashCode( program.ProgramInstance.Class )
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
