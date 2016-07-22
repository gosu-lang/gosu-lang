package gw.util.math
uses java.math.BigDecimal
uses java.math.RoundingMode

/**
 * Complex number having a real and imaginary component.
 */
public class ComplexNumber {
  /** The real part */
  private var r:BigDecimal as Real
  /** The imaginary part */
  private var i:BigDecimal as Imaginary

  private var rndMode:RoundingMode as RoundingMode = RoundingMode.HALF_DOWN
  private var scaleVal:int as Scale = 2

  construct() {
    Real = BigDecimal.valueOf(0)
    Real.setScale( Scale, RoundingMode )
    Imaginary = BigDecimal.valueOf(0)
    Imaginary.setScale(Scale,RoundingMode)
  }


  construct(rr:BigDecimal, ii:BigDecimal) {
    r = rr
    i = ii
    r.setScale(Scale, RoundingMode )
    i.setScale(Scale,RoundingMode)
  }


  /**
   * Display the current ComplexNumber as a String, for use in
   * print() and elsewhere.
   */
  public override function toString() : java.lang.String {
    var sb = new java.lang.StringBuffer().append(r.setScale(Scale,RoundingMode));
    if (i.compareTo(BigDecimal.ZERO) >= 0) {
      sb.append(" + ");  // else append(i) appends - sign
    } else {
      sb.append(" - ");
    }
    return sb.append('i').append(i.abs().setScale(Scale,RoundingMode)).toString();
  }

  /**
   * Return the magnitude of a complex number
   *
   */
  property get Magnitude() : BigDecimal {
    return gw.util.math.BigDecimalUtil.bigSqrt(r.multiply(r).add( i.multiply(i)));
  }

  /**
   * Add another ComplexNumber to this one
   */
  public function add(other:ComplexNumber) : ComplexNumber {
    return add(this, other);
  }

  /**
   * Add two Complexes
   */
  public static function add(c1:ComplexNumber, c2:ComplexNumber) : ComplexNumber {
    return new ComplexNumber(c1.r.add(c2.r), c1.i.add(c2.i));
  }

  /**
   * Subtract another ComplexNumber from this one
   */
  public function subtract(other:ComplexNumber) :ComplexNumber {
    return subtract(this, other);
  }

  /**
   * Subtract two Complexes
   */
  public static function subtract(c1:ComplexNumber, c2:ComplexNumber) : ComplexNumber{
    return new ComplexNumber(c1.r.subtract(c2.r), c1.i.subtract(c2.i));
  }

  /**
   * Multiply this ComplexNumber times another one
   */
  public function multiply(other:ComplexNumber) : ComplexNumber{
    return multiply(this, other);
  }


  public function divide(c1:ComplexNumber) : ComplexNumber{
    return divide(this,c1);
  }

  /**
   * Multiply two Complexes
   */
  public static function multiply(c1:ComplexNumber, c2:ComplexNumber) : ComplexNumber {
    var c1r_x_c2r = c1.r.multiply(c2.r)
    var c1i_x_c2i = c1.i.multiply(c2.i)
    var c1r_x_c2i = c1.r.multiply(c2.i)
    var c1i_x_c2r = c1.i.multiply(c2.r)

    return new ComplexNumber(c1r_x_c2r.subtract(c1i_x_c2i), c1r_x_c2i.add( c1i_x_c2r))
  }

  /**
   * Divide c1 by c2.
   */
  public static function divide(c1:ComplexNumber, c2:ComplexNumber) : ComplexNumber {
    var c1r_x_c2r = c1.r.multiply(c2.r)
    var c1i_x_c2i = c1.i.multiply(c2.i)
    var c1r_x_c2i = c1.r.multiply(c2.i)
    var c1i_x_c2r = c1.i.multiply(c2.r)
    var c2r_x_c2r = c2.r.multiply(c2.r)
    var c2i_x_c2i = c2.i.multiply(c2.i)
    var dividend1 = c1r_x_c2r.add(c1i_x_c2i)
    var dividend2 = c1i_x_c2r.subtract(c1r_x_c2i)
    var divisor1 = c2r_x_c2r.add(c2i_x_c2i)
    var divisor2 = c2r_x_c2r.add(c2i_x_c2i)

    if(divisor1.equals(BigDecimal.ZERO) || divisor2.equals(BigDecimal.ZERO)) {
      if(dividend1.equals(BigDecimal.ZERO) && dividend2.equals(BigDecimal.ZERO)) {
        return new ComplexNumber(0,0)
      }
    }

    return new ComplexNumber(
        dividend1.divide(divisor1),
        dividend2.divide(divisor2))
  }

  /* Compare this ComplexNumber number with another
   */
  public override function equals(o:Object) : boolean {
    if (!(o typeis ComplexNumber)) throw new IllegalArgumentException("ComplexNumber.equals argument must be a ComplexNumber")
    var other = o as ComplexNumber
    if(this == other) return true

    return r.equals(other.r) && i.equals(other.i)
  }

  /* Generate a hashCode; not sure how well distributed these are.
   */
  public override function hashCode() : int {
    return this.r.hashCode() | this.i.hashCode()
  }


  property get PolarCoordinates() : PolarCoordinate {
    var mag = this.Magnitude
    var angle = this.Angle
    var rVal = new PolarCoordinate(mag, angle)
    return rVal
  }

  /**
   * Returns the angle in degrees.
   * @return
   */
  property get Angle() : BigDecimal {
    var rVal:double = 0
    if(BigDecimal.ZERO.equals(r) || BigDecimal.ZERO.equals(i) ) {
      if(BigDecimal.ZERO.equals(r)) {
        rVal = i.compareTo(BigDecimal.ZERO) > 0 ? 90 : 270
      } else {
        rVal = r.compareTo(BigDecimal.ZERO) > 0 ? 0 : 180
      }
    }

    var α:double = Math.toDegrees(Math.atan2(i.doubleValue(),r.doubleValue()))

    if(r.compareTo(BigDecimal.ZERO) > 0 && i.compareTo(BigDecimal.ZERO) > 0) {
      rVal = α
    } else if(r.compareTo(BigDecimal.ZERO) < 0 && i.compareTo(BigDecimal.ZERO) > 0) {
      rVal = α
    } else if(r.compareTo(BigDecimal.ZERO) < 0 && i.compareTo(BigDecimal.ZERO) < 0) {
      rVal = 360 + α
    } else if(r.compareTo(BigDecimal.ZERO) > 0 && i.compareTo(BigDecimal.ZERO) < 0) {
      rVal = 360 + α
    }

    return BigDecimal.valueOf(rVal)
  }

}