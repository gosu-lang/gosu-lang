package life

uses java.awt.Point
uses java.awt.event.MouseWheelEvent
uses java.awt.event.MouseEvent
uses java.awt.EventQueue

class ZoomHandler extends AbstractHandler {
  var _cellAnchorPt: Point
  var _pt: Point

  construct( board: Board ) {
    super( board )
    board.addMouseListener( this )
    board.addMouseMotionListener( this )
    board.addMouseWheelListener( this )
  }

  override function mouseMoved( e: MouseEvent ) {
    _pt = e.Point
    _cellAnchorPt = Board.cellLocationAt( e.Point )
  }

  override function mouseDragged( e: MouseEvent ) {
    _pt = e.Point
    _cellAnchorPt = Board.cellLocationAt( e.Point )
  }

  override function mouseWheelMoved( e: MouseWheelEvent ) {
      Board.postModelChange( \-> {
        var amount = e.WheelRotation
        var cellSize = Board.Model.CellSize - amount
        if( cellSize > 0 && cellSize <= 100 ) {
          var origin = new Point( -_cellAnchorPt.x + _pt.x/cellSize, -_cellAnchorPt.y + _pt.y/cellSize )
          Board.Model.updateCellSize( cellSize, Board.Model.Size )
          EventQueue.invokeLater( \-> {Board.Origin = origin} )
         }
      } )
  }
}