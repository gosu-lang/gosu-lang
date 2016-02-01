package match3

uses javax.swing.JFrame
uses javax.swing.JPanel
uses java.awt.BorderLayout
uses java.awt.event.WindowAdapter
uses java.awt.event.WindowEvent

/**
 */
final class BoardFrame extends JFrame {
  construct() {
    super( "Match 3" )
    DefaultCloseOperation = DISPOSE_ON_CLOSE
    ContentPane = new JPanel( new BorderLayout() )
    var board = new Board()
    ContentPane.add( board, BorderLayout.CENTER )
    ContentPane.add( new ScoreBoard( board.Model ), BorderLayout.NORTH )
    var timeBoard = new TimeBoard( board.Model )
    ContentPane.add( timeBoard, BorderLayout.SOUTH )
    pack()
    setLocation( 300, 300 )

    addWindowListener( new WindowAdapter() {
      function windowClosed( e: WindowEvent ) {
        timeBoard.dispose()
      }
    } )
  }
}