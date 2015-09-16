package gw.specContrib.classes.property_Declarations

class Errant_BooleanPropertyConflict {
  property get BothGetIs111(): boolean {
    return false
  }

  function getBothGetIs111(): boolean {
    return false
  }

  function isBothGetIs111(): boolean {   //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
    return false
  }
}