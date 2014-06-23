package gw.lang.parser
uses gw.testharness.DoNotVerifyResource
uses java.lang.CharSequence


@DoNotVerifyResource
class Errant_FunctionProblems {
  
  function functionOnOuter() {}
  
  class ShouldNotHaveErrors<Q extends Boolean> extends Super {
    function func1() {}
    function func1( s : String ) {}
    function func1<T>( s : T ) {}
    function func1( s : Q ) {}
     
    override function returnsString() : String { return null }

    function functionOnOuter(){}
    override function genericFunction<F>( s : F ){}
    function genericFunction<F extends Boolean>( s : F ){}
  }
  
  class Super {
    function returnsString() : CharSequence { return null }
    final function finalMethod() {}
    function methodToBeOverridden() {}
    function publicMethodToBeOverridden() {}

    function genericFunction<F>( s : F ){}
  } 
  
  @DoNotVerifyResource
  class BadOverridesOfSuper extends Super {
    override function returnsString() : Object { return null }
    override function finalMethod() {}
    function methodToBeOverridden() {}
    override private function publicMethodToBeOverridden() {}
    override static function staticFunction(){}
    override function methodFromEnhancement() {}
  }
   
  class WarnOnImplicitMaskOfEnhancementMethod extends Super {
    function methodFromEnhancement() {}
    function badFunc() { foo } // just to generate a PRE
  }

  @DoNotVerifyResource
  class InnerClassThatImproperlyOverridesOuterClassMethod extends Errant_FunctionProblems {
    override private function functionOnOuter(){}
  }
  
  @DoNotVerifyResource
  class BasicConflictingMethods {
    function noArgConflict() {}
    function noArgConflict() {}
    
    function oneArgConflict( s : String ) {}
    function oneArgConflict( s : String ) {}

    function twoArgConflict( s : String ) {}
    function twoArgConflict( s : String ) {}
  }

  @DoNotVerifyResource
  class ReificationConflicts<Q, R extends String> {
    // all three of the different methods should have reification errors
    function oneArgConflicts<T>( arg : T ){}    
    function oneArgConflicts<T>( arg : T ){}    
    function oneArgConflicts( arg : Object ){}    
    function oneArgConflicts( arg : Q ){}
        
    // all three of the different methods should have reification errors
    function oneArgConflicts2<T extends String>( arg : T ){}    
    function oneArgConflicts2<T extends String>( arg : T ){}    
    function oneArgConflicts2( arg : String ){}    
    function oneArgConflicts2( arg : R ){}    
  }
  
  @DoNotVerifyResource
  class ReificationConflicts2 {
    function func1( s : foo.ErrorType ) {}
    function func1( s : Object ) {}
    
    function func1r( s : Object ) {}
    function func1r( s : foo.ErrorType ) {}
  }

  static class ShouldNotHaveErrors2<T> implements java.util.List<T> {
 
    override function size(): int{
      return 0
    }

    override property get Empty(): boolean{
      return false
    }

    override function contains( o: java.lang.Object ): boolean{
      return false
    }

    override function iterator(): java.util.Iterator <T>{
      return null
    }

    override function toArray(): java.lang.Object[]{
      return null
    }

    override function toArray<E>( a: E[] ): E[]{
      return null
    }

    override function add( e: T ): boolean{
      return false
    }

    override function remove( o: java.lang.Object ): boolean{
      return false
    }

    override function containsAll( c: java.util.Collection <?> ): boolean{
      return false
    }

    override function addAll( c: java.util.Collection <? extends T> ): boolean{
      return false
    }

    override function addAll( index: int, c: java.util.Collection <? extends T> ): boolean{
      return false
    }

    override function removeAll( c: java.util.Collection <?> ): boolean{
      return false
    }

    override function retainAll( c: java.util.Collection <?> ): boolean{
      return false
    }

    override function clear(){
    }

    override function get( index: int ): T{
      return null
    }

    override function set( index: int, element: T ): T{
      return null
    }

    override function add( index: int, element: T ){
    }

    override function remove( index: int ): T{
      return null
    }

    override function indexOf( o: java.lang.Object ): int{
      return 0
    }

    override function lastIndexOf( o: java.lang.Object ): int{
      return 0
    }

    override function listIterator(): java.util.ListIterator <T>{
      return null
    }

    override function listIterator( index: int ): java.util.ListIterator <T>{
      return null
    }

    override function subList( fromIndex: int, toIndex: int ): java.util.List <T>{
      return null
    }
  }

  class SuperWithFuncWithTypeVar {
    function funcWTypeVar<T>() {}
  }
  class SubWithFuncWithNoTypeVar extends SuperWithFuncWithTypeVar {
    override function funcWTypeVar() {}
  }
  class SubWithFuncWithOneTypeVar extends SuperWithFuncWithTypeVar {
    override function funcWTypeVar<Q>() {}
  }
  class SubWithFuncWithOneTypeVar2 extends SuperWithFuncWithTypeVar {
    override function funcWTypeVar<T>() {}
  }
  class SubWithFuncWithTwoTypeVars extends SuperWithFuncWithTypeVar {
    override function funcWTypeVar<T, Q>() {}
  }
  
}