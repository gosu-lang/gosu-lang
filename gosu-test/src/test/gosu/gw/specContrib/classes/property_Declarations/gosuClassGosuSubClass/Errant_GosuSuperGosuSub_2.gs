package gw.specContrib.classes.property_Declarations.gosuClassGosuSubClass

uses java.lang.Integer

class Errant_GosuSuperGosuSub_2 {
  //Case 2 - both super class and subclass have properties
  class Case2Super {
    property get NormalProperty() : String { return null }
    property set NormalProperty(s: String) {}

    property get differntCaseProperty() : String { return null }
    property set differntCaseProperty(s: String) {}

    property get DontAgreeOnTypeInSubClass() : String { return null }
    property set DontAgreeOnTypeInSubClass(s: String) {}

    property get GetterInSuperSetterInSub() : String { return null }
    property set GetterInSubSetterInSuper(s : String) {}
  }
  class Case2Sub extends Case2Super {
    property get NormalProperty() : String { return null }
    property set NormalProperty(s: String) {}

    //IDE-1812 - Parser issue - should not show error
    property get DifferntCaseProperty() : Integer { return null }
    property set DifferntCaseProperty(s: Integer) {}

    property get DontAgreeOnTypeInSubClass() : Integer { return null }            //## issuekeys: 'GETDONTAGREEONTYPEINSUBCLASS()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUSUBCLASS.ERRANT_GOSUSUPERGOSUSUB_2.CASE2SUB' CLASHES WITH 'GETDONTAGREEONTYPEINSUBCLASS()' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUSUBCLASS.ERRANT_GOSUSUPERGOSUSUB_2.CASE2SUPER'; ATTEMPTING TO USE INCOMPATIBLE RETURN TYPE
    property set DontAgreeOnTypeInSubClass(s: Integer) {}            //## issuekeys: PROPERTY TYPE IS NOT COMPATIBLE WITH TYPE OF PROPERTY 'DONTAGREEONTYPEINSUBCLASS' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUSUBCLASS.ERRANT_GOSUSUPERGOSUSUB_2.CASE2SUPER' THAT IS BEING OVERRIDDEN

    property set GetterInSuperSetterInSub(s : String) {}
    property get GetterInSubSetterInSuper() : String { return null }
  }
}