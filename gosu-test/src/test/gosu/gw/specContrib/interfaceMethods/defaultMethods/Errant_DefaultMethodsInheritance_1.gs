package gw.specContrib.interfaceMethods.defaultMethods

class Errant_DefaultMethodsInheritance_1 {
  interface InterfaceA {
    function defaultFunctionFirst() {
    }

    function defaultFunctionSecond() {
    }

    function defaultFunctionThird() {
    }
  }

  interface InterfaceB extends InterfaceA {
    //Not mentioning the first default method

    //Redeclaring the second default method
    override function defaultFunctionSecond()

    //Redefining the third default method
    override function defaultFunctionThird() {
      super[InterfaceA].defaultFunctionThird()
    }
  }

  //need to implement defaultFunctionSecond() since it is redeclared in InterfaceB and is abstract now
  class MyClass1 implements InterfaceB {      //## issuekeys: CLASS 'MYCLASS1' MUST EITHER BE DECLARED ABSTRACT OR IMPLEMENT ABSTRACT METHOD 'DEFAULTFUNCTIONSECOND()' IN 'INTERFACEB'
  }

  class MyClass2 implements InterfaceB {
    //Need to implement second default method as it was redeclared in InterfaceB
    override function defaultFunctionSecond() {
      super[InterfaceB].defaultFunctionThird()
    }

    function test() {
      this.defaultFunctionSecond()
      this.defaultFunctionFirst()
      this.defaultFunctionThird()
    }
  }

  //3 level inheritance
  interface XI {
    function hello() {
    }
  }

  interface YI extends XI {

    override function hello() {
      super[XI].hello()

      //IDE-2580 - OS Gosu Issue
      var x = super[XI]      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      //IDE-2587
      var y = super      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      super      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      super[XI]      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
    }
  }

  interface ZI extends YI {
    override function hello() {
      super[YI].hello()
      super[XI].hello()      //## issuekeys: 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSINHERITANCE_1.XI' IS NOT A DIRECT SUPERTYPE
      super[42].hello()      //## issuekeys: UNSPECIFIED SUPER REFERENCE IS NOT ALLOWED IN EXTENSION METHOD
      super["foo"].hello()      //## issuekeys: UNSPECIFIED SUPER REFERENCE IS NOT ALLOWED IN EXTENSION METHOD
      super[XI]      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      var x = super      //## issuekeys: '.' EXPECTED AFTER 'SUPER'
      var y = super[XI]      //## issuekeys: '.' EXPECTED AFTER 'SUPER'

    }
  }


  interface MySuperType {
    function foo() {
    }
  }

  class MyClass implements MySuperType {
    function test() {
      super[MySuperType].foo()

      var a = MySuperType
      super[a].foo()      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'TYPE<GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ERRANT_DEFAULTMETHODSINHERITANCE_1.MYSUPERTYPE>', REQUIRED: 'JAVA.LANG.STRING'
    }
  }
}