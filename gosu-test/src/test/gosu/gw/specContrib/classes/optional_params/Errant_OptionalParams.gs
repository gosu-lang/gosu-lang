package gw.specContrib.classes.optional_params

class Errant_OptionalParams {
  function testJavaVarArgAsOptional() {
    String.join( "hi", {"asfd"} )
    String.join( "hi" )
    String.join()  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_FOR_METHOD_ON_CLASS, MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION

    Arrays.asList()
    Arrays.asList( {9} )
  }
}