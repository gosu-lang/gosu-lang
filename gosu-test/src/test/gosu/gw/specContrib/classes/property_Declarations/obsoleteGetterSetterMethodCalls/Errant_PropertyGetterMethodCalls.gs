package gw.specContrib.classes.property_Declarations.obsoleteGetterSetterMethodCalls

class Errant_PropertyGetterMethodCalls {
  var _name : String as Name

  function fooName() {
    var x1 = Name
    var x2 = getName()      //## KB(JIRANEEDED-0000) issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
    Name = "vicky"
    setName("Vicky")      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
  }

  property get Age() : int { return 42 }
  property set Age(i : int) {}

  function fooAge() {
    var y1 = Age
    var y2 = getAge()      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
    Age = 42
    setAge(42)      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
  }

  var _employed : boolean as EmpStatus

  function fooEmpStatus() {
    var z1 = EmpStatus
    var z2 = getEmpStatus()     //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
    var z3 = isEmpStatus()      //## issuekeys: MSG_NO_SUCH_FUNCTION
    EmpStatus = true
    setEmpStatus(false)      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
  }

  var _small : String as small

  function fooSmall() {
    var s1 = small
    var s2 = getsmall()      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
    small = "small"
    setsmall("small")      //## issuekeys: CANNOT MAKE GETTER/SETTER METHOD TYPE CALL ON PROPERTIES. USE GOSU STYLE PROPERTY ACCESS INSTEAD
  }
}
