package play.stones

uses java.awt.Graphics
uses javax.swing.Icon
uses javax.swing.ImageIcon
uses java.awt.Graphics2D

/**
 */
class ImageStoneRenderer implements IStoneRenderer {
  var _icon : ImageIcon
  
  construct( strIconRes: String ) {
    _icon = new ImageIcon( (typeof this).TypeLoader.getResource( strIconRes ) )
  }
  override function render( stone: StoneComponent, g: Graphics ) {
    var x = ((stone.Width-4) - _icon.IconWidth)/2
    var y = ((stone.Height-4) - _icon.IconHeight)/2
    if( x < 0 ) {
      var g2d = g as Graphics2D
      g2d.scale( (stone.Width-4.0)/_icon.IconWidth as double, (stone.Height-4.0)/_icon.IconHeight as double )
      x = 0
      y = 0
    }
    _icon.paintIcon( stone, g, x, y )
  }
}