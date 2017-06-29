package gw.specContrib.types

class Errant_TypeLiteralExplicitType {

  class Temp122 {
    //Case #1
    //here type of xx0 is Metatype with supertypes as Type and IGosuClass i.e. it is a real metatype and hence BackingClass property is there.
    var mType = Temp122.Type           //inferred type is MetaType. Hence BackingClass is resolved
    var xx1 = mType.BackingClass

    //Case #2
    var literalMT: Type<Temp122> = Temp122.Type   //explicit Type Literal. Hence no BackingClass property
    var xx2 = literalMT.BackingClass      //## issuekeys: CANNOT RESOLVE SYMBOL 'BACKINGCLASS'

    //Case #3
    var mTypeList = {Temp122.Type, Temp122.Type}    //inferred type is MetaType. Hence BackingClass is resolved
    var xx3 = mTypeList.get(0).BackingClass

    //Case #4
    var literalMTypeList: List<Type<Temp122>> = {Temp122.Type}
    var xx4 = literalMTypeList.get(0)     //This is a literal metatype as inferred from the context. But OS Gosu resolved BackingClass on it. IDE parser does not. Patched until OS Gosu confirms/fixes
    var xx42 = xx4.BackingClass
  }
}