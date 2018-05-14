package gw.lang.spec_old.typeinfo.accessibility

class HasPrivateInnerClass
{
  function foo()
  {
    var x : InnerClass
    print( x )
  }
    
  private class InnerClass
  {
  }
}
