package gw.specContrib.featureLiterals.gosuMembersBinding

class Errant_FLThis {
   var field1 : String
   var _prop1 : String as Prop1

   property get Prop2() : String { return null }
   property set Prop2(str : String) {}

   construct(str : String) {
   }
   function method1() {}
   static function staticMethod1() {}

   var fieldFL11 = #field1
   var fieldFL12 = this#field1

   var propFL11 = #Prop1
   var propFL12 = this#Prop1

   var propFL21 = #Prop2
   var propFL22 = this#Prop2

   var constructFL11 = #construct(String)
   var constructFL12 = this#construct(String)      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
   var constructFL21 = #construct("mystring")
   var constructFL22 = this#construct("mystring")      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
  //IDE-1571 - issue in OS Gosu
  var constructFL31 = #construct()
   var constructFL32 = this#construct()      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO


   var methodFL11 = #method1()
   var methodFL12 = this#method1()

   var staticMethodFL11 = #staticMethod1()
   var staticMethodFL12 = this#staticMethod1()      //## issuekeys: A STATIC FEATURE OR CONSTRUCTOR MUST BE REFERENCED DIRECTLY ON THE TYPE THAT THE FEATURE BELONGS TO
 }