package gw.specContrib.classes.property_Declarations

class Errant_OverrideGetMethodGosu extends Errant_OverrideGetMethodJava {
  override function get() : String {   //No error expected as get() in Java should not create a property
    return null
  }
}
