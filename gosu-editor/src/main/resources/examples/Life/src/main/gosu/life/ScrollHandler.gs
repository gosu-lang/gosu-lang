package life

uses java.awt.event.MouseEvent
uses java.awt.Point
uses java.awt.event.MouseWheelEvent

class ScrollHandler extends AbstractHandler {
  var _anchor: Point
  var _origin: Point

  construct( board: Board ) {
    super( board )
  }

  override function mousePressed( e: MouseEvent ) {
    _anchor = e.Point
    _origin = Board.Origin
  }

  override function mouseDragged( e: MouseEvent ) {
    scroll( e.Point )
  }

  private function scroll(pt: Point) {
    var xDiff = (_anchor.x - pt.x) / Board.CellSize
    var yDiff = (_anchor.y - pt.y) / Board.CellSize
    Board.Origin = new Point( _origin.x - xDiff, _origin.y - yDiff )
  }
}