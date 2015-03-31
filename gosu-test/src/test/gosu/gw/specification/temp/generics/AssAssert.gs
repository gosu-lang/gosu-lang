package gw.specification.temp.generics
class AssAssert extends BaseAssert<String, AssAssert> implements AssertEvaluator<String, AssAssert> {
  override function evaluate(assertion: AssAssert): AssAssert {
    return null
  }
}