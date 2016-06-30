package gw.specContrib.classes.property_Declarations

@Arguments(:methodName = "wQuote")  //## issuekeys: MSG_MISSING_REQUIRED_ARGUMENTS
class Errant_MissingRequiredParams {
  function asdf() {
    foo( :s2 = "" )  //## issuekeys: MSG_MISSING_REQUIRED_ARGUMENTS
    foo( :s1 = "" )  //## issuekeys: MSG_WRONG_NUMBER_OF_ARGS_TO_FUNCTION
  }

  function foo( s1: String, s2: String ) {}
}
