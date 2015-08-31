package play.stones

uses javax.swing.*
uses java.awt.*
uses java.awt.event.ActionListener
uses java.awt.event.ActionEvent

/**
 */
class TimeBoard extends JPanel {
  var _timeBar: JProgressBar
  var _controlPanl: JLabel
  var _boardModel: BoardModel
  var _timer: Timer
  
  construct( model: BoardModel ) {
    Border = BorderFactory.createEmptyBorder( 0, 0, 2, 0 )
    Layout = new BorderLayout()
    _boardModel = model
    makeTimeBar()
    makeControlPanel()
  }
  
  private function makeTimeBar() {
    _timeBar = new JProgressBar( JProgressBar.HORIZONTAL, 0, BoardModel.MAX_TIME )
    var initialTime = _timeBar.Maximum/2
    _boardModel.addToLevelTime( initialTime )
    _timeBar.Value = initialTime
    _timer = new Timer( BoardModel.MAX_TIME/_boardModel.Level, \ e -> timeTick() )
    add( _timeBar, BorderLayout.NORTH ) 
    _boardModel.addScoreChangeListener( \ chg -> scoreChanged( chg ) )
    _boardModel.addLevelTimeChangeListener( \ -> timeChanged() )
    _boardModel.addLevelChangeListener( \ -> levelChanged() )
    _timer.start()
  }

  private function makeControlPanel() {
  }
  
  private function timeTick() {
    _boardModel.addToLevelTime( -1 - _boardModel.Level )
  }
  
  private function scoreChanged( chg: int ) {
    var timeChg = chg/10/_boardModel.Level
    var newTime = _boardModel.LevelTime + timeChg
    if( newTime > BoardModel.MAX_TIME ) {
      timeChg = BoardModel.MAX_TIME - _boardModel.LevelTime
    }
    _boardModel.addToLevelTime( timeChg )
    
  }

  private function timeChanged() {
    _timeBar.Value = _boardModel.LevelTime
    if( _timeBar.Value <= 0 ) {
      _timer.stop()
    }
  }

  private function levelChanged() {
    _boardModel.addToLevelTime( -BoardModel.MAX_TIME/2 )
  }
}