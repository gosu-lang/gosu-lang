package life

uses javax.swing.JPanel
uses javax.swing.border.LineBorder
uses java.awt.Graphics2D
uses java.awt.Graphics
uses java.awt.Dimension
uses java.awt.Color
uses java.awt.Point
uses java.awt.image.BufferedImage
uses java.util.concurrent.Executors
uses java.util.concurrent.ExecutorService

class Board extends JPanel {
  final static var SELCTION_COLOR = new Color( 0, 0, 255, 128 )
  var _model: LifeModel as Model
  var _density: int as Density
  var _frequency: int as Frequency
  var _origin: Point as Origin
  var _modelProcessor: ModelProcessor
  var _scroller: ScrollHandler
  var _zoomer: ZoomHandler
  var _selector: SelectionHandler
  var _drawHandler: DrawHandler
  var _listeners: List<block(board: Board)>
  var _freqListeners: Set<block()>
  var _colorScheme: ColorScheme as BoardColorScheme
  var _buffer: BufferedImage

  construct( model: LifeModel, density: int = 16, frequency: int = 100 ) {
    _origin = new Point( 0, 0 )
    _density = density
    _frequency = frequency
    _colorScheme = Manila
    _model = model
    Border = new LineBorder( _colorScheme.Grid )
    _scroller = new ScrollHandler( this )
    _zoomer = new ZoomHandler( this )
    _selector = new SelectionHandler( this )
    _drawHandler = new DrawHandler( this )
    _listeners = {}
    _freqListeners = {}
  }

  function start() {
    if( _modelProcessor == null ) {
      _modelProcessor = new()
      _modelProcessor.start()
    }
  }
  function stop() {
    _modelProcessor?.shutdown()
    _modelProcessor = null
  }

  function resetTimer( frequency: int ) {
    stop()
    Frequency = frequency
    start()
  }

  function reset( size: Dimension, infinite = false, multithreaded = true, populator: Populator = Random, cellSize = 100, frequency = 40, density = 16, start = true ) {
    stop()
    _model = new LifeModel( infinite, multithreaded, cellSize, size, getPopulatorCells( size, populator ) )
    _density = density
    _frequency = frequency
    _origin = new( 0, 0 )
    fireReset()
    if( start ) {
      start()
    }
    invalidate()
    revalidate()
  }

  property get Running(): boolean {
    return _modelProcessor != null
  }

  property get CellSize() : int {
    return _model.CellSize
  }

  property get XCells() : int {
    return Width / CellSize
  }
  property get YCells() : int {
    return Height/CellSize
  }

  property set Origin( value: Point ) {
    _origin = value
    repaintBuffer()
  }

  property get OriginPoint() : Point {
    return new( Origin.x * CellSize, Origin.y * CellSize )
  }

  override function paintComponent( g: Graphics ) {
    if( _buffer != null ) {
      g.drawImage( _buffer, 0, 0, null )
    }
  }

  function repaintBuffer() {
    _buffer = paintComponentViaBuffer()
    repaint()
  }

  function paintComponentViaBuffer() : BufferedImage {
    var bi = new BufferedImage( Width, Height, BufferedImage.TYPE_INT_ARGB )
    using( var g = bi.createGraphics() ) {
      g.Color = _colorScheme.Cell
      g.fillRect( 0, 0, Width, Height )
      drawGrid( g )
      drawCells( g, _model.LiveCells, _colorScheme.Live )
      drawCells( g, _selector.Selection, _selector.Dragging ? _colorScheme.Cell : SELCTION_COLOR )
      drawCells( g, _drawHandler.Line, _drawHandler.Dragging ? _colorScheme.Cell : SELCTION_COLOR )
    }
    return bi
  }

  private function drawGrid( g: Graphics2D ) {
    var step = CellSize
    if( step < 2 ) {
      return
    }
    g.Color = _colorScheme.Grid
    for( i in 1..XCells ) {
      g.drawLine( step * i, 0, step * i, Height )
    }
    for( i in 1..YCells ) {
      g.drawLine( 0, step * i, Width, step * i )
    }
  }

