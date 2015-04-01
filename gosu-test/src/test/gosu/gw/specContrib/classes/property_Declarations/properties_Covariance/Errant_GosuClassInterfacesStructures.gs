package gw.specContrib.classes.property_Declarations.properties_Covariance

/**
 * Simple cases of covariance involving Structure and Interface
 * One positive case and one negative
 */

class Errant_GosuClassInterfacesStructures {
  structure MyStructure{}
  interface MyInterface extends MyStructure {}

  property get MyProp1() : MyInterface {return null}
  property set MyProp1(a : MyStructure) {}

  property get MyProp2() : MyStructure {return null}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY
  property set MyProp2(a : MyInterface) {}      //## issuekeys: INVALID PROPERTY DECLARATION: GETTER AND SETTER SHOULD AGREE ON THE TYPE OF THE PROPERTY


}