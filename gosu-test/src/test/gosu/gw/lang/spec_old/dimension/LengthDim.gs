package gw.lang.spec_old.dimension

uses java.lang.Integer 

final class LengthDim extends AbstractDim<LengthDim> 
{
  construct( value : Integer )
  {
    super( value )
  }

  function divide( t : TimeDim ) : RateDim
  {
    return new RateDim( toNumber() / t.toNumber() )
  }  
}
