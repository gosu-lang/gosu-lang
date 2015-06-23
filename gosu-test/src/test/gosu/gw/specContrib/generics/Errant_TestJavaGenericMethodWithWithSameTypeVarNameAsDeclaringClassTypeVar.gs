package gw.specContrib.generics

class Errant_TestJavaGenericMethodWithWithSameTypeVarNameAsDeclaringClassTypeVar {
  // IDE-2273
  function testMe() {
    // Infer builder1()'s type var from the return type
    var builderOfString: JavaClass2.Builder<String> = JavaClass2.builder1()
    var jc1: JavaClass2<String> = builderOfString.build()

    // verify builder1()'s type var is Object
    var builderOfObject = JavaClass2.builder1()
    var jc2: JavaClass2<String> = builderOfObject.build()  //## issuekeys: MSG_TYPE_MISMATCH

    // Ensure JavaClass2's T does not clobber builder2's T i.e., builder2() call should not resolve T, should be Object
    var builderOfObject2 = JavaClass2<String>.builder2();
    var jc3: JavaClass2<String> = builderOfObject2.build()  //## issuekeys: MSG_TYPE_MISMATCH
  }
}
