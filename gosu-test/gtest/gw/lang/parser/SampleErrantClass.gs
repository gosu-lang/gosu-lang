package gw.lang.parser
uses gw.testharness.DoNotVerifyResource
uses java.util.concurrent.atomic.AtomicInteger

@DoNotVerifyResource
class SampleErrantClass {

  var ai : AtomicInteger = 0

  function foo() {
    for( i in 0..|10 ) {}
  }

}
