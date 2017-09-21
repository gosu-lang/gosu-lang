package gw.specContrib.classes.property_Declarations


var obj = new JavaClassWithOverloadedPropertyMethod()
obj.isGood() // tests overloaded property method
var result: String = obj.setGood( false ) // tests setter method can have non-void return
obj.Good = true // tests setter with non-void still accessible via writable property syntax