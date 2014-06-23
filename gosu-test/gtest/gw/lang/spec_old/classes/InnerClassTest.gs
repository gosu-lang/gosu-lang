package gw.lang.spec_old.classes
uses gw.test.TestClass
uses java.lang.Runnable

class InnerClassTest extends TestClass
{
  function testInnerClassVariableCaptureWorks() {
    var i = 10
    var runnable = new Runnable() {
      override function run() {
        i = 20
      }
    }
    runnable.run()
    assertEquals( 20, i )
  }

  class SuperClass {
    
    protected function callsFoo() : int {
      return foo( 0 )
    }

    protected function foo( j : int ) : int {
      return 20
    }
  }

  function captureParam( i : int ) : SuperClass {
    return new SuperClass() {
      protected override function foo(j : int) : int {
        return i
      }
    }
  }
  
  function testInnerClassVariableCaptureWorksInOverridenCase() {
    assertEquals( 10, captureParam( 10 ).callsFoo() )
  }

}
