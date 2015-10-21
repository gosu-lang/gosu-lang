package org.gosulang.foo

enhancement PersonEnhancement : Person {

  property get FullName() : String {
    return this.FirstName + ' ' + this.LastName
  }

}