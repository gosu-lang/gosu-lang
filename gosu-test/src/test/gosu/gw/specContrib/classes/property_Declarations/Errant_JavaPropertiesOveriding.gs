package gw.specContrib.classes.property_Declarations

class Errant_JavaPropertiesOveriding implements JavaClass1 {  //## issuekeys: MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD, MSG_UNIMPLEMENTED_METHOD

  override function setDestinationID(destinationID: int) {      //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY, MSG_PROPERTY_AND_FUNCTION_CONFLICT

  }

  override function getDestinationID(): int {      //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY, MSG_PROPERTY_AND_FUNCTION_CONFLICT
    return 0
  }

  override function setDestination(destination: boolean) {      //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY, MSG_PROPERTY_AND_FUNCTION_CONFLICT

  }

  override function isDestination(): boolean {      //## issuekeys: MSG_FUNCTION_NOT_OVERRIDE_PROPERTY
    return false
  }

}