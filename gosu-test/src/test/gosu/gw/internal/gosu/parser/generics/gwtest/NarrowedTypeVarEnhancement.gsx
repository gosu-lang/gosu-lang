package gw.internal.gosu.parser.generics.gwtest

uses java.util.Date

enhancement NarrowedTypeVarEnhancement<T> : java.util.List<T>
{
  function totalOfNarrowedItems() : long
  {
    var result : long = 0
    for( item in this )
    {
      if( item typeis Date )
      {
        result += item.Time
      }
    }
    return result
  }
}
