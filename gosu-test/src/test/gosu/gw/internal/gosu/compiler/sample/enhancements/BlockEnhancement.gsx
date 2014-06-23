package gw.internal.gosu.compiler.sample.enhancements

uses java.util.*
uses gw.util.Pair

enhancement BlockEnhancement<T> : _BlockEnhanced<T> {

  function testBlockInvocation<Q>( mapper(T):Q ) : List<Q> {
    var returnList = new ArrayList<Q>()
    for( elt in this ) {
      returnList.add( mapper( elt ) )
    }
    return returnList
  }

  function declareAndExecuteBlock() : String {
    var blk = \-> "declaredAndInvokedInEhancement"
    return blk()
  }

  function produceBlockWithNoCapture() : block():String {
    return \-> "declaredInEnhancement"
  }

  function produceBlockWithLocalVarCaputure() : Pair<block(), block():int> {
    var i = 0
    return Pair.make( \-> {i = 42}, \-> i )
  }

  function produceBlockWithPropertyCaputure() : block():int {
    return \-> LocalProp
  }

  function produceBlockWithIndirectPropertyCaputure() : block():int {
    return \-> this.LocalProp
  }

  function produceBlockWithFunctionCaputure() : block():int {
    return \-> localFunc()
  }

  function produceBlockWithIndirectFunctionCaputure() : block():int {
    return \-> this.localFunc()
  }
  
  property get LocalProp() : int  {
    return 42
  }

  function localFunc() : int  {
    return 42
  }

}