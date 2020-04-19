package gw.specContrib.structural

class No_MethodClash_StructureObject {

  class JJ{}
  class KK extends JJ{}
  structure S{
    function hello()
  }

  function foo1(s : S) : JJ{
    return null
  }

  // support overloading involving structural param types
  function foo1(O : Object) : KK {
    return null
  }

}