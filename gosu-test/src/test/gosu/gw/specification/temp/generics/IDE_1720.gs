package gw.specification.temp.generics

class IDE_1720 {
    class A<T>{}
    class B<T> extends A<T> {}

    class C {}
    class D extends C {}

    function hello() {
      var aC : A<C>
      var aD : A<D>

      var bC : B<C>
      var bD : B<D>

      var d: D
      var c: C
      print( c as D )
      var a111 = aC as B<C>
      var a112 = aD as B<D>
      var a113 = aC as B<D>
      var a114 = aD as B<C>
      var a115 = bD as A<D>    //## issuekeys: MSG_UNNECESSARY_COERCION
      var a116 = bC as A<D>
    }
}