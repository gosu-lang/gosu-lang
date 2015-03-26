package gw.specContrib.classes.property_Declarations.gosuClassGosuSubClass

class Errant_GosuSuperGosuSub_1 {
  class Case1Super {
    //Fields are in super class and properties with same name in subclass
    var myProp1: String as MyProp1
    var normalPropertyInSub: String
    var sameCaseSmallInSub: String
    var SameCaseCapitalInSub: String

    property get MyProp2(): String { return null }

    //Properties in super class and same name fields in subclass
    property get NormalPropertyInSuper(): String { return null}

    property set NormalPropertyInSuper(s: String) { }

    property get sameCaseSmallInSuper(): String {return null}

    property set sameCaseSmallInSuper(s: String) {}

    property get SameCaseCapitalInSuper(): String {return null}

    property set SameCaseCapitalInSuper(s: String) {}

    //Just the property setter in super class and field in the sub class
    property set JustTheSetterInSuper(s: String) { }

    //Field is in super class and only property setter in subclass
    var JustTheSetterInSub: String
  }

  class Case1sub extends Case1Super {
    override property get MyProp1(): String {return null}

    property get NormalPropertyInSub(): String {return null}

    property set NormalPropertyInSub(s: String) {}

    property get sameCaseSmallInSub(): String {return null}      //## issuekeys: VARIABLE 'SAMECASESMALLINSUB' IS ALREADY DEFINED IN THE SCOPE

    //"Discrepancy in compiler and IDE error reporting - Not a real Issue"
    property set sameCaseSmallInSub(s: String) {}

    property get SameCaseCapitalInSub(): String {return null}      //## issuekeys: VARIABLE 'SAMECASECAPITALINSUB' IS ALREADY DEFINED IN THE SCOPE

    //"Discrepancy in compiler and IDE error reporting - Not a real Issue"
    property set SameCaseCapitalInSub(s: String) { }

    var normalPropertyInSuper: String
    var sameCaseSmallInSuper: String      //## issuekeys: VARIABLE 'SAMECASESMALLINSUPER' IS ALREADY DEFINED IN THE SCOPE
    var SameCaseCapitalInSuper: String      //## issuekeys: VARIABLE 'SAMECASECAPITALINSUPER' IS ALREADY DEFINED IN THE SCOPE

    var JustTheSetterInSuper: String      //## issuekeys: VARIABLE 'JUSTTHESETTERINSUPER' IS ALREADY DEFINED IN THE SCOPE

    property set JustTheSetterInSub(s: String) {}      //## issuekeys: VARIABLE 'JUSTTHESETTERINSUB' IS ALREADY DEFINED IN THE SCOPE
  }

}