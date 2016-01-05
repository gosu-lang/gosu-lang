package gw.lang.spec_old.generics

uses java.lang.CharSequence

enhancement SubOfRecursiveType2Enhancement<T extends CharSequence, B extends SubOfRecursiveType2<T, B>> : SubOfRecursiveType2<T,B> 
{
  function enhMethod( t : T, b : B ) : B
  {
    print( t )
    return b
  }
}
