package gw.specification.expressions.objectCreationExpressions

uses java.util.ArrayList
uses java.util.LinkedList

class Errant_ObjectCreationExpressionsTest {

  class inner {}

  function testNoDotNew() {
    var o : Errant_ObjectCreationExpressionsTest
    var c : inner = o.new inner()  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_SUCH_FUNCTION
  }

  function testInstanceCreation() {
    var i0 : List = new ArrayList()
    i0.add("x")
    var i1 : List = new ArrayList(i0)
    var i2 : List = new LinkedList()
    var i3 : List = new LinkedList(i0)
    var i4 = new block()  //## issuekeys: MSG_EXPECTING_NEW_ARRAY_OR_CTOR, MSG_BLOCKS_LITERAL_NOT_ALLOWED_IN_NEW_EXPR
    var i5 = new gw()  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME, MSG_EXPECTING_TYPE_TO_FOLLOW_PACKAGE_NAME
  }
}