package gw.util.science
uses gw.util.Rational
uses java.math.BigDecimal

// My temporary and hacky attempt at getting some trig functions for rationals
class RationalTrig {
  private static final var instance: RationalTrig = new RationalTrig( 10 )
  private var _cosFactors : List<Rational>
  private var _precision : int
  
  static function instance() : RationalTrig {
    return instance
  }
  
  private construct( precision: int ) {
    _precision = precision
    _cosFactors = {}
    var one = Rational.ONE
    var stopWhen = Rational.get( BigDecimal.ONE.movePointLeft( precision ) )
    var divWith = one + one
    var inc = divWith
    var factor: Rational = null
    do {
      factor = one / divWith
      _cosFactors.add( factor )
      inc = inc + one
      divWith = divWith * inc
      inc = inc + one
      divWith = divWith * inc
    }
    while( factor.compareTo( stopWhen ) > 0 )
  }
  
  function cos( x: Rational ) : Rational {
    return Math.cos( x )  
  }
  
  function sin( x: Rational ) : Rational {
    return Math.sin( x )  
  }
  
  function atan( x: Rational ) : Rational {
    return Math.atan( x )  
  }

  
//  function cos( x: Rational ) : Rational {
//    var res = Rational.ONE
//    var xn = x * x
//    for( i in 0..|_cosFactors.size() ) {
//      var factor = _cosFactors.get( i )
//      factor = factor * xn 
//      if( i % 2 == 0 ) {
//        factor = factor.negate()
//      }
//      res = res + factor
//      xn *= x
//      xn *= x
//    }
//    return res
//  }
//  
//  function sin( x: Rational ) : Rational {
//    return (Rational.ONE - cos( x ).pow( 2 )).sqrt()
//  }
// 
//  function atan( x: Rational ) : Rational {
//    var answer : Rational = x
//    var bAdd = false
//    // based on atan(x) series: x - x^3/3 + x^5/5 - x^7/7 + x^9/9 ...
//    for( i in (3..110).step( 2 ) ) {
//      var elem : Rational = x.pow( i ).divide( Rational.get( i ) )
//      answer = bAdd ? answer.add( elem ) : answer.subtract( elem )
//      bAdd = !bAdd
//    }
//    return answer  
//  }
//    
  public static function main(args : String[]) : void {
    var ts = instance
    print( "java.lang.Math.cos(0.5)=" + Math.cos(0.5))
    print("this.cos(0.5)=" + ts.cos( Rational.get( "0.5" ) ).toBigDecimal() )
    
    print( "java.lang.Math.sin(0.5)=" + Math.sin(0.5))
    print("this.sin(0.5)=" + ts.sin( Rational.get( "0.5" ) ).toBigDecimal() )
  }
}