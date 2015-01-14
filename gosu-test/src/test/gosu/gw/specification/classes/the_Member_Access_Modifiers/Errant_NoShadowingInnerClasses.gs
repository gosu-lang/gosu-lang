package gw.specification.classes.the_Member_Access_Modifiers

uses java.lang.Runnable
uses gw.lang.reflect.features.FeatureReference
uses gw.specification.classes.the_Member_Access_Modifiers.p0.internalC0

class Errant_NoShadowingInnerClasses implements Runnable {
  var  v0 : int
  static var  v1 : int
  function m() {}
  class c {}
  interface i {}
  structure s {}
  enum e {}
  delegate d represents Runnable = \  -> {  }

  class nested {
    var v0 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var m = 1
    var c = 1
    var i = 1
    var s = 1
    var e = 1
    var d = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    function m() {}
    class c {}
    enum e {}

    class nested2 {
      var v0 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var v1 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var m = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var c = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var i = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var s = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var e = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      var d = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      function m() {}
      class c {}
      enum e {}
    }

    function shadow() {
      var tmp : int
      var tmp2 : Type
      var tmp3 : FeatureReference

      tmp = v0
      tmp = v1
      tmp = m
      tmp = c
      tmp = i
      tmp = s
      tmp = e
      tmp = d

      tmp3 = Errant_NoShadowingInnerClasses#m()
      tmp2 = Errant_NoShadowingInnerClasses.c
      tmp2 = Errant_NoShadowingInnerClasses.e
      tmp2 = Errant_NoShadowingInnerClasses.i
      tmp2 = Errant_NoShadowingInnerClasses.s
      tmp3 = nested#m()
      tmp2 = nested.c
      tmp2 = nested.e

      tmp3 = nested2#m()
      tmp2 = nested2.c
      tmp2 = nested2.e
    }
  }

  static class staticNested{
    var v0 = 1
    var v1 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var m = 1
    var c = 1
    var i = 1
    var s = 1
    var e = 1
    var d = 1
    function m() {}
    class c {}
    enum e {}
    interface i {}
    structure s {}

    function shadow() {
      var tmp : int
      var tmp2 : Type
      var tmp3 : FeatureReference

      tmp = v0
      tmp = v1
      tmp = m
      tmp = c
      tmp = i
      tmp = s
      tmp = e
      tmp = d

      tmp3 = Errant_NoShadowingInnerClasses#m()
      tmp2 = Errant_NoShadowingInnerClasses.c
      tmp2 = Errant_NoShadowingInnerClasses.e
      tmp2 = Errant_NoShadowingInnerClasses.i
      tmp2 = Errant_NoShadowingInnerClasses.s
      tmp3 = nested#m()
      tmp2 = nested.c
      tmp2 = nested.e

      tmp3 = staticNested#m()
      tmp2 = staticNested.c
      tmp2 = staticNested.e
      tmp2 = staticNested.i
      tmp2 = staticNested.s
    }
  }

  static class subC0 extends internalC0 {
    static var b : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  }

  function shadowParam(v0 : int, v1 : int, m : int, c : int, i : int, s : int, e : int, d : int) { }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED, MSG_VARIABLE_ALREADY_DEFINED, MSG_VARIABLE_ALREADY_DEFINED

  function shadow() {
    var v0 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var v1 = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var m = 1
    var c = 1
    var i = 1
    var s = 1
    var e = 1
    var d = 1  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  }

  function shadow2() {
    var c: int
    var r = new Runnable() {
      var c = 2   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

      class inner {
        function innerFunc() {
          var c = 2   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
        }
      }
      function run() {  //## issuekeys: MSG_MISSING_OVERRIDE_MODIFIER
        var c = 2  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
      }
    }
  }
}