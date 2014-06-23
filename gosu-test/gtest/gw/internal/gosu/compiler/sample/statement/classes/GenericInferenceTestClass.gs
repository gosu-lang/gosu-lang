package gw.internal.gosu.compiler.sample.statement.classes

uses java.util.ArrayList

class GenericInferenceTestClass
{
  function getElemFromListOfStrings() : String
  {
    var l = new ArrayList<String>()
    l.add( "hello" )
    var elem = l.get( 0 )
    return elem
  }

  function getCharFromElemFromListOfStrings() : char
  {
    var l = new ArrayList<String>()
    l.add( "hello" )
    var firstChar = l.get( 0 ).charAt( 0 ) // infers get() as string
    return firstChar
  }
}