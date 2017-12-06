package gw.specContrib.typeinference

uses gw.util.Pair

class Errant_InferConstructorTypeFromArgs {

  static class Hey<T extends CharSequence> {
    construct( t: T ) {}
    construct( t: List<T> ) {}
    construct( t: Object ) {}

    property get MuhT() : T { return null }

    function foo() {
      var hey = new Hey( "" )
      // Use a String method to verify type
      var test = hey.MuhT.substring( 8 )

      var list = new Hey( {""} )
      // Use a String method to verify type
      var test0 = list.MuhT.substring( 8 )

      var helo = new Hey( new StringBuilder() )
      // use StringBuilder method to verify type
      var test2 = helo.MuhT.append( 2 )

      var numba = new Hey( 8 )
      // Can't infer from int, so default to CharSequence
      var test3 = numba.MuhT.intValue()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
  }

  static class Batin<T extends Comparable<T>> {
    construct( t: T ) {}
    construct( t: List<T> ) {}
    construct( t: Object ) {}

    property get MuhT() : T { return null }

    function foo() {
      var hey = new Batin( "" )
      // Use a String method to verify type
      var test = hey.MuhT.substring( 8 )

      var hey2 = new Batin( {""} )
      // Use a String method to verify type
      var test2 = hey.MuhT.substring( 8 )

      var numba = new Batin2( 8bd )
      var test4 = numba.MuhT.add( 5bd )

      var foo = new Batin2( new StringBuilder() )
      // StringBuilder not Comparable, default to Ccomparable
      var test5 = numba.MuhT.append( "" )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_SUCH_FUNCTION
    }
  }

  static class Batin2<T extends Comparable> {
    construct( t: T ) {}
    construct( t: List<T> ) {}
    construct( t: Object ) {}

    property get MuhT() : T { return null }

    function foo() {
      var hey = new Batin2( "" )
      // Use a String method to verify type
      var test = hey.MuhT.substring( 8 )

      var hey2 = new Batin2( {""} )
      // Use a String method to verify type
      var test2 = hey.MuhT.substring( 8 )

      var numba = new Batin2( 8bd )
      var test4 = numba.MuhT.add( 5bd )

      var foo = new Batin2( new StringBuilder() )
      // StringBuilder not Comparable, default to Ccomparable
      var test5 = numba.MuhT.append( "" )  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_SUCH_FUNCTION
    }
  }

  static class Batin3
  {
    function make<B>( ref: Pair<String, B> ) : Pair<String, B> { return null }

    function hi()
    {
      var x = make( new Pair( "", new StringBuilder() ) )
      x.First.charAt( 0 )    // verify Pair<String, StringBuilder>
      x.Second.append( 'a' ) // verify Pair<String, StringBuilder>
    }
  }

  static class Batin4<T>
  {
    construct( t: T ) {}

    function foo( o: Object[] ) {}

    function hi()
    {
      var cs: CharSequence
      foo( {new Batin4( "hi" ), new Batin4( cs )} )
    }
  }
}