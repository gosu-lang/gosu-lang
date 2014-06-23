package gw.internal.gosu.regression.subpackage

class TakesABlock {

  construct() {

  }
  
  static function callBlock<T>(blockArg() : T) : T {
    return blockArg()  
  }

}
