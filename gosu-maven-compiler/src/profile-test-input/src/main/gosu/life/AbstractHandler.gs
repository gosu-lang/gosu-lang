package life

uses java.awt.event.MouseAdapter
uses java.awt.Point

abstract class AbstractHandler extends MouseAdapter {
  var _board: Board as Board
  var _enabled: boolean as Enabled

  construct( board: Board ) {
    _board = board
  }

  property get Enabled() : boolean {
    return _enabled
  }
  property set Enabled( enabled: boolean ) {
    _enabled = enabled
    if( enabled ) {
      _board.addMouseListener( this )
      _board.addMouseMotionListener( this )
    }
    else {
      _board.removeMouseListener( this )
      _board.removeMouseMotionListener( this )
    }
  }
}