package gw.perf.play.stones

uses javax.swing.UIManager
uses java.awt.EventQueue

class Main {
  public static function main( args: String[] ) {
    EventQueue.invokeLater( \-> {
      UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() )
      new BoardFrame().Visible = true
    } )
  }
}