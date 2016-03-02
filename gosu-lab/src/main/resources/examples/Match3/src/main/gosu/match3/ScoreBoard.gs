package match3

uses javax.swing.JPanel
uses java.awt.BorderLayout
uses javax.swing.JLabel
uses java.awt.Font
uses javax.swing.BorderFactory
uses javax.swing.JButton
uses javax.swing.JToggleButton

/**
 */
class ScoreBoard extends JPanel {
  var _score: JLabel
  var _lvl: JLabel
  var _testPanel: JPanel
  var _boardModel: BoardModel
  
  construct( model: BoardModel ) {
    Border = BorderFactory.createEmptyBorder( 2, 8, 2, 8 )
    Layout = new BorderLayout()
    _boardModel = model
    makeLevelCtrl()
    makeScoreCtrl()
    //makeTestPanel()
  }
  
  private function makeScoreCtrl() {
    _score = new JLabel( "0" )
    _score.Font = new Font( "dialog", Font.PLAIN, 16 )
    add( _score, BorderLayout.WEST )
    _boardModel.addScoreChangeListener( \ chg -> _score.setText( _boardModel.Score as String ) )
  }
  
  private function makeLevelCtrl() {
    _lvl = new JLabel( "Level 1" )
    _lvl.Font = new Font( "dialog", Font.PLAIN, 16 )
    add( _lvl, BorderLayout.EAST )
    _boardModel.addLevelChangeListener( \-> _lvl.setText( "Level " + _boardModel.Level ) )
  }
  
  private function makeTestPanel() {
    _testPanel = new JPanel( new BorderLayout() )
    var btn = new JButton( "test" )
    btn.addActionListener( \ e -> {_boardModel.TestMode = !_boardModel.TestMode} )
    _testPanel.add( btn, BorderLayout.CENTER )
    add( _testPanel, BorderLayout.CENTER )
  }
}