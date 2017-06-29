package gw.specContrib.statements.usesStatement

uses java.lang.String#valueOf( int )
uses java.lang.String#charAt( int )  //## issuekeys: MSG_CANNOT_REFERENCE_NON_STATIC_FEATURE_HERE
uses gw.specContrib.statements.usesStatement.Constants#*
uses gw.specContrib.statements.usesStatement.SomeProps#*  //## issuekeys: MSG_ONLY_GOSU_JAVA_TYPES
uses gw.specContrib.statements.usesStatement.Constants#StaticFuncWithArrayArgument(String[])
uses gw.specContrib.statements.usesStatement.Constants#StaticFuncWithArrayArgument(String) //## issuekeys: MSG_NO_SUCH_FUNCTION

class Errant_BadStaticImports {
}
