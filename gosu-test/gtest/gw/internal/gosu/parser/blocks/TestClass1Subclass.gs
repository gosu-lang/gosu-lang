package gw.internal.gosu.parser.blocks

class TestClass1Subclass extends TestClass1 {

  construct( y : int )
  {
    super( y )
  }

  function getXUpdaterFromSubclass() : block(int)
  {
    return \ y : int -> { updateX( y ) }
  }

  function updateX( i : int ) {
    super.x = i
  }
}