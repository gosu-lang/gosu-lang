package gw.specification.structures

uses java.lang.CharSequence
uses java.util.Map

class TestStructures {
  structure Echo {
    function echo( i: boolean ) : boolean
    function echo( i: byte ) : byte
    function echo( i: char ) : char
    function echo( i: short ) : short
    function echo( i: int ) : int
    function echo( i: long ) : long
    function echo( i: float ) : float
    function echo( i: double ) : double
    function echo( i: int[] ) : int[]
    function echo( i: String ) : String
    function echo( i: List<String> ) : List<String>
  }

  structure GenericEcho<E extends CharSequence> {
    function echo( e: E ) : E
    function echo<F>( e: E, f: F ) : Map<E, F>
    function echo<F>( f: F, e: E ) : F
  }

  structure PropertyStruct {
    property get Foo() : String
    property set Foo( s: String )
  }
}