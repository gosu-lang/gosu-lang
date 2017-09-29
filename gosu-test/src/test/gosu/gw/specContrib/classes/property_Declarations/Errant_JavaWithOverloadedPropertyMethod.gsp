package gw.specContrib.classes.property_Declarations


var obj = new JavaClassWithOverloadedPropertyMethod()
obj.isGood() // tests overloaded property method
print(obj.Good) // same as above using Gosu property access
var result: String = obj.setGood( false ) // tests setter method can have non-void return

// impossible to use property access on a property method with non-void return type 
obj.Good = true //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE
