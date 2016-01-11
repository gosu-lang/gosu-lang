package gw.lang.spec_old.generics

class HasSimpleGenericMethod<T>
{
  public static function of<Q>(reference : Q) : HasSimpleGenericMethod<Q>
  {
    return null
  }
}
