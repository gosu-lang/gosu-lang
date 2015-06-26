package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction

uses java.lang.Integer
uses java.util.ArrayList
uses java.util.HashMap

class Errant_GosuTestClass2 {

  //Gosu class implementing JavaInterface
  class ClassA1 implements JavaInterface2 {
    function test() {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({'c', 2b, 3s})
      var x1c : List = foo({1,2,"xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2 : List = super[JavaInterface2].foo(new ArrayList<Integer>())
      var x2a : List = super[JavaInterface2].foo({1, 2, 3})
      var x2b : List = super[JavaInterface2].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[JavaInterface2].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[JavaInterface2].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[JavaInterface2].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
    }
  }

  class ClassA2 implements JavaInterface2 {
    override function foo(arrayList : ArrayList<Integer>) : List { return null}
    function test() {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.CLASSA2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[JavaInterface2].foo(new ArrayList<Integer>())
      var x2a : List = super[JavaInterface2].foo({1, 2, 3})
      var x2b : List = super[JavaInterface2].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[JavaInterface2].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.CLASSA2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[JavaInterface2].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.CLASSA2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[JavaInterface2].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
    }
  }

  class ClassA3 implements JavaInterface2 {
    function foo() : HashMap { return null}
    function test() {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x0a : HashMap = foo()
      var x0b : HashMap = foo({1, 2, 3})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.HASHMAP'
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[JavaInterface2].foo(new ArrayList<Integer>())
      var x2a : List = super[JavaInterface2].foo({1, 2, 3})
      var x2b : List = super[JavaInterface2].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[JavaInterface2].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[JavaInterface2].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[JavaInterface2].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
    }
  }

  class ClassA4 implements JavaInterface2 {
    function foo(hashMap : HashMap<Integer, Integer>) : HashMap { return null}
    function test() {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x0a : HashMap = foo({1->2})
      var x0b : HashMap = foo({1, 2, 3})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.HASHMAP'
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[JavaInterface2].foo(new ArrayList<Integer>())
      var x2a : List = super[JavaInterface2].foo({1, 2, 3})
      var x2b : List = super[JavaInterface2].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[JavaInterface2].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[JavaInterface2].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[JavaInterface2].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x7 : HashMap = foo(new ArrayList<Integer>())      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.UTIL.HASHMAP'
    }
  }


  //Gosu Interface extending Java Interface
  interface GosuInterface2a extends JavaInterface2 {
    function bar() {}
  }
  class TestClassA1 implements GosuInterface2a {
    function test () {
      //IDE-2594 - OG Gosu shows error "No function defined for foo" - BAD
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[GosuInterface2a].foo(new ArrayList<Integer>())
      var x2a : List = super[GosuInterface2a].foo({1, 2, 3})
      var x2b : List = super[GosuInterface2a].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[GosuInterface2a].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[GosuInterface2a].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[GosuInterface2a].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.JAVAINTERFACE2' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'

    }
  }

  interface GosuInterface2b extends JavaInterface2 {
    override function foo(arrayList : ArrayList<Integer>) : List { return null }
    function bar(){}
  }
  class TestClassA2 implements GosuInterface2b {
    function test () {
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[GosuInterface2b].foo(new ArrayList<Integer>())
      var x2a : List = super[GosuInterface2b].foo({1, 2, 3})
      var x2b : List = super[GosuInterface2b].foo({1, 2, "xyz"})      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[GosuInterface2b].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[GosuInterface2b].foo(new ArrayList())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[GosuInterface2b].foo(new ArrayList<String>())      //## issuekeys: 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.INTEGER>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.JAVAINTERACTION.TOBEPUSHED.ERRANT_GOSUTESTCLASS2.GOSUINTERFACE2B' CANNOT BE APPLIED TO '(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'

    }
  }
  interface GosuInterface2c extends JavaInterface2 {
    function foo(s: String): String { return null}
  }
  class TestClassA3 implements GosuInterface2c {
//    function foo(s:String) {}
    function test () {
      var x0a : String = foo("mystring")
      var x0b : String = foo({"abc"})      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x0c : String = foo({1, 2, 3})      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'JAVA.UTIL.LIST<JAVA.LANG.OBJECT>', REQUIRED: 'JAVA.LANG.STRING'
      var x1 : List = foo(new ArrayList<Integer>())
      var x1a : List = foo({1,2,3})
      var x1b : List = foo({1,2,"xyz"})      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x1c : List = foo({'c', 2b, 3s})
      var x2 : List = super[GosuInterface2c].foo(new ArrayList<Integer>())
      var x2a : List = super[GosuInterface2c].foo({1, 2, 3})
      var x2b : List = super[GosuInterface2c].foo({1, 2, "xyz"})      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<JAVA.IO.SERIALIZABLE & JAVA.LANG.COMPARABLE<? EXTENDS JAVA.LANG.COMPARABLE<?>>>>)'
      var x2c : List = super[GosuInterface2c].foo({'c', 2b, 3s})
      var x3 : List = foo(new ArrayList())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST)'
      var x4 : List = super[GosuInterface2c].foo(new ArrayList())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST)'
      var x5 : List = foo(new ArrayList<String>())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
      var x6 : List = super[GosuInterface2c].foo(new ArrayList<String>())      //## issuekeys: CANNOT RESOLVE METHOD 'FOO(JAVA.UTIL.ARRAYLIST<JAVA.LANG.STRING>)'
    }
  }
}