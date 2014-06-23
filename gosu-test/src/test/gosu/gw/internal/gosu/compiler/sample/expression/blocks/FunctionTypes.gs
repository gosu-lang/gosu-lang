package gw.internal.gosu.compiler.sample.expression.blocks

class FunctionTypes {

  function takesABlockWithNoArgsOrReturnType( sample() ) {
  }

  function takesABlockWithNoArgs( sample():Boolean ) {
  }

  function takesABlockWithOneArg( sample(Boolean):Boolean ) {
  }

  function takesABlockWithTwoArgs( sample(Boolean,Boolean):Boolean ) {
  }

  function returnsABlockWithNoArgsOrReturnType() : block() {
    return null
  }

  function returnsABlockWithNoArgs() : block():Boolean {
    return null
  }

  function returnsABlockWithOneArg() : block(Boolean):Boolean {
    return null
  }

  function returnsABlockWithTwoArgs() : block(Boolean,Boolean):Boolean {
    return null
  }

  function returnsABlock() : Object {
    return typeof \-> print("foo")
  }

}