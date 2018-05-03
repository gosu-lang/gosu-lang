package gw.specContrib.classes.property_Declarations

class Errant_GetterSetterInSuperSubJava {
  function foo() {
    var javaSub : JavaSub
    var age = javaSub.Age
    var age2 = javaSub.getAge()
    var name = javaSub.Name
    var name2 = javaSub.getName()

    javaSub.Age = 42
    javaSub.setAge(42)

    javaSub.Name = "Mick"
    javaSub.setName("Jack")
  }

  function bar() {
    var javaSub : JavaSuper
    var age = javaSub.Age
    var age2 = javaSub.getAge()
    var name = javaSub.Name      //## issuekeys: CANNOT RESOLVE SYMBOL 'NAME'
    var name2 = javaSub.getName()      //## issuekeys: CANNOT RESOLVE METHOD 'GETNAME()'

    javaSub.Age = 42      //## issuekeys: PROPERTY 'AGE' IS NOT WRITABLE
    javaSub.setAge(42)      //## issuekeys: CANNOT RESOLVE METHOD 'SETAGE(INT)'

    javaSub.Name = "Mick"
    javaSub.setName("Jack")
  }

}