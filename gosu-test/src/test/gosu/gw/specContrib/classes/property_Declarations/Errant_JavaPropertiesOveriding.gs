package gw.specContrib.classes.property_Declarations

class Errant_JavaPropertiesOveriding implements JavaClass {

  override function setDestinationID(destinationID: int) {      //## issuekeys: METHOD DOES NOT OVERRIDE METHOD FROM ITS SUPERCLASS. CONVERT THE METHOD INTO A PROPERTY

  }

  override function getDestinationID(): int {      //## issuekeys: METHOD DOES NOT OVERRIDE METHOD FROM ITS SUPERCLASS. CONVERT THE METHOD INTO A PROPERTY
    return 0
  }

  override function setDestination(destination: boolean) {      //## issuekeys: METHOD DOES NOT OVERRIDE METHOD FROM ITS SUPERCLASS. CONVERT THE METHOD INTO A PROPERTY

  }

  override function isDestination(): boolean {      //## issuekeys: METHOD DOES NOT OVERRIDE METHOD FROM ITS SUPERCLASS. CONVERT THE METHOD INTO A PROPERTY
    return false
  }

}