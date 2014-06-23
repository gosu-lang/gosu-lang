package gw.internal.gosu.parser.generics.gwtest
uses java.lang.CharSequence

class TestSuperIsBounded<T extends CharSequence>
{
  function superIsBounded() : TestSuperIsBounded<? super T>
  {
    return null
  }
}
