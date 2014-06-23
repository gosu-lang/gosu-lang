package gw.internal.gosu.parser.generics.gwtest

uses java.lang.Iterable
uses java.util.Iterator
uses java.lang.UnsupportedOperationException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantIterableParameterizedImpl implements Iterable<String>
{
  function iterator() : Iterator<DateTime> // should cause err
  {
    return
      new Iterator<DateTime>()
      {
        function hasNext() : boolean
        {
          return true
        }

        function next() : DateTime
        {
          return null
        }

        function remove()
        {
          throw new UnsupportedOperationException()
        }
      }
  }
}