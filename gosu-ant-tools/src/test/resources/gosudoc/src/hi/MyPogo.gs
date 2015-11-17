package gosudoc.src.hi //Gosudoc strictly checks the paths of inputs relative to the root (the resources folder)  

/**
 * This is a well-documented plain ol' Gosu class
 */
class MyPogo {
  
  var _aStringField : String as SomeStringProperty 
  var _anIntField : int as SomeIntProperty

  /**
   * A sample public method
   * @param arg0 a String that you can set
   * @param arg1 a number that you can set, with a default value
   * @param arg2 a String that you can set, with a default value
   * @return a meaningless mish-mash of arguments
   */
  function doIt(arg0 : String, arg1 : int = 42, arg2 : String = "a default value") : String {
    return arg0 + arg1 + arg2
  }

}