package gw.lang.spec_old.expressions

class Errant_WarningForExpansionOnDot
{
  construct()
  {
    var s : MemberAccessTestClass[] = { new MemberAccessTestClass( "a" ), new MemberAccessTestClass( "b" ) }
    print( s.X  ) // Should warn about use of expansion on '.', use '*.' instead
  }
}
