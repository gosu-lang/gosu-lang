package gw.specification.variablesParametersFieldsScope

uses java.util.zip.ZipFile

class Errant_ScopeTest {

  function foreachScope() {
    var i = 0
    for(var i in 0..i+1) {  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      i++
    }
    for(var j in 0..10 index k) {
      j = k+1
    }
    j = k+1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME

    for(var j in 0..j+1) {  //## issuekeys: MSG_EXPECTING_ARRAYTYPE_FOREACH, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME
      j++  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
    }

  }

  function usingScope() {
    var a = 1
    using (var a = new ZipFile("31")) {  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    }

    using (var zf = new ZipFile("31")) {
      var b = zf.Name
    }
    finally {
      var b = zf.Name
    }
    var c = zf.Name  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }

  function stringTemplateScope() {
    var c = 1
    var str = "${c + 1}"
    str = "${foo + 1}"  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME
  }

  function switchScope() : void {
    var tmp = 0
    switch (tmp) {
      case 0:
          var tmp2 = 0
          switch (tmp2) {
            case 0:
                break
            case 1:
                break
          }
          break
      case 1:
          tmp2 = 1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
          switch (tmp2) {  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
            case 0:
                break
            case 1:
                break
          }
          break
    }
  }

  function switchScope_PL33314() : void {
    var item2 = Math.round(Math.floor(Math.random()*10))
    var invalidVar2 =""
    switch (item2){
      case 9 :
          break
        default :
        invalidVar2 = "this should compile and cause no error"
        var invalidVar = "this should not compile and causes VerifyError as a result"
    }
    print(invalidVar2)
    print(invalidVar)  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }

  function tryCatchScope() {
    try {
      var f = 1/0
    } catch(e : ArithmeticException) { }
    f = 1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME

    try {
      var f = 1/0
    } catch(e : ArithmeticException) {
      var e1 = e
      var g = f  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_BAD_IDENTIFIER_NAME
    }
    var e1 = e  //## issuekeys: MSG_BAD_IDENTIFIER_NAME

    try {
      var f = 1/0
      try {
        var f = 1/0  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      } catch( e : Throwable) {}

    } catch(e : ArithmeticException) { }
  }

  var h = 1 + x  //## issuekeys: MSG_ILLEGAL_FORWARD_REFERENCE
  function m1(x : int): void {  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    x = 7
  }

  function m2(v2 : int): void {
    x = 7
  }

  function m3(v3 : int): void {
    x = 7
    var x : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  }

  function m4(v4 : int): void {
    x = 7
    {
      var x : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      x = 7
    }
  }

  var x : int

  var g = 1 + x



  function m5(y_0 : int) : void {
    y_0 = 8
  }
  function m6(v2 : int) : void {
    y = 8
  }
  function m7(v3 : int) : void {
    y = 8
    var y_0 : int
    y_0 = 8
  }
  function m8(v4 : int) : void {
    y = 8
    {
      var y_0 : int
      y_0 = 8
    }
  }
  var y : int
}

