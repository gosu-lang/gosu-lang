package gw.internal.gosu.parser.composition

class HasPropertyImpl implements IHasProperty
{
  delegate _foo represents IHasProperty 

  construct() 
  {
    _foo = new HasProperty()
  }

  static class HasProperty implements IHasProperty
  {
    override property get Foo() : String
    {
      return "hello"
    }
  }
}
