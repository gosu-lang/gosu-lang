package gw.specification.expressions.elementAccessExpression.p0

uses java.util.ArrayList
uses java.lang.Integer

/**
 * Created by Sky on 2015/03/03 with IntelliJ IDEA.
 */
class MyList extends ArrayList<Integer> {
    function food() {
      var f : Integer = this[0]
    }
}