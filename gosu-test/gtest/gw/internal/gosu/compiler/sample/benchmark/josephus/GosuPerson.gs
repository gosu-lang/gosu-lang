package gw.internal.gosu.compiler.sample.benchmark.josephus

public class GosuPerson {
  var _count : int as Count
  var _prev : GosuPerson as Prev
  var _next : GosuPerson as Next

  construct( c : int ) {
    _count = c
  }

  function shout( shout : int, deadif : int ) : int {
    if( shout < deadif ) return shout + 1
    Prev.Next = Next
    Next.Prev = Prev
    return 1
  }
}
