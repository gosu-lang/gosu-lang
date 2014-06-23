package gw.util
uses gw.test.TestClass
uses java.io.File

class ShellTest extends TestClass {
  
  function testBuildProcessDoesntGrabStdOut() {
    var out = File.createTempFile( "Foo", ".txt" )
    var cmd = (Shell.isWindows() ? "cmd /c dir" : "ls" ) + " >> \"" + out.CanonicalPath + "\""
    print( "Command to execute is ${cmd}" )
    var ps = Shell.buildProcess( cmd )
    print( "This should not be in the file" )
    assertFalse( out.read().contains( "This should not be in the file" ) )
    var foo = ps.start()
    assertFalse( out.read().contains( "This should not be in the file" ) )
    foo.waitFor()
    assertFalse( out.read().contains( "This should not be in the file" ) )
  }
  
}
