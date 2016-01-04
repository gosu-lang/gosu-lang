package gw.lang.spec_old.statements.class_stmt.inner_classes.anonymous_classes
uses java.lang.Runnable

class StaticAnonymousClass
{
  static var _data : String
  
  static function foo() : String
  {
    var anonymous = 
      new Runnable()
      {
        function run()
        {
          _data = "itworks"
        }
      }
    anonymous.run()
    return _data
  }
}
