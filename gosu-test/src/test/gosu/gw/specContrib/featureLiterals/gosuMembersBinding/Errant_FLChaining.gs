package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLChaining {
  class Employee {
    var _name: String as Name
    var _age: int as Age

    var _boss1: Employee as Boss1
    var _boss2: Employee

    property get Boss2(): Employee {
      return _boss2
    }

    property set Boss2(boss: Employee) {
      _boss2 = boss
    }

    function empFun(): Employee {
      return null
    }
  }

  function chainedFeatureLiterals() {
    var anEmp = new Employee(){:Name = "John", :Age = 25}
    //Define FL for above using chaining
    //Unbound
    var boss1FL = Employee#Boss1
    var boss2FL = Employee#Boss2

    var boss1NameFL11 = Employee#Boss1#Name
    //If the expression qualifier (expression before '#') has FeatureReference type, then it's chained feature literal expression
    var boss1NameFL12 = Employee#Boss1.get(anEmp)#Name  //This is not chaning because type of Employee#Boss1.get(anEmp) is an Emplyee and not a FL
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


    // non-property chaining
    var invalid1: Object = Employee#Boss1#empFun()  //## issuekeys: NON-PROPERTY LITERAL CHAINING
    // IDE-2556
    var invalid2: Object = Employee#empFun()#Boss1  //## issuekeys: NON-PROPERTY LITERAL CHAINING
    var invalid3: Object = anEmp#Boss1#empFun()     //## issuekeys: NON-PROPERTY LITERAL CHAINING
    var invalid4: Object = anEmp#empFun()#Boss2     //## issuekeys: NON-PROPERTY LITERAL CHAINING


  }

  //Chaining with Access modifiers and also more tests for IDE-618
  class Engineer{

    var _boss1: Engineer as Boss1

  private var _namePrivate: String
  private var agePrivate: int

  protected var _nameProtected: String
  protected var ageProtected: int

  public var _namePublic: String
  public var agePublic: int

  internal var _nameInternal: String
  internal var ageInternal: int
}



  function chainedFeatureLiterals2() {
      var instance1 = new Engineer()
    var privateNameChaining = Engineer#Boss1#_namePrivate
    var protectedNameChaining = Engineer#Boss1#_nameProtected
    var publicNameChaining = Engineer#Boss1#_namePublic
    var internalNameChaining = Engineer#Boss1#_nameInternal

    var privateAgeChaining = Engineer#Boss1#agePrivate
    var protectedAgeChaining = Engineer#Boss1#ageProtected
    var publicAgeChaining = Engineer#Boss1#agePublic
    var internalAgeChaining = Engineer#Boss1#ageInternal

    //If the expression qualifier (expression before '#') has FeatureReference type, then it's chained feature literal expression
    var boss1NameFLPrivate = Engineer#Boss1.get(instance1)#_namePrivate  //This is not chaning because type of Engineer#Boss1.get(instance1) is an Engineer and not a FL
    var boss1NameFLProtected = Engineer#Boss1.get(instance1)#_nameProtected
    var boss1NameFLPublic = Engineer#Boss1.get(instance1)#_namePublic
    var boss1NameFLInternal = Engineer#Boss1.get(instance1)#_nameInternal

    Engineer#Boss1#_namePrivate.set(instance1, "Jack Bauer")
    Engineer#Boss1#_nameProtected.set(instance1, "Jack Bauer")
    Engineer#Boss1#_namePublic.set(instance1, "Jack Bauer")
    Engineer#Boss1#_nameInternal.set(instance1, "Jack Bauer")
  }

}