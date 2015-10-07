package org.gosulang.foo

class Person {

  private var _lastname : String as readonly LastName
  private var _firstname : String as readonly FirstName

  //this line produces a warning
  var x : java.util.List<int>

}