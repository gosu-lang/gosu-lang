package gw.gosudoc

uses com.example.bootstrap.Example
uses gw.gosudoc.util.BaseGosuDocTest
uses org.junit.Assert
uses org.junit.Test

class BootstrapTest extends BaseGosuDocTest{

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

  @Test
  function javaStandardClassesAreNotDocumented() {
    var html = gosuDocForType( String )
    Assert.assertNull( "Should not find gosu doc for String", html )
  }

  @Test
  function gosuStandardClassesAreNotDocumented() {
    var html = gosuDocForType( gw.lang.Gosu )
    Assert.assertNull( "Should not find gosu doc for Gosu class", html )
  }

}