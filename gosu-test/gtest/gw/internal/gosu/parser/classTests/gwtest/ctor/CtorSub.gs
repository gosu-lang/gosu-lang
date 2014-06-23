package gw.internal.gosu.parser.classTests.gwtest.ctor

class CtorSub extends Ctor {

  construct() {
    this(4)
  }

  construct(n : int) {
    super(n)
  }

  construct(n: int, o:int) {
    super( \->print('foo') )
  }
  
  construct( blk:block() ) {
    super( blk )
  }

  construct(n: int, o:int, p:int) {
    this( \->print('foo') )
  }
}