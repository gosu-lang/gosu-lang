package gw.specContrib.interfaceMethods.defaultMethods

interface Errant_DefaultMethodsAccessModifier {
  private function somefun(){}            //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
  protected function somefun1(){}            //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE
  internal function somefun2(){}            //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
  public function somefun3(){}


  interface NestedInterface{
     function noModifier() {}
     public function publicFun() {}
    //IDE-2576 - OS Gosu does not show errors. To be fixed.
     internal function internalFun() {}            //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
     private function privateFun() {}            //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
     protected function protectedFun() {}            //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE

    interface NestedNestedInterface {
      function noModifier() {}
      public function publicFun() {}
      internal function internalFun() {}            //## issuekeys: MODIFIER 'PACKAGELOCAL' NOT ALLOWED HERE
      private function privateFun() {}            //## issuekeys: MODIFIER 'PRIVATE' NOT ALLOWED HERE
      protected function protectedFun() {}            //## issuekeys: MODIFIER 'PROTECTED' NOT ALLOWED HERE
    }
  }
}