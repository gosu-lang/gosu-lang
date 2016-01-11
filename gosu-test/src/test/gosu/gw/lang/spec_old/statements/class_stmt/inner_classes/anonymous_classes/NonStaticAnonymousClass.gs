package gw.lang.spec_old.statements.class_stmt.inner_classes.anonymous_classes
uses java.lang.Runnable

class NonStaticAnonymousClass
{
  var _data : String
  
  function foo() : String
  {
    var anonymous = 
      new Runnable()
      {
        override function run()
        {
          _data = "itworks"
        }
      }
    anonymous.run()
    return _data
  }
}
