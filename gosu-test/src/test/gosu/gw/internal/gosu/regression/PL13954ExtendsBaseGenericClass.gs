package gw.internal.gosu.regression

uses java.util.*

class PL13954ExtendsBaseGenericClass<T> extends PL13954BaseGenericClass<T> {

  var _nodeSet:Set<T>

  construct() {
    _nodeSet = new HashSet<T>()
  }

  override function visit(node : T) {
    _nodeSet.add(node)
  }

  /**
   * Should be invoked only after traversal.
   */
  property get NodeSet() : Set<T> {
    return _nodeSet
  }
}