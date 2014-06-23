/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.navigation

uses com.intellij.psi.PsiElement
uses com.intellij.psi.PsiMethod
uses com.intellij.psi.search.searches.SuperMethodsSearch
uses com.intellij.psi.util.MethodSignatureBackedByPsiMethod
uses com.intellij.util.Processor
uses gw.plugin.ij.framework.MarkerType
uses gw.plugin.ij.framework.generator.GosuClassFile
uses gw.plugin.ij.framework.generator.GosuTestingResource
uses gw.plugin.ij.framework.generator.JavaClassFile
uses gw.plugin.ij.framework.generator.JavaInterfaceFile
uses gw.plugin.ij.lang.psi.impl.search.GosuMethodSuperSearcher

uses java.util.ArrayList

class GotoSuperMethodTest extends GosuUpDownMethodTest {
  function testGotoJavaMethodFromGosuProperty() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public boolean ##isEmpty() {return false}\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA {\n" +
      "  override property get [[Empty]]() {return false}\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  function testGotoJavaMethodFromGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void ##baseFoo( String a ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedF extends BaseA {\n" +
      " function [[baseFoo]]( a : String ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  function testGotoJavaMethodFromGosuSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void ##baseFoo( String a ) {\n" +
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
      " function [[baseFoo]]( a : String ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
  }

  function testGotoJavaMethodFromGosuSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "class BaseA {\n" +
      "  public void ##baseFoo( String a ) {\n" +
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
      " function [[baseFoo]]( a : String ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInClassFromGosuSameTypeVar1() {
    var fsup = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void ##baseFoo( String a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF extends BaseA {\n" +
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInClassFromGosuSameTypeVar2() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void ##baseFoo( String a );\n" +
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
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInClassFromGosuSameTypeVar3() {
    var fsup1 = new JavaClassFile (
      "package some.pkg;\n" +
      "abstract class BaseA {\n" +
      "  public abstract void ##baseFoo( String a );\n" +
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
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInInterfaceFromGosuSameTypeVar1() {
    var fsup = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void ##baseFoo( String a );\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "abstract class DerivedF implements IBaseA {\n" +
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInInterfaceFromGosuSameTypeVar2() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void ##baseFoo( String a );\n" +
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
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
  }

  function testGotoAbstractJavaMethodDeclarationInInterfaceFromGosuSameTypeVar3() {
    var fsup1 = new JavaInterfaceFile (
      "package some.pkg;\n" +
      "interface IBaseA {\n" +
      "  public void ##baseFoo( String a )\n" +
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
      " function [[baseFoo]]( a : String)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoMethodInGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function ##baseFoo( u : java.util.Collection<String> ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      " function [[baseFoo]]( a : java.util.Collection<String>)\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  //generic

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
      " function [[baseFoo]]( a : A ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : A ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
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
      " function [[baseFoo]]( a : A ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
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
      " function [[baseFoo]]( a : F ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : F ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
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
      " function [[baseFoo]]( a : F ) {}\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
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
      " function [[baseFoo]]( a : A ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : java.util.ArrayList ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : java.util.ArrayList ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : java.util.HashMap<K, V> ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsub })
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
      " function [[baseFoo]]( a : A)\n" +
      "}"
    )
    testUp({ fsup1, fsup2, fsup3, fsub })
  }

  function testGotoGenericMethodInGosuSuperClass() {
    var fsup = new GosuClassFile (
      "package some.pkg\n" +
      "class BaseA {\n" +
      "  function ##baseFoo<U>( u : U ) {\n" +
      "  }\n" +
      "}"
    )
    var fsub = new GosuClassFile (
      "package some.pkg\n" +
      "class DerivedD extends BaseA {\n" +
      " function [[baseFoo]]<String>( a : String)\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      "  function [[foo]]<T extends java.util.Collection<String>>( t : T ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      "  function [[foo]]<T extends java.util.Collection<String>>( t : T ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
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
      "  function [[foo]]( t : java.util.List<String> ) {}\n" +
      "}"
    )
    testUp({ fsup, fsub })
  }

  /*function testGotoBoundedGenericMethodInJavaSuperClassFromMoreNarrowlyBoundedGenericGosuClassUsingUncheckedBoundingTypes() {
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
    testUp({ fsup, fsub })
  }*/

  /*function testGotoRegularMethodInJavaSuperClassFromBoundedGenericGosuClassWhenLesserGenericMatchExists() {
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
    testUp({ fsup, fsub })
  }*/

  function testUp(resources: GosuTestingResource[]) {
    var markers = getAllMarkers(resources.map(\r -> configureByText(r.fileName, r.content)))
    var caret = markers.getCaret( MarkerType.CARET2)
    var ranges = markers.getRanges()
    assertEquals("Range marker is not defined.", 1, ranges.size())

    var expectedTarget = findNamedElement(caret.File.findElementAt(caret.offset));

    for (offset in ranges[0].StartOffset..ranges[0].EndOffset) {
      var source = findNamedElement(ranges[0].File.findElementAt(offset));
      var realTarget = gotoSuperMethod(source as PsiMethod)
      assertNotNull("Navigation failed with ${ranges.first()} at ${offset}", realTarget)
      assertEquals(expectedTarget, realTarget)
    }
  }

  function gotoSuperMethod(m: PsiMethod) : PsiElement {
    var found = m.findDeepestSuperMethod()
    if (found != null) {
      return found
    }

    // Special searcher from Gosu properties to java methods
    var searcher : GosuMethodSuperSearcher  = new GosuMethodSuperSearcher()
    var params = new SuperMethodsSearch.SearchParameters(m, null, true, false)
    var signatures = new ArrayList<MethodSignatureBackedByPsiMethod>()
    searcher.execute(params, new Processor<MethodSignatureBackedByPsiMethod>(){
      function process(signature: MethodSignatureBackedByPsiMethod): boolean {
        signatures.add(signature)
        return true
      }
    });
    return signatures.first()?.getMethod();
  }
}