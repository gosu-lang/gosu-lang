package gw.internal.gosu.parser.structural

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_StructureAssignableToStructureTest {

  structure IStructure {
    function callMe( a: String ) : int
  }

  structure IGenericStructure<A> {
    function callMe( a: A ) : int
  }
  structure IGenericStructure2<B> {
    function callMe( a: B ) : int
  }

  structure IGenericMethodStructure<T> {
    function callMe<M1 extends T>( a: M1 ) : int
  }
  structure IGenericMethodStructure2<T> {
    function callMe<M2 extends T>( a: M2 ) : int
  }

  structure IGenericMethodWithGenericReturnStructure<T> {
    function callMe<M1 extends T>( a: M1 ) : M1
  }
  structure IGenericMethodWithGenericReturnStructure2<T> {
    function callMe<M2 extends T>( a: M2 ) : M2
  }


  function assignabilityNormalAndRawGeneric() {
    var normStruct : IStructure
    var genStruct : IGenericStructure

    // Ok, params are contravariant
    normStruct = genStruct
    // Error, params are covariant
     genStruct = normStruct  //## issuekeys: MSG_TYPE_MISMATCH
  }

//  structure Foo {
//     property get CellPhone() : String
//  }
//  function assignabilityNormalAndParameterized() {
//
//    var p : Person
//    var q : Foo = p
//  }

  function assignabilityNormalAndParameterized() {
    var normStruct : IStructure
    var genStruct : IGenericStructure<String>

    // Ok, callMe() params are equal wrt String type parameter
    normStruct = genStruct
    // Ok, callMe() params are equal wrt String type parameter
    genStruct = normStruct
  }

  function assignabilityRawGenericAndRawGeneric() {
    var genStruct : IGenericStructure
    var genStruct2 : IGenericStructure2

    // Ok, params are equal
    genStruct = genStruct2
    // Ok, params are equal
    genStruct2 = genStruct
  }

  function assignabilityParameterizedAndParameterized() {
    var genStruct : IGenericStructure<Object>
    var genStruct2 : IGenericStructure<String>

    // Ok, as interfaces they are assignable via covariant type vars, no structural check necessary in parser
    genStruct = genStruct2
    // Ok, genStruct2 is structurally assignable to genStruct (callMe() params are contravariant)
    genStruct2 = genStruct
  }

  function assignabilityRawGenericMethod() {
    var genStruct : IGenericMethodStructure
    var genStruct2 : IGenericMethodStructure2

    // Ok, params are equal
    genStruct = genStruct2
    // Ok, params are equal
    genStruct2 = genStruct
  }

  function assignabilityRawGenericMethodAndParameterized() {
    var genStruct : IGenericMethodStructure
    var genStruct2 : IGenericMethodStructure2<String>

    // Error, params are covariant
    genStruct = genStruct2  //## issuekeys: MSG_TYPE_MISMATCH
    // Ok, params are contravariant
    genStruct2 = genStruct
  }


  function assignabilityRawGenericMethodWithGenericReturn() {
    var genStruct : IGenericMethodWithGenericReturnStructure
    var genStruct2 : IGenericMethodWithGenericReturnStructure2

    // Error, params are
    genStruct = genStruct2
    // Ok, params are equal
    genStruct2 = genStruct
  }

  function assignabilityRawGenericMethodAndParameterizedReturn() {
    var genStruct : IGenericMethodWithGenericReturnStructure
    var genStruct2 : IGenericMethodWithGenericReturnStructure2<String>

    // Error, params are covariant
    genStruct = genStruct2  //## issuekeys: MSG_TYPE_MISMATCH
    // Error, return type is contravariant
    genStruct2 = genStruct  //## issuekeys: MSG_TYPE_MISMATCH
  }


}