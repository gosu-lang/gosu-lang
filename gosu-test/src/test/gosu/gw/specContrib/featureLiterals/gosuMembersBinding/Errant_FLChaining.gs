package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLChaining {
  class Employee {
    var _name : String as Name
    var _age : int as Age

    var _boss1 : Employee as Boss1
    var _boss2 : Employee
    property get Boss2() : Employee {
      return _boss2
    }
    property set Boss2(boss : Employee) {
      _boss2 = boss
    }

    function empFun() {}
  }

  function chainFeatureLiterals () {
    var anEmp = new Employee(){:Name = "John", :Age = 25}
    //Define FL for above using chaining
    //Unbound
    var boss1FL = Employee#Boss1
    var boss2FL = Employee#Boss2

    var boss1NameFL11 = Employee#Boss1#Name
    var boss1NameFL12 = Employee#Boss1.get(anEmp)#Name
    Employee#Boss1#Name.set(anEmp, "Jack Bauer")
    var boss1AgeFL11 = Employee#Boss1#Age
    var boss1AgeFL12 = Employee#Boss1.get(anEmp)#Age
    Employee#Boss1#Age.set(anEmp, 42)

    var boss2NameFL11 = Employee#Boss2#Name
    var boss2NameFL12 = Employee#Boss2.get(anEmp)#Name
    Employee#Boss2#Name.set(anEmp, "Jack Bauer")
    var boss2AgeFL11 = Employee#Boss2#Age
    var boss2AgeFL12 = Employee#Boss2.get(anEmp)#Age
    Employee#Boss2#Age.set(anEmp, 42)


    //Bound
    var boss1FLBound = anEmp#Boss1
    var boss2FLBound = anEmp#Boss2

    var boss1NameFLBound11 = anEmp#Boss1#Name
    var boss1NameFLBound12 = anEmp#Boss1.get()#Name
    anEmp#Boss1#Name.set("Jack Bauer")
    var boss1AgeFLBound11 = anEmp#Boss1#Age
    var boss1AgeFLBound12 = anEmp#Boss1.get()#Age
    anEmp#Boss1#Age.set(42)

    var boss2NameFLBound11 = anEmp#Boss2#Name
    var boss2NameFLBound12 = anEmp#Boss2.get()#Name
    anEmp#Boss2#Name.set("Jack Bauer")
    var boss2AgeFLBound11 = anEmp#Boss2#Age
    var boss2AgeFLBound12 = anEmp#Boss2.get()#Age
    anEmp#Boss2#Age.set(42)
  }
}