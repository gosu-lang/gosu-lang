package gw.specContrib.refresh.classloader.discrete

uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.IType
uses gw.lang.reflect.ITypeRef
uses gw.internal.gosu.compiler.GosuClassLoader
uses gw.lang.parser.ParserOptions
uses gw.lang.parser.GosuParserFactory
uses gw.config.CommonServices
uses gw.lang.reflect.java.JavaTypes
uses org.junit.Assert#assertNotNull
uses org.junit.Assert#assertTrue
uses org.junit.Ignore
uses org.junit.Test

public class DiscreteLoadingTest {

  @Test
  function testNonDiscrete() : void {
    var idGarbage1Class = loadAndRunPerm()
    assertNotNull( idGarbage1Class )
    TypeSystem.refresh( true )
    System.gc()
    Thread.sleep( 1000 )

    // Should be SAME classes
    assertTrue( idGarbage1Class == loadAndRunPerm() )
  }

  @Test
  function testDiscrete() : void {
    var oldValue = gw.internal.gosu.parser.ExecutionEnvironment.instance().getDiscretePackages()
    gw.internal.gosu.parser.ExecutionEnvironment.instance().setDiscretePackages( {"gw.specContrib.refresh.classloader.discrete.temp"} )
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
      gw.internal.gosu.parser.ExecutionEnvironment.instance().setDiscretePackages( oldValue )
    }
  }

  @Test
  @Ignore
  function testProgram() {
    var fqn = "scott.McKinney"
    var idGarbage1Class = loadProgram( fqn, "'hello'" )
    assertNotNull( idGarbage1Class )
    TypeSystem.refresh( TypeSystem.getByFullName( fqn ) as ITypeRef )
    System.gc()
    Thread.sleep( 1000 )

    // Should be DIFFERENT classes
    assertTrue( idGarbage1Class != loadProgram( fqn, "'bye'" ) )
  }

  @Test
  @Ignore
  function testScratchpad() {
    var idGarbage1Class = loadProgram( Gosu.GOSU_SCRATCHPAD_FQN, "'hello'" )
    assertNotNull( idGarbage1Class )
    TypeSystem.refresh( TypeSystem.getByFullName( Gosu.GOSU_SCRATCHPAD_FQN ) as ITypeRef )
    System.gc()
    Thread.sleep( 1000 )

    // Should be DIFFERENT classes
    assertTrue( idGarbage1Class != loadProgram( Gosu.GOSU_SCRATCHPAD_FQN, "'bye'" ) )
  }

  private function loadProgram( fqn: String, script: String ) : int {
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

  private function loadAndRunTemp() : int {
    var type = TypeSystem.getByFullName( "gw.specContrib.refresh.classloader.discrete.temp.Garbage2" )
    var garbage2 = type.TypeInfo.getConstructor( {} ).Constructor.newInstance( {} )
    return System.identityHashCode( garbage2.Class.getMethod( "getGarbage", {} ).invoke( garbage2, {} ).Class )
  }

  private function loadAndRunPerm() : int {
    var type = TypeSystem.getByFullName( "gw.specContrib.refresh.classloader.discrete.perm.Garbage2" )
    var garbage2 = type.TypeInfo.getConstructor( {} ).Constructor.newInstance( {} )
    return System.identityHashCode( garbage2.Class.getMethod( "getGarbage", {} ).invoke( garbage2, {} ).Class )
  }

}
