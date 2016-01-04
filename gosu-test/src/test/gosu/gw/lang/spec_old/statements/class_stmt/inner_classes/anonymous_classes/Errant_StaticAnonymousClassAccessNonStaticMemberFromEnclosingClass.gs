package gw.lang.spec_old.statements.class_stmt.inner_classes.anonymous_classes
uses java.lang.Runnable

@gw.testharness.DoNotVerifyResource
class Errant_StaticAnonymousClassAccessNonStaticMemberFromEnclosingClass
{
  var _data : String
  
  static function foo()
  {
    var anonymous = 
      new Runnable()
      {
        override function run()
        {
          _data = "nodice"
        }
      }
  }
}
