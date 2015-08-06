package gw.internal.gosu.parser.generics.gwtest

uses java.lang.Iterable
uses java.util.Iterator
uses java.util.Date
uses java.lang.UnsupportedOperationException
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantIterableParameterizedImpl implements Iterable<String>
{
  function iterator() : Iterator<Date> // should cause err
  {
    return
      new Iterator<Date>()
      {
        function hasNext() : boolean
        {
          return true
        }

        function next() : Date
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