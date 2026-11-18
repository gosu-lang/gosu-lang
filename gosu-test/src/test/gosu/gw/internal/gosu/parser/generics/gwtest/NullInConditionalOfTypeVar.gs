package gw.internal.gosu.parser.generics.gwtest

class NullInConditionalOfTypeVar
{
  function itemOrNull<T>( item : T, cond : boolean ) : T
  {
    return cond ? item : null
  }

  function nullOrItem<T>( item : T, cond : boolean ) : T
  {
    return cond ? null : item
  }
}
