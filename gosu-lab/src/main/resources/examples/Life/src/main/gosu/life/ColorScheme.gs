package life

uses java.awt.Color
uses java.awt.SystemColor

enum ColorScheme {
  Manila( SystemColor.control, new(255, 255, 210), SystemColor.infoText),
  Reagan( SystemColor.darkGray, Color.black, Color.green ),
  Citrine ( SystemColor.gray, SystemColor.darkGray, new(255, 200, 0) ),

  final var _grid: Color as Grid
  final var _cell: Color as Cell
  final var _live: Color as Live

  private construct( grid: Color, cell: Color, live: Color ) {
    _grid = grid
    _cell = cell
    _live = live
  }
}