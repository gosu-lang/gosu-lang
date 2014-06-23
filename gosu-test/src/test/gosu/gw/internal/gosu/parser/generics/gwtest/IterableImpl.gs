package gw.internal.gosu.parser.generics.gwtest

uses java.lang.Iterable
uses java.util.Iterator
uses java.lang.UnsupportedOperationException

class IterableImpl implements Iterable
{
  function iterator() : Iterator
  {
    return
      new Iterator()
      {
        function hasNext() : boolean
        {
          return true
        }

        function next() : Object
        {
          return new DateTime()
        }

        function remove()
        {
          throw new UnsupportedOperationException()
        }
      }
  }
}