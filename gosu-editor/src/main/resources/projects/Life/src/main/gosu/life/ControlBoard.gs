package life

uses javax.swing.JPanel
uses java.awt.GridBagLayout
uses java.awt.GridBagConstraints
uses javax.swing.JComboBox
uses javax.swing.JSlider
uses javax.swing.ImageIcon
uses javax.swing.JLabel
uses javax.swing.JToggleButton
uses javax.swing.ButtonGroup
uses javax.swing.border.EmptyBorder
uses java.awt.Point
uses java.awt.Dimension
uses java.awt.EventQueue

class ControlBoard {
  var _board: Board
  var _frame: BoardFrame
  var _topBar: JPanel as TopBar
  var _leftBar: JPanel as LeftBar
  var _btnStartStop: JToggleButton
  var _btnInfinite: JToggleButton
  var _btnMultithreaded: JToggleButton
  var _btnSelection: JToggleButton
  var _btnDraw: JToggleButton
  var _btnScroll: JToggleButton
  var _cbPopulator: JComboBox
  var _sliderCellSize: JSlider
  var _labelDensity: JLabel
  var _sliderDensity: JSlider
  var _frequency: JComboBox
  var _colorScheme: JComboBox

  construct( model: Board, frame: BoardFrame ) {
    _board = model
    _frame = frame
    makeTopBar()
    makeLeftBar()
  }

  function init() {
    _cbPopulator.SelectedItem = Populator.Random
    _sliderCellSize.Value = _board.CellSize
    _frequency.SelectedItem = 100
    _colorScheme.SelectedItem = ColorScheme.Manila
  }

