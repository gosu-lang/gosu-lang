/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.plugin.ij.framework.generator.JavaInterfaceFile

class GotoDeclGenericTest extends AbstractGotoDeclTest {

  function testGotoGenericJavaMethodFromGenericGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A> extends BaseA<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoGenericJavaMethodFromGenericGosuSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB<A> extends BaseA<A>{\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A> extends BaseB<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsub })
  }

  function testGotoGenericJavaMethodFromGenericGosuSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB<A> extends BaseA<A>{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<A> extends BaseB<A> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A> extends DerivedD<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoGenericJavaMethodFromGenericGosuDifferentTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<F> extends BaseA<F> {\n" +
      "  function caller( a : F ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoGenericJavaMethodFromGenericGosuDifferentTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB<B> extends BaseA<B> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<F> extends BaseB<F> {\n" +
      "  function caller( a : F ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsub })
  }

  function testGotoGenericJavaMethodFromGenericGosuDifferentTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB<B> extends BaseA<B>{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D> extends BaseB<D> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<F> extends DerivedD<F> {\n" +
      "  function caller( a : F ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoBoundedGenericJavaMethodFromGenericGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.List> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A extends java.util.List> extends BaseA<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoParameterizedGenericJavaMethodFromGenericGosu() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA<java.util.ArrayList> {\n" +
      "  function caller( a : java.util.ArrayList ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedParameterizedGenericJavaMethodFromGenericGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.List> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA<java.util.ArrayList> {\n" +
      "  function caller( a : java.util.ArrayList ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoComplexGenericJavaMethodFromGenericGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.Map<K, V>, K, V> {\n" +
      "  public void ##baseFoo( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<K, V> extends BaseA<java.util.HashMap<K, V>, K, V> {\n" +
      "  function caller( a : java.util.HashMap<K, V> ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInClassFromGenericGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void ##baseFoo( A a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends BaseA<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInClassFromGenericGosuSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void ##baseFoo( A a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB<A> extends BaseA<A>{\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends BaseB<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInClassFromGenericGosuSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void ##baseFoo( A a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB<A> extends BaseA<A>{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedD<A> extends BaseB<A> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends DerivedD<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInInterfaceFromGenericGosuSameTypeVar1() {
    var fsup = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void ##baseFoo( A a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> implements IBaseA<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInInterfaceFromGenericGosuSameTypeVar2() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void ##baseFoo( A a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB<A> implements IBaseA<A> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends BaseB<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGenericJavaMethodDeclarationInInterfaceFromGenericGosuSameTypeVar3() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void ##baseFoo( A a )\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB<A> implements IBaseA<A>{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedD<A> extends BaseB<A> {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends DerivedD<A> {\n" +
      "  function caller( a : A ) {\n" +
      "    [[baseFoo]]( a )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup1, fsup2, fsup3, fsub })
  }

//  function testGotoGenericMethodFromCallerInParameterizedClass() {
//    var fjsup1 = new JavaClassFile (
//      "package some.pkg;\n" +
//      "class BaseA<A> {\n" +
//      "}"
//    )
//    var fjsup2 = new JavaClassFile (
//      "package some.pkg;\n" +
//      "class BaseB<B> extends BaseA<B> {\n" +
//      "}"
//    )
//    var fjsup3 = new JavaClassFile (
//      "package some.pkg;\n" +
//      "class BaseC<C> extends BaseB<C> {\n" +
//      "}"
//    )
//    var fsub4 = new GosuClassFile (
//      "package some.pkg\n" +
//      "class DerivedD<D> extends BaseC<D> {\n" +
//      "}"
//    )
//    var fsub5 = new GosuClassFile (
//      "package some.pkg\n" +
//      "class DerivedE<E> extends DerivedD<E> {\n" +
//      "}"
//    )
//    var fsub6 = new GosuClassFile (
//      "package some.pkg\n" +
//      "class DerivedF<F> extends DerivedE<F> {\n" +
//      "}"
//    )
//  }

  function testGotoGenericMethodInSameGosuClass() {
    test(
      "  function ##foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }"
    )
  }

  function testGotoBoundedGenericMethodInSameGosuClass() {
    test(
      "  function foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function ##foo<T extends java.util.Collection<String>>( t : T ) {\n" +
      "  }\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n\n" +
      "  }"
    )
  }

  function testGotoRegularMethodInSameGosuClassWhenLesserGenericMatchExists() {
    test(
      "  function foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function foo<T extends java.util.Collection<String>>( t : T ) {\n" +
      "  }\n" +
      "  function ##foo( r : java.util.List<String> ) {\n" +
      "  }\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }"
    )
  }

  function testGotoGenericMethodInGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function ##foo<U>( u : U ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function ##foo<T extends java.util.Collection<String>>( t : T ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInGosuSuperClassWhenLesserGenericMatchExists() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function foo<T extends java.util.Collection<String>>( t : T ) {\n" +
      "  }\n" +
      "  function ##foo( r : java.util.List<String> ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoGenericMethodInJavaSuperClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void ##foo( U u ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInJavaSuperClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void ##foo( T t ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoRegularMethodInJavaSuperClassWhenLesserGenericMatchExists() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void foo( T t ) {\n" +
      "  }\n" +
      "  public void ##foo( java.util.List<String> r ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new java.util.ArrayList<String>() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoGenericMethodInJavaSuperClassFromGenericGosuClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void ##foo( U u ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<T> extends BaseA {\n" +
      "  function caller() {\n" +
      "    [[foo]]( new T() )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInJavaSuperClassFromBoundedGenericGosuClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void ##foo( T t ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<T extends java.util.Collection<String>> extends BaseA {\n" +
      "  function caller( t : T ) {\n" +
      "    [[foo]]( t )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInJavaSuperClassFromMoreNarrowlyBoundedGenericGosuClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void ##foo( T t ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<T extends java.util.List<String>> extends BaseA {\n" +
      "  function caller( t : T ) {\n" +
      "    [[foo]]( t )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodInJavaSuperClassFromMoreNarrowlyBoundedGenericGosuClassUsingUncheckedBoundingTypes() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection> void ##foo( T t ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<T extends java.util.List> extends BaseA {\n" +
      "  function caller( t : T ) {\n" +
      "    [[foo]]( t )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoRegularMethodInJavaSuperClassFromBoundedGenericGosuClassWhenLesserGenericMatchExists() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void foo( T t ) {\n" +
      "  }\n" +
      "  public void ##foo( java.util.List<String> r ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<T extends java.util.List<String>> extends BaseA {\n" +
      "  function caller( t : T ) {\n" +
      "    [[foo]]( t )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingGenericBlockParamInGosuSuperClassSameTypeVar() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class DerivedF<D> extends DerivedD<D> {\n" +
      "  function caller( blk( a : D ) : D ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingBoundedGenericBlockParamInGosuSuperClassSameTypeVar() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D extends Number> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class DerivedF<D extends Number> extends DerivedD<D> {\n" +
      "  function caller( blk( a : D ) : D ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingGenericBlockParamInGosuSuperClassDifferentTypeVar() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class DerivedF<F> extends DerivedD<F> {\n" +
      "  function caller( blk( a : F ) : F ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingBoundedGenericBlockParamInGosuSuperClassDifferentTypeVar() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D extends Number> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class DerivedF<F extends Number> extends DerivedD<F> {\n" +
      "  function caller( blk( a : F ) : F ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingGenericBlockParamInParameterizedGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.util.concurrent.Callable\n" +
      "class DerivedF extends DerivedD<String> {\n" +
      "  function caller( blk( a : String ) : String ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

  function testGotoMethodAcceptingBoundedGenericBlockParamInParameterizedGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD<D extends java.lang.Number> {\n" +
      "  function ##baseFoo( blk( a : D ) : D ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "uses java.lang.Integer\n" +
      "class DerivedF extends DerivedD<Integer> {\n" +
      "  function caller( blk( a : Integer ) : Integer ) {\n" +
      "    [[baseFoo]]( blk )\n" +
      "  }\n" +
      "}"
    )
    test({ fsup, fsub })
  }

}
