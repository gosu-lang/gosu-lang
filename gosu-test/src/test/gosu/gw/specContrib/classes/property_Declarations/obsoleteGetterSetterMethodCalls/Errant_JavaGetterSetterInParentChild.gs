package gw.specContrib.classes.property_Declarations.obsoleteGetterSetterMethodCalls

class Errant_JavaGetterSetterInParentChild {
  function foo() {
    var xx = new JavaChild()
    print(xx.Name)
    xx.Name = "sdf"
    print(xx.Name2)      //## issuekeys: CANNOT RESOLVE SYMBOL 'NAME2'
    xx.Name2 = "sdf"      //## issuekeys: CANNOT RESOLVE SYMBOL 'NAME2'
    print(xx.name2)      //## issuekeys: CANNOT RESOLVE SYMBOL 'NAME2'
    xx.name2 = "sdf"      //## issuekeys: CANNOT RESOLVE SYMBOL 'NAME2'
    //GOSU-47
    print(xx.Name3)      //## issuekeys: Property Name3 not readable - error in OS Gosu only - 1.14.7
    xx.Name3 = "sdf"
    //GOSU-47
    print(xx.Name4)       //## issuekeys: Property Name4 not readable - error in OS Gosu only - 1.14.7
    xx.Name4 = "sdf"      //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND,MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }
}
