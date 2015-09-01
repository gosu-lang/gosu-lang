package gw.perf.play.stones

uses java.awt.*
uses sun.net.www.content.audio.x_aiff

/**
 */
class StonesLayout extends GridLayout {
  var _bUseModel: boolean as UseModel
  construct() {
    super( 8, 8, 2, 2 )
  }

  function getColumnPos( parent: Board, col: int ) : int {
    var insets = parent.Insets
    var w = parent.Width - (insets.left + insets.right)
    w = (w - (Columns - 1) * Hgap) / Columns
    return insets.left + Vgap * (col+1) + w * col
  }
  
  function getRowPos( parent: Board, row: int ) : int {
    var insets = parent.Insets
    var h = parent.Height - (insets.top + insets.bottom)
    h = (h - (Rows - 1) * Vgap) / Rows
    return insets.top + Hgap * (row+1) + h * row
  }

  function getWidth( parent: Board ) : int {
    var insets = parent.Insets
    var w = parent.Width - (insets.left + insets.right)
    w = (w - (Columns - 1) * Hgap) / Columns
    return w
  }

  function layoutComponentsUsingModel( parent: Board, comps: Component[] ) {
    var insets = parent.Insets
    var w = parent.Width - (insets.left + insets.right)
    var h = parent.Height - (insets.top + insets.bottom)
    w = (w - (Columns - 1) * Hgap) / Columns
    h = (h - (Rows - 1) * Vgap) / Rows
    using( parent.TreeLock as IMonitorLock ) {
      for( c in comps ) {
        var stoneComp = c as StoneComponent
        var row = stoneComp.Stone.Row
        var col = stoneComp.Stone.Column
        stoneComp.setBounds( insets.left + Vgap * (col+1) + w * col,
                             insets.top + Hgap * (row+1) + h * row,
                             w, h )
      }
    }
  }
  
  override function layoutContainer(parent: Container) {
    layoutComponentsUsingModel( parent as Board, parent.Components )
  }
}