package gw.specContrib.generics

class Errant_ShadowTypeVars<T>
{
  // Ok for static function type var to have same name since not in scope of class' type var
  static function staticFunc<T>( t: T ) { print( t ) }

  function instanceFunc<T>( t: T ) { print( t ) }  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
}