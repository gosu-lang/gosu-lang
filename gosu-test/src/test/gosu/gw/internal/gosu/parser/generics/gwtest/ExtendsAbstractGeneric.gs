package gw.internal.gosu.parser.generics.gwtest

class ExtendsAbstractGeneric extends AbstractGenericWithOneMethod<String>
{
  override function foo( t : String ) : String
  {
    return "hello"
  }
}