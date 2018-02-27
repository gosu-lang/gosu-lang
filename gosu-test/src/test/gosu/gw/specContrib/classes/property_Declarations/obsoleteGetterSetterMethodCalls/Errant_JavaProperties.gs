package gw.specContrib.classes.property_Declarations.obsoleteGetterSetterMethodCalls

/**
 * Created by vdahuja on 7/25/17.
 */
class Errant_JavaProperties {
  function foo() {
    var xx = new JavaOne()

    var ee1 = xx.Employee
    var ea1 = xx.getEmployee()
    var eb1 = xx.isEmployee()
    xx.Employee = true
    xx.setEmployee(true)

    //No property generated
    var ee2 = xx.Employee2      //## issuekeys: CANNOT RESOLVE SYMBOL 'EMPLOYEE2'
    var ea2 = xx.getemployee2()
    var eb2 = xx.isemployee2()
    xx.Employee2 = true      //## issuekeys: CANNOT RESOLVE SYMBOL 'EMPLOYEE2'
    xx.setemployee2(true)

    //property generated but not both get/is for boolean
    var ee3 = xx.Employee3
    var ea3 = xx.get_Employee3()      //## issuekeys: CANNOT RESOLVE METHOD 'GET_EMPLOYEE3()'
    var eb3 = xx.is_Employee3()
    xx.Employee3 = true
    xx.set_Employee3(false)


    var ee33 = xx.Employee33
    var ea33 = xx.get_Employee33()
    var eb33 = xx.is_Employee33()      //## issuekeys: CANNOT RESOLVE METHOD 'IS_EMPLOYEE33()'
    xx.Employee33 = true
    xx.set_Employee33(true)

    //property generated with _ and small letter.. but not both is/get
    var ee4 = xx.Employee4            //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var ea4 = xx.get_employee4()      //## issuekeys: CANNOT RESOLVE METHOD 'GET_EMPLOYEE4()'
    var eb4 = xx.is_employee4()
    xx.Employee4 = true            //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND,MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    xx.set_employee4(false)
  }
}
