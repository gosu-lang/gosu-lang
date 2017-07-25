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
    xx.Name4 = "sdf"
  }

  //todo vd : separate tests for Gosu 1.13.x. Following tests are for 1.13.x
  /*
    function foo() {
    var xx = new JavaChild()
    print(xx.Name)
    xx.Name = "sdf"
    print(xx.Name2)
    xx.Name2 = "sdf"      //## put issue key here: PROPERTY 'NAME2' IS NOT WRITABLE
    print(xx.name2)      //## put issue key here: CANNOT RESOLVE SYMBOL 'NAME2'
    xx.name2 = "sdf"      //## put issue key here: CANNOT RESOLVE SYMBOL 'NAME2'
    print(xx.Name3)
    xx.Name3 = "sdf"      //## put issue key here: PROPERTY 'NAME3' IS NOT WRITABLE
    print(xx.Name4)
    xx.Name4 = "sdf"      //## put issue key here: PROPERTY 'NAME4' IS NOT WRITABLE
   }
   */
}