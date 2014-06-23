package gw.internal.gosu.parser.generics.gwtest

uses java.lang.Iterable
uses java.util.Iterator
uses java.lang.UnsupportedOperationException

class IterableParameterizedImpl implements Iterable<String>
{
  function iterator() : Iterator<String>
  {
    return
      new Iterator<String>()
      {
        function hasNext() : boolean
        {
          return true
        }

        function next() : String
        {
          return "hello"
        }

        function remove()
        {
          throw new UnsupportedOperationException()
        }
      }
  }
}