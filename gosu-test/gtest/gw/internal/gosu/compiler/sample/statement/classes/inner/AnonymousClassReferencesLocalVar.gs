package gw.internal.gosu.compiler.sample.statement.classes.inner

uses java.lang.Runnable

class AnonymousClassReferencesLocalVar
{
  var _str : String as Str
  var _strAfter : String as StrAfter 

  function foo() : Runnable
  {
    var localStr = "hello"
    var r =
      new Runnable()
      {
        override function run()
        {
          _str = localStr
          localStr = "bye"
        }
      }
    r.run()
    _strAfter = localStr
    return r
  }

  function fooWithCapturedArg( paramStr : String ) : Runnable
  {
    var r =
      new Runnable()
      {
        override function run()
        {
          _str = paramStr
          paramStr = "bye"
        }
      }
    r.run()
    _strAfter = paramStr
    return r
  }

  function nestedAnonymousClassCapturesOutersCapture() : Runnable
  {
    var localStr = "hello"
    var r =
      new Runnable()
      {
        override function run()
        {
          var temp = localStr // captured

          new Runnable()
          {
            override function run()
            {
              _str = localStr
              localStr = "bye"
            }
          }.run()
        }
      }
    r.run()
    _strAfter = localStr
    return r
  }
}
