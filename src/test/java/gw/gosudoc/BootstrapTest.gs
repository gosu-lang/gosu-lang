package gw.gosudoc

uses com.example.bootstrap.Example
uses org.junit.Assert
uses org.junit.Test

class BootstrapTest extends BaseGosuDocTest {

  @Test
  function publicClassIsDocumented() {
    var html = gosuDocForType( Example )
    Assert.assertNotNull( "Should find gosu doc for " + Example, html )
  }

  @Test
  function internalClassIsNotDocumented() {
    var html = gosuDocForString( "com.example.bootstrap.PrivateClass" )
    Assert.assertNull( "Should not find gosu doc for PrivateClass", html )
  }

}