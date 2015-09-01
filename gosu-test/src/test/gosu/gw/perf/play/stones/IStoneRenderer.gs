package gw.perf.play.stones

uses javax.swing.Icon
uses java.awt.Graphics

/**
 */
interface IStoneRenderer {
  function render( stone: StoneComponent, g: Graphics )
}