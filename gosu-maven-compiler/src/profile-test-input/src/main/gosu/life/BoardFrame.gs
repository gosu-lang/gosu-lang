package life

uses javax.swing.JFrame
uses javax.swing.JPanel
uses java.awt.BorderLayout
uses java.awt.event.WindowAdapter
uses java.awt.event.WindowEvent
uses java.awt.event.ComponentAdapter
uses java.awt.event.ComponentEvent
uses java.awt.EventQueue
uses javax.swing.border.EmptyBorder
uses javax.swing.JComponent
uses java.awt.Point
uses javax.swing.ImageIcon
uses javax.swing.JScrollPane

/**
 */
final class BoardFrame extends JFrame {
  var _board: Board
  var _controlBoard: ControlBoard
  var _statusBoard: StatusBoard as StatBoard
  var _clientArea: JPanel as ClientArea

  @SuppressWarnings( "all" )
  construct() {
    super( "Game of Life" )
    IconImage = new ImageIcon( Class.getResource( "/life/images/gol.png" ) ).Image
    ContentPane.setLayout( new BorderLayout() )
    _clientArea = new JPanel( new BorderLayout() )
    _board = new Board( new LifeModel() )
    _clientArea.add( _board, BorderLayout.CENTER )
    ContentPane.add( _clientArea, BorderLayout.CENTER )
    _controlBoard = new ControlBoard( _board, this )
    ContentPane.add( new JScrollPane( _controlBoard.TopBar, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {:Border = new EmptyBorder(0, 0, 0, 0)}, BorderLayout.NORTH )
    ContentPane.add( new JScrollPane( _controlBoard.LeftBar, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ) {:Border = new EmptyBorder(0, 0, 0, 0)}, BorderLayout.WEST )
    _statusBoard = new StatusBoard( _board, this )
    ContentPane.add( _statusBoard, BorderLayout.SOUTH )
    (ContentPane as JComponent).Border = new EmptyBorder( 4, 4, 0, 4 )

    addComponentListener( new ComponentAdapter() {
      override function componentResized( e: ComponentEvent ) {
        adjustClientArea()
      }
    } )

    addWindowListener( new WindowAdapter() {
        override function windowClosing( e: WindowEvent ) {
          try {
            System.exit( 0 )
          }
          catch( se: SecurityException ) {
            dispose()
          }
        }
      } )

    setLocation( 300, 100 )
    setSize( 550, 475 )
    EventQueue.invokeLater( \-> {
        _controlBoard.init()
        _statusBoard.init()
        _board.start()}
      )
  }

  function adjustClientArea() {
    _controlBoard.resetCellSize()
    _clientArea.revalidate()
  }

  override function dispose() {
    super.dispose()
    _board.stop()
  }
}