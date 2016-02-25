package life

final class InfinitiCell extends Cell {
  construct( x: int, y: int ) {
    super( x, y )
    _hash = 1543 * x + y
  }

  override function equals( obj: Object ): boolean {
    var that = obj as InfinitiCell
    var ret = _x == that._x && _y == that._y
    return ret
  }
}