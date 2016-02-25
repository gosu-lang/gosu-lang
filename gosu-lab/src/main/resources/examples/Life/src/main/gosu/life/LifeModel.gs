package life

uses java.awt.Dimension
uses gw.util.concurrent.ConcurrentHashSet
uses java.util.concurrent.ConcurrentHashMap
uses java.awt.Point
uses java.awt.EventQueue
uses java.util.concurrent.atomic.AtomicInteger
uses java.util.stream.Stream

class LifeModel {
  var _infinite: boolean as Infinite
  var _cellSize: int as CellSize
  var _size: Dimension as Size
  var _live: Set<Cell> as LiveCells
  var _dead: Cell[] as DeadNeighbors
  var _cellCache: Cell[][]
  var _cellCacheInfinite: ConcurrentHashMap<Cell, Cell>
  var _listeners: Set<block()>
  var _cellSizeListeners: Set<block()>
  var _multithreaded: boolean as Multithreaded

  construct( infinite = false, multithreaded = true, cellSize = 4, size: Dimension = null, liveCells: Collection<Point> = {} ) {
    _infinite = infinite
    _multithreaded = multithreaded
    _size = size ?: new( 400, 400 )
    _cellSize = cellSize
    _listeners = new ConcurrentHashSet<block()>()
    _cellSizeListeners = new ConcurrentHashSet<block()>()
    initCellCache()
    reset( liveCells.map( \ pt -> getCell( pt.x, pt.y ) ) )
  }

  function getCell( x: int, y: int ) : Cell {
    if( _infinite ) {
      var cell = new InfinitiCell( x, y )
      var prev = _cellCacheInfinite.putIfAbsent( cell, cell )
      return prev ?: cell
    }
    else {
      return _cellCache[x][y]
    }
  }

  private function initCellCache() {
    if( Infinite ) {
      _cellCacheInfinite = new( 10000 )
    }
    else {
      var cells = new Cell[Size.width][Size.height]
      for( x in 0..|Size.width ) {
        for( y in 0..|Size.height ) {
          cells[x][y] = new Cell( x, y )
        }
      }
      _cellCache = cells
    }
  }

  final function reset( locations: Collection<Cell> ) {
    LiveCells = new ConcurrentHashSet<Cell>( new HashSet( locations ) )
    LiveCells.each( \ cell -> {cell.Live = true} )
    addDeadNeighbors()
  }

  function process() {
    var newLive = processNewLive( new ConcurrentHashSet<Cell>( 1024 ) )
    reviveTheDead( newLive )
    stream( LiveCells ).forEach( \ l -> {l.Live = newLive.contains( l )} )
    stream( newLive ).forEach( \ l -> {l.Live = true} )
    LiveCells = newLive
    addDeadNeighbors()
    notifyListeners( _listeners )
  }

  private function processNewLive( newLive: Set<Cell> ) : Set<Cell> {
    stream( LiveCells ).forEach( \ cell -> {
      var neighbors = findNeighbors( cell )
      var count = 0
      for( neighbor in neighbors ) {
        if( neighbor.Live ) {
          count++
        }
      }
      if( count == 2 || count == 3 ) {
        newLive.add( cell )
      }
    } )
    return newLive
  }

  private function reviveTheDead( newLive: Set<Cell> ) {
    stream( DeadNeighbors ).forEach( \ cell -> {
      var neighbors = findNeighbors( cell )
      var count = 0
      for( neighbor in neighbors ) {
        if( neighbor.Live ) {
          count++
        }
      }
      if( count == 3 ) {
        newLive.add( cell )
      }
    } )
  }

  private function addDeadNeighbors() {
    var dead = new Cell[LiveCells.size() * 8]
    var i = new AtomicInteger( 0 )
    stream( LiveCells ).forEach( \ cell -> {
      var neighbors = findNeighbors( cell )
      for( neighbor in neighbors ) {
        if( !neighbor.Live ) {
          dead[i.AndIncrement] = neighbor
        }
      }
    } )
    var copy = new Cell[i.get()]
    System.arraycopy( dead, 0, copy, 0, copy.length )
    DeadNeighbors = copy
  }

