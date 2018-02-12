package gw.specContrib.structural

class Errant_MethodClash_StructureObject {

  class JJ{}
  class KK{}
  structure S{
    function hello()
  }

  function foo1(s : S) : JJ{
    return null
  }
  //IDE-1787 - Error expected as all structure types erase to Object at runtime
  function foo1(O : Object) : KK { return null}   //## NO ERROR, a structure as a parameter type is no longer erased, therefore this method is no longer a duplicate

}