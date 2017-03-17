package gw.specContrib.generics

uses java.lang.Class
uses java.lang.Enum

class GosuReferencesRecursiveJavaTypeVar {

 function getMeBack(value : Enum) : Enum {
   var en = get(value.DeclaringClass)
   if (en != null) {
     return en
   }
   return null
 }

 reified function get<T extends Enum>(enumType : Class<T>) : T {
   return MuhEnum.HI as T
 }
}