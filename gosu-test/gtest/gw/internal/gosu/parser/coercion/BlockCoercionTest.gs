package gw.internal.gosu.parser.coercion

class BlockCoercionTest
{

  var _b : boolean as BoolVal

  function BlockCoercionTest()
  {
    _b = false
  }

  function getTrueSetter() : java.lang.Runnable
  {
    return \-> { _b = true }
  }

  function getFalseSetter() : java.lang.Runnable
  {
    return \-> { _b = false }
  }


}