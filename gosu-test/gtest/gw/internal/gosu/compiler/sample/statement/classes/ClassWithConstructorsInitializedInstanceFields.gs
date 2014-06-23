package gw.internal.gosu.compiler.sample.statement.classes

class ClassWithConstructorsInitializedInstanceFields
{
  public var _uninitboolean : boolean
  public var _uninitbyte : byte
  public var _uninitshort : short
  public var _uninitint : int
  public var _uninitlong : long
  public var _uninitfloat : float
  public var _uninitdouble : double
  public var _uninitchar : char
  public var _uninitstring : String

  public var _uninitarrboolean : boolean[]
  public var _uninitarrbyte : byte[]
  public var _uninitarrshort : short[]
  public var _uninitarrint : int[]
  public var _uninitarrlong : long[]
  public var _uninitarrfloat : float[]
  public var _uninitarrdouble : double[]
  public var _uninitarrchar : char[]
  public var _uninitarrstring : String[]

  public var _boolean : boolean = true
  public var _byte : byte = 1
  public var _short : short = 2
  public var _int : int = 3
  public var _long : long = 4
  public var _float : float = 5
  public var _double : double = 6
  public var _char : char = '7'
  public var _string : String = "8"

  public var _arrboolean : boolean[] = new boolean[] {true}
  public var _arrbyte : byte[] = new byte[] {1,2}
  public var _arrshort : short[] = new short[] {3,4}
  public var _arrint : int[] = new int[] {5,6}
  public var _arrlong : long[] = new long[] {7,8}
  public var _arrfloat : float[] = new float[] {9.1 as float, 0.1 as float}
  public var _arrdouble : double[] = new double[] {1.1, 1.2}
  public var _arrchar : char[] = new char[] {'a', 'b'}
  public var _arrstring : String[] = new String[] {"c", "d"}

  construct( str : String )
  {
  }

  construct()
  {
    this( "default" )
  }
}