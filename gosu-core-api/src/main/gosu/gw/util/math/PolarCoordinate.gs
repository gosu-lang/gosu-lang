package gw.util.math
uses java.math.BigDecimal

/**
 * Represents a polar coordinate. Underneath it is backed by a cartesian complex number since the
 * maths are simpler.
 */
public class PolarCoordinate {
    var mag:BigDecimal as Magnitude = BigDecimal.ZERO
    var ang:BigDecimal as Angle = BigDecimal.ZERO
    private static final var PI =  BigDecimal.valueOf(Math.PI)
    private static final var _180 = new BigDecimal("180")

    private var rect = new ComplexNumber()

    public construct() {

    }

    public construct(pMag:BigDecimal, pAng:BigDecimal) {
        mag = pMag;
        ang = pAng;
        updateValues()
    }

    public construct(compR:ComplexNumber) {
        Magnitude = compR.Magnitude
        Angle = compR.Angle
        rect = compR
    }

    property get RectangularCoordinates() : ComplexNumber {
        return rect
    }

    private function updateValues() {
        var angRadians = Math.toRadians(ang.doubleValue())
        var r = mag.multiply(BigDecimal.valueOf(Math.cos(angRadians)))
        var i = mag.multiply(BigDecimal.valueOf(Math.sin(angRadians)))
        rect.Real = r
        rect.Imaginary = i
    }

    public override function toString() : String {
        return Magnitude.setScale(5, BigDecimal.ROUND_HALF_UP) + "@" + ang.setScale(4, BigDecimal.ROUND_HALF_UP)
    }

    public function add( o:PolarCoordinate) :PolarCoordinate{
        this.updateValues()
        o.updateValues()
        return new PolarCoordinate(this.rect.add(o.rect))
    }
}