  private function stream<T>( c: Collection<T> ) : Stream<T> {
    return _multithreaded ? c.parallelStream() : c.stream()
  }

  private function stream<T>( c: T[] ) : Stream<T> {
    return _multithreaded ? Arrays.stream( c ).parallel() : Arrays.stream( c )
  }

  private function findNeighbors( cell: Cell ): ArrayList<Cell> {
    var neighbors = new ArrayList<Cell>( 8 )
    var x = cell.X
    var y = cell.Y

    if( _infinite ) {
      neighbors.add( getCell( x-1, y ) )
      neighbors.add( getCell( x-1, y-1 ) )
      neighbors.add( getCell( x-1, y+1 ) )
      neighbors.add( getCell( x+1, y ) )
      neighbors.add( getCell( x+1, y-1 ) )
      neighbors.add( getCell( x+1, y+1 ) )
      neighbors.add( getCell( x, y-1 ) )
      neighbors.add( getCell( x, y+1 ) )
    }
    else {
      if( cell.X > 0 ) {
        neighbors.add( getCell( x-1, y ) )
        if( cell.Y > 0 ) {
          neighbors.add( getCell( x-1, y-1 ) )
        }
        if( cell.Y < Size.height-1 ) {
          neighbors.add( getCell( x-1, y+1 ) )
        }
      }
      if( cell.X < Size.width-1 ) {
        neighbors.add( getCell( x+1, y ) )
        if( cell.Y > 0 ) {
          neighbors.add( getCell( x+1, y-1 ) )
        }
        if( cell.Y < Size.height-1 ) {
          neighbors.add( getCell( x+1, y+1 ) )
        }
      }
      if( cell.Y > 0 ) {
        neighbors.add( getCell( x, y-1 ) )
      }
      if( cell.Y < Size.height-1 ) {
        neighbors.add( getCell( x, y+1 ) )
      }
    }

    return neighbors
  }

  function addLiveCells( cells: Collection<Cell> ) {
    cells.each( \ cell -> {cell.Live = true} )
    LiveCells.addAll( cells )
    addDeadNeighbors()
  }

  function killLiveCells( cells: Collection<Cell> ) {
    cells.each( \ cell -> {cell.Live = false} )
    LiveCells.removeAll( cells )
    addDeadNeighbors()
  }

  function updateCellSize( value: int, size: Dimension ) {
    var oldSize = _cellSize
    _cellSize = value
    _size = size
    if( !Infinite ) {
      retainCacheCells( oldSize )
    }
    notifyListeners( _cellSizeListeners )
  }

  private function retainCacheCells( oldRes: int ) {
    var oldCache = _cellCache
    if( Size.width > oldCache.length ) {
      var height = Math.max( Size.height, oldCache[0].length )
      var cells = new Cell[Size.width][height]
      for( x in 0..|Size.width ) {
        if( x < oldCache.length ) {
          System.arraycopy( oldCache[x], 0, cells[x], 0, oldCache[x].length )
          for( y in oldCache[x].length..|height ) {
            cells[x][y] = new Cell( x, y )
          }
        }
        else {
          for( y in 0..|height ) {
            cells[x][y] = new Cell( x, y )
          }
        }
      }
      _cellCache = cells
    }
    else if( Size.height > oldCache[0].length ) {
      var width = Math.max(Size.width, oldCache.length)
      var cells = new Cell[width][Size.height]
      for( x in 0..|width ) {
        for( y in 0..|Size.height ) {
          if( y < oldCache[x].length ) {
            System.arraycopy( oldCache[x], 0, cells[x], 0, oldCache[x].length )
          }
          else {
            cells[x][y] = new Cell( x, y )
          }
        }
      }
      _cellCache = cells
    }
  }

  function addGenerationListener( listener() ) {
    _listeners.add( listener )
  }

  function addCellSizeListener( listener(size: int) ) {
    _cellSizeListeners.add( \-> listener( CellSize ) )
  }

  private function notifyListeners( listeners: Collection<block()> ) {
    var post = \-> {
      for( listener in listeners ) {
        listener()
      }
    }
    if( EventQueue.isDispatchThread() ) {
      post()
    }
    else {
      EventQueue.invokeLater( post )
    }
  }
}