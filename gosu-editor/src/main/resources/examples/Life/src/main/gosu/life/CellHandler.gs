package life

uses java.awt.event.MouseEvent
uses java.awt.Point
uses java.awt.event.MouseAdapter

class CellHandler extends MouseAdapter {
  var _statusBoard: StatusBoard

  construct( statusBoard: StatusBoard ) {
    _statusBoard = statusBoard
  }

  override function mouseMoved( e: MouseEvent ) {
    handleMotion( e.Point )
  }

  override function mouseDragged( e: MouseEvent ) {
    handleMotion( e.Point )
  }

  private function handleMotion( pt: Point ) {
    try {
      var cell = _statusBoard.Board.cellAt( pt )
      _statusBoard.CursorCell.Text = "(${cell?.X}, ${cell?.Y})"
    }
    catch( e: ArrayIndexOutOfBoundsException ) {
    }
  }
}