  private function makeTopBar() {
    _topBar = new JPanel()
    _topBar.Border = new EmptyBorder( 0, 0, 4, 0 )
    var layout = new GridBagLayout()
    _topBar.Layout = layout
    var x = 0

    // Start/Stop
    _btnStartStop = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/power.png" ) ) )
      _btnStartStop.ToolTipText = "Start/Stop"
      _btnStartStop.addActionListener( \e -> {toggle() enableControls()} )
      _btnStartStop.PreferredSize = new( _btnStartStop.PreferredSize.height, _btnStartStop.PreferredSize.height )
      _btnStartStop.Selected = true
      var gc = new GridBagConstraints()
      gc.gridx = x x++
      gc.gridwidth = 1
      _topBar.add( _btnStartStop, gc )

    // Populators
    _cbPopulator = new JComboBox( Populator.AllValues.toArray() )
      _cbPopulator.setMaximumRowCount( Populator.values().length )
      _cbPopulator.SelectedItem = Populator.Random
      _cbPopulator.addActionListener( \e -> resetPopulator() )
      _cbPopulator.ToolTipText = "<html>Choose a <b>pattern</b>, go <b>random</b>, or draw your own on a <b>clear</b> board"
      gc.insets = new (0, 10, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _cbPopulator, gc )

    // Infinite
    _btnInfinite = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/infiniti.png" ) ) )
      _btnInfinite.ToolTipText = "Infinite space"
      _btnInfinite.addActionListener( \e -> reset() )
      _btnInfinite.PreferredSize = new( _btnInfinite.PreferredSize.height, _btnInfinite.PreferredSize.height )
      gc.insets = new( 0, 10, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _btnInfinite, gc )

    // Multithreaded
    _btnMultithreaded = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/parallel.png" ) ) )
      _btnMultithreaded.ToolTipText = "Run in multiple threads"
      _btnMultithreaded.addActionListener( \e -> {_board.Model.Multithreaded = _btnMultithreaded.Selected} )
      _btnMultithreaded.PreferredSize = new( _btnMultithreaded.PreferredSize.height, _btnMultithreaded.PreferredSize.height )
      gc.insets = new( 0, 10, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _btnMultithreaded, gc )

    // Selection
    var buttonGroup = new ButtonGroup()
    _btnSelection = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/select.png" ) ) )
      _btnSelection.ToolTipText = "<html><b>Select</b> patterns, use the clipboard"
      buttonGroup.add( _btnSelection )
      _btnSelection.addActionListener( \e -> enableTools() )
      _btnSelection.PreferredSize = new( _btnSelection.PreferredSize.height, _btnSelection.PreferredSize.height )
      gc.insets = new( 0, 10, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _btnSelection, gc )
    
    // Draw
    _btnDraw = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/pencil.png" ) ) )
      _btnDraw.ToolTipText = "<html><b>Draw</b> on the board"
      buttonGroup.add( _btnDraw )
      _btnDraw.addActionListener( \e -> {enableTools()} )
      _btnDraw.PreferredSize = new( _btnDraw.PreferredSize.height, _btnDraw.PreferredSize.height )
      gc.insets = new( 0, 2, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _btnDraw, gc )
    
    // Scroll
    _btnScroll = new JToggleButton( new ImageIcon( Class.getResource( "/life/images/scroll.png" ) ) )
      _btnScroll.ToolTipText = "<html><b>Scroll</b> the board"
      buttonGroup.add( _btnScroll )
      _btnScroll.addActionListener( \e -> {enableTools()} )
      _btnScroll.PreferredSize = new( _btnScroll.PreferredSize.height, _btnScroll.PreferredSize.height )
      gc.insets = new (0, 2, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _btnScroll, gc )

    // Frequency
    var icon = new JLabel( new ImageIcon( Class.getResource( "/life/images/frequency.png" ) ) )
      icon.ToolTipText = "<html>Timer frequency (millis)"
      gc.gridx = x x++
      gc.insets = new( 0, 10, 0, 1 )
      _topBar.add( icon, gc )
    _frequency = new JComboBox( new Integer[] {1, 2, 3, 5, 8, 10, 15, 20, 25, 30, 35, 40, 45, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 750, 1000} )
      _frequency.ToolTipText = "<html>Timer frequency (millis)"
      _frequency.addActionListener( \e -> {resetTimer()} )
      gc.insets = new( 0, 0, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _frequency, gc )

    // Color Scheme
    icon = new JLabel( new ImageIcon( Class.getResource( "/life/images/color.png" ) ) )
      icon.ToolTipText = "<html>Color scheme"
      gc.gridx = x x++
      gc.insets = new( 0, 10, 0, 1 )
      _topBar.add( icon, gc )
    _colorScheme = new JComboBox( new ColorScheme[] {Manila, Reagan, Citrine} )
      _colorScheme.ToolTipText = "<html>Color scheme"
      _colorScheme.addActionListener( \e -> {_board.BoardColorScheme = _colorScheme.SelectedItem as ColorScheme} )
      gc.insets = new( 0, 0, 0, 0 )
      gc.gridx = x x++
      _topBar.add( _colorScheme, gc )

    // Padding
    var filler = new JPanel()
      gc.gridx = x x++
      gc.fill = GridBagConstraints.HORIZONTAL
      gc.gridwidth = GridBagConstraints.REMAINDER
      gc.insets = new( 0, 10, 0, 0 )
      gc.weightx = 1
      _topBar.add( filler, gc )

    _board.Model.addGenerationListener( \-> enableControls() )
  }
  
