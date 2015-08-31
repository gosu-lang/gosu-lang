package play.stones

uses javax.swing.*
uses java.awt.*

/**
 */
class ColorStoneRenderer implements IStoneRenderer {
  var _color : Color

  construct( color: Color ) {
    _color = color
  }

  override function render( stone: StoneComponent, g: Graphics ) {
    g.setColor( _color )
    g.fillRect( 0, 0, stone.Width, stone.Height )
  }
}