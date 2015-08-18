package gw.specContrib.classes

class Errant_GosuImplementJavaIsGetPropertyMethods implements JavaIsGetPropertyMethods {
  override property get MyProperty(): boolean {
    return false
  }

  override function isMyProperty(): boolean { //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
    return false
  }

  static class Errant_Inner implements JavaIsGetPropertyMethods {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD
    override property get MyProperty(): boolean {
      return false
    }

    // Missing isMyProperty() override
  }
}