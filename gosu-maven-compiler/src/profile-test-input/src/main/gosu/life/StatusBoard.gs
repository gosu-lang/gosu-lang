package life

uses java.awt.GridBagLayout
uses java.awt.GridBagConstraints
uses javax.swing.JPanel
uses javax.swing.JLabel
uses javax.swing.border.EmptyBorder
uses java.awt.SystemColor
uses javax.swing.border.MatteBorder
uses javax.swing.border.CompoundBorder

class StatusBoard extends JPanel {
  var _board: Board as Board
  var _frame: BoardFrame
  var _liveCount: JLabel
  var _genRate: JLabel
  var _generation: JLabel
  var _labelCellSize: JLabel
  var _labelFrequency: JLabel
  var _labelCell: JLabel as CursorCell
  var _mark: long

  construct( model: Board, frame: BoardFrame ) {
    _board = model
    _frame = frame
    Border = new EmptyBorder( 0, 0, 4, 0 )
    addControls()
    MaximumSize = PreferredSize
    _board.addResetListener( \ board ->
        {
          board.Model.addGenerationListener( \-> update() )
          board.addFrequencyListener( \-> updateFrequency() )
        } )
    var cursorCellHandler = new CellHandler( this )
    _board.addMouseMotionListener( cursorCellHandler  )
    _board.addMouseListener( cursorCellHandler  )
  }

  function init() {
    _liveCount.Text = String.valueOf( _board.Model.LiveCells.size() )
    updateFrequency()
    update()
  }

  private function addControls() {
    var layout = new GridBagLayout()
    Layout = layout
    var x = 0

    // Live cell count
    _liveCount = new JLabel( "1" )
      _liveCount.ToolTipText = "Live cell count"
      _liveCount.Font = new( _liveCount.Font.Name, _liveCount.Font.Style & ~Font.BOLD, _liveCount.Font.Size )
      _liveCount.HorizontalAlignment = JLabel.RIGHT
      _liveCount.PreferredSize = new( 90, _liveCount.PreferredSize.height )
      _liveCount.Border = new EmptyBorder( 0, 0, 0, 10 )
      var gc = new GridBagConstraints()
      gc.gridx = x x++
      add( _liveCount, gc )

    // Generation rate
    _genRate = new JLabel( "1" )
      _genRate.ToolTipText = "Generation rate"
      _genRate.Font = new( _genRate.Font.Name, _genRate.Font.Style & ~Font.BOLD, _genRate.Font.Size )
      _genRate.HorizontalAlignment = JLabel.RIGHT
      _genRate.PreferredSize = new( 100, _genRate.PreferredSize.height )
      _genRate.Border = new CompoundBorder( new MatteBorder( 0, 1, 0, 0, SystemColor.controlDkShadow ), new EmptyBorder( 0, 10, 0, 10 ) )
      _board.addResetListener( \e -> _board.Model.addGenerationListener( \-> updateGenRate() ) )
      gc.gridx = x x++
      add( _genRate, gc )

    // Generation count
    _generation = new JLabel( "1" )
      _generation.ToolTipText = "Generation"
      _generation.Font = new( _generation.Font.Name, _generation.Font.Style & ~Font.BOLD, _generation.Font.Size )
      _generation.HorizontalAlignment = JLabel.RIGHT
      _generation.Border = new CompoundBorder( new MatteBorder( 0, 1, 0, 0, SystemColor.controlDkShadow ), new EmptyBorder( 0, 10, 0, 10 ) )
      var count = 0
      _board.addResetListener( \e -> _board.Model.addGenerationListener( \-> {count++ _generation.Text = String.format( "%,d gen", {count} )} ) )
      _board.addResetListener( \ board -> {count = 0})
      gc.gridx = x x++
      add( _generation, gc )

    _labelCellSize = new JLabel()
      _labelCellSize.ToolTipText = "Cell size"
      _labelCellSize.Font = _liveCount.Font
      _labelCellSize.Border = new CompoundBorder( new MatteBorder( 0, 1, 0, 0, SystemColor.controlDkShadow ), new EmptyBorder( 0, 10, 0, 10 ) )
      gc.gridx = x x++
      add( _labelCellSize, gc )

    _labelFrequency = new JLabel()
      _labelFrequency.ToolTipText = "Tick rate"
      _labelFrequency.Font = _liveCount.Font
      _labelFrequency.Border = new CompoundBorder( new MatteBorder( 0, 1, 0, 0, SystemColor.controlDkShadow ), new EmptyBorder( 0, 10, 0, 10 ) )
      _board.addFrequencyListener( \-> updateFrequency() )
      gc.gridx = x x++
      add( _labelFrequency, gc )

    _labelCell = new JLabel()
      _labelCell.ToolTipText = "Cell under cursor"
      _labelCell.Font = _liveCount.Font
      _labelCell.Border = new CompoundBorder( new MatteBorder( 0, 1, 0, 0, SystemColor.controlDkShadow ), new EmptyBorder( 0, 10, 0, 0 ) )
      gc.gridx = x x++
      add( _labelCell, gc )

    var remainder = new JLabel()
      gc.gridx = x x++
      gc.fill = GridBagConstraints.HORIZONTAL
      gc.gridwidth = GridBagConstraints.REMAINDER
      gc.insets = new ( 0, 10, 0, 0 )
      gc.weightx = 1
      add( remainder, gc )
  }

  private function update() {
    _liveCount.Text = String.format( "%,d", {_board.Model.LiveCells.size()} ) + " live"
    _labelCellSize.Text = _board.Model.CellSize + "px"
  }

  private function updateFrequency() {
    _labelFrequency.Text = String.format( "%.3f", {_board.Frequency/1000f} ) + "s"
  }

  private function updateGenRate() {
    _mark = _mark == 0 ? System.currentTimeMillis() : _mark
    var now = System.currentTimeMillis()
    var rate = now - _mark
    _mark = now
    if( rate < .0001 ) {
      return;
    }
    _genRate.Text = genPerSecond( rate )
  }

  function genPerSecond( rate: long ): String {
    var value = Math.round( 1d/(rate/1000d) )
    return String.format( "%,d", {value} ) + " gen / s"
  }
}