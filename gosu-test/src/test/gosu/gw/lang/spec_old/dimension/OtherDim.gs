package gw.lang.spec_old.dimension
uses java.lang.Integer

final class OtherDim implements IDimension<OtherDim,Integer>
{
  construct()
  {
  }

  override function numberType() : java.lang.Class<Integer>
  {
    return Integer
  }

  override function toNumber() : Integer
  {
    return 1
  }

  override function fromNumber( p0: Integer ) : OtherDim
  {
    return this
  }

  override function compareTo( o: OtherDim ) : int {
    return 0
  }
}
