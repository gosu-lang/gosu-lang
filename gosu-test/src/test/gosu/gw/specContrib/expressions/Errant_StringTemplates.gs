package gw.specContrib.expressions\n

class Errant_StringTemplates {
  var foo : int

  function testErrors() {
    var uu = "<%  var s = "Some Text"\n var u = s %>${u}"
    var vv = "<% { var s = "Some Text"\n var u = s } %>${u}"      //## issuekeys: CANNOT RESOLVE SYMBOL 'U'
    var ww = "<%=  var s = "Some Text"\n var u = s %>"      //## issuekeys: UNEXPECTED TOKEN: VAR
    var xx = "<%= {var s = "Some Text"\n var u = s}%>"      //## issuekeys: RBRACE EXPECTED
    var yy = "<%   var s = "Some Text"\n var u = s %>${s}"
    var zz = "<%  {var s = "Some Text"\n var u = s}%>${s}"      //## issuekeys: CANNOT RESOLVE SYMBOL 'S'

    var a = "<%= "x z" %>"
    var b = "<%= Math.max(foo * 3, 12) %>"
    var c = "<%= a * b %>"      //## issuekeys: OPERATOR '*' CANNOT BE APPLIED TO 'JAVA.LANG.STRING', 'JAVA.LANG.STRING'
    var d = "<%= "x z" >"      //## issuekeys: ILLEGAL LINE END IN STRING LITERAL
    var e = "<%= Math.max(foo * 3, 12) =%>"      //## issuekeys: GST_EXPRESSION_END EXPECTED
    var f = "<%= a + b %>"
    var g = "<% a + b %>"      //## issuekeys: NOT A STATEMENT
    var h = "<% print(a + b) %>"      // wow...can you actually call print() from inside a template??!
    var i = "<% class Foo {}\n var x = new Foo() %>"
    var j = "<% var a = "x z" %>"      //## issuekeys: VARIABLE 'A' IS ALREADY DEFINED IN THE SCOPE
    var k = "<% b = 10 %>"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'INT', REQUIRED: 'JAVA.LANG.STRING'
    var l = "<% c = "10" %>" // and you can change a variable from inside a template.  Wow.
  }
}
