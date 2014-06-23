package gw.internal.gosu.compiler

uses gw.test.TestClass

class ManagedProgramInstanceTest extends TestClass {

  function testManagedProgramCycle()
  {
    var instance = MyManagedProgram.Type.ProgramInstance as MyProgramBase
    instance.evaluate( null )
    assertEquals( "before, program, after, foo your bar", instance.results() )
  }
}
