package gw.specContrib.dimensions.physics
uses junit.framework.TestCase

class PhysicsTest extends gw.test.TestClass implements UnitConstants {
  function testMe() {
    var rate = 50 *mph
    var time = 3 *min
    var distance = rate * time
    assertTrue( 2.5 *mi == distance )
    assertTrue( 4.02336 *km == distance.to( km ) )
    time = distance / rate
    assertTrue( 3 *min == distance / rate )

    var mass = 20 * g
    var acc = 9.8 * (m/s/s)
    var force = mass * acc
    assertTrue( 196 *(g*(m/s/s)) == force )
    assertTrue( 0.196bd *(kg*(m/s/s)) == force.to( kg*(m/s/s) ) )
    assertTrue( "1.417670714837139298612298722559885 lb ft/s/s" == force.to( lb*(ft/s/s) ).toString() )

    var momentum = mass * rate
    assertTrue( 1000 *(g*(mi/hr)) == mass * rate )
    assertTrue( 0.44704 *(kg*(m/s)) == momentum.to( Ns ) )    
  }
}