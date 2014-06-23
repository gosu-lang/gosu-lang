package gw.internal.gosu.compiler.sample.benchmark.josephus

uses java.lang.System

class GosuChain
{
    var _first : GosuPerson
    static var _lc : long = 0

    construct( size : int )
    {
        var last : GosuPerson
        var current : GosuPerson
        var i : int = 0
        while( i < size )
        {
            current = new GosuPerson(i)
            if( _first == null ) _first = current
            if( last != null )
            {
                last.Next = current
                current.Prev = last
            }
            last = current
            i++
        }
        _first.Prev = last
        last.Next = _first
    }

    function kill( nth: int ) : GosuPerson
    {
        var current = _first
        var shout : int  = 1
        while( !(current.Next === current) )
        {
            shout = current.shout( shout, nth )
            current = current.Next
            _lc++
        }
        _first = current
        return current
    }

    function getFirst() : GosuPerson
    {
        return _first
    }

    static function run() : int
    {
        var ITER : int = 100000
        var start = System.nanoTime()
        var i : int  = 0
        while( i < ITER )
        {
            var chain = new GosuChain( 40 )
            chain.kill( 3 )
            i++
        }
        print( _lc )
        var end = System.nanoTime()
        var iNanos = (end - start) / ITER
        print( iNanos )
        return iNanos as int
    }
}