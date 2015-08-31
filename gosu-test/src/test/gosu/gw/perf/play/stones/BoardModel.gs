package play.stones

uses java.util.Random
uses java.util.HashSet
uses java.util.Set
uses java.util.ArrayList
uses java.util.TreeSet

/**
 */
final class BoardModel {
  static final var _MAX_TIME: int as MAX_TIME = 500   
  var _stones: Stone[][] as Stones
  var _score: int as readonly Score
  var _level: int as readonly Level
  var _levelTime: int as readonly LevelTime
  var _scoreChgListeners: List<block(chg:int)>
  var _levelChgListeners: List<block()>
  var _levelTimeChgListeners: List<block()>
  var _rand : Random
  var _testMode: boolean as TestMode

  construct() {
    _rand = new Random()
    _scoreChgListeners = {}
    _levelChgListeners = {}
    _levelTimeChgListeners = {}
  }

  function reset() {
    _score = 0
    _level = 1
    resetStones()
  }
  
  function resetStones() {
    _stones = new Stone[8][8]
    for( row in 0..7 ) {
      for( column in 0..7 ) {
        addStone( row, column )
      }
    }
  }
  
  private function addStone( row: int, column: int ) {
    var stone : Stone
    do {
      stone = makeRandomStone( row, column )
      _stones[row][column] = stone
    } while( !findMatches( stone ).Empty )
  }

  public function findMatches() : Set<Stone> {
    var matches = new TreeSet<Stone>()
    for( row in _stones ) {
      for( stone in row ) {
        var m = findMatches( stone )
        if( m.size() > 2 ) {
          matches.addAll( m )
        }
      }
    }
    return matches
  }

  function findMatches( stone: Stone ) : Set<Stone> {
    var matches = new HashSet<Stone>()
    var horzMatch = findHorzMatch( stone )
    if( horzMatch.size() > 2 ) {
      matches.addAll( horzMatch )
    }
    var vertMatch = findVertMatch( stone )
    if( vertMatch.size() > 2 ) {
      matches.addAll( vertMatch )
    }
    return matches
  }
  
  private function findHorzMatch( stone: Stone ) : Set<Stone> {
    var match = new HashSet<Stone>()
    for( col in stone.Column..0 ) {
      var csr = _stones[stone.Row][col]
      if( csr != null && csr.StoneType == stone.StoneType ) {
        match.add( csr )
      }
      else {
        break
      }
    }
    for( col in stone.Column..7 ) {
      var csr = _stones[stone.Row][col]
      if( csr != null && csr.StoneType == stone.StoneType ) {
        match.add( csr )
      }
      else {
        break
      }
    }
    return match
  }

  private function findVertMatch( stone: Stone ) : Set<Stone> {
    var match = new HashSet<Stone>()
    for( row in stone.Row..0 ) {
      var csr = _stones[row][stone.Column]
      if( csr != null && csr.StoneType == stone.StoneType ) {
        match.add( csr )
      }
      else {
        break
      }
    }
    for( row in stone.Row..7 ) {
      var csr = _stones[row][stone.Column]
      if( csr != null && csr.StoneType == stone.StoneType ) {
        match.add( csr )
      }
      else {
        break
      }
    }
    return match
  }

  private function makeRandomStone( row: int, column: int ) : Stone {
    var iStoneType = _rand.nextInt( 6 )
    return new Stone( iStoneType, row, column )
  }

  function swap( s1: Stone, s2: Stone ) {
    var iRow = s1.Row
    var iCol = s1.Column
    s1.Row = s2.Row
    s1.Column = s2.Column
    s2.Row = iRow
    s2.Column = iCol

    assign( s1 )
    assign( s2 )
  }

  function assign( stone: Stone ) {
    Stones[stone.Row][stone.Column] = stone
  }
  
  function removeMatchesAndReturnColumnsToFall( matchedStones: Set<Stone> ) : List<StoneColumn> {
    var colsAbove = removeMatchesAndGetColsAbove( matchedStones )
    pushColumnsAndInsertNewOnes( colsAbove )
    return colsAbove
  }
  
  private function pushColumnsAndInsertNewOnes( colsAbove: List<StoneColumn> ) {
    for( stoneCol in colsAbove ) {
      var col = stoneCol.AboveStones
      while( col.Empty || (col.last().Row < 7 && _stones[col.last().Row+1][col.last().Column] == null) ) {
        // Push all stones down one space
        for( stone in col ) {
          stone.Row++
          assign( stone )
        }
        // Fill in empty space from pushing stones from above
        var newStone = makeRandomStone( 0, stoneCol.Column )
        newStone.New = true
        assign( newStone )
        col.add( 0, newStone )
      }
    }
  }
  
  private function removeMatchesAndGetColsAbove( matchedStones: Set<Stone> ) : List<StoneColumn> {
    var columns = new ArrayList<StoneColumn>()
    for( stone in matchedStones ) {
      // find column of stones above removed stone
      if( stone.Row > 0 ) {
        var aboveStone = _stones[stone.Row-1][stone.Column]
        if( aboveStone != null && !matchedStones.contains( aboveStone ) ) {
          var column = new ArrayList<Stone>()
          for( row in 0..|stone.Row ) {
            var s = _stones[row][stone.Column]
            column.add( s )
          }
          columns.add( new StoneColumn() {:AboveStones = column, :Column = stone.Column} )
        }
      }
      else {
        columns.add( new StoneColumn() {:AboveStones = {}, :Column = stone.Column} )
      }
      _stones[stone.Row][stone.Column] = null
    }
    return columns
  }

  internal function addToScore( stones: Set<Stone> ) {
    var count = stones.Count
    var unit = 100
    if( count > 4 ) {
      unit = 300 
    }
    else if( count > 3 ) {
      unit = 200
    }
    var increase = count * Level * unit
    _score += increase
    notifyOfScoreChange( increase )
  }

  internal function addToLevelTime( chg: int ) {
    _levelTime += chg
    notifyOfLevelTimeChange()
  }

  internal function advanceLevel() {
    _level++
    notifyOfLevelChange()
  }

  function addScoreChangeListener( scoreChanged(chg: int) ) {
    _scoreChgListeners.add( scoreChanged )
  }

  function addLevelChangeListener( levelChanged() ) {
    _levelChgListeners.add( levelChanged )
  }

  function addLevelTimeChangeListener( levelTimeChanged() ) {
    _levelTimeChgListeners.add( levelTimeChanged )
  }

  private function notifyOfScoreChange( change: int ) {
    _scoreChgListeners.each( \ elt -> elt( change ) )
  }
  
  private function notifyOfLevelChange() {
    _levelChgListeners.each( \ elt -> elt() )
  }

  private function notifyOfLevelTimeChange() {
    _levelTimeChgListeners.each( \ elt -> elt() )
  }
}