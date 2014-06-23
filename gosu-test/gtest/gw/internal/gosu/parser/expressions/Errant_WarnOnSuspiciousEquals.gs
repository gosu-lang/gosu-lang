package gw.internal.gosu.parser.expressions


@gw.testharness.DoNotVerifyResource
class Errant_WarnOnSuspiciousEquals {
  
  var x = blah // compile error to force issue
  
  override function equals( o : Object ) : boolean {
     return this == o
  }

  function equals( o : Object, o2 : Object ) : boolean {
     return this == o
  }

  function equals( o : String ) : boolean {
     return this == o
  }

  class Foo {
    override function equals( o : Object ) : boolean {
      new java.lang.Runnable() {
        override function run() {
          if( this == o ) print( "foo" )
        }
      }.run()
      return false
    }
  }

  class Bar {
    override function equals( o : Object ) : boolean {
      new java.lang.Runnable() {
        override function run() {
        }
        override function equals( o2 : Object ) : boolean {
          return this == o2
        }
      }.run()
      return false
    }
  }

}
