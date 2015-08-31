package play.stones

uses java.lang.Comparable

/**
 */
class Stone implements Comparable<Stone> {
  var _iStoneType: int as StoneType
  var _iRow: int as Row
  var _iColumn: int as Column
  var _bNew: boolean as New
  
  construct( iStoneType: int, iRow: int, iColumn: int ) {
    _iStoneType = iStoneType
    _iRow = iRow
    _iColumn = iColumn
  }

  override function compareTo( to: Stone ): int {
    if( Row < to.Row ) {
      return -1
    }  
    if( Row > to.Row ) {
      return 1
    }
    if( Column < to.Column ) {
      return -1
    }
    if( Column > to.Column ) {
      return 1
    }
    return 0
  }
}