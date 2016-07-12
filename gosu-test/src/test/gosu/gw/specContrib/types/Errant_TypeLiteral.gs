package gw.specContrib.types

class Errant_TypeLiteral {
  var int1 = Integer
  var int2 = Integer.Type
  var int3 = Integer.TYPE
  var int4 : Type<Integer> = Integer
  var int5 : Type<Integer> = Integer.Type
  var int6 : Type<Integer> = Integer.TYPE

  var int7 = Integer.parseInt("0")
  var int8 = Integer.Type.parseInt("0")                         //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
  var int9 = int1.parseInt("0")
  var int10 = int2.parseInt("0")                                //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
  var int11 = int3.parseInt("0")                                //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
  var int12 = int4.parseInt("0")
  var int13 = int5.parseInt("0")
  var int14 = int6.parseInt("0")

  var int15 = int1.MIN_VALUE
  var int16 = int2.MIN_VALUE                                    //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var int17 = (int2 as Type<Integer>).MIN_VALUE                 //## issuekeys: MSG_UNNECESSARY_COERCION
  var int18 = int3.MIN_VALUE                                    //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var int19 = (int3 as Type<Integer>).MIN_VALUE
  var int20 = int4.MIN_VALUE
  var int21 = int5.MIN_VALUE
  var int22 = int6.MIN_VALUE

  var int23 = Integer.MIN_VALUE
  var int24 = Integer.Type.MIN_VALUE                           //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var int25 = Integer.TYPE.MIN_VALUE                           //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

  var int26 = (Integer.Type as Type<Integer>).MAX_VALUE        //## issuekeys: MSG_UNNECESSARY_COERCION
  var int27 = (Integer.TYPE as Type<Integer>).MIN_VALUE

  var int28 = (Integer.MIN_VALUE)
  var int29 = (Integer.Type.MIN_VALUE)                         //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var int30 = ((Integer as Type<Integer>).MIN_VALUE)           //## issuekeys: MSG_UNNECESSARY_COERCION
  var int31 = ((Integer.Type as Type<Integer>).MIN_VALUE)      //## issuekeys: MSG_UNNECESSARY_COERCION

  var boolean1 = (Integer.Dynamic)                             //## issuekeys: MSG_DEPRECATED_MEMBER
  var boolean2 = (Integer.Type.LiteralMetaType.Dynamic)        
  var boolean3 = (Integer.Type.Dynamic)
  var boolean4 = ((Integer as Type<Integer>).Dynamic)          //## issuekeys: MSG_DEPRECATED_MEMBER,MSG_UNNECESSARY_COERCION
  var boolean5 = ((Integer.Type as Type<Integer>).Dynamic)     //## issuekeys: MSG_DEPRECATED_MEMBER,MSG_UNNECESSARY_COERCION


  var a = (Integer.BackingClass)                               //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var a2 = (Integer.Type.LiteralMetaType.BackingClass)         //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var b = (Integer.Type.BackingClass)
  var c = ((Integer as Type<Integer>).BackingClass)            //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var d = ((Integer.Type as Type<Integer>).BackingClass)       //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

  var e1 = int1.BackingClass                                   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var e2 = int2.BackingClass
  var e3 = int3.BackingClass                                   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var e4 = int4.BackingClass                                   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var e5 = int5.BackingClass                                   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  var e6 = int6.BackingClass                                   //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

  function literalTest() {
    var local1 = Integer
    var local2 = Integer.Type
    var local3 = Integer.TYPE
    var local4 : Type<Integer> = Integer
    var local5 : Type<Integer> = Integer.Type
    var local6 : Type<Integer> = Integer.TYPE

    // static method, requires type literal
    print(Integer.parseInt("0"))
    print(Integer.Type.parseInt("0"))                          //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
    print(local1.parseInt("0"))
    print(local2.parseInt("0"))                                //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
    print(local3.parseInt("0"))                                //## issuekeys: MSG_NO_FUNCTION_DESCRIPTOR_FOUND
    print(local4.parseInt("0"))
    print(local5.parseInt("0"))
    print(local6.parseInt("0"))

    print(local1.MIN_VALUE)
    print(local2.MIN_VALUE)                                    //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    print((local2 as Type<Integer>).MIN_VALUE)                 //## issuekeys: MSG_UNNECESSARY_COERCION
    print(local3.MIN_VALUE)                                    //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    print((local3 as Type<Integer>).MIN_VALUE)
    print(local4.MIN_VALUE)
    print(local5.MIN_VALUE)
    print(local6.MIN_VALUE)

    print(Integer.MIN_VALUE)
    print(Integer.Type.MIN_VALUE)                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    print(Integer.TYPE.MIN_VALUE)                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

    print((Integer.Type as Type<Integer>).MAX_VALUE)           //## issuekeys: MSG_UNNECESSARY_COERCION
    print((Integer.TYPE as Type<Integer>).MIN_VALUE)

    print(Integer.MIN_VALUE)
    print(Integer.Type.MIN_VALUE)                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    print((Integer as Type<Integer>).MIN_VALUE)                //## issuekeys: MSG_UNNECESSARY_COERCION
    print((Integer.Type as Type<Integer>).MIN_VALUE)           //## issuekeys: MSG_UNNECESSARY_COERCION

    print(Integer.Dynamic)                                     //## issuekeys: MSG_DEPRECATED_MEMBER
    print(Integer.Type.LiteralMetaType.Dynamic)            
    print(Integer.Type.Dynamic)
    print((Integer as Type<Integer>).Dynamic)                  //## issuekeys: MSG_DEPRECATED_MEMBER,MSG_UNNECESSARY_COERCION
    print((Integer.Type as Type<Integer>).Dynamic)             //## issuekeys: MSG_DEPRECATED_MEMBER,MSG_UNNECESSARY_COERCION

    var _a = (Integer.BackingClass)                            //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _a2 = (Integer.Type.LiteralMetaType.BackingClass)      //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _b = (Integer.Type.BackingClass)
    var _c = ((Integer as Type<Integer>).BackingClass)         //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _d = ((Integer.Type as Type<Integer>).BackingClass)    //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND

    var _e1 = local1.BackingClass                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _e2 = local2.BackingClass
    var _e3 = local3.BackingClass                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _e4 = local4.BackingClass                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _e5 = local5.BackingClass                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var _e6 = local6.BackingClass                              //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }
}
