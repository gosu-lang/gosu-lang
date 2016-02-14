package life

uses java.awt.Point

enum Populator {
  Clear ( "Clear", {} ),
  Random( "Random" ),
  Pulsar( "Pulsar", {new(4, 6),new(4, 7),new(4, 13),new(4, 14),new(5, 7),new(5, 8),new(5, 12),new(5, 13),new(6, 4),new(6, 7),new(6, 9),new(6, 11),new(6, 13),new(6, 16),new(7, 4),new(7, 5),new(7, 6),new(7, 8),new(7, 9),new(7, 11),new(7, 12),new(7, 14),new(7, 15),new(7, 16),new(8, 5),new(8, 7),new(8, 9),new(8, 11),new(8, 13),new(8, 15),new(9, 6),new(9, 7),new(9, 8),new(9, 12),new(9, 13),new(9, 14),new(11, 6),new(11, 7),new(11, 8),new(11, 12),new(11, 13),new(11, 14),new(12, 5),new(12, 7),new(12, 9),new(12, 11),new(12, 13),new(12, 15),new(13, 4),new(13, 5),new(13, 6),new(13, 8),new(13, 9),new(13, 11),new(13, 12),new(13, 14),new(13, 15),new(13, 16),new(14, 4),new(14, 7),new(14, 9),new(14, 11),new(14, 13),new(14, 16),new(15, 7),new(15, 8),new(15, 12),new(15, 13),new(16, 6),new(16, 7),new(16, 13),new(16, 14)} ),
  Glider( "Glider", {new(1,0), new(2,1), new(0,2), new(1,2), new(2,2)} ),
  Spaceship( "Spaceship", {new(1,0), new(2,0), new(0,1), new(1,1), new(2,1), new(3,1), new(0,2), new(1,2), new(3,2), new(4,2), new(2,3), new(3,3)} ),
  Pentadecathlon( "Pentadecathlon", {new (11, 10), new (11, 11), new (10, 12), new (12, 12), new (11, 13), new (11, 14), new (11, 15), new (11, 16), new (10, 17), new (12, 17), new (11, 18), new (11, 19) } ),
  Acorn( "Acorn", new HashSet<java.awt.Point>() {new(178, 172),new(179, 170),new(179, 172),new(181, 171),new(182, 172),new(183, 172),new(184, 172)}, 2 ),
  GliderGun( "Glider Gun", new HashSet<java.awt.Point>() {new(6, 10),new(6, 11),new(7, 10),new(7, 11),new(16, 10),new(16, 11),new(16, 12),new(17, 9),new(17, 13),new(18, 8),new(18, 14),new(19, 8),new(19, 14),new(20, 11),new(21, 9),new(21, 13),new(22, 10),new(22, 11),new(22, 12),new(23, 11),new(26, 8),new(26, 9),new(26, 10),new(27, 8),new(27, 9),new(27, 10),new(28, 7),new(28, 11),new(30, 6),new(30, 7),new(30, 11),new(30, 12),new(40, 8),new(40, 9),new(41, 8),new(41, 9)} ),
  SwitchEngine( "Switch Engine", new HashSet<java.awt.Point>() {new(129, 135),new(129, 136),new(129, 139),new(130, 135),new(130, 138),new(131, 135),new(131, 138),new(131, 139),new(132, 137),new(133, 135),new(133, 137),new(133, 138),new(133, 139)}, 1 ),

  var _name: String as Label
  var _liveCells: Collection<Point> as LiveCells
  var _cellSize: int as CellSize

  private construct( name: String, liveCells: Collection<Point> = null, cellSize = 0 ){
    _name = name
    _liveCells = liveCells
    _cellSize = cellSize
  }

  override function toString() : String {
    return Label
  }
}