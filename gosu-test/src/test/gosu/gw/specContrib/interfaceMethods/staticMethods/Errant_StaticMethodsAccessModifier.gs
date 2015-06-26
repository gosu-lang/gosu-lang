package gw.specContrib.interfaceMethods.staticMethods

interface Errant_StaticMethodsAccessModifier {
  private static function somefun(){}      //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
  protected static function somefun1(){}      //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE
  internal static function somefun2(){}      //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
  public static function somefun3(){}


  interface NestedInterface{
    static function noModifier() {}
    public static function publicFun() {}
    //IDE-2576 - OS Gosu does not show errors. To be fixed.
    internal static function internalFun() {}      //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
    private static function privateFun() {}      //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
    protected static function protectedFun() {}      //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE

    interface NestedNestedInterface {
      static function noModifier() {}
      public static function publicFun() {}
      internal static function internalFun() {}      //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
      private static function privateFun() {}      //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
      protected static function protectedFun() {}      //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE
    }
  }
}