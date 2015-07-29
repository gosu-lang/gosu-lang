package gw.specContrib.interfaceMethods

interface Errant_InterfaceMemberModifiers {
  private function somefun()       //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  protected function somefun1()    //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  internal function somefun2()     //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
  public function somefun3()
  function somefun4()

  interface NestedInterface {
    private function somefun()    //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
    protected function somefun1()   //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
    internal function somefun2()    //## issuekeys: MSG_NOT_ALLOWED_IN_INTERFACE
    public function somefun3()
    function somefun4()
  }
}