package gw.perf.play.stones

uses javax.swing.JFrame
uses javax.swing.JPanel
uses java.awt.BorderLayout
uses java.awt.event.WindowAdapter
uses java.awt.event.WindowEvent
uses java.lang.System

/**
 */
final class BoardFrame extends JFrame {
  construct() {
    super( "Stones" )

    ContentPane = new JPanel( new BorderLayout() )
    var board = new Board()
    ContentPane.add( board, BorderLayout.CENTER )
    ContentPane.add( new ScoreBoard( board.Model ), BorderLayout.NORTH )
    ContentPane.add( new TimeBoard( board.Model ), BorderLayout.SOUTH )
    pack()
    setLocation( 300, 300 )

    addWindowListener( new WindowAdapter() {
      override function windowClosing( e: WindowEvent ) {
        System.exit( 0 )
      }
    } )
  }
}