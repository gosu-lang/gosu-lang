package life

uses java.awt.event.MouseEvent
uses java.awt.Point
uses javax.swing.JPopupMenu
uses javax.swing.JMenuItem
uses java.awt.Toolkit
uses java.awt.datatransfer.StringSelection
uses java.awt.datatransfer.DataFlavor
uses java.awt.EventQueue

class SelectionHandler extends AbstractHandler {
  var _anchorCell: Point
  var _dragCell: Point
  var _dragging: Boolean as Dragging

  construct( board: Board ) {
    super( board )
  }

  override property set Enabled( enabled: boolean ) {
    _anchorCell = null
    super.Enabled = enabled
  }

  property get Selection() : Collection<Cell> {
    if( _anchorCell == null || _dragCell == null ) {
      return Collections.emptyList<Cell>()
    }

    var selection = new HashSet<Cell>()
    for( x in _anchorCell.x.._dragCell.x ) {
      selection.add( Board.Model.getCell( x, _anchorCell.y ) )
      selection.add( Board.Model.getCell( x, _dragCell.y ) )
    }
    for( y in _anchorCell.y.._dragCell.y ) {
      selection.add( Board.Model.getCell( _anchorCell.x, y ) )
      selection.add( Board.Model.getCell( _dragCell.x, y ) )
    }
    return selection
  }

  property get SelectedLiveCells() : Collection<Cell>  {
    var selected = new ArrayList<Cell>()
    for( x in _anchorCell.x.._dragCell.x ) {
      for( y in _anchorCell.y.._dragCell.y ) {
        var cell = Board.Model.getCell( x, y )
        if( Board.Model.LiveCells.contains( cell ) ) {
          selected.add( cell )
        }
      }
    }
    return selected
  }

  function clearSelection() {
    _anchorCell = null
    _dragCell = null
    Board.repaintBuffer()
  }

  override function mousePressed( e: MouseEvent ) {
    _anchorCell = findCellAt( e.Point )
    _dragCell = _anchorCell
    Board.repaintBuffer()
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
    if( _anchorCell == _dragCell ) {
      clearSelection()
    }
    else {
      var popup = new JPopupMenu()
      var item = new JMenuItem( "Cut" )
        item.addActionListener( \ev -> cut() )
        popup.add( item )
      item = new JMenuItem( "Copy" )
        item.addActionListener( \ev -> copy() )
        popup.add( item )
      item = new JMenuItem( "Paste" )
        item.addActionListener( \ev -> paste() )
        popup.add( item )
      popup.show( Board, e.X, e.Y )
    }
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

  function cut() {
    copy()
    var cells = SelectedLiveCells
    Board.postModelChange( \-> Board.Model.killLiveCells( cells ) )
    clearSelection()
  }

  function copy() {
    var offsetCell = _anchorCell.x < _dragCell.x ? _anchorCell : _dragCell
    var copy = "new HashSet<java.awt.Point>() {"
    var first = true
    for( cell in SelectedLiveCells ) {
      copy += (first ? "" : ",") + "new(${cell.X-offsetCell.x}, ${cell.Y-offsetCell.y})"
      first = false
    }
    copy += "}"
    Toolkit.DefaultToolkit.SystemClipboard.setContents( new StringSelection( copy ), null )
    EventQueue.invokeLater( \-> clearSelection() )
  }

  function paste() {
    var contents = Toolkit.DefaultToolkit.SystemClipboard.getContents( null )
    var value = contents.getTransferData( DataFlavor.stringFlavor ) as String
    if( value.startsWith( "new HashSet" ) ) {
      var points = eval( value ) as HashSet<Point>
      var cells = mapCellsToSelection( points )
      Board.postModelChange( \-> {
          Board.Model.addLiveCells( cells )
        } )
    }
    clearSelection()
  }

  private function mapCellsToSelection( points: Collection<Point> ): Collection<Cell> {
    var offsetCell = _anchorCell.x < _dragCell.x ? _anchorCell : _dragCell
    return points.map( \pt -> Board.Model.getCell( pt.x + offsetCell.x, pt.y + offsetCell.y ) )
  }
}