package gw.specification.temp.delegates

class Errant_DelegateMemberConflict {
  interface IA {
    function somefun()
  }
  interface IB {
    function somefun()
  }
  class A implements IA {
    override function somefun() {
    }
  }
  class B implements IB {
    override function somefun() {
    }
  }

  class C implements IA, IB {
    delegate a represents IA = new A()
    delegate b represents IB = new B()  //## issuekeys: MSG_DELEGATE_METHOD_CONFLICT
  }
}