package gw.specContrib.classes.property_Declarations

class Errant_GosuSetterOverridingJavaSetter extends JavaClass3 {

  property set Text(s : String ) {}      //## issuekeys: PROPERTY SETTER DOES NOT OVERRIDE SETTER FROM ITS SUPERCLASS. ADD A GETTER TO THE SUPERCLASS OR CONVERT THE PROPERTY TO A METHOD

  property set Text2(s : String ) {}

  property set OtherText(s : String ) {}

}