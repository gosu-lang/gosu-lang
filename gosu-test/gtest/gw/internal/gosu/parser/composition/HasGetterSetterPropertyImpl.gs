package gw.internal.gosu.parser.composition

class HasGetterSetterPropertyImpl implements IHasGetterSetterProperty
{
  delegate _foo represents IHasGetterSetterProperty 

  construct()
  {
    _foo = new HasGetterSetterProperty()
  }

  static class HasGetterSetterProperty implements IHasGetterSetterProperty
  {
    var _foo : String as Foo
    construct()
    {
      _foo = "hello" 
    }
  }
}
