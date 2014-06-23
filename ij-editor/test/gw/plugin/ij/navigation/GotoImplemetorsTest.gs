/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses com.intellij.psi.PsiElement
uses com.intellij.psi.PsiMethod
uses com.intellij.psi.search.searches.OverridingMethodsSearch
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.plugin.ij.framework.generator.JavaInterfaceFile

class GotoImplemetorsTest extends GosuUpDownMethodTest {

  function testGotoGosuMethodFromJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void [[baseFoo]]( String a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA {\n" +
      " function ##baseFoo( a : String ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoGosuMethodFromJavaSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void [[baseFoo]]( String a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB extends BaseA{\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseB {\n" +
      " function ##baseFoo( a : String ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoGosuMethodFromJavaSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void [[baseFoo]]( String a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseB extends BaseA{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseB {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends DerivedD {\n" +
      " function ##baseFoo( a : String ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInClassFromJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void [[baseFoo]]( String a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends BaseA {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInClassFromJavaSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void [[baseFoo]]( String a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB extends BaseA{\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends BaseB {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInClassFromJavaSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void [[baseFoo]]( String a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB extends BaseA{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedD extends BaseB {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends DerivedD {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInInterfaceFromJavaSameTypeVar1() {
    var fsup = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void [[baseFoo]]( String a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF implements IBaseA {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInInterfaceFromJavaSameTypeVar2() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void [[baseFoo]]( String a );\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB implements IBaseA {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends BaseB {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGosuMethodDeclarationInInterfaceFromJavaSameTypeVar3() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void [[baseFoo]]( String a )\n" +
      "}"
    )
    var fsup2 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseB implements IBaseA{\n" +
      "}"
    )
    var fsup3 = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedD extends BaseB {\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends DerivedD {\n" +
      " function ##baseFoo( a : String)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoMethodFromGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function [[baseFoo]]( u : java.util.Collection<String> ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      " function ##baseFoo( a : java.util.Collection<String>)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  //generic

  function testGotoGenericGosuMethodFromGenericJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A> extends BaseA<A> {\n" +
      " function ##baseFoo( a : A ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoGenericGosuMethodFromGenericJavaSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
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
      " function ##baseFoo( a : A ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoGenericGosuMethodFromGenericJavaSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
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
      " function ##baseFoo( a : A ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoGenericGosuMethodFromGenericJavaDifferentTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<F> extends BaseA<F> {\n" +
      " function ##baseFoo( a : F ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoGenericGosuMethodFromGenericJavaDifferentTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
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
      " function ##baseFoo( a : F ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoGenericGosuMethodFromGenericJavaDifferentTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
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
      " function ##baseFoo( a : F ) {}\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoBoundedGenericGosuMethodFromGenericJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.List> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<A extends java.util.List> extends BaseA<A> {\n" +
      " function ##baseFoo( a : A ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoParameterizedGenericGosuMethodFromGenericJava() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA<java.util.ArrayList> {\n" +
      " function ##baseFoo( a : java.util.ArrayList ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoBoundedParameterizedGenericGosuMethodFromGenericJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.List> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA<java.util.ArrayList> {\n" +
      " function ##baseFoo( a : java.util.ArrayList ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoComplexGenericGosuMethodFromGenericJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA<A extends java.util.Map<K, V>, K, V> {\n" +
      "  public void [[baseFoo]]( A a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF<K, V> extends BaseA<java.util.HashMap<K, V>, K, V> {\n" +
      " function ##baseFoo( a : java.util.HashMap<K, V> ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInClassFromGenericJavaSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void [[baseFoo]]( A a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> extends BaseA<A> {\n" +
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInClassFromGenericJavaSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void [[baseFoo]]( A a );\n" +
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
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInClassFromGenericJavaSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA<A> {\n" +
      "  public abstract void [[baseFoo]]( A a );\n" +
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
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInInterfaceFromGenericJavaSameTypeVar1() {
    var fsup = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void [[baseFoo]]( A a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF<A> implements IBaseA<A> {\n" +
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInInterfaceFromGenericJavaSameTypeVar2() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void [[baseFoo]]( A a );\n" +
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
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractGenericGosuMethodDeclarationInInterfaceFromGenericJavaSameTypeVar3() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA<A> {\n" +
      "  public void [[baseFoo]]( A a )\n" +
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
      " function ##baseFoo( a : A)\n" +
      "}"
    )
    testDown({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoGenericMethodFromGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function [[baseFoo]]<U>( u : U ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      " function ##baseFoo<String>( a : String)\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodFromGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function foo<U>( u : U ) {\n" +
      "  }\n" +
      "  function [[foo]]<T extends java.util.Collection<String>>( t : T ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function ##foo<T extends java.util.Collection<String>>( t : T ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoBoundedGenericMethodFromJavaSuperClass() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void [[foo]]( T t ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function ##foo<T extends java.util.Collection<String>>( t : T ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testGotoRegularMethodFromJavaSuperClassWhenLesserGenericMatchExists() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public <U> void foo( U u ) {\n" +
      "  }\n" +
      "  public <T extends java.util.Collection<String>> void foo( T t ) {\n" +
      "  }\n" +
      "  public void [[foo]]( java.util.List<String> r ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      "  function ##foo( t : java.util.List<String> ) {}\n" +
      "}"
    )
    testDown({ fsup, fsub })
  }

  function testDown(resources: GosuTestingResource[]) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var caret = markers.getCaret( MarkerType.CARET2)
    var ranges = markers.getRanges()
    assertEquals("Range marker is not defined.", 1, ranges.size())

    var expectedTarget = findNamedElement(caret.File.findElementAt(caret.offset));

    for (offset in ranges[0].StartOffset..ranges[0].EndOffset) {
      var source = findNamedElement(ranges[0].File.findElementAt(offset));
      var realTarget = gotoImplementorsMethod(source as PsiMethod)
      assertNotNull("Navigation failed with ${ranges.first()} at ${offset}", realTarget)
      assertEquals(expectedTarget, realTarget)
    }
  }

  function gotoImplementorsMethod(m: PsiMethod) : PsiElement {
    var q = OverridingMethodsSearch.search(m)
    if (q == null) {
      return null
    }
    var a = q.findAll().toArray()
    a = q.findAll().toArray()
    return a.length == 0 ? null : a[0] as PsiElement
  }
}