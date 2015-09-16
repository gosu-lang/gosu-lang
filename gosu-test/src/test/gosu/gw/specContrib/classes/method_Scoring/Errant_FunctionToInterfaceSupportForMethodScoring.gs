package gw.specContrib.classes.method_Scoring

class Errant_FunctionToInterfaceSupportForMethodScoring {
  function test() {
    var x = a( \-> { return new Object() } )    //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    var y = a( \-> print("hi") )
    print( statictypeof y == I1 )

    var z = a( \-> "" )
    print( statictypeof z == I2 )
  }

  function a(s: I1) : I1 { return null }

  function a(s: I2) : I2 { return null }

  interface I1 {
    function m()
  }

  interface I2 {
    function get(): String
  }
}