package gw.internal.gosu.regression

uses gw.test.TestClass

class DeeplyNestedBlocksRegressionTest extends TestClass {

  function testDeepNesting() {
    foo(\ -> bar(\ -> foo(\ -> bar(\ -> foo(\ -> bar(\ -> foo(\ -> {print("hi")} )))))))
  }
  
  function foo(a : block()) { 
    a() 
  } 

  function bar(b : block()) { 
    b() 
  } 


}
