package gw.lang.spec_old.generics
uses java.util.Arrays
uses java.util.List

class GenericClassWithTypeVarArrayParam
{
  function foo<T>( yay: T[] ) : List<T>
  {
    return Arrays.asList( yay )
  }
}
