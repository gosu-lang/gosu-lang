package gw.specification.statements.returnsExitsAndExceptions

uses java.lang.Exception

class Errant_UnreachabilityTest {
  function m0() : int {
    return 0
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m1() : int {
    throw "whatever"
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m2() : int {
    while(true) {
      var i = 8
      return 0
    }
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m3() : int {
    var s = 3
    while(s < 3) {
      var i = 8
      return 0
    }
    print("OK")
  }  //## issuekeys: MSG_MISSING_RETURN

  function m4() : int {
    var s = 3
    while(s < 3) {
      var i = 8
      return 0
    }
    print("OK")
    return 0
  }

  function m5() : int {
    var s = 3
    while(s < 3) {
      break
      print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
    }
    return 0
  }

  function m6() : int {
    var s = 3
    while(s < 3) {
      continue
      print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
    }
    return 0
  }

  function m7() : int {
    var s = 3
    do {
      var i = 8
      return 0
    } while(s < 3)
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m8() : int {
    var s = 3
    if (s < 3) {
      return 0
    }
    print("OK")
    return 0
  }

  function m9() : int {
    var s = 3
    if (s < 3) {
      return 0
    } else {
      return 0
    }
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m10() : int {
    var s = 3
    if (s < 3) {
      return 0
    } else {
      var b = 0
    }
    print("OK")
    return 0
  }

  function m11() : int {
    var s = 3
    if (s < 3) {
      return 0
    } else if (s == 1) {
      var b = 0
    }
    print("OK")
    return 0
  }

  function m12() : int {
    var x = 3
    switch(x)
    {
      case 1:
        break
      default:
        return 0
        print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
    }
    return 0
  }

  function m13() : int {
    var x = 3
    switch(x)
    {
      case 1:
        return 0
      default:
    }
    print("OK")
    return 0
  }

  function m14() : int {
    var x = 3
    switch(x)
    {
      case 1:
        return 0
        print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
    }
    return 0
  }

  function m15() : int {
    var x = 3
    switch(x)
    {
      case 1:
        return 0
      default:
        return 0
    }
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m16() : int {
    try {
      return 0
    } finally {
    }
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m17() : int {
    try {
      return 0
    } catch( e : Exception ) {
      return 0
    }
    print("unreachable")  //## issuekeys: MSG_UNREACHABLE_STMT
  }

  function m18() : int {
    try {
      return 0
    } catch( e : Exception ) {
      var x = 0
    }
    print("OK")
    return 0
  }

  function m19() : int {
    try {
      var x = 0
    } catch( e : Exception ) {
      return 0
    }
    print("OK")
    return 0
  }

  function m20() : int {
    try {
      var x = 0
    } catch( e : Exception ) {
      var x = 0
    }
    print("OK")
    return 0
  }
}
