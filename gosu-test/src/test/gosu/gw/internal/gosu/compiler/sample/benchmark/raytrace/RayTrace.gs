package gw.internal.gosu.compiler.sample.benchmark.raytrace;

uses java.util.*
uses java.io.*
uses java.lang.Math
uses java.lang.Integer
uses java.lang.Float
uses gw.util.StreamUtil

class RayTrace {

  var delta = Math.sqrt( Math.ulp( 1.0 ) )
  var inf = Float.POSITIVE_INFINITY

  class Vec {

    public var x : double
    public var y : double
    public var z : double

    construct( x2 : double, y2 : double, z2 : double )
    {
      x = x2
      y = y2
      z = z2
    }
  }

  function add( a : Vec, b : Vec ) : Vec
  {
    return new Vec( a.x + b.x, a.y + b.y, a.z + b.z )
  }

  function sub( a : Vec, b : Vec ) : Vec
  {
    return new Vec( a.x - b.x, a.y - b.y, a.z - b.z )
  }

  function scale( s : double, a : Vec ) : Vec
  {
    return new Vec( s * a.x, s * a.y, s * a.z )
  }

  function dot( a : Vec, b : Vec ) : double
  {
    return a.x * b.x + a.y * b.y + a.z * b.z
  }

  function unitise( a : Vec ) : Vec
  {
    return scale( 1 / Math.sqrt( dot( a, a ) ), a )
  }

  class Ray {

    public var orig : Vec
    public var dir : Vec

    construct( o : Vec, d : Vec )
    {
      orig = o
      dir = d
    }
  }

  class Hit {

    public var lambda : double

    public var normal : Vec

    construct( l : double, n : Vec )
    {
      lambda = l
      normal = n
    }
  }

  abstract class Scene {
    abstract function intersect( i : Hit, ray : Ray ) : Hit
  }

  class Sphere extends Scene {

    public var center : Vec

    public var radius : double

    construct( c : Vec, r : double )
    {
      center = c
      radius = r
    }

    function ray_sphere( ray : Ray ) : double
    {
      var v = sub( center, ray.orig )
      var b = dot( v, ray.dir )
      var disc = b * b - dot( v, v ) + radius * radius
      if (disc < 0) return inf
      var d = Math.sqrt( disc )
      var t2 = b + d
      if (t2 < 0) return inf
      var t1 = b - d
      return ( t1 > 0 ? t1 : t2 )
    }

    override function intersect( i : Hit, ray : Ray ) : Hit
    {
      var l = ray_sphere( ray )
      if (l >= i.lambda) return i
      var n = add( ray.orig, sub( scale( l, ray.dir ), center ) )
      return new Hit( l, unitise( n ) )
    }
  }

  class Group extends Scene {

    public var bound : Sphere

    public var objs : ArrayList<Scene>

    construct( b : Sphere )
    {
      bound = b
      objs = new ArrayList<Scene>()
    }

    override function intersect( i : Hit, ray : Ray ) : Hit
    {
      var l = bound.ray_sphere( ray )
      if (l >= i.lambda) return i
      for( scene in objs ) {
        i = scene.intersect( i, ray )
      }
      return i
    }
  }

  function ray_trace( light : Vec, ray : Ray, scene : Scene ) : double
  {
    var i = scene.intersect( new Hit( inf, new Vec( 0, 0, 0 ) ), ray )
    if (i.lambda == inf) return 0
    var o = add( ray.orig, add( scale( i.lambda, ray.dir ), scale( delta, i.normal ) ) )
    var g = dot( i.normal, light )
    if (g >= 0) return 0.
    var sray = new Ray( o, scale( -1, light ) )
    var si = scene.intersect( new Hit( inf, new Vec( 0, 0, 0 ) ), sray )
    return ( si.lambda == inf ? -g : 0 )
  }

  function create( level : int, c : Vec, r : double ) : Scene
  {
    var sphere = new Sphere( c, r )
    if (level == 1) return sphere
    var group = new Group( new Sphere( c, 3 * r ) )
    group.objs.add( sphere )
    var rn = 3 * r / Math.sqrt( 12 )
    for (dz in (-1..1).step( 2 ))
     for (dx in(-1..1).step( 2 )) {
      var c2 = new Vec( c.x + dx * rn, c.y + rn, c.z + dz * rn )
      group.objs.add( create( level - 1, c2, r / 2 ) )
    }
    return group
  }

  function run( n : int, level : int, ss : int )
  {
    var scene = create( level, new Vec( 0, -1, 0 ), 1 )
    var out = new FileOutputStream( "image.pgm" )
    out.write( StreamUtil.toBytes( "P5\n" + n + " " + n + "\n255\n" ) )
    for (y in (n - 1)..0)
     for (x in 0..|n) {
      var g : double
      for (dx in 0..|ss)
        for (dy in 0..|ss) {
          var d = new Vec( x + dx * 1. / ss - n / 2., y + dy * 1. / ss - n / 2., n )
          var ray = new Ray( new Vec( 0, 0, -4 ), unitise( d ) )
          g =g+ ray_trace( unitise( new Vec( -1, -3, 2 ) ), ray, scene )
      }
      out.write( ( .5 + 255. * g / ( ss * ss ) ) as int )
    }
    out.close()
  }

  static function main()
  {
    new RayTrace().run( 512, 8, 4 )
  }
}
