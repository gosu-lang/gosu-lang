package org.gosulang.foo

class MyGosu 
{
  property Name: String
  
  // NOTE: FromJava references MyGosu, so this verifeis bi-directional
  // reference support at the member declaration level
  property FJ: FromJava 
  
  construct()
  {
    _Name = "no_args"
  }
  
  construct( name: String )
  {
    _Name = name
  }
  
  function method_no_args() : MyGosu
  {
    _Name = "method_no_args"
    return this
  }
  
  function method_one_arg( text: String ) : MyGosu
  {
    _Name = text
    return this
  }
  
  function gen_method_no_args<E>() : MyGosu
  {
    _Name = "gen_method_no_args"
    return this
  }
  
  function gen_method_one_arg<E extends CharSequence>( text: E ) : MyGosu
  {
    _Name = text.toString()
    return this
  }

  // Note reified methods should NOT be accessible from Java.  Otherwise, if we create 
  // bridge methods so Java could access them without explicitly providing type params 
  // as arguments, there would likely be issues with the methods not behaving correctly.
  // They are reified for a reason.
  //
  // Leaving them here to test the Java CANNOT access them.
  
  reified function reified_gen_method_no_args<E>() : String
  {
    _Name = "reified_gen_method_no_args"
    return E.Type.Name
  }
  
  reified function reified_gen_method_one_arg<E extends CharSequence>( text: E ) : String
  {
    _Name = text.toString()
    return E.Type.Name
  }
}