  private function makeLeftBar() {
    _leftBar = new JPanel()
    var layout = new GridBagLayout()
    _leftBar.Layout = layout
    var y = 0
    
    // CellSize
    var icon = new JLabel( new ImageIcon( Class.getResource( "/life/images/resolution.png" ) ) )
      icon.ToolTipText = "<html>Set the <b>cell size</b>"
      var gc = new GridBagConstraints()
      gc.gridy = y y++
      gc.gridheight = 1
      gc.insets = new( 10, 0, 0, 0 )
      _leftBar.add( icon, gc )
    _sliderCellSize = new JSlider( JSlider.VERTICAL, 1, 24, 4 )
      _sliderCellSize.addChangeListener( \e -> resetCellSize() )
      _sliderCellSize.PreferredSize = new( _sliderCellSize.PreferredSize.width, 80 )
      _board.Model.addCellSizeListener( \ size ->  {_sliderCellSize.Value = size} )
      gc.gridy = y y++
      gc.insets = new( 0, 0, 0, 0 )
      _leftBar.add( _sliderCellSize, gc )

    // Density
    _labelDensity = new JLabel( new ImageIcon( Class.getResource( "/life/images/density.png" ) ) )
      _labelDensity.ToolTipText = "<html>Set the initial number of <b>live</b> cells on the board"
      gc.gridy = y y++
      gc.insets = new ( 10, 0, 0, 0 )
      _leftBar.add( _labelDensity, gc )
      _sliderDensity = new JSlider( JSlider.VERTICAL, 1, 100, _board.Density )
      _sliderDensity.addChangeListener( \e -> reset() )
      _sliderDensity.PreferredSize = new( _sliderDensity.PreferredSize.width, 80 )
      gc.gridy = y y++
      gc.insets = new( 0, 0, 0, 0 )
      _leftBar.add( _sliderDensity, gc )

    // Tick Count
    var filler = new JLabel()
      gc.gridy = y y++
      gc.fill = GridBagConstraints.VERTICAL
      gc.gridheight = GridBagConstraints.REMAINDER
      gc.insets = new( 10, 0, 0, 0 )
      gc.weighty = 1
      _leftBar.add( filler, gc )

    _leftBar.PreferredSize = new( _btnStartStop.PreferredSize.width + 4, _leftBar.PreferredSize.height )
  }

  private function toggle() {
    if( _board.Running ) {
      _board.stop()
    }
    else {
      _board.start()
    }
  }

  private function enableControls() {
    _btnStartStop.Selected = _board.Running
    _sliderDensity.Enabled = _cbPopulator.SelectedItem == Populator.Random
    _labelDensity.Enabled = _sliderDensity.Enabled
  }

  private function enableTools() {
    _board.enableSelection( _btnSelection.Selected )
    _board.enableDrawing( _btnDraw.Selected )
    _board.enableScrolling( _btnScroll.Selected )
  }

  private function reset() {
    _board.reset( new( _board.Width/_sliderCellSize.Value, _board.Height/_sliderCellSize.Value ), _btnInfinite.Selected, _btnMultithreaded.Selected, _cbPopulator.SelectedItem as Populator, _sliderCellSize.Value, _frequency.SelectedItem as int, _sliderDensity.Value, _board.Running )
    _board.Model.addGenerationListener( \-> enableControls() )
    _board.Model.addCellSizeListener( \ size ->  {_sliderCellSize.Value = size} )
    _frame.adjustClientArea()
  }

  function resetCellSize() {
    _board.postModelChange( \-> {
      var res = _sliderCellSize.Value
      var size = new Dimension( _board.Width / res, _board.Height / res )
      _board.Model.updateCellSize( res, size )
      EventQueue.invokeLater( \-> _board.repaintBuffer() )
    } )
  }

  private function resetPopulator() {
    var populator = _cbPopulator.SelectedItem as Populator
    var res = populator.CellSize
    if( res > 0 ) {
      res = maybeFinerIfNotFit( populator.LiveCells, res )
      _sliderCellSize.Value = res
    }
    reset()
  }

  private function resetTimer() {
    _board.resetTimer( _frequency.SelectedItem as int )
  }

  function maybeFinerIfNotFit( live: Collection<Point>, res: int ) : int {
    while( res > 1 ) {
      var maxX = live.maxBy( \ pt -> pt.x )
      var maxY = live.maxBy( \ pt -> pt.y )
      if( maxX.X >= _board.XCells - _board.XCells/3 ||
          maxY.Y >= _board.YCells - _board.YCells/3 ) {
        res--
      }
      else {
        break
      }
    }
    return res
  }
}