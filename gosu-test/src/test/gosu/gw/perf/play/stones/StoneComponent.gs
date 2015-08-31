package play.stones

uses javax.swing.JPanel
uses java.awt.Graphics
uses java.awt.Dimension

/**
 */
class StoneComponent extends JPanel {
  var _stone : Stone as Stone
  var _board : Board as Board
  
  construct( stone: Stone, board: Board ) {
    _stone = stone
    _board = board
  }
  
  override function paintComponent(g: Graphics) {
    var renderer = _board.getRenderer( Stone )
    renderer.render( this, g )
  }

  override property get PreferredSize(): Dimension {
    return new( 32, 32 )
  }
}