  private function drawCells( g: Graphics2D, cells: Collection<Cell>, color: Color )  {
    g.Color = color
    var cellSize = Math.max( CellSize, 1 )
    var borderSize = cellSize > 1 ? 1 : 0
    g.translate( Origin.x * cellSize, Origin.y * cellSize )
    for( cell in cells ) {
      g.fillRect( cell.X * cellSize + borderSize, cell.Y * cellSize + borderSize, cellSize - borderSize, cellSize - borderSize )
    }
    g.translate( -(Origin.x * cellSize), -(Origin.y * cellSize) )
  }

  function cellAt( loc: Point ) : Cell {
    var cellLoc = cellLocationAt( loc )
    if( !Model.Infinite ) {
      if( cellLoc.x < 0 || cellLoc.y < 0 || cellLoc.x >= XCells || cellLoc.y >= YCells ) {
        return null
      }
    }
    return Model.getCell( cellLoc.x, cellLoc.y )
  }

  function cellLocationAt( loc: Point ) : Point {
    loc = translate( loc )
    var x = loc.x / CellSize
    var y = loc.y / CellSize
    return new( x, y )
  }

  function translate( pt: Point ) : Point {
    return new( pt.x - OriginPoint.x, pt.y - OriginPoint.y )
  }

  function enableScrolling( enable: boolean ) {
    if( enable && !_scroller.Enabled ) {
      _scroller.Enabled = true
    }
    else {
      _scroller.Enabled = false
    }
  }

  function enableSelection( enable: boolean ) {
    if( enable && !_selector.Enabled ) {
      _selector.Enabled = true
    }
    else {
      _selector.Enabled = false
    }
  }

  function enableDrawing( enable: boolean ) {
    if( enable && !_drawHandler.Enabled ) {
      _drawHandler.Enabled = true
    }
    else {
      _drawHandler.Enabled = false
    }
  }

  function addResetListener( listener( board: Board ) ) {
    _listeners.add( listener )
  }
  function fireReset() {
    _listeners.each( \l -> l( this ))
  }


  property set Frequency( value: int ) {
    _frequency = value
    _freqListeners.each( \l -> l() )
  }
  function addFrequencyListener( listener() ) {
    _freqListeners.add( listener )
  }

  function postModelChange( change() ) {
    if( _modelProcessor == null ) {
      change()
    }
    else {
      _modelProcessor.submit( change )
    }
  }

  private function getPopulatorCells( size: Dimension, populator: Populator ) : Collection<Point> {
    if( populator.LiveCells != null ) {
      return populator.LiveCells
    }
    return randomize( size, populator.LiveCells )
  }

  private function randomize( size: Dimension, cells: Collection<Point> ) : List<Point> {
    var live = new ArrayList<Point>()
    for( x in 0..|size.width ) {
      for( y in 0..|size.height ) {
        if( Math.random() * 100 < Density ) {
          live.add( new( x, y ) )
        }
      }
    }
    return live
  }

  private class ModelProcessor {
    var _executor: ExecutorService
    var _shutdown: boolean as Shutdown

    construct() {
      _executor = Executors.newSingleThreadExecutor()
    }

    function start() {
      _executor.submit( \-> process( System.currentTimeMillis() ) )
    }

    function submit( change() ) {
      _executor.submit( new Change( change ) )
    }

    function shutdown() {
      _shutdown = true
      _executor.shutdown()
    }

    function process( last: long ) {
      if( _shutdown ) {
        return
      }

      var current = System.currentTimeMillis()
      if( current - last >= Frequency ) {
        last = current
        _model.process()
        _buffer = paintComponentViaBuffer()
        repaint()
      }
      else {
        using( this as IMonitorLock ) {
          wait( Math.max( Frequency/10, 1 ) )
        }
      }
      _executor.submit( \-> process( last ) )
    }

    class Change implements Runnable {
      var _change()

      construct( change() ) {
        _change = change
      }

      override function run() {
        if( !Shutdown ) {
          _change()
        }
      }
    }
  }
}