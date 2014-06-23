package gw.lang.spec_old.bugs
uses java.util.concurrent.Callable

@gw.testharness.DoNotVerifyResource
enhancement Errant_BugDumpingGroundTestEnhancement : BugDumpingGroundTest {

  function sampleEnhancementMethod() : Callable<Callable<String>> {
    return \-> new Callable<String>() {
      var x : String = this.getTestString()
      override function call() : String {
        return x
      }  
    }
  }
 
}
