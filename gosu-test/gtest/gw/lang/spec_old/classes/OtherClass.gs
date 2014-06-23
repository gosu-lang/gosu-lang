package gw.lang.spec_old.classes

class OtherClass {
  function callme() : InternalClass
  {
    new InternalClass().publicFunc()
    return new InternalClass()
  }
  
  function getPrivateClass() : PrivateClass
  {
    return new PrivateClass()
  }
  
  private class PrivateClass
  {
    function publicFunc() {print("hello")}
    
    override function toString() : String
    {
      return "PrivateClass"
    }
  }
}
