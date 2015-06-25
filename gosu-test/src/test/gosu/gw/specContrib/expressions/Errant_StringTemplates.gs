package gw.specContrib.expressions

class Errant_StringTemplates {
  var foo : int

  function testErrors() {
    var uu = "<%  var s = "Some Text" ; var u = s %>${u}" //## issuekeys:  MSG_STATEMENT_ON_SAME_LINE
    var vv = "<% { var s = "Some Text" ; var u = s } %>${u}" //## issuekeys: CANNOT RESOLVE SYMBOL 'U'
    var ww = "<%=  var s = "Some Text" ; var u = s %>"      //## issuekeys: UNEXPECTED TOKEN: VAR
    var xx = "<%= {var s = "Some Text" ; var u = s}%>"      //## issuekeys: RBRACE EXPECTED
    var yy = "<%   var s = "Some Text" ; var u = s %>${s}"  //## issuekeys: MSG_STATEMENT_ON_SAME_LINE
    var zz = "<%  {var s = "Some Text" ; var u = s}%>${s}"      //## issuekeys: CANNOT RESOLVE SYMBOL 'S'

    var a = "<%= "x z" %>"
    var b = "<%= Math.max(foo * 3, 12) %>"
    var c = "<%= a * b %>"      //## issuekeys: OPERATOR '*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
    var d = "<%= "x z" >"      //## issuekeys: ILLEGAL LINE END IN STRING LITERAL
    var e = "<%= Math.max(foo * 3, 12) =%>"      //## issuekeys: GST_EXPRESSION_END EXPECTED
    var f = "<%= a + b %>"
    var g = "<% a + b %>"      //## issuekeys: NOT A STATEMENT
    var h = "<% print(a + b) %>"      // wow...can you actually call print() from inside a template??!
    var i = "<% var a = "x z" %>"      //## issuekeys: VARIABLE 'A' IS ALREADY DEFINED IN THE SCOPE
    var j = "<% b = 10 %>"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
    var k = "<% c = "10" %>" // and you can change a variable from inside a template.  Wow.

    var m = "<% extends Object %>"      //## issuekeys: GST_SCRIPTLET_END EXPECTED
    var n = "<%= extends Object %>"      //## issuekeys: UNEXPECTED TOKEN: EXTENDS
    var o = "<% params(x : String, y : boolean) %>"      //## issuekeys: CANNOT RESOLVE METHOD 'PARAMS(?)'
    var p = "<%= params(x : String, y : boolean) %>"      //## issuekeys: CANNOT RESOLVE METHOD 'PARAMS(?)'

    var q = "<% class Foo { } ; var x = new Foo() %>" //## issuekeys: TEMPLATE CANNOT DECLARE CLASS, INTERFACE, STRUCTURE OR ENHANCEMENT
    var r = "<% interface Foo { } %>" //## issuekeys: TEMPLATE CANNOT DECLARE CLASS, INTERFACE, STRUCTURE OR ENHANCEMENT
    var s = "<% structure Foo { } %>" //## issuekeys: TEMPLATE CANNOT DECLARE CLASS, INTERFACE, STRUCTURE OR ENHANCEMENT
    var t = "<% enhancement Foo : Integer { } %>" //## issuekeys: CANNOT RESOLVE SYMBOL 'ENHANCEMENT'

    var w = "<%= class Foo { } ; var x = new Foo() %>" //## issuekeys: UNEXPECTED TOKEN: CLASS
    var x = "<%= interface Foo { } %>" //## issuekeys: UNEXPECTED TOKEN: INTERFACE
    var y = "<%= structure Foo { } %>" //## issuekeys: UNEXPECTED TOKEN: STRUCTURE
    var z = "<%= enhancement Foo : Integer { } %>" //## issuekeys: CANNOT RESOLVE SYMBOL 'ENHANCEMENT'
  }
}
