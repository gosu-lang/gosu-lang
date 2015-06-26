package gw.specContrib.featureLiterals.gosuMembersBinding.enhancements

class FLTestClass {

  //bind to class
  function testBind() {

    //bind property
    var p111 = FLClass#EnhProperty
    //bind method
    var m111 = FLClass#enhMethod()
    var m112 = FLClass#enhMethod(String)
    var m113 = FLClass#enhMethod("mystring")

    //invoke
    var instance1 : FLClass

    //property
    var x111 = p111.get(instance1)
    p111.set(instance1, "mystring")

    //method
    var y111 = m111.invoke(instance1, "method string")
    var y112 = m112.invoke(instance1, "method string")
    var y113 = m113.invoke(instance1)
  }

  function testBindToInstance() {
    var instance1 : FLClass
    //bind property
    var p211 = instance1#EnhProperty
    //bind method
    var m211 = instance1#enhMethod()
    var m212 = instance1#enhMethod(String)
    var m213 = instance1#enhMethod("mystring")


    //invoke
    //property
    var x211 = p211.get()
    p211.set("mystring")
    //invoke method
    var y2111 = m211.invoke("mystring")
    var y2112 = m212.invoke("mystring")
    var y2113 = m213.invoke()
  }
}