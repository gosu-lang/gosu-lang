package gw.internal.gosu.compiler.sample.enhancements

class _GenericEnhanced<T>
{
  var _sampleField : T as SampleProp

  function sampleFunction() : String {
    return "A function"
  }

  function sampleFunctionWithArgs( s : String ) : String {
    return "sampleFunctionWithArgs called with " + s
  }

  /*
  function test() : String {
    return this.SampleProp
  }

  function invokesPropertyIndirectly() : String {
    return this.SampleProp
  }
  */

}