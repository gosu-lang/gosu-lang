package gw.internal.gosu.parser.annotation

uses gw.BaseVerifyErrantTest

class SuppressWarningsTest extends BaseVerifyErrantTest {

  function testErrant_WithoutSuppressWarnings() {
    processErrantType( Errant_WithoutSuppressWarnings )
  }


  function testErrant_ClassLevelSuppressWarnings_garbage() {
    processErrantType( Errant_ClassLevelSuppressWarnings_garbage )
  }

  function testErrant_ClassLevelSuppressWarnings_all() {
    processErrantType( Errant_ClassLevelSuppressWarnings_all )
  }

  function testErrant_ClassLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_ClassLevelSuppressWarnings_deprecation )
  }


  function testErrant_FunctionLevelSuppressWarnings_garbage() {
    processErrantType( Errant_FunctionLevelSuppressWarnings_garbage )
  }

  function testErrant_FunctionLevelSuppressWarnings_all() {
    processErrantType( Errant_FunctionLevelSuppressWarnings_all )
  }

  function testErrant_FunctionLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_FunctionLevelSuppressWarnings_deprecation )
  }


  function testErrant_PropertyLevelSuppressWarnings_garbage() {
    processErrantType( Errant_PropertyLevelSuppressWarnings_garbage )
  }

  function testErrant_PropertyLevelSuppressWarnings_all() {
    processErrantType( Errant_PropertyLevelSuppressWarnings_all )
  }

  function testErrant_PropertyLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_PropertyLevelSuppressWarnings_deprecation )
  }


  function testErrant_ConstructorLevelSuppressWarnings_garbage() {
    processErrantType( Errant_ConstructorLevelSuppressWarnings_garbage )
  }

  function testErrant_ConstructorLevelSuppressWarnings_all() {
    processErrantType( Errant_ConstructorLevelSuppressWarnings_all )
  }

  function testErrant_ConstructorLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_ConstructorLevelSuppressWarnings_deprecation )
  }


  function testErrant_FieldLevelSuppressWarnings_garbage() {
    processErrantType( Errant_FieldLevelSuppressWarnings_garbage )
  }

  function testErrant_FieldLevelSuppressWarnings_all() {
    processErrantType( Errant_FieldLevelSuppressWarnings_all )
  }

  function testErrant_FieldLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_FieldLevelSuppressWarnings_deprecation )
  }


  function testErrant_ParameterLevelSuppressWarnings_garbage() {
    processErrantType( Errant_ParameterLevelSuppressWarnings_garbage )
  }

  function testErrant_ParameterLevelSuppressWarnings_all() {
    processErrantType( Errant_ParameterLevelSuppressWarnings_all )
  }

  function testErrant_ParameterLevelSuppressWarnings_deprecation() {
    processErrantType( Errant_ParameterLevelSuppressWarnings_deprecation )
  }

}