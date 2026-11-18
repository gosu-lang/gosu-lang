package gw.internal.gosu.parser.generics.gwtest

uses java.util.ArrayList

class NarrowedTypeVarParameterized
{
  function runList<T>( items : List<T> ) : List<T>
  {
    if( items typeis Runnable )
    {
      items.run()
      return items
    }
    return null
  }

  function runArrayList<T>( items : ArrayList<T> ) : T
  {
    if( items typeis Runnable )
    {
      items.run()
      return items.get( 0 )
    }
    return null
  }
}
