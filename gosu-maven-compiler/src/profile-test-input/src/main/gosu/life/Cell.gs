package life

class Cell {
  static var c = 0
  internal var _x: int as readonly X
  internal var _y: int as readonly Y
  var _live: boolean as Live
  internal var _hash: int

  construct( x: int, y: int ) {
    _x = x
    _y = y
    c++
    _hash = c
  }

  override function hashCode(): int {
    return _hash
  }

  override function toString() : String {
    return "Cell: (" + _x + ", " + _y + ") " + (Live ? "Live" : "Dead")
  }
}