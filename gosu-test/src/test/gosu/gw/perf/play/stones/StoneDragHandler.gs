package gw.perf.play.stones

uses java.awt.event.MouseAdapter
uses java.awt.event.MouseEvent
uses java.awt.Point
uses java.lang.Math

/**
 */
class StoneDragHandler extends MouseAdapter {
  var _pt : Point
  var _board : Board

  construct( board: Board ) {
    _board = board
  }

  override function mousePressed(e: MouseEvent) {
    _pt = e.Point
  }

  override function mouseReleased(e: MouseEvent) {
    if( _pt == null ) {
      return
    }
    var stone = e.Component as StoneComponent
    var iXDelta = e.X - _pt.x
    if( Math.abs( iXDelta ) >= stone.Width/2 ) {
      _board.horizontalDelta( stone, iXDelta )
    }
    else {
      var iYDelta = e.Y - _pt.y
      if( Math.abs( iYDelta ) >= stone.Height/2 ) {
        _board.verticalDelta( stone, iYDelta )
      }
    }
    _pt = null
  }
}