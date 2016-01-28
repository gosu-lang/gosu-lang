package life

uses java.awt.event.MouseEvent
uses java.awt.Point

class DrawHandler extends AbstractHandler {
  var _on: boolean
  var _anchorCell: Point
  var _dragCell: Point
  var _dragging: boolean as Dragging

  construct( board: Board ) {
    super( board )
  }

  property get Line() : Collection<Cell> {
    if( _anchorCell == null || _dragCell == null ) {
      return Collections.emptyList<Cell>()
    }
    return line( _anchorCell.x, _anchorCell.y, _dragCell.x, _dragCell.y )
  }

  override function mousePressed( e: MouseEvent ) {
    var cell = findCellAt( e.Point )
    _anchorCell = cell
    _dragCell = cell
    _on = !Board.Model.LiveCells.contains( Board.Model.getCell( cell.x, cell.y ) )
    mouseDragged( e )
  }

  override function mouseDragged( e: MouseEvent ) {
    _dragging = true
    Board.repaintBuffer()
    var cell = findCellAt( e.Point )
    _dragCell = cell?:_dragCell
    _dragging = false
    Board.repaintBuffer()
  }

  override function mouseReleased( e: MouseEvent ) {
    _dragging = true
    Board.repaintBuffer()

    var line = line( _anchorCell.x, _anchorCell.y, _dragCell.x, _dragCell.y )
    _anchorCell = null
    _dragCell = null
    Board.repaintBuffer()
    _dragging = false

    Board.postModelChange( \-> {
      if( _on ) {
        Board.Model.addLiveCells( line )
      }
      else {
        Board.Model.killLiveCells( line )
      }
      Board.repaintBuffer()
    } )
  }

  function line( x0: int, y0: int, x1: int, y1: int ) : Collection<Cell> {
    var cells: Set<Cell> = {}
    var deltax = x1 as float - x0
    if( deltax == 0 ) {
      for( y in y0..y1 ) {
        cells.add( Board.Model.getCell( x0, y ) )
      }
      return cells
    }

    var y = y0
    var deltay = y1 as float - y0
    var error = 0f
    var deltaerr = Math.abs( deltay / deltax )    // Assume deltax != 0 (line is not vertical),
    // note that this division needs to be done in a way that preserves the fractional part
    for( x in x0..x1 ) {
      cells.add( Board.Model.getCell( x, y ) )
      error = error + deltaerr
      while( error >= 0.5f ) {
        cells.add( Board.Model.getCell( x, y ) )
        y = y + ((y1 - y0) < 0 ? -1 : 1)
        y = (y0..y1).contains( y ) ? y : y1
        error = error - 1f
      }
    }
    return cells
  }

  function findCellAt( loc: Point ) : Point {
    loc = Board.translate( loc )
    var x = loc.x / Board.CellSize
    var y = loc.y / Board.CellSize
    try {
      if( Board.Model.Infinite ) {
        return new Point( x, y )
      }
      return new Point( x >= Board.XCells ? Board.XCells-1 : x < 0 ? 0 : x,
      y >= Board.YCells ? Board.YCells-1 : y < 0 ? 0 : y )
    }
    catch( e: ArrayIndexOutOfBoundsException ) {
      return null
    }
  }
}