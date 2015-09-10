package gw.specContrib.classes

class Errant_GosuImplementJavaIsGetPropertyMethods {
  static class Test1 implements JavaIsGetPropertyMethods {
    override property get MyProperty(): boolean {
      return false
    }

    override function isMyProperty(): boolean {
      return true
    }
  }

  static class Test2 implements JavaIsGetPropertyMethods {   //## issuekeys: MSG_UNIMPLEMENTED_METHOD
    override property get MyProperty(): boolean {
      return false
    }

    override function getMyProperty(): boolean {  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY
      return true
    }
  }

  static class Test3 implements JavaIsGetPropertyMethods {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD
    override property get MyProperty(): boolean {
      return false
    }
  }

  static class Test4 implements JavaIsGetPropertyMethods {   //## issuekeys: MSG_UNIMPLEMENTED_METHOD
    override function isMyProperty(): boolean {
      return true
    }
  }

  static class Test5 implements JavaIsGetPropertyMethods {   //## issuekeys: MSG_UNIMPLEMENTED_METHOD
    override function getMyProperty(): boolean {  //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY
      return true
    }
  }
}