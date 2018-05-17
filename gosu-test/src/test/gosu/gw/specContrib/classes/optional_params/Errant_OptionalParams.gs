package gw.specContrib.classes.optional_params

uses gw.util.Pair
uses gw.lang.reflect.IType

class Errant_OptionalParams {
  function testJavaVarArgAsOptional() {
    String.join( "hi", {"asfd"} )
    String.join( "hi" )
    String.join()  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_FOR_METHOD_ON_CLASS, MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION

    Arrays.asList()
    Arrays.asList( {9} )
  }

  function testVarArgsWithOverloadedMethods() {
    var l1: ImmutableList<Pair<IType, Object>> = ImmutableList.of()
    var l2: ImmutableList<Pair<IType, Object>> = ImmutableList.of(Pair.make(String, ""))
    var l3: ImmutableList<Pair<IType, Object>> = ImmutableList.of(Pair.make(String, ""), {})
    var l3alt: ImmutableList<Pair<IType, Object>> = ImmutableList.of(Pair.make(String, ""))

    var l4: String = OverloadedVarArgs.overloaded( "abc", null )
    var l5: String = OverloadedVarArgs.overloaded( "abc", {} )
    var l5alt: String = OverloadedVarArgs.overloaded( "abc" )
    var l6 = OverloadedVarArgs.overloaded( null, {} )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var l6alt = OverloadedVarArgs.overloaded( null )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var l7 = OverloadedVarArgs.overloaded( new Object(), {} )
    var l7alt = OverloadedVarArgs.overloaded( new Object() )
    var l8: String = l7  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var l8alt: String = l7alt  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    var m4: String = OverloadedVarArgs.overloaded2( "abc", null ) //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m5: String = OverloadedVarArgs.overloaded2( "abc", {} )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m5alt: String = OverloadedVarArgs.overloaded2( "abc" )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m6 = OverloadedVarArgs.overloaded2( null, {} )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m6alt = OverloadedVarArgs.overloaded2( null )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m7 = OverloadedVarArgs.overloaded2( new Object(), {} )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
    var m7alt = OverloadedVarArgs.overloaded2( new Object() )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION
  }
}
