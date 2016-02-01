package match3

uses javax.swing.Icon
uses java.awt.Graphics

/**
 */
interface IStoneRenderer {
  function render( stone: StoneComponent, g: Graphics )
}