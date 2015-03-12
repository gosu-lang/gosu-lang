package gw.specification.expressions.elementAccessExpression.p0
/**
 * Created by Sky on 2015/03/05 with IntelliJ IDEA.
 */
class B extends A{
  var bf2 : String as BField2
  static var bf4 : String as BField4 = "from class B"

  construct(var1 : int, var2 : String){
    super(var1)
    this.bf2 = var2
  }

}