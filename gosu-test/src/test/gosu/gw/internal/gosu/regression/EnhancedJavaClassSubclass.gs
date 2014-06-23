package gw.internal.gosu.regression

class EnhancedJavaClassSubclass extends EnhancedJavaClass {

  function callProtectedEnhancementFunction() : String {
    return protectedFunction()  
  }

  function superEcho(arg : String) : String {
    return super.echo(arg)  
  }

  function thisEcho(arg : String) : String {
    return this.echo( arg )
  }
  
  function unqualifiedEcho(arg : String) : String {
    return echo( arg )
  }